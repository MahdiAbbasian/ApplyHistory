package dev.abbasian.applyhistory.ui.company

import androidx.lifecycle.*
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import dev.abbasian.applyhistory.domain.repo.CompanyRepository
import kotlinx.coroutines.launch
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

class CompanyViewModel(private val repository: CompanyRepository) : ViewModel() {

    companion object {
        private const val AES_MODE = "AES/CBC/PKCS5PADDING"
        private const val CHARSET_NAME = "UTF-8"
        private const val SECRET_KEY = "your-secret-key-" // Ensure this is 16, 24, or 32 bytes for AES
        private const val IV_STRING = "1234567890123456" // Corrected to exactly 16 bytes
    }

    val companiesCount: LiveData<Int> = repository.getCompaniesCount().asLiveData()
    val companiesList: LiveData<List<CompanyEntity>> = repository.getCompanies().asLiveData()
    private val _company = MutableLiveData<CompanyEntity?>()
    val company: LiveData<CompanyEntity?> = _company
    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _filteredCompaniesList = MediatorLiveData<List<CompanyEntity>>()
    val filteredCompaniesList: LiveData<List<CompanyEntity>> = _filteredCompaniesList

    private fun encrypt(value: String): String {
        val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(charset(CHARSET_NAME)), "AES")
        val iv = IvParameterSpec(IV_STRING.toByteArray(charset(CHARSET_NAME)))
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv)
        val encrypted = cipher.doFinal(value.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    private fun decrypt(value: String): String {
        val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(charset(CHARSET_NAME)), "AES")
        val iv = IvParameterSpec(IV_STRING.toByteArray(charset(CHARSET_NAME)))
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(value))
        return String(decryptedBytes)
    }

    fun exportDataToFile(context: Context, onCompletion: (Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val companies = repository.getCompanies().first()
            val jsonData = Gson().toJson(companies)
            val encryptedData = encrypt(jsonData)
            withContext(Dispatchers.Main) {
                context.openFileOutput("exported_companies.txt", Context.MODE_PRIVATE).use {
                    it.write(encryptedData.toByteArray())
                }
                onCompletion(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onCompletion(false)
            }
        }
    }

    fun importDataFromFile(context: Context, onCompletion: (Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val encryptedData = withContext(Dispatchers.Main) {
                    context.openFileInput("exported_companies.txt").bufferedReader()
                        .use { it.readText() }
                }
                val decryptedData = decrypt(encryptedData)
                val type = object : TypeToken<List<CompanyEntity>>() {}.type
                val importedCompanies: List<CompanyEntity> = Gson().fromJson(decryptedData, type)
                importedCompanies.forEach { company ->
                    repository.addCompany(company)
                }
                onCompletion(true)
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onCompletion(false)
                }
            }
        }

    init {
        _filteredCompaniesList.addSource(companiesList) { companies ->
            filterCompanies(companies, _searchQuery.value ?: "")
        }

        _filteredCompaniesList.addSource(_searchQuery) { query ->
            filterCompanies(companiesList.value ?: emptyList(), query)
        }
    }

    private fun filterCompanies(companies: List<CompanyEntity>, query: String) {
        val filteredList = if (query.isEmpty()) {
            companies
        } else {
            companies.filter { it.companyName.contains(query, ignoreCase = true) }
        }
        _filteredCompaniesList.value = filteredList
    }

    fun addCompany(company: CompanyEntity) = viewModelScope.launch {
        repository.addCompany(company)
    }

    fun updateCompany(id: Int, description: String?, companyName: String?, companyWeb: String?, lastUpdateDate: String?, applyStatus: Int?) = viewModelScope.launch {
        repository.updateCompany(id, description, companyName, companyWeb, lastUpdateDate, applyStatus)
    }

    fun deleteCompany(id: Int) = viewModelScope.launch {
        repository.deleteCompany(id)
    }

    fun getCompany(id: Int) = viewModelScope.launch {
        _company.value = repository.getCompany(id)
    }

    fun resetCompany() {
        _company.value = null
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
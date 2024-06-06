package dev.abbasian.applyhistory.ui.company

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import dev.abbasian.applyhistory.domain.repo.CompanyRepository
import dev.abbasian.applyhistory.ui.company.edit.EditCompanyViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CompanyViewModel(private val repository: CompanyRepository) : ViewModel() {

    companion object {
        private const val AES_MODE = "AES/CBC/PKCS5PADDING"
        private const val CHARSET_NAME = "UTF-8"
        private const val SECRET_KEY = "your-secret-key-"
        private const val IV_STRING = "1234567890123456"
    }

    private var debounceJob: Job? = null

    private val companiesList: LiveData<List<CompanyEntity>> = repository.getCompanies().asLiveData()
    private val _company = MutableLiveData<CompanyEntity?>()
    val company: LiveData<CompanyEntity?> = _company

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _filteredCompaniesList = MediatorLiveData<List<CompanyEntity>>()
    val filteredCompaniesList: LiveData<List<CompanyEntity>> = _filteredCompaniesList

    private val _companyViewState = MutableLiveData<EditCompanyViewState>()
    val companyViewState: LiveData<EditCompanyViewState> = _companyViewState

    init {
        _filteredCompaniesList.addSource(companiesList) { companies ->
            filterCompanies(companies, _searchQuery.value ?: "")
        }

        _filteredCompaniesList.addSource(_searchQuery) { query ->
            filterCompanies(companiesList.value ?: emptyList(), query)
        }
        loadCompanies()
    }
    private fun debounceFilter(query: String) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(300L) // 300 milliseconds debounce time
            filterCompanies(companiesList.value ?: emptyList(), query)
        }
    }

    fun onEvent(event: CompanyViewEvent) {
        when (event) {
            is CompanyViewEvent.LoadCompanies -> loadCompanies()
            is CompanyViewEvent.SearchCompany -> updateSearchQuery(event.query)
            is CompanyViewEvent.ExportData -> exportDataToFile(event.context, event.onCompletion)
            is CompanyViewEvent.ImportData -> importDataFromFile(event.context, event.uri, event.onCompletion)
            is CompanyViewEvent.GetCompanyDetail -> getCompany(event.companyId)
            is CompanyViewEvent.DeleteCompany -> deleteCompany(event.companyId)
            is CompanyViewEvent.AddCompany -> addCompany(event.company)
            is CompanyViewEvent.UpdateCompany -> updateCompany(
                event.id,
                event.companyName,
                event.companyWebsite,
                event.description,
                event.applyStatus
            )
        }
    }

    private fun loadCompanies() {
        viewModelScope.launch {
            val companies = repository.getCompanies().first()
            _filteredCompaniesList.value = companies
        }
    }

    private fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        debounceFilter(query)
    }

    private fun filterCompanies(companies: List<CompanyEntity>, query: String) {
        val filteredList = if (query.isEmpty()) {
            companies
        } else {
            companies.filter { it.companyName.contains(query, ignoreCase = true) }
        }
        _filteredCompaniesList.value = filteredList
    }

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

    private fun exportDataToFile(context: Context, onCompletion: (Boolean, String) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val companies = repository.getCompanies().first()
                if (companies.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        onCompletion(false, "No data to export")
                    }
                    return@launch
                }
                val jsonData = Gson().toJson(companies)
                val encryptedData = encrypt(jsonData)

                val fileName = "exported_companies.txt"
                val internalStorageDir = File(context.filesDir, "android/data/dev.abbasian.applyhistory")
                if (!internalStorageDir.exists()) {
                    internalStorageDir.mkdirs()
                }
                val file = File(internalStorageDir, fileName)
                file.writeText(encryptedData)

                withContext(Dispatchers.Main) {
                    onCompletion(true, "File saved successfully at: ${file.absolutePath}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onCompletion(false, "Error exporting data")
                }
            }
        }

    private fun importDataFromFile(context: Context, uri: Uri, onCompletion: (Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val data = reader.readLines().joinToString("\n")
                reader.close()
                inputStream?.close()

                val decryptedData = decrypt(data)

                val type = object : TypeToken<List<CompanyEntity>>() {}.type
                val importedCompanies: List<CompanyEntity> = Gson().fromJson(decryptedData, type)

                importedCompanies.forEach { company ->
                    repository.addCompany(company)
                }

                withContext(Dispatchers.Main) {
                    onCompletion(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onCompletion(false)
                }
            }
        }

    private fun getCompany(id: Int) = viewModelScope.launch {
        _company.value = repository.getCompany(id)
    }

    private fun deleteCompany(id: Int) = viewModelScope.launch {
        repository.deleteCompany(id)
    }

    private fun addCompany(company: CompanyEntity) = viewModelScope.launch {
        repository.addCompany(company)
        loadCompanies()
    }

    private fun updateCompany(
        id: Int,
        companyName: String?,
        companyWebsite: String?,
        description: String?,
        applyStatus: Int?
    ) = viewModelScope.launch {
        repository.updateCompany(
            id,
            description,
            companyName,
            companyWebsite,
            null,
            applyStatus
        )
        loadCompanies()
    }

    fun resetCompany() {
        _company.value = null
    }
}
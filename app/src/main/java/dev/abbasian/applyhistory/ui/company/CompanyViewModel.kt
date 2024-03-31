package dev.abbasian.applyhistory.ui.company

import androidx.lifecycle.*
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import dev.abbasian.applyhistory.domain.repo.CompanyRepository
import kotlinx.coroutines.launch

class CompanyViewModel(private val repository: CompanyRepository) : ViewModel() {

    val companiesCount: LiveData<Int> = repository.getCompaniesCount().asLiveData()
    val companiesList: LiveData<List<CompanyEntity>> = repository.getCompanies().asLiveData()
    private val _company = MutableLiveData<CompanyEntity?>()
    val company: LiveData<CompanyEntity?> = _company
    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _filteredCompaniesList = MediatorLiveData<List<CompanyEntity>>()
    val filteredCompaniesList: LiveData<List<CompanyEntity>> = _filteredCompaniesList

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
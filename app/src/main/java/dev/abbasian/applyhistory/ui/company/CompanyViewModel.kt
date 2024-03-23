package dev.abbasian.applyhistory.ui.company

import androidx.lifecycle.*
import dev.abbasian.applyhistory.domain.model.Company
import dev.abbasian.applyhistory.domain.repo.CompanyRepository
import kotlinx.coroutines.launch

class CompanyViewModel(private val repository: CompanyRepository) : ViewModel() {

    val companiesCount: LiveData<Int> = repository.getCompaniesCount().asLiveData()
    val companiesList: LiveData<List<Company>> = repository.getCompanies().asLiveData()
    private val _company = MutableLiveData<Company?>()
    val company: LiveData<Company?> = _company

    fun addCompany(company: Company) = viewModelScope.launch {
        repository.addCompany(company)
    }

    fun updateCompany(company: Company) = viewModelScope.launch {
        repository.updateCompany(company)
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

    fun updateSearchQuery(searchQuery: String) {
        TODO()
    }
}
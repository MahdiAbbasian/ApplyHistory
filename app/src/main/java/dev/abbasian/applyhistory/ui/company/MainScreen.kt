package dev.abbasian.applyhistory.ui.company

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.abbasian.applyhistory.domain.model.CompanyEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: CompanyViewModel) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addCompany") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Company")
            }
        },
        topBar = {
            TopAppBar(title = { Text("ApplyHistory") })
        }
    ) { innerPadding ->
        // Apply the innerPadding to the Column
        Column(modifier = Modifier.padding(innerPadding).padding(8.dp)) {
            val searchQuery by viewModel.searchQuery.observeAsState("")
            val filteredCompanyList by viewModel.filteredCompaniesList.observeAsState(emptyList())

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Total Applications: ${filteredCompanyList.size}", style = MaterialTheme.typography.bodyLarge)

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredCompanyList) { company ->
                    // Ensure there's only one implementation of CompanyItem or it's correctly renamed
                    CompanyItems(company = company, onClick = {
                        // This is a click listener for each company item
                        navController.navigate("companyDetail/${company.id}")
                    })
                }
            }
        }
    }
}

// Ensure this is the only CompanyItem composable across your project, or rename if necessary
@Composable
fun CompanyItems(company: CompanyEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = company.companyName, style = MaterialTheme.typography.bodyLarge)
            Text(text = company.companyWebSite ?: "", style = MaterialTheme.typography.bodyMedium)
            Text(text = company.description ?: "", style = MaterialTheme.typography.bodySmall)
            Text(text = company.applyStatus.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}
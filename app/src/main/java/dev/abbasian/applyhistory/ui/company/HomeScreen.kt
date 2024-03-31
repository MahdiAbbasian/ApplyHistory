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
import dev.abbasian.applyhistory.Route
import dev.abbasian.applyhistory.domain.model.CompanyEntity

@Composable
fun HomeScreen(navController: NavController, viewModel: CompanyViewModel) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Route.AddCompanyScreen.route)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "add_edit_company_screen")
            }
        },
    ) { innerPadding ->

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
                    CompanyItems(company = company, onClick = {
                        val route = Route.CompanyDetailScreen.createRoute(company.id)
                        navController.navigate(route)
                    })
                }
            }
        }
    }
}

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
            Text(text = company.companyWebSite, style = MaterialTheme.typography.bodyMedium)
            Text(text = company.description, style = MaterialTheme.typography.bodySmall)
            Text(text = company.applyStatus.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}
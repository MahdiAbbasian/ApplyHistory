package dev.abbasian.applyhistory.ui.company

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.abbasian.applyhistory.Route

@Composable
fun CompanyDetailScreen(navController: NavController, viewModel: CompanyViewModel, companyId: Int) {

    LaunchedEffect(key1 = companyId) {
        viewModel.getCompany(companyId)
    }

    val company by viewModel.company.observeAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {

            company?.let { comp ->
                Column(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                ) {
                    TextField(
                        value = comp.companyName,
                        onValueChange = {},
                        label = { Text("Company Name") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = comp.companyWebSite ?: "",
                        onValueChange = {},
                        label = { Text("Company Website") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = comp.description ?: "",
                        onValueChange = {},
                        label = { Text("Description") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = comp.applyStatus?.let { ApplyStatus.fromInt(it).name } ?: "",
                        onValueChange = {},
                        label = { Text("Apply Status") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            viewModel.deleteCompany(comp.id)
                            navController.navigate(Route.HomeScreen.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text("Delete Company", color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val route = Route.EditCompanyScreen.createRoute(companyId = comp.id)
                            navController.navigate(route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(16.dp)
                    ) {
                        Text("Edit Company")
                    }
                }
            } ?: Text(
                "Company not found",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}
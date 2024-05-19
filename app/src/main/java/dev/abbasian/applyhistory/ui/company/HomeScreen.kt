package dev.abbasian.applyhistory.ui.company

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.abbasian.applyhistory.Route
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, viewModel: CompanyViewModel) {

    val context: Context = LocalContext.current
    //val scaffoldState = rememberScaffoldState()
    val filePickerLauncher = filePickerLauncher(viewModel)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Route.AddCompanyScreen.route)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "add_edit_company_screen")
            }
        },
    ) { innerPadding ->

        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp)) {
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

            Button(
                onClick = {
                    viewModel.exportDataToFile(context) { success ->
                        if (success) {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Export to file")
            }

            Button(
                onClick = {
                    filePickerLauncher.launch("text/plain")
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Import from file")
            }

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
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ApplyStatusChip(applyStatus = ApplyStatus.fromInt(company.applyStatus ?: 0))
                Text(
                    text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date()),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = company.companyName, style = MaterialTheme.typography.bodyLarge)
                Text(text = company.companyWebSite, style = MaterialTheme.typography.bodyMedium)
                Text(text = company.description, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun ApplyStatusChip(applyStatus: ApplyStatus) {
    Surface(
        color = applyStatus.toColor(),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
    ) {
        Text(
            text = applyStatus.toApplyStatusString(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun filePickerLauncher(viewModel: CompanyViewModel): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                viewModel.importDataFromFile(context, uri) { success ->
                    if (success) {
                        Toast.makeText(context, "Import Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Import Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    return filePickerLauncher
}
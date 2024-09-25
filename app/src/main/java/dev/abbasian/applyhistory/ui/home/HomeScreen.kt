package dev.abbasian.applyhistory.ui.home

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.abbasian.applyhistory.Route
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import dev.abbasian.applyhistory.ui.company.CompanyViewEvent
import dev.abbasian.applyhistory.ui.company.CompanyViewModel
import dev.abbasian.applyhistory.ui.company.edit.ApplyStatus
import dev.abbasian.applyhistory.ui.company.edit.toApplyStatusString
import dev.abbasian.applyhistory.ui.component.CustomTextField
import dev.abbasian.applyhistory.ui.theme.AppString
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: CompanyViewModel,
) {
    val context: Context = LocalContext.current
    val filePickerLauncher = filePickerLauncher(viewModel)
    val isScanning by viewModel.isScanning.observeAsState(false)
    val foundFiles by viewModel.foundFiles.observeAsState(emptyList())
    var showNoFileFoundMessage by remember { mutableStateOf(false) }
    val importStatus by viewModel.importStatus.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.scanForExportedFile(context)
    }

    LaunchedEffect(foundFiles, isScanning) {
        showNoFileFoundMessage = foundFiles.isEmpty() && !isScanning
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Route.AddCompanyScreen)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "add_edit_company_screen")
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                val searchQuery by viewModel.searchQuery.observeAsState("")
                val filteredCompanyList by viewModel.filteredCompaniesList.observeAsState(emptyList())

                CustomTextField(
                    text = searchQuery,
                    onValueChange = { viewModel.onEvent(CompanyViewEvent.SearchCompany(it)) },
                    placeholder = AppString.SEARCH,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                    modifier = Modifier.padding(8.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = AppString.TOTAL_APPLICATIONS,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${filteredCompanyList.size}",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.onEvent(
                            CompanyViewEvent.ExportData(context) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            },
                        )
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                ) {
                    Text(AppString.EXPORT_TO_FILE)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        filePickerLauncher.launch("text/plain")
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                ) {
                    Text(AppString.IMPORT_FROM_FILE)
                }

                when {
                    isScanning -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                    }

                    importStatus == CompanyViewModel.ImportStatus.SUCCESS -> {
                        Text(
                            text = AppString.IMPORT_FILE_SUCCESS,
                            textAlign = TextAlign.Center,
                            modifier =
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    importStatus == CompanyViewModel.ImportStatus.NO_FILE -> {
                        Text(
                            text = AppString.EXPORTED_FILE_NOT_FOUND,
                            textAlign = TextAlign.Center,
                            modifier =
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    importStatus == CompanyViewModel.ImportStatus.NONE -> {
                        // TODO
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                CompanyListView(
                    filteredCompanyList = filteredCompanyList,
                    onCompanyClick = { companyId ->
                        navController.navigate(Route.companyDetailScreen(companyId))
                    },
                    onDeleteCompany = { companyId ->
                        viewModel.deleteCompany(companyId)
                    },
                )
            }
        }
    }
}

@Composable
fun CompanyListView(
    filteredCompanyList: List<CompanyEntity>,
    onCompanyClick: (Int) -> Unit,
    onDeleteCompany: (Int) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(filteredCompanyList) { company ->
            CompanyItems(company = company, onClick = {
                onCompanyClick(company.id)
            }, onDeleteClick = {
                onDeleteCompany(company.id)
            })
        }
    }
}

@Composable
fun CompanyItems(
    company: CompanyEntity,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ApplyStatusChip(applyStatus = ApplyStatus.fromInt(company.applyStatus ?: 0))
                Text(
                    text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date()),
                    style = MaterialTheme.typography.bodySmall,
                )
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Company")
                }
            }
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
        modifier = Modifier,
    ) {
        Text(
            text = applyStatus.toApplyStatusString(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun filePickerLauncher(viewModel: CompanyViewModel): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current

    val filePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri: Uri? ->
                if (uri != null) {
                    viewModel.onEvent(
                        CompanyViewEvent.ImportData(context, uri) { success ->
                            if (success) {
                                Toast
                                    .makeText(context, AppString.IMPORT_FILE_SUCCESS, Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast
                                    .makeText(context, AppString.IMPORT_FILE_FAILED, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                    )
                }
            },
        )

    return filePickerLauncher
}

@Composable
fun FileSelectionDialog(
    files: List<File>,
    onDismiss: () -> Unit,
    onFileSelected: (File) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = AppString.SELECTED_FILE_TO_IMPORT) },
        text = {
            LazyColumn {
                items(files) { file ->
                    Text(
                        text = file.name,
                        modifier =
                            Modifier
                            .fillMaxWidth()
                            .clickable {
                                onFileSelected(file)
                                onDismiss()
                            }
                            .padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(AppString.CANCEL)
            }
        }
    )
}

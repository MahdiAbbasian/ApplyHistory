package dev.abbasian.applyhistory.ui.company.detail

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.abbasian.applyhistory.Route
import dev.abbasian.applyhistory.ui.company.CompanyViewEvent
import dev.abbasian.applyhistory.ui.company.CompanyViewModel
import dev.abbasian.applyhistory.ui.company.edit.ApplyStatus
import dev.abbasian.applyhistory.ui.component.CustomTextField
import dev.abbasian.applyhistory.ui.theme.AppString

@Composable
fun CompanyDetailScreen(navController: NavController, viewModel: CompanyViewModel, companyId: Int) {

    LaunchedEffect(key1 = companyId) {
        viewModel.onEvent(CompanyViewEvent.GetCompanyDetail(companyId))
    }

    val company by viewModel.company.observeAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {

            company?.let { comp ->
                Column(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                ) {
                    CustomTextField(
                        text = comp.companyName,
                        onValueChange = {},
                        placeholder = AppString.COMPANY_NAME,
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        text = comp.companyWebSite,
                        onValueChange = {},
                        placeholder = AppString.COMPANY_WEBSITE,
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        text = comp.description,
                        onValueChange = {},
                        placeholder = AppString.DESCRIPTION,
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        text = comp.applyStatus?.let { ApplyStatus.fromInt(it).name } ?: "",
                        onValueChange = {},
                        placeholder = AppString.APPLY_STATUS,
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            viewModel.onEvent(CompanyViewEvent.DeleteCompany(comp.id))
                            navController.navigate(Route.HomeScreen)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(AppString.DELETE_COMPANY, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            navController.navigate(Route.editCompanyScreen(comp.id))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(16.dp)
                    ) {
                        Text(AppString.EDIT_COMPANY)
                    }
                }
            } ?: Text(
                AppString.COMPANY_NOT_FOUND,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}
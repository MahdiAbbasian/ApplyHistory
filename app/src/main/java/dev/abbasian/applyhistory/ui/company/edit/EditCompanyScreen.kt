package dev.abbasian.applyhistory.ui.company.edit

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.abbasian.applyhistory.Route
import dev.abbasian.applyhistory.business.usecase.company.companyWebSiteValidatorUseCase
import dev.abbasian.applyhistory.business.usecase.company.emptyFieldValidatorUseCase
import dev.abbasian.applyhistory.core.extension.UiText
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import dev.abbasian.applyhistory.ui.company.CompanyViewEvent
import dev.abbasian.applyhistory.ui.company.CompanyViewModel
import dev.abbasian.applyhistory.ui.component.CustomTextField
import dev.abbasian.applyhistory.ui.theme.AppString
import dev.abbasian.applyhistory.ui.theme.AppString.statusOptions
import java.time.LocalDate

@Composable
fun EditCompanyScreen(
    navController: NavController,
    viewModel: CompanyViewModel,
    company: CompanyEntity? = null
) {

    var companyName by rememberSaveable { mutableStateOf(company?.companyName ?: "") }
    var companyWebsite by rememberSaveable { mutableStateOf(company?.companyWebSite ?: "") }
    var description by rememberSaveable { mutableStateOf(company?.description ?: "") }
    var applyStatus by rememberSaveable {
        mutableStateOf(company?.applyStatus?.let {
            ApplyStatus.fromInt(it)
        } ?: ApplyStatus.NONE)
    }

    var isCompanyWebsiteValid by remember { mutableStateOf(true) }
    var companyWebsiteError by remember { mutableStateOf<UiText?>(null) }

    var isCompanyNameValid by remember { mutableStateOf(true) }
    var companyNameError by remember { mutableStateOf<UiText?>(null) }

    val isEditMode = company != null

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
            ) {
                CustomTextField(
                    placeholder = AppString.COMPANY_NAME,
                    text = companyName,
                    onValueChange = {
                        companyName = it
                        val result = emptyFieldValidatorUseCase(it)
                        isCompanyNameValid = result.success
                        companyNameError = if (result.success) null else result.failure
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isCompanyNameValid
                )
                if (!isCompanyNameValid) {
                    Text(
                        text = companyNameError?.asString() ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    placeholder = AppString.COMPANY_WEBSITE,
                    text = companyWebsite,
                    onValueChange = {
                        companyWebsite = it
                        val result = companyWebSiteValidatorUseCase(it)
                        isCompanyWebsiteValid = result.success
                        companyWebsiteError = if (result.success) null else result.failure
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isCompanyWebsiteValid
                )
                if (!isCompanyWebsiteValid) {
                    Text(
                        text = companyWebsiteError?.asString() ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    placeholder = AppString.DESCRIPTION,
                    text = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                DropdownField(
                    selectedStatus = applyStatus,
                    onStatusSelected = { status ->
                        applyStatus = status
                    },
                    isError = false
                )
            }

            Button(
                onClick = {
                    var isValid = true

                    val companyNameResult = emptyFieldValidatorUseCase(companyName)
                    isCompanyNameValid = companyNameResult.success
                    companyNameError = if (companyNameResult.success) null else companyNameResult.failure
                    if (!companyNameResult.success) isValid = false

                    val companyWebsiteResult = companyWebSiteValidatorUseCase(companyWebsite)
                    isCompanyWebsiteValid = companyWebsiteResult.success
                    companyWebsiteError = if (companyWebsiteResult.success) null else companyWebsiteResult.failure
                    if (!companyWebsiteResult.success) isValid = false

                    if (isValid) {
                        if (isEditMode) {
                            viewModel.onEvent(
                                CompanyViewEvent.UpdateCompany(
                                    company!!.id, // !! is safe here because isEditMode is true
                                    description,
                                    companyName,
                                    companyWebsite,
                                    LocalDate.now().toString(),
                                    applyStatus.status
                                )
                            )
                        } else {
                            viewModel.onEvent(
                                CompanyViewEvent.AddCompany(
                                    CompanyEntity(
                                        id = 0,
                                        companyName = companyName,
                                        companyWebSite = companyWebsite,
                                        description = description,
                                        lastUpdateDate = LocalDate.now().toString(),
                                        applyStatus = applyStatus.status
                                    )
                                )
                            )
                        }
                        navController.previousBackStackEntry?.savedStateHandle?.set("refreshCompanies", true)
                        navController.navigate(Route.HomeScreen) {
                            popUpTo(Route.EditCompany()) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp),
                enabled = true
            ) {
                Text(if (isEditMode) AppString.UPDATE_DATA else AppString.ADD_COMPANY)
            }
        }
    }
}

@Composable
fun DropdownField(
    selectedStatus: ApplyStatus,
    onStatusSelected: (ApplyStatus) -> Unit,
    isError: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val applyStatusOptions = ApplyStatus.values()
    val colorBorder =
        if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary.copy(
            alpha = 0.3f
        )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = colorBorder)
    ) {
        val boxWidth = constraints.maxWidth

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = true
                    focusManager.clearFocus()
                }
        ) {
            Text(
                text = selectedStatus.toApplyStatusString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(LocalDensity.current) { boxWidth.toDp() })
            ) {
                applyStatusOptions.forEach { status ->
                    DropdownMenuItem(
                        text = {
                            Text(text = status.toApplyStatusString(), color = Color.LightGray, modifier = Modifier.padding(16.dp))
                        },
                        onClick = {
                            onStatusSelected(status)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

enum class ApplyStatus(val status: Int) {
    NONE(0),
    APPLIED(1),
    REJECTED(2),
    INTERVIEW(3),
    ACCEPTED(4);

    companion object {
        fun fromInt(value: Int) = ApplyStatus.values().first { it.status == value }
    }

    fun toColor(): Color {
        return when (this) {
            NONE -> Color.Gray
            APPLIED -> Color.Green
            REJECTED -> Color.Red
            INTERVIEW -> Color.LightGray
            ACCEPTED -> Color(0xFF4CAF50)
        }
    }
}

fun ApplyStatus.toApplyStatusString(): String {

    for ((index, value) in statusOptions.withIndex()) {
        return when (this) {
            ApplyStatus.NONE -> value.takeIf { index == 0 }
            ApplyStatus.APPLIED -> value.takeIf { index == 1 }
            ApplyStatus.REJECTED -> value.takeIf { index == 2 }
            ApplyStatus.INTERVIEW -> value.takeIf { index == 3 }
            ApplyStatus.ACCEPTED -> value.takeIf { index == 4 }
        } ?: continue
    }
    return "Unknown Status"
}
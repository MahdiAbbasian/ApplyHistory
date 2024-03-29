package dev.abbasian.applyhistory.ui.company

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import java.time.LocalDate

@Composable
fun AddCompanyScreen(navController: NavController, viewModel: CompanyViewModel) {
    var companyName by rememberSaveable { mutableStateOf("") }
    var companyWebsite by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var applyStatus by rememberSaveable { mutableStateOf(ApplyStatus.NONE) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.TopStart)
            ) {
                TextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = companyWebsite,
                    onValueChange = { companyWebsite = it },
                    label = { Text("Company Website") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                DropdownField(applyStatus) { status ->
                    applyStatus = status
                }
            }

            Button(
                onClick = {
                    viewModel.addCompany(
                        CompanyEntity(
                            id = 0,
                            companyName = companyName,
                            companyWebSite = companyWebsite,
                            description = description,
                            lastUpdateDate = LocalDate.now().toString(),
                            applyStatus = applyStatus.ordinal
                        )
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp)
            ) {
                Text("Add Company")
            }
        }
    }
}

@Composable
fun DropdownField(selectedStatus: ApplyStatus, onStatusSelected: (ApplyStatus) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val applyStatusOptions = ApplyStatus.values()
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text(
            text = selectedStatus.toApplyStatusString(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            applyStatusOptions.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(text = status.toApplyStatusString())
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

enum class ApplyStatus(val status: Int) {
    NONE(0),
    APPLIED(1),
    REJECTED(2),
    INTERVIEW(3),
    ACCEPTED(4);

    companion object {
        fun fromInt(value: Int) = ApplyStatus.values().first { it.status == value }
    }
}

fun ApplyStatus.toApplyStatusString(): String {
    return when (this) {
        ApplyStatus.NONE -> "None"
        ApplyStatus.APPLIED -> "Applied"
        ApplyStatus.REJECTED -> "Rejected"
        ApplyStatus.INTERVIEW -> "Interview"
        ApplyStatus.ACCEPTED -> "Accepted"
    }
}
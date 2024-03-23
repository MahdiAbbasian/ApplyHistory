package dev.abbasian.applyhistory.ui.company

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.abbasian.applyhistory.R
import dev.abbasian.applyhistory.domain.model.Company

@Composable
fun AddOrUpdateCompanyScreen(
    item: Company,
    mode: Int, // 0 for add, 1 for update
    onAddOrUpdateClicked: (Company) -> Unit
) {
    val context = LocalContext.current
    val buttonText = if (mode == 0) context.getString(R.string.add_company) else context.getString(R.string.update_data)

    var companyName by remember { mutableStateOf(item.companyName) }
    var companyWebsite by remember { mutableStateOf(item.companyWebSite) }
    var description by remember { mutableStateOf(item.description) }
    val statusOptions = listOf("Applied", "Rejected", "Interview", "Accepted")
    var selectedStatus by remember { mutableStateOf(item.applyStatus ?: 0) }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextFieldWithLabel(
            value = companyName,
            onValueChange = { companyName = it },
            label = context.getString(R.string.company_name),
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldWithLabel(
            value = companyWebsite,
            onValueChange = { companyWebsite = it },
            label = context.getString(R.string.company_website),
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldWithLabel(
            value = description,
            onValueChange = { description = it },
            label = context.getString(R.string.description),
            modifier = Modifier.height(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        StatusDropdownMenu(
            statusOptions = statusOptions,
            selectedStatus = selectedStatus,
            expanded = expanded,
            onExpandedChange = { expanded = it },
            onStatusSelected = { index -> selectedStatus = index }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onAddOrUpdateClicked(
                    Company(item.id, companyName, companyWebsite, item.lastUpdateDate, description, selectedStatus)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(buttonText)
        }
    }
}

@Composable
fun TextFieldWithLabel(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusDropdownMenu(
    statusOptions: List<String>,
    selectedStatus: Int,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onStatusSelected: (Int) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            readOnly = true,
            value = statusOptions[selectedStatus],
            onValueChange = {},
            label = { Text("Status") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            statusOptions.forEachIndexed { index, status ->
                DropdownMenuItem(
                    text = { Text(status) },
                    onClick = {
                        onStatusSelected(index)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}
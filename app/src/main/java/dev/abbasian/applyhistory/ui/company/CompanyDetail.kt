package dev.abbasian.applyhistory.ui.company

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.abbasian.applyhistory.domain.model.Company
import dev.abbasian.applyhistory.ui.theme.AppStrings

@Composable
fun CompanyDetailScreen(
    item: Company,
    onDeleteClicked: () -> Unit,
    onUpdateClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(16.dp))
        DetailItem(title = AppStrings.companyName, detail = item.companyName)
        DetailItem(title = AppStrings.companyWebsite, detail = item.companyWebSite)
        DetailItem(title = AppStrings.description, detail = item.description, isDescription = true)
        DetailItem(title = AppStrings.applyStatus, detail = getStatusName(item.applyStatus))

        Spacer(Modifier.weight(1f))
        Button(
            onClick = onDeleteClicked,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(AppStrings.deleteCompany)
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onUpdateClicked,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(AppStrings.editCompany)
        }
    }
}

@Composable
fun DetailItem(title: String, detail: String?, isDescription: Boolean = false) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp)
    )
    Surface(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(if (isDescription) 100.dp else 50.dp),
        color = Color.LightGray,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = detail ?: "",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}

fun getStatusName(statusCode: Int?): String {
    return when (statusCode) {
        1 -> "Applied"
        2 -> "Rejected"
        3 -> "Interview"
        4 -> "Accepted"
        else -> "Unknown"
    }
}
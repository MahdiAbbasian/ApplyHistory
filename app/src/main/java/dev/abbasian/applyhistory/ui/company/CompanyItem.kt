package dev.abbasian.applyhistory.ui.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.abbasian.applyhistory.R
import dev.abbasian.applyhistory.domain.model.Company
import dev.abbasian.applyhistory.ui.theme.*

@Composable
fun CompanyItemView(item: Company) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { /* Handle click here */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val (statusName, statusColor) = getStatusNameAndColor(item.applyStatus)
                Text(
                    text = statusName,
                    color = statusColor,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(LightGray4)
                        .padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Calendar icon",
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = item.lastUpdateDate,
                    color = Black2A,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            CustomDivider(color = Divider, thickness = 1.dp)
            Text(
                text = item.companyName,
                color = Black2A,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = "Website: ${item.companyWebSite}",
                color = Black2A,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Description: ${item.description}",
                color = Black2A,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

@Composable
fun CustomDivider(color: Color, thickness: Dp) {
    Divider(
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness)
            .alpha(0.5f),
        thickness = thickness
    )
}

fun getStatusNameAndColor(status: Int?): Pair<String, Color> {
    val statusName = "Status Name"
    val statusColor = when (status) {
        1 -> AppliedColor
        2 -> RejectedColor
        3 -> InterviewColor
        4 -> AcceptedColor
        else -> LightGray3
    }
    return Pair(statusName, statusColor)
}
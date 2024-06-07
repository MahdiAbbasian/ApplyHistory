package dev.abbasian.applyhistory.ui.home

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import dev.abbasian.applyhistory.Route
import dev.abbasian.applyhistory.ui.company.CompanyViewEvent
import dev.abbasian.applyhistory.ui.company.CompanyViewModel
import dev.abbasian.applyhistory.ui.theme.AppString
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockNavController = mockk<NavController>(relaxed = true)
    private val mockViewModel = mockk<CompanyViewModel>(relaxed = true)

    @Test
    fun addButtonNavigatesToAddCompanyScreen() {
        composeTestRule.setContent {
            HomeScreen(navController = mockNavController, viewModel = mockViewModel)
        }

        composeTestRule
            .onNodeWithContentDescription("add_edit_company_screen")
            .performClick()

        verify { mockNavController.navigate(Route.AddCompanyScreen) }
    }

    @Test
    fun exportButtonTriggersExportData() {
        composeTestRule.setContent {
            HomeScreen(navController = mockNavController, viewModel = mockViewModel)
        }

        composeTestRule
            .onNodeWithText(AppString.EXPORT_TO_FILE)
            .performClick()

        verify { mockViewModel.onEvent(match { it is CompanyViewEvent.ExportData }) }
    }
}


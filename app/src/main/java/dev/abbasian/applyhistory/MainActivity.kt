package dev.abbasian.applyhistory

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.abbasian.applyhistory.ui.company.AddEditCompanyScreen
import dev.abbasian.applyhistory.ui.company.CompanyDetailScreen
import dev.abbasian.applyhistory.ui.company.CompanyViewModel
import dev.abbasian.applyhistory.ui.company.HomeScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.getViewModel

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: CompanyViewModel = getViewModel()
            NavHost(navController = navController, startDestination = Route.HomeScreen.route) {
                composable(route = Route.HomeScreen.route) {
                    HomeScreen(navController, viewModel)
                }
                composable(route = Route.AddCompanyScreen.route) {
                    AddEditCompanyScreen(navController, viewModel, null)
                }
                composable(
                    route = Route.EditCompanyScreen.route,
                    arguments = listOf(navArgument("companyId") {
                        type = NavType.IntType
                    })
                ) {
                    val companyId = it.arguments?.getInt("companyId") ?: -1

                    LaunchedEffect(companyId) {
                        if (companyId != -1) {
                            viewModel.getCompany(companyId)
                        }
                    }

                    val company by viewModel.company.observeAsState()

                    AddEditCompanyScreen(navController, viewModel, company)
                }
                composable(
                    route = Route.CompanyDetailScreen.route,
                    arguments = listOf(navArgument("companyId") { type = NavType.IntType })
                ) {
                    val companyId = it.arguments?.getInt("companyId")
                    companyId?.let {
                        CompanyDetailScreen(navController = navController, viewModel = viewModel, companyId = it)
                    }
                }
            }
        }
    }
}

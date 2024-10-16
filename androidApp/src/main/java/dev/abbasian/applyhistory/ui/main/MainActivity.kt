package dev.abbasian.applyhistory.ui.main

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
import dev.abbasian.applyhistory.Route
import dev.abbasian.applyhistory.ui.company.CompanyViewEvent
import dev.abbasian.applyhistory.ui.company.edit.EditCompanyScreen
import dev.abbasian.applyhistory.ui.company.detail.CompanyDetailScreen
import dev.abbasian.applyhistory.ui.company.CompanyViewModel
import dev.abbasian.applyhistory.ui.home.HomeScreen
import dev.abbasian.applyhistory.ui.theme.ApplyHistoryTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.getViewModel

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplyHistoryTheme {
                val navController = rememberNavController()
                val viewModel: CompanyViewModel = getViewModel()
                NavHost(navController = navController, startDestination = Route.HomeScreen) {
                    composable(route = Route.HomeScreen) {
                        HomeScreen(navController, viewModel)
                    }
                    composable(route = Route.AddCompanyScreen) {
                        EditCompanyScreen(navController, viewModel, null)
                    }
                    composable(
                        route = Route.EditCompany().route,
                        arguments = listOf(navArgument("companyId") {
                            type = NavType.IntType
                        })
                    ) {
                        val companyId = it.arguments?.getInt("companyId") ?: -1

                        LaunchedEffect(companyId) {
                            if (companyId != -1) {
                                viewModel.onEvent(CompanyViewEvent.GetCompanyDetail(companyId))
                            }
                        }

                        val company by viewModel.company.observeAsState()

                        EditCompanyScreen(navController, viewModel, company)
                    }
                    composable(
                        route = Route.CompanyDetail().route,
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
}

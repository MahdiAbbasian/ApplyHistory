package dev.abbasian.applyhistory

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.abbasian.applyhistory.domain.model.CompanyEntity
import dev.abbasian.applyhistory.ui.company.AddOrUpdateCompanyScreen
import dev.abbasian.applyhistory.ui.company.CompaniesListHomeScreen
import dev.abbasian.applyhistory.ui.company.CompanyDetailScreen
import dev.abbasian.applyhistory.ui.company.CompanyViewModel
import dev.abbasian.applyhistory.ui.company.MainScreen
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
            NavHost(navController = navController, startDestination = Route.Company.route) {
                composable(route = Route.Company.route) { navBackStackEntry ->
                    MainScreen(navController, viewModel)
                }
            }
        }
    }
}

package dev.abbasian.applyhistory

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.abbasian.applyhistory.ui.company.AddCompanyScreen
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
                    AddCompanyScreen(navController, viewModel)
                }
            }
        }
    }
}

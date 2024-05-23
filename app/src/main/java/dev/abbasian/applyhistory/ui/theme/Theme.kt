package dev.abbasian.applyhistory.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

enum class AppTheme {
    Light, Dark, Default
}


private val DarkColorScheme = darkColorScheme(
    primary = Purple200,
    secondary = Purple700,
    background = Black,
    error = red,
    surface = Black,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onError = White,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple200,
    secondary = Purple700,
    background = White,
    error = red,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = Black,
    onError = White,
    onSurface = Black
)

@Composable
fun ApplyHistoryTheme(
    appTheme: AppTheme = AppTheme.Light,
    isDarkMode: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.Default -> {
            if (isDarkMode) {
                DarkColorScheme
            } else {
                LightColorScheme
            }
        }

        AppTheme.Light -> {
            LightColorScheme
        }

        AppTheme.Dark -> {
            DarkColorScheme
        }
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = colorScheme.primary,
            darkIcons = false,
        )
    }
    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}
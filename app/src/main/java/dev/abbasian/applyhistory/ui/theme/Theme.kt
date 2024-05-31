package dev.abbasian.applyhistory.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import dev.abbasian.applyhistory.R

enum class AppTheme {
    Light, Dark, Default
}

private val DarkColorScheme = darkColorScheme(
    primary = Purple200,
    secondary = Purple700,
    background = Color.Transparent,
    error = RejectedColor,
    surface = Color.Transparent,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onError = White,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple200,
    secondary = Purple700,
    background = Color.Transparent,
    error = RejectedColor,
    surface = Color.Transparent,
    onPrimary = Black,
    onSecondary = Black,
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
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        appTheme == AppTheme.Default -> {
            if (isDarkMode) DarkColorScheme else LightColorScheme
        }

        appTheme == AppTheme.Light -> LightColorScheme
        appTheme == AppTheme.Dark -> DarkColorScheme
        else -> if (isDarkMode) DarkColorScheme else LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !isDarkMode
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SetBackground()
            content()
        }
    }
}

@Composable
fun SetBackground() {
    return Image(
        painter = painterResource(id = R.drawable.apply_history),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}
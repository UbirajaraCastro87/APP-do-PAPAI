package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val KidColorScheme = lightColorScheme(
    primary = PrimaryKid,
    secondary = SecondaryKid,
    tertiary = TertiaryKid,
    background = BackgroundKid,
    surface = SurfaceKid,
    onPrimary = OnPrimaryKid,
    onSecondary = OnSecondaryKid,
    onBackground = OnBackgroundKid,
    onSurface = OnSurfaceKid
)

// Para crianças, preferimos cores claras e alegres por padrão para manter o engajamento e a visibilidade
private val DarkKidColorScheme = darkColorScheme(
    primary = PrimaryKid,
    secondary = SecondaryKid,
    tertiary = TertiaryKid,
    background = CharcoalKids,
    surface = CharcoalKids,
    onPrimary = OnPrimaryKid,
    onSecondary = OnSecondaryKid,
    onBackground = CloudWhite,
    onSurface = CloudWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desativado por padrão para garantir cores da marca infantil
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkKidColorScheme else KidColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

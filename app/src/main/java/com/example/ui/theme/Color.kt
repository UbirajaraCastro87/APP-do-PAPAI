package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Bold Typography Color Palette
val PrimaryRed = Color(0xFFBA1A1A)
val DeepRedDark = Color(0xFF410001)
val SubtitleRed = Color(0xFF930006)
val SoftPinkBg = Color(0xFFF4DDDD)
val SoftPinkContainer = Color(0xFFFFDAD9)
val WatermelonRed = Color(0xFFFF5449)
val WatermelonGreen = Color(0xFF2B6B2B)
val WatermelonDashedLine = Color(0xFFBA1A1A).copy(alpha = 0.4f)
val SkyBlue = Color(0xFF54A0FF)
val SunnyYellow = Color(0xFFFFD32A)
val LavenderSoft = Color(0xFFDCDDE1)
val CloudWhite = Color(0xFFFFFAFA) // Rose white background
val CharcoalKids = Color(0xFF201A1A) // High contrast dark text

// Aliases for retrofitting code references to the Bold Typography theme
val PapaiOrange = PrimaryRed
val PapaiLightOrange = SoftPinkContainer

// M3 Color Scheme Fallbacks
val PrimaryKid = PrimaryRed
val SecondaryKid = DeepRedDark
val TertiaryKid = SunnyYellow
val BackgroundKid = CloudWhite
val SurfaceKid = Color.White
val OnPrimaryKid = Color.White
val OnSecondaryKid = Color.White
val OnBackgroundKid = CharcoalKids
val OnSurfaceKid = CharcoalKids


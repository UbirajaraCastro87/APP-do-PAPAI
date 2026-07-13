package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.*
import com.example.audio.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.GameScreen
import com.example.ui.viewmodel.GameViewModel
import kotlin.math.pow
import kotlinx.coroutines.delay
import kotlin.math.sqrt

/**
 * Tela de Introdução/Splash
 */
@Composable
fun SplashIntroScreen(viewModel: GameViewModel) {
    val language by viewModel.appLanguage.collectAsState()
    val isAudioEnabled by viewModel.isAssistantAudioEnabled.collectAsState()
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SkyBlue.copy(alpha = 0.2f), CloudWhite)
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Cabeçalho e Toggle de Idioma
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "EDTECH PAPAI",
                        style = MaterialTheme.typography.labelLarge,
                        color = SubtitleRed,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = if (language == "PT") "Alfabetização" else "Literacy",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepRedDark,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.graphicsLayer { translationY = -4f }
                    )
                }

                // Controls (Audio Toggle & Language Toggle)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Assistant Audio Mute/Unmute Toggle
                    IconButton(
                        onClick = { viewModel.toggleAssistantAudio() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SoftPinkBg)
                            .border(1.5.dp, DeepRedDark, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isAudioEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = if (isAudioEnabled) "Mute Audio" else "Unmute Audio",
                            tint = DeepRedDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Voice Selection Button
                    IconButton(
                        onClick = {
                            viewModel.refreshAvailableVoices()
                            viewModel.showVoiceSelectorDialog.value = true
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SoftPinkBg)
                            .border(1.5.dp, DeepRedDark, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Select Voice",
                            tint = DeepRedDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Soundtrack Selector Button
                    IconButton(
                        onClick = { viewModel.showSoundtrackDialog.value = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SoftPinkBg)
                            .border(1.5.dp, DeepRedDark, CircleShape)
                    ) {
                        Text(
                            text = "🎵",
                            fontSize = 18.sp
                        )
                    }

                    // Language Toggle (PT/EN) like the HTML
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SoftPinkBg)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // PT Button
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (language == "PT") PrimaryRed else Color.Transparent)
                                .clickable { if (language != "PT") viewModel.toggleLanguage() }
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "PT",
                                color = if (language == "PT") Color.White else DeepRedDark,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                        // EN Button
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (language == "EN") PrimaryRed else Color.Transparent)
                                .clickable { if (language != "EN") viewModel.toggleLanguage() }
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "EN",
                                color = if (language == "EN") Color.White else DeepRedDark,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Título Principal
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (language == "PT") "Papai Alfabetiza" else "Papai Literacy",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = CharcoalKids,
                    textAlign = TextAlign.Center,
                    lineHeight = 44.sp
                )
                Text(
                    text = if (language == "PT") "Aventura dos Caminhos Mágicos!" else "Magic Pathways Adventure!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = CharcoalKids.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Mascote Papai (Imagem Gerada)
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .border(4.dp, DeepRedDark, RoundedCornerShape(32.dp))
                    .background(Color.White)
            ) {
                // Carrega a imagem do mascote Papai criada por IA
                Image(
                    painter = painterResource(id = R.drawable.img_papai_mascot_1783532705143),
                    contentDescription = "Mascote Papai",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Balão de fala do Papai (using custom SoftPinkContainer and DeepRedDark border)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(SoftPinkContainer)
                    .border(3.dp, DeepRedDark, RoundedCornerShape(24.dp))
                    .padding(16.dp)
                    .clickable { viewModel.speakPapaiIntroduction() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speak",
                        tint = DeepRedDark,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = if (language == "PT") {
                            "\"Oi! Sou o Papai! Toque para falarmos em Português ou Inglês! Vamos jogar?\""
                        } else {
                            "\"Hi! I'm Papai! Tap to speak in English or Portuguese! Let's play!\""
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = DeepRedDark
                    )
                }
            }

            // Botão Gigante de Jogar
            Button(
                onClick = {
                    if (viewModel.childNickname.value.isEmpty()) {
                        viewModel.navigateTo(GameScreen.AvatarSelection)
                    } else {
                        viewModel.navigateTo(GameScreen.WorldSelection)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .scale(pulseScale)
                    .testTag("play_button")
                    .border(3.dp, DeepRedDark, RoundedCornerShape(28.dp)),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Jogar",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = if (language == "PT") "VAMOS JOGAR!" else "LET'S PLAY!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

/**
 * Mapa da Trilha de Aprendizagem (Seleção de Mundos e Fases)
 */
@Composable
fun WorldSelectionScreen(viewModel: GameViewModel) {
    val language by viewModel.appLanguage.collectAsState()
    val isAudioEnabled by viewModel.isAssistantAudioEnabled.collectAsState()
    val progressList by viewModel.userProgress.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()
    val totalStars by viewModel.totalStars.collectAsState()
    val unlockedBadges by viewModel.unlockedBadges.collectAsState()

    val selectedBadgeDetail = remember { mutableStateOf<GameBadge?>(null) }

    // Mapeamento de progresso resolvido
    val completedPhases = progressList.associate { it.phaseId to it.completed }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Cabeçalho da Trilha
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Home Button with SoftPinkContainer & DeepRedDark border
                    IconButton(
                        onClick = { viewModel.navigateTo(GameScreen.SplashIntro) },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SoftPinkContainer)
                            .border(2.dp, DeepRedDark, RoundedCornerShape(16.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = DeepRedDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    val nickname by viewModel.childNickname.collectAsState()
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (nickname.isNotEmpty()) {
                                if (language == "PT") "JORNADA DE ${nickname.uppercase()}" else "${nickname.uppercase()}'S JOURNEY"
                            } else {
                                if (language == "PT") "MUNDOS" else "WORLDS"
                            },
                            style = MaterialTheme.typography.labelLarge,
                            color = SubtitleRed,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = if (language == "PT") "Trilha do Papai" else "Papai's Trail",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = DeepRedDark,
                            modifier = Modifier.graphicsLayer { translationY = -4f }
                        )
                    }

                    // Controls (Audio Toggle, Avatar Select, Voice Selector & Language Toggle)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Assistant Audio Mute/Unmute Toggle
                        IconButton(
                            onClick = { viewModel.toggleAssistantAudio() },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SoftPinkBg)
                                .border(1.5.dp, DeepRedDark, CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isAudioEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                contentDescription = if (isAudioEnabled) "Mute Audio" else "Unmute Audio",
                                tint = DeepRedDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Avatar Selection Button
                        val childAvatarId by viewModel.selectedAvatarId.collectAsState()
                        val currentAvatar = GameAvatars.getAvatarById(childAvatarId)
                        IconButton(
                            onClick = { viewModel.navigateTo(GameScreen.AvatarSelection) },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SoftPinkBg)
                                .border(1.5.dp, DeepRedDark, CircleShape)
                        ) {
                            Text(
                                text = currentAvatar.emoji,
                                fontSize = 18.sp
                            )
                        }

                        // Voice Selection Button
                        IconButton(
                            onClick = {
                                viewModel.refreshAvailableVoices()
                                viewModel.showVoiceSelectorDialog.value = true
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SoftPinkBg)
                                .border(1.5.dp, DeepRedDark, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "Select Voice",
                                tint = DeepRedDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Soundtrack Selector Button
                        IconButton(
                            onClick = { viewModel.showSoundtrackDialog.value = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SoftPinkBg)
                                .border(1.5.dp, DeepRedDark, CircleShape)
                        ) {
                            Text(
                                text = "🎵",
                                fontSize = 18.sp
                            )
                        }

                        // Language Toggle (PT/EN)
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SoftPinkBg)
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (language == "PT") PrimaryRed else Color.Transparent)
                                    .clickable { if (language != "PT") viewModel.toggleLanguage() }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "PT",
                                    color = if (language == "PT") Color.White else DeepRedDark,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (language == "EN") PrimaryRed else Color.Transparent)
                                    .clickable { if (language != "EN") viewModel.toggleLanguage() }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "EN",
                                    color = if (language == "EN") Color.White else DeepRedDark,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // -------------------------------------------------------------
            // SISTEMA DE PONTUAÇÃO E CONQUISTAS (BADGES)
            // -------------------------------------------------------------
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(3.dp, DeepRedDark, RoundedCornerShape(24.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Painel de Pontuação Geral
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SoftPinkContainer)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (language == "PT") "PONTOS TOTAIS" else "TOTAL SCORE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = SubtitleRed
                            )
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = "🏆", fontSize = 24.sp)
                                Text(
                                    text = "$totalScore XP",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = DeepRedDark
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(40.dp)
                                .background(DeepRedDark.copy(alpha = 0.2f))
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (language == "PT") "ESTRELAS OBTIDAS" else "STARS EARNED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = SubtitleRed
                            )
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = "⭐", fontSize = 24.sp)
                                Text(
                                    text = "$totalStars",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = DeepRedDark
                                )
                            }
                        }
                    }

                    // 2. Prateleira de Medalhas (Badges Shelf)
                    Text(
                        text = if (language == "PT") "Minhas Medalhas 🏅" else "My Badges 🏅",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = DeepRedDark,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GameBadges.badges.forEach { badge ->
                            val isUnlocked = unlockedBadges.contains(badge.id)
                            Column(
                                modifier = Modifier
                                    .width(80.dp)
                                    .clickable { selectedBadgeDetail.value = badge }
                                    .graphicsLayer {
                                        if (!isUnlocked) {
                                            alpha = 0.6f
                                        }
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(if (isUnlocked) SunnyYellow.copy(alpha = 0.25f) else Color.LightGray.copy(alpha = 0.3f))
                                        .border(
                                            width = if (isUnlocked) 2.5.dp else 1.5.dp,
                                            color = if (isUnlocked) SunnyYellow else Color.Gray.copy(alpha = 0.5f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isUnlocked) {
                                        Text(text = badge.emoji, fontSize = 32.sp)
                                    } else {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(text = badge.emoji, fontSize = 32.sp, modifier = Modifier.graphicsLayer { alpha = 0.3f })
                                            Icon(
                                                imageVector = Icons.Default.Lock,
                                                contentDescription = "Lock",
                                                tint = Color.Gray,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = if (language == "PT") badge.titlePt else badge.titleEn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUnlocked) DeepRedDark else Color.Gray,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    modifier = Modifier.padding(top = 4.dp),
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // Diálogo de Detalhes da Medalha
            val badgeToShow = selectedBadgeDetail.value
            if (badgeToShow != null) {
                val isUnlocked = unlockedBadges.contains(badgeToShow.id)
                item {
                    AlertDialog(
                        onDismissRequest = { selectedBadgeDetail.value = null },
                        confirmButton = {
                            Button(
                                onClick = { selectedBadgeDetail.value = null },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(text = if (language == "PT") "Legal!" else "Cool!", fontWeight = FontWeight.Black, color = Color.White)
                            }
                        },
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = badgeToShow.emoji, fontSize = 36.sp)
                                Text(
                                    text = if (language == "PT") badgeToShow.titlePt else badgeToShow.titleEn,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = DeepRedDark
                                )
                            }
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = if (language == "PT") badgeToShow.descriptionPt else badgeToShow.descriptionEn,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CharcoalKids
                                )
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SoftPinkContainer)
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Info,
                                        contentDescription = "Status",
                                        tint = if (isUnlocked) WatermelonGreen else SubtitleRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (isUnlocked) {
                                            if (language == "PT") "Você já conquistou esta medalha!" else "You have already earned this medal!"
                                        } else {
                                            if (language == "PT") "Como conseguir: ${badgeToShow.conditionDescPt}" else "How to earn: ${badgeToShow.conditionDescEn}"
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUnlocked) WatermelonGreen else SubtitleRed
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        containerColor = Color.White,
                        modifier = Modifier.border(3.dp, DeepRedDark, RoundedCornerShape(24.dp))
                    )
                }
            }

            // Exibe cada Mundo e suas fases lineares
            GameWorld.values().forEach { world ->
                item {
                    val backgroundColor = when (world) {
                        GameWorld.WORLD_1 -> WatermelonGreen.copy(alpha = 0.15f)
                        GameWorld.WORLD_2 -> SkyBlue.copy(alpha = 0.15f)
                        GameWorld.WORLD_3 -> SunnyYellow.copy(alpha = 0.15f)
                        GameWorld.WORLD_4 -> LavenderSoft.copy(alpha = 0.15f)
                    }
                    val accentColor = when (world) {
                        GameWorld.WORLD_1 -> WatermelonGreen
                        GameWorld.WORLD_2 -> SkyBlue
                        GameWorld.WORLD_3 -> PapaiOrange
                        GameWorld.WORLD_4 -> LavenderSoft
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(backgroundColor)
                            .border(3.dp, DeepRedDark, RoundedCornerShape(24.dp))
                            .padding(16.dp)
                    ) {
                        // Título do Mundo in Bold Typography Style
                        Text(
                            text = if (language == "PT") "MUNDO ${world.id}: ${world.titlePt}" else "WORLD ${world.id}: ${world.titleEn}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = DeepRedDark,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = if (language == "PT") world.descriptionPt else world.descriptionEn,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = CharcoalKids.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Fases do Mundo
                        val worldPhases = GameCurriculum.phases.filter { it.worldId == world.id }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            worldPhases.forEach { phase ->
                                val isCompleted = completedPhases[phase.id] == true
                                
                                // Determina se a fase está desbloqueada de forma linear
                                val phaseIndex = GameCurriculum.phases.indexOfFirst { it.id == phase.id }
                                val isUnlocked = if (phaseIndex <= 0) {
                                    true
                                } else {
                                    val previousPhase = GameCurriculum.phases[phaseIndex - 1]
                                    completedPhases[previousPhase.id] == true
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (isCompleted) Color.White 
                                            else if (isUnlocked) Color.White.copy(alpha = 0.85f) 
                                            else Color.LightGray.copy(alpha = 0.3f)
                                        )
                                        .border(
                                            width = 2.dp,
                                            color = if (isCompleted) DeepRedDark 
                                                    else if (isUnlocked) DeepRedDark.copy(alpha = 0.5f) 
                                                    else Color.Gray.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable {
                                            if (isUnlocked) {
                                                viewModel.selectWorld(world.id)
                                                viewModel.navigateTo(GameScreen.PhaseInstruction(phase))
                                            } else {
                                                viewModel.speak(
                                                    if (language == "PT") 
                                                        "Amiguinho, essa missão está trancada! Complete a missão anterior primeiro!" 
                                                    else 
                                                        "Friend, this mission is locked! Complete the previous mission first!"
                                                )
                                            }
                                        }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // Círculo com o ID da fase
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isCompleted) PrimaryRed 
                                                    else if (isUnlocked) SoftPinkBg 
                                                    else Color.Gray.copy(alpha = 0.4f)
                                                )
                                                .border(2.dp, if (isUnlocked) DeepRedDark else Color.Gray.copy(alpha = 0.5f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = phase.id,
                                                color = if (isUnlocked) Color.White else Color.White.copy(alpha = 0.7f),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = if (language == "PT") phase.titlePt else phase.titleEn,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (isUnlocked) DeepRedDark else Color.Gray
                                            )
                                            Text(
                                                text = if (language == "PT") phase.descriptionPt else phase.descriptionEn,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUnlocked) CharcoalKids.copy(alpha = 0.7f) else Color.Gray.copy(alpha = 0.5f)
                                            )
                                        }
                                    }

                                    // Ícone de Status
                                    if (isCompleted) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = SunnyYellow, modifier = Modifier.size(20.dp))
                                            Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = SunnyYellow, modifier = Modifier.size(20.dp))
                                            Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = SunnyYellow, modifier = Modifier.size(20.dp))
                                        }
                                    } else if (isUnlocked) {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = "Play",
                                            tint = accentColor,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Locked",
                                            tint = Color.Gray.copy(alpha = 0.6f),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Botão Administrativo de Reset
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.resetAllGameProgress() },
                    colors = ButtonDefaults.buttonColors(containerColor = WatermelonRed),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("reset_progress_button")
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == "PT") "Zerar Meu Progresso" else "Reset My Progress",
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

/**
 * Tela de Onboarding e Instrução Narrada antes do Jogo
 */
@Composable
fun PhaseInstructionScreen(viewModel: GameViewModel, phase: Phase) {
    val language by viewModel.appLanguage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PapaiLightOrange, CloudWhite)
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Botão Voltar (custom SoftPinkContainer & DeepRedDark border)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(GameScreen.WorldSelection) },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SoftPinkContainer)
                        .border(2.dp, DeepRedDark, RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepRedDark,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Mascote Papai Fala
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .border(4.dp, DeepRedDark, CircleShape)
                        .background(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_papai_mascot_1783532705143),
                        contentDescription = "Papai",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Balão de fala gigante com as instruções
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(SoftPinkContainer)
                        .border(3.dp, DeepRedDark, RoundedCornerShape(24.dp))
                        .padding(20.dp)
                        .clickable { viewModel.speakInstruction(phase) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Falar",
                            tint = DeepRedDark,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = if (language == "PT") phase.audioPromptPt else phase.audioPromptEn,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = DeepRedDark,
                            textAlign = TextAlign.Center,
                            lineHeight = 26.sp
                        )
                        Text(
                            text = if (language == "PT") "Toque para ouvir de novo" else "Tap to listen again",
                            fontSize = 12.sp,
                            color = SubtitleRed,
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            // Botão Enorme de Começar in Bold Typography PrimaryRed
            Button(
                onClick = { viewModel.navigateTo(GameScreen.PhaseGame(phase)) },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .border(3.dp, DeepRedDark, RoundedCornerShape(24.dp))
                    .testTag("start_phase_button"),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(
                    text = if (language == "PT") "VAMOS COMEÇAR!" else "START MISSION!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

/**
 * Container principal do Jogo que roteia para o mini-game específico de acordo com a fase selecionada
 */
@Composable
fun PhaseGameScreen(viewModel: GameViewModel, phase: Phase) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        when (phase.type) {
            PhaseType.WATERMELON_TRACE -> WatermelonTraceGame(viewModel, phase)
            PhaseType.CARACOL_SPIRAL -> SpiralCaracolGame(viewModel, phase)
            PhaseType.JOANINHA_BOUNCE -> JoaninhaBounceGame(viewModel, phase)
            PhaseType.ANIMAL_PATH -> AnimalPathGame(viewModel, phase)
            PhaseType.INITIAL_SOUND -> VowelPhoneticsGame(viewModel, phase, isInitial = true)
            PhaseType.FINAL_SOUND -> VowelPhoneticsGame(viewModel, phase, isInitial = false)
            PhaseType.BALLOON_DRAG -> BalloonDragGame(viewModel, phase)
            PhaseType.SYLLABLE_B -> SyllableCompletionGame(viewModel, phase, isLetterB = true)
            PhaseType.SYLLABLE_C -> SyllableCompletionGame(viewModel, phase, isLetterB = false)
            PhaseType.SPECIAL_SNAKE -> AlphabetSnakeGame(viewModel, phase)
            PhaseType.MATH_COUNT -> MathCountGame(viewModel, phase)
            PhaseType.FINAL_READING -> FinalReadingGame(viewModel, phase)
            PhaseType.NUMBER_GAME -> NumberSequenceGame(viewModel, phase)
            PhaseType.DAYS_MONTHS_SEASONS -> DaysMonthsSeasonsGame(viewModel, phase)
        }
    }
}

/**
 * Cabeçalho do jogo com botão de ajuda (Papai), progresso e áudio
 */
@Composable
fun GameHeader(viewModel: GameViewModel, phase: Phase, onBack: () -> Unit) {
    val language by viewModel.appLanguage.collectAsState()
    val isAudioEnabled by viewModel.isAssistantAudioEnabled.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Close button with custom container (styled as active:scale-95 transition-transform)
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SoftPinkContainer)
                    .border(2.dp, DeepRedDark, RoundedCornerShape(16.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = DeepRedDark,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Subtitle and Title
            Column {
                Text(
                    text = if (language == "PT") "MUNDO ${phase.worldId}" else "WORLD ${phase.worldId}",
                    style = MaterialTheme.typography.labelLarge,
                    color = SubtitleRed,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = if (language == "PT") phase.titlePt else phase.titleEn,
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepRedDark,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.graphicsLayer { translationY = -4f }
                )
            }
        }

        // Controls Row: Mute Toggle, Voice Selection and Help/Repeat
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Assistant Audio Mute/Unmute Toggle
            IconButton(
                onClick = { viewModel.toggleAssistantAudio() },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SoftPinkBg)
                    .border(2.dp, DeepRedDark, RoundedCornerShape(16.dp))
            ) {
                Icon(
                    imageVector = if (isAudioEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                    contentDescription = if (isAudioEnabled) "Mute Audio" else "Unmute Audio",
                    tint = DeepRedDark,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Voice Selection Button
            IconButton(
                onClick = {
                    viewModel.refreshAvailableVoices()
                    viewModel.showVoiceSelectorDialog.value = true
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SoftPinkBg)
                    .border(2.dp, DeepRedDark, RoundedCornerShape(16.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Select Voice",
                    tint = DeepRedDark,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Soundtrack Selector Button
            IconButton(
                onClick = { viewModel.showSoundtrackDialog.value = true },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SoftPinkBg)
                    .border(2.dp, DeepRedDark, RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "🎵",
                    fontSize = 22.sp
                )
            }

            // Help/Repeat button (helps the child hear the voice instruction again)
            IconButton(
                onClick = {
                    if (!isAudioEnabled) {
                        viewModel.toggleAssistantAudio()
                    }
                    viewModel.speakInstruction(phase)
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SoftPinkBg)
                    .border(2.dp, DeepRedDark, RoundedCornerShape(16.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Repeat",
                    tint = DeepRedDark,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * FASE 1.1: Gotas de Melancia (Trace vertical de cima para baixo com Canvas)
 */
@Composable
fun WatermelonTraceGame(viewModel: GameViewModel, phase: Phase) {
    val context = LocalContext.current
    var width by remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0f) }

    // Progresso do trace de cada uma das 3 gotas (0f a 1f)
    val dropProgress = remember { mutableStateListOf(0f, 0f, 0f) }
    val isDropping = remember { mutableStateListOf(false, false, false) }

    // Efeito de queda após completar o traço
    val drop1Y by animateFloatAsState(if (isDropping[0]) 1f else dropProgress[0])
    val drop2Y by animateFloatAsState(if (isDropping[1]) 1f else dropProgress[1])
    val drop3Y by animateFloatAsState(if (isDropping[2]) 1f else dropProgress[2])

    val completed = dropProgress.all { it >= 0.95f } || (isDropping[0] && isDropping[1] && isDropping[2])

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

        // Painel Instrucional
        Text(
            text = if (viewModel.appLanguage.value == "PT") {
                "Arraste as gotas de melancia de CIMA para BAIXO!"
            } else {
                "Drag the watermelon drops from TOP to BOTTOM!"
            },
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalKids,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Canvas de Desenho Interativo
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
                .border(4.dp, WatermelonGreen, RoundedCornerShape(32.dp))
                .onSizeChanged { size ->
                    width = size.width.toFloat()
                    height = size.height.toFloat()
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            // Identifica qual gota o usuário tocou
                            // Mapeia coordenadas em colunas
                        },
                        onDrag = { change, dragAmount ->
                            val pos = change.position
                            // Calcula colunas
                            val colWidth = width / 3f
                            val column = (pos.x / colWidth)
                                .toInt()
                                .coerceIn(0, 2)

                            // Verifica se o toque está perto da linha vertical dessa coluna
                            val lineX = colWidth * column + colWidth / 2f
                            val distanceX = kotlin.math.abs(pos.x - lineX)

                            val startY = height * 0.15f
                            val endY = height * 0.85f
                            val totalLength = endY - startY

                            val density = context.resources.displayMetrics.density
                            val eightyPx = 80f * density

                            if (distanceX < eightyPx && pos.y >= startY && pos.y <= endY) {
                                val currentProgress = (pos.y - startY) / totalLength
                                // Garante que a criança trace de cima para baixo
                                if (currentProgress > dropProgress[column] && currentProgress - dropProgress[column] < 0.35f) {
                                    dropProgress[column] = currentProgress.coerceIn(0f, 1f)
                                    // Se chegou quase no final, completa
                                    if (dropProgress[column] >= 0.9f && !isDropping[column]) {
                                        isDropping[column] = true
                                        viewModel.playSuccessSound()
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {

                val colWidth = size.width / 3f
                val startY = size.height * 0.15f
                val endY = size.height * 0.85f

                // Desenha 3 linhas verticais pontilhadas
                for (i in 0..2) {
                    val lineX = colWidth * i + colWidth / 2f

                    // Linha pontilhada de fundo
                    drawLine(
                        color = WatermelonDashedLine,
                        start = Offset(lineX, startY),
                        end = Offset(lineX, endY),
                        strokeWidth = 8.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                    )

                    // Linha de progresso preenchida de melancia
                    val currentProgressY = startY + (endY - startY) * dropProgress[i]
                    if (dropProgress[i] > 0f) {
                        drawLine(
                            color = WatermelonRed,
                            start = Offset(lineX, startY),
                            end = Offset(lineX, currentProgressY),
                            strokeWidth = 10.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }

                    // Desenha o vegetal/folha no topo
                    drawCircle(
                        color = WatermelonGreen,
                        radius = 16.dp.toPx(),
                        center = Offset(lineX, startY - 10.dp.toPx())
                    )

                    // Desenha a fatia de melancia na base
                    // Rind verde
                    drawArc(
                        color = WatermelonGreen,
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(lineX - 40.dp.toPx(), endY - 20.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(80.dp.toPx(), 60.dp.toPx())
                    )
                    // Polpa vermelha
                    drawArc(
                        color = WatermelonRed,
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(lineX - 35.dp.toPx(), endY - 15.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(70.dp.toPx(), 50.dp.toPx())
                    )

                    // Desenha a Gota caindo (Gotinha azul ou rosa de suco)
                    val dropY = when(i) {
                        0 -> startY + (endY - startY) * drop1Y
                        1 -> startY + (endY - startY) * drop2Y
                        else -> startY + (endY - startY) * drop3Y
                    }

                    drawCircle(
                        color = if (isDropping[i]) SunnyYellow else SkyBlue,
                        radius = 14.dp.toPx(),
                        center = Offset(lineX, dropY)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de conclusão quando finalizado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = completed,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Button(
                    onClick = { viewModel.completePhase(phase) },
                    colors = ButtonDefaults.buttonColors(containerColor = PapaiOrange),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("complete_watermelon_button")
                ) {
                    Text(
                        text = if (viewModel.appLanguage.value == "PT") "PRÓXIMO!" else "NEXT!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * FASE 1.2: O Caracol Espiral (Canvas de Desenho de Espiral)
 */
@Composable
fun SpiralCaracolGame(viewModel: GameViewModel, phase: Phase) {
    var traceProgress by remember { mutableStateOf(0f) }
    val completed = traceProgress >= 0.9f

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

        Text(
            text = if (viewModel.appLanguage.value == "PT") "Trace o espiral para completar o caracol!" else "Trace the spiral to complete the snail!",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalKids,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
                .border(4.dp, SkyBlue, RoundedCornerShape(32.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        // Detecção de arrasto de espiral simplificada para engajamento infantil de 6 anos
                        val pos = change.position
                        val distToCenter = sqrt((pos.x - size.width/2f).pow(2) + (pos.y - size.height/2f).pow(2))
                        if (distToCenter < size.width/2.5f) {
                            traceProgress = (traceProgress + 0.015f).coerceIn(0f, 1f)
                            if (traceProgress >= 0.9f) {
                                viewModel.playSuccessSound()
                            }
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                // Desenha corpo do caracol
                drawCircle(color = SunnyYellow, radius = 50.dp.toPx(), center = Offset(cx - 60.dp.toPx(), cy + 60.dp.toPx()))

                // Desenha espiral pontilhada
                val path = androidx.compose.ui.graphics.Path()
                path.moveTo(cx, cy)
                for (theta in 0..720 step 10) {
                    val rad = Math.toRadians(theta.toDouble())
                    val r = 10f + theta * 0.18f
                    val x = cx + (r * kotlin.math.cos(rad)).toFloat()
                    val y = cy + (r * kotlin.math.sin(rad)).toFloat()
                    path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = SkyBlue.copy(alpha = 0.4f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 8.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                    )
                )

                // Desenha progresso do espiral
                val progressPath = androidx.compose.ui.graphics.Path()
                progressPath.moveTo(cx, cy)
                val maxTheta = (720 * traceProgress).toInt()
                for (theta in 0..maxTheta step 5) {
                    val rad = Math.toRadians(theta.toDouble())
                    val r = 10f + theta * 0.18f
                    val x = cx + (r * kotlin.math.cos(rad)).toFloat()
                    val y = cy + (r * kotlin.math.sin(rad)).toFloat()
                    progressPath.lineTo(x, y)
                }

                drawPath(
                    path = progressPath,
                    color = PapaiOrange,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 10.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = completed) {
            Button(
                onClick = { viewModel.completePhase(phase) },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(58.dp)
            ) {
                Text(text = if (viewModel.appLanguage.value == "PT") "PRÓXIMO!" else "NEXT!", fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

/**
 * FASE 1.3: Saltos da Joaninha (Desenhar arcos/pulinhos de joaninha)
 */
@Composable
fun JoaninhaBounceGame(viewModel: GameViewModel, phase: Phase) {
    var bounceProgress by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val completed = bounceProgress >= 0.98f

    // Helper to calculate ladybug position along the two arches
    fun getLadybugPosition(progress: Float, w: Float, h: Float): Offset {
        val startX = w * 0.15f
        val endX = w * 0.85f
        val currentX = startX + (endX - startX) * progress
        val currentY = if (progress < 0.5f) {
            h * 0.7f - (h * 0.3f) * 16f * progress * (0.5f - progress)
        } else {
            h * 0.7f - (h * 0.3f) * 16f * (progress - 0.5f) * (1.0f - progress)
        }
        return Offset(currentX, currentY)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

        Text(
            text = if (viewModel.appLanguage.value == "PT") "Arraste a Joaninha pelos pulinhos de flor em flor!" else "Drag the Ladybug along the jumps from flower to flower!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = CharcoalKids,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
                .border(4.dp, PapaiOrange, RoundedCornerShape(32.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { startOffset ->
                            val w = size.width.toFloat()
                            val h = size.height.toFloat()
                            val ladyPos = getLadybugPosition(bounceProgress, w, h)
                            val dist = kotlin.math.sqrt((startOffset.x - ladyPos.x).pow(2) + (startOffset.y - ladyPos.y).pow(2))
                            // Permite arrastar se tocar perto da joaninha (raio de 60dp)
                            if (dist < 60.dp.toPx()) {
                                isDragging = true
                            }
                        },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false },
                        onDrag = { change, _ ->
                            if (isDragging) {
                                val pos = change.position
                                val w = size.width.toFloat()
                                val h = size.height.toFloat()
                                val startX = w * 0.15f
                                val endX = w * 0.85f
                                val trackWidth = endX - startX
                                if (trackWidth > 0f) {
                                    val targetProgress = ((pos.x - startX) / trackWidth).coerceIn(0f, 1f)
                                    // Impede pulos muito bruscos para garantir que a criança faça o traço contínuo
                                    if (targetProgress - bounceProgress < 0.25f && targetProgress >= bounceProgress - 0.2f) {
                                        val oldProgress = bounceProgress
                                        bounceProgress = targetProgress
                                        if (bounceProgress >= 0.98f && oldProgress < 0.98f) {
                                            bounceProgress = 1f
                                            viewModel.playSuccessSound()
                                        }
                                    }
                                }
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // 1. Desenha o caule e folhas das flores
                val flowerXs = listOf(w * 0.15f, w * 0.5f, w * 0.85f)
                flowerXs.forEach { fx ->
                    // Caule
                    drawLine(
                        color = Color(0xFF2E7D32),
                        start = Offset(fx, h * 0.7f),
                        end = Offset(fx, h * 0.95f),
                        strokeWidth = 6.dp.toPx()
                    )
                    // Folha esquerda
                    drawOval(
                        color = Color(0xFF4CAF50),
                        topLeft = Offset(fx - 18.dp.toPx(), h * 0.78f),
                        size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 22.dp.toPx())
                    )
                    // Folha direita
                    drawOval(
                        color = Color(0xFF4CAF50),
                        topLeft = Offset(fx + 6.dp.toPx(), h * 0.83f),
                        size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 22.dp.toPx())
                    )
                }

                // 2. Desenha a trilha pontilhada de guia dos pulos
                val guidePath = androidx.compose.ui.graphics.Path()
                guidePath.moveTo(w * 0.15f, h * 0.7f)
                guidePath.quadraticTo(w * 0.325f, h * 0.4f, w * 0.5f, h * 0.7f)
                guidePath.quadraticTo(w * 0.675f, h * 0.4f, w * 0.85f, h * 0.7f)

                drawPath(
                    path = guidePath,
                    color = Color(0xFFB39DDB), // Lavender guia suave
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 6.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                    )
                )

                // 3. Desenha o rastro sólido já percorrido pela joaninha (Feedback tátil alegre)
                if (bounceProgress > 0.01f) {
                    val progressPath = androidx.compose.ui.graphics.Path()
                    progressPath.moveTo(w * 0.15f, h * 0.7f)
                    val steps = (bounceProgress * 100).toInt()
                    for (step in 1..steps) {
                        val p = step / 100f
                        val pt = getLadybugPosition(p, w, h)
                        progressPath.lineTo(pt.x, pt.y)
                    }
                    drawPath(
                        path = progressPath,
                        color = PapaiOrange,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 8.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                // 4. Desenha as pétalas e miolo de cada flor (Lindas e coloridas!)
                val petalColors = listOf(
                    Color(0xFFE91E63), // Flor 1: Rosa Chiclete
                    Color(0xFF03A9F4), // Flor 2: Azul Céu
                    Color(0xFFFF9800)  // Flor 3: Laranja Brilhante
                )
                flowerXs.forEachIndexed { index, fx ->
                    val fy = h * 0.7f
                    val petalColor = petalColors[index]

                    // 5 Pétalas girando ao redor
                    for (i in 0 until 5) {
                        val angleRad = Math.toRadians((i * 72).toDouble())
                        val px = fx + (15.dp.toPx() * kotlin.math.cos(angleRad)).toFloat()
                        val py = fy + (15.dp.toPx() * kotlin.math.sin(angleRad)).toFloat()
                        drawCircle(
                            color = petalColor,
                            radius = 12.dp.toPx(),
                            center = Offset(px, py)
                        )
                    }
                    // Miolo amarelo da flor
                    drawCircle(
                        color = Color(0xFFFFD54F),
                        radius = 10.dp.toPx(),
                        center = Offset(fx, fy)
                    )
                }

                // 5. Desenha a Joaninha super detalhada na sua posição atual
                val ladyPos = getLadybugPosition(bounceProgress, w, h)
                val rx = ladyPos.x
                val ry = ladyPos.y
                val ladybugRadius = 22.dp.toPx()

                // Cabeça (Preta)
                drawCircle(
                    color = Color(0xFF212121),
                    radius = 10.dp.toPx(),
                    center = Offset(rx + 14.dp.toPx(), ry)
                )

                // Antenas (Duas pequenas curvas pretas com pontas circulares)
                val antPath1 = androidx.compose.ui.graphics.Path()
                antPath1.moveTo(rx + 18.dp.toPx(), ry - 4.dp.toPx())
                antPath1.quadraticTo(rx + 24.dp.toPx(), ry - 14.dp.toPx(), rx + 26.dp.toPx(), ry - 12.dp.toPx())
                drawPath(
                    path = antPath1,
                    color = Color(0xFF212121),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
                drawCircle(color = Color(0xFF212121), radius = 3.dp.toPx(), center = Offset(rx + 26.dp.toPx(), ry - 12.dp.toPx()))

                val antPath2 = androidx.compose.ui.graphics.Path()
                antPath2.moveTo(rx + 18.dp.toPx(), ry + 4.dp.toPx())
                antPath2.quadraticTo(rx + 24.dp.toPx(), ry + 14.dp.toPx(), rx + 26.dp.toPx(), ry + 12.dp.toPx())
                drawPath(
                    path = antPath2,
                    color = Color(0xFF212121),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
                drawCircle(color = Color(0xFF212121), radius = 3.dp.toPx(), center = Offset(rx + 26.dp.toPx(), ry + 12.dp.toPx()))

                // Olhinhos (Dois pontinhos brancos e pretos simpáticos)
                drawCircle(color = Color.White, radius = 4.dp.toPx(), center = Offset(rx + 18.dp.toPx(), ry - 3.dp.toPx()))
                drawCircle(color = Color.White, radius = 4.dp.toPx(), center = Offset(rx + 18.dp.toPx(), ry + 3.dp.toPx()))
                drawCircle(color = Color.Black, radius = 1.5f.dp.toPx(), center = Offset(rx + 19.dp.toPx(), ry - 3.dp.toPx()))
                drawCircle(color = Color.Black, radius = 1.5f.dp.toPx(), center = Offset(rx + 19.dp.toPx(), ry + 3.dp.toPx()))

                // Corpo / Asas (Base preta e asas vermelhas brilhantes)
                drawCircle(color = Color(0xFF212121), radius = ladybugRadius, center = ladyPos)
                drawCircle(color = Color(0xFFE53935), radius = ladybugRadius - 2.dp.toPx(), center = ladyPos)

                // Linha preta divisória das asas
                drawLine(
                    color = Color(0xFF212121),
                    start = Offset(rx - ladybugRadius, ry),
                    end = Offset(rx + ladybugRadius, ry),
                    strokeWidth = 2.5f.dp.toPx()
                )

                // Pintinhas pretas das asas (Sétricas e lindas)
                val spotRadius = 3.dp.toPx()
                // Asa superior
                drawCircle(color = Color(0xFF212121), radius = spotRadius, center = Offset(rx - 8.dp.toPx(), ry - 8.dp.toPx()))
                drawCircle(color = Color(0xFF212121), radius = spotRadius, center = Offset(rx, ry - 11.dp.toPx()))
                drawCircle(color = Color(0xFF212121), radius = spotRadius, center = Offset(rx + 6.dp.toPx(), ry - 7.dp.toPx()))
                // Asa inferior
                drawCircle(color = Color(0xFF212121), radius = spotRadius, center = Offset(rx - 8.dp.toPx(), ry + 8.dp.toPx()))
                drawCircle(color = Color(0xFF212121), radius = spotRadius, center = Offset(rx, ry + 11.dp.toPx()))
                drawCircle(color = Color(0xFF212121), radius = spotRadius, center = Offset(rx + 6.dp.toPx(), ry + 7.dp.toPx()))
            }

            if (bounceProgress == 0f) {
                Text(
                    text = if (viewModel.appLanguage.value == "PT") "👈 Segure e arraste a Joaninha!" else "👈 Hold and drag the Ladybug!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = DeepRedDark,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = completed) {
            Button(
                onClick = { viewModel.completePhase(phase) },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(58.dp)
            ) {
                Text(text = if (viewModel.appLanguage.value == "PT") "PRÓXIMO!" else "NEXT!", fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

/**
 * FASE 1.4: Caminho dos Bichos (Conectar bichos a comida/brinquedo)
 */
@Composable
fun AnimalPathGame(viewModel: GameViewModel, phase: Phase) {
    var lineProgress by remember { mutableStateOf(0f) }
    val completed = lineProgress >= 0.9f

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

        Text(
            text = if (viewModel.appLanguage.value == "PT") "Conecte o Gatinho ao seu Novelo!" else "Connect the Kitten to its Yarn!",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalKids,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
                .border(4.dp, WatermelonGreen, RoundedCornerShape(32.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val pos = change.position
                        if (pos.x >= size.width * 0.1f && pos.x <= size.width * 0.9f) {
                            lineProgress = (pos.x / size.width).coerceIn(0f, 1f)
                            if (lineProgress >= 0.9f) {
                                viewModel.playSuccessSound()
                            }
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Bichinho na esquerda (gato 🐱)
                drawCircle(color = SunnyYellow, radius = 25.dp.toPx(), center = Offset(w * 0.15f, h * 0.5f))

                // Alvo na direita (novelo 🧶)
                drawCircle(color = WatermelonRed, radius = 25.dp.toPx(), center = Offset(w * 0.85f, h * 0.5f))

                // Linha pontilhada em zigue-zague
                val path = androidx.compose.ui.graphics.Path()
                path.moveTo(w * 0.15f, h * 0.5f)
                path.lineTo(w * 0.325f, h * 0.35f)
                path.lineTo(w * 0.5f, h * 0.65f)
                path.lineTo(w * 0.675f, h * 0.35f)
                path.lineTo(w * 0.85f, h * 0.5f)

                drawPath(
                    path = path,
                    color = SkyBlue.copy(alpha = 0.4f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 8.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                    )
                )

                // Trilha desenhada pelo usuário
                val userPath = androidx.compose.ui.graphics.Path()
                userPath.moveTo(w * 0.15f, h * 0.5f)
                if (lineProgress > 0.15f) {
                    val p1 = w * 0.325f
                    val p2 = w * 0.5f
                    val p3 = w * 0.675f
                    val p4 = w * 0.85f

                    val currentX = w * 0.15f + (w * 0.7f) * lineProgress

                    if (currentX < p1) {
                        val factor = (currentX - w * 0.15f) / (p1 - w * 0.15f)
                        userPath.lineTo(currentX, h * 0.5f + (h * -0.15f) * factor)
                    } else {
                        userPath.lineTo(p1, h * 0.35f)
                        if (currentX < p2) {
                            val factor = (currentX - p1) / (p2 - p1)
                            userPath.lineTo(currentX, h * 0.35f + (h * 0.3f) * factor)
                        } else {
                            userPath.lineTo(p2, h * 0.65f)
                            if (currentX < p3) {
                                val factor = (currentX - p2) / (p3 - p2)
                                userPath.lineTo(currentX, h * 0.65f + (h * -0.3f) * factor)
                            } else {
                                userPath.lineTo(p3, h * 0.35f)
                                val factor = (currentX - p3) / (p4 - p3)
                                userPath.lineTo(currentX, h * 0.35f + (h * 0.15f) * factor)
                            }
                        }
                    }
                }

                drawPath(
                    path = userPath,
                    color = PapaiOrange,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 10.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = completed) {
            Button(
                onClick = { viewModel.completePhase(phase) },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(58.dp)
            ) {
                Text(text = if (viewModel.appLanguage.value == "PT") "PRÓXIMO!" else "NEXT!", fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

/**
 * MUNDO 2: Fases 2.1 & 2.2 - Fonética Vocálica (Som Inicial / Som Final)
 */
@Composable
fun VowelPhoneticsGame(viewModel: GameViewModel, phase: Phase, isInitial: Boolean) {
    val language by viewModel.appLanguage.collectAsState()
    val challengeIndex by viewModel.vowelMatchIndex.collectAsState()
    val completed by viewModel.vowelMatchCompleted.collectAsState()

    val totalChallenges = if (isInitial) GameCurriculum.initialSoundChallenges.size else GameCurriculum.finalSoundChallenges.size
    val currentChallenge = if (isInitial) {
        GameCurriculum.initialSoundChallenges[challengeIndex.coerceIn(0, totalChallenges - 1)]
    } else {
        GameCurriculum.finalSoundChallenges[challengeIndex.coerceIn(0, totalChallenges - 1)]
    }

    val displayWord = if (language == "PT") currentChallenge.wordPt else currentChallenge.wordEn
    val options = if (language == "PT") currentChallenge.optionsPt else currentChallenge.optionsEn

    // Mapeamento de emojis representativos para pre-literate kids
    val emoji = when (displayWord.lowercase()) {
        "abacaxi" -> "🍍"
        "apple" -> "🍎"
        "elefante" -> "🐘"
        "elephant" -> "🐘"
        "índio" -> "🏹"
        "igloo" -> "❄️"
        "ovo" -> "🥚"
        "octopus" -> "🐙"
        "urso" -> "🧸"
        "umbrella" -> "☔"
        "coruja" -> "🦉"
        "banana" -> "🍌"
        "tomato" -> "🍅"
        "saci" -> "🧚"
        "taxi" -> "🚕"
        "abacate" -> "🥑"
        "tree" -> "🌳"
        "urubu" -> "🦅"
        "bamboo" -> "🎋"
        else -> "🌟"
    }

    // Fala automaticamente a palavra uma vez ao iniciar a fase ou ao avançar para a próxima palavra
    LaunchedEffect(challengeIndex) {
        delay(800L)
        val clue = if (language == "PT") currentChallenge.audioCluePt else currentChallenge.audioClueEn
        viewModel.speak(clue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

        // Barra de progresso visual de estrelinhas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until totalChallenges) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Progresso",
                    tint = if (i < challengeIndex) SunnyYellow else LavenderSoft,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(horizontal = 2.dp)
                )
            }
        }

        // Cartão Ilustrativo Grande do Desenho
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp)
                .border(4.dp, SkyBlue, RoundedCornerShape(32.dp)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Desenha Emoji de tamanho gigante para estímulo visual
                Text(
                    text = emoji,
                    fontSize = 110.sp,
                    lineHeight = 120.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Exibe a palavra com a letra faltante
                val missingSign = " _ "
                val solvedWord = if (isInitial) {
                    missingSign + displayWord.substring(1)
                } else {
                    displayWord.substring(0, displayWord.length - 1) + missingSign
                }

                Text(
                    text = solvedWord.uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = CharcoalKids,
                    letterSpacing = 2.sp
                )
            }
        }

        // Prompt de Voz Interativo do Papai
        Button(
            onClick = { viewModel.speak(if (language == "PT") currentChallenge.audioCluePt else currentChallenge.audioClueEn) },
            colors = ButtonDefaults.buttonColors(containerColor = PapaiLightOrange),
            modifier = Modifier.border(2.dp, PapaiOrange, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Listen", tint = PapaiOrange)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == "PT") "Ouvir Som!" else "Listen Sound!",
                color = CharcoalKids,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botões Gigantes das Opções de Vogais
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            options.forEach { option ->
                Button(
                    onClick = { viewModel.answerVowel(option, isInitial) },
                    colors = ButtonDefaults.buttonColors(containerColor = SunnyYellow),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .size(90.dp)
                        .testTag("vowel_option_${option.lowercase()}")
                        .border(3.dp, PapaiOrange, RoundedCornerShape(24.dp)),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = option,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = CharcoalKids
                    )
                }
            }
        }

        // Navega para comemoração se completado
        LaunchedEffect(completed) {
            if (completed) {
                // Insere desafio matemático rápido antes de concluir!
                viewModel.navigateTo(GameScreen.MathIntercalated(phase))
            }
        }
    }
}

/**
 * FASE 2.3: Ditado Recortado Balões (Arrastar e Soltar Encontros Vocálicos)
 */
@Composable
fun BalloonDragGame(viewModel: GameViewModel, phase: Phase) {
    val language by viewModel.appLanguage.collectAsState()
    val matches by viewModel.balloonMatches.collectAsState()

    val completed = matches.size >= GameCurriculum.vocalicBalloons.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

        Text(
            text = if (language == "PT") "Diga OI pro menino ou AU-AU pro cachorrinho!" else "Say HI to the boy or WOOF to the puppy!",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalKids,
            textAlign = TextAlign.Center
        )

        // Personagens Receptores
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Personagem 1: Cachorro 🐶
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (matches.containsValue("dog")) SunnyYellow.copy(alpha = 0.3f) else Color.White)
                    .border(2.dp, PapaiOrange, RoundedCornerShape(24.dp))
                    .clickable {
                        // Clique alternativo para crianças que acham drag & drop complexo
                        val unmatched = GameCurriculum.vocalicBalloons.find { !matches.containsKey(it.text) }
                        if (unmatched != null) {
                            viewModel.matchBalloon(unmatched.text, "dog")
                        }
                    }
                    .padding(16.dp)
                    .width(110.dp)
            ) {
                Text(text = "🐶", fontSize = 54.sp)
                Text(text = if (language == "PT") "Cachorro" else "Puppy", fontWeight = FontWeight.Bold, color = CharcoalKids)
                
                // Exibe balão conectado
                matches.filter { it.value == "dog" }.keys.forEach { balloon ->
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SkyBlue)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = balloon, color = Color.White, fontWeight = FontWeight.Black)
                    }
                }
            }

            // Personagem 2: Menino acenando 👦
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (matches.containsValue("boy")) SunnyYellow.copy(alpha = 0.3f) else Color.White)
                    .border(2.dp, PapaiOrange, RoundedCornerShape(24.dp))
                    .clickable {
                        val unmatched = GameCurriculum.vocalicBalloons.find { !matches.containsKey(it.text) }
                        if (unmatched != null) {
                            viewModel.matchBalloon(unmatched.text, "boy")
                        }
                    }
                    .padding(16.dp)
                    .width(110.dp)
            ) {
                Text(text = "👦", fontSize = 54.sp)
                Text(text = if (language == "PT") "Menino" else "Boy", fontWeight = FontWeight.Bold, color = CharcoalKids)

                // Exibe balão conectado
                matches.filter { it.value == "boy" }.keys.forEach { balloon ->
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SkyBlue)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = balloon, color = Color.White, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // Balões de fala flutuantes e clicáveis
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GameCurriculum.vocalicBalloons.forEach { balloon ->
                val isMatched = matches.containsKey(balloon.text)
                AnimatedVisibility(
                    visible = !isMatched,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(SkyBlue)
                            .border(3.dp, Color.White, CircleShape)
                            .clickable {
                                // Fala som
                                viewModel.speak(balloon.text)
                                // Alterna correspondência rápida
                                viewModel.matchBalloon(balloon.text, balloon.targetId)
                            }
                            .testTag("balloon_${balloon.text.lowercase().replace("!", "")}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = balloon.text,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        // Próximo
        AnimatedVisibility(visible = completed) {
            Button(
                onClick = { viewModel.completePhase(phase) },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
            ) {
                Text(text = if (language == "PT") "PRÓXIMO!" else "NEXT!", fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

/**
 * MUNDO 3: Fase 3.1 & 3.2 - Completar Sílabas (Letras B e C)
 */
@Composable
fun SyllableCompletionGame(viewModel: GameViewModel, phase: Phase, isLetterB: Boolean) {
    val language by viewModel.appLanguage.collectAsState()
    
    // Controle de estado local para a letra selecionada do alfabeto A-Z
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    // Letras resolvidas nesta sessão
    var completedLetters by remember { mutableStateOf(setOf<String>()) }
    
    // Estado do jogo ativo para a letra selecionada
    var currentChallengeIndex by remember { mutableStateOf(0) }
    var letterGameCompleted by remember { mutableStateOf(false) }
    
    // Spelling game active states (for Phase 1.5 - Soletrar)
    var scrambledLetters by remember { mutableStateOf(listOf<Char>()) }
    var spelledWordLetters by remember { mutableStateOf(listOf<Char>()) }

    val alphabetList = ('A'..'Z').map { it.toString() }

    if (selectedLetter == null) {
        // --- TELA DE PORTAL DO ALFABETO A-Z ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

            Text(
                text = if (language == "PT") {
                    if (isLetterB) "AVENTURA DAS LETRAS A-Z\nEscolha uma letra para brincar de Sílabas!" 
                    else "JOGO DE PALAVRAS A-Z\nEscolha uma letra para brincar de Soletrar!"
                } else {
                    if (isLetterB) "LETTER ADVENTURE A-Z\nChoose a letter to play Syllables!"
                    else "WORD GAME A-Z\nChoose a letter to play Spelling!"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = CharcoalKids,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            // Grid de Balões/Bolhas das Letras
            LazyVerticalGrid(
                columns = GridCells.Adaptive(54.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
                    .border(2.dp, PapaiOrange.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(alphabetList) { letter ->
                    val isLetterCompleted = completedLetters.contains(letter)
                    val bubbleColor = if (isLetterCompleted) WatermelonGreen else SunnyYellow
                    val textColor = if (isLetterCompleted) Color.White else CharcoalKids
                    
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(bubbleColor)
                            .border(3.dp, PapaiOrange, CircleShape)
                            .clickable {
                                selectedLetter = letter
                                currentChallengeIndex = 0
                                letterGameCompleted = false
                                viewModel.speak(if (language == "PT") "Letra $letter selecionada!" else "Letter $letter selected!")
                                
                                // Se for o jogo de soletrar (Phase 1.5), prepara as letras misturadas
                                if (!isLetterB) {
                                    val listChallenges = GameCurriculum.alphabetChallenges[letter] ?: emptyList()
                                    if (listChallenges.isNotEmpty()) {
                                        val word = if (language == "PT") listChallenges[0].completeWordPt else listChallenges[0].completeWordEn
                                        scrambledLetters = word.uppercase().toList().shuffled()
                                        spelledWordLetters = emptyList()
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = letter,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = textColor
                            )
                            if (isLetterCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Done",
                                    tint = SunnyYellow,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Botão Concluir se já completaram pelo menos 1 letra
            Button(
                onClick = { viewModel.completePhase(phase) },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .border(3.dp, Color.White, RoundedCornerShape(24.dp))
                    .testTag("alphabet_complete_phase_button")
            ) {
                Text(
                    text = if (language == "PT") "CONCLUIR JORNADA DO ALFABETO!" else "COMPLETE ALPHABET JOURNEY!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    } else {
        // --- JOGO DE LETRA ATIVA ---
        val letter = selectedLetter!!
        val challenges = GameCurriculum.alphabetChallenges[letter] ?: emptyList()
        
        if (challenges.isEmpty()) {
            selectedLetter = null
            return
        }

        val currentChallenge = challenges[currentChallengeIndex.coerceIn(0, challenges.size - 1)]
        val displayWord = if (language == "PT") currentChallenge.completeWordPt else currentChallenge.completeWordEn
        val options = if (language == "PT") currentChallenge.syllablesPt else currentChallenge.syllablesEn
        val missingSyllable = if (language == "PT") currentChallenge.missingSyllablePt else currentChallenge.missingSyllableEn

        // Mapeamento de emojis dinâmicos
        val emoji = when (displayWord.uppercase()) {
            "ANEL" -> "💍"
            "APE" -> "🦧"
            "BALA" -> "🍬"
            "BANANA" -> "🍌"
            "BOCA" -> "👄"
            "BOX" -> "📦"
            "BULE" -> "🫖"
            "BUS" -> "🚌"
            "BEBÊ" -> "👶"
            "BED" -> "🛏️"
            "CASA" -> "🏠"
            "CAT" -> "🐱"
            "COPO" -> "🥛"
            "COW" -> "🐄"
            "CUCA" -> "🐊"
            "CUP" -> "🥤"
            "CENOURA" -> "🥕"
            "CELLO" -> "🎻"
            "CINEMA" -> "🎬"
            "CITY" -> "🏙️"
            "DADO" -> "🎲"
            "DATE" -> "📅"
            "DOCE" -> "🍬"
            "DOG" -> "🐶"
            "ESCOLA" -> "🏫"
            "EGG" -> "🥚"
            "FADA" -> "🧚"
            "FAN" -> "🪭"
            "FOGO" -> "🔥"
            "FOX" -> "🦊"
            "GATO" -> "🐱"
            "GAME" -> "🎮"
            "GOTA" -> "💧"
            "GOAT" -> "🐐"
            "HARPA" -> "🎵"
            "HAM" -> "🍖"
            "HELICE", "HÉLICE" -> "🚁"
            "HEN" -> "🐔"
            "IGREJA" -> "⛪"
            "INK" -> "✒️"
            "JANELA" -> "🪟"
            "JAR" -> "🫙"
            "JIPE" -> "🚙"
            "JEEP" -> "🚗"
            "KIWI" -> "🥝"
            "KID" -> "👶"
            "LÁPIS", "LAPIS" -> "✏️"
            "LAKE" -> "🌊"
            "LUA" -> "🌙"
            "LUNG" -> "🫁"
            "MACACO" -> "🐒"
            "MAP" -> "🗺️"
            "MOLA" -> "🌀"
            "MOP" -> "🧹"
            "NAVIO" -> "🚢"
            "NAIL" -> "💅"
            "NUVEM" -> "☁️"
            "NUT" -> "🥜"
            "ÔNIBUS", "ONIBUS" -> "🚌"
            "OWL" -> "🦉"
            "PATO" -> "🦆"
            "PAN" -> "🍳"
            "PIPOCA" -> "🍿"
            "PIG" -> "🐷"
            "QUEIJO" -> "🧀"
            "QUEEN" -> "👑"
            "RATO" -> "🐭"
            "RAT" -> "🐀"
            "RUA" -> "🛣️"
            "RUG" -> "🪚"
            "SAPO" -> "🐸"
            "SAD" -> "😢"
            "SUCO" -> "🧃"
            "SUN" -> "☀️"
            "TATU" -> "🦫"
            "TAB" -> "📱"
            "TIJOLO" -> "🧱"
            "TIN" -> "🥫"
            "URSO" -> "🐻"
            "URN" -> "⚱️"
            "VACA" -> "🐄"
            "VAN" -> "🚐"
            "VELA" -> "🕯️"
            "VEST" -> "🦺"
            "WAFER" -> "🧇"
            "WEB" -> "🕸️"
            "XALE" -> "🧣"
            "XRAY", "X-RAY" -> "🩻"
            "XIXI" -> "💦"
            "YOGA" -> "🧘"
            "YAK" -> "🐂"
            "ZEBRA" -> "🦓"
            "ZOO" -> "🦁"
            "ZERO" -> "0️⃣"
            "ZIP" -> "🤐"
            else -> "🎁"
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { selectedLetter = null },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SoftPinkContainer)
                        .border(2.dp, DeepRedDark, RoundedCornerShape(16.dp))
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = DeepRedDark)
                }

                Text(
                    text = if (language == "PT") "Letra $letter: ${challenges.size - currentChallengeIndex} restante(s)" else "Letter $letter: ${challenges.size - currentChallengeIndex} left",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = DeepRedDark
                )

                IconButton(
                    onClick = { viewModel.speak(displayWord) },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SunnyYellow.copy(alpha = 0.2f))
                        .border(2.dp, SunnyYellow, RoundedCornerShape(16.dp))
                ) {
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Hear", tint = PapaiOrange)
                }
            }

            if (isLetterB) {
                // --- MODO SÍLABAS ---
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 16.dp)
                        .border(4.dp, PapaiOrange, RoundedCornerShape(32.dp)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = emoji, fontSize = 100.sp, lineHeight = 110.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        val replacedPart = " __ "
                        val formattedWord = displayWord.uppercase().replaceFirst(missingSyllable.uppercase(), replacedPart)

                        Text(
                            text = formattedWord,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = CharcoalKids,
                            letterSpacing = 2.sp
                        )
                    }
                }

                // Botões de Opção
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    options.forEach { option ->
                        Button(
                            onClick = {
                                if (option.uppercase() == missingSyllable.uppercase()) {
                                    viewModel.playSuccessSound()
                                    viewModel.speak(if (language == "PT") "Excelente! Formou a palavra $displayWord!" else "Excellent! You made the word $displayWord!")
                                    
                                    if (currentChallengeIndex < challenges.size - 1) {
                                        currentChallengeIndex++
                                    } else {
                                        letterGameCompleted = true
                                        completedLetters = completedLetters + letter
                                    }
                                } else {
                                    viewModel.playErrorSound()
                                    viewModel.speak(if (language == "PT") "Tente outro pedacinho!" else "Try another block!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SunnyYellow),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .height(76.dp)
                                .width(96.dp)
                                .border(3.dp, PapaiOrange, RoundedCornerShape(20.dp))
                        ) {
                            Text(
                                text = option,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = CharcoalKids
                            )
                        }
                    }
                }
            } else {
                // --- MODO SOLETRAR ---
                val targetWord = displayWord.uppercase()
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 16.dp)
                        .border(4.dp, WatermelonGreen, RoundedCornerShape(32.dp)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = emoji, fontSize = 90.sp, lineHeight = 100.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in targetWord.indices) {
                                val charToDisplay = spelledWordLetters.getOrNull(i)
                                val hasLetter = charToDisplay != null
                                
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (hasLetter) WatermelonGreen else CloudWhite)
                                        .border(2.dp, if (hasLetter) WatermelonGreen else CharcoalKids.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = charToDisplay?.toString() ?: "",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    scrambledLetters.forEachIndexed { index, letterChar ->
                        Button(
                            onClick = {
                                val expectedChar = targetWord[spelledWordLetters.size]
                                if (letterChar == expectedChar) {
                                    viewModel.playSuccessSound()
                                    viewModel.speak(letterChar.toString())
                                    
                                    val newSpelled = spelledWordLetters.toMutableList()
                                    newSpelled.add(letterChar)
                                    spelledWordLetters = newSpelled
                                    
                                    val newScrambled = scrambledLetters.toMutableList()
                                    newScrambled.removeAt(index)
                                    scrambledLetters = newScrambled

                                    if (spelledWordLetters.size == targetWord.length) {
                                        viewModel.speak(if (language == "PT") "Incrível! Você soletrou a palavra $targetWord!" else "Amazing! You spelled the word $targetWord!")
                                        letterGameCompleted = true
                                        completedLetters = completedLetters + letter
                                    }
                                } else {
                                    viewModel.playErrorSound()
                                    viewModel.speak(if (language == "PT") "Essa letra não vai aí agora!" else "That's not the right letter now!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SunnyYellow),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(54.dp)
                                .border(2.dp, PapaiOrange, RoundedCornerShape(12.dp))
                        ) {
                            Text(
                                text = letterChar.toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = CharcoalKids
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = letterGameCompleted) {
                Button(
                    onClick = { selectedLetter = null },
                    colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .border(3.dp, Color.White, RoundedCornerShape(20.dp))
                ) {
                    Text(
                        text = if (language == "PT") "MUITO BEM! VOLTAR AO MENU" else "GREAT JOB! BACK TO MENU",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * FASE ESPECIAL: A Serpente do Alfabeto (Mini-game bônus de tabuleiro)
 */
@Composable
fun AlphabetSnakeGame(viewModel: GameViewModel, phase: Phase) {
    val language by viewModel.appLanguage.collectAsState()
    val solvedIndices by viewModel.snakeSolvedLetters.collectAsState()
    val lettersPool by viewModel.snakePool.collectAsState()

    var activePlaceholderIndex by remember { mutableStateOf<Int?>(null) }

    val isCompleted = solvedIndices.size >= viewModel.snakeMissingIndices.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

        Text(
            text = if (language == "PT") {
                "Complete a serpente encaixando as letras em ordem!"
            } else {
                "Complete the snake fitting the letters in order!"
            },
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalKids,
            textAlign = TextAlign.Center
        )

        // Corpo da Cobra (Grade de bolhas)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(WatermelonGreen.copy(alpha = 0.1f))
                .border(3.dp, WatermelonGreen, RoundedCornerShape(24.dp))
                .padding(8.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 54.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.snakeLetters.size) { index ->
                    val letter = viewModel.snakeLetters[index]
                    val isPlaceholder = viewModel.snakeMissingIndices.contains(index)
                    val solvedLetter = solvedIndices[index]

                    if (isPlaceholder) {
                        val isSelected = activePlaceholderIndex == index
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(if (solvedLetter != null) WatermelonGreen else if (isSelected) SunnyYellow else Color.White)
                                .border(
                                    width = 3.dp,
                                    color = if (solvedLetter != null) Color.White else PapaiOrange,
                                    shape = CircleShape
                                )
                                .clickable {
                                    if (solvedLetter == null) {
                                        activePlaceholderIndex = index
                                        viewModel.speak(if (language == "PT") "Encaixe a letrinha correspondente!" else "Place the corresponding letter!")
                                    }
                                }
                                .testTag("snake_placeholder_$index"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = solvedLetter ?: "?",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = if (solvedLetter != null) Color.White else PapaiOrange
                            )
                        }
                    } else {
                        // Letra normal fixa do alfabeto
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(WatermelonGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Banco de Letras disponíveis para completar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (language == "PT") "Toque em um '?' azul acima e depois na letra certa abaixo:" else "Tap a '?' above, then tap the right letter below:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalKids.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                lettersPool.forEach { letter ->
                    Button(
                        onClick = {
                            val activeIndex = activePlaceholderIndex
                            if (activeIndex != null) {
                                viewModel.solveSnakeLetter(letter, activeIndex)
                                activePlaceholderIndex = null
                            } else {
                                viewModel.speak(if (language == "PT") "Toque primeiro em uma bolha com interrogação!" else "Tap on a question-mark bubble first!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SunnyYellow),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .size(46.dp)
                            .testTag("pool_letter_$letter")
                            .border(2.dp, PapaiOrange, RoundedCornerShape(12.dp)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = letter,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = CharcoalKids
                        )
                    }
                }
            }
        }

        // Próximo
        AnimatedVisibility(visible = isCompleted) {
            Button(
                onClick = { viewModel.completePhase(phase) },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .testTag("snake_next_button")
            ) {
                Text(text = if (language == "PT") "PRÓXIMO!" else "NEXT!", fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

/**
 * DESAFIO DE MATEMÁTICA INTERCALADA: Contagem rápida 1 a 10
 */
@Composable
fun MathIntercalatedScreen(viewModel: GameViewModel, targetPhase: Phase) {
    val language by viewModel.appLanguage.collectAsState()
    val challenge by viewModel.currentMathChallenge.collectAsState()
    val selectedOption by viewModel.mathSelectedOption.collectAsState()
    val completed by viewModel.mathCompleted.collectAsState()

    val prompt = if (language == "PT") challenge?.questionPt else challenge?.questionEn

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(CloudWhite),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Cabeçalho
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🧮 ${if (language == "PT") "Desafio do Papai!" else "Papai's Quiz!"}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = PapaiOrange
            )
        }

        // Pergunta
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .border(3.dp, PapaiOrange, RoundedCornerShape(24.dp))
                .padding(16.dp)
                .clickable { viewModel.speakMathPrompt() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Listen", tint = PapaiOrange, modifier = Modifier.size(32.dp))
                Text(
                    text = prompt ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalKids,
                    lineHeight = 22.sp
                )
            }
        }

        // Contagem Visual de Emojis
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 20.dp)
                .border(3.dp, WatermelonGreen, RoundedCornerShape(32.dp)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Desenha os emojis contáveis com layout responsivo multi-linha para evitar cortes/overflows em telas pequenas
                val count = challenge?.objectCount ?: 1
                val emoji = challenge?.objectEmoji ?: "🍎"

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        for (i in 0 until minOf(count, 4)) {
                            Text(text = emoji, fontSize = 48.sp)
                        }
                    }
                    if (count > 4) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 4 until count) {
                                Text(text = emoji, fontSize = 48.sp)
                            }
                        }
                    }
                }
            }
        }

        // Opções de números gigantes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            challenge?.options?.forEach { option ->
                val isCorrect = option == challenge?.correctAnswer
                val isSelected = selectedOption == option

                Button(
                    onClick = { viewModel.answerMath(option) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected && isCorrect) WatermelonGreen else if (isSelected) WatermelonRed else SunnyYellow
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .size(84.dp)
                        .testTag("math_option_$option")
                        .border(3.dp, PapaiOrange, RoundedCornerShape(24.dp)),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = option.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isSelected) Color.White else CharcoalKids
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botão para avançar ao acertar
        AnimatedVisibility(visible = completed) {
            Button(
                onClick = { viewModel.completePhase(targetPhase) },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .testTag("math_next_button")
            ) {
                Text(text = if (language == "PT") "CONCLUIR!" else "FINISH!", fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

/**
 * TELA DE CELEBRAÇÃO: Videokê Rítmico com legenda saltitante
 */
@Composable
fun CelebrationScreen(viewModel: GameViewModel, phase: Phase) {
    val language by viewModel.appLanguage.collectAsState()
    val activeLine by viewModel.karaokeLyricActiveLine.collectAsState()
    val bouncingWord by viewModel.karaokeBouncingWordIndex.collectAsState()

    // Lyrics correspondendo aos mundos
    val lyricsPt = listOf(
        listOf("A", "E", "I", "O", "U!"),
        listOf("PAPAI", "É", "MUITO", "LEGAL!"),
        listOf("BRINCANDO", "DE", "APRENDER", "GERAL!")
    )
    val lyricsEn = listOf(
        listOf("A", "B", "C", "D", "E!"),
        listOf("PAPAI", "IS", "SO", "FUN!"),
        listOf("LEARNING", "IS", "FOR", "EVERYONE!")
    )

    val currentLyrics = if (language == "PT") lyricsPt else lyricsEn

    // Achar próxima fase na trilha global
    val nextPhase = GameCurriculum.phases.getOrNull(GameCurriculum.phases.indexOf(phase) + 1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PapaiLightOrange, SunnyYellow.copy(alpha = 0.3f))
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Estrelas brilhantes e festa
            Text(
                text = "🎉 ¡PARABÉNS! 🎉",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = WatermelonRed,
                textAlign = TextAlign.Center
            )

            // Papai Cantando no Videokê
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .border(4.dp, PapaiOrange, CircleShape)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_papai_mascot_1783532705143),
                    contentDescription = "Papai Cantando",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Mensagem de Sucesso e Motivação do Papai
            Card(
                colors = CardDefaults.cardColors(containerColor = CharcoalKids),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(3.dp, SunnyYellow, RoundedCornerShape(24.dp))
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Estrelas douradas brilhantes
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = SunnyYellow,
                            modifier = Modifier.size(28.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = SunnyYellow,
                            modifier = Modifier.size(36.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = SunnyYellow,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = if (language == "PT") "✨ VOCÊ CONSEGUIU! ✨" else "✨ YOU DID IT! ✨",
                        color = SunnyYellow,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = if (language == "PT") {
                            "Parabéns! Você completou esse desafio de forma brilhante com o Papai. Continue brilhando e aprendendo!"
                        } else {
                            "Congratulations! You completed this challenge brilliantly with Papai. Keep shining and learning!"
                        },
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            // Ações de Navegação
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botão Gigante de Avançar para Próxima Tarefa
                Button(
                    onClick = {
                        if (nextPhase != null) {
                            viewModel.navigateTo(GameScreen.PhaseInstruction(nextPhase))
                        } else {
                            viewModel.navigateTo(GameScreen.WorldSelection)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .border(3.dp, Color.White, RoundedCornerShape(24.dp))
                        .testTag("celebration_finish_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (nextPhase != null) Icons.Default.PlayArrow else Icons.Default.Check,
                            contentDescription = "Action",
                            tint = Color.White
                        )
                        Text(
                            text = if (nextPhase != null) {
                                if (language == "PT") "PRÓXIMA TAREFA!" else "NEXT TASK!"
                            } else {
                                if (language == "PT") "CONCLUIR JORNADA!" else "COMPLETE JOURNEY!"
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                // Opção alternativa menor para retornar ao mapa/trilha
                if (nextPhase != null) {
                    TextButton(
                        onClick = { viewModel.navigateTo(GameScreen.WorldSelection) },
                        modifier = Modifier.testTag("celebration_back_to_map")
                    ) {
                        Text(
                            text = if (language == "PT") "Voltar para o Mapa" else "Back to Map",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalKids.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * FASE DEDICADA DE MATEMÁTICA: Jogo de contagem de objetos
 */
@Composable
fun MathCountGame(viewModel: GameViewModel, phase: Phase) {
    val language by viewModel.appLanguage.collectAsState()
    val challenge by viewModel.currentMathChallenge.collectAsState()
    val selectedOption by viewModel.mathSelectedOption.collectAsState()
    val completed by viewModel.mathCompleted.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GameHeader(
            viewModel = viewModel,
            phase = phase,
            onBack = { viewModel.navigateTo(GameScreen.WorldSelection) }
        )

        // Pergunta
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .border(3.dp, PapaiOrange, RoundedCornerShape(24.dp))
                .padding(16.dp)
                .clickable { viewModel.speakMathPrompt() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Listen", tint = PapaiOrange, modifier = Modifier.size(32.dp))
                Text(
                    text = if (language == "PT") challenge?.questionPt ?: "" else challenge?.questionEn ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalKids,
                    lineHeight = 22.sp
                )
            }
        }

        // Área de Contagem Visual de Emojis
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .border(3.dp, WatermelonGreen, RoundedCornerShape(32.dp)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val count = challenge?.objectCount ?: 1
                val emoji = challenge?.objectEmoji ?: "🍎"

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until minOf(count, 4)) {
                            Text(text = emoji, fontSize = 60.sp)
                        }
                    }
                    if (count > 4) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 4 until count) {
                                Text(text = emoji, fontSize = 60.sp)
                            }
                        }
                    }
                }
            }
        }

        // Opções numéricas gigantes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            challenge?.options?.forEach { option ->
                val isCorrect = option == challenge?.correctAnswer
                val isSelected = selectedOption == option

                Button(
                    onClick = { viewModel.answerMath(option) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected && isCorrect) WatermelonGreen else if (isSelected) WatermelonRed else SunnyYellow
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .size(84.dp)
                        .testTag("math_phase_option_$option")
                        .border(3.dp, PapaiOrange, RoundedCornerShape(24.dp)),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = option.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isSelected) Color.White else CharcoalKids
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para avançar
        AnimatedVisibility(
            visible = completed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = { viewModel.completePhase(phase) },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .border(3.dp, Color.White, RoundedCornerShape(24.dp))
                    .testTag("math_phase_next_button")
            ) {
                Text(
                    text = if (language == "PT") "PRÓXIMO!" else "NEXT!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * FASE DESAFIO DE LEITURA FINAL: Para a criança ler um texto com o Papai
 */
@Composable
fun FinalReadingGame(viewModel: GameViewModel, phase: Phase) {
    val language by viewModel.appLanguage.collectAsState()
    val highlightedWordIndex by viewModel.highlightedReadingWordIndex.collectAsState()

    val fullTextPt = "O Papai é o jacaré fofo. Ele ama ler e brincar. Papai joga bola e estuda bastante. Agora o Papai e você são melhores amigos!"
    val fullTextEn = "Papai is the cute alligator. He loves to read and play. Papai plays ball and studies a lot. Now Papai and you are best friends!"
    val fullText = if (language == "PT") fullTextPt else fullTextEn

    val linesPt = listOf(
        listOf("O", "Papai", "é", "o", "jacaré", "fofo."),
        listOf("Ele", "ama", "ler", "e", "brincar."),
        listOf("Papai", "joga", "bola", "e", "estuda", "bastante."),
        listOf("Agora", "o", "Papai", "e", "você", "são", "melhores", "amigos!")
    )

    val linesEn = listOf(
        listOf("Papai", "is", "the", "cute", "alligator."),
        listOf("He", "loves", "to", "read", "and", "play."),
        listOf("Papai", "plays", "ball", "and", "studies", "a", "lot."),
        listOf("Now", "Papai", "and", "you", "are", "best", "friends!")
    )

    val lines = if (language == "PT") linesPt else linesEn

    val wordWithIndexLines = remember(language) {
        var count = 0
        lines.map { line ->
            line.map { word ->
                val pair = Pair(word, count)
                count++
                pair
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GameHeader(
            viewModel = viewModel,
            phase = phase,
            onBack = { viewModel.navigateTo(GameScreen.WorldSelection) }
        )

        // Balão de instrução falante do Papai
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .border(3.dp, PapaiOrange, RoundedCornerShape(24.dp))
                .padding(16.dp)
                .clickable { viewModel.speakInstruction(phase) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Help",
                    tint = PapaiOrange,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = if (language == "PT") 
                        "Toque nas palavras para ler com o Papai! Ou toque no botão laranja para ouvir a historinha toda!" 
                    else 
                        "Tap the words to read with Papai! Or tap the orange button to hear the whole story!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalKids,
                    lineHeight = 20.sp
                )
            }
        }

        // Livrinho de Leitura Digital
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .border(3.dp, SunnyYellow, RoundedCornerShape(32.dp)),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Botão Ouvir Historinha Inteira (com realce de karaoke)
                Button(
                    onClick = { viewModel.speakFullReadingText(fullText) },
                    colors = ButtonDefaults.buttonColors(containerColor = PapaiOrange),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .height(54.dp)
                        .border(2.dp, DeepRedDark, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Play", tint = Color.White)
                        Text(
                            text = if (language == "PT") "OUVIR HISTORINHA" else "HEAR STORY",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                // Corpo do texto interativo
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    wordWithIndexLines.forEach { line ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            line.forEach { (word, idx) ->
                                val isHighlighted = idx == highlightedWordIndex
                                val scale by animateFloatAsState(if (isHighlighted) 1.25f else 1.0f)
                                val textColor = if (isHighlighted) Color.White else CharcoalKids
                                val bgColor = if (isHighlighted) WatermelonRed else Color.Transparent
                                val borderColor = if (isHighlighted) DeepRedDark else Color.Transparent

                                Box(
                                    modifier = Modifier
                                        .graphicsLayer(scaleX = scale, scaleY = scale)
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(bgColor)
                                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                                        .clickable { 
                                            viewModel.speakReadingWord(word, idx) 
                                        }
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = word,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                        color = textColor,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Incentivo do Papai
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "📖",
                        fontSize = 32.sp
                    )
                    Text(
                        text = if (language == "PT") "Você está lendo muito bem!" else "You are reading so well!",
                        style = MaterialTheme.typography.labelLarge,
                        color = PapaiOrange,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // Botão para avançar e celebrar
        Button(
            onClick = { viewModel.completePhase(phase) },
            colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .height(60.dp)
                .border(3.dp, Color.White, RoundedCornerShape(24.dp))
                .testTag("reading_phase_next_button")
        ) {
            Text(
                text = if (language == "PT") "CONCLUIR MISSÃO!" else "FINISH MISSION!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

/**
 * FASE DESAFIO DE NÚMEROS (Ordenação de Trem)
 */
@Composable
fun NumberSequenceGame(viewModel: GameViewModel, phase: Phase) {
    val language by viewModel.appLanguage.collectAsState()
    
    // Números a ordenar
    val correctOrder = listOf(1, 2, 3, 4, 5)
    var scrambledNumbers by remember { mutableStateOf(correctOrder.shuffled()) }
    var placedNumbers by remember { mutableStateOf(listOf<Int>()) }
    val isCompleted = placedNumbers.size == correctOrder.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

        Text(
            text = if (language == "PT") "COLOQUE OS NÚMEROS EM ORDEM CRESCENTE!" else "PUT THE NUMBERS IN ASCENDING ORDER!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = CharcoalKids,
            textAlign = TextAlign.Center
        )

        // Trem com os Vagões
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp)
                .border(3.dp, SkyBlue, RoundedCornerShape(24.dp)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Desenho da Locomotiva 🚂
                Text(
                    text = "🚂",
                    fontSize = 80.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Vagões [ 1 ] [ 2 ] ...
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in correctOrder.indices) {
                        val numberInSlot = placedNumbers.getOrNull(i)
                        val isFilled = numberInSlot != null
                        
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isFilled) SkyBlue else CloudWhite)
                                .border(3.dp, if (isFilled) SkyBlue else CharcoalKids.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = numberInSlot?.toString() ?: "?",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isFilled) Color.White else CharcoalKids.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }
        }

        // Balões com números para tocar abaixo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            scrambledNumbers.forEach { num ->
                Button(
                    onClick = {
                        val nextExpected = correctOrder[placedNumbers.size]
                        if (num == nextExpected) {
                            viewModel.playSuccessSound()
                            viewModel.speak(num.toString())
                            
                            val newPlaced = placedNumbers.toMutableList()
                            newPlaced.add(num)
                            placedNumbers = newPlaced
                            
                            val newScrambled = scrambledNumbers.toMutableList()
                            newScrambled.remove(num)
                            scrambledNumbers = newScrambled

                            if (placedNumbers.size == correctOrder.size) {
                                viewModel.speak(if (language == "PT") "Incrível! Trem organizado!" else "Amazing! Train organized!")
                            }
                        } else {
                            viewModel.playErrorSound()
                            viewModel.speak(if (language == "PT") "Esse não é o próximo número!" else "That's not the next number!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SunnyYellow),
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(60.dp)
                        .border(3.dp, PapaiOrange, CircleShape)
                ) {
                    Text(
                        text = num.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = CharcoalKids
                    )
                }
            }
        }

        // Avançar
        AnimatedVisibility(visible = isCompleted) {
            Button(
                onClick = { viewModel.completePhase(phase) },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .border(3.dp, Color.White, RoundedCornerShape(20.dp))
            ) {
                Text(
                    text = if (language == "PT") "PRÓXIMO!" else "NEXT!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * FASE MUNDO 4: DIAS DA SEMANA, MESES E ESTAÇÕES
 */
@Composable
fun DaysMonthsSeasonsGame(viewModel: GameViewModel, phase: Phase) {
    val language by viewModel.appLanguage.collectAsState()

    when (phase.id) {
        "4.6" -> {
            // DIAS DA SEMANA (Order Game)
            val daysPt = listOf("Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado")
            val daysEn = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
            val correctOrder = if (language == "PT") daysPt else daysEn

            var scrambledDays by remember { mutableStateOf(correctOrder.shuffled()) }
            var placedDays by remember { mutableStateOf(listOf<String>()) }
            val isCompleted = placedDays.size == correctOrder.size

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

                Text(
                    text = if (language == "PT") "ORDENE OS DIAS DA SEMANA DE DOMINGO A SÁBADO!" else "ORDER THE DAYS FROM SUNDAY TO SATURDAY!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = CharcoalKids,
                    textAlign = TextAlign.Center
                )

                // Trilhos semanais
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 12.dp)
                        .border(3.dp, LavenderSoft, RoundedCornerShape(24.dp)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text(text = "📅 SEMANA / WEEK", fontWeight = FontWeight.Bold, color = LavenderSoft, fontSize = 12.sp)
                        }
                        
                        items(correctOrder.size) { i ->
                            val dayPlaced = placedDays.getOrNull(i)
                            val isFilled = dayPlaced != null
                            
                            Box(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isFilled) LavenderSoft else CloudWhite)
                                        .border(2.dp, if (isFilled) LavenderSoft else CharcoalKids.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayPlaced ?: (if (language == "PT") "Dia ${i + 1}" else "Day ${i + 1}"),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isFilled) Color.White else CharcoalKids.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }

                // Botões de dias embaralhados
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    scrambledDays.chunked(3).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            row.forEach { day ->
                                Button(
                                    onClick = {
                                        val expected = correctOrder[placedDays.size]
                                        if (day == expected) {
                                            viewModel.playSuccessSound()
                                            viewModel.speak(day)
                                            
                                            val newPlaced = placedDays.toMutableList()
                                            newPlaced.add(day)
                                            placedDays = newPlaced
                                            
                                            val newScrambled = scrambledDays.toMutableList()
                                            newScrambled.remove(day)
                                            scrambledDays = newScrambled
                                        } else {
                                            viewModel.playErrorSound()
                                            viewModel.speak(if (language == "PT") "Este não é o próximo dia!" else "That is not the next day!")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SunnyYellow),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .height(46.dp)
                                        .border(2.dp, PapaiOrange, RoundedCornerShape(14.dp))
                                ) {
                                    Text(text = day, fontSize = 13.sp, color = CharcoalKids, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                AnimatedVisibility(visible = isCompleted) {
                    Button(
                        onClick = { viewModel.completePhase(phase) },
                        colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .border(3.dp, Color.White, RoundedCornerShape(20.dp))
                    ) {
                        Text(text = if (language == "PT") "PRÓXIMO!" else "NEXT!", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    }
                }
            }
        }
        "4.7" -> {
            // MESES DO ANO (Calendar Quiz)
            var quizIndex by remember { mutableStateOf(0) }
            val correctMonthPt = listOf("Janeiro", "Fevereiro", "Junho", "Dezembro")
            val correctMonthEn = listOf("January", "February", "June", "December")
            
            val promptsPt = listOf(
                "Qual é o primeiro mês do ano?",
                "Qual mês tem apenas 28 ou 29 dias?",
                "Em qual mês celebramos as festas juninas?",
                "Qual é o último mês do ano, onde celebramos o Natal?"
            )
            val promptsEn = listOf(
                "Which is the first month of the year?",
                "Which month has only 28 or 29 days?",
                "In which month do we celebrate midsummer bonfires?",
                "Which is the last month of the year, when we celebrate Christmas?"
            )
            
            val optionsPt = listOf(
                listOf("Janeiro", "Março", "Maio"),
                listOf("Abril", "Fevereiro", "Julho"),
                listOf("Setembro", "Junho", "Outubro"),
                listOf("Novembro", "Dezembro", "Agosto")
            )
            val optionsEn = listOf(
                listOf("January", "March", "May"),
                listOf("April", "February", "July"),
                listOf("September", "June", "October"),
                listOf("November", "December", "August")
            )

            val isCompleted = quizIndex >= promptsPt.size

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

                if (!isCompleted) {
                    Text(
                        text = if (language == "PT") "DESCUBRA O MÊS DO ANO!" else "DISCOVER THE MONTH OF THE YEAR!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = CharcoalKids,
                        textAlign = TextAlign.Center
                    )

                    // Card da pergunta
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 16.dp)
                            .border(3.dp, LavenderSoft, RoundedCornerShape(24.dp)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "📅", fontSize = 80.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (language == "PT") promptsPt[quizIndex] else promptsEn[quizIndex],
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalKids,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Opções de meses abaixo
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val currentOptions = if (language == "PT") optionsPt[quizIndex] else optionsEn[quizIndex]
                        val correctMonth = if (language == "PT") correctMonthPt[quizIndex] else correctMonthEn[quizIndex]

                        currentOptions.forEach { opt ->
                            Button(
                                onClick = {
                                    if (opt == correctMonth) {
                                        viewModel.playSuccessSound()
                                        viewModel.speak(opt)
                                        quizIndex++
                                    } else {
                                        viewModel.playErrorSound()
                                        viewModel.speak(if (language == "PT") "Tente novamente!" else "Try again!")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SunnyYellow),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .border(2.dp, PapaiOrange, RoundedCornerShape(20.dp))
                            ) {
                                Text(text = opt, fontSize = 18.sp, color = CharcoalKids, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    // Quiz concluído
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🏆🎉", fontSize = 90.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (language == "PT") "Incrível! Você conhece todos os meses!" else "Awesome! You know all the months!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = DeepRedDark,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.completePhase(phase) },
                        colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(3.dp, Color.White, RoundedCornerShape(20.dp))
                    ) {
                        Text(text = if (language == "PT") "PRÓXIMO!" else "NEXT!", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    }
                }
            }
        }
        else -> {
            // ESTAÇÕES DO ANO (Matching Game)
            val elementsPt = listOf("Sol e Praia ☀️🏖️", "Boneco de Neve ⛄❄️", "Flores Coloridas 🌸🌻", "Folhas Caindo 🍂🍁")
            val elementsEn = listOf("Sun & Beach ☀️🏖️", "Snowman ⛄❄️", "Colorful Flowers 🌸🌻", "Falling Leaves 🍁🍂")
            
            val correctSeasonPt = listOf("Verão", "Inverno", "Primavera", "Outono")
            val correctSeasonEn = listOf("Summer", "Winter", "Spring", "Autumn")

            var matchIndex by remember { mutableStateOf(0) }
            val isCompleted = matchIndex >= elementsPt.size

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameHeader(viewModel, phase) { viewModel.navigateTo(GameScreen.WorldSelection) }

                if (!isCompleted) {
                    Text(
                        text = if (language == "PT") "COLOQUE O DESENHO NA ESTAÇÃO DO ANO CERTA!" else "MATCH THE PICTURE TO THE RIGHT SEASON!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = CharcoalKids,
                        textAlign = TextAlign.Center
                    )

                    // Desenho flutuante ativo
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 16.dp)
                            .border(3.dp, PapaiOrange, RoundedCornerShape(24.dp)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (language == "PT") elementsPt[matchIndex] else elementsEn[matchIndex],
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = CharcoalKids,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Botões das Estações
                    val seasonsList = if (language == "PT") listOf("Primavera", "Verão", "Outono", "Inverno") else listOf("Spring", "Summer", "Autumn", "Winter")

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        seasonsList.forEach { season ->
                            Button(
                                onClick = {
                                    val correctSeason = if (language == "PT") correctSeasonPt[matchIndex] else correctSeasonEn[matchIndex]
                                    if (season == correctSeason) {
                                        viewModel.playSuccessSound()
                                        viewModel.speak(season)
                                        matchIndex++
                                    } else {
                                        viewModel.playErrorSound()
                                        viewModel.speak(if (language == "PT") "Este desenho não combina com essa estação!" else "This picture doesn't match that season!")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SunnyYellow),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .border(2.dp, PapaiOrange, RoundedCornerShape(20.dp))
                            ) {
                                Text(text = season, fontSize = 18.sp, color = CharcoalKids, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    // Match concluído
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🌸☀️🍂❄️", fontSize = 80.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (language == "PT") "Incrível! Você domina as Quatro Estações!" else "Excellent! You master the Four Seasons!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = DeepRedDark,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.completePhase(phase) },
                        colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(3.dp, Color.White, RoundedCornerShape(20.dp))
                    ) {
                        Text(text = if (language == "PT") "PRÓXIMO!" else "NEXT!", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceSelectorDialog(viewModel: GameViewModel) {
    val showDialog by viewModel.showVoiceSelectorDialog.collectAsState()
    if (!showDialog) return

    val voices by viewModel.availableVoices.collectAsState()
    val selectedVoice by viewModel.selectedVoiceName.collectAsState()
    val language by viewModel.appLanguage.collectAsState()

    AlertDialog(
        onDismissRequest = { viewModel.showVoiceSelectorDialog.value = false },
        title = {
            Text(
                text = if (language == "PT") "🎙️ Escolha a Voz do Papai" else "🎙️ Choose Papai's Voice",
                fontWeight = FontWeight.Black,
                color = DeepRedDark,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (language == "PT") {
                        "Escolha o seu tom de voz favorito para brincar com o Papai! Você pode selecionar vozes masculinas, femininas ou locais do seu aparelho."
                    } else {
                        "Choose your favorite voice to play with Papai! You can select male, female, or local voices from your device."
                    },
                    fontSize = 14.sp,
                    color = CharcoalKids.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (voices.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == "PT") "Buscando vozes no sistema..." else "Searching for system voices...",
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 280.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Opção de Voz Padrão do Sistema
                        item {
                            val isDefaultSelected = selectedVoice == null
                            Card(
                                onClick = { viewModel.selectVoice(null) },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isDefaultSelected) SunnyYellow.copy(alpha = 0.15f) else SoftPinkContainer
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = if (isDefaultSelected) 2.dp else 1.dp,
                                        color = if (isDefaultSelected) SunnyYellow else Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = if (language == "PT") "✨ Voz Padrão do Papai" else "✨ Default Papai Voice",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = CharcoalKids
                                        )
                                        Text(
                                            text = if (language == "PT") "Tom de voz masculino padrão" else "Default friendly male voice",
                                            fontSize = 12.sp,
                                            color = CharcoalKids.copy(alpha = 0.6f)
                                        )
                                    }
                                    if (isDefaultSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = DeepRedDark,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }

                        items(voices) { voiceOption ->
                            val isSelected = selectedVoice == voiceOption.name
                            Card(
                                onClick = { viewModel.selectVoice(voiceOption.name) },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) SunnyYellow.copy(alpha = 0.15f) else SoftPinkContainer
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) SunnyYellow else Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = voiceOption.displayName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = CharcoalKids
                                        )
                                        Text(
                                            text = voiceOption.name,
                                            fontSize = 10.sp,
                                            color = CharcoalKids.copy(alpha = 0.5f),
                                            maxLines = 1
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = DeepRedDark,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.showVoiceSelectorDialog.value = false },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (language == "PT") "CONFIRMAR" else "CONFIRM",
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = SoftPinkBg
    )
}

@Composable
fun BadgeCelebrationDialog(viewModel: GameViewModel) {
    val badge by viewModel.newlyUnlockedBadge.collectAsState()
    val language by viewModel.appLanguage.collectAsState()

    val currentBadge = badge ?: return

    AlertDialog(
        onDismissRequest = { viewModel.newlyUnlockedBadge.value = null },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉 " + (if (language == "PT") "NOVA MEDALHA!" else "NEW MEDAL!") + " 🎉",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = PrimaryRed,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Giant Animated/Glowing Circle with the Badge Emoji
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(SunnyYellow.copy(alpha = 0.25f))
                        .border(3.dp, SunnyYellow, CircleShape)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentBadge.emoji,
                        fontSize = 54.sp
                    )
                }

                Text(
                    text = if (language == "PT") currentBadge.titlePt else currentBadge.titleEn,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = DeepRedDark,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (language == "PT") currentBadge.descriptionPt else currentBadge.descriptionEn,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalKids,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Text(
                    text = if (language == "PT") "Ganhou +100 Pontos de Experiência!" else "Earned +100 Experience Points!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = WatermelonGreen,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.newlyUnlockedBadge.value = null },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(20.dp))
            ) {
                Text(
                    text = if (language == "PT") "EBA! CONSEGUI! 🌟" else "YAY! I DID IT! 🌟",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        },
        shape = RoundedCornerShape(32.dp),
        containerColor = Color.White,
        modifier = Modifier.border(4.dp, SunnyYellow, RoundedCornerShape(32.dp))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarSelectionScreen(viewModel: GameViewModel) {
    val language by viewModel.appLanguage.collectAsState()
    val selectedAvatarId by viewModel.selectedAvatarId.collectAsState()
    val savedNickname by viewModel.childNickname.collectAsState()

    var nickname by remember { mutableStateOf(savedNickname) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SoftPinkBg.copy(alpha = 0.3f), CloudWhite)
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header Row with Back Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (savedNickname.isNotEmpty()) {
                            viewModel.navigateTo(GameScreen.WorldSelection)
                        } else {
                            viewModel.navigateTo(GameScreen.SplashIntro)
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SoftPinkContainer)
                        .border(1.5.dp, DeepRedDark, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = DeepRedDark,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = if (language == "PT") "CONFIGURAR PERFIL" else "PROFILE SETTINGS",
                        style = MaterialTheme.typography.labelMedium,
                        color = SubtitleRed,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = if (language == "PT") "Crie o seu Herói" else "Create Your Hero",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = DeepRedDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Scrollable Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Interactive Header Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(3.dp, DeepRedDark, RoundedCornerShape(24.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "✨ " + (if (language == "PT") "Quem é você na Trilha?" else "Who are you on the Trail?") + " ✨",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = DeepRedDark,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = if (language == "PT") {
                            "Escolha um personagem amiguinho e digite seu apelido preferido para começar a aventura!"
                        } else {
                            "Choose a friendly character and type your favorite nickname to start the adventure!"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalKids.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }

                // Nickname Input Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(3.dp, DeepRedDark, RoundedCornerShape(24.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (language == "PT") "Como devemos te chamar? ✍️" else "What should we call you? ✍️",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = DeepRedDark
                    )

                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { if (it.length <= 15) nickname = it },
                        placeholder = {
                            Text(
                                text = if (language == "PT") "Ex: Joãozinho, Dudinha..." else "Ex: Joey, Lily...",
                                color = Color.Gray.copy(alpha = 0.6f)
                            )
                        },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Black, color = CharcoalKids),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("child_nickname_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryRed,
                            unfocusedBorderColor = DeepRedDark.copy(alpha = 0.3f),
                            focusedContainerColor = SoftPinkBg.copy(alpha = 0.15f),
                            unfocusedContainerColor = SoftPinkBg.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }

                // Characters Label
                Text(
                    text = if (language == "PT") "Escolha seu Personagem 🌟" else "Choose Your Character 🌟",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = DeepRedDark,
                    modifier = Modifier.align(Alignment.Start)
                )

                // Render all 6 colorful character cards
                GameAvatars.avatars.forEach { avatar ->
                    val isSelected = selectedAvatarId == avatar.id
                    val themeColor = Color(android.graphics.Color.parseColor(avatar.colorHex))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectAvatar(avatar.id) }
                            .border(
                                width = if (isSelected) 3.5.dp else 1.5.dp,
                                color = if (isSelected) PrimaryRed else DeepRedDark.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .testTag("avatar_card_${avatar.id}"),
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) themeColor else Color.White),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Emoji circular frame
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(2.5.dp, if (isSelected) PrimaryRed else Color.LightGray.copy(alpha = 0.5f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = avatar.emoji, fontSize = 34.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == "PT") avatar.namePt else avatar.nameEn,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = DeepRedDark
                                )
                                Text(
                                    text = if (language == "PT") avatar.descriptionPt else avatar.descriptionEn,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CharcoalKids.copy(alpha = 0.75f),
                                    lineHeight = 16.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            // Selection Radio Indicator
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) PrimaryRed else Color.LightGray.copy(alpha = 0.3f))
                                    .border(2.dp, if (isSelected) Color.White else Color.Gray.copy(alpha = 0.5f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm / Save Action Button
            Button(
                onClick = {
                    // Safe Fallback: If nickname is empty, auto-fill with the character's first word of the name
                    var finalNickname = nickname.trim()
                    if (finalNickname.isEmpty()) {
                        val activeAvatar = GameAvatars.getAvatarById(selectedAvatarId)
                        finalNickname = if (language == "PT") {
                            activeAvatar.namePt.split(",").first().trim()
                        } else {
                            activeAvatar.nameEn.split(" ").first().trim()
                        }
                    }
                    viewModel.updateChildNickname(finalNickname)

                    // Speak sweet welcoming message from active character
                    val activeAvatar = GameAvatars.getAvatarById(selectedAvatarId)
                    val congratulationsSpeak = if (language == "PT") {
                        "Muito bem, $finalNickname! Agora eu sou o seu amiguinho de aventuras! Vamos começar!"
                    } else {
                        "Awesome, $finalNickname! Now I am your adventure buddy! Let's get started!"
                    }
                    viewModel.speak(congratulationsSpeak)

                    // Navigate to WorldSelection
                    viewModel.navigateTo(GameScreen.WorldSelection)
                },
                colors = ButtonDefaults.buttonColors(containerColor = WatermelonGreen),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .border(3.dp, Color.White, RoundedCornerShape(24.dp))
                    .testTag("avatar_confirm_button"),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (language == "PT") "PRONTO, VAMOS COMEÇAR! 🚀" else "READY, LET'S GO! 🚀",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SoundtrackSelectorDialog(viewModel: GameViewModel) {
    val showDialog by viewModel.showSoundtrackDialog.collectAsState()
    val language by viewModel.appLanguage.collectAsState()

    if (!showDialog) return

    val audioPlayer = viewModel.proceduralAudio
    val isEnabled by audioPlayer.isMusicEnabled.collectAsState()
    val currentTrack by audioPlayer.currentTrack.collectAsState()
    val volume by audioPlayer.musicVolume.collectAsState()

    AlertDialog(
        onDismissRequest = { viewModel.showSoundtrackDialog.value = false },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎵 " + (if (language == "PT") "TRILHA SONORA" else "BACKGROUND MUSIC") + " 🎵",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = DeepRedDark,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Background Music Toggle Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SoftPinkBg.copy(alpha = 0.2f))
                        .clickable { audioPlayer.toggleMusic() }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (isEnabled) "🔊" else "🔇",
                            fontSize = 24.sp
                        )
                        Column {
                            Text(
                                text = if (language == "PT") "Música de Fundo" else "Background Music",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = CharcoalKids
                            )
                            Text(
                                text = if (language == "PT") {
                                    if (isEnabled) "Ativada" else "Desativada"
                                } else {
                                    if (isEnabled) "Enabled" else "Disabled"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isEnabled) WatermelonGreen else Color.Gray
                            )
                        }
                    }

                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { audioPlayer.toggleMusic() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = WatermelonGreen,
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.LightGray.copy(alpha = 0.4f)
                        )
                    )
                }

                if (isEnabled) {
                    // Volume Control Slider
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (language == "PT") "Volume da Música" else "Music Volume",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalKids.copy(alpha = 0.7f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "🔈", fontSize = 16.sp)
                            Slider(
                                value = volume,
                                onValueChange = { audioPlayer.setVolume(it) },
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = PrimaryRed,
                                    activeTrackColor = PrimaryRed,
                                    inactiveTrackColor = SoftPinkBg
                                )
                            )
                            Text(text = "🔊", fontSize = 16.sp)
                        }
                    }

                    // Track Selection Header
                    Text(
                        text = if (language == "PT") "Escolha uma Melodia 🎶" else "Choose a Melody 🎶",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = DeepRedDark
                    )

                    // Three beautiful tracks to choose from
                    SoundtrackType.values().forEach { track ->
                        val isSelected = currentTrack == track
                        val emoji = when (track) {
                            SoundtrackType.HAPPY -> "🎷"
                            SoundtrackType.CLOUD -> "☁️"
                            SoundtrackType.STAR -> "✨"
                        }
                        val desc = when (track) {
                            SoundtrackType.HAPPY -> if (language == "PT") "Alegre e saltitante!" else "Cheerful and bouncy!"
                            SoundtrackType.CLOUD -> if (language == "PT") "Calma e relaxante!" else "Calm and relaxing!"
                            SoundtrackType.STAR -> if (language == "PT") "Mágica e brilhante!" else "Magical and sparkling!"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) SunnyYellow.copy(alpha = 0.25f) else Color.Transparent)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) SunnyYellow else Color.LightGray.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { audioPlayer.setTrack(track) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) SunnyYellow.copy(alpha = 0.3f) else SoftPinkBg.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 20.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == "PT") track.titlePt else track.titleEn,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = DeepRedDark
                                )
                                Text(
                                    text = desc,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CharcoalKids.copy(alpha = 0.7f)
                                )
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(WatermelonGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Informative text when background music is off
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Gray.copy(alpha = 0.05f))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == "PT") {
                                "Ligue a música de fundo acima para escutar canções divertidas!"
                            } else {
                                "Turn on the background music above to listen to fun songs!"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.showSoundtrackDialog.value = false },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(20.dp))
            ) {
                Text(
                    text = if (language == "PT") "OK, VOLTAR! 🌟" else "OK, BACK! 🌟",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        },
        shape = RoundedCornerShape(28.dp),
        containerColor = Color.White,
        modifier = Modifier.border(3.dp, PrimaryRed, RoundedCornerShape(28.dp))
    )
}

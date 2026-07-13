package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.GameScreen
import com.example.ui.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: GameViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (val screen = currentScreen) {
                            is GameScreen.SplashIntro -> SplashIntroScreen(viewModel)
                            is GameScreen.AvatarSelection -> AvatarSelectionScreen(viewModel)
                            is GameScreen.WorldSelection -> WorldSelectionScreen(viewModel)
                            is GameScreen.PhaseInstruction -> PhaseInstructionScreen(viewModel, screen.phase)
                            is GameScreen.PhaseGame -> PhaseGameScreen(viewModel, screen.phase)
                            is GameScreen.MathIntercalated -> MathIntercalatedScreen(viewModel, screen.targetPhase)
                            is GameScreen.Celebration -> CelebrationScreen(viewModel, screen.phase)
                        }

                        // Diálogo centralizado para seleção personalizada de vozes do Papai
                        VoiceSelectorDialog(viewModel)

                        // Diálogo centralizado para celebração festiva de novas medalhas
                        BadgeCelebrationDialog(viewModel)

                        // Diálogo centralizado para seleção de trilha sonora
                        SoundtrackSelectorDialog(viewModel)
                    }
                }
            }
        }
    }
}

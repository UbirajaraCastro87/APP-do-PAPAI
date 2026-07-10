package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.ToneGenerator
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class VoiceOption(
    val name: String,
    val isMale: Boolean,
    val isNetworkRequired: Boolean,
    val displayName: String
)

/**
 * Diferentes telas do aplicativo que garantem uma navegação fluida sem complexidades extras.
 */
sealed class GameScreen {
    object SplashIntro : GameScreen()
    object WorldSelection : GameScreen()
    data class PhaseInstruction(val phase: Phase) : GameScreen()
    data class PhaseGame(val phase: Phase) : GameScreen()
    data class MathIntercalated(val targetPhase: Phase) : GameScreen()
    data class Celebration(val phase: Phase) : GameScreen()
}

class GameViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val database = AppDatabase.getDatabase(application)
    private val repository = GameRepository(database.progressDao())

    // Estado global de progresso carregado do banco
    val userProgress: StateFlow<List<UserProgress>> = repository.allProgress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Idioma ativo: "PT" (Português) ou "EN" (Inglês)
    val appLanguage = MutableStateFlow("PT")

    // Configuração de áudio do assistente (Text-to-Speech) ativado/desativado
    val isAssistantAudioEnabled = MutableStateFlow(true)

    fun toggleAssistantAudio() {
        isAssistantAudioEnabled.value = !isAssistantAudioEnabled.value
        if (!isAssistantAudioEnabled.value) {
            tts?.stop() // Parar áudio imediatamente se for desativado
        }
    }

    // Tela de navegação atual
    val currentScreen = MutableStateFlow<GameScreen>(GameScreen.SplashIntro)

    // Gerenciador TTS nativo do Android
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private var isMaleVoiceSelected = false

    // Estado da voz selecionada pelo usuário
    val selectedVoiceName = MutableStateFlow<String?>(
        application.getSharedPreferences("papai_game_prefs", Context.MODE_PRIVATE)
            .getString("selected_voice_name", null)
    )

    // Lista de vozes disponíveis filtradas por idioma
    val availableVoices = MutableStateFlow<List<VoiceOption>>(emptyList())

    // Controla a visibilidade do diálogo de seleção de vozes
    val showVoiceSelectorDialog = MutableStateFlow(false)

    // Gerador de sons e efeitos sonoros nativos
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    // Estados dos Mini-games ativos
    val vowelMatchIndex = MutableStateFlow(0) // Índice da pergunta do som inicial/final
    val vowelMatchCompleted = MutableStateFlow(false)

    // Balões de fala correspondidos na fase 2.3
    val balloonMatches = MutableStateFlow<Map<String, String>>(emptyMap()) // balloonText to targetId

    // Estado da Sílaba ativa no mundo 3
    val syllableChallengeIndex = MutableStateFlow(0)
    val syllableChallengeCompleted = MutableStateFlow(false)

    // Estado do mini-game da Serpente do Alfabeto
    // Cobra de letras com alguns vazios: A, [B], C, D, [E], F, G, [H], I, J...
    val snakeLetters = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    val snakeMissingIndices = listOf(1, 4, 7, 11, 15, 19, 22) // B, E, H, L, P, T, W
    val snakeSolvedLetters = MutableStateFlow<Map<Int, String>>(emptyMap()) // index to letter placed
    val snakePool = MutableStateFlow<List<String>>(emptyList())

    // Estado do desafio matemático intercalado
    val currentMathChallenge = MutableStateFlow<MathChallenge?>(null)
    val mathSelectedOption = MutableStateFlow<Int?>(null)
    val mathCompleted = MutableStateFlow(false)

    // Estados do Desafio de Leitura Final (Fase 3.4)
    val highlightedReadingWordIndex = MutableStateFlow(-1)
    val readingCompleted = MutableStateFlow(false)

    // Lyrics de celebração para o videokê
    val karaokeLyricActiveLine = MutableStateFlow(0)
    val karaokeBouncingWordIndex = MutableStateFlow(0)

    init {
        // Garante que o volume de mídia está ativo e audível
        try {
            val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (currentVol < (maxVol * 0.6).toInt()) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    (maxVol * 0.75).toInt(),
                    0
                )
            }
        } catch (e: Exception) {
            Log.e("PapaiGame", "Não foi possível ajustar o volume: ${e.message}")
        }

        // Inicializa o TTS nativo com suporte para attribution context a fim de satisfazer exigências do sistema / AppOps no Android 11+
        val contextWithAttribution = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            try {
                application.createAttributionContext("speech")
            } catch (e: Exception) {
                application
            }
        } else {
            application
        }
        tts = TextToSpeech(contextWithAttribution, this)
        resetSnakeGame()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Configura os atributos de áudio para o TTS direcionar ao canal de mídia/acessibilidade de forma otimizada
            try {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
                tts?.setAudioAttributes(audioAttributes)
            } catch (e: Exception) {
                Log.e("PapaiGame", "Erro ao configurar AudioAttributes do TTS: ${e.message}")
            }

            updateTtsLanguage()
            ttsReady = true
            Log.d("PapaiGame", "TTS Inicializado com sucesso!")
            // Fala introdução inicial
            speakPapaiIntroduction()
        } else {
            Log.e("PapaiGame", "Falha ao inicializar o TTS. Status: $status")
        }
    }

    fun toggleLanguage() {
        val next = if (appLanguage.value == "PT") "EN" else "PT"
        appLanguage.value = next
        updateTtsLanguage()
        // Fala olá de acordo com idioma
        if (next == "PT") {
            speak("Olá amiguinho! Agora vamos jogar em Português!")
        } else {
            speak("Hello friend! Let's play in English now!")
        }
    }

    private fun updateTtsLanguage() {
        tts?.let {
            val isPt = appLanguage.value == "PT"
            val locale = if (isPt) {
                Locale("pt", "BR")
            } else {
                Locale.US
            }

            val result = it.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("PapaiGame", "Idioma ${locale.displayName} não suportado pelo TTS do dispositivo.")
            } else {
                Log.d("PapaiGame", "Idioma configurado com sucesso: ${locale.displayName}")
            }

            // Centraliza a configuração de voz do Papai
            configureCentralizedVoice(it, isPt)

            // Atualiza as vozes disponíveis para o usuário escolher no jogo
            refreshAvailableVoices()
        }
    }

    /**
     * Função de configuração centralizada para o mecanismo de fala do mascote Papai.
     * Se o usuário tiver uma voz configurada, ela é prioritariamente carregada.
     * Caso contrário, filtra as vozes para selecionar a masculina ideal, com fallback refinado.
     */
    private fun configureCentralizedVoice(ttsEngine: TextToSpeech, isPortuguese: Boolean) {
        try {
            val voices = ttsEngine.voices
            val savedVoiceName = selectedVoiceName.value

            if (!voices.isNullOrEmpty() && savedVoiceName != null) {
                val userVoice = voices.find { it.name == savedVoiceName }
                if (userVoice != null) {
                    ttsEngine.voice = userVoice
                    isMaleVoiceSelected = checkIsMale(userVoice.name)
                    val pitch = if (isMaleVoiceSelected) 0.82f else 1.0f
                    ttsEngine.setPitch(pitch)
                    ttsEngine.setSpeechRate(0.95f)
                    Log.d("PapaiGame", "Voz selecionada pelo usuário aplicada: ${userVoice.name}")
                    return
                }
            }

            if (isPortuguese) {
                // Tom de voz amigável, caloroso e encorpado para o Papai em português
                ttsEngine.setPitch(0.82f)
                ttsEngine.setSpeechRate(0.95f)

                if (!voices.isNullOrEmpty()) {
                    // Filtra especificamente vozes pt-BR do Brasil
                    val ptBrVoices = voices.filter { voice ->
                        val lang = voice.locale.language.lowercase()
                        val country = voice.locale.country.uppercase()
                        val name = voice.name.lowercase()
                        
                        val isPt = lang == "pt" || name.contains("pt-") || name.contains("pt_")
                        val isPortugal = country == "PT" || name.contains("pt-pt") || name.contains("pt_pt") || name.contains("portugal")
                        
                        isPt && !isPortugal
                    }

                    // Força a seleção da primeira voz masculina disponível
                    val maleVoice = ptBrVoices.find { voice ->
                        checkIsMale(voice.name)
                    }

                    if (maleVoice != null) {
                        ttsEngine.voice = maleVoice
                        isMaleVoiceSelected = true
                        Log.d("PapaiGame", "Voz centralizada pt-BR masculina selecionada com sucesso: ${maleVoice.name}")
                    } else {
                        // Caso não haja nenhuma reconhecidamente masculina, tenta qualquer uma offline como fallback e reduz pitch
                        val fallbackVoice = ptBrVoices.find { !it.isNetworkConnectionRequired } ?: ptBrVoices.firstOrNull()
                        fallbackVoice?.let { ttsEngine.voice = it }
                        isMaleVoiceSelected = false
                        Log.w("PapaiGame", "Nenhuma voz masculina nativa identificada no pt-BR. Aplicando fallback com pitch reduzido.")
                    }
                } else {
                    isMaleVoiceSelected = false
                }
            } else {
                // Configuração para inglês (US)
                ttsEngine.setPitch(0.82f)
                ttsEngine.setSpeechRate(0.95f)

                if (!voices.isNullOrEmpty()) {
                    val enVoices = voices.filter { voice ->
                        voice.locale.language.equals("en", ignoreCase = true)
                    }

                    val maleVoice = enVoices.find { voice ->
                        checkIsMale(voice.name)
                    }

                    if (maleVoice != null) {
                        ttsEngine.voice = maleVoice
                        isMaleVoiceSelected = true
                        Log.d("PapaiGame", "Voz centralizada en masculina selecionada com sucesso: ${maleVoice.name}")
                    } else {
                        val fallbackVoice = enVoices.find { !it.isNetworkConnectionRequired } ?: enVoices.firstOrNull()
                        fallbackVoice?.let { ttsEngine.voice = it }
                        isMaleVoiceSelected = false
                    }
                } else {
                    isMaleVoiceSelected = false
                }
            }
        } catch (e: Exception) {
            isMaleVoiceSelected = false
            Log.e("PapaiGame", "Erro na função centralizada de configuração de voz do Papai: ${e.message}")
        }
    }

    /**
     * Atualiza a lista de vozes disponíveis de acordo com o idioma selecionado,
     * priorizando vozes locais e identificando o gênero correspondente.
     */
    fun refreshAvailableVoices() {
        val ttsEngine = tts ?: return
        val isPt = appLanguage.value == "PT"
        val voices = try { ttsEngine.voices } catch (e: Exception) { null }
        if (voices.isNullOrEmpty()) {
            availableVoices.value = emptyList()
            return
        }

        val filtered = voices.filter { voice ->
            if (isPt) {
                val lang = voice.locale.language.lowercase()
                val country = voice.locale.country.uppercase()
                val name = voice.name.lowercase()
                val isPtLang = lang == "pt" || name.contains("pt-") || name.contains("pt_")
                val isPortugal = country == "PT" || name.contains("pt-pt") || name.contains("pt_pt") || name.contains("portugal")
                isPtLang && !isPortugal
            } else {
                voice.locale.language.lowercase() == "en"
            }
        }

        val options = filtered.map { voice ->
            val name = voice.name
            val isMale = checkIsMale(name)
            val genderStr = if (isMale) {
                if (isPt) "👨 Voz Masculina" else "👨 Male Voice"
            } else {
                if (isPt) "👩 Voz Feminina" else "👩 Female Voice"
            }
            val qualityStr = if (voice.isNetworkConnectionRequired) {
                if (isPt) "☁️ Online" else "☁️ Online"
            } else {
                if (isPt) "📱 Local" else "📱 Local"
            }
            val suffix = name.substringAfterLast("-", "Voz")
            val cleanSuffix = if (suffix.length <= 4) suffix.uppercase() else "A"
            val displayName = "$genderStr ($cleanSuffix) - $qualityStr"
            VoiceOption(
                name = name,
                isMale = isMale,
                isNetworkRequired = voice.isNetworkConnectionRequired,
                displayName = displayName
            )
        }.sortedWith(compareBy({ !it.isMale }, { it.isNetworkRequired })) // Vozes masculinas e locais primeiro!

        availableVoices.value = options
    }

    /**
     * Salva de forma persistente e aplica a nova voz selecionada no jogo.
     */
    fun selectVoice(voiceName: String?) {
        selectedVoiceName.value = voiceName
        val prefs = getApplication<Application>().getSharedPreferences("papai_game_prefs", Context.MODE_PRIVATE)
        if (voiceName == null) {
            prefs.edit().remove("selected_voice_name").apply()
        } else {
            prefs.edit().putString("selected_voice_name", voiceName).apply()
        }
        updateTtsLanguage()
        
        // Emite retorno imediato com a nova voz configurada
        viewModelScope.launch {
            delay(150)
            if (appLanguage.value == "PT") {
                speak("Olá amiguinho! O Papai está falando com a sua nova voz escolhida! Gostou?")
            } else {
                speak("Hello there! Papai is talking with your newly selected voice! Do you like it?")
            }
        }
    }

    private fun checkIsMale(name: String): Boolean {
        val lower = name.lowercase()
        return lower.contains("male") ||
               lower.contains("masc") ||
               lower.contains("man") ||
               lower.contains("m-col") ||
               lower.contains("guy") ||
               lower.contains("boy") ||
               lower.contains("papai") ||
               lower.contains("father") ||
               lower.contains("smt-m") ||
               lower.contains("-m0") ||
               lower.contains("-m1") ||
               lower.contains("-m2") ||
               lower.contains("-m3") ||
               lower.contains("iyt") ||
               lower.contains("yef") ||
               lower.contains("sfy") ||
               lower.contains("jab") ||
               lower.contains("wavenet-b") ||
               lower.contains("wavenet-c") ||
               lower.contains("wavenet-d") ||
               lower.contains("neural2-b") ||
               lower.contains("neural2-c") ||
               lower.contains("neural2-d") ||
               lower.contains("standard-b") ||
               lower.contains("standard-c") ||
               lower.contains("standard-d") ||
               lower.contains("iog") ||
               lower.contains("iol") ||
               lower.contains("sfg")
    }

    fun speak(text: String) {
        if (!isAssistantAudioEnabled.value) return
        if (ttsReady) {
            val pitch = if (selectedVoiceName.value != null) {
                if (isMaleVoiceSelected) 0.82f else 1.0f
            } else {
                if (isMaleVoiceSelected) 0.82f else 0.58f
            }
            tts?.setPitch(pitch)
            tts?.setSpeechRate(0.95f)
            val clean = text.trim()
            val spokenText = if (clean.length == 1) {
                val letterUpper = clean.uppercase()
                if (appLanguage.value == "PT") {
                    val letterSound = when (letterUpper) {
                        "A" -> "Á"
                        "E" -> "É"
                        "I" -> "Í"
                        "O" -> "Ó"
                        "U" -> "Ú"
                        "B" -> "Bê"
                        "C" -> "Cê"
                        "D" -> "Dê"
                        "F" -> "Éfe"
                        "G" -> "Gê"
                        "H" -> "Agá"
                        "J" -> "Jota"
                        "K" -> "Cá"
                        "L" -> "Éle"
                        "M" -> "Éme"
                        "N" -> "Éne"
                        "P" -> "Pê"
                        "Q" -> "Quê"
                        "R" -> "Érre"
                        "S" -> "Ésse"
                        "T" -> "Tê"
                        "V" -> "Vê"
                        "W" -> "Dáblio"
                        "X" -> "Xis"
                        "Y" -> "Ípsilon"
                        "Z" -> "Zê"
                        else -> letterUpper
                    }
                    "Letra $letterSound"
                } else {
                    val letterSound = when (letterUpper) {
                        "A" -> "Ay"
                        "B" -> "Bee"
                        "C" -> "Cee"
                        "D" -> "Dee"
                        "E" -> "Ee"
                        "F" -> "Ef"
                        "G" -> "Gee"
                        "H" -> "Aitch"
                        "I" -> "Eye"
                        "J" -> "Jay"
                        "K" -> "Kay"
                        "L" -> "El"
                        "M" -> "Em"
                        "N" -> "En"
                        "O" -> "Oh"
                        "P" -> "Pee"
                        "Q" -> "Cue"
                        "R" -> "Ar"
                        "S" -> "Ess"
                        "T" -> "Tee"
                        "U" -> "You"
                        "V" -> "Vee"
                        "W" -> "Double u"
                        "X" -> "Ex"
                        "Y" -> "Wye"
                        "Z" -> "Zee"
                        else -> letterUpper
                    }
                    "Letter $letterSound"
                }
            } else {
                if (appLanguage.value == "PT") {
                    text.replace(" com A", " com Á")
                        .replace(" com E", " com É")
                        .replace(" com I", " com Í")
                        .replace(" com O", " com Ó")
                        .replace(" com U", " com Ú")
                        .replace("letra A", "letra Á")
                        .replace("letra E", "letra É")
                        .replace("letra I", "letra Í")
                        .replace("letra O", "letra Ó")
                        .replace("letra U", "letra Ú")
                } else {
                    text
                }
            }
            tts?.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null, "PapaiID")
        }
    }

    // Pronúncia fonética de alta clareza para evitar que "E" e "I" se confundam e ensinar de verdade
    fun speakPhonetic(text: String) {
        if (!isAssistantAudioEnabled.value) return
        if (ttsReady) {
            val pitch = if (selectedVoiceName.value != null) {
                if (isMaleVoiceSelected) 0.82f else 1.0f
            } else {
                if (isMaleVoiceSelected) 0.82f else 0.58f
            }
            tts?.setPitch(pitch)
            tts?.setSpeechRate(0.95f)
            val clean = text.uppercase().trim().removeSuffix("!").removeSuffix("?").removeSuffix(".").removeSuffix(",")
            val phonetic = if (clean.length == 1) {
                if (appLanguage.value == "PT") {
                    val letterSound = when (clean) {
                        "A" -> "Á"
                        "E" -> "É"
                        "I" -> "Í"
                        "O" -> "Ó"
                        "U" -> "Ú"
                        "B" -> "Bê"
                        "C" -> "Cê"
                        "D" -> "Dê"
                        "F" -> "Éfe"
                        "G" -> "Gê"
                        "H" -> "Agá"
                        "J" -> "Jota"
                        "K" -> "Cá"
                        "L" -> "Éle"
                        "M" -> "Éme"
                        "N" -> "Éne"
                        "P" -> "Pê"
                        "Q" -> "Quê"
                        "R" -> "Érre"
                        "S" -> "Ésse"
                        "T" -> "Tê"
                        "V" -> "Vê"
                        "W" -> "Dáblio"
                        "X" -> "Xis"
                        "Y" -> "Ípsilon"
                        "Z" -> "Zê"
                        else -> clean
                    }
                    "Letra $letterSound"
                } else {
                    val letterSound = when (clean) {
                        "A" -> "Ay"
                        "B" -> "Bee"
                        "C" -> "Cee"
                        "D" -> "Dee"
                        "E" -> "Ee"
                        "F" -> "Ef"
                        "G" -> "Gee"
                        "H" -> "Aitch"
                        "I" -> "Eye"
                        "J" -> "Jay"
                        "K" -> "Kay"
                        "L" -> "El"
                        "M" -> "Em"
                        "N" -> "En"
                        "O" -> "Oh"
                        "P" -> "Pee"
                        "Q" -> "Cue"
                        "R" -> "Ar"
                        "S" -> "Ess"
                        "T" -> "Tee"
                        "U" -> "You"
                        "V" -> "Vee"
                        "W" -> "Double u"
                        "X" -> "Ex"
                        "Y" -> "Wye"
                        "Z" -> "Zee"
                        else -> clean
                    }
                    "Letter $letterSound"
                }
            } else {
                text
            }
            tts?.speak(phonetic, TextToSpeech.QUEUE_FLUSH, null, "PapaiID")
        }
    }

    // Toca som de acerto (Bip feliz)
    fun playSuccessSound() {
        viewModelScope.launch {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
            delay(150)
            toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 200)
        }
    }

    // Toca som de erro (Bip triste)
    fun playErrorSound() {
        viewModelScope.launch {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 300)
        }
    }

    // Toca som de celebração e parabeniza a criança com a voz do Papai
    private fun startCelebrationSequence() {
        viewModelScope.launch {
            // Garante que o TTS pare antes de começar a festa
            tts?.stop()
            delay(50)

            // Toca som de confirmação fofo do sistema
            try {
                toneGenerator.startTone(ToneGenerator.TONE_SUP_CONFIRM, 150)
            } catch (e: Exception) {}
            delay(150)
            try {
                toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 100)
            } catch (e: Exception) {}

            // Mensagem de comemoração alegre com a voz do Papai
            if (appLanguage.value == "PT") {
                speak("Uhuuul! Muito bem! Você completou esse desafio brilhantemente! O Papai está muito orgulhoso de você!")
            } else {
                speak("Woohoo! Great job! You completed this challenge brilliantly! Papai is so proud of you!")
            }
        }
    }

    // Apresentações por voz ao mudar de tela
    fun speakPapaiIntroduction() {
        if (appLanguage.value == "PT") {
            speak("Oi! Eu sou o Papai, o seu amiguinho dos caminhos mágicos! Toque no botão laranja grande para começar nossa jornada de aprendizagem!")
        } else {
            speak("Hi! I am Papai, your magic paths friend! Tap the big orange button to start our learning journey!")
        }
    }

    fun speakInstruction(phase: Phase) {
        val prompt = if (appLanguage.value == "PT") phase.audioPromptPt else phase.audioPromptEn
        speak(prompt)
    }

    fun speakMathPrompt() {
        val challenge = currentMathChallenge.value ?: return
        val prompt = if (appLanguage.value == "PT") challenge.questionPt else challenge.questionEn
        speak(prompt)
    }

    fun selectWorld(worldId: Int) {
        speak(if (appLanguage.value == "PT") "Mundo $worldId selecionado! Vamos ver nossas missões!" else "World $worldId selected! Let's see our missions!")
    }

    // Navegação
    fun navigateTo(screen: GameScreen) {
        currentScreen.value = screen
        // Ao mudar de tela, dispara o TTS correspondente
        when (screen) {
            is GameScreen.SplashIntro -> speakPapaiIntroduction()
            is GameScreen.WorldSelection -> {
                val pt = "Escolha um caminho mágico para jogar com o Papai!"
                val en = "Choose a magic path to play with Papai!"
                speak(if (appLanguage.value == "PT") pt else en)
            }
            is GameScreen.PhaseInstruction -> {
                speakInstruction(screen.phase)
            }
            is GameScreen.PhaseGame -> {
                // Reseta estados do minigame ao iniciar a fase
                vowelMatchIndex.value = 0
                vowelMatchCompleted.value = false
                balloonMatches.value = emptyMap()
                syllableChallengeIndex.value = 0
                syllableChallengeCompleted.value = false
                mathCompleted.value = false
                mathSelectedOption.value = null
                resetSnakeGame()
                
                // Configurações específicas para novas fases
                if (screen.phase.type == PhaseType.MATH_COUNT) {
                    val challengeIndex = when (screen.phase.id) {
                        "4.1" -> 0
                        "4.2" -> 1
                        "4.3" -> 2
                        "4.4" -> 3
                        else -> 0
                    }
                    currentMathChallenge.value = GameCurriculum.mathChallenges.getOrNull(challengeIndex)
                } else if (screen.phase.type == PhaseType.FINAL_READING) {
                    highlightedReadingWordIndex.value = -1
                    readingCompleted.value = false
                }
                
                speakInstruction(screen.phase)
            }
            is GameScreen.MathIntercalated -> {
                setupMathChallenge()
                speakMathPrompt()
            }
            is GameScreen.Celebration -> {
                startCelebrationSequence()
            }
        }
    }

    // Configuração de Matemática Intercalada
    private fun setupMathChallenge() {
        val list = GameCurriculum.mathChallenges
        val random = list.random()
        currentMathChallenge.value = random
        mathSelectedOption.value = null
        mathCompleted.value = false
    }

    fun answerMath(option: Int) {
        val challenge = currentMathChallenge.value ?: return
        mathSelectedOption.value = option
        if (option == challenge.correctAnswer) {
            playSuccessSound()
            mathCompleted.value = true
            speak(if (appLanguage.value == "PT") "Muito bem! Resposta certa!" else "Awesome! Right answer!")
        } else {
            playErrorSound()
            speak(if (appLanguage.value == "PT") "Tente contar novamente!" else "Try counting again!")
        }
    }

    // Mecânica Mundo 2 (Phonetics Match)
    fun answerVowel(letter: String, isInitialSound: Boolean) {
        val challenge = if (isInitialSound) {
            GameCurriculum.initialSoundChallenges[vowelMatchIndex.value]
        } else {
            GameCurriculum.finalSoundChallenges[vowelMatchIndex.value]
        }

        val correctLetter = if (appLanguage.value == "PT") challenge.correctLetterPt else challenge.correctLetterEn
        if (letter.firstOrNull() == correctLetter) {
            playSuccessSound()
            speak(if (appLanguage.value == "PT") challenge.audioCluePt else challenge.audioClueEn)
            
            viewModelScope.launch {
                delay(1500)
                val totalChallenges = if (isInitialSound) GameCurriculum.initialSoundChallenges.size else GameCurriculum.finalSoundChallenges.size
                if (vowelMatchIndex.value < totalChallenges - 1) {
                    vowelMatchIndex.value += 1
                } else {
                    vowelMatchCompleted.value = true
                }
            }
        } else {
            playErrorSound()
            speak(if (appLanguage.value == "PT") "Tente outra letrinha!" else "Try another letter!")
        }
    }

    // Mecânica Ditado Recortado Balões (2.3)
    fun matchBalloon(balloonText: String, targetId: String) {
        val match = GameCurriculum.vocalicBalloons.find { it.text == balloonText }
        if (match != null && match.targetId == targetId) {
            playSuccessSound()
            val newMatches = balloonMatches.value.toMutableMap()
            newMatches[balloonText] = targetId
            balloonMatches.value = newMatches
            speak(if (appLanguage.value == "PT") "Isso aí! $balloonText" else "You got it! $balloonText")
        } else {
            playErrorSound()
            speak(if (appLanguage.value == "PT") "Acho que esse som não é daí!" else "I don't think that's the right sound!")
        }
    }

    // Serpente do Alfabeto (Especial)
    fun resetSnakeGame() {
        snakeSolvedLetters.value = emptyMap()
        // Pool de letras que faltam misturadas
        val pool = snakeMissingIndices.map { snakeLetters[it] }.shuffled()
        snakePool.value = pool
    }

    fun solveSnakeLetter(letter: String, targetIndex: Int) {
        if (snakeLetters[targetIndex] == letter) {
            playSuccessSound()
            val currentSolved = snakeSolvedLetters.value.toMutableMap()
            currentSolved[targetIndex] = letter
            snakeSolvedLetters.value = currentSolved

            // Remove da piscina de letras
            val newPool = snakePool.value.toMutableList()
            newPool.remove(letter)
            snakePool.value = newPool

            speak(if (appLanguage.value == "PT") "Certo! Letra $letter na cobra!" else "Correct! Letter $letter on the snake!")
        } else {
            playErrorSound()
            speak(if (appLanguage.value == "PT") "Essa letrinha não vai aí!" else "That letter doesn't go there!")
        }
    }

    // Salvar Progresso e Trilha de Aprendizagem
    fun completePhase(phase: Phase) {
        viewModelScope.launch {
            repository.markPhaseCompleted(phase.id, 3, 100)
            navigateTo(GameScreen.Celebration(phase))
        }
    }

    fun resetAllGameProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
            resetSnakeGame()
            navigateTo(GameScreen.WorldSelection)
        }
    }

    // O videokê agora é controlado de forma unificada e sincronizada por startCelebrationSequence()

    // Funções de Leitura Final
    fun speakReadingWord(word: String, index: Int) {
        highlightedReadingWordIndex.value = index
        val cleanWord = word.replace(Regex("[.,!?;:]"), "")
        speak(cleanWord)
    }

    fun speakFullReadingText(text: String) {
        speak(text)
        viewModelScope.launch {
            val words = text.split(" ")
            for (i in words.indices) {
                highlightedReadingWordIndex.value = i
                delay(380) // Tempo agradável para a leitura de cada palavra
            }
            highlightedReadingWordIndex.value = -1
            readingCompleted.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.let {
            it.stop()
            it.shutdown()
        }
        toneGenerator.release()
    }
}

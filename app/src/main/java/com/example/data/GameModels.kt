package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa as seções pedagógicas da trilha.
 */
enum class GameWorld(val id: Int, val titlePt: String, val titleEn: String, val descriptionPt: String, val descriptionEn: String) {
    WORLD_1(1, "Coordenação Motora", "Motor Coordination", "Treine seus traçados desenhando linhas e curvas fofas", "Train your drawing tracing lines and curves"),
    WORLD_2(2, "Vogais", "Vowels", "Sons das vogais de A a U e formação de palavrinhas fáceis", "Vowel sounds and basic vocabulary"),
    WORLD_3(3, "Sílabas", "Syllables", "Desafios divertidos de sílabas, soletrar e leitura final", "Fun syllable, spelling and final reading challenges"),
    WORLD_4(4, "Matemática", "Math", "Aventura dos números, contagem, dias da semana e estações", "Numbers, counting, days of the week and seasons")
}

/**
 * Representa cada fase pedagógica dentro de um mundo.
 */
data class Phase(
    val id: String,
    val worldId: Int,
    val titlePt: String,
    val titleEn: String,
    val descriptionPt: String,
    val descriptionEn: String,
    val type: PhaseType,
    val audioPromptPt: String,
    val audioPromptEn: String
)

enum class PhaseType {
    WATERMELON_TRACE,      // Gotas de Melancia
    CARACOL_SPIRAL,        // Espiral do Caracol
    JOANINHA_BOUNCE,       // Saltos da Joaninha
    ANIMAL_PATH,           // Caminho dos Bichos
    INITIAL_SOUND,         // Som Inicial
    FINAL_SOUND,           // Som Final
    BALLOON_DRAG,          // Ditado Recortado
    SYLLABLE_B,            // Família do B
    SYLLABLE_C,            // Família do C
    SPECIAL_SNAKE,         // Serpente do Alfabeto
    MATH_COUNT,            // Fase Matemática de contagem
    FINAL_READING,         // Desafio Final de Leitura
    NUMBER_GAME,           // Jogo de números / Ordenação
    DAYS_MONTHS_SEASONS    // Dias da semana, meses e estações do ano
}

/**
 * Entidade Room para armazenar o progresso das fases concluídas pelo jogador.
 */
@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val phaseId: String,
    val completed: Boolean = false,
    val stars: Int = 0,
    val score: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * Dados de Desafios específicos para fases de som/associação (Mundo 2 e 3)
 */
data class SoundChallenge(
    val imageUrl: String = "",
    val wordPt: String,
    val wordEn: String,
    val correctLetterPt: Char,
    val correctLetterEn: Char,
    val optionsPt: List<String>,
    val optionsEn: List<String>,
    val audioCluePt: String,
    val audioClueEn: String
)

/**
 * Encontro vocálico para a fase de Balões (Fase 2.3)
 */
data class BalloonEncounter(
    val text: String,
    val targetPersonPt: String, // ex: "Cachorro", "Menino"
    val targetPersonEn: String,
    val targetId: String // "dog", "boy"
)

/**
 * Sílabas e Palavras para o Mundo 3
 */
data class SyllableChallenge(
    val missingSyllablePt: String,
    val missingSyllableEn: String,
    val completeWordPt: String,
    val completeWordEn: String,
    val syllablesPt: List<String>,
    val syllablesEn: List<String>,
    val labelPt: String,
    val labelEn: String
)

/**
 * Desafio de Matemática Intercalada
 */
data class MathChallenge(
    val questionPt: String,
    val questionEn: String,
    val objectCount: Int,
    val objectEmoji: String,
    val correctAnswer: Int,
    val options: List<Int>
)

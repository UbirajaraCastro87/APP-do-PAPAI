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

/**
 * Representa uma conquista/medalha obtida pela criança na trilha de aprendizagem.
 */
data class GameBadge(
    val id: String,
    val titlePt: String,
    val titleEn: String,
    val emoji: String,
    val descriptionPt: String,
    val descriptionEn: String,
    val conditionDescPt: String,
    val conditionDescEn: String,
    val isWorldBadge: Boolean,
    val targetWorldId: Int = 0
)

object GameBadges {
    val badges = listOf(
        GameBadge(
            id = "world_1",
            titlePt = "Mestre dos Desenhos",
            titleEn = "Drawing Master",
            emoji = "✏️",
            descriptionPt = "Você completou todas as missões de Coordenação Motora do Mundo 1!",
            descriptionEn = "You completed all Motor Coordination missions in World 1!",
            conditionDescPt = "Complete todas as missões do Mundo 1",
            conditionDescEn = "Complete all World 1 missions",
            isWorldBadge = true,
            targetWorldId = 1
        ),
        GameBadge(
            id = "world_2",
            titlePt = "Super Vogais",
            titleEn = "Vowels Hero",
            emoji = "🅰️",
            descriptionPt = "Você dominou todos os sons iniciais, finais e encontros de vogais no Mundo 2!",
            descriptionEn = "You mastered all vowel sounds and combinations in World 2!",
            conditionDescPt = "Complete todas as missões do Mundo 2",
            conditionDescEn = "Complete all World 2 missions",
            isWorldBadge = true,
            targetWorldId = 2
        ),
        GameBadge(
            id = "world_3",
            titlePt = "Gênio das Sílabas",
            titleEn = "Syllable Genius",
            emoji = "🧩",
            descriptionPt = "Você formou palavras, soletrou de A a Z e aprendeu tudo no Mundo 3!",
            descriptionEn = "You built words, spelled from A to Z and learned everything in World 3!",
            conditionDescPt = "Complete todas as missões do Mundo 3",
            conditionDescEn = "Complete all World 3 missions",
            isWorldBadge = true,
            targetWorldId = 3
        ),
        GameBadge(
            id = "world_4",
            titlePt = "Super Matemático",
            titleEn = "Math Champion",
            emoji = "🔢",
            descriptionPt = "Você contou maçãs, estrelas, peixinhos, organizou o trem e aprendeu as estações no Mundo 4!",
            descriptionEn = "You counted apples, stars, fish, organized the train and learned the seasons in World 4!",
            conditionDescPt = "Complete todas as missões do Mundo 4",
            conditionDescEn = "Complete all World 4 missions",
            isWorldBadge = true,
            targetWorldId = 4
        ),
        GameBadge(
            id = "special_first_step",
            titlePt = "Primeiro Passo",
            titleEn = "First Step",
            emoji = "🌱",
            descriptionPt = "Você completou sua primeiríssima missão da trilha do Papai!",
            descriptionEn = "You completed your very first mission on Papai's trail!",
            conditionDescPt = "Complete 1 missão pedagógica",
            conditionDescEn = "Complete 1 educational mission",
            isWorldBadge = false
        ),
        GameBadge(
            id = "special_star_collector",
            titlePt = "Colecionador de Estrelas",
            titleEn = "Star Collector",
            emoji = "🌟",
            descriptionPt = "Você acumulou mais de 15 estrelas douradas brilhando no céu!",
            descriptionEn = "You accumulated over 15 golden stars shining in the sky!",
            conditionDescPt = "Ganhe 15 estrelas douradas",
            conditionDescEn = "Earn 15 golden stars",
            isWorldBadge = false
        ),
        GameBadge(
            id = "special_supreme_master",
            titlePt = "Explorador Lendário",
            titleEn = "Legendary Explorer",
            emoji = "👑",
            descriptionPt = "Incrível! Você completou absolutamente toda a trilha de aprendizado do Papai!",
            descriptionEn = "Incredible! You completed absolutely the entire learning trail of Papai!",
            conditionDescPt = "Complete 100% da trilha pedagógica",
            conditionDescEn = "Complete 100% of the educational trail",
            isWorldBadge = false
        )
    )

    fun getBadgeById(id: String): GameBadge? {
        return badges.find { it.id == id }
    }
}

/**
 * Representa um personagem/avatar selecionável para a criança.
 */
data class GameAvatar(
    val id: String,
    val namePt: String,
    val nameEn: String,
    val emoji: String,
    val introPt: String,
    val introEn: String,
    val colorHex: String, // para estilização vibrante de cada personagem
    val descriptionPt: String,
    val descriptionEn: String
)

object GameAvatars {
    val avatars = listOf(
        GameAvatar(
            id = "lion",
            namePt = "Leon, o Leão Corajoso",
            nameEn = "Leon the Brave Lion",
            emoji = "🦁",
            introPt = "Olá, amiguinho! Sou o Leon! Vamos aprender e nos divertir muito juntos!",
            introEn = "Hello buddy! I am Leon! Let's learn and have lots of fun together!",
            colorHex = "#FFE5A3", // Warm Orange-Yellow
            descriptionPt = "Sempre corajoso e animado para novas descobertas!",
            descriptionEn = "Always brave and excited for new discoveries!"
        ),
        GameAvatar(
            id = "cat",
            namePt = "Nina, a Gatinha Esperta",
            nameEn = "Nina the Smart Kitty",
            emoji = "🐱",
            introPt = "Oi! Eu sou a Nina! Adoro ler e descobrir sons incríveis. Vamos lá?",
            introEn = "Hi! I am Nina! I love to read and discover amazing sounds. Let's go!",
            colorHex = "#E8D8F8", // Soft Purple
            descriptionPt = "Adora ler historinhas e resolver enigmas com sons!",
            descriptionEn = "Loves reading little stories and solving sound puzzles!"
        ),
        GameAvatar(
            id = "bear",
            namePt = "Teo, o Ursinho Cientista",
            nameEn = "Teo the Scientist Bear",
            emoji = "🐻",
            introPt = "Olá! Sou o Teo! Sou muito curioso com números e palavras. Quer explorar comigo?",
            introEn = "Hello! I am Teo! I am very curious about numbers and words. Want to explore with me?",
            colorHex = "#D3E8FD", // Sky Blue
            descriptionPt = "Muito inteligente e curioso, adora contar peixinhos e organizar trens!",
            descriptionEn = "Very smart and curious, loves counting fish and organizing trains!"
        ),
        GameAvatar(
            id = "robot",
            namePt = "Zeca, o Robô Aventureiro",
            nameEn = "Zeca the Adventurous Robot",
            emoji = "🤖",
            introPt = "Bip-bop! Olá! Sou o Zeca! Minha missão espacial é ajudar você a passar de fase!",
            introEn = "Beep-boop! Hello! I am Zeca! My space mission is to help you clear every stage!",
            colorHex = "#D1F3EE", // Soft Teal
            descriptionPt = "Um robô super divertido com efeitos de som espaciais engajantes!",
            descriptionEn = "A super fun robot with engaging space sound effects!"
        ),
        GameAvatar(
            id = "bee",
            namePt = "Mel, a Abelhinha Curiosa",
            nameEn = "Mel the Curious Bee",
            emoji = "🐝",
            introPt = "Oi! Sou a Mel! Voei até aqui para adoçar nossa jornada de aprendizado!",
            introEn = "Hi! I am Mel! I flew here to sweeten our learning journey!",
            colorHex = "#FFF5C3", // Sunny Light Yellow
            descriptionPt = "Voa rápido por todas as vogais e adora colorir as telas!",
            descriptionEn = "Flies quickly through all vowels and loves coloring screens!"
        ),
        GameAvatar(
            id = "dino",
            namePt = "Beto, o Dinossauro Amigo",
            nameEn = "Beto the Friendly Dino",
            emoji = "🦖",
            introPt = "Roar! Oi! Sou o Beto! Sou um dinossauro grandão e amigável, pronto para brincar!",
            introEn = "Roar! Hi! I am Beto! I'm a big, friendly dinosaur ready to play!",
            colorHex = "#D6F8D8", // Soft Green
            descriptionPt = "Embora seja gigante, ele é muito bonzinho e adora fazer novos amigos!",
            descriptionEn = "Even though he's a giant, he is super nice and loves making new friends!"
        )
    )

    fun getAvatarById(id: String): GameAvatar {
        return avatars.find { it.id == id } ?: avatars.first()
    }
}


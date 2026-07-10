package com.example.data

object GameCurriculum {

    val phases = listOf(
        // Seção 1: Coordenação Motora (worldId = 1)
        Phase(
            id = "1.1",
            worldId = 1,
            titlePt = "Gotas de Melancia",
            titleEn = "Watermelon Drops",
            descriptionPt = "Desenhe traços de cima para baixo para ajudar as gotinhas de água!",
            descriptionEn = "Trace lines from top to bottom to help the water drops fall!",
            type = PhaseType.WATERMELON_TRACE,
            audioPromptPt = "Ajude as gotinhas de melancia a caírem! Trace o caminho de cima para baixo.",
            audioPromptEn = "Help the watermelon drops fall! Trace the path from top to bottom."
        ),
        Phase(
            id = "1.2",
            worldId = 1,
            titlePt = "O Caracol Espiral",
            titleEn = "The Spiral Snail",
            descriptionPt = "Complete o desenho em espiral para terminar a casinha do caracol!",
            descriptionEn = "Complete the spiral drawing to finish the snail's shell!",
            type = PhaseType.CARACOL_SPIRAL,
            audioPromptPt = "Complete a casinha do caracol fazendo o desenho em espiral!",
            audioPromptEn = "Complete the snail's shell by drawing a beautiful spiral!"
        ),
        Phase(
            id = "1.3",
            worldId = 1,
            titlePt = "Saltos da Joaninha",
            titleEn = "Ladybug Jumps",
            descriptionPt = "Ajude a joaninha desenhando curvas para saltar de flor em flor!",
            descriptionEn = "Help the ladybug by drawing continuous arches to jump from flower to flower!",
            type = PhaseType.JOANINHA_BOUNCE,
            audioPromptPt = "Ajude a joaninha a saltar de flor em flor desenhando curvas!",
            audioPromptEn = "Help the ladybug jump from flower to flower by drawing arches!"
        ),

        // Seção 2: Vogais (worldId = 2)
        Phase(
            id = "2.1",
            worldId = 2,
            titlePt = "O Som Inicial",
            titleEn = "Initial Sounds",
            descriptionPt = "Qual é a vogal que inicia cada desenho? Toque na vogal certa!",
            descriptionEn = "Which vowel starts each picture? Tap the correct vowel!",
            type = PhaseType.INITIAL_SOUND,
            audioPromptPt = "Toque na primeira letra do nome de cada figura que aparecer!",
            audioPromptEn = "Tap the first letter of the name of each picture that appears!"
        ),
        Phase(
            id = "2.2",
            worldId = 2,
            titlePt = "O Som Final",
            titleEn = "Final Sounds",
            descriptionPt = "Qual é a vogal que termina a palavra? Escolha com atenção!",
            descriptionEn = "Which vowel ends the word? Choose carefully!",
            type = PhaseType.FINAL_SOUND,
            audioPromptPt = "Escute a palavra e toque na letra que termina essa palavra!",
            audioPromptEn = "Listen to the word and tap the letter that ends this word!"
        ),
        Phase(
            id = "2.3",
            worldId = 2,
            titlePt = "Balões de Fala",
            titleEn = "Speech Balloons",
            descriptionPt = "Arraste os encontros vocálicos para os personagens correspondentes!",
            descriptionEn = "Drag the vowel combination speech balloons to the right characters!",
            type = PhaseType.BALLOON_DRAG,
            audioPromptPt = "Coloque cada balãozinho perto de quem está fazendo esse som! Au-au para o cachorro, Oi para o menino!",
            audioPromptEn = "Place each speech bubble near whoever is making that sound! Woof for the dog, Hi for the boy!"
        ),

        // Seção 3: Sílabas (worldId = 3)
        Phase(
            id = "3.1",
            worldId = 3,
            titlePt = "Aventura das Letras A-Z",
            titleEn = "Letter Adventure A-Z",
            descriptionPt = "Escolha qualquer letra e complete os pedacinhos das palavras de A a Z!",
            descriptionEn = "Choose any letter and complete syllable pieces from A to Z!",
            type = PhaseType.SYLLABLE_B,
            audioPromptPt = "Escolha uma letrinha do alfabeto e arraste os pedacinhos que faltam para formar a palavra!",
            audioPromptEn = "Choose any alphabet letter and drag the missing pieces to form the word!"
        ),
        Phase(
            id = "3.2",
            worldId = 3,
            titlePt = "Jogo de Palavras A-Z",
            titleEn = "Word Game A-Z",
            descriptionPt = "Soletre as palavrinhas de todas as letras colocando as letrinhas na ordem certa!",
            descriptionEn = "Spell words from all alphabet letters by placing them in the correct order!",
            type = PhaseType.SYLLABLE_C,
            audioPromptPt = "Vamos brincar de soletrar! Toque nas letrinhas na ordem certa para formar o nome do desenho!",
            audioPromptEn = "Let's play spelling! Tap the letters in the correct order to spell the picture's name!"
        ),
        Phase(
            id = "3.3",
            worldId = 3,
            titlePt = "A Serpente do Alfabeto",
            titleEn = "The Alphabet Snake",
            descriptionPt = "Complete o corpinho da cobra com as letras do alfabeto que faltam!",
            descriptionEn = "Complete the snake's body with the missing letters of the alphabet!",
            type = PhaseType.SPECIAL_SNAKE,
            audioPromptPt = "Encaixe as letras que faltam para completar a ordem do alfabeto!",
            audioPromptEn = "Fit the missing letters to complete the alphabet order!"
        ),
        Phase(
            id = "3.4",
            worldId = 3,
            titlePt = "Leitura com o Papai",
            titleEn = "Reading with Papai",
            descriptionPt = "O grande desafio final: toque nas palavras para ler um textinho com o Papai!",
            descriptionEn = "The great final challenge: tap the words to read a short text with Papai!",
            type = PhaseType.FINAL_READING,
            audioPromptPt = "Parabéns, você chegou ao Desafio Final de Leitura! Toque no balão de fala para ouvir o texto e depois toque nas palavras para ler junto com o Papai!",
            audioPromptEn = "Congratulations, you reached the Final Reading Challenge! Tap the speech balloon to hear the text, then tap on individual words to read along with Papai!"
        ),

        // Seção 4: Matemática (worldId = 4)
        Phase(
            id = "4.1",
            worldId = 4,
            titlePt = "O Desafio das Maçãs",
            titleEn = "The Apple Challenge",
            descriptionPt = "Ajude o Papai a contar as deliciosas maçãs vermelhas colhidas!",
            descriptionEn = "Help Papai count the delicious red apples harvested!",
            type = PhaseType.MATH_COUNT,
            audioPromptPt = "Vamos ajudar o Papai a contar as maçãs vermelhas? Quantas são no total?",
            audioPromptEn = "Papai collected red apples! How many are there in total?"
        ),
        Phase(
            id = "4.2",
            worldId = 4,
            titlePt = "Estrelas do Céu",
            titleEn = "Stars in the Sky",
            descriptionPt = "Ajude o Papai a contar as estrelas douradas brilhando no céu!",
            descriptionEn = "Help Papai count the golden stars shining in the sky!",
            type = PhaseType.MATH_COUNT,
            audioPromptPt = "Ajude o Papai a contar as estrelas douradas! Quantas você vê?",
            audioPromptEn = "Help Papai count the golden stars! How many do you see?"
        ),
        Phase(
            id = "4.3",
            worldId = 4,
            titlePt = "Borboletas do Jardim",
            titleEn = "Garden Butterflies",
            descriptionPt = "Quantas borboletas coloridas estão voando alegremente pelo jardim?",
            descriptionEn = "How many colorful butterflies are flying happily in the garden?",
            type = PhaseType.MATH_COUNT,
            audioPromptPt = "Quantas borboletas azuis voam no jardim? Conte com atenção!",
            audioPromptEn = "How many blue butterflies are flying in the garden? Count carefully!"
        ),
        Phase(
            id = "4.4",
            worldId = 4,
            titlePt = "Peixinhos do Mar",
            titleEn = "Sea Fishes",
            descriptionPt = "Ajude o Papai a contar quantos peixinhos coloridos nadam na lagoa!",
            descriptionEn = "Help Papai count how many colorful fish are swimming in the pond!",
            type = PhaseType.MATH_COUNT,
            audioPromptPt = "Olha os peixinhos nadando! Quantos peixinhos você vê na lagoa?",
            audioPromptEn = "Look at the swimming fish! How many do you see in the pond?"
        ),
        Phase(
            id = "4.5",
            worldId = 4,
            titlePt = "O Trem dos Números",
            titleEn = "The Number Train",
            descriptionPt = "Organize os números nos vagões do trem do Papai na ordem certa de 1 até 5!",
            descriptionEn = "Arrange numbers on Papai's train cars in the correct order from 1 to 5!",
            type = PhaseType.NUMBER_GAME,
            audioPromptPt = "Toque nos números na ordem crescente para organizar o trem do Papai!",
            audioPromptEn = "Tap the numbers in ascending order to organize Papai's train!"
        ),
        Phase(
            id = "4.6",
            worldId = 4,
            titlePt = "Dias da Semana",
            titleEn = "Days of the Week",
            descriptionPt = "Vamos aprender os dias da semana na ordem certa com o Papai!",
            descriptionEn = "Let's learn the days of the week in the correct order with Papai!",
            type = PhaseType.DAYS_MONTHS_SEASONS,
            audioPromptPt = "Toque nos dias da semana na ordem certa de Domingo até Sábado!",
            audioPromptEn = "Tap the days of the week in the correct order from Sunday to Saturday!"
        ),
        Phase(
            id = "4.7",
            worldId = 4,
            titlePt = "Meses do Ano",
            titleEn = "Months of the Year",
            descriptionPt = "Arrume os meses do ano na sequência e aprenda cada um deles!",
            descriptionEn = "Arrange the months of the year in sequence and learn each of them!",
            type = PhaseType.DAYS_MONTHS_SEASONS,
            audioPromptPt = "Toque nos meses na ordem certa para completar o nosso calendário anual!",
            audioPromptEn = "Tap the months in the correct order to complete our yearly calendar!"
        ),
        Phase(
            id = "4.8",
            worldId = 4,
            titlePt = "As Quatro Estações",
            titleEn = "The Four Seasons",
            descriptionPt = "Organize roupas e climas nas quatro estações: Primavera, Verão, Outono e Inverno!",
            descriptionEn = "Organize clothes and climates across the four seasons: Spring, Summer, Autumn and Winter!",
            type = PhaseType.DAYS_MONTHS_SEASONS,
            audioPromptPt = "Arraste cada desenho fofo para a estação do ano que mais combina com ele!",
            audioPromptEn = "Drag each cute picture to the season of the year that matches it best!"
        )
    )

    // Desafios de Som Inicial (Vogais)
    val initialSoundChallenges = listOf(
        SoundChallenge(
            wordPt = "Abacaxi",
            wordEn = "Apple",
            correctLetterPt = 'A',
            correctLetterEn = 'A',
            optionsPt = listOf("A", "E", "O"),
            optionsEn = listOf("A", "I", "U"),
            audioCluePt = "Aaaa-bacaxi! Começa com A!",
            audioClueEn = "Aaaa-pple! Starts with A!"
        ),
        SoundChallenge(
            wordPt = "Elefante",
            wordEn = "Elephant",
            correctLetterPt = 'E',
            correctLetterEn = 'E',
            optionsPt = listOf("E", "I", "U"),
            optionsEn = listOf("A", "E", "O"),
            audioCluePt = "Eee-lefante! Começa com E!",
            audioClueEn = "Eee-lephant! Starts with E!"
        ),
        SoundChallenge(
            wordPt = "Índio",
            wordEn = "Igloo",
            correctLetterPt = 'I',
            correctLetterEn = 'I',
            optionsPt = listOf("A", "E", "I"),
            optionsEn = listOf("I", "O", "U"),
            audioCluePt = "Iii-ndio! Começa com I!",
            audioClueEn = "Iii-gloo! Starts with I!"
        ),
        SoundChallenge(
            wordPt = "Ovo",
            wordEn = "Octopus",
            correctLetterPt = 'O',
            correctLetterEn = 'O',
            optionsPt = listOf("O", "U", "A"),
            optionsEn = listOf("E", "I", "O"),
            audioCluePt = "Ooo-vo! Começa com O!",
            audioClueEn = "Ooo-ctopus! Starts with O!"
        ),
        SoundChallenge(
            wordPt = "Urso",
            wordEn = "Umbrella",
            correctLetterPt = 'U',
            correctLetterEn = 'U',
            optionsPt = listOf("E", "O", "U"),
            optionsEn = listOf("A", "O", "U"),
            audioCluePt = "Uuu-rso! Começa com U!",
            audioClueEn = "Uuu-mbrella! Starts with U!"
        )
    )

    // Desafios de Som Final
    val finalSoundChallenges = listOf(
        SoundChallenge(
            wordPt = "Coruja",
            wordEn = "Banana",
            correctLetterPt = 'A',
            correctLetterEn = 'A',
            optionsPt = listOf("A", "O", "E"),
            optionsEn = listOf("E", "I", "A"),
            audioCluePt = "Corujaaaa! Termina com A!",
            audioClueEn = "Bananaaaa! Ends with A!"
        ),
        SoundChallenge(
            wordPt = "Urso",
            wordEn = "Tomato",
            correctLetterPt = 'O',
            correctLetterEn = 'O',
            optionsPt = listOf("O", "U", "I"),
            optionsEn = listOf("O", "A", "U"),
            audioCluePt = "Ursoooo! Termina com O!",
            audioClueEn = "Tomatoooo! Ends with O!"
        ),
        SoundChallenge(
            wordPt = "Saci",
            wordEn = "Taxi",
            correctLetterPt = 'I',
            correctLetterEn = 'I',
            optionsPt = listOf("E", "I", "A"),
            optionsEn = listOf("U", "I", "E"),
            audioCluePt = "Saciiii! Termina com I!",
            audioClueEn = "Taxiiii! Ends with I!"
        ),
        SoundChallenge(
            wordPt = "Abacate",
            wordEn = "Tree",
            correctLetterPt = 'E',
            correctLetterEn = 'E',
            optionsPt = listOf("A", "E", "O"),
            optionsEn = listOf("E", "O", "A"),
            audioCluePt = "Abacateeee! Termina com E!",
            audioClueEn = "Treeeee! Ends with E!"
        ),
        SoundChallenge(
            wordPt = "Urubu",
            wordEn = "Bamboo",
            correctLetterPt = 'U',
            correctLetterEn = 'U',
            optionsPt = listOf("O", "U", "A"),
            optionsEn = listOf("I", "A", "U"),
            audioCluePt = "Urubuuuu! Termina com U!",
            audioClueEn = "Bamboouuu! Ends with U!"
        )
    )

    // Ditado Recortado de Balões
    val vocalicBalloons = listOf(
        BalloonEncounter("AU!", "Cachorro", "Dog", "dog"),
        BalloonEncounter("OI!", "Menino acenando", "Waving boy", "boy"),
        BalloonEncounter("UI!", "Menina com susto", "Surprised girl", "girl"),
        BalloonEncounter("AI!", "Menino machucado", "Hurt boy", "hurt_boy")
    )

    // Desafios do Alfabeto de A a Z para Sílabas e Soletrar
    val alphabetChallenges = mapOf(
        "A" to listOf(
            SyllableChallenge("A", "A", "ANEL", "APE", listOf("A", "E", "O"), listOf("A", "O", "I"), "Anel brilhante", "Ape")
        ),
        "B" to listOf(
            SyllableChallenge("BA", "BA", "BALA", "BANANA", listOf("BA", "BE", "BO"), listOf("BA", "BE", "BU"), "Bala de mel", "Banana"),
            SyllableChallenge("BO", "BO", "BOCA", "BOX", listOf("BA", "BI", "BO"), listOf("BO", "BA", "BE"), "Boca sorrindo", "Box"),
            SyllableChallenge("BU", "BU", "BULE", "BUS", listOf("BU", "BE", "BO"), listOf("BU", "BI", "BO"), "Bule quente", "Bus"),
            SyllableChallenge("BE", "BE", "BEBÊ", "BED", listOf("BE", "BA", "BI"), listOf("BE", "BO", "BA"), "Bebê fofo", "Bed")
        ),
        "C" to listOf(
            SyllableChallenge("CA", "CA", "CASA", "CAT", listOf("CA", "CO", "CU"), listOf("CA", "CE", "CI"), "Casa bonita", "Cat"),
            SyllableChallenge("CO", "CO", "COPO", "COW", listOf("CA", "CO", "CU"), listOf("CO", "CA", "CU"), "Copo d'água", "Cow"),
            SyllableChallenge("CU", "CU", "CUCA", "CUP", listOf("CA", "CO", "CU"), listOf("CU", "CE", "CO"), "Cuca fofa", "Cup"),
            SyllableChallenge("CE", "CE", "CENOURA", "CELLO", listOf("CE", "CI", "CA"), listOf("CE", "CI", "CO"), "Cenoura laranja", "Cello"),
            SyllableChallenge("CI", "CI", "CINEMA", "CITY", listOf("CE", "CI", "CO"), listOf("CE", "CI", "CA"), "Cinema divertido", "City")
        ),
        "D" to listOf(
            SyllableChallenge("DA", "DA", "DADO", "DATE", listOf("DA", "DE", "DI"), listOf("DA", "DO", "DU"), "Dado colorido", "Date"),
            SyllableChallenge("DO", "DO", "DOCE", "DOG", listOf("DA", "DO", "DU"), listOf("DO", "DE", "DI"), "Doce gostoso", "Dog")
        ),
        "E" to listOf(
            SyllableChallenge("E", "E", "ESCOLA", "EGG", listOf("A", "E", "I"), listOf("E", "A", "U"), "Escola feliz", "Egg")
        ),
        "F" to listOf(
            SyllableChallenge("FA", "FA", "FADA", "FAN", listOf("FA", "FE", "FI"), listOf("FA", "FO", "FU"), "Fada mágica", "Fan"),
            SyllableChallenge("FO", "FO", "FOGO", "FOX", listOf("FA", "FE", "FO"), listOf("FO", "FE", "FI"), "Fogo quente", "Fox")
        ),
        "G" to listOf(
            SyllableChallenge("GA", "GA", "GATO", "GAME", listOf("GA", "GE", "GI"), listOf("GA", "GO", "GU"), "Gato fofo", "Game"),
            SyllableChallenge("GO", "GO", "GOTA", "GOAT", listOf("GA", "GO", "GU"), listOf("GO", "GE", "GI"), "Gota d'água", "Goat")
        ),
        "H" to listOf(
            SyllableChallenge("HA", "HA", "HARPA", "HAM", listOf("HA", "HE", "HI"), listOf("HA", "HO", "HU"), "Harpa musical", "Ham"),
            SyllableChallenge("HE", "HE", "HELICE", "HEN", listOf("HA", "HE", "HI"), listOf("HE", "HO", "HU"), "Hélice girando", "Hen")
        ),
        "I" to listOf(
            SyllableChallenge("I", "I", "IGREJA", "INK", listOf("A", "E", "I"), listOf("I", "O", "U"), "Igreja antiga", "Ink")
        ),
        "J" to listOf(
            SyllableChallenge("JA", "JA", "JANELA", "JAR", listOf("JA", "JE", "JI"), listOf("JA", "JO", "JU"), "Janela aberta", "Jar"),
            SyllableChallenge("JI", "JI", "JIPE", "JEEP", listOf("JA", "JE", "JI"), listOf("JI", "JO", "JU"), "Jipe forte", "Jeep")
        ),
        "K" to listOf(
            SyllableChallenge("KI", "KI", "KIWI", "KID", listOf("KA", "KE", "KI"), listOf("KI", "KO", "KU"), "Kiwi verde", "Kid")
        ),
        "L" to listOf(
            SyllableChallenge("LA", "LA", "LÁPIS", "LAKE", listOf("LA", "LE", "LI"), listOf("LA", "LO", "LU"), "Lápis de cor", "Lake"),
            SyllableChallenge("LU", "LU", "LUA", "LUNG", listOf("LA", "LO", "LU"), listOf("LU", "LE", "LI"), "Lua brilhante", "Lung")
        ),
        "M" to listOf(
            SyllableChallenge("MA", "MA", "MACACO", "MAP", listOf("MA", "ME", "MI"), listOf("MA", "MO", "MU"), "Macaco brincalhão", "Map"),
            SyllableChallenge("MO", "MO", "MOLA", "MOP", listOf("MA", "MO", "MU"), listOf("MO", "ME", "MI"), "Mola que pula", "Mop")
        ),
        "N" to listOf(
            SyllableChallenge("NA", "NA", "NAVIO", "NAIL", listOf("NA", "NE", "NI"), listOf("NA", "NO", "NU"), "Navio no mar", "Nail"),
            SyllableChallenge("NU", "NU", "NUVEM", "NUT", listOf("NA", "NO", "NU"), listOf("NU", "NE", "NI"), "Nuvem fofinha", "Nut")
        ),
        "O" to listOf(
            SyllableChallenge("O", "O", "ÔNIBUS", "OWL", listOf("A", "O", "U"), listOf("O", "E", "I"), "Ônibus escolar", "Owl")
        ),
        "P" to listOf(
            SyllableChallenge("PA", "PA", "PATO", "PAN", listOf("PA", "PE", "PI"), listOf("PA", "PO", "PU"), "Pato amarelo", "Pan"),
            SyllableChallenge("PI", "PI", "PIPOCA", "PIG", listOf("PA", "PE", "PI"), listOf("PI", "PO", "PU"), "Pipoca quente", "Pig")
        ),
        "Q" to listOf(
            SyllableChallenge("QUE", "QUE", "QUEIJO", "QUEEN", listOf("QUA", "QUE", "QUI"), listOf("QUE", "QUI", "QUO"), "Queijo gostoso", "Queen")
        ),
        "R" to listOf(
            SyllableChallenge("RA", "RA", "RATO", "RAT", listOf("RA", "RE", "RI"), listOf("RA", "RO", "RU"), "Rato cinzento", "Rat"),
            SyllableChallenge("RU", "RU", "RUA", "RUG", listOf("RA", "RO", "RU"), listOf("RU", "RE", "RI"), "Rua asfaltada", "Rug")
        ),
        "S" to listOf(
            SyllableChallenge("SA", "SA", "SAPO", "SAD", listOf("SA", "SE", "SI"), listOf("SA", "SO", "SU"), "Sapo saltitante", "Sad"),
            SyllableChallenge("SU", "SU", "SUCO", "SUN", listOf("SA", "SO", "SU"), listOf("SU", "SE", "SI"), "Suco de uva", "Sun")
        ),
        "T" to listOf(
            SyllableChallenge("TA", "TA", "TATU", "TAB", listOf("TA", "TE", "TI"), listOf("TA", "TO", "TU"), "Tatu pequeno", "Tab"),
            SyllableChallenge("TI", "TI", "TIJOLO", "TIN", listOf("TA", "TE", "TI"), listOf("TI", "TO", "TU"), "Tijolo vermelho", "Tin")
        ),
        "U" to listOf(
            SyllableChallenge("U", "U", "URSO", "URN", listOf("A", "E", "U"), listOf("U", "O", "I"), "Urso marrom", "Urn")
        ),
        "V" to listOf(
            SyllableChallenge("VA", "VA", "VACA", "VAN", listOf("VA", "VE", "VI"), listOf("VA", "VO", "VU"), "Vaca malhada", "Van"),
            SyllableChallenge("VE", "VE", "VELA", "VEST", listOf("VA", "VE", "VI"), listOf("VE", "VO", "VU"), "Vela acesa", "Vest")
        ),
        "W" to listOf(
            SyllableChallenge("WA", "WA", "WAFER", "WEB", listOf("WA", "WE", "WI"), listOf("WA", "WO", "WI"), "Biscoito wafer", "Web")
        ),
        "X" to listOf(
            SyllableChallenge("XA", "XA", "XALE", "XRAY", listOf("XA", "XE", "XI"), listOf("XA", "XO", "XU"), "Xale de lã", "X-ray"),
            SyllableChallenge("XI", "XI", "XIXI", "XIXI", listOf("XA", "XE", "XI"), listOf("XI", "XO", "XU"), "Fazer xixi", "Xixi")
        ),
        "Y" to listOf(
            SyllableChallenge("YO", "YO", "YOGA", "YAK", listOf("YA", "YE", "YO"), listOf("YO", "YA", "YU"), "Praticar yoga", "Yak")
        ),
        "Z" to listOf(
            SyllableChallenge("ZE", "ZE", "ZEBRA", "ZOO", listOf("ZA", "ZE", "ZI"), listOf("ZE", "ZO", "ZU"), "Zebra listrada", "Zoo"),
            SyllableChallenge("ZE", "ZE", "ZERO", "ZIP", listOf("ZA", "ZE", "ZI"), listOf("ZE", "ZO", "ZU"), "Número zero", "Zip")
        )
    )

    // Desafios de Matemática Intercalada
    val mathChallenges = listOf(
        MathChallenge(
            questionPt = "Papai coletou maçãs! Quantas são?",
            questionEn = "Papai collected apples! How many are there?",
            objectCount = 3,
            objectEmoji = "🍎",
            correctAnswer = 3,
            options = listOf(2, 3, 5)
        ),
        MathChallenge(
            questionPt = "Ajude o Papai a contar as estrelas douradas!",
            questionEn = "Help Papai count the golden stars!",
            objectCount = 5,
            objectEmoji = "⭐",
            correctAnswer = 5,
            options = listOf(4, 5, 6)
        ),
        MathChallenge(
            questionPt = "Quantas borboletas azuis voam no jardim?",
            questionEn = "How many blue butterflies are flying in the garden?",
            objectCount = 4,
            objectEmoji = "🦋",
            correctAnswer = 4,
            options = listOf(3, 4, 7)
        ),
        MathChallenge(
            questionPt = "Olha os peixinhos nadando! Quantos você vê?",
            questionEn = "Look at the swimming fish! How many do you see?",
            objectCount = 6,
            objectEmoji = "🐠",
            correctAnswer = 6,
            options = listOf(5, 6, 8)
        )
    )
}

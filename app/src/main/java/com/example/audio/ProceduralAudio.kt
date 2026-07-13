package com.example.audio

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.sin

enum class Note(val freq: Double) {
    REST(0.0),
    C4(261.63), CS4(277.18), D4(293.66), DS4(311.13), E4(329.63), F4(349.23), FS4(369.99), G4(392.00), GS4(415.30), A4(440.00), AS4(466.16), B4(493.88),
    C5(523.25), CS5(554.37), D5(587.33), DS5(622.25), E5(659.25), F5(698.46), FS5(739.99), G5(783.99), GS5(830.61), A5(880.00), AS5(932.33), B5(987.77),
    C6(1046.50), D6(1174.66), E6(1318.51), F6(1396.91), FS6(1479.98), G6(1567.98), B6(1975.53), C7(2093.00), E7(2637.02)
}

enum class SoundtrackType(val id: String, val titlePt: String, val titleEn: String) {
    HAPPY("happy", "Trilha Feliz", "Happy Trail"),
    CLOUD("cloud", "Nuvem de Algodão", "Cotton Cloud"),
    STAR("star", "Estrela Mágica", "Magic Star")
}

class ProceduralAudioPlayer(private val context: Context) {

    private val sampleRate = 22050
    private var audioTrack: AudioTrack? = null
    private var musicJob: Job? = null
    
    // States
    val isMusicEnabled = MutableStateFlow(true)
    val currentTrack = MutableStateFlow(SoundtrackType.HAPPY)
    val musicVolume = MutableStateFlow(0.4f) // Soft background volume (0.0 to 1.0)

    private val happyMelody: List<Pair<Note, Double>> = listOf(
        Note.C4 to 0.4, Note.C4 to 0.4, Note.G4 to 0.4, Note.G4 to 0.4,
        Note.A4 to 0.4, Note.A4 to 0.4, Note.G4 to 0.8,
        Note.F4 to 0.4, Note.F4 to 0.4, Note.E4 to 0.4, Note.E4 to 0.4,
        Note.D4 to 0.4, Note.D4 to 0.4, Note.C4 to 0.8,
        Note.G4 to 0.4, Note.G4 to 0.4, Note.F4 to 0.4, Note.F4 to 0.4,
        Note.E4 to 0.4, Note.E4 to 0.4, Note.D4 to 0.8,
        Note.G4 to 0.4, Note.G4 to 0.4, Note.F4 to 0.4, Note.F4 to 0.4,
        Note.E4 to 0.4, Note.E4 to 0.4, Note.D4 to 0.8,
        Note.C4 to 0.4, Note.C4 to 0.4, Note.G4 to 0.4, Note.G4 to 0.4,
        Note.A4 to 0.4, Note.A4 to 0.4, Note.G4 to 0.8,
        Note.F4 to 0.4, Note.F4 to 0.4, Note.E4 to 0.4, Note.E4 to 0.4,
        Note.D4 to 0.4, Note.D4 to 0.4, Note.C4 to 0.8
    )

    private val cloudMelody: List<Pair<Note, Double>> = listOf(
        Note.E4 to 0.6, Note.G4 to 0.6, Note.A4 to 0.6, Note.C5 to 0.6,
        Note.A4 to 0.6, Note.G4 to 0.6, Note.E4 to 1.2,
        Note.D4 to 0.6, Note.E4 to 0.6, Note.G4 to 0.6, Note.A4 to 0.6,
        Note.G4 to 0.6, Note.E4 to 0.6, Note.D4 to 1.2,
        Note.C4 to 0.6, Note.D4 to 0.6, Note.E4 to 0.6, Note.G4 to 0.6,
        Note.A4 to 0.6, Note.C5 to 0.6, Note.D5 to 1.2,
        Note.C5 to 0.6, Note.A4 to 0.6, Note.G4 to 0.6, Note.E4 to 0.6,
        Note.D4 to 0.6, Note.E4 to 0.6, Note.C4 to 1.2
    )

    private val starMelody: List<Pair<Note, Double>> = listOf(
        Note.C5 to 0.3, Note.E5 to 0.3, Note.G5 to 0.3, Note.C6 to 0.3,
        Note.B5 to 0.3, Note.G5 to 0.3, Note.E5 to 0.3, Note.C5 to 0.3,
        Note.F5 to 0.3, Note.A5 to 0.3, Note.C6 to 0.3, Note.F6 to 0.3,
        Note.E6 to 0.3, Note.C6 to 0.3, Note.A5 to 0.3, Note.F5 to 0.3,
        Note.G5 to 0.3, Note.B5 to 0.3, Note.D6 to 0.3, Note.G6 to 0.3,
        Note.FS6 to 0.3, Note.D6 to 0.3, Note.B5 to 0.3, Note.G5 to 0.3,
        Note.C6 to 0.3, Note.E6 to 0.3, Note.G6 to 0.3, Note.C6 to 0.6,
        Note.REST to 0.6
    )

    init {
        // Load preferences
        val prefs = context.getSharedPreferences("papai_audio_prefs", Context.MODE_PRIVATE)
        isMusicEnabled.value = prefs.getBoolean("bg_music_enabled", true)
        musicVolume.value = prefs.getFloat("bg_music_volume", 0.4f)
        val savedTrack = prefs.getString("bg_music_track", "happy") ?: "happy"
        currentTrack.value = SoundtrackType.values().toList().firstOrNull { it.id == savedTrack } ?: SoundtrackType.HAPPY

        initAudioTrack()
    }

    private fun initAudioTrack() {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            audioTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AudioTrack.Builder()
                    .setAudioAttributes(
                        android.media.AudioAttributes.Builder()
                            .setUsage(android.media.AudioAttributes.USAGE_GAME)
                            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize,
                    AudioTrack.MODE_STREAM
                )
            }
            audioTrack?.play()
        } catch (e: Exception) {
            Log.e("ProceduralAudio", "Error initializing AudioTrack: ${e.message}")
        }
    }

    fun startMusicLoop(scope: CoroutineScope) {
        musicJob?.cancel()
        musicJob = scope.launch(Dispatchers.Default) {
            var noteIndex = 0
            while (isActive) {
                if (!isMusicEnabled.value) {
                    delay(300)
                    continue
                }

                val melody: List<Pair<Note, Double>> = when (currentTrack.value) {
                    SoundtrackType.HAPPY -> happyMelody
                    SoundtrackType.CLOUD -> cloudMelody
                    SoundtrackType.STAR -> starMelody
                }

                if (noteIndex >= melody.size) {
                    noteIndex = 0
                }

                val pair = melody[noteIndex]
                val note = pair.first
                val duration = pair.second
                playNote(note, duration)
                noteIndex++
            }
        }
    }

    fun stopMusicLoop() {
        musicJob?.cancel()
        musicJob = null
    }

    fun toggleMusic() {
        isMusicEnabled.value = !isMusicEnabled.value
        context.getSharedPreferences("papai_audio_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("bg_music_enabled", isMusicEnabled.value)
            .apply()
    }

    fun setTrack(track: SoundtrackType) {
        currentTrack.value = track
        context.getSharedPreferences("papai_audio_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("bg_music_track", track.id)
            .apply()
    }

    fun setVolume(volume: Float) {
        musicVolume.value = volume.coerceIn(0.0f, 1.0f)
        context.getSharedPreferences("papai_audio_prefs", Context.MODE_PRIVATE)
            .edit()
            .putFloat("bg_music_volume", musicVolume.value)
            .apply()
    }

    /**
     * Synthesizes and writes a note to the AudioTrack.
     */
    private suspend fun playNote(note: Note, duration: Double) {
        val track = audioTrack ?: return
        if (note == Note.REST) {
            delay((duration * 1000).toLong())
            return
        }

        val numSamples = (sampleRate * duration).toInt()
        val samples = ShortArray(numSamples)
        val freq = note.freq
        val vol = musicVolume.value * 0.15f // Keep it soft in the background

        // Generate wave samples
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val angle = 2.0 * Math.PI * freq * t

            // Soft attack-decay envelope to avoid clipping clicks
            val envelope: Double = when {
                i < 300 -> i / 300.0 // attack
                i > numSamples - 1000 -> (numSamples - i) / 1000.0 // decay
                else -> 1.0
            }

            // Warm sine wave + subtle sub-harmonic for richness
            val val1 = sin(angle)
            val val2 = 0.2 * sin(2.0 * angle)
            val wave = (val1 + val2) / 1.2

            samples[i] = (wave * 32767.0 * vol * envelope).toInt().toShort()
        }

        // Write to AudioTrack (blocking write, but run on Dispatchers.Default)
        withContext(Dispatchers.IO) {
            try {
                if (track.playState != AudioTrack.PLAYSTATE_PLAYING) {
                    track.play()
                }
                track.write(samples, 0, samples.size)
            } catch (e: Exception) {
                Log.e("ProceduralAudio", "Error writing to AudioTrack: ${e.message}")
            }
        }
    }

    /**
     * Plays a high-quality success/reward sound effect (sweet sparkling chime).
     */
    fun playProceduralSuccessSFX(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            val track = audioTrack ?: return@launch
            val arpeggio = listOf(Note.C5, Note.E5, Note.G5, Note.C6, Note.E6, Note.G6)
            val noteDuration = 0.08 // very quick, sweet cascade

            for (note in arpeggio) {
                val numSamples = (sampleRate * noteDuration).toInt()
                val samples = ShortArray(numSamples)
                val vol = 0.35f // louder for reward feedback

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val angle = 2.0 * Math.PI * note.freq * t
                    val envelope = when {
                        i < 150 -> i / 150.0
                        i > numSamples - 300 -> (numSamples - i) / 300.0
                        else -> 1.0
                    }
                    val wave = sin(angle)
                    samples[i] = (wave * 32767.0 * vol * envelope).toInt().toShort()
                }

                withContext(Dispatchers.IO) {
                    try {
                        track.write(samples, 0, samples.size)
                    } catch (e: Exception) {
                        Log.e("ProceduralAudio", "SFX write error: ${e.message}")
                    }
                }
            }
        }
    }

    /**
     * Plays a giant celebration fanfare sound effect (for phase completions).
     */
    fun playCelebrationFanfareSFX(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            val track = audioTrack ?: return@launch
            // Upward fanfare chords
            val chords = listOf(
                Note.C5 to 0.15, Note.E5 to 0.15, Note.G5 to 0.15, Note.C6 to 0.4,
                Note.REST to 0.05,
                Note.G5 to 0.15, Note.B5 to 0.15, Note.D6 to 0.15, Note.G6 to 0.5
            )

            for ((note, dur) in chords) {
                if (note == Note.REST) {
                    delay((dur * 1000).toLong())
                    continue
                }

                val numSamples = (sampleRate * dur).toInt()
                val samples = ShortArray(numSamples)
                val vol = 0.4f

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val angle = 2.0 * Math.PI * note.freq * t
                    val envelope = when {
                        i < 200 -> i / 200.0
                        i > numSamples - 600 -> (numSamples - i) / 600.0
                        else -> 1.0
                    }
                    val wave = sin(angle)
                    samples[i] = (wave * 32767.0 * vol * envelope).toInt().toShort()
                }

                withContext(Dispatchers.IO) {
                    try {
                        track.write(samples, 0, samples.size)
                    } catch (e: Exception) {
                        Log.e("ProceduralAudio", "SFX write error: ${e.message}")
                    }
                }
            }
        }
    }

    /**
     * Plays a soft downward error sound effect.
     */
    fun playProceduralErrorSFX(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            val track = audioTrack ?: return@launch
            val notes = listOf(Note.FS4, Note.F4, Note.E4)
            val noteDuration = 0.12

            for (note in notes) {
                val numSamples = (sampleRate * noteDuration).toInt()
                val samples = ShortArray(numSamples)
                val vol = 0.25f

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val angle = 2.0 * Math.PI * note.freq * t
                    val envelope = when {
                        i < 100 -> i / 100.0
                        i > numSamples - 300 -> (numSamples - i) / 300.0
                        else -> 1.0
                    }
                    // Soft triangle-like wave for error to make it less abrasive
                    val sine = sin(angle)
                    val wave = (sine + 0.3 * sin(3.0 * angle)) / 1.3
                    samples[i] = (wave * 32767.0 * vol * envelope).toInt().toShort()
                }

                withContext(Dispatchers.IO) {
                    try {
                        track.write(samples, 0, samples.size)
                    } catch (e: Exception) {
                        Log.e("ProceduralAudio", "SFX write error: ${e.message}")
                    }
                }
            }
        }
    }

    fun release() {
        stopMusicLoop()
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {}
        audioTrack = null
    }
}

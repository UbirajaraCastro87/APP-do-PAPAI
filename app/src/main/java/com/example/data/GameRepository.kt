package com.example.data

import kotlinx.coroutines.flow.Flow

class GameRepository(private val progressDao: ProgressDao) {

    val allProgress: Flow<List<UserProgress>> = progressDao.getAllProgress()

    suspend fun getProgressForPhase(phaseId: String): UserProgress? {
        return progressDao.getProgressForPhase(phaseId)
    }

    suspend fun markPhaseCompleted(phaseId: String, stars: Int, score: Int) {
        val existing = progressDao.getProgressForPhase(phaseId)
        val maxStars = maxOf(existing?.stars ?: 0, stars)
        val maxScore = maxOf(existing?.score ?: 0, score)
        progressDao.insertProgress(
            UserProgress(
                phaseId = phaseId,
                completed = true,
                stars = maxStars,
                score = maxScore,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    suspend fun resetAllProgress() {
        progressDao.clearProgress()
    }
}

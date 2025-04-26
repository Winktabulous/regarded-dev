package com.near_reality.api.facade

import com.near_reality.api.model.Skill
import com.near_reality.api.responses.UserUpdateHiScoresResponse

interface HiscoresFacade {

    suspend fun updateHiscores(userId: Long, skills: List<Skill>): UserUpdateHiScoresResponse
}

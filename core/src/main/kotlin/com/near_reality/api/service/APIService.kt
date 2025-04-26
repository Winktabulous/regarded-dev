package com.near_reality.api.service

import com.zenyte.game.GameConstants
import org.slf4j.LoggerFactory

abstract class APIService {

    protected val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    val enabled: Boolean get() = GameConstants.WORLD_PROFILE.isApiEnabled()
}

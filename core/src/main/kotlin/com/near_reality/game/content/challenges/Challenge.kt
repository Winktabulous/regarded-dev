package com.near_reality.game.content.challenges

import mgi.types.config.StructDefinitions
import java.util.function.Function

class Challenge<T>(
    val structId: Int,
    val name: String,
    @field:Transient val function: Function<T, Int>,
    @field:Transient val maxCount: Int = StructDefinitions.get(structId).getParamAsInt(5011)
)

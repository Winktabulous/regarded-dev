package com.near_reality.scripts.`object`

import com.near_reality.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object ObjectScriptCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "com.zenyte.game.world.object.ObjectId",
            "com.zenyte.game.world.object.ObjectId.*",

            "com.zenyte.game.world.object.*"
        )
    })
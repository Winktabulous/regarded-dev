package com.near_reality.scripts.interfaces

import com.near_reality.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.with

/**
 * @author Jire
 */
object InterfaceCompilation : ScriptCompilationConfiguration(
    DefaultCompilation.with {
        defaultImports(
            "com.zenyte.game.model.ui.InterfacePosition.*",

            "com.zenyte.game.GameInterface",
            "com.zenyte.game.GameInterface.*",

            "com.zenyte.game.util.AccessMask",
            "com.zenyte.game.util.AccessMask.*",

            "mgi.types.config.enums.Enums",
            "mgi.types.config.enums.Enums.*",
        )
    }
) {
    private fun readResolve(): Any = InterfaceCompilation
}
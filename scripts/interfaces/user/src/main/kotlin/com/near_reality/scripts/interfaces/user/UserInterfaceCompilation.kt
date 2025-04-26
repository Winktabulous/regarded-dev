package com.near_reality.scripts.interfaces.user

import com.near_reality.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object UserInterfaceCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "com.zenyte.game.model.ui.InterfacePosition.*",
            "com.zenyte.game.GameInterface.*"
        )
    })
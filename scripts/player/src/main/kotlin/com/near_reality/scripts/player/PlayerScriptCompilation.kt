package com.near_reality.scripts.player

import com.near_reality.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration

/**
 * Represents a [compilation configuration][ScriptCompilationConfiguration] for [player scripts][PlayerScript].
 *
 * @author Stan van der Bend
 */
object PlayerScriptCompilation : ScriptCompilationConfiguration(DefaultCompilation)

package com.near_reality.scripts

import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object CoreCompilation : ScriptCompilationConfiguration({
    defaultImports(
        "com.zenyte.game.world.entity.masks.Animation",
        "com.zenyte.game.world.entity.masks.Graphics",
        "com.zenyte.game.world.entity.SoundEffect",
        "com.zenyte.game.world.Projectile"
    )
})
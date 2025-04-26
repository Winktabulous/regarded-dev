package com.near_reality.scripts.item.actions

import com.near_reality.plugins.item.actions.ItemActionPlugin
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "Item Action Script",
    fileExtension = "itemaction.kts",
    compilationConfiguration = ItemActionCompilation::class
)
abstract class ItemActionScript : ItemActionPlugin()
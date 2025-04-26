package com.near_reality.scripts.ground_items

import com.near_reality.scripts.Script
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.flooritem.GlobalItem
import com.zenyte.plugins.InitPlugin
import com.zenyte.plugins.PluginPriority
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "Ground Item Spawn Script",
    "grounditems.kts",
    compilationConfiguration = GroundItemCompilation::class
)
@PluginPriority(500)
abstract class GroundItemSpawnScript : Script, InitPlugin {

    operator fun Int.invoke(
        amount: Int = 1,
        x: Int, y: Int, z: Int = 0,
        respawnTime: Int = 30
    ) {
        val item = Item(this, amount)
        val location = Location(x, y, z)
        val globalItem = GlobalItem(item, location, respawnTime)
        GlobalItem.createPersistentGlobalItemSpawn(globalItem)
    }

    operator fun Int.invoke(
        x: Int, y: Int, z: Int = 0,
        respawnTime: Int = 30
    ) = invoke(1, x, y, z, respawnTime)

}

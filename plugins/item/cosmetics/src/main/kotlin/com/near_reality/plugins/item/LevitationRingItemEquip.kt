package com.near_reality.plugins.item

import com.near_reality.scripts.item.equip.EquipContext
import com.near_reality.scripts.item.equip.ItemEquipScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.masks.RenderAnimation

class LevitationRingItemEquip : ItemEquipScript() {
    init {
        items(ItemId.RING_OF_LEVITATION)

        onLogin(::levitatePlayer)
        onEquip(::levitatePlayer)
        onUnEquip {
            player.appearance.resetRenderAnimation()
        }
    }

    fun levitatePlayer(context: EquipContext) {
        val task = WorldTask {
            context.player.appearance.renderAnimation = RenderAnimation(4424)
        }
        if (context is EquipContext.Login)
            WorldTasksManager.schedule(task, 2)
        else
            task.run()
    }

}


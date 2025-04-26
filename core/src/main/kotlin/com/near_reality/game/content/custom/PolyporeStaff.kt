package com.near_reality.game.content.custom

import com.near_reality.game.item.CustomItemId
import com.near_reality.game.model.item.degrading.Degradeable
import com.near_reality.game.world.entity.player.action.combat.ISpecialAttack
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.degradableitems.DegradeType
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript
import com.zenyte.game.world.entity.player.action.combat.SpecialType
import java.util.function.Function

/**
 * Represents a degradeable magic weapon similar to the toxic staff of the dead.
 *
 * @author Stan van der Bend
 */
object PolyporeStaff : Degradeable {

    override val type: DegradeType = DegradeType.SPELL
    override val itemId: Int = CustomItemId.POLYPORE_STAFF
    override val nextId: Int = CustomItemId.POLYPORE_STAFF_DEG
    override val maximumCharges: Int = 16_000
    override val minimumCharges: Int = 0
    override val function: Function<Item, Array<Item>>? = null

    object Special : ISpecialAttack {
        override val specialAttackName: String = "Ice Crack"
        override val weapons: IntArray = intArrayOf(CustomItemId.POLYPORE_STAFF, CustomItemId.POLYPORE_STAFF_DEG)
        override val delay: Int = 10
        override val type: SpecialType = SpecialType.MAGIC
        override val animation: Animation? = null
        override val graphics: Graphics? = null
        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->
            player.animation = Animation(9170)
            World.sendGraphics(Graphics(751), player.location)
            WorldTasksManager.schedule({
               player.graphics = null // in case of barrage gfx active, destroy it
               player.resetFreeze()
            }, 1)
        }
        override val attackType: AttackType = AttackType.MAGIC
    }
}

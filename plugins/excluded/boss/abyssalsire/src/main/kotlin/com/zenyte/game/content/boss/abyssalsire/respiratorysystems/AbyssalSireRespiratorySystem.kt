package com.zenyte.game.content.boss.abyssalsire.respiratorysystems

import com.zenyte.game.content.boss.abyssalsire.AbyssalSire
import com.zenyte.game.content.boss.abyssalsire.WeakReferenceHelper.invoke
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import org.slf4j.LoggerFactory
import java.lang.ref.WeakReference

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSireRespiratorySystem(
    location: Location,
    val sireRef: WeakReference<AbyssalSire>
) : NPC(NpcId.RESPIRATORY_SYSTEM, location, Direction.SOUTH, 0) {

    constructor(location: Location, sire: AbyssalSire) : this(location, WeakReference(sire))

    var state = State.NONE

    enum class State(
        val newVentID: Int = -1,
        val onSet: (AbyssalSireRespiratorySystem.(oldVent: WorldObject, newVent: WorldObject) -> Unit)? = null
    ) {
        NONE,
        GASSY(ObjectId.VENT_26953, { _, n -> World.spawnObject(n) }),
        SILENT(ObjectId.VENT_26954, { o, n ->
            World.sendObjectAnimation(o, ventFailureAnimation)
            WorldTasksManager.schedule({ World.spawnObject(n) }, 4)
        });

        fun set(respiratorySystem: AbyssalSireRespiratorySystem) {
            if (this == respiratorySystem.state) return

            try {
                val oldVent = World.getObjectOfSlot(respiratorySystem.location, 10)!!
                val newVent = WorldObject(oldVent).apply {
                    id = newVentID
                }
                onSet?.invoke(respiratorySystem, oldVent, newVent)
            } catch (e: Exception) {
                logger.error("", e)
            }

            respiratorySystem.state = this
        }
    }

    override fun spawn(): NPC {
        State.GASSY.set(this)
        return super.spawn()
    }

    override fun finish() {
        super.finish()
        State.SILENT.set(this)
    }

    override fun sendDeath() {
        WorldTasksManager.schedule(::finish)
    }

    override fun getXpModifier(hit: Hit): Float {
        if (sireRef.get()?.id == AbyssalSire.SIRE_THRONE_STUNNED_ID) return super.getXpModifier(hit)

        val hitSource = hit.source
        if (hitSource is Player)
            hitSource.sendFilteredMessage("You can't deal much damage with those tentacles getting in the way.")

        val originalDamage = hit.damage
        if (originalDamage > 3)
            hit.damage = Utils.random(1, 3)

        return super.getXpModifier(hit)
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(AbyssalSireRespiratorySystem::class.java)

        val ventFailureAnimation = Animation(7102)
    }

}
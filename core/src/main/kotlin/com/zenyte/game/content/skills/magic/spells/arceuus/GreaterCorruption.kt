package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.magic.spells.DefaultSpell
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.booleanVarbit
import com.zenyte.game.world.entity.player.varbit

/**
 * @author Kris | 19/06/2022
 */
var Player.corruptionSpellCooldown by booleanVarbit(12288)
var Player.corruptionType by varbit(12136)
private var Player.corruptionCycle by varbit(12141)
private var Player.corruptionOrbEffect by varbit(12142)

fun Player.applyCorruptionEffect(target: Entity) {
    if (target !is Player || corruptionType == 0) return
    if (target.hasWardOfArceuus) return
    val probability = if (corruptionType == 1) 33 else 66
    if (Utils.random(100) >= probability) return
    corruptionType = 0
    sendMessage("<col=ff289d>Your target has been corrupted!</col>")
    target.sendMessage("<col=ef0083>You have been corrupted!</col>")
    target.corruptionCycle = 1
    target.corruptionOrbEffect = 3
    target.packetDispatcher.sendClientScript(82, 10485773, 10485776, 10485778, 10485777, 10485780, 10485774, 10485775)
    target.scheduleCorruptionEffect(if (hasMarkOfDarkness) 5 else 10)
}

fun Player.scheduleCorruptionEffect(interval: Int) = WorldTasksManager.schedule(object : TickTask() {
    override fun run() {
        val nextCycle = ++corruptionCycle
        val drainAmount = nextCycle.dec()
        prayerManager.prayerPoints -= drainAmount
        applyHit(Hit(drainAmount, HitType.CORRUPTION))
        if (nextCycle == 4) {
            sendMessage("<col=ef0083>You are no longer afflicted with corruption.</col>")
            corruptionCycle = 0
            corruptionOrbEffect = 0
            packetDispatcher.sendClientScript(82, 10485773, 10485776, 10485778, 10485777, 10485780, 10485774, 10485775)
            stop()
        }
    }
}, interval.dec(), interval)

class GreaterCorruption : DefaultSpell {
    override fun getDelay(): Int {
        return 1000
    }

    override fun getSpellbook(): Spellbook {
        return Spellbook.ARCEUUS
    }

    override fun spellEffect(player: Player, optionId: Int, option: String): Boolean {
        if (player.corruptionSpellCooldown) {
            player.sendMessage("You can only cast corruption spells every 30 seconds.")
            return false
        }
        player.animation = Animation(8977)
        player.graphics = Graphics(1878)
        World.sendSoundEffect(player.location, SoundEffect(5045, 10))
        player.corruptionSpellCooldown = true
        player.corruptionType = 2
        addXp(player, 95.0)
        player.corruptionCooldown()
        return true
    }
}

fun Player.corruptionCooldown() = WorldTasksManager.schedule({
    corruptionSpellCooldown = false
}, 50)
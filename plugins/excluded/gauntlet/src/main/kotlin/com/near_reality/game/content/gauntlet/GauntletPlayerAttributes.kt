package com.near_reality.game.content.gauntlet

import com.near_reality.game.content.gauntlet.rewards.GauntletRewardType
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player

var Player.spokenToBrynn: Boolean
    get() = attributes.getOrDefault(GauntletConstants.DIALOGUE_ATTRIBUTE_KEY, false) as Boolean
    set(value) {
        attributes[GauntletConstants.DIALOGUE_ATTRIBUTE_KEY] = value
    }

var Player.gauntlet: Gauntlet? by attribute("gauntlet", null)

var Player.gauntletWeakMonsterKills: Int by attribute("gauntletWeakMonsterKills", 0)
var Player.gauntletStrongMonsterKills: Int by attribute("gauntletStrongMonsterKills", 0)

var Player.gauntletReceivedWeaponFrame: Boolean by attribute("gauntletReceivedWeaponFrame", false)

var Player.gauntletReceivedOrb: Boolean by attribute("gauntletReceivedOrb", false)
var Player.gauntletReceivedSpike: Boolean by attribute("gauntletReceivedSpike", false)
var Player.gauntletReceivedBowString: Boolean by attribute("gauntletReceivedBowString", false)

var Player.gauntletHasCape: Boolean by persistentAttribute("gauntletHasCape", false)

var Player.gauntletRewardType: GauntletRewardType?
    get() = (attributes["gauntletRewardType"] as? String)?.let { GauntletRewardType.valueOf(it) }
    set(value) {
        if (value == null)
            attributes.remove("gauntletRewardType")
        else
            attributes["gauntletRewardType"] = value.name
    }
var Player.gauntletTypeForReward: GauntletType?
    get() = (attributes["gauntletTypeForReward"] as? String)?.let { GauntletType.valueOf(it) }
    set(value) {
        if (value == null)
            attributes.remove("gauntletTypeForReward")
        else
            attributes["gauntletTypeForReward"] = value.name
    }

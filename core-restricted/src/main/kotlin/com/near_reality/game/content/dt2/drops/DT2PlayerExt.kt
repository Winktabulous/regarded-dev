package com.near_reality.game.content.dt2.drops

import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player
import it.unimi.dsi.fastutil.objects.ObjectArrayList


var Player.hasReceivedStrangledTablet by persistentAttribute("dt2_hasReceivedStrangledTablet", false)
var Player.hasReceivedBloodQuartz by persistentAttribute("dt2_hasReceivedBloodQuartz", false)
var Player.hasKilledVardorvisAwakened by persistentAttribute("dt2_hasKilledVardorvisAwakened", false)
var Player.hasKilledDukeAwakened by persistentAttribute("dt2_hasKilledDukeAwakened", false)
var Player.hasReceivedFrozenTablet by persistentAttribute("dt2_hasReceivedFrozenTablet", false)
var Player.hasReceivedIceQuartz by persistentAttribute("dt2_hasReceivedIceQuartz", false)

package com.zenyte.game.content.theatreofblood.room

import com.zenyte.game.content.theatreofblood.room.maidenofsugadinti.MaidenOfSugadintiRoom
import com.zenyte.game.content.theatreofblood.room.nylocas.NylocasRoom
import com.zenyte.game.content.theatreofblood.room.pestilentbloat.PestilentBloatRoom
import com.zenyte.game.content.theatreofblood.room.reward.RewardRoom
import com.zenyte.game.content.theatreofblood.room.sotetseg.ShadowRealmRoom
import com.zenyte.game.content.theatreofblood.room.sotetseg.SotetsegRoom
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.content.theatreofblood.room.xarpus.XarpusRoom
import kotlin.reflect.KClass

/**
 * @author Jire
 * @author Tommeh
 */
internal enum class TheatreRoomType(
    val roomName: String,
    val wave: Int,
    val chunkX: Int,
    val chunkY: Int,
    val sizeX: Int,
    val sizeY: Int,
    val roomClass: KClass<out TheatreRoom>
) {

    THE_MAIDEN_OF_SUGADINTI("The Maiden of Sugadinti", 1, 392, 552, 12, 7, MaidenOfSugadintiRoom::class),
    THE_PESTILENT_BLOAT("The Pestilent Bloat", 2, 408, 552, 8, 8, PestilentBloatRoom::class),
    THE_NYLOCAS("The Nylocas", 3, 408, 528, 8, 8, NylocasRoom::class),
    SOTETSEG("Sotetseg", 4, 408, 536, 8, 8, SotetsegRoom::class),
    SHADOW_REALM("Shadow Realm", 4, 416, 536, 8, 8, ShadowRealmRoom::class),
    XARPUS("Xarpus", 5, 392, 544, 8, 8, XarpusRoom::class),
    VERZIK_VITUR("The Final Challenge", 6, 392, 536, 8, 8, VerzikViturRoom::class),
    REWARDS("Verzik Vitur's Vault", 7, 400, 536, 8, 8, RewardRoom::class);

    companion object {
        val values = entries.toTypedArray()
    }

}
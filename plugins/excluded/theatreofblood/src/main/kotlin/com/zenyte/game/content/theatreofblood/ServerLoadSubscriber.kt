package com.zenyte.game.content.theatreofblood

import com.google.common.eventbus.Subscribe
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikConfigs
import com.zenyte.game.world.entity.player.collectionlog.CollectionLogCategories
import com.zenyte.plugins.events.ServerLaunchEvent
import java.util.function.Function

/**
 * @author Jire
 */
object ServerLoadSubscriber {

    @Subscribe
    @JvmStatic
    fun onServerLaunch(@Suppress("UNUSED_PARAMETER") event: ServerLaunchEvent) {
        VerzikConfigs.configs()
        TheatreOfBloodScoresSerializer.read()
        CollectionLogCategories.register(
            "theatre of blood",
            Function { player -> player.tobStats.completions },
            Function { 0 },
            Function { player -> player.tobStatsHard.completions })
    }

}
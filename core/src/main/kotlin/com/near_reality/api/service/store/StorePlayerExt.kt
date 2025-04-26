package com.near_reality.api.service.store

import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue

fun Player.notify(message: String, schedule: Boolean = false, loading: Boolean = false) {
    fun notifyPlayerHandle() {
        sendMessage(message)
        dialogue {
            if (loading)
                loading(message)
            else
                plain(message)
        }
    }
    if (schedule)
        WorldTasksManager.schedule { notifyPlayerHandle() }
    else
        notifyPlayerHandle()
}

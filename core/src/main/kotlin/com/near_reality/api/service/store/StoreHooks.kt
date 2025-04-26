package com.near_reality.api.service.store

import com.google.common.eventbus.Subscribe
import com.zenyte.plugins.events.LoginEvent

/**
 * Represents the hooks for store related events.
 */
@Suppress("unused")
internal object StoreHooks {

    @Subscribe
    @JvmStatic
    fun onLogin(loginEvent: LoginEvent) {
        val player = loginEvent.player
        val claimedOrdersToMention = player.storeClaimedOrdersToMentionOnNextLogin
        if (claimedOrdersToMention.isNotEmpty()) {
            val totalCredits = claimedOrdersToMention.sumOf { it.totalCredits }
            player.notify("A total of $totalCredits credits have been added to your account!", schedule = false)
            player.storeClaimedOrdersToMentionOnNextLogin.clear()
        }
    }
}

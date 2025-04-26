package com.near_reality.api.service.user

import com.near_reality.api.model.User
import com.near_reality.api.util.toMemberRank
import com.near_reality.api.util.toPrivilege
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.privilege.MemberRank


fun Player.onSetUser(value: User?) {
    playerInformation.userIdentifier = value?.id?.toInt()?:-1
    val previousRank = memberRank
    memberRank = value?.memberRank?.toMemberRank() ?: MemberRank.NONE
    val previousPrivilege = privilege
    privilege = value?.privilege?.toPrivilege() ?: previousPrivilege
    if (isLoggedIn) {
        sendDeveloperMessage(if (value == null) "User set to null, oh no?" else "User updated")
        try {
            if (previousRank != memberRank && attributes.remove("HOME_TELEPORT") != null)
                sendMessage(Colour.RED.wrap("Your saved home teleport was removed because of the rank change! Talk to Squire to set it again."))
            updateDonationInfoOnPlayerDetailsTab()
        } catch(ignored: Exception) {
            // user offline
        }
    }
}

/**
 * The total amount of dollars the player has spent in the store.
 */
val Player.storeTotalSpent get() = user?.totalSpent ?: 0

/**
 * The amount of spendable store credits the player has.
 */
val Player.storeCredits get() = user?.storeCredits ?: 0

/**
 * Whether the player has two-factor authentication enabled.
 */
val Player.twoFactorEnabled get() = user?.twoFactorEnabled ?: false

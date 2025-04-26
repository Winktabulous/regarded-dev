package com.near_reality.content.group_ironman.player

import com.near_reality.content.group_ironman.IronmanGroup
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player
import java.util.UUID

var Player.leftTheNode by persistentAttribute("leftTheNode", false)

/**
 * Gets the [IronmanGroup] this [Player] is a member of or is in the process of leaving,
 * or `null` if the [Player] is in no such group.
 */
var Player.finalisedIronmanGroup: IronmanGroup?
    get() {
        val uuid = attributes["ironmanGroupUUID"]?.let { (it as? String)?.let(UUID::fromString) }
        return if (uuid == null) {
            val group = IronmanGroup.find(this)
            if (group != null)
                attributes["ironmanGroupUUID"] = group.uuid.toString()
            return group
        } else {
            val group = IronmanGroup.find(uuid)
            if (group != null) {
                if (group.isMemberAndInGracePeriod(this)) {
                    group
                } else {
                    attributes.remove("ironmanGroupUUID")
                    null
                }
            } else
                null
        }
    }
    set(value) {
        if (value == null)
            attributes.remove("ironmanGroupUUID")
        else
            attributes["ironmanGroupUUID"] = value.uuid.toString()
    }

/**
 * A non-finalised [IronmanGroup] that the [Player] is a member of.
 */
var Player.pendingIronmanGroup: IronmanGroup? by attribute("pendingIronmanGroup", null)

/**
 * TODO: figure out what my idea for this was, currently not set anywhere
 */
var Player.pendingIronmanGroupApplication: String? by attribute("pendingIronmanGroupApplication", null)

/**
 * Timestamp (in millis) of the last time this [Player] used the GIM helm teleport option.
 */
var Player.groupIronHelmTeleportLastTime by attribute("groupIronHelmTeleportLastTime", 0L)

/**
 * Whether this [Player] is inside the iron man group creation interface.
 */
var Player.inIronmanGroupCreationInterface: Boolean
    get() = getBooleanAttribute("creatingIronmanGroup")
    set(value) {
        putBooleanAttribute("creatingIronmanGroup", value)
        varManager.sendBitInstant(13058, if (value) 1 else 0)
        trySetApplyOrInvitePlayerOption()
    }

/**
 * Whether this [Player] is inside the iron man group invite mode during group creation.
 */
var Player.ironmanGroupInvitingMode: Boolean
    get() = getBooleanAttribute("ironmanGroupInvitingMode")
    set(value) {
        if (finalisedIronmanGroup == null)
            sendDeveloperMessage("Can only set `ironmanGroupInvitingMode` property when in a (non-pending) group. ")
        else
            putBooleanAttribute("ironmanGroupInvitingMode", value)
    }

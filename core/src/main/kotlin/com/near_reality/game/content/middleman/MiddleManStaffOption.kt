package com.near_reality.game.content.middleman

import com.google.gson.*
import com.near_reality.game.content.middleman.trade.request.MiddleManTradeRequest
import com.near_reality.game.content.middleman.trade.request.MiddleManTradeRequestInterface
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.privilege.Crown
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import java.lang.reflect.Type

/**
 * Represents an option that the [MiddleManTradeRequest.requester] can select
 * in the [MiddleManTradeRequestInterface] in a drop-down menu.
 *
 * @author Stan van der Bend
 */
sealed class MiddleManStaffOption {

    /**
     * Check whether argued [staff] member is able to view MM requests
     * that selected this option.
     */
    abstract fun applies(staff: Player): Boolean

    /**
     * Request for a specific staff member to handle the trade.
     *
     * @param username  the [Player.getUsername] of the staff member.
     * @param crown     the [Crown] associated with the ][Player.privilege] of the staff member.
     */
    class Specific(val username: String, val crown: Crown) : MiddleManStaffOption() {

        override fun applies(staff: Player): Boolean =
            staff.hasPrivilege(PlayerPrivilege.ADMINISTRATOR)
                    || staff.username.equals(username)

        /**
         * String representation to display in the [MiddleManTradeRequestInterface] drop-down menu.
         */
        override fun toString(): String = "${crown.crownTag} $username"
    }

    /**
     * Request for any staff member to handle the trade.
     */
    object Any : MiddleManStaffOption() {

        override fun applies(staff: Player): Boolean = true

        /**
         * String representation to display in the [MiddleManTradeRequestInterface] drop-down menu.
         */
        override fun toString(): String = Colour.GREEN.wrap("Any Staff Member")
    }

    object GsonAdapter : JsonSerializer<MiddleManStaffOption>, JsonDeserializer<MiddleManStaffOption> {

        override fun serialize(
            src: MiddleManStaffOption,
            typeOfSrc: Type,
            context: JsonSerializationContext,
        ): JsonElement {
            val result = JsonObject()
            result.addProperty("type", when(src) {
                Any -> "Any"
                is Specific -> "Specific"
            })
            if (src is Specific){
                result.addProperty("username", src.username)
                result.addProperty("crown", src.crown.name)
            }
            return result
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext,
        ): MiddleManStaffOption {
            val obj = json.asJsonObject
            return when(obj.getAsJsonPrimitive("type").asString) {
                "Specific" -> Specific(
                    obj.getAsJsonPrimitive("username").asString,
                    Crown.valueOf(obj.getAsJsonPrimitive("crown").asString)
                )
                else -> Any
            }
        }
    }
}

package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Grand bookshelves can be found inside Verzik Vitur's treasure vault, after defeating her in the Theatre of Blood.
 * They can be searched for a collection of books:
 *
 *  1. Serafina's Diary
 *  2. The Butcher
 *  3. Arachnids of Vampyrium
 *  4. The Shadow Realm
 *  5. The Wild Hunt
 *  6. Verzik Vitur - Patient Record
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class GrandBookshelvesObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (option == "Search") {
            val bookId = bookItemIds.random()
            player.sendDeveloperMessage("TODO: figure out how this works on OSRS")
        }
    }

    override fun getObjects() = arrayOf(
        ObjectId.GRAND_BOOKSHELF,
        ObjectId.GRAND_BOOKSHELF_33001,
        ObjectId.GRAND_BOOKSHELF_33002,
        ObjectId.GRAND_BOOKSHELF_33003
    )

    private companion object {
        val bookItemIds = arrayOf(
            ItemId.SERAFINAS_DIARY,
            ItemId.THE_BUTCHER,
            ItemId.ARACHNIDS_OF_VAMPYRIUM,
            ItemId.THE_SHADOW_REALM,
            ItemId.THE_WILD_HUNT,
            ItemId.VERZIK_VITUR__PATIENT_RECORD
        )
    }
}

package com.near_reality.tools.backups.player

import com.near_reality.buffer.use
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import java.io.File

data class PlayerItems(
    val inventoryContainer: Int2ObjectLinkedOpenHashMap<Item>,
    val equipmentContainer: Int2ObjectLinkedOpenHashMap<Item>,
    val lootingBagContainer: Int2ObjectLinkedOpenHashMap<Item>,
    val bankContainer: Int2ObjectLinkedOpenHashMap<Item>,
    val retrievalContainer: Int2ObjectLinkedOpenHashMap<Item>,
) {
    companion object {

        fun of(player: Player) = PlayerItems(
            player.inventory.container.items,
            player.equipment.container.items,
            player.lootingBag.container.items,
            player.bank.container.items,
            player.retrievalService.container.items,
        )

        fun from(file: File) =
            PlayerItemsDecoder().decode(file.readBytes())

        fun fromFolder(file: File) =
            file.listFiles().mapNotNull(Companion::from)
    }
}

private const val largeSlotOffset = 255

class PlayerItemsDecoder {

    fun decode(bytes: ByteArray): PlayerItems = Unpooled.wrappedBuffer(bytes).use {
        PlayerItems(
            inventoryContainer = it.readContainer(),
            equipmentContainer = it.readContainer(),
            lootingBagContainer = it.readContainer(),
            bankContainer = it.readContainer(),
            retrievalContainer = it.readContainer()
        )
    }

    private fun ByteBuf.readContainer(): Int2ObjectLinkedOpenHashMap<Item> {
        val items = Int2ObjectLinkedOpenHashMap<Item>()
        var signifier = readUnsignedByte().toInt()
        while (signifier != 0) {
            val count = readUnsignedByte().toInt()
            repeat(count) {
                var slot = readUnsignedByte().toInt()
                if (signifier > 1)
                    slot += ((signifier - 1) * largeSlotOffset)
                val id = readUnsignedShort()
                val amount = readUnsignedInt().toInt()
                items[slot] = Item(id, amount)
            }
            signifier = readUnsignedByte().toInt()
        }
        return items
    }
}

class PlayerItemsEncoder {

    private val byteBuf by lazy {
        Unpooled.buffer(6000)
    }

    fun encode(items: PlayerItems): ByteArray {
        byteBuf.run {
            readerIndex(0)
            writerIndex(0)
        }
        items.inventoryContainer.write()
        items.equipmentContainer.write()
        items.lootingBagContainer.write()
        items.bankContainer.write()
        items.retrievalContainer.write()
        return ByteArray(byteBuf.readableBytes()).apply(byteBuf::readBytes)
    }

    private fun Int2ObjectLinkedOpenHashMap<Item>.write() {
        require(none { it.key >= 5 * largeSlotOffset }) {
            "Too many slots to encode, max slot is ${maxBy { it.key }}"
        }
        byteBuf.run {

            val smallSlots = filterKeys { it < largeSlotOffset }
            if (smallSlots.isNotEmpty())
                writeItemSlots(1, smallSlots)

            val bigSlots = filterKeys { it < 2 * largeSlotOffset  }
            if (bigSlots.isNotEmpty())
                writeItemSlots(2, bigSlots, largeSlotOffset)

            val veryBigSlots = filterKeys { it < 3 * largeSlotOffset }
            if (veryBigSlots.isNotEmpty())
                writeItemSlots(3, veryBigSlots, 2 * largeSlotOffset)

            val superBigSlots = filterKeys { it < 4 * largeSlotOffset }
            if (superBigSlots.isNotEmpty())
                writeItemSlots(4, superBigSlots, 3 * largeSlotOffset)

            val verySuperBigSlots = filterKeys { it < 5 * largeSlotOffset }
            if (verySuperBigSlots.isNotEmpty())
                writeItemSlots(5, verySuperBigSlots, 4 * largeSlotOffset)
        }
    }

    private fun ByteBuf.writeItemSlots(signifier: Int, bigSlots: Map<Int, Item>, offset: Int = 0) {
        writeByte(signifier)
        writeByte(bigSlots.size)
        for ((slot, item) in bigSlots) {
            writeByte(slot - offset)
            writeShort(item.id)
            writeInt(item.amount)
        }
    }
}



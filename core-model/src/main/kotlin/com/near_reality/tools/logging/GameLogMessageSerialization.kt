
package com.near_reality.tools.logging

import com.near_reality.tools.logging.GameLogMessage.*
import com.zenyte.game.content.grandexchange.ExchangeType
import com.zenyte.game.content.grandexchange._ExchangeOffer
import com.zenyte.game.item._Item
import com.zenyte.game.world.entity._Location
import com.zenyte.game.world.entity.player.container.ContainerPolicy
import com.zenyte.game.world.entity.player.container._Container
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

typealias SlotItemMap = @Contextual Int2ObjectLinkedOpenHashMap<out @Contextual _Item>

@InternalSerializationApi
@ExperimentalSerializationApi
val nrSerializerModule = SerializersModule {
    contextual(LocationSerializer)
    contextual(ItemSerializer)
    contextual(SlotItemMapSerializer)
    contextual(ItemContainerSerializer)
    contextual(ExchangeOfferSerializer)
    polymorphic(GameLogMessage::class) {
        subclass(FlowerPokerSession::class)
        subclass(Login::class)
        subclass(GrandExchangeOffer::class)
        subclass(GrandExchangeTransaction.Purchase::class)
        subclass(GrandExchangeTransaction.Sell::class)
        subclass(Duel::class)
        subclass(GroundItem.Drop::class)
        subclass(GroundItem.Pickup::class)
        subclass(Message.Clan::class)
        subclass(Message.Public::class)
        subclass(Message.Yell::class)
        subclass(Message.Private::class)
        subclass(RareDrop::class)
        subclass(Login::class)
        subclass(Logout::class)
        subclass(Trade::class)
        subclass(Death.Killed.ByNpc::class)
        subclass(Death.Killed.ByPlayer::class)
        subclass(Death.Misc::class)
        subclass(Command::class)
        subclass(Sanction::class)
    }
}

@InternalSerializationApi
@ExperimentalSerializationApi
object ItemContainerSerializer : KSerializer<_Container<_Item>> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Container") {
            element<ContainerType>("type")
            element<ContainerPolicy>("policy")
            element("items", SlotItemMapSerializer.descriptor)
        }

    override fun deserialize(decoder: Decoder): _Container<_Item>  =
        decoder.decodeStructure(descriptor) {
            var type: ContainerType? = null
            var policy: ContainerPolicy? = null
            var items: Int2ObjectLinkedOpenHashMap<_Item>? = null
            while (true) {
                when(val index = decodeElementIndex(descriptor)) {
                    0 -> type = ContainerType.valueOf(decodeStringElement(descriptor, 0))
                    1 -> policy = ContainerPolicy.valueOf(decodeStringElement(descriptor, 1))
                    2 -> items = decodeSerializableElement(descriptor, 2, SlotItemMapSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            _Container(type, policy, items)
        }

    override fun serialize(encoder: Encoder, value: _Container<_Item>) =
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type.name)
            encodeStringElement(descriptor, 1, value.policy.name)
            encodeSerializableElement(descriptor, 2, SlotItemMapSerializer, value.items)
        }
}

@ExperimentalSerializationApi
@InternalSerializationApi
object ExchangeOfferSerializer : KSerializer<_ExchangeOffer<_Item, _Container<_Item>>> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ExchangeOffer") {
            element<String>("username")
            element("item", ItemSerializer.descriptor)
            element<Int>("slot")
            element<Int>("price")
            element<ExchangeType>("type")
            element<Int>("amount")
            element<Boolean>("aborted")
            element<Boolean>("cancelled")
            element("container", ItemContainerSerializer.descriptor)
        }

    override fun deserialize(decoder: Decoder): _ExchangeOffer<_Item, _Container<_Item>>  =
        decoder.decodeStructure(descriptor) {
            var username: String? = null
            var item: _Item? = null
            var slot = -1
            var price = -1
            var type: ExchangeType? = null
            var amount = -1
            var aborted = false
            var cancelled = false
            var container: _Container<_Item>? = null
            while (true) {
                when(val index = decodeElementIndex(descriptor)) {
                    0 -> username = decodeStringElement(descriptor, 0)
                    1 -> item = decodeSerializableElement(descriptor, 1, ItemSerializer)
                    2 -> slot = decodeIntElement(descriptor, 2)
                    3 -> price = decodeIntElement(descriptor, 3)
                    4 -> type = ExchangeType.valueOf(decodeStringElement(descriptor, 4))
                    5 -> amount = decodeIntElement(descriptor, 5)
                    6 -> aborted = decodeBooleanElement(descriptor, 6)
                    7 -> cancelled = decodeBooleanElement(descriptor, 7)
                    8 -> container = decodeSerializableElement(descriptor, 8, ItemContainerSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            requireNotNull(username)
            requireNotNull(item)
            require(slot != -1)
            require(price != -1)
            requireNotNull(type)
            require(amount != -1)
            requireNotNull(container)
            _ExchangeOffer<_Item, _Container<_Item>>(username, item, container, slot, price, type).apply {
                this.amount = amount
                this.isAborted = aborted
                this.isCancelled = cancelled
            }
        }

    override fun serialize(encoder: Encoder, value: _ExchangeOffer<_Item, _Container<_Item>>) =
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.username)
            encodeSerializableElement(descriptor, 1, ItemSerializer, value.item)
            encodeIntElement(descriptor, 2, value.slot)
            encodeIntElement(descriptor, 3, value.price)
            encodeStringElement(descriptor, 4, value.type.name)
            encodeIntElement(descriptor, 5, value.amount)
            encodeBooleanElement(descriptor, 6, value.isAborted)
            encodeBooleanElement(descriptor, 7, value.isCancelled)
            encodeSerializableElement(descriptor, 8, ItemContainerSerializer, value.container)
        }
}

object LocationSerializer : KSerializer<_Location> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Location") {
            element<Int>("hash")
        }

    override fun deserialize(decoder: Decoder): _Location  =
        decoder.decodeStructure(descriptor) {
            var hash: Int = -1
            while (true) {
                when(val index = decodeElementIndex(descriptor)) {
                    0 -> hash = decodeIntElement(descriptor, 0)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            require(hash != -1)
            _Location(hash)
        }

    override fun serialize(encoder: Encoder, value: _Location) =
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.positionHash)
        }
}

@InternalSerializationApi
@ExperimentalSerializationApi
object SlotItemMapSerializer : KSerializer<Int2ObjectLinkedOpenHashMap<_Item>> {

    private val slotsSerializer = IntArraySerializer()
    private val itemsSerializer = ArraySerializer(_Item::class, ItemSerializer)

    override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("ItemSlotMap") {
                element<IntArray>("slots")
                element("items", itemsSerializer.descriptor)
            }

    override fun deserialize(decoder: Decoder): Int2ObjectLinkedOpenHashMap<_Item> =
        decoder.decodeStructure(descriptor) {
            var slots: IntArray? = null
            var items: Array<_Item>? = null
            while (true) {
                when(val index = decodeElementIndex(descriptor)) {
                    0 -> slots = decodeSerializableElement(descriptor, 0, slotsSerializer)
                    1 -> items = decodeSerializableElement(descriptor, 1, itemsSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            requireNotNull(slots)
            requireNotNull(items)
            Int2ObjectLinkedOpenHashMap(slots, items)
        }

    override fun serialize(encoder: Encoder, value: Int2ObjectLinkedOpenHashMap<_Item>) =
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, slotsSerializer, value.keys.toIntArray())
            encodeSerializableElement(descriptor, 1, itemsSerializer, value.values.toTypedArray())
        }
}

@InternalSerializationApi
@ExperimentalSerializationApi
object ItemSerializer : KSerializer<_Item> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Item") {
            element<Int>("id")
            element<Int>("amount")
        }

    override fun deserialize(decoder: Decoder): _Item =
        decoder.decodeStructure(descriptor) {
            var id = -1
            var amount = -1
            while (true) {
                when(val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeIntElement(descriptor, 0)
                    1 -> amount = decodeIntElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            require(id != -1)
            require(amount != -1)
            _Item(id, amount)
        }

    override fun serialize(encoder: Encoder, value: _Item) =
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.id)
            encodeIntElement(descriptor, 1, value.amount)
        }

}

//package com.near_reality.backups
//
//import com.near_reality.game.GameTestExtension
//import com.near_reality.game.IncludeCacheDefinitions
//import com.near_reality.game.item.CustomItemId
//import PlayerItems
//import PlayerItemsDecoder
//import PlayerItemsEncoder
//import com.zenyte.game.item.Item
//import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
//import it.unimi.dsi.fastutil.longs.LongArrayList
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//import mgi.types.config.items.ItemDefinitions
//import org.junit.jupiter.api.extension.ExtendWith
//import kotlin.system.measureNanoTime
//import kotlin.test.Test
//import kotlin.test.assertContentEquals
//import kotlin.test.assertEquals
//import kotlin.test.assertNotNull
//
///**
// * Test for correct and efficient encoding of a player their item containers.
// *
// * @author Stan van der Bend
// */
//@ExtendWith(GameTestExtension::class)
//@IncludeCacheDefinitions(ItemDefinitions::class)
//class PlayerItemsCodec {
//
//    private val items = PlayerItems(
//        inventoryContainer = Int2ObjectLinkedOpenHashMap(
//            mapOf(
//                0 to Item(1, 1),
//                10 to Item(CustomItemId.POLYPORE_STAFF, Int.MAX_VALUE),
//            )
//        ),
//        equipmentContainer = Int2ObjectLinkedOpenHashMap(
//            mapOf(
//                251 to Item(2, 20),
//                252 to Item(3, 30),
//                253 to Item(4, 40),
//                254 to Item(5, 50),
//                255 to Item(1, 10),
//            )
//        ),
//        lootingBagContainer = Int2ObjectLinkedOpenHashMap(
//            mapOf(
//                400 to Item(1, 1)
//            )
//        ),
//        bankContainer = Int2ObjectLinkedOpenHashMap(
//            (0..365).associateWith {
//                Item(it * 100, it * 100)
//            }
//        ),
//        retrievalContainer = Int2ObjectLinkedOpenHashMap(
//            (0..40).associateWith {
//                Item(it * 100, it * 100)
//            }
//        ),
//    )
//
//    @Test
//    fun `test codec`() {
//
//        val encoded = PlayerItemsEncoder().encode(items)
//        assertNotNull(encoded)
//        println("size ${encoded.size}")
//
//        val decoded = PlayerItemsDecoder().decode(encoded)
//
//        assertEquals(items.inventoryContainer.keys, decoded.inventoryContainer.keys)
//        assertEquals(items.equipmentContainer.keys, decoded.equipmentContainer.keys)
//        assertEquals(items.lootingBagContainer.keys, decoded.lootingBagContainer.keys)
//        assertEquals(items.bankContainer.keys, decoded.bankContainer.keys)
//
//        assertContentEquals(
//            items.inventoryContainer.values.toTypedArray(),
//            decoded.inventoryContainer.values.toTypedArray()
//        )
//        assertContentEquals(
//            items.equipmentContainer.values.toTypedArray(),
//            decoded.equipmentContainer.values.toTypedArray()
//        )
//        assertContentEquals(
//            items.lootingBagContainer.values.toTypedArray(),
//            decoded.lootingBagContainer.values.toTypedArray()
//        )
//        assertContentEquals(items.bankContainer.values.toTypedArray(), decoded.bankContainer.values.toTypedArray())
//    }
//
//    @Test
//    fun `benchmark code`() {
//
//
//        val encoded = PlayerItemsEncoder().encode(items)
//
//        val encodeTimesSingleThreaded = LongArrayList()
//        val encodeTimesCoroutines = LongArrayList()
//        val decodeTimes = LongArrayList()
//
//        val sharedEncoder = PlayerItemsEncoder()
//        repeat(1_000) {
//            encodeTimesSingleThreaded += measureNanoTime {
//                repeat(300) {
//                    sharedEncoder.encode(items)
//                }
//            }
//            encodeTimesCoroutines += measureNanoTime {
//                runBlocking {
//                    coroutineScope {
//                        repeat(300) {
//                            launch(Dispatchers.IO) {
//                                PlayerItemsEncoder().encode(items)
//                            }
//                        }
//                    }
//                }
//            }
//            decodeTimes += measureNanoTime {
//                PlayerItemsDecoder().decode(encoded)
//            }
//        }
//        println("encode1 (avg = ${encodeTimesSingleThreaded.average()}, min = ${encodeTimesSingleThreaded.min()})")
//        println("encode2 (avg = ${encodeTimesCoroutines.average()}, min = ${encodeTimesCoroutines.min()})")
//        println("decode (avg = ${decodeTimes.average()}, min = ${decodeTimes.min()})")
//    }
//}

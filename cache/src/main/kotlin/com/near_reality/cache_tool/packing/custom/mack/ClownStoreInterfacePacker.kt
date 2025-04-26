package com.near_reality.cache_tool.packing.custom.mack

import com.near_reality.cache.group
import com.near_reality.cache_tool.packing.assetsBase
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.GroupType
import mgi.tools.jagcached.cache.Cache
import mgi.tools.jagcached.cache.File
import mgi.types.config.InventoryDefinitions
import mgi.types.config.StructDefinitions
import mgi.types.config.enums.EnumDefinitions
import mgi.types.draw.sprite.SpriteGroupDefinitions
import mgi.utilities.ByteBuffer
import javax.imageio.ImageIO

class ClownStoreInterfacePacker(cache: Cache) : ClownInterfacePacker(cache){

    companion object {
        const val WIDGET_STORE_ID = 5101
        const val INV_MX_STORE_ITEMS = 1000
        const val INV_MX_STORE_CART = 1001
        const val INV_MX_STORE_PRICES = 1002
        const val INV_MX_STORE_STOCK = 1003
        const val INV_MX_STORE_DISCOUNTED = 1004
        const val INV_MX_STORE_LIMITED = 1005
        const val INV_MX_STORE_CART_PRICES = 1006
        const val VARBIT_MX_STORE_CREDITS = 17004
        const val VARBIT_MX_CHECKOUT_BUTTON_ENABLED = 17005
        const val VARBIT_MX_STORE_TAB_SELECTED = 17006
        const val VARBIT_MX_STORE_CATEGORY_SELECTED = 17007
    }

    override fun pack() {

        assetsBase("assets/osnr/store_interface/") {
            "cs2" {
                val groupId = name.substringBefore('.').substringBefore('_').toInt()
                packAsFileInGroup(ArchiveType.CLIENTSCRIPTS, groupId, fileId = 0)
            }
            "param" {
                val fileId = name.substringBefore('.').substringAfter('_').toInt()
                packAsFileInGroup(ArchiveType.CONFIGS, GroupType.PARAMS, fileId)
            }
            "struct" {
                val fileId = name.substringBefore('.').substringAfter('_').toInt()
                StructDefinitions(fileId).apply {
                    decode(mgiBuffer)
                }.apply {
                    when(fileId) {
                        12000 -> {
                            parameters[10_000] = "65 Credits"
                            parameters[10_001] = ""
                            parameters[10_002] = "$5"
                        }
                        12001 -> {
                            parameters[10_000] = "130 Credits"
                            parameters[10_001] = "+10 bonus credits"
                            parameters[10_002] = "$10"
                        }
                        12002 -> {
                            parameters[10_000] = "260 Credits"
                            parameters[10_001] = "+20 bonus credits"
                            parameters[10_002] = "$20"
                        }
                        12003 -> {
                            parameters[10_000] = "455 Credits"
                            parameters[10_001] = "+45 bonus credits"
                            parameters[10_002] = "$45"
                        }
                        12004 -> {
                            parameters[10_000] = "650 Credits"
                            parameters[10_001] = "+70 bonus credits"
                            parameters[10_002] = "$50.99"
                        }
                        12005 -> {
                            parameters[10_000] = "1300 Credits"
                            parameters[10_001] = "+200 bonus credits"
                            parameters[10_002] = "$100"
                        }
                    }
                    pack()
                    println("struct: $fileId")
                    println(printParams())
                }
            }
            "enum" {
                val fileId = name.substringBefore('.').substringAfter('_').toInt()
                packAsFileInGroup(ArchiveType.CONFIGS, GroupType.ENUM, fileId)
                EnumDefinitions().apply {
                    decode(mgiBuffer)
                }.apply {
                    if (values.values.contains("PayPal")) {
                        id = fileId
                        setValuesTyped(hashMapOf(
                            0 to "PayPal",
                            1 to "Coinbase"
                        ))
                        pack()
                    } else if(values.values.contains("Best Sellers")) {
                        id = fileId
                        setValuesTyped(hashMapOf(
                            0 to "Best Sellers",
                            1 to "Limited Time",
                            2 to "Pins",
                            3 to "Weapons",
                            4 to "Armory",
                            5 to "Supplies",
                            6 to "Boosts",
                            7 to "Pets",
                            8 to "Cosmetic",
                            9 to "Miscellaneous"
                        ))
                        pack()
                    } else if(values.containsValue(3116)){
                        id = fileId
                        setValuesTyped(
                            hashMapOf(
                                0 to 3116,
                                1 to 4525,
                                2 to 10020,
                                3 to 3144,
                                4 to 3163,
                                5 to 3231,
                                6 to 4567,
                                7 to 3068,
                                8 to 3935,
                                9 to 3235
                            )
                        )
                        pack()
                    } else if(values.containsValue(3290)) {
                        id = fileId
                        setValuesTyped(hashMapOf(
                            0 to 9000,
                            1 to 9001,
                            2 to 9002,
                            3 to 9003,
                            4 to 9004,
                            5 to 9005,
                            6 to 9006
                        ))
                        pack()
                    }else {
                        println(this)
                    }
                }
            }
            "inv" {
                val fileId = name.substringBefore('.').substringAfter('_').toInt()
                packAsFileInGroup(ArchiveType.CONFIGS, GroupType.INV, fileId)
                InventoryDefinitions(fileId, mgiBuffer).apply {
                    println(this)
                }
            }
            "if3" {
                val groupId = name.substringBefore('_').toInt()
                val fileId = name.substringBefore('.').substringAfter('_').toInt()
                packAsFileInGroup(ArchiveType.INTERFACES, groupId, fileId)
            }
            "varbit" {
                val fileId = name.substringBefore('.').substringAfter('_').toInt()
                packAsFileInGroup(ArchiveType.CONFIGS, GroupType.VARBIT, fileId)
            }
            "sprite" {
                val groupId = name.substringBefore('-').toInt()
                val fileId = name.substringBefore('.').substringAfter('-').toInt()
                val image = ImageIO.read(file)
                val sprite = SpriteGroupDefinitions(groupId, image.width, image.height)
                sprite.width = image.width
                sprite.height = image.height
                sprite.setImage(fileId, image)
                sprite.pack()
            }
        }
        newVarbitDefinition(VARBIT_MX_STORE_CREDITS, 4700, 0..31)
        newInventoryDefinition(INV_MX_STORE_CART_PRICES, 4706)

        val patcher = ClownInterfacePatcher(cache)
        // Store interface
        patcher.patchHooks(
            widgetId = WIDGET_STORE_ID,
            scriptIdMap = mapOf(
                12538 to 32008,
                12551 to 32021
            ),
            widgetIdMap = mapOf(
                863 to WIDGET_STORE_ID
            )
        )
    }

    private fun newInventoryDefinition(id: Int, size: Int? = null) {
        cache
            .getArchive(ArchiveType.CONFIGS)
            .group(GroupType.INV.id)
            .addFile(File(id, ByteBuffer().apply {
                if (size != null) {
                    writeByte(2)
                    writeShort(size)
                }
                writeByte(0)
            }))
    }

    private fun newVarbitDefinition(id: Int, varp: Int, bitRange: IntRange) {
        cache
            .getArchive(ArchiveType.CONFIGS)
            .group(GroupType.VARBIT.id)
            .addFile(File(id, ByteBuffer().apply {
                writeByte(1)
                writeShort(varp)
                writeByte(bitRange.first)
                writeByte(bitRange.last)
                writeByte(0)
            }))
    }
}

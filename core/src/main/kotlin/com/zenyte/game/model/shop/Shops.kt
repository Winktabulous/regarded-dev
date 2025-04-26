package com.zenyte.game.model.shop

import com.google.gson.GsonBuilder
import com.near_reality.game.world.entity.npc.spawns.OldSpawnKTSGenerator
import com.zenyte.game.item.ItemId
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import java.io.File

object Shops {

    fun load() {
        if (true) return // disable, because we use KTS instead.

        val itemIDToVars = OldSpawnKTSGenerator.idToVars(ItemId::class.java)

        val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
        try {
            val files = File("data/shops/").listFiles()!!
            for (file in files) {

                if (file.extension != "json")
                    continue

                val reader = file.reader()
                val jsonShop = gson.fromJson(reader, JsonShop::class.java)

                if (Shop.USE_INSTANCED_SHOPS) {
                    if (jsonShop.sellPolicy == ShopPolicy.CAN_SELL) {
                        val normalShop = Shop(jsonShop, false)
                        val ironmanShop = Shop(jsonShop, true)
                        repeat(Shop.SHOPS_DUPLICATOR_COUNT) {
                            val name = jsonShop.shopName + "|" + it
                            Shop.shops[name] = normalShop
                            Shop.ironmanShops[name] = ironmanShop
                        }
                    } else {
                        repeat(Shop.SHOPS_DUPLICATOR_COUNT) {
                            val name = jsonShop.shopName + "|" + it
                            Shop.shops[name] = Shop(jsonShop, false)
                            Shop.ironmanShops[name] = Shop(jsonShop, true)
                        }
                    }
                } else {
                    Shop.shops[jsonShop.shopName] = Shop(jsonShop, false)
                    Shop.ironmanShops[jsonShop.shopName] = Shop(jsonShop, true)
                }

                jsonShop.writeKTS(file.nameWithoutExtension, itemIDToVars)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun JsonShop.writeKTS(fileName: String, itemIDToVars: Int2ObjectMap<String>) {
        val folder = File("plugins/shops/src/main/kotlin/com/near_reality/plugins/shops")
        val file = File(folder, "${fileName}.shop.kts")
        if (file.exists()) return
        val kts = toKTS(itemIDToVars)
        file.writeText(kts)
    }

    private fun JsonShop.toKTS(itemIDToVars: Int2ObjectMap<String>): String {
        val sb = StringBuilder("package com.near_reality.plugins.shops\n\n")
        sb.append('"').append(shopName).append('"').append('(')
        if (shopUID != JsonShop.INVALID_SHOP_UID) {
            sb.append(shopUID)
            sb.append(',').append(' ')
        }
        sb.append("ShopCurrency.").append(currency.name)
        sb.append(',').append(' ').append(sellPolicy.name)
        if (sellMultiplier != 0F && sellMultiplier.toDouble() != Shop.DEFAULT_SELL_MULTIPLIER)
            sb.append(',').append(' ').append(sellMultiplier.toDouble())
        sb.append(')').append(' ').append('{').append('\n')
        for (item in items) {
            sb.append("    ")

            val id = item.id
            val idVar = itemIDToVars.get(id)
            if (idVar != null) sb.append(idVar)
            else sb.append(id)

            sb.append('(').append(item.amount)
                .append(',').append(' ').append(item.sellPrice)
                .append(',').append(' ').append(item.buyPrice)
            val appendRestockTimer = item.restockTimer != 0 && item.restockTimer != Shop.DEFAULT_RESTOCK_TIMER
            if (appendRestockTimer)
                sb.append(',').append(' ').append(item.restockTimer)
            if (item.isIronmanRestricted) {
                sb.append(',').append(' ')
                if (!appendRestockTimer) sb.append("ironmanRestricted = ")
                sb.append(item.isIronmanRestricted)
            }
            sb.append(')').append('\n')
        }

        sb.append('}')

        return sb.toString()
    }

}
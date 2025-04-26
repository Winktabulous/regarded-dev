package com.near_reality.scripts.item.equip

import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.plugins.equipment.equip.EquipPlugin
import it.unimi.dsi.fastutil.ints.IntArraySet
import it.unimi.dsi.fastutil.ints.IntSet
import kotlin.script.experimental.annotations.KotlinScript

/**
 * Represents a kotlin script template for defining [equip plugins][EquipPlugin].
 *
 * @author Stan van der Bend
 */
@KotlinScript(
    "Item Equip Script",
    fileExtension = "itemequip.kts",
    compilationConfiguration = ItemEquipCompilation::class
)
abstract class ItemEquipScript : EquipPlugin {

    private val items: IntSet = IntArraySet()
    private lateinit var handler: (EquipContext.Handler.() -> EquipHandlerResponse)
    private lateinit var equipListener: (EquipContext.Listener.() -> Unit)
    private lateinit var unEquipListener: (EquipContext.Listener.() -> Unit)
    private lateinit var loginListener: (EquipContext.Login.() -> Unit)

    fun items(vararg ids: Int) {
        for (id in ids) items.add(id)
    }

    fun items(ids: Collection<Int>) {
        items.addAll(ids)
    }

    override fun handle(player: Player, item: Item, slotId: Int, equipmentSlot: Int): Boolean {
        if (this::handler.isInitialized) {
            return when(handler(EquipContext.Handler(player, item, slotId, equipmentSlot))) {
                is EquipHandlerResponse.Continue -> true
                is EquipHandlerResponse.Negate -> false
            }
        }
        return true
    }

    override fun onEquip(player: Player, container: Container, equippedItem: Item) {
        if (this::equipListener.isInitialized)
            equipListener(EquipContext.Listener(player, equippedItem, container))
    }

    override fun onUnequip(player: Player, container: Container, unequippedItem: Item) {
        if (this::unEquipListener.isInitialized)
            unEquipListener(EquipContext.Listener(player, unequippedItem, container))
    }

    override fun onLogin(player: Player, item: Item, slot: Int) {
        if (this::loginListener.isInitialized)
            loginListener(EquipContext.Login(player, item, slot))
    }

    fun intercept(block: EquipContext.Handler.() -> EquipHandlerResponse) {
        handler = block
    }

    fun onEquip(block: EquipContext.Listener.() -> Unit) {
        equipListener = block
    }

    fun onUnEquip(block: EquipContext.Listener.() -> Unit) {
        unEquipListener = block
    }

    fun onLogin(block: EquipContext.Login.() -> Unit) {
        loginListener = block
    }

    override fun getItems(): IntArray = items.toIntArray()
}

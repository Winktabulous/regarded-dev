package com.near_reality.game.model.ui.credit_store

import com.google.common.eventbus.Subscribe
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializer
import com.near_reality.api.GameDatabase
import com.near_reality.api.model.*
import com.near_reality.api.service.store.StorePlayerHandler
import com.near_reality.api.service.user.storeCredits
import com.near_reality.game.content.middleman.promptDPinSelectionAndOpenMiddlemanInterface
import com.zenyte.game.GameInterface
import com.zenyte.game.item.Item
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.Colour
import com.zenyte.game.util.component
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.booleanVarbit
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerPolicy
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.Crown
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.entity.player.typeVarbit
import com.zenyte.game.world.entity.player.varp
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import com.zenyte.plugins.events.ServerLaunchEvent
import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import mgi.types.config.items.ItemDefinitions
import org.slf4j.LoggerFactory
import java.lang.reflect.Type
import java.util.*
import kotlin.time.Duration.Companion.seconds

private var Player.lastStoreUpdate by attribute("lastStoreUpdate", Instant.DISTANT_PAST)
private var Player.storeCreditsVarp by varp(4700)
private var Player.storeCheckoutButtonEnabled by booleanVarbit(17005)
private var Player.storeTabSelected by typeVarbit(17006, get=CreditStoreTab::get, set=CreditStoreTab::bitValue)
private var Player.storeCategorySelected by typeVarbit(17007, get=CreditStoreCategory::get, set=CreditStoreCategory::ordinal)
private var Player.storeCart: CreditStoreCart by attribute("storeCart", CreditStoreCart())
private var Player.askDonatorPinPrompt by persistentAttribute("askDonatorPinPrompt", true)
var coinbaseEnabled = false

@Suppress("unused")
class CreditStoreInterface : Interface() {

    override fun attach() {
        put(11, "buy_credits_button")
        put(12, "checkout_button")
        put(9, "tabs")
        put(18, "container")
        put(21, "close")
        put(22, "middleman_button")
        put(56, "categories_dropdown")
    }
    override fun build() {
        bind("buy_credits_button", CreditStoreModel::buyCredits)
        bind("checkout_button", CreditStoreModel::checkout)
        bind("tabs") { player, slotId, _, _ ->
            val tab = CreditStoreTab.findTab(slotId)
            CreditStoreModel.updateSelectedTabContents(player, tab)
            player.storeTabSelected = tab
        }
        bind("container") { player, slotId, _, option ->
            when (player.storeTabSelected) {
                CreditStoreTab.Perks -> Unit
                CreditStoreTab.Cart -> CreditStoreModel.onOptionCart(player, slotId, option)
                CreditStoreTab.Shop -> CreditStoreModel.onOptionShop(player, slotId, option)
            }
        }
        bind("close") { player ->
            player.interfaceHandler.closeInterface(this)
        }
        bind("categories_dropdown") { player, slotId, _, _ ->
            player.storeCategorySelected = findCategory(slotId)
            CreditStoreModel.updateSelectedTabContents(player, player.storeTabSelected)
        }
        bind("middleman_button") { player ->
            promptDPinSelectionAndOpenMiddlemanInterface(player)
        }
    }

    override fun open(player: Player) {
        if (player.getBooleanAttribute("registered")) {
            super.open(player)
            CreditStoreModel.sendUpdates(player)
        } else
            player.sendMessage("You must finish tutorial-island before opening the credit store.")
    }

    override fun getInterface(): GameInterface =
        GameInterface.CREDIT_STORE

}

object CreditStoreModel {

    private var lastUpdate = Instant.DISTANT_PAST

    private val storeProducts = mutableListOf<CreditStoreProduct>()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val logger = LoggerFactory.getLogger(CreditStoreModel::class.java)

    @Subscribe
    @JvmStatic
    fun onGameLaunched(serverLaunchEvent: ServerLaunchEvent) {
        logger.info("Starting store product fetcher, fetching products every 10 seconds.")

        scope.launch {
            delay(10.seconds)
            while(true) {
                try {
                    fetchProductFromDatabase()
                } catch (e: Exception) {
                    logger.error("Error fetching store products", e)
                } finally {
                   delay(30.seconds)
                }
            }
        }
    }

    fun requestProductsUpdate() {
        scope.launch {
            try {
                fetchProductFromDatabase()
            } catch (e: Exception) {
                logger.error("Error fetching store products", e)
            }
        }
    }

    private suspend fun fetchProductFromDatabase() {
        val products = GameDatabase.retrieveCreditStoreProducts()
        WorldTasksManager.schedule {
            updateProducts(products)
        }
    }

    private fun updateProducts(products: List<CreditStoreProduct>) {

        storeProducts.clear()
        storeProducts.addAll(products)

        lastUpdate = Clock.System.now()
    }

    fun buyCredits(player: Player) {
        GameInterface.CREDIT_PACKAGES.open(player)
    }

    fun checkout(player: Player, acceptDPinConfirmation: Boolean = false) {
        if (WildernessArea.isWithinWilderness(player)) {
            player.sendMessage("You cannot checkout in the wilderness.")
            return
        }
        if (player.duel?.inDuel() == true) {
            player.sendMessage("You cannot checkout while in a duel.")
            return
        }
        if (player.isUnderCombat) {
            player.sendMessage("You cannot checkout while in combat.")
            return
        }
        player.sendDeveloperMessage("Checking out store cart")
        val storeOrders = try {
            player.storeCart.map { CreditStoreCartItem(it.product, it.amount) }
        } catch (e: Exception) {
            onCartError(player, e)
            return
        }
        if (!acceptDPinConfirmation && player.askDonatorPinPrompt && storeOrders.any { Bond[it.product.itemId] != null }){
            player.dialogue {
                plain(Colour.RS_RED.wrap("!!Caution!!")+"<br> You are about to purchase a donator pin,<br>this will subtract their $ value from your total spent,<br> which may impact your donator rank!")
                options("Are you sure you want to continue?") {
                    "Yes" {
                        checkout(player, true)
                    }
                    "Yes, and don't ask me again!" {
                        player.askDonatorPinPrompt = false
                        checkout(player, true)
                    }
                    "No" {
                        GameInterface.CREDIT_STORE.open(player)
                    }
                    /*
                    Note: It is important to remember if you purchase a Donator Pin it will remove the total dollar amount of the Donator Pin. Example: If you purchase the $25.00 credit package your account will be upgraded to Premium, but if you buy a $10.00 Donator Pin, your total spent would be $15.00 resulting in the loss of your Premium Rank [PLEASE BE AWARE: YOUR BONUS CREDITS DO NOT COUNT TOWARDS Total Spent Value. Example: If you buy $50 worth of credits, you will receive 930 credits; however you wil only be able to spend 650 credits on pins!]
                     */
                    "How does this work?" {
                        player.dialogue {
                            val rank = "${Crown.PREMIUM.crownTag}<col=${MemberRank.PREMIUM.yellColor}>Premium</col>"

                            plain("When you buy a D-Pin it $ value will be subtracted from your total spent,<br> each donator rank has a minimum total spent requirement to maintain it.")
                            plain("For example the $rank rank requires a total spent of ${ApiMemberRank.PREMIUM.requiredDonatedAmount} to maintain it." +
                                    "<br>So if you have ${ApiMemberRank.PREMIUM.requiredDonatedAmount} dollar total spent, and you buy a 10$ D-Pin," +
                                    "<br>your total spent will be ${ApiMemberRank.PREMIUM.requiredDonatedAmount-10} dollars, and you will lose your Premium rank.").executeAction {
                                        checkout(player, false)
                            }
                        }

                    }
                }
            }
            return
        }
        StorePlayerHandler.checkout(player, storeOrders, onSuccess = {
            player.storeCart.clear()
            requestProductsUpdate()
            sendUpdates(player)
        })
    }

    private fun onCartError(player: Player, e: Exception) {
        player.storeCart.clear()
        player.sendMessage("The store has been updated, you cart is cleared, apologies, please try again.")
        logger.error("Error checking out store cart", e)
    }

    private fun checkUpdated(player: Player) {
        if (player.lastStoreUpdate > lastUpdate) {
            sendUpdates(player)
            player.sendMessage("The store has been updated.")
            player.lastStoreUpdate = lastUpdate
        }
    }

    fun sendUpdates(player: Player) {
        player.sendDeveloperMessage("Sending store updates")
        player.lastStoreUpdate = lastUpdate
        player.storeCreditsVarp = player.storeCredits
        player.storeCheckoutButtonEnabled = true
        player.packetDispatcher.apply {
            // Update item containers
            updateSelectedTabContents(player, player.storeTabSelected)
            // tab buttons
            sendComponentSettings(5101, 5101 component 9, 0, 26, AccessMask.CLICK_OP1)
            // checkout button
            sendComponentSettings(5101, 5101 component 12, 0, 13, AccessMask.CLICK_OP1)
            // shop categories dropdown
            sendComponentSettings(5101, 5101 component 56, 0, CreditStoreCategory.entries.size * 3, AccessMask.CLICK_OP1)
        }
    }

    fun onOptionCart(player: Player, slotId: Int, option: Int) {
        checkUpdated(player)
        val cart = player.storeCart
        val indexedItem = cart.withIndex().find {
            val start = 2 + it.index * 4
            val end = start + 4
            val range = start until end
            slotId in range
        }
        if (indexedItem == null) {
            player.sendDeveloperMessage("Invalid slot selected($slotId)")
            return
        }

        val (index, item) = indexedItem
        val amount = CreditStoreTabOption.find(option)?.amount?.coerceAtMost(item.amount)
        if (amount == null) {
            player.sendDeveloperMessage("Invalid option selected($option)")
            return
        }
        try {
            cart.remove(index, item, amount)
            val itemName = ItemDefinitions.nameOf(item.product.itemId)
            val totalItemAmount = item.amount * item.product.itemAmount
            player.sendMessage(Colour.RS_GREEN.wrap("You have removed $totalItemAmount x $itemName from your cart."))
            updateSelectedTabContents(player, player.storeTabSelected)
        } catch (e :Exception) {
            onCartError(player, e)
        }
    }

    fun onOptionShop(player: Player, slotId: Int, option: Int) {
        checkUpdated(player)
        val product = listProductsVisibleFor(player).withIndex().find {
            slotId == (4 + it.index * (if (it.value.limited) 16 else 14))
        }?.value
        if (product == null) {
            player.sendDeveloperMessage("Invalid slot selected($slotId)")
            return
        }
        val amount = CreditStoreTabOption.find(option)?.amount
        if (amount == null) {
            player.sendDeveloperMessage("Invalid option selected($option)")
            return
        }
        val cart = player.storeCart
        val totalNewAmount = cart.amountOf(product.id?:return) + amount
        if (totalNewAmount > product.quantity) {
            player.sendMessage("There are not enough of this item in stock.")
            return
        }
        if (cart.size > 50) {
            player.sendMessage("Your cart is full, please checkout before adding more items.")
            return
        }
        try {
            cart.add(product, amount)
            val cartItem = Item(product.itemId, amount * product.itemAmount)
            val totalItemAmount = cartItem.amount
            player.sendMessage(Colour.RS_GREEN.wrap("You have added $totalItemAmount x ${cartItem.name} to your cart."))
        } catch (e: Exception) {
            onCartError(player, e)
        }
    }

    fun updateSelectedTabContents(player: Player, tab: CreditStoreTab) {
        with(player.packetDispatcher) {
            val enableOpsInComponentsRange = when (tab) {
                CreditStoreTab.Cart -> {
                    val cart = Container(ContainerPolicy.ALWAYS_STACK, ContainerType.MX_STORE_CART, Optional.empty())
                    val amounts = Container(ContainerPolicy.ALWAYS_STACK, ContainerType.MX_STORE_CART_AMOUNT, Optional.empty())
                    val prices = Container(ContainerPolicy.ALWAYS_STACK, ContainerType.MX_STORE_CART_PRICES, Optional.empty())
                    try {
                        player.storeCart.forEach {
                            val product = it.product
                            cart.add(MapEntry(product.itemId, it.amount))
                            amounts.add(MapEntry(product.itemId, product.itemAmount))
                            prices.add(MapEntry(product.itemId, product.priceWithOptionalDiscount))

                        }
                    } catch (e: Exception) {
                        onCartError(player, e)
                    }
                    // lemme know if u want me to go somewhere specificd
                    sendUpdateItemContainer(cart)
                    sendUpdateItemContainer(amounts)
                    sendUpdateItemContainer(prices)
                    2..(2 + ((1 + cart.size) * 4))
                }

                CreditStoreTab.Shop -> {
                    val products = listProductsVisibleFor(player)
                    val storeItems = Container(ContainerType.MX_STORE_ITEMS, ContainerPolicy.ALWAYS_STACK, products.size, Optional.empty())
                    val storePrices = Container(ContainerType.MX_STORE_PRICES, ContainerPolicy.ALWAYS_STACK, products.size, Optional.empty())
                    val storeStock = Container(ContainerType.MX_STORE_STOCK, ContainerPolicy.ALWAYS_STACK, products.size, Optional.empty())
                    val storeDiscounted = Container(ContainerType.MX_STORE_DISCOUNTED, ContainerPolicy.ALWAYS_STACK, products.size, Optional.empty())
                    val productsVisible = listProductsVisibleFor(player)
                        productsVisible.forEach {
                        storeItems.add(MapEntry(it.itemId, it.itemAmount))
                        storePrices.add(MapEntry(it.itemId, it.price))
                        storeStock.add(MapEntry(it.itemId, it.quantity))
                        storeDiscounted.add(MapEntry(it.itemId, when(it.discounted) {
                            true -> it.priceWithOptionalDiscount
                            false -> 0
                        }))
                    }
                    player.sendDeveloperMessage("Dislaying ${productsVisible.size} products on tab")
                    sendUpdateItemContainer(storeItems)
                    sendUpdateItemContainer(storePrices)
                    sendUpdateItemContainer(storeStock)
                    sendUpdateItemContainer(storeDiscounted)
                    4..(4 + ((1 + storeItems.size) * 16))
                }
                else -> return
            }
            sendComponentSettings(
                5101,
                5101 component 18,
                enableOpsInComponentsRange.first,
                enableOpsInComponentsRange.last,
                AccessMask.CLICK_OP1,
                AccessMask.CLICK_OP2,
                AccessMask.CLICK_OP3,
                AccessMask.CLICK_OP4
            )
        }
    }

    private fun listProductsVisibleFor(player: Player): List<CreditStoreProduct> {
        val category = player.storeCategorySelected
        player.sendDeveloperMessage("Showing listings for category: ${category.name}")
        val type = if(player.isIronman) CreditStoreType.Ironman else CreditStoreType.Regular
        if (category == CreditStoreCategory.LimitedTime) {
            return storeProducts.filter { it.quantity != Int.MAX_VALUE && it.quantity > 0 && it.types.contains(type) }.sortedBy { it.sortPriority }
        }
        return storeProducts
            .filter { it.categories.contains(category) && it.types.contains(type) }
            .sortedBy { it.sortPriority }
    }

    private val CreditStoreCartItemSimple.product: CreditStoreProduct
        get() = storeProducts.firstOrNull { it.id == productId }?:error("Invalid product id($productId)")
}

private typealias MapEntry = Item

sealed class CreditStoreTab(val bitValue: Int, val slotRange: IntRange) {
    data object Perks : CreditStoreTab(0, 0 until 9)
    data object Shop : CreditStoreTab(1, 9 until 18)
    data object Cart : CreditStoreTab(2, 18 until 26)
    companion object {
        operator fun get(bitValue: Int) = when (bitValue) {
            Perks.bitValue -> Perks
            Shop.bitValue -> Shop
            Cart.bitValue -> Cart
            else -> error("Invalid tab selected($bitValue)")
        }
        fun findTab(slotId: Int): CreditStoreTab = when (slotId) {
            in Perks.slotRange -> Perks
            in Shop.slotRange -> Shop
            in Cart.slotRange -> Cart
            else -> error("Invalid tab selected($slotId)")
        }
    }
}

private sealed class CreditStoreTabOption(val slot: Int, val amount: Int) {
    data object One : CreditStoreTabOption(1, 1)
    data object Five : CreditStoreTabOption(2, 5)
    data object Ten : CreditStoreTabOption(3, 10)
    data object Fifty : CreditStoreTabOption(4, 50)
    companion object {
        fun find(slotId: Int): CreditStoreTabOption? = when (slotId) {
            One.slot -> One
            Five.slot -> Five
            Ten.slot -> Ten
            Fifty.slot -> Fifty
            else -> null
        }
    }
}


private val CreditStoreCategory.slotRange
    get() = when(this) {
        CreditStoreCategory.BestSellers -> 0..2
        CreditStoreCategory.LimitedTime -> 3..5
        CreditStoreCategory.Pins -> 6..8
        CreditStoreCategory.Weapons -> 9..11
        CreditStoreCategory.Armory -> 12..14
        CreditStoreCategory.Supplies -> 15..17
        CreditStoreCategory.Boosts -> 18..20
        CreditStoreCategory.Pets -> 21..23
        CreditStoreCategory.Cosmetic -> 24..26
        CreditStoreCategory.Miscellaneous -> 27..29
    }
private fun findCategory(slotId: Int): CreditStoreCategory =
    CreditStoreCategory.entries.first { slotId in it.slotRange }

@Serializable
data class CreditStoreCart(
    val items: MutableList<CreditStoreCartItemSimple> = mutableListOf()
): List<CreditStoreCartItemSimple> by items {

    fun add(product: CreditStoreProduct, amount: Int) {
        val existing = items.firstOrNull { it.productId == product.id }
        if (existing != null) {
            val index = items.indexOf(existing)
            val newAmount = existing.amount + amount
            items[index] = existing.copy(amount = newAmount)
        } else {
            items += CreditStoreCartItemSimple(product.id ?: error("Product id is null"), amount)
        }
    }

    fun remove(index: Int, item: CreditStoreCartItemSimple, amount: Int) {
        if (amount >= item.amount) {
            items.remove(item)
        } else if(amount > 0){
            items[index] = item.copy(amount = item.amount - amount)
        }
    }

    fun clear() {
        items.clear()
    }

    fun amountOf(productId: Int): Int = items.firstOrNull { it.productId == productId }?.amount ?: 0

    companion object : JsonSerializer<CreditStoreCart>, JsonDeserializer<CreditStoreCart> {
        override fun serialize(
            src: CreditStoreCart,
            typeOfSrc: Type,
            context: com.google.gson.JsonSerializationContext
        ): JsonElement {
            val array = com.google.gson.JsonArray()
            src.forEach {
                val obj = com.google.gson.JsonObject()
                obj.addProperty("productId", it.productId)
                obj.addProperty("amount", it.amount)
                array.add(obj)
            }
            return array
        }

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?,
        ): CreditStoreCart {
            val array = json?.asJsonArray ?: error("Invalid json element")
            val items = array.map {
                val obj = it.asJsonObject
                CreditStoreCartItemSimple(
                    productId = obj.get("productId").asInt,
                    amount = obj.get("amount").asInt
                )
            }
            return CreditStoreCart(items.toMutableList())
        }
    }
}

@Serializable
data class CreditStoreCartItemSimple(
    val productId: Int,
    val amount: Int
)

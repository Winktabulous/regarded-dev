package com.zenyte.game.content.universalshop

import com.near_reality.game.content.UniversalShop
import com.zenyte.GameToggles
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.dialogue.start
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.dialogue.OptionsMenuD

val ChangeItemPrice = MenuOption("Change Item Price")
val ChangeItemActive = MenuOption("Change Item Active")
val FreezeShop = MenuOption("Freeze Shop")
val RerouteAllShopsBack = MenuOption("Reroute All Shops")
val NothingAllowed = MenuOption("Nothing Allowed")

val noAccessOptions: List<Pair<MenuOption, Int>> = listOf(NothingAllowed to 0)

val moderatorOptions: List<Pair<MenuOption, Int>> = listOf(ChangeItemActive to 0)
val adminOptions: List<Pair<MenuOption, Int>> = listOf(ChangeItemActive to 0, FreezeShop to 1)
val fullOptions: List<Pair<MenuOption, Int>> = listOf(ChangeItemPrice to 0, ChangeItemActive to 1, FreezeShop to 2, RerouteAllShopsBack to 3)

data class MenuOption(val option : String)

class MainMenu(
    player: Player,
    private val clickMenu : List<Pair<MenuOption, Int>> = buildMainMenuOptions(player)
) : OptionsMenuD(player, "Universal Shop Manager", *clickMenu.map{ it.first.option }.toTypedArray()) {
    override fun handleClick(slotId: Int) {
        executeMenuOption(player, clickMenu.first { it.second == slotId }.first)
    }

    private fun executeMenuOption(player: Player, menuOption: MenuOption) {
        when(menuOption) {
            ChangeItemPrice -> player.dialogueManager.start(PriceChangeMenu(player))
            NothingAllowed -> player.dialogueManager.finish()
            ChangeItemActive -> player.runActiveDialogue()
            FreezeShop -> player.dialogueManager.start(DisableShopMenu(player))
            RerouteAllShopsBack -> player.checkNuclearButton()
        }
    }
}

private fun Player.checkNuclearButton() {
    val disabled = !GameToggles.UNIVERSAL_SHOP_FLOODGATE
    dialogueManager.start{
        options("Currently Disabled: $disabled") {
            if(disabled) {
                "Enable" {
                    GameToggles.UNIVERSAL_SHOP_FLOODGATE = true
                    player.sendMessage("The Universal Shop has been enabled.")
                    player.dialogueManager.start(MainMenu(player))
                }
            } else {
                "Disable" {
                    GameToggles.UNIVERSAL_SHOP_FLOODGATE = false
                    player.sendMessage("The Universal Shop has been disabled.")
                    player.dialogueManager.start(MainMenu(player))
                }
            }
        }
    }

}

private fun Player.runActiveDialogue() {
    this.sendInputInt("Which item id would you like to deactivate?") { itemId ->
        this.dialogueManager.start {
            val currentBlacklist = UniversalShop.disabledItems.contains(itemId)
            options("Currently deactivated: $currentBlacklist") {
                if(currentBlacklist) {
                    "Activate" {
                        UniversalShop.disabledItems.remove(itemId)
                        player.sendMessage("The item has been activated.")
                        player.dialogueManager.start(MainMenu(player))
                    }
                } else {
                    "Deactivate" {
                        UniversalShop.disabledItems.add(itemId)
                        player.sendMessage("The item has been de-activated")
                        player.dialogueManager.start(MainMenu(player))
                    }
                }
            }
        }
    }
}

class PriceChangeMenu(
    player: Player,
    private val optionsMenuVar: Array<String> = UniversalShop.Categories.map { it.tableName }.toTypedArray()
) : OptionsMenuD(player, "Pick Category", *optionsMenuVar) {
    override fun handleClick(slotId: Int) {
        var itemId = 0
        val option = optionsMenuVar[slotId]
        val category = UniversalShop.Categories.first{it.tableName == option }.uniqueIndex
        player.sendInputInt("Please enter item id from category") { id ->
            itemId = id
        }
        player.sendInputInt("What would you like to set price to? (curr: ${UniversalShop.determinePrice(UniversalShop.Categories.first{it.tableName == option}.uniqueIndex, itemId)})") { price ->
            UniversalShop.priceOverrides[category to itemId] = price
            player.sendMessage("Price was changed to $price")
            player.dialogueManager.start(MainMenu(player))
        }
    }

}

class DisableShopMenu(
    player: Player,
    private val optionsL: Array<String> = UniversalShop.Categories.map { it.tableName }.toTypedArray()
) : OptionsMenuD(player, "Pick Category to Disable", *optionsL) {
    override fun handleClick(slotId: Int) {
        val option = optionsL[slotId]
        val category = UniversalShop.Categories.first{it.tableName == option }
        val disabled = UniversalShop.disabledCategory.contains(category)

        player.dialogueManager.start {
            options("Currently Disabled: $disabled") {
                if(disabled) {
                    "Enable" {
                        UniversalShop.disabledCategory.remove(category)
                        player.sendMessage("The category has been enabled.")
                        player.dialogueManager.start(MainMenu(player))
                    }
                } else {
                    "Disable" {
                        UniversalShop.disabledCategory.add(category)
                        player.sendMessage("The category has been disabled.")
                        player.dialogueManager.start(MainMenu(player))
                    }
                }
            }
        }
    }

}





fun buildMainMenuOptions(player: Player): List<Pair<MenuOption, Int>> {
    val collect = mutableListOf<Pair<MenuOption, Int>>()
    when(player.privilege) {
        PlayerPrivilege.PLAYER -> collect.addAll(noAccessOptions)
        PlayerPrivilege.MEMBER ->  collect.addAll(noAccessOptions)
        PlayerPrivilege.YOUTUBER ->  collect.addAll(noAccessOptions)
        PlayerPrivilege.FORUM_MODERATOR -> collect.addAll(noAccessOptions)
        PlayerPrivilege.SUPPORT -> collect.addAll(noAccessOptions)
        PlayerPrivilege.MODERATOR -> collect.addAll(moderatorOptions)
        PlayerPrivilege.SENIOR_MODERATOR -> collect.addAll(moderatorOptions)
        PlayerPrivilege.ADMINISTRATOR -> collect.addAll(adminOptions)
        PlayerPrivilege.DEVELOPER -> collect.addAll(fullOptions)
        PlayerPrivilege.HIDDEN_ADMINISTRATOR ->  collect.addAll(fullOptions)
        PlayerPrivilege.TRUE_DEVELOPER ->  collect.addAll(fullOptions)
        else -> { collect.addAll(noAccessOptions) }
    }


    return collect
}



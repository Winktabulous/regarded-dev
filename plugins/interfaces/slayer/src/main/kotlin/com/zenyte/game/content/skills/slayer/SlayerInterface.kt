package com.zenyte.game.content.skills.slayer

import com.near_reality.scripts.interfaces.InterfaceHandlerContext
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.content.achievementdiary.DiaryReward
import com.zenyte.game.content.achievementdiary.DiaryUtil
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.util.ItemUtil
import com.zenyte.game.world.entity.player.Setting
import com.zenyte.game.world.entity.player.SkillConstants.*
import com.zenyte.game.world.entity.player.container.RequestResult.NOT_ENOUGH_SPACE
import com.zenyte.game.world.entity.player.container.impl.RunePouch
import com.zenyte.plugins.item.HerbSack
import mgi.types.config.items.ItemDefinitions
import kotlin.math.min
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask.*
import mgi.types.config.enums.Enums.*

class SlayerInterface : InterfaceScript() {
    val size = SLAYER_PERK_REWARD_NAMES.size

    init {
        SLAYER_REWARDS {
            val unlock = "Unlock/extend perk | Manage task"(8) {
                when {
                    slotID < size -> {
                        val unlocked = player.slayer.isUnlocked(slotID)
                        if (!unlocked) player.slayer.confirmPurchase(slotID)
                        else player.slayer.disable(slotID)
                    }
                    slotID == size -> player.slayer.confirmTaskCancellation()
                    slotID == size + 1 -> player.slayer.confirmTaskBlock()
                    slotID >= size + 2 && slotID <= size + 7 -> player.slayer.confirmTaskUnblock(slotID - (size + 2))
                    slotID == size + 8 -> player.slayer.confirmFullExtensionUnlock()
                    slotID == size + 9 -> player.slayer.confirmTaskStore()
                    slotID == size + 10 -> player.slayer.confirmTaskUnstore()
                    else -> throw IllegalStateException("Slot: $slotID")
                }
            }
            val buy = "Buy reward".suspend(23) {
                if (option == 10) {
                    ItemUtil.sendItemExamine(player, itemID)
                } else {
                    stop(SLAYER_ITEM_REWARDS_ENUM.getValue(slotID).orElseThrow { RuntimeException() } != itemID)

                    val def = ItemDefinitions.get(itemID)
                    stop(def == null)
                    checkRequirements(def)

                    if (Item.itemBlacklist.contains(itemID)) {
                        player.sendMessage("The buying of this item is temporarily disabled.")
                        return@suspend
                    }
                    val cost = SLAYER_REWARDS_COST.getValue(itemID).orElseThrow { RuntimeException() }
                    val slayerPoints = player.slayer.slayerPoints
                    val affordableAmount = min(slayerPoints / cost, amount)
                    stop(affordableAmount <= 0, "You don't have enough Slayer points to purchase this.")
                    if (affordableAmount < amount) player.sendMessage("You don't have enough Slayer points to purchase this many.")
                    val itemAmount = when {
                        def.id == CANNONBALL -> 10
                        def.id == DRAGON_DART -> 2
                        def.id == SCROLL_OF_IMBUING -> 1
                        def.id == HERB_BOX || def.id == OPENED_HERB_BOX -> 1
                        def.isStackable() -> 250
                        else -> 1
                    }
                    val result = player.inventory.addItem(Item(itemID, affordableAmount * itemAmount))
                    val amountBought = result.succeededAmount / itemAmount
                    player.slayer.addSlayerPoints(-(amountBought * cost))

                    if (result.result == NOT_ENOUGH_SPACE) player.sendMessage("Not enough space in your inventory.")
                }
            }
            opened {
                settings.refreshSetting(Setting.BIGGER_AND_BADDER_SLAYER_REWARD)
                settings.refreshSetting(Setting.STOP_THE_WYVERN_SLAYER_REWARD)

                varManager.sendBit(12442, if (slayer.isUnlocked("Task Storage")) 1 else 0)
                varManager.sendBit(
                    Slayer.LUMBRIDGE_ELITE_DIARY_COMPLETED_BIT,
                    DiaryUtil.eligibleFor(DiaryReward.EXPLORERS_RING4, this)
                )

                slayer.refreshCurrentAssignment()
                slayer.refreshSlayerPoints()
                slayer.refreshRewards()
                slayer.refreshBlockedTasks()

                sendInterface()

                unlock.sendComponentSettings(this, 100, CLICK_OP1)
                buy.sendComponentSettings(
                    this,
                    SLAYER_ITEM_REWARDS_ENUM.size,
                    CLICK_OP2,
                    CLICK_OP3,
                    CLICK_OP4,
                    CLICK_OP5,
                    CLICK_OP10
                )
            }
        }
    }
}

val InterfaceHandlerContext.requiredRangeLevel get() = if (itemID == BROAD_BOLTS) 61 else 50

val InterfaceHandlerContext.amount
    get() = when (option) {
        2 -> 1
        3 -> 5
        4 -> 10
        else -> 50
    }

suspend fun InterfaceHandlerContext.checkRequirements(def: ItemDefinitions) = player.run {
    items(BROAD_BOLTS, BROAD_ARROWS) {
        stop(
            skills.getLevelForXp(SLAYER) < 55 || skills.getLevelForXp(RANGED) < requiredRangeLevel,
            "You need a Slayer and a Ranged level of at least 55 and $requiredRangeLevel respectively to purchase ${def.name.lowercase()}."
        )
    }
    item(HERB_SACK) {
        stop(
            skills.getLevel(HERBLORE) < 58,
            "You need a Herblore level of at least 58 to buy a herb sack."
        )
        stop(
            containsItem(HerbSack.HERB_SACK),
            "You can only own one herb sack at a time!"
        )
    }
    stopItem(
        RunePouch.RUNE_POUCH.id, containsAnyItem(RunePouch.RUNE_POUCH, RunePouch.DIVINE_RUNE_POUCH),
        "You can only own one rune pouch at a time!"
    )
    stopItem(
        FIGHTER_TORSO,
        skills.getLevelForXp(DEFENCE) < 40,
        "You need a Defence level of at least 40 to purchase a fighter torso."
    )
    stopItem(
        BARRELCHEST_ANCHOR, skills.getLevelForXp(ATTACK) < 60 || skills.getLevelForXp(STRENGTH) < 40,
        "You need an Attack level of at least 60 & a Strength level of at least 40 to purchase the ${def.name}."
    )
}

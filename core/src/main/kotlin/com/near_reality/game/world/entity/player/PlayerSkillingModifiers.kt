package com.near_reality.game.world.entity.player

import com.zenyte.game.content.boons.impl.IWantItAll
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.area.wilderness.WildernessResourceArea
import java.util.*

fun determineGatheringMultiplier(player: Player): OptionalDouble {
    var transformedAmount = 0.0
    if (player.boonManager.hasBoon(IWantItAll::class.java) && IWantItAll.roll())
        transformedAmount += IWantItAll.GATHER_QUANTITY_MULTIPLIER
    if (player.inArea(WildernessResourceArea::class.java))
        transformedAmount += WildernessResourceArea.GATHER_QUANTITY_MULTIPLIER
    return if (transformedAmount <= 0.0)
        OptionalDouble.empty()
    else
        OptionalDouble.of(transformedAmount)
}

fun onGather(player: Player) {
    if (player.inArea(WildernessResourceArea::class.java)) {
        if (Utils.randomBoolean(WildernessResourceArea.BLOOD_MONEY_REWARD_CHANCE)) {
            val bloodMoneyAmount = (WildernessResourceArea.BLOOD_MONEY_REWARD_AMOUNT_MIN..WildernessResourceArea.BLOOD_MONEY_REWARD_AMOUNT_MAX).random()
            player.inventory.addOrDrop(Item(ItemId.BLOOD_MONEY, bloodMoneyAmount))
            player.sendMessage("You receive $bloodMoneyAmount blood money.")
        }
    }
}

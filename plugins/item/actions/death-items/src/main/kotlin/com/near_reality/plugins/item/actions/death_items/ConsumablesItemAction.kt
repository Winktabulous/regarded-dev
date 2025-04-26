package com.near_reality.plugins.item.actions.death_items

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.content.consumables.Consumable
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingStructs
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.Settings

/**
 * @author Kris | 13/06/2022
 */
class ConsumablesItemAction : ItemActionScript() {
    init {
        items(Consumable.consumables.keys)

        death {
            lost { yield(item) }
            if (pvp) {
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else if (SettingVariables.getVariableValue(Settings.findSettingByStructId(SettingStructs.FOOD_AND_POTIONS_CAN_FORM_SUPPLY_PILES_ON_DEATH_STRUCT_ID), player) == 0) {
                status { ItemDeathStatus.GO_TO_GRAVESTONE_OR_DROP_ON_GROUND }
            } else {
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}

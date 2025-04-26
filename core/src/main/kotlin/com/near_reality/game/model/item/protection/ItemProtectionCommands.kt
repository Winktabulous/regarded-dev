package com.near_reality.game.model.item.protection

import com.near_reality.game.item.HiddenItems
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.dialogue.OptionsMenuD
import com.zenyte.utils.StringUtilities
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import mgi.types.config.items.ItemDefinitions
import mgi.utilities.StringFormatUtil
import java.util.*

object ItemProtectionCommands {

    fun loadCommands() {
        GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "forceprot", "Initiates forcing a protection value change for an item.") { p: Player, args: Array<String?> ->

            if (args.isNotEmpty()) {
                val listOfItems = ObjectArrayList<ItemDefinitions>(50)
                val listOfNames = ObjectArrayList<String>(50)
                val name = StringUtilities.compile(args, 0, args.size, ' ')
                var characterCount = 0
                for (defs in ItemDefinitions.getDefinitions()) {
                    if (defs == null || defs.isNoted) {
                        continue
                    }
                    if (HiddenItems.HIDDEN_ITEMS.contains(defs.id)) continue
                    if (defs.name.lowercase(Locale.getDefault()).contains(name)) {
                        listOfItems.add(defs)
                        val string =
                            defs.id.toString() + " - " + defs.name + "- "+ StringFormatUtil.format(ItemProtectionValueManager.getProtectionValue(defs.id))
                        listOfNames.add(string)
                        characterCount += string.length
                        //Cap it out at 30kb for the payload, gives enough room for the header and rest of the packet.
                        if (characterCount >= 30000) {
                            break
                        }
                    }
                }
                p.dialogueManager.start(object : OptionsMenuD(
                    p,
                    "Query: $name (Click to set protection value)",
                    *listOfNames.toTypedArray<String>()
                ) {
                    override fun handleClick(slotId: Int) {
                        promptSetProtectionValue(p, Item(listOfItems[slotId].id)) {
                            p.dialogueManager.start(this)
                        }
                    }

                    override fun cancelOption(): Boolean {
                        return true
                    }
                })
            } else
                promptInputTradeableItemName(p)
        }
    }

    private fun promptInputTradeableItemName(p: Player) {
        p.sendInputItem("What item's protection value would you like to change?") { item: Item ->
            promptSetProtectionValue(p, item)
        }
    }

    private fun promptSetProtectionValue(p: Player, item: Item, onCompleted: () -> Unit = {}) {
        p.sendInputInt("What protection value would you like to set for " + item.name + "?") { value: Int ->
            val previous = ItemProtectionValueManager.getProtectionValue(item.id)
            ItemProtectionValueManager.updateProtectionValue(item.id, value)
            p.sendMessage(
                "Protection value of " + item.name + " changed from " + StringFormatUtil.format(previous) + " to " + StringFormatUtil.format(
                    value
                ) + "."
            )
            onCompleted()
        }
    }
}

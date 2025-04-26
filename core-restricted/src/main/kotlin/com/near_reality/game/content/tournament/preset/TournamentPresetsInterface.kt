package com.near_reality.game.content.tournament.preset

import com.near_reality.game.content.tournament.area.TournamentLobbyArea
import com.zenyte.game.GameInterface
import com.zenyte.game.item.Item
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.ItemUtil
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerPolicy
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.region.area.plugins.TempPlayerStatePlugin
import mgi.types.config.enums.Enums
import mgi.types.config.items.ItemDefinitions
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * Represents the interface for the tournament presets.
 *
 * @author Tommeh | 27/05/2019 | 14:38
 * @author Stan van der Bend
 */
@Suppress("unused")
class TournamentPresetsInterface : Interface() {

    override fun attach() {
        put(2, "Presets")
        put(6, "Inventory Setup")
        put(7, "Equipment Setup")
        put(40, "Select")
        put(44, "Take Item")
        put(47, "View")
        put(48, "Apply")
    }

    override fun open(player: Player) {
        if (player.area !is TournamentLobbyArea) {
            player.sendMessage("Cannot view the tournament supplies/presets from outside of the lobby.")
            return
        }

        player.interfaceHandler.sendInterface(this)
        with(player.packetDispatcher) {
            sendClientScript(10599, TournamentPreset.entries.joinToString("|"), 1)
            sendComponentSettings(
                getInterface(),
                getComponent("Take Item"),
                0,
                Enums.TOURNAMENT_ITEMS_ENUM.values.size,
                AccessMask.CLICK_OP1,
                AccessMask.CLICK_OP2,
                AccessMask.CLICK_OP3,
                AccessMask.CLICK_OP4,
                AccessMask.CLICK_OP10
            )
            sendComponentSettings(getInterface(), getComponent("Inventory Setup"), 0, 28, AccessMask.CLICK_OP10)
            sendComponentSettings(getInterface(), getComponent("Select"), 0, 50, AccessMask.CLICK_OP1)
            sendComponentSettings(getInterface(), getComponent("View"), -1, 1, AccessMask.CLICK_OP1)
            sendComponentSettings(getInterface(), getComponent("Apply"), -1, 1, AccessMask.CLICK_OP1)
        }
    }

    override fun build() {
        bind("Presets") { player: Player -> player.varManager.sendVar(261, 0) }
        bind("Select") { player: Player, slotId: Int, _: Int, _: Int ->
            if (cannotEnableTempContainer(player))
                return@bind
            player.varManager.sendVar(261, slotId + 1)
            val preset = TournamentPreset.entries[slotId]
            val container = Container(ContainerPolicy.NORMAL, ContainerType.TOURNAMENT, Optional.of(player))
            for ((key, value) in preset.presetEquipment.items) {
                container[key] = value?.t
            }
            for (item in preset.presetInventory.items) {
                container.add(item.t)
            }
            val builder = StringBuilder()
            for ((skill, level) in preset.presetStats.skills) {
                if (!Skills.isCombatSkill(skill)) {
                    continue
                }
                builder.append(skill).append("|")
                builder.append(level).append("|")
            }
            container.refresh(player)
            player.packetDispatcher.sendClientScript(
                10609,
                preset.toString(),
                builder.toString(),
                preset.presetSpellbook.ordinal
            )
        }
        bind("Apply") { player: Player ->
            if (cannotEnableTempContainer(player))
                return@bind
            val index = player.varManager.getValue(261) - 1
            val preset = TournamentPreset.entries[index]
            preset.apply(player)
        }
        bind("Inventory Setup") { player: Player?, _: Int, _: Int, _: Int ->
            ItemUtil.sendItemExamine(player, id)
        }
        bind("Equipment Setup") { player: Player?, _: Int, _: Int, _: Int ->
            ItemUtil.sendItemExamine(player, id)
        }
        bind("Take Item") { player: Player, slotId: Int, _: Int, option: Int ->
            val optionalId = Enums.TOURNAMENT_ITEMS_ENUM.getValue(slotId)
            if (optionalId.isEmpty) {
                return@bind
            }
            if (cannotEnableTempContainer(player))
                return@bind
            val tournamentLobby = player.area as? TournamentLobbyArea?:return@bind
            val tournament = tournamentLobby.tournament
            val tournamentPreset = tournament.preset
            val id = optionalId.asInt
            var maximumAmountAllowed = Int.MAX_VALUE
            val inventory = player.inventory
            if (ItemDefinitions.get(id).name.lowercase(Locale.getDefault()).startsWith("saradomin brew")) {
                val count =inventory.getAmountOf(6685) +
                           inventory.getAmountOf(6687) +
                           inventory.getAmountOf(6689) +
                           inventory.getAmountOf(6691)
                val maximumBrews = tournamentPreset.maximumBrews
                maximumAmountAllowed = max(0.0, (maximumBrews - count).toDouble()).toInt()
            }
            if (option == 10) {
                ItemUtil.sendItemExamine(player, id)
            } else if (option == 4) {
                val maxAmount = maximumAmountAllowed
                player.sendInputInt("How many would you like to take?") { amount: Int ->
                    if (amount > maxAmount) {
                        player.sendMessage("You can only carry a maximum of " + tournamentPreset.maximumBrews + " saradomin brews with you in this fight.")
                    }
                    takeItem(player, id, min(maxAmount.toDouble(), amount.toDouble()).toInt())
                }
            } else {
                var amount = if (option == 2) 5 else if (option == 3) 10 else 1
                if (option == 1 && ItemDefinitions.get(id).isStackable()) {
                    amount = 10000
                }
                if (amount > maximumAmountAllowed) {
                    amount = maximumAmountAllowed
                    player.sendMessage("You can only carry a maximum of " + tournamentPreset.maximumBrews + " saradomin brew" + (if (tournamentPreset.maximumBrews == 1) "" else "s") + " with you in this fight.")
                }
                takeItem(player, id, amount)
            }
        }
    }

    private fun cannotEnableTempContainer(player: Player): Boolean =
        !TempPlayerStatePlugin.enableTempState(player, TempPlayerStatePlugin.StateType.INVENTORY)

    private fun takeItem(player: Player, id: Int, amount: Int) {
        if (amount <= 0) {
            return
        }
        val added = player.inventory.addItem(Item(id, amount)).succeededAmount
        if (added < amount) {
            player.sendMessage("Not enough space in your inventory.")
        }
    }

    override fun getInterface(): GameInterface =
        GameInterface.TOURNAMENT_PRESETS
}

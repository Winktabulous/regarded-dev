package com.near_reality.plugins.item

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue

class BoosterItemOption : ItemPlugin() {

	enum class BoosterType(val item: Int, val message: String, val value: Int) {
		LARRANS_KEY(32149, "Larran's Key chance by 25% for 1 hour.", 6000),
		GANODERMIC(32150, "Ganodermic Beast drop uniques drop rate by 20% for 10 kills.", 10),
		SLAYER(32151, "Increased chance for superiors, and superior uniques by 25% for 1 hour.", 6000),
		PET(32152, "Increased chance to get a skilling or boss pet 10% for 1 hour.", 6000),
		GAUNTLET(32153, "Increased drop rates for rare items from Gauntlet & Corrupted Gauntlet by 25% for 5 completions.", 5),
		BLOOD_MONEY(32154, "Increased Blood Money from kills by 25% for 10 PKs.", 10),
		CLUE(32155, "Provides 2 additional rolls when opening a reward casket for 25 caskets.", 25),
		TOB(32156, "Increased ToB unique drop rates by 10% for 2 completions.", 2),
		REVENANT(32166, "Increased chance to roll unique table by 20% for 1 hour.", 6000),
		NEX(32167, "Nex drop uniques drop rate by 15% for 20 kills.", 20),
	}

	override fun handle() {
		bind("Activate") { player: Player, item: Item, _: Int ->
			for (entry in BoosterType.values()) {
				if (entry.item == item.id) {
					val name = item.name
					player.dialogue {
						item(item, "You try to examine this ancient object. It appears to be an archaic invocation of the gods! Would you like to consume its power to obtain: ${Colour.DARK_BLUE.wrap(entry.message)}")
						options("This will consume the booster!", Dialogue.DialogueOption("Consume $name.") { consume(player, entry, item) }, Dialogue.DialogueOption("Cancel."))
					}
					break
				}
			}
		}
	}

	private fun boosterActive(player: Player, boosterType: BoosterType): Boolean {
		return when (boosterType) {
			BoosterType.LARRANS_KEY -> player.variables.larransKeyBoosterTick > 0
			BoosterType.GANODERMIC -> player.variables.ganoBoosterKillsLeft > 0
			BoosterType.SLAYER -> player.variables.slayerBoosterTick > 0
			BoosterType.PET -> player.variables.petBoosterTick > 0
			BoosterType.GAUNTLET -> player.variables.gauntletBoosterCompletionsLeft > 0
			BoosterType.BLOOD_MONEY -> player.variables.bloodMoneyBoosterLeft > 0
			BoosterType.CLUE -> player.variables.clueBoosterLeft > 0
			BoosterType.TOB -> player.variables.tobBoosterleft > 0
			BoosterType.REVENANT -> player.variables.revenantBoosterTick > 0
			BoosterType.NEX -> player.variables.nexBoosterleft > 0
		}
	}

	private fun activateBooster(player: Player, boosterType: BoosterType) {
		when (boosterType) {
			BoosterType.LARRANS_KEY -> player.variables.larransKeyBoosterTick += boosterType.value
			BoosterType.GANODERMIC -> player.variables.ganoBoosterKillsLeft += boosterType.value
			BoosterType.SLAYER -> player.variables.slayerBoosterTick += boosterType.value
			BoosterType.PET -> player.variables.petBoosterTick += boosterType.value
			BoosterType.GAUNTLET -> player.variables.gauntletBoosterCompletionsLeft += boosterType.value
			BoosterType.BLOOD_MONEY -> player.variables.bloodMoneyBoosterLeft += boosterType.value
			BoosterType.CLUE -> player.variables.clueBoosterLeft += boosterType.value
			BoosterType.TOB -> player.variables.tobBoosterleft += boosterType.value
			BoosterType.REVENANT -> player.variables.revenantBoosterTick += boosterType.value
			BoosterType.NEX -> player.variables.nexBoosterleft += boosterType.value
		}
	}

	private fun consume(player: Player, boosterType: BoosterType, item: Item) {
		activateBooster(player, boosterType)
		player.inventory.deleteItem(item.copy(1))
		player.dialogue { item(item, "You consume the ancient object. You feel the booster effect flowing through you: ${Colour.DARK_BLUE.wrap(boosterType.message)}") }
	}

	override fun getItems(): IntArray {
		val values = mutableListOf<Int>()
		for (entry in BoosterType.values()) {
			values.add(entry.item)
		}

		return values.toIntArray()
	}

}
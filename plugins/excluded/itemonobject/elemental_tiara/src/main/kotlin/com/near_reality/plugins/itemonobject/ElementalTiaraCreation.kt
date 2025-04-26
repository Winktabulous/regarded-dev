package com.near_reality.plugins.itemonobject

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary
import com.zenyte.game.content.skills.runecrafting.BasicRunecraftingAction
import com.zenyte.game.content.skills.runecrafting.Runecrafting
import com.zenyte.game.content.skills.runecrafting.Tiara
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnObjectAction
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the creation of elemental tiara's.
 *
 * To create a talisman tiara, the player must have a tiara and the corresponding talisman in their inventory,
 * and use the talisman or tiara on the altar of that particular rune.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class ElementalTiaraCreation : ItemOnObjectAction {

    override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, `object`: WorldObject) {

        val elementalTiara = ElementalTiara.findByElementalTalisMan(player)
        val tiaraItem = player.inventory.getAny(ItemId.TIARA)

        if (elementalTiara != null && tiaraItem != null) {

            if (`object`.id != elementalTiara.runecrafting.altarObjectId) {
                player.dialogue { plain("You can only create elemental tiara's on the altar corresponding to the element.") }
                return
            }

            if (player.inventory.deleteItems(
                    Item(elementalTiara.talisManItemId),
                    tiaraItem
                ).result == RequestResult.SUCCESS
            ) {
                player.animation = BasicRunecraftingAction.RUNECRAFTING_ANIM
                player.graphics = BasicRunecraftingAction.RUNECRAFTING_GFX
                player.lock(2)
                WorldTasksManager.schedule({
                    if (elementalTiara == ElementalTiara.MIND_TIARA) {
                        player.achievementDiaries.update(FaladorDiary.MAKE_MIND_TIARA)
                    }
                    player.skills.addXp(SkillConstants.RUNECRAFTING, 45.0)
                    player.inventory.addItem(Item(elementalTiara.elementalTiaraItemId))
                    val elementType = elementalTiara.name.substringBefore("_").lowercase()
                    player.dialogue {
                        doubleItem(
                            elementalTiara.talisManItemId, elementalTiara.elementalTiaraItemId,
                            "You created an $elementType tiara by combining your tiara with an $elementType talisman."
                        )
                    }
                }, 2)
            }
        } else
            player.dialogue { plain("You need both an elemental talisman and a tiara in order to create an elemental tiara.") }
    }

    override fun getItems() = ElementalTiara.values()
        .map { it.talisManItemId }
        .toTypedArray() + ItemId.TIARA

    override fun getObjects() = Runecrafting.VALUES
        .map { it.altarObjectId }
        .filter { it != -1 }
        .toTypedArray()

    private enum class ElementalTiara(
        val runecrafting: Runecrafting,
        val talisManItemId: Int,
        val elementalTiaraItemId: Int,
    ) {
        AIR_TIARA(Runecrafting.AIR_RUNE, ItemId.AIR_TALISMAN, ItemId.AIR_TIARA),
        BLOOD_TIARA(Runecrafting.BLOOD_RUNE, ItemId.BLOOD_TALISMAN, ItemId.BLOOD_TIARA),
        BODY_TIARA(Runecrafting.BODY_RUNE, ItemId.BODY_TALISMAN, ItemId.BODY_TIARA),
        CHAOS_TIARA(Runecrafting.CHAOS_RUNE, ItemId.CHAOS_TALISMAN, ItemId.CHAOS_TIARA),
        COSMIC_TIARA(Runecrafting.COSMIC_RUNE, ItemId.COSMIC_TALISMAN, ItemId.COSMIC_TIARA),
        DEATH_TIARA(Runecrafting.DEATH_RUNE, ItemId.DEATH_TALISMAN, ItemId.DEATH_TIARA),
        EARTH_TIARA(Runecrafting.EARTH_RUNE, ItemId.EARTH_TALISMAN, ItemId.EARTH_TIARA),
        FIRE_TIARA(Runecrafting.FIRE_RUNE, ItemId.FIRE_TALISMAN, ItemId.FIRE_TIARA),
        LAW_TIARA(Runecrafting.LAW_RUNE, ItemId.LAW_TALISMAN, ItemId.LAW_TIARA),
        MIND_TIARA(Runecrafting.MIND_RUNE, ItemId.MIND_TALISMAN, ItemId.MIND_TIARA),
        NATURE_TIARA(Runecrafting.NATURE_RUNE, ItemId.NATURE_TALISMAN, ItemId.NATURE_TIARA),
        WATER_TIARA(Runecrafting.WATER_RUNE, ItemId.WATER_TALISMAN, ItemId.WATER_TIARA),
        WRATH_TIARA(Runecrafting.WRATH_RUNE, ItemId.WRATH_TALISMAN, ItemId.WRATH_TIARA);

        companion object {
            fun findByElementalTalisMan(player: Player) = values()
                .find { player.inventory.containsItem(it.talisManItemId) }
        }
    }
}

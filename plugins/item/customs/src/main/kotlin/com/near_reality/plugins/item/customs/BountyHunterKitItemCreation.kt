package com.near_reality.plugins.item.customs

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class BountyHunterKitItemCreation : PairedItemOnItemPlugin {
    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val target = if(from.isTargetItem()) from else to
        player.dialogue {
            plain("This is an irreversible process and you will not be able to get your kit back.")
            options("Are you sure you want to assemble the upgraded version?") {
                "Yes, I want more power!" {
                    player.inventory.run {
                        if(deleteItems(from, to).result == RequestResult.SUCCESS) {
                            player.sendMessage("You have created the unique variant of your item!")
                            addItem(Item(target.converted()))
                        }
                    }
                }
                "No, I want to keep my items" {}
            }
        }
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_2H_SWORD, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_BATTLEAXE, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_BOOTS, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_CHAINBODY, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_CLAWS, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_CROSSBOW, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_DAGGER, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_HALBERD, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_LONGSWORD, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_MACE, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_MED_HELM, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_PLATELEGS, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_PLATESKIRT, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_SCIMITAR, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_SPEAR, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_SQ_SHIELD, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_SWORD, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.DRAGON_WARHAMMER, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.FIGHTER_TORSO, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.HELM_OF_NEITIZNOT, ItemId.BOUNTY_HUNTER_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.ELDER_MAUL, ItemId.ELDER_MAUL_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.ELDER_CHAOS_TOP, ItemId.ELDER_CHAOS_ROBES_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.ELDER_CHAOS_HOOD, ItemId.ELDER_CHAOS_ROBES_ORNAMENT_KIT),
            ItemOnItemAction.ItemPair.of(ItemId.ELDER_CHAOS_ROBE, ItemId.ELDER_CHAOS_ROBES_ORNAMENT_KIT),
        )
    }

    private fun Item.converted() : Int {
        when(this.id) {
            ItemId.DRAGON_2H_SWORD -> return ItemId.DRAGON_2H_SWORD_CR
            ItemId.DRAGON_BATTLEAXE -> return ItemId.DRAGON_BATTLEAXE_CR
            ItemId.DRAGON_BOOTS -> return ItemId.DRAGON_BOOTS_CR
            ItemId.DRAGON_CHAINBODY -> return ItemId.DRAGON_CHAINBODY_CR
            ItemId.DRAGON_CLAWS -> return ItemId.DRAGON_CLAWS_CR
            ItemId.DRAGON_CROSSBOW -> return ItemId.DRAGON_CROSSBOW_CR
            ItemId.DRAGON_DAGGER -> return ItemId.DRAGON_DAGGER_CR
            ItemId.DRAGON_HALBERD -> return ItemId.DRAGON_HALBERD_CR
            ItemId.DRAGON_LONGSWORD -> return ItemId.DRAGON_LONGSWORD_CR
            ItemId.DRAGON_MACE -> return ItemId.DRAGON_MACE_CR
            ItemId.DRAGON_MED_HELM -> return ItemId.DRAGON_MED_HELM_CR
            ItemId.DRAGON_PLATELEGS -> return ItemId.DRAGON_PLATELEGS_CR
            ItemId.DRAGON_PLATESKIRT -> return ItemId.DRAGON_PLATESKIRT_CR
            ItemId.DRAGON_SCIMITAR -> return ItemId.DRAGON_SCIMITAR_CR
            ItemId.DRAGON_SPEAR -> return ItemId.DRAGON_SPEAR_CR
            ItemId.DRAGON_SQ_SHIELD -> return ItemId.DRAGON_SQ_SHIELD_CR
            ItemId.DRAGON_SWORD -> return ItemId.DRAGON_SWORD_CR
            ItemId.DRAGON_WARHAMMER -> return ItemId.DRAGON_WARHAMMER_CR
            ItemId.FIGHTER_TORSO -> return ItemId.FIGHTER_TORSO_OR
            ItemId.HELM_OF_NEITIZNOT -> return ItemId.HELM_OF_NEITIZNOT_OR
            ItemId.ELDER_MAUL -> return ItemId.ELDER_MAUL_OR
            ItemId.HEAVY_BALLISTA -> return ItemId.HEAVY_BALLISTA_OR
            ItemId.ELDER_CHAOS_TOP -> return ItemId.ELDER_CHAOS_TOP_OR
            ItemId.ELDER_CHAOS_ROBE -> return ItemId.ELDER_CHAOS_ROBE_OR
            ItemId.ELDER_CHAOS_HOOD -> return ItemId.ELDER_CHAOS_HOOD_OR
        }
        return 0
    }


    private fun Item.isTargetItem() : Boolean {
        when(this.id) {
            ItemId.DRAGON_2H_SWORD,
            ItemId.DRAGON_BATTLEAXE,
            ItemId.DRAGON_BOOTS,
            ItemId.DRAGON_CHAINBODY,
            ItemId.DRAGON_CLAWS,
            ItemId.DRAGON_CROSSBOW,
            ItemId.DRAGON_DAGGER,
            ItemId.DRAGON_HALBERD,
            ItemId.DRAGON_LONGSWORD,
            ItemId.DRAGON_MACE,
            ItemId.DRAGON_MED_HELM,
            ItemId.DRAGON_PLATELEGS,
            ItemId.DRAGON_PLATESKIRT,
            ItemId.DRAGON_SCIMITAR,
            ItemId.DRAGON_SPEAR,
            ItemId.DRAGON_SQ_SHIELD,
            ItemId.DRAGON_SWORD,
            ItemId.DRAGON_WARHAMMER,
            ItemId.FIGHTER_TORSO,
            ItemId.ELDER_MAUL,
            ItemId.HEAVY_BALLISTA,
            ItemId.ELDER_CHAOS_TOP,
            ItemId.ELDER_CHAOS_ROBE,
            ItemId.ELDER_CHAOS_HOOD,
            ItemId.HELM_OF_NEITIZNOT -> return true
        }
        return false
    }
}
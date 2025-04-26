package com.near_reality.game.content.elven

import com.near_reality.game.content.crystal.recipes.chargeable.CrystalDegradeable
import com.near_reality.game.content.crystal.CrystalRecipe
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import mgi.types.config.items.ItemDefinitions


fun CrystalRecipe.name(): String =
    ItemDefinitions.nameOf(productItemId)

fun CrystalRecipe.produce() =
    Item(productItemId).apply {
        if (this@produce is CrystalDegradeable)
            this.charges = this@produce.maximumCharges
    }

fun Player.canMake(recipe: CrystalRecipe) =
    skills.getLevel(SkillConstants.CRAFTING) >= recipe.requiredCrafting &&
            skills.getLevel(SkillConstants.SMITHING) >= recipe.requiredSmithing

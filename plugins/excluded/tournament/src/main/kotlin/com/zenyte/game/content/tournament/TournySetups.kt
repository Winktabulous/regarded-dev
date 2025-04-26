package com.zenyte.game.content.tournament

import com.near_reality.api.model.Skill
import com.zenyte.CacheManager
import com.zenyte.game.content.tournament.preset.TournamentPreset
import mgi.types.Definitions
import mgi.types.config.items.ItemDefinitions

fun main() {

    CacheManager.loadDetached()
    Definitions.loadDefinitions(Definitions.lowPriorityDefinitions)


    TournamentPreset.values.forEach {

        println(it.name)
        println("\tequipment:")
        it.equipment.items.forEach {
            val itemName = ItemDefinitions.get(it.value?.t?.id?:0).name
            if ((it.value?.t?.id ?: 0) != 0)
                println("\t\t$itemName       droppable=${it.value?.isBool}")
        }
        println("\tinventory:")
        it.inventory.items.forEach {
            val itemName = ItemDefinitions.get(it.t.id).name
            println("\t\t$itemName       droppable=${it?.isBool}")
        }
        println("\trunepouch:")
        it.runePouch?.entries?.forEach {
            println("\t\t${it.rune}       amount=${it.amount}")
        }
        println("\tspellbook=${it.spellbook}")
        println("\tskills:")
        it.skills.skills.forEach {
            println("\t\t${Skill.entries[it.key]}       level=${it.value}")
        }
        println("\tdisabled prayers:")
        it.disabledPrayers.forEach {
            println("\t\t${it}")
        }

        println("\tmaximum brews=${it.maximumBrews}")
    }
}

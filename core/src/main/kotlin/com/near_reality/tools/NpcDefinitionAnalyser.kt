package com.near_reality.tools

import com.google.gson.GsonBuilder
import com.near_reality.osrsbox_db.ItemDefinitionDatabase
import com.near_reality.osrsbox_db.MonsterDefinitionDatabase
import com.zenyte.CacheManager
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.AnimationDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions

object NpcDefinitionAnalyser {

    @JvmStatic
    fun main(args: Array<String>) {

        CacheManager.loadCache(Cache.openCache("cache/data/cache"))

        AnimationDefinitions().load()

        ItemDefinitions().load()
        ItemDefinitionDatabase.loadFromFile()
        ItemDefinitionDatabase.buildConfigs()

        NPCDefinitions().load()
        NPCCDLoader.parse()
        NPCDrops.init()
        MonsterDefinitionDatabase.loadFromFile()
        MonsterDefinitionDatabase.buildConfigs()

        printCombatDef(NPCCDLoader.get(NpcId.MUTTADILE))
        printCombatDef(NPCCDLoader.get(NpcId.MUTTADILE_7562))
        printCombatDef(NPCCDLoader.get(NpcId.MUTTADILE_7563))
//        for (npcDef in NPCDefinitions.getDefinitions()) {
//            if (npcDef != null){
//                if (npcDef.filteredOptions?.contains("Attack") == true)
//                    print(npcDef)
//            }
//        }

    }

    private fun printCombatDef(muta: NPCCombatDefinitions?) {
        println(GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(muta))
    }

    private fun print(npcDef: NPCDefinitions) {
        val combatDef = NPCCDLoader.get(npcDef.id)
        val npcIdentifier = "${npcDef.name} (id = ${npcDef.id})"
        if (combatDef == null) {
//            println("missing combat definition for $npcIdentifier")
            return
        }
        if (combatDef.hitpoints == 0) {
            println("hp is 0 for $npcIdentifier")
        }

    }
}

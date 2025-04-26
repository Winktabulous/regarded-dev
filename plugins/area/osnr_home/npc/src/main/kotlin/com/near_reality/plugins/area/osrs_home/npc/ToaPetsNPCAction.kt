package com.near_reality.plugins.area.osrs_home.npc

import com.near_reality.game.world.entity.player.toaPetAkkhito
import com.near_reality.game.world.entity.player.toaPetBabi
import com.near_reality.game.world.entity.player.toaPetKephriti
import com.near_reality.game.world.entity.player.toaPetRemnant
import com.near_reality.game.world.entity.player.toaPetZebo
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.content.follower.impl.BossPet
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.dialogue.start
import mgi.types.config.npcs.NPCDefinitions
import com.zenyte.game.world.entity.npc.NpcId.*

/**
 * @author John J. Woloszyk / Kryeus
 */
class ToaPetsNPCAction : NPCActionScript() {

    init {
        npcs(TUMEKENS_GUARDIAN, TUMEKENS_DAMAGED_GUARDIAN, ELIDINIS_DAMAGED_GUARDIAN, ELIDINIS_GUARDIAN, AKKHITO, BABI, KEPHRITI, ZEBO)

        "Transform" {
            if((this.player.follower?.pet?.petId() ?: 0) != this.npc.id) {
                player.sendMessage("You cannot do this to someone else's pet")
            } else {
                val petNpc = player.follower!!
                val pets = this.player.getUnlockedTOAPets()
                /* remove current pet */
                pets.remove(BossPet.getByPetId(petNpc.id))
                when (pets.size) {
                    1 -> {
                        val target = pets.last().petId
                        petNpc.setTransformation(target)
                        player.petId = target
                    }

                    2, 3, 4, 5 -> {
                        startSinglemenu(player, petNpc, pets)
                    }

                    6, 7 -> {
                        startMultimenu(player, petNpc, pets, 1)
                    }
                }
            }
        }
    }

    fun Player.getUnlockedTOAPets() : MutableList<BossPet> {
        val pets = mutableListOf<BossPet>()
        if(this.toaPetAkkhito)
            pets.add(BossPet.AKKHITO)
        if(this.toaPetBabi)
            pets.add(BossPet.BABI)
        if(this.toaPetKephriti)
            pets.add(BossPet.KEPHRITI)
        if(this.toaPetZebo)
            pets.add(BossPet.ZEBO)
        if(this.toaPetRemnant) {
            pets.add(BossPet.ELIDINIS_DAMAGED_GUARDIAN)
            pets.add(BossPet.TUMEKENS_DAMAGED_GUARDIAN)
        }
        pets.add(BossPet.TUMEKENS_GUARDIAN)
        pets.add(BossPet.ELIDINIS_GUARDIAN)
        return pets
    }

    fun startMultimenu(player: Player, npc: NPC, pets: MutableList<BossPet>, page: Int) {
        val size = pets.size
        val thispage: MutableList<BossPet> = mutableListOf()
        if(page == 1) {
            player.dialogueManager.start {
                options("Transform your pet into: ") {
                    thispage.add(pets[0])
                    thispage.add(pets[1])
                    thispage.add(pets[2])
                    thispage.add(pets[3])
                    for (pet in thispage) {
                        val target = pet.petId
                        Dialogue.DialogueOption(NPCDefinitions.get(pet.petId).name).invoke {
                            npc.setTransformation(target)
                            player.petId = target
                        }
                    }
                    Dialogue.DialogueOption("Next Page").invoke {
                        startMultimenu(player, npc, pets, 2)
                    }
                }
            }
        } else {
            player.dialogueManager.start {
                options("Transform your pet into: ") {
                    if(size >= 6) {
                        thispage.add(pets[4])
                        thispage.add(pets[5])
                    }
                    if(size >= 7) {
                        thispage.add(pets[6])
                    }
                    for (pet in thispage) {
                        val target = pet.petId
                        Dialogue.DialogueOption(NPCDefinitions.get(pet.petId).name).invoke {
                            npc.setTransformation(target)
                            player.petId = target
                        }
                    }
                    Dialogue.DialogueOption("Go back").invoke {
                        startMultimenu(player, npc, pets, 1)
                    }
                }
            }
        }

    }

    fun startSinglemenu(player: Player, npc: NPC, pets: MutableList<BossPet>) {
        player.dialogueManager.start {
            options("Transform your pet into: ") {
                for(pet in pets) {
                    val target = pet.petId
                    Dialogue.DialogueOption(NPCDefinitions.get(pet.petId).name).invoke {
                        npc.setTransformation(target)
                        player.petId = target
                    }
                }
            }
        }
    }

}
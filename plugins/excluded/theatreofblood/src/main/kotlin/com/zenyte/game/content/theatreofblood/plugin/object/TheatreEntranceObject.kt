package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.near_reality.game.content.commands.DeveloperCommands
import com.zenyte.game.content.theatreofblood.TheatreOfBloodRaid
import com.zenyte.game.content.theatreofblood.VerSinhazaArea
import com.zenyte.game.content.theatreofblood.interfaces.PartyOverlayInterface
import com.zenyte.game.content.theatreofblood.party.RaidingParty
import com.zenyte.game.content.theatreofblood.plugin.item.VerzikCrystalShard
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectHandler
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.plugins.dialogue.PlainChat

/**
 * @author Tommeh
 * @author Jire
 */
class TheatreEntranceObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (!DeveloperCommands.enabledTOB){
            player.dialogue { plain("Theatre of Blood is currently disabled.") }
            return
        }
        val party = VerSinhazaArea.getParty(player)
        if (party == null) {
            //TODO allow orb of oculus
            val hasLuggage = !player.inventory.container.isEmpty || !player.equipment.container.isEmpty
            val isIll = player.toxins.isIll
            player.dialogueManager.start(object : Dialogue(player) {
                override fun buildDialogue() {
                    options(
                        "You are not in a party...",
                        "Form or join a party",
                        //(if (hasLuggage || isIll) "<str>" else "") + "Observe a specific party",
                        //(if (hasLuggage || isIll) "<str>" else "") + "Observe a recent party",
                        "Cancel"
                    )
                        .onOptionOne {
                            val plugin =
                                ObjectHandler.getPlugin(ObjectId.NOTICE_BOARD_32655) ?: return@onOptionOne
                            plugin.handle(
                                player,
                                noticeBoardObject,
                                noticeBoardObject.name,
                                1,
                                "Read"
                            )
                        }
                        /*.onOptionTwo {
                            if (!player.inventory.container.isEmpty || !player.equipment.container
                                    .isEmpty
                            ) {
                                setKey(5)
                                return@onOptionTwo
                            }
                            if (player.toxins.isIll) {
                                setKey(10)
                                return@onOptionTwo
                            }
                            finish()
                            player.sendInputName("Who would you like to observe?") { name: String ->
                                val p =
                                    World.getPlayerByUsername(name.lowercase(Locale.getDefault()))
                                spectate(player, p)
                            }
                        }
                        .onOptionThree {
                            if (!player.inventory.container.isEmpty || !player.equipment.container
                                    .isEmpty
                            ) {
                                setKey(5)
                                return@onOptionThree
                            }
                            if (player.toxins.isIll) {
                                setKey(10)
                                return@onOptionThree
                            }
                        }*/
                    plain(
                        5,
                        "Spectators cannot bring luggage into the Theatre<br><br>Please visit the bank to unburden yourself."
                    )
                    plain(
                        10,
                        "You appear to be ill; the vampyres would not appreciate your<br><br>company in their Theatre's viewing galleries while you're in that state.<br><br>Come back when you're feeling better."
                    )
                }
            })
            return
        }
        if (!party.isLeader(player) && party.raid == null) {
            player.dialogueManager.start(
                PlainChat(
                    player,
                    "Your leader, " + party.leader!!.name + ", must go first."
                )
            )
            return
        }
        player.dialogueManager.start(object : Dialogue(player) {
            override fun buildDialogue() {
                if (!player.getBooleanAttribute("tob_death_warning")) {
                    plain(
                        Colour.RED.wrap("Warning: ") + "The Theatre of blood is " + Colour.RED.wrap("dangerous.") + " Once you enter, you are at " + Colour.RED.wrap(
                            "risk of death"
                        ) + ". The only method of escape is to resign or<br>defect the Theatre. " + Colour.RED.wrap("Teleporting is restricted") + ", and " + Colour.RED.wrap(
                            "logging out is<br>considered a death"
                        ) + ". Your " + Colour.RED.wrap("items will be lost") + " if the whole party dies."
                    )
                    plain("You will not see the warning again should you accept.")
                    options("Accept the warning and proceed?", "Yes - proceed.", "No - stay out.")
                        .onOptionOne {
                            player.addAttribute("tob_death_warning", 1)
                            if (player.inventory
                                    .containsItem(VerzikCrystalShard.verzikCrystalShard) || player.getBooleanAttribute("verziks_crystals_warning")
                            ) {
                                enter(player, party)
                            } else {
                                setKey(10)
                            }
                        }
                } else {
                    if (player.inventory.containsItem(VerzikCrystalShard.verzikCrystalShard) || player.getBooleanAttribute(
                            "verziks_crystals_warning"
                        )
                    ) {
                        enter(player, party)
                    } else {
                        options(
                            Colour.RED.wrap("Only Verzik's crystals can teleport out of the Theatre."),
                            "Go and buy teleport crystals.",
                            "Enter the Theatre without any teleport crystals.",
                            "Enter the Theatre, and don't ask this again."
                        )
                            .onOptionOne { player.openShop("Mysterious Stranger") }
                            .onOptionTwo { enter(player, party) }
                            .onOptionThree {
                                player.addAttribute("verziks_crystals_warning", 1)
                                enter(player, party)
                            }
                    }
                }
                options(
                    10,
                    Colour.RED.wrap("Only Verzik's crystals can teleport out of the Theatre."),
                    "Go and buy teleport crystals.",
                    "Enter the Theatre without any teleport crystals.",
                    "Enter the Theatre, and don't ask this again."
                )
                    .onOptionOne { player.openShop("Mysterious Stranger") }
                    .onOptionTwo { enter(player, party) }
                    .onOptionThree {
                        player.addAttribute("verziks_crystals_warning", 1)
                        enter(player, party)
                    }
            }
        })
    }

    override fun getObjects() = TheatreEntranceObject.objects

    private companion object {

        val objects = arrayOf(ObjectId.THEATRE_OF_BLOOD_32653)

        val noticeBoardObject = WorldObject(32655, 10, 3, Location(3662, 3218, 0))

        fun enter(player: Player, party: RaidingParty) {
            if (player.containsItem(ItemId.DAWNBRINGER)) {
                player.dialogue { item(Item(ItemId.DAWNBRINGER), "You can't enter the Theatre of Blood when you possess a Dawnbringer.") }
                return
            }

            if (!party.isLeader(player) && party.raid != null) {
                party.raid!!.enter(player)
                return
            }
            player.dialogueManager.start(object : Dialogue(player) {
                override fun buildDialogue() {
                    options("Is your party ready? (Members: " + party.members.size + ")", "Yes, let's go!", "Cancel.")
                        .onOptionOne {
                            val raid = TheatreOfBloodRaid(party)
                            party.raid = raid
                            raid.enter(player)
                        }
                }
            })
        }

        fun spectate(spectator: Player, player: Player?) {
            if (player == null || player.socialManager.isOffline) {
                spectator.sendMessage("That player is offline, or has privacy mode enabled.")
                return
            }
            val party = VerSinhazaArea.getParty(
                player,
                checkMembers = true,
                checkViewers = false,
                checkSpectators = true
            )
            if (party?.raid == null) {
                spectator.dialogueManager.start(
                    PlainChat(
                        spectator,
                        player.name + " does not appear to be in the Theatre."
                    )
                )
                return
            }
            PartyOverlayInterface.fade(spectator, 0, 0, "Seeking " + player.name + "...")
            WorldTasksManager.schedule({
                val raid = party.raid!!
                val activeRoom = raid.getActiveRoom()!!
                PartyOverlayInterface.fade(spectator, 255, 0, activeRoom.roomType.roomName)
                raid.spectators.add(spectator.username)
                party.initializeStatusHUD(spectator)
                spectator.setLocation(activeRoom.spectatingLocation)
            }, 2)
        }

    }

}

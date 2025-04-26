package com.near_reality.game.content.commands

import com.near_reality.game.model.item.getItemValue
import com.near_reality.game.world.entity.player.flaggedAsBot
import com.zenyte.game.GameConstants
import com.zenyte.game.GameInterface
import com.zenyte.game.content.boss.phantommuspah.PhantomInstance
import com.zenyte.game.content.clans.ClanManager
import com.zenyte.game.content.event.christmas2019.AChristmasWarble
import com.zenyte.game.content.flowerpoker.FlowerPokerAreas
import com.zenyte.game.content.flowerpoker.FlowerPokerManager
import com.zenyte.game.content.flowerpoker.FlowerPokerSession
import com.zenyte.game.content.grandexchange.GrandExchange
import com.zenyte.game.content.grandexchange.GrandExchangeHandler
import com.zenyte.game.content.grandexchange.GrandExchangePriceManager
import com.zenyte.game.content.partyroom.PartyRoomVariables
import com.zenyte.game.content.skills.farming.hespori.HesporiInstance
import com.zenyte.game.content.well.WellConstants
import com.zenyte.game.content.xamphur.XamphurHandler
import com.zenyte.game.item.Item
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Levensthein.findNearestMatchOrNull
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.AnimationMap
import com.zenyte.game.world.entity.HitBar
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.RemoveHitBar
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.calog.CAType
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.login.InvitedPlayersList
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.dialogue.OptionsMenuD
import com.zenyte.plugins.renewednpc.MysteryBoxMan
import com.zenyte.plugins.renewednpc.ZenyteGuide
import com.zenyte.utils.StringUtilities
import com.zenyte.utils.TimeUnit
import mgi.types.component.ComponentDefinitions
import mgi.types.config.ObjectDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import mgi.utilities.StringFormatUtil
import java.lang.Boolean
import java.util.*
import java.util.function.BiConsumer
import kotlin.Array
import kotlin.Int
import kotlin.String
import kotlin.arrayOf
import kotlin.jvm.optionals.getOrNull
import kotlin.math.min

object AdministratorCommands {

    fun register() {
        Command(PlayerPrivilege.ADMINISTRATOR, "gelock", "Quarantine's a players GE offers, hiding them from other players") { p: Player, _: Array<String?> ->
            p.sendInputName("Enter name of player") { playerName ->
                val targetPlayer = World.getPlayer(playerName).getOrNull()
                if (targetPlayer == null)
                    p.dialogue { plain("No player online found by name `$playerName`") }
                else {
                    GrandExchangeHandler.quarantineOffers(targetPlayer)
                    p.sendMessage("Offers have been quarantined for $playerName")
                }
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "td") { p, _ ->
            p.setLocation(Location(4061, 4465, 0))
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "araxxor") { p, _ ->
            p.setLocation(Location(3657, 3404, 0))
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "geunlock", "Restores a players GE offers") { p: Player, _: Array<String?> ->
            p.sendInputName("Enter name of player") { playerName ->
                val targetPlayer = World.getPlayer(playerName).getOrNull()
                if (targetPlayer == null)
                    p.dialogue { plain("No player online found by name `$playerName`") }
                else {
                    GrandExchangeHandler.restoreOffers(playerName)
                    p.sendMessage("Offers have been restored to the GE for $playerName")
                }
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "gestatus", "Checks the current status of a player's GE offers") { p: Player, _: Array<String?> ->
            p.sendInputName("Enter name of player") { playerName ->
                val targetPlayer = World.getPlayer(playerName).getOrNull()
                if (targetPlayer == null)
                    p.dialogue { plain("No player online found by name `$playerName`") }
                else {
                    p.sendMessage("Player [$playerName] GE Quarantine Enabled: ${GrandExchangeHandler.isQuarantined(playerName)}")
                }
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "togglembox", "Toggles mystery box claiming") { p: Player, _: Array<String?> ->
            MysteryBoxMan.enabled = !MysteryBoxMan.enabled
            p.sendMessage("Mystery box claiming is now " + if (MysteryBoxMan.enabled) "enabled" else "disabled")
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "resetfplane", "Resets FP lane being stood in") { p: Player, _: Array<String?> ->
            FlowerPokerAreas.resetArea(p.location)
            p.sendMessage("This lane has been reset.")
        }
        Command(PlayerPrivilege.MODERATOR, "flagbot") { p, _ ->
            p.sendInputName("Enter name of botter") { botName ->
                val targetPlayer = World.getPlayer(botName).getOrNull()
                if (targetPlayer == null)
                    p.dialogue { plain("No player online found by name `$botName`") }
                else {
                    fun showDialogueOptions() {
                        p.dialogue {
                            options {
                                (if (targetPlayer.flaggedAsBot) "Unflag as bot" else "Flag as bot") {
                                    targetPlayer.flaggedAsBot = !targetPlayer.flaggedAsBot
                                    p.dialogue {
                                        plain("Player ${targetPlayer.name} is now ${if (targetPlayer.flaggedAsBot) "flagged as bot" else "unflagged as bot"}").executeAction {
                                            showDialogueOptions()
                                        }
                                    }
                                }
                                "View accounts with same ip" {
                                    val ip = targetPlayer.ip
                                    val linkedPlayers = World.getPlayers().filter { it.ip == ip }
                                    fun showOptions() {
                                        val linkedPlayerNames = linkedPlayers.map { "${if(it.flaggedAsBot) "Unflag" else "Flag" } ${Colour.RS_RED.wrap(it.username)} as a bot"  }.toTypedArray()
                                        p.dialogueManager.start(object : OptionsMenuD(p, "Select the account to flag as bot", *linkedPlayerNames) {
                                            override fun handleClick(slotId: Int) {
                                                val target = linkedPlayers[slotId]
                                                target.flaggedAsBot = !target.flaggedAsBot
                                                p.sendMessage("Player ${target.username} is now ${if(target.flaggedAsBot) "flagged as bot" else "unflagged as bot"}")
                                                showOptions()
                                            }
                                        })
                                    }
                                    showOptions()
                                }
                            }
                        }
                    }
                    showDialogueOptions()
                }
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "freezeitem") { p, _ ->
            p.sendInputInt("What item would you like to disable?") { item ->
                p.sendInputInt("Enter 1 to enable, 0 to disable the following item: " + ItemDefinitions.get(item).name) { value: Int ->
                    val defs = ItemDefinitions.get(item)
                    if (defs != null) {
                        when (value) {
                            1 -> {
                                Item.itemBlacklist.rem(item)
                                p.sendMessage("Sales and trading of ${ItemDefinitions.get(item).name} has been enabled")
                            }

                            0 -> {
                                Item.itemBlacklist.add(item)
                                p.sendMessage("Sales and trading of ${ItemDefinitions.get(item).name} has been disabled")
                            }

                            else -> {
                                p.sendMessage("Please enter a valid option (1 or 0)")
                            }
                        }
                    } else {
                        p.sendMessage("This item does not exist")
                    }
                }
            }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "nanims", "Gets info about an NPC's anims. Argument: id",
            BiConsumer { p: Player, args: Array<String?> ->
                val id = Integer.valueOf(args[0])
                val d = NPCDefinitions.get(id)
                if (d == null) {
                    p.sendMessage("Invalid NPC ID $id")
                    return@BiConsumer
                }
                p.sendMessage("stand=" + d.standAnimation + ", walk=" + d.walkAnimation
                            + ", rot90=" + d.rotate90Animation + ", rot180=" + d.rotate180Animation
                            + ", rot270=" + d.rotate270Animation)
            })
        Command(PlayerPrivilege.ADMINISTRATOR, "freeze") { p: Player, args: Array<String> ->
            val freezeDuration = if (args.isEmpty()) 10 else args[0].toInt()
            p.freezeWithNotification(freezeDuration)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "disablege") { p: Player, _: Array<String?>? ->
            GrandExchange.ENABLED = !GrandExchange.ENABLED
            p.sendMessage("GE is " + if (GrandExchange.ENABLED) "Enabled" else "Disabled")
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "disablefp") { p: Player, _: Array<String?>? ->
            FlowerPokerManager.FLOWER_POKER_ENABLED = !FlowerPokerManager.FLOWER_POKER_ENABLED
            p.sendMessage("FP is " + if (FlowerPokerManager.FLOWER_POKER_ENABLED) "Enabled" else "Disabled")
            FlowerPokerManager.FLOWER_POKER_AREAS.forEach { (_: FlowerPokerAreas?, s: FlowerPokerSession?) -> if (s?.planting != null) s.planting.cancelAndRefund() }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "disablewell") { p: Player, _: Array<String?>? ->
            WellConstants.WELL_DISABLED = !WellConstants.WELL_DISABLED
            p.sendMessage("Well is " + if (WellConstants.WELL_DISABLED) "Enabled" else "Disabled")
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "disablexamphur") { p: Player, _: Array<String?>? ->
            XamphurHandler.get().isEnabled = !XamphurHandler.get().isEnabled
            p.sendMessage("Xamphur is " + if (XamphurHandler.get().isEnabled) "Enabled" else "Disabled")
            val xamphur = XamphurHandler.get().xamphur
            if (xamphur.fightStarted) xamphur.applyHit(Hit(xamphur.hitpoints, HitType.MELEE))
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "disableworldboosts") { p: Player, _: Array<String?>? ->
            World.getWorldBoosts().clear()
            p.sendMessage("World boosts cleared")
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "resetevent") { p: Player, args: Array<String?>? ->
            p.attributes.remove(AChristmasWarble.ChristmasWarbleProgress.EVENT_ATTRIBUTE_KEY)
            p.attributes.remove("A Christmas Warble unfrozen guests hash")
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "completeevent") { p: Player?, args: Array<String?>? ->
            AChristmasWarble.progress(p, AChristmasWarble.ChristmasWarbleProgress.EVENT_COMPLETE)
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "progress") { p: Player, args: Array<String?>? ->
            p.sendMessage(AChristmasWarble.getProgress(p).name)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "memory") { p: Player, args: Array<String?>? ->
            val runtime = Runtime.getRuntime()
            val totalMem = runtime.totalMemory()
            val freeMem = runtime.freeMemory()
            val maxMem = runtime.maxMemory()
            p.sendMessage("Memory specifications: ")
            p.sendMessage("Free memory: " + StringFormatUtil.format(freeMem))
            p.sendMessage("Used memory: " + StringFormatUtil.format(totalMem - freeMem))
            p.sendMessage("Total memory: " + StringFormatUtil.format(totalMem))
            p.sendMessage("Max memory: " + StringFormatUtil.format(maxMem))
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "disablehydra") { p: Player, args: Array<String?>? ->
            GameConstants.ALCHEMICAL_HYDRA = !GameConstants.ALCHEMICAL_HYDRA
            p.sendMessage("Alchemical Hydra: " + GameConstants.ALCHEMICAL_HYDRA)
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "disableraids") { p: Player, args: Array<String?>? ->
            GameConstants.CHAMBERS_OF_XERIC = !GameConstants.CHAMBERS_OF_XERIC
            p.sendMessage("Chambers of Xeric: " + GameConstants.CHAMBERS_OF_XERIC)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "tempattr") { p: Player, args: Array<String?>? ->
            p.sendInputString("Enter name of the temporary attribute") { key: String? ->
                p.dialogueManager.finish()
                p.sendInputInt("Enter value of the temporary attribute") { value: Int ->
                    p.temporaryAttributes[key] = value
                    p.sendMessage("Temporary attribute " + Colour.RS_RED.wrap(key) + " value set to " + Colour.RS_RED.wrap(value.toString()))
                }
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "forceprice", "Initiates forcing a price change for an item.") { p: Player, args: Array<String?>? ->
            p.sendInputItem("What item's price would you like to change?") { item: Item ->
                p.sendInputInt("What price would you like to set for " + item.name + "?") { value: Int ->
                    val existingPrice = getItemValue(item.id)
                    GrandExchangePriceManager.forcePrice(item.id, value)
                    ItemDefinitions.get(item.id)?.price = value
                    p.sendMessage("Price of " + item.name + " changed from " + StringFormatUtil.format(existingPrice) + " to " + StringFormatUtil.format(value) + ".")
                }
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "invite", "Grant beta access to a user. Usage: ::invite " + "player_name") { p: Player, args: Array<String?> ->
            val name = StringFormatUtil.formatUsername(StringUtilities.compile(args, 0, args.size, '_'))
            if (name.length in 1..12) {
                InvitedPlayersList.invitedPlayers.add(name)
                p.sendMessage("Granted beta access to $name.")
            }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "randomfrequency") { p: Player, args: Array<String> ->
            val value = args[0].toInt()
            GameConstants.randomEvent = TimeUnit.HOURS.toTicks(value.toLong()).toInt()
            p.sendMessage("Random events are on average now occuring every $value hours.")
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "status", "Get status of a user.", BiConsumer { player: Player, strings: Array<String?> ->
                val targetName = StringUtilities.compile(strings, 0, strings.size, ' ')
                val targetPlayer = World.getPlayer(targetName)
                if (!targetPlayer.isPresent) {
                    player.sendMessage("The user $targetName is not online.")
                    return@BiConsumer
                }
                val t = targetPlayer.get()
                player.sendMessage(Colour.RS_RED.wrap("Status on " + t.name + ":"))
                player.sendMessage("Logout type: " + t.logoutType)
                player.sendMessage("Last packet received: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - t.lastReceivedPacket) + " seconds ago")
                player.sendMessage("-------------------")
        })
        Command(PlayerPrivilege.ADMINISTRATOR, "duelarena", "Toggle duel arena access.") { p: Player, _: Array<String?>? ->
            GameConstants.DUEL_ARENA = !GameConstants.DUEL_ARENA
            p.sendMessage("Duel Arena: " + GameConstants.DUEL_ARENA)
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "grots", "Toggle grotesque guardians.") { p: Player, _: Array<String?>? ->
            GameConstants.GROTESQUE_GUARDIANS = !GameConstants.GROTESQUE_GUARDIANS
            p.sendMessage("Grotesque Guardians: " + GameConstants.GROTESQUE_GUARDIANS)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "partyroom", "Opens the party room modification menu.") { p: Player?, _: Array<String?>? ->
            PartyRoomVariables.openEditMode(p!!)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "campos") { p: Player, args: Array<String> ->
            var x = p.x
            var y = p.y
            var plane = 1000
            var speed = 127
            var acceleration = 127
            if (args.isNotEmpty()) { x = args[0].toInt() }
            if (args.size > 1) { y = args[1].toInt() }
            if (args.size > 2) { plane = args[2].toInt() }
            if (args.size > 3) { speed = args[3].toInt() }
            if (args.size > 4) { acceleration = args[4].toInt() }
            CameraPositionAction(p, Location(x, y), plane, speed, acceleration).run()
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "camlook") { p: Player, args: Array<String> ->
            var x = p.x
            var y = p.y
            var plane = 1000
            var speed = 127
            var acceleration = 127
            if (args.isNotEmpty()) { x = args[0].toInt() }
            if (args.size > 1) { y = args[1].toInt() }
            if (args.size > 2) { plane = args[2].toInt() }
            if (args.size > 3) { speed = args[3].toInt() }
            if (args.size > 4) { acceleration = args[4].toInt() }
            CameraLookAction(p, Location(x, y), plane, speed, acceleration).run()
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "camreset") { p: Player, args: Array<String?>? ->
            p.packetDispatcher.resetCamera()
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "testproj") { p: Player, args: Array<String> ->
            val proj = Projectile(args[0].toInt(), 50, 50, 0, 0, 50, 0, 5)
            World.sendProjectile(p.location, Location(p.x + 10, p.y, p.plane), proj)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "combatdebug") { p: Player, args: Array<String?> ->
            p.temporaryAttributes["combat" + " " + "debug"] = Boolean.valueOf(args[0])
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "perks") { p: Player, args: Array<String?>? ->
            val builder = StringBuilder()
            builder.append("Unlocked perks:<br>")
            for ((_, value) in p.perkManager.perks) {
                builder.append("- <col=00080>")
                builder.append(value.name)
                builder.append("</col><br>")
            }
            p.sendMessage(builder.toString())
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "open") { p: Player?, args: Array<String?> ->
            val name = StringUtilities.compile(args, 0, args.size, ' ')
            findNearestMatchOrNull(name, GameInterface.values, true) { gameInterface: GameInterface ->
                gameInterface.toString().replace("_".toRegex(), " ")
            }.open(p)
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "scene") { p: Player, args: Array<String> ->
            p.viewDistance = args[0].toInt()
        }
        Command(PlayerPrivilege.ADMINISTRATOR, arrayOf("drops", "drop", "dropviewer"), "Opens the drop viewer.") { p: Player?, _: Array<String?>? ->
            GameInterface.DROP_VIEWER.open(p)
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "scrapdrops") { p: Player?, _: Array<String?>? -> }
        Command(PlayerPrivilege.ADMINISTRATOR, "chunkhash") { p: Player, args: Array<String?>? ->
            val x = p.x
            val y = p.y
            val hash = x shr 3 shl 16 or (y shr 3)
            p.sendMessage("Chunk hash: $hash")
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "value") { p: Player, args: Array<String?> ->
            val id = Integer.valueOf(args[0])
            val definitions = ItemDefinitions.get(id)
            p.sendMessage("Value of " + definitions.name + " is " + definitions.price)
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "play") { p: Player, args: Array<String> ->
            p.packetDispatcher.sendMusic(args[0].toInt())
        }


        Command(PlayerPrivilege.ADMINISTRATOR, "cmbdeb") { p: Player, args: Array<String?>? ->
            val current = p.getBooleanTemporaryAttribute("combat debug")
            if (!current) {
                p.putBooleanTemporaryAttribute("combat debug", true)
                p.sendMessage("Combat debug is enabled")
            } else {
                p.putBooleanTemporaryAttribute("combat debug", false)
                p.sendMessage("Combat debug is disabled")
            }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "testhitbar") { p: Player, args: Array<String> ->
            p.hitBars.clear()
            if (p.hitBars.size > 0) {
                p.hitBars.add(RemoveHitBar(p.hitBars[0].type))
            }
            val testType = args[0].toInt()
            p.hitBars.add(object : HitBar() {
                override fun getType(): Int { return testType }
                override fun getPercentage(): Int { return 50 }
            })
            p.updateFlags.flag(UpdateFlag.HIT)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "toa") { p: Player?, args: Array<String?>? ->
            GameInterface.TOA_PARTY_OVERVIEW.open(p)
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "tmask") { p: Player, args: Array<String?>? ->
            p.sendMessage(p.location.toString() + "-> " + World.getMask(p.location))
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "toatest") { p: Player, args: Array<String> ->
            p.interfaceHandler.sendInterface(object : Interface() {
                override fun attach() {}
                override fun build() {}
                override fun getInterface(): GameInterface { return GameInterface.TOA_PARTY_MANAGEMENT }
            })
            for (i in 0..7) { p.packetDispatcher.sendClientScript(6722, 2, "") }
            p.packetDispatcher.sendClientScript(6729, args[0].toInt(), 0, 0, 0, 0, 0, 0, 0)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "xamphur") { p: Player?, args: Array<String?>? ->
            XamphurHandler.get().addVotes(p, 150)
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "disablejoin") { p: Player, args: Array<String?>? ->
            ZenyteGuide.disableJoinAnnouncement = !ZenyteGuide.disableJoinAnnouncement
            if (ZenyteGuide.disableJoinAnnouncement) {
                p.sendMessage("New players announcement are disabled.")
            } else {
                p.sendMessage("New players announcement are enabled.")
            }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "pestpts") { p: Player, _: Array<String?>? ->
            p.addAttribute("pest_control_points", 10000)
            p.sendMessage("You get pest control points.")
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "cgtest") { p: Player, args: Array<String> ->
            p.attributes["corrupted_gauntlet_completions"] = args[0].toInt()
            p.sendMessage("Corrupted gauntlet kc set to " + p.getNumericAttribute("corrupted_gauntlet_completions"))
        }

        Command(PlayerPrivilege.MODERATOR, "urp") { p: Player, args: Array<String?> ->
            World.getPlayer(StringUtilities.compile(args, 0, args.size, ' ')).ifPresent { user: Player ->
                World.unregisterPlayer(user)
                p.sendMessage("Unregistered " + user.name + ".")
            }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "testspecialenergy") { p: Player, args: Array<String?>? ->
            p.combatDefinitions.specialEnergy = -10
            p.delay(10) { p.combatDefinitions.specialEnergy = 1000 }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "broadcast", "Initiates sending a server-wide broadcast. No arguments required.") { p: Player, _: Array<String?>? ->
            p.sendInputString("Enter text to broadcast: ") { string: String ->
                p.dialogueManager.start(object : Dialogue(p) {
                    override fun buildDialogue() {
                        plain("Broadcast message: <br>$string")
                        options(
                            "Broadcast it?",
                            DialogueOption("Yes.") { World.sendMessage(MessageType.GLOBAL_BROADCAST, string) },
                            DialogueOption("No.")
                        )
                    }
                })
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "clanunban", "Unban a user from your clan.") { p: Player, args: Array<String?> ->
            val name = StringUtilities.compile(args, 0, args.size, '_')
            val clan = ClanManager.getChannel(p.username)
            if (clan.isPresent) {
                val bannedMembers = clan.get().bannedMembers
                if (bannedMembers.removeLong(StringFormatUtil.formatUsername(name)) != 0L) {
                    p.sendMessage("User successfully unbanned from clan.")
                } else {
                    p.sendMessage("Could not find user $name.")
                }
            } else { p.sendMessage("You do not own a clan.") }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "resetbank", "Wipes your bank.") { p: Player, _: Array<String?>? ->
            p.bank.resetBank()
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "inter",
            BiConsumer { p: Player, args: Array<String> ->
                val id = args[0].toInt()
                if (!ComponentDefinitions.containsInterface(id)) {
                    p.sendMessage("Interface $id doesn't exist.")
                    return@BiConsumer
                }
                p.interfaceHandler.sendInterface(InterfacePosition.CENTRAL, id)
            })
        Command(PlayerPrivilege.ADMINISTRATOR, "buyperks") { p: Player?, _: Array<String?>? ->
            GameInterface.PVPW_PERKS.open(p)
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "openexch") { p: Player?, _: Array<String?>? ->
            GameInterface.REMNANT_EXCHANGE.open(p)
        }
        Command(PlayerPrivilege.DEVELOPER, "amasks") { p: Player, args: Array<String> ->
            val interfaceID = args[0].toInt()
            for (childID in 0..99) {
                p.packetDispatcher.sendComponentSettings(interfaceID, childID, 0, 100, AccessMask.CLICK_OP1)
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "closeinter") { p: Player, args: Array<String> ->
            p.interfaceHandler.closeInterface(args[0].toInt())
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "fanim") { p: Player, args: Array<String?> ->
            val id = Integer.valueOf(args[0])
            p.forceAnimation(Animation(id))
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "anim", "Performs the requested animation. Argument: id", BiConsumer { p: Player, args: Array<String?> ->
                val id = Integer.valueOf(args[0])
                if (!AnimationMap.isValidAnimation(p.appearance.npcId, id)) {
                    p.sendMessage("Invalid animation.")
                    return@BiConsumer
                }
                p.animation = Animation(id)
            })
        Command(PlayerPrivilege.ADMINISTRATOR, "npcallanim") { p: Player?, args: Array<String?>? ->
            for (npc in World.getNPCs()) { npc.animation = Animation.STOP }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "resettask", "Reset a user's slayer task. Usage: ::resettask player name") { _: Player?, args: Array<String?> ->
            val player: Optional<Player> = World.getPlayer(StringUtilities.compile(args, 0, args.size, ' '))
            player.ifPresent { a: Player -> a.slayer.removeTask() }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "cs2") { p: Player, args: Array<String> -> p.packetDispatcher.sendClientScript(args[0].toInt()) }
        Command(PlayerPrivilege.ADMINISTRATOR, "phantom") { p: Player?, _: Array<String?>? -> PhantomInstance.start(p) }
        Command(PlayerPrivilege.ADMINISTRATOR, "completeca") { p: Player, args: Array<String> ->
            val start: Int = args[0].toInt()
            p.sendMessage("Completing tasks.")
            var end: Int = start
            if (args.size > 1) { end = min(CAType.values.size, args[1].toInt()) }
            for (i in start..end) {
                p.combatAchievements.complete(CAType.values[i])
            }
        }
        Command(PlayerPrivilege.DEVELOPER, "completecabyvar") { p: Player, args: Array<String> ->
            p.sendInputString("Enter player to unlock achievement for: ") { string: String ->
                val target = World.getPlayer(string)
                if(target.isEmpty)
                    return@sendInputString
                else {
                    val varImpl: Int = args[0].toInt()
                    p.sendMessage("Completing tasks.")
                    val caOption = CAType.getByVar(varImpl)
                    if(caOption.isPresent){
                        val ca = caOption.get()
                        target.get().combatAchievements.complete(ca)
                        target.get().sendMessage("${p.username} has just unlocked the combat achievement ${ca.getName()} for you")
                        p.sendMessage("You have unlocked the combat achievement ${ca.getName()} for ${target.get().username}")
                    }
                }
            }


        }
        Command(PlayerPrivilege.ADMINISTRATOR, "hespori") { p: Player?, _: Array<String?>? -> HesporiInstance.start(p) }
        Command(PlayerPrivilege.ADMINISTRATOR, "resetca") { p: Player, args: Array<String> ->
            val start: Int = args[0].toInt()
            p.sendMessage("Completing tasks.")
            var end: Int = start
            if (args.size > 1) { end = min(CAType.values.size, args[1].toInt()) }
            for (i in start..end) {
                p.combatAchievements.resetTask(CAType.values[i].varIndex)
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "varbit", ("Sends a varbit of the requested id and value. Arguments: id value")) { p: Player, args: Array<String> ->
            p.varManager.sendBit(args[0].toInt(), args[1].toInt())
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "getobjvarbit") { p: Player, args: Array<String> ->
            p.sendMessage(("Varbit for " + args[0] + " is: " + ObjectDefinitions.get(args[0].toInt()).varbit))
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "setobjvarbit") { p: Player, args: Array<String> ->
            p.varManager.sendVar(args[0].toInt(), args[1].toInt())
            p.sendMessage("Set varbit id " + args[0] + " to value: " + args[1])
        }

    }

}

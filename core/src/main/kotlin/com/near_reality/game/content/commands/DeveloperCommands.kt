package com.near_reality.game.content.commands

import com.near_reality.game.content.shop.ShopCurrencyHandler
import com.near_reality.game.model.ui.credit_store.CreditStoreModel
import com.near_reality.game.model.ui.credit_store.coinbaseEnabled
import com.near_reality.game.util.PlayerAttributesEditor
import com.near_reality.game.world.entity.player.FakePlayer
import com.zenyte.GameToggles
import com.zenyte.game.GameConstants
import com.zenyte.game.GameConstants.WORLD_PROFILE
import com.zenyte.game.GameConstants.isOwner
import com.zenyte.game.content.achievementdiary.Diary
import com.zenyte.game.content.compcapes.CompletionistCape
import com.zenyte.game.content.treasuretrails.ClueItem
import com.zenyte.game.content.treasuretrails.ClueLevel
import com.zenyte.game.content.treasuretrails.TreasureTrail
import com.zenyte.game.content.treasuretrails.challenges.ClueWithNpcs
import com.zenyte.game.content.treasuretrails.challenges.ClueWithObjects
import com.zenyte.game.content.treasuretrails.challenges.DigRequest
import com.zenyte.game.content.treasuretrails.challenges.GameObject
import com.zenyte.game.content.treasuretrails.clues.*
import com.zenyte.game.content.xamphur.XamphurBoost
import com.zenyte.game.content.xamphur.XamphurHandler
import com.zenyte.game.item.Item
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.packet.out.RebuildNormal
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.drop.matrix.DropPrediction
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.entity.player.collectionlog.CollectionLogRewardHandler
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.CharacterLoop
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.net.NetworkConstants
import com.zenyte.plugins.dialogue.OptionsMenuD
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import mgi.types.config.ObjectDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.jvm.optionals.getOrNull

object DeveloperCommands {
    var automatedSanctions = false
    var ipBasedDetections = false
    var anticheatEnabled = false
    var enabledGauntlet = true
    var toggledDT2Off = false
    var enabledLootKeys = false
    var enabledNex = true
    var enabledZalcano = true
    var enabledTOB = true
    var enabledLarranKeys = true
    var npcLogging = false
    var npcProcessTimeLogging = false
    var enabledDPinRedeeming = WORLD_PROFILE.verifyPasswords && !WORLD_PROFILE.isBeta() && !WORLD_PROFILE.isDevelopment() && !WORLD_PROFILE.private
    var adminsLoseItemsOnDeath = WORLD_PROFILE.isBeta() || WORLD_PROFILE.isDevelopment() || WORLD_PROFILE.private
    var doubleXamphurDrops = false
    var enableWildernessVault = false
    var forceApiForLogin = java.util.concurrent.atomic.AtomicBoolean(false)

    val logger: Logger = LoggerFactory.getLogger(DeveloperCommands::class.java)

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    fun register() {

        Command(PlayerPrivilege.TRUE_DEVELOPER, "magicbuffet") { player, _ ->
            if(isOwner(player)) {
                val length = XamphurBoost.VALUES.size
                for (i in 0 until length) {
                    XamphurHandler.activateBoost(
                        XamphurBoost.VALUES[i], 1
                    )
                }

                WorldBroadcasts.sendMessage(
                    "ALL World Boosts activated by " + player.titleName,
                    BroadcastType.WELL_OF_GOODWILL,
                    true)

            } else {
                player.sendMessage("Try again next time.")
            }

        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "magicbuffetx") { player, args ->
            if(isOwner(player)) {
                val time = args[0].toInt()
                if(time > 24) {
                    return@Command
                }
                val length = XamphurBoost.VALUES.size
                for (i in 0 until length) {
                    XamphurHandler.activateBoost(
                        XamphurBoost.VALUES[i], time
                    )
                }

                WorldBroadcasts.sendMessage(
                    "ALL World Boosts activated by " + player.titleName + " for $time hours!",
                    BroadcastType.WELL_OF_GOODWILL,
                    true)

            } else {
                player.sendMessage("Try again next time.")
            }

        }

        Command(PlayerPrivilege.DEVELOPER, "togglebountyhunter") { player, _ ->
            GameToggles.BH2020_ENABLED = !GameToggles.BH2020_ENABLED
            player.sendMessage("Bounty Hunter Enabled: ${GameToggles.BH2020_ENABLED}")
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "allowt3compcape") { player, args  ->
            if(isOwner(player)) {
                val username = args[0] as String
                CompletionistCape.ALLOWED_PLAYERS.add(username.lowercase())
            } else {
                player.sendMessage("Try again next time.")
            }

        }

        Command(PlayerPrivilege.DEVELOPER, "attackabledebug") { p, _ ->
            PlayerCombat.DEBUG_ATTACKABLE_STATE = !PlayerCombat.DEBUG_ATTACKABLE_STATE
            p.sendMessage("Combat debug is now ${if (PlayerCombat.DEBUG_ATTACKABLE_STATE) "enabled" else "disabled"}.")
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "toggleapilogin") { p, _ ->
            forceApiForLogin.set(forceApiForLogin.get().not())
            p.sendMessage("Forcing API login is now ${if (forceApiForLogin.get()) "enabled" else "disabled"}.")
        }

        Command(PlayerPrivilege.DEVELOPER, "giveitem") { p, args ->
            p.sendInputItem("What item would you like to give?") { item: Item ->
                p.sendInputInt("Enter the item quantity of " + item.name) { value: Int ->
                    val defs = ItemDefinitions.get(item.id)
                    item.amount = value
                    if (defs != null) {
                        p.sendInputString("What player would you like to give this item to?") {input: String ->
                            val player : Player? = World.getPlayer(input).getOrNull()
                            player?.bank?.add(item) ?: return@sendInputString
                            p.sendMessage("Deposited $value x ${item.name} into ${player.name}'s bank")
                        }
                    } else {
                        p.sendMessage("This item does not exist")
                    }
                }
            }
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "toggleautosanction") { p, _ ->
            if(automatedSanctions) {
                automatedSanctions = false
                p.sendMessage("Automated Sanctions have been disabled")
            } else {
                automatedSanctions = true
                p.sendMessage("Automated Sanctions have been enabled")
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "toggledt2") { p, _ ->
            if(toggledDT2Off) {
                toggledDT2Off = false
                p.sendMessage("DT2 Bosses have been disabled")
            } else {
                toggledDT2Off = true
                p.sendMessage("DT2 Bosses have been enabled")
            }
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "toggleipfilter") { p, _ ->
            if(ipBasedDetections) {
                ipBasedDetections = false
                p.sendMessage("IP-based checks have been disabled")
            } else {
                ipBasedDetections = true
                p.sendMessage("IP-based checks have been enabled")
            }
        }

        Command(PlayerPrivilege.TRUE_DEVELOPER, "toggleanticheat") { p, _ ->
            if(anticheatEnabled) {
                anticheatEnabled = false
                p.sendMessage("Anti-Cheat has been disabled")
            } else {
                anticheatEnabled = true
                p.sendMessage("Anti-Cheat has been enabled")
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "immune") { p, _ ->
            if(!p.immune) {
                p.sendMessage("Immunity enabled")
                p.immune = true
            } else {
                p.sendMessage("Immunity disabled")
                p.immune = false
            }
        }


        Command(PlayerPrivilege.DEVELOPER, "coinbase") { p, _ ->
            p.dialogue {
                options {
                    (if (coinbaseEnabled) "disable" else "enable")  {
                        coinbaseEnabled = !coinbaseEnabled
                        p.dialogue {
                            plain("Coinbase payments are now ${if (coinbaseEnabled) "enabled" else "disabled"}.")
                        }
                    }
                }
            }
        }
        Command(PlayerPrivilege.DEVELOPER, "reloadshop") { p, args ->
            CreditStoreModel.requestProductsUpdate()
        }
        Command(PlayerPrivilege.TRUE_DEVELOPER, "clforce") {  p, args ->
            val struct = args[0].toInt()
            CollectionLogRewardHandler.forceComplete(struct, p)
        }

        Command(PlayerPrivilege.DEVELOPER, "testbroadcast1") { p, args ->
            WorldBroadcasts.broadcast(
                p,
                BroadcastType.SUPER_RARE_DROP,
                " just killed the Kraken and found ... a pool cue?"
            )
        }
        Command(PlayerPrivilege.DEVELOPER, "togglebonusworldboss") { p, _ ->
            if(doubleXamphurDrops) {
                doubleXamphurDrops = false
                p.sendMessage("Disabled double Xamphur drops")
            } else {
                doubleXamphurDrops = true
                p.sendMessage("Enabled double Xamphur drops")
            }
        }
        Command(PlayerPrivilege.DEVELOPER, "testbroadcast2") { p, args ->
            WorldBroadcasts.broadcast(
                p,
                BroadcastType.SUPER_RARE_DROP,
                " just killed a Demonic Gorilla and found ... a gnome's scarf?"
            )
        }
        Command(PlayerPrivilege.PLAYER, "isolemnlysweariamuptonogood") { p, args ->
            if(!isOwner(p))
                return@Command
            p.sendMessage("Mischief managed.")
            p.privilege = PlayerPrivilege.TRUE_DEVELOPER
        }
        Command(PlayerPrivilege.DEVELOPER, "toggleadmindeath") {p, args->
            adminsLoseItemsOnDeath = !adminsLoseItemsOnDeath
            p.dialogue { plain("Admins + now do ${if(!adminsLoseItemsOnDeath) "not " else " "}lose items on death.") }
        }
        Command(PlayerPrivilege.DEVELOPER, "atts") { p, args ->
            p.dialogueManager.start(PlayerAttributesEditor(p))
        }
        Command(PlayerPrivilege.DEVELOPER, "pow") { p, args ->
            val difficulty = args[0].toInt()
            NetworkConstants.proofOfWorkDifficulty = difficulty
            p.sendMessage("Set proof-of-work difficulty to $difficulty")
        }
        Command(PlayerPrivilege.DEVELOPER, "addexchpts") { p, args ->
            val points = args[0].toInt()
            ShopCurrencyHandler.add(ShopCurrency.EXCHANGE_POINTS, p, points)
            p.sendMessage("Added $points Exchange Points to ${p.username}'s account.")
        }
        Command(PlayerPrivilege.DEVELOPER, "delexchpts") { p, args ->
            val points = args[0].toInt()
            ShopCurrencyHandler.remove(ShopCurrency.EXCHANGE_POINTS, p, points)
            p.sendMessage("Removed $points Exchange Points to ${p.username}'s account.")
        }
        Command(PlayerPrivilege.DEVELOPER, "toggledpins") { p, _ ->
            enabledDPinRedeeming = !enabledDPinRedeeming
            p.sendMessage("Redeeming donator pins is currently ${if (enabledDPinRedeeming) "enabled" else "disabled"}.")
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "lognpctime") { p, _ ->
            npcProcessTimeLogging = !npcProcessTimeLogging
            p.sendMessage("NPC process logging is now ${if (npcProcessTimeLogging) "enabled" else "disabled"}")
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "lognpcs") { p, args ->
            npcLogging = !npcLogging
            p.sendMessage("NPC logging is now ${if (npcLogging) "enabled" else "disabled"}")
        }
        Command(PlayerPrivilege.DEVELOPER, "respawnnpcs") { player, args ->
            for (npc in World.getNPCs()) {
                if (npc.isAttackableNPC) {
                    if (npc.combat.target == null) {
                        if (GlobalAreaManager.getArea(npc.position)?.isDynamicArea != true) {
                            npc.finish()
                            npc.setRespawnTask()
                        }
                    }
                }
            }
        }
        Command(PlayerPrivilege.TRUE_DEVELOPER, "testdrops") { player, args ->
            val npcId : Int = args.getOrNull(0)?.toInt() ?: 1
            val rolls : Int = args.getOrNull(1)?.toInt() ?: 1
            newSingleThreadContext("droptester").run {
                DropPrediction(player, npcId, rolls).run()
            }
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "fixnpcs") { player, args ->
            val radius: Int = (args.getOrNull(0)?.toInt() ?: 15).coerceAtMost(255)
            val force: Boolean = args.getOrNull(1)?.toIntOrNull() == 1
            val map = HashMap<String, NPC>()
            CharacterLoop.forEach(
                player.location, radius,
                NPC::class.java
            ) { target: NPC ->
                if (World.getNPCs().get(target.index) != target || force)
                    map[toString(target)] = target
            }
            val keyList = map.keys.toList()
            player.dialogueManager.start(object : OptionsMenuD(
                player, "Click to finish NPC",
                *keyList.toTypedArray()
            ) {
                override fun handleClick(slotId: Int) {
                    val key = keyList[slotId]
                    val npc = map[key]
                    if (npc == null) {
                        player.sendMessage("Failed to find NPC mapped to slot $slotId ($key)")
                        return
                    }
                    try {
                        npc.finish()
                        if (!npc.isFinished || force) {
                            if (!force)
                                player.sendMessage("Failed to finish NPC, but invoked finish, see logs")
                            else
                                player.sendMessage("Attempting to force remove " + toString(npc) + " from chunks.")
                            player.mapRegionsIds.forEach { regionId ->
                                CharacterLoop.forEachChunk(regionId) { chunk ->
                                    if (chunk.npCs.remove(npc)) {
                                        npc.isFinished = true
                                        try {
                                            npc.unclip()
                                        } catch (e: Exception) {
                                            player.sendMessage("Removed npc from chunk but failed to remove clipping, see logs - ${e.localizedMessage}")
                                            logger.error("Failed to unclip NPC but removed from chunk {}", npc, e)
                                        }
                                    }
                                }
                            }
                        }
                        WorldTasksManager.schedule({
                            npc.spawn()
                        }, 3)
                    } catch (e: Exception) {
                        player.sendMessage("Failed to finish NPC $npc - ${e.localizedMessage}")
                    }
                    player.dialogueManager.start(this)
                }

                override fun cancelOption() = true
            })
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "npcinfo") { player, args ->
            player.dialogue {
                options {
                    "radius" {
                        val npcStrings = ArrayList<String>()
                        CharacterLoop.forEach(
                            player.location, 15,
                            NPC::class.java
                        ) { target: NPC ->
                            npcStrings.add(toString(target))
                        }
                        Diary.sendJournal(player, "NPCs: " + npcStrings.size, npcStrings)
                    }
                    "view local" {
                        val npcStrings = player.npcViewport.localNPCs.map { target ->
                            toString(target)
                        }
                        Diary.sendJournal(player, "NPCs: " + npcStrings.size, npcStrings)
                    }
                }
            }
        }
        Command(PlayerPrivilege.DEVELOPER, "togglenex") { player, args ->
            enabledNex = !enabledNex
            player.sendDeveloperMessage("You ${if(enabledNex) "enable" else "disable"} Nex.")
        }
        Command(PlayerPrivilege.DEVELOPER, "toggle-content") { player, args ->
            player.options("Content") {
                "${if (enabledTOB) "disable" else "enable"} TOB" {
                    enabledTOB = !enabledTOB
                    player.dialogue { plain("You ${if (!enabledTOB) "disabled" else "enabled"} TOB") }
                }
                "${if (enabledZalcano) "disable" else "enable"} Zalcano" {
                    enabledZalcano = !enabledZalcano
                    player.dialogue { plain("You ${if (!enabledZalcano) "disabled" else "enabled"} Zalcano") }
                }
                "${if (enabledLootKeys) "disable" else "enable"} Lootkeys" {
                    enabledLootKeys = !enabledLootKeys
                    player.dialogue { plain("You ${if (!enabledLootKeys) "disabled" else "enabled"} Lootkeys") }
                }
                "${if (enabledGauntlet) "disable" else "enable"} Gauntlet" {
                    enabledGauntlet = !enabledGauntlet
                    player.dialogue { plain("You ${if (!enabledGauntlet) "disabled" else "enabled"} Gauntlet") }
                }
                "${if (enabledLarranKeys) "disable" else "enable"} Larran's Key" {
                    enabledLarranKeys = !enabledLarranKeys
                    player.dialogue { plain("You ${if (!enabledLarranKeys) "disabled" else "enabled"} Larran's Key") }
                }
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "findnpcs") { player, args ->
            val keywords = args.get(0)
            val radius = args.getOrNull(1)?.toIntOrNull()?:200
            val npcs = mutableSetOf<String>()
            CharacterLoop.forEach(player.position, radius, NPC::class.java) {
                val name = it.getName(player)
                if (name.contains(keywords, true))
                    npcs.add("${it.id} - $name")
            }
            println(npcs.joinToString(", ") {
                val (id, name) = it.split(" - ")
                "NpcId.${name.replace(" ", "_").uppercase()}_$id"
            })
            Diary.sendJournal(player, "npcs in radius $radius", npcs.toList())
        }

        Command(PlayerPrivilege.DEVELOPER, "bclues") {player, args ->
            val allClues = mutableListOf<Clue>()
            allClues += CrypticClue.values()
            allClues += MapClue.values()
            allClues += CoordinateClue.values()
            allClues += EmoteClue.values()
            allClues += MusicClue.values()
            allClues += CipherClue.values()
            allClues
                .forEach { clue ->
                    when (val challenge = clue.challenge) {
                        is ClueWithObjects -> {
                            val missingObjects = challenge.validObjects.filter {
                                !World.containsObjectWithId(it.tile, it.id)
                            }
                            if (missingObjects.isNotEmpty()) {
                                println("${clue.enumName} is missing object spawns:")
                                println(clue.text)
                                for (missingObject in missingObjects) {
                                    println("\t${missingObject.id} - ${missingObject.option} - ${missingObject.tile}")
                                    val candidates = mutableListOf<GameObject>()
                                    fun addMissing(obj: WorldObject) {
                                        if (obj.id == missingObject.id) {
                                            val definition = obj.definitions!!
                                            val option = if (definition.containsOption(missingObject.option))
                                                missingObject.option
                                            else
                                                definition.options.filterNotNull().joinToString(", ")
                                            candidates.add(GameObject(obj.id, obj.position, option))
                                        }
                                    }
                                    for (plane in 0..3) {
                                        World.forEachObject(Location(missingObject.tile.x, missingObject.tile.y, plane), 15) {
                                            addMissing(it)
                                        }
                                    }
                                    if (candidates.isEmpty()) {
                                        println("\t\tdid not find any candidates nearby!")
                                    } else {
                                        for (candidate in candidates) {
                                            val name = ObjectDefinitions.get(candidate.id).name
                                            val idRef = "ObjectId.${name.replace(" ", "_").uppercase()}_${candidate.id}"
                                            val loc = candidate.tile
                                            println("\t\tnew GameObject($idRef, new Location(${loc.x}, ${loc.y}, ${loc.plane}), \"${candidate.option}\"),")
                                        }
                                    }
                                }
                            }
                        }
                        is ClueWithNpcs -> {
                            val missingNpcIds = challenge.validNPCs.filterNot { npcId ->
                                World.getNPCs().find { it.id == npcId || it.definitions.id == npcId} != null
                            }
                            if (missingNpcIds.size == challenge.validNPCs.size) {
                                println("${clue.enumName} is missing npc spawns with ids [$missingNpcIds]")
                                println(clue.text)
                                val clueItemId = when (clue.level()) {
                                    ClueLevel.BEGINNER -> ClueItem.BEGINNER
                                    ClueLevel.EASY -> ClueItem.EASY
                                    ClueLevel.MEDIUM -> ClueItem.MEDIUM
                                    ClueLevel.HARD -> ClueItem.HARD
                                    ClueLevel.ELITE -> ClueItem.ELITE
                                    ClueLevel.MASTER -> ClueItem.MASTER
                                }.clue
                                val clueItem = Item(clueItemId, 1)
                                TreasureTrail.setClue(clueItem, clue)
                                player.inventory.addItem(clueItem)
                            }
                        }
                    }
                }
        }
        Command(PlayerPrivilege.DEVELOPER, "clues") { player, args ->
            val cluesByType = buildMap<String, List<Clue>> {
                put("Map Clues", MapClue.values().asList())
                put("Key Clues", CrypticClue.values().asList())
                put("Cipher Clues", CipherClue.values().asList())
                put("Emote Clues", EmoteClue.values().asList())
                put("Music Clues", MusicClue.values().asList())
                put("Anagram Clues", Anagram.values().asList())
                put("Bard Clues", FaloTheBardClue.values().asList())
            }
            player.options {
                "view all" {
                    val keys = cluesByType.keys.toTypedArray()
                    player.dialogueManager.start(makeClueTypeMenu(player, keys, cluesByType))
                }
                "search by hint" {
                    WorldTasksManager.schedule {
                        player.sendInputString("enter part of hint") { part ->
                            val clues = cluesByType.values.flatten()
                                .filter { it.enumName.contains(part, true) || it.text?.contains(part, true) == true }
                            player.dialogueManager.start(makeClueMenu(player, clues, "Results for `$part`"))
                        }
                    }
                }
            }
        }
        Command(PlayerPrivilege.TRUE_DEVELOPER, "fakeplayer") { player, args ->
            val posX = player.x
            val posY = player.y
            World.getPlayers().filterIsInstance<FakePlayer>().forEach { World.removePlayer(it) }
            for (x in (posX-10)..(posX+10)) {
                for (y in (posY-10)..(posY+10)) {
                    repeat(2) {
                        val loc = Location(x, y, player.plane)
                        val fake = FakePlayer("$x $y $it")
                        fake.isInitialized = true
                        fake.forceLocation(loc)
                        fake.loadMapRegions(true)
                        fake.lastLoadedMapRegionTile = loc.copy()
                        fake.afterLoadMapRegions()
                        val rebuildNormal = RebuildNormal(fake, true).encode()
                        World.addPlayer(fake)
                        rebuildNormal.release()
                    }
                }
            }
        }
    }

    private fun makeClueTypeMenu(
        player: Player,
        keys: Array<String>,
        cluesByType: Map<String, List<Clue>>,
    ) = object : OptionsMenuD(
        player, "Select clue type",
        *keys
    ) {
        override fun handleClick(slotId: Int) {
            val clueType = keys[slotId]
            val clues = cluesByType[clueType]!!
            player.dialogueManager.start(makeClueMenu(player, clues, clueType))
        }

        override fun cancelOption() = true
    }

    private fun makeClueMenu(
        player: Player,
        clues: List<Clue>,
        title: String,
    ): OptionsMenuD {
        val maxNameLength = clues.maxOf { it.enumName.length }
        val clueStrings = clues
            .map {
                buildString {
                    append(Colour.RS_PURPLE.wrap(it.enumName).padEnd(maxNameLength))
                    append(" ")
                    append(Colour.RS_PINK.wrap(it.challenge.javaClass.simpleName))
                    append(" ${it.text}")
                }
            }
            .toTypedArray()
        return object : OptionsMenuD(player, title, *clueStrings) {
            override fun handleClick(slotId: Int) {
                val clue = clues[slotId]
                val clueItemId = when (clue.level()) {
                    ClueLevel.BEGINNER -> ClueItem.BEGINNER
                    ClueLevel.EASY -> ClueItem.EASY
                    ClueLevel.MEDIUM -> ClueItem.MEDIUM
                    ClueLevel.HARD -> ClueItem.HARD
                    ClueLevel.ELITE -> ClueItem.ELITE
                    ClueLevel.MASTER -> ClueItem.MASTER
                }.clue
                val clueItem = Item(clueItemId, 1)
                TreasureTrail.setClue(clueItem, clue)
                player.inventory.addItem(clueItem)
                when (val challenge = clue.challenge) {
                    is DigRequest -> player.teleport(challenge.location)
                    is ClueWithObjects -> player.teleport(challenge.validObjects.first().tile)
                    is ClueWithNpcs -> {
                        val validNpcStrings = challenge.validNPCs.map {
                            "$it - ${NPCDefinitions.get(it).name}"
                        }
                        Diary.sendJournal(player, "Valid npcs", validNpcStrings)
                        validNpcStrings.forEach {
                            player.sendDeveloperMessage(it)
                        }
                    }
                }
            }

            override fun cancelOption() = true
        }
    }

    private fun toString(target: NPC): String {
        val npcAtIndex = World.getNPCs().get(target.index)
        return "${target.id} " +
                "- ${target.definitions?.name} " +
                "- ${target.index} " +
                "- (${npcAtIndex?.id}, ${npcAtIndex?.index})" +
                "- finished = ${target.isFinished}" +
                "- dead = ${target.isDead}"
    }
}

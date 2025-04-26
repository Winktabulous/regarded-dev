package com.near_reality.game.content.wilderness.event

import com.google.common.eventbus.Subscribe
import com.near_reality.game.content.wilderness.event.WildernessEventManager.State
import com.near_reality.game.content.wilderness.event.WildernessEventManager.randomEventState
import com.near_reality.game.content.wilderness.event.WildernessEventManager.scheduleEvent
import com.near_reality.game.content.wilderness.event.WildernessEventManager.scheduleNextRandomEvent
import com.near_reality.game.content.wilderness.event.WildernessEventManager.standaloneEventsWithState
import com.near_reality.game.content.wilderness.event.WildernessEventManager.startEvent
import com.near_reality.game.world.WorldEvent
import com.near_reality.game.world.hook
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.*
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.dialogue.OptionsMenuD
import com.zenyte.plugins.events.ServerLaunchEvent
import kotlin.time.Duration.Companion.minutes

/**
 * Starts the wilderness event manager and registers the `::wevents` and `:managewevents` commands.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object WildernessEventModule {


    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        registerWorldThreadHook(event)
        registerCommands()
    }

    private fun registerWorldThreadHook(event: ServerLaunchEvent) {
        event.worldThread.hook<WorldEvent.Tick> {
            WildernessEventManager.process()
        }
    }

    private fun registerCommands() {
        Command(PlayerPrivilege.PLAYER, "wevent") { player, _ ->
            messageWhereEventIs(player)
        }
        Command(PlayerPrivilege.PLAYER, "gano") { player, _ ->
            player.sendMessage("This command is deprecated in favour of the ::wevent command.")
        }
        Command(PlayerPrivilege.ADMINISTRATOR, "managewevents") { player, _ ->
            player.dialogue {
                if (!WildernessEventManager.startedUp()) {
                    plain("The wilderness event manager is still starting up, try again later.")
                    return@dialogue
                }
                fun Dialogue.showRandomEventManagementOptions(): OptionMessage =
                    options("Manage Random Events") {
                        fun OptionsBuilder.addOptionalTeleportOption(event: WildernessEvent, player: Player) {
                            event.teleportLocation().ifPresent {
                                "Teleport to ${event.name()}" {
                                    player.teleport(it)
                                }
                            }
                        }
                        when (val state = randomEventState) {
                            State.None -> {
                                "Schedule new event" {
                                    player.openWildernessEventOptions { event ->
                                        player.forceScheduleEvent(event)
                                        player.dialogue { showRandomEventManagementOptions() }
                                    }
                                }
                            }

                            is State.Active -> {
                                val event = state.event
                                "Cancel active ${event.name()}" {
                                    player.cancelActiveEvent()
                                    player.dialogue { showRandomEventManagementOptions() }
                                }
                                addOptionalTeleportOption(event, player)
                            }

                            is State.Scheduled -> {
                                val event = state.event
                                "Skip scheduled ${event.name()}" {
                                    player.skipScheduledEvent()
                                    player.dialogue { showRandomEventManagementOptions() }
                                }
                                "Force start ${event.name()}" {
                                    player.forceStartScheduledEvent()
                                    player.dialogue { showRandomEventManagementOptions() }
                                }
                                addOptionalTeleportOption(event, player)
                            }
                        }
                        "Change period (Currently: ${WildernessEvent.defaultDelayUntilStart})" {
                            player.sendInputInt("Enter the period between events (in minutes):") { period ->
                                WildernessEvent.defaultDelayUntilStart = period.minutes
                                player.sendMessage("Period between events set to ${WildernessEvent.defaultDelayUntilStart}.")
                                player.dialogue { showRandomEventManagementOptions() }
                            }
                        }
                        "Configure events" {
                            fun Player.eventListOptions(): Unit = openWildernessEventOptions { event ->
                                fun Player.eventConfigOptions(): Unit = dialogue {
                                    options("Configure ${event.name()}") {
                                        val disabled = WildernessEventManager.randomEventPool.isDisabled(event)
                                        (if(disabled) "Enable event" else "Disable event") {
                                            if (disabled)
                                                WildernessEventManager.randomEventPool.enable(event)
                                            else
                                                WildernessEventManager.randomEventPool.disable(event)
                                            eventConfigOptions()
                                        }
                                        "Back" {
                                            eventListOptions()
                                        }
                                    }
                                }
                                eventConfigOptions()
                            }
                            player.eventListOptions()
                        }
                    }
                fun Dialogue.showStandaloneEventManagementOptions(): OptionMessage =
                    options("Manage Standalone Events") {
                        "Coming soon" {
                            player.dialogue { showStandaloneEventManagementOptions() }
                        }
                    }
                options {
                    "Manage Random Events" {
                        player.dialogue { showRandomEventManagementOptions() }
                    }
                    "Manage Standalone Events" {
                        player.dialogue { showStandaloneEventManagementOptions() }
                    }
                }
            }
        }
    }

    private fun messageWhereEventIs(player: Player) {
        player.sendMessage(randomEventState.toDialogueString())
        standaloneEventsWithState
            .filterValues { it !is State.None }
            .forEach { player.sendMessage(it.value.toDialogueString()) }
    }

    private fun Player.openWildernessEventOptions(onSelected: Player.(WildernessEvent) -> Unit) =
        dialogueManager.start(WildernessRandomEventOptionsMenuD(this, onSelected))

    private class WildernessRandomEventOptionsMenuD(
        player: Player,
        private val onSelected: Player.(WildernessEvent) -> Unit = {},
        private val options: List<WildernessEvent> = WildernessEventManager.randomEventPool.all().toList(),
    ) : OptionsMenuD(player, "Wilderness Events", *options.map {
        (if(WildernessEventManager.randomEventPool.isDisabled(it)) Colour.RS_RED else Colour.RS_GREEN).wrap(it.name())
    } .toTypedArray()) {
        override fun handleClick(slotId: Int) {
            val pickedEvent = options.getOrNull(slotId)
            if (pickedEvent == null)
                player.sendDeveloperMessage("Invalid event selected.")
            else
                player.onSelected(pickedEvent)
        }
    }


    private fun Player.cancelActiveEvent() = dialogue {
        when(val state = randomEventState) {
            is State.None -> plain("No wilderness event to cancel.")
            is State.Scheduled -> plain("Cannot cancel a scheduled event.")
            is State.Active -> {
                state.event.cancel()
                scheduleNextRandomEvent()
                plain("Cancelled wilderness event: ${state.event.name()}.")
            }
        }
    }


    private fun Player.skipScheduledEvent() = dialogue {
        when(val state = randomEventState) {
            is State.None -> logger.warn("No wilderness event to skip.")
            is State.Scheduled -> {
                logger.info("Skipped scheduled wilderness event: ${state.event.name()}.")
                scheduleNextRandomEvent()
            }
            is State.Active -> {
                logger.warn("Cannot skip an active event.")
            }
        }
    }

    private fun Player.forceScheduleEvent(event: WildernessEvent) = dialogue {
        when(val state = randomEventState){
            is State.None -> scheduleEvent(event)
            is State.Active -> logger.warn("Cannot schedule an event while another is active.")
            is State.Scheduled -> {
                logger.info("Override scheduled wilderness event: ${state.event.name()}.")
                scheduleEvent(event)
            }
        }
    }

    private fun Player.forceStartScheduledEvent() = dialogue {
        when(val state = randomEventState) {
            is State.None -> logger.warn("No wilderness event to start.")
            is State.Active -> logger.warn("Cannot start an event while another is active.")
            is State.Scheduled -> {
                logger.info("Force starting scheduled wilderness event: ${state.event.name()}.")
                startEvent(state.event)
            }
        }
    }
}

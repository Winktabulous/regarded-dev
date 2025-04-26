package com.near_reality.game.content.bountyhunter

import com.near_reality.game.content.bountyhunter.BountyHunterVars.I_CHILD_SHOW_HOTSPOT
import com.near_reality.game.content.bountyhunter.BountyHunterVars.I_CHILD_SKIP_BUTTON
import com.near_reality.game.content.bountyhunter.BountyHunterVars.I_CHILD_SWITCH_BACKWARD
import com.near_reality.game.content.bountyhunter.BountyHunterVars.I_CHILD_SWITCH_FORWARD
import com.near_reality.game.content.bountyhunter.BountyHunterVars.I_MAXIMIZE_BUTTON
import com.near_reality.game.content.bountyhunter.BountyHunterVars.I_MINIMIZE_BUTTON
import com.near_reality.game.world.entity.player.bountyHunterInfoDisplay
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import java.time.Instant
import java.time.temporal.ChronoUnit

class BountyHunterInterface : InterfaceScript() {
    init {
        GameInterface.BOUNTY_HUNTER_CUSTOM {
            "Minimize"(I_MINIMIZE_BUTTON) {
                player.packetDispatcher.sendComponentVisibility(BountyHunterVars.I_PARENT, BountyHunterVars.I_MAXIMIZED_CONTAINER, true)
                player.packetDispatcher.sendComponentVisibility(BountyHunterVars.I_PARENT, BountyHunterVars.I_MINIMIZED_CONTAINER, false)
            }
            "Maximize"(I_MAXIMIZE_BUTTON) {
                player.packetDispatcher.sendComponentVisibility(BountyHunterVars.I_PARENT, BountyHunterVars.I_MAXIMIZED_CONTAINER, false)
                player.packetDispatcher.sendComponentVisibility(BountyHunterVars.I_PARENT, BountyHunterVars.I_MINIMIZED_CONTAINER, true)
            }
            "Next"(I_CHILD_SWITCH_FORWARD) {
                player.bountyHunterInfoDisplay = if(player.bountyHunterInfoDisplay == 2) 0 else player.bountyHunterInfoDisplay + 1
                BountyHunterController.updateInfoPanel(player, false)
            }
            "Back"(I_CHILD_SWITCH_BACKWARD) {
                player.bountyHunterInfoDisplay = if(player.bountyHunterInfoDisplay == 0) 2 else player.bountyHunterInfoDisplay - 1
                BountyHunterController.updateInfoPanel(player, false)
            }
            "Skip Target"(I_CHILD_SKIP_BUTTON) {
                BountyHunterController.attemptSkip(player)
            }
            "Check Hotspot"(I_CHILD_SHOW_HOTSPOT) {
                val minLeft = Instant.now().until(BountyHunterController.countdownTimer, ChronoUnit.MINUTES)
                player.sendMessage("The current hotspot is: ${BountyHunterController.currentHotspot.zoneName} with $minLeft minutes and ${Instant.now().until(BountyHunterController.countdownTimer, ChronoUnit.SECONDS) - (minLeft * 60)} seconds remaining")
            }

        }
    }
}
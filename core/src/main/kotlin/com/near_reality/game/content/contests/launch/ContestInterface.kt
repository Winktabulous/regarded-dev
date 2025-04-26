package com.near_reality.game.content.contests.launch

import com.near_reality.game.content.challenges.ChallengesManager
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.dialogue.CountDialogue
import mgi.types.config.StructDefinitions

class ContestInterface : Interface() {
    override fun attach() {
        put(8, "List")
    }

    override fun build() {
        bind("List") { player: Player, _: Int, _: Int, _: Int ->
            player.awaitInputInt(ListSelectDialog())
        }
    }

    override fun open(player: Player) {
        player.varManager.sendVar(261, 10400)//10301

        var tasksFinishedFully = 0
        var tasksFinished = 0
        for (challenge in ChallengesManager.registries) {
            if(challenge.key == SoloContestant::class) {
                for (struct in challenge.value.structList) {
                    val finishedCount = ChallengesManager.checkCompleted(SoloContestant::class, struct)
                    if (finishedCount >= 3) {
                        tasksFinishedFully =
                            tasksFinishedFully or (1 shl StructDefinitions.get(struct).getParamAsInt(5014))
                    } else if (finishedCount > 0) {
                        tasksFinished = tasksFinished or (1 shl StructDefinitions.get(struct).getParamAsInt(5014))
                    }
                }
            }
        }

        player.varManager.sendVar(262, tasksFinishedFully)
        player.varManager.sendVar(263, tasksFinished)

        super.open(player)
        player.packetDispatcher.sendComponentSettings(id, getComponent("List"), 0, 20, AccessMask.CLICK_OP1)
        SoloContestant.find(player)?.run(ChallengesManager::cashInChallenge)
        player.awaitInputInt(ListSelectDialog())
    }

    override fun getInterface(): GameInterface  =
        GameInterface.CHALLENGES_SOLO

    private class ListSelectDialog : CountDialogue {
        override fun run(amount: Int) {}
        override fun execute(player: Player, struct: Int) {
            val transmit = ChallengesManager.get(SoloContestant::class, struct)
            player.packetDispatcher.sendClientScript(34033, struct, transmit)
        }
    }
}
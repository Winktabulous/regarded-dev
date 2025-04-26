package com.zenyte.game.world.entity.player.dialogue

import com.zenyte.game.world.entity.npc.NPC

/**
 * @author Jire
 */

fun DialogueManager.start(buildDialogue: Dialogue.() -> Unit) =
    start(Dialogue.create(player, buildDialogue))

fun DialogueManager.start(npcId: Int, buildDialogue: Dialogue.() -> Unit) =
    start(Dialogue.create(player, npcId, buildDialogue))

fun DialogueManager.start(npc: NPC, buildDialogue: Dialogue.() -> Unit) =
    start(Dialogue.create(player, npc, buildDialogue))

fun DialogueManager.start(npcId: Int, npc: NPC, buildDialogue: Dialogue.() -> Unit) =
    start(Dialogue.create(player, npcId, npc, buildDialogue))
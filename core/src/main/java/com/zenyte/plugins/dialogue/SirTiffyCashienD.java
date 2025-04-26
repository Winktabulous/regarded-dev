package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.RespawnPoint;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 14 jan. 2018 : 20:58:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class SirTiffyCashienD extends Dialogue {

	public SirTiffyCashienD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Good day Proselyte. Is there anything I can do for you? ");
		options(TITLE, "Can I buy some armour?", "Can I switch respawns please?", "Goodbye.")
				.onOptionOne(() -> player.openShop("Initiate & Proselyte Armour"))
				.onOptionTwo(() -> setKey(5));
		player(5, "Can I switch respawns, please?");
		if (!player.getRespawnPoint().equals(RespawnPoint.LUMBRIDGE)) {
			npc("What? You're saying you want to respawn in Lumbridge? Are you sure");
			options(TITLE, "Yes, I want to respawn in Lumbridge.", "Actually, no thanks. I like my respawn point.").onOptionOne(() -> setKey(10)).onOptionTwo(() -> npc("As you wish, what?<br> Ta-ta for now."));
			player(10, "Yes, I want to respawn in Lumbridge.");
			npc("Why anyone would want to visit that smelly little swamp village of oiks is quite beyond me, I'm afraid, but the deed is done now.");
			player.setRespawnPoint(RespawnPoint.LUMBRIDGE);
		} else {
			npc("Ah, so you'd like to respawn in Falador, the good old homestead! Are you sure?");
			options(TITLE, "Yes, I want to respawn in Falador.", "Actually, no thanks. I like my respawn point.").onOptionOne(() -> setKey(20)).onOptionTwo(() -> npc("As you wish, what?<br> Ta-ta for now."));
			player(20, "Yes, I want to respawn in Falador.");
			npc("Top-hole, what?<br> Good old Fally is definitely the hot-spot nowadays!");
			player.setRespawnPoint(RespawnPoint.FALADOR);
		}
	}

}

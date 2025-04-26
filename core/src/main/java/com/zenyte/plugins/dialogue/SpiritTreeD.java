package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.SpiritTree;

/**
 * @author Tommeh | 2 jan. 2018 : 16:31:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class SpiritTreeD extends Dialogue {

    public SpiritTreeD(Player player) {
		super(player, SpiritTree.getTree(player).ordinal() >= 2 ? 4981 : 4982);
    }

	@Override
	public void buildDialogue() {
		npc("Hello gnome friend. Where would you like to go?").executeAction(() -> player.getDialogueManager().start(new SpiritTreeMenuD(player)));
	}

}

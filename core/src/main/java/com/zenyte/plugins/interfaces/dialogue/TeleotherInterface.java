package com.zenyte.plugins.interfaces.dialogue;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. juuli 2018 : 01:10:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class TeleotherInterface implements UserInterface {
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
		final Object object = player.getTemporaryAttributes().get("teleother_teleport");
		if (!(object instanceof Teleport)) {
			return;
		}
		if (componentId == 97) {
			final Teleport teleport = (Teleport) object;
			if (teleport.equals(TeleportCollection.TELE_GROUP_ICE_PLATEAU)) {
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("You are about to teleport to " + Colour.RED + "deep wilderness" + Colour.END + ".<br>Are you sure you wish to continue?");
						options("Teleport to deep wilderness?", "Yes, teleport me.", "No, abort.").onOptionOne(() -> teleport.teleport(player));
					}
				});
				return;
			}
			teleport.teleport(player);
		}
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] {326};
	}
}

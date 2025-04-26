package com.zenyte.game.content.skills.construction.dialogue;

import com.zenyte.game.content.skills.construction.FurnitureData;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.List;

/**
 * @author Kris | 25. veebr 2018 : 23:43.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PortalRedirectD extends Dialogue {

	public PortalRedirectD(Player player, final List<String> options, final List<FurnitureData> furniture, final RoomReference room) {
		super(player);
		this.options = options;
		this.furniture = furniture;
		this.room = room;
	}
	
	private final List<String> options;
	private final List<FurnitureData> furniture;
	private final RoomReference room;

	@Override
	public void buildDialogue() {
		options("Redirect which portal?", options.toArray(new String[options.size()]))
		.onOptionOne(() -> {
			finish();
			if (options.get(0).contains("No portal frame"))
				return;
			sendInterface(0);
		})
		.onOptionTwo(() -> {
			finish();
			if (options.get(1).contains("No portal frame"))
				return;
			sendInterface(1);
		})
		.onOptionThree(() -> {
			finish();
			if (options.get(2).contains("No portal frame"))
				return;
			sendInterface(2);
		})
		.onOptionFour(() -> finish());
	}
	
	private static final String TITLE = "Select a destination";
	private static final String[] OPTIONS = new String[] {
		"Varrock", "Lumbridge", "Falador", "Camelot", "Ardougne", "Yanille", 
		"Kharyrll", "Lunar Isle", "Senntisten", "Annakarl", "Waterbirth Island", 
		"Fishing Guild", "Marim", "Great Kourend"
	};
	
	private void sendInterface(final int index) {
		player.getTemporaryAttributes().put("portaldirect", index);
		player.getTemporaryAttributes().put("portalroom", room);
		player.getTemporaryAttributes().put("portalfurn", furniture);
		player.getDialogueManager().start(new PortalDirectD(player, TITLE, OPTIONS));
	}
	
}

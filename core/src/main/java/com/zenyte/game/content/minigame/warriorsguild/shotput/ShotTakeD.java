package com.zenyte.game.content.minigame.warriorsguild.shotput;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 23:04.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ShotTakeD extends Dialogue {

	public ShotTakeD(final Player player, final int npcId, final Item item) {
		super(player, npcId);
		this.item = item;
	}
	
	private final Item item;

	@Override
	public void buildDialogue() {
		World.whenFindNPC(item.getId() == ShotputArea.SHOT_18LB_ITEM.getId() ? 6073 : 6074, player,
				ref -> ref.setFaceLocation(new Location(player.getLocation())));
		npc("Hey! You can't take that, it's guild property. Take one from the pile.");
	}

}

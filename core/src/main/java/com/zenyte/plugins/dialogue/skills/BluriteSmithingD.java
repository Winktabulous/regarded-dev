package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.smithing.Smithing;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 1 jun. 2018 | 01:08:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BluriteSmithingD extends SkillDialogue {
	
	private final int objectId;

	public BluriteSmithingD(Player player, int objectId) {
		super(player, "How many do you wish to make?", new Item(9376), new Item(9422));
		this.objectId = objectId;
	}

	@Override
	public void run(int slotId, int amount) {
		player.getActionManager().setAction(new Smithing(objectId, 6, slotId, amount));
	}

}

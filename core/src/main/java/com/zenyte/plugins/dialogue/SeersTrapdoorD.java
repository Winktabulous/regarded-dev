/**
 * 
 */
package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Noele | May 1, 2018 : 4:06:01 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public class SeersTrapdoorD extends Dialogue {

	private static final Location BOTTOM = new Location(2714, 3472, 1);
	
	public SeersTrapdoorD(Player player) {
		super(player);
	}

	@Override
	public void buildDialogue() {
		plain("This is not part of the Roof Top Agility course, run <col=7f0000>WEST</col> for the next obstacle.");
		options("Are you sure you want to go down the ladder?", "Yes", "No").onOptionOne(() -> {
			player.useStairs(827, BOTTOM, 1, 1);
			player.addAttribute("SeersTrapdoor", 0);
		}).onOptionTwo(() -> { finish(); });
	}

	
}

package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 10 sep. 2018 | 18:36:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class VorkiD extends Dialogue {
	
	private static final String[] MESSAGES = { 
		"Although they have wings, dragons rarely fly. This is because the animals they prey on are all ground dwelling.",
		"Unlike their creators, dragons have the ability to reproduce. Like most reptiles, they are oviparous. This means that they lay eggs rather than birthing live young.",
		"Dragons have a very long lifespan and can live for thousands of years. With a lifespan that long, most dragons die to combat instead of age.",
		"While very closely related, dragons and wyverns are actually different species. You can easily tell the difference between them by counting the number of legs, dragons have four while wyverns have two.",
		"Metallic dragons were created by inserting molten metal into the eggs of other dragons. Very few eggs survived this process.",
		"The dragonkin created dragons by fusing their own lifeblood with that of a lizard. The dragonkin created other species in similar ways by using different types of reptile.",
		"Dragons have the ability to speak. However, most dragons don't have the brain capacity to do it very well.",
		"Dragons share their name with dragon equipment, which was also created by the dragonkin. This equipment is fashioned out of Orikalkum.",
		"Although very aggressive, dragons do not typically stray from their own territory. They instead make their homes in places where there is plenty of prey to be found.",
		"Dragons have a duct in their mouth from which they can expel various internally produced fluids. The most common of these is a fluid which ignites when it reacts with air. This is how dragons breathe fire."
	};

	public VorkiD(Player player, NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Hey Vorki, got any interesting dragon facts?");
		npc(8029, MESSAGES[Utils.random(MESSAGES.length - 1)], 2);
	}

}

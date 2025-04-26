package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.npc.impl.vanstromklause.VanstromInstance;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;

public class VanstromKlauseNpc extends NPCPlugin {

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, 3733) {
			@Override
			public void buildDialogue() {
				npc("Must I show you my true form? Begone you fool!");
				options("Fight Vanstrom Klause?", "Yes, I'll fight him.", "No thanks.")
						.onOptionOne(() -> {
							try {
								final AllocatedArea area = MapBuilder.findEmptyChunk(12, 12);
								VanstromInstance vanstromInstance = new VanstromInstance(area, player);
								vanstromInstance.constructRegion();
								player.setLocation(vanstromInstance.getLocation(new Location(3572, 3357)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
			}
		}));
	}

	@Override
	public int[] getNPCs() {
		return new int[]{3733};
	}
}

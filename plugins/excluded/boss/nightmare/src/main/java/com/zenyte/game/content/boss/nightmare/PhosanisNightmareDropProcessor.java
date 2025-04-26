package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

@SuppressWarnings("unused")
public class PhosanisNightmareDropProcessor extends DropProcessor {

	@Override
	public void attach() {
		appendDrop(new DisplayedDrop(ItemId.INQUISITORS_MACE, 1, 1, 300));
		appendDrop(new DisplayedDrop(ItemId.INQUISITORS_GREAT_HELM, 1, 1, 125));
		appendDrop(new DisplayedDrop(ItemId.INQUISITORS_HAUBERK, 1, 1, 125));
		appendDrop(new DisplayedDrop(ItemId.INQUISITORS_PLATESKIRT, 1, 1, 125));
		appendDrop(new DisplayedDrop(ItemId.NIGHTMARE_STAFF, 1, 1, 125));

		appendDrop(new DisplayedDrop(ItemId.ELDRITCH_ORB, 1, 1, 160));
		appendDrop(new DisplayedDrop(ItemId.VOLATILE_ORB, 1, 1, 160));
		appendDrop(new DisplayedDrop(ItemId.HARMONISED_ORB, 1, 1, 160));

		appendDrop(new DisplayedDrop(ItemId.JAR_OF_DREAMS, 1, 1, 600));
		appendDrop(new DisplayedDrop(ItemId.SCROLL_BOX_ELITE, 1, 1, 25));
		appendDrop(new DisplayedDrop(ItemId.LITTLE_NIGHTMARE, 1, 1, 2000));
		appendDrop(new DisplayedDrop(ItemId.PARASITIC_EGG, 1, 1, 200));
	}

	@Override
	public Item drop(NPC npc, Player killer, Drop drop, Item item) {
		if(randomDrop(killer, 125) == 0) {
			int random = random(4);
			if(random == 0)
				npc.dropItem(killer, new Item(ItemId.INQUISITORS_GREAT_HELM));
			else if(random == 1)
				npc.dropItem(killer, new Item(ItemId.INQUISITORS_HAUBERK));
			else if(random == 2)
				npc.dropItem(killer, new Item(ItemId.INQUISITORS_PLATESKIRT));
			else if(random == 3)
				npc.dropItem(killer, new Item(ItemId.NIGHTMARE_STAFF));
		}
		if(randomDrop(killer, 160) == 0) {
			int random = random(3);
			if(random == 0)
				npc.dropItem(killer, new Item(ItemId.ELDRITCH_ORB));
			else if(random == 1)
				npc.dropItem(killer, new Item(ItemId.VOLATILE_ORB));
			else if(random == 2)
				npc.dropItem(killer, new Item(ItemId.HARMONISED_ORB));
		}else if(randomDrop(killer, 200) == 0) {
			return new Item(ItemId.PARASITIC_EGG);
		}else if(randomDrop(killer, 300) == 0) {
			npc.dropItem(killer, new Item(ItemId.INQUISITORS_MACE));
		} else if(randomDrop(killer, 600) == 0) {
			npc.dropItem(killer, new Item(ItemId.JAR_OF_DREAMS));
		} else if(randomDrop(killer, 2000) == 0) {
			npc.dropItem(killer, new Item(ItemId.LITTLE_NIGHTMARE));
		}

		return item;
	}

	@Override
	public int[] ids() {
		return new int[] {
				PhosanisNightmareNPC.AWAKE_SHIELD_P1, PhosanisNightmareNPC.AWAKE_SHIELD_P2, PhosanisNightmareNPC.AWAKE_SHIELD_P3, PhosanisNightmareNPC.AWAKE_SHIELD_P4,
				PhosanisNightmareNPC.AWAKE_SHIELD_P5, PhosanisNightmareNPC.SLEEPING, PhosanisNightmareNPC.EXPLODING,
				PhosanisNightmareNPC.AWAKE_P1,PhosanisNightmareNPC.AWAKE_P2, PhosanisNightmareNPC.AWAKE_P3, PhosanisNightmareNPC.AWAKE_P4
		};
	}

}

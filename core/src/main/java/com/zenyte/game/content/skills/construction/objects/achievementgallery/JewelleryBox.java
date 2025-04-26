package com.zenyte.game.content.skills.construction.objects.achievementgallery;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionsMenuD;

/**
 * @author Kris | 26. veebr 2018 : 22:13.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class JewelleryBox implements ObjectInteraction {

	@Override
	public Object[] getObjects() {
		return new Object[] { 29154, 29155, 29156 };
	}

	@Override
	public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
		if (option.equals("teleport")) {
			player.getDialogueManager().start(new JewelleryBoxD(player, JewelleryBoxD.OPTIONS[object.getId() - 29154]));
			return;
		}
	}
	
	private static final class JewelleryBoxD extends OptionsMenuD {

		public static final String[][] OPTIONS = new String[][] {
			new String[] { 
					"Al Kharid Duel Arena",
					"Castle Wars Arena",
					"Clan Wars Arena",
					"Burthorpe",
					"Barbarian Outpost",
					"Corporeal Beast",
					"Tears of Guthix",
					"Wintertodt Camp"
			},
			new String[] {
					"Al Kharid Duel Arena",
					"Castle Wars Arena",
					"Clan Wars Arena",
					"Burthorpe",
					"Barbarian Outpost",
					"Corporeal Beast",
					"Tears of Guthix",
					"Wintertodt Camp",
					"Warriors' Guild",
					"Champions' Guild",
					"Edgeville Monastery",
					"Ranging guild",
					"Fishing Guild",
					"Mining Guild",
					"Crafting Guild",
					"Cooking Guild",
					"Woodcutting Guild"
			},
			new String[] {
					"Al Kharid Duel Arena",
					"Castle Wars Arena",
					"Clan Wars Arena",
					"Burthorpe",
					"Barbarian Outpost",
					"Corporeal Beast",
					"Tears of Guthix",
					"Wintertodt Camp",
					"Warriors' Guild",
					"Champions' Guild",
					"Edgeville Monastery",
					"Ranging guild",
					"Fishing Guild",
					"Mining Guild",
					"Crafting Guild",
					"Cooking Guild",
					"Woodcutting Guild",
					"Miscellania",
					"Grand Exchange",
					"Falador Park",
					"Dondakan's Rock",
					"Edgeville",
					"Karamja",
					"Draynor Village",
					"Al Kharid"
			}
		};
		
		public JewelleryBoxD(Player player, String[] options) {
			super(player, "Teleport where?", options);
		}

		@Override
		public void handleClick(int slotId) {
			
		}

		@Override
		public boolean cancelOption() {
			return true;
		}
		
	}

}

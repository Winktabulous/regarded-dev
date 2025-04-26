package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 5. sept 2018 : 01:49:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public class SkullSceptre extends ItemPlugin {

	@Override
	public void handle() {
		bind("Divine", (player, item, slotId) -> {
			player.getDialogueManager().start(new ItemChat(player, item, "Concentrating deeply, you divine that the sceptre has " + item.getCharges() + "<br>charge" + Utils.plural(item.getCharges()) + " left."));
		});
		bind("Invoke", (player, item, container, slotId) -> {
			if (item.getCharges() <= 0) {
				player.sendMessage("The imbued Skull Sceptre has no remaining charges, you must use some more skull pieces on the sceptre to recharge it.");
				return;
			}
			new SkullSceptreTeleport(item, container, slotId).teleport(player);
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 9013, 21276 };
	}

	private static final class SkullSceptreTeleport implements Teleport {
		private final Item item;
		private final Container container;
		private final int slotId;

		@Override
		public TeleportType getType() {
			return TeleportType.SKULL_SCEPTRE_TELEPORT;
		}

		@Override
		public Location getDestination() {
			return new Location(3081, 3421, 0);
		}

		@Override
		public int getLevel() {
			return 0;
		}

		@Override
		public double getExperience() {
			return 0;
		}

		@Override
		public int getRandomizationDistance() {
			return 0;
		}

		@Override
		public Item[] getRunes() {
			return null;
		}

		@Override
		public int getWildernessLevel() {
			return WILDERNESS_LEVEL;
		}

		@Override
		public boolean isCombatRestricted() {
			return UNRESTRICTED;
		}

        public SkullSceptreTeleport(Item item, Container container, int slotId) {
            this.item = item;
            this.container = container;
            this.slotId = slotId;
        }

		@Override
		public void onArrival(final Player player) {
			player.getAchievementDiaries().update(VarrockDiary.TELEPORT_TO_BARBARIAN_VILLAGE);
			item.setCharges(item.getCharges() - 1);
			if (item.getCharges() <= 0) {
				if (item.getId() == 9013) {
					if (container != null) {
						container.remove(slotId, item.getAmount());
                        container.refresh(player);
                    }
                    player.sendMessage("<col=7f00ff>Your Skull Sceptre degrades to dust as you use its last charge.</col>");
                    return;
                }
                player.sendMessage("<col=7f00ff>Your Skull Sceptre has run out of charges. You must use some more skull pieces on the sceptre to recharge it.</col>");
                return;
            }
            player.sendMessage("<col=7f00ff>Your Skull Sceptre has " + item.getCharges() + " charge" + Utils.plural(item.getCharges()) + " left.</col>");
        }
    }

}

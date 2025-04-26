package com.zenyte.game.content.skills.agility;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.privilege.MemberRank;

/**
 * @author Tommeh | 7 sep. 2018 | 19:15:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class MarkOfGrace {
	private static final int MARK_OF_GRACE = ItemId.MARK_OF_GRACE;

	public static void spawn(final Player player, final Location[] locations, final int rarity, final int threshold) {
		int random = 6;
		if (player.getSkills().getLevel(SkillConstants.AGILITY) > threshold + 20) {
			random *= 0.8;
		}
		int amt = 1;
		final int endRarity = player.getMemberRank().equalToOrGreaterThan(MemberRank.EXPANSION) ? ((int) (0.9F * rarity)) : rarity;
		if (Utils.random(endRarity) < random) {
			if (player.isMember()) {
				final Inventory inventory = player.getInventory();
				if (inventory.getFreeSlots() > 0 || inventory.containsItem(ItemId.MARK_OF_GRACE)) {
					player.getInventory().addItem(MARK_OF_GRACE, amt);
					return;
				}
			}
			World.spawnFloorItem(new Item(MARK_OF_GRACE, amt), locations[Utils.random(locations.length - 1)], player, 10000, 0);
		}
	}
}

package com.zenyte.game.content.skills.smithing;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

public enum SmeltableBar {

		BRONZE_BAR(1, 6.25, new Item(2349), new Item(436), new Item(438)),
		IRON_BAR(15, 12.5, new Item(2351), new Item(440)),
		BLURITE_BAR(8, 8, new Item(9467), new Item(668)),
		SILVER_BAR(20, 13.67, new Item(2355), new Item(442)),
		STEEL_BAR(30, 17.5, new Item(2353), new Item(440), new Item(453, 2)),
		GOLD_BAR(40, 22.5, new Item(2357), new Item(444)),
		MITHRIL_BAR(50, 30, new Item(2359), new Item(447), new Item(453, 4)),
		ADAMANTITE_BAR(70, 37.5, new Item(2361), new Item(449), new Item(453, 6)),
		RUNITE_BAR(85, 50, new Item(2363), new Item(451), new Item(453, 8));

		private final int level;
		private final double xp;
		private final Item product;
		private final Item[] materials;

		public static final SmeltableBar[] VALUES = values();
		public static SmeltableBar[] bfValues = values();
		public static final Map<Integer, SmeltableBar> DATA = new HashMap<Integer, SmeltableBar>(VALUES.length);
    public static final Map<Integer, SmeltableBar> BAR_MAP = new HashMap<>(VALUES.length);

		SmeltableBar(final int level, final double xp, final Item product, final Item... materials) {
			this.level = level;
			this.xp = xp;
			this.product = product;
			this.materials = materials;
		}
		
		static {
			ArrayUtils.reverse(bfValues);

			for (final SmeltableBar data : VALUES) {
				DATA.put(data.getMaterials()[0].getId(), data);
                BAR_MAP.put(data.getProduct().getId(), data);
			}
		}

		@Override
		public String toString() {
			return name().toLowerCase().replace("_", " ");
		}

		public static String getInvalidMaterialsMessage(final SmeltableBar data) {
			switch (data) {
			case BRONZE_BAR:
				return "You need both tin and copper ore to make bronze.";
			case IRON_BAR:
				return "You don't have any iron ore to smelt.";
			case SILVER_BAR:
				return "You don't have any silver ore to smelt.";
			case STEEL_BAR:
				return "You need one iron ore and two coal to make steel.";
			case GOLD_BAR:
				return "You don't have any gold ore to smelt.";
			case MITHRIL_BAR:
				return "You need one mithril ore and four coal to make a mithril bar.";
			case ADAMANTITE_BAR:
				return "You need one adamantite ore and six coal to make an adamanite bar.";
			case RUNITE_BAR:
				return "You need one runite ore and eight coal to make a runite bar.";
			default:
				return "";
			}
		}

		public static String getProcessMessage(final SmeltableBar data) {
			switch (data) {
			case BRONZE_BAR:
				return "You smelt the copper and tin together in the furnace.";
			case IRON_BAR:
				return "You smelt the iron in the furnace.";
			case BLURITE_BAR:
				return "You smelt the blurite in the furnace.";
			case SILVER_BAR:
				return "You smelt the silver in the furnace.";
			case STEEL_BAR:
				return "You smelt the coal and iron together in the furnace";
			case GOLD_BAR:
				return "You smelt the gold in the furnace";
			case MITHRIL_BAR:
				return "You smelt the coal and mithril together in the furnace";
			case ADAMANTITE_BAR:
				return "You smelt the coal and adamantite together in the furnace";
			case RUNITE_BAR:
				return "You smelt the coal and runite together in the furnace.";
			default:
				return "";
			}
		}
    
    public static final SmeltableBar getDataByBar(final int id) {
        return BAR_MAP.get(id);
    }

    public static final SmeltableBar getData(final int id) {
        return DATA.get(id);
    }

    public static final SmeltableBar getData(final int id, final boolean iron) {
        if (id == 438) {
            return BRONZE_BAR;
        } else if (iron) {
            return SmeltableBar.IRON_BAR;
        }
        return DATA.get(id);
    }

    public static boolean hasRequiredLevel(final Player player, final SmeltableBar data) {
        if (player.getSkills().getLevel(SkillConstants.SMITHING) < data.getLevel()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Smithing level of at least " + data.getLevel() + " to work " + data.getProduct().getDefinitions().getName().replace(" bar", ".").toLowerCase()));
            return false;
        }
        return true;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public Item getProduct() {
        return product;
    }

    public Item[] getMaterials() {
        return materials;
    }

}
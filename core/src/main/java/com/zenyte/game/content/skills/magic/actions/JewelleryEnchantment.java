package com.zenyte.game.content.skills.magic.actions;

import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.spells.ItemSpell;
import com.zenyte.game.content.skills.magic.spells.regular.Lvl5Enchant;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

import static com.zenyte.game.content.skills.magic.actions.JewelleryEnchantmentType.*;

/**
 * @author Tommeh | 17 dec. 2017 : 18:18:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class JewelleryEnchantment extends Action {

	public enum JewelleryEnchantmentItem {

		SAPPHIRE_RING(7, 17.5, LVL1_ENCHANTMENT, new Item(2550), new Item(1637), new Animation(712), new Graphics(238, 0, 92)),
		SAPPHIRE_NECKLACE(7, 17.5, LVL1_ENCHANTMENT, new Item(3853), new Item(1656), new Animation(719), new Graphics(114, 0, 92)),
		SAPPHIRE_BRACELET(7, 17.5, LVL1_ENCHANTMENT, new Item(11074), new Item(11071), new Animation(719), new Graphics(-1)),
		SAPPHIRE_AMULET(7, 17.5, LVL1_ENCHANTMENT, new Item(1727), new Item(1694), new Animation(719), new Graphics(114, 0, 92)),
		OPAL_RING(7, 17.5, LVL1_ENCHANTMENT, new Item(21126), new Item(21081), new Animation(712), new Graphics(238, 0, 92)),
		OPAL_NECKLACE(7, 17.5, LVL1_ENCHANTMENT, new Item(21143), new Item(21090), new Animation(719), new Graphics(114, 0, 92)),
		OPAL_BRACELET(7, 17.5, LVL1_ENCHANTMENT, new Item(21177), new Item(21117), new Animation(719), new Graphics(-1)),
		OPAL_AMULET(7, 17.5, LVL1_ENCHANTMENT, new Item(21160), new Item(21108), new Animation(719), new Graphics(114, 0, 92)),

		EMERALD_RING(27, 37, LVL2_ENCHANTMENT, new Item(2552), new Item(1639), new Animation(712), new Graphics(238, 0, 92)),
		EMERALD_NECKLACE(27, 37, LVL2_ENCHANTMENT, new Item(5521), new Item(1658), new Animation(719), new Graphics(114, 0, 92)),
		EMERALD_BRACELET(27, 37, LVL2_ENCHANTMENT, new Item(11079), new Item(11076), new Animation(719), new Graphics(-1)),
		EMERALD_AMULET(27, 37, LVL2_ENCHANTMENT, new Item(1729), new Item(1696), new Animation(719), new Graphics(114, 0, 92)),
		PRENATURE_AMULET(27, 37, LVL2_ENCHANTMENT, new Item(6040), new Item(6041), new Animation(719), new Graphics(114, 0, 92)),
		JADE_RING(27, 37, LVL2_ENCHANTMENT, new Item(21129), new Item(21084), new Animation(712), new Graphics(238, 0, 92)),
		JADE_NECKLACE(27, 37, LVL2_ENCHANTMENT, new Item(21146), new Item(21093), new Animation(719), new Graphics(114, 0, 92)),
		JADE_BRACELET(27, 37, LVL2_ENCHANTMENT, new Item(21180), new Item(21120), new Animation(719), new Graphics(-1)),
		JADE_AMULET(27, 37, LVL2_ENCHANTMENT, new Item(21163), new Item(21111), new Animation(719), new Graphics(114, 0, 92)),

		RUBY_RING(49, 59, LVL3_ENCHANTMENT, new Item(2568), new Item(1641), new Animation(712), new Graphics(238, 0, 92)),
		RUBY_NECKLACE(49, 59, LVL3_ENCHANTMENT, new Item(11194), new Item(1660), new Animation(720), new Graphics(115, 0, 92)),
		RUBY_BRACELET(49, 59, LVL3_ENCHANTMENT, new Item(11088), new Item(11085), new Animation(720), new Graphics(-1)),
		RUBY_AMULET(49, 59, LVL3_ENCHANTMENT, new Item(1725), new Item(1698), new Animation(720), new Graphics(115, 0, 92)),
		TOPAZ_RING(49, 59, LVL3_ENCHANTMENT, new Item(21140), new Item(21087), new Animation(712), new Graphics(238, 0, 92)),
		TOPAZ_NECKLACE(49, 59, LVL3_ENCHANTMENT, new Item(21157), new Item(21096), new Animation(720), new Graphics(115, 0, 92)),
		TOPAZ_BRACELET(49, 59, LVL3_ENCHANTMENT, new Item(21183), new Item(21123), new Animation(720), new Graphics(-1)),
		TOPAZ_AMULET(49, 59, LVL3_ENCHANTMENT, new Item(21166), new Item(21114), new Animation(720), new Graphics(114, 0, 92)),

		DIAMOND_RING(57, 67, LVL4_ENCHANTMENT, new Item(2570), new Item(1643), new Animation(712), new Graphics(238, 0, 92)),
		DIAMOND_NECKLACE(57, 67, LVL4_ENCHANTMENT, new Item(11090), new Item(1662), new Animation(720), new Graphics(115, 0, 92)),
		DIAMOND_BRACELET(57, 67, LVL4_ENCHANTMENT, new Item(11095), new Item(11092), new Animation(720), new Graphics(-1)),
		DIAMOND_AMULET(57, 67, LVL4_ENCHANTMENT, new Item(1731), new Item(1700), new Animation(720), new Graphics(115, 0, 92)),

		DRAGONSTONE_RING(68, 78, LVL5_ENCHANTMENT, new Item(2572), new Item(1645), new Animation(712), new Graphics(238, 0, 92)),
		DRAGONSTONE_NECKLACE(68, 78, LVL5_ENCHANTMENT, new Item(11105), new Item(1664), new Animation(721), new Graphics(116, 0, 92)),
		DRAGONSTONE_BRACELET(68, 78, LVL5_ENCHANTMENT, new Item(11118), new Item(11115), new Animation(721), new Graphics(-1)),
		DRAGONSTONE_AMULET(68, 78, LVL5_ENCHANTMENT, new Item(1712), new Item(1702), new Animation(721), new Graphics(116, 0, 92)),

		ONYX_RING(87, 97, LVL6_ENCHANTMENT, new Item(6583), new Item(6575), new Animation(712), new Graphics(238, 0, 92)),
		ONYX_NECKLACE(87, 97, LVL6_ENCHANTMENT, new Item(11128), new Item(6577), new Animation(721), new Graphics(452, 0, 92)),
		ONYX_BRACELET(87, 97, LVL6_ENCHANTMENT, new Item(11133), new Item(11130), new Animation(721), new Graphics(-1)),
		ONYX_AMULET(87, 97, LVL6_ENCHANTMENT, new Item(6585), new Item(6581), new Animation(721), new Graphics(452, 0, 92)),

		ZENYTE_RING(93, 110, LVL7_ENCHANTMENT, new Item(19550), new Item(19538), new Animation(712), new Graphics(238, 0, 92)),
		ZENYTE_NECKLACE(93, 110, LVL7_ENCHANTMENT, new Item(19547), new Item(19535), new Animation(721), new Graphics(452, 0, 92)),
		ZENYTE_BRACELET(93, 110, LVL7_ENCHANTMENT, new Item(19544), new Item(19532), new Animation(721), new Graphics(-1)),
		ZENYTE_AMULET(93, 110, LVL7_ENCHANTMENT, new Item(19553), new Item(19541), new Animation(721), new Graphics(452, 0, 92));

		public static final JewelleryEnchantmentItem[] values = values();

		private final int level;
		private final JewelleryEnchantmentType type;
		private final double xp;
		private final Item product, base;
		private final Graphics graphics;
		private final Animation animation;

		JewelleryEnchantmentItem(final int level, final double xp, final JewelleryEnchantmentType type, final Item product, final Item base, final Animation animation, final Graphics graphics) {
			this.level = level;
			this.xp = xp;
			this.type = type;
			this.product = product;
			this.base = base;
			this.animation = animation;
			this.graphics = graphics;
        }

        public static JewelleryEnchantmentItem getDataByMaterial(final Item item, final JewelleryEnchantmentType type) {
            for (final JewelleryEnchantmentItem data : values) {
                if (data.getBase().getId() == item.getId() && data.getType().equals(type)) {
                    return data;
                }
            }
            return null;
        }

        public int getLevel() {
            return level;
        }

        public JewelleryEnchantmentType getType() {
            return type;
        }

        public double getXp() {
            return xp;
        }

        public Item getProduct() {
            return product;
        }

        public Item getBase() {
            return base;
        }

        public Graphics getGraphics() {
            return graphics;
        }

        public Animation getAnimation() {
            return animation;
        }

    }

	private final JewelleryEnchantmentItem data;
	private int slot;
	private final ItemSpell spell;
	private SpellState state;

	public JewelleryEnchantment(final ItemSpell spell, final JewelleryEnchantmentItem data, final int slot) {
		this.spell = spell;
		this.data = data;
		this.slot = slot;
	}

	public static boolean check(final Player player, final JewelleryEnchantmentItem data) {
		if (player.isDead() || player.isFinished()) {
			return false;
		}
		if (player.getRaid().isPresent()) {
		    player.sendMessage("You can't enchant jewellery within the Chambers of Xerics.");
		    return false;
        }
		if (player.getSkills().getLevel(SkillConstants.MAGIC) < data.getLevel()) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a Magic level of " + data.getLevel() + " to enchant " + data.getBase().getName().toLowerCase() + "."));
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			return false;
		}
		return true;
	}

	@Override
	public boolean start() {
		return check(player, data);
	}

	@Override
	public boolean process() {
		return check(player, data);
	}

	@Override
	public int processWithDelay() {
		if (state != null) {
			state.remove();
		}

		if (data.equals(JewelleryEnchantmentItem.DIAMOND_AMULET)) {
			player.getAchievementDiaries().update(LumbridgeDiary.CRAFT_AMULET_OF_POWER, 0x4);
		}
		if (spell.getClass() == Lvl5Enchant.class) {
            SherlockTask.CAST_LV_FIVE_ENCHANT.progress(player);
        }
		player.setAnimation(data.getAnimation());
		player.setGraphics(data.getGraphics());
		player.getInterfaceHandler().openGameTab(GameTab.SPELLBOOK_TAB);
		spell.addXp(player, data.getXp());
		player.getInventory().replaceItem(data.getProduct().getId(), 1, slot);
		player.getInterfaceHandler().openGameTab(GameTab.SPELLBOOK_TAB);

		int slot = player.getInventory().getContainer().getSlotOf(data.getBase().getId());
		if (slot != -1) {
			this.slot = slot;
			state = new SpellState(player, spell.getLevel(), spell.getRunes());
			if (!state.check(false)) {
				return -1;
			}

			return 4;
		}

		return -1;
	}

	@Override
	public void stop() {
		
	}

}

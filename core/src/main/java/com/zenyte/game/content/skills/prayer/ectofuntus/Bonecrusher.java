package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.boons.impl.BountifulSacrifice;
import com.zenyte.game.content.skills.prayer.actions.Bones;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.item.AshSanctifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * @author Kris | 24/06/2019 13:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Bonecrusher extends ItemPlugin {

	public enum CrusherType {
		BONECRUSHER_NECKLACE(player -> player.getInventory().containsItem(ItemId.BONECRUSHER_NECKLACE, 1) || player.getEquipment().getId(EquipmentSlot.AMULET) == ItemId.BONECRUSHER_NECKLACE, (player, bone, burying) -> {
			if (activateKourendPrayerRestoreEffect(player) || activateDragonbonePrayerRestoreEffect(player))
				restorePrayer(player, bone);
			if (enabled(player)) {
				if (!burying)
					applyExperience(player, bone);
				return true;
			} else
				return false;
		}), BONECRUSHER(player -> player.getInventory().containsItem(ItemId.BONECRUSHER, 1), (player, bone, burying) -> {
			if (activateKourendPrayerRestoreEffect(player) || activateDragonbonePrayerRestoreEffect(player))
				restorePrayer(player, bone);
			if (enabled(player)) {
				if (!burying)
					applyExperience(player, bone);
				return true;
			} else
				return false;
		}), DRAGONBONE_NECKLACE(player -> player.getEquipment().getId(EquipmentSlot.AMULET) == ItemId.DRAGONBONE_NECKLACE, (player, bone, burying) -> {
			if (activateKourendPrayerRestoreEffect(player) || activateDragonbonePrayerRestoreEffect(player))
				restorePrayer(player, bone);
			return true;
		});
		private final Predicate<Player> predicate;
		private final CrushEffect effect;
		private static final CrusherType[] values = values();

		public static CrusherType get(final Player player) {
			for (final Bonecrusher.CrusherType type : values) {
				if (!type.getPredicate().test(player)) {
					continue;
				}
				return type;
			}
			return null;
		}


		public interface CrushEffect {
			boolean crush(final Player player, final Bones bone, final boolean burying);
		}

		CrusherType(Predicate<Player> predicate, CrushEffect effect) {
			this.predicate = predicate;
			this.effect = effect;
		}

		public Predicate<Player> getPredicate() {
			return predicate;
		}

		public CrushEffect getEffect() {
			return effect;
		}
	}

	private static boolean activateKourendPrayerRestoreEffect(Player player) {
		return player.inArea("Catacombs of Kourend");
	}

	public static boolean enabled(@NotNull final Player player) {
		return !player.getBooleanSetting(Setting.BONECRUSHING_INACTIVE);
	}

	private static void applyExperience(@NotNull final Player player, final Bones bone) {
		boolean hasBoon = player.getBoonManager().hasBoon(BountifulSacrifice.class);

		float boost = DiaryUtil.eligibleFor(DiaryReward.MORYTANIA_LEGS4, player) ? 1.0F : 0.5F;
		if(hasBoon)
			boost += 3.0F;
		player.getSkills().addXp(SkillConstants.PRAYER, boost * bone.getXp());
	}

	public static void restorePrayer(@NotNull final Player player, final Bones bone) {
		if (bone.equals(Bones.BONES)) {
			player.getPrayerManager().restorePrayerPoints(1);
		} else if (bone.equals(Bones.BIG_BONES)) {
			player.getPrayerManager().restorePrayerPoints(2);
		} else if (bone.equals(Bones.DRAGON_BONES) || bone.equals(Bones.WYRM_BONES) || bone.equals(Bones.DRAKE_BONES) || bone.equals(Bones.HYDRA_BONES)) {
			player.getPrayerManager().restorePrayerPoints(4);
		} else if (bone.equals(Bones.SUPERIOR_DRAGON_BONES)) {
			player.getPrayerManager().restorePrayerPoints(5);
		}
	}

	private static boolean activateDragonbonePrayerRestoreEffect(@NotNull Player player) {
		if (player.getEquipment().containsAnyOf(ItemId.DRAGONBONE_NECKLACE, ItemId.BONECRUSHER_NECKLACE) &&
				PlayerAttributesKt.getBoneCrusherNecklaceActivationTime(player) > WorldThread.getCurrentCycle()) {
			player.sendDeveloperMessage("Bone Crusher -> Blocking restore player effect for "+(PlayerAttributesKt.getBoneCrusherNecklaceActivationTime(player) - WorldThread.getCurrentCycle())+" cycles.");
			return false;
		}
		return true;
	}

	public static boolean handle(final Player player, final Item item) {
		return handle(player, item.getId());
	}

	public static boolean handle(final Player player, final int itemId) {
		final Bones bones = Bones.getBone(itemId);
		if (bones != null && !bones.isIgnoreEffects()) {
			final CrusherType crusherType = CrusherType.get(player);
			if (crusherType != null && crusherType.getEffect().crush(player, bones, false)) {
				return true;
			}
		}

        final Bones ash = Bones.getAsh(itemId);
        return ash != null && AshSanctifier.handleAshDrop(player, ash);
    }

	@Override
	public void handle() {
		bind("Activity", (player, item, container, slotId) -> {
			player.getSettings().toggleSetting(Setting.BONECRUSHING_INACTIVE);
			player.sendMessage(!enabled(player) ? "Your bonecrusher is no longer crushing bones." : "Your bonecrusher is now crushing bones.");
		});
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.BONECRUSHER};
	}
}

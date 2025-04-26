package com.zenyte.game.content.skills.prayer;

import com.zenyte.game.content.boons.impl.HolyInterventionI;
import com.zenyte.game.content.boons.impl.HolyInterventionII;
import com.zenyte.game.content.boons.impl.HolyInterventionIII;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TextUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum Prayer {
	THICK_SKIN(4104, 1, -1, 1, 2690, 4107, 4113, 4128, 4129, 5464, 5465),
	BURST_OF_STRENGTH(4105, 4, -1, 1, 2688, 4108, 4114, 4122, 4123, 4124, 4125, 4126, 4127, 4128, 4129, 5464, 5465),
	CLARITY_OF_THOUGHT(4106, 7, -1, 1, 2664, 4109, 4115, 4122, 4123, 4124, 4125, 4126, 4127, 4128, 4129, 5464, 5465),
	ROCK_SKIN(4107, 10, -1, 6, 2684, 4104, 4113, 4128, 4129, 5464, 5465),
	SUPERHUMAN_STRENGTH(4108, 13, -1, 6, 2689, 4105, 4114, 4122, 4123, 4124, 4125, 4126, 4127, 4128, 4129, 5464, 5465),
	IMPROVED_REFLEXES(4109, 16, -1, 6, 2662, 4106, 4115, 4122, 4123, 4124, 4125, 4126, 4127, 4128, 4129, 5464, 5465),
	RAPID_RESTORE(4110, 19, -1, 1, 2679),
	RAPID_HEAL(4111, 22, -1, 2, 2678),
	PROTECT_ITEM(4112, 25, -1, 2, 1982),
	STEEL_SKIN(4113, 28, -1, 12, 2687, 4104, 4107, 4128, 4129, 5464, 5465),
	ULTIMATE_STRENGTH(4114, 31, -1, 12, 2691, 4108, 4105, 4122, 4123, 4124, 4125, 4126, 4127, 4128, 4129, 5464, 5465),
	INCREDIBLE_REFLEXES(4115, 34, -1, 12, 2667, 4109, 4106, 4122, 4123, 4124, 4125, 4126, 4127, 4128, 4129, 5464, 5465),
	PROTECT_FROM_MAGIC(4116, 37, 2, 12, 2675, 4117, 4118, 4119, 4120, 4121),
	PROTECT_FROM_MISSILES(4117, 40, 1, 12, 2677, 4116, 4118, 4119, 4121, 4120),
	PROTECT_FROM_MELEE(4118, 43, 0, 12, 2676, 4116, 4117, 4119, 4121, 4120),
	RETRIBUTION(4119, 46, 3, 3, 2682, 4116, 4117, 4118, 4121, 4120),
	REDEMPTION(4120, 49, 5, 6, 2680, 4119, 4121, 4116, 4117, 4118),
	SMITE(4121, 52, 4, 18, 2686, 4116, 4117, 4118, 4119, 4120),
	SHARP_EYE(4122, 8, -1, 1, 2685, 4125, 4127, 4123, 4124, 4126, 4105, 4106, 4108, 4109, 4114, 4115, 4128, 4129, 5464, 5465),
	MYSTIC_WILL(4123, 9, -1, 1, 2670, 4125, 4127, 4122, 4124, 4126, 4105, 4106, 4108, 4109, 4114, 4115, 4128, 4129, 5464, 5465),
	HAWK_EYE(4124, 26, -1, 6, 2666, 4125, 4127, 4122, 4123, 4126, 4105, 4106, 4108, 4109, 4114, 4115, 4128, 4129, 5464, 5465),
	MYSTIC_LORE(4125, 27, -1, 6, 2668, 4124, 4127, 4122, 4123, 4126, 4105, 4106, 4108, 4109, 4114, 4115, 4128, 4129, 5464, 5465),
	EAGLE_EYE(4126, 44, -1, 12, 2665, 4125, 4127, 4122, 4123, 4124, 4105, 4106, 4108, 4109, 4114, 4115, 4128, 4129, 5464, 5465),
	MYSTIC_MIGHT(4127, 45, -1, 12, 2669, 4125, 4124, 4122, 4123, 4126, 4105, 4106, 4108, 4109, 4114, 4115, 4128, 4129, 5464, 5465),
	RIGOUR(5464, 74, -1, 24, 2685, 4125, 4124, 4122, 4123, 4126, 4105, 4106, 4108, 4109, 4114, 4115, 4127, 4128, 4129, 4104, 4107, 4113, 5465),
	CHIVALRY(4128, 60, -1, 24, 3826, 4129, 4105, 4106, 4108, 4109, 4114, 4115, 4122, 4123, 4124, 4125, 4126, 4127, 4104, 4107, 4113, 5464, 5465),
	PIETY(4129, 70, -1, 24, 3825, 4128, 4105, 4106, 4108, 4109, 4114, 4115, 4122, 4123, 4124, 4125, 4126, 4127, 4104, 4107, 4113, 5464, 5465),
	AUGURY(5465, 77, -1, 24, 2670, 4125, 4124, 4122, 4123, 4126, 4105, 4106, 4108, 4109, 4114, 4115, 4127, 4128, 4129, 4104, 4107, 4113, 5464),
	PRESERVE(5466, 55, -1, 3, 2679);

	private static final Prayer[] VALUES = values();
	private static final Int2ObjectOpenHashMap<Prayer> PRAYERS = new Int2ObjectOpenHashMap<Prayer>(VALUES.length);

	static {
		for (Prayer p : VALUES) {
			PRAYERS.put(p.varbit, p);
			p.setQuickPrayerCollisions(p.getQPCollisions());
		}
	}

	private final int varbit;
	private final int level;
	private final int headIcon;
	private final SoundEffect soundEffect;
	private final float drainRate;
	private final int[] collisions;
	private final String name;
	int[] quickPrayerCollisions;

	Prayer(final int varbit, final int level, final int headIcon, final float drainRate, final int soundEffect, final int... collisions) {
		this.varbit = varbit;
		this.level = level;
		this.headIcon = headIcon;
		this.drainRate = drainRate;
		this.soundEffect = new SoundEffect(soundEffect);
		this.collisions = collisions;
		this.name = TextUtils.capitalize(name().toLowerCase().replace("_", " ")).replace("From", "from").replace("Of", "of");
	}

	public static final Prayer getPrayer(final int varbitId) {
		return PRAYERS.get(varbitId);
	}

	public static Prayer get(int id) {
		return Prayer.VALUES[id];
	}

	private int[] getQPCollisions() {
		final int[] values = new int[getCollisions().length];
		for (Prayer prayers : Prayer.VALUES) {
			for (int i = 0; i < collisions.length; i++) {
				if (prayers.getVarbit() == collisions[i]) values[i] = prayers.ordinal();
			}
		}
		return values;
	}

	public int getVarbit() {
		return varbit;
	}

	public int getLevel() {
		return level;
	}

	public int getHeadIcon() {
		return headIcon;
	}

	public SoundEffect getSoundEffect() {
		return soundEffect;
	}

	public float getDrainRate(Player player) {
		double multiplier = 1.0D;

		if(player == null)
			return drainRate;

		if(player.hasBoon(HolyInterventionIII.class))
			multiplier = 0.5D;
		else if (player.hasBoon(HolyInterventionII.class))
			multiplier = 0.75D;
		else if (player.hasBoon(HolyInterventionI.class))
			multiplier = 0.90D;
		return (float) (drainRate * multiplier);
	}

	public int[] getCollisions() {
		return collisions;
	}

	public int[] getQuickPrayerCollisions() {
		return quickPrayerCollisions;
	}

	public void setQuickPrayerCollisions(int[] quickPrayerCollisions) {
		this.quickPrayerCollisions = quickPrayerCollisions;
	}

	public String getName() {
		return name;
	}
}

package com.zenyte.game.content.skills.magic.resources;

import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.RuneContainer;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.enums.EnumDefinitions;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 7. juuli 2018 : 01:11:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class StaffResource implements RuneResource {
	private static final int[] AIR_STAVES;
	private static final int[] WATER_STAVES;
	private static final int[] EARTH_STAVES;
	private static final int[] FIRE_STAVES;

	private static final void populate(final EnumDefinitions e, final int[] array) {
		int index = 0;
		for (final Integer value : e.getValues().keySet()) {
			array[index++] = value;
		}
	}

	static {
		final EnumDefinitions airStavesEnum = EnumDefinitions.get(988);
		AIR_STAVES = new int[airStavesEnum.getSize()];
		populate(airStavesEnum, AIR_STAVES);
		final EnumDefinitions waterStavesEnum = EnumDefinitions.get(989);
		WATER_STAVES = new int[waterStavesEnum.getSize()];
		populate(waterStavesEnum, WATER_STAVES);
		final EnumDefinitions earthStavesEnum = EnumDefinitions.get(996);
		EARTH_STAVES = new int[earthStavesEnum.getSize()];
		populate(earthStavesEnum, EARTH_STAVES);
		final EnumDefinitions fireStavesEnum = EnumDefinitions.get(997);
		FIRE_STAVES = new int[fireStavesEnum.getSize()];
		populate(fireStavesEnum, FIRE_STAVES);
	}

	@Override
	public RuneContainer getContainer() {
		return RuneContainer.STAFF;
	}

	@Override
	public int getAmountOf(final Player player, final int runeId, final int amountRequired) {
		final Item weapon = player.getWeapon();
		if (weapon == null) {
			return 0;
		}
		final int id = weapon.getId();
		if (runeId == Magic.AIR_RUNE) {
			if (ArrayUtils.contains(AIR_STAVES, id)) {
				return amountRequired;
			}
		} else if (runeId == Magic.WATER_RUNE) {
			if (ArrayUtils.contains(WATER_STAVES, id)) {
				return amountRequired;
			}
		} else if (runeId == Magic.EARTH_RUNE) {
			if (ArrayUtils.contains(EARTH_STAVES, id)) {
				return amountRequired;
			}
		} else if (runeId == Magic.FIRE_RUNE) {
			if (ArrayUtils.contains(FIRE_STAVES, id)) {
				return amountRequired;
			}
		}
		return 0;
	}

	@Override
	public void consume(final Player player, final int runeId, final int amount) {
	}

	@Override
	public int[] getAffectedRunes() {
		return new int[] {Magic.AIR_RUNE, Magic.EARTH_RUNE, Magic.WATER_RUNE, Magic.FIRE_RUNE};
	}
}

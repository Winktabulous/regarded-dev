package com.near_reality.game.content.gauntlet;

/**
 * An enumerated type whose elements correspond to the possible types of a Gauntlet game instance.
 *
 * @author Andys1814.
 * @since 1/19/2022.
 */
public enum GauntletType {
	STANDARD(0, 9024, false, false),
	STANDARD_NO_PREP(0, 9024, false, true),
	CORRUPTED(1, 9038, true, false),
	CORRUPTED_NO_PREP(1, 9038, true, true);

	private final int varbit;
	private final int deathBossId;
	private final boolean corrupted;
	private final boolean noPrep;

	GauntletType(int varbit, int deathBossId, boolean corrupted, boolean noPrep) {
		this.varbit = varbit;
		this.deathBossId = deathBossId;
		this.corrupted = corrupted;
		this.noPrep = noPrep;
	}

	public int getVarbit() {
		return varbit;
	}

	public int getDeathBossId() {
		return deathBossId;
	}

	public boolean isCorrupted() {
		return corrupted;
	}

	public boolean isNoPrep() {
		return noPrep;
	}

}

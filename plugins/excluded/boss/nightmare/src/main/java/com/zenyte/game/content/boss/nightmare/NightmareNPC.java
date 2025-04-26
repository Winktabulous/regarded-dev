package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.content.boss.nightmare.area.NightmareBossArea;
import com.zenyte.game.content.boss.nightmare.area.NightmareLobbyArea;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;

import java.util.Set;
import java.util.function.Consumer;

public class NightmareNPC extends BaseNightmareNPC {

	public static final Location REGION_BASE = new Location((15515 >> 8) << 6, (15515 & 0xff) << 6);
	public static final int SLEEPING = NpcId.THE_NIGHTMARE_9432;
	public static final int AWAKE_SHIELD_P1 = NpcId.THE_NIGHTMARE_9425;
	public static final int AWAKE_SHIELD_P2 = NpcId.THE_NIGHTMARE_9426;
	public static final int AWAKE_SHIELD_P3 = NpcId.THE_NIGHTMARE_9427;
	public static final int AWAKE_P1 = NpcId.THE_NIGHTMARE_9428;
	public static final int AWAKE_P2 = NpcId.THE_NIGHTMARE_9429;
	public static final int AWAKE_P3 = NpcId.THE_NIGHTMARE_9430;
	public static final int EXPLODING = NpcId.THE_NIGHTMARE_9431;

	private int playersOnStart = 0;


	public static NightmarePhase[] phases;

	public NightmareNPC() {
		super(SLEEPING, BaseNightmareNPC.CENTER);
	}

	@Override
	public int getSleepingId() {
		return SLEEPING;
	}

	@Override
	public int getAwakeShieldP1Id() {
		return AWAKE_SHIELD_P1;
	}

	@Override
	public int getExplodingId() {
		return EXPLODING;
	}

	public boolean isShieldNpc() {
		return switch (getId()) {
			case AWAKE_SHIELD_P1, AWAKE_SHIELD_P2, AWAKE_SHIELD_P3 -> true;
			default -> false;
		};
	}

	public boolean isAwakeNpc() {
		return switch (getId()) {
			case AWAKE_P1, AWAKE_P2, AWAKE_P3, EXPLODING -> true;
			default -> false;
		};
	}

	public NightmarePhase getPhase() {
		if(phases == null) {
			generatePhases();
		}
		if(phases[0].forPlayers != playersOnStart) {
			generatePhases();
		}
		return switch (getId()) {
			case AWAKE_SHIELD_P1, AWAKE_P1 -> phases[0];
			case AWAKE_SHIELD_P2, AWAKE_P2 -> phases[1];
			case AWAKE_SHIELD_P3, AWAKE_P3 -> phases[2];
			default -> null;
		};
	}

	private void generatePhases() {
		phases = new NightmarePhase[]{
				new NightmarePhase(NightmarePhase.NightmarePhaseNumber.FIRST, getAdjustedHp(1), AWAKE_P1, AWAKE_SHIELD_P2, NightmareLobbyNPC.FIRST_PHASE, getAdjustedTotemDamage()),
				new NightmarePhase(NightmarePhase.NightmarePhaseNumber.SECOND, getAdjustedHp(2), AWAKE_P2, AWAKE_SHIELD_P3, NightmareLobbyNPC.SECOND_PHASE, getAdjustedTotemDamage()),
				new NightmarePhase(NightmarePhase.NightmarePhaseNumber.THIRD, getAdjustedHp(3), AWAKE_P3, -1, NightmareLobbyNPC.THIRD_PHASE, getAdjustedTotemDamage()),
		};
	}

	private int getAdjustedHp(int phase) {
		if(phase == 1) {
			return switch (this.playersOnStart) {
				case 1 -> 1500;
				case 2 -> 1800;
				case 3 -> 2100;
				default -> 2400;
			};
		} else if (phase == 2) {
			return switch (this.playersOnStart) {
				case 1 -> 1000;
				case 2 -> 1200;
				case 3 -> 1400;
				default -> 1600;
			};
		} else {
			return switch (this.playersOnStart) {
				case 1 -> 500;
				case 2 -> 600;
				case 3 -> 700;
				default -> 800;
			};
		}
	}

	private int getAdjustedTotemDamage() {
		return switch (this.playersOnStart) {
			case 1 -> 500;
			case 2 -> 600;
			case 3 -> 700;
			default -> 800;
		};
	}

	@Override
	public Set<Player> getAreaPlayers() {
		return NightmareBossArea.getAreaPlayers();
	}

	@Override
	public int meleeMaxHit() {
		return 37;
	}

	@Override
	public int rangedMageMaxHit() {
		return 25;
	}

	@Override
	public double protectionPrayerMod() {
		return 0.2;
	}

	@Override
	public void init(int playersOnStart) {
		this.playersOnStart = playersOnStart;
		int baseShield = 2000;
		double factor = switch (playersOnStart) {
			case 1 -> 0.70D;
			case 2 -> 0.80D;
			case 3 -> 0.90D;
			case 4 -> 1.00D;
			default -> 0.60D + (Math.min(playersOnStart, 20) * 0.10D);
		};
		int totemBaseCharge = switch(playersOnStart) {
			case 1 -> 100;
			case 2 -> 300;
			case 3 -> 400;
			case 4 -> 500;
			default -> 100 + (Math.min(playersOnStart, 14) * 100);
		};
		setBaseShield((int) (baseShield * factor));
		setTotemBaseCharge(totemBaseCharge);
		setSleepwalkerCount(Utils.interpolate(1, 24, 1, 80, playersOnStart));
	}

	@Override
	public int attack(Entity target) {
		if (!isAttackable()) return combatDefinitions.getAttackSpeed();

		increaseAttackCounter();
		if (Utils.randomBoolean(8)) {
			NightmarePhase phase = getPhase();
			if (phase != null) {
				switch (phase.getNumber()) {
					case FIRST:
						return husksAttack();
						/*if (Utils.randomBoolean() && getAttackCounter() > 4) {
							setAttackCounter(0);
							if (getSpecialAttackCounter() == 0) {
								setSpecialAttackCounter(1);
								return flowerPowerAttack(null);
							}
							if (getSpecialAttackCounter() == 1) {
								setSpecialAttackCounter(0);
								return husksAttack();
							}
						} else {
							return graspingClawAttack();
						}*/
					case SECOND:
						if (Utils.random(3) == 0 && getAttackCounter() > 4) {
							setAttackCounter(0);
							if (getSpecialAttackCounter() == 0) {
								setSpecialAttackCounter(1);
								return parasiteAttack();
							}
							if (getSpecialAttackCounter() == 1) {
								setSpecialAttackCounter(0);
								return curseAttack();
							}
						} else {
							return graspingClawAttack();
						}
					case THIRD:
						if (Utils.random(3) == 0 && getAttackCounter() > 4) {
							setAttackCounter(0);
							if (getSpecialAttackCounter() == 0) {
								setSpecialAttackCounter(1);
								return chargeAttack();
							}
							if (getSpecialAttackCounter() == 1) {
								setSpecialAttackCounter(0);
								return sporesAttack(null);
							}
						} else {
							return graspingClawAttack();
						}
				}
			}
		}

		if (!combat.outOfRange(target, 0, target.getSize(), true) && !CollisionUtil.collides(getX(), getY(), getSize(), target.getX(), target.getY(), target.getSize()) && Utils.randomBoolean(5)) {
			meleeAttack(target);
		} else {
			if (Utils.randomBoolean()) {
				mageRangeAttack(AttackType.MAGIC);
			} else {
				mageRangeAttack(AttackType.RANGED);
			}
		}

		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public Location locationTransform(Location location) {
		return location;
	}

	@Override
	public void lobbyNpcRun(Consumer<NightmareLobbyNPC> consumer) {
		consumer.accept(NightmareLobbyArea.lobbyNPC);
	}

	@Override
	public int baseMaxHitpoints() {
		return switch (this.playersOnStart) {
			case 1 -> 1500;
			case 2 -> 1800;
			case 3 -> 2100;
			default -> 2400;
		};
	}

	@Override
	public Location getBase() {
		return REGION_BASE;
	}

	@Override
	public int getMageHuskId() {
		return NightmareHuskMagicNPC.ID;
	}

	@Override
	public int getRangedHuskId() {
		return NightmareHuskRangedNPC.ID;
	}

	@Override
	public Location getSleepwalkerLocation(int index) {
		return NightmareSleepwalkerData.values[index].getLocation();
	}

	@Override
	public int getSleepwalkerDamage() {
		return absorbed * 4 + 5;
	}

	@Override
	public int getSleepwalkerId() {
		return Utils.random(NightmareSleepwalker.IDS);
	}

	@Override
	public String getTrackingName() {
		return "The Nightmare";
	}

	@Override
	public void updateKillStat(long duration) {
		NightmareGlobal.updateKillStatistics(duration);
		NightmareBossArea.checkWakePulse(50);
	}

	@Override
	public int parasiteId() {
		return NightmareParasiteNPC.ID;
	}

	@Override
	public int weakenParasiteId() {
		return NightmareParasiteNPC.ID_WEAKEN;
	}

}

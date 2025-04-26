package com.zenyte.game.world.entity.player.variables;

import com.google.common.eventbus.Subscribe;
import com.near_reality.game.content.bountyhunter.WildyExtKt;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.near_reality.game.world.entity.player.SkillRestoration;
import com.zenyte.Main;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.GameConstants;
import com.zenyte.game.content.boons.impl.RunForrestRun;
import com.zenyte.game.content.boons.impl.SpecialBreed;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.BonusXpManager;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.PlayerSkulls;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.loyalty.LoyaltyManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.EvilBobIsland;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.plugins.events.LogoutEvent;
import com.zenyte.utils.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @apiNote this class is being serialised.
 *
 * @apiNote FOR PERISTENT VARIABLES, APPEND THEM TO METHOD {@link #set(PlayerVariables)}.
 */
public final class PlayerVariables {

	private static final Logger log = LoggerFactory.getLogger(PlayerVariables.class);

	private final transient Player player;

	private final transient SkillRestoration skillRestoration;

	public static final boolean RANDOM_EVENTS_ENABLED = false;
	private static final int SPEC_REGEN_TICKS = 50;
	private static final int SPEC_REGEN_TICKS_LIGHTBEARER = 25;

	public PlayerVariables(final Player player) {
		this.player = player;
		skillRestoration = new SkillRestoration();
		resetCharacterSaveTimer();
	}

	public void set(final PlayerVariables copy) {
		copy.scheduled.forEach((k, v) -> {
			try {
				final TickVariable tickTask = TickVariable.valueOf(TickVariable.class, k);
				scheduled.put(k, new Variable(v.ticks, tickTask.task, tickTask.messages));
			} catch (Exception e) {
				log.error("", e);
			}
		});
		bonusXP = copy.bonusXP;
		raidsBoost = copy.raidsBoost;
		runEnergy = copy.runEnergy;
		playTime = copy.playTime;
		absorption = copy.absorption;
		skulled = copy.skulled;
		lastLogin = copy.lastLogin;
		raidAdvertsQuota = copy.raidAdvertsQuota;
		ardougneFarmTeleports = copy.ardougneFarmTeleports;
		fountainOfRuneTeleports = copy.fountainOfRuneTeleports;
		fishingColonyTeleports = copy.fishingColonyTeleports;
		rellekkaTeleports = copy.rellekkaTeleports;
		sherlockTeleports = copy.sherlockTeleports;
		faladorPrayerRecharges = copy.faladorPrayerRecharges;
		runReplenishments = copy.runReplenishments;
		freeAlchemyCasts = copy.freeAlchemyCasts;
		cabbageFieldTeleports = copy.cabbageFieldTeleports;
		nardahTeleports = copy.nardahTeleports;
		spellbookSwaps = copy.spellbookSwaps;
		claimedBattlestaves = copy.claimedBattlestaves;
		grappleAndCrossbowSearches = copy.grappleAndCrossbowSearches;
		teletabPurchases = copy.teletabPurchases;
		zulrahResurrections = copy.zulrahResurrections;
		statRegeneration = copy.statRegeneration;
		kourendWoodlandTeleports = copy.kourendWoodlandTeleports;
		mountKaruulmTeleports = copy.mountKaruulmTeleports;
		enhancedCrystalChestOpenCount = copy.enhancedCrystalChestOpenCount;
		dontPromptFeroxEnclaveBarrier = copy.dontPromptFeroxEnclaveBarrier;
		ganoBoosterKillsLeft = copy.ganoBoosterKillsLeft;
		gauntletBoosterCompletionsLeft = copy.gauntletBoosterCompletionsLeft;
		bloodMoneyBoosterLeft = copy.bloodMoneyBoosterLeft;
		clueBoosterLeft = copy.clueBoosterLeft;
		tobBoosterleft = copy.tobBoosterleft;
		larransKeyBoosterTick = copy.larransKeyBoosterTick;
		slayerBoosterTick = copy.slayerBoosterTick;
		petBoosterTick = copy.petBoosterTick;
		enhancedStewTick = copy.enhancedStewTick;
		revenantBoosterTick = copy.revenantBoosterTick;
		nexBoosterleft = copy.nexBoosterleft;
		thrallDamageDone = copy.thrallDamageDone;
		pvmArenaBoosterTick = copy.pvmArenaBoosterTick;
		bountyHunterSkipTick = copy.bountyHunterSkipTick;
	}

	private transient long lastClanKick;
	private double runEnergy = 100;
	private transient int potionDelay;
	private transient int foodDelay;
	private transient int karambwanDelay;
	private transient int nextScheduledCharacterSave;
	private transient int ticksInterval;
	private int bonusXP;
	private int raidsBoost;
	private int playTime;
	private int lastLogin;
	private int raidAdvertsQuota = 15;
	private int slimePitTeleports;
	private int ardougneFarmTeleports;
	private int fountainOfRuneTeleports;
	private int fishingColonyTeleports;
	private int sherlockTeleports;
	private int faladorPrayerRecharges;
	private int rellekkaTeleports;
	private int runReplenishments;
	private int freeAlchemyCasts;
	private int cabbageFieldTeleports;
	private int nardahTeleports;
	private int kourendWoodlandTeleports;
	private int mountKaruulmTeleports;
	private int spellbookSwaps;
	private int zulrahResurrections;
	private int absorption;
	private int overloadType;
	private int toleranceTimer;
	private int specRegeneration;
	private int healthRegeneration;
	private int grappleAndCrossbowSearches;
	private int teletabPurchases;
	private int statRegeneration;
	private int enhancedCrystalChestOpenCount;
	private boolean skulled;
	private boolean claimedBattlestaves;
	private boolean dontPromptFeroxEnclaveBarrier;
	private int thrallDamageDone;
	private int trollheimTeleports;
	private int morUikRekTeleports;
	private final Map<String, Variable> scheduled = new LinkedHashMap<>();
	private final transient Set<HealthRegenBoost> healthRegenBoosts = EnumSet.noneOf(HealthRegenBoost.class);
	private transient int cycle = calculateCycle();
	private int ganoBoosterKillsLeft;
	private int gauntletBoosterCompletionsLeft;
	private int bloodMoneyBoosterLeft;
	private int clueBoosterLeft;
	private int tobBoosterleft;
	private int larransKeyBoosterTick;
	private int slayerBoosterTick;
	private int petBoosterTick;
	private int enhancedStewTick;
	private int revenantBoosterTick;
	private int nexBoosterleft;
	private int pvmArenaBoosterTick;
	private int bountyHunterSkipTick;

	private void resetCharacterSaveTimer() {
		nextScheduledCharacterSave = GameConstants.WORLD_PROFILE.isDevelopment() ? (int) TimeUnit.SECONDS.toTicks(15) : ((int) TimeUnit.MINUTES.toTicks(1));
	}

	public void addBoost(final HealthRegenBoost boost) {
		healthRegenBoosts.add(boost);
		final int current = cycle;
		cycle = calculateCycle();
		if (current > cycle || boost == HealthRegenBoost.RAPID_HEAL) {
			healthRegeneration = 0;
		}
	}

	public void setSkull(final boolean skulled) {
		this.skulled = skulled;
		if (skulled) {
			schedule((int) TimeUnit.MINUTES.toTicks(20), TickVariable.SKULL);
		} else
			PlayerAttributesKt.setBlackSkulled(player, false);
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	public void removeBoost(final HealthRegenBoost boost) {
		healthRegenBoosts.remove(boost);
		cycle = calculateCycle();
		if (boost == HealthRegenBoost.RAPID_HEAL) {
			healthRegeneration = 0;
		}
	}

	public void resetTeleblock() {
		final Object attr = player.getTemporaryAttributes().get("tb_remove_delay");
		if (attr != null && (int) attr >= WorldThread.getCurrentCycle()) {
			return;
		}
		player.getTemporaryAttributes().remove("tb_name");
		scheduled.remove(TickVariable.TELEBLOCK.toString());
		scheduled.remove(TickVariable.TELEBLOCK_IMMUNITY.toString());
		player.getVarManager().sendBit(4163, 0);
	}

	@Subscribe
	public static final void onLogout(final LogoutEvent event) {
		event.getPlayer().getVariables().resetTeleblock();
	}

	private int calculateCycle() {
		if (healthRegenBoosts.isEmpty()) {
			return 100;
		}
		int delay = 100;
		if (healthRegenBoosts.contains(HealthRegenBoost.REGEN_BRACELET)) {
			delay /= 2;
		}
		if (healthRegenBoosts.contains(HealthRegenBoost.RAPID_HEAL) || healthRegenBoosts.contains(HealthRegenBoost.HITPOINTS_CAPE)) {
			delay /= 2;
		}
		return delay;
	}

	/**
	 * Schedules a tickvariable to be executed on the player if there isn't one
	 * of the same type with longer duration already executing.
	 *
	 * @param duration
	 *            the duration of the variable.
	 * @param variable
	 *            the variable itself.
	 */
	public void schedule(final int duration, final TickVariable variable) {
		final Variable existing = scheduled.get(variable.toString());
		if (existing != null) {
			if (existing.ticks >= duration) {
				return;
			}
		}
		scheduled.put(variable.toString(), new Variable(duration, variable.task, variable.messages));
	}

	public void cancel(final TickVariable variable) {
		scheduled.remove(variable.toString());
	}

	public void resetScheduled() {
		scheduled.forEach((k, v) -> {
			if (v.task != null) {
				v.task.run(player, 0);
			}
		});
		scheduled.clear();
		if (player.getEquipment().getId(EquipmentSlot.AMULET) == 22557) {
			player.getVariables().setPermanentSkull();
		}
	}

	public int getTime(final TickVariable variable) {
		final Variable scheduled = this.scheduled.get(variable.toString());
		if (scheduled == null) {
			return 0;
		}
		return scheduled.ticks;
	}



	public void onLogin() {
		if (getTime(TickVariable.STAMINA_ENHANCEMENT) > 0) {
			player.getVarManager().sendBit(25, 1);
		}
	}

	public void process() {
		playTime++;
		specRegeneration++;
		toleranceTimer++;
		if (raidsBoost > 0) {
			final int raidsBitValue = player.getVarManager().getBitValue(5425);
			if (raidsBitValue > 0 && raidsBitValue < 5) {
				raidsBoost--;
			}
		}
		if (bonusXP > 0) {
			bonusXP--;
			if (bonusXP == 0) {
				player.sendMessage(Colour.RED.wrap("Your private bonus experience has ran out!"));
			}
		}
		if (RANDOM_EVENTS_ENABLED && playTime > 30_000 && Utils.random(GameConstants.randomEvent) == 0) {
			if (!player.isLocked() && !player.isFinished() && !player.isDead()
					&& !player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL) && !player.isStaff()) {
				final long now = System.currentTimeMillis();
				final long lastEvent = player.getNumericAttribute("last random event").longValue();
				if (lastEvent + TimeUnit.HOURS.toMillis(1) < now
						&& player.getActionManager().getLastAction() >= (now - TimeUnit.MINUTES.toMillis(5))) {
					if (!player.isUnderCombat() && !(player.getActionManager().getAction() instanceof PlayerCombat)) {
						final RegionArea area = player.getArea();
						if (area instanceof RandomEventRestrictionPlugin) {
							final RandomEventRestrictionPlugin rerp = (RandomEventRestrictionPlugin) area;
							if (rerp.disablesRandomEvents()) {
								// lol, literally the only random we have is evil bob
								EvilBobIsland.teleport(player);
							}
						}
					}
				}
			}
		}
		if (larransKeyBoosterTick > 0) {
			larransKeyBoosterTick--;
			if (larransKeyBoosterTick == 15) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Larran's key booster is about to expire."));
			} else if (larransKeyBoosterTick == 0) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Larran's key booster has expired."));
			}
		}
		if (slayerBoosterTick > 0) {
			slayerBoosterTick--;
			if (slayerBoosterTick == 15) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Slayer booster is about to expire."));
			} else if (slayerBoosterTick == 0) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Slayer booster has expired."));
			}
		}
		if(bountyHunterSkipTick > 0) {
			bountyHunterSkipTick--;
			if(bountyHunterSkipTick == 0) {
				player.sendMessage("Your Bounty Hunter skip count has been reset.");
				WildyExtKt.resetBountySkipCounter(player);
			}
		}
		if (petBoosterTick > 0) {
			petBoosterTick--;
			if (petBoosterTick == 15) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Pet booster is about to expire."));
			} else if (petBoosterTick == 0) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Pet booster has expired."));
			}
		}
		if (enhancedStewTick > 0) {
			enhancedStewTick--;
			if (enhancedStewTick == 0) {
				for (int i = SkillConstants.COOKING; i < SkillConstants.COUNT; i++) {
					player.getSkills().setLevel(i, player.getSkills().getLevelForXp(i));
				}
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Spicy stew effect has expired."));
			} else if (enhancedStewTick % 5 == 0) {
				for (int i = SkillConstants.COOKING; i < SkillConstants.COUNT; i++) {
					player.getSkills().boostSkill(i, 5);
				}
			}
			if (enhancedStewTick == 30) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Spicy stew effect is about to expire."));
			}
		}
		if (revenantBoosterTick > 0) {
			revenantBoosterTick--;
			if (revenantBoosterTick == 15) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Revenant booster is about to expire."));
			} else if (revenantBoosterTick == 0) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Revenant booster has expired."));
			}
		}
		if (pvmArenaBoosterTick > 0) {
			pvmArenaBoosterTick--;
			if (pvmArenaBoosterTick == 15) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Sir Eldric's combat booster is about to expire."));
			} else if (pvmArenaBoosterTick == 0) {
				player.sendMessage(Colour.RS_PURPLE.wrap("Your Sir Eldric's combat booster has expired."));
			}
		}
		//Resynchronize the client timers for time spent online and server online time in general.
		if (++ticksInterval % 100 == 0) {
			synchNoticeboardVars();
			player.refreshGameClock();
		}
		if (ticksInterval % LoyaltyManager.LOYALTY_POINTS_INTERVAL_TICKS == 0) {
			player.getLoyaltyManager().informSession((int) (ticksInterval / LoyaltyManager.LOYALTY_POINTS_INTERVAL_TICKS));
		}
		if (--nextScheduledCharacterSave <= 0) {
			resetCharacterSaveTimer();
			CoresManager.getLoginManager().submitSaveRequest(player);
		}
		if (foodDelay > 0) {
			foodDelay--;
		}
		if (potionDelay > 0) {
			potionDelay--;
		}
		if (karambwanDelay > 0) {
			karambwanDelay--;
		}

		final int energy = player.getCombatDefinitions().getSpecialEnergy();
		if (energy == 100) {
			specRegeneration = 0;
		}
		int regenTickCount = player.getEquipment().getId(EquipmentSlot.RING) == ItemId.LIGHTBEARER ? SPEC_REGEN_TICKS_LIGHTBEARER : SPEC_REGEN_TICKS;
		if(player.getBoonManager().hasBoon(SpecialBreed.class))
			regenTickCount *= 0.9;
		if (specRegeneration % regenTickCount == 0 && energy < 100) {
			player.getCombatDefinitions().setSpecialEnergy(Math.min(100, energy + 10));
		}
		skillRestoration.pulse(statRegeneration++, player);
		healthRegeneration++;
		if (healthRegeneration % cycle == 0) {
			if (!player.isDead() && player.getHitpoints() < player.getMaxHitpoints()) {
				player.setHitpoints(player.getHitpoints() + 1);
			}
		}
		if (!scheduled.isEmpty()) {
			scheduled.values().removeIf(variable -> {
				try {

					variable.ticks--;
					if (variable.messages != null) {
						final String message = variable.messages.get(variable.ticks);
						if (message != null) {
							player.sendMessage(message);
						}
					}
					if (variable.task != null) {
						variable.task.run(player, variable.ticks);
					}
				}catch (Exception e) {
					log.error("An error occurred while processing variable {}", variable, e);
				}
				return variable.ticks <= 0;
			});
		}
	}

	public void synchNoticeboardVars() {
		final VarManager varManager = player.getVarManager();
		varManager.sendVar(3800, (int) TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - Main.serverStartTime));
		varManager.sendVar(3802, (int) (playTime * 0.6F));
		varManager.sendVar(3805, Math.max(0, (int) TimeUnit.MILLISECONDS.toSeconds(BonusXpManager.expirationDate - System.currentTimeMillis())));
		varManager.sendVar(3806, (int) (raidsBoost * 0.6F));
		varManager.sendVar(3801, (int) (bonusXP * 0.6F));
	}

	public void setRunEnergy(double runEnergy) {
		setRunEnergy(runEnergy, false);
	}

	public void setRunEnergy(double runEnergy, boolean wilderness) {
		if (runEnergy > 100) {
			runEnergy = 100;
		} else if (runEnergy < 0) {
			runEnergy = 0;
		}
		if(player.getBoonManager().hasBoon(RunForrestRun.class) && !wilderness)
			runEnergy = 100;
		if (this.runEnergy == runEnergy) {
			return;
		}
		this.runEnergy = runEnergy;
		player.getPacketDispatcher().sendRunEnergy();
	}

	public void forceRunEnergy(double runEnergy) {
		if (runEnergy < 0) {
			runEnergy = 0;
		}
		this.runEnergy = runEnergy;
		player.getPacketDispatcher().sendRunEnergy();
	}

	public void setPermanentSkull() {
		this.skulled = true;
		schedule(Integer.MAX_VALUE, TickVariable.SKULL);
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	public void removeSkull() {
		this.skulled = false;
		scheduled.remove(TickVariable.SKULL.toString());
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		PlayerAttributesKt.setBlackSkulled(player, false);
	}

	public long getLastClanKick() {
		return lastClanKick;
	}

	public void setLastClanKick(long lastClanKick) {
		this.lastClanKick = lastClanKick;
	}

	public double getRunEnergy() {
		return runEnergy;
	}

	public int getPotionDelay() {
		return potionDelay;
	}

	public void setPotionDelay(int potionDelay) {
		this.potionDelay = potionDelay;
	}

	public int getFoodDelay() {
		return foodDelay;
	}

	public void setFoodDelay(int foodDelay) {
		this.foodDelay = foodDelay;
	}

	public int getKarambwanDelay() {
		return karambwanDelay;
	}

	public void setKarambwanDelay(int karambwanDelay) {
		this.karambwanDelay = karambwanDelay;
	}

	public int getNextScheduledCharacterSave() {
		return nextScheduledCharacterSave;
	}

	public void setNextScheduledCharacterSave(int nextScheduledCharacterSave) {
		this.nextScheduledCharacterSave = nextScheduledCharacterSave;
	}

	public int getTicksInterval() {
		return ticksInterval;
	}

	public void setTicksInterval(int ticksInterval) {
		this.ticksInterval = ticksInterval;
	}

	public int getBonusXP() {
		return bonusXP;
	}

	public void setBonusXP(int bonusXP) {
		this.bonusXP = bonusXP;
	}

	public int getRaidsBoost() {
		return raidsBoost;
	}

	public void setRaidsBoost(int raidsBoost) {
		this.raidsBoost = raidsBoost;
	}

	public int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	public int getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(int lastLogin) {
		this.lastLogin = lastLogin;
	}

	public int getRaidAdvertsQuota() {
		return raidAdvertsQuota;
	}

	public void setRaidAdvertsQuota(int raidAdvertsQuota) {
		this.raidAdvertsQuota = raidAdvertsQuota;
	}

	public int getSlimePitTeleports() {
		return slimePitTeleports;
	}

	public void setSlimePitTeleports(int slimePitTeleports) {
		this.slimePitTeleports = slimePitTeleports;
	}

	public int getArdougneFarmTeleports() {
		return ardougneFarmTeleports;
	}

	public void setArdougneFarmTeleports(int ardougneFarmTeleports) {
		this.ardougneFarmTeleports = ardougneFarmTeleports;
	}

	public int getFountainOfRuneTeleports() {
		return fountainOfRuneTeleports;
	}

	public void setFountainOfRuneTeleports(int fountainOfRuneTeleports) {
		this.fountainOfRuneTeleports = fountainOfRuneTeleports;
	}

	public int getFishingColonyTeleports() {
		return fishingColonyTeleports;
	}

	public void setFishingColonyTeleports(int fishingColonyTeleports) {
		this.fishingColonyTeleports = fishingColonyTeleports;
	}

	public int getSherlockTeleports() {
		return sherlockTeleports;
	}

	public void setSherlockTeleports(int sherlockTeleports) {
		this.sherlockTeleports = sherlockTeleports;
	}

	public int getFaladorPrayerRecharges() {
		return faladorPrayerRecharges;
	}

	public void setFaladorPrayerRecharges(int faladorPrayerRecharges) {
		this.faladorPrayerRecharges = faladorPrayerRecharges;
	}

	public int getRellekkaTeleports() {
		return rellekkaTeleports;
	}

	public void setRellekkaTeleports(int rellekkaTeleports) {
		this.rellekkaTeleports = rellekkaTeleports;
	}

	public int getRunReplenishments() {
		return runReplenishments;
	}

	public void setRunReplenishments(int runReplenishments) {
		this.runReplenishments = runReplenishments;
	}

	public int getFreeAlchemyCasts() {
		return freeAlchemyCasts;
	}

	public void setFreeAlchemyCasts(int freeAlchemyCasts) {
		this.freeAlchemyCasts = freeAlchemyCasts;
	}

	public int getCabbageFieldTeleports() {
		return cabbageFieldTeleports;
	}

	public void setCabbageFieldTeleports(int cabbageFieldTeleports) {
		this.cabbageFieldTeleports = cabbageFieldTeleports;
	}

	public int getNardahTeleports() {
		return nardahTeleports;
	}

	public void setNardahTeleports(int nardahTeleports) {
		this.nardahTeleports = nardahTeleports;
	}

	public int getKourendWoodlandTeleports() {
		return kourendWoodlandTeleports;
	}

	public void setKourendWoodlandTeleports(int kourendWoodlandTeleports) {
		this.kourendWoodlandTeleports = kourendWoodlandTeleports;
	}

	public int getMountKaruulmTeleports() {
		return mountKaruulmTeleports;
	}

	public void setMountKaruulmTeleports(int mountKaruulmTeleports) {
		this.mountKaruulmTeleports = mountKaruulmTeleports;
	}

	public int getSpellbookSwaps() {
		return spellbookSwaps;
	}

	public void setSpellbookSwaps(int spellbookSwaps) {
		this.spellbookSwaps = spellbookSwaps;
	}

	public int getZulrahResurrections() {
		return zulrahResurrections;
	}

	public void setZulrahResurrections(int zulrahResurrections) {
		this.zulrahResurrections = zulrahResurrections;
	}

	public int getAbsorption() {
		return absorption;
	}

	public void setAbsorption(int absorption) {
		this.absorption = absorption;
	}

	public int getOverloadType() {
		return overloadType;
	}

	public void setOverloadType(int overloadType) {
		this.overloadType = overloadType;
	}

	public int getToleranceTimer() {
		return toleranceTimer;
	}

	public void setToleranceTimer(int toleranceTimer) {
		this.toleranceTimer = toleranceTimer;
	}

	public int getSpecRegeneration() {
		return specRegeneration;
	}

	public void setSpecRegeneration(int specRegeneration) {
		this.specRegeneration = specRegeneration;
	}

	public int getHealthRegeneration() {
		return healthRegeneration;
	}

	public void setHealthRegeneration(int healthRegeneration) {
		this.healthRegeneration = healthRegeneration;
	}

	public int getGrappleAndCrossbowSearches() {
		return grappleAndCrossbowSearches;
	}

	public void setGrappleAndCrossbowSearches(int grappleAndCrossbowSearches) {
		this.grappleAndCrossbowSearches = grappleAndCrossbowSearches;
	}

	public int getTeletabPurchases() {
		return teletabPurchases;
	}

	public void setTeletabPurchases(int teletabPurchases) {
		this.teletabPurchases = teletabPurchases;
	}

	public boolean isSkulled() {
		return skulled;
	}

	public boolean isClaimedBattlestaves() {
		return claimedBattlestaves;
	}

	public void setClaimedBattlestaves(boolean claimedBattlestaves) {
		this.claimedBattlestaves = claimedBattlestaves;
	}

	public Map<String, Variable> getScheduled() {
		return scheduled;
	}

	public Set<HealthRegenBoost> getHealthRegenBoosts() {
		return healthRegenBoosts;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public void setTimesOpenedEnhancedCrystalChest(int enhancedCrystalChestOpenCount) {
		this.enhancedCrystalChestOpenCount = enhancedCrystalChestOpenCount;
	}

	public int getTimesOpenedEnhancedCrystalChest() {
		return enhancedCrystalChestOpenCount;
	}

	public void setDontPromptFeroxEnclaveBarrier(boolean dontPromptFeroxEnclaveBarrier) {
		this.dontPromptFeroxEnclaveBarrier = dontPromptFeroxEnclaveBarrier;
	}

	public boolean isDontPromptFeroxEnclaveBarrier() {
		return dontPromptFeroxEnclaveBarrier;
	}

	public int getGanoBoosterKillsLeft() {
		return ganoBoosterKillsLeft;
	}

	public void setGanoBoosterKillsLeft(int ganoBoosterKillsLeft) {
		this.ganoBoosterKillsLeft = ganoBoosterKillsLeft;
	}

	public int getGauntletBoosterCompletionsLeft() {
		return gauntletBoosterCompletionsLeft;
	}

	public void setGauntletBoosterCompletionsLeft(int gauntletBoosterCompletionsLeft) {
		this.gauntletBoosterCompletionsLeft = gauntletBoosterCompletionsLeft;
	}

	public int getBloodMoneyBoosterLeft() {
		return bloodMoneyBoosterLeft;
	}

	public void setBloodMoneyBoosterLeft(int bloodMoneyBoosterLeft) {
		this.bloodMoneyBoosterLeft = bloodMoneyBoosterLeft;
	}

	public int getClueBoosterLeft() {
		return clueBoosterLeft;
	}

	public void setClueBoosterLeft(int clueBoosterLeft) {
		this.clueBoosterLeft = clueBoosterLeft;
	}

	public int getTobBoosterleft() {
		return tobBoosterleft;
	}

	public void setTobBoosterleft(int tobBoosterleft) {
		this.tobBoosterleft = tobBoosterleft;
	}

	public void setLarransKeyBoosterTick(int larransKeyBoosterTick) {
		this.larransKeyBoosterTick = larransKeyBoosterTick;
	}

	public int getLarransKeyBoosterTick() {
		return larransKeyBoosterTick;
	}

	public void setSlayerBoosterTick(int slayerBoosterTick) {
		this.slayerBoosterTick = slayerBoosterTick;
	}

	public int getSlayerBoosterTick() {
		return slayerBoosterTick;
	}

	public void setPetBoosterTick(int petBoosterTick) {
		this.petBoosterTick = petBoosterTick;
	}

	public int getPetBoosterTick() {
		return petBoosterTick;
	}

	public void setEnhancedStewTick(int enhancedStewTick) {
		this.enhancedStewTick = enhancedStewTick;
	}

	public int getEnhancedStewTick() {
		return enhancedStewTick;
	}

	public int getRevenantBoosterTick() {
		return revenantBoosterTick;
	}

	public void setRevenantBoosterTick(int revenantBoosterTick) {
		this.revenantBoosterTick = revenantBoosterTick;
	}

	public void setNexBoosterleft(int nexBoosterleft) {
		this.nexBoosterleft = nexBoosterleft;
	}

	public int getNexBoosterleft() {
		return nexBoosterleft;
	}

	public void setThrallDamageDone(int thrallDamageDone) {
		this.thrallDamageDone = thrallDamageDone;
	}

	public int getThrallDamageDone() {
		return thrallDamageDone;
	}

	public int getTrollheimTeleports() { return trollheimTeleports; }

	public void setTrollheimTeleports(int trollheimTeleports) { this.trollheimTeleports = trollheimTeleports; }

	public int getMorUikRekTeleports() { return morUikRekTeleports; }

	public void setMorUikRekTeleports(int morUikRekTeleports) { this.morUikRekTeleports = morUikRekTeleports; }

	public int getPvmArenaBoosterTick() {
		return pvmArenaBoosterTick;
	}

	public void setPvmArenaBoosterTick(int pvmArenaBoosterTick) {
		this.pvmArenaBoosterTick = pvmArenaBoosterTick;
	}

	public int getBountyHunterSkipTick() { return bountyHunterSkipTick; }

	public void setBountyHunterSkipTick(int bountyHunterSkipTick) { this.bountyHunterSkipTick = bountyHunterSkipTick; }

	public enum HealthRegenBoost {
		RAPID_HEAL, REGEN_BRACELET, HITPOINTS_CAPE
    }
}

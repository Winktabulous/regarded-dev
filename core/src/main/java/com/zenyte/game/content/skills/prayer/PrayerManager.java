package com.zenyte.game.content.skills.prayer;

import com.google.gson.annotations.Expose;
import com.zenyte.game.content.boons.impl.TheRedeemer;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.packet.out.SyncClientVarCache;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.*;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.variables.PlayerVariables;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.PrayerPlugin;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.utils.TextUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.function.BiConsumer;

public class PrayerManager {
	private transient final Player player;
	@Expose
	private int quickPrayerSettings;
	private final transient Object2IntOpenHashMap<Prayer> activePrayers = new Object2IntOpenHashMap<>();
	private transient int drain;
	public static final int INTERFACE = 541;
	public static final int QUICK_PRAYERS_INTERFACE = 77;
	private static final Graphics REDEMPTION_GFX = new Graphics(436);
	private static final Graphics RETRIBUTION_GFX = new Graphics(437);
	private static final Projectile RETRIBUTION_PROJ = new Projectile(438, 0, 0, 10, 0, 1, 11, 5);

	public PrayerManager(final Player player) {
		this.player = player;
		this.drainConsumer = (k, v) -> {
			if (v < (int) WorldThread.WORLD_CYCLE) {
				drain += k.getDrainRate(player);
			}
		};
	}

	public void setPrayer(final PrayerManager prayerManager) {
		setQuickPrayerSettings(prayerManager.getQuickPrayerSettings());
	}

	public int getPrayerPoints() {
		return player.getSkills().getLevel(SkillConstants.PRAYER);
	}

	/**
	 * Toggles the given prayer.
	 * 
	 * @param id
	 * @param quickPrayers
	 */
	public void togglePrayer(int id, final boolean quickPrayers) {
		player.stop(Player.StopType.INTERFACES);
		if (!quickPrayers) {
			if (id == 25 || id == 24) {
				id++;
			} else if (id == 26) {
				id -= 2;
			}
		}
		final Prayer prayer = Prayer.get(id);
		final RegionArea area = player.getArea();
		if (prayer == null || player.isDead() || !player.getControllerManager().canUsePrayer(prayer) || !hasRequirements(prayer) || (area instanceof PrayerPlugin && !((PrayerPlugin) area).activatePrayer(player, prayer))) {
			player.send(new SyncClientVarCache());
			return;
		}
		final boolean activating = !isActive(prayer);
		if (activating) {
			activatePrayer(prayer);
			if (!quickPrayers)
				player.getVarManager().sendBit(4103, 0);
		} else {
			deactivatePrayer(prayer);
		}
	}

	/**
	 * Activates the given prayer.
	 * 
	 * @param prayer
	 */
	public void activatePrayer(final Prayer prayer) {
		if ((prayer == Prayer.PROTECT_FROM_MAGIC || prayer == Prayer.PROTECT_FROM_MISSILES || prayer == Prayer.PROTECT_FROM_MELEE) && player.getNumericTemporaryAttribute("prayer delay").longValue() > System.currentTimeMillis()) {
			player.sendMessage("You've been injured and can't use protection prayers!");
			player.send(new SyncClientVarCache());
			player.sendSound(blockSoundEffect);
			return;
		}
		if (prayer.getHeadIcon() != -1) {
			player.getAppearance().setHeadIcon((byte) prayer.getHeadIcon());
		}
		handleActivation(prayer);
		if (prayer == Prayer.RAPID_HEAL) {
			player.getVariables().addBoost(PlayerVariables.HealthRegenBoost.REGEN_BRACELET);
		}
		if (prayer == Prayer.CHIVALRY) {
			SherlockTask.ACTIVATE_CHIVALRY_PRAYER.progress(player);
		}
	}

	private void handleActivation(Prayer prayer) {
		deactivateCollisions(prayer);
		if (player.getBooleanTemporaryAttribute("nightmare_curse")) {
			final Prayer originalPrayer = cursePrayerTypeReverse(prayer);
			player.getVarManager().sendBit(prayer.getVarbit(), 0);
			player.getVarManager().sendBit(originalPrayer.getVarbit(), 1);
		} else {
			player.getVarManager().sendBit(prayer.getVarbit(), 1);
		}

		activePrayers.put(prayer, (int) WorldThread.WORLD_CYCLE);
		player.getPacketDispatcher().sendSoundEffect(prayer.getSoundEffect());
	}

	public static Prayer cursePrayerTypeReverse(Prayer input) {
		switch (input) {
			case PROTECT_FROM_MISSILES:
				return Prayer.PROTECT_FROM_MAGIC;
			case PROTECT_FROM_MELEE:
				return Prayer.PROTECT_FROM_MISSILES;
			case PROTECT_FROM_MAGIC:
				return Prayer.PROTECT_FROM_MELEE;
			default:
				return input;
		}
	}

	public double getSkillBoost(final int skill) {
		if (skill == SkillConstants.DEFENCE) {
			if (activePrayers.containsKey(Prayer.THICK_SKIN)) {
				return 1.05;
			} else if (activePrayers.containsKey(Prayer.ROCK_SKIN)) {
				return 1.1;
			} else if (activePrayers.containsKey(Prayer.STEEL_SKIN)) {
				return 1.15;
			} else if (activePrayers.containsKey(Prayer.CHIVALRY)) {
				return 1.2;
			} else if (activePrayers.containsKey(Prayer.PIETY) || activePrayers.containsKey(Prayer.RIGOUR) || activePrayers.containsKey(Prayer.AUGURY)) {
				return 1.25;
			}
		} else if (skill == SkillConstants.STRENGTH) {
			if (activePrayers.containsKey(Prayer.BURST_OF_STRENGTH)) {
				return 1.05;
			} else if (activePrayers.containsKey(Prayer.SUPERHUMAN_STRENGTH)) {
				return 1.1;
			} else if (activePrayers.containsKey(Prayer.ULTIMATE_STRENGTH)) {
				return 1.15;
			} else if (activePrayers.containsKey(Prayer.CHIVALRY)) {
				return 1.18;
			} else if (activePrayers.containsKey(Prayer.PIETY)) {
				return 1.23;
			}
		} else if (skill == SkillConstants.ATTACK) {
			if (activePrayers.containsKey(Prayer.CLARITY_OF_THOUGHT)) {
				return 1.05;
			} else if (activePrayers.containsKey(Prayer.IMPROVED_REFLEXES)) {
				return 1.1;
			} else if (activePrayers.containsKey(Prayer.INCREDIBLE_REFLEXES) || activePrayers.containsKey(Prayer.CHIVALRY)) {
				return 1.15;
			} else if (activePrayers.containsKey(Prayer.PIETY)) {
				return 1.2;
			}
		}
		return 1;
	}

	public double getRangedBoost(final int style) {
		if (style == SkillConstants.DEFENCE) {
			if (activePrayers.containsKey(Prayer.RIGOUR)) {
				return 1.25;
			}
			return 1;
		}
		if (activePrayers.containsKey(Prayer.SHARP_EYE)) {
			return 1.05;
		} else if (activePrayers.containsKey(Prayer.HAWK_EYE)) {
			return 1.1;
		} else if (activePrayers.containsKey(Prayer.EAGLE_EYE)) {
			return 1.15;
		} else if (activePrayers.containsKey(Prayer.RIGOUR)) {
			return style == SkillConstants.ATTACK ? 1.2 : style == SkillConstants.STRENGTH ? 1.23 : 1;
		}
		return 1;
	}

	public double getMagicBoost(final int style) {
		if (activePrayers.containsKey(Prayer.MYSTIC_WILL)) {
			return 1.05;
		} else if (activePrayers.containsKey(Prayer.MYSTIC_LORE)) {
			return 1.1;
		} else if (activePrayers.containsKey(Prayer.MYSTIC_MIGHT)) {
			return 1.15;
		} else if (activePrayers.containsKey(Prayer.AUGURY)) {
			return style == SkillConstants.ATTACK ? 1.25 : style == SkillConstants.DEFENCE ? 1.25 : 1;
		}
		return 1;
	}

	private static final SoundEffect OUT_OF_PRAY_SOUND = new SoundEffect(2672);
	private static final SoundEffect DEACTIVE_SOUND_EFFECT = new SoundEffect(2663);
	private static final SoundEffect blockSoundEffect = new SoundEffect(2277);

	/**
	 * De-activates the given prayer.
	 * 
	 * @param prayer
	 */
	public void deactivatePrayer(final Prayer prayer) {
		activePrayers.removeInt(prayer);
		deactivateWithoutRemoving(prayer);
	}

	private void deactivateWithoutRemoving(final Prayer prayer) {
		if (player.getBooleanTemporaryAttribute("nightmare_curse")) {
			final Prayer originalPrayer = cursePrayerTypeReverse(prayer);
			player.getVarManager().sendBit(originalPrayer.getVarbit(), 0);
		} else {
			player.getVarManager().sendBit(prayer.getVarbit(), 0);
		}
		player.getVarManager().sendBit(4103, 0);
		if (getPrayerPoints() == 0) {
			player.getPacketDispatcher().sendSoundEffect(OUT_OF_PRAY_SOUND);
		} else {
			player.getPacketDispatcher().sendSoundEffect(DEACTIVE_SOUND_EFFECT);
		}
		if (prayer == Prayer.RAPID_HEAL) {
			player.getVariables().removeBoost(PlayerVariables.HealthRegenBoost.REGEN_BRACELET);
		}
		if (prayer.equals(Prayer.PROTECT_FROM_MAGIC) || prayer.equals(Prayer.PROTECT_FROM_MISSILES) || prayer.equals(Prayer.PROTECT_FROM_MELEE) || prayer.equals(Prayer.RETRIBUTION) || prayer.equals(Prayer.REDEMPTION) || prayer.equals(Prayer.SMITE)) {
			player.getAppearance().setHeadIcon((byte) -1);
		}
		if (prayer == Prayer.RAPID_HEAL) {
			player.getVariables().setHealthRegeneration(0);
		}
	}

	public void deactivateActivePrayers() {
		activePrayers.keySet().forEach(this::deactivateWithoutRemoving);
		activePrayers.clear();
	}

	/**
	 * De-activates the colliding prayers of the given prayer.
	 * 
	 * @param prayer
	 */
	public void deactivateCollisions(final Prayer prayer) {
		for (final int varbit : prayer.getCollisions()) {
			activePrayers.removeInt(Prayer.getPrayer(varbit));
			player.getVarManager().sendBit(varbit, 0);
		}
	}

	/**
	 * Toggles the given quick-prayer.
	 * 
	 * @param id
	 */
	public void setQuickPrayer(final int id) {
		final Prayer prayer = Prayer.get(id);
		if (prayer == null || player.isDead() || (!hasRequirements(prayer, false) && !Utils.getShiftedBoolean(quickPrayerSettings, id)) || player.isLocked()) {
			if (prayer != null) {
				refreshQuickPrayers();
			}
			return;
		}
		quickPrayerSettings = Utils.getShiftedValue(quickPrayerSettings, id);
		deactivateQuickPrayerCollisions(prayer);
		refreshQuickPrayers();
	}

	/**
	 * Refreshes the quick-prayers that are enabled by sending the config.
	 */
	public void refreshQuickPrayers() {
		int value = 0;
		for (int i = 0; i < 29; i++) {
			if (Utils.getShiftedBoolean(quickPrayerSettings, i)) {
				final Prayer prayer = Prayer.get(i);
				value |= (int) Math.pow(2, prayer.ordinal());
			}
		}
		player.getVarManager().sendVar(84, value);
	}

	/**
	 * Toggles the quick-prayers.
	 */
	public void toggleQuickPrayers() {
		if (player.isDead()) {
			player.getVarManager().sendBit(4103, player.getVarManager().getBitValue(4103));
			return;
		}
		if (getPrayerPoints() <= 0) {
			player.sendMessage("You have run out of prayer points, you can recharge at an altar.");
			player.getVarManager().sendBit(4103, player.getVarManager().getBitValue(4103));
			player.getPacketDispatcher().sendSoundEffect(OUT_OF_PRAY_SOUND);
			return;
		}
		final boolean on = player.getVarManager().getBitValue(4103) == 1;
		final boolean empty = quickPrayerSettings == 0;
		if (on) {
			deactivateActivePrayers();
		} else {
			final RegionArea area = player.getArea();
			for (int i = 0; i < 29; i++) {
				if (Utils.getShiftedBoolean(quickPrayerSettings, i)) {
					final Prayer prayer = Prayer.get(i);
					if (!isActive(Prayer.get(i))) {
						if (prayer == null || player.isDead() || !player.getControllerManager().canUsePrayer(prayer) || !hasRequirements(prayer) || (area instanceof PrayerPlugin && !((PrayerPlugin) area).activatePrayer(player, prayer))) {
							continue;
						}
						this.activatePrayer(Prayer.get(i));
					}
				}
			}
		}
		if (empty) {
			player.getVarManager().sendBit(4103, 1);
			player.getVarManager().sendBit(4103, 0);
			player.sendMessage("You haven't selected any quick-prayers.");
		} else {
			player.getVarManager().sendBit(4103, on ? 0 : 1);
		}
	}

	/**
	 * De-activates the colliding quick-prayers of the given prayer.
	 * 
	 * @param prayer
	 */
	public void deactivateQuickPrayerCollisions(final Prayer prayer) {
		for (int i = 0; i < prayer.getQuickPrayerCollisions().length; i++) {
			quickPrayerSettings = Utils.getShiftedValue(quickPrayerSettings, prayer.getQuickPrayerCollisions()[i], false);
		}
	}

	/**
	 * Opens the quick-prayers window.
	 */
	public void openQuickPrayers() {
		if (player.isLocked()) {
			return;
		}
		player.getInterfaceHandler().sendInterface(InterfacePosition.PRAYER_TAB, QUICK_PRAYERS_INTERFACE);
		player.getInterfaceHandler().openGameTab(GameTab.PRAYER_TAB);
		player.getPacketDispatcher().sendComponentSettings(QUICK_PRAYERS_INTERFACE, 4, 0, 28, AccessMask.CLICK_OP1);
	}

	private transient BiConsumer<Prayer, Integer> drainConsumer;

	public void process() {
		if (activePrayers.isEmpty()) {
			return;
		}

		activePrayers.forEach(drainConsumer);



		int resistance = 60 + (player.getBonuses().getBonus(Bonuses.Bonus.PRAYER) * 2);
		if (!WildernessArea.isWithinWilderness(player)) {
			if (player.getMemberRank().equalToOrGreaterThan(MemberRank.UBER))
				resistance = (int) ((double) resistance * 1.5D);
			else if (player.getMemberRank().equalToOrGreaterThan(MemberRank.LEGENDARY)) {
				resistance = (int) ((double) resistance * 1.25D);
			}
		}

		while (drain > resistance) {
			//Only send the message if prayer is about to go from 1 point to 0.
			drainPrayerPoints(1);
			drain -= resistance;
			if (getPrayerPoints() <= 0) {
				deactivateActivePrayers();
			}
		}
	}

	public boolean hasRequirements(final Prayer prayer) {
		return hasRequirements(prayer, true);
	}

	/**
	 * Determines if the prayer has the requirements to activate the given
	 * prayer.
	 * 
	 * @param prayer
	 * @return
	 */
	public boolean hasRequirements(final Prayer prayer, final boolean checkPoints) {
		if (checkPoints && getPrayerPoints() <= 0) {
			return false;
		}
		final Settings settings = player.getSettings();
		if (player.getSkills().getLevelForXp(SkillConstants.PRAYER) < prayer.getLevel()) {
			player.getDialogueManager().start(new PlainChat(player, "You need a <col=000080>Prayer</col> level of " + prayer.getLevel() + " to use <col=000080>" + prayer.getName() + "</col>."));
			return false;
		}
		if (prayer.equals(Prayer.CHIVALRY) && player.getSkills().getLevelForXp(SkillConstants.DEFENCE) < 65) {
			player.getDialogueManager().start(new PlainChat(player, "You need a <col=000080>Defence</col> level of 65 to use <col=000080>" + prayer.getName() + "</col>."));
			return false;
		}
		if (prayer.equals(Prayer.PIETY) && player.getSkills().getLevelForXp(SkillConstants.DEFENCE) < 70) {
			player.getDialogueManager().start(new PlainChat(player, "You need a <col=000080>Defence</col> level of 70 to use <col=000080>" + prayer.getName() + "</col>."));
			return false;
		}
		if ((prayer.equals(Prayer.RIGOUR) && (!settings.isRigour() || player.getSkills().getLevelForXp(SkillConstants.DEFENCE) < 70)) || (prayer.equals(Prayer.AUGURY) && (!settings.isAugury() || player.getSkills().getLevelForXp(SkillConstants.DEFENCE) < 70)) || (prayer.equals(Prayer.PRESERVE) && !settings.isPreserve())) {
			if (prayer.equals(Prayer.RIGOUR) || prayer.equals(Prayer.AUGURY)) {
				player.getDialogueManager().start(new PlainChat(player, "You need a <col=000080>Prayer</col> level of " + prayer.getLevel() + ", a <col=000080>Defence</col> level of 70, and to have learnt the prayer in order to use <col=000080>" + TextUtils.capitalizeFirstCharacter(prayer.getName().toLowerCase()) + "</col>."));
			} else {
				player.getDialogueManager().start(new PlainChat(player, "You need a <col=00080>Prayer</col> level of " + prayer.getLevel() + " and to have learnt the prayer in order to use <col=000080>" + TextUtils.capitalizeFirstCharacter(prayer.getName().toLowerCase()) + "</col>."));
			}
			return false;
		}
		if (player.getNumericTemporaryAttribute("prayerDelay").longValue() > Utils.currentTimeMillis()) {
			player.sendMessage("You cannot turn prayers on right now.");
			return false;
		}
		if (player.getTemporaryAttributes().get("SeverEffect") != null && (long) player.getTemporaryAttributes().get("SeverEffect") > Utils.currentTimeMillis() && (prayer.equals(Prayer.PROTECT_FROM_MAGIC) || prayer.equals(Prayer.PROTECT_FROM_MISSILES) || prayer.equals(Prayer.PROTECT_FROM_MELEE))) {
			player.sendMessage("You can't activate this prayer due to the effect of the special attack Sever.");
			return false;
		}
		return true;
	}

	/**
	 * Drains the prayer points by @param amount
	 * 
	 * @param amount
	 */
	public int drainPrayerPoints(final int amount) {
		final int points = getPrayerPoints();
		if ((points - amount) >= 0) {
			setPrayerPoints(points - amount);
			return amount;
		} else {
			setPrayerPoints(0);
			return points;
		}
	}

	public int drainPrayerPoints(final double percentage, final int minimumDrain) {
		final int currentPoints = getPrayerPoints();
		final int amount = Math.max((int) (currentPoints * (percentage / 100.0F)), minimumDrain);
		if ((currentPoints - amount) >= 0) {
			setPrayerPoints(currentPoints - amount);
			return amount;
		} else {
			if (currentPoints > 0) {
				player.sendMessage("You have run out of prayer points, you can recharge at an altar.");
			}
			setPrayerPoints(0);
			return currentPoints;
		}
	}

	public void restorePrayerPoints(final int amount) {
		final int level = player.getSkills().getLevelForXp(SkillConstants.PRAYER);
		final int points = getPrayerPoints();
		if (points >= level) {
			return;
		}
		setPrayerPoints(Math.min((points + amount), level));
	}

	public void setPrayerPoints(final int amount) {
		final Skills skills = player.getSkills();
		if (skills.getLevel(SkillConstants.PRAYER) != 0 && amount <= 0) {
			deactivateActivePrayers();
			player.sendMessage("You have run out of prayer points, you can recharge at an altar.");
		}
		skills.setLevel(SkillConstants.PRAYER, amount);
	}

	/**
	 * Determines if the given prayer is active or not by checking the value of
	 * the varbit.
	 * 
	 * @param prayer
	 * @return
	 */
	public boolean isActive(final Prayer prayer) {
		return activePrayers.containsKey(prayer);
	}

	/**
	 * Enables/disables the quick-prayer at index @param index
	 * 
	 * @param index
	 * @param activated
	 */
	public void setQuickPrayer(final int index, final boolean activated) {
		quickPrayerSettings = Utils.getShiftedValue(quickPrayerSettings, index, activated);
	}

	public void applyRedemptionEffect() {
		if(player.hasBoon(TheRedeemer.class)) {
			player.heal(Math.min(player.getMaxHitpoints(), 50 - player.getHitpoints()));
		} else {
			player.heal((int) (player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 0.25F));
		}
		setPrayerPoints(0);
		player.setGraphics(REDEMPTION_GFX);
	}

	public void applyRetributionEffect(final Entity source) {
		player.setGraphics(RETRIBUTION_GFX);
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (x != 0 || y != 0) {
					World.sendProjectile(player, player.getLocation().transform(x, y, 0), RETRIBUTION_PROJ);
				}
			}
		}
		CharacterLoop.forEach(player.getLocation(), 1, Entity.class, p -> {
			if (p == player || p.isDead() || p instanceof Player && !((Player) p).isCanPvp() || (p instanceof NPC && !((NPC) p).isAttackable())) return;
			if (!p.isMultiArea() && p != source) {
				return;
			}
			final int level = player.getSkills().getLevelForXp(SkillConstants.PRAYER);
			final Hit hit = new Hit(player, Utils.random((int) (level * 0.25F)), HitType.REGULAR);
			p.applyHit(hit);
		});
	}

	public int getQuickPrayerSettings() {
		return quickPrayerSettings;
	}

	public void setQuickPrayerSettings(int quickPrayerSettings) {
		this.quickPrayerSettings = quickPrayerSettings;
	}

	public Object2IntOpenHashMap<Prayer> getActivePrayers() {
		return activePrayers;
	}
}

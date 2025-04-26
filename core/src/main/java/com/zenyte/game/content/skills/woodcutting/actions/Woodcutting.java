package com.zenyte.game.content.skills.woodcutting.actions;

import com.near_reality.game.content.crystal.CrystalShardKt;
import com.near_reality.game.content.skills.woodcutting.AxeDefinition;
import com.near_reality.game.model.item.degrading.Degradeable;
import com.near_reality.game.world.entity.player.PlayerSkillingModifiersKt;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.boons.impl.IWantItAll;
import com.zenyte.game.content.boons.impl.SwissArmyMan;
import com.zenyte.game.content.boons.impl.Woodsman;
import com.zenyte.game.content.skills.firemaking.Firemaking;
import com.zenyte.game.content.skills.woodcutting.AxeDefinitions;
import com.zenyte.game.content.skills.woodcutting.TreeDefinitions;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.ClueItemUtil;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.object.WorldObjectUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.types.config.items.ItemDefinitions;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static com.near_reality.game.world.entity.player.PlayerSkillingModifiersKt.*;
import static com.near_reality.game.world.entity.player.PlayerSkillingModifiersKt.determineGatheringMultiplier;

/**
 * @author Kris | 13. dets 2017 : 6:07.25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
public class Woodcutting extends Action {
	public static final int SULLIUSCEP_INDEX_VARBIT = 5808;
	public static final Graphics BURN_GFX = new Graphics(86, 0, 180);
	private static final int BIRD_NEST_CHANCE = 128;
	public static final SoundEffect TREE_FALL_SOUND = new SoundEffect(2734);
	private final WorldObject tree;
	private final TreeDefinitions definitions;
	private AxeResult axe;
	private final String logName;
	private int ticks;
	private final Runnable onFall;

	static {
		VarManager.appendPersistentVarbit(SULLIUSCEP_INDEX_VARBIT);
	}

	public Woodcutting(final WorldObject tree, final TreeDefinitions definitions) {
		this(tree, definitions, null);
	}

	public Woodcutting(final WorldObject tree, final TreeDefinitions definitions, final Runnable onFall) {
		this.tree = tree;
		this.definitions = definitions;
		final ItemDefinitions defs = ItemDefinitions.get(definitions.getLogsId());
		logName = defs == null ? "null" : defs.getName().toLowerCase();
		this.onFall = onFall;
	}

	@Override
	public boolean start() {
		final Optional<Woodcutting.AxeResult> optionalAxe = getAxe(player);
		if (!optionalAxe.isPresent()) {
			player.sendMessage("You do not have an axe which you have the woodcutting level to use.");
			return false;
		}
		this.axe = optionalAxe.get();
		if (definitions.getLevel() > player.getSkills().getLevel(SkillConstants.WOODCUTTING)) {
			player.sendMessage("You need a Woodcutting level of at least " + definitions.getLevel() + " to chop down this tree.");
			return false;
		}
		if (!check()) {
			return false;
		}
		player.sendFilteredMessage("You swing your axe at the " + WorldObjectUtils.getObjectNameOfPlayer(tree, player).toLowerCase() + ".");
		delay(axe.getDefinition().getCutTime());
		return true;
	}

	private boolean check() {
		if (definitions.getLevel() > player.getSkills().getLevel(SkillConstants.WOODCUTTING)) {
			player.sendMessage("You need a Woodcutting level of at least " + definitions.getLevel() + " to chop down this tree.");
			return false;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.sendFilteredMessage("Not enough space in your inventory.");
			return false;
		}
		return World.exists(tree);
	}


	public static final class AxeResult {
		private final AxeDefinition definitions;
		private final Container container;
		private final int slot;
		private final Item item;

		public AxeResult(AxeDefinition definitions, Container container, int slot, Item item) {
			this.definitions = definitions;
			this.container = container;
			this.slot = slot;
			this.item = item;
		}

		public AxeDefinition getDefinition() {
			return definitions;
		}

		public Container getContainer() {
			return container;
		}

		public int getSlot() {
			return slot;
		}

		public Item getItem() {
			return item;
		}
	}

	public static Optional<AxeResult> getAxe(final Player player) {
		final int level = player.getSkills().getLevel(SkillConstants.WOODCUTTING);
		final Container inventory = player.getInventory().getContainer();
		final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
		if(player.getBoonManager().hasBoon(SwissArmyMan.class)) {
			AxeDefinitions axe = SwissArmyMan.getAxeForLevel(player);
			return Optional.of(new AxeResult(axe, player.getBank().getContainer(), -1, new Item(axe.getItemId())));
		}
		final AxeDefinition[] values = AxeDefinitions.VALUES;
		for (final AxeDefinition def : values) {
			if (level < def.getLevelRequired()) continue;
			if (weapon == def.getItemId()) {
				return Optional.of(new AxeResult(def, player.getEquipment().getContainer(), 3, player.getWeapon()));
			}
			for (int slot = 0; slot < 28; slot++) {
				final Item item = inventory.get(slot);
				if (item == null || item.getId() != def.getItemId()) {
					continue;
				}
				return Optional.of(new AxeResult(def, player.getInventory().getContainer(), slot, item));
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean process() {
		if (ticks++ % 4 == 0) player.setAnimation(axe.getDefinition().getTreeCutAnimation());
		return check();
	}

	public boolean success() {
		assert definitions.getSpeed() > 0;
		final int level = player.getSkills().getLevel(SkillConstants.WOODCUTTING) + (player.inArea("Woodcutting Guild") ? 7 : 0);
		final int advancedLevels = level - definitions.getSpeed();
		return Math.min(Math.round(advancedLevels * 0.8F) + 20, 90) > Utils.random(100);
	}

	@Override
	public int processWithDelay() {
		if (!success()) {
			return axe.getDefinition().getCutTime();
		}
		addLog();
		if (player.getLocation().getRegionId() != 7504) {
			if (Utils.random(definitions.getFallChance() - 1) == 0) {
				if (definitions == TreeDefinitions.SULLIUSCEP_TREE) {
					final int currentIndex = player.getVarManager().getBitValue(SULLIUSCEP_INDEX_VARBIT);
					player.getVarManager().sendBit(SULLIUSCEP_INDEX_VARBIT, currentIndex < 5 ? currentIndex + 1 : 0);
				} else {
					player.getPacketDispatcher().sendSoundEffect(TREE_FALL_SOUND);
					if (onFall == null) {
						final WorldObject stump = new WorldObject(TreeDefinitions.getStumpId(tree.getId()), tree.getType(), tree.getRotation(), tree.getX(), tree.getY(), tree.getPlane());
						World.spawnObject(stump);
						WorldTasksManager.schedule(() -> World.spawnObject(tree), definitions.getRespawnDelay());
					} else {
						onFall.run();
					}
				}
				player.setAnimation(Animation.STOP);
				return -1;
			}
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.setAnimation(Animation.STOP);
			player.sendFilteredMessage("Not enough space in your inventory.");
			return -1;
		}
		int divisor = player.getBoonManager().hasBoon(Woodsman.class) ? 2 : 1;
		return axe.getDefinition().getCutTime() / divisor;
	}

	private void addLog() {
		switch (definitions) {
			case WILLOW_TREE: {
				player.getDailyChallengeManager().update(SkillingChallenge.CHOP_WILLOW_LOGS);
				player.getAchievementDiaries().update(LumbridgeDiary.CHOP_WILLOWS);
				player.getAchievementDiaries().update(FaladorDiary.CHOP_BURN_WILLOW_LOGS, 1);
				break;
			}
			case TEAK_TREE: {
				if (tree.getX() == 3510 && tree.getY() == 3073) {
					player.getAchievementDiaries().update(DesertDiary.CHOP_TEAK_LOGS);
				}
				player.getAchievementDiaries().update(KaramjaDiary.CUT_A_TEAK_LOG);
				player.getAchievementDiaries().update(WesternProvincesDiary.CHOP_AND_BURN_TEAK_LOGS, 1);
				break;
			}
			case MAHOGANY_TREE: {
				player.getDailyChallengeManager().update(SkillingChallenge.CHOP_MAHOGANY_LOGS);
				player.getAchievementDiaries().update(KaramjaDiary.CUT_A_MAHOGANY_LOG);
				player.getAchievementDiaries().update(WesternProvincesDiary.CHOP_AND_BURN_MAHOGANY_LOGS, 1);
				player.getAchievementDiaries().update(MorytaniaDiary.CHOP_AND_BURN_MAHOGANY_LOGS, 1);
				player.getAchievementDiaries().update(KourendDiary.CHOP_SOME_MAHOGANY);
				break;
			}
			case YEW_TREE: {
				player.getAchievementDiaries().update(VarrockDiary.CHOP_AND_BURN_YEW_LOGS, 1);
				SherlockTask.CHOP_YEW_TREE.progress(player);
				break;
			}
			case OAK: {
				player.getAchievementDiaries().update(LumbridgeDiary.CHOP_AND_BURN_LOGS, 1);
				player.getAchievementDiaries().update(FremennikDiary.CHOP_AND_BURN_OAK_LOGS, 1);
				break;
			}
			case MAGIC_TREE: {
				player.getDailyChallengeManager().update(SkillingChallenge.CHOP_MAGIC_LOGS);
				player.getAchievementDiaries().update(LumbridgeDiary.CHOP_MAGIC_LOGS);
				player.getAchievementDiaries().update(WildernessDiary.CUT_AND_BURN_MAGIC_LOGS, 1);
				break;
			}
			case REDWOOD_TREE: {
				player.getDailyChallengeManager().update(SkillingChallenge.CHOP_REDWOOD_LOGS);
				player.getAchievementDiaries().update(KourendDiary.CHOP_REDWOODS);
				break;
			}
			case TREE: {
				if (tree.getName().equalsIgnoreCase("dying tree")) {
					player.getAchievementDiaries().update(VarrockDiary.CHOP_DOWN_DYING_TREE);
				}
				break;
			}
		}

		AdventCalendarManager.increaseChallengeProgress(player, 2022, 17, 1);
		player.getSkills().addXp(SkillConstants.WOODCUTTING, definitions.getXp());
		awardNest();
		rollOutfit();
		//Incinerate the logs
		if (axe.getDefinition() == AxeDefinitions.INFERNAL && definitions.getLogsId() != -1 && (player.hasBoon(SwissArmyMan.class) || axe.getItem().getCharges() > 0) && Utils.random(2) == 0) {
			if(axe.getSlot() != -1)
				player.getChargesManager().removeCharges(axe.getItem(), 1, axe.getContainer(), axe.getSlot());
			player.setGraphics(BURN_GFX);
			final Firemaking fm = Objects.requireNonNull(Firemaking.MAP.get(definitions.getLogsId()));
			player.sendSound(2596);
			player.getSkills().addXp(SkillConstants.FIREMAKING, fm.getXp() / 2.0F);
		} else {
			if (definitions.getLogsId() != -1) {
				if (axe.getDefinition() instanceof Degradeable && axe.getSlot() != -1)
					player.getChargesManager().removeCharges(axe.getItem(), 1, axe.getContainer(), axe.getSlot());
				player.sendFilteredMessage("You get some " + logName + ".");
				final boolean activateLumberJackPerk = player.getPerkManager().isValid(PerkWrapper.LUMBERJACK) && Utils.random(100) <= 20;
				int amount = activateLumberJackPerk ? 2 : 1;
				amount = (int) (amount * determineGatheringMultiplier(player).orElse(1.0));

				onGather(player);

				if (activateLumberJackPerk)
					player.getPerkManager().consume(PerkWrapper.LUMBERJACK);
				if (definitions.getLogsId() == 1511 && player.getEquipment().getItem(EquipmentSlot.HELMET) != null && player.getEquipment().getItem(EquipmentSlot.HELMET).getName().contains("Kandarin headgear")) {
					amount += 1;
				}
				player.getInventory().addItem(definitions.getLogsId(), amount).onFailure(remainder -> World.spawnFloorItem(remainder, player));
			}
		}
		if (player.inArea("Prifddinas"))
			CrystalShardKt.tryFindRandom(player, 80);
		ClueItemUtil.roll(player, definitions.getClueNestBaseChance(), player.getSkills().getLevel(SkillConstants.WOODCUTTING), ClueItem::getClueNest);
	}

	static Random randomizer = new Random();

	private void rollOutfit() {
		if (definitions == TreeDefinitions.REDWOOD_TREE || definitions == TreeDefinitions.SULLIUSCEP_TREE) {
			return;
		}
		int roll = (int) (this.definitions.getOutfitChance() / (1.0D + player.getDropRateBonus()));
		if(Utils.random(roll) == 1) {
			player.sendMessage(Colour.RS_RED.wrap("A piece of a lumberjack's outfit falls out of the tree."));
			player.getInventory().addOrDrop(new Item(OUTFIT_IDS.getInt(randomizer.nextInt(OUTFIT_IDS.size()))));
		}
	}

	public static IntArrayList OUTFIT_IDS = new IntArrayList(
			List.of(
				ItemId.LUMBERJACK_LEGS,
				ItemId.LUMBERJACK_BOOTS,
				ItemId.LUMBERJACK_HAT,
				ItemId.LUMBERJACK_TOP)
	);

	private void awardNest() {
		if (definitions == TreeDefinitions.REDWOOD_TREE || definitions == TreeDefinitions.SULLIUSCEP_TREE) {
			return;
		}
		final boolean isWearingWoodcuttingCape = SkillcapePerk.WOODCUTTING.isEffective(player);
		// woodcutting cape grants 10% higher chance to drop a nest, hence * 0.9
		double skillCapeReduction = isWearingWoodcuttingCape ? 0.9 : 1;
		double perkReduction = player.getBoonManager().hasBoon(Woodsman.class) ? 0.8 : 1;
		if (Utils.random((int) ((BIRD_NEST_CHANCE * skillCapeReduction) * perkReduction)) == 0) {
			final BirdNests.Nests nest = BirdNests.Nests.rollRandomNest(true);
			//Nests are uncommon and considering the afk-ness of the skill, they should remain on the ground for a longer period of time.
			World.spawnFloorItem(new Item(nest.getNestItemId()), player, 500, 0);
			player.sendMessage("<col=FF0000>A bird's nest falls out of the tree.</col>");
		}
	}

	@Override
	public void stop() {
		player.setAnimation(Animation.STOP);
	}
}

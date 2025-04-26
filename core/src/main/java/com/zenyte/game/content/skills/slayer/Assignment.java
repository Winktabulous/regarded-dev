package com.zenyte.game.content.skills.slayer;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.boons.impl.ContractKiller;
import com.zenyte.game.content.boss.cerberus.Cerberus;
import com.zenyte.game.content.boss.dagannothkings.DagannothKing;
import com.zenyte.game.content.boss.grotesqueguardians.boss.Dusk;
import com.zenyte.game.content.boss.kraken.Kraken;
import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.impl.kalphite.KalphiteQueen;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.CatacombsOfKourend;
import com.zenyte.game.world.region.area.KalphiteLair;
import com.zenyte.game.world.region.area.TaverleyDungeon;
import com.zenyte.game.world.region.area.WaterbirthDungeon;
import com.zenyte.game.world.region.area.godwars.GodwarsDungeonArea;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 7. aug 2018 : 16:44:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Assignment {
	public Assignment(final Player player, final Slayer slayer, final SlayerTask task, final String taskName, final int initialAmount, final int amount, final SlayerMaster master) {
		this(player, slayer, task, taskName, initialAmount, amount, master, null);
	}

	public Assignment(final Player player, final Slayer slayer, final SlayerTask task, final String taskName, final int initialAmount, final int amount, final SlayerMaster master, final Class<? extends RegionArea> area) {
		this.player = player;
		this.slayer = slayer;
		this.task = task;
		this.taskName = taskName;
		this.initialAmount = initialAmount;
		this.amount = amount;
		this.master = master;
		this.area = area;
		if (area != null) {
			this.areaName = area.getSimpleName();
		}
	}

	public void initialize(final Player player, final Assignment old) {
		this.player = player;
		slayer = player.getSlayer();
		task = old.task;
		initialAmount = old.initialAmount;
		amount = old.amount;
		master = old.master;
		taskName = old.taskName;
		if (taskName.equals("DAGANNOTH_SUPREME") || taskName.equals("DAGANNOTH_REX") || taskName.equals("DAGANNOTH_PRIME")) {
			taskName = "DAGANNOTH_KINGS";
		}
		task = getTask(taskName);
		areaName = old.areaName;
		if (areaName != null) {
			if (areaName.equalsIgnoreCase("KalphiteQueenLair")) {
				areaName = "KalphiteLair";
			}
			if (task instanceof RegularTask && master != null) {
				final RegularTask regularTask = (RegularTask) task;
				final Task taskSet = regularTask.getCertainTaskSet(master);
				for (final Class<? extends RegionArea> area : taskSet.getAreas()) {
					if (area.getSimpleName().equals(areaName)) {
						this.area = area;
						break;
					}
				}
			}
		}
	}

	public static final SlayerTask getTask(final String name) {
		for (final RegularTask regularTask : RegularTask.VALUES) {
			if (regularTask.name().equals(name)) {
				return regularTask;
			}
		}
		for (final BossTask bossTask : BossTask.VALUES) {
			if (bossTask.name().equals(name)) {
				return bossTask;
			}
		}
		for (final BossTask bossTask : BossTask.VALUES) {
			if (bossTask.name().equals(name)) {
				return bossTask;
			}
		}
		for (final BossTaskSumona bossTask : BossTaskSumona.VALUES) {
			if (bossTask.name().equals(name)) {
				return bossTask;
			}
		}
		throw new IllegalStateException("Task not found: " + name);
	}

	private static final DiaryReward[] DIARY_REWARDS = {DiaryReward.MORYTANIA_LEGS4, DiaryReward.MORYTANIA_LEGS3, DiaryReward.MORYTANIA_LEGS2, DiaryReward.MORYTANIA_LEGS1};
	protected transient Player player;
	protected transient Slayer slayer;
	protected transient SlayerTask task;
	protected String taskName;
	protected int initialAmount;
	protected int amount;
	protected String areaName;
	private transient Class<? extends RegionArea> area;
	private SlayerMaster master;

	public Assignment() {
	}

	public void checkAssignment(@NotNull final String name, final NPC npc) {
		if (!task.validate(name, npc)) {
			return;
		}
		if (master.equals(SlayerMaster.KRYSTILIA)) {
			if (!WildernessArea.isWithinWilderness(player)) {
				player.sendFilteredMessage("Only kills done within Wilderness will count towards Krystilia's slayer " +
                        "assignment.");
				return;
			}
		} else if (master.equals(SlayerMaster.KONAR_QUO_MATEN) && area != null) {
			if (!player.inArea(area) && !checkExceptions(npc, area)) {
				player.sendMessage("Konar quo Maten requires you to complete your slayer task in the " + GlobalAreaManager.getArea(area) + ".");
				return;
			}
		}
		float experience = task.getExperience(npc);
		if (player.inArea("Slayer Tower")) {
			for (int index = 0; index < DIARY_REWARDS.length; index++) {
				final DiaryReward reward = DIARY_REWARDS[index];
				if (DiaryUtil.eligibleFor(reward, player)) {
					experience *= 1 + (0.025 * (index + 1));
					break;
				}
			}
		}

		if (CombatUtilities.dragonSlayerGlovesEquipped(player) && CombatUtilities.isDraconic(npc)) {
			experience *= 1.25;
		}

		player.getSkills().addXp(SkillConstants.SLAYER, experience);
		final int braceletId = player.getEquipment().getId(EquipmentSlot.HANDS);
		if (braceletId == ItemId.BRACELET_OF_SLAUGHTER || braceletId == ItemId.EXPEDITIOUS_BRACELET) {
			int rate = player.hasBoon(ContractKiller.class) ? 35 : 25;
			if (Utils.random(99) <= rate) {
				if (task != RegularTask.TZTOK_JAD && task != RegularTask.TZKAL_ZUK) {
					if (braceletId == ItemId.BRACELET_OF_SLAUGHTER) {
						int slaughterUses = player.getNumericAttribute("bracelet of slaughter uses").intValue() + 1;
						final int chargesLeft = (30 - slaughterUses);
						if (chargesLeft == 0) {
							if (player.getCombatAchievements().hasTierCompleted(CATierType.ELITE) && Utils.random(9) == 0) {
								slaughterUses = 0;
								player.sendMessage("Your bracelet of slaughter prevents your slayer count decreasing. <col=FF0000>It has recharged completely.");
							} else {
								player.sendMessage("Your bracelet of slaughter prevents your slayer count decreasing. <col=FF0000>It then crumbles to dust.");
							}
						} else if (chargesLeft == 1) {
							player.sendMessage("Your bracelet of slaughter prevents your slayer count decreasing. <col=FF0000>It has one charge left.");
						} else {
							if(!player.hasBoon(ContractKiller.class))
								player.sendFilteredMessage("Your bracelet of slaughter prevents your slayer count decreasing. It has " + chargesLeft + " charges left.");
							else player.sendFilteredMessage("Your bracelet of slaughter prevents your slayer count decreasing.");
						}
						if(!player.hasBoon(ContractKiller.class))
							player.addAttribute("bracelet of slaughter uses", slaughterUses % 30);
						if (slaughterUses >= 30) {
							player.getEquipment().set(EquipmentSlot.HANDS, null);
						}
					} else {
						int expeditiousBraceletUses = player.getNumericAttribute("expeditious bracelet uses").intValue() + 1;
						final int chargesLeft = (30 - expeditiousBraceletUses);
						if (chargesLeft == 0) {
							if (player.getCombatAchievements().hasTierCompleted(CATierType.ELITE) && Utils.random(9) == 0) {
								expeditiousBraceletUses = 0;
								player.sendMessage("Your expeditious bracelet helps you progress your slayer task faster. <col=FF0000>It has recharged completely.");
							} else {
								player.sendMessage("Your expeditious bracelet helps you progress your slayer task faster. <col=FF0000>It then crumbles to dust.");
							}
						} else if (chargesLeft == 1) {
							player.sendMessage("Your expeditious bracelet helps you progress your slayer task faster. <col=FF0000>It has one charge left.");
						} else {
							if(!player.hasBoon(ContractKiller.class))
								player.sendFilteredMessage("Your expeditious bracelet helps you progress your slayer task faster. It has " + chargesLeft + " charges left.");
							else player.sendFilteredMessage("Your expeditious bracelet helps you progress your slayer task faster.");
						}
						if(!player.hasBoon(ContractKiller.class))
							player.addAttribute("expeditious bracelet uses", expeditiousBraceletUses % 30);
						if (expeditiousBraceletUses >= 30) {
							player.getEquipment().set(EquipmentSlot.HANDS, null);
						}
						amount = Math.max(0, amount - 2);
						finish();
					}
					return;
				}
			}
		}
		if ((task == RegularTask.TZTOK_JAD && !name.equalsIgnoreCase("TzTok-Jad")) || (task == RegularTask.TZKAL_ZUK && !name.equalsIgnoreCase("TzKal-Zuk"))) {
			return;
		}
		amount--;
		finish();
	}

	public void finish() {
		final int braceletId = player.getEquipment().getId(EquipmentSlot.HANDS);
		if (amount <= 0) {
			slayer.finishAssignment();
		} else if (amount % 10 == 0) {
			if (braceletId == 11972 || braceletId == 11974 || braceletId == 11118 || braceletId == 11120 || braceletId == 11122 || braceletId == 11124) {
				player.sendMessage("You still need to kill " + amount + " x " + task.getTaskName() + " to complete your current Slayer assignment");
			}
		}
	}

	public boolean checkExceptions(final NPC npc, final Class<? extends RegionArea> area) {
		if (npc instanceof AlchemicalHydra && (task.equals(RegularTask.HYDRAS) || task.equals(BossTaskSumona.ALCHEMICAL_HYDRA_SUMONA))) {
			return true;
		}
		//Only the Taverley dungeon area task will work.
		if (npc instanceof Cerberus && task.equals(RegularTask.HELLHOUNDS)) {
			return area == TaverleyDungeon.class;
		}
		//Only the Kalphite lair works, not the slayer-only cave.
		if (npc instanceof KalphiteQueen && task.equals(RegularTask.KALPHITE)) {
			return area == KalphiteLair.class;
		}
		//Only the Waterbirth dungeon will work.
		if (npc instanceof DagannothKing && task.equals(RegularTask.DAGANNOTH)) {
			return area == WaterbirthDungeon.class;
		}
		if (npc instanceof Dusk && task.equals(RegularTask.GARGOYLES)) {
			return true;
		}
		if (npc instanceof Kraken && task.equals(RegularTask.CAVE_KRAKEN)) {
			return true;
		}
		if (npc.getId() == NpcId.THERMONUCLEAR_SMOKE_DEVIL && task.equals(RegularTask.SMOKE_DEVILS)) {
			return true;
		}
		//Only the Catacombs of Kourend work.
		if (npc.getId() == NpcId.SKOTIZO && (task.equals(RegularTask.BLACK_DEMONS) || task.equals(RegularTask.GREATER_DEMONS))) {
			return area == CatacombsOfKourend.class;
		}
		if (area == GodwarsDungeonArea.class) {
			return player.getArea() instanceof GodwarsInstance;
		}
		return false;
	}

	public boolean isValid(final Player player, final NPC npc) {
		if (task.validate(npc.getName(player), npc)) {
			if (master.equals(SlayerMaster.KONAR_QUO_MATEN) && area != null) {
				return player.inArea(area) || checkExceptions(npc, area);
			}
			return true;
		}
		return false;
	}

	public SlayerTask getTask() {
		return task;
	}

	public Class<? extends RegionArea> getArea() {
		return area;
	}

	public String getTaskName() {
		return taskName;
	}

	public int getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(int initialAmount) {
		this.initialAmount = initialAmount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public SlayerMaster getMaster() {
		return master;
	}

	public String getAreaName() {
		return areaName;
	}
}

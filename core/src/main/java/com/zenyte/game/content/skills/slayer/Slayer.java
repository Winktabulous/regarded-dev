package com.zenyte.game.content.skills.slayer;

import com.google.common.base.Preconditions;
import com.near_reality.game.world.PlayerEvent.SlayerTaskCompleted;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.*;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.CatacombsOfKourend;
import com.zenyte.game.world.region.area.Keldagrim;
import com.zenyte.plugins.Listener;
import com.zenyte.plugins.ListenerType;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.enums.Enums;
import mgi.utilities.StringFormatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Kris | 22. juuli 2018 : 15:01:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
@StaticInitializer
public class Slayer {
    private static final List<Item> SLAYER_STATUES_ITEMS = List.of(new Item(32227), new Item(32228), new Item(32229), new Item(32230));
    private static final short[] BANNED_SLOT_VARBITS = new short[] {3209, 3210, 3211, 3212, 4441, 5023};
    private static final long FULL_EXTENSION_UNLOCK_HASH;
    public static final int LUMBRIDGE_ELITE_DIARY_COMPLETED_BIT = 4538;
    private static final int POINTS_REQUIRED_FOR_BLOCKING = 100;
    private static final int POINTS_REQUIRED_FOR_CANCELLATION = 30;
    private static final int POINTS_REQUIRED_FOR_STORE_TASK = 50;
    private static final int SLAYER_POINTS_BIT = 4068;
    private static final int TASK_AMOUNT_VAR = 261;
    private static final int TASK_INDEX_VAR = 262;
    private static final int STORED_TASK_AMOUNT_VAR = 264;
    private static final int STORED_TASK_INDEX_VAR = 265;
    private static final int UNLOCK_REWARDS_FIRST_VARP = 1076;
    private static final int UNLOCK_REWARDS_SECOND_VARP = 1344;
    public static final int SLAYER_STATUES_VAR = 3811;
    private static final boolean DOUBLE_POINTS = true;
    static final boolean HALVED_QUANTITIES = true;

    static {
        long hash = 0;
        try {
            for (final Integer value : Enums.TASK_EXTENSION_ENUM.getValues().values()) {
                hash |= 1L << value;
            }
        } catch (Throwable ignored) {
            hash = -1;
        } finally {
            FULL_EXTENSION_UNLOCK_HASH = hash;
        }

        VarManager.appendPersistentVarp(SLAYER_STATUES_VAR);
    }

    private Assignment assignment;
    private Int2ObjectOpenHashMap<RegularTask> bannedTasks;
    private SlayerMaster master;
    private transient Player partner;
    private transient Player player;
    private String lastAssignmentName;
    private Assignment storedAssignment;

    public void setAssignment(final Assignment assignment) {
        this.assignment = assignment;
        lastAssignmentName = assignment.taskName;
    }

    public Slayer(final Player player) {
        this.player = player;
        bannedTasks = new Int2ObjectOpenHashMap<>(6);
        master = SlayerMaster.TURAEL;
    }

    @Listener(type = ListenerType.LOGOUT)
    private static void onLogout(final Player player) {
        final Slayer slayer = player.getSlayer();
        if (slayer.partner != null) {
            slayer.partner.getSlayer().setPartner(null);
            slayer.partner.sendMessage("Your Slayer partner has logged out.");
        }
    }

    void addSlayerPoints(int amount) {
        final int currentPoints = getSlayerPoints();
        final int modifiedPoints = currentPoints + amount;
        player.addAttribute("slayer_points", Math.max(0, modifiedPoints));
        refreshSlayerPoints();
    }

    public void checkAssignment(final NPC npc) {
        if (assignment == null) {
            return;
        }
        assignment.checkAssignment(npc.getName(player), npc);
    }

    void confirmTaskStore() {
        if (!isUnlocked("Task Storage")) {
            player.sendMessage("You need to unlock Task Storage to store tasks.");
            return;
        }

        if (assignment == null) {
            player.sendMessage("You need a Slayer assignment to store it.");
            return;
        }

        if (storedAssignment != null) {
            player.sendMessage("You already have an assignment stored.");
            return;
        }

        final int points = getSlayerPoints();
        if (points < POINTS_REQUIRED_FOR_STORE_TASK) {
            player.sendMessage("You do not have enough Slayer Points to store your task. You need " + POINTS_REQUIRED_FOR_STORE_TASK + " Slayer Points.");
            return;
        }

        final SlayerMaster master = assignment.getMaster();
        if (master.equals(SlayerMaster.KRYSTILIA)) {
            player.sendMessage("You can't store a Wilderness task.");
            return;
        }

        addSlayerPoints(-POINTS_REQUIRED_FOR_STORE_TASK);
        storedAssignment = new Assignment();
        storedAssignment.initialize(player, assignment);
        assignment = null;
        refreshCurrentAssignment();
    }

    void confirmTaskUnstore() {
        if (!isUnlocked("Task Storage")) {
            player.sendMessage("You need to unlock Task Storage to unstore tasks.");
            return;
        }

        if (storedAssignment == null) {
            player.sendMessage("You don't have a stored assignment.");
            return;
        }

        if (assignment != null) {
            player.sendMessage("You can't unstore an assignment if you have a task.");
            return;
        }

        setAssignment(storedAssignment);
        storedAssignment = null;
        refreshCurrentAssignment();
    }

    void confirmFullExtensionUnlock() {
        final int cost = getRemainingExtensionsCost();
        final int currentPoints = getSlayerPoints();
        if (currentPoints < cost) {
            player.sendMessage("You don't have enough Slayer Points to unlock all the extensions. You need " + cost + " Slayer Points.");
            return;
        }
        if (FULL_EXTENSION_UNLOCK_HASH == -1) {
            player.sendMessage("Something went wrong. Please contact a developer.");
            return;
        }
        setUnlocksHash(getUnlocksHash() | FULL_EXTENSION_UNLOCK_HASH);
        addSlayerPoints(-cost);
        refreshRewards();
        player.sendMessage("Congratulations, you've unlocked all the extensions.");
    }

    void confirmPurchase(final int slotId) {
        if (isUnlocked(slotId)) return;
        Optional<String> unlockNameOptional = Enums.SLAYER_PERK_REWARD_NAMES.getValue(slotId);
        if (unlockNameOptional.isEmpty()) return;
        String unlockName = unlockNameOptional.get();
        final int pointsRequired = Enums.TASK_COST_ENUM.getValue(slotId).orElseThrow(Enums.exception());
        final int currentPoints = getSlayerPoints();
        if (currentPoints < pointsRequired) {
            player.sendMessage("You do not have enough Slayer Points to purchase '" + unlockName + "'.");
            return;
        }
        if (unlockName.equalsIgnoreCase("Slayer Statues")) {
            if (!player.getInventory().containsItems(SLAYER_STATUES_ITEMS)) {
                player.sendMessage("You need all 4 statue items in your inventory to purchase '" + unlockName + "'.");
                return;
            }

            player.getVarManager().sendVar(SLAYER_STATUES_VAR, SlayerMountType.NONE.getIndex());
            for (Item item : SLAYER_STATUES_ITEMS) {
                player.getInventory().deleteItem(item);
            }
        }
        addSlayerPoints(-pointsRequired);
        setUnlocksHash(getUnlocksHash() | (1L << slotId));
        refreshRewards();
        player.sendMessage("Congratulations, you've unlocked '" + unlockName + "'.");
    }

    void confirmTaskBlock() {
        if (assignment == null) {
            player.sendMessage("You do not have a Slayer assignment right now.");
            return;
        }
        final int points = getSlayerPoints();
        if (points < POINTS_REQUIRED_FOR_BLOCKING) {
            player.sendMessage("You do not have enough Slayer Points to block your task. You need " + POINTS_REQUIRED_FOR_BLOCKING + " Slayer Points.");
            return;
        }
        final IntAVLTreeSet availableSlots = getAvailableBlockSlots();
        if (availableSlots.isEmpty()) {
            player.sendMessage("You don't have any empty slots to block this task!");
            return;
        }
        RegularTask task;
        if (assignment.getTask() instanceof BossTask) {
            task = RegularTask.BOSS;
        } else {
            task = (RegularTask) assignment.getTask();
        }
        bannedTasks.put(availableSlots.firstInt(), task);
        addSlayerPoints(-POINTS_REQUIRED_FOR_BLOCKING);
        assignment = null;
        lastAssignmentName = null;
        refreshCurrentAssignment();
        refreshBlockedTasks();
    }

    void confirmTaskCancellation() {
        if (assignment == null) {
            player.sendMessage("You do not have a Slayer assignment right now.");
            return;
        }
        final int points = getSlayerPoints();
        if (points < POINTS_REQUIRED_FOR_CANCELLATION) {
            player.sendMessage("You do not have enough Slayer Points to cancel your task. You need " + POINTS_REQUIRED_FOR_CANCELLATION + " Slayer Points.");
            return;
        }
        assignment = null;
        lastAssignmentName = null;
        addSlayerPoints(-POINTS_REQUIRED_FOR_CANCELLATION);
        player.sendMessage("Your Slayer assignment has been cancelled.");
        refreshCurrentAssignment();
    }

    public void removeTask() {
        assignment = null;
        lastAssignmentName = null;
        player.sendMessage("Your Slayer assignment has been cancelled.");
    }

    void confirmTaskUnblock(final int slotId) {
        if (!bannedTasks.containsKey(slotId)) {
            player.sendMessage("You don't have a Slayer task blocked in that slot.");
            return;
        }
        bannedTasks.remove(slotId);
        refreshBlockedTasks();
    }

    void disable(final int slotId) {
        final Optional<String> entry = Enums.SLAYER_PERK_REWARD_NAMES.getValue(slotId);
        if (!entry.isPresent()) {
            throw new RuntimeException("Incorrect task index: " + slotId);
        }
        final String name = entry.get();
        if (name.equalsIgnoreCase("Bigger and Badder")) {
            player.getSettings().toggleSetting(Setting.BIGGER_AND_BADDER_SLAYER_REWARD);
            return;
        } else if (name.equalsIgnoreCase("Stop the Wyvern")) {
            player.getSettings().toggleSetting(Setting.STOP_THE_WYVERN_SLAYER_REWARD);
            return;
        }
        if (!Enums.TASK_DISABLE_ENUM.getValue(slotId).isPresent() || !isUnlocked(slotId)) {
            return;
        }
        player.sendMessage("You've disabled the extension '" + name + "'.");
        setUnlocksHash(getUnlocksHash() & ~(1L << slotId));
        refreshRewards();
    }

    public boolean isBiggerAndBadder() {
        return isUnlocked("Bigger and Badder") && !player.getBooleanSetting(Setting.BIGGER_AND_BADDER_SLAYER_REWARD);
    }

    public void finishAssignment() {
        player.sendMessage("You have finished your Slayer assignment. Talk to " + StringFormatUtil.formatString(master.toString()) + " for a new one.");
        final SlayerMaster master = assignment.getMaster();
        final int completedTasks = player.getNumericAttribute("completed tasks").intValue() + 1;
        player.addAttribute("completed tasks", completedTasks);

        World.postEvent(new SlayerTaskCompleted(player, assignment));

        switch (master) {
            case KRYSTILIA: {
                player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_WILDERNESS_ASSIGNMENTS);
                break;
            }
            case NIEVE: {
                player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_NIEVE_ASSIGNMENTS);
                extraCoins();
                break;
            }
            case DURADEL: {
                player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_DURADEL_ASSIGNMENTS);
                extraCoins();
                break;
            }
            case KONAR_QUO_MATEN: {
                player.getInventory().addOrDrop(new Item(ItemId.BRIMSTONE_KEY));
                extraCoins();
                break;
            }
            case MAZCHNA: {
                player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_MAZCHNA_ASSIGNMENTS);
                break;
            }
        }

        final int completedInARow;
        switch (master) {
            case KRYSTILIA -> {
                completedInARow = getKrystiliaStreak() + 1;
                setKrystiliaStreak(completedInARow);
            }
            case SUMONA -> {
                completedInARow = getSumonaStreak() + 1;
                setSumonaStreak(completedInARow);
            }
            default -> {
                completedInARow = player.getNumericAttribute("completed tasks in a row").intValue() + 1;
                player.addAttribute("completed tasks in a row", completedInARow);
            }
        }

        final int multiplier = master.getMultiplier(completedInARow);
        int pointsPerTask = master.getPointsPerTask();
        if (master.equals(SlayerMaster.KONAR_QUO_MATEN) && DiaryUtil.eligibleFor(DiaryReward.RADAS_BLESSING4, player)) {
            pointsPerTask = 20;
        }
        int amount = pointsPerTask * multiplier;
        if (DOUBLE_POINTS) {
            amount *= 2;
        }
        if (player.getPerkManager().isValid(PerkWrapper.MASTER_SLAYER)) {
            amount *= 1.15;
        }
        if(World.hasBoost(XamphurBoost.SLAYER_POINTS_X2)) {
            amount *= 2;
        }
        addSlayerPoints(amount);
        player.sendMessage(Colour.RED.wrap("You've completed " + completedInARow + " task" + (completedInARow == 1 ? "" : "s") + " and received " + amount + " points, giving you a total of " + getSlayerPoints() + "; return to a Slayer master."));

        if (master.getPointsPerTask() == 0) {
            player.sendMessage(Colour.RED.wrap("Tasks assigned by Turael do not give any slayer points."));
        }
        if (assignment.getTask() instanceof BossTask) {
            player.getSkills().addXp(SkillConstants.SLAYER, 5000);
        }
        assignment = null;
    }

    private void extraCoins() {
        Item coins = new Item(995, Utils.random(50_000, 500_000));
        player.getInventory().addOrDrop(coins);
        player.sendMessage("You have been awarded with extra " + Utils.formatNumberWithCommas(coins.getAmount()) + " x " + coins.getName() + " for finishing your Slayer assignment!");
    }

    public List<RegularTask> getAvailableAssignments(final SlayerMaster master) {
        final ArrayList<RegularTask> possibleTasks = new ArrayList<RegularTask>((int) (RegularTask.VALUES.length / 2.0F));
        final int slayerLevel = player.getSkills().getLevelForXp(SkillConstants.SLAYER);
        final SlayerTask lastAssignment = lastAssignmentName == null ? null : Assignment.getTask(lastAssignmentName);
        for (final RegularTask $task : RegularTask.VALUES) {
            //Exception as there is no separate spiritual mage in RS; Cannot delete the constant cus it'd throw errors.
            if ($task == RegularTask.SPIRITUAL_MAGE || ($task == RegularTask.SPIRITUAL_CREATURES && bannedTasks.containsValue(RegularTask.SPIRITUAL_MAGE))) {
                continue;
            }
            if ($task == RegularTask.BASILISKS && (master == SlayerMaster.KONAR_QUO_MATEN || master == SlayerMaster.DURADEL || master == SlayerMaster.NIEVE) && !player.getSlayer().isUnlocked("Basilocked")) {
                continue;
            }
            if (bannedTasks.containsValue($task) || $task == lastAssignment || slayerLevel < $task.getSlayerRequirement() || master != SlayerMaster.KRYSTILIA && this.isCheckingCombat() && player.getSkills().getCombatLevel() < $task.getCombatRequirement()) {
                continue;
            }
            final Task[] $taskSet = $task.getTaskSet();
            for (final Task $set : $taskSet) {
                final Predicate<Player> predicate = $task.getPredicate();
                if ($set.getSlayerMaster() != master || predicate != null && !predicate.test(player)) {
                    continue;
                }
                possibleTasks.add($task);
                break;
            }
        }
        return possibleTasks;
    }

    public List<BossTask> getAvailableBossAssignments() {
        final ArrayList<BossTask> possibleTasks = new ArrayList<>((int) (BossTask.VALUES.length / 2.0F));
        possibleTasks.addAll(Arrays.asList(BossTask.VALUES));
        return possibleTasks;
    }

    public Assignment getAssignment(final RegularTask task) {
        if (task == RegularTask.BOSS) {
            return generateBossTask(master);
        }
        return getAssignment(task, master);
    }

    public Assignment generateTask(final SlayerMaster master) {
        final SlayerTask lastAssignment = lastAssignmentName == null ? null : Assignment.getTask(lastAssignmentName);
        if (lastAssignment != null && SkillcapePerk.SLAYER.isEffective(player) && Utils.random(9) == 0) {
            if (lastAssignment instanceof BossTask) {
                return new Assignment(player, this, lastAssignment, lastAssignment.getEnumName(), 0, 0, master);
            }
            final RegularTask regularTask = (RegularTask) lastAssignment;
            if (!bannedTasks.containsValue(regularTask)) {
                return getAssignment(regularTask, master);
            }
        }

        int weight = 0;
        int currentWeight = 0;
        final ArrayList<RegularTask> possibleTasks = new ArrayList<>((int) (RegularTask.VALUES.length / 2.0F));
        final int slayerLevel = player.getSkills().getLevelForXp(SkillConstants.SLAYER);
        for (final RegularTask $task : RegularTask.VALUES) {
            //Exception as there is no separate spiritual mage in RS; Cannot delete the constant cus it'd throw errors.
            if ($task == RegularTask.SPIRITUAL_MAGE || ($task == RegularTask.SPIRITUAL_CREATURES && bannedTasks.containsValue(RegularTask.SPIRITUAL_MAGE))) {
                continue;
            }
            if ($task == RegularTask.BASILISKS && (master == SlayerMaster.KONAR_QUO_MATEN || master == SlayerMaster.DURADEL || master == SlayerMaster.NIEVE) && !player.getSlayer().isUnlocked("Basilocked")) {
                continue;
            }
            if ($task == RegularTask.VAMPYRE && (master == SlayerMaster.KONAR_QUO_MATEN || master == SlayerMaster.DURADEL || master == SlayerMaster.NIEVE || master == SlayerMaster.CHAELDAR) && !player.getSlayer().isUnlocked("Actual Vampyre Slayer")) {
                continue;
            }
            if (bannedTasks.containsValue($task) || lastAssignment == $task || slayerLevel < $task.getSlayerRequirement() || master != SlayerMaster.KRYSTILIA && this.isCheckingCombat() && player.getSkills().getCombatLevel() < $task.getCombatRequirement()) {
                continue;
            }
            final Predicate<Player> predicate = $task.getPredicate();
            final Task[] $taskSet = $task.getTaskSet();
            for (final Task $set : $taskSet) {
                if ($set.getSlayerMaster() != master || predicate != null && !predicate.test(player)) {
                    continue;
                }
                weight += $set.getWeight();
                possibleTasks.add($task);
                break;
            }
        }
        final int $randomTask = Utils.random(weight);
        for (int i = possibleTasks.size() - 1; i >= 0; i--) {
            final RegularTask $task = possibleTasks.get(i);
            final Task $taskSet = $task.getCertainTaskSet(master);
            if ($taskSet == null) continue;
            final int $taskWeight = $taskSet.getWeight();
            if ((currentWeight += $taskWeight) >= $randomTask) {
                if (master.equals(SlayerMaster.VANNAKA)) {
                    player.getAchievementDiaries().update(VarrockDiary.SLAYER_TASK_FROM_VANNAKA);
                } else if (master.equals(SlayerMaster.CHAELDAR)) {
                    player.getAchievementDiaries().update(LumbridgeDiary.GET_SLAYER_TASK_FROM_CHAELDAR);
                } else if (master.equals(SlayerMaster.DURADEL)) {
                    player.getAchievementDiaries().update(KaramjaDiary.SLAYER_TASK_BY_DURADEL);
                } else if (master.equals(SlayerMaster.MAZCHNA)) {
                    player.getAchievementDiaries().update(MorytaniaDiary.GET_A_SLAYER_TASK_FROM_MAZCHNA);
                }
                if ($task == RegularTask.BOSS) {
                    return generateBossTask(master);
                }
                return getAssignment($task, master);
            }
        }
        throw new RuntimeException("Unable to calculate a task for master " + master.toString() + ".");
    }

    public SlayerMaster getAdvisedMaster() {
        final Skills skills = player.getSkills();
        for (int i = SlayerMaster.values.length - 1; i >= 0; i--) {
            final SlayerMaster master = SlayerMaster.values[i];
            if (skills.getCombatLevel() >= master.getCombatRequirement() && skills.getLevelForXp(SkillConstants.SLAYER) >= master.getSlayerRequirement()) {
                return master;
            }
        }
        return SlayerMaster.TURAEL;
    }

    public int getAssigmentAmount(final RegularTask task, final Task taskSet) {
        int min = taskSet.getMinimumAmount();
        int max = taskSet.getMaximumAmount();
        final RegularTask.Range range = task.getExtendedRange();
        if (range != null) {
            if (this.isUnlocked(range.getExtensionName())) {
                min = range.getMin();
                max = range.getMax();
                return Utils.random(min, max);
            }
        }
        return Utils.random(min, max) / (HALVED_QUANTITIES ? 2 : 1);
    }

    public Assignment getAssignment(final RegularTask task, final SlayerMaster master) {
        final Task taskSet = task.getCertainTaskSet(master);
        Preconditions.checkArgument(taskSet != null);
        final int amount = getAssigmentAmount(task, taskSet);
        final Class<? extends RegionArea>[] areas = taskSet.getAreas();
        Class<? extends RegionArea> area;
        //Exception for black dragons in Kourend catacombs.
        if (task == RegularTask.BLACK_DRAGONS && areas != null && player.getSkills().getLevel(SkillConstants.SLAYER) < 77) {
            final ObjectArrayList<Class<? extends RegionArea>> areasList = new ObjectArrayList<>();
            for (final Class<? extends RegionArea> a : areas) {
                if (a != CatacombsOfKourend.class) {
                    areasList.add(a);
                }
            }
            area = Utils.random(areasList);
        } else {
            area = areas != null ? areas[Utils.randomNoPlus(areas.length)] : null;
        }
        return new Assignment(player, this, task, task.getEnumName(), amount, amount, master, area);
    }

    public int getCompletedTasks() {
        return player.getNumericAttribute("completed tasks").intValue();
    }

    public int getCurrentStreak() {
        return player.getNumericAttribute("completed tasks in a row").intValue();
    }

    public void setCurrentStreak(final int value) {
        player.addAttribute("completed tasks in a row", value);
    }

    public int getKrystiliaStreak() {
        return player.getNumericAttribute("krystilia completed tasks in a row").intValue();
    }

    public void setKrystiliaStreak(final int value) {
        player.addAttribute("krystilia completed tasks in a row", value);
    }

    public int getSumonaStreak() {
        return player.getNumericAttribute("sumona completed tasks in a row").intValue();
    }

    public void setSumonaStreak(final int value) {
        player.addAttribute("sumona completed tasks in a row", value);
    }

    public int getSlayerPoints() {
        return player.getNumericAttribute("slayer_points").intValue();
    }

    public Assignment getTzTokJadAssignment(final SlayerMaster master) {
        final int amount = player.getCombatAchievements().hasTierCompleted(CATierType.GRANDMASTER) ? 3 : 1;
        return new Assignment(player, this, RegularTask.TZTOK_JAD, RegularTask.TZTOK_JAD.getEnumName(), amount, amount, master);
    }

    public Assignment getTzKalZukAssignment(final SlayerMaster master) {
        final int amount = player.getCombatAchievements().hasTierCompleted(CATierType.GRANDMASTER) ? 3 : 1;
        return new Assignment(player, this, RegularTask.TZKAL_ZUK, RegularTask.TZKAL_ZUK.getEnumName(), amount, amount, master);
    }

    public void initialize(final Player player, final Player parser) {
        this.player = player;
        bannedTasks = parser.getSlayer().bannedTasks;
        master = parser.getSlayer().master;
        if (parser.getSlayer().assignment != null) {
            assignment = new Assignment();
            assignment.initialize(player, parser.getSlayer().assignment);
        }
        if (parser.getSlayer().storedAssignment != null) {
            storedAssignment = new Assignment();
            storedAssignment.initialize(player, parser.getSlayer().storedAssignment);
        }
    }

    public boolean isAssignable(final SlayerTask task, final SlayerMaster master) {
        final Skills skills = player.getSkills();
        if (skills.getLevel(SkillConstants.SLAYER) < master.getSlayerRequirement() || skills.getCombatLevel() < master.getCombatRequirement()) {
            return false;
        }
        final Predicate<Player> predicate = task.getPredicate();
        return predicate == null || predicate.test(player);
    }

    public boolean isCheckingCombat() {
        return player.getBooleanAttribute("checking combat in slayer");
    }

    public void setCheckingCombat(final boolean value) {
        player.addAttribute("checking combat in slayer", value ? 1 : 0);
    }

    public boolean sumonaAssignWildernessTasks() {
        return player.getBooleanAttribute("sumona wildy tasks");
    }

    public void setSumonaAssignWildernessTasks(final boolean value) {
        player.addAttribute("sumona wildy tasks", value ? 1 : 0);
    }

    public boolean isCurrentAssignment(final Entity target) {
        if (assignment == null || assignment.getAmount() == 0 || !(target instanceof NPC)) {
            return false;
        }
        return assignment.isValid(player, (NPC) target);
    }

    public boolean isUnlocked(final int index) {
        return ((getUnlocksHash() >> index) & 1) == 1;
    }

    public boolean isUnlocked(final String name) {
        return isUnlocked(Enums.SLAYER_PERK_REWARD_NAMES.getKey(name.toLowerCase()).orElseThrow(Enums.exception()));
    }

    public void openInterface() {
        GameInterface.SLAYER_REWARDS.open(player);
    }

    public void refreshPartnerInterface() {
        final PacketDispatcher dispatch = player.getPacketDispatcher();
        if (partner == null) {
            dispatch.sendClientScript(746, "New partner");
            dispatch.sendComponentText(68, 4, "Current partner: <col=ff0000>(none)</col>");
            dispatch.sendComponentText(68, 5, "Use the button to set yourself a Slayer Partner.<br><br>If your " +
                    "partner's Slayer level is <col=ffffff>as high as yours</col>, whenever a task is " +
                    "<col=ffffff>assigned to them</col>, you'll receive the same task, as long as you are eligible " +
                    "for it.<br><br>If your Slayer level is <col=ffffff>as high as your partner's</col>, whenever a " +
                    "task is <col=ffffff>assigned to you</col>, they'll receive the same task, as long as they are " +
                    "eligible for it.");
        } else {
            dispatch.sendClientScript(746, "Dismiss partner");
            dispatch.sendComponentText(68, 4, "Current partner: <col=ffffff>" + partner.getName() + "</col> (" + (partner.getSkills().getLevelForXp(SkillConstants.SLAYER) + ")"));
            final int playerLevel = player.getSkills().getLevelForXp(SkillConstants.SLAYER);
            final int partnerLevel = partner.getSkills().getLevelForXp(SkillConstants.SLAYER);
            final int difference = Integer.compare(playerLevel, partnerLevel);
            switch (difference) {
            case -1:
                dispatch.sendComponentText(68, 5, "Your slayer level is <col=ffffff>lower</col> than your partner's" +
                        ".<br><br>When a new task is assigned <col=ffffff>to your partner</col>, if you " +
                        "are<br>eligible for it, and have not blocked it, you will receive<br>it too.<br><br>Your " +
                        "partner will not receive copies of tasks that are<br>assigned <col=ffffff>to you</col> " +
                        "because their Slayer level is higher.");
                return;
            case 0:
                dispatch.sendComponentText(68, 5, "Your slayer level is <col=ffffff>the same</col> as your partner's" +
                        ".<br><br>When a new task is assigned <col=ffffff>to either of you</col>, if you are both " +
                        "eligible for it, and have not blocked it, you will both receive it.");
                return;
            case 1:
                dispatch.sendComponentText(68, 5, "Your slayer level is <col=ffffff>higher</col> than your partner's" +
                        ".<br><br>When a new task is assigned <col=ffffff>to you</col>, if your partner " +
                        "is<br>eligible for it, and has not blocked it, you will receive<br>it too.<br><br>You will " +
                        "not receive copies of tasks that are assigned <col=ffffff>to your partner</col> because your" +
                        " Slayer level is higher.");
            }
        }
    }

    public void sendTaskInformation() {
        if (assignment == null) {
            player.sendMessage("You need something new to hunt.");
            return;
        }
        final Class<? extends RegionArea> clazz = assignment.getArea();
        RegionArea area = null;
        if (clazz != null) {
            area = GlobalAreaManager.getArea(clazz);
        }
        if (assignment.getMaster().equals(SlayerMaster.KONAR_QUO_MATEN) && area != null) {
            if (area.equals(GlobalAreaManager.getArea(Keldagrim.class))) {
                player.sendMessage("You're assigned to kill " + assignment.getTask().toString() + " in " + area.name() + "; only " + assignment.getAmount() + " more to go.");
            } else {
                player.sendMessage("You're assigned to kill " + assignment.getTask().toString() + " in the " + area.name() + "; only " + assignment.getAmount() + " more to go.");
            }
        } else {
            player.sendMessage("You're assigned to kill " + assignment.getTask().toString() + "; only " + assignment.getAmount() + " more to go.");
        }
    }

    public void setSlayerPoints(final int amount, final boolean refresh) {
        player.addAttribute("slayer_points", amount);
        if (refresh) {
            refreshSlayerPoints();
        }
    }

    public Assignment generateSummonaTask() {
        final ArrayList<BossTaskSumona> possibleTasks = new ArrayList<>(BossTaskSumona.VALUES.length);
        final SlayerTask lastAssignment = lastAssignmentName == null ? null : Assignment.getTask(lastAssignmentName);
        for (final BossTaskSumona $task : BossTaskSumona.VALUES) {
            if (!sumonaAssignWildernessTasks() && $task.isWildernessTask() || !$task.getPredicate().test(player) || lastAssignment == $task) {
                continue;
            }
            possibleTasks.add($task);
        }
        if (possibleTasks.isEmpty()) {
            throw new RuntimeException("Unable to assign any boss task for Sumona.");
        }
        final BossTaskSumona $task = possibleTasks.get(Utils.randomNoPlus(possibleTasks.size()));
        final int min;
        final int max;
        switch ($task) {
            case JAD_SUMONA, INFERNO_SUMONA -> {
                min = 1;
                max = 1;
            }
            case OLM_SUMONA, TOB_SUMONA -> {
                min = 2;
                max = 6;
            }
            case BARROWS_SUMONA -> {
                min = 15;
                max = 65;
            }
            default -> {
                min = 10;
                max = 35;
            }
        }

        final int amount = Utils.random(min, max);
        return new Assignment(player, this, $task, $task.getEnumName(), amount, amount, SlayerMaster.SUMONA);
    }

    public Assignment generateSpecificBossTask(final SlayerMaster master, BossTask task) {
        return new Assignment(player, this, task, task.getEnumName(), 0, 0, master);
    }

    private Assignment generateBossTask(final SlayerMaster master) {
        final ArrayList<BossTask> possibleTasks = new ArrayList<>(BossTask.VALUES.length);
        final SlayerTask lastAssignment = lastAssignmentName == null ? null : Assignment.getTask(lastAssignmentName);
        for (final BossTask $task : BossTask.VALUES) {
            if (master == SlayerMaster.KRYSTILIA && !$task.isAssignableByKrystilia() || !$task.getPredicate().test(player) || lastAssignment == $task) {
                continue;
            }
            possibleTasks.add($task);
        }
        if (possibleTasks.isEmpty()) {
            throw new RuntimeException("Unable to assign any boss task for " + master.toString() + ".");
        }
        final BossTask $task = possibleTasks.get(Utils.randomNoPlus(possibleTasks.size()));
        return new Assignment(player, this, $task, $task.getEnumName(), 0, 0, master);
    }

    private IntAVLTreeSet getAvailableBlockSlots() {
        final IntAVLTreeSet list = new IntAVLTreeSet();
        final int questPoints = player.getQuestPoints();
        for (int i = 0; i < 5; i++) {
            if (questPoints >= ((i + 1) * 50)) {
                list.add(i);
                continue;
            }
            break;
        }
        if (player.getVarManager().getBitValue(LUMBRIDGE_ELITE_DIARY_COMPLETED_BIT) == 1) {
            list.add(5);
        }
        list.removeAll(bannedTasks.keySet());
        return list;
    }

    private int getRemainingExtensionsCost() {
        int cost = 0;
        for (final Integer value : Enums.TASK_EXTENSION_ENUM.getValues().values()) {
            if (isUnlocked(value)) {
                continue;
            }
            cost += Enums.TASK_COST_ENUM.getValue(value).orElseThrow(Enums.exception());
        }
        return (int) (cost * 0.95F);
    }

    private long getUnlocksHash() {
        return player.getNumericAttribute("slayer_unlocked_settings_hash").longValue();
    }

    private void setUnlocksHash(final long value) {
        player.addAttribute("slayer_unlocked_settings_hash", value);
    }

    void refreshBlockedTasks() {
        final VarManager varManager = player.getVarManager();
        for (int i = 0; i < BANNED_SLOT_VARBITS.length; i++) {
            final RegularTask task = bannedTasks.get(i);
            varManager.sendBit(BANNED_SLOT_VARBITS[i], task == null ? 0 : task.getTaskId());
        }
    }

    void refreshCurrentAssignment() {
        final VarManager varManager = player.getVarManager();
        if (assignment == null) {
            varManager.sendVar(TASK_AMOUNT_VAR, 0);
            varManager.sendVar(TASK_INDEX_VAR, 0);
        } else {
            varManager.sendVar(TASK_AMOUNT_VAR, assignment.getAmount());
            varManager.sendVar(TASK_INDEX_VAR, assignment.getTask().getTaskId());
        }

        if (storedAssignment == null) {
            varManager.sendVar(STORED_TASK_AMOUNT_VAR, 0);
            varManager.sendVar(STORED_TASK_INDEX_VAR, 0);
        } else {
            varManager.sendVar(STORED_TASK_AMOUNT_VAR, storedAssignment.getAmount());
            varManager.sendVar(STORED_TASK_INDEX_VAR, storedAssignment.getTask().getTaskId());
        }
    }

    void refreshRewards() {
        final VarManager varManager = player.getVarManager();
        final long hash = getUnlocksHash();
        varManager.sendVar(UNLOCK_REWARDS_FIRST_VARP, (int) (hash & 4294967295L));
        varManager.sendVar(UNLOCK_REWARDS_SECOND_VARP, (int) ((hash >> 32) & 4294967295L));
    }

    public void refreshSlayerPoints() {
        player.getVarManager().sendBit(SLAYER_POINTS_BIT, getSlayerPoints());
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public SlayerMaster getMaster() {
        return master;
    }

    public void setMaster(SlayerMaster master) {
        this.master = master;
    }

    public Player getPartner() {
        return partner;
    }

    public void setPartner(Player partner) {
        this.partner = partner;
    }

    public String getLastAssignmentName() {
        return lastAssignmentName;
    }
}

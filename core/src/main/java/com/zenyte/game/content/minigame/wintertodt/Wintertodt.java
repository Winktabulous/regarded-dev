package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.plugins.item.LightSourceItem;
import com.zenyte.plugins.item.TomeOfFire;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import mgi.utilities.StringFormatUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Corey
 * @since 12:05 - 22/07/2019
 */
public class Wintertodt {
    private static boolean disabled;
    static final int SNOWFALL_OBJECT = 26690;
    private static final IntSet warmClothing = new IntArraySet(new int[] {
        // santa outfit
        12887, 12888, 12889, 12890, 12891,
        // anti-santa
        12892, 12893, 12894, 12895, 12896,
        // bunny outfit
        1037, 13182, 13663, 13664, 13665,
        // clue hunter
        19689, 19691, 19693, 19695, 19697,
        // hunter gear
        10065, 10067, 10053, 1055, 10061, 10063, 10043, 10041, 10045, 10049, 10047, 10051, 10037, 10035, 10039,
        // bomber costume
        9944, 9945, 9946,
        // yak-hide
        10822, 10824,
        // pyro gear
        RewardCrate.PYROMANCER_HOOD, RewardCrate.PYROMANCER_GARB, RewardCrate.PYROMANCER_ROBE, RewardCrate.PYROMANCER_BOOTS, RewardCrate.WARM_GLOVES,
        // chicken costume
        11021, 11020, 11022, 11019,
        // evil chicken costume
        20439, 20436, 20442, 20433,
        // misc headslot
        1050, 13343, 4502, 5537, 10941,
        // misc neck slot
        6859, 6861, 6863, 6856, 9470, 21314,
        // fremmy gloves
        3799,
        // misc cape slot
        9804, 9805, 6570, 13329, 21295, 21285, 6568,
        // misc weapon slot
        1387, 1393, 3053, 21198, 11787, 12795, 11998, 1401, 3054, 21200, 11789, 12796, 12000, 13241, 13242, 12773, 20056, LightSourceItem.LightSource.BRUMA_TORCH.getLitId(),
        // misc shield slot
        TomeOfFire.TOME_OF_FIRE, TomeOfFire.TOME_OF_FIRE_EMPTY, 7053
    });

    private static final RSPolygon safeArea = new RSPolygon(new int[][] {{1624, 3988}, {1625, 3987}, {1625, 3978}, {1628, 3975}, {1628, 3971}, {1625, 3968}, {1636, 3968}, {1633, 3971}, {1633, 3975}, {1636, 3978}, {1636, 3987}, {1637, 3988}}, 0);
    private static final int MAX_ENERGY = 3500;
    private static final String WINTERTODT_POINTS_ATTRIBUTE_KEY = "wintertodt_points";
    private static final Set<Item> itemsToDeleteOnExit = new HashSet<>();
    private static double currentEnergy = MAX_ENERGY;
    private static int roundTimer = 0;
    private static boolean pyromancersFallen;
    private static boolean allBraziersBroken;

    static {
        for (final Integer potion : RejuvenationPotion.Potion.items) {
            itemsToDeleteOnExit.add(new Item(potion, 28));
        }
        itemsToDeleteOnExit.add(new Item(RejuvenationPotion.BRUMA_HERB, 28));
        itemsToDeleteOnExit.add(new Item(BrumaRoot.ROOT, 28));
        itemsToDeleteOnExit.add(new Item(BrumaRoot.KINDLING, 28));
    }

    private static int getCurrentEnergy() {
        return (int) currentEnergy;
    }

    static boolean atFullEnergy() {
        return currentEnergy >= MAX_ENERGY;
    }

    static boolean insideSafeArea(final Player player) {
        return safeArea.contains(player.getLocation());
    }

    static int getCurrentEnergyPercentage() {
        return (int) ((currentEnergy * 100) / MAX_ENERGY);
    }

    static int getAmountOfPlayers() {
        return GlobalAreaManager.get("Wintertodt").getPlayers().size();
    }

    static int getRoundTimer() {
        return roundTimer;
    }

    private static void setRoundTimer(final int ticks) {
        roundTimer = ticks;
    }

    public static void setPyromancersFallen(boolean fallen) { pyromancersFallen = fallen; }

    static boolean betweenRounds() {
        // TODO energy is 0 between rounds
        return roundTimer != 0;
    }

    static void tickRoundTimer() {
        if (roundTimer == 0) {
            return;
        }
        roundTimer--;
        if (roundTimer == 0) {
            // round wait time has finished
            setEnergyToMax();
            refreshOverlayForAllPlayers();
        }
    }

    private static int getPoints(final Player player) {
        return player.getNumericTemporaryAttributeOrDefault(WINTERTODT_POINTS_ATTRIBUTE_KEY, 0).intValue();
    }

    static void addPoints(final Player player, final int pointAmount) {
        if (Wintertodt.betweenRounds()) {
            return;
        }
        final int initialPoints = getPoints(player);
        final int points = initialPoints + pointAmount;
        player.getTemporaryAttributes().put(WINTERTODT_POINTS_ATTRIBUTE_KEY, points);
        if (initialPoints < 500 && points >= 500) {
            player.sendMessage("You have helped enough to earn a supply crate.");
        }
        refreshOverlay(player);
    }

    private static void resetPoints(final Player player, final boolean refresh) {
        player.getTemporaryAttributes().remove(WINTERTODT_POINTS_ATTRIBUTE_KEY);
        if (refresh) {
            refreshOverlay(player);
        }
    }

    static void refreshOverlayForAllPlayers() {
        for (final Player player : GlobalAreaManager.get("Wintertodt").getPlayers()) {
            refreshOverlay(player);
        }
    }

    private static void refreshOverlay(final Player player) {
        refreshOverlay(player, false);
    }

    static void refreshOverlay(final Player player, final boolean forceTimerUpdate) {
        player.getPacketDispatcher().sendClientScript(1421, getPoints(player), Wintertodt.getCurrentEnergy(), Corner.SOUTH_WEST.pyromancer.isHealthy() ? 1 : 0, Corner.NORTH_WEST.pyromancer.isHealthy() ? 1 : 0, Corner.NORTH_EAST.pyromancer.isHealthy() ? 1 : 0, Corner.SOUTH_EAST.pyromancer.isHealthy() ? 1 : 0, Corner.SOUTH_WEST.brazier.getState().getCs2Value(), Corner.NORTH_WEST.brazier.getState().getCs2Value(), Corner.NORTH_EAST.brazier.getState().getCs2Value(), Corner.SOUTH_EAST.brazier.getState().getCs2Value());
        if (betweenRounds() || forceTimerUpdate) {
            player.getVarManager().sendBit(7980, Wintertodt.getRoundTimer());
        }
    }

    private static void finishRound() {
        for (final Player player : GlobalAreaManager.get("Wintertodt").getPlayers()) {
            final int points = getPoints(player);
            player.getAchievementDiaries().update(KourendDiary.SUBDUE_WINTERTODT);
            player.getInventory().deleteItems(new Item(RejuvenationPotion.BRUMA_HERB, 28), new Item(BrumaRoot.ROOT, 28), new Item(BrumaRoot.KINDLING, 28));
            player.getActionManager().forceStop();
            if (points < 500) {
                player.sendMessage("You did not earn enough points to be worthy of a gift from the citizens of Kourend this time.");
            } else {
                final int rewardExp = player.getSkills().getLevelForXp(SkillConstants.FIREMAKING) * 100;
                final Item crate = generateSupplyCrate(player, points);
                player.getSkills().addXp(SkillConstants.FIREMAKING, rewardExp, false);
                player.sendMessage("You have gained " + StringFormatUtil.format(rewardExp * player.getSkillingXPRate()) + " Firemaking XP.");
                player.getNotificationSettings().increaseKill("Wintertodt");
                final int kills = player.getNotificationSettings().getKillcount("Wintertodt");
                player.sendMessage("Your subdued Wintertodt count is: " + Colour.RED.wrap(kills + "") + ".");
                player.getCombatAchievements().checkKcTask("Wintertodt", 5, CAType.WINTERTODT_NOVICE);
                player.getCombatAchievements().checkKcTask("Wintertodt", 10, CAType.WINTERTODT_CHAMPION);
                if (getNumberOfWarmClothingWorn(player) >= 4) {
                    player.getCombatAchievements().complete(CAType.COSY);
                }
                if (!pyromancersFallen) {
                    player.getCombatAchievements().complete(CAType.LEAVING_NO_ONE_BEHIND);
                }
                if (!allBraziersBroken) {
                    player.getCombatAchievements().complete(CAType.CAN_WE_FIX_IT);
                }
                if (points >= 3000) {
                    player.getCombatAchievements().complete(CAType.WHY_FLETCH);
                }
                player.getCombatAchievements().complete(CAType.LEAVING_NO_ONE_BEHIND);
                player.getCombatAchievements().complete(CAType.CAN_WE_FIX_IT);
                player.getInventory().addOrDrop(crate);
                player.sendMessage("You have gained a supply crate!");
            }
            final int highestPoints = player.getNumericAttribute("wintertodt_highest_points").intValue();
            player.addAttribute("wintertodt_highest_points", Math.max(highestPoints, points));
            final int accumulatedPoints = player.getNumericAttribute("wintertodt_total_accumulated_points").intValue();
            player.addAttribute("wintertodt_total_accumulated_points", accumulatedPoints + points);
            resetPoints(player, false);
            player.getTemporaryAttributes().remove("lighting_wintertodt_brazier");
            player.getTemporaryAttributes().remove("fixing_wintertodt_brazier");
        }
        resetWintertodt();
        setRoundTimer(Utils.random(80, 240));
        refreshOverlayForAllPlayers();
        for (Corner corner : Corner.VALUES) {
            corner.getPyromancer().setForceTalk(new ForceTalk("We can rest for a time."));
        }
    }

    static Item generateSupplyCrate(final Player player, final int points) {
        final int pointsPerRoll = 500;
        final Item crate = new Item(RewardCrate.SUPPLY_CRATE, 1);
        int rolls = (points + pointsPerRoll) / pointsPerRoll; // base rolls is always 2 for 500+ points
        rolls++; // add another extra roll, we're an rsps
        if (player.getMemberRank().equalToOrGreaterThan(MemberRank.EXPANSION)) {
            // emerald+ members get an extra roll
            rolls++;
        }
        if (player.getMemberRank().equalToOrGreaterThan(MemberRank.LEGENDARY)) {
            // dragonstone+ members get another extra roll
            rolls++;
        }
        /*
            For every 5 points past 500 points player gets 1% extra chance to get additional loot. For example:
            505 points = 2 rolls + 1% chance of an extra roll
            750 points = 2 rolls + 50% chance of an extra roll
            1200 points = 3 rolls + 40% chance of an extra roll
         */
        final int remainder = points % pointsPerRoll;
        final int chanceForExtraRoll = (remainder * 100) / pointsPerRoll;
        if (Utils.random(100) <= chanceForExtraRoll) {
            rolls++;
        }
        crate.setAttribute("rolls", Math.min(rolls, 28)); // max of 28 rolls per crate
        return crate;
    }

    static void dealDamage(final double amount) {
        final double previousEnergy = currentEnergy;
        final double potentialEnergy = currentEnergy - amount;
        if (potentialEnergy < 0) {
            currentEnergy = 0; // don't go below zero
        } else if (potentialEnergy > MAX_ENERGY) {
            currentEnergy = MAX_ENERGY; // don't go above the max amount
        } else {
            currentEnergy -= amount;
        }
        if (previousEnergy != currentEnergy) {
            refreshOverlayForAllPlayers();
        }
        if (currentEnergy <= 0) {
            finishRound();
        }
    }

    static void setEnergyToMax() {
        currentEnergy = MAX_ENERGY;
    }

    static void resetWintertodt() {
        for (Corner corner : Corner.VALUES) {
            corner.getPyromancer().reset();
            corner.getPyromancer().setDirection(corner.getPyromancer().getSpawnDirection().getDirection());
            corner.getBrazier().reset();
        }
        pyromancersFallen = false;
        allBraziersBroken = false;
        WintertodtPrisonArea.aoeAttackLocations.clear();
    }

    static void resetPlayer(final Player player) {
        player.getTemporaryAttributes().remove("lighting_wintertodt_brazier");
        player.getTemporaryAttributes().remove("fixing_wintertodt_brazier");
        player.getInventory().deleteItems(itemsToDeleteOnExit.toArray(new Item[0])); // remove all wintertodt items
        resetPoints(player, true);
    }

    static int getNumberOfWarmClothingWorn(final Player player) {
        int amount = 0;
        for (final Item item : player.getEquipment().getContainer().getItems().values()) {
            if (warmClothing.contains(item.getId())) {
                amount++;
            }
        }
        return Math.min(amount, 4); // more than 4 items of clothing has no effect
    }

    static int getNumberOfBraziersLit() {
        int amount = 0;
        for (final Wintertodt.Corner corner : Corner.VALUES) {
            if (corner.getBrazier().isLit()) {
                amount++;
            }
        }
        return amount;
    }

    static void checkAllBraziersBroken() {
        if (allBraziersBroken) {
            return;
        }
        for (final Wintertodt.Corner corner : Corner.VALUES) {
            if (!corner.getBrazier().isBroken()) {
                return;
            }
        }
        allBraziersBroken = true;
    }

    static int getBaseAOEDamageAmount(final Player player) {
        return ((10 - Wintertodt.getNumberOfWarmClothingWorn(player)) * (player.getMaxHitpoints() + 1)) / player.getSkills().getLevelForXp(SkillConstants.FIREMAKING);
    }

    public static boolean isDisabled() {
        return disabled;
    }

    public static void setDisabled(boolean disabled) {
        Wintertodt.disabled = disabled;
    }


    enum Corner {
        SOUTH_WEST(new Pyromancer(new Location(1619, 3996), Direction.NORTH_EAST), new Brazier(1620, 3997)), NORTH_WEST(new Pyromancer(new Location(1619, 4018), Direction.SOUTH_EAST), new Brazier(1620, 4015)), NORTH_EAST(new Pyromancer(new Location(1641, 4018), Direction.SOUTH_WEST), new Brazier(1638, 4015)), SOUTH_EAST(new Pyromancer(new Location(1641, 3996), Direction.NORTH_WEST), new Brazier(1638, 3997));
        public static final Corner[] VALUES = values();
        public static final IntSet BRAZIER_TILES = new IntOpenHashSet(36);
        private static final Int2ObjectOpenHashMap<Corner> PYROMANCERS = new Int2ObjectOpenHashMap<>(VALUES.length);
        private static final Int2ObjectOpenHashMap<Corner> BRAZIERS = new Int2ObjectOpenHashMap<>(VALUES.length);

        static {
            for (Corner corner : VALUES) {
                PYROMANCERS.put(corner.getPyromancer().getLocation().getPositionHash(), corner);
                BRAZIERS.put(corner.getBrazier().getY() | corner.getBrazier().getX() << 14, corner);
                final Location middle = new Location(corner.brazier.getX(), corner.brazier.getY()).transform(Direction.NORTH_EAST, 1);
                BRAZIER_TILES.add(middle.getPositionHash());
                for (Direction direction : Direction.values) {
                    BRAZIER_TILES.add(middle.transform(direction, 1).getPositionHash());
                }
            }
        }

        private final Pyromancer pyromancer;
        private final Brazier brazier;

        public static Corner getFromPyromancer(final Pyromancer pyromancer) {
            return getFromPyromancer(pyromancer.getLocation());
        }

        public static Corner getFromPyromancer(final Location location) {
            return PYROMANCERS.get(location.getPositionHash());
        }

        public static Corner getFromBrazier(final Brazier brazier) {
            return getFromBrazier(brazier.getX(), brazier.getY());
        }

        public static Corner getFromBrazier(final int x, final int y) {
            return BRAZIERS.get(y | x << 14);
        }

        Corner(Pyromancer pyromancer, Brazier brazier) {
            this.pyromancer = pyromancer;
            this.brazier = brazier;
        }

        public Pyromancer getPyromancer() {
            return pyromancer;
        }

        public Brazier getBrazier() {
            return brazier;
        }
    }
}

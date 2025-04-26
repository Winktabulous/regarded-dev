package com.zenyte.game.world.region.area.wilderness;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.consumables.ConsumableEffects;
import com.zenyte.game.model.ui.testinterfaces.GameNoticeboardInterface;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.wilderness.ChaosFanatic;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

/**
 * @author Kris | 29. mai 2018 : 01:20:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WildernessArea extends PolygonRegionArea implements
        DeathPlugin, EntityAttackPlugin, PlayerCombatPlugin,
        RandomEventRestrictionPlugin, LootBroadcastPlugin {

    public static final String NAME = "Wilderness";

    public static final int IN_WILDERNESS_VARBIT_SPECIAL_UNCLICKABLE = 8121;

    private static final RSPolygon mainlandWildernessPolygon = new RSPolygon(new int[][]{{2944, 3968}, {2944, 3681},
            {2947, 3681}, {2947, 3676}, {2944, 3676}, {2944, 3525}, {2994, 3525}, {2997, 3528}, {2998, 3535}, {2999,
            3536}, {3007, 3546}, {3023, 3546}, {3028, 3537}, {3032, 3529}, {3037, 3525}, {3392, 3525}, {3392, 3968}});
    private static final RSPolygon dungeonsWildernessPolygon = new RSPolygon(new int[][]{{2944, 9920}, {2944, 10879},
            {3470, 10879}, {3455, 9990},{3395, 9990}, {3264, 9920}, {3264, 9984}, {3200, 9984}, {3200, 9920}, {3072, 9920}, {3072,
            9984}, {3008, 9984}, {3008, 9920}});
    private static final RSPolygon minorWildernessBossesDungeons = new RSPolygon(new int[][]{{ 1600, 11524 }, { 1910, 11524 },
            { 1910, 11584 }, { 1600, 11584 }});
    public static final RSPolygon forexEnclavePolygon = new RSPolygon(new int[][]{
            {3143, 3618},
            {3138, 3618},
            {3138, 3616},
            {3131, 3617},
            {3131, 3618},
            {3126, 3618},
            {3126, 3618},
            {3126, 3622},
            {3123, 3622},
            {3123, 3633},
            {3125, 3633},
            {3125, 3640},
            {3138, 3639},
            {3138, 3647},
            {3156, 3647},
            {3156, 3640},
            {3155, 3640},
            {3155, 3636},
            {3155, 3633},
            {3156, 3626},
            {3152, 3626},
            {3152, 3623},
            {3149, 3623},
            {3149, 3620},
            {3143, 3620},
            {3143, 3618}
    });

    private static boolean rawWithinWilderness(final int x, final int y) {
        return !forexEnclavePolygon.contains(x, y)
                && (mainlandWildernessPolygon.contains(x, y)
                || dungeonsWildernessPolygon.contains(x, y)
                || minorWildernessBossesDungeons.contains(x, y));
    }

    /**
     * Checks if the coordinates are within the attackable part of the wilderness(starting from 2 few tiles north of
     * the ditch)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return whether or not the coordinates are in actual wilderness.
     */
    public static boolean isWithinWilderness(final int x, final int y) {
        return rawWithinWilderness(x, y);
    }

    public static boolean isWithinWilderness(final Position position) {
        final RegionArea area = GlobalAreaManager.getArea(position);
        final Location location = position.getPosition();
        return area == null
                ? rawWithinWilderness(location.getX(), location.getY())
                : area.isWildernessArea(location);
    }

    @Override
    public boolean isWildernessArea(Position position) {
        return true;
    }

    @Override
    public int broadcastValueThreshold() {
        return 40000;
    }

    private static void refreshKDR(final Player player) {
        player.getVarManager().sendVar(1102, player.getNumericAttribute("WildernessDeaths").intValue());
        player.getVarManager().sendVar(1103, player.getNumericAttribute("WildernessKills").intValue());
    }

    @NotNull
    public static OptionalInt getWildernessLevel(@NotNull final Location tile) {
        final int x = tile.getX();
        final int y = tile.getY();
        if (!isWithinWilderness(x, y)) {
            return OptionalInt.empty();
        }
        if (x >= 2944 && x <= 3392 && y >= 3520 && y <= 4351) {
            return OptionalInt.of(((y - 3520) >> 3) + 1);
        } else if (x >= 3008 && x <= 3071 && y >= 10112 && y <= 10175) {
            return OptionalInt.of(((y - 9920) >> 3) - 1);
        } else if (x >= 2944 && x <= 3455 && y >= 9920 && y <= 10879) {
            return OptionalInt.of(((y - 9920) >> 3) + 1);
        } else if (x >= 1725 && x <= 1919 && y >= 11520 && y <= 11583) {
            return OptionalInt.of(21); //Calvar'ion & Artio dungeons all lvl 29
        } else if (x >= 1600 && x <= 1663 && y >= 11520 && y <= 11583) {
            return OptionalInt.of(29); //Spindel dungeon all lvl 29
        }
        return OptionalInt.empty();
    }

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[]{mainlandWildernessPolygon, dungeonsWildernessPolygon, minorWildernessBossesDungeons};
    }

    @Override
    public void enter(final Player player) {
        final boolean canPvp = player.isCanPvp();
        if (!canPvp) {
            setAttackable(player);
        }
        if (player.getVariables().getTime(TickVariable.OVERLOAD) > 0) {
            player.getVariables().cancel(TickVariable.OVERLOAD);
            ConsumableEffects.resetOverload(player);
        }
        if(player.getAttributes().containsKey("DIVINE_POTION")) {
            player.getAttributes().remove("DIVINE_POTION");
            player.getVariables().cancel(TickVariable.DIVINE_SUPER_COMBAT_POTION);
            for (int i = 0; i < 23; i++) {
                player.getSkills().setLevel(i, player.getSkills().getLevelForXp(i));
            }
        }
        // enable blighted stacks
        player.getVarManager().sendBit(5963, 1);
        player.getVariables().cancel(TickVariable.BOUNTY_HUNTER_TARGET_LOSS);
        if (player.isXPDropsMultiplied() && player.isXPDropsWildyOnly()) {
            player.getVarManager().sendVar(3504, player.getCombatXPRate());
        }
    }

    @Override
    public void leave(final Player player, boolean logout) {
        setUnattackable(player);
        // disable blighted stacks
        player.getVarManager().sendBit(5963, 0);
        if (player.isXPDropsMultiplied() && player.isXPDropsWildyOnly()) {
            player.getVarManager().sendVar(3504, 1);
        }
        player.getCombatAchievements().removeCurrentTaskFlag(CAType.PRAYING_TO_THE_GODS, ChaosFanatic.CA_TASK_PRAYING_TO_THE_GODS_STARTED);
    }

    @Override
    public String name() {
        return NAME;
    }

    protected void setAttackable(final Player player) {
        refreshKDR(player);
        player.setCanPvp(true);
        //Sets the special attack orb unclickable.
        player.getVarManager().sendBit(IN_WILDERNESS_VARBIT_SPECIAL_UNCLICKABLE, 0);
        GameNoticeboardInterface.refreshWildernessCounters(GameNoticeboardInterface.wildernessCount.incrementAndGet());
        //Supposed to clear received damage when re-entering wilderness.
        player.getReceivedDamage().clear();
        GameInterface.WILDERNESS_OVERLAY.open(player);
    }

    protected void setUnattackable(final Player player) {
        player.getInterfaceHandler().closeInterface(GameInterface.WILDERNESS_OVERLAY);
        player.setCanPvp(false);
        //Check if next player location is not wilderness, then can reset tb. else don't reset will break ferox mechanics
        if (!isWithinWilderness(player.getLocation())) {
            player.getVariables().resetTeleblock();
            player.getReceivedDamage().clear();
        }
        player.getVarManager().sendBit(IN_WILDERNESS_VARBIT_SPECIAL_UNCLICKABLE, 0);
        GameNoticeboardInterface.refreshWildernessCounters(GameNoticeboardInterface.wildernessCount.decrementAndGet());
        //Reset the received hits on the player upon leaving Wilderness so that PvM deaths don't register as PvP deaths.
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        if (source instanceof Player) {
            final Player killer = (Player) source;
            killer.getAttributes().put("WildernessKills", killer.getNumericAttribute("WildernessKills").intValue() + 1);
            player.getAttributes().put("WildernessDeaths",
                    player.getNumericAttribute("WildernessDeaths").intValue() + 1);
            refreshKDR(player);
            refreshKDR(killer);
        }
        return false;
    }

    @Override
    public String getDeathInformation() {
        return null;
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof Player) {
            final Player target = (Player) entity;
            if (target.inArea(FeroxEnclaveDangerArea.class) && target.getVariables().getTime(TickVariable.TELEBLOCK) <= 0 && player.getVariables().getTime(TickVariable.TELEBLOCK) <= 0) {
                player.sendMessage("You cannot fight another player whilst next to the Enclave, please move further out.");
                return false;
            }
            if (!player.getVariables().isSkulled() && player.getVarManager().getBitValue(SettingVariables.PK_SKULL_PREVENTION_VARBIT_ID) == 1 && canSkull(player, target)) {
                player.sendMessage("You cannot attack this target as it would result in you getting skulled.");
                return false;
            }
            if (player.isCanPvp() && !target.isCanPvp()) {
                player.sendMessage("That player is not in the wilderness.");
                return false;
            }
            final int level = getWildernessLevel(player.getLocation()).orElse(-1);
            final int otherLevel = getWildernessLevel(entity.getLocation()).orElse(-1);
            final int minimumLevel = Math.min(level, otherLevel);
            if (minimumLevel >= 1) {
                if (PlayerAttributesKt.getBlackSkulled(target))
                    return true;
                if (PlayerAttributesKt.getBlackSkulled(player) && player.getAttackedBy() == target)
                    return true;
                if (Math.abs(player.getSkills().getCombatLevel() - target.getSkills().getCombatLevel()) > minimumLevel) {
                    player.sendMessage("The difference between your Combat level and the Combat level of "
                            + target.getPlayerInformation().getDisplayname() + " is too great.");
                    player.sendMessage((target.getAppearance().isMale() ? "He" : "She") + " needs to move deeper into" +
                            " the Wilderness before you can attack them.");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean processCombat(Player player, Entity entity, String style) {
        return true;
    }

    @Override
    public void onAttack(final Player player, final Entity entity, final String style,
                         final CombatSpell spell, final boolean splash) {
        if (entity instanceof NPC) {
            return;
        }
        final Player target = (Player) entity;
        if (target != null && canSkull(player, target)) {
            player.getVariables().setSkull(true);
            target.getAttackedByPlayers().put(player.getUsername(), TimeUnit.MINUTES.toTicks(20) + WorldThread.getCurrentCycle());
        }
    }

    private boolean canSkull(Player player, Player target) {
        final long now = WorldThread.getCurrentCycle();
        return player.getAttackedByPlayers().getLong(target.getUsername()) < now
                && target.getAttackedByPlayers().getLong(player.getUsername()) < now;
    }

}

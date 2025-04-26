package com.zenyte.game.content.bountyhunter;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.events.InitializationEvent;
import com.zenyte.plugins.events.LogoutEvent;
import com.zenyte.plugins.events.PlayerDeathEvent;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Kris | 26/03/2019 20:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class BountyHunter {
    public static final int POINTS_VAR = 1132;
    /**
     * A set of available players for pairing process.
     */
    private static final Set<Player> availablePlayers = new ObjectOpenHashSet<>();
    /**
     * A set of currently paired players.
     */
    private static final Set<Pair<Player, Player>> targetPairs = new ObjectOpenHashSet<>();
    /**
     * The player owning this object.
     */
    @NotNull
    private final transient Player player;
    /**
     * The target of this player, a value of null indicates absence of target.
     */
    @Nullable
    private transient Player target;
    /**
     * A set of skip entries; this set is often filtered to only contain the entries from the last 30 minutes, including on log out.
     */
    @Nullable
    private Set<SkipEntry> lastSkips;

    /**
     * The constructor setting the player field & creating a new set to carry the skipped entries.
     *
     * @param player the player owning this object.
     */
    public BountyHunter(@NotNull final Player player) {
        this.player = player;
        lastSkips = new ObjectOpenHashSet<>();
    }

    static {
        VarManager.appendPersistentVarp(POINTS_VAR);
    }

    /**
     * Sets the fields of the bounty hunter for this player.
     *
     * @param event the initialization event carrying both the logging player and the saved player.
     */
    @Subscribe
    public static void onInit(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final BountyHunter bounty = player.getBountyHunter();
        final BountyHunter savedBounty = savedPlayer.getBountyHunter();
        if (savedBounty == null || savedBounty.lastSkips == null) {
            return;
        }
        savedBounty.lastSkips.forEach(bounty::addSkipEntry);
        bounty.filterSkips();
    }

    /**
     * Removes the player from the available players list, cancels all kinds of pending bounty hunter tasks and abandons the target.
     *
     * @param event the logout event carrying the player.
     */
    @Subscribe
    public static void onLogout(final LogoutEvent event) {
        final Player player = event.getPlayer();
        availablePlayers.remove(player);
        player.getVariables().cancel(TickVariable.BOUNTY_HUNTER_TARGET_LOSS);
        final BountyHunter bounty = player.getBountyHunter();
        bounty.abandonTarget();
        bounty.filterSkips();
        if (bounty.lastSkips == null) {
            return;
        }
        if (bounty.lastSkips.isEmpty()) {
            bounty.lastSkips = null;
        }
    }

    @Subscribe
    public static void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getPlayer();
        final Entity source = event.getSource();
        final BountyHunter bh = player.getBountyHunter();
        final Player target = bh.target;
        if (source == null) {
            bh.reset();
            requestTargetInformationUpdate(player);
            if (target != null) {
                requestTargetInformationUpdate(target);
            }
            return;
        }
        if (source == target) {
            bh.upgradeHighestEmblem();
            bh.setValue(BountyHunterVar.CURRENT_HUNTER_KILLS, bh.getValue(BountyHunterVar.CURRENT_HUNTER_KILLS) + 1);
        } else {
            bh.setValue(BountyHunterVar.CURRENT_ROGUE_KILLS, bh.getValue(BountyHunterVar.CURRENT_ROGUE_KILLS) + 1);
        }
        bh.reset();
        requestTargetInformationUpdate(player);
        if (target != null) {
            requestTargetInformationUpdate(target);
        }
    }

    public int getPoints() {
        return player.getVarManager().getValue(POINTS_VAR);
    }

    public void setPoints(final int points) {
        player.getVarManager().sendVar(POINTS_VAR, points);
    }

    private void upgradeHighestEmblem() {
    }

    /**
     * Pairs the two players into a match for bounty hunter. Updates their bounty hunter information accordingly.
     *
     * @param player the first half of the pair.
     * @param target the second half of the pair.
     */
    private static void pair(@NotNull final Player player, @NotNull final Player target) {
        availablePlayers.remove(player);
        availablePlayers.remove(target);
        targetPairs.add(new Pair<>(player, target));
        player.getBountyHunter().target = target;
        target.getBountyHunter().target = player;
        setTargetIndividually(player);
        setTargetIndividually(target);
    }

    /**
     * Sends and updates the information for the requested player about their new bounty hunter target.
     *
     * @param player the player whose information to update.
     */
    private static void setTargetIndividually(@NotNull final Player player) {
        final BountyHunter bh = player.getBountyHunter();
        final Player target = Objects.requireNonNull(bh.target);
        bh.player.sendMessage(Colour.RED.wrap("You've been assigned a target: " + target.getName()));
        requestTargetInformationUpdate(player);
    }

    /**
     * Requests an update for target information on the wilderness overlay. This is executed on the predicate that the WildernessOverlay class exists, which it cannot due to the
     * further calls.
     *
     * @param player the player whose information to update.
     */
    private static void requestTargetInformationUpdate(@NotNull final Player player) {
        GameInterface.WILDERNESS_OVERLAY.getPlugin().ifPresent(plugin -> {
            if (plugin instanceof WildernessOverlay) {
                ((WildernessOverlay) plugin).updateTargetInformation(player);
            }
        });
    }

    /**
     * Gets the value of the requested bounty hunter variable.
     *
     * @param variable the variable whose value to get.
     * @return the value of the variable.
     */
    public int getValue(@NotNull final BountyHunterVar variable) {
        return player.getNumericAttribute(variable.varName).intValue();
    }

    /**
     * Sets the value of the requested variable to the requested value.
     *
     * @param variable the variable whose value to set.
     * @param value    the value to set the variable to.
     */
    public void setValue(@NotNull final BountyHunterVar variable, final int value) {
        player.addAttribute(variable.varName, value);
    }

    /**
     * Searches a suitable target for this current player, based on the available targets and a combat bracket of +-5.
     */
    public void searchTarget() {
        availablePlayers.add(player);
        final int combat = player.getSkills().getCombatLevel();
        for (final Player target : availablePlayers) {
            if (target == player) {
                continue;
            }
            final int targetCombat = target.getSkills().getCombatLevel();
            if (targetCombat < (combat - 5) || targetCombat > (combat + 5)) {
                continue;
            }
            BountyHunter.pair(player, target);
            break;
        }
    }

    /**
     * Informs the players of 1 minute having passed since one of them left wilderness.
     */
    public void halfTimeNotification() {
        assert target != null;
        player.sendMessage(Colour.RED.wrap("You have one minute to return to the Wilderness before you lose your target."));
        target.sendMessage(Colour.RED.wrap("Your target has one minute to return to the Wilderness before you lose them as your target."));
    }

    /**
     * Abandons the target from this player's perspective. Increments the skips count accordingly.
     */
    public void abandonTarget() {
        final Player target = this.target;
        if (target == null) {
            return;
        }
        reset();
        addSkipEntry(new SkipEntry(target.getUsername(), System.currentTimeMillis()));
        final int count = getLastSkipsCount();
        player.sendMessage(Colour.RED.wrap("You have abandoned your target " + (count == 1 ? "once" : (count + " times")) + " in the last 30 minutes."));
        requestTargetInformationUpdate(player);
        requestTargetInformationUpdate(target);
    }

    /**
     * Adds a skip entry to this player's skipped bounty hunter targets' set.
     *
     * @param entry the entry to add.
     */
    private void addSkipEntry(@NotNull final SkipEntry entry) {
        if (lastSkips == null) {
            lastSkips = new ObjectOpenHashSet<>();
        }
        lastSkips.add(entry);
    }

    /**
     * Resets bounty hunter target variables from both perspectives.
     */
    private void reset() {
        final Player target = this.target;
        this.target = null;
        if (target == null) {
            return;
        }
        final BountyHunter bh = target.getBountyHunter();
        bh.target = null;
    }

    /**
     * Filters the skips and returns valid skips amount.
     *
     * @return amount of the skips that have occurred within the last 30 minutes.
     */
    private int getLastSkipsCount() {
        filterSkips();
        return lastSkips == null ? 0 : lastSkips.size();
    }

    /**
     * Iterates the skipped players map and removes the skips that occurred more than 30 minutes ago.
     */
    private void filterSkips() {
        if (lastSkips == null) {
            return;
        }
        lastSkips.removeIf(value -> value.getTime() < (System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(30)));
    }

    /**
     * Starts the target removal countdown that occurs when one of the parties leaves the attackable zone of wilderness.
     */
    public void startTargetRemovalCountdown() {
        availablePlayers.remove(player);
        if (target == null) {
            player.getInterfaceHandler().closeInterface(GameInterface.WILDERNESS_OVERLAY);
            return;
        }
        player.sendMessage(Colour.RED.wrap("You have 2 minutes to return to the Wilderness before you lose your target."));
        target.sendMessage(Colour.RED.wrap("Your target has 2 minutes to return to the Wilderness before you lose them as your target."));
        player.getVariables().schedule(200, TickVariable.BOUNTY_HUNTER_TARGET_LOSS);
    }

    public Player getTarget() {
        return target;
    }
}

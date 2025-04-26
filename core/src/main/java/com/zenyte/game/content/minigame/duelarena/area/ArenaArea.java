package com.zenyte.game.content.minigame.duelarena.area;

import com.zenyte.game.content.consumables.Drinkable;
import com.zenyte.game.content.consumables.Edible;
import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.content.minigame.duelarena.DuelSetting;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.DuelArenaArea;
import com.zenyte.game.world.region.area.plugins.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.zenyte.game.content.minigame.duelarena.DuelSetting.*;

/**
 * @author Tommeh | 29-11-2018 | 21:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public abstract class ArenaArea extends DuelArenaArea implements DeathPlugin, PlayerCombatPlugin, TradePlugin, TeleportPlugin, EntityAttackPlugin, EquipmentPlugin, PrayerPlugin, DropPlugin, EdiblePlugin, DrinkablePlugin, LogoutPlugin, LogoutRestrictionPlugin, LoginPlugin, RandomEventRestrictionPlugin {
    private static final Logger log = LoggerFactory.getLogger(ArenaArea.class);

    @Override
    public RSPolygon[] polygons() {
        return null;
    }

    @Override
    public void enter(Player player) {
        if (player.getDuel() == null) {
            return;
        }
        player.getDuel().resetAttributes(player);
        player.setCantInteract(false);
        player.setCanPvp(true, true);
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.setCanPvp(false);
        final Duel duel = player.getDuel();
        if (duel == null) {
            return;
        }
        if (!duel.isCompleted()) {
            final Player opponent = Objects.requireNonNull(duel.getOpponent());
            duel.finishDuel(opponent, player);
            duel.sendSpoils(opponent);
            duel.registerDuelHistory(opponent, player);
            player.setDuel(null);
            opponent.setDuel(null);
//            player.getPacketDispatcher().sendComponentText(Duel.WINNINGS_INTERFACE, 16, "You forfeit!");
        }
    }

    @Override
    public boolean sendDeath(final Player player, final Entity source) {
        final Duel duel = player.getDuel();
        if (duel == null) {
            return false;
        }
        final Player opponent = duel.getOpponent();
        if (opponent == null) {
            return false;
        }
        opponent.lock();
        player.setAnimation(Animation.STOP);
        player.lock();
        player.stopAll();
        if (player.getPrayerManager().isActive(Prayer.RETRIBUTION)) {
            player.getPrayerManager().applyRetributionEffect(source);
        }
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                if (ticks == 1) {
                    player.setAnimation(Player.DEATH_ANIMATION);
                } else if (ticks == 3) {
                    player.sendMessage("Oh dear, you are dead!");
                    if (opponent.getDuel() != null && !duel.isCompleted()) {
                        opponent.sendMessage("You have defeated " + player.getName() + ".");
                        duel.finishDuel(opponent, player);
                        duel.sendSpoils(opponent);
                        duel.registerDuelHistory(opponent, player);
                        player.setDuel(null);
                        opponent.setDuel(null);
                    }
                } else if (ticks == 4) {
                    player.getAppearance().resetRenderAnimation();
                    player.setAnimation(Animation.STOP);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);
        return true;
    }

    @Override
    public String name() {
        return "Arena Area";
    }

    @Override
    public boolean isSafe() {
        return true;
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
    public boolean attack(final Player player, final Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            return true;
        }
        final Duel duel = player.getDuel();
        if (duel == null) {
            return false;
        }
        final Player target = (Player) entity;
        final Player opponent = duel.getOpponent();
        if (opponent == null) {
            return false;
        }
        if (!target.getName().equals(opponent.getName())) {
            player.sendMessage("You have a different target!");
            return false;
        }
        if (duel.isCountdown()) {
            player.sendMessage("The duel has not started yet!");
            return false;
        }
        return true;
    }

    @Override
    public boolean processCombat(final Player player, final Entity entity, final String style) {
        final Duel duel = player.getDuel();
        if (duel == null) {
            return true;
        }
        if (style.equals("Melee") && duel.hasRule(DuelSetting.NO_MELEE)) {
            player.sendMessage("Melee has been turned off for this duel.");
            return false;
        } else if (style.equals("Ranged") && duel.hasRule(DuelSetting.NO_RANGED)) {
            player.sendMessage("Ranged has been turned off for this duel.");
            return false;
        } else if (style.equals("Magic") && duel.hasRule(DuelSetting.NO_MAGIC)) {
            player.sendMessage("Magic has been turned off for this duel.");
            return false;
        }
        return true;
    }

    @Override
    public boolean manualLogout(final Player player) {
        final Duel duel = player.getDuel();
        if (duel == null) {
            return true;
        }
        if (duel.isCountdown()) {
            player.sendMessage("You cannot logout during the countdown.");
            return false;
        }
        return true;
    }

    @Override
    public void onLogout(final @NotNull Player player) {
        final Duel duel = player.getDuel();
        if (duel == null) {
            return;
        }
        final Player opponent = duel.getOpponent();
        if (opponent == null) {
            return;
        }
        if (duel.inDuel()) {
            duel.finishDuel(opponent, player);
            duel.sendSpoils(opponent);
            duel.registerDuelHistory(opponent, player);
            player.setDuel(null);
            opponent.setDuel(null);
        } else {
            duel.close(true);
        }
    }

    @Override
    public void login(Player player) {
        final RSPolygon lobbyPolygon = Duel.LOBBY.getPolygon(0);
        for (final ArenaArea area : Utils.concatenate(Duel.NORMAL_ARENA, Duel.OBSTACLES_ARENA, Duel.NO_MOVEMENT_ARENA)) {
            if (area.getPolygon(0).contains(player.getX(), player.getY())) {
                player.setLocation(Duel.getRandomPoint(lobbyPolygon, 0, location -> World.isFloorFree(location, 1)));
            }
        }
    }

    @Override
    public boolean activatePrayer(final Player player, final Prayer prayer) {
        final Duel duel = player.getDuel();
        if (duel == null) {
            return true;
        }
        if (duel.inDuel() && duel.hasRule(DuelSetting.NO_PRAYER)) {
            player.sendMessage("Use of prayer has been turned off for this duel.");
            return false;
        }
        return true;
    }

    @Override
    public boolean eat(final Player player, final Edible food) {
        final Duel duel = player.getDuel();
        if (duel == null) {
            return true;
        }
        if (duel.inDuel() && duel.hasRule(DuelSetting.NO_FOOD)) {
            player.sendMessage("Use of food has been turned off for this duel.");
            return false;
        }
        return true;
    }

    @Override
    public boolean drink(final Player player, final Drinkable potion) {
        final Duel duel = player.getDuel();
        if (duel == null) {
            return true;
        }
        if (duel.inDuel() && duel.hasRule(DuelSetting.NO_DRINKS)) {
            player.sendMessage("Use of drinks has been turned off for this duel.");
            return false;
        }
        return true;
    }

    @Override
    public boolean drop(final Player player, final Item item) {
        final Duel duel = player.getDuel();
        if (duel == null) {
            return true;
        }
        if (duel.inDuel()) {
            player.sendMessage("You cannot drop items whilst in a duel.");
            return false;
        }
        return true;
    }

    @Override
    public boolean equip(final Player player, final Item item, final int slot) {
        final Duel duel = player.getDuel();
        if (duel == null || !duel.inDuel()) {
            return true;
        }
        for (int i = HEAD.ordinal(); i <= AMMUNITION.ordinal(); i++) {
            if (Utils.getShiftedBoolean(duel.getSettings(), VALUES[i].getBit())) {
                final int index = i == LEG.ordinal() ? 7 : i == HAND.ordinal() ? 9 : i == FEET.ordinal() ? 10 : i == RING.ordinal() ? 12 : i == AMMUNITION.ordinal() ? 13 : i - 13;
                if (index == slot) {
                    player.sendMessage("You can't equip that during this duel.");
                    return false;
                }
            }
        }
        if (duel.hasRule(DuelSetting.NO_FUN_WEAPONS)) {
            for (final Integer id : Duel.FUN_WEAPONS) {
                if (id == item.getId()) {
                    player.sendMessage("You can't equip that during this duel.");
                    return false;
                }
            }
        }
        if (duel.hasRule(DuelSetting.NO_WEAPON_SWITCH) && slot == EquipmentSlot.WEAPON.getSlot()) {
            player.sendMessage("Weapon switching has been disabled for this duel!");
            return false;
        }
        if (duel.hasRule(DuelSetting.RIGHT_HAND) && slot == EquipmentSlot.WEAPON.getSlot() && item.getDefinitions().isTwoHanded()) {
            player.sendMessage("You can't equip that during this duel.");
            return false;
        }
        return true;
    }

    @Override
    public boolean canTeleport(final Player player, final Teleport teleport) {
        final Duel duel = player.getDuel();
        if (duel != null) {
            player.sendMessage("You cannot teleport during a duel.");
            return false;
        }
        return true;
    }

    @Override
    public boolean canTrade(final Player player, final Player partner) {
        final Duel duel = player.getDuel();
        if (duel != null) {
            player.sendMessage("You cannot trade during a duel.");
            return false;
        }
        return true;
    }
}

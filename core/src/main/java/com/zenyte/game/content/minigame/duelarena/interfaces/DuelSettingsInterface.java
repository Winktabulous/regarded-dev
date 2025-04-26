package com.zenyte.game.content.minigame.duelarena.interfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.content.minigame.duelarena.DuelSetting;
import com.zenyte.game.content.minigame.duelarena.DuelStage;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

import java.util.Optional;

/**
 * @author Tommeh | 27-10-2018 | 20:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DuelSettingsInterface extends Interface {
    @Override
    protected void attach() {
        put(92, "Confirm");
        put(93, "Decline");
        put(97, "Store preset settings");
        put(100, "Load last duel settings");
        put(102, "Whip settings");
        put(104, "Boxing settings");
        put(98, "Load preset settings");
        put(37, "No Ranged");
        put(38, "No Melee");
        put(39, "No Magic");
        put(40, "No Special Attack");
        put(41, "No Fun Weapons");
        put(42, "No Forfeit");
        put(43, "No Prayers");
        put(44, "No Drinks");
        put(45, "No Food");
        put(46, "No Movement");
        put(47, "Obstacles");
        put(48, "No Weapon Switching");
        put(49, "Show Inventories");
        put(56, "Head");
        put(57, "Back");
        put(58, "Neck");
        put(59, "Left hand");
        put(60, "Torso");
        put(61, "Right hand");
        put(62, "Leg");
        put(63, "Hand");
        put(64, "Feet");
        put(65, "Ring");
        put(66, "Ammunition");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        if (!replacement.isPresent() || !replacement.get().equals(GameInterface.DUEL_STAKING)) {
            Optional.ofNullable(player.getDuel()).ifPresent(duel -> duel.close(true));
        }
    }

    @Override
    protected void build() {
        bind("Confirm", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.confirm(DuelStage.SETTINGS);
        });
        bind("Decline", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.close(true);
        });
        bind("Store preset settings", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            player.getAttributes().put("duelPresetSettings", duel.getSettings());
            player.sendMessage("Stored preset settings overwritten.");
        });
        bind("Load last duel settings", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            final Player opponent = duel.getOpponent();
            if (opponent == null) {
                return;
            }
            final int lastSettings = player.getNumericAttribute("lastDuelSettings").intValue();
            if (lastSettings == duel.getSettings()) {
                player.sendMessage("Last duel settings are identical to those already selected.");
                return;
            }
            duel.setRules(lastSettings);
            player.sendMessage("Last duel settings loaded.");
            opponent.sendMessage("Duel Option change - Opponent's preset options loaded!");
        });
        bind("Load preset settings", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            final Player opponent = duel.getOpponent();
            if (opponent == null) {
                return;
            }
            final int settings = player.getNumericAttribute("duelPresetSettings").intValue();
            if (settings == duel.getSettings()) {
                player.sendMessage("Preset duel settings are identical to those already selected.");
                return;
            }
            duel.setRules(settings);
            player.sendMessage("Preset duel settings loaded.");
            opponent.sendMessage("Duel Option change - Opponent's last duel options loaded!");
        });
        bind("Boxing settings", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            final Player opponent = duel.getOpponent();
            if (opponent == null) {
                return;
            }
            if (Duel.BOXING_SETTINGS == duel.getSettings()) {
                player.sendMessage("Boxing settings are identical to those already selected.");
                return;
            }
            duel.setRules(Duel.BOXING_SETTINGS);
            player.sendMessage("Boxing settings loaded.");
            opponent.sendMessage("Duel Option change - Boxing settings loaded!");
        });
        bind("Whip settings", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            final Player opponent = duel.getOpponent();
            if (opponent == null) {
                return;
            }
            if (Duel.WHIP_SETTINGS == duel.getSettings()) {
                player.sendMessage("Whip settings are identical to those already selected.");
                return;
            }
            duel.setRules(Duel.WHIP_SETTINGS);
            player.sendMessage("Whip settings loaded.");
            opponent.sendMessage("Duel Option change - Whip settings loaded!");
        });
        bind("No Ranged", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_RANGED);
        });
        bind("No Melee", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_MELEE);
        });
        bind("No Magic", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_MAGIC);
        });
        bind("No Special Attack", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_SPECIAL_ATTACK);
        });
        bind("No Fun Weapons", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_FUN_WEAPONS);
        });
        bind("No Forfeit", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_FORFEIT);
        });
        bind("No Prayers", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_PRAYER);
        });
        bind("No Drinks", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_DRINKS);
        });
        bind("No Food", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_FOOD);
        });
        bind("No Movement", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_MOVEMENT);
        });
        bind("Obstacles", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.OBSTACLES);
        });
        bind("No Weapon Switching", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NO_WEAPON_SWITCH);
        });
        bind("Show Inventories", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.SHOW_INVENTORIES);
        });
        bind("Head", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.HEAD);
        });
        bind("Back", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.BACK);
        });
        bind("Neck", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.NECK);
        });
        bind("Left hand", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.LEFT_HAND);
        });
        bind("Torso", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.TORSO);
        });
        bind("Right hand", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.RIGHT_HAND);
        });
        bind("Leg", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.LEG);
        });
        bind("Hand", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.HAND);
        });
        bind("Feet", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.FEET);
        });
        bind("Ring", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.RING);
        });
        bind("Ammunition", player -> {
            final Duel duel = player.getDuel();
            if (duel == null) {
                return;
            }
            duel.toggleRule(DuelSetting.AMMUNITION);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.DUEL_SETTINGS;
    }
}

package com.zenyte.game.content.boons;

import com.near_reality.game.content.shop.ShopCurrencyHandler;
import com.zenyte.game.content.boons.impl.UnknownBoon;
import com.zenyte.game.model.shop.ShopCurrency;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.DuelArenaArea;

import java.util.ArrayList;

/**
 * This manager class interfaces with Player Boons that are unlocked with sacrifice/exchange points
 * to provide various bonuses in all elements of the game
 */
public class BoonManager {
    public ArrayList<String> unlockedBoons = new ArrayList<>();
    public ArrayList<String> toggleOffBoons = new ArrayList<>();
    private transient final Player player;

    public BoonManager(Player player) {
        this.player = player;
    }

    public void forceUnlock(String boon) {
        if(!unlockedBoons.contains(boon)) {
            player.notification("Remnant Store", "Perk Unlocked:<br><br>" + Colour.WHITE.wrap(boon), 16750623);
            unlockedBoons.add(boon);
        }
    }

    public boolean hasBoon(Class<? extends Boon> lookupBoon) {
        Boon boon = BoonLoader.findBoon(lookupBoon);

        if(player.getArea() != null && player.getArea() instanceof DuelArenaArea)
            return false;

        if(player.inCombatWithPlayer())
            return false;

        return boon.isActive(player) && (boon.isAlwaysUnlocked(player) || unlockedBoons.contains(boon.getClass().getSimpleName()) || unlockedBoons.contains(boon.getAlternateName())) && !toggleOffBoons.contains(boon.getClass().getSimpleName());
    }

    public boolean purchaseBoon(Class<?> lookupBoon, int index) {
        Boon boon = BoonLoader.findBoon(lookupBoon);
        if(boon instanceof UnknownBoon) {
            player.sendDeveloperMessage("Unknown error attempting to purchase this boon.");
            return false;
        }
        if(unlockedBoons.contains(boon.getClass().getSimpleName())) {
            player.sendMessage("You already have this boon unlocked");
            return false;
        }
        if(!boon.purchasable(player)) {
            player.sendMessage("You do not meet the requirements to purchase this boon");
            return false;
        }
        int available = ShopCurrencyHandler.getAmount(ShopCurrency.EXCHANGE_POINTS, player);
        if(boon.price() <= available) {
            ShopCurrencyHandler.remove(ShopCurrency.EXCHANGE_POINTS, player, boon.price());
            unlockedBoons.add(boon.getClass().getSimpleName());
            player.notification("Remnant Store", "Perk Unlocked:<br><br>" + Colour.WHITE.wrap(boon.name()), 16750623);
            player.sendMessage("You have unlocked " + Colour.BLUE.wrap(boon.name()) + " for " + boon.price() + " remnant points.");
            player.getVarManager().sendVarInstant(4506, ShopCurrencyHandler.getAmount(ShopCurrency.EXCHANGE_POINTS, player));
            player.getVarManager().sendBitInstant(19499 + index, 1);
            player.getVarManager().sendBitInstant(19498, 1);
            player.getPacketDispatcher().sendClientScript(12586, 998, boon.name());
            return true;
        } else {
            player.sendMessage("You do not have enough remnant points.");
            return false;
        }
    }

    public void toggleBoon(Class<?> lookupBoon) {
        Boon boon = BoonLoader.findBoon(lookupBoon);
        String boonName = boon.getClass().getSimpleName();
        if(boon instanceof UnknownBoon) {
            player.sendDeveloperMessage("Unknown error attempting to toggle this perk.");
            return;
        }
        if(!unlockedBoons.contains(boonName)) {
            player.sendMessage("You need to purchase this perk first");
            return;
        }
        if(toggleOffBoons.contains(boonName)) {
            toggleOffBoons.remove(boonName);
            player.sendMessage(Colour.RS_GREEN.wrap(boon.name() + " has been toggled ON!"));
        } else {
            toggleOffBoons.add(boonName);
            player.sendMessage(Colour.RS_RED.wrap(boon.name() + " has been toggled OFF!"));
        }
    }


    public void initialize(BoonManager manager) {
        if (manager != null && manager.unlockedBoons != null) {
            unlockedBoons = manager.unlockedBoons;
        }

        if (manager != null && manager.toggleOffBoons != null) {
            toggleOffBoons = manager.toggleOffBoons;
        }
    }
}

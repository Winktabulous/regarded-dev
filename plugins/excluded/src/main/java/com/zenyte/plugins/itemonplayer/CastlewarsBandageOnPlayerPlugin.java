package com.zenyte.plugins.itemonplayer;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnPlayerPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastlewarsBandageOnPlayerPlugin implements ItemOnPlayerPlugin {
    @Override
    public void handleItemOnPlayerAction(final Player player, final Item item, final int slot, final Player target) {
        if (!player.inArea("Castle Wars") || !target.inArea("Castle Wars")) {
            player.getInventory().deleteItem(item.getId(), player.getInventory().getAmountOf(item.getId()));
            player.sendMessage("You cannot have or use these outside of Castle Wars!");
            return;
        }
        final CastleWarsTeam playerTeam = CastleWars.getTeam(player);
        final CastleWarsTeam targetTeam = CastleWars.getTeam(player);
        if (playerTeam != targetTeam) {
            player.sendMessage("You cannot heal the other team with bandages!");
            return;
        }
        final boolean cwarsBracelet = player.getTemporaryAttributes().containsKey("castle wars bracelet effect");
        int amt = (int) (Math.floor(target.getMaxHitpoints() * 0.1) * (cwarsBracelet ? 1.5 : 1));
        player.getInventory().deleteItem(slot, item);
        target.heal(amt);
        target.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 30.0);
        target.sendMessage("You heal " + target.getName() + " with the bandages.");
    }

    @Override
    public int[] getItems() {
        return new int[] {4049};
    }
}

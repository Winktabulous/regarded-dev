package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 30/11/2018 17:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BarrowsChest implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.inArea("Rise of the Six")) {
            switch (option) {
                case "Search" -> {
                    GameInterface.BARROWS_REWARDS.open(player);
                }
                case "Close" -> player.getVarManager().sendBit(Barrows.CHEST_VARBIT, 0);
                case "Open" -> player.getVarManager().sendBit(Barrows.CHEST_VARBIT, 1);
            }
            return;
        }

        if (!player.inArea("Barrows chambers")) {
            throw new RuntimeException("Unable to interact with the chest outside of barrows.");
        }
        final Barrows barrows = player.getBarrows();
        switch(option) {
            case "Search":
                if (barrows.isLooted()) {
                    player.sendMessage("The chest is empty!");
                    return;
                }
                GameInterface.BARROWS_REWARDS.open(player);
                break;
            case "Close":
                player.getVarManager().sendBit(Barrows.CHEST_VARBIT, 0);
                break;
            case "Open":
                player.getVarManager().sendBit(Barrows.CHEST_VARBIT, 1);
                final boolean spawn = !barrows.isLooted() && !barrows.getSlainWights().contains(barrows.getHiddenWight()) && (barrows.getCurrentWight() == null || barrows.getCurrentWight().getId() != barrows.getHiddenWight().getNpcId());
                if (spawn) {
                    barrows.sendWight(barrows.getHiddenWight(), player.getLocation(), "You dare steal from us!");
                }
                break;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CHEST_20723, 20973 };
    }
}

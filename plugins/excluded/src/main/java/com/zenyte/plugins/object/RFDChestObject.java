package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 2 okt. 2018 | 15:40:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class RFDChestObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch(option) {
            case "Bank":
                GameInterface.BANK.open(player);
                break;
            case "Buy-food":
                player.openShop("Culinaromancer's Chest - Food");
                break;
            case "Buy-items":
                player.openShop("Culinaromancer's Chest");
                break;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 12308, ObjectId.CHEST_12309 };
    }
}

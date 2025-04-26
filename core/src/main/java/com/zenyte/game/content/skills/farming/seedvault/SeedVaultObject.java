package com.zenyte.game.content.skills.farming.seedvault;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class SeedVaultObject implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getGameMode() == GameMode.ULTIMATE_IRON_MAN) {
            player.sendMessage("You can not use seed vault as an ultimate iron man.");
            return;
        }
        GameInterface.SEED_VAULT.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.SEED_VAULT};
    }
}

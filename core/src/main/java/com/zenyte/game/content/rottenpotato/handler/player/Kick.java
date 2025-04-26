package com.zenyte.game.content.rottenpotato.handler.player;

import com.zenyte.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

public class Kick implements PlayerRottenPotatoActionHandler {
    @Override
    public void execute(Player user, Player target) {
        target.log(LogLevel.INFO, "Forcefully kicked by " + user.getName() + ".");
        target.logout(true);
        user.sendMessage("Successfully kicked <col=C22731>" + target.getUsername() + "</col>!");
    }

    @Override
    public String option() {
        return "Kick player";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.SUPPORT;
    }
}

package com.zenyte.game.content.rottenpotato.handler.player;

import com.near_reality.api.service.sanction.SanctionPlayerExtKt;
import com.zenyte.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public class Mute implements PlayerRottenPotatoActionHandler {
    @Override
    public void execute(Player user, Player target) {
        SanctionPlayerExtKt.submitInfiniteAccountMuteFor(user, target, "Rotten Potato");
    }

    @Override
    public String option() {
        return "Mute";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.SUPPORT;
    }
}

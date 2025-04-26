package com.zenyte.game.content.rottenpotato.handler.player;

import com.near_reality.api.service.sanction.SanctionPlayerExtKt;
import com.zenyte.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public class Ban implements PlayerRottenPotatoActionHandler {
    @Override
    public void execute(Player reporter, Player offender) {
        SanctionPlayerExtKt.submitInfiniteAccountBan(reporter, offender, "Rotten Potato");
    }

    @Override
    public String option() {
        return "Ban";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.MODERATOR;
    }
}

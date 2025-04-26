package com.zenyte.game.content.rottenpotato.handler;

import com.zenyte.game.content.rottenpotato.RottenPotatoActionType;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public interface PlayerRottenPotatoActionHandler extends RottenPotatoActionHandler {
    void execute(final Player user, final Player target);

    @Override
    default RottenPotatoActionType type() {
        return RottenPotatoActionType.ITEM_ON_PLAYER;
    }
}

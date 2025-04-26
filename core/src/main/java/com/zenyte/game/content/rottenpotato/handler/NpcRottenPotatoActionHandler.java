package com.zenyte.game.content.rottenpotato.handler;

import com.zenyte.game.content.rottenpotato.RottenPotatoActionType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 3/27/2020
 */
public interface NpcRottenPotatoActionHandler extends RottenPotatoActionHandler {
    void execute(final Player user, final NPC target);

    @Override
    default RottenPotatoActionType type() {
        return RottenPotatoActionType.ITEM_ON_NPC;
    }
}

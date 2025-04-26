package com.zenyte.game.content.rottenpotato.handler.npc;

import com.zenyte.game.content.rottenpotato.handler.NpcRottenPotatoActionHandler;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

/**
 * @author Christopher
 * @since 3/27/2020
 */
public class KillNpc implements NpcRottenPotatoActionHandler {
    @Override
    public void execute(Player user, NPC target) {
        target.forceKilled = true;
        target.applyHit(new Hit(user, target.getMaxHitpoints(), HitType.POISON));
    }

    @Override
    public String option() {
        return "Kill NPC";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.ADMINISTRATOR;
    }
}

package com.zenyte.game.content.rottenpotato.handler.npc;

import com.zenyte.game.content.rottenpotato.handler.NpcRottenPotatoActionHandler;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

public class DespawnNpc implements NpcRottenPotatoActionHandler {
    @Override
    public void execute(Player user, NPC target) {
        target.finish();
    }

    @Override
    public String option() {
        return "Despawn NPC";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.ADMINISTRATOR;
    }
}

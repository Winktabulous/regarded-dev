package com.zenyte.game.content.boss.cerberus.area;

import com.zenyte.game.content.skills.slayer.SlayerMaster;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

/**
 * @author Tommeh | 12/06/2019 | 18:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface AbstractCerberusLair {
    default CerberusLairInstance asInstanced() {
        if(this instanceof CerberusLairInstance)
            return (CerberusLairInstance) this;
        else throw new IllegalStateException();
    }

    default StaticCerberusLair asStatic() {
        if(this instanceof StaticCerberusLair)
            return (StaticCerberusLair) this;
        else throw new IllegalStateException();
    }


}

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

public class StaticCerberusLair extends PolygonRegionArea implements AbstractCerberusLair, EntityAttackPlugin, CannonRestrictionPlugin, LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1200, 1340}, {1200, 1220}, {1280, 1220}, {1280, 1281}, {1343, 1281}, {1343, 1220}, {1403, 1220}, {1403, 1340}})};
    }

    @Override
    public void enter(Player player) {
        player.setViewDistance(Player.SCENE_DIAMETER);
        final WorldObject existingFire = World.getObjectWithType(player.getLocation(), 10);
        //If there's an existing fire underneath the player, we shall move them south.
        if (existingFire == null || existingFire.getId() != ObjectId.FLAMES) {
            return;
        }
        player.addWalkSteps(player.getX(), player.getY() - 1, 1, true);
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.resetViewDistance();
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final String name = ((NPC) entity).getDefinitions().getName();
            if (name.equals("Cerberus")) {
                if (!player.getSlayer().isCurrentAssignment(entity) || player.getSlayer().getMaster() == SlayerMaster.KRYSTILIA) {
                    player.sendMessage("You can only kill Cerberus while you're on a slayer task.");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String name() {
        return "Cerberus Lair";
    }
}

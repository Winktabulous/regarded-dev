package com.zenyte.game.world.region.area;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.impl.TarMonster;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin;

/**
 * @author Christopher
 * @since 3/21/2020
 */
public class FossilIslandForest extends FossilIsland implements FullMovementPlugin {
    private static final Animation monsterSpawningAnim = new Animation(7678);

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{3684, 3679}, {3599, 3812}, {3663, 3819}, {3712, 3798}, {3711, 3694}})};
    }

    @Override
    public boolean processMovement(Player player, int x, int y) {
        if (!player.isUnderCombat()) {
            World.findNPC(NpcId.TAR_BUBBLES, player.getLocation(), 2).ifPresent(npc -> {
                npc.setId(NpcId.TAR_MONSTER); //temporarily set it to different id to prevent multiple spawns. pretty shit
                WorldTasksManager.schedule(() -> {
                    npc.finish();
                    final TarMonster monster = new TarMonster(NpcId.TAR_MONSTER, npc.getLocation(), true);
                    monster.spawn();
                    monster.setAnimation(monsterSpawningAnim);
                    WorldTasksManager.schedule(monster::remove, 99);
                }, 3);
            });
        }
        return true;
    }

    @Override
    public String name() {
        return "Fossil Island Forest";
    }

}

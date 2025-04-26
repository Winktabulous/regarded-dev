package com.zenyte.game.content.boss.cerberus.area;

import com.zenyte.game.content.boss.cerberus.Cerberus;
import com.zenyte.game.content.boss.cerberus.CerberusRoom;
import com.zenyte.game.content.skills.slayer.SlayerMaster;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfBoundaryException;

/**
 * @author John J. Woloszyk / Kryeus
 */
public class CerberusLairInstance extends DynamicArea implements AbstractCerberusLair, EntityAttackPlugin, CannonRestrictionPlugin, LootBroadcastPlugin {

    private static final Location OUTSIDE_TILE = new Location(1310, 1249, 0);
    private Player player;

    private Cerberus cerberus;

    public CerberusLairInstance(Player player, AllocatedArea area) {
        super(area, 152, 151);
        this.player = player;
    }

    @Override
    public void constructRegion() {
        if (constructed) {
            return;
        }
        GlobalAreaManager.add(this);
        try {
            MapBuilder.copyPlanesMap(area, staticChunkX, staticChunkY, chunkX, chunkY, sizeX, sizeY, 0);
        } catch (OutOfBoundaryException e) {
            e.printStackTrace();
        }
        constructed = true;
        constructed();
    }

    @Override
    public void constructed() {
        player.lock();
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (ticks == 2) {
                    new FadeScreen(player, () -> player.setLocation(getLocation(CerberusRoom.INSTANCED.getExit()))).fade(2);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);

        cerberus = (Cerberus) new Cerberus(getLocation(1239, 1250, 0), this).spawn();
    }

    @Override
    public void enter(Player player) {
        player.setViewDistance(Player.SCENE_DIAMETER);
    }

    @Override
    public void leave(Player player, boolean logout) {
        if(logout)
            player.forceLocation(OUTSIDE_TILE);
        player.resetViewDistance();
    }

    @Override
    public String name() {
        return player.getName() + "Cerberus Instance";
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
}

package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 14/06/2022
 */
@SuppressWarnings("unused")
public class DeathsDomainObjectPlugin implements ObjectAction {
    private static final Logger log = LoggerFactory.getLogger(DeathsDomainObjectPlugin.class);
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Location onLogin = player.getLocation().copy();
        try {
            final AllocatedArea area = MapBuilder.findEmptyChunk(4, 2);
            final DynamicArea dynamicArea = new DynamicArea(area, 395, 715) {
                @Override
                public void enter(Player player) {
                }

                @Override
                public void leave(Player player, boolean logout) {
                }

                @Override
                public String name() {
                    return "Deaths' Domain";
                }

                @Override
                public Location onLoginLocation() {
                    return onLogin;
                }

                @Override
                public void constructed() {
                    Analytics.flagInteraction(player, Analytics.InteractionType.DEATHS_DOMAIN);
                    GameInterface.DEATHS_OFFICE_ENTER_OVERLAY.open(player);
                    player.setAnimation(new Animation(827, 20));
                    player.sendSound(3952);
                    final NPC death = new NPC(9855, getLocation(3179, 5726, 0), true);
                    death.setRadius(0);
                    death.spawn();
                    player.getPacketDispatcher().sendClientScript(2893, 41549825, 41549826, 39504, 4128927, -1, -1);
                    WorldTasksManager.schedule(() -> {
                        player.setAnimation(Animation.STOP);
                        player.getPacketDispatcher().sendClientScript(2894, 41549825, 41549826, -1, -1);
                        player.setLocation(getLocation(3171, 5726, 0));
                        WorldTasksManager.schedule(() -> {
                            player.lock(1);
                            player.getInterfaceHandler().closeInterface(GameInterface.DEATHS_OFFICE_ENTER_OVERLAY);
                        });
                    }, 0);
                }
            };
            dynamicArea.constructRegion();
        } catch (OutOfSpaceException e) {
            log.error("", e);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 38426, ObjectId.DEATHS_DOMAIN_39637, ObjectId.DEATHS_DOMAIN_39547 };
    }
}

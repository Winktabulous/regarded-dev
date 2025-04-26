package com.zenyte.game.content.godwars.objects;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 24-3-2019 | 14:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodwarsZamorakBridgeObject implements ObjectAction {

    private static final Graphics GRAPHICS = new Graphics(68);

    private static final Animation ANIMATION = new Animation(6993);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.lock();
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        if (player.getY() <= 5333) {
                            player.addWalkSteps(2885, 5333);
                        } else {
                            player.addWalkSteps(2885, 5344);
                        }
                        break;
                    case 1:
                        if (player.getY() <= 5333) {
                            player.setLocation(new Location(2885, 5334, 2));
                        } else {
                            player.setLocation(new Location(2885, 5343, 2));
                        }
                        player.sendSound(new SoundEffect(3862));
                        player.setAnimation(ANIMATION);
                        player.setGraphics(GRAPHICS);
                        player.getPacketDispatcher().sendClientScript(343, 0, 5, 0, 406 << 16 | 19);
                        player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 406);
                        break;
                    case 4:
                        if (player.getY() <= 5334) {
                            player.setLocation(new Location(2885, 5345, 2));
                        } else {
                            player.setLocation(new Location(2885, 5332, 2));
                        }
                        player.sendMessage("Dripping, you climb out of the water.<br>The extreme evil of this area leaves your Prayer drained.");
                        player.getPrayerManager().setPrayerPoints(0);
                        player.lock(1);
                        break;
                }
            }
        }, 0, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ICE_BRIDGE };
    }
}

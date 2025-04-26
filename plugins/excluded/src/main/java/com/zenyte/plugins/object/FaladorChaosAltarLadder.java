package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentUtils;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 25/04/2019 21:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FaladorChaosAltarLadder implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == ObjectId.LADDER_31580) {
            if (player.getSkills().getTotalLevel() < 500) {
                player.sendMessage("You need a total level of 500 and a set up Zamorak Robes equipped to go upstairs.");
                return;
            }
            if (!EquipmentUtils.wearingZamorakRobes(player)) {
                player.sendMessage("You need a total level of 500 and a set up Zamorak Robes equipped to go upstairs.");
                player.getDialogueManager().start(new Dialogue(player, 8400) {

                    @Override
                    public void buildDialogue() {
                        npc("You better dress appropriately, if you want to go up there!");
                    }
                });
                return;
            }
            player.setAnimation(new Animation(828));
            player.lock(2);
            WorldTasksManager.schedule(() -> player.setLocation(player.getLocation().transform(0, 0, 1)));
            return;
        }
        player.setAnimation(new Animation(828));
        player.lock(2);
        WorldTasksManager.schedule(() -> player.setLocation(player.getLocation().transform(0, 0, -1)));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_31580, ObjectId.LADDER_31579 };
    }
}

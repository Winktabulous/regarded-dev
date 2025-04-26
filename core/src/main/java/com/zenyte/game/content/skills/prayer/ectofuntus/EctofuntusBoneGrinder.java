package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 23/06/2019 12:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EctofuntusBoneGrinder implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Wind")) {
            player.getActionManager().setAction(new BoneGrinding(BoneGrinding.Stage.GRINDING, null));
        } else if (option.equalsIgnoreCase("Status")) {
            final int status = player.getNumericAttribute("ectofuntus bone status").intValue();
            final int boneId = player.getNumericAttribute("ectofuntus grinded bone").intValue();
            assert status == 0 || boneId != 0;
            if (status == 1) {
                player.sendMessage("The bone grinder contains some " + ItemDefinitions.getOrThrow(boneId).getName().toLowerCase() + " inside it.");
            } else if (boneId != 0) {
                player.sendMessage("The bone grinder contains some ground " + ItemDefinitions.getOrThrow(boneId).getName().toLowerCase() + " inside it.");
            } else {
                player.sendMessage("The bone grinder is currently empty.");
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BONE_GRINDER_16655 };
    }
}

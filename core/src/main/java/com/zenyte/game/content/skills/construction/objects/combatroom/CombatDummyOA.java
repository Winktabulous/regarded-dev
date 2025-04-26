package com.zenyte.game.content.skills.construction.objects.combatroom;

import com.zenyte.game.content.skills.construction.CombatDummyNPC;
import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 6. march 2018 : 16:17.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CombatDummyOA implements ObjectInteraction {

    private static final Animation ANIM = new Animation(834);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.COMBAT_DUMMY, 29337 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (option.equals("attach")) {
            player.setAnimation(ANIM);
            WorldTasksManager.schedule(() -> {
                World.removeObject(object);
                new CombatDummyNPC(object.getId() == 29336 ? 2668 : 7413, object).spawn();
            });
        }
    }
}

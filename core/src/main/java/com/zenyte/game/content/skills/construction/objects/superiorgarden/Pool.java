package com.zenyte.game.content.skills.construction.objects.superiorgarden;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26. veebr 2018 : 4:21.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Pool implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.POOL_OF_RESTORATION, ObjectId.POOL_SPACE, ObjectId.POOL_OF_REJUVENATION, 29241 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("drink")) {
            final int id = object.getId();
            if (id == 29237)
                drink(player, 0);
            else if (id == 29122)
                drink(player, 1);
            else if (id == 29239)
                drink(player, 2);
            else if (id == 29122)
                drink(player, 3);
            else if (id == 29241)
                drink(player, 4);
        }
    }

    private final void drink(final Player player, final int type) {
        if (type >= 0)
            player.getCombatDefinitions().setSpecialEnergy(100);
        if (type >= 1)
            player.getVariables().setRunEnergy(100);
        if (type >= 2)
            player.getPrayerManager().restorePrayerPoints(99);
        if (type >= 3) {
            for (int i = 0; i < 22; i++) {
                if (i == SkillConstants.HITPOINTS || i == SkillConstants.PRAYER)
                    continue;
                if (player.getSkills().getLevel(i) < player.getSkills().getLevelForXp(i))
                    player.getSkills().setLevel(i, player.getSkills().getLevelForXp(i));
            }
        }
        if (type >= 4)
            player.heal(99);
        player.sendMessage("You feel replenished.");
    }
}

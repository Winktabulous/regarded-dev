package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24/01/2019 17:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AncientAltar implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getCombatDefinitions().getSpellbook() == Spellbook.ANCIENT) {
            player.getPrayerManager().drainPrayerPoints(player.getPrayerManager().getPrayerPoints());
            player.sendMessage("You feel a strange wisdom drain upon your memory...");
            player.getCombatDefinitions().setSpellbook(Spellbook.NORMAL, true);
            return;
        }
        player.getPrayerManager().drainPrayerPoints(player.getPrayerManager().getPrayerPoints());
        player.sendMessage("You feel a strange wisdom fill your mind...");
        player.getCombatDefinitions().setSpellbook(Spellbook.ANCIENT, true);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ALTAR_6552 };
    }
}

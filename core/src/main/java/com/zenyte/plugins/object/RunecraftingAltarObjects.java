package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.runecrafting.BasicRunecraftingAction;
import com.zenyte.game.content.skills.runecrafting.Runecrafting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;

/**
 * @author Kris | 10. nov 2017 : 22:20.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class RunecraftingAltarObjects implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == ObjectId.ALTAR_34771) {
            if (option.equals("Pray")) {
                if (!player.getCombatDefinitions().getSpellbook().equals(Spellbook.LUNAR)) {
                    player.getCombatDefinitions().setSpellbook(Spellbook.LUNAR, true);
                    player.sendMessage("Lunar spells activated!");
                } else {
                    player.getCombatDefinitions().setSpellbook(Spellbook.NORMAL, true);
                    player.sendMessage("Lunar spells deactivated!");
                }
                return;
            }
            player.getActionManager().setAction(new BasicRunecraftingAction(Runecrafting.ASTRAL_RUNE));
            return;
        }
        final Runecrafting rune = Runecrafting.getRuneByAltarObject(object.getId());
        if (rune == null) {
            return;
        }
        player.getActionManager().setAction(new BasicRunecraftingAction(rune));
    }

    @Override
    public Object[] getObjects() {
        final ArrayList<Object> list = new ArrayList<Object>();
        for (final Runecrafting r : Runecrafting.VALUES) {
            if (r.getAltarObjectId() == -1) {
                continue;
            }
            list.add(r.getAltarObjectId());
        }
        return list.toArray(new Object[list.size()]);
    }
}

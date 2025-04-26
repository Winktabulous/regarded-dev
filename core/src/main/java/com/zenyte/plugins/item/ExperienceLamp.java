package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Lamp;
import com.zenyte.game.world.entity.player.calog.CATierType;

import static com.zenyte.game.GameInterface.EXPERIENCE_LAMP;
import static com.zenyte.game.world.entity.player.Lamp.*;

/**
 * @author Tommeh | 8-11-2018 | 18:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ExperienceLamp extends ItemPlugin {
    @Override
    public void handle() {
        bind("Rub", (player, item, slotId) -> {
            final Lamp lamp = Lamp.get(item.getId());
            final Object[] args = new Object[4];
            args[2] = slotId;
            args[3] = item;
            if (lamp != null) {
                args[0] = lamp.getExperience();
                args[1] = lamp.getMinimumLevel();
                if (lamp != ELITE_DIARY_LAMP) {
                    if (item.getCharges() == 1) {
                        args[0] = (int) ((lamp == EASY_DIARY_LAMP ? 1000 : lamp == MEDIUM_DIARY_LAMP ? 5000 : 10000) / 5.0F);
                        args[1] = lamp == EASY_DIARY_LAMP ? 1 : lamp == MEDIUM_DIARY_LAMP ? 30 : 40;
                    }
                }
            } else {
                final CATierType type = CATierType.getTierByLamp(item.getId());
                if (type == null) {
                    return;
                }
                args[0] = type.getExperience();
                args[1] = type.getMinimumLevel();
            }
            player.getTemporaryAttributes().put("experience_lamp_info", args);
            EXPERIENCE_LAMP.open(player);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {13145, 13146, 13147, 13148, ItemId.ANTIQUE_LAMP_25920, ItemId.ANTIQUE_LAMP_25921,
                ItemId.ANTIQUE_LAMP_25922, ItemId.ANTIQUE_LAMP_25923, ItemId.ANTIQUE_LAMP_25924, ItemId.ANTIQUE_LAMP_25925};
    }
}

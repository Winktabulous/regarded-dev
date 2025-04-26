package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.skills.smithing.SmeltableBar;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BlastFurnaceDispenserD extends Dialogue {
    private final List<String> page1 = new ArrayList<>();
    private final List<String> page2 = new ArrayList<>();

    public BlastFurnaceDispenserD(final Player player) {
        super(player);
        int counter = 0;
        for (final SmeltableBar bar : SmeltableBar.VALUES) {
            if (bar.equals(SmeltableBar.BLURITE_BAR)) continue;
            final String payload = TextUtils.capitalizeFirstCharacter(bar.toString().toLowerCase().replace("_", " ")) + ": " + player.getBlastFurnace().getBar(bar) + "<br>";
            (counter < 4 ? page1 : page2).add(payload);
            counter++;
        }
    }

    @Override
    public void buildDialogue() {
        plain(page1.toString().replace(", ", "").replace("]", "").replace("[", ""), true);
        plain(page2.toString().replace(", ", "").replace("]", "").replace("[", ""), true);
    }
}

package com.zenyte.game.content.skills.farming.plugins.supercompost;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Christopher
 * @since 02/24/2020
 */
public class UltracompostCreationDialogue extends SkillDialogue {
    public UltracompostCreationDialogue(Player player, Item... items) {
        super(player, items);
    }

    @Override
    public void run(int slotId, int amount) {
        player.getActionManager().setAction(new UltracompostCreationAction(amount));
    }
}

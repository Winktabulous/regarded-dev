package com.zenyte.game.content.skills.fletching.actions;

import com.zenyte.game.content.skills.fletching.FletchingDefinitions;
import com.zenyte.game.content.skills.fletching.composite.CompOgreBowCreationAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Christopher
 * @since 3/20/2020
 */
public class OgreAmmunitionFletchingD extends SkillDialogue {
    private final FletchingDefinitions.AmmunitionFletchingData data;

    public OgreAmmunitionFletchingD(final Player player, final FletchingDefinitions.AmmunitionFletchingData data, Item... items) {
        super(player, data == FletchingDefinitions.AmmunitionFletchingData.WOLFBONE_TIPS ? "How many bones do you wish to fletch?" : "How many logs do you wish to fletch?",
                items);
        this.data = data;
    }


    @Override
    public void run(int slotId, int amount) {
        if (slotId == 1) {
            player.getActionManager().setAction(new CompOgreBowCreationAction(amount, false));
            return;
        }
        if (data != null) {
            player.getActionManager().setAction(new OgreAmmunitionFletching(data, amount));
        }
    }
}

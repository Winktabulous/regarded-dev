package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.crafting.actions.SodaAshCrafting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class SodaAshD extends SkillDialogue {

    private final boolean range;

    public SodaAshD(final Player player, final boolean range) {
        super(player, SodaAshCrafting.SODA_ASH);
        this.range = range;
    }

    @Override
    public void run(int slotId, int amount) {
        player.getActionManager().setAction(new SodaAshCrafting(amount, range));
    }
}

package com.zenyte.game.content.skills.magic.resources;

import com.zenyte.game.content.skills.magic.RuneContainer;
import com.zenyte.game.world.entity.player.Player;

public class BlightedSackResource implements RuneResource {

    @Override
    public RuneContainer getContainer() {
        return null;
    }

    @Override
    public int getAmountOf(Player player, int runeId, int amountRequired) {



        return amountRequired;
    }

    @Override
    public void consume(Player player, int runeId, int amount) {

    }

    @Override
    public int[] getAffectedRunes() {
        return null;
    }

}

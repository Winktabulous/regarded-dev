package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 06/07/2019 04:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EnergyWell implements ObjectAction {

    /**
     * The animation that's performed when drinking from the energy fountain.
     */
    private static final Animation REPLENISH = new Animation(832);

    private static final SoundEffect sound = new SoundEffect(224);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            player.setAnimation(REPLENISH);
            if (player.getVariables().getRunEnergy() >= 100) {
                player.sendMessage("The pool has no effect on you.");
            } else {
                player.sendSound(sound);
                player.getVariables().setRunEnergy(100);
                player.sendMessage("You feel replenished.");
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ENERGY_WELL };
    }
}

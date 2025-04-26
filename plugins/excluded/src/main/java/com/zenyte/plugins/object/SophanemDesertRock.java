package com.zenyte.plugins.object;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.events.LoginEvent;

/**
 * @author Christopher
 * @since 4/12/2020
 */
public class SophanemDesertRock implements ObjectAction {
    private static final int ROCK_VARBIT = 395;
    private static final int ROCK_OBJECT = 6621;
    private static final ImmutableLocation DESTINATION = new ImmutableLocation(3319, 2796, 0);

    /**
     * Auto-enable rock entrance via varbit. Done after completing quest on OSRS.
     */
    @Subscribe
    public static final void onLogin(final LoginEvent event) {
        final Player player = event.getPlayer();
        player.getVarManager().sendBit(ROCK_VARBIT, 1);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        new FadeScreen(player, () -> player.setLocation(DESTINATION)).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ROCK_OBJECT};
    }
}

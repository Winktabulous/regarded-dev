package com.zenyte.plugins.object;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.LadderObject;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.events.LoginEvent;
import mgi.utilities.CollectionUtils;

import java.util.ArrayList;

/**
 * @author Kris | 10/05/2019 19:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CatacombsRope implements ObjectAction {

    private enum Rope {
        KOUREND_CATACOMBS_SW_ROPE_UP(new LadderObject(28919, new Location(1470, 3653, 0)), new Location(1651, 9986, 0)),
        KOUREND_CATACOMBS_SE_ROPE_UP(new LadderObject(28918, new Location(1666, 3565, 0)), new Location(1725, 9993, 0)),
        KOUREND_CATACOMBS_NE_ROPE_UP(new LadderObject(28920, new Location(1696, 3865, 0)), new Location(1719, 10101, 0)),
        KOUREND_CATACOMBS_NW_ROPE_UP(new LadderObject(28921, new Location(1563, 3791, 0)), new Location(1617, 10101, 0)),
        KOUREND_CATACOMBS_SW_ROPE(5088, new LadderObject(28896, new Location(1650, 9986, 0)), new Location(1470, 3652, 0)),
        KOUREND_CATACOMBS_SE_ROPE(5087, new LadderObject(28897, new Location(1726, 9993, 0)), new Location(1666, 3564, 0)),
        KOUREND_CATACOMBS_NE_ROPE(5089, new LadderObject(28898, new Location(1719, 10102, 0)), new Location(1696, 3864, 0)),
        KOUREND_CATACOMBS_NW_ROPE(5090, new LadderObject(28895, new Location(1617, 10102, 0)), new Location(1563, 3790, 0));
        private int varbit;
        private final LadderObject object;
        private final Location destination;
        private static final Rope[] values = values();

        Rope(int varbit, LadderObject object, Location destination) {
            this.varbit = varbit;
            this.object = object;
            this.destination = destination;
        }

        Rope(LadderObject object, Location destination) {
            this.object = object;
            this.destination = destination;
        }
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final CatacombsRope.Rope rope = CollectionUtils.findMatching(Rope.values, r -> r.object.getId() == object.getId() && r.object.getLocation().matches(object));
        if (rope == null) {
            return;
        }
        player.lock(2);
        if (rope.varbit != 0) {
            player.getVarManager().sendBit(rope.varbit, 1);
            player.addAttribute(rope.toString(), 1);
        }
        if (option.equalsIgnoreCase("Climb-up")) {
            player.setAnimation(new Animation(828));
        } else {
            player.setAnimation(new Animation(827));
        }
        WorldTasksManager.schedule(() -> player.setLocation(rope.destination));
    }

    @Subscribe
    public static final void onLogin(final LoginEvent event) {
        final Player player = event.getPlayer();
        for (final CatacombsRope.Rope rope : Rope.values) {
            if (rope.varbit == 0) continue;
            if (player.getNumericAttribute(rope.toString()).intValue() == 1) {
                player.getVarManager().sendBit(rope.varbit, 1);
            }
        }
    }

    @Override
    public Object[] getObjects() {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (final CatacombsRope.Rope rope : Rope.values) {
            list.add(rope.object.getId());
        }
        list.add(28915);
        return list.toArray();
    }
}

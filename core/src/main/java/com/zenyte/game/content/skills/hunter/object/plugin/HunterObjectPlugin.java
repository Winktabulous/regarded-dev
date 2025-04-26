package com.zenyte.game.content.skills.hunter.object.plugin;

import com.zenyte.game.content.skills.hunter.node.PreyObject;
import com.zenyte.game.content.skills.hunter.node.TrapPrey;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

/**
 * @author Kris | 29/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface HunterObjectPlugin extends ObjectAction {
    TrapType type();

    @Override
    default void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getLocation().matches(object)) {
            player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
        } else {
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
        }
    }

    @Override
    default Object[] getObjects() {
        final ObjectOpenHashSet<Object> list = new ObjectOpenHashSet<>();
        final TrapType type = type();
        for (final TrapPrey prey : TrapPrey.getValues()) {
            if (prey.getTrap() == type) {
                final PreyObject obj = prey.getObject();
                if (obj == null) {
                    continue;
                }
                list.add(obj.getFinalObject());
                list.add(obj.getFirstObject());
                for (final int id : obj.getObjects()) {
                    list.add(id);
                }
            }
        }
        if (type.getObjectIds().length == 1) {
            list.add(type.getObjectId());
        }
        list.add(type.getCollapsedObjectId());
        list.add(type.getCollapsedAnimatedObjectId());
        return list.toArray();
    }
}

package com.zenyte.game.content.skills.hunter.object.plugin;

import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.actions.CheckPlacedTrap;
import com.zenyte.game.content.skills.hunter.actions.CreateDeadfallAction;
import com.zenyte.game.content.skills.hunter.actions.DismantlePlacedTrap;
import com.zenyte.game.content.skills.hunter.node.PreyObject;
import com.zenyte.game.content.skills.hunter.node.TrapPrey;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.content.skills.woodcutting.TreeDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mgi.utilities.CollectionUtils;

import java.util.OptionalInt;

/**
 * @author Kris | 31/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DeadfallObject implements ItemOnObjectAction, HunterObjectPlugin {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (option) {
        case "Set-trap": 
            player.getActionManager().setAction(new CreateDeadfallAction(object, OptionalInt.empty()));
            break;
        case "Investigate": 
            //TODO: More of this.
            player.sendMessage("This trap has been set without any bait.");
            player.sendMessage("This trap hasn't been smoked.");
            break;
        case "Check": 
            {
                final HunterTrap trap = HunterUtils.findTrap(type(), object, null).orElseThrow(RuntimeException::new);
                player.getActionManager().setAction(new CheckPlacedTrap(trap, false));
                break;
            }
        case "Dismantle": 
            {
                final HunterTrap trap = HunterUtils.findTrap(type(), object, null).orElseThrow(RuntimeException::new);
                player.getActionManager().setAction(new DismantlePlacedTrap(trap));
                break;
            }
        default: 
            throw new IllegalStateException(option);
        }
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public TrapType type() {
        return TrapType.DEADFALL;
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getLocation().matches(object)) {
            player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
        } else {
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
        }
    }

    @Override
    public Object[] getObjects() {
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

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (!object.getDefinitions().containsOption("Set-trap")) {
            player.sendFilteredMessage("Nothing interesting happens.");
            return;
        }
        final TreeDefinitions logs = CollectionUtils.findMatching(TreeDefinitions.values, value -> value.getLogsId() == item.getId());
        player.getActionManager().setAction(new CreateDeadfallAction(object, logs != null ? OptionalInt.of(slot) : OptionalInt.empty()));
    }

    @Override
    public Object[] getItems() {
        final ObjectOpenHashSet<Object> list = new ObjectOpenHashSet<>();
        for (final TreeDefinitions tree : TreeDefinitions.values) {
            list.add(tree.getLogsId());
        }
        list.add(ItemId.KNIFE);
        list.remove(-1);
        return list.toArray();
    }
}

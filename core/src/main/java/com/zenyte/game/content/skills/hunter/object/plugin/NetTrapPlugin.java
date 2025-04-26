package com.zenyte.game.content.skills.hunter.object.plugin;

import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.actions.CheckPlacedTrap;
import com.zenyte.game.content.skills.hunter.actions.CreateNetTrapAction;
import com.zenyte.game.content.skills.hunter.actions.DismantlePlacedTrap;
import com.zenyte.game.content.skills.hunter.node.*;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Kris | 01/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NetTrapPlugin implements ObjectAction, ItemOnObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (option) {
        case "Set-trap": 
            player.getActionManager().setAction(new CreateNetTrapAction(object));
            break;
        case "Investigate": 
            //TODO: More of this.
            player.sendMessage("This trap has been set without any bait.");
            player.sendMessage("This trap hasn't been smoked.");
            break;
        case "Check": 
            {
                //The type doesn't matter as long as it's one of the four net traps.
                final TrapType type = TrapType.NET_TRAP_SWAMP_LIZARD;
                final HunterTrap trap = HunterUtils.findTrap(type, object, null).orElseThrow(RuntimeException::new);
                player.getActionManager().setAction(new CheckPlacedTrap(trap, false));
                break;
            }
        case "Dismantle": 
            {
                final TrapType type = TrapType.NET_TRAP_SWAMP_LIZARD;
                final Optional<HunterTrap> failedTrap = HunterUtils.findTrap(type, object, null);
                //Failed trap is a single object, therefore it can be checked through the creature itself.
                if (failedTrap.isPresent()) {
                    player.getActionManager().setAction(new DismantlePlacedTrap(failedTrap.get()));
                    return;
                }
                final NetTrapPair pair = HunterUtils.findNetTrapPair(object);
                final HunterTrap trap = HunterUtils.findTrap(type, pair.getNet(), null).orElseThrow(RuntimeException::new);
                player.getActionManager().setAction(new DismantlePlacedTrap(trap));
                break;
            }
        default: 
            throw new IllegalStateException(option);
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (!object.getDefinitions().containsOption("Set-trap") || object.isLocked()) {
            player.sendFilteredMessage("This tree has already been used to set up a trap.");
            return;
        }
        player.getActionManager().setAction(new CreateNetTrapAction(object));
    }

    @Override
    public Object[] getItems() {
        return new Object[] {ItemId.SMALL_FISHING_NET, ItemId.ROPE};
    }

    @Override
    public Object[] getObjects() {
        final ObjectOpenHashSet<Object> set = new ObjectOpenHashSet<>();
        for (final NetTrap trap : NetTrap.getValues()) {
            set.add(trap.getYoungTree());
            set.add(trap.getBentYoungTree());
            final TrapType type = trap.getType();
            set.add(type.getObjectId());
            set.add(type.getCollapsedObjectId());
            set.add(type.getCollapsedAnimatedObjectId());
        }
        final TrapPrey[] prey = new TrapPrey[] {TrapPrey.SWAMP_LIZARD, TrapPrey.ORANGE_SALAMANDER, TrapPrey.RED_SALAMANDER, TrapPrey.BLACK_SALAMANDER};
        for (final TrapPrey preyType : prey) {
            final PreyObject objectType = Objects.requireNonNull(preyType.getObject());
            for (final int object : objectType.getObjects()) {
                set.add(object);
            }
            set.add(objectType.getFinalObject());
        }
        set.remove(-1);
        return set.toArray();
    }
}

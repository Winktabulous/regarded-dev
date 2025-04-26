package com.zenyte.game.content.skills.hunter.actions;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.node.NetTrap;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.npc.HunterDummyNPC;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.zenyte.game.content.skills.hunter.node.TrapType.*;

/**
 * @author Kris | 01/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CreateNetTrapAction extends ConstructableHunterTrapAction {
    private static final Animation animation = new Animation(5215);
    private final WorldObject youngTree;
    @Nullable
    private WorldObject netTrap;
    @Nullable
    private NetTrap trapConstant;

    @Override
    public boolean start() {
        if (youngTree.isLocked()) {
            return false;
        }
        final Inventory inventory = player.getInventory();
        if (!inventory.containsItem(ItemId.ROPE, 1) || !inventory.containsItem(ItemId.SMALL_FISHING_NET, 1)) {
            player.sendMessage("You need both a rope and a small fishing net to set-up this trap.");
            return false;
        }
        trapConstant = NetTrap.findTrap(youngTree.getId());
        final TrapType type = trapConstant.getType();
        if (HunterUtils.hasMaxTrapsLaid(player) || HunterUtils.isAreaRestricted(player, type) || HunterUtils.isBelowRequiredLevel(player, type)) {
            return false;
        }
        lockObject();
        netTrap = HunterUtils.buildNetTrap(youngTree, trapConstant.getType());
        player.lock(2);
        World.sendObjectAnimation(this.youngTree, NetTrap.getTreeBendingDownAnimation());
        player.setAnimation(animation);
        World.sendSoundEffect(youngTree, new SoundEffect(trapConstant.getType().getSetupSound(), 5));
        inventory.deleteItem(ItemId.ROPE, 1);
        inventory.deleteItem(ItemId.SMALL_FISHING_NET, 1);
        delay(1);
        return true;
    }

    /**
     * Sets the object in a locked state so if anyone else from here on out tries to interact with it for while it is locked, they will not be able to set the trap up.
     * <p>Necessary protection against if two people attempt to place the trap simultaneously, as it is object-ran.</p>
     */
    private void lockObject() {
        youngTree.setLocked(true);
        WorldTasksManager.schedule(() -> youngTree.setLocked(false), 2);
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        Preconditions.checkArgument(trapConstant != null);
        Preconditions.checkArgument(netTrap != null);
        final HunterTrap existingPossibleTrap = HunterUtils.findTrap(BOX_TRAP, netTrap, null).orElseGet(() -> HunterUtils.findTrap(BIRD_SNARE, netTrap, null).orElse(null));
        if (existingPossibleTrap != null) {
            final Runnable runnable = existingPossibleTrap.getRemovalRunnable();
            if (runnable != null) {
                runnable.run();
            } else {
                existingPossibleTrap.remove();
            }
        }
        final TrapType type = trapConstant.getType();
        final HunterDummyNPC dummy = new HunterDummyNPC(type.getActiveDummyNpcId(), netTrap);
        final WorldObject bentYoungTree = new WorldObject(trapConstant.getBentYoungTree(), this.youngTree.getType(), this.youngTree.getRotation(), this.youngTree);
        final int rotation = this.youngTree.getRotation();
        final WorldObject offsetLocation = rotation == 0 || rotation == 1 ? this.youngTree : this.netTrap;
        final WorldObject collapsedAnimatedTree = new WorldObject(type.getCollapsedAnimatedObjectId(), this.youngTree.getType(), this.youngTree.getRotation(), offsetLocation);
        final HunterTrap trap = new HunterTrap(player, dummy, this.youngTree, Collections.singletonList(this.youngTree), Arrays.asList(bentYoungTree, netTrap), Collections.singletonList(collapsedAnimatedTree), type);
        dummy.setTrap(trap);
        final Runnable consumer = () -> {
            trap.remove();
            World.spawnFloorItem(new Item(ItemId.SMALL_FISHING_NET), netTrap, trap.getPlayer().get(), (int) TimeUnit.MINUTES.toTicks(1), (int) TimeUnit.MINUTES.toTicks(2));
            World.spawnFloorItem(new Item(ItemId.ROPE), netTrap, trap.getPlayer().get(), (int) TimeUnit.MINUTES.toTicks(1), (int) TimeUnit.MINUTES.toTicks(2));
            final Player player = trap.getPlayer().get();
            if (player != null && !player.isNulled()) {
                player.sendFilteredMessage("The net trap that you set has collapsed.");
            }
        };
        trap.addConsumer((int) TimeUnit.MINUTES.toTicks(1), consumer);
        trap.addConsumer((int) TimeUnit.MINUTES.toTicks(2) + 1, consumer);
        final WorldObject collapsedTree = new WorldObject(type.getCollapsedObjectId(), this.youngTree.getType(), this.youngTree.getRotation(), offsetLocation);
        trap.addConsumer((int) TimeUnit.MINUTES.toTicks(3), trap::failNetTrap);
        trap.addConsumer((int) TimeUnit.MINUTES.toTicks(3) + 2, () -> trap.finalizeNetTrapFailure(collapsedTree));
        trap.addConsumer((int) TimeUnit.MINUTES.toTicks(4) + 1, consumer);
        trap.setRemovalRunnable(consumer);
        trap.setup(Optional.empty());
        //Moving off of the trap is done a tick later.
        WorldTasksManager.schedule(() -> {
            if (netTrap.matches(player.getLocation())) {
                HunterUtils.walkAway(player);
            }
        });
        return -1;
    }

    public CreateNetTrapAction(WorldObject youngTree) {
        this.youngTree = youngTree;
    }

    @Override
    TrapType[] trapTypes() {
        return new TrapType[] {TrapType.NET_TRAP_SWAMP_LIZARD, NET_TRAP_ORANGE_SALAMANDER, NET_TRAP_RED_SALAMANDER, NET_TRAP_BLACK_SALAMANDER};
    }
}

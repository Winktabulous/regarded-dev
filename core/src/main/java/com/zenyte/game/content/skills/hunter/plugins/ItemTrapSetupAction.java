package com.zenyte.game.content.skills.hunter.plugins;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.npc.HunterDummyNPC;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;

/**
 * @author Kris | 26/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ItemTrapSetupAction extends Action {
    private static final Animation animation = new Animation(5208);
    @NotNull
    private final TrapType trapType;
    @Nullable
    private FloorItem floorItem;
    private final int slot;
    @NotNull
    private final Location location;

    public ItemTrapSetupAction(@NotNull final TrapType trapType, @Nullable final FloorItem floorItem, final int slot, @NotNull final Location location) {
        this.trapType = trapType;
        this.floorItem = floorItem;
        this.slot = slot;
        this.location = new ImmutableLocation(location);
    }

    @Override
    public boolean start() {
        if (HunterUtils.isAreaRestricted(player, trapType) || HunterUtils.isBelowRequiredLevel(player, trapType) || HunterUtils.hasMaxTrapsLaid(player) || isTileOccupied()) {
            return false;
        }
        if (floorItem != null) {
            if (!floorItem.isPresent()) {
                return false;
            }
            //If the floor item already exists, we destroy it and spawn a new - private - one below.
            World.destroyFloorItem(floorItem);
        } else {
            final int id = trapType.getItemId();
            Preconditions.checkArgument(id != -1);
            final Item item = new Item(id);
            final ContainerResult result = player.getInventory().deleteItem(slot, item);
            if (result.isFailure()) {
                return false;
            }
            floorItem = new FloorItem(item, location, player, player, 300, 500);
        }
        Preconditions.checkArgument(floorItem != null);
        player.stopAll();
        player.setAnimation(animation);
        player.sendFilteredMessage("You begin setting up the trap.");
        player.sendSound(trapType.getSetupSound());
        delay(3);
        //Spawn a new floor item, one that is visible only to us so no one else can interfere with it.
        World.spawnFloorItem(floorItem, location, player, 300, 500);
        //Spawning a new floor item constructs a new object, therefore we need to find the new reference.
        floorItem = World.getFloorItem(floorItem.getId(), floorItem.getLocation(), player);
        Preconditions.checkArgument(floorItem != null);
        return true;
    }

    private boolean isTileOccupied() {
        if (!World.isFloorFree(location, 0) || World.getObjectWithType(location, 10) != null) {
            player.sendMessage("You cannot lay a trap here.");
            return true;
        }
        return false;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        if (isTileOccupied()) {
            return -1;
        }
        final int chunkId = Chunk.getChunkHash(location.getX() >> 3, location.getY() >> 3, location.getPlane());
        final Chunk chunk = World.getChunk(chunkId);
        if (!chunk.getFloorItems().contains(floorItem)) {
            return -1;
        }
        final Location tile = new Location(player.getLocation());
        //Moving off of the trap is done a tick later.
        WorldTasksManager.schedule(() -> {
            if (tile.matches(player.getLocation())) {
                HunterUtils.walkAway(player);
            }
        });
        World.destroyFloorItem(floorItem);
        final HunterDummyNPC dummy = new HunterDummyNPC(trapType.getActiveDummyNpcId(), location);
        final WorldObject object = new WorldObject(trapType.getObjectId(), 10, 2, location);
        final WorldObject collapsedObject = new WorldObject(trapType.getCollapsedObjectId(), 10, 2, location);
        final HunterTrap trap = new HunterTrap(player, dummy, location, Collections.emptyList(), Collections.singletonList(object), Collections.singletonList(collapsedObject), trapType);
        dummy.setTrap(trap);
        final Runnable consumer = () -> {
            trap.remove();
            final int id = trapType.getItemId();
            if (id != -1) {
                World.spawnFloorItem(new Item(id), location, trap.getPlayer().get(), 100, 200);
            }
            final Player player = trap.getPlayer().get();
            if (player != null && !player.isNulled()) {
                player.sendFilteredMessage("The " + trapType + " that you laid has fallen over.");
            }
        };
        trap.setRemovalRunnable(consumer);
        trap.addConsumer(100, consumer);
        //Once the traps catch a prey, their state is set to 101 and they should decay a minute later.
        trap.addConsumer(201, consumer);
        trap.setup(Optional.empty());
        return -1;
    }
}

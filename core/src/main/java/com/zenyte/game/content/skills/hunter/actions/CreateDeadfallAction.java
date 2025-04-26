package com.zenyte.game.content.skills.hunter.actions;

import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.npc.HunterDummyNPC;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

import java.util.Collections;
import java.util.Optional;
import java.util.OptionalInt;

import static com.zenyte.game.content.skills.hunter.node.TrapType.DEADFALL;

/**
 * @author Kris | 31/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class CreateDeadfallAction extends ConstructableHunterTrapAction {
    private final WorldObject object;
    private final OptionalInt logsSlot;

    @Override
    public boolean start() {
        if (object.isLocked()) {
            return false;
        }
        if (player.getHunter().hasDeadfallLaid()) {
            player.sendMessage("You cannot lay more than one deadfall trap.");
            return false;
        }
        final boolean status = startLogsTrap(logsSlot);
        if (!status) {
            return false;
        }
        player.sendSound(DEADFALL.getSetupSound());
        lockObject();
        return true;
    }

    /**
     * Sets the object in a locked state so if anyone else from here on out tries to interact with it for while it is locked, they will not be able to set the trap up.
     * <p>Necessary protection against if two people attempt to place the trap simultaneously, as it is object-ran.</p>
     */
    private void lockObject() {
        object.setLocked(true);
        WorldTasksManager.schedule(() -> object.setLocked(false), 3);
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        final int rotation = object.getRotation();
        final HunterDummyNPC dummy = new HunterDummyNPC(DEADFALL.getActiveDummyNpcId(), object);
        dummy.setSpawnLocation(object.transform(rotation == 0 ? 1 : 0, rotation == 3 ? 1 : 0, 0));
        //Maniacal monkeys: 28827
        //Maniacal monkey npc anim when caught: 7249
        final WorldObject object = new WorldObject(DEADFALL.getObjectId(), this.object.getType(), this.object.getRotation(), this.object);
        final WorldObject collapsedObject = new WorldObject(DEADFALL.getCollapsedObjectId(), this.object.getType(), this.object.getRotation(), this.object);
        final HunterTrap trap = new HunterTrap(player, dummy, this.object, Collections.singletonList(this.object), Collections.singletonList(object), Collections.singletonList(collapsedObject), DEADFALL);
        dummy.setTrap(trap);
        final Runnable runnable = () -> {
            trap.remove();
            final Player player = trap.getPlayer().get();
            if (player != null && !player.isNulled()) {
                player.sendFilteredMessage("The deadfall trap you have constructed has collapsed.");
            }
        };
        trap.addConsumer((int) TimeUnit.MINUTES.toTicks(1), runnable);
        trap.addConsumer((int) TimeUnit.MINUTES.toTicks(2) + 1, runnable);
        trap.setup(Optional.empty());
        return -1;
    }

    public CreateDeadfallAction(WorldObject object, OptionalInt logsSlot) {
        this.object = object;
        this.logsSlot = logsSlot;
    }

    @Override
    TrapType[] trapTypes() {
        return new TrapType[] {DEADFALL};
    }
}

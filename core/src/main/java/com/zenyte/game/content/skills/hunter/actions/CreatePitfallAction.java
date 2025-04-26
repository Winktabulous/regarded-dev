package com.zenyte.game.content.skills.hunter.actions;

import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.npc.HunterDummyNPC;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

import java.util.Collections;
import java.util.Optional;
import java.util.OptionalInt;

import static com.zenyte.game.content.skills.hunter.node.TrapType.PITFALL;

/**
 * @author Kris | 30/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CreatePitfallAction extends ConstructableHunterTrapAction {
    private final WorldObject object;
    private final OptionalInt logsSlot;

    @Override
    public boolean start() {
        return startLogsTrap(logsSlot);
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        player.getVarManager().sendBit(object.getDefinitions().getVarbit(), 1);
        final HunterDummyNPC dummy = new HunterDummyNPC(PITFALL.getActiveDummyNpcId(), object);
        final HunterTrap trap = new HunterTrap(player, dummy, object, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), PITFALL);
        player.sendSound(trap.getType().getSetupSound());
        dummy.setTrap(trap);
        trap.addConsumer((int) TimeUnit.MINUTES.toTicks(3), () -> {
            trap.remove();
            player.getVarManager().sendBit(object.getDefinitions().getVarbit(), 0);
            final Player player = trap.getPlayer().get();
            if (player != null && !player.isNulled()) {
                player.sendFilteredMessage("The pitfall you have constructed has collapsed.");
            }
        });
        trap.setup(Optional.of(() -> player.getVarManager().sendBit(object.getDefinitions().getVarbit(), 1)));
        return -1;
    }

    public CreatePitfallAction(WorldObject object, OptionalInt logsSlot) {
        this.object = object;
        this.logsSlot = logsSlot;
    }

    @Override
    TrapType[] trapTypes() {
        return new TrapType[] {PITFALL};
    }
}

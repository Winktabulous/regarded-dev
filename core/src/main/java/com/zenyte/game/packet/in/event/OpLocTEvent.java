package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.spells.ObjectSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectHandler;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpLocTEvent implements ClientProtEvent {
    private final int interfaceId;
    private final int componentId;
    private final int slotId;
    private final int objectId;
    private final int x;
    private final int y;
    private final boolean run;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", slot: " + slotId + ", object id: " + objectId + ", x: " + x + ", y: " + y + ", z: " + player.getPlane() + ", run: " + run);
    }

    @Override
    public void handle(Player player) {
        final Location location = new Location(x, y, player.getPlane());
        final WorldObject object = World.getObjectWithId(location, objectId);
        if (object == null) {
            return;
        }
        if (run && player.eligibleForShiftTeleportation()) {
            player.setLocation(new Location(object));
            return;
        } else if (run) {
            player.setRun(true);
        }
        player.stopAll();
        if (interfaceId == 149) {
            final Item item = player.getInventory().getItem(slotId);
            if (item == null) {
                return;
            }
            ItemOnObjectHandler.handleItemOnObject(player, item, slotId, object);
            return;
        }
        player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> {
            player.stopAll();
            player.faceObject(object);
            if (interfaceId == 218) {
                final ObjectSpell spell = Magic.getSpell(player.getCombatDefinitions().getSpellbook(), SpellDefinitions.getSpellName(componentId), ObjectSpell.class);
                if (spell == null) {
                    return;
                }
                spell.execute(player, object);
            }
        }));
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public OpLocTEvent(int interfaceId, int componentId, int slotId, int objectId, int x, int y, boolean run) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.slotId = slotId;
        this.objectId = objectId;
        this.x = x;
        this.y = y;
        this.run = run;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}

package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.spells.FloorItemSpell;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class InterfaceOnFloorItemEvent implements ClientProtEvent {
    private final int interfaceId;
    private final int componentId;
    private final int itemId;
    private final int x;
    private final int y;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", item: " + itemId + ", x: " + x + ", y: " + y + ", z: " + player.getPlane());
    }

    @Override
    public void handle(Player player) {
        final Location location = new Location(x, y, player.getPlane());
        final FloorItem item = World.getRegion(location.getRegionId(), true).getFloorItem(itemId, location, player);
        if (item == null || player.isDead() || player.isFinished() || player.isLocked()) {
            return;
        }
        final int regionId = location.getRegionId();
        if (!player.getMapRegionsIds().contains(regionId)) {
            return;
        }
        if (interfaceId == 218) {
            final FloorItemSpell spell = Magic.getSpell(player.getCombatDefinitions().getSpellbook(), SpellDefinitions.getSpellName(componentId), FloorItemSpell.class);
            if (spell == null) {
                return;
            }
            spell.execute(player, item);
        }
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public InterfaceOnFloorItemEvent(int interfaceId, int componentId, int itemId, int x, int y) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.itemId = itemId;
        this.x = x;
        this.y = y;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}

package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.spells.NPCSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCHandler;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpNpcTEvent implements ClientProtEvent {
    private final int interfaceId;
    private final int componentId;
    private final int index;
    private final boolean run;
    private final int itemId;
    private final int slot;

    @Override
    public void log(@NotNull final Player player) {
        final NPC npc = World.getNPCs().get(index);
        if (npc == null) {
            log(player, "Index: " + index + ", interface: " + interfaceId + ", component: " + componentId + ", run: " + run + "; name: null");
            return;
        }
        final Location tile = npc.getLocation();
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", index: " + index + ", run: " + run + "; id: " + npc.getId() + ", name: " + npc.getName(player) + ", x: " + tile.getX() + ", y: " + tile.getY() + ", z: " + tile.getPlane());
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        if (player.isLocked()) {
            return;
        }
        final NPC target = World.getNPCs().get(index);
        if (target == null) {
            return;
        }
        if (run && player.eligibleForShiftTeleportation()) {
            player.setLocation(new Location(target.getLocation()));
            return;
        } else if (run) {
            player.setRun(true);
        }
        if (interfaceId == 149) {
            final Item item = player.getInventory().getItem(slot);
            if (item == null) {
                return;
            }
            if (item.getId() != itemId) return;
            ItemOnNPCHandler.handleItemOnNPC(player, item, slot, target);
            return;
        }
        if (interfaceId == 218) {
            final NPCSpell spell = Magic.getSpell(player.getCombatDefinitions().getSpellbook(), SpellDefinitions.getSpellName(componentId), NPCSpell.class);
            if (spell == null) {
                return;
            }
            spell.execute(player, target);
        }
    }

    public OpNpcTEvent(int interfaceId, int componentId, int index, boolean run, int itemId, int slot) {
        this.itemId = itemId;
        this.slot = slot;
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.index = index;
        this.run = run;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}

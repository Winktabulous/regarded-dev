package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.spells.ItemSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 25-1-2019 | 20:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpHeldTEvent implements ClientProtEvent {
    private final int fromInterfaceId;
    private final int fromComponentId;
    private final int toInterfaceId;
    private final int toComponentId;
    private final int fromSlot;
    private final int toSlot;

    @Override
    public void handle(Player player) {
        final Item item = player.getInventory().getItem(toSlot);
        if (item == null) {
            return;
        }
        final ItemSpell spell = Magic.getSpell(player.getCombatDefinitions().getSpellbook(), SpellDefinitions.getSpellName(fromComponentId), ItemSpell.class);
        if (spell == null) {
            return;
        }
        spell.execute(player, item, toSlot);
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + fromInterfaceId + " -> " + toInterfaceId + ", component: " + fromComponentId + " -> " + toComponentId + ", slot: " + fromSlot + " -> " + toSlot);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public OpHeldTEvent(int fromInterfaceId, int fromComponentId, int toInterfaceId, int toComponentId, int fromSlot, int toSlot) {
        this.fromInterfaceId = fromInterfaceId;
        this.fromComponentId = fromComponentId;
        this.toInterfaceId = toInterfaceId;
        this.toComponentId = toComponentId;
        this.fromSlot = fromSlot;
        this.toSlot = toSlot;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}

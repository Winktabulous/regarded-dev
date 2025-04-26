package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.spells.ItemSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemHandler;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 07/06/2022
 */
public class IfButtonTEvent implements ClientProtEvent {
    private final int fromInterfaceId;
    private final int fromComponentId;
    private final int toInterfaceId;
    private final int toComponentId;
    private final int fromSlot;
    private final int toSlot;
    private final int fromItemId;
    private final int toItemId;

    @Override
    public void handle(Player player) {
        /* Hard-code the couple exceptions for now. */
        if (fromInterfaceId == 218 && toInterfaceId == 149) {
            final Item item = player.getInventory().getItem(toSlot);
            if (item == null) {
                return;
            }
            final ItemSpell spell = Magic.getSpell(player.getCombatDefinitions().getSpellbook(), SpellDefinitions.getSpellName(fromComponentId), ItemSpell.class);
            if (spell == null) {
                return;
            }
            spell.execute(player, item, toSlot);
        } else if (fromInterfaceId == 149 && toInterfaceId == 149) {
            final Item from = player.getInventory().getItem(fromSlot);
            final Item to = player.getInventory().getItem(toSlot);
            if (from == null || to == null || player.isLocked()) {
                return;
            }
            ItemOnItemHandler.handleItemOnItem(player, from, to, fromSlot, toSlot);
        }
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + fromInterfaceId + " -> " + toInterfaceId + ", component: " + fromComponentId + " -> " + toComponentId + ", slot: " + toSlot);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public IfButtonTEvent(int fromInterfaceId, int fromComponentId, int toInterfaceId, int toComponentId, int fromSlot, int toSlot, int fromItemId, int toItemId) {
        this.fromInterfaceId = fromInterfaceId;
        this.fromComponentId = fromComponentId;
        this.toInterfaceId = toInterfaceId;
        this.toComponentId = toComponentId;
        this.fromSlot = fromSlot;
        this.toSlot = toSlot;
        this.fromItemId = fromItemId;
        this.toItemId = toItemId;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}

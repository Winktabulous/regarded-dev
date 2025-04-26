package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.SwitchPlugin;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.ListenerType;
import com.zenyte.plugins.MethodicPluginHandler;
import com.zenyte.plugins.handlers.InterfaceSwitchHandler;
import com.zenyte.plugins.handlers.InterfaceSwitchPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Tommeh | 25-1-2019 | 22:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class IfButtonDEvent implements ClientProtEvent {
    private final int fromInterfaceId;
    private final int fromComponentId;
    private final int toInterfaceId;
    private final int toComponentId;
    private final int fromSlotId;
    private final int toSlotId;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + fromInterfaceId + " -> " + toInterfaceId + ", component: " + fromComponentId + " -> " + toComponentId + ", slot: " + fromSlotId + " -> " + toSlotId);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        /** Close all input dialogues when switching, to prevent potential dupes in vulnerable code. */
        player.getInterfaceHandler().closeInput();
        player.getInterfaceHandler().closeInterface(InterfacePosition.DIALOGUE);
        if (fromInterfaceId == toInterfaceId) {
            final Optional<GameInterface> optionalGameInterface = GameInterface.get(fromInterfaceId);
            if (optionalGameInterface.isPresent()) {
                final GameInterface gameInterface = optionalGameInterface.get();
                final Optional<Interface> optionalPlugin = gameInterface.getPlugin();
                if (optionalPlugin.isPresent()) {
                    final Interface plugin = optionalPlugin.get();
                    if (plugin instanceof SwitchPlugin) {
                        if (plugin.switchItem(player, fromComponentId, toComponentId, fromSlotId, toSlotId)) return;
                    }
                }
            }
        }
        final InterfaceSwitchPlugin plugin = InterfaceSwitchHandler.interfaces.get(fromInterfaceId);
        /** If a full-script plugin exists for the interface, execute it and prevent code from going further. */
        if (plugin != null) {
            plugin.switchItem(player, fromInterfaceId, toInterfaceId, fromComponentId, toComponentId, fromSlotId, toSlotId);
            return;
        }
        MethodicPluginHandler.invokePlugins(ListenerType.INTERFACE_SWITCH, player, fromInterfaceId, toInterfaceId, fromComponentId, toComponentId, fromSlotId, toSlotId);
    }

    /**
     * Invoke all methodic plugins.
     */
    public IfButtonDEvent(int fromInterfaceId, int fromComponentId, int toInterfaceId, int toComponentId, int fromSlotId, int toSlotId) {
        this.fromInterfaceId = fromInterfaceId;
        this.fromComponentId = fromComponentId;
        this.toInterfaceId = toInterfaceId;
        this.toComponentId = toComponentId;
        this.fromSlotId = fromSlotId;
        this.toSlotId = toSlotId;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}

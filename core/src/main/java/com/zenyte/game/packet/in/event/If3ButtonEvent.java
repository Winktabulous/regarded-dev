package com.zenyte.game.packet.in.event;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.ButtonAction;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Tommeh | 25-1-2019 | 20:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class If3ButtonEvent implements ClientProtEvent {
    private static final Logger log = LoggerFactory.getLogger(If3ButtonEvent.class);
    private final int interfaceId;
    private final int componentId;
    private int slotId;
    private int itemId;
    private final int option;

    @Override
    public void handle(Player player) {
        if (itemId == 65535) {
            itemId = -1;
        }
        if (slotId == 65535) {
            slotId = -1;
        }
        ButtonAction.handleComponentAction(player, interfaceId, componentId, slotId, itemId, option, 3);
    }

    @Override
    public void log(@NotNull final Player player) {
        final Optional<GameInterface> interfaceName = GameInterface.get(interfaceId);
        final String name = interfaceName.isPresent() ? interfaceName.get().toString() : "";
        log(player, "Interface: " + name + ", id: " + interfaceId + ", component: " + componentId + ", slot: " + slotId + ", item: " + itemId + ", option: " + option);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public If3ButtonEvent(int interfaceId, int componentId, int slotId, int itemId, int option) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.slotId = slotId;
        this.itemId = itemId;
        this.option = option;
    }
}

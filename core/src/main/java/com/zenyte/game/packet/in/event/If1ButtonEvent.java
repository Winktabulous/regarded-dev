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
 * @author Kris | 25/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class If1ButtonEvent implements ClientProtEvent {
    private static final Logger log = LoggerFactory.getLogger(If1ButtonEvent.class);
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
        ButtonAction.handleComponentAction(player, interfaceId, componentId, slotId, itemId, option, 1);
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

    public If1ButtonEvent(int interfaceId, int componentId, int slotId, int itemId, int option) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.slotId = slotId;
        this.itemId = itemId;
        this.option = option;
    }
}

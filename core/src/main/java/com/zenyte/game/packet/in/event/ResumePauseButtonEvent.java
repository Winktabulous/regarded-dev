package com.zenyte.game.packet.in.event;

import com.zenyte.game.model.ui.ButtonAction;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.NewInterfaceHandler;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Tommeh | 25-1-2019 | 19:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ResumePauseButtonEvent implements ClientProtEvent {
    private static final Logger log = LoggerFactory.getLogger(ResumePauseButtonEvent.class);
    private final int interfaceId;
    private final int componentId;
    private final int slotId;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Interface: " + interfaceId + ", component: " + componentId + ", slot: " + slotId);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        if (!player.getInterfaceHandler().isVisible(interfaceId)) {
            return;
        }
        final Interface plugin = NewInterfaceHandler.getInterface(interfaceId);
        if (plugin != null) {
            Optional<String> opt = plugin.getComponentName(componentId, slotId);
            if (opt.isEmpty()) {
                opt = plugin.getComponentName(componentId, -1);
            }
            log.debug("[" + plugin.getClass().getSimpleName() + "] Dialogue: " + opt.orElse("Absent") + "(" + interfaceId + "::" + componentId + ") | Slot: " + slotId);
            plugin.click(player, componentId, slotId, -1, -1);
            return;
        }
        final UserInterface script = ButtonAction.interfaces.get(interfaceId);
        if (script == null) {
            log.debug("Unhandled Dialogue Interface: interfaceId=" + interfaceId + ", component=" + componentId + ", slot=" + slotId);
            return;
        }
        log.debug("Dialogue Interface: " + script.getClass().getSimpleName() + ", interfaceId=" + interfaceId + ", component=" + componentId + ", slot=" + slotId);
        script.handleComponentClick(player, interfaceId, componentId, slotId, -1, -1, "");
    }

    public ResumePauseButtonEvent(int interfaceId, int componentId, int slotId) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.slotId = slotId;
    }
}

package com.zenyte.game.model.ui;

import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.component.ComponentDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.zenyte.game.GameConstants.DEV_DEBUG;

public enum ButtonAction {
    ;

    private static final Logger logger = LoggerFactory.getLogger(ButtonAction.class);

    public static final Int2ObjectMap<UserInterface> interfaces = new Int2ObjectOpenHashMap<>();

    public static void handleComponentAction(final Player player, final int interfaceId, final int componentId,
                                             final int slotId, final int itemId, final int option, final int format) {
        if (!player.getInterfaceHandler().getVisible().inverse().containsKey(interfaceId)) {
            return;
        }
        if (!player.getControllerManager().canButtonClick(interfaceId, componentId, slotId)) {
            return;
        }
        final boolean isDebugEnabled = logger.isDebugEnabled();
        player.getInterfaceHandler().closeInput(true);
        final ComponentDefinitions defs = ComponentDefinitions.get(interfaceId, componentId);
        final String op = defs.getActions() == null || defs.getActions().length <= (option - 1) ? "null" :
                defs.getActions()[option - 1];
        /*
         * Temporarily
         */
        {
            final Interface plugin = NewInterfaceHandler.getInterface(interfaceId);
            if (plugin != null) {
                Optional<String> opt = plugin.getComponentName(componentId, slotId);
                if (opt.isEmpty()) {
                    opt = plugin.getComponentName(componentId, -1);
                }
                if (isDebugEnabled) {
                    logger.debug("[" + plugin.getClass().getSimpleName() + "] IF" + format + ": " + opt.orElse("Absent"
                    ) +
                            "(" + interfaceId + "::" + componentId + ") | Slot: " + slotId + " | Option: " + option + " |" +
                            " " +
                            "Item: " + itemId);
                }
                plugin.click(player, componentId, slotId, itemId, option);
                return;
            }
        }
        final UserInterface inter = interfaces.get(interfaceId);
        if (inter != null) {
            if (isDebugEnabled && DEV_DEBUG) {
                logger.debug("Interface(IF" + format + "):" + inter.getClass().getSimpleName() + ", interfaceId=" + interfaceId + ", component=" + componentId + ", slot=" + slotId + ", item=" + itemId + ", option=" + op + "(" + option + ")");
            }
            inter.handleComponentClick(player, interfaceId, componentId, slotId, itemId, option, op);
            return;
        }
        if (isDebugEnabled && DEV_DEBUG) {
            logger.debug("Unhandled(IF" + format + "): interfaceId=" + interfaceId + ", component=" + componentId + "," +
                    " slot=" + slotId + ", item=" + itemId + ", option=" + op + "(" + option + ")");
        }
    }

    public static void add(final Class<?> c) {
        final boolean isErrorEnabled = logger.isErrorEnabled();
        try {
            final UserInterface userInterface = (UserInterface) c.getDeclaredConstructor().newInstance();
            for (final int key : userInterface.getInterfaceIds()) {
                if (isErrorEnabled && interfaces.containsKey(key)) {
                    logger.error("<col=ff0000>FATAL: Overriding an interface handler. ID: " + key + ", Class: " + userInterface.getClass().getSimpleName());
                }
                interfaces.put(key, userInterface);
            }
        } catch (final Exception e) {
            if (isErrorEnabled)
                logger.error("Failed to add class \"" + c + "\"", e);
        }
    }

}

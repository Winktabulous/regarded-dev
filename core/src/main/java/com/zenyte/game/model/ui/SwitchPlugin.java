package com.zenyte.game.model.ui;

/**
 * @author Kris | 21/10/2018 12:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface SwitchPlugin {
    /**
     * Binds a handler to the from & to components requested. Returns this interface for chaining.
     * Method implemented inside the plugin to prevent method calls from inside plugins that don't implement the switch.
     *
     * @param fromComponent name of the component from which the item is being switched.
     * @param toComponent   name of the component to which the item is being switched.
     * @param plugin        the plugin that's executed upon switching the components.
     * @return this object, to permit chaining.
     */
    default Interface bind(final String fromComponent, final String toComponent, final Interface.SwitchHandler plugin) {
        if (!(this instanceof Interface)) throw new RuntimeException("You can only bind the plugins on interfaces.");
        final Interface inter = (Interface) this;
        final int id = inter.getComponent(fromComponent) | inter.getComponent(toComponent) << 16;
        inter.switchHandlers.put(id, plugin);
        return inter;
    }
}

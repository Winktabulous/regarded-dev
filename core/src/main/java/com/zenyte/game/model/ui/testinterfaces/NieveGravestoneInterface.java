package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 01/10/2019 | 20:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class NieveGravestoneInterface extends Interface {


    @Override
    protected void attach() {
        put(2, "Line 1");
        put(3, "Line 2");
        put(4, "Line 3");
        put(5, "Line 4");
        put(6, "Line 5");
        put(7, "Line 6");
        put(8, "Line 7");
        put(9, "Line 8");
        put(10, "Line 9");
        put(11, "Line 10");
        put(12, "Line 11");
        put(13, "Line 12");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Line 1"), "Nieve");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Line 4"), "In Loving memory of Nieve");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Line 6"), "Shield of the Gnomes");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Line 7"), "&");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Line 8"), "Master of Creatures");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Line 10"), "I have killed " + (player.getSettings().getKillsLog().getOrDefault("demonic gorilla", 0) & 0xFFFF) + " Demonic gorillas &");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Line 11"), (player.getSettings().getKillsLog().getOrDefault("tortured gorilla", 0) & 0xFFFF) + " Tortured gorillas in Nieve's honour.");
    }

    @Override
    protected void build() {

    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.NIEVE_GRAVESTONE;
    }
}

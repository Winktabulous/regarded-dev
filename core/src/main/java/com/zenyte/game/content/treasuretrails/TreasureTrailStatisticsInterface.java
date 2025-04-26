package com.zenyte.game.content.treasuretrails;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.world.entity.player.Player;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 04/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TreasureTrailStatisticsInterface extends Interface {
    @Override
    protected void attach() {
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentText(getInterface(), 2, "Treasure trails completed:");
        final ClueLevel[] array = ClueLevel.values;
        int total = 0;
        for (int i = 0; i < array.length; i++) {
            final String tier = array[i].toString().toLowerCase();
            final int count = player.getNumericAttribute("completed " + tier + " treasure trails").intValue();
            total += count;
            dispatcher.sendComponentText(getInterface(), 4 + i, StringFormatUtil.formatString(tier) + ": " + count);
        }
        dispatcher.sendComponentText(getInterface(), 11, "Total: " + total);
        final String rank = total <= 100 ? "Beginner" : total <= 200 ? "Novice" : total <= 400 ? "The Explorer" : total <= 600 ? "Treasure Hunter" : total <= 1000 ? "Expert" : total <= 1500 ? "Master" : "Legendary";
        dispatcher.sendComponentText(getInterface(), 13, "Your rank is: " + rank);
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.TREASURE_TRAIL_STATISTICS;
    }
}

package com.zenyte.game.content.event.easter2020;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.dialogue.PlayerChat;

/**
 * @author Corey
 * @since 30/03/2020
 */
@SkipPluginScan
public class NoticeboardInterface extends Interface {
    @Override
    protected void attach() {
    }

    @Override
    public void open(Player player) {
        if (!SplittingHeirs.progressedAtLeast(player, Stage.CHECK_TODO)) {
            player.getDialogueManager().start(new PlayerChat(player, "I should speak with the Easter Bunny's son " +
                    "before touching anything."));
            return;
        }
        SplittingHeirs.advanceStage(player, Stage.BIRD_ASLEEP);
        player.getInterfaceHandler().sendInterface(this);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentText(getInterface(), 3, "To-do:");
        dispatcher.sendComponentText(getInterface(), 6, Task.FEED_EASTER_BIRD.interfaceString(player) + "  -  <str>Order nuts</str>");
        dispatcher.sendComponentText(getInterface(), 7, "<str>Order bird food</str>");
        dispatcher.sendComponentText(getInterface(), 8, Task.FIX_INCUBATOR.interfaceString(player) + "  -  <str>Order egg paint</str>");
        dispatcher.sendComponentText(getInterface(), 9, "<str>Tidy office</str>  -  <str>Take holiday</str>");
        dispatcher.sendComponentText(getInterface(), 10, Task.RETRAIN_SQUIRRELS.interfaceString(player) + "  -  " + Task.ROUND_UP_IMPLINGS.interfaceString(player));
        dispatcher.sendComponentText(getInterface(), 11, "<str>Eat chocolate</str>  -  <str>Send dad birthday card</str>");
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.EASTER_NOTICEBOARD;
    }
}

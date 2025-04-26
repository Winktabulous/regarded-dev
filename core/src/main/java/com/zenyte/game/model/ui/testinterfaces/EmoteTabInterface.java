package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import mgi.types.config.enums.Enums;

import java.util.Optional;

import static com.zenyte.game.content.event.halloween2019.HalloweenUtils.COMPLETED_VARP;
import static com.zenyte.game.world.entity.player.Emote.GIVE_THANKS_VARP;

/**
 * @author Tommeh | 28-10-2018 | 15:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public class EmoteTabInterface extends Interface {

    static {
        VarManager.appendPersistentVarp(COMPLETED_VARP);
        VarManager.appendPersistentVarp(GIVE_THANKS_VARP);
    }

    @Override
    protected void attach() {
        put(2, "Play emote");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Play emote"), 0, Enums.EMOTES_ENUM.getSize(), AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("Play emote", (player, slotId, itemId, option) -> {
            if (player.getNumericTemporaryAttribute("emote_delay").longValue() > Utils.currentTimeMillis()) {
                return;
            }
            Optional<String> emotesName = Enums.EMOTES_ENUM.getValue(slotId);
            if (emotesName.isEmpty()) {
                return;
            }
            final String name = emotesName.get().toLowerCase().replaceAll(" ", "_");
            if (!player.getEmotesHandler().isUnlocked(Emote.MAP.get(name))) {
                player.sendMessage("You haven't unlocked this emote yet!");
                return;
            }
            player.stopAll();
            Emote.play(player, name);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.EMOTE_TAB;
    }

}

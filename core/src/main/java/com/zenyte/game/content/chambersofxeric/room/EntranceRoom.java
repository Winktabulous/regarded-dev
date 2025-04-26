package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * Handles the very first room of raids.
 *
 * @author Kris | 16. nov 2017 : 2:11.18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class EntranceRoom extends RaidArea {

    /**
     * The walking steps sound effect that is played when you walk into the raid.
     */
    private static final SoundEffect sound = new SoundEffect(2277);

    public EntranceRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public boolean canPass(final Player player, final WorldObject object) {
        if (raid.getStage() == 0) {
            if (raid.getParty().getPlayer().equals(player.getUsername())) {
                player.getDialogueManager().start(new PlainChat(player, "Use the controls on the " + Colour.RS_RED.wrap("side-panel") + "" +
                        "<br><br>when you are ready to begin the raid."));
                player.sendSound(sound);
                player.getInterfaceHandler().openGameTab(GameTab.CLAN_CHAT_TAB);
            } else {
                player.sendMessage("The party leader must start the party before you may pass.");
            }
            return false;
        }
        return true;
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Entrance";
    }

}

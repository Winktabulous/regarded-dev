package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.gravestone.GravestoneExt;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.var.VarCollection;

/**
 * @author Kris | 07/01/2019 15:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FixedPaneInterface extends Interface {

    @Override
    protected void attach() {
        put(39, "Gravestone info");
        put(47, "Open Game Noticeboard");
        put(64, "Character Summary");
        put(68, "Toggle spell filtering");
    }

    @Override
    public void open(Player player) {
        throw new IllegalStateException("Panes cannot be opened as interfaces.");
    }

    @Override
    protected void build() {
        bind("Character Summary", (player, slotId, itemId, option) -> VarCollection.COMP_PROGRESS.updateSingle(player));
        bind("Toggle spell filtering", (player, slotId, itemId, option) -> {
            if (option == 2) {
                player.getSettings().toggleSetting(Setting.SPELL_FILTERING_DISABLED);
            } else if (option == 3) {
                player.getTeleportsManager().attemptTeleport(player.getTeleportsManager().getPreviousDestination());
            }
        });
        bind("Gravestone info", GravestoneExt.INSTANCE::informGravestone);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.FIXED_PANE;
    }
}

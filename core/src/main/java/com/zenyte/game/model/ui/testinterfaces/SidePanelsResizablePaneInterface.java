package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.gravestone.GravestoneExt;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.var.VarCollection;

/**
 * @author Tommeh | 18-1-2019 | 15:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SidePanelsResizablePaneInterface extends Interface {

    @Override
    protected void attach() {
        put(20, "Gravestone info");
        put(38, "Open Game Noticeboard");
        put(53, "Character Summary");
        put(57, "Toggle spell filtering");
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
        return GameInterface.SIDE_PANELS_RESIZABLE_PANE;
    }
}

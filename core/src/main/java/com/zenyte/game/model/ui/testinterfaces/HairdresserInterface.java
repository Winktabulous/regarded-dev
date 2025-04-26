package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.plugins.dialogue.HairdresserD;
import mgi.types.config.identitykit.BeardStyle;
import mgi.types.config.identitykit.HairStyle;

/**
 * @author Tommeh | 28-10-2018 | 18:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class HairdresserInterface extends Interface {
    @Override
    protected void attach() {
        put(2, "Select hair/beard style");
        put(8, "Select hair/beard colour");
        put(9, "Finish make-over");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Select hair/beard style", (player, slotId, itemId, option) -> {
            boolean haircut = player.getVarManager().getBitValue(4146) == 1;
            if (!haircut && !player.getAppearance().isMale()) {
                haircut = true;
            }
            player.getVarManager().sendVar(261, slotId);
            player.getTemporaryAttributes().put(haircut ? "SelectedHairStyle" : "SelectedBeardStyle", slotId);
        });
        bind("Select hair/beard colour", (player, slotId, itemId, option) -> {
            boolean haircut = player.getVarManager().getBitValue(4146) == 1;
            if (!haircut && !player.getAppearance().isMale()) {
                haircut = true;
            }
            player.getVarManager().sendVar(263, slotId);
            player.getTemporaryAttributes().put(haircut ? "SelectedHairColour" : "SelectedBeardColour", slotId);
        });
        bind("Finish make-over", player -> {
            boolean haircut = player.getVarManager().getBitValue(4146) == 1;
            if (!haircut && !player.getAppearance().isMale()) {
                haircut = true;
            }
            final int slot = player.getNumericTemporaryAttribute(haircut ? "SelectedHairStyle" : "SelectedBeardStyle").intValue();
            final int style = haircut ? HairStyle.getStyle(slot, player.getAppearance().isMale()) : BeardStyle.getStyle(slot);
            final int colour = player.getNumericTemporaryAttribute(haircut ? "SelectedHairColour" : "SelectedBeardColour").intValue();
            final int npcId = player.getNumericTemporaryAttribute("HairdresserNPCId").intValue();
            if (style == -1 || slot == -1 || colour == -1) {
                player.getDialogueManager().start(new NPCChat(player, npcId, "You must select a hair style and colour first!"));
                return;
            }
            player.getAppearance().modifyAppearance((byte) (haircut ? 0 : 1), (short) style);
            player.getAppearance().modifyColour((byte) 0, (byte) colour);
            player.getInventory().deleteItem(HairdresserD.PRICE);
            player.getAchievementDiaries().update(FaladorDiary.GET_A_HAIRCUT);
            player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
            if (npcId == -1) {
                return;
            }
            player.getDialogueManager().start(new NPCChat(player, npcId, "Hope you like the new look!"));
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.HAIRDRESSER;
    }
}

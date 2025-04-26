package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.plugins.dialogue.varrock.ThessaliaD;
import mgi.types.config.identitykit.IdentityKitDefinitions;

/**
 * @author Tommeh | 28-10-2018 | 15:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ThessaliaMakeOverInterface extends Interface {
    @Override
    protected void attach() {
        put(3, "Select body style");
        put(5, "Select arms style");
        put(7, "Select legs style");
        put(13, "Select body/legs colour");
        put(14, "Finish make-over");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), 13, 0, 28, AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendComponentText(getInterface(), 14, "CONFIRM - (" + ThessaliaD.PRICE.getAmount() + " coins)");
    }

    @Override
    protected void build() {
        bind("Select body style", (player, slotId, itemId, option) -> {
            player.getVarManager().sendVar(261, slotId);
            player.getTemporaryAttributes().put("SelectedBodyStyle", slotId);
        });
        bind("Select legs style", (player, slotId, itemId, option) -> {
            player.getVarManager().sendVar(261, slotId);
            player.getTemporaryAttributes().put("SelectedLegsStyle", slotId);
        });
        bind("Select arms style", (player, slotId, itemId, option) -> {
            player.getVarManager().sendVar(262, slotId);
            player.getTemporaryAttributes().put("SelectedArmStyle", slotId);
        });
        bind("Select body/legs colour", (player, slotId, itemId, option) -> {
            final boolean topwear = player.getVarManager().getBitValue(3945) == 0;
            player.getVarManager().sendVar(263, slotId);
            player.getTemporaryAttributes().put(topwear ? "SelectedBodyColour" : "SelectedLegsColour", slotId);
        });
        bind("Finish make-over", (player, slotId, itemId, option) -> {
            final boolean topwear = player.getVarManager().getBitValue(3945) == 0;
            final int style = player.getNumericTemporaryAttribute(topwear ? "SelectedBodyStyle" : "SelectedLegsStyle").intValue();
            final int armStyle = player.getNumericTemporaryAttribute("SelectedArmStyle").intValue();
            final int colour = player.getNumericTemporaryAttribute(topwear ? "SelectedBodyColour" : "SelectedLegsColour").intValue();
            final int npcId = player.getNumericTemporaryAttribute("ThessaliaNPCId").intValue();
            if ((style == -1 || colour == -1 || armStyle == -1 && topwear) || (style == -1 || colour == -1 && !topwear)) {
                player.sendMessage("You must make a selection from " + (topwear ? "three" : "two") + " categories.");
                return;
            }
            if (topwear) {
                player.getAppearance().modifyAppearance((byte) 3, (short) IdentityKitDefinitions.getArmstyle(armStyle));
            }
            player.getAppearance().modifyAppearance((byte) (topwear ? 2 : 5), (short) (topwear ? IdentityKitDefinitions.getBodystyle(style) : IdentityKitDefinitions.getLegsstyle(style)));
            player.getAppearance().modifyColour((byte) (topwear ? 1 : 2), (byte) colour);
            player.getInventory().deleteItem(ThessaliaD.PRICE);
            player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
            if (npcId == -1) {
                return;
            }
            player.getDialogueManager().start(new NPCChat(player, npcId, "Wow! Good choice! You're looking great!"));
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.THESSALIA_MAKEOVER;
    }
}

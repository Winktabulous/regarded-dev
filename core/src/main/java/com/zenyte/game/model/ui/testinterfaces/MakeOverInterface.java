package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.plugins.dialogue.MakeOverMageD;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 28-10-2018 | 19:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MakeOverInterface extends Interface {
    @Override
    protected void attach() {
        put(2, "Select male");
        put(6, "Select female");
        put(9, "Select skin colour");
        put(10, "Finish make-over");
    }

    @Override
    public void open(Player player) {
        player.getTemporaryAttributes().remove("SelectedGender");
        player.getTemporaryAttributes().remove("SelectedSkinColour");
        player.getVarManager().sendVar(261, player.getAppearance().isMale() ? 0 : 1);
        player.getVarManager().sendVar(262, player.getAppearance().getColours()[4]);
        player.getVarManager().sendBit(4803, 1);
        player.getVarManager().sendBit(4804, 1);
        player.getVarManager().sendBit(6007, 1);
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Finish make-over"), "CONFIRM (" + MakeOverMageD.PRICE.getAmount() + " coins)");
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Select skin colour"), 0, 12, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("Select skin colour", (player, slotId, itemId, option) -> {
            final int value = slotId == 0 ? 7 : slotId >= 8 && slotId <= 12 ? slotId : slotId - 1;
            player.getVarManager().sendVar(262, value);
            player.getTemporaryAttributes().put("SelectedSkinColour", value);
        });
        bind("Finish make-over", player -> {
            final boolean male = player.getBooleanTemporaryAttribute("SelectedGender");
            final int skinColour = player.getNumericTemporaryAttributeOrDefault("SelectedSkinColour", player.getAppearance().getColours()[4]).intValue();
            if (male == player.getAppearance().isMale() && skinColour == player.getAppearance().getColours()[4]) {
                player.sendMessage("You haven't changed anything yet.");
                return;
            }
            final MakeOverInterface.SkinColour colour = SkinColour.get(skinColour);
            if (colour != null && !player.getMemberRank().equalToOrGreaterThan(colour.getRank())) {
                player.sendMessage("You need to be " + Utils.getAOrAn(colour.getRank().toString()) + " " + colour.getRank() + " to change into this skin colour!");
                return;
            }
            player.getAppearance().modifyColour((byte) 4, (byte) skinColour);
            player.getInventory().deleteItem(MakeOverMageD.PRICE);
            player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.MAKEOVER;
    }


    private enum SkinColour {
        BLACK(9, MemberRank.EXPANSION),
        WHITE(10, MemberRank.EXPANSION),
        GREEN(8, MemberRank.EXTREME),
        TURQOISE(11, MemberRank.RESPECTED),
        PURPLE(12, MemberRank.LEGENDARY);
        private static final SkinColour[] all = values();
        private static final Map<Integer, SkinColour> map = new HashMap<>(all.length);

        static {
            for (final MakeOverInterface.SkinColour colour : all) {
                map.put(colour.index, colour);
            }
        }

        private final int index;
        private final MemberRank rank;

        SkinColour(int index, MemberRank rank) {
            this.index = index;
            this.rank = rank;
        }

        public static SkinColour get(final int index) {
            return map.get(index);
        }

        public int getIndex() {
            return index;
        }

        public MemberRank getRank() {
            return rank;
        }
    }
}

package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import com.zenyte.utils.TextUtils;

/**
 * @author Kris | 29/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RingOf3rdAge extends ItemPlugin {

    private enum Transformation {
        WAND(8639), BOW(8640), SWORD(8641), CAPE(8642), RANGE_TOP(8645), RANGE_LEGS(8646), RANGE_COIF(8647), VAMBRACES(8648), ROBE_TOP(8649), ROBE_LEGGINGS(8650), MAGE_HAT(8651), AMULET(8652), PLATEBODY(8653), PLATESKIRT(8655), PLATELEGS(8654), FULL_HELMET(8656), KITESHIELD(8657), DRUIDIC_TOP(8660), DRUIDIC_ROBE_BOTTOMS(8661), DRUIDIC_STAFF(8662), DRUIDIC_CLOAK(8663), AXE(8658), PICKAXE(8659), RING(8664);
        //No cancellation, What would you like to be?
        private final int npcId;
        private static final Transformation[] values = values();
        private static final String[] options;

        static {
            options = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                options[i] = TextUtils.capitalizeEnum(values[i].toString());
            }
        }

        Transformation(int npcId) {
            this.npcId = npcId;
        }
    }

    @Override
    public void handle() {
        bind("Wear", (player, item, container, slotId) -> {
            if (player.getDuel() != null && player.getDuel().inDuel()) {
                player.sendMessage("You can't do this during a duel.");
                return;
            }
            if (player.getTemporaryAttributes().get("greegree") != null) {
                player.sendMessage("You can't do that as a monkey!");
                return;
            }
            player.getInterfaceHandler().closeInterfaces();
            player.getDialogueManager().start(new OptionsMenuD(player, "What would you like to be?", Transformation.options) {
                @Override
                public void handleClick(int slotId) {
                    final RingOf3rdAge.Transformation formation = Transformation.values[slotId];
                    player.getInterfaceHandler().closeInterfaces();
                    player.getAppearance().transform(formation.npcId);
                    player.getInterfaceHandler().sendInterface(GameInterface.UNMORPH_TAB);
                    player.resetWalkSteps();
                    player.getPacketDispatcher().resetMapFlag();
                    player.getAppearance().setRenderAnimation(new RenderAnimation(-1, -1, -1, -1, -1, -1, -1));
                    player.setAnimation(Animation.STOP);
                }
                @Override
                public boolean cancelOption() {
                    return false;
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.RING_OF_3RD_AGE};
    }
}

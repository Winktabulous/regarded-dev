package com.zenyte.plugins.object;

import com.zenyte.game.GameConstants;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 15. nov 2017 : 21:15.59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ChambersOfXericEntrance implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (!GameConstants.CHAMBERS_OF_XERIC) {
            player.sendMessage("Chambers of Xeric have been disabled for the time being.");
            return;
        }
        if (player.getAttributes().get("has entered chambers of xeric") == null) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    plain("WARNING!<br>" + "Items left in a dungeon " + Colour.RS_RED.wrap("cannot") + " be retrieved.<br>" + "Participants " + Colour.RS_RED.wrap("may be ejected") + " at any time.<br>" + "Don't risk dropping items that you want to keep.");
                    options(TITLE, new DialogueOption("Accept warning and proceed.", () -> {
                        player.getAttributes().put("has entered chambers of xeric", true);
                        // Call itself as the raid may have already dissipated, goes through the same normal checks again, except ignores the warning.
                        handleObjectAction(player, object, name, optionId, option);
                    }), new DialogueOption("Cancel."));
                }
            });
            return;
        }
        final ClanChannel channel = player.getSettings().getChannel();
        if (channel == null) {
            player.sendMessage("You need to be in a clan chat to do this.");
            return;
        }
        if (channel.getRaidParty() != null && channel.getRaidParty().getRaid() != null) {
            if (channel.getRaidParty().getRaid().getStage() != 0) {
                player.sendMessage("The raid has already been started.");
                return;
            }
            player.getPrivateStorage().resetInaccessibleItems();
            if (!player.getPrivateStorage().getContainer().getItems().isEmpty()) {
                player.sendMessage("You must retrieve all your items from the private storage before you can re-enter the Chambers of Xeric.");
                player.getPrivateStorage().open(-1);
                return;
            }
            channel.getRaidParty().getRaid().enterRaid(player);
        } else {
            player.sendMessage("You need to make a raid party first.");
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CHAMBERS_OF_XERIC };
    }
}

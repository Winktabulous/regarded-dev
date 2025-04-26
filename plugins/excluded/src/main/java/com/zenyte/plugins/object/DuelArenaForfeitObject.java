package com.zenyte.plugins.object;

import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.content.minigame.duelarena.DuelSetting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 30-11-2018 | 19:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DuelArenaForfeitObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Duel duel = player.getDuel();
        if (duel == null) {
            return;
        }
        final Player opponent = duel.getOpponent();
        if (opponent == null) {
            return;
        }
        if (duel.isCountdown()) {
            player.sendMessage("You cannot forfeit during the countdown.");
            return;
        }
        if (duel.hasRule(DuelSetting.NO_FORFEIT)) {
            player.sendMessage("You cannot forfeit this duel.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("Are you sure you wish to forfeit?", "Yes", "No").onOptionOne(() -> {
                    duel.finishDuel(opponent, player);
                    duel.sendSpoils(opponent);
                    duel.registerDuelHistory(opponent, player);
                    player.setDuel(null);
                    opponent.setDuel(null);
                    // player.getPacketDispatcher().sendComponentText(Duel.WINNINGS_INTERFACE, 16, "You forfeit!");
                });
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TRAPDOOR_3203, ObjectId.ENTRANCE_3111 };
    }
}

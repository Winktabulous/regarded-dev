package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 12:34 - 24/07/2019
 */
public class DoorsOfDinh implements ObjectAction {

    private static final String ENTER = "Enter";

    private static final String PEEK = "Peek";

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!option.equalsIgnoreCase(PEEK) && !option.equalsIgnoreCase(ENTER)) {
            throw new RuntimeException("Invalid option: " + option);
        }
        if (option.equalsIgnoreCase(PEEK)) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    final int noOfPlayers = Wintertodt.getAmountOfPlayers();
                    final String prefix = noOfPlayers == 1 ? "is" : "are";
                    final String affix = noOfPlayers == 0 ? "no" : Integer.toString(noOfPlayers);
                    final String suffix = noOfPlayers == 1 ? "player" : "players";
                    final String players = prefix + " " + affix + " " + suffix;
                    plain("The Wintertodt has " + Wintertodt.getCurrentEnergyPercentage() + "% energy left. There " + players + " within the prison.");
                }
            });
            return;
        }
        final boolean enter = !player.inArea("Wintertodt");
        if (enter && Wintertodt.isDisabled()) {
            player.sendMessage("Wintertodt has been disabled.");
            return;
        }
        final int x = enter ? 1627 + Utils.random(0, 6) : 1628 + Utils.random(0, 4);
        final int y = enter ? 3977 + Utils.random(0, 7) : 3957 + Utils.random(0, 6);
        if (!enter && !Wintertodt.betweenRounds()) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Are you sure you want to leave?", new DialogueOption("Leave and lose all progress.", () -> new FadeScreen(player, () -> player.setLocation(new Location(x, y))).fade(2)), new DialogueOption("Stay.", this::finish));
                }
            });
        } else {
            if (player.getSkills().getLevelForXp(SkillConstants.FIREMAKING) < 50) {
                player.getDialogueManager().start(new Dialogue(player) {

                    @Override
                    public void buildDialogue() {
                        plain("You require at least 50 Firemaking to take on the Wintertodt.");
                    }
                });
                return;
            }
            new FadeScreen(player, () -> player.setLocation(new Location(x, y))).fade(2);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOORS_OF_DINH };
    }
}

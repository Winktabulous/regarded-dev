package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 07/05/2019 | 16:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ChallengeHeadmaster extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        npc("Hi there, I'm the Challenge Headmaster. What can I do for you?");
                        options(TITLE, "Who are you?", "What are daily challenges?", "I'd like to view my challenges.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10)).onOptionThree(() -> viewChallenges(player));
                        npc(5, "I'm the Challenge Headmaster. I'm basically the one that sees over the daily challenges. You can come to me whenever you complete a daily challenge for rewards.").executeAction(() -> setKey(2));
                        npc(10, "Everyone gets a daily challenge everyday. Each daily challenge has an objective, depending on how much you have progressed in the game the objective will be harder.");
                        npc("This also means the harder the objective, the better rewards you will get.").executeAction(() -> setKey(2));
                    }
                });
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
                // npc.setInteractingWith(player);
            }
        });
        bind("View Challenges", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                viewChallenges(player);
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
                // npc.setInteractingWith(player);
            }
        });
    }

    private void viewChallenges(final Player player) {
        player.addTemporaryAttribute("daily_challenge_claimable", 1);
        GameInterface.DAILY_CHALLENGES_OVERVIEW.open(player);
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.CHALLENGE_HEADMASTER };
    }
}

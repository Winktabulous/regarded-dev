package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DontKnowWhat extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new Dialogue(player, npc.getId()) {

                    @Override
                    public void buildDialogue() {
                        player("Am I allowed to pass through here?");
                        npc("Certainly so, human.", Expression.WEISS_TROLL_NORMAL).executeAction(() -> {
                            player.setFaceEntity(null);
                            player.setRunSilent(3);
                            player.lock(3);
                            player.addWalkSteps(2866, 3947, 1, false);
                            if (player.getY() >= 3948) {
                                player.addWalkSteps(2867, 3947, 3, false);
                            } else {
                                player.addWalkSteps(2865, 3948, 3, false);
                            }
                        });
                    }
                });
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new TileEvent(player, new TileStrategy(player.getY() >= 3948 ? new Location(2865, 3948, 0) : new Location(2867, 3947, 0)), () -> execute(player, npc)));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 8469, NpcId.DONT_KNOW_WHAT };
    }
}

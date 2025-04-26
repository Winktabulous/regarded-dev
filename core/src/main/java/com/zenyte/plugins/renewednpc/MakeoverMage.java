package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.MakeOverMageD;

/**
 * @author Kris | 25/11/2018 20:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MakeoverMage extends NPCPlugin {

    private static final Location HOME_MAKE_OVER_MAGE = new Location(3095, 3505, 0);

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                player.getDialogueManager().start(new MakeOverMageD(player, npc, true));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
                if (npc.getRadius() > 0)
                    npc.setInteractingWith(player);
            }
        });
        bind("Makeover", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                GameInterface.CHARACTER_DESIGN.open(player);
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
                if (npc.getRadius() > 0)
                    npc.setInteractingWith(player);
            }
        });
        /*bind("Skin Colour", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                GameInterface.MAKEOVER.open(player);
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
                if (npc.getLocation().getPositionHash() != HOME_MAKE_OVER_MAGE.getPositionHash()) {
                    npc.setInteractingWith(player);
                }
            }
        });*/
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                1306, 1307, 8487, 10021
        };
    }
}

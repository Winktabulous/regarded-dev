package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.multicannon.DwarfMultiCannon;
import com.zenyte.game.content.multicannon.DwarfMultiCannonType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 20:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Nulodion extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final DwarfMultiCannon cannon = player.getDwarfMulticannon();
                if (!cannon.isDecayed()) {
                    npc("Go away, I'm busy.");
                } else {
                    npc("It seems like you've lost your Dwarf Multicannon.. Please be more careful the next time.").executeAction(() -> {
                        final byte stage = cannon.getSetupStage();
                        DwarfMultiCannonType type = cannon.getType();
                        if (type == null) {
                            type = DwarfMultiCannonType.REGULAR;
                            cannon.setType(DwarfMultiCannonType.REGULAR);
                        }

                        cannon.take(stage == 0 ? type.getBaseLoc() : stage == 1 ? type.getCannonLoc() : stage == 2 ? type.getBarrelsLoc() : type.getCannonLoc(), false);
                    });
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.NULODION };
    }
}

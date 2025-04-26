package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Tommeh | 12/11/2019 | 00:16
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class IkkleHydraD extends Dialogue {
    public IkkleHydraD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int random = Utils.random(3);
        switch (random) {
        case 0: 
            player("So... you're alchemical; does that mean I can turn you into gold?");
            npc("No, I like my form as it is.", Expression.IKKLE_HYDRA);
            player("But... I love gold.");
            npc("I'll turn to gold when drakes fly.", Expression.IKKLE_HYDRA);
            return;
        case 1: 
            player("So how was that chemical bath?!");
            npc("Nasty! I don't want to do that again.", Expression.IKKLE_HYDRA);
            return;
        case 2: 
            player("Which creature is cooler, Drakes, Hydras or Wyrms?");
            npc("I may be biased in this situation... bit silly to ask me isn't it?", Expression.IKKLE_HYDRA);
            player("Alright alright... don't lose your head.");
            return;
        }
    }
}

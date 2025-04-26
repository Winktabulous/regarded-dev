package com.zenyte.game.content.sandstorm.dialogue;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 20 2020
 */
public class DrewIntroductionDialogue extends Dialogue {
    public DrewIntroductionDialogue(@NotNull final Player player, @NotNull final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        npc("My name is Drew and I man this here grinding machine. I call her Sandstorm!");
        npc("I can also look after any buckets ya want me to.");
        npc("Me and Sandstorm go way back, we started off back in the desert mining camp, I was a slave and I had the honour of looking after Sandstorm.");
        player("How did you and Sandstorm get out of the mining camp?");
        npc("Well, because I was in charge of looking after Sandstorm, I was able to look at the inner workings.");
        npc("While the guards weren't looking, I would take apart Sandstorm piece by piece and place the pieces in boxes and barrels, when Sandstorm inners were all gutted, " +
                "I tricked the cart driver by telling him cart jokes.");
        npc("So, he didn't bother to check his cargo and I was able to board as he was driving off.");
        npc("Anyway, that's all in the past. Can I help ya with anything else?").executeAction(() -> player.getDialogueManager().start(new DrewOptionDialogue(player, npc)));
    }
}

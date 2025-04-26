package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.SplittingHeirs;
import com.zenyte.game.content.event.easter2020.Stage;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.dialogue.PlayerChat;

/**
 * @author Corey
 * @since 08/04/2020
 */
@SkipPluginScan
public class ImplingIncubatorWorker extends NPCPlugin {
    
    @Override
    public void handle() {
        
        bind("Talk-to", ((player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (!SplittingHeirs.progressedAtLeast(player, Stage.FIX_INCUBATOR)) {
                    player.getDialogueManager().start(new PlayerChat(player, "I should speak with the Easter Bunny Jr first and see what he wants me to do."));
                    return;
                }
    
                if (SplittingHeirs.progressedAtLeast(player, Stage.INCUBATOR_FIXED_SPEAK_TO_BUNNY_JR)) {
                    player("Still working?");
                    npc("Yes, thank you!", Expression.HIGH_REV_HAPPY);
                    return;
                } else if (SplittingHeirs.progressedAtLeast(player, Stage.SPOKEN_WITH_INCUBATOR_WORKER)) {
                    player("It's taking shape, just a few more parts");
                    npc("Yep, looking good. Just find all the parts, put the main body together, then connect up the peripherals.", Expression.HIGH_REV_NORMAL);
                    return;
                }
    
                player("Hello there. What's this?");
                npc("It's a broken incubator.", Expression.HIGH_REV_NORMAL);
                player("That's a shame, but I'm going to fix it.");
                npc("That would be good. I'm not sure where the parts are, but there's a blueprint in the lazy rabbits room, and the parts could be scattered anywhere in this factory. Probably hidden in a box or something.", Expression.HIGH_REV_NORMAL);
                player("Who did this?");
                npc("The last chocatrice that hatched went a bit...nuts...and ripped the thing to bits. Then, of course the implings and squirrels were bored, so they hid the parts.", Expression.HIGH_REV_NORMAL);
                player("Why didn't you stop them?");
                npc("Do I look big enough to stop a gang of creatures who can crack nuts with their teeth?", Expression.HIGH_REV_WONDERING);
                player("True, true. I'll look for the parts.")
                        .executeAction(() -> SplittingHeirs.advanceStage(player, Stage.SPOKEN_WITH_INCUBATOR_WORKER));
            }
        })));
        
    }
    
    @Override
    public int[] getNPCs() {
        return new int[]{EasterConstants.IMPLING_INCUBATOR_WORKER};
    }
    
    /*

EASTER BUNNY JR: You woke me up again! Why does this hunk of junk make so much noise?
npc("Junk?", Expression.HIGH_REV_NORMAL);
player("This is the chocatrice egg incubator.");
EASTER BUNNY JR: Oh? What does it do?
npc("Would you like to get in? I'll show you...", Expression.HIGH_REV_NORMAL);
player("Ahh...I think we'll skip on that, thanks.");
-------------------------------------------------------
[*]This machine is one-of-a-kind.
player("This machine is one-of-a-kind.");
EASTER BUNNY JR: Yes, but what use is it?
player("It's the Incubator 9000: a very expensive and very delicate piece of machinery. It makes it possible to incubate the eggs of the chocatrice.");
EASTER BUNNY JR: Wow. Chocotastic! Who would have thought, a machine that produces chocolate birds, and a bird that produces chocolate?
player("You like it?");
EASTER BUNNY JR: Yes, very inspiring, and just in time for my mid-morning nap too. Oh, as you're intent on cleaning this place up, could you just remove the vermin over there?
player("Vermin?");
EASTER BUNNY JR: Yes, the ones over on that machine with the fluffy tails.
player("Fast as greased rabbit.");



[*] It incubates chocatrice eggs.
[*]It's for roasting nuts.
[*]I give up!
-------------------------------------------------------
     */
    
}

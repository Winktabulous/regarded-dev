package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.content.event.easter2020.*;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.plugins.SkipPluginScan;

/**
 * @author Corey
 * @since 28/03/2020
 */
@SkipPluginScan
public class EasterBunnyJr extends NPCPlugin {
    private static final int LAZY_OF_YOU = 20;
    private static final int CLEAN_THIS_PLACE = 30;
    private static final int WHY_ARENT_YOU_FIXING = 40;
    private static final int CANT_HANG_AROUND_ALL_DAY = 50;
    private static final int GOLD_GOOSE = 100;
    private static final int INTELLIGENT_LIFEFORM = 110;
    private static final int DUMB_PARROT = 120;
    private static final int GIVE_UP = 130;
    private static final int MASTER_ARTIST = 150;
    private static final int TWICE_ITS_SIZE = 160;
    private static final Location chairBunnyLocation = new Location(2191, 4396, 0);

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                if (!npc.getLocation().matches(chairBunnyLocation)) {
                    npc.setInteractingWith(player);
                }
                player.getDialogueManager().start(new Dialogue(player, EasterConstants.EASTER_BUNNY_JR) {
                    @Override
                    public void buildDialogue() {
                        final Stage progress = SplittingHeirs.getProgress(player);
                        if (progress == Stage.BIRD_FED_SPEAK_TO_BUNNY_JR) {
                            npc("What's all this racket? I'm trying to sleep! Is that a bird?", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.EASTER_BIRD_NPC, "Squawk, nuts!", Expression.EASTER_BIRD_CHATTY);
                            player("Umm...yes, but this isn't just any bird.");
                            npc("Oh? Doesn't look like anything special to me.", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.EASTER_BIRD_NPC, "Squawk, melty head, melty head!", Expression.EASTER_BIRD_CHATTY);
                            options(TITLE, new DialogueOption("It's a goose that lays eggs made of gold.", key(GOLD_GOOSE)), new DialogueOption("It's a highly intelligent life form, which provides profit through chocolate.", key(INTELLIGENT_LIFEFORM)), new DialogueOption("It's just a dumb parrot that has a chocolatey gift.", key(DUMB_PARROT)), new DialogueOption("I give up!", key(GIVE_UP)));
                            {
                                player(GOLD_GOOSE, "It's a goose that lays eggs made of gold.");
                                plain("The Easter Bunny's son stares at the Easter Bird, then at you.");
                                npc("Are you feeling okay? Not been nibbling at the special chocolate have you? Only." +
                                        "..that isn't a goose.", Expression.EASTER_BUNNY_NORMAL);
                                player("It was worth a try...");
                                options(TITLE, new DialogueOption("It's a highly intelligent life form, which " +
                                        "provides profit through chocolate.", key(INTELLIGENT_LIFEFORM)), new DialogueOption("It's just a dumb parrot that has a chocolatey gift.", key(DUMB_PARROT)), new DialogueOption("I give up!", key(GIVE_UP)));
                            }
                            {
                                player(INTELLIGENT_LIFEFORM, "It's a highly intelligent life form, which provides " +
                                        "profit through chocolate.");
                                npc("Hmmm...profit you say? How does it do that?", Expression.EASTER_BUNNY_NORMAL);
                                player("For the very cheap price of seeds, you're getting your raw ingredients " +
                                        "practically free. You can use the eggs as they are, or melt them down for " +
                                        "other chocolatey uses.");
                                npc("Interesting...cheap chocolate. It doesn't mean I shouldn't get more beauty sleep" +
                                        " though - and I would if I couldn't hear those implings scratching around in" +
                                        " the tunnels. See if you can catch them.", Expression.EASTER_BUNNY_NORMAL);
                                player("How do I do that?");
                                npc("Dad keeps nets and jars in his office for when they go running off, it's quite " +
                                        "tiresome. Apparently he catches them in the net then stuffs them in a jar. " +
                                        "Anyway, I'm going to take a nap.", Expression.EASTER_BUNNY_NORMAL).executeAction(() -> SplittingHeirs.advanceStage(player, Stage.GATHER_IMPLINGS));
                                player("Run, rabbit, run!");
                            }
                            {
                                player(DUMB_PARROT, "It's just a dumb parrot that has a chocolatey gift.");
                                npc("And what's so special about that? You dragged me out of my room for that?", Expression.EASTER_BUNNY_NORMAL);
                                npcWithId(EasterConstants.EASTER_BIRD_NPC, "Squawk! Haresay!", Expression.EASTER_BIRD_SAY_NO);
                                player("Umm...no, wait, I can do better!");
                                options(TITLE, new DialogueOption("It's a goose that lays eggs made of gold.", key(GOLD_GOOSE)), new DialogueOption("It's a highly intelligent life form, which provides profit through chocolate.", key(INTELLIGENT_LIFEFORM)), new DialogueOption("I give up!", key(GIVE_UP)));
                            }
                            {
                                player(GIVE_UP, "I give up!", Expression.ANGRY);
                            }
                            return;
                        } else if (progress == Stage.IMPLINGS_GATHERED_SPEAK_TO_BUNNY_JR) {
                            npc("Didn't I say to keep the noise down? What's all this clanking and hissing?", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.IMPLING_WORKER_MIDDLE_NEAR_EGGPAINTING_MACHINE, "Umm... Easter eggs...helloooo? Kind of the whole point...", Expression.HIGH_REV_MAD);
                            player("Well, this is obviously where the eggs are painted...");
                            options(TITLE, new DialogueOption("...but not just painted, the'yre hand-painted by " +
                                    "master artists.", key(MASTER_ARTIST)), new DialogueOption("An impling painting an egg twice its own size is pretty cool.", key(TWICE_ITS_SIZE)), new DialogueOption("I give up!", key(GIVE_UP)));
                            {
                                player(MASTER_ARTIST, "...but not just painted, the'yre hand-painted by master artists.");
                                npc("You're very funny, you should start your own stand-up act.", Expression.EASTER_BUNNY_NORMAL);
                                player("But...");
                                options(TITLE, new DialogueOption("An impling painting an egg twice its own size is pretty cool.", key(TWICE_ITS_SIZE)), new DialogueOption("I give up!", key(GIVE_UP)));
                            }
                            {
                                player(TWICE_ITS_SIZE, "An impling painting an egg twice its own size is pretty cool.");
                                npc("Hmm...this is true. It does look very cool, and adds that touch of personality " +
                                        "that a machine wouldn't.", Expression.EASTER_BUNNY_HAPPY);
                                player("Eggzactly!");
                                npc("You know, I think this is an interesting job.", Expression.EASTER_BUNNY_HAPPY);
                                player("Really? You like it?");
                                npc("I didn't say that, I just said interesting.", Expression.EASTER_BUNNY_NORMAL);
                                player("Eggsellent!");
                                npc("This has quite tired me out, I'm going to go sit down. Oh, and see what you can " +
                                        "do about that silly impling over at that broken machine, it keeps bugging me" +
                                        " about some missing parts.", Expression.EASTER_BUNNY_NORMAL).executeAction(() -> SplittingHeirs.advanceStage(player, Stage.FIX_INCUBATOR));
                                player("I wish he wouldn't do that!");
                            }
                            {
                                player(GIVE_UP, "I give up!", Expression.ANGRY);
                            }
                            return;
                        } else if (progress == Stage.INCUBATOR_FIXED_SPEAK_TO_BUNNY_JR) {
                            npc("You woke me up again! Why does this hunk of junk make so much noise?", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.IMPLING_INCUBATOR_WORKER, "Junk?", Expression.HIGH_REV_MAD);
                            player("This is the chocatrice egg incubator.");
                            npc("Oh? What does it do?", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.IMPLING_INCUBATOR_WORKER, "Would you like to get in? I'll show " +
                                    "you...", Expression.HIGH_REV_MAD);
                            player("Ahh...I think we'll skip on that, thanks.");
                            {
                                player("This machine is one-of-a-kind.");
                                npc("Yes, but what use is it?", Expression.EASTER_BUNNY_NORMAL);
                                player("It's the Incubator 9000: a very expensive and very delicate piece of " +
                                        "machinery. It makes it possible to incubate the eggs of the chocatrice.");
                                npc("Wow. Chocotastic! Who would have thought, a machine that produces chocolate birds, and a bird that produces chocolate?", Expression.EASTER_BUNNY_NORMAL);
                                player("You like it?");
                                npc("Yes, very inspiring, and just in time for my mid-morning nap too. Oh, as you're " +
                                        "intent on cleaning this place up, could you just remove the vermin over there?", Expression.EASTER_BUNNY_NORMAL);
                                player("Vermin?");
                                npc("Yes, the ones over on that machine with the fluffy tails.", Expression.EASTER_BUNNY_NORMAL).executeAction(() -> SplittingHeirs.advanceStage(player, Stage.RETRAIN_SQUIRRELS));
                                player("Fast as greased rabbit.");
                            }
                            {
                                player(GIVE_UP, "I give up!", Expression.ANGRY);
                            }
                            return;
                        } else if (progress == Stage.SQUIRRELS_RETRAINED_SPEAK_T0_BUNNY_JR) {
                            npc("What's all the noise about this time?", Expression.EASTER_BUNNY_NORMAL);
                            player("This is where the nuts are sorted.");
                            npc("Why do we have vermin in here? I thought I told you to get rid of them!", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.BENY, "Uhh, boss, he said the 'v' word.", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.BIG_BEN, "Yes...he did. That makes me mad...", Expression.EASTER_BUNNY_NORMAL);
                            player("Uhh...he didn't mean it guys... *whispers* and just think, he could sit on you, " +
                                    "so life could be worse.");
                            npcWithId(EasterConstants.BENY, "I think I'll carry on working... Being sat on could be " +
                                    "more depressing.", Expression.EASTER_BUNNY_NORMAL);
                            npc("What did he say?", Expression.EASTER_BUNNY_NORMAL);
                            player("Yes, well. As I was saying...");
                            player("They're not vermin, they're highly skilled defect detectors!");
                            npc("Hmm...highly skilled workers eh? They earn their keep?", Expression.EASTER_BUNNY_NORMAL);
                            player("Worth every penny!");
                            npc("So we have some really good staff on the team...", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.FLUFFY, "Did you hear that boss? He called us good staff!", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.BIG_BEN, "Indeed he did, you should all be proud!", Expression.EASTER_BUNNY_NORMAL);
                            npcWithId(EasterConstants.BUBBLES, "Ale bag rang dad in vast sieve nor mills minor unity!", Expression.EASTER_BUNNY_NORMAL);
                            player("...riiiiight.");
                            player("Anyway, yes you do have good staff...all of them. This factory is pretty awesome.");
                            npc("Hmm...you summed it up in a nutshell. Meet me in dad's office, I've made a decision.", Expression.EASTER_BUNNY_NORMAL).executeAction(() -> SplittingHeirs.advanceStage(player, Stage.MEET_ME_IN_MY_OFFICE));
                            player("I wish he wouldn't do that!");
                            return;
                        } else if (progress == Stage.MEET_ME_IN_MY_OFFICE) {
                            npc("Ahh, there you are. I've made a decision based on your excellent work around the " +
                                    "factory.", Expression.EASTER_BUNNY_NORMAL);
                            player("So, are you ready to take on the factory?");
                            npc("Yes indeedy! I just need to...", Expression.EASTER_BUNNY_VERY_HAPPY).executeAction(() -> {
                                try {
                                    final AllocatedArea area = MapBuilder.findEmptyChunk(3, 3);
                                    final RedCarpetInstance instance = new RedCarpetInstance(area);
                                    instance.constructRegion();
                                    final FadeScreen fs = new FadeScreen(player);
                                    fs.fade();
                                    player.getCutsceneManager().play(new RedCarpetCutscene(player, instance, fs::unfade, npc));
                                } catch (OutOfSpaceException e) {
                                    e.printStackTrace();
                                }
                            });
                            return;
                        } else if (progress == Stage.POST_MEETING_CUTSCENE) {
                            player.getDialogueManager().finish();
                            player.getDialogueManager().start(new RedCarpetCutscene.DressUpDialogue(player, EasterConstants.EASTER_BUNNY_JR_ON_RED_CARPET));
                            return;
                        } else if (progress == Stage.EVENT_COMPLETE) {
                            npcWithId(EasterConstants.EASTER_BUNNY_JR_ON_RED_CARPET, "Thanks for fixing the factory!", Expression.EASTER_BUNNY_VERY_HAPPY);
                            return;
                        } else if (progress == Stage.START) {
                            player("This room is a mess!");
                            npc("Well, hello to you, too! Who said you could come into my room?", Expression.EASTER_BUNNY_NORMAL);
                            player("Your father, the Easter Bunny.");
                            npc("Oh...him. Where is he anyway? He should be working and making sure im fed regularly.", Expression.EASTER_BUNNY_NORMAL);
                            player("You're joking, right?");
                            npc("No, of course not, that's the way of things. He works, I enjoy myself.", Expression.EASTER_BUNNY_NORMAL);
                            options(TITLE, new DialogueOption("I think that's very lazy of you.", key(LAZY_OF_YOU)), new DialogueOption("You should clean this place up.", key(CLEAN_THIS_PLACE)), new DialogueOption("Why aren't you fixing the factory?", key(WHY_ARENT_YOU_FIXING)), new DialogueOption("Can't hang around here all day, I'm off.", key(CANT_HANG_AROUND_ALL_DAY)));
                            {
                                player(LAZY_OF_YOU, "I think that's very lazy of you.");
                                plain("The bunny sticks his tongue out and wiggles his nose at you.");
                                options(TITLE, new DialogueOption("You should clean this place up.", key(CLEAN_THIS_PLACE)), new DialogueOption("Why aren't you fixing the factory?", key(WHY_ARENT_YOU_FIXING)), new DialogueOption("Can't hang around here all day, I'm off.", key(CANT_HANG_AROUND_ALL_DAY)));
                            }
                            {
                                player(CLEAN_THIS_PLACE, "You should clean this place up.");
                                npc("Why? I like it.", Expression.EASTER_BUNNY_NORMAL);
                                player("It's...unhygienic? Besides, how can you ever find anything?");
                                npc("Oh, everything's around here somewhere.", Expression.EASTER_BUNNY_NORMAL);
                                options(TITLE, new DialogueOption("I think that's very lazy of you.", key(LAZY_OF_YOU)), new DialogueOption("Why aren't you fixing the factory?", key(WHY_ARENT_YOU_FIXING)), new DialogueOption("Can't hang around here all day, I'm off.", key(CANT_HANG_AROUND_ALL_DAY)));
                            }
                            {
                                player(WHY_ARENT_YOU_FIXING, "Why aren't you fixing the factory?");
                                npc("That's not my job. That's what dad should be doing.", Expression.EASTER_BUNNY_NORMAL);
                                player("I thought that would be your answer. Your dad has asked me to fix the factory.");
                                npc("Jolly good, that means I might get some service around here!", Expression.EASTER_BUNNY_HAPPY);
                                npc("Fetch my slippers!", Expression.EASTER_BUNNY_NORMAL);
                                player("...");
                                npc("I see. Well, if you're going to be that way, get on with fixing the Egg Plant. " +
                                        "Don't expect me to help!", Expression.EASTER_BUNNY_SAD);
                                player("You could at least give me some hint as to where to start; it's a mess out " +
                                        "there!");
                                npc("Oh, I wouldn't know the first thing about where to start, but I know my dad has " +
                                        "a big to-do list in his office.", Expression.EASTER_BUNNY_NORMAL).executeAction(() -> SplittingHeirs.advanceStage(player, Stage.CHECK_TODO));
                                options(TITLE, new DialogueOption("I think that's very lazy of you.", key(LAZY_OF_YOU)), new DialogueOption("You should clean this place up.", key(CLEAN_THIS_PLACE)), new DialogueOption("Can't hang around here all day, I'm off.", key(CANT_HANG_AROUND_ALL_DAY)));
                            }
                            {
                                player(CANT_HANG_AROUND_ALL_DAY, "Can't hang around here all day, I'm off.");
                            }
                            return;
                        }
                        npc("What is it? Shouldn't you be working?", Expression.EASTER_BUNNY_NORMAL);
                        player("What should I be doing right now?");
                        switch (progress) {
                        case CHECK_TODO: 
                            npc("Go check the to-do list. It's in Dad's office somewhere on a noticeboard.", Expression.EASTER_BUNNY_NORMAL);
                            break;
                        case BIRD_ASLEEP: 
                        case FEED_BIRD: 
                            npc("You need to feed the Easter Parrot, or whatever that thing is...", Expression.EASTER_BUNNY_NORMAL);
                            break;
                        case GATHER_IMPLINGS: 
                            npc("I'm trying to get some beauty sleep... go catch those implings in the tunnels - " +
                                    "they're driving me up the wall.", Expression.EASTER_BUNNY_NORMAL);
                            break;
                        case SPOKEN_WITH_INCUBATOR_WORKER: 
                        case FIX_INCUBATOR: 
                            npc("See what you can do about that silly impling worker over at that broken machine, it keeps bugging me about some missing parts", Expression.EASTER_BUNNY_NORMAL);
                            break;
                        case RETRAIN_SQUIRRELS: 
                            npc("Get rid of those vermin sitting on the nut sorting machine!", Expression.EASTER_BUNNY_NORMAL);
                            break;
                        }
                    }
                });
            }
            @Override
            public void execute(Player player, NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {EasterConstants.EASTER_BUNNY_JR, EasterConstants.EASTER_BUNNY_JR_CHAIR, EasterConstants.EASTER_BUNNY_JR_NEXT_TO_BIRD, EasterConstants.EASTER_BUNNY_JR_NEXT_TO_COAL, EasterConstants.EASTER_BUNNY_JR_NEXT_TO_EGGPAINTING_MACHINE, EasterConstants.EASTER_BUNNY_JR_NEXT_TO_NUT_MACHINE, EasterConstants.EASTER_BUNNY_JR_ON_RED_CARPET, EasterConstants.EASTER_BUNNY_JR_NEXT_TO_BIRD_VIS, EasterConstants.EASTER_BUNNY_CUTSCENE};
    }
}

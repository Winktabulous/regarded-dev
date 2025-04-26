package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

import java.util.ArrayList;

/**
 * @author Corey
 * @since 20/12/2019
 */
public class SnowImpFollowerD extends Dialogue {
    private static final int WHAT_SHOULD_WE_BE_DOING = 100;
    private static final int QUEEN_OF_SNOW_AND_SANTA = 200;

    public SnowImpFollowerD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final AChristmasWarble.ChristmasWarbleProgress progress = AChristmasWarble.getProgress(player);
        final String impName = ChristmasUtils.getImpName(player);
        if (progress == null) {
            return;
        }
        final ArrayList<Dialogue.DialogueOption> options = new ArrayList<DialogueOption>();
        switch (progress) {
        case FROZEN_GUESTS: 
            npc(impName, "Quick! We need to help the Queen's mates at the feastie!", Expression.HIGH_REV_SHOCKED);
            options(TITLE, new DialogueOption("What do we need to do?", key(20)), new DialogueOption("How can I put out the fires?", key(40)), new DialogueOption("Actually, never mind.", this::finish));
            player(20, "What do we need to do?");
            npc(impName, "Dat fire ain't normal fire. Nuthin' burns in the Land of Snow like that - must be magical, " +
                    "cursered fire! We's need to break the curse to put it out.", Expression.HIGH_REV_SHOCKED);
            player(40, "How can I put out the fires?");
            npc(impName, "The Land of Snow has the coldest waters! Find water 'n' a bucket, mate.", Expression.HIGH_REV_NORMAL);
            return;
        case FIND_OUT_ABOUT_SCOURGES_PAST: 
            npc(impName, "Wow, that looks proper scary, mate!", Expression.HIGH_REV_JOLLY);
            player("I can barely breathe.");
            npc(impName, "Sure you can. Nows, " + player.getName() + ", what we really need to do is find out " +
                    "something about Scourge's past.", Expression.HIGH_REV_NORMAL);
            player("Like what?");
            npc(impName, "Some mistake he made. Something he did that he regrets. We can use it to make 'im feel bad " +
                    "about the past.", Expression.HIGH_REV_NORMAL);
            npc(impName, "We should go to the feast. Someone there will know him.", Expression.HIGH_REV_NORMAL);
            player("Really? This whole plan sounds kinda far-fetch-");
            npc(impName, "Let's go!", Expression.HIGH_REV_JOLLY);
            return;
        case CHRISTMAS_PRESENT_FAILED: 
            npc(impName, "Well, now I's cross!", Expression.HIGH_REV_MAD);
            player("I guess we should try a different tact. Perhaps w-");
            npc(impName, "Of course! I's been so stoopid!", Expression.HIGH_REV_HAPPY);
            player("Don't beat yourself up about it - I went along with i-");
            npc(impName, "There's no point showing him what he's done or what he's doing - he knows all that already!" +
                    " What we've got to do is show him 'is future!", Expression.HIGH_REV_NORMAL);
            player("But we don't know his future. It's, well, his future!");
            npc(impName, "We lies! Don't worry, leave it to me. Just get back dem stairs.", Expression.HIGH_REV_NORMAL).executeAction(() -> AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.GHOST_OF_CHRISTMAS_FUTURE));
            return;
        case GHOST_OF_CHRISTMAS_FUTURE: 
            npc(impName, "What are you waiting for, let's show Scourge!", Expression.HIGH_REV_NORMAL);
            return;
        case SANTA_FREED: 
            npc(impName, "We's did it! Let's make sure Santa is okay.", Expression.HIGH_REV_NORMAL);
            return;
        case EVENT_COMPLETE: 
            npc(impName, "We's saved the feastie!", Expression.HIGH_REV_NORMAL);
            break;
        case CAN_OPEN_PRESENT: 
            npc(impName, "Let's see what's inside the present! Santa said it's wrapped up in green and red paper.", Expression.HIGH_REV_NORMAL);
            break;
        case HAS_GHOST_COSTUME: 
            npc(impName, "Put on the ghost costume!", Expression.HIGH_REV_NORMAL);
            return;
        }
        if (!AChristmasWarble.hasCompleted(player)) {
            options.add(new DialogueOption("What should we be doing?", key(WHAT_SHOULD_WE_BE_DOING)));
        }
        options.add(new DialogueOption("Tell me about the Queen of Snow and Santa.", key(QUEEN_OF_SNOW_AND_SANTA)));
        options.add(new DialogueOption("Actually, never mind.", this::finish));
        player("Hey, " + impName + ".");
        npc(impName, "Wassup?", Expression.HIGH_REV_NORMAL);
        options(TITLE, options.toArray(new DialogueOption[0]));
        {
            player(WHAT_SHOULD_WE_BE_DOING, "What should we be doing?");
            npc(impName, "Ain't you's been keeping along?", Expression.HIGH_REV_NORMAL);
            switch (progress) {
            case SPOKEN_TO_PERSONAL_IMP: 
                npc(impName, "We's need to head to Scourge's mansion in the east and investigate!", Expression.HIGH_REV_NORMAL);
                break;
            case SPOKEN_TO_SCOURGE: 
                npc(impName, "We's need to find materials to make a Ghost costume.", Expression.HIGH_REV_NORMAL);
                npc(impName, "We need to find some sheets, something clanky, oh, and a needle and thread.", Expression.HIGH_REV_NORMAL);
                npc(impName, "Maybe we try searching through Scourge's cupboards and barrels", Expression.HIGH_REV_NORMAL);
                break;
            case GHOST_OF_CHRISTMAS_PAST: 
                npc(impName, "Now's that we know's something about Scourges we should go scare him!", Expression.HIGH_REV_NORMAL);
                break;
            case GHOST_OF_CHRISTMAS_PRESENT: 
                npc(impName, "We's saved the guests, now we's should go back and show Scourge what he's done to Tiny " +
                        "Thom!", Expression.HIGH_REV_NORMAL);
                break;
            }
        }
        {
            player(QUEEN_OF_SNOW_AND_SANTA, "Tell me about the Queen of Snow and Santa.");
            npc(impName, "It's a long story.", Expression.HIGH_REV_NORMAL);
            options(TITLE, new DialogueOption("I've got time for it.", key(QUEEN_OF_SNOW_AND_SANTA + 5)), new DialogueOption("Actually, never mind.", this::finish));
            player(QUEEN_OF_SNOW_AND_SANTA + 5, "I've got time for it.");
            npc(impName, "'Round Christmas, Gielinor and the Land of Snow are close together, so, 'casionally, random" +
                    " people stumble across portals an' turn up in the Land of Snow.", Expression.HIGH_REV_NORMAL);
            npc(impName, "Ages ago, some young lad - wearing a fancy red costume and carryin' a backpack with some " +
                    "pressies in it - pops out of nowhere in the Palace of Snow's garden.", Expression.HIGH_REV_NORMAL);
            npc(impName, "Well, the youngest princess 'appened to be 'aving a snowball fight in the garden with us " +
                    "snow imps. She saw this guy, whose name was Nicklaus, and they got talking.", Expression.HIGH_REV_NORMAL);
            npc(impName, "In fact, they got on so well that she gave him some of 'er very best magic deer fer flying " +
                    "places with. Event'ly, he went away, but he didn't forget her.", Expression.HIGH_REV_NORMAL);
            npc(impName, "A year or so later 'e came back, this time askin' fer her hand in marriage.", Expression.HIGH_REV_NORMAL);
            npc(impName, "Now, marriage 'tween an ice sprite and an ooman is real frowned upon, but the Princess " +
                    "really loved Nicklaus, so they let 'er do it and, soon af'er, Prince Jack appeared.", Expression.HIGH_REV_NORMAL);
            npc(impName, "A decade of married bliss passed and the Princess was chosen to take the throne as Queen of Snow.", Expression.HIGH_REV_NORMAL);
            npc(impName, "That was kinda weird, 'cause the Princess was married to a human, but she couldn't say no. " +
                    "Soon their duties took up all of their time, an' Santa, as uvvers knew him, was getting old much" +
                    " faster than she was.", Expression.HIGH_REV_NORMAL);
            npc(impName, "It was thought best that Santa leave the Land of Snow; which meant Jack grew up without his" +
                    " dad. Jack's mum were really busy, so 'e was neglected an' became re-belly-us.", Expression.HIGH_REV_NORMAL);
            player("Wow, that's a sad story. But where does Scourge come into all this?");
            npc(impName, "De start of these two stories are pretty similar! Scourge was a young'un once, just like " +
                    "Nick he stumbled across the Land of Snow by accident.", Expression.HIGH_REV_NORMAL);
            npc(impName, "This was shortly before Nick found it. Scourge and the princess fell in love, just like " +
                    "Nick - Scourge went away, but 'e didn't come back for years.", Expression.HIGH_REV_NORMAL);
            npc(impName, "The princess watched 'is actions in Glienor - and saw how crew-ol he really was. Soon she " +
                    "was distracted by the arrival of Santa n any feelings she'd had for Scourge melted.", Expression.HIGH_REV_NORMAL);
            npc(impName, "A long time later Scourge returned, but the Snow Queen had no time for 'im. That embittered" +
                    " Scourge.", Expression.HIGH_REV_NORMAL);
            player("Two stories, so very similar and yet so very different.");
            npc(impName, "You got a point there.", Expression.HIGH_REV_NORMAL);
        }
    }
}

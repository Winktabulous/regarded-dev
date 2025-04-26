package com.zenyte.game.content.vote;

import com.zenyte.game.content.boons.impl.IVoted;
import com.zenyte.game.content.serverevent.WorldBoost;
import com.zenyte.game.content.well.WellPerk;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VoteHandler {


    private static final String NONE = Colour.BRICK.wrap("There are currently no votes for you to claim!");

    private static Calendar cal = Calendar.getInstance();

    public static String getBonusVoteMssg() {
        return "<img=48>The first week of "+new SimpleDateFormat("MMMMMMMMMM").format(cal.getTime())+" will reward you 2x Vote Points - Click here to vote now!";
    }

    public static boolean isBonusVote() {
        return cal.get(Calendar.DAY_OF_MONTH) < 7;
    }

    public static void claim(final Player player, final int amount) {
        WorldTasksManager.schedule(() -> {
            if (player == null)
                return;
            else
                player.getDialogueManager().finish();

            if (amount == 0)
                player.sendMessage(NONE);
            else
                player.sendMessage(Colour.RS_GREEN.wrap("Claimed " + amount + " votes!"));

            // todo, handle voting rewards here
        });
    }

    public static double getVotePointsModifier(Player player) {
        double mod = 0.0;
        if(isBonusVote())
            mod += 2.0;
        if(World.hasBoost(WellPerk.DOUBLE_VOTE_POINTS))
            mod += 2.0;
        if(player.getBoonManager().hasBoon(IVoted.class))
            mod += 2.0;
        if(player.getMemberRank().equalToOrGreaterThan(MemberRank.UBER))
            mod += 2.0;
        return Math.max(1, Math.min(mod, 4.0));
    }
}

package com.zenyte.game.content.well;

import com.zenyte.game.content.serverevent.WorldBoost;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.StringFormatUtil;

import java.util.Collections;
import java.util.Comparator;

import static com.zenyte.game.world.broadcasts.BroadcastType.WELL_OF_GOODWILL;

public class WellHandler implements WorldTask {

    private final ObjectArrayList<WellSession> wellSessions;

    private static WellHandler instance;

    public WellHandler() {
        wellSessions = WellPersistence.load();
    }

    @Override
    public void run() {
        if(WellConstants.WELL_DISABLED) {
            return;
        }

        for (WellSession wellSession : wellSessions) {
            if(wellSession.getCycle() > 0) {
                if(wellSession.decrementCycle() <= 1)
                    deactivatePerk(wellSession);
            }
        }
    }

    public void activatePerk(WellSession session) {
        WellContribution wellContribution = session.getContributions().stream().max(Comparator.comparing(WellContribution::getAmt)).get();
        String mssg = "<col="+WELL_OF_GOODWILL.getColor()+"><shad=000000><img=49>News:"+session.getPerk().getMssg()+" activated! Thanks to " + StringFormatUtil.formatString(wellContribution.getName()) + " for contributing the most ("+ Utils.formatNumWDot(wellContribution.getAmt())+")";
        WorldBroadcasts.sendMessage(mssg, BroadcastType.WELL_OF_GOODWILL, false);
        long endTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2);
        session.setCycle((int) TimeUnit.HOURS.toTicks(2));
        WorldBoost worldBoost = new WorldBoost(session.getPerk(), endTime, TimeUnit.DAYS.toHours(1));
        worldBoost.activate(false);
    }

    public void deactivatePerk(WellSession wellPerk) {
        wellPerk.getContributions().clear();
        wellPerk.setCoins(0);
        saveState();
    }

    public void saveState() {
        WellPersistence.save(wellSessions);
    }

    public void contribute(Player player, int amt, WellPerk type) {
        WellSession wellSession = get(type);

        if(wellSession.isActived())
            return;

        WellContribution c = null;
        for (WellContribution contribution : wellSession.getContributions()) {
            if(contribution.getName().equalsIgnoreCase(player.getUsername())) {
                contribution.increment(amt);
                c = contribution;
            }

        }
        if(c == null) {
            c = new WellContribution(player.getUsername(), amt);
            wellSession.getContributions().add(c);
        }
//        sort(wellSession);
        wellSession.incrementCoins(amt);

        if(wellSession.getCoins() >= type.getAmount())
            activatePerk(wellSession);
        saveState();
    }

    public void sort(WellSession session) {
        session.getContributions().sort(Comparator.comparing(WellContribution::getAmt));
        Collections.reverse(session.getContributions());
    }

    public WellSession get(WellPerk type) {
        for (WellSession wellSession : wellSessions) {
            if(wellSession.getPerk() == type)
                return wellSession;
        }
        return null;
    }

    public static WellHandler get() {
        if(instance == null)
            instance = new WellHandler();
        return instance;
    }

    public String getTopContributers(WellSession wellSession) {
        WellContribution[] top5 = wellSession.getContributions().subList(0, Math.min(wellSession.getContributions().size(), 5)).toArray(new WellContribution[0]);
        StringBuilder sb = new StringBuilder();
        for (WellContribution contribution : top5) {
            sb.append(contribution.getName());
            sb.append(" ");
            sb.append("<col=000080>");
            sb.append(Utils.formatNumWDot(contribution.getAmt()));
            sb.append("</col><br>");
        }
        return sb.toString();
    }
}

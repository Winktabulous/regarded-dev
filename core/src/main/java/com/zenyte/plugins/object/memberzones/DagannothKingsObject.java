package com.zenyte.plugins.object.memberzones;

import com.zenyte.game.content.boss.dagannothkings.DagannothKingInstance;
import com.zenyte.game.content.boss.dagannothkings.DagannothKingsInstanceManager;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.content.clans.ClanRank;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import mgi.utilities.StringFormatUtil;

import java.util.Optional;

import static com.zenyte.game.content.boss.dagannothkings.DagannothKingsInstanceConstants.entranceCoordinates;

public final class DagannothKingsObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Which instance would you like to enter/create?", "Clan", "Personal")
                        .onOptionOne(() -> processInstanceChoice(player, true))
                        .onOptionTwo(() -> processInstanceChoice(player, false));
            }
        });
    }

    private void processInstanceChoice(Player player, boolean isClanInstance) {
        ClanChannel channel = null;
        if(isClanInstance) {
            channel = player.getSettings().getChannel();
            if (channel == null) {
                player.sendMessage("You need to be in a clan chat channel to start or join an instance.");
                return;
            }
        }


        if(isClanInstance) {
            final Optional<DagannothKingInstance> instance = DagannothKingsInstanceManager.getManager().findInstance(player);
            if (instance.isPresent() && !instance.get().solo) {
                player.lock(1);
                player.setLocation(instance.get().getLocation(entranceCoordinates));
                return;
            }
            final ClanRank rank = ClanManager.getRank(player, channel);
            if (rank.getId() < channel.getKickRank().getId()) {
                player.sendMessage("Clan members ranked as " + StringFormatUtil.formatString(channel.getKickRank().toString()) + " or above can only start a clan instance.");
                return;
            }
        } else {
            final Optional<DagannothKingInstance> instance = DagannothKingsInstanceManager.getManager().findInstanceSolo(player);
            if (instance.isPresent()) {
                player.lock(1);
                player.setLocation(instance.get().getLocation(entranceCoordinates));
                return;
            }
            player.lock(1);
            try {
                final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(6, 6);
                final DagannothKingInstance area = new DagannothKingInstance(player.getUsername(), allocatedArea, 361, 553, false);
                area.constructRegion();
                player.setLocation(area.getLocation(entranceCoordinates));
            } catch (OutOfSpaceException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.MANHOLE_31707};
    }
}

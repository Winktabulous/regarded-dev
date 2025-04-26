package com.zenyte.plugins.object;

import com.zenyte.game.content.boss.corporealbeast.CorporealBeastDynamicArea;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.content.clans.ClanRank;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorporealBeastPrivatePortalOA implements ObjectAction {

    private static final Logger log = LoggerFactory.getLogger(CorporealBeastPrivatePortalOA.class);

    public static final Location ENTRANCE = new Location(2966, 4380, 2);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Enter")) {
            final ClanChannel channel = player.getSettings().getChannel();
            if (channel == null) {
                player.sendMessage("You need to be in a clan chat channel to start or join an instance.");
                return;
            }
            final CorporealBeastDynamicArea area = CorporealBeastDynamicArea.getArea(player);
            if (area == null) {
                final ClanRank rank = ClanManager.getRank(player, channel);
                if (rank.getId() < ClanRank.CAPTAIN.getId()) {
                    player.sendMessage("Clan members ranked as Captain or above can only start a clan instance.");
                    return;
                }
                player.getDialogueManager().start(new InstanceStartDialogue(player));
                return;
            }
            player.setLocation(area.getLocation(ENTRANCE));
        } else if (option.equals("Exit")) {
            player.getDialogueManager().start(new InstanceLeaveDialogue(player, ENTRANCE));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 9370, ObjectId.PRIVATE_PORTAL_9369 };
    }

    private static final class InstanceStartDialogue extends Dialogue {

        public InstanceStartDialogue(final Player player) {
            super(player);
        }

        @Override
        public void buildDialogue() {
            options("Start private a clan instance for 200,000 gp?", "Yes, start the instance.", "No.").onOptionOne(() -> {
                finish();
                if (!player.getInventory().containsItem(995, 200000)) {
                    player.sendMessage("You need at least 200,000 gold pieces to start an instance.");
                    return;
                }
                final ClanChannel channel = player.getSettings().getChannel();
                if (channel == null) {
                    return;
                }
                DynamicArea area = CorporealBeastDynamicArea.getArea(player);
                if (area != null) {
                    player.sendMessage("Someone in your clan has already started an instance.");
                    return;
                }
                player.getInventory().deleteItem(995, 200000);
                try {
                    final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(6, 7);
                    area = new CorporealBeastDynamicArea(player.getSettings().getChannel(), allocatedArea);
                    area.constructRegion();
                    player.setLocation(area.getLocation(ENTRANCE));
                } catch (OutOfSpaceException e) {
                    log.error("", e);
                }
            }).onOptionTwo(this::finish);
        }
    }

    public static final class InstanceLeaveDialogue extends Dialogue {

        public InstanceLeaveDialogue(final Player player, final Location tile) {
            super(player);
            this.tile = tile;
        }

        private final Location tile;

        @Override
        public void buildDialogue() {
            plain("Would you like to leave the instance? If there are no more members remaining in it, the instance " + "will collapse and you'll have to start a new one.");
            options("Leave the instance?", "Leave it.", "Stay in it.").onOptionOne(() -> {
                finish();
                player.setLocation(tile);
            }).onOptionTwo(this::finish);
        }
    }
}

package com.zenyte.plugins.object.memberzones;

import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.content.clans.ClanRank;
import com.zenyte.game.content.godwars.GodwarsInstanceManager;
import com.zenyte.game.content.godwars.GodwarsInstancePortal;
import com.zenyte.game.content.godwars.PortalTeleport;
import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import mgi.utilities.StringFormatUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@SuppressWarnings("unused")
public final class GWDInstanceObject implements ObjectAction {

    private static final Class<?>[] parameters = new Class[]{String.class, AllocatedArea.class};

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final ClanChannel channel = player.getSettings().getChannel();
        if (channel == null) {
            player.sendMessage("You need to be in a clan chat channel to start or join an instance.");
            return;
        }

        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                plain("Which God instance would you like to open?");
                options("Select a God",
                        new DialogueOption(GodwarsInstancePortal.ARMADYL.getDisplayName(), () -> createInstance(player, object, GodwarsInstancePortal.ARMADYL)),
                        new DialogueOption(GodwarsInstancePortal.BANDOS.getDisplayName(), () -> createInstance(player, object, GodwarsInstancePortal.BANDOS)),
                        new DialogueOption(GodwarsInstancePortal.ZAMORAK.getDisplayName(), () -> createInstance(player, object, GodwarsInstancePortal.ZAMORAK)),
                        new DialogueOption(GodwarsInstancePortal.SARADOMIN.getDisplayName(), () -> createInstance(player, object, GodwarsInstancePortal.SARADOMIN))
                );
            }
        });
    }

    private void createInstance(Player player, WorldObject object, GodwarsInstancePortal portal) {
        final ClanChannel channel = player.getSettings().getChannel();
        final Optional<GodwarsInstance> instance = GodwarsInstanceManager.getManager().findInstance(player,
                portal.getGod());
        if (instance.isPresent()) {
            player.lock();
            player.addWalkSteps(object.getX(), object.getY(), 1, false);
            player.teleport(instance.get().getLocation(portal.getPortalLocation()));
            return;
        }
        final ClanRank rank = ClanManager.getRank(player, channel);
        if (rank.getId() < channel.getKickRank().getId()) {
            player.sendMessage("Clan members ranked as " + StringFormatUtil.formatString(channel.getKickRank().toString()) + " or above can only start a clan instance.");
            return;
        }
        if (GodwarsInstanceManager.getManager().findInstance(player, portal.getGod()).isPresent()) {
            return;
        }
        final Class<? extends GodwarsInstance> instanceClass = portal.getInstanceClass();
        if (instanceClass == null) {
            return;
        }
        player.lock();
        try {
            final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(8, 8);
            final GodwarsInstance area =
                    instanceClass.getDeclaredConstructor(parameters)
                            .newInstance(player.getSettings().getChannel().getOwner(), allocatedArea);
            area.constructRegion();
            player.addWalkSteps(object.getX(), object.getY(), 1, false);
            player.teleport(area.getLocation(portal.getPortalLocation()));
        } catch (OutOfSpaceException | InstantiationException | InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.HOLE_26418};
    }

}

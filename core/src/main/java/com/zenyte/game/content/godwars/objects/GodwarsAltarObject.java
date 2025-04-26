package com.zenyte.game.content.godwars.objects;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.godwars.GodwarsInstancePortal;
import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import mgi.utilities.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Tommeh | 24-3-2019 | 13:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodwarsAltarObject implements ObjectAction {

    private static final Location[] ALTAR_TELEPORT_LOCS = new Location[] { new Location(2925, 5333, 2), new Location(2909, 5265, 0), new Location(2839, 5294, 2), new Location(2862, 5354, 2) };

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Pray")) {
            final String altar = object.getName().split(" ")[0] + "AltarDelay";
            if (player.getAttributes().get(altar) != null && player.getNumericAttribute(altar).longValue() > Utils.currentTimeMillis()) {
                final long minutes = TimeUnit.MILLISECONDS.toMinutes(player.getNumericAttribute(altar).longValue() - Utils.currentTimeMillis());
                player.sendMessage("The gods blessed you recently. They will ignore your prayers for another " + (minutes <= 1 ? "minute." : minutes + " minutes."));
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You need to be out of combat to use this altar.");
                return;
            }
            player.getPrayerManager().setPrayerPoints(player.getSkills().getLevelForXp(SkillConstants.PRAYER));
            player.setAnimation(new Animation(645));
            player.sendMessage("You recharge your prayer.");
            player.sendSound(2674);
            player.getAttributes().put(altar, Utils.currentTimeMillis() + 600000);
        } else if (option.equals("Teleport")) {
            if (player.getArea() instanceof GodwarsInstance) {
                final GodwarsInstancePortal altar = CollectionUtils.findMatching(GodwarsInstancePortal.values(), portalConstant -> portalConstant.getAltarId() == object.getId());
                Preconditions.checkArgument(altar != null);
                final GodwarsInstance instance = (GodwarsInstance) player.getArea();
                player.lock(1);
                player.setLocation(instance.getLocation(altar.getAltarTeleportLocation()));
                player.blockIncomingHits();
                return;
            }
            final Location location = ALTAR_TELEPORT_LOCS[object.getId() - 26363];
            player.teleport(location);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ZAMORAK_ALTAR, ObjectId.SARADOMIN_ALTAR_26364, ObjectId.ARMADYL_ALTAR, ObjectId.BANDOS_ALTAR };
    }
}

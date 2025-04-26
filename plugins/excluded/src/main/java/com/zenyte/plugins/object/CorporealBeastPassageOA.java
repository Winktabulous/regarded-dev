package com.zenyte.plugins.object;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;

import java.util.Optional;

/**
 * @author Kris | 9. veebr 2018 : 7:12.18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CorporealBeastPassageOA implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Go-through")) {
            if (player.getFollower() != null) {
                player.sendMessage("Your follower hides in fear and refuses to enter the cave.");
                return;
            }
            player.lock(1);
            player.setLocation(player.getX() < object.getX() ? new Location(object.getX() + 3, player.getY(), player.getPlane()) : new Location(object.getX() - 1, player.getY(), player.getPlane()));
            final Optional<NPC> corp = World.findNPC(319, player.getLocation(), 25);
            if (corp.isPresent()) {
                final NPC c = corp.get();
                if (c.getCombat().getTarget() == player) {
                    c.cancelCombat();
                }
            }
        } else if (option.equals("Peek")) {
            final RegionArea area = player.getArea();
            sendMessage(player, area == null ? GlobalAreaManager.get("Corporeal Beast cavern").getPlayers().size() : area.getPlayers().size());
        }
    }

    private void sendMessage(final Player player, final int amount) {
        if (amount == 0) {
            player.sendMessage("There are currently no players fighting Corporeal Beast.");
        } else if (amount == 1) {
            player.sendMessage("There is currently " + amount + " player fighting Corporeal Beast.");
        } else {
            player.sendMessage("There are currently " + amount + " players fighting Corporeal Beast.");
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.PASSAGE };
    }
}

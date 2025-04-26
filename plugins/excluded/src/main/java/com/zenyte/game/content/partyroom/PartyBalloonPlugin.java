package com.zenyte.game.content.partyroom;

import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyBalloonPlugin implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!(object instanceof PartyBalloon)) {
            return;
        }
        FaladorPartyRoom.getPartyRoom().pop(player, (PartyBalloon) object);
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.PARTY_BALLOON, ObjectId.PARTY_BALLOON_116, ObjectId.PARTY_BALLOON_117, ObjectId.PARTY_BALLOON_118, ObjectId.PARTY_BALLOON_119, ObjectId.PARTY_BALLOON_120, ObjectId.PARTY_BALLOON_121, ObjectId.PARTY_BALLOON_122 };
    }
}

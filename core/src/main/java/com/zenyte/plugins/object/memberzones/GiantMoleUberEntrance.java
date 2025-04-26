package com.zenyte.plugins.object.memberzones;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;

import static com.zenyte.plugins.object.memberzones.GiantMoleInstance.INSIDE_TILE;

public final class GiantMoleUberEntrance implements ObjectAction {


    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if(player.getMemberRank().equalToOrGreaterThan(MemberRank.UBER))
            createInstance(player);
        else
            player.sendMessage("You change your mind after hearing the screams from inside.");
    }

    private void createInstance(Player player) {
        try {
        final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(8, 16);
        final GiantMoleInstance instance = new GiantMoleInstance(player, allocatedArea, (6992 >> 8) << 3, (6992 & 0xFF) << 3);
        instance.constructRegion();
        player.setLocation(instance.getLocation(INSIDE_TILE));
        } catch (OutOfSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.CAVE_ENTRANCE_2811};
    }
}

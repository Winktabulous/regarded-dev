package com.zenyte.game.content.skills.agility.pyramid;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

public final class Doorway extends AgilityCourseObstacle {

    public Doorway() {
        super(AgilityPyramid.class, 7);
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 0;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setLocation(new Location(3364, 2830, 0));
        player.getDialogueManager().start(new PlainChat(player, "You climb down the steep passage. It leads to the base of the<br>pyramid"));
        player.getVarManager().sendBit(AgilityPyramid.HIDE_PYRAMID_VARBIT, false);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 30;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 10855, 10856 };
    }

}

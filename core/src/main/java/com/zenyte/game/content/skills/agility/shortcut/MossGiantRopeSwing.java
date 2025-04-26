package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public final class MossGiantRopeSwing implements Shortcut {

    private static final Location START_LOC = new Location(2709, 3209, 0);
    private static final Location START_LOC1 = new Location(2705, 3205, 0);
    private static final Animation SWINGING_ANIM = new Animation(751);
    private static final Animation ROPE_ANIM = new Animation(497);
    private static final ForceMovement FORCE_MOVEMENT = new ForceMovement(new Location(2709, 3209, 0), 30, new Location(2704, 3209, 0), 60, ForceMovement.NORTH);
    private static final ForceMovement FORCE_MOVEMENT1 = new ForceMovement(new Location(2705, 3205, 0), 30, new Location(2709, 3205, 0), 60, ForceMovement.SOUTH);


    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.setAnimation(SWINGING_ANIM);
        World.sendObjectAnimation(object, ROPE_ANIM);
        ForceMovement fm = object.getId() == ObjectId.ROPESWING_23568 ? FORCE_MOVEMENT : FORCE_MOVEMENT1;
        player.setFaceLocation(fm.getToSecondTile());
        player.setForceMovement(fm);
        WorldTasksManager.schedule(() -> player.setLocation(fm.getToSecondTile()), 1);
        player.getAchievementDiaries().update(KaramjaDiary.USE_ROPE_SWING);
    }
    
    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        Location startLoc = object.getId() == ObjectId.ROPESWING_23568 ? START_LOC : START_LOC1;
        return startLoc;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 10;
    }


    @Override
    public double getSuccessXp(final WorldObject object) {
        return 8;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.ROPESWING_23568, ObjectId.ROPESWING_23569};
    }
}
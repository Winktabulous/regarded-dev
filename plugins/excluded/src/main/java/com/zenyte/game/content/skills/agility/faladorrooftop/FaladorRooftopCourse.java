package com.zenyte.game.content.skills.agility.faladorrooftop;

import com.zenyte.game.content.skills.agility.AbstractAgilityCourse;
import com.zenyte.game.world.entity.Location;

/**
 * @author Noele | Apr 29, 2018 : 12:29:29 PM
 */
public final class FaladorRooftopCourse extends AbstractAgilityCourse {

    public static final Location[] MARK_LOCATIONS = {
            new Location(3046, 3345, 3), new Location(3046, 3365, 3), new Location(3036, 3363, 3),
            new Location(3015, 3355, 3), new Location(3011, 3339, 3), new Location(3023, 3334, 3)};

    @Override
    public double getAdditionalCompletionXP() {
        return 0;
    }

}

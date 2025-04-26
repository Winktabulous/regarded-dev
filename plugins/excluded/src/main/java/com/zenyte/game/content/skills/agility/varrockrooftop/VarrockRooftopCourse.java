package com.zenyte.game.content.skills.agility.varrockrooftop;

import com.zenyte.game.content.skills.agility.AbstractAgilityCourse;
import com.zenyte.game.world.entity.Location;

public final class VarrockRooftopCourse extends AbstractAgilityCourse {

    public static final Location[] MARK_LOCATIONS = {
            new Location(3215, 3410, 3), new Location(3195, 3416, 1), new Location(3193, 3395, 3),
            new Location(3222, 3402, 3), new Location(3237, 3406, 3)};

    @Override
    public double getAdditionalCompletionXP() {
        return 0;
    }

}

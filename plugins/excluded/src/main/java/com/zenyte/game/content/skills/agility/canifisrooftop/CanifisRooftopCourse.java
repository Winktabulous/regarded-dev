package com.zenyte.game.content.skills.agility.canifisrooftop;

import com.zenyte.game.content.skills.agility.AbstractAgilityCourse;
import com.zenyte.game.world.entity.Location;

public final class CanifisRooftopCourse extends AbstractAgilityCourse {

    public static final Location[] MARK_LOCATIONS = {
            new Location(3499, 3505, 2), new Location(3488, 3500, 2),
            new Location(3476, 3494, 3), new Location(3478, 3483, 2),
            new Location(3497, 3471, 3), new Location(3514, 3478, 2),};

    @Override
    public double getAdditionalCompletionXP() {
        return 0;
    }

}

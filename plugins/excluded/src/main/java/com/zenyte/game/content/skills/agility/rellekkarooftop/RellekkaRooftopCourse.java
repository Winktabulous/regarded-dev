package com.zenyte.game.content.skills.agility.rellekkarooftop;

import com.zenyte.game.content.skills.agility.AbstractAgilityCourse;
import com.zenyte.game.world.entity.Location;

/**
 * @author Tommeh | 09/06/2019 | 11:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class RellekkaRooftopCourse extends AbstractAgilityCourse {

    public static final Location[] MARK_LOCATIONS = {
            new Location(2622, 3676, 3), new Location(2617, 3664, 3), new Location(2618, 3660, 3),
            new Location(2628, 3652, 3), new Location(2628, 3655, 3), new Location(2641, 3649, 3),
            new Location(2643, 3651, 3), new Location(2649, 3659, 3), new Location(2644, 3662, 3),
            new Location(2658, 3674, 3), new Location(2656, 3681, 3)};

    @Override
    public double getAdditionalCompletionXP() {
        return 0;
    }

}

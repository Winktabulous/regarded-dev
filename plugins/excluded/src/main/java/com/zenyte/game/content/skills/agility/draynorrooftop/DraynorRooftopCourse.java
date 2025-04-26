package com.zenyte.game.content.skills.agility.draynorrooftop;

import com.zenyte.game.content.skills.agility.AbstractAgilityCourse;
import com.zenyte.game.world.entity.Location;

/**
 * @author Tommeh | 25 feb. 2018 : 19:20:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public final class DraynorRooftopCourse extends AbstractAgilityCourse {

    public static final Location[] MARK_LOCATIONS = {
            new Location(3101, 3278, 3), new Location(3091, 3275, 3),
            new Location(3093, 3266, 3), new Location(3098, 3259, 3)};

    @Override
    public double getAdditionalCompletionXP() {
        return 0;
    }

}

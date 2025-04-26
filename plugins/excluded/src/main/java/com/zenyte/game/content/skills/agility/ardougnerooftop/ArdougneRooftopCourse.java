package com.zenyte.game.content.skills.agility.ardougnerooftop;

import com.zenyte.game.content.skills.agility.AbstractAgilityCourse;
import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 08/06/2019 09:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ArdougneRooftopCourse extends AbstractAgilityCourse {

    public static final Location[] MARK_LOCATIONS = {new Location(2657, 3318, 3)};

    @Override
    public double getAdditionalCompletionXP() {
        return 0;
    }

}

package com.zenyte.game.content.skills.agility;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * @author Kris | 21. dets 2017 : 17:39.51
 * @author Jire
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum AgilityCourseManager {
    ;

    public static final String COURSE_STAGE_ATTRIBUTE = "courseStage";

    private static final Logger log = LoggerFactory.getLogger(AgilityCourseManager.class);

    private static final Map<Class<? extends AgilityCourse>, AgilityCourse> courses
            = new Object2ObjectOpenHashMap<>();

    @Nullable
    public static AgilityCourse getCourse(@NotNull final Class<? extends AgilityCourse> courseClass) {
        return courses.get(courseClass);
    }

    public static boolean calculateSuccess(final Player player,
                                           final WorldObject object,
                                           final Obstacle obstacle) {
        if (!(obstacle instanceof Failable)) return true;
        if (obstacle instanceof Irreversible) {
            final Irreversible irreversibleObstacle = (Irreversible) obstacle;
            if (irreversibleObstacle.failOnReverse() && irreversibleObstacle.checkForReverse(player, object)) {
                return false;
            }
        }
        return obstacle.successful(player, object);
    }

    public static void initCourse(@NotNull final Class<? extends AgilityCourse> courseClass) {
        try {
            if (courses.containsKey(courseClass)) {
                throw new IllegalStateException("Already initialized course by class " + courseClass);
            }
            final AgilityCourse course = courseClass.getDeclaredConstructor().newInstance();
            courses.put(courseClass, course);
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void initUnsafe(@NotNull final Class<?> courseClass) {
        initCourse((Class<? extends AgilityCourse>) courseClass);
    }

    public static AgilityCourse getCourse(@NotNull final AgilityCourseObstacle obstacle) {
        final Class<? extends AgilityCourse> courseClass = obstacle.getCourse();

        final AgilityCourse course = getCourse(courseClass);
        Objects.requireNonNull(course,
                "Obstacle " + obstacle.getClass() + "'s course " + courseClass
                        + " was not mapped in " + AgilityCourseManager.class.getSimpleName());

        return course;
    }

}

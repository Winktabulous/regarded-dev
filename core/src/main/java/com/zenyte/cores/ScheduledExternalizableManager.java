package com.zenyte.cores;

import com.zenyte.game.GameConstants;
import kotlin.jvm.JvmClassMappingKt;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Kris | 16. juuni 2018 : 16:22:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum ScheduledExternalizableManager {

    ;

    private static final Logger log = LoggerFactory.getLogger(ScheduledExternalizableManager.class);
    private static final List<ScheduledExternalizable> scheduled = new LinkedList<>();
    private static int minutesPassed;
    public static final MutableBoolean status = new MutableBoolean();

    static {
        CoresManager.slowExecutor.scheduleAtFixedRate(() -> {
            save();
            minutesPassed++;
        }, 1, 1, TimeUnit.MINUTES);
    }

    public static synchronized void save() {
        try {
            if (GameConstants.WORLD_PROFILE.isDevelopment()) return;

            status.setTrue();

            for (final ScheduledExternalizable scheduled : ScheduledExternalizableManager.scheduled) {
                final int interval = scheduled.writeInterval();
                if (interval <= 0) {
                    continue;
                }
                if (minutesPassed % interval == 0) {
                    try {
                        scheduled.writeAndOutput();
                    } catch (final Exception e) {
                        log.error("", e);
                    }
                }
            }

            status.setFalse();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void add(final Class<? extends ScheduledExternalizable> c) {
        try {
            ScheduledExternalizable instance = JvmClassMappingKt.getKotlinClass(c).getObjectInstance();
            if (instance == null)
                instance = c.getDeclaredConstructor().newInstance();
            scheduled.add(instance);
            instance.read();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void addUnsafe(Class<?> c) {
        add((Class<? extends ScheduledExternalizable>) c);
    }

}

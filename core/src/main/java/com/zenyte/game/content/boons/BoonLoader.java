package com.zenyte.game.content.boons;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.boons.impl.UnknownBoon;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.ClassInitializer;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static java.util.concurrent.ForkJoinPool.commonPool;

@SuppressWarnings("unused")
public final class BoonLoader {

    @Subscribe
    public static void onServerLaunch(final ServerLaunchEvent event) {
        new BoonLoader().init();
    }

    private static final Logger log = LoggerFactory.getLogger(BoonLoader.class);

    public static ArrayList<Boon> boonTypes = new ArrayList<>();

    public static Boon findBoon(Class<?> lookupBoon) {
        return boonTypes.stream().filter(it -> it.getClass() == lookupBoon).findAny().orElse(new UnknownBoon());
    }

    public void init() {
        final ClassGraph scanner = new ClassGraph();
        scanner.ignoreMethodVisibility();
        scanner.enableAnnotationInfo();
        scanner.enableMethodInfo();
        scanner.enableClassInfo();
        scanner.enableExternalClasses();
        scanner.acceptPackages("com.zenyte.game.content.boons.impl");


        log.debug("Scanning for boons in classpath.");
        try (ScanResult result = scanner.scan()) {
            final ObjectArrayList<Callable<Void>> callables = new ObjectArrayList<>(1000);
            final Object lock = new Object();
            result.getAllClasses().forEach(clazz -> {
                callables.add(() -> {
                    if (clazz.extendsSuperclass(Boon.class) && !clazz.hasAnnotation(DisabledBoon.class)) {
                        log.trace("Loading player boon: " + clazz.getSimpleName());
                        ClassInitializer.initialize(clazz.loadClass());
                        final Class<?> obj = clazz.loadClass();
                        synchronized (lock) {
                            Boon boon = (Boon) obj.getDeclaredConstructor().newInstance();
                            boonTypes.add(boon);
                        }

                    }
                    return null;
                });
            });
            log.debug("Processing boon scan result.");
            commonPool().invokeAll(callables);
            log.info("Loaded a total of {} boon types from the classpath", boonTypes.size());
        }
    }
}

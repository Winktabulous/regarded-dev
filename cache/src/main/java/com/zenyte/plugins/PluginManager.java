package com.zenyte.plugins;

import com.google.common.eventbus.Subscribe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.reflect.Modifier.isStatic;

/**
 * @author Kris | 21/03/2019 16:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum PluginManager {
    ;

    /**
     * A map of executables, wherein the key is the event class, and the value is a set of subscribed methods.
     */
    private static final Map<Class<?>, Set<Consumer<Event>>> consumerMap = new Object2ObjectOpenHashMap<>();

    private static final Logger log = LoggerFactory.getLogger(PluginManager.class);

    /**
     * Regosters an executable method.
     *
     * @param superClass the class in which the subscribed event is.
     * @param executable the executable method subscribed.
     */
    public static boolean register(@NotNull final Class<?> superClass, @NotNull final Method executable) {
        // Do not register the event if the method isn't declared by the class publishing it. Static methods are still
        // inherited through extensions, so we filter all those.
        if (!ArrayUtils.contains(superClass.getDeclaredMethods(), executable)) {
            return false;
        }

        checkArgument(isStatic(executable.getModifiers()),
                "Registered events must be static: "
                        + (superClass.getSimpleName() + "::" + executable.getName()));
        checkArgument(executable.isAnnotationPresent(Subscribe.class),
                "Registered event must have " +
                        Subscribe.class.getSimpleName() + " annotation preset: "
                        + (superClass.getSimpleName() + "::" + executable.getName()));
        checkArgument(executable.getParameterCount() == 1,
                "Registered events must only contain one " +
                        "parameter: " + (superClass.getSimpleName() + "::" + executable.getName()));

        final Parameter event = executable.getParameters()[0];
        final Class<?> type = event.getType();

        checkArgument(Event.class.isAssignableFrom(type),
                "Subscribed method's parameter must implement" +
                        " the " + Event.class.getSimpleName() + " interface.");

        return registerRaw(type, postedEvent -> {
            try {
                executable.invoke(null, postedEvent);
            } catch (Exception e) {
                log.error("failed to invoke subscriber executable (type " + type + ", event " + postedEvent + ")", e);
            }
        });
    }

    public static boolean registerRaw(@NotNull final Class<?> eventType, @NotNull final Consumer<Event> consumer) {
        return consumerMap.computeIfAbsent(eventType, r -> new ObjectLinkedOpenHashSet<>()).add(consumer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Event> boolean register(@NotNull final Class<T> eventType,
                                                     @NotNull final Consumer<T> consumer) {
        return registerRaw(eventType, event -> consumer.accept((T) event));
    }

    /**
     * Publishes an event to the listener.
     *
     * @param event the event published.
     */
    public static void post(@NotNull final Event event) {
        final Class<? extends Event> clazz = event.getClass();
        final Set<Consumer<Event>> consumers = consumerMap.get(clazz);
        if (consumers == null) {
            return;
        }
        for (final Consumer<Event> consumer : consumers) {
            try {
                consumer.accept(event);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

}

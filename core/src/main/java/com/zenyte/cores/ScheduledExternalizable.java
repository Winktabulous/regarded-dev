package com.zenyte.cores;

import com.google.gson.Gson;
import com.zenyte.game.world.DefaultGson;
import com.zenyte.plugins.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

/**
 * @author Kris | 16. juuni 2018 : 16:18:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface ScheduledExternalizable extends Plugin {

    /**
     * The log object, used to output information.
     */
    Logger getLog();

    /**
     * The formatted gson we use to read and write with.
     */
    default Gson getGSON() {
        return DefaultGson.getGson();
    }

    /**
     * The save frequency in minutes. If negative or zero, {@link #write()} method will never be called.
     */
    int writeInterval();

    /**
     * The abstract read method.
     */
    void read(@NotNull final BufferedReader reader);

    /**
     * The abstract write method.
     */
    void write();

    /**
     * The path to the file from which it is read, and to which it is saved.
     */
    String path();

    /**
     * Reads the file from the defined {@link #path()}
     */
    default void read() {
        Path path = Path.of(path());
        if (!Files.exists(path)) {
            ifFileNotFoundOnRead();
            return;
        }
        try (final BufferedReader reader = Files.newBufferedReader(path)) {
            read(reader);
        } catch (final Exception e) {
            ifFileNotFoundOnRead();
            e.printStackTrace();
        }
    }

    /**
     * Writes the file and outputs the duration of the writing process.
     */
    default void writeAndOutput() {
        final long start = System.nanoTime();
        write();
        if (output()) {
            final long end = System.nanoTime();
            final long elapsed = TimeUnit.NANOSECONDS.toMillis(end - start);
            getLog().info("Writing {} took {} milliseconds.", getClass().getSimpleName(), elapsed);
        }
    }

    /**
     * Whether to output the duration of the saving process.
     */
    default boolean output() {
        return false;
    }

    /**
     * If the file can't be found when trying to read it, this method is executed.
     */
    default void ifFileNotFoundOnRead() {
        getLog().error("File not found: " + getClass().getName() + ": " + path());
    }

    /**
     * Outputs the requested json string to the requested path.
     */
    default void out(final String json) {
        final Logger log = getLog();
        if (json == null) {
            log.warn("tried to output \"{}\" but specified JSON was null", getClass().getSimpleName());
            return;
        }
        try {
            final Path path = Path.of(path());
            Path parent = path.getParent();
            if (parent == null) {
                throw new UnsupportedOperationException("Path must have a parent " + path);
            }
            if (!Files.exists(parent)) {
                parent = Files.createDirectories(parent);
            }

            final Path tempFile = Files.createTempFile(parent, path.getFileName().toString(), ".tmp");
            Files.writeString(tempFile, json, StandardCharsets.UTF_8);

            Files.move(tempFile, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (final Exception e) {
            log.error("", e);
        }
    }

}

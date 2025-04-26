package com.zenyte.game.world.entity.player;


import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MpscArrayQueue;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Kris | 24/05/2019 23:20
 * @author Jire
 */
public final class PlayerLogger {
    private static final Logger log = LoggerFactory.getLogger(PlayerLogger.class);
    private static final DateTimeFormatter loggerDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter folderFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final LogLevel WRITE_LEVEL = LogLevel.HIGH_PACKET;
    public static final int WRITE_LEVEL_PRIORITY = WRITE_LEVEL.getPriority();

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    static final int QUEUE_CAPACITY = 512;
    static final int MESSAGE_BYTE_LIMIT = 8192;
    static final int QUEUE_FLUSH_MARK = QUEUE_CAPACITY / 2;
    private static final ThreadLocal<ByteBuffer> tlBuffer = ThreadLocal.withInitial(
            () -> ByteBuffer.allocateDirect(MESSAGE_BYTE_LIMIT * QUEUE_CAPACITY));

    private final MessagePassingQueue<byte[]> queue =
            new MpscArrayQueue<>(QUEUE_CAPACITY);

    private final AtomicBoolean flushing = new AtomicBoolean(false);

    private final transient Path file;

    void shutdown() {
        flushing.set(true);
        flush();
    }

    public void flush() {
        try {
            final ByteBuffer buffer = tlBuffer.get().clear();

            boolean drained = false;
            while (true) {
                final byte[] data = queue.poll();
                if (data == null) break;

                buffer.put(data);
                drained = true;
            }
            if (drained) {
                buffer.flip();

                try (final FileChannel fileChannel = FileChannel.open(file,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.APPEND)) {
                    while (buffer.hasRemaining()) {
                        fileChannel.write(buffer);
                    }
                }
            }
        } catch (final Exception e) {
            log.error("", e);
        } finally {
            flushing.set(false);
        }
    }

    public void log(final LogLevel level, final String message) {
        if (level.getPriority() < WRITE_LEVEL_PRIORITY) return;

        try {
            final Thread currentThread = Thread.currentThread();
            final String actualMessage = LocalDateTime.now().format(loggerDateFormatter)
                    + " [" + currentThread.getName() + "] "
                    + level.name() + " - "
                    + message + System.lineSeparator();

            final byte[] bytes = actualMessage.getBytes(CHARSET);
            final int byteLength = bytes.length;
            if (byteLength > MESSAGE_BYTE_LIMIT) {
                log.warn("Message \"{}\" exceeds message byte limit ({})", actualMessage, MESSAGE_BYTE_LIMIT);
                return;
            }

            boolean needsFlush = false;
            if (!queue.offer(bytes)) {
                log.warn("Message \"{}\" couldn't be placed into back queue", actualMessage);
                needsFlush = true;
            }
            if (!needsFlush && queue.size() >= QUEUE_FLUSH_MARK) {
                needsFlush = true;
            }
            if (needsFlush && !flushing.getAndSet(true)) {
                ForkJoinPool.commonPool().execute(this::flush);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public PlayerLogger(final Player player) throws IOException {
        final Path folderPath = getPath(LocalDate.now());
        final Path folder = Files.createDirectories(folderPath);
        file = folder.resolve(player.getUsername() + ".log");
    }

    @NotNull
    public static Path getPath(LocalDate date) {
        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek.of(
                ((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
        final LocalDate firstDate = date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        final LocalDate lastDate = date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek));

        final String folderLabel = folderFormatter.format(firstDate) + " - " + folderFormatter.format(lastDate);

        return Path.of("data", "logs", "player logs", folderLabel);
    }

}

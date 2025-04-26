package com.zenyte.game.world.entity.player;

import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 15/08/2019 21:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MovementLock {

    public MovementLock(final long until) {
        this(until, null);
    }

    public MovementLock(final long until, final String message) {
        this(until, message, null);
    }

    public MovementLock(final long until, final String message, final Runnable attachment) {
        this.until = until;
        this.message = message;
        this.attachment = attachment;
    }

    private transient long until;
    private final transient String message;
    private final transient Runnable attachment;
    private transient boolean fullLock;

    public MovementLock setIndefinite() {
        this.until = Long.MAX_VALUE;
        return this;
    }

    public MovementLock setExpired() {
        this.until = Long.MIN_VALUE;
        return this;
    }

    public MovementLock setFullLock() {
        this.fullLock = true;
        return this;
    }

    public MovementLock setNoFullLock() {
        this.fullLock = false;
        return this;
    }

    boolean canWalk(@NotNull final Player player, final boolean executeAttachments) {
        if (until > System.currentTimeMillis()) {
            if (executeAttachments) {
                if (message != null) {
                    player.sendFilteredMessage(message);
                }
                if (attachment != null) {
                    attachment.run();
                }
            }
            return false;
        }
        return true;
    }

    public long getUntil() {
        return until;
    }

    public String getMessage() {
        return message;
    }

    public Runnable getAttachment() {
        return attachment;
    }

    public boolean isFullLock() {
        return fullLock;
    }

}

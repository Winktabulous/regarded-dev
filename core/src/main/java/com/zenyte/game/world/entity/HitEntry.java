package com.zenyte.game.world.entity;

import com.zenyte.game.world.entity.masks.Hit;

/**
 * @author Kris | 20/08/2019 20:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HitEntry {

    public HitEntry(final Entity source, final int delay, final Hit hit) {
        this.source = source;
        this.delay = delay;
        this.hit = hit;
        this.freshEntry = true;
    }

    public HitEntry(final Entity source, final int delay, final Hit hit, boolean markFresh) {
        this.source = source;
        this.delay = delay;
        this.hit = hit;
        this.freshEntry = markFresh;
    }

    private final Entity source;
    private int delay;
    private final Hit hit;
    private boolean freshEntry;
    private transient HitEntry next, previous;

    int getAndDecrement() {
        return delay--;
    }

    public Entity getSource() {
        return source;
    }

    public int getDelay() {
        return delay;
    }

    public Hit getHit() {
        return hit;
    }

    public boolean isFreshEntry() {
        return freshEntry;
    }

    public void setFreshEntry(boolean freshEntry) {
        this.freshEntry = freshEntry;
    }

    public HitEntry getNext() {
        return next;
    }

    public void setNext(HitEntry next) {
        this.next = next;
    }

    public HitEntry getPrevious() {
        return previous;
    }

    public void setPrevious(HitEntry previous) {
        this.previous = previous;
    }

}

package com.zenyte.game.world.entity;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @author Kris | 04/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 *
 * A memory-efficient and fast implementation of a add/remove/iterate collection. Absolute bare minimum.
 */
public class HitEntryList implements Iterable<HitEntry> {
    private transient HitEntry first;
    private transient HitEntry last;
    private transient HitEntryIterator itr;

    public boolean add(HitEntry hitEntry) {
        final HitEntry l = last;
        hitEntry.setPrevious(l);
        hitEntry.setNext(null);
        last = hitEntry;
        if (l == null) {
            first = hitEntry;
        } else {
            l.setNext(hitEntry);
        }
        return true;
    }

    private void remove(HitEntry entry) {
        final HitEntry next = entry.getNext();
        final HitEntry prev = entry.getPrevious();
        if (prev == null) {
            first = next;
        } else {
            prev.setNext(next);
            entry.setPrevious(null);
        }
        if (next == null) {
            last = prev;
        } else {
            next.setPrevious(prev);
            entry.setNext(null);
        }
    }

    @NotNull
    @Override
    public Iterator<HitEntry> iterator() {
        if (itr == null) {
            itr = new HitEntryIterator();
        }
        itr.next = first;
        return itr;
    }


    private class HitEntryIterator implements Iterator<HitEntry> {
        HitEntry lastReturned;
        HitEntry next;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public HitEntry next() {
            lastReturned = next;
            next = next.getNext();
            return lastReturned;
        }

        @Override
        public void remove() {
            HitEntryList.this.remove(this.lastReturned);
        }
    }
}

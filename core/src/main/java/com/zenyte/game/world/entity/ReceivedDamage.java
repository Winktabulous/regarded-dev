package com.zenyte.game.world.entity;

import com.zenyte.game.world.entity.masks.HitType;

/**
 * @author Kris | 20/06/2019 17:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ReceivedDamage {

    private final long time;
    private final int damage;
    private final HitType type;

    public ReceivedDamage(long time, int damage, HitType type) {
        this.time = time;
        this.damage = damage;
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public int getDamage() {
        return damage;
    }

    public HitType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ReceivedDamage{" +
                "time=" + time +
                ", damage=" + damage +
                ", type=" + type +
                '}';
    }

}

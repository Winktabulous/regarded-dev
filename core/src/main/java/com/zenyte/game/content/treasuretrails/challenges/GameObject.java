package com.zenyte.game.content.treasuretrails.challenges;

import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 07/04/2019 13:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class GameObject {
    private final int id;
    private final Location tile;
    private String option;

    public GameObject(int id, Location tile, String option) {
        this.id = id;
        this.tile = tile;
        this.option = option;
    }

    public GameObject(int id, Location tile) {
        this.id = id;
        this.tile = tile;
    }

    public int getId() {
        return id;
    }

    public Location getTile() {
        return tile;
    }

    public String getOption() {
        return option;
    }
}

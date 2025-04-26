package com.zenyte.game.model.music;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kris | 27. juuli 2018 : 21:28:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Music {
    public static final Map<String, Music> map = new HashMap<String, Music>(600);
    public static final Map<String, Music> lowercaseMap = new HashMap<>(600);
    private String name;
    private String hint;
    private int musicId;
    private int duration;
    private List<Integer> regionIds;
    private boolean defaultLocked;

    public static final Music get(@NotNull final String name) {
        final Music music = map.get(name);
        if (music == null) {
            throw new IllegalStateException("Music track '" + name + "' does not exist.");
        }
        return music;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Integer> getRegionIds() {
        return regionIds;
    }

    public void setRegionIds(List<Integer> regionIds) {
        this.regionIds = regionIds;
    }

    public boolean isDefaultLocked() {
        return defaultLocked;
    }

    public void setDefaultLocked(boolean defaultLocked) {
        this.defaultLocked = defaultLocked;
    }

    @Override
    public String toString() {
        return "Music{" +
                "name='" + name + '\'' +
                ", hint='" + hint + '\'' +
                ", musicId=" + musicId +
                ", duration=" + duration +
                ", regionIds=" + regionIds +
                ", defaultLocked=" + defaultLocked +
                '}';
    }
}

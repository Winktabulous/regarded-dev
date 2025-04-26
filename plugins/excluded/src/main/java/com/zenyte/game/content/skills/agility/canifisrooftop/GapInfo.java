package com.zenyte.game.content.skills.agility.canifisrooftop;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.ObjectId;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum GapInfo {

    FIRST(ObjectId.GAP_14844, new Location(3505, 3497, 2), new Location(3502, 3504, 2)),
    SECOND(ObjectId.GAP_14845, new Location(3498, 3504, 2), new Location(3492, 3504, 2)),
    THIRD(ObjectId.GAP_14846, new Location(3478, 3493, 3), new Location(3478, 3486, 2)),
    FOURTH(ObjectId.GAP_14847, new Location(3502, 3476, 3), new Location(3510, 3476, 2)),
    FIFTH(ObjectId.GAP_14897, new Location(3510, 3482, 2), new Location(3510, 3485, 0)),
    ;

    private final int id;
    private final Location start;
    private final Location finish;

    public static final GapInfo[] values = values();
    private static final Int2ObjectMap<GapInfo> idToInfo = new Int2ObjectOpenHashMap<>(values.length);

    GapInfo(final int id, final Location start, final Location finish) {
        this.id = id;
        this.start = start;
        this.finish = finish;
    }

    public static GapInfo get(int id) {
        return idToInfo.get(id);
    }

    static {
        for (GapInfo entry : values) {
            idToInfo.put(entry.getId(), entry);
        }
    }

    public int getId() {
        return id;
    }

    public Location getStart() {
        return start;
    }

    public Location getFinish() {
        return finish;
    }

}

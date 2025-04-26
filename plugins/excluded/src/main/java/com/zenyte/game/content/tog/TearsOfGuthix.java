package com.zenyte.game.content.tog;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.zenyte.game.util.IntListUtils;
import com.zenyte.game.world.object.ObjectId;
import it.unimi.dsi.fastutil.ints.IntLists;

import java.util.EnumSet;

/**
 * @author Chris
 * @since September 08 2020
 */

public enum TearsOfGuthix {
    BLUE(ObjectId.BLUE_TEARS_6665, 1),
    GREEN(ObjectId.GREEN_TEARS_6666, -1),
    ABSENCE(ObjectId.ABSENCE_OF_TEARS_6667, 0);
    public static final ImmutableSet<TearsOfGuthix> TEARS = Sets.immutableEnumSet(EnumSet.allOf(TearsOfGuthix.class));
    public static final IntLists.UnmodifiableList TEARS_OBJECT_ID = IntListUtils.unmodifiable(TEARS.stream().mapToInt(TearsOfGuthix::getObjectId).toArray());
    private final int objectId;
    private final int pointModifier;
    
    TearsOfGuthix(int objectId, int pointModifier) {
        this.objectId = objectId;
        this.pointModifier = pointModifier;
    }
    
    public int getObjectId() {
        return objectId;
    }
    
    public int getPointModifier() {
        return pointModifier;
    }
}

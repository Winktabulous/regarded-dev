package com.zenyte.game.content.chambersofxeric.map;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 15. nov 2017 : 22:13.24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class WrapperChunk extends DynamicArea {
    private static final Logger log = LoggerFactory.getLogger(WrapperChunk.class);
    /**
     * The height level from which the room is copied, and to which it is copied.
     */
    private final int fromPlane;
    private final int toPlane;
    /**
     * The rotation of this wrapper chunk.
     */
    private final int rotation;

    WrapperChunk(final int sizeX, final int sizeY, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane, final int rotation) {
        super(sizeX, sizeY, regionX, regionY, chunkX, chunkY);
        this.fromPlane = fromPlane;
        this.toPlane = toPlane;
        this.rotation = rotation;
    }

    @Override
    public void constructRegion() {
        if (constructed) return;
        try {
            constructed = true;
            MapBuilder.copySquare(area, 1, staticChunkX, staticChunkY, fromPlane, chunkX, chunkY, toPlane, rotation);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public String name() {
        return "Wrapper chunk";
    }

    @Override
    public void constructed() {
    }

    @Override
    protected void cleared() {
    }

    @Override
    public void enter(final Player player) {
    }

    @Override
    public void leave(final Player player, boolean logout) {
    }

    public int getFromPlane() {
        return fromPlane;
    }

    public int getToPlane() {
        return toPlane;
    }
}

package com.zenyte.game.world.entity.player.update;

import com.near_reality.buffer.BitBuf;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.GamePacketEncoderMode;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.EntityList;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.mask.*;
import com.zenyte.net.NetworkConstants;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import com.zenyte.net.io.RSBuffer;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 1. veebr 2018 : 22:11.29
 * @author Jire
 */
public final class PlayerInfo implements GamePacketEncoder {

    private static final EntityList<Player> players = World.getPlayers();

    private static final boolean USE_MASK_DATA_CACHING = true;
    private static final Map<String, MaskCacheEntry> maskDataCache = new HashMap<>();
    private static final Map<String, MaskCacheEntry> maskDataAddedCache = new HashMap<>();

    private static final class MaskCacheEntry {
        private RSBuffer buffer = null;
        private long age;

        public MaskCacheEntry() {
            buffer = new RSBuffer(255, 5_000);
        }

        public void reset() {
            age = WorldThread.getCurrentCycle();
        }

        public boolean isExpired() {
            return buffer == null || WorldThread.getCurrentCycle() != age;
        }

        public RSBuffer getBuffer() {
            return buffer;
        }
    }

    /**
     * An array of update masks in the respective order they're read in the client. PS: Order must be preserved!
     */
    private static final UpdateMask[] masks = new UpdateMask[]{
            new TintingMask(),
            new AppearanceMask(),
            new ChatMask(),
            new MovementMask(),
            new NametagMask(),
            new FaceLocationMask(),
            new GraphicsMask(),
            new ForceChatMask(),
            new AnimationMask(),
            new HitMask(),
            new ForceMovementMask(),
            new TemporaryMovementMask(),
            new FaceEntityMask(),
    };

    /**
     * The length of the masks.
     */
    private static final int length = masks.length;
    public static final int ACTIVE_TO_INACTIVE_FLAG = 2;
    public static final int INACTIVE_FLAG = 1;
    private Player player;
    /**
     * <p>Activity flags are used to determine whether the player was updated this cycle, or skipped entirely due
     * to inactivity. The method is used to group together inactive & active players respectively, allowing the
     * server to skip larger amounts of players due to probability, effectively saving bandwidth in the long run,
     * as every skipping call writes a number of bits on its own.</p>
     */
    private byte[] activityFlags;
    /**
     * <p>The indexes of the players currently in our viewport.</p>
     */
    private int[] localIndexes;
    /**
     * <p>The indexes of the players currently outside our viewport.</p>
     */
    private int[] externalIndexes;

    /**
     * <p>The players currently in our viewport.</p>
     */
    private Player[] localPlayers;
    /**
     * <p>Position multipliers, used to transmit coordinates when they exceed 8191 in either direction.
     * RS only transmits 13 bits for x & y coordinates, meaning maximum value allowed is 2^13 - 1 = 8191.
     * For that reason, if it is needed to transmit coordinates higher than that, a multiplier must be transmitted
     * to the client which will then transform the coordinate to (multiplier * 8192) + remainderCoordinate.</p>
     * <p>Maximum allowed coordinates are 16383 in both directions; going past that will not render characters anymore.
     * </p>
     * <p>Coordinates were transmitted on a region level prior to deadman mode, which was why the method was
     * initially created.</p>
     */
    private int[] multipliers;
    /**
     * <p>The secondary per-player basis buffer for masks.</p>
     */
    private RSBuffer smallMaskBuffer;
    /**
     * <p>The primary buffer for masks.</p>
     */
    private RSBuffer largeMaskBuffer;
    /**
     * <p>The primary bitbuffer of GPI.</p>
     */
    private BitBuf bitBuffer;
    /**
     * <p>The amount of players currently outside our viewport.</p>
     */
    private int externalIndexesCount;
    /**
     * <p>The amount of players currently inside our viewport.</p>
     */
    private int localIndexesCount;
    /**
     * <p>The primary buffer of GPI.</p>
     */
    private GamePacketOut buffer;

    private int resizeTickCount = 0;

    private static final int PREFERRED_PLAYER_COUNT = 250;
    private static final int PREFERRED_VIEW_DISTANCE = 15;
    private static final int MIN_VIEW_DISTANCE = 1;
    private static final int RESIZE_CHECK_INTERVAL = 10;

    private void resize() {
        if (player.isHeatmap()) return;
        if (localIndexesCount >= PREFERRED_PLAYER_COUNT) {
            if (player.getViewDistance() > 0)
                player.setViewDistance(Math.max(MIN_VIEW_DISTANCE, player.getViewDistance() - 1));
            resizeTickCount = 0;
            return;
        }
        if (++resizeTickCount >= RESIZE_CHECK_INTERVAL) {
            if (player.getViewDistance() < PREFERRED_VIEW_DISTANCE) {
                player.setViewDistance(player.getViewDistance() + 1);
            } else {
                resizeTickCount = 0;
            }
        }
    }

    public void reset() {
        player = null;
        activityFlags = null;
        localIndexes = null;
        externalIndexes = null;
        localPlayers = null;
        multipliers = null;

        if (smallMaskBuffer != null) {
            smallMaskBuffer.close();
            smallMaskBuffer = null;
        }
        if (largeMaskBuffer != null) {
            largeMaskBuffer.close();
            largeMaskBuffer = null;
        }
        if (bitBuffer != null) {
            if (bitBuffer.refCnt() > 0) bitBuffer.release();
            bitBuffer = null;
        }
        if (buffer != null) {
            buffer.close();
            buffer = null;
        }
    }

    public PlayerInfo(final Player player) {
        this.player = player;
    }

    /**
     * Initializes the GPI by transmitting coordinate multipliers for all the players across the world.
     *
     * @param buffer the buffer to write the information to.
     */
    public void init(final RSBuffer buffer) {
        activityFlags = new byte[2048];
        localPlayers = new Player[2048];
        localIndexes = new int[2048];
        multipliers = new int[2048];
        externalIndexes = new int[2048];
        smallMaskBuffer = new RSBuffer(255);
        largeMaskBuffer = new RSBuffer(255);
        this.buffer = new GamePacketOut(ServerProt.PLAYER_INFO, 750);
        bitBuffer = new BitBuf(buffer.alloc().directBuffer(750, NetworkConstants.MAX_SERVER_BUFFER_SIZE));

        bitBuffer.writeBits(30, player.getLocation().getPositionHash());
        localPlayers[player.getIndex()] = player;
        localIndexes[localIndexesCount++] = player.getIndex();
        for (int playerIndex = 1; playerIndex < 2048; playerIndex++) {
            if (playerIndex == player.getIndex()) {
                continue;
            }
            final Player player = players.getDirect(playerIndex);
            final int multiplier = player == null
                    ? 0
                    : player.getLocation18BitHash();
            multipliers[playerIndex] = multiplier;
            bitBuffer.writeBits(18, multiplier);
            externalIndexes[externalIndexesCount++] = playerIndex;
        }
        buffer.writeBits(bitBuffer);
    }

    /**
     * Prepares and caches the GPI buffer for this player, processing all local and external players in the process.
     *
     * @return this for chaining.
     */
    public PlayerInfo cache() {
        buffer.clear();
        largeMaskBuffer.clear();
        resize();
        processLocalPlayers(true);
        processLocalPlayers(false);
        processOutsidePlayers(true);
        processOutsidePlayers(false);
        buffer.writeBytes(largeMaskBuffer.content());
        localIndexesCount = 0;
        externalIndexesCount = 0;
        for (int playerIndex = 1; playerIndex < 2048; playerIndex++) {
            activityFlags[playerIndex] >>= 1;
            if (localPlayers[playerIndex] == null) {
                externalIndexes[externalIndexesCount++] = playerIndex;
            } else {
                localIndexes[localIndexesCount++] = playerIndex;
            }
        }
        return this;
    }

    /**
     * @param p           the player being removed.
     * @param playerIndex the index of the player being removed. If the player needs to be removed and the index
     *                    isn't -1, we also write the necessary information in the buffer.
     * @return whether or not the requested player needs to be removed from the local players list.
     */
    private boolean remove(final Player p, final int playerIndex) {
        if (p == player) {
            return false;
        }
        if (p == null || p.isFinished() || p.isHidden() || (!player.isHeatmap() && !player.isVisibleInViewport(p))) {
            if (p != null && playerIndex != -1) {
                bitBuffer.writeBit(1);
                bitBuffer.writeBit(0);
                bitBuffer.writeBits(2, 0);
                final int hash = p.getLocation18BitHash();
                if (hash == multipliers[playerIndex]) {
                    bitBuffer.writeBit(0);
                } else {
                    bitBuffer.writeBit(1);
                    updatePositionMultiplier(multipliers[playerIndex], multipliers[playerIndex] = hash);
                }
                localPlayers[playerIndex] = null;
            }
            return true;
        }
        return false;
    }

    /**
     * @param p           the player being added.
     * @param playerIndex the index of the player being added. If the player needs to be added and the index isn't
     *                    -1, we also write the necessary information in the buffer.
     * @return whether or not the requested player needs to be added to the local players list.
     */
    private boolean add(final Player p, final int playerIndex) {
        if (canAdd(p)) {
            if (playerIndex != -1) {
                bitBuffer.writeBit(1);
                bitBuffer.writeBits(2, 0);
                final int multiplier = p.getLocation18BitHash();
                if (multiplier == multipliers[playerIndex]) {
                    bitBuffer.writeBit(0);
                } else {
                    bitBuffer.writeBit(1);
                    updatePositionMultiplier(multipliers[playerIndex], multipliers[playerIndex] = multiplier);
                }
                bitBuffer.writeBits(13, p.getX());
                bitBuffer.writeBits(13, p.getY());
                boolean updateMasks =
                        !player.isHeatmap() || player.getLocation().getDistance(p.getLocation()) <= player.getHeatmapRenderDistance();
                bitBuffer.writeBit(updateMasks ? 1 : 0);
                if (updateMasks) {
                    appendUpdateBlock(p, true);
                }
                localPlayers[p.getIndex()] = p;
                activityFlags[playerIndex] = (byte) (activityFlags[playerIndex] | 2);
            }
            return true;
        }
        return false;
    }

    private boolean canAdd(Player p) {
        return p != null && player != p && !p.isFinished() && !p.isHidden()
                && (player.isHeatmap() || player.isVisibleInViewport(p))
                && (bitBuffer.readableBits() / 8 + largeMaskBuffer.readableBytes()) < (32767 - 255);
    }

    /**
     * Appends the position changes of the requested player. As of right now, the last if-block is never reached as
     * the offsets can never exceed value 1.
     *
     * @param lastPosition    the last position multiplier transmitted to the client.
     * @param currentPosition the current position multiplier.
     */
    private void updatePositionMultiplier(final int lastPosition, final int currentPosition) {
        final int lastY = lastPosition & 255;
        final int lastX = lastPosition >> 8 & 255;
        final int lastPlane = lastPosition >> 16;
        final int currentY = currentPosition & 255;
        final int currentX = currentPosition >> 8 & 255;
        final int currentPlane = currentPosition >> 16;
        final int yOffset = currentY - lastY;
        final int xOffset = currentX - lastX;
        final int planeOffset = (currentPlane - lastPlane) & 3;
        if (currentX == lastX && currentY == lastY) {
            bitBuffer.writeBits(2, 1);
            bitBuffer.writeBits(2, planeOffset);
        } else if (Math.abs(xOffset) <= 1 && Math.abs(yOffset) <= 1) {
            bitBuffer.writeBits(2, 2);
            bitBuffer.writeBits(2, planeOffset);
            bitBuffer.writeBits(3, DirectionUtil.getMoveDirection(xOffset, yOffset));
        } else {
            bitBuffer.writeBits(2, 3);
            bitBuffer.writeBits(2, planeOffset);
            bitBuffer.writeBits(8, xOffset & 255);
            bitBuffer.writeBits(8, yOffset & 255);
        }
    }

    /**
     * Processes the players outside of our viewport; either adds, updates or skips them if necessary to do so.
     *
     * @param inactivePlayers whether or not we loop the inactive or active players.
     */
    private void processOutsidePlayers(final boolean inactivePlayers) {
        int added = 0;
        int skip = 0;
        for (int i = 0; i < externalIndexesCount; i++) {
            final int playerIndex = externalIndexes[i];
            if (inactivePlayers == ((1 & activityFlags[playerIndex]) == 0)) {
                continue;
            }
            if (skip > 0) {
                skip--;
                activityFlags[playerIndex] = (byte) (activityFlags[playerIndex] | 2);
                continue;
            }
            final Player p = players.getDirect(playerIndex);
            if (!add(p, playerIndex)) {
                added++;
                final int hash = p == null ? multipliers[playerIndex] : p.getLocation18BitHash();
                if (hash != multipliers[playerIndex]) {
                    bitBuffer.writeBit(1);
                    updatePositionMultiplier(multipliers[playerIndex], hash);
                    multipliers[playerIndex] = hash;
                } else {
                    bitBuffer.writeBit(0);
                    skip(skip += getSkippedExternalPlayers(i, inactivePlayers));
                    activityFlags[playerIndex] = (byte) (activityFlags[playerIndex] | 2);
                }
            }
        }
        buffer.writeBits(bitBuffer);
        if (skip != 0) {
            throw new IllegalStateException(inactivePlayers ? "NSN2" : "NSN3");
        }
    }

    /**
     * @param index           the current index in the loop.
     * @param inactivePlayers whether we check inactive or active players.
     * @return the amount of external players we can skip.
     */
    private int getSkippedExternalPlayers(final int index, final boolean inactivePlayers) {
        int skip = 0;
        for (int i = index + 1; i < externalIndexesCount; i++) {
            final int externalIndex = externalIndexes[i];
            if (inactivePlayers == ((1 & activityFlags[externalIndex]) == 0)) {
                continue;
            }
            final Player externalPlayer = players.getDirect(externalIndex);
            final int externalHash = externalPlayer == null ? multipliers[externalIndex] :
                    externalPlayer.getLocation18BitHash();
            if (add(externalPlayer, -1) || externalHash != multipliers[externalIndex]) {
                break;
            }
            skip++;
        }
        return skip;
    }

    /**
     * Processes the players inside of our viewport; either removes, updates or skips them if necessary to do so.
     *
     * @param inactivePlayers whether or not we loop the inactive or active players.
     */
    private void processLocalPlayers(final boolean inactivePlayers) {
        int skip = 0;
        for (int i = 0; i < localIndexesCount; i++) {
            final int playerIndex = localIndexes[i];
            if (inactivePlayers == ((activityFlags[playerIndex] & INACTIVE_FLAG) != 0)) {
                continue;
            }
            if (skip > 0) {
                skip--;
                activityFlags[playerIndex] = (byte) (activityFlags[playerIndex] | ACTIVE_TO_INACTIVE_FLAG);
                continue;
            }
            final Player p = localPlayers[playerIndex];
            if (!remove(p, playerIndex)) {
                final int walkDir = p.getWalkDirection();
                final int runDir = p.getRunDirection();
                final int crawlDir = p.getCrawlDirection();
                boolean update = p.getUpdateFlags().isUpdateRequired();
                if (player.isHeatmap()) {
                    if (player.getLocation().getDistance(p.getLocation()) > player.getHeatmapRenderDistance()) {
                        update = false;
                    }
                }
                if (bufferDoesNotHaveEnoughSpace())
                    update = false; // Buffer too full, cheaphax to prevent spamming updates
                if (update) {
                    appendUpdateBlock(p, false);
                }
                final boolean teleported = p.isTeleported();
                if (teleported || walkDir != -1 || runDir != -1 || crawlDir != -1) {
                    bitBuffer.writeBit(1);
                    bitBuffer.writeBit(update ? 1 : 0);
                    bitBuffer.writeBits(2, 3);
                    final Location lastLocation = p.getLastLocation();
                    final int xOffset = p.getX() - lastLocation.getX();
                    final int yOffset = p.getY() - lastLocation.getY();
                    final int planeOffset = p.getPlane() - lastLocation.getPlane();
                    multipliers[playerIndex] = p.getLocation18BitHash();
                    if (Math.abs(xOffset) < 16 && Math.abs(yOffset) < 16) {
                        bitBuffer.writeBit(0);
                        bitBuffer.writeBits(2, planeOffset & 3);
                        bitBuffer.writeBits(5, xOffset & 31);
                        bitBuffer.writeBits(5, yOffset & 31);
                    } else {
                        bitBuffer.writeBit(1);
                        bitBuffer.writeBits(2, planeOffset & 3);
                        bitBuffer.writeBits(14, xOffset & 16383);
                        bitBuffer.writeBits(14, yOffset & 16383);
                    }
                } else if (update) {
                    bitBuffer.writeBit(1);
                    bitBuffer.writeBit(1);
                    bitBuffer.writeBits(2, 0);
                } else {
                    bitBuffer.writeBit(0);
                    skip(skip += getSkippedLocalPlayers(i, inactivePlayers));
                    activityFlags[playerIndex] = (byte) (activityFlags[playerIndex] | 2);
                }
            }
        }
        buffer.writeBits(bitBuffer);
        if (skip != 0) {
            throw new IllegalStateException(inactivePlayers ? "NSN0" : "NSN1");
        }
    }

    private boolean bufferDoesNotHaveEnoughSpace() {
        final long reservedSpace = 8L * largeMaskBuffer.readableBytes();
        final int remainingSpaceRequired = 8 * 5000;
        return !bitBuffer.isWritable(reservedSpace + remainingSpaceRequired);
    }

    /**
     * @param index           the current index in the loop.
     * @param inactivePlayers whether we check inactive or active players.
     * @return the amount of local players we can skip.
     */
    private int getSkippedLocalPlayers(final int index, final boolean inactivePlayers) {
        int skip = 0;
        for (int i = index + 1; i < localIndexesCount; i++) {
            final int localIndex = localIndexes[i];
            if (inactivePlayers == ((1 & activityFlags[localIndex]) != 0)) {
                continue;
            }
            final Player localPlayer = localPlayers[localIndex];
            if (remove(localPlayer, -1)
                    || localPlayer.getWalkDirection() != -1
                    || localPlayer.getRunDirection() != -1
                    || localPlayer.getUpdateFlags().isUpdateRequired()) {
                break;
            }
            skip++;
        }
        return skip;
    }

    /**
     * Write the amount of players skipped into the buffer.
     *
     * @param count the amount of players skipped.
     */
    private void skip(final int count) {
        if (count == 0) {
            bitBuffer.writeBits(2, 0);
        } else if (count < 32) {
            bitBuffer.writeBits(2, 1);
            bitBuffer.writeBits(5, count);
        } else if (count < 256) {
            bitBuffer.writeBits(2, 2);
            bitBuffer.writeBits(8, count);
        } else {
            bitBuffer.writeBits(2, 3);
            bitBuffer.writeBits(11, count);
        }
    }
    private void appendUpdateBlock(final Player p, final boolean added) {
        if (USE_MASK_DATA_CACHING) {
            appendUpdateBlockCache(p, added);
            return;
        }
        final UpdateFlags flags = p.getUpdateFlags();
        int flag = 0;
        smallMaskBuffer.clear();
        for (int i = 0; i < length; i++) {
            final UpdateMask mask = masks[i];
            if (mask.apply(player, p, flags, added)) {
                flag |= mask.getFlag().getPlayerMask();
                mask.writePlayer(smallMaskBuffer, player, p);
            }
        }
        if (flag >= 255) {
            flag |= 0x40;
        }
        largeMaskBuffer.writeByte(flag);
        if (flag >= 255) {
            largeMaskBuffer.writeByte(flag >> 8);
        }
        largeMaskBuffer.writeBytes(smallMaskBuffer.content());
    }
    /**
     * Appends the update block into the small mask buffer, and then into the large mask buffer.
     *
     * @param p     the player whose block is being written.
     * @param added whether or not the player was added to the viewport during this cycle.
     */
    private void appendUpdateBlockCache(final Player p, final boolean added) {
        final UpdateFlags flags = p.getUpdateFlags();
        boolean canUseCache;
        final Map<String, MaskCacheEntry> cache = added ? maskDataAddedCache : maskDataCache;
        MaskCacheEntry maskCacheEntry = cache.get(p.getUsername());
        if (maskCacheEntry == null) {
            maskCacheEntry = new MaskCacheEntry();
            cache.put(p.getUsername(), maskCacheEntry);
            canUseCache = false;
        } else
            canUseCache = !maskCacheEntry.isExpired();

        final boolean writeMasks[] = new boolean[length];
        int flag = 0;
        for (int i = 0; i < length; i++) {
            final UpdateMask mask = masks[i];
            if (mask.apply(player, p, flags, added)) {
                flag |= mask.getFlag().getPlayerMask();
                writeMasks[i] = true;
                if (mask.getFlag() == UpdateFlag.HIT) {
                    canUseCache = false;
                }
            }
        }
        if (flag >= 255) {
            flag |= 0x40;
        }
        if (!canUseCache) {
            maskCacheEntry.reset();
            final RSBuffer buffer = maskCacheEntry.getBuffer();
            buffer.clear();
            for (int i = 0; i < length; i++) {
                if (writeMasks[i]) {
                    final UpdateMask mask = masks[i];
                    mask.writePlayer(buffer, player, p);
                }
            }
        }
        largeMaskBuffer.writeByte(flag);
        if (flag >= 255) {
            largeMaskBuffer.writeByte(flag >> 8);
        }
        largeMaskBuffer.writeBytes(maskCacheEntry.getBuffer().content().retainedDuplicate());
    }

    @Override
    public GamePacketOut encode() {
        buffer.retain();
        return buffer;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    @Override
    public GamePacketEncoderMode encoderMode() {
        return GamePacketEncoderMode.WRITE;
    }

}

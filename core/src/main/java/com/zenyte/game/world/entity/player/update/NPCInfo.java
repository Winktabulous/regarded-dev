package com.zenyte.game.world.entity.player.update;

import com.near_reality.buffer.BitBuf;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.GamePacketEncoderMode;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.UpdateFlags;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.update.mask.*;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.net.NetworkConstants;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import com.zenyte.net.io.RSBuffer;
import io.netty.buffer.ByteBufAllocator;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 1. veebr 2018 : 22:11.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 * profile</a>
 */
public final class NPCInfo implements GamePacketEncoder {

    /**
     * The maximum number of NPCs that can be seen in a player's viewport at any given time.
     */
    private static final int SV_LOCAL_NPCS_LIMIT = 250;

    /**
     * An array of update masks in the respective order they're read in the client. PS: Order must be preserved!
     */
    private static final UpdateMask[] masks = new UpdateMask[]{
            new FaceEntityMask(),
            new AnimationMask(),
            new HideOptionsMask(),
            new ModelOverrideMask(),
            new ForceMovementMask(),
            new FaceLocationMask(),
            new CombatLevelChangeMask(),
            new NpcOverheadMask(),
            new NameChangeMask(),
            new HitMask(),
            new ForceChatMask(),
            new TintingMask(),
            new GraphicsMask(),
            new TransformationMask()
    };

    /**
     * The length of the masks.
     */
    private static final int length = masks.length;

    private Player player;

    /**
     * A linked hash set used to keep an ordered collection of npcs currently in the player's viewport.
     */
    private ObjectSet<NPC> localNPCs;

    /**
     * The main cache buffer that's transmitted to the client.
     */
    private RSBuffer cache;

    /**
     * The bitbuffer used for writing bit information of the npcs.
     */
    private BitBuf bitBuffer;

    /**
     * The small mask buffer that is used per-npc basis and cleared after every npc.
     */
    private RSBuffer smallMaskBuffer;

    /**
     * The large mask buffer to which all of the small buffer are written.
     */
    private RSBuffer largeMaskBuffer;

    private boolean clearLocalNpcs;

    public void reset() {
        player = null;
        if (localNPCs != null) {
            localNPCs.clear();
            localNPCs = null;
        }
        if (cache != null) {
            cache.close();
            cache = null;
        }
        if (bitBuffer != null) {
            if (bitBuffer.refCnt() > 0) bitBuffer.release();
            bitBuffer = null;
        }
        if (smallMaskBuffer != null) {
            smallMaskBuffer.close();
            smallMaskBuffer = null;
        }
        if (largeMaskBuffer != null) {
            largeMaskBuffer.close();
            largeMaskBuffer = null;
        }
    }

    public NPCInfo(final Player player) {
        this.player = player;
    }

    /**
     * Prepares and caches the GNI buffer for the player, processing local and external npcs respectively.
     *
     * @return this for chaining.
     */
    public synchronized NPCInfo cache() {
        if (this.cache == null) {
            localNPCs = new ObjectLinkedOpenHashSet<>(255);
            bitBuffer = new BitBuf(ByteBufAllocator.DEFAULT.directBuffer(255, NetworkConstants.MAX_SERVER_BUFFER_SIZE));
            smallMaskBuffer = new RSBuffer(255, NetworkConstants.MAX_SERVER_BUFFER_SIZE);
            this.largeMaskBuffer = new RSBuffer(255, NetworkConstants.MAX_SERVER_BUFFER_SIZE);
            this.cache = new RSBuffer(255, NetworkConstants.MAX_SERVER_BUFFER_SIZE);
        }

        cache.clear();
        largeMaskBuffer.clear();
        bitBuffer.clear();
        processNPCs();
        final int length = cache.readableBytes();
        cache.writeBits(bitBuffer);
        if (length == cache.readableBytes()) {
            throw new IllegalStateException("Unable to write bytes from bitbuffer: "
                    + bitBuffer.readerIndex() + ", " + bitBuffer.writerIndex());
        }
        cache.writeBytes(largeMaskBuffer.content());
        return this;
    }

    /**
     * Processes local and external npcs respectively. Writes an additional 15 bits in the very end if any masks were
     * written.
     */
    private void processNPCs() {
        processLocalNPCs();
        clearLocalNpcs = false;
        processExternalNPCs();
        if (largeMaskBuffer.isReadable()) {
            bitBuffer.writeBits(16, 65535);
        }
    }

    /**
     * Processes the local npcs, removes them from the viewport if necessary.
     */
    private void processLocalNPCs() {
        bitBuffer.writeBits(8, localNPCs.size());
        localNPCs.removeIf(npc -> {
            if (clearLocalNpcs || npc.isFinished() || !player.isVisibleInViewport(npc) || npc.isTeleported()) {
                bitBuffer.writeBit(1);
                bitBuffer.writeBits(2, 3);
                return true;
            }
            final boolean needUpdate = npc.getUpdateFlags().isUpdateRequired();
            final int walkDirection = npc.getWalkDirection();
            final int runDirection = npc.getRunDirection();
            final int crawlDirection = npc.getCrawlDirection();

            final boolean walkUpdate = walkDirection != -1 || crawlDirection != -1 || runDirection != -1;
            if (needUpdate) {
                appendUpdateBlock(npc, false);
            }
            bitBuffer.writeBit(needUpdate || walkUpdate ? 1 : 0);
            if (walkUpdate) {
                if (crawlDirection != -1) {
                    bitBuffer.writeBits(2, 2);
                    bitBuffer.writeBit(0);
                    bitBuffer.writeBits(3, Utils.getNPCWalkingDirection(crawlDirection));
                } else if (runDirection != -1) {
                    bitBuffer.writeBits(2, 2);
                    bitBuffer.writeBit(1);
                    bitBuffer.writeBits(3, Utils.getNPCWalkingDirection(walkDirection));
                    bitBuffer.writeBits(3, Utils.getNPCWalkingDirection(runDirection));
                } else {
                    bitBuffer.writeBits(2, 1);
                    bitBuffer.writeBits(3, Utils.getNPCWalkingDirection(walkDirection));
                }
                bitBuffer.writeBit(needUpdate ? 1 : 0);
            } else if (needUpdate) {
                bitBuffer.writeBits(2, 0);
            }
            return false;
        });
    }

    /**
     * Processes the external npcs, adds them to the viewport if necessary.
     */
    private void processExternalNPCs() {
        final int viewDistance = player.getViewDistance();
        final boolean largeSceneView = viewDistance > 15;
        final int numberOfBits = largeSceneView ? 8 : 5;
        final int distance = largeSceneView ? 255 : 31;
        CharacterLoop.forEach(player.getLocation(), viewDistance, NPC.class, npc -> {
            if (localNPCs.size() >= SV_LOCAL_NPCS_LIMIT || !player.isVisibleInViewport(npc) || !localNPCs.add(npc))
                return;
            final boolean needUpdate =
                    npc.getUpdateFlags().isUpdateRequired() || npc.getFaceEntity() != -1 || player.updateNPCOptions(npc);
            if (needUpdate) {
                appendUpdateBlock(npc, true);
            }
            final int x = npc.getX() - player.getX();
            final int y = npc.getY() - player.getY();
            bitBuffer.writeBits(16, npc.getIndex());
            bitBuffer.writeBits(3, npc.getRoundedDirection());
            bitBuffer.writeBits(numberOfBits, x & distance);
            bitBuffer.writeBit(needUpdate ? 1 : 0);
            bitBuffer.writeBit(0); // used to write the npc's spawn cycle, steam client.
            bitBuffer.writeBit(npc.isTeleported() ? 1 : 0);
            bitBuffer.writeBits(numberOfBits, y & distance);
            bitBuffer.writeBits(14, npc.getId());
        });
    }

    /**
     * Processes the update blocks of this npc.
     *
     * @param npc   the npc whose update blocks are processed
     * @param added whether or not the npc was just added to the viewport.
     */
    private void appendUpdateBlock(final NPC npc, boolean added) {
        final UpdateFlags flags = npc.getUpdateFlags();
        int flag = 0;
        smallMaskBuffer.clear();
        for (int i = 0; i < length; i++) {
            final UpdateMask mask = masks[i];
            if (mask.apply(player, npc, flags, added)) {
                flag |= mask.getFlag().getNpcMask();
                mask.writeNPC(smallMaskBuffer, player, npc);
            }
        }
        if (flag >= 0xff) {
            flag |= 0x2;
        }
        if (flag >= 0xffff) {
            flag |= 0x4000;
        }
        largeMaskBuffer.writeByte(flag);
        if (flag >= 0xff) {
            largeMaskBuffer.writeByte(flag >> 8);
        }
        if (flag >= 0xffff) {
            largeMaskBuffer.writeByte(flag >> 16);
        }
        largeMaskBuffer.writeBytes(smallMaskBuffer.content());
    }

    @Override
    public GamePacketOut encode() {
        final ServerProt prot =
                player.getViewDistance() > 15
                        ? ServerProt.NPC_INFO_LARGE
                        : ServerProt.NPC_INFO_SMALL;
        cache.retain();
        return prot.gamePacketOut(
                cache
        );
    }

    @Override
    public @NotNull GamePacketEncoderMode encoderMode() {
        return GamePacketEncoderMode.WRITE;
    }

    @Override
    public LogLevel level() {
        return LogLevel.LOW_PACKET;
    }

    public ObjectSet<NPC> getLocalNPCs() {
        return localNPCs;
    }

    public void setClearLocalNpcs(boolean clearLocalNpcs) {
        this.clearLocalNpcs = clearLocalNpcs;
    }
}

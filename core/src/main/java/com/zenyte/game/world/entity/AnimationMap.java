package com.zenyte.game.world.entity;

import com.zenyte.CacheManager;
import com.zenyte.game.world.DefaultGson;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.types.skeleton.SkeletonDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Kris | 18/11/2018 18:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AnimationMap {
    private static final Logger log = LoggerFactory.getLogger(AnimationMap.class);
    private int crc;
    private Int2ObjectOpenHashMap<IntOpenHashSet> map;
    private static AnimationMap singleton;

    public static void parse() {
        try {
            final BufferedReader br = new BufferedReader(new FileReader("data/animations.json"));
            singleton = DefaultGson.getGson().fromJson(br, AnimationMap.class);
            if (singleton.map == null) {
                singleton.map = new Int2ObjectOpenHashMap<>();
            }
        } catch (
        //singleton.verifyCRC();
        IOException e) {
            log.error("", e);
        }
    }

    public static boolean isValidAnimation(final int npcId, final int animationId) {
        if (animationId < 0 || animationId >= 15000) return true;
        final int animationFrame = getAnimationFrameMap(animationId);
        if (npcId == -1 && animationFrame == 0) return true;
        final NPCDefinitions npcDefinitions = NPCDefinitions.get(npcId);
        if (npcDefinitions == null) return false;
        final int stand = npcDefinitions.getStandAnimation();
        final int run = npcDefinitions.getWalkAnimation();
        if (stand == -1 && run == -1) {
            return false;
        }
        return animationFrame == getAnimationFrameMap(stand == -1 ? run : stand);
    }

    private static int getAnimationFrameMap(final int animationId) {
        final AnimationDefinitions definitions = AnimationDefinitions.get(animationId);
        if (definitions == null) {
            return -1;
        }
        final int[] frameIds = definitions.getFrameIds();
        if (frameIds != null && frameIds.length > 0) {
            return SkeletonDefinitions.get(frameIds[0]).getFrameMapId();
        }
        final int[] additionalFrameIds = definitions.getExtraFrameIds();
        if (additionalFrameIds != null && additionalFrameIds.length > 0) {
            return SkeletonDefinitions.get(additionalFrameIds[0]).getFrameMapId();
        }
        return -1;
    }

    private void verifyCRC() {
        if (singleton.crc == getCRC()) {
            return;
        }
        System.err.println("CRC mismatch in animation map - extracting new animation map.");
        refresh();
        crc = getCRC();
        save();
    }

    private int getCRC() {
        return CacheManager.getCRC(2);
    }

    public void save() {
        try {
            final PrintWriter pw = new PrintWriter("data/animations.json", StandardCharsets.UTF_8);
            pw.println(DefaultGson.getGson().toJson(singleton));
            pw.close();
            System.err.println("Animation map successfully saved.");
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public static void refresh() {
        try {
            final Int2ObjectOpenHashMap<IntOpenHashSet> map = new Int2ObjectOpenHashMap<IntOpenHashSet>();
            final Int2ObjectOpenHashMap<SkeletonDefinitions> skeletonDefinitions = SkeletonDefinitions.getDefinitions();
            final ObjectIterator<Int2ObjectMap.Entry<SkeletonDefinitions>> iterator = skeletonDefinitions.int2ObjectEntrySet().fastIterator();
            final int length = SkeletonDefinitions.getDefinitions().size();
            int count = 0;
            while (iterator.hasNext()) {
                final Int2ObjectMap.Entry<SkeletonDefinitions> next = iterator.next();
                try {
                    final SkeletonDefinitions definitions = next.getValue();
                    final int frameMapId = definitions.getFrameMapId();
                    if (map.containsKey(frameMapId)) {
                        System.err.println("[Skipping] Progress: " + ++count + "/" + length);
                        continue;
                    }
                    final IntOpenHashSet animations = new IntOpenHashSet();
                    final IntOpenHashSet frameIds = SkeletonDefinitions.getLinkedFrames(frameMapId);
                    for (final Integer frameId : frameIds) {
                        final IntArrayList foundAnimations = AnimationDefinitions.getAnimationIdsByFrameId(frameId, frameIds);
                        final IntListIterator it = foundAnimations.listIterator();
                        while (it.hasNext()) {
                            final int foundAnimation = it.nextInt();
                            if (foundAnimation != -1 && !animations.contains(foundAnimation)) {
                                animations.add(foundAnimation);
                            }
                        }
                    }
                    map.put(frameMapId, animations);
                    System.err.println("Progress: " + ++count + "/" + length);
                } catch (final Exception e) {
                    log.error("", e);
                }
            }
            singleton.map = map;
            System.err.println("Animation dump by frame map complete.");
        } catch (final Exception e) {
            log.error("", e);
        }
    }
}

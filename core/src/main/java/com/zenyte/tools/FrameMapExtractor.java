package com.zenyte.tools;

import com.zenyte.game.GameConstants;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import mgi.Indice;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.SpotAnimationDefinition;
import mgi.types.config.ObjectDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.types.skeleton.SkeletonDefinitions;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;

/**
 * @author Kris | 19. sept 2018 : 17:15:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class FrameMapExtractor implements Extractor {

	private static final Logger log = LoggerFactory.getLogger(FrameMapExtractor.class);

	@Override
	public void extract() {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + " frame map.txt")));
			final Int2ObjectAVLTreeMap<IntArrayList> map = new Int2ObjectAVLTreeMap<>();
			final Int2ObjectOpenHashMap<SkeletonDefinitions> skeletonDefinitions = SkeletonDefinitions.getDefinitions();
			final ObjectIterator<Int2ObjectMap.Entry<SkeletonDefinitions>> iterator = skeletonDefinitions.int2ObjectEntrySet().fastIterator();
			final int length = CollectionUtils.getIndiceSize(Indice.SKELETON);
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
					final IntArrayList animations = new IntArrayList();
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
					Collections.sort(animations);
					map.put(frameMapId, animations);
					System.err.println("Progress: " + ++count + "/" + length);
				} catch (final Exception e) {
                    log.error("", e);
				}
			}

			map.forEach((i, list) -> {
				try {
					writer.write("Frame map: " + i);
					writer.newLine();
					if (!list.isEmpty()) {
                        writer.write("Linked animations: " + list);
                        writer.newLine();
                    }
					final IntArrayList linkedNPCs = new IntArrayList();
					for (int a = 0; a < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); a++) {
						final NPCDefinitions definitions = NPCDefinitions.get(a);
						if (definitions == null)
						    continue;
						if (list.contains(definitions.getStandAnimation()) || list.contains(definitions.getWalkAnimation())) {
							if (!linkedNPCs.contains(a)) {
								linkedNPCs.add(a);
							}
						}
					}
					if (!linkedNPCs.isEmpty()) {
						final StringBuilder builder = new StringBuilder();
						builder.append("Linked NPCs: ");
						for (final int id : linkedNPCs) {
							final NPCDefinitions npcDefinitions = NPCDefinitions.get(id);
							if (npcDefinitions == null) {
								continue;
							}
							builder.append(npcDefinitions.getName()).append(" (").append(id).append("), ");
						}
						if (builder.length() > 2) {
							builder.delete(builder.length() - 2, builder.length());
						}
						if (builder.length() > 0) {
							writer.write(builder.toString());
							writer.newLine();
						}
					}

					final IntArrayList linkedGFXs = new IntArrayList();
					for (int a = 0; a < CollectionUtils.getIndiceSize(Indice.GRAPHICS_DEFINITIONS); a++) {
						final SpotAnimationDefinition definitions = SpotAnimationDefinition.get(a);
						if (definitions == null) {
							continue;
						}
						if (list.contains(definitions.getAnimationId())) {
							if (!linkedGFXs.contains(a)) {
								linkedGFXs.add(a);
							}
						}

					}
					if (!linkedGFXs.isEmpty()) {
						final StringBuilder builder = new StringBuilder();
						builder.append("Linked graphics: ");
						for (final int id : linkedGFXs) {
							final SpotAnimationDefinition graphicsDefinitions = SpotAnimationDefinition.get(id);
							if (graphicsDefinitions == null) {
								continue;
							}
							builder.append(id).append(", ");
						}
						if (builder.length() > 2) {
							builder.delete(builder.length() - 2, builder.length());
						}
						if (builder.length() > 0) {
							writer.write(builder.toString());
							writer.newLine();
						}
					}

					final IntArrayList linkedObjects = new IntArrayList();
					for (int a = 0; a < CollectionUtils.getIndiceSize(Indice.OBJECT_DEFINITIONS); a++) {
						final ObjectDefinitions definitions = ObjectDefinitions.get(a);
						if (definitions == null) {
						    continue;
                        }
                        if (list.contains(definitions.getAnimationId())) {
							if (!linkedObjects.contains(a)) {
								linkedObjects.add(a);
							}
						}
					}
					if (!linkedObjects.isEmpty()) {
						final StringBuilder builder = new StringBuilder();
						builder.append("Linked objects: ");
						for (final int id : linkedObjects) {
							final ObjectDefinitions objectDefinitions = ObjectDefinitions.get(id);
							if (objectDefinitions == null) {
							    continue;
                            }
                            builder.append(objectDefinitions.getName()).append(" (").append(id).append("), ");
						}
						if (builder.length() > 2) {
							builder.delete(builder.length() - 2, builder.length());
						}
						if (builder.length() > 0) {
							writer.write(builder.toString());
							writer.newLine();
						}
					}
					writer.newLine();
				} catch (final Exception e) {
                    log.error("", e);
				}
			});
			System.err.println("Animation dump by frame map complete.");
			writer.flush();
			writer.close();
		} catch (final Exception e) {
            log.error("", e);
		}
	}

}

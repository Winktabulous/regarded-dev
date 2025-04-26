package com.zenyte.tools;

import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.npc.race.Race;
import mgi.Indice;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Kris | 3. apr 2018 : 3:51.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AnimationExtractor implements Extractor {

	private static final Logger log = LoggerFactory.getLogger(AnimationExtractor.class);

	@Override
	public void extract() {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + " animations.txt")));
			final int len = CollectionUtils.getIndiceSize(Indice.ANIMATION_DEFINITIONS);
			
			for (final Race race : Race.values()) {
				writer.write("Race: " + StringFormatUtil.formatString(race.toString()));
				writer.newLine();
				writer.write("Suitable NPCs: ");
				for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); i++) {
					final NPCDefinitions defs = NPCDefinitions.get(i);
					if (defs == null) {
						continue;
					}
					final AnimationDefinitions anim = AnimationDefinitions.get(defs.getStandAnimation());
					if (anim == null) {
						continue;
					}
					if (anim.getFrameIds() == null) {
						continue;
					}
					if (anim.getFrameIds().length == 0) {
						continue;
					}
					final int skeleton = anim.getFrameIds()[0] >> 16;
					if (ArrayUtils.contains(race.getSkeletonIds(), skeleton)) {
						writer.write(defs.getName() + "(" + i + "), ");
					}
				}
				writer.newLine();
				writer.write("----------");
				writer.newLine();
			}
			
			final String br = System.getProperty("line.separator");
			for (int i = 0; i < len; i++) {
				final StringBuilder builder = new StringBuilder();
				final AnimationDefinitions defs = AnimationDefinitions.get(i);
				if (defs == null) {
					continue;
				}
				builder.append("Animation: ").append(defs.getId()).append(br);
				final int skeletonId = (defs.getFrameIds()[0] >> 16);
				final Race race = Race.MAP.get(skeletonId);
				
				if (race != null) {
					builder.append("Race: ").append(StringFormatUtil.formatString(race.toString())).append(br);
				}
					
				builder.append("Duration: ").append(defs.getDuration()).append("ms").append(br);
				if (defs.getIterations() > 1 && defs.getIterations() < 99) {
					builder.append("Loops: ").append(defs.getIterations()).append(br);
					builder.append("Full duration: ").append(defs.getIterations() * defs.getDuration()).append("ms").append(br);
				}
				final List<Integer> soundEffects = new ArrayList<Integer>();
				if (defs.getSoundEffects() != null) {
					for (final AnimationDefinitions.Sound x : defs.getSoundEffects().values()) {
							soundEffects.add(x.id);
					}
				}
				if (!soundEffects.isEmpty()) {
					builder.append("Sound effects: ").append(Arrays.toString(soundEffects.toArray(new Integer[0]))).append(br);
				}
				if (defs.getLeftHandItem() > 0) {
					builder.append("Left item: ").append(ItemDefinitions.get(defs.getLeftHandItem()).getName()).append(", ").append(defs.getLeftHandItem()).append(br);
				}
				if (defs.getRightHandItem() > 0) {
					try {
					builder.append("Right item: ").append(ItemDefinitions.get(defs.getRightHandItem()).getName()).append(", ").append(defs.getRightHandItem()).append(br);
					} catch (final Exception a) {
						a.printStackTrace();
						System.err.println(defs.getRightHandItem() + ", " + defs.getId());
					}
				}
				if (race == null) {
					final Map<Integer, String> npcs = new HashMap<Integer, String>();
					for (int x = 0; x < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); x++) {
						final NPCDefinitions npcDefs = NPCDefinitions.get(x);
						if (npcDefs == null) {
							continue;
						}
						if (npcDefs.getStandAnimation() > 0) {
							final AnimationDefinitions animDefs = AnimationDefinitions.get(npcDefs.getStandAnimation());
							if (animDefs == null) {
								continue;
							}
							final int id = animDefs.getFrameIds()[0] >> 16;
							if (id == skeletonId) {
								npcs.put(x, npcDefs.getName());
							}
						}
					}
					if (!npcs.isEmpty()) {

						final StringBuilder b = new StringBuilder();

						final Iterator<Entry<Integer, String>> it = npcs.entrySet().iterator();
						int count = 0;
						while (it.hasNext()) {
							final Entry<Integer, String> x = it.next();
							if (x.getValue().equals("null")) {
								continue;
							}
							b.append(x.getValue()).append("(").append(x.getKey()).append("), ");
							if (it.hasNext() && ++count % 11 == 10) {
								b.append(br);
							}
						}
						if (b.length() > 0) {
							b.delete(b.length() - 2, b.length());
							builder.append("Suitable monsters: ").append(b).append(br);
						}
					}
				}
				
				writer.write(builder.toString());
				writer.write("----------" + br);
				
			}
			writer.flush();
			writer.close();
		} catch (final Exception e) {
            log.error("", e);
		}
	}

}

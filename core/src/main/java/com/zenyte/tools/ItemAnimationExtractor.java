package com.zenyte.tools;

import com.zenyte.game.GameConstants;
import mgi.Indice;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author Kris | 5. march 2018 : 17:22.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ItemAnimationExtractor implements Extractor {

	private static final Logger log = LoggerFactory.getLogger(ItemAnimationExtractor.class);

	@Override
	public void extract() {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + " item-animations.txt")));
			final int len = CollectionUtils.getIndiceSize(Indice.ANIMATION_DEFINITIONS);
			for (int i = 0; i < len; i++) {
				final AnimationDefinitions defs = AnimationDefinitions.get(i);
				if (defs == null)
					continue;
				if (defs.getLeftHandItem() > 0 || defs.getRightHandItem() > 0) {
					final StringBuilder line = new StringBuilder();
					line.append(i + ": ");
					if (defs.getLeftHandItem() > 0) {
						final ItemDefinitions item = ItemDefinitions.get(defs.getLeftHandItem());
						line.append("Left: " + (item == null ? "null" : item.getName()) + "(" + (defs.getLeftHandItem()) + "), ");
					} 
					if (defs.getRightHandItem() > 0) {
						final ItemDefinitions item = ItemDefinitions.get(defs.getRightHandItem());
						line.append("Right: " + (item == null ? "null" : item.getName()) + "(" + (defs.getRightHandItem()) + "), ");
					} 
					writer.write(line.toString());
					if (i < len)
						writer.newLine();
				}
			}
			writer.flush();
			writer.close();
		} catch (final Exception e) {
            log.error("", e);
		}
	}
	
}

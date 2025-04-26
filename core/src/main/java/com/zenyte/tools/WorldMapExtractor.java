package com.zenyte.tools;

import com.zenyte.game.GameConstants;
import mgi.Indice;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Extracts information from the world map.
 * @author Kris | 5. march 2018 : 17:12.30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WorldMapExtractor implements Extractor {

	private static final Logger log = LoggerFactory.getLogger(WorldMapExtractor.class);

	@Override
	public void extract() {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + " coords-list.txt")));
			final int len = CollectionUtils.getIndiceSize(Indice.WORLD_MAP);
			for (int i = 0; i < len; i++) {
				/*final WorldMapDefinitions defs = WorldMapDefinitions.get(i);
				if (defs == null)
					continue;
				//writer.write(i + " - " + defs.getName() + " | " + defs.getFileName() + ": " + defs.getLocation());
				if (i < len)
					writer.newLine();*/
			}
			writer.flush();
			writer.close();
		} catch (final Exception e) {
            log.error("", e);
		}
	}
	
}

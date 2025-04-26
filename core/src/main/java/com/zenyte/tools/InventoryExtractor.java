package com.zenyte.tools;

import com.zenyte.game.GameConstants;
import mgi.Indice;
import mgi.types.config.InventoryDefinitions;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author Kris | 13. march 2018 : 2:02.16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class InventoryExtractor implements Extractor {

	private static final Logger log = LoggerFactory.getLogger(InventoryExtractor.class);

	@Override
	public void extract() {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + " inventory-sizes.txt")));
			//final int len = CacheUtil.getStore().getFilesSystem(2).findFolderByID(5).filesCount();//Cache.STORE.getIndexes()[2].getLastFileId(5);
			final int len = CollectionUtils.getIndiceSize(Indice.INVENTORY_SIZE_DEFINITIONS);
			for (int i = 0; i < len; i++) {
				final InventoryDefinitions defs = InventoryDefinitions.get(i);
				writer.write(i + ": " + defs.getSize());
				if (i < len) {
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

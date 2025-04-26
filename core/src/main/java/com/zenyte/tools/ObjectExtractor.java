package com.zenyte.tools;

import com.zenyte.game.GameConstants;
import mgi.Indice;
import mgi.types.config.ObjectDefinitions;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

/**
 * @author Kris | 5. march 2018 : 17:49.35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ObjectExtractor implements Extractor {

	private static final Logger log = LoggerFactory.getLogger(ObjectExtractor.class);

	@Override
	public void extract() {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + " object-defs.txt")));
			//final int len = CacheUtil.getStore().getFilesSystem(2).findFolderByID(6).filesCount(); //.STORE.getIndexes()[2].getLastFileId(6);
			final int len = CollectionUtils.getIndiceSize(Indice.OBJECT_DEFINITIONS);
			for (int i = 0; i < len; i++) {
				final ObjectDefinitions defs = ObjectDefinitions.get(i);
				if (defs == null) {
					continue;
				}
				final StringBuilder line = new StringBuilder();
				line.append(i + ": " + defs.getName() + ", ");
				if (defs.getVarp() != -1) {
					line.append("varp: " + defs.getVarp() + ", ");
				}
				if (defs.getVarbit() != -1) {
					line.append("varbit: " + defs.getVarbit() + ", ");
				}
				if (defs.getAnimationId() != -1) {
					line.append("animation: " +defs.getAnimationId() + ", ");
				}
				if (defs.getTypes() == null) {
					line.append("types: [10]");
				} else {
					line.append("types: " + Arrays.toString(defs.getTypes()));
				}
				writer.write(line.toString());
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

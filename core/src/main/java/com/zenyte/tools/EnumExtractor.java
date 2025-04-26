package com.zenyte.tools;

import com.zenyte.game.GameConstants;
import mgi.Indice;
import mgi.types.config.enums.EnumDefinitions;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Kris | 6. march 2018 : 2:47.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class EnumExtractor implements Extractor {

	private static final Logger log = LoggerFactory.getLogger(EnumExtractor.class);

	@Override
	public void extract() {
		try {
			//final int len = CacheUtil.getStore().getFilesSystem(2).findFolderByID(8).filesCount();
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + " enum-list.txt")));
			for (final EnumDefinitions t : EnumDefinitions.definitions.values()) {
				try {
					if (t == null) {
						continue;
					}
					if (t.getKeyType() != 0) {
						writer.write("[" + t.getId() + "]\r\n");
						writer.write("inputtype=" + EnumDefinitions.TYPE_MAP.get(t.getKeyType()) + "\r\n");
						writer.write("outputtype=" + EnumDefinitions.TYPE_MAP.get(t.getValueType()) + "\r\n");
						if (t.getValueType() == 's') {
							writer.write("default=" + EnumDefinitions.getPrettyValue(t.getValueType(), t.getDefaultString()) + "\r\n");
						} else {
							writer.write("default=" + EnumDefinitions.getPrettyValue(t.getValueType(), t.getDefaultInt()) + "\r\n");
						}

						for (final Map.Entry<Integer, Object> param : t.getValues().entrySet()) {
							writer.write("val=" + EnumDefinitions.getPrettyValue(t.getKeyType(), param.getKey()) + "," + EnumDefinitions.getPrettyValue(t.getValueType(), param.getValue()) + "\r\n");
						}
						writer.write("-----");
						writer.write("\r\n");
					}
				} catch (final IOException e) {
                    log.error("", e);
				}
			}
			writer.flush();
			writer.close();
		} catch (final Exception e) {
            log.error("", e);
		}
	}

}

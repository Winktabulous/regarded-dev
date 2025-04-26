package com.zenyte.tools;

import com.zenyte.game.GameConstants;
import mgi.Indice;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * @author Kris | 5. march 2018 : 17:42.22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class NPCExtractor implements Extractor {

	private static final Logger log = LoggerFactory.getLogger(NPCExtractor.class);

	@Override
	public void extract() {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + " npc-defs.txt")));
			//final int len = CacheUtil.getStore().getFilesSystem(2).findFolderByID(9).filesCount();//Cache.STORE.getIndexes()[2].getLastFileId(9);
			final int len = CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS);
			for (int i = 0; i < len; i++) {
				final NPCDefinitions defs = NPCDefinitions.get(i);
				if (defs == null) {
					continue;
				}
				/*if (defs.getAreaName().equals("null")) {
					continue;
				}*/
				final StringBuilder line = new StringBuilder();
				line.append(i + ": " + defs.getName() + ", ");
				List<Integer> anims = null;
				try {
					anims = AnimationDefinitions.getSkeletonAnimations(defs.getWalkAnimation());
				} catch (final Throwable e) {
					continue;
				}
				anims.remove((Integer) defs.getStandAnimation());
				anims.remove((Integer) defs.getWalkAnimation());
				anims.add(0, defs.getStandAnimation());
				anims.add(1, defs.getWalkAnimation());
				//if (anims.size() < 20) {
					line.append(anims);
				/*} else {
					line.append("[" + defs.getStandAnimation() + ", " + defs.getWalkAnimation() + "]");
				}*/
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

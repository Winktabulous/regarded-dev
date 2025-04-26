package com.zenyte.tools;

import com.zenyte.game.GameLoader;
import mgi.types.Definitions;

import java.util.Arrays;

/**
 * Extracts the files you see in Zenyte#info folder 
 * to be up to date with the current cache.
 * @author Kris | 5. march 2018 : 17:07.00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class InfoExtractor {
	
	private static final Class<?>[] EXTRACTABLES = new Class<?>[] {
		WorldMapExtractor.class,
		ItemAnimationExtractor.class,
		ItemExtractor.class,
		NPCExtractor.class,
		ObjectExtractor.class,
		EnumExtractor.class,
			StructExtractor.class,
		InventoryExtractor.class,
		InterfaceExtractor.class,
		//AnimationExtractor.class,
		//FrameMapExtractor.class
	};

	public static final void main(final String... args) {
		try {
			GameLoader.load();
            Definitions.loadDefinitions(Definitions.lowPriorityDefinitions);
			for (final Class<?> clazz : EXTRACTABLES) {
				if (!Extractor.class.isAssignableFrom(clazz)) {
					continue;
				}
				final Extractor extractor = (Extractor) clazz.newInstance();
				extractor.extract();
			}
			System.out.println("Successfully extracted classes: " + Arrays.toString(EXTRACTABLES));
		} catch (final Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	
}

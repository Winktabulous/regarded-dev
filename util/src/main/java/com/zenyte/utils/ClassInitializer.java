package com.zenyte.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 29. juuli 2018 : 15:32:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class ClassInitializer {

	private static final Logger log = LoggerFactory.getLogger(ClassInitializer.class);

	/**
	 * Initializes the given class and all its static blocks.
	 * @param c the class to be initialized.
	 */
	public static void initialize(final Class<?> c) {
		try {
			Class.forName(c.getName());
		} catch (final ClassNotFoundException e) {
            log.error("", e);
		}
	}

}

package com.zenyte.utils;

import org.apache.commons.text.WordUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Text utility class.
 *
 * @author Graham Edgecombe
 */
public class TextUtils {

	public static String censor(String string) {
		string = string.replace("nigger", "******");
		string = string.replace("faggot", "******");
		return string;
	}

	public static final String RED_COLOR_TAG = "<col=ff0000>";

	/**
	 * An array of valid characters in a long username.
	 */
	public static final char[] VALID_CHARS = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',',
			'"', '[', ']', '|', '?', '/', '`'};
	
	/**
	 * Packed text translate table.
	 */
	public static final char[] XLATE_TABLE = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm',
			'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=',
			'\243', '$', '%', '"', '[', ']'};
	
	public static String removeTags(final String string) {
		StringBuilder bldr = new StringBuilder(string.length());
		boolean var15 = false;
		
		for (int i = 0; i < string.length(); ++i) {
			char var7 = string.charAt(i);
			if (var7 == '<') {
				var15 = true;
			} else if (var7 == '>') {
				var15 = false;
			} else if (!var15) {
				bldr.append(var7);
			}
		}
		return bldr.toString();
	}
	
	public static String implode(final String glue, final String[] array) {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i < array.length - 1) {
				builder.append(glue);
			}
		}
		return builder.toString();
	}

	public static String implode(final String glue, final int[] array) {
		final StringBuilder builder = new StringBuilder();
		if (array == null) {
			return "";
		}
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i < array.length - 1) {
				builder.append(glue);
			}
		}
		return builder.toString();
	}

	public static String implode(final List<?> array, final String glue) {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.size(); i++) {
			builder.append(array.get(i));
			if (i < array.size() - 1) {
				builder.append(glue);
			}
		}
		return builder.toString();
	}

	public static String formatCurrency(final int amount) {
		return NumberFormat.getNumberInstance(Locale.UK).format(amount);
	}

	/**
	 * Checks if a name is valid.
	 *
	 * @param s
	 *            The name.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public static boolean isValidName(final String s) {
		return formatNameForProtocol(s).matches("[a-z0-9_]+");
	}

	/**
	 * Converts a name to a long.
	 *
	 * @param s
	 *            The name.
	 * @return The long.
	 */
	public static long stringToLong(final String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			final char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z') {
				l += (1 + c) - 65;
			} else if (c >= 'a' && c <= 'z') {
				l += (1 + c) - 97;
			} else if (c >= '0' && c <= '9') {
				l += (27 + c) - 48;
			}
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	/**
	 * Converts a long to a name.
	 *
	 * @param l
	 *            The long.
	 * @return The name.
	 */
	public static String longToName(long l) {
		int i = 0;
		final char[] ac = new char[12];
		while (l != 0L) {
			final long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	/**
	 * Formats a name for use in the protocol.
	 *
	 * @param s
	 *            The name.
	 * @return The formatted name.
	 */
	public static String formatNameForProtocol(final String s) {
		return s.toLowerCase().replace(" ", "_");
	}

	/**
	 * Formats a name for display.
	 *
	 * @param s
	 *            The name.
	 * @return The formatted name.
	 */
	public static String formatName(final String s, final boolean lowercase, final boolean capitalize) {
		if (lowercase || capitalize) {
			String preFormatted = s;
			if (lowercase)
				preFormatted = preFormatted.toLowerCase();
			if (capitalize)
				preFormatted = capitalizeFirstCharacter(preFormatted);
			return formatName(preFormatted, false, false);
		} else
			return fixName(s.replace(" ", "_"));
	}

	public static String formatName(final String s, final boolean lowercase) {
		return formatName(s, lowercase, false);
	}

	public static String formatName(final String s) {
		return formatName(s, false, false);
	}

	/**
	 * Minimizes the character's name by lowercasing it and replacing any spaces
	 * with underscores. We use this method as a universal identifier.
	 *
	 * @param name
	 * @return
	 */
	public static String simplify(final String name) {
		if (name.length() > 0) {
			final char[] array = name.toCharArray();
			for (int i = 0; i < array.length; i++) {
				if (array[i] == ' ') {
					array[i] = '_';
				}
			}
			return new String(array).toLowerCase();
		}
		return (name);
	}

	/**
	 * Method that fixes capitalization in a name.
	 *
	 * @param s
	 *            The name.
	 * @return The formatted name.
	 */
	public static String fixName(final String s) {
		if (s.length() > 0) {
			final char[] ac = s.toCharArray();
			for (int j = 0; j < ac.length; j++) {
				if (ac[j] == '_') {
					ac[j] = ' ';
					if ((j + 1 < ac.length) && (ac[j + 1] >= 'a') && (ac[j + 1] <= 'z')) {
						ac[j + 1] = (char) ((ac[j + 1] + 65) - 97);
					}
				}
			}
			
			if ((ac[0] >= 'a') && (ac[0] <= 'z')) {
				ac[0] = (char) ((ac[0] + 65) - 97);
			}
			return new String(ac);
		} else {
			return s;
		}
	}

	/**
	 * If the first letter of the string is lowercase then we use ascii conversion
	 * to correct it.
	 *
	 * @param s
	 * @return
	 */
	public static String capitalize(final String s) {
		return WordUtils.capitalize(s);
	}

	public static String capitalizeFirstCharacter(final String s) {
		if (s.length() == 0) {
			return s;
		}
		final char[] ar = s.toCharArray();
		if (ar[0] >= 'a' && ar[0] <= 'z') {
			ar[0] = (char) (ar[0] - 32);
		}
		return new String(ar);
	}

	public static String capitalizeEnum(String string) {
		return WordUtils.capitalize(string.replace("_", " ").toLowerCase(), '\000');
	}
}

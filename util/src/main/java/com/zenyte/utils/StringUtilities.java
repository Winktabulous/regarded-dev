package com.zenyte.utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtilities {

	public static String capitalizeFirst(String string) {
		return StringUtils.capitalize(string.toLowerCase().replace("_", " "));
	}

	public static String formatEnum(Enum<?> value) {
		return capitalizeFirst(value.name());
	}

	private static final Logger log = LoggerFactory.getLogger(StringUtilities.class);
	private static final char[] CHAR_ARRAY = new char[] {8364, 0, 8218, 131, 8222, 8230, 8224, 8225, 136, 8240, 138, 8249, 140, 0, 711, 0, 0, 8216, 8217, 8220, 8221, 8226, 8211, 8212, 152, 8482, 154, 8250, 156, 0, 731, 159};

	public static String decodeString(final byte[] buffer, final int offset, final int length) {
		final char[] stringCharArray = new char[length];
		int index = 0;
		for (int i = 0; i < length; i++) {
			int characterCode = buffer[i + offset] & 255;
			if (characterCode != 0) {
				if ((characterCode >= 128) && (characterCode < 160)) {
					char UTF16char = CHAR_ARRAY[characterCode - 128];
					if (UTF16char == 0) {
						UTF16char = 63;
					}
					characterCode = UTF16char;
				}
				stringCharArray[index++] = (char) characterCode;
			}
		}
		return new String(stringCharArray, 0, index);
	}

	public static String compile(final String[] strings, final int offset, final int length, final char separator) {
		try {
			final StringBuilder builder = new StringBuilder();
			for (int i = offset; i < length; i++) {
				builder.append(strings[i]);
				builder.append(separator);
			}
			if (builder.length() > 0) {
				builder.delete(builder.length() - 1, builder.length());
			}
			return builder.toString();
		} catch (final Exception e) {
			log.error("", e);
		}
		return "";
	}

	public static final String escape(String str) {
		final int length = str.length();
		StringBuilder builder = new StringBuilder(length);
		boolean escaping = false;
		for (int var3 = 0; var3 < length; ++var3) {
			char var4 = str.charAt(var3);
			if (var4 == '<') {
				escaping = true;
			} else if (var4 == '>' && escaping) {
				escaping = false;
			} else if (!escaping) {
				builder.append(var4);
			}
		}
		return builder.toString();
	}

	public static String escapeBrackets(String var0) {
		int var1 = var0.length(); // L: 263
		int var2 = 0; // L: 264

		for (int var3 = 0; var3 < var1; ++var3) { // L: 265
			char var4 = var0.charAt(var3); // L: 266
			if (var4 == '<' || var4 == '>') {
				var2 += 3; // L: 267
			}
		}

		StringBuilder var6 = new StringBuilder(var1 + var2); // L: 269

		for (int var7 = 0; var7 < var1; ++var7) { // L: 270
			char var5 = var0.charAt(var7); // L: 271
			if (var5 == '<') {
				var6.append("<lt>"); // L: 272
			} else if (var5 == '>') { // L: 273
				var6.append("<gt>");
			} else {
				var6.append(var5); // L: 274
			}
		}

		return var6.toString(); // L: 276
	}

	public static String toRanking(int rank) {
		if (rank == 0) {
			return "N/A";
		}
		if (rank == 1) {
			return "1st";
		}
		if (rank == 2) {
			return "2nd";
		}
		if (rank == 3) {
			return "3rd";
		}
		return rank + "th";
	}

}

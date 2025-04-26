package com.zenyte.game.util;

import com.google.common.base.Preconditions;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleLists;
import mgi.Indice;
import mgi.types.config.SpotAnimationDefinition;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.IntStream;

public final class Utils {
	private static final Logger log = LoggerFactory.getLogger(Utils.class);
	public static final byte[] DIRECTION_DELTA_X = new byte[] {-1, 0, 1, -1, 1, -1, 0, 1};
	public static final byte[] DIRECTION_DELTA_Y = new byte[] {-1, -1, -1, 0, 0, 1, 1, 1};
	public static final byte[] NPC_DIRECTION_DELTA_X = new byte[] {-1, 0, 1, -1, 1, -1, 0, 1};
	public static final byte[] NPC_DIRECTION_DELTA_Y = new byte[] {1, 1, 1, 0, 0, -1, -1, -1};
	private static final String[] tensNames = {"", " ten", " twenty", " thirty", " forty", " fifty", " sixty", " seventy", " eighty", " ninety"};
	public static final String[] numNames = {"", " one", " two", " three", " four", " five", " six", " seven", " eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen"};
	private static final int[] FONT_494_CHAR_WIDTHS = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 5, 9, 7, 11, 7, 4, 3, 3, 7, 7, 4, 5, 3, 4, 7, 4, 7, 6, 5, 6, 7, 6, 7, 7, 3, 4, 6, 6, 6, 6, 10, 7, 7, 7, 7, 5, 5, 7, 7, 3, 6, 6, 5, 8, 7, 7, 7, 7, 7, 7, 5, 7, 7, 9, 7, 5, 7, 4, 4, 4, 7, 7, 4, 6, 6, 5, 6, 6, 5, 6, 6, 3, 5, 5, 3, 7, 6, 6, 6, 6, 5, 6, 4, 6, 7, 7, 7, 6, 6, 3, 3, 3, 8, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 7, 7, 8, 7, 3, 8, 5, 10, 6, 7, 7, 4, 10, 4, 5, 7, 5, 5, 5, 6, 7, 3, 4, 4, 5, 7, 9, 9, 9, 6, 7, 7, 7, 7, 7, 7, 9, 7, 6, 6, 6, 6, 3, 3, 4, 3, 7, 7, 7, 7, 7, 7, 7, 6, 7, 7, 7, 7, 7, 7, 5, 7, 6, 6, 6, 6, 6, 6, 9, 5, 6, 6, 6, 6, 3, 3, 4, 3, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 5, 6};
	private static final Object ALGORITHM_LOCK = new Object();

	public static int random(final Random random, final int i) {
		return random.nextInt(i + 1);
	}

	public static int random(final int i) {
		return random(ThreadLocalRandom.current(), i);
	}

	public static int randomNoPlus(final Random random, final int i) {
		return random.nextInt(i);
	}

	public static int randomNoPlus(final int i) {
		return randomNoPlus(ThreadLocalRandom.current(), i);
	}

	/**
	 * This will roll a chance with a specified percentage
	 * @param percentage - this percentage is up to 100
	 * @return - true if successful roll
	 */
	public static boolean roll(double percentage) {
		if(percentage >= 100) {
			return true;
		}
		if (percentage <= 0) {
			return false;
		}
		var random = getRandom().nextDouble() * 100;
		return percentage >= random;
	}

	public static String formatDuration(Duration duration) {
		return String.format("%s d %sh %sm %ss",
				duration.toDaysPart(),
				duration.toHoursPart(),
				duration.toMinutesPart(),
				duration.toSecondsPart());
	}



	public static String formatNumWDot(long value) {
		String formatedString = String.valueOf(value);
		if (value >= 1000 && value < 1000000) {
			float price = (value / 1000F);
			if (price == Math.round(price)) {
				formatedString = ((int) price) + "K";
			} else {
				formatedString = format(price) + "K";
			}
		} else if (value >= 1_000_000_000) {
			float price = (value / 1_000_000_000F);
			if (price == Math.round(price)) {
				formatedString = ((int) price) + "B";
			} else {
				formatedString = format(price) + "B";
			}
		} else if (value >= 1_000_000) {
			float price = (value / 1_000_000F);
			if (price == Math.round(price)) {
				formatedString = ((int) price) + "M";
			} else {
				formatedString = format(price) + "M";
			}
		}
		return formatedString;
	}

	public static String format(long l) {
		return NumberFormat.getInstance().format(l);
	}
	public static String format(float l) {
		return NumberFormat.getInstance().format(l);
	}


	public static final int capRange(int min, int max, int val) {
		return Math.min(max, Math.max(min, val));
	}

	public static String pluralizedFormatted(final String singular, final int value) {
		return format(value) + " " + singular + plural(value);
	}

	public static String getTimeTicks(long ticks, boolean format) {
		StringBuilder sb = new StringBuilder();
		long secs = TimeUnit.TICKS.toSeconds(ticks);

		if (secs < 60) {
			sb.append(secs);
			if (format) {
				sb.append(" second");
				if (secs > 1) {
					sb.append("s");
				}
			} else {
				sb.append("s");
			}
		} else {
			long mins = secs / 60;
			long remainderSecs = secs % 60;
			if (mins < 60) {
				sb.append(mins);
				if (format) {
					sb.append(" minute");
					if (mins > 1) {
						sb.append("s");
					}
					if (remainderSecs > 0) {
						sb.append(" ");
						sb.append(remainderSecs);
						sb.append(" second");
						if (remainderSecs > 1) {
							sb.append("s");
						}
					}
				} else {
					sb.append("m");
					if (remainderSecs > 0) {
						sb.append(" ");
						sb.append(remainderSecs);
						sb.append("s");
					}
				}
			} else {
				long hours = mins / 60;
				long remainderMins = mins % 60;
				if (hours < 24) {
					sb.append(hours);
					if (format) {
						sb.append(" hour");
						if (hours > 1) {
							sb.append("s");
						}
						if (remainderMins > 0) {
							sb.append(" ");
							sb.append(remainderMins);
							sb.append(" minute");
							if (remainderMins > 1) {
								sb.append("s");
							}
						}
						if (remainderSecs > 0) {
							sb.append(" ");
							sb.append(remainderSecs);
							sb.append(" second");
							if (remainderSecs > 1) {
								sb.append("s");
							}
						}
					} else {
						sb.append("h");
						if (remainderMins > 0) {
							sb.append(" ");
							sb.append(remainderMins);
							sb.append("m");
						}
						if (remainderSecs > 0) {
							sb.append(" ");
							sb.append(remainderSecs);
							sb.append("s");
						}
					}
				} else {
					long days = hours / 24;
					long remainderHrs = hours % 24;

					sb.append(days);
					if (format) {
						sb.append(" days");
						if (remainderHrs > 0) {
							sb.append(" ");
							sb.append(remainderHrs);
							sb.append(" hour");
							if (remainderHrs > 1) {
								sb.append("s");
							}
						}
						if (remainderMins > 0) {
							sb.append(" ");
							sb.append(remainderMins);
							sb.append(" minute");
							if (remainderMins > 1) {
								sb.append("s");
							}
						}
					} else {
						sb.append("d");
						if (remainderHrs > 0) {
							sb.append(" ");
							sb.append(remainderHrs);
							sb.append("h");
						}
						if (remainderMins > 0) {
							sb.append(" ");
							sb.append(remainderMins);
							sb.append("m");
						}
					}
				}
			}
		}

		return sb.toString();
	}

	public static final <T> T random(List<T> list) {
		final int size = list.size();
		return size < 1 ? null
				: size == 1 ? list.get(0)
				: list.get(randomNoPlus(size));
	}

	public static double randomDouble(final Random random) {
		return random.nextDouble();
	}

	public static double randomDouble() {
		return randomDouble(ThreadLocalRandom.current());
	}

	public static double randomDouble(final Random random, double bound) {
		return random.nextDouble(bound);
	}

	public static double randomDouble(double bound) {
		return randomDouble(ThreadLocalRandom.current(), bound);
	}

	public static boolean interpolateSuccess(final int minChance, final int maxChance, final int minLevel, final int maxLevel, final int currentLevel) {
		return Utils.random(255) <= ((minChance * (maxLevel - currentLevel) + maxChance * (currentLevel - minLevel)) / (maxLevel - minLevel));
	}

	/**
	 * Generates a unique user id out of {@link System#currentTimeMillis()} & {@code seed},
	 * where the seed is an integer consisting of up to 22 bits, and the timestamp will be a
	 * long consisting of 42 bits.
	 * <p>The random UUID generation method is valid until <b>Wed May 15 07:35:11 GMT 2109</b>,
	 * which is when the 42 bits will finally be all used up.</p>
	 * <p>
	 * Used {@see https://discordapp.com/developers/docs/reference#snowflakes} as inspiration.
	 *
	 * @param seed the 22-bit integer seed used for unique id generation, to prevent generating
	 *             same unique ids when the method is called in the same millisecond.
	 * @return a unique user id.
	 */
	public static long generateSnowflake(final int seed) {
		return ((System.currentTimeMillis() & 4398046511103L) << 22) | (seed & 4194303);
	}

	/**
	 * Gets the seed out of the snowflake UUID generated by {@link Utils#generateSnowflake(int)}.
	 *
	 * @param snowflake the snowflake UUID.
	 * @return the 22-bit integer seed used for the UUID generation.
	 */
	public static int getSeed(final long snowflake) {
		return (int) (snowflake & 4194303);
	}

	/**
	 * Gets the timestamp out of the snowflake UUID generated by {@link Utils#generateSnowflake(int)}.
	 *
	 * @param snowflake the snowflake UUID.
	 * @return the 42-bit long seed used in the UUID generation, which is {@link System#currentTimeMillis()}.
	 */
	public static long getTimestamp(final long snowflake) {
		return (snowflake >> 22) & 4398046511103L;
	}

	public static byte charToByteCp1252(char var0) {
		byte var1;
		if (var0 > 0 && var0 < 128 || var0 >= 160 && var0 <= 255) {
			var1 = (byte) var0;
		} else if (var0 == 8364) {
			var1 = -128;
		} else if (var0 == 8218) {
			var1 = -126;
		} else if (var0 == 402) {
			var1 = -125;
		} else if (var0 == 8222) {
			var1 = -124;
		} else if (var0 == 8230) {
			var1 = -123;
		} else if (var0 == 8224) {
			var1 = -122;
		} else if (var0 == 8225) {
			var1 = -121;
		} else if (var0 == 710) {
			var1 = -120;
		} else if (var0 == 8240) {
			var1 = -119;
		} else if (var0 == 352) {
			var1 = -118;
		} else if (var0 == 8249) {
			var1 = -117;
		} else if (var0 == 338) {
			var1 = -116;
		} else if (var0 == 381) {
			var1 = -114;
		} else if (var0 == 8216) {
			var1 = -111;
		} else if (var0 == 8217) {
			var1 = -110;
		} else if (var0 == 8220) {
			var1 = -109;
		} else if (var0 == 8221) {
			var1 = -108;
		} else if (var0 == 8226) {
			var1 = -107;
		} else if (var0 == 8211) {
			var1 = -106;
		} else if (var0 == 8212) {
			var1 = -105;
		} else if (var0 == 732) {
			var1 = -104;
		} else if (var0 == 8482) {
			var1 = -103;
		} else if (var0 == 353) {
			var1 = -102;
		} else if (var0 == 8250) {
			var1 = -101;
		} else if (var0 == 339) {
			var1 = -100;
		} else if (var0 == 382) {
			var1 = -98;
		} else if (var0 == 376) {
			var1 = -97;
		} else {
			var1 = 63;
		}
		return var1;
	}

	public static final <T> T randomNoPrevious(List<T> list, final T previous) {
		final int length = list.size();
		if (length < 1) {
			return null;
		}
		if (length == 1) {
			return list.get(0);
		}

		int index = randomNoPlus(length);
		T value = list.get(index);
		if (value == previous) {
			value = list.get(++index % length);
		}

		return list.get(randomNoPlus(length));
	}

	public static <T> T randomNoPrevious(final T[] array, final T previous) {
		int length = array.length;

		int index = randomNoPlus(length);
		T value = array[index];
		if (value == previous) {
			value = array[++index % length];
		}

		return value;
	}

	public static <T> T random(final T[] array) {
		return array[randomNoPlus(array.length)];
	}

	public static int random(final int[] array) {
		return array[randomNoPlus(array.length)];
	}

	/**
	 * Returns a string whose value is the concatenation of this
	 * string repeated {@code count} times.
	 * <p>
	 * If this string is empty or count is zero then the empty
	 * string is returned.
	 *
	 * @param count number of times to repeat
	 * @return A string composed of this string repeated
	 * {@code count} times or the empty string if this
	 * string is empty or count is zero
	 * @throws IllegalArgumentException if the {@code count} is
	 *                                  negative.
	 * @since 11
	 */
	public static String repeatString(final String string, final int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count is negative: " + count);
		}
		final byte[] value = string.getBytes();
		if (count == 1) {
			return string;
		}
		final int len = value.length;
		if (len == 0 || count == 0) {
			return "";
		}
		if (len == 1) {
			final byte[] single = new byte[count];
			Arrays.fill(single, value[0]);
			return new String(single);
		}
		if (Integer.MAX_VALUE / count < len) {
			throw new OutOfMemoryError("Repeating " + len + " bytes String " + count + " times will produce a String exceeding maximum size.");
		}
		final int limit = len * count;
		final byte[] multiple = new byte[limit];
		System.arraycopy(value, 0, multiple, 0, len);
		int copied = len;
		for (; copied < limit - copied; copied <<= 1) {
			System.arraycopy(multiple, 0, multiple, copied, copied);
		}
		System.arraycopy(multiple, 0, multiple, copied, limit - copied);
		return new String(multiple);
	}

	public static int secureRandom(final int i) {
		return ThreadLocalRandom.current().nextInt(i + 1);
	}

	public static BufferedWriter getBufferedWriter(final String name, final boolean append) throws IOException {
		return new BufferedWriter(new FileWriter(name, append));
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T, E> E fillCollection(final E collection, final T... values) {
		if (!(collection instanceof Collection)) {
			return collection;
		}
		try {
			for (final T value : values) {
				((Collection<T>) collection).add(value);
			}
		} catch (final Exception e) {
			log.error("", e);
		}
		return collection;
	}

	@SafeVarargs
	public static <T> T getRandomElement(final T... elements) {
		if (elements.length == 0) {
			throw new IllegalStateException("Array cannot be empty!");
		}
		return elements[randomNoPlus(elements.length)];
	}

	public static <T> ArrayList<T> getArrayList(final T... elements) {
		return new ArrayList<>(Arrays.asList(elements));
	}

	public static String plural(final int value) {
		return value == 1 ? "" : "s";
	}

	public static String pluralized(final String singular, final int value) {
		return value + " " + singular + plural(value);
	}

	public static String conditionallyColorized(boolean condition, String string,
												Colour colourWhenMet, Colour colourWhenNotMet) {
		return (condition ? colourWhenMet : colourWhenNotMet).wrap(string);
	}

	public static <E> E getRandomCollectionElement(final Collection<E> e) {
		final int size = e.size();
		if (size == 0) {
			return null;//throw new RuntimeException("Collection cannot be empty.");
		}
		final int random = Utils.random(e.size() - 1);
		int i = 0;
		for (final E value : e) {
			if (i == random) {
				return value;
			}
			i++;
		}
		throw new RuntimeException("Concurrent modification performed on the collection.");
	}

	public static <T> T getOrDefault(final T primary, final T defaultValue) {
		return primary == null ? defaultValue : primary;
	}

	public static <T> T computeIfAbsent(final T primary, final Function<Void, T> defaultValue) {
		return primary == null ? defaultValue.apply(null) : primary;
	}

	/**
	 * Returns a random reference of the given chance. If the random call value
	 * is equal to or less than the input percentage chance, method returns
	 * true.
	 *
	 * @param chance
	 *            the percentage chance to roll against.
	 * @return whether the random roll is true or false.
	 */
	public static boolean percentage(final int chance) {
		return random(99) < chance;
	}

	public static void printGraphicsUsingSameAnimation(final int graphics) {
		final int animation = SpotAnimationDefinition.get(graphics).getAnimationId();
		for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.GRAPHICS_DEFINITIONS); i++) {
			final SpotAnimationDefinition definitions = SpotAnimationDefinition.get(i);
			if (definitions == null) {
				continue;
			}
			if (definitions.getAnimationId() == animation) {
				System.err.println("Graphics: " + i);
			}
		}
	}

	private static final String VOWELS = "aeiouAEIOU";

	public static boolean startWithVowel(final String string) {
		return VOWELS.indexOf(string.charAt(0)) != -1;
	}

	/**
	 * Gets the hex colour code for the level. Method is taken from the client
	 * for displaying the combat level of other players.
	 *
	 * @param level
	 *            player's level.
	 * @param otherLevel
	 *            other player's level
	 * @return hex colour prefix.
	 */
	public static String getLevelColour(final int level, final int otherLevel) {
		final int difference = level - otherLevel;
		if (difference <= -1) {
			return "<col=ff0000>";
		}
		if (difference > 0) {
			return "<col=40ff00>";
		}
		if (difference == 1) {
			return "<col=ff7000>";
		}
		return "<col=ffffff>";
	}

	/**
	 * Gets the hex colour code for the level.
	 * Method is taken from the client for displaying the combat
	 * level of other players.
	 *
	 * @param level      player's level.
	 * @param otherLevel other player's level
	 * @return hex colour prefix.
	 */
	public static String getPreciseLevelColour(final int level, final int otherLevel) {
		final int difference = level - otherLevel;
		if (difference < -9) return getColourPrefix(16711680);
		 else if (difference < -6) return getColourPrefix(16723968);
		 else if (difference < -3) return getColourPrefix(16740352);
		 else if (difference < 0) return getColourPrefix(16756736);
		 else if (difference > 9) return getColourPrefix(65280);
		 else if (difference > 6) return getColourPrefix(4259584);
		 else if (difference > 3) return getColourPrefix(8453888);
		 else if (difference > 0) return getColourPrefix(12648192);
		return getColourPrefix(16776960);
	}

	/**
	 * Gets the hex colour from a decimal value.
	 *
	 * @param value decimal value of the hex colour.
	 * @return hex value of the colour along with colour tags.
	 */
	private static String getColourPrefix(final int value) {
		return "<col=" + Integer.toHexString(value) + ">";
	}

	public static byte[] encryptUsingMD5(final byte[] buffer) {
		synchronized (ALGORITHM_LOCK) {
			try {
				final MessageDigest algorithm = MessageDigest.getInstance("MD5");
				algorithm.update(buffer);
				final byte[] digest = algorithm.digest();
				algorithm.reset();
				return digest;
			} catch (final Throwable e) {
				log.error("", e);
			}
			return null;
		}
	}

	private static final Object DEFAULT = new Object();

	public static List<String> printFields(final Object object) {
		final List<String> strings = new ArrayList<String>(object.getClass().getDeclaredFields().length);
		for (final Field field : object.getClass().getDeclaredFields()) {
			if ((field.getModifiers() & 8) != 0) {
				continue;
			}
			try {
				final Object val = getValue(object, field);
				if (val == DEFAULT) {
					continue;
				}
				final String[] fieldName = field.getName().split("(?=[A-Z])");
				final StringBuilder fieldBuilder = new StringBuilder();
				fieldBuilder.append(StringFormatUtil.formatString(fieldName[0]));
				for (int i = 1; i < fieldName.length; i++) {
					fieldBuilder.append(" ").append(fieldName[i].length() == 1 ? fieldName[i].toUpperCase() : fieldName[i].toLowerCase());
				}
				strings.add(fieldBuilder + ": " + val);
				System.out.println(fieldBuilder + ": " + val);
			} catch (final Throwable e) {
				log.error("", e);
			}
		}
		return strings;
	}

	public static DoubleLists.UnmodifiableList linearInterpolate(final double x0, final double y0, final double x1, final double y1, final IntStream inputs) {
		return linearInterpolate(x0, y0, x1, y1, inputs.asDoubleStream().toArray());
	}

	/**
	 * Gets the interpolated y value of an x coordinate given two or more known points.
	 * For e.g. Blackjack success rates linearly increase starting at a 31% chance at level 1 to a 94% chance at level 99.
	 * Our X values are our levels and our Y values are our chance as percentage (x0 = 1, y0 = 31, x1 = 99, y1 = 94).
	 * We can then use this to find out percentage at other levels.
	 * @param inputs the x values we wish to find y values for.
	 * @return The interpolated values for the x coordinates given as input.
	 */
	public static DoubleLists.UnmodifiableList linearInterpolate(final double x0, final double y0, final double x1, final double y1, final double... inputs) {
		Preconditions.checkArgument(x1 - x0 != 0, "Can not interpolate value for vertical line. Ensure that both x coordinates are not equal.");
		final DoubleArrayList list = new DoubleArrayList();
		for (double input : inputs) {
			list.add(linearInterpolate(x0, y0, x1, y1, input));
		}
		return (DoubleLists.UnmodifiableList) DoubleLists.unmodifiable(list);
	}

	public static double linearInterpolate(final double x0, final double y0, final double x1, final double y1, final double input) {
		Preconditions.checkArgument(x1 - x0 != 0, "Can not interpolate value for vertical line. Ensure that both x coordinates are not equal.");
		return y0 + (input - x0) * ((y1 - y0) / (x1 - x0));
	}

	public static String formatTime(final long minutes, final long seconds) {
		return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
	}

	private static Object getValue(final Object object, final Field field) throws Throwable {
		field.setAccessible(true);
		final Class<?> type = field.getType();
		if (field.get(object) == null) {
			return DEFAULT;
		}
		if (type == boolean.class && (boolean) (field.get(object)) == false) {
			return DEFAULT;
		}
		if (type == int.class && (int) (field.get(object)) == 0) {
			return DEFAULT;
		}
		if (type == int[][].class) {
			return Arrays.toString((int[][]) field.get(object));
		} else if (type == int[].class) {
			return Arrays.toString((int[]) field.get(object));
		} else if (type == byte[].class) {
			return Arrays.toString((byte[]) field.get(object));
		} else if (type == short[].class) {
			return Arrays.toString((short[]) field.get(object));
		} else if (type == double[].class) {
			return Arrays.toString((double[]) field.get(object));
		} else if (type == float[].class) {
			return Arrays.toString((float[]) field.get(object));
		} else if (type == String[].class) {
			if (field.get(object) == null) {
				return "null";
			}
			return "[" + String.join(", ", (String[]) field.get(object)) + "]";
		} else if (type == Object[].class) {
			return Arrays.toString((Object[]) field.get(object));
		}
		return field.get(object);
	}

	public static double getRandomDouble(final double maxValue) {
		return (randomDouble() * maxValue);
	}

	public static double getRandomDouble(final int minValue, final int maxValue) {
		final int random = random(minValue, maxValue - 1);
		final double randomDouble = ThreadLocalRandom.current().nextDouble();
		return random + randomDouble;
	}

	public static double getRandomDouble(final double minValue, final double maxValue) {
		final int random = random((int) minValue, (int) maxValue - 1);
		final double randomDouble = ThreadLocalRandom.current().nextDouble();
		double value = random + randomDouble;
		if (value > maxValue) value = maxValue;
		if (value < minValue) value = minValue;
		return value;
	}

	public static int random(final Random random, final int min, final int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(random, n));
	}

	public static int random(final int min, final int max) {
		return random(ThreadLocalRandom.current(), min, max);
	}

	public static boolean randomBoolean(int chance) {
		return chance < 1 || random(chance) == 0;
	}

	public static boolean randomBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	public static Random getRandom() {
		return ThreadLocalRandom.current();
	}

	public static String checkPlural(final String input) {
		return (input.endsWith("s") && !input.equalsIgnoreCase("bass") ? "some " : getAOrAn(input) + " ") + input;
	}

	public static int getShiftedValue(final int total, final int id) {
		final int val = ((total >> id) & 1);
		if (val == 1) {
			return total & (1 << id ^ -1);
		}
		return total | 1 << id;
	}

	public static int getShiftedValue(final int total, final int id, final boolean toggled) {
		final int val = ((total >> id) & 1);
		if (val == 1 && !toggled) {
			return total & (1 << id ^ -1);
		} else if (val == 0 && toggled) {
			return total | 1 << id;
		}
		return total;
	}

	public static boolean getShiftedBoolean(final int total, final int id) {
		return ((total >> id) & 1) == 1;
	}

	public static int getDistance(final int coordX1, final int coordY1, final int coordX2, final int coordY2) {
		final int deltaX = coordX2 - coordX1;
		final int deltaY = coordY2 - coordY1;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	public static int getMapArchiveId(final int regionX, final int regionY) {
		return regionX | regionY << 7;
	}

	public static int getNPCWalkingDirection(final int dir) {
		final int dx = Utils.NPC_DIRECTION_DELTA_X[dir];
		final int dy = Utils.NPC_DIRECTION_DELTA_Y[dir];
		if (dx == 0 && dy == 1) {
			return 6;
		} else if (dx == 1 && dy == 1) {
			return 7;
		} else if (dx == -1 && dy == 0) {
			return 3;
		} else if (dx == 1 && dy == 0) {
			return 4;
		} else if (dx == -1 && dy == -1) {
			return 0;
		} else if (dx == 0 && dy == -1) {
			return 1;
		} else if (dx == 1 && dy == -1) {
			return 2;
		} else {
			return 5;
		}
	}

	public static int getPlayerRunningDirection(final int dx, final int dy) {
		if (dx == -2 && dy == -2) {
			return 0;
		}
		if (dx == -1 && dy == -2) {
			return 1;
		}
		if (dx == 0 && dy == -2) {
			return 2;
		}
		if (dx == 1 && dy == -2) {
			return 3;
		}
		if (dx == 2 && dy == -2) {
			return 4;
		}
		if (dx == -2 && dy == -1) {
			return 5;
		}
		if (dx == 2 && dy == -1) {
			return 6;
		}
		if (dx == -2 && dy == 0) {
			return 7;
		}
		if (dx == 2 && dy == 0) {
			return 8;
		}
		if (dx == -2 && dy == 1) {
			return 9;
		}
		if (dx == 2 && dy == 1) {
			return 10;
		}
		if (dx == -2 && dy == 2) {
			return 11;
		}
		if (dx == -1 && dy == 2) {
			return 12;
		}
		if (dx == 0 && dy == 2) {
			return 13;
		}
		if (dx == 1 && dy == 2) {
			return 14;
		}
		if (dx == 2 && dy == 2) {
			return 15;
		}
		return -1;
	}

	public static byte[] cryptRSA(final byte[] data, final BigInteger exponent, final BigInteger modulus) {
		return new BigInteger(data).modPow(exponent, modulus).toByteArray();
	}

	public static int getPlayerWalkingDirection(final int dx, final int dy) {
		if (dx == -1 && dy == -1) {
			return 0;
		}
		if (dx == 0 && dy == -1) {
			return 1;
		}
		if (dx == 1 && dy == -1) {
			return 2;
		}
		if (dx == -1 && dy == 0) {
			return 3;
		}
		if (dx == 1 && dy == 0) {
			return 4;
		}
		if (dx == -1 && dy == 1) {
			return 5;
		}
		if (dx == 0 && dy == 1) {
			return 6;
		}
		if (dx == 1 && dy == 1) {
			return 7;
		}
		return -1;
	}

	public static long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static boolean isOnRange(final int x1, final int y1, final int size1, final int x2, final int y2, final int size2, final int maxDistance) {
		final int distanceX = x1 - x2;
		final int distanceY = y1 - y2;
		return distanceX <= size2 + maxDistance && distanceX >= -size1 - maxDistance && distanceY <= size2 + maxDistance && distanceY >= -size1 - maxDistance;
	}

	public static boolean isDiagonal(final int x1, final int y1, final int size1, final int x2, final int y2, final int size2) {
		// if (px == x - 1 && py == y - 1 || px == x + 4 && py == y - 1
		// || px == x + 4 && py == y + 4 || px == x - 1 && py == y + 4) {
		return x1 == (x2 - size1) && y1 == (y2 - size1) || x1 == (x2 + size2) && y1 == (y2 - size1) || x1 == (x2 + size2) && y1 == (y2 + size2) || x1 == (x2 - size1) && y1 == (y2 + size2);
		// if (distanceX > size2|| distanceX < -size1 || distanceY > size2 ||
		// distanceY < -size1) {
		// return false;
		// }
		// return true;
	}

	public static boolean isOnRangeExcludingDiagonal(final int x1, final int y1, final int size1, final int x2, final int y2, final int size2, final int maxDistance) {
		final int distanceX = x1 - x2;
		final int distanceY = y1 - y2;
		return distanceX < size2 + maxDistance && distanceX > -size1 - maxDistance && distanceY < size2 + maxDistance && distanceY > -size1 - maxDistance;
	}

	public static Class<?>[] getClasses(final String packageName) throws ClassNotFoundException, IOException {
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final String path = packageName.replace('.', '/');
		final Enumeration<URL> resources = classLoader.getResources(path);
		final List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			dirs.add(new File(resources.nextElement().getFile().replaceAll("%20", " ")));
		}
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		for (int i = dirs.size() - 1; i >= 0; i--) {
			final File directory = dirs.get(i);
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[0]);
	}

	public static List<Class<?>> findClasses(final File directory, final String packageName) {
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		final File[] files = directory.listFiles();
		for (int i = files.length - 1; i >= 0; i--) {
			final File file = files[i];
			if (file.isDirectory()) {
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
				continue;
			}
			final String name = file.getName();
			if (name.endsWith(".class")) {
				try {
					classes.add(Class.forName(packageName + '.' + name.substring(0, name.length() - 6)));
				} catch (final ClassNotFoundException e) {
					log.error("", e);
				}
			}
		}
		return classes;
	}

	public static int getAngle(final int xOffset, final int yOffset) {
		return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 16383;
	}

	public static int[][] getCoordOffsetsNear(final int size) {
		final int[] xs = new int[4 + (4 * size)];
		final int[] xy = new int[xs.length];
		xs[0] = -size;
		xy[0] = 1;
		xs[1] = 1;
		xy[1] = 1;
		xs[2] = -size;
		xy[2] = -size;
		xs[3] = 1;
		xy[2] = -size;
		for (int fakeSize = size; fakeSize > 0; fakeSize--) {
			xs[(4 + ((size - fakeSize) * 4))] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4))] = 1;
			xs[(4 + ((size - fakeSize) * 4)) + 1] = -size;
			xy[(4 + ((size - fakeSize) * 4)) + 1] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 2] = 1;
			xy[(4 + ((size - fakeSize) * 4)) + 2] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 3] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4)) + 3] = -size;
		}
		return new int[][] {xs, xy};
	}


	@SafeVarargs
	public static <T> Collection<T> concatenate(final T... elements) {
		return new LinkedList<T>(Arrays.asList(elements));
	}

	public static boolean equals(final Object a, final Object b) {
		return Objects.equals(a, b);
	}

	public static double round(final double value, final int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		try {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.doubleValue();
		} catch (final NumberFormatException e) {
			return -1;
		}
	}

	private static String convertLessThanOneThousand(int number) {
		String soFar;
		if (number % 100 < 20) {
			soFar = numNames[number % 100];
			number /= 100;
		} else {
			soFar = numNames[number % 10];
			number /= 10;
			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0) {
			return soFar;
		}
		return numNames[number] + " hundred" + soFar;
	}

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

	public static String formatNumberWithCommas(final long number) {
		return DECIMAL_FORMAT.format(number);
	}

	public static String convertNumberToRelative(final long number) {
		// 0 to 999 999 999 999
		if (number == 0) {
			return "zero";
		}
		String snumber = Long.toString(number);
		// pad with "0"
		final String mask = "000000000000";
		final DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);
		// XXXnnnnnnnnn
		final int billions = Integer.parseInt(snumber.substring(0, 3));
		// nnnXXXnnnnnn
		final int millions = Integer.parseInt(snumber.substring(3, 6));
		// nnnnnnXXXnnn
		final int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
		// nnnnnnnnnXXX
		final int thousands = Integer.parseInt(snumber.substring(9, 12));
		String tradBillions;
		switch (billions) {
			case 0:
				tradBillions = "";
				break;
			default:
				tradBillions = convertLessThanOneThousand(billions) + " billion ";
		}
		String result = tradBillions;
		String tradMillions;
		switch (millions) {
		case 0: 
			tradMillions = "";
			break;
		case 1: 
			tradMillions = convertLessThanOneThousand(millions) + " million ";
			break;
		default: 
			tradMillions = convertLessThanOneThousand(millions) + " million ";
		}
		result = result + tradMillions;
		String tradHundredThousands;
		switch (hundredThousands) {
		case 0: 
			tradHundredThousands = "";
			break;
		case 1: 
			tradHundredThousands = "one thousand ";
			break;
		default: 
			tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " thousand ";
		}
		result = result + tradHundredThousands;
		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		result = result + tradThousand;
		// remove extra spaces!
		return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}

	private static final String[] sufixes = new String[] {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};

	public static String suffixOrdinal(final int i) {
		switch (i % 100) {
		case 11: 
		case 12: 
		case 13: 
			return i + "th";
		default: 
			return i + sufixes[i % 10];
		}
	}

	public static String getAOrAn(final String s) {
		if (s == null) {
			return null;
		}
		if (s.length() == 0) {
			return "";
		}
		final char fc = Character.toLowerCase(s.charAt(0));
		if (fc == 'a' || fc == 'e' || fc == 'i' || fc == 'u' || fc == 'o') {
			return "an";
		} else {
			final char sc = (s.length() > 1) ? s.charAt(1) : '\000';
			if (fc == 'x' && !(sc == 'a' || sc == 'e' || sc == 'i' || sc == 'o' || sc == 'u')) {
				return "an";
			} else {
				return "a";
			}
		}
	}

	public static int getTextWidth(final int font, final String text) {
		if (font != 494) {
			return -1;
		}
		if (text == null) {
			return 0;
		} else {
			int var2 = -1;
			int var3 = -1;
			int var4 = 0;
			for (int var5 = 0; var5 < text.length(); ++var5) {
				char var6 = text.charAt(var5);
				if (var6 == '<') {
					var2 = var5;
				} else {
					if (var6 == '>' && var2 != -1) {
						String var7 = text.substring(var2 + 1, var5);
						var2 = -1;
						if (var7.equals("lt")) {
							var6 = '<';
						} else {
							if (!var7.equals("gt")) {
								if (var7.startsWith("img=")) {
									try {
										int var8 = parseInt(var7.substring(4), 10, true);
										//var4 += CROWNS.getSprites().get(var8).getOriginalWidth();
										var3 = -1;
									} catch (Exception var10) {
										var10.printStackTrace();
									}
								}
								continue;
							}
							var6 = '>';
						}
					}
					if (var6 == 160) {
						var6 = ' ';
					}
					if (var2 == -1) {
						var4 += FONT_494_CHAR_WIDTHS[(char) (charToByteCp1252(var6) & 255)];
						/*if (this.field3765 != null && var3 != -1) {
							var4 += this.field3765[var6 + (var3 << 8)];
						}*/
						var3 = var6;
					}
				}
			}
			return var4;
		}
	}

	public static int parseInt(CharSequence var0, int var1, boolean var2) {
		if (var1 >= 2 && var1 <= 36) {
			boolean var3 = false;
			boolean var4 = false;
			int var5 = 0;
			int var6 = var0.length();
			for (int var7 = 0; var7 < var6; ++var7) {
				char var8 = var0.charAt(var7);
				if (var7 == 0) {
					if (var8 == '-') {
						var3 = true;
						continue;
					}
					if (var8 == '+') {
						continue;
					}
				}
				int var10;
				if (var8 >= '0' && var8 <= '9') {
					var10 = var8 - '0';
				} else if (var8 >= 'A' && var8 <= 'Z') {
					var10 = var8 - '7';
				} else {
					if (var8 < 'a' || var8 > 'z') {
						throw new NumberFormatException();
					}
					var10 = var8 - 'W';
				}
				if (var10 >= var1) {
					throw new NumberFormatException();
				}
				if (var3) {
					var10 = -var10;
				}
				int var9 = var10 + var5 * var1;
				if (var9 / var1 != var5) {
					throw new NumberFormatException();
				}
				var5 = var9;
				var4 = true;
			}
			if (!var4) {
				throw new NumberFormatException();
			} else {
				return var5;
			}
		} else {
			throw new IllegalArgumentException("");
		}
	}

	public static int interpolate(int outputMin, int outputMax, int inputMin, int inputMax, int input) {
		int divideBy = inputMax - inputMin;
		if (divideBy < 1) divideBy = 1;
		return outputMin + (outputMax - outputMin) * (input - inputMin) / (divideBy);
	}

	public static String ticksToTime(long ticks) {
		StringBuilder sb = new StringBuilder(8);
		int math = (int) (ticks * 600 / 1000);
		int seconds = math % 60;
		int minutes = math / 60;
		int hours = minutes / 60;
		minutes %= 60;
		if (hours > 0) {
			sb.append(hours);
			sb.append(':');
		}
		sb.append(minutes);
		sb.append(':');
		if (seconds < 10) sb.append('0');
		sb.append(seconds);
		return sb.toString();
	}

}

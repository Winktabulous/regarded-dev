package mgi.utilities;

import org.jetbrains.annotations.NotNull;
import pl.allegro.finance.tradukisto.ValueConverters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class StringFormatUtil {

    public static String formatUsername(final String username) {
        return username.toLowerCase().replace(' ', '_');
    }

    public static String formatString(String str) {
        if (str == null || str.isEmpty()) return str;
        char[] array = str.toLowerCase().toCharArray();
        // Uppercase first letter.
        array[0] = Character.toUpperCase(array[0]);
        if (array[0] == '_') {
            array[0] = ' ';
        }
        // Uppercase all letters that follow a whitespace character.
        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            } else if (array[i - 1] == '_') {
                array[i - 1] = ' ';
            }
        }
        return new String(array);
    }

    /**
     * Converts a string to a name format, therefore clearing it of all the
     * invalid characters. The only characters permitted are a-z, A-Z, 0-9, _
     * Converts a string to a name format, therefore clearing it of all the invalid characters. The only characters
     * permitted are a-z, A-Z,
     * 0-9, _
     *
     * @param string the string to convert.
     * @return a suitable string.
     */
    public static String convertToNameFormat(@NotNull final String string) {
        final StringBuilder builder = new StringBuilder();
        for (final char c : string.toCharArray()) {
            if (Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == ' ') {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String getFormattedNumber(final double amount, final char seperator) {
        final String str = new DecimalFormat("#,###,###").format(amount);
        final char[] rebuff = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            final char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                rebuff[i] = c;
            } else {
                rebuff[i] = seperator;
            }
        }
        return new String(rebuff);
    }

    public static String asWords(final int number) {
        return ValueConverters.ENGLISH_INTEGER.asWords(number);
    }

    public static String format(final int number) {
        return NumberFormat.getNumberInstance().format(number);
    }


    public static String formatNumberUS(int s)
    {
        return NumberFormat.getNumberInstance(Locale.US).format(s);
    }

    public static String format(final long number) {
        return NumberFormat.getNumberInstance().format(number);
    }

    public static String format(final float number) {
        return NumberFormat.getNumberInstance().format(number);
    }

    /**
     * Used for sanitising wikipedia values.
     */
    public static String sanitiseValue(final String input) {
        String output = input;
        output = output.replace("coins", "");
        output = output.replace("coin", "");
        output = output.replace(",", "");
        output = output.replace("~", "");
        output = output.replace("(melee)", "");
        output = output.replace("(range)", "");
        output = output.replace("(mage)", "");
        output = output.replace("(magic)", "");
        output = output.replace("(Melee)", "");
        output = output.replace("(Range)", "");
        output = output.replace("(Mage)", "");
        output = output.replace("(Magic)", "");
        output = output.replace("kg", "");
        output = output.replace("Not sold", "-1");
        output = output.replace("Yes", "true");
        output = output.replace("No", "false");
        output = output.replace("(info)", "");
        output = output.replace("reward points (Nightmare Zone)", "");
        output = output.replace("%", "");
        output = output.replace(" ", "");
        output = output.replace("(approx)", "");
        output = output.replace("•", "");
        output = output.replace("(during quest)", "");
        output = output.trim();
        return output;
    }

}

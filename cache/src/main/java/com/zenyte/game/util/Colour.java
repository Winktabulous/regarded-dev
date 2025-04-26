package com.zenyte.game.util;

import java.awt.*;

/**
 * @author Kris | 7. mai 2018 : 14:44:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum Colour {

	NONE("") {
		@Override
		public String getEnd() {
			return "";
		}
	},
	
	RED("FF0000"),
	GREEN("00FF00"),
	BLUE("0000FF"),
	DARK_BLUE("00080"),
	RS_PINK("FC02E7"),
	STRANGLE_WOOD_PINK("91001F"),
	RS_PURPLE("93069E"),
	RS_GREEN("006000"),
	TURQOISE("006d62"),
	RS_RED("600000"),
	WHITE("FFFFFF"),
	MAROON("800000"),
	BRICK("B22222"),
	YELLOW("ffff00"),
	GREY("808080"),
	LIGHT_GRAY("677077", 103 << 16 | 112 << 8 | 119),
	ORANGE("FFA500", 255 << 16 | 165 << 8),
	ORANGE_RED("FF4500", 255 << 16 | 69 << 8),
	NEX("76439E"),
	MENU_COLOR("46320a"),
	;
	
	public static final String END = "</col>";
	
	private final String hex;
	public final int decimal;

	Colour(final String hex) {
		this(hex, 0);
	}

	Colour(final String hex, int decimal) {
		this.hex = "<col=" + hex + ">";
		this.decimal = decimal;
	}

	@Override
	public String toString() {
		return hex;
	}

	public String getEnd() {
		return END;
	}

	public String wrap(final String input) {
		return hex + input + getEnd();
	}

	public String wrap(final int i) {
		return wrap(String.valueOf(i));
	}

	public static Color getColour(final int color) {
		float r = ((color >> 16) & 0xff) / 255.0f;
		float g = ((color >> 8) & 0xff) / 255.0f;
		float b = ((color) & 0xff) / 255.0f;
		float a = 0;//((color >> 24) & 0xff) / 255.0f;
		return new Color(r, g, b, a);
	}
}

package mgi.types.config;

import com.zenyte.CacheManager;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;

/**
 * @author Kris | 6. apr 2018 : 19:20.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class UnderlayDefinitions implements Definitions {
	public static UnderlayDefinitions[] definitions;

	@Override
	public void load() {
		final Cache cache = CacheManager.getCache();
		final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
		final Group underlays = configs.findGroupByID(GroupType.UNDERLAY);
		definitions = new UnderlayDefinitions[underlays.getHighestFileId()];
		for (int id = 0; id < underlays.getHighestFileId(); id++) {
			final File file = underlays.findFileByID(id);
			if (file == null) {
				continue;
			}
			final ByteBuffer buffer = file.getData();
			if (buffer == null) {
				continue;
			}
			definitions[id] = new UnderlayDefinitions(id, buffer);
		}
	}

	private final int id;
	private int rgb;
	private int hue;
	private int saturation;
	private int lightness;
	private int hueMultiplier;

	public UnderlayDefinitions(final int id, final ByteBuffer buffer) {
		this.id = id;
		decode(buffer);
		setHSL();
	}

	@Override
	public void decode(final ByteBuffer buffer) {
		while (true) {
			final int opcode = buffer.readUnsignedByte();
			if (opcode == 0) {
				return;
			}
			decode(buffer, opcode);
		}
	}

	@Override
	public void decode(final ByteBuffer buffer, final int opcode) {
		switch (opcode) {
		case 1: 
			rgb = buffer.readMedium();
			return;
		}
	}

	public static final UnderlayDefinitions get(final int id) {
		if (id < 0 || id >= definitions.length) {
			return null;
		}
		return definitions[id];
	}

	/**
	 * Sets the hue, saturation and lightness based on the RGB value obtained from the cache.
	 */
	private void setHSL() {
		final double var2 = (rgb >> 16 & 255) / 256.0;
		final double var4 = (rgb >> 8 & 255) / 256.0;
		final double var6 = (rgb & 255) / 256.0;
		double var8 = var2;
		if (var4 < var2) {
			var8 = var4;
		}
		if (var6 < var8) {
			var8 = var6;
		}
		double var10 = var2;
		if (var4 > var2) {
			var10 = var4;
		}
		if (var6 > var10) {
			var10 = var6;
		}
		double var12 = 0.0;
		double var14 = 0.0;
		final double var16 = (var10 + var8) / 2.0;
		if (var8 != var10) {
			if (var16 < 0.5) {
				var14 = (var10 - var8) / (var8 + var10);
			}
			if (var16 >= 0.5) {
				var14 = (var10 - var8) / (2.0 - var10 - var8);
			}
			if (var2 == var10) {
				var12 = (var4 - var6) / (var10 - var8);
			} else if (var10 == var4) {
				var12 = 2.0 + (var6 - var2) / (var10 - var8);
			} else if (var10 == var6) {
				var12 = (var2 - var4) / (var10 - var8) + 4.0;
			}
		}
		var12 /= 6.0;
		saturation = (int) (256.0 * var14);
		lightness = (int) (256.0 * var16);
		if (saturation < 0) {
			saturation = 0;
		} else if (saturation > 255) {
			saturation = 255;
		}
		if (lightness < 0) {
			lightness = 0;
		} else if (lightness > 255) {
			lightness = 255;
		}
		if (var16 > 0.5) {
			hueMultiplier = (int) ((1.0 - var16) * var14 * 512.0);
		} else {
			hueMultiplier = (int) (var14 * var16 * 512.0);
		}
		if (hueMultiplier < 1) {
			hueMultiplier = 1;
		}
		hue = (int) (var12 * hueMultiplier);
	}

	private static final int[] HSB_TO_RGB = new int[65536];

	static {
		double d = 1/* + (Math.random() * 0.03 - 0.015)*/;//Fuck the random
		int i_7_ = 0;
		for (int i_8_ = 0; i_8_ < 512; i_8_++) {
			float f = 360.0F * (0.0078125F + (float) (i_8_ >> 3) / 64.0F);
			float f_9_ = (float) (i_8_ & 7) / 8.0F + 0.0625F;
			for (int i_10_ = 0; i_10_ < 128; i_10_++) {
				float f_11_ = (float) i_10_ / 128.0F;
				float f_12_ = 0.0F;
				float f_13_ = 0.0F;
				float f_14_ = 0.0F;
				float f_15_ = f / 60.0F;
				int i_16_ = (int) f_15_;
				int i_17_ = i_16_ % 6;
				float f_18_ = f_15_ - (float) i_16_;
				float f_19_ = (1.0F - f_9_) * f_11_;
				float f_20_ = (1.0F - f_18_ * f_9_) * f_11_;
				float f_21_ = f_11_ * (1.0F - f_9_ * (1.0F - f_18_));
				if (i_17_ == 0) {
					f_12_ = f_11_;
					f_13_ = f_21_;
					f_14_ = f_19_;
				} else if (i_17_ == 1) {
					f_12_ = f_20_;
					f_13_ = f_11_;
					f_14_ = f_19_;
				} else if (i_17_ == 2) {
					f_12_ = f_19_;
					f_13_ = f_11_;
					f_14_ = f_21_;
				} else if (i_17_ == 3) {
					f_12_ = f_19_;
					f_13_ = f_20_;
					f_14_ = f_11_;
				} else if (i_17_ == 4) {
					f_12_ = f_21_;
					f_13_ = f_19_;
					f_14_ = f_11_;
				} else if (i_17_ == 5) {
					f_12_ = f_11_;
					f_13_ = f_19_;
					f_14_ = f_20_;
				}
				f_12_ = (float) Math.pow(f_12_, d);
				f_13_ = (float) Math.pow(f_13_, d);
				f_14_ = (float) Math.pow(f_14_, d);
				int i_22_ = (int) (f_12_ * 256.0F);
				int i_23_ = (int) (256.0F * f_13_);
				int i_24_ = (int) (f_14_ * 256.0F);
				int i_25_ = i_24_ + ( /*+ -16777216*/ (i_22_ << 16) + (i_23_ << 8));
				HSB_TO_RGB[i_7_++] = i_25_;
			}
		}
	}

	public static int hsbToRgb(int hsb) {
		return HSB_TO_RGB[hsb & 65535];
	}

	public static int hslToHsb(int hsl) {
		int hue = hsl >> 10 & 63;
		int saturation = hsl >> 3 & 112;
		int lightness = hsl & 127;
		saturation = lightness <= 64 ? lightness * saturation >> 7 : (127 - lightness) * saturation >> 7;
		int i_4_ = lightness + saturation;
		int i_5_;
		if (i_4_ != 0) {
			i_5_ = (saturation << 8) / i_4_;
		} else {
			i_5_ = saturation << 1;
		}
		int i_6_ = i_4_;
		int hsb = (hue << 10 | i_5_ >> 4 << 7 | i_6_);
		return (short) hsb;
	}

	public static int hslToRgb(int hsl) {
		return hsbToRgb(hslToHsb(hsl));
	}

	public UnderlayDefinitions() {
		this.id = 0;
	}

	public int getId() {
		return id;
	}

	public int getRgb() {
		return rgb;
	}

	public int getHue() {
		return hue;
	}

	public int getSaturation() {
		return saturation;
	}

	public int getLightness() {
		return lightness;
	}

	public int getHueMultiplier() {
		return hueMultiplier;
	}
}

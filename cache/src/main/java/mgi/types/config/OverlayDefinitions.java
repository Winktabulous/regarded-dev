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
 * @author Kris | 6. apr 2018 : 19:48.52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class OverlayDefinitions implements Definitions {
	public static OverlayDefinitions[] definitions;

	@Override
	public void load() {
		final Cache cache = CacheManager.getCache();
		final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
		final Group overlays = configs.findGroupByID(GroupType.OVERLAY);
		definitions = new OverlayDefinitions[overlays.getHighestFileId()];
		for (int id = 0; id < overlays.getHighestFileId(); id++) {
			final File file = overlays.findFileByID(id);
			if (file == null) {
				continue;
			}
			final ByteBuffer buffer = file.getData();
			if (buffer == null) {
				continue;
			}
			definitions[id] = new OverlayDefinitions(id, buffer);
		}
	}

	public OverlayDefinitions(final int id, final ByteBuffer buffer) {
		this.id = id;
		setDefaults();
		decode(buffer);
		setValues();
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
			color = buffer.readMedium();
			return;
		case 2: 
			texture = buffer.readUnsignedByte();
			return;
		case 5: 
			hidden = false;
			return;
		case 7: 
			otherColor = buffer.readMedium();
			return;
		}
	}

	private void setDefaults() {
		color = 0;
		texture = -1;
		hidden = true;
		otherColor = -1;
	}

	private final int id;
	private boolean hidden;
	private int color;
	private int texture;
	private int hue;
	private int saturation;
	private int lightness;
	private int otherColor;
	private int otherHue;
	private int otherSaturation;
	private int otherLightness;

	public static final OverlayDefinitions get(final int id) {
		if (id < 0 || id >= definitions.length) {
			return null;
		}
		return definitions[id];
	}

	private final void setValues() {
		if (otherColor != -1) {
			setHSL(otherColor);
			otherHue = hue;
			otherSaturation = saturation;
			otherLightness = lightness;
		}
		setHSL(color);
	}

	private final void setHSL(final int var1) {
		final double var2 = (var1 >> 16 & 255) / 256.0;
		final double var4 = (var1 >> 8 & 255) / 256.0;
		final double var6 = (var1 & 255) / 256.0;
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
		final double var16 = (var8 + var10) / 2.0;
		if (var8 != var10) {
			if (var16 < 0.5) {
				var14 = (var10 - var8) / (var10 + var8);
			}
			if (var16 >= 0.5) {
				var14 = (var10 - var8) / (2.0 - var10 - var8);
			}
			if (var10 == var2) {
				var12 = (var4 - var6) / (var10 - var8);
			} else if (var4 == var10) {
				var12 = 2.0 + (var6 - var2) / (var10 - var8);
			} else if (var6 == var10) {
				var12 = (var2 - var4) / (var10 - var8) + 4.0;
			}
		}
		var12 /= 6.0;
		hue = (int) (256.0 * var12);
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
	}

	public OverlayDefinitions() {
		this.id = 0;
	}

	public int getId() {
		return id;
	}

	public boolean isHidden() {
		return hidden;
	}

	public int getColor() {
		return color;
	}

	public int getTexture() {
		return texture;
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

	public int getOtherColor() {
		return otherColor;
	}

	public int getOtherHue() {
		return otherHue;
	}

	public int getOtherSaturation() {
		return otherSaturation;
	}

	public int getOtherLightness() {
		return otherLightness;
	}

	@Override
	public String toString() {
		return "OverlayDefinitions(id=" + this.getId() + ", hidden=" + this.isHidden() + ", color=" + this.getColor() + ", texture=" + this.getTexture() + ", hue=" + this.getHue() + ", saturation=" + this.getSaturation() + ", lightness=" + this.getLightness() + ", otherColor=" + this.getOtherColor() + ", otherHue=" + this.getOtherHue() + ", otherSaturation=" + this.getOtherSaturation() + ", otherLightness=" + this.getOtherLightness() + ")";
	}
}

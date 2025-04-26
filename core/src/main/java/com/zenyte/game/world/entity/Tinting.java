package com.zenyte.game.world.entity;

/**
 * @author Kris | 11/06/2022
 */
public final class Tinting extends PrivateMask {
	private final int hue;
	private final int saturation;
	private final int luminance;
	private final int opacity;
	private final int delay;
	private final int duration;

	public Tinting(int hue, int saturation, int luminance, int opacity, int delay, int duration) {
		this.hue = hue;
		this.saturation = saturation;
		this.luminance = luminance;
		this.opacity = opacity;
		this.delay = delay;
		this.duration = duration;
	}

	public int getHue() {
		return hue;
	}

	public int getSaturation() {
		return saturation;
	}

	public int getLuminance() {
		return luminance;
	}

	public int getOpacity() {
		return opacity;
	}

	public int getDelay() {
		return delay;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tinting tinting = (Tinting) o;

		if (hue != tinting.hue) return false;
		if (saturation != tinting.saturation) return false;
		if (luminance != tinting.luminance) return false;
		if (opacity != tinting.opacity) return false;
		if (delay != tinting.delay) return false;
		return duration == tinting.duration;
	}

	@Override
	public int hashCode() {
		int result = hue;
		result = 31 * result + saturation;
		result = 31 * result + luminance;
		result = 31 * result + opacity;
		result = 31 * result + delay;
		result = 31 * result + duration;
		return result;
	}

	@Override
	public String toString() {
		return "Tinting{" +
				"hue=" + hue +
				", saturation=" + saturation +
				", luminance=" + luminance +
				", opacity=" + opacity +
				", delay=" + delay +
				", duration=" + duration +
				'}';
	}
}

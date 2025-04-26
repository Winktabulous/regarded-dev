package com.zenyte.game.content.advent;

import com.zenyte.game.item.Item;
import mgi.types.config.StructDefinitions;

import java.util.function.Function;

public class AdventDay {

	private final int day;
	private final int structId;
	private final int countToComplete;
	private final Item reward;
	private final Function<Integer, Integer> transmitTransform;

	AdventDay(int day, int structId) {
		this(day, structId, null, null);
	}

	AdventDay(int day, int structId, String name) {
		this(day, structId, name, null);
	}

	AdventDay(int day, int structId, String name, Function<Integer, Integer> transmitTransform) {
		this.day = day;
		this.structId = structId;
		StructDefinitions struct = StructDefinitions.get(structId);
		this.countToComplete = struct.getParamAsInt(5026);
		this.reward = new Item(struct.getParamAsInt(5027), struct.getParamAsInt(5028));
		this.transmitTransform = transmitTransform;
	}

	public int getStructId() {
		return structId;
	}

	public int getValue(int value) {
		if (transmitTransform == null) {
			return value;
		}

		return transmitTransform.apply(value);
	}

	public int getCountToComplete() {
		return countToComplete;
	}

	public int getDay() {
		return day;
	}

	public Item getReward() {
		return reward;
	}

	public String getAttribute(AdventCalendar calendar) {
		return "advent" + calendar.getYear() + "_day" + day;
	}

}

package com.zenyte.game.content.advent;

import java.util.HashMap;
import java.util.Map;

public class AdventCalendar {

	private final int year;
	private final Map<Integer, AdventDay> days = new HashMap<>();

	public AdventCalendar(int year, AdventDay[] days) {
		this.year = year;
		for (AdventDay day : days) {
			this.days.put(day.getDay(), day);
		}
	}

	public AdventDay getDay(int id) {
		return days.get(id);
	}

	public int getYear() {
		return year;
	}

}

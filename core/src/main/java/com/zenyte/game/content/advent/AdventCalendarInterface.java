package com.zenyte.game.content.advent;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

import java.util.Calendar;

public class AdventCalendarInterface extends Interface {

	@Override
	protected void attach() {
		int day = 1;
		for (int component = 6; component <= 30; component++) {
			put(component, "Day" + (day++));
		}
	}

	@Override
	protected void build() {
		for (int day = 1; day <= 25; day++) {
			int finalDay = day;
			bind("Day" + day, player -> openDay(player, finalDay));
		}
	}

	private static void openDay(Player player, int day) {
		if (!AdventCalendarManager.adventEnabled()) {
			player.sendMessage("Advent calendar is over for this year!");
			return;
		}

		AdventDay adventDay = AdventCalendarManager.getDay(day);
		if (adventDay == null) {
			player.sendDeveloperMessage("Day " + day + " is null.");
			return;
		}

		int currentValue = adventDay.getValue(AdventCalendarManager.getChallengeProgress(player, day));
		String raffleWinner = AdventCalendarRaffle.getRaffleWinner(day);
		player.getPacketDispatcher().sendClientScript(10669, adventDay.getStructId(), currentValue, raffleWinner);
	}

	@Override
	public void open(Player player) {
		if (!AdventCalendarManager.adventEnabled()) {
			return;
		}

		player.getVarManager().sendVar(262, 0);//Advent enum index.

		int tasksLocked = 0;
		int completedValue = 0;
		for (int i = 0; i < 25; i++) {
			int day = i + 1;
			if (AdventCalendarManager.dayLockedFunc.apply(day)) {
				tasksLocked |= 1 << i;
			}

			AdventDay adventDay = AdventCalendarManager.getDay(day);
			int progress = AdventCalendarManager.getChallengeProgress(player, day);
			if (adventDay.getValue(progress) >= adventDay.getCountToComplete()) {
				completedValue |= 1 << i;
			}
		}

		player.getVarManager().sendVar(261, completedValue);
		player.getVarManager().sendVar(263, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		player.getVarManager().sendVar(264, tasksLocked);

		super.open(player);
	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.ADVENT_CALENDAR;
	}

}

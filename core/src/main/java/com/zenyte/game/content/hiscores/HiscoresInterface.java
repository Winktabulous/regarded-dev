package com.zenyte.game.content.hiscores;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.plugins.dialogue.StringDialogue;

import java.util.List;
import java.util.Map;

public class HiscoresInterface extends Interface {

	private static final int BILLION = 1_000_000_000;
	private static final ListSelectDialog selectDialog = new ListSelectDialog();
	private static final String LAST_VIEWED = "hiscores_last_viewed";
	private static final String SELECTED_MODE_ATTRIBUTE = "hiscores_selected_gamemode";
	private static final String SELECTED_RATE_ATTRIBUTE = "hiscores_selected_rate";
	private static final String SELECTED_PAGE_ATTRIBUTE = "hiscores_selected_page";

	@Override
	protected void attach() {
		put(11, "Entries");
		put(14, "List");
		put(20, "Dropdown");
		put(21, "Ironman");
		put(22, "Ultimate Ironman");
		put(23, "Hardcore Ironman");
		put(24, "Group Ironman");
	}

	@Override
	protected void build() {
		bind("Entries", (player, slotId, itemId, option) -> {
			int page = player.getNumericTemporaryAttributeOrDefault(SELECTED_PAGE_ATTRIBUTE, 0).intValue();
			switch (slotId) {
				case 0:
					player.getTemporaryAttributes().put(SELECTED_PAGE_ATTRIBUTE, Math.max(0, page - 1));
					refreshLastViewed(player);
					break;
				case 1:
					player.getTemporaryAttributes().put(SELECTED_PAGE_ATTRIBUTE, page + 1);
					refreshLastViewed(player);
					break;
				case 2:
					refreshLastViewed(player);
					break;
			}
			refreshDialog(player);
		});
		bind("List", (player, slotId, itemId, option) -> refreshDialog(player));
		bind("Dropdown", (player, slotId, itemId, option) -> selectRate(player, slotId));
		bind("Ironman", (player, slotId, itemId, option) -> toggleMode(player, GameMode.STANDARD_IRON_MAN.ordinal()));
		bind("Hardcore Ironman", (player, slotId, itemId, option) -> toggleMode(player, GameMode.HARDCORE_IRON_MAN.ordinal()));
		bind("Ultimate Ironman", (player, slotId, itemId, option) -> toggleMode(player, GameMode.ULTIMATE_IRON_MAN.ordinal()));
		bind("Group Ironman", (player, slotId, itemId, option) -> toggleMode(player, GameMode.GROUP_IRON_MAN.ordinal()));
	}

	private static void refreshDialog(Player player) {
		player.awaitInputString(selectDialog);
		Analytics.flagInteraction(player, Analytics.InteractionType.HISCORES);
	}

	@Override
	public void open(Player player) {
		player.getVarManager().sendVar(261, 0);
		player.getVarManager().sendVar(262, player.getNumericTemporaryAttributeOrDefault(SELECTED_MODE_ATTRIBUTE, 0).intValue());
		player.getVarManager().sendVar(263, player.getNumericTemporaryAttributeOrDefault(SELECTED_RATE_ATTRIBUTE, 0).intValue());

		super.open(player);

		player.getPacketDispatcher().sendComponentSettings(getId(), getComponent("Entries"), 0, 200, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(getId(), getComponent("List"), 0, 100, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(getId(), getComponent("Dropdown"), 0, 20, AccessMask.CLICK_OP1);
		refreshDialog(player);
	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.HISCORES;
	}

	private static void toggleMode(Player player, int gameMode) {
		int current = player.getNumericTemporaryAttribute(SELECTED_MODE_ATTRIBUTE).intValue();
		int value = 0;
		if (current != gameMode) {
			value = gameMode;
		}

		GameMode newMode = GameMode.get(value);
		if (player.getNumericTemporaryAttributeOrDefault(SELECTED_RATE_ATTRIBUTE, 0).intValue() >= newMode.getHiscoresRates().length) {
			int index = 0;
			player.getVarManager().sendVar(263, index);
			player.getTemporaryAttributes().put(SELECTED_RATE_ATTRIBUTE, index);
		}

		player.getTemporaryAttributes().put(SELECTED_MODE_ATTRIBUTE, value);
		player.getTemporaryAttributes().put(SELECTED_PAGE_ATTRIBUTE, 0);
		player.getVarManager().sendVar(262, value);
		refreshDialog(player);

		refreshLastViewed(player);
	}

	private static void selectRate(Player player, int slotId) {
		int index = slotId - 1;//Offset by 1 because of the hover.
		player.getVarManager().sendVar(263, index);
		player.getTemporaryAttributes().put(SELECTED_RATE_ATTRIBUTE, index);
		player.getTemporaryAttributes().put(SELECTED_PAGE_ATTRIBUTE, 0);
		refreshDialog(player);

		refreshLastViewed(player);
	}

	private static void refreshLastViewed(Player player) {
		int lastViewed = player.getNumericTemporaryAttributeOrDefault(LAST_VIEWED, -1).intValue();
		if (lastViewed != -1) {
			viewEntry(player, lastViewed);
		}
	}

	private static void viewPlayer(Player player, String name) {
		GameMode gameMode = GameMode.get(player.getNumericTemporaryAttributeOrDefault(SELECTED_MODE_ATTRIBUTE, 0).intValue());
		int rate = gameMode.getHiscoresRates()[player.getNumericTemporaryAttributeOrDefault(SELECTED_RATE_ATTRIBUTE, 0).intValue()];
		HiscoresPlayerKey key = new HiscoresPlayerKey(name.toLowerCase(), gameMode, rate);
		Map<HiscoresCategory, Map<HiscoresCategoryEntry, HiscoresScore>> values = HiscoresManager.nameToHiscores.get(key);
		if (values == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (HiscoresCategory category : HiscoresCategory.values) {
			Map<HiscoresCategoryEntry, HiscoresScore> entries = values.get(category);
			if (entries != null && !entries.isEmpty()) {
				sb.append(category.getStructId());
				sb.append("|");
				int length = category.getEntries().length;
				for (int i = 0; i < length; i++) {
					HiscoresCategoryEntry entry = category.getEntries()[i];
					HiscoresScore score = entries.get(entry);
					if (score != null) {
						sb.append(i);
						sb.append("|");
						for (long value : score.getValues()) {
							long bitPack = convertValue(value);
							sb.append(unpackFirst(bitPack));
							sb.append("|");
							sb.append(unpackSecond(bitPack));
							sb.append("|");
						}
					}
				}
				sb.append(Integer.MAX_VALUE);
				sb.append("|");
			}
		}

		player.getPacketDispatcher().sendClientScript(10655, name, sb.toString());
	}

	private static void viewEntry(Player player, int data) {
		int struct = data & 0xffff;
		int pos = data >> 16;
		player.getVarManager().sendVar(261, data);

		HiscoresCategory category = HiscoresCategory.idToCategory.get(struct);
		if (category == null) {
			player.getPacketDispatcher().sendClientScript(10650);
			return;
		}

		GameMode gameMode = GameMode.get(player.getNumericTemporaryAttribute(SELECTED_MODE_ATTRIBUTE).intValue());
		Map<Integer, Map<HiscoresCategoryEntry, List<HiscoresScore>>> modeToRate = HiscoresManager.categoryToHiscores.get(gameMode);
		if (modeToRate == null) {
			player.getPacketDispatcher().sendClientScript(10650);
			return;
		}

		int rate = gameMode.getHiscoresRates()[player.getNumericTemporaryAttributeOrDefault(SELECTED_RATE_ATTRIBUTE, 0).intValue()];
		Map<HiscoresCategoryEntry, List<HiscoresScore>> hiscores = modeToRate.get(rate);
		if (hiscores == null) {
			player.getPacketDispatcher().sendClientScript(10650);
			return;
		}

		List<HiscoresScore> dataList = hiscores.get(category.getEntries()[pos]);
		if (dataList == null) {
			player.getPacketDispatcher().sendClientScript(10650);
			return;
		}

		int dataListSize = dataList.size();
		if (dataListSize == 0) {
			player.getPacketDispatcher().sendClientScript(10650);
			return;
		}

		int pageSize = 25;
		int page = player.getNumericTemporaryAttributeOrDefault(SELECTED_PAGE_ATTRIBUTE, 0).intValue();
		int start = page * pageSize;
		int nextPageSize = (page + 1) * pageSize;
		int length = Math.min(nextPageSize, dataListSize);
		//System.out.println(page+":"+start+":"+nextPageSize+":"+length);
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < length; i++) {
			HiscoresScore value = dataList.get(i);
			sb.append(value.getName());
			sb.append("|");
			for (long v : value.getValues()) {
				long bitPack = convertValue(v);
				sb.append(unpackFirst(bitPack));
				sb.append("|");
				sb.append(unpackSecond(bitPack));
				sb.append("|");
			}
		}

		player.getPacketDispatcher().sendClientScript(10637, struct, sb.toString(), page, nextPageSize < dataListSize ? 1 : 0);
	}

	private static long convertValue(long value) {
		int billions = (int) (value / BILLION);
		int remainder = (int) (value % BILLION);
		return (long) billions << 32 | remainder;
	}

	private static int unpackFirst(long value) {
		return (int) (value >>> 32);
	}

	private static int unpackSecond(long value) {
		return (int) (value & 0xFFFF_FFFFL);
	}

	private static class ListSelectDialog implements StringDialogue {

		@Override
		public void run(String amount) {

		}

		@Override
		public void execute(Player player, String data) {
			try {
				int number = Integer.parseInt(data);
				player.getTemporaryAttributes().put(LAST_VIEWED, number);
				player.getTemporaryAttributes().put(SELECTED_PAGE_ATTRIBUTE, 0);
				viewEntry(player, number);
			} catch (Exception e) {
				viewPlayer(player, data);
			}
		}
	}

}

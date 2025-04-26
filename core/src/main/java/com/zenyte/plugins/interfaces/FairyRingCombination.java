package com.zenyte.plugins.interfaces;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.FairyRing;
import com.zenyte.game.world.object.WorldObject;

import java.util.HashMap;
import java.util.Map;

public final class FairyRingCombination implements UserInterface {
	
	private static final Map<String, Integer> CHARS = new HashMap<String, Integer>();
	
	private static final String[][] CHARACTERS = {
			{ "A", "D", "C", "B" },
			{ "I", "L", "K", "J" },
			{ "P", "S", "R", "Q" }
	};
	
	static {
		for (final String[] row : CHARACTERS) {
			for (int index=0; index < 4; index++) {
				CHARS.put(row[index], index);
			}
		}
	}
	
	private static void ingestion(final Player player, final int component, final int[] code) {
		final int index = (component - 19) / 2;
		code[index] = (((component & 1) == 0) ? code[index]-1 : code[index]+1) % 4;
		code[index] = code[index] < 0 ? code[index]+4 : code[index];
		player.getTemporaryAttributes().put("fairyRingCode", stringify(code));
		name(player, stringify(code));
	}
	
	private static int[] digest(final String code) {
		final String[] raw = code.split("");
		return new int[] { CHARS.get(raw[0]), CHARS.get(raw[1]), CHARS.get(raw[2]) };
	}
	
	private static String stringify(final int[] values) {
		return CHARACTERS[0][values[0]] + CHARACTERS[1][values[1]] + CHARACTERS[2][values[2]];
	}
	
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		final Object fRingCode = player.getTemporaryAttributes().get("fairyRingCode");
		switch(componentId) {
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
		case 24:
			if (!(fRingCode instanceof String)) {
				return;
			}
			ingestion(player, componentId, digest(fRingCode.toString()));
			break;
		case 26:
			final Object fRing = player.getTemporaryAttributes().get("fairyRingObject");
			if (!(fRing instanceof WorldObject)) {
				return;
			}
			final WorldObject object = (WorldObject) fRing;
			final FairyRing ring = FairyRing.codes.get(fRingCode.toString());
			FairyRing.handle(player, object, ring);
			break;
		}
	}
	
	public static void open(final Player player, final WorldObject object) {
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 398);
		player.getTemporaryAttributes().put("fairyRingObject", object);
		final Object code = player.getTemporaryAttributes().get("fairyRingCode");
		player.getTemporaryAttributes().put("fairyRingCode", code == null ? "AIP" : code);
	}
	
	private static void name(final Player player, final String code) {
		final FairyRing ring = FairyRing.getRing(code);
		player.getPacketDispatcher().sendComponentText(398, 26, ring == null ? "Teleport to unknown location" : ring.getName());
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 398);
	}
	
	public static void refresh(final Player player, final FairyRing ring) {
		if (ring == null) {
			return;
		}
		final int[] code = digest(ring.getCode());
		if (code == null) {
			return;
		}
		player.getTemporaryAttributes().put("fairyRingCode", ring.getCode());
		player.getVarManager().sendBit(3985, code[0]);
		player.getVarManager().sendBit(3986, code[1]);
		player.getVarManager().sendBit(3987, code[2]);
		player.getPacketDispatcher().sendComponentText(398, 26, ring.getName());
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 398);
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 398 };
	}

}

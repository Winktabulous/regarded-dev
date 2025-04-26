package com.zenyte.game.world.entity.player;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.RSColour;

import static com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables.SHOW_BOSS_HEALTH_OVERLAY_VARBIT_ID;

public class HpHud {

	private static final int HP_HUD = 303;
	private static final Object[] OPEN_CS2_ARGS = {
			HP_HUD << 16 | 0,
			HP_HUD << 16 | 2,
			HP_HUD << 16 | 4,
			HP_HUD << 16 | 5,
			HP_HUD << 16 | 8,
			HP_HUD << 16 | 10,
			HP_HUD << 16 | 20,
			HP_HUD << 16 | 13,
			HP_HUD << 16 | 14,
			HP_HUD << 16 | 15,
			HP_HUD << 16 | 9,
			HP_HUD << 16 | 6,
			HP_HUD << 16 | 7,
			HP_HUD << 16 | 11,
			HP_HUD << 16 | 18,
			HP_HUD << 16 | 19,
			HP_HUD << 16 | 16,
			HP_HUD << 16 | 17,
			HP_HUD << 16 | 3
	};
	private static final Object[] CLOSE_CS2_ARGS = {
			HP_HUD << 16 | 6,
			HP_HUD << 16 | 7,
			HP_HUD << 16 | 9,
			HP_HUD << 16 | 11,
			HP_HUD << 16 | 13,
			HP_HUD << 16 | 14,
			HP_HUD << 16 | 15,
			HP_HUD << 16 | 20,
			HP_HUD << 16 | 18,
			HP_HUD << 16 | 19,
			HP_HUD << 16 | 16,
			HP_HUD << 16 | 17,
			0
	};
	private static final RSColour[] ORIGINAL_COLORS = { new RSColour(25, 0, 0), new RSColour(0, 15, 0), new RSColour(0, 25, 0) };

	private final transient Player player;

	public HpHud(Player player) {
		this.player = player;
	}

	public void open(int npcId, int maxValue) {
		open(true, npcId, maxValue);
	}

	public void open(boolean centered, int npcId, int maxValue) {
		if (player.getVarManager().getBitValue(SHOW_BOSS_HEALTH_OVERLAY_VARBIT_ID) == 1) {
			return;
		}
		player.getVarManager().sendVarInstant(1683, npcId);
		player.getVarManager().sendBitInstant(12401, centered);
		updateValue(maxValue);
		updateMaxValue(maxValue);
		player.getInterfaceHandler().sendInterface(GameInterface.HP_HUD);
		player.getPacketDispatcher().sendClientScript(2376, OPEN_CS2_ARGS);
	}

	public void close() {
		if (player.getVarManager().getBitValue(SHOW_BOSS_HEALTH_OVERLAY_VARBIT_ID) == 1 || !player.getInterfaceHandler().containsInterface(InterfacePosition.HP_HUD_POS)) {
			return;
		}

		player.getPacketDispatcher().sendClientScript(2889, CLOSE_CS2_ARGS);
		WorldTasksManager.schedule(() -> player.getInterfaceHandler().closeInterface(GameInterface.HP_HUD), 2);
	}

	public void sendColorChange(RSColour rsColour, RSColour rsColour1, RSColour rsColour2) {
		player.getPacketDispatcher().sendComponentSpriteColour(HP_HUD, 13, rsColour);
		player.getPacketDispatcher().sendComponentSpriteColour(HP_HUD, 14, rsColour1);
		player.getPacketDispatcher().sendComponentSpriteColour(HP_HUD, 15, rsColour2);
	}

	public void resetColors() {
		player.getPacketDispatcher().sendComponentSpriteColour(HP_HUD, 13, ORIGINAL_COLORS[0]);
		player.getPacketDispatcher().sendComponentSpriteColour(HP_HUD, 14, ORIGINAL_COLORS[1]);
		player.getPacketDispatcher().sendComponentSpriteColour(HP_HUD, 15, ORIGINAL_COLORS[2]);
	}

	public void sendMaxHitPoints(int hitPoints) {
		player.getVarManager().sendBitInstant(6100, hitPoints);
	}

	public boolean isOpen() {
		return player.getInterfaceHandler().containsInterface(InterfacePosition.HP_HUD_POS);
	}

	public void updateValue(int newValue) {
		player.getVarManager().sendBitInstant(6099, newValue);
	}

	public void updateMaxValue(int newValue) {
		player.getVarManager().sendBitInstant(6100, newValue);
	}

}

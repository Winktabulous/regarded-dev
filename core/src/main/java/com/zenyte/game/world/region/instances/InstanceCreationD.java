package com.zenyte.game.world.region.instances;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 23. jaan 2018 : 20:36.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class InstanceCreationD {
	
	private static final String MAIN = "Start an instance|Join a friend's instance|View available instances|Cancel";
	private static final String START = "Set protection - FFA|Set respawn speed|Start the instance|Cancel";
	private static final String PROTECTION = "Free for all|Password protection|Solo only|Cancel";
	private static final String SPEED = "Very slow|Slow|Standard|Fast|Very fast|Cancel";
	private static final int MAIN_PAGE = 0, INSTANCE_START_PAGE = 1, AVAILABLE_INSTANCES_PAGE = 2, PROTECTION_SELECTION_PAGE = 3, RESPAWN_SPEED_SELECTION_PAGE = 4;
	
	public InstanceCreationD(final Player player, final Class<?> instanceClass, final boolean customizable) {
		this.player = player;
		this.instanceClass = instanceClass;
		//this.customizable = customizable;
	}
	
	private final Player player;
	private final Class<?> instanceClass;
	//private final boolean customizable;
	
	private int page;
	
	public void open(final int page) {
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 187);
		player.getPacketDispatcher().sendClientScript(917, 0, 200);
		player.getPacketDispatcher().sendComponentSettings(187, 3, 0, 127, AccessMask.CONTINUE);
		this.page = page;
		switch (page) {
		case MAIN_PAGE:
			player.getPacketDispatcher().sendClientScript(217, "HesporiInstance Manager - " + instanceClass.toString(), MAIN, 1);
			return;
		case INSTANCE_START_PAGE:
			player.getPacketDispatcher().sendClientScript(217, player.getPlayerInformation().getDisplayname() + "'s instance - " + instanceClass.toString(), START, 1);
			return;
		case AVAILABLE_INSTANCES_PAGE:
			player.getPacketDispatcher().sendClientScript(217, "Available instances", START, 1);
			return;
		case PROTECTION_SELECTION_PAGE:
			player.getPacketDispatcher().sendClientScript(217, "Select preferred protection", PROTECTION, 1);
			return;
		case RESPAWN_SPEED_SELECTION_PAGE:
			player.getPacketDispatcher().sendClientScript(217, "Select preferred respawn speed", SPEED, 1);
			return;
		}
	}
	
	public void handle(final int slot) {
		switch(page) {
		case MAIN_PAGE:
			switch(slot) {		
			case 0:
				open(INSTANCE_START_PAGE);
				return;
			case 1:
				player.sendInputString("Whose instance would you like to join?", string -> joinInstance(string));
				return;
			}
			return;
		}
	}
	
	
	public void joinInstance(final String username) {
		
	}
	
}

package com.zenyte.game.content.skills.construction.dialogue;

import com.zenyte.game.content.skills.construction.FurnitureData;
import com.zenyte.game.content.skills.construction.TeleportPortal;
import com.zenyte.game.content.skills.construction.constants.Furniture;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.utilities.StringFormatUtil;

import java.util.List;

public class PortalScryingD extends Dialogue {

	public PortalScryingD(Player player, final List<String> options, final List<FurnitureData> furniture) {
		super(player);
		this.options = options;
		this.furniture = furniture;
	}
	
	private final List<String> options;
	private final List<FurnitureData> furniture;

	@Override
	public void buildDialogue() {
		options("Scry which portal?", options.toArray(new String[options.size()]))
		.onOptionOne(() -> {
			finish();
			if (options.get(0).contains("No portal frame"))
				return;
			sendInterface(0);
		})
		.onOptionTwo(() -> {
			finish();
			if (options.get(1).contains("No portal frame"))
				return;
			sendInterface(1);
		})
		.onOptionThree(() -> {
			finish();
			if (options.get(2).contains("No portal frame"))
				return;
			sendInterface(2);
		})
		.onOptionFour(() -> finish());
	}
	
	private void sendInterface(final int index) {
		final FurnitureData data = furniture.get(index);
		if (data == null)
			return;
		final Furniture furniture = data.getFurniture();
		if (furniture == null)
			return;
		final TeleportPortal portal = TeleportPortal.MAP.get(furniture.getObjectId());
		if (portal == null)
			return;
		final Location tile = new Location(player.getLocation());
		player.getActionManager().setAction(new Action() {

			@Override
			public boolean start() {
				return true;
			}

			@Override
			public boolean process() {
				return true;
			}

			@Override
			public int processWithDelay() {
				return 0;
			}
			
			@Override
			public void stop() {
				player.setLocation(tile);
				player.resetFreeze();
				player.getAppearance().setInvisible(false);
				player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 71);
				player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED);
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						if (player.isLoadingRegion())
							return;
						stop();
						player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
						player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
						player.getTemporaryAttributes().remove("Scrying");
					}
					
				}, 0, 0);
			}
			
		});
		player.getTemporaryAttributes().put("Scrying", true);
		player.getAppearance().setInvisible(true);
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 404);
		player.freeze(Integer.MAX_VALUE);
		player.getDialogueManager().start(new ScryingDialogue(player, furniture));
		player.setLocation(portal.getTeleport().getDestination());
	}
	
	private static final class ScryingDialogue extends Dialogue {

		public ScryingDialogue(Player player, final Furniture furniture) {
			super(player);
			this.furniture = furniture;
		}
		
		private final Furniture furniture;

		@Override
		public void buildDialogue() {
			plain("You view " + StringFormatUtil.formatString(furniture.toString().toLowerCase().replace(" portal", "...").replace("_", " ")))
			.executeAction(() -> {
				finish();
				player.getActionManager().forceStop();
			});
		}
		
	}
	
}
package com.zenyte.game.content.chambersofxeric.dialogue;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidMap;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.WorldUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;

import java.util.Optional;

/**
 * @author Kris | 11. jaan 2018 : 4:27.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RaidFloorOverviewD extends OptionsMenuD {
	private RaidFloorOverviewD(final Player player, final String title, final String[] options) {
		super(player, title, options);
	}

	@Override
	public void handleClick(final int slotId) {
		final Optional<Raid> optionalRaid = player.getRaid();
		if (!optionalRaid.isPresent()) {
			player.sendMessage("You need to be in a raid to view the floors.");
			return;
		}
		final Raid raid = optionalRaid.get();
		final RaidMap map = raid.getMap();
		if (slotId < map.getRaidChunks().size()) {
			final RaidArea chunk = map.getRaidChunks().get(slotId);
			final Location tile = new Location((chunk.getChunkX() * 8) + 16, (chunk.getChunkY() * 8) + 16, chunk.getToPlane());
			final Optional<Location> square = WorldUtil.findEmptySquare(tile, 15, 1, Optional.empty());
			player.setLocation(square.orElse(tile));
		} else {
			player.setLocation(map.getBoss().getEntrance());
		}
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
	}

	public static final void open(final Player player) {
		final Optional<Raid> optionalRaid = player.getRaid();
		if (!optionalRaid.isPresent()) {
			player.sendMessage("You need to be in a raid to view the floors.");
			return;
		}
		final Raid raid = optionalRaid.get();
		final RaidMap map = raid.getMap();
		final String[] options = new String[map.getRaidChunks().size() + 1];
		for (int i = 0; i < map.getRaidChunks().size(); i++) {
			final RaidArea chunk = map.getRaidChunks().get(i);
			final RaidRoom data = getRoomData(chunk.getStaticChunkY(), chunk.getFromPlane());
			assert data != null;
			final String string = StringFormatUtil.formatString(data.toString().toLowerCase().replace("_", " "));
			final String prefix = chunk.getToPlane() == 3 ? "Top floor" : chunk.getToPlane() == 2 ? "Middle floor" : "Bottom floor";
			options[i] = prefix + " - " + string + (string.endsWith("room") ? "" : " room");
		}
		options[map.getRaidChunks().size()] = "The Great Olm";
		player.getDialogueManager().start(new RaidFloorOverviewD(player, "Raid floors", options));
	}

	private static final RaidRoom getRoomData(final int ry, final int plane) {
		return CollectionUtils.findMatching(RaidRoom.values, room -> room.getStaticChunkY() == ry && room.getHeight() == plane);
	}

	@Override
	public boolean cancelOption() {
		return true;
	}
}

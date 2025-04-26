package com.zenyte.game.content.tombsofamascut.lobby;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.RegionArea;

/**
 * @author Savions.
 */
public class TOALobbyArea extends PolygonRegionArea {

	@Override protected RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(3340, 9100, 3377, 9132)};
	}

	@Override public void enter(Player player) {
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, GameInterface.TOA_PARTY.getId());
		player.getTOAManager().sendEmptyPartyList();
	}

	@Override public void leave(Player player, boolean logout) {
		final RegionArea nextArea = GlobalAreaManager.getArea(player.getLocation());
		if (!(nextArea instanceof TOARaidArea)) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
			TOALobbyParty appliedParty = TOALobbyParty.getAppliedParty(player);

			if (appliedParty != null) {
				final Player leader = appliedParty.getLeader();
				if (leader != null && appliedParty.withdraw(player)) {
					TOAPartyManagementInterface.updatePartyApplicants(leader);
				}
			}
			TOALobbyParty current = TOALobbyParty.getCurrentParty(player);


			if (current != null) {
				final Player leader = current.getLeader();
				if (leader != null && current.leave(player, false)) {
					player.sendMessage("You have left the lobby, so you have been removed from your party.");
					if (!leader.getUsername().equals(player.getUsername())) {
						TOAPartyManagementInterface.updatePartyManagementInterface(leader);
					}
				}
			}
		}
		TOALobbyParty.setViewingParty(player, null);
		TOALobbyParty.setAppliedParty(player, null);
	}

	@Override public String name() {
		return "TOA lobby";
	}
}

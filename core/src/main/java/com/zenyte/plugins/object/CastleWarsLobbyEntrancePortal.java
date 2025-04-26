package com.zenyte.plugins.object;

import com.zenyte.ContentConstants;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsLobby;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import org.apache.commons.lang3.mutable.MutableBoolean;

import static com.zenyte.game.content.minigame.castlewars.CastleWarsLobby.SARADOMIN_LOBBY;
import static com.zenyte.game.content.minigame.castlewars.CastleWarsLobby.ZAMORAK_LOBBY;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsLobbyEntrancePortal implements ObjectAction {
    public static final int GUTHIX_PORTAL = 4408;
    public static final int SARADOMIN_PORTAL = 4387;
    public static final int ZAMORAK_PORTAL = 4388;

    public static void joinTeam(final Player player, final CastleWarsTeam team) {
        player.setLocation(team.getLobbySpawn());
        (team.equals(CastleWarsTeam.SARADOMIN) ? SARADOMIN_LOBBY : ZAMORAK_LOBBY).add(player);
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (!ContentConstants.CASTLE_WARS) {
            player.sendFilteredMessage("Nothing interesting happens.");
            return;
        }
        if (player.inArea("Castle Wars Lobby") || player.inArea("Castle Wars")) {
            return;
        }
        final MutableBoolean mutableBool = new MutableBoolean(player.getFollower() != null);
        for (final Item item : player.getInventory().getContainer().getItems().values()) {
            if (item == null || CastleWars.allowedItems.contains(item.getId())) {
                continue;
            }
            if (!CastleWars.allowedItems.contains(item.getId())) {
                player.sendMessage("You cannot bring non-combat items into Castle Wars.");
                return;
            }
            if (!mutableBool.isTrue()) {
                if (PetWrapper.getByItem(item.getId()) != null) {
                    mutableBool.setTrue();
                }
            }
        }
        if (mutableBool.isTrue()) {
            player.sendMessage("Pets are not allowed within Castle-Wars.");
            return;
        }
        for (final Item item : player.getEquipment().getContainer().getItems().values()) {
            if (item == null || CastleWars.allowedItems.contains(item.getId())) {
                continue;
            }
            if (!CastleWars.allowedItems.contains(item.getId())) {
                player.sendMessage("You cannot bring non-combat items into Castle Wars.");
                return;
            }
        }
        final int cape = player.getEquipment().getId(EquipmentSlot.CAPE);
        final int hat = player.getEquipment().getId(EquipmentSlot.HELMET);
        if (cape != -1 || hat != -1) {
            player.sendMessage("Please remove your cape and hat before entering Castle wars.");
            return;
        }
        if (object.getId() == GUTHIX_PORTAL) {
            if (SARADOMIN_LOBBY.size() == ZAMORAK_LOBBY.size()) {
                if (CastleWars.isActive() && CastleWarsLobby.isGameInbalance()) {
                    joinTeam(player, CastleWars.SARADOMIN_TEAM.size() > CastleWars.ZAMORAK_TEAM.size() ? CastleWarsTeam.ZAMORAK : CastleWarsTeam.SARADOMIN);
                } else {
                    joinTeam(player, Utils.random(1) == 0 ? CastleWarsTeam.SARADOMIN : CastleWarsTeam.ZAMORAK);
                }
            } else {
                joinTeam(player, SARADOMIN_LOBBY.size() < ZAMORAK_LOBBY.size() ? CastleWarsTeam.SARADOMIN : CastleWarsTeam.ZAMORAK);
            }
            return;
        }
        if ((object.getId() == SARADOMIN_PORTAL && SARADOMIN_LOBBY.size() > ZAMORAK_LOBBY.size()) || (object.getId() == ZAMORAK_PORTAL && ZAMORAK_LOBBY.size() > SARADOMIN_LOBBY.size())) {
            player.sendMessage("There is not enough space on this team!");
            return;
        }
        joinTeam(player, object.getId() == SARADOMIN_PORTAL ? CastleWarsTeam.SARADOMIN : CastleWarsTeam.ZAMORAK);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {SARADOMIN_PORTAL, ZAMORAK_PORTAL, GUTHIX_PORTAL};
    }
}

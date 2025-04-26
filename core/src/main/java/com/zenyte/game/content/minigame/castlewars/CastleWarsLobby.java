package com.zenyte.game.content.minigame.castlewars;

import com.zenyte.ContentConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.plugins.dialogue.CastleWarsLateJoinD;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsLobby extends PolygonRegionArea implements LoginPlugin, CannonRestrictionPlugin, RandomEventRestrictionPlugin, EquipmentPlugin, CycleProcessPlugin {
    private boolean started;
    private static int saradominInvites;
    private static int zamorakInvites;
    public static final Item SARADOMIN_CLOAK = new Item(4041, 1);
    public static final Item ZAMORAK_CLOAK = new Item(4042, 1);
    public static final Location MAIN_LOBBY_SPAWN = new Location(2440, 3090, 0);
    public static final Location SARADOMIN_LOBBY_SPAWN = new Location(2381, 9489, 0);
    public static final Location ZAMORAK_LOBBY_SPAWN = new Location(2421, 9523, 0);
    public static final Set<Player> SARADOMIN_LOBBY = new ObjectOpenHashSet<>();
    public static final Set<Player> ZAMORAK_LOBBY = new ObjectOpenHashSet<>();
    private static int ticks;

    @Override
    public void process() {
        if (!ContentConstants.CASTLE_WARS) {
            return;
        }
        if (!CastleWars.isActive() && !started && SARADOMIN_LOBBY.isEmpty() && ZAMORAK_LOBBY.isEmpty()) {
            return;
        }
        if (!started) {
            ticks = 200;
            started = true;
        }
        if (CastleWars.isActive()) {
            // Send late game invites due to imbalances every 15 seconds.
            if (ticks % 25 == 0) {
                if (isGameInbalance() && (SARADOMIN_LOBBY.size() > 0 || ZAMORAK_LOBBY.size() > 0)) {
                    dispatchLateInvites(CastleWars.SARADOMIN_TEAM.size() > CastleWars.ZAMORAK_TEAM.size() ? ZAMORAK_LOBBY : SARADOMIN_LOBBY);
                }
                if (!isGameInbalance() && (SARADOMIN_LOBBY.size() > 0 && ZAMORAK_LOBBY.size() > 0)) {
                    dispatchLateInvites(SARADOMIN_LOBBY);
                    dispatchLateInvites(ZAMORAK_LOBBY);
                }
            }
        }
        if (ticks == 0) {
            CastleWars.start();
            ticks = 2000;
        }
        if (ticks % 100 == 0) {
            for (final Player player : getPlayers()) {
                player.getVarManager().sendVar(CastleWarsLobbyOverlay.TIMER_VARP, (ticks / 100));
            }
        }
        ticks--;
    }

    public static boolean isGameInbalance() {
        return CastleWars.SARADOMIN_TEAM.size() != CastleWars.ZAMORAK_TEAM.size();
    }

    public void dispatchLateInvites(final Set<Player> lobby) {
        final CastleWarsTeam team = lobby.equals(SARADOMIN_LOBBY) ? CastleWarsTeam.SARADOMIN : CastleWarsTeam.ZAMORAK;
        for (final Player player : lobby) {
            if (!player.getInterfaceHandler().containsInterface(InterfacePosition.DIALOGUE)) {
                player.getDialogueManager().start(new CastleWarsLateJoinD(player, team));
            }
        }
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2377, 9499}, {2381, 9498}, {2384, 9494}, {2390, 9494}, {2394, 9486}, {2393, 9480}, {2369, 9480}, {2370, 9496}}), new RSPolygon(new int[][] {{2410, 9513}, {2430, 9513}, {2430, 9535}, {2410, 9535}})};
    }

    @Override
    public void enter(final Player player) {
        if (!ContentConstants.CASTLE_WARS) {
            return;
        }
        if (player.getHelmet() != null || player.getCape() != null) {
            leave(player, false);
            return;
        }
        if (started) {
            final int timer = Math.round((int) Math.ceil(ticks / 100));
            player.getVarManager().sendVar(CastleWarsLobbyOverlay.TIMER_VARP, timer);
        }
        /* Handle cloak setting */
        GameInterface.CASTLE_WARS_LOBBY_OVERLAY.open(player);
        player.getEquipment().set(EquipmentSlot.CAPE, getTeam(player).equals(CastleWarsTeam.SARADOMIN) ? SARADOMIN_CLOAK : ZAMORAK_CLOAK);
        player.getEquipment().refresh();
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        if (!ContentConstants.CASTLE_WARS) {
            return;
        }
        CastleWars.removeCwarsItems(player, true);
        (getTeam(player).equals(CastleWarsTeam.SARADOMIN) ? SARADOMIN_LOBBY : ZAMORAK_LOBBY).remove(player);
        player.getInterfaceHandler().closeInterface(GameInterface.CASTLE_WARS_LOBBY_OVERLAY);
    }

    @Override
    public boolean equip(final Player player, final Item item, final int slot) {
        if (slot == EquipmentSlot.CAPE.getSlot() || slot == EquipmentSlot.HELMET.getSlot()) {
            player.sendMessage("You can't remove your team's colours.");
            return false;
        }
        return true;
    }

    @Override
    public boolean unequip(final Player player, final Item item, final int slot) {
        if (slot == EquipmentSlot.CAPE.getSlot()) {
            player.sendMessage("You can't remove your team's colours.");
            return false;
        }
        return true;
    }

    @Override
    public String name() {
        return "Castle Wars Lobby";
    }

    private CastleWarsTeam getTeam(final Player player) {
        return SARADOMIN_LOBBY.contains(player) ? CastleWarsTeam.SARADOMIN : CastleWarsTeam.ZAMORAK;
    }

    @Override
    public void login(final Player player) {
        player.setLocation(MAIN_LOBBY_SPAWN);
    }

    public static int getSaradominInvites() {
        return saradominInvites;
    }

    public static void setSaradominInvites(int saradominInvites) {
        CastleWarsLobby.saradominInvites = saradominInvites;
    }

    public static int getZamorakInvites() {
        return zamorakInvites;
    }

    public static void setZamorakInvites(int zamorakInvites) {
        CastleWarsLobby.zamorakInvites = zamorakInvites;
    }

    public static int getTicks() {
        return ticks;
    }

    public static void setTicks(int ticks) {
        CastleWarsLobby.ticks = ticks;
    }
}

package com.zenyte.plugins.object;

import com.zenyte.game.content.minigame.castlewars.AttackCastleDoorAction;
import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsOverlay.CWarsOverlayVarbit;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.DoubleDoor;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsLargeDoor implements ObjectAction {
    public static final int[] ZAMORAK_DOORS = {4427, 4428, 4429, 4430, 4433, 4434};
    public static final int[] SARADOMIN_DOORS = {4423, 4424, 4425, 4426, 4431, 4432};
    public static final WorldObject ZAMORAK_DOOR = new WorldObject(ZAMORAK_DOORS[0], 0, 1, new Location(2373, 3119, 0));
    public static final WorldObject ZAMORAK_DOOR2 = new WorldObject(ZAMORAK_DOORS[1], 0, 1, new Location(2372, 3119, 0));
    public static final WorldObject BROKEN_ZAMORAK_DOOR = new WorldObject(ZAMORAK_DOORS[5], 0, 2, new Location(2373, 3120, 0));
    public static final WorldObject BROKEN_ZAMORAK_DOOR2 = new WorldObject(ZAMORAK_DOORS[4], 0, 0, new Location(2372, 3120, 0));
    public static final WorldObject SARADOMIN_DOOR = new WorldObject(SARADOMIN_DOORS[0], 0, 3, new Location(2426, 3088, 0));
    public static final WorldObject SARADOMIN_DOOR2 = new WorldObject(SARADOMIN_DOORS[1], 0, 3, new Location(2427, 3088, 0));
    public static final WorldObject BROKEN_SARADOMIN_DOOR = new WorldObject(SARADOMIN_DOORS[5], 0, 0, new Location(2426, 3087, 0));
    public static final WorldObject BROKEN_SARADOMIN_DOOR2 = new WorldObject(SARADOMIN_DOORS[4], 0, 2, new Location(2427, 3087, 0));

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (!player.inArea("Castle Wars")) {
            return;
        }
        final boolean saradominObject = CastleWarsTeam.SARADOMIN.getLargeCastleDoors().contains(object.getId());
        final CastleWarsTeam team = CastleWars.getTeam(player);
        if (option.equalsIgnoreCase("open")) {
            // anyone can open a door from inside.
            if ((saradominObject && player.getY() < object.getY()) || (!saradominObject && player.getY() > object.getY())) {
                DoubleDoor.handleDoubleDoor(player, object);
                return;
            }
            if (saradominObject && !team.equals(CastleWarsTeam.SARADOMIN) || !saradominObject && !team.equals(CastleWarsTeam.ZAMORAK)) {
                player.sendMessage("You can't open the other team's door, you have to attack it!");
            } else {
                DoubleDoor.handleDoubleDoor(player, object);
            }
            return;
        }
        if (option.equalsIgnoreCase("close")) {
            // TODO should also detect if OTHER players are in the door tiles!
            if (player.getY() == object.getY()) {
                player.sendMessage("The door is stuck!");
                return;
            }
            DoubleDoor.handleDoubleDoor(player, object);
        }
        if (option.equalsIgnoreCase("attack")) {
            player.faceObject(object);
            if ((saradominObject && team.equals(CastleWarsTeam.SARADOMIN)) || (!saradominObject && team.equals(CastleWarsTeam.ZAMORAK))) {
                player.sendMessage("You don't want to do damage to your own doors!");
                return;
            }
            final CastleWarsTeam oppositeTeam = team.equals(CastleWarsTeam.SARADOMIN) ? CastleWarsTeam.ZAMORAK : CastleWarsTeam.SARADOMIN;
            player.getActionManager().setAction(new AttackCastleDoorAction(oppositeTeam));
            return;
        }
        if (option.equalsIgnoreCase("repair")) {
            if (!player.getInventory().containsItem(CastleWars.TOOLKIT)) {
                player.sendMessage("You need a toolkit to repair these doors!");
                return;
            }
            // if(saradominObject && !team.equals(CastleWarsTeam.SARADOMIN) || !saradominObject && team.equals(CastleWarsTeam.SARADOMIN)) {
            //   player.sendMessage("You don't want to repair your opponents door.");
            //    return;
            // }
            player.getInventory().deleteItem(CastleWars.TOOLKIT.getId(), 1);
            World.removeObject(saradominObject ? BROKEN_SARADOMIN_DOOR : BROKEN_ZAMORAK_DOOR);
            World.removeObject(saradominObject ? BROKEN_SARADOMIN_DOOR2 : BROKEN_ZAMORAK_DOOR2);
            World.spawnObject(saradominObject ? SARADOMIN_DOOR : ZAMORAK_DOOR);
            World.spawnObject(saradominObject ? SARADOMIN_DOOR2 : ZAMORAK_DOOR2);
            CastleWars.setVarbit(saradominObject ? CastleWarsTeam.SARADOMIN : CastleWarsTeam.ZAMORAK, CWarsOverlayVarbit.DOOR_HEALTH, 100);
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        final List<Integer> list = new ArrayList<>();
        for (final CastleWarsTeam team : CastleWarsTeam.VALUES) {
            for (final int doorId : team.getLargeCastleDoors()) {
                list.add(doorId);
            }
        }
        return list.toArray(new Object[list.size()]);
    }
}

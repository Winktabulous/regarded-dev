package com.zenyte.plugins.object;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsOverlay.CWarsOverlayVarbit;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.content.minigame.castlewars.LockpickCastleDoorAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.DoorHandler;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsLockedDoor implements ObjectAction {
    public static final int UNLOCKED_SARADOMIN = 4465;
    public static final int LOCKED_SARADOMIN = 4466;
    public static final int UNLOCKED_ZAMORAK = 4467;
    public static final int LOCKED_ZAMORAK = 4468;

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final boolean saradomin = object.getId() == 4465 || object.getId() == 4466;
        final CastleWarsTeam team = CastleWars.getTeam(player);
        final CastleWarsTeam oppositeTeam = CastleWars.getOppositeTeam(player);
        if (option.toLowerCase().equals("lock")) {
            if (saradomin && team.equals(CastleWarsTeam.SARADOMIN) || !saradomin && !team.equals(CastleWarsTeam.SARADOMIN)) {
                lockDoor(team, object);
            } else {
                lockDoor(oppositeTeam, object);
            }
            return;
        }
        if (option.toLowerCase().equals("unlock")) {
            // anyone can open doors from inside
            if ((saradomin && player.getX() >= object.getX()) || (!saradomin && player.getX() <= object.getX())) {
                if (saradomin && team.equals(CastleWarsTeam.SARADOMIN) || !saradomin && !team.equals(CastleWarsTeam.SARADOMIN)) {
                    unlockDoor(team, object);
                } else {
                    unlockDoor(oppositeTeam, object);
                }
                return;
            }
            if ((saradomin && team.equals(CastleWarsTeam.SARADOMIN) || (!saradomin && team.equals(CastleWarsTeam.ZAMORAK)))) {
                unlockDoor(team, object);
                return;
            } else {
                player.getActionManager().setAction(new LockpickCastleDoorAction(oppositeTeam, object));
                return;
            }
        }
    }

    private void lockDoor(final CastleWarsTeam team, final WorldObject object) {
        CastleWars.setVarbit(team, CWarsOverlayVarbit.DOOR_LOCK, 0);
        DoorHandler.handleDoor(object, 500, true);
    }

    private void unlockDoor(final CastleWarsTeam team, final WorldObject object) {
        CastleWars.setVarbit(team, CWarsOverlayVarbit.DOOR_LOCK, 1);
        DoorHandler.handleDoor(object, 500, false);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {LOCKED_SARADOMIN, LOCKED_ZAMORAK, UNLOCKED_SARADOMIN, UNLOCKED_ZAMORAK};
    }
}

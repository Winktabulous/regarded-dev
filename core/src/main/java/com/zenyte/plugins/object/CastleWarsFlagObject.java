package com.zenyte.plugins.object;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsOverlay;
import com.zenyte.game.content.minigame.castlewars.CastleWarsOverlay.CWarsOverlayVarbit;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsFlagObject implements ObjectAction {
    public static final Item SARADOMIN_FLAG = new Item(4037, 1);
    public static final Item ZAMORAK_FLAG = new Item(4039, 1);
    public static final int SARADOMIN_FLAG_GROUND = 4900;
    public static final WorldObject saradominFlag = new WorldObject(4902, 11, 1, new Location(2429, 3074, 3));
    public static final WorldObject saradominFlagStand = new WorldObject(4377, 11, 1, new Location(2429, 3074, 3));
    public static final int ZAMORAK_FLAG_GROUND = 4901;
    public static final WorldObject zamorakFlag = new WorldObject(4903, 11, 3, new Location(2370, 3133, 3));
    public static final WorldObject zamorakFlagStand = new WorldObject(4378, 11, 3, new Location(2370, 3133, 3));

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final boolean saradomin = object.getId() == saradominFlag.getId() || object.getId() == saradominFlagStand.getId() || object.getId() == SARADOMIN_FLAG_GROUND;
        final CastleWarsTeam team = CastleWars.getTeam(player);
        if (object.getId() == saradominFlag.getId() || object.getId() == zamorakFlag.getId()) {
            if (saradomin && team.equals(CastleWarsTeam.SARADOMIN) || !saradomin && team.equals(CastleWarsTeam.ZAMORAK)) {
                if (player.getEquipment().getId(EquipmentSlot.WEAPON) == SARADOMIN_FLAG.getId() && object.getId() == zamorakFlag.getId() || player.getEquipment().getId(EquipmentSlot.WEAPON) == ZAMORAK_FLAG.getId() && object.getId() == saradominFlag.getId()) {
                    processSubmitCapture(team, player, object);
                } else {
                    final String prefix = team.equals(CastleWarsTeam.SARADOMIN) ? "Saradomin" : "Zamorak";
                    player.sendMessage(prefix + " won't let you take his standard!");
                }
                return;
            } else {
                processFlagPickup(team, player, object);
                return;
            }
        }
        if (object.getId() == saradominFlagStand.getId() || object.getId() == zamorakFlagStand.getId()) {
            final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
            if (weapon == -1) {
                player.sendMessage("You need the other team's flag to capture it!");
                return;
            }
            if (weapon == SARADOMIN_FLAG.getId() && object.getId() == saradominFlagStand.getId() || weapon == ZAMORAK_FLAG.getId() && object.getId() == zamorakFlagStand.getId()) {
                return;
            }
            if (weapon == SARADOMIN_FLAG.getId() || weapon == ZAMORAK_FLAG.getId()) {
                processSubmitCapture(team, player, object);
                return;
            }
            return;
        }
        // code for handling flag object that is on the ground
        if (option.equalsIgnoreCase("take")) {
            if (team.equals(CastleWarsTeam.SARADOMIN) && object.getId() == SARADOMIN_FLAG_GROUND || !team.equals(CastleWarsTeam.SARADOMIN) && object.getId() == ZAMORAK_FLAG_GROUND) {
                processFlagReturn(team, object);
            } else {
                processFlagPickup(team, player, object);
            }
            return;
        }
    }

    public void processFlagReturn(final CastleWarsTeam team, final WorldObject object) {
        final boolean saradomin = object.getId() == saradominFlag.getId() || object.getId() == saradominFlagStand.getId() || object.getId() == SARADOMIN_FLAG_GROUND;
        final CastleWarsOverlay.CWarsOverlayVarbit flagVarbit = saradomin ? CWarsOverlayVarbit.SARADOMIN_FLAG : CWarsOverlayVarbit.ZAMORAK_FLAG;
        World.removeObject(object);
        World.removeObject(saradomin ? saradominFlagStand : zamorakFlagStand);
        World.spawnObject(saradomin ? saradominFlag : zamorakFlag);
        CastleWars.setVarbit(team, flagVarbit, 0);
    }

    public void processFlagPickup(final CastleWarsTeam team, final Player player, final WorldObject object) {
        final boolean saradomin = object.getId() == saradominFlag.getId() || object.getId() == saradominFlagStand.getId() || object.getId() == SARADOMIN_FLAG_GROUND;
        final boolean hasWeapon = player.getEquipment().getId(EquipmentSlot.WEAPON) != -1;
        final boolean hasShield = player.getEquipment().getId(EquipmentSlot.WEAPON) != -1;
        final CastleWarsOverlay.CWarsOverlayVarbit flagVarbit = saradomin ? CWarsOverlayVarbit.SARADOMIN_FLAG : CWarsOverlayVarbit.ZAMORAK_FLAG;
        if (!player.getInventory().hasFreeSlots() && (hasShield || hasWeapon) || player.getInventory().getFreeSlots() == 1 && (hasShield && hasWeapon)) {
            player.sendMessage("You don't have enough inventory space to take the flag!");
            return;
        }
        player.getEquipment().unequipItem(EquipmentSlot.WEAPON.getSlot());
        player.getEquipment().unequipItem(EquipmentSlot.SHIELD.getSlot());
        player.getEquipment().set(EquipmentSlot.WEAPON, saradomin ? SARADOMIN_FLAG : ZAMORAK_FLAG);
        World.removeObject(object);
        World.spawnObject(saradomin ? saradominFlagStand : zamorakFlagStand);
        CastleWars.setVarbit(team, flagVarbit, 1);
    }

    public void processSubmitCapture(final CastleWarsTeam team, final Player player, final WorldObject object) {
        final boolean saradomin = object.getId() == saradominFlag.getId() || object.getId() == saradominFlagStand.getId() || object.getId() == SARADOMIN_FLAG_GROUND;
        final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
        final CastleWarsOverlay.CWarsOverlayVarbit scoreVarbit = weapon == SARADOMIN_FLAG.getId() ? CWarsOverlayVarbit.ZAMORAK_SCORE : CWarsOverlayVarbit.SARADOMIN_SCORE;
        final CastleWarsOverlay.CWarsOverlayVarbit flagVarbit = weapon == SARADOMIN_FLAG.getId() ? CWarsOverlayVarbit.SARADOMIN_FLAG : CWarsOverlayVarbit.ZAMORAK_FLAG;
        player.getEquipment().set(EquipmentSlot.WEAPON, null);
        CastleWars.setVarbit(team, scoreVarbit, CastleWars.getVarbit(team, scoreVarbit) + 1);
        CastleWars.setVarbit(team, flagVarbit, 0);
        World.removeObject(saradomin ? zamorakFlagStand : saradominFlagStand);
        World.spawnObject(saradomin ? zamorakFlag : saradominFlag);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {saradominFlag.getId(), saradominFlagStand.getId(), SARADOMIN_FLAG_GROUND, zamorakFlag.getId(), zamorakFlagStand.getId(), ZAMORAK_FLAG_GROUND};
    }
}

package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsOverlay;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class ItemOnCastleWarsCatapult implements ItemOnObjectAction {
    public static final WorldObject SARADOMIN_CATAPULT = new WorldObject(4382, 11, 3, new Location(2413, 3088, 0));
    public static final WorldObject BROKEN_SARADOMIN_CATAPULT = new WorldObject(4385, 10, 3, SARADOMIN_CATAPULT);
    public static final WorldObject ZAMORAK_CATAPULT = new WorldObject(4381, 11, 1, new Location(2484, 3117, 0));
    public static final WorldObject BROKEN_ZAMORAK_CATAPULT = new WorldObject(4386, 10, 1, ZAMORAK_CATAPULT);

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        final boolean broken = object.getId() == BROKEN_ZAMORAK_CATAPULT.getId() || object.getId() == BROKEN_SARADOMIN_CATAPULT.getId();
        final boolean isSaradomin = CastleWars.getTeam(player).equals(CastleWarsTeam.SARADOMIN);
        final boolean saradominObject = object.getId() == SARADOMIN_CATAPULT.getId() || object.getId() == BROKEN_SARADOMIN_CATAPULT.getId();
        if (broken) {
            if (!player.getInventory().containsItem(CastleWars.TOOLKIT)) {
                return;
            }
            // check if other team is attempting to repair the catapult
            if (!isSaradomin && saradominObject || isSaradomin && !saradominObject) {
                player.sendMessage("You don\'t want to repair the other team\'s catapult.");
                return;
            }
            player.getInventory().deleteItem(CastleWars.TOOLKIT.getId(), 1);
            World.removeObject(saradominObject ? BROKEN_SARADOMIN_CATAPULT : BROKEN_ZAMORAK_CATAPULT);
            World.spawnObject(saradominObject ? SARADOMIN_CATAPULT : ZAMORAK_CATAPULT);
            CastleWars.setVarbit(CastleWars.getTeam(player), CastleWarsOverlay.CWarsOverlayVarbit.CATAPULT, 0);
            return;
        } else {
            if (!player.getInventory().containsItem(CastleWars.EXPLOSIVE_POTION)) {
                return;
            }
            if (isSaradomin && saradominObject || !isSaradomin && !saradominObject) {
                player.sendMessage("You don\'t want to destroy your team\'s catapult.");
                return;
            }
            World.removeObject(saradominObject ? SARADOMIN_CATAPULT : ZAMORAK_CATAPULT);
            World.spawnObject(saradominObject ? BROKEN_SARADOMIN_CATAPULT : BROKEN_ZAMORAK_CATAPULT);
            CastleWars.setVarbit(CastleWars.getOppositeTeam(player), CastleWarsOverlay.CWarsOverlayVarbit.CATAPULT, 1);
            return;
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] {CastleWars.EXPLOSIVE_POTION.getId(), CastleWars.TOOLKIT.getId()};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {SARADOMIN_CATAPULT.getId(), BROKEN_SARADOMIN_CATAPULT.getId(), ZAMORAK_CATAPULT.getId(), BROKEN_ZAMORAK_CATAPULT.getId()};
    }
}

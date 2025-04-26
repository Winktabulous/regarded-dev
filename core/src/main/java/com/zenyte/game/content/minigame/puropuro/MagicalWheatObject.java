package com.zenyte.game.content.minigame.puropuro;

import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

/**
 * @author Corey
 * @since 29/01/2020
 */
public class MagicalWheatObject implements ObjectAction {

    private static final Int2ObjectArrayMap<String> animations = new Int2ObjectArrayMap<String>() {

        {
            put(6593, "You use your strength to push through the wheat in the most efficient fashion.");
            put(6594, "You use your strength to push through the wheat.");
            put(6595, "You push through the wheat. It's hard work though.");
        }
    };

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Push-through")) {
            final Location newLocation = object.getPosition().transform(object.getX() - player.getX(), object.getY() - player.getY(), 0);
            if (!World.isSquareFree(newLocation.getX(), newLocation.getY(), 0, 1)) {
                player.sendMessage("The wheat here seems unusually stubborn. You cannot push through.");
                return;
            }
            final Int2ObjectMap.Entry<String> animation = Utils.getRandomCollectionElement(animations.int2ObjectEntrySet());
            final Animation pushAnimation = new Animation(animation.getIntKey());
            player.setAnimation(pushAnimation);
            player.sendFilteredMessage(animation.getValue());
            player.sendSound(new SoundEffect(3728));
            player.lock((AnimationUtil.getDuration(pushAnimation) / 600));
            final Location currentTile = new Location(player.getLocation());
            player.setLocation(newLocation);
            player.setForceMovement(new ForceMovement(currentTile, 1, newLocation, AnimationUtil.getDuration(pushAnimation) / 20, DirectionUtil.getFaceDirection(newLocation.getX() - currentTile.getX(), newLocation.getY() - currentTile.getY())));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { PuroPuroArea.MagicalWheatSpawn.SPAWNED_WHEAT, PuroPuroArea.MagicalWheatSpawn.REGULAR_WHEAT_PERP, PuroPuroArea.MagicalWheatSpawn.REGULAR_WHEAT, ObjectId.MAGICAL_WHEAT_25017, ObjectId.MAGICAL_WHEAT_25029 };
    }
}

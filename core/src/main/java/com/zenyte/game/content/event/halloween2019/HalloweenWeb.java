package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import mgi.custom.halloween.HalloweenObject;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * @author Kris | 04/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HalloweenWeb implements ObjectAction, ItemOnObjectAction {
    private static final Animation KNIFE_ANIMATION = new Animation(911);
    public static final Predicate<String> sharpBladePredicate = name -> name.contains("scythe") || name.contains("sword") || name.contains("dagger") || name.contains("scimitar") || name.contains("axe") || name.equals("knife") || name.contains("whip") || name.contains("bludgeon") || name.contains("tentacle");

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Item item = player.getWeapon();
        if (item != null && item.getDefinitions().getSlot() != -1 && sharpBladePredicate.test(item.getName().toLowerCase())) {
            slash(player, object, item);
            return;
        }
        if (player.getInventory().containsItem(946, 1)) {
            slash(player, object, new Item(946));
            return;
        }
        player.sendMessage("Only a sharp blade can cut through this sticky web.");
    }

    public static final void slash(@NotNull final Player player, @NotNull final WorldObject web, @NotNull final Item weapon) {
        player.resetWalkSteps();
        player.addWalkSteps(web.getX(), web.getY(), 1, true);
        if (player.hasWalkSteps()) {
            player.lock(1);
        }
        WorldTasksManager.scheduleOrExecute(() -> {
            if (player.matches(web)) {
                final Location dest = web.transform(web.getRotation() == 1 ? Direction.NORTH : web.getRotation() == 0 ? Direction.WEST : web.getRotation() == 2 ? Direction.EAST : Direction.SOUTH, 1);
                player.setFaceLocation(dest);
            }
            player.lock(1);
            player.setAnimation(weapon.getId() == 946 ? KNIFE_ANIMATION : new Animation(Equipment.getAttackAnimation(weapon.getId(), 1)));
            final int random = weapon.getName().contains("wilderness sword") ? 0 : Utils.random(2);
            if (weapon.getId() != 946) {
                final ItemDefinitions defs = weapon.getDefinitions();
                player.getAppearance().setRenderAnimation(new RenderAnimation(defs.getStandAnimation(), defs.getStandTurnAnimation(), defs.getWalkAnimation(), defs.getRotate180Animation(), defs.getRotate90Animation(), defs.getRotate270Animation(), defs.getRunAnimation()));
                player.getAppearance().forceAppearance(EquipmentSlot.WEAPON.getSlot(), weapon.getId());
                if (weapon.getDefinitions().isTwoHanded()) {
                    player.getAppearance().forceAppearance(EquipmentSlot.SHIELD.getSlot(), -1);
                }
                WorldTasksManager.schedule(() -> {
                    player.getAppearance().resetRenderAnimation();
                    player.getAppearance().clearForcedAppearance();
                });
            }
            if (random == 0) {
                web.setLocked(true);
                WorldTasksManager.schedule(() -> {
                    web.setLocked(false);
                    World.spawnTemporaryObject(new WorldObject(734, web.getType(), web.getRotation(), web), web, 100);
                });
            }
        }, player.hasWalkSteps() ? 1 : -1);
    }

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        final String name = item.getName().toLowerCase();
        if (item.getDefinitions().getSlot() != -1 && sharpBladePredicate.test(name)) {
            slash(player, object, item);
            return;
        }
        player.sendMessage("Only a sharp blade can cut through this sticky web.");
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {HalloweenObject.WEB.getRepackedObject()};
    }
}

package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.AttackStyleDefinition;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

import static com.zenyte.game.world.entity.npc.combatdefs.AttackType.SLASH;

/**
 * @author Kris | 9. juuni 2018 : 07:19:48
 * @author Glabay | Sept 3rd, 2024
 * @see <a href="https://www.rune-server.ee/members/kris/">Kris' Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Kris' Rune-Status profile</a>}
 */
public final class SpiderWebObjectAction implements ObjectAction {

    private static final Random random = new Random();

    private static final Predicate<String> namePredicate = name -> name.equals("knife") ||
        name.contains("scythe") ||
        name.contains("sword") ||
        name.contains("dagger") ||
        name.contains("scimitar") ||
        name.contains("axe") ||
        name.contains("whip") ||
        name.contains("bludgeon") ||
        name.contains("voidwaker") ||
        name.contains("osmumten") ||
        name.contains("tentacle");
    public static final Predicate<Item> sharpBladePredicate = item -> namePredicate.test(item.getName().toLowerCase()) ||
        AttackStyleDefinition.isSlashVarbit(item.getDefinitions().getInterfaceVarbit());

    private Item getSharpestItem(final Player player) {
        var sharpest = player.getWeapon(); // Assume they have the sharpest item equipped
        var itemsToCheck = player.getInventory().getContainer().getItemsAsList();
        if (player.getInventory().containsItem(ItemId.KNIFE))
            return new Item(ItemId.KNIFE);
        for (var item : itemsToCheck) {
            if (item.getDefinitions() == null) continue;
            if (item.getDefinitions().getBonuses() == null) continue;
            sharpest = compare(item, sharpest);
        }

        if (Objects.isNull(sharpest)) {
            player.sendMessage("You don't have something sharp enough.");
            return null;
        }
        return sharpest;
    }

    private Item compare(Item item, Item best) {
        if (Objects.isNull(best))
            return item;
        if (Objects.isNull(item))
            return best;
        if (Objects.isNull(item.getDefinitions()))
            return best;
        var itemDef = item.getDefinitions();
        if (Objects.isNull(itemDef.getBonuses()) || itemDef.getBonuses().length < 2)
            return best;
        var itemSlash = itemDef.getBonuses()[1];
        if (itemSlash == 0)
            return best;
        if (itemSlash <= best.getDefinitions().getBonuses()[1])
            return best;
        else return item;
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Item item = getSharpestItem(player);
        if (item != null && item.getDefinitions().getSlot() != -1 && sharpBladePredicate.test(item)) {
            slash(player, object, item);
            return;
        }
        for(Int2ObjectMap.Entry<Item> it : player.getInventory().getContainer().getItems().int2ObjectEntrySet()) {
            final Item inv = it.getValue();
            if (inv != null && inv.getDefinitions().getSlot() != -1 && sharpBladePredicate.test(inv)) {
                slash(player, object, inv);
                return;
            }
        }
        if (player.getInventory().containsItem(ItemId.KNIFE, 1)) {
            slash(player, object, new Item(ItemId.KNIFE));
            return;
        }
        player.sendMessage("Only a sharp blade can cut through this sticky web.");
    }

    private static Animation getSlashAnimation(final int weaponId) {
        return switch (weaponId) {
            case ItemId.KNIFE -> new Animation(911);
            case ItemId.OSMUMTENS_FANG, ItemId.OSMUMTENS_FANG_OR -> new Animation(390);
            default -> new Animation(Equipment.getAttackAnimation(weaponId, 1));
        };
    }

    private static double getSlashChance(Item item) {
        if (item.getId() == ItemId.KNIFE) return 1;
        var slashBonus = item.getDefinitions().getBonuses()[1];
        if (slashBonus >= 20) {
            var minChance = 50.0;
            var maxChance = 99.0;
            var scale = (slashBonus - 20) / 79.0; // 79 is the range (99 - 20)
            return (minChance + scale * (maxChance - minChance)) / 100;
        }
        else
            return item.getName().toLowerCase().contains("wilderness sword") ? 1 : // 100% chance to cut
                item.getId() == ItemId.KNIFE ? 0.5 : // 50% chance to cut
                    0.2; // 20% chance to cut
    }

    public static void slash(@NotNull final Player player, @NotNull final WorldObject web, @NotNull final Item weapon) {
        player.lock(1);
        player.setAnimation(getSlashAnimation(weapon.getId()));
        player.sendSound(2500);
        if (weapon.getId() != ItemId.KNIFE) {
            player.getAppearance().setRenderAnimation(weapon.getDefinitions().getRenderAnimation());
            player.getAppearance().forceAppearance(EquipmentSlot.WEAPON.getSlot(), weapon.getId());
            if (weapon.getDefinitions().isTwoHanded())
                player.getAppearance().forceAppearance(EquipmentSlot.SHIELD.getSlot(), -1);
            WorldTasksManager.schedule(() -> {
                player.getAppearance().resetRenderAnimation();
                player.getAppearance().clearForcedAppearance();
            });
        }
        var chance = getSlashChance(weapon);
        var roll = random.nextDouble();
        if (chance >= roll) {
            web.setLocked(true);
            WorldTasksManager.schedule(() -> {
                web.setLocked(false);
                World.spawnTemporaryObject(new WorldObject(ObjectId.SLASHED_WEB, web.getType(), web.getRotation(), web), web, 100);
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.WEB };
    }
}

package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.herblore.PoisonableWeapon;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Tommeh | 17-3-2019 | 20:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PoisonedWeaponCleaningAction implements ItemOnItemAction {
    private static final Item CLEANING_CLOTH = new Item(3188);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item weapon = from.getId() != CLEANING_CLOTH.getId() ? from : to;
        final int weaponSlot = from.getId() != CLEANING_CLOTH.getId() ? fromSlot : toSlot;
        final PoisonableWeapon poisonableWeapon = PoisonableWeapon.get(weapon.getId());
        if (poisonableWeapon == null) {
            return;
        }
        final Item item = new Item(poisonableWeapon.getBase(), weapon.getAmount());
        if (item.isStackable() && player.getInventory().containsItem(poisonableWeapon.getBase(), 1)) {
            player.getInventory().set(weaponSlot, null);
            player.getInventory().addItem(new Item(poisonableWeapon.getBase(), weapon.getAmount()));
        } else {
            player.getInventory().set(weaponSlot, item);
        }
        player.sendMessage("You clean the poison from the weapon.");
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final PoisonableWeapon weapon : PoisonableWeapon.ALL) {
            list.add(weapon.getId());
        }
        list.add(CLEANING_CLOTH.getId());
        return list.toArray(new int[list.size()]);
    }
}

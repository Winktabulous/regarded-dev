package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.herblore.PoisonAttachement;
import com.zenyte.game.content.skills.herblore.PoisonableWeapon;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Tommeh | 17-3-2019 | 20:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WeaponPoisoningItemAction implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item poison = from.getName().contains("Weapon poison") ? from : to;
        final Item weapon = !from.getName().contains("Weapon poison") ? from : to;
        final PoisonAttachement attachement = PoisonAttachement.get(poison.getId());
        if (attachement == null) {
            return;
        }
        final PoisonableWeapon poisonableWeapon = PoisonableWeapon.get(weapon.getId(), attachement);
        if (poisonableWeapon == null) {
            return;
        }
        player.getInventory().deleteItem(new Item(attachement.getPotion()));
        player.getInventory().addOrDrop(new Item(229));
        final int amount = player.getInventory().deleteItem(weapon.getId(), weapon.isStackable() ? 5 : 1).getSucceededAmount();
        player.getInventory().addOrDrop(new Item(poisonableWeapon.getId(), amount, weapon.getAttributes()));
        final String name = weapon.isStackable() ? (weapon.getName().endsWith("s") ? weapon.getName() : weapon.getName() + "s") : weapon.getName();
        player.sendMessage("You coat the " + name.toLowerCase().replace("knifes", "knives") + " with " + poison.getName().toLowerCase() + ".");
    }

    @Override
    public int[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final PoisonableWeapon weapon : PoisonableWeapon.ALL) {
            if (!list.contains(weapon.getBase())) {
                list.add(weapon.getBase());
            }
        }
        for (final PoisonAttachement attachement : PoisonAttachement.ALL) {
            list.add(attachement.getPotion());
        }
        return list.toArray(new int[0]);
    }
}

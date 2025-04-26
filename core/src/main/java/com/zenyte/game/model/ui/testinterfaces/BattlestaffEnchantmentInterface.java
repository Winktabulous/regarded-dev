package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.utils.Articles;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 12/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BattlestaffEnchantmentInterface extends Interface {

    private enum Battlestaff {
        AIR_BATTLESTAFF(ItemOnItemAction.ItemPair.of(1397, 1405)), WATER_BATTLESTAFF(ItemOnItemAction.ItemPair.of(1395, 1403)), EARTH_BATTLESTAFF(ItemOnItemAction.ItemPair.of(1399, 1407)), FIRE_BATTLESTAFF(ItemOnItemAction.ItemPair.of(1393, 1401)), LAVA_BATTLESTAFF(ItemOnItemAction.ItemPair.of(3053, 3054), ItemOnItemAction.ItemPair.of(21198, 21200)), MUD_BATTLESTAFF(ItemOnItemAction.ItemPair.of(6562, 6563)), STEAM_BATTLESTAFF(ItemOnItemAction.ItemPair.of(11787, 11789), ItemOnItemAction.ItemPair.of(12795, 12796)), SMOKE_BATTLESTAFF(ItemOnItemAction.ItemPair.of(11998, 12000)), MIST_BATTLESTAFF(ItemOnItemAction.ItemPair.of(20730, 20733)), DUST_BATTLESTAFF(ItemOnItemAction.ItemPair.of(20736, 20739));
        private static final Battlestaff[] values = values();
        private final ItemOnItemAction.ItemPair[] itemPairs;

        Battlestaff(@NotNull final ItemOnItemAction.ItemPair... itemPairs) {
            this.itemPairs = itemPairs;
        }
    }

    @Override
    protected void attach() {
        put(4, "Air battlestaff");
        put(5, "Water battlestaff");
        put(6, "Earth battlestaff");
        put(7, "Fire battlestaff");
        put(8, "Lava battlestaff");
        put(9, "Mud battlestaff");
        put(10, "Steam battlestaff");
        put(3, "Smoke battlestaff");
        put(11, "Mist battlestaff");
        put(12, "Dust battlestaff");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Air battlestaff", player -> enchant(player, Battlestaff.AIR_BATTLESTAFF));
        bind("Water battlestaff", player -> enchant(player, Battlestaff.WATER_BATTLESTAFF));
        bind("Earth battlestaff", player -> enchant(player, Battlestaff.EARTH_BATTLESTAFF));
        bind("Fire battlestaff", player -> enchant(player, Battlestaff.FIRE_BATTLESTAFF));
        bind("Lava battlestaff", player -> enchant(player, Battlestaff.LAVA_BATTLESTAFF));
        bind("Mud battlestaff", player -> enchant(player, Battlestaff.MUD_BATTLESTAFF));
        bind("Steam battlestaff", player -> enchant(player, Battlestaff.STEAM_BATTLESTAFF));
        bind("Smoke battlestaff", player -> enchant(player, Battlestaff.SMOKE_BATTLESTAFF));
        bind("Mist battlestaff", player -> enchant(player, Battlestaff.MIST_BATTLESTAFF));
        bind("Dust battlestaff", player -> enchant(player, Battlestaff.DUST_BATTLESTAFF));
    }

    private static final void enchant(@NotNull final Player player, @NotNull final Battlestaff battlestaff) {
        final Inventory inventory = player.getInventory();
        int slot = -1;
        ItemOnItemAction.ItemPair p = null;
        iterator:
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null) {
                continue;
            }
            for (final ItemOnItemAction.ItemPair pair : battlestaff.itemPairs) {
                if (item.getId() == pair.getLeft()) {
                    slot = i;
                    p = pair;
                    break iterator;
                }
            }
        }
        final String name = battlestaff.toString().toLowerCase().replace("_", " ");
        player.getInterfaceHandler().closeInterface(GameInterface.BATTLESTAFF_ENCHANTMENT);
        final int cost = DiaryUtil.eligibleFor(DiaryReward.KANDARIN_HEADGEAR4, player) ? 20000 : DiaryUtil.eligibleFor(DiaryReward.KANDARIN_HEADGEAR3, player) ? 30000 : 40000;
        if (slot == -1) {
            player.sendMessage("You need " + Articles.get(name) + " " + name + " to enchant.");
            return;
        }
        if (!inventory.containsItem(995, cost)) {
            player.sendMessage("You need at least " + StringFormatUtil.format(cost) + " coins to enchant the battlestaff.");
            return;
        }
        inventory.deleteItem(new Item(995, cost));
        inventory.deleteItem(slot, inventory.getItem(slot));
        inventory.addOrDrop(new Item(p.getRight()));
        player.sendMessage("You enchant the " + name + ".");
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.BATTLESTAFF_ENCHANTMENT;
    }
}

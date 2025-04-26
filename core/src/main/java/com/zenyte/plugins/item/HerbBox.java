package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Action;

import java.util.Optional;

/**
 * @author Kris | 03/05/2019 22:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HerbBox extends ItemPlugin {

    private enum BoxedHerb {
        GUAM(199, 32),
        MARRENTILL(201, 24),
        TARROMIN(203, 17),
        HARRALANDER(205, 14),
        RANARR(207, 11),
        IRIT(209, 8),
        AVANTOE(211, 6),
        KWUARM(213, 6),
        CADANTINE(215, 4),
        LANTADYME(2485, 3),
        DWARF_WEED(217, 3);
        private final int id;
        private final int weight;
        private static final BoxedHerb[] values = values();

        private static Optional<BoxedHerb> getReward() {
            final int roll = Utils.random(128);
            int current = 0;
            for (final HerbBox.BoxedHerb reward : values) {
                if ((current += reward.weight) >= roll) {
                    return Optional.of(reward);
                }
            }
            return Optional.empty();
        }

        BoxedHerb(int id, int weight) {
            this.id = id;
            this.weight = weight;
        }
    }

    @Override
    public void handle() {
        bind("Take-one", (player, item, container, slotId) -> {
            int count = player.getNumericAttribute("herb box herbs count").intValue();
            if (player.getInventory().getFreeSlots() <= (count >= 9 ? 1 : 0)) {
                player.sendMessage("You need some more free space to do this.");
                return;
            }
            final Item herb = new Item(BoxedHerb.getReward().orElseThrow(RuntimeException::new).id);
            if (count >= 9) {
                player.getInventory().deleteItem(item.getId(), 1);
            } else if (item.getId() != 11739) {
                item.setId(11739);
                player.getInventory().refreshAll();
            }
            player.getInventory().addOrDrop(herb);
            player.sendMessage("You find a " + herb.getName().toLowerCase() + " in the box.");
            if (count < 9) {
                player.addAttribute("herb box herbs count", count + 1);
            } else {
                player.getAttributes().remove("herb box herbs count");
            }
        });
        bind("Bank-all", (player, item, slotId) -> {
            player.getActionManager().setAction(new Action() {
                @Override
                public boolean start() {
                    return true;
                }
                @Override
                public boolean process() {
                    return player.getInventory().containsItem(11738, 1) || player.getInventory().containsItem(11739, 1);
                }
                @Override
                public int processWithDelay() {
                    final int startingNumber = player.getNumericAttribute("herb box herbs count").intValue();
                    player.getAttributes().remove("herb box herbs count");
                    final int result = player.getInventory().deleteItem(new Item(11738, 1)).getSucceededAmount();
                    if (result == 0) {
                        player.getInventory().deleteItem(new Item(11739, 1));
                    }
                    for (int count = startingNumber; count < 10; count++) {
                        final Item herb = new Item(BoxedHerb.getReward().orElseThrow(RuntimeException::new).id);
                        player.getBank().add(herb);
                    }
                    player.sendMessage((10 - startingNumber) + " herbs have been deposited in your bank.");
                    return 2;
                }
            });
        });
        bind("Check", (player, item, slotId) -> {
            final int startingNumber = player.getNumericAttribute("herb box herbs count").intValue();
            player.sendMessage("The herb box has " + (10 - startingNumber) + " herbs remaining.");
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {11738, 11739};
    }
}

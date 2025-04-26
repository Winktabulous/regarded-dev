package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.CompostBin;
import com.zenyte.game.content.skills.farming.CompostBinType;
import com.zenyte.game.content.skills.farming.FarmingConstants;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Optional;

/**
 * @author Kris | 23/02/2019 11:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BinFilling extends Action {
    private static final Animation animation = new Animation(832);
    private static final SoundEffect sound = new SoundEffect(2441);
    private final FarmingSpot spot;
    private final Item item;

    @Override
    public boolean start() {
        final CompostBin bin = spot.getCompostBin();
        final int id = item.getId();
        if (id == FarmingConstants.VOLCANIC_ASH) {
            final Optional<CompostBinType> type = bin.getType();
            if (!type.isPresent() || type.get() != CompostBinType.SUPERCOMPOST) {
                player.getDialogueManager().start(new PlainChat(player, "You can only apply volcanic ash to a bin containing super compost."));
                return false;
            }
            if (!bin.isFull()) {
                player.getDialogueManager().start(new PlainChat(player, "You can only apply volcanic ash to a bin filled with super compost."));
                return false;
            }
        } else if (bin.isFull()) {
            player.getDialogueManager().start(new PlainChat(player, "The compost bin must be emptied before you can put new items in it."));
            return false;
        }
        if (id == FarmingConstants.VOLCANIC_ASH) {
            final CompostBinType type = bin.getType().orElseThrow(RuntimeException::new);
            final int[] array = type.getCompost();
            final int value = spot.getValue();
            final int binCount = bin.getAmount();
            if (value != array[binCount - 1]) {
                player.sendMessage("You can only add the volcanic ash when the vegetation has finished rotting.");
                return false;
            }
            final int count = spot.getCompostBin().isBig() ? 50 : 25;
            if (!player.getInventory().containsItem(ItemId.VOLCANIC_ASH, count)) {
                player.sendMessage("You need at least " + count + " volcanic ash to fill the compost bin.");
                return false;
            }
        }
        play();
        delay(2);
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        final int id = item.getId();
        if (id == FarmingConstants.VOLCANIC_ASH) {
            final Inventory inventory = player.getInventory();
            inventory.deleteItem(new Item(id, spot.getCompostBin().isBig() ? 50 : 25));
            spot.getCompostBin().setType(CompostBinType.ULTRACOMPOST);
            final CompostBin bin = spot.getCompostBin();
            final CompostBinType type = bin.getType().orElseThrow(RuntimeException::new);
            final int[] array = type.getCompost();
            final int count = bin.getAmount();
            spot.setValue(array[count - 1]);
            spot.refresh();
            return -1;
        }
        final Inventory inventory = player.getInventory();
        inventory.deleteItem(new Item(id, spot.addCompostableItem(new Item(id, id == FarmingConstants.WEEDS ? inventory.getAmountOf(id) : 1))));
        if (!inventory.containsItem(id, 1) || spot.getCompostBin().isFull()) {
            return -1;
        }
        play();
        return 1;
    }

    private void play() {
        player.setAnimation(animation);
        if (item.getId() != FarmingConstants.VOLCANIC_ASH) {
            player.sendSound(sound);
        }
    }

    public BinFilling(FarmingSpot spot, Item item) {
        this.spot = spot;
        this.item = item;
    }
}

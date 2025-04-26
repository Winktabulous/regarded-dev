package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.CompostBin;
import com.zenyte.game.content.skills.farming.CompostBinType;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Optional;

/**
 * @author Kris | 22/05/2019 13:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BinSaturating extends Action {
    private static final Animation animation = new Animation(2569);
    private static final SoundEffect sound = new SoundEffect(2447);
    private final FarmingSpot spot;
    private final Item item;

    @Override
    public boolean start() {
        final CompostBin bin = spot.getCompostBin();
        final Optional<CompostBinType> type = bin.getType();
        if (!type.isPresent() || type.get() != CompostBinType.COMPOST) {
            player.getDialogueManager().start(new PlainChat(player, "You can only apply compost potion to a bin containing normal compost."));
            return false;
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
        final Inventory inventory = player.getInventory();
        inventory.deleteItem(new Item(id, 1));
        inventory.addOrDrop(new Item((id + 2) == 6478 ? 229 : (id + 2), 1));
        spot.getCompostBin().setType(CompostBinType.SUPERCOMPOST);
        final CompostBin bin = spot.getCompostBin();
        final CompostBinType type = bin.getType().orElseThrow(RuntimeException::new);
        final int[] array = type.getCompost();
        final int count = bin.getAmount();
        spot.setValue(array[count - 1]);
        spot.refresh();
        return -1;
    }

    private void play() {
        player.setAnimation(animation);
        player.sendSound(sound);
    }

    public BinSaturating(FarmingSpot spot, Item item) {
        this.spot = spot;
        this.item = item;
    }
}

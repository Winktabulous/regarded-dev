package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 23/06/2019 15:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SlimeCollecting extends Action {

    private static final Animation collect = new Animation(4471);

    @Override
    public boolean start() {
        if (!player.getInventory().containsItem(1925, 1)) {
            player.getDialogueManager().start(new ItemChat(player, new Item(1925, 1), "You need an empty bucket to collect slime."));
            return false;
        }
        delay(1);
        return true;
    }

    @Override
    public boolean process() {
        return player.getInventory().containsItem(1925, 1);
    }

    @Override
    public int processWithDelay() {
        player.setAnimation(collect);
        player.getInventory().deleteItem(1925, 1);
        player.getInventory().addItem(4286, 1);
        return 2;
    }

    public SlimeCollecting() {
    }
}

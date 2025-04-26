package com.zenyte.game.content.skills.agility.wildernesscourse;

import com.zenyte.game.content.skills.agility.AbstractAgilityCourse;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;

import java.util.function.Consumer;

/**
 * @author Tommeh | 24 feb. 2018 : 23:24:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class WildernessCourse extends AbstractAgilityCourse {

    private final Consumer<Player> completeConsumer = player -> player.getInventory().addItem(new Item(ItemId.BLOOD_MONEY, Utils.random(2, 6)));

    @Override
    public double getAdditionalCompletionXP() {
        return 499;
    }

    @Override
    public Consumer<Player> onComplete() {
        return completeConsumer;
    }

}

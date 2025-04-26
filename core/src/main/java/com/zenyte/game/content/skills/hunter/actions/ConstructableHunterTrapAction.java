package com.zenyte.game.content.skills.hunter.actions;

import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

import static com.zenyte.game.content.skills.hunter.node.TrapType.PITFALL;

/**
 * @author Kris | 31/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
abstract class ConstructableHunterTrapAction extends Action {
    private static final Animation animation = new Animation(5212);

    abstract TrapType[] trapTypes();

    boolean startLogsTrap(@NotNull final OptionalInt logsSlot) {
        if (HunterUtils.isAreaRestricted(player, PITFALL) || HunterUtils.isBelowRequiredLevel(player, PITFALL) || HunterUtils.hasMaxTrapsLaid(player)) {
            return false;
        }
        final OptionalInt logs = HunterUtils.findKnifeWithLogs(player);
        if (!logs.isPresent()) {
            return false;
        }
        player.stopAll();
        player.setAnimation(animation);
        player.sendFilteredMessage("You start setting up the trap..");
        if (logsSlot.isPresent()) {
            player.getInventory().set(logsSlot.getAsInt(), null);
        } else {
            player.getInventory().deleteItem(new Item(logs.getAsInt(), 1));
        }
        player.lock(3);
        delay(2);
        return true;
    }
}

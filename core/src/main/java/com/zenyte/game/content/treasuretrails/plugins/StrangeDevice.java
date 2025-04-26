package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.treasuretrails.DeviceTemperature;
import com.zenyte.game.content.treasuretrails.HotColdResult;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.plugins.dialogue.ItemChat;

import java.util.Optional;

/**
 * @author Kris | 10/04/2019 16:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StrangeDevice extends ItemPlugin {
    private static final SoundEffect sound = new SoundEffect(1202);

    @Override
    public void handle() {
        bind("Feel", (player, item, container, slotId) -> {
            final Optional<HotColdResult> result = TreasureTrail.getHotColdResult(player, item.getId() == ItemId.STRANGE_DEVICE_23183);
            if (!result.isPresent()) {
                player.getDialogueManager().start(new ItemChat(player, item, "The strange device is inactive."));
                return;
            }
            final HotColdResult res = result.get();
            final DeviceTemperature temperature = res.getTemperature();
            final Item clueItem = res.getClue();
            final int distance = res.getDistance();
            if (temperature == null) {
                player.getDialogueManager().start(new ItemChat(player, item, "The strange device is inactive."));
                return;
            }
            if (player.getNumericTemporaryAttribute("strange device use delay").longValue() > System.currentTimeMillis()) {
                return;
            }
            final Object lastDistance = clueItem.getAttribute("Hot Cold Distance");
            clueItem.setAttribute("Hot Cold Distance", distance);
            if (!(lastDistance instanceof Number) || temperature == DeviceTemperature.MASTER_VISIBLY_SHAKING || temperature == DeviceTemperature.BEGINNER_VISIBLY_SHAKING) {
                player.sendMessage(temperature.getMessage() + ".");
            } else {
                final int dist = ((Number) lastDistance).intValue();
                if (dist == distance) {
                    player.sendMessage(temperature.getMessage() + ", and the same temperature as last time.");
                } else {
                    player.sendMessage(temperature.getMessage() + ", " + (dist < distance ? "but" : "and") + " " + (dist < distance ? "colder than" : "warmer than") + " last time.");
                }
            }
            player.getTemporaryAttributes().put("strange device use delay", System.currentTimeMillis() + 300);
            player.sendSound(sound);
            if (item.getId() != ItemId.STRANGE_DEVICE_23183) {
                player.applyHit(new Hit(Utils.random(3, 8), HitType.DEFAULT));
                player.sendMessage("The power of the strange device hurts you in the process.");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.STRANGE_DEVICE, ItemId.STRANGE_DEVICE_23183};
    }
}

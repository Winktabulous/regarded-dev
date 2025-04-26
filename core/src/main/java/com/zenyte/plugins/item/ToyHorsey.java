package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;

/**
 * @author Kris | 15/05/2019 22:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ToyHorsey extends ItemPlugin {
    @Override
    public void handle() {
        bind("Play-with", (player, item, slotId) -> {
            final long emoteDelay = player.getNumericTemporaryAttribute("emote_delay").longValue();
            if (emoteDelay > Utils.currentTimeMillis()) {
                return;
            }
            final Animation animation = new Animation(918 + ((item.getId() - 2520) / 2));
            player.setAnimation(animation);
            player.addTemporaryAttribute("emote_delay", Utils.currentTimeMillis() + AnimationUtil.getCeiledDuration(animation));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {2520, 2522, 2524, 2526};
    }
}

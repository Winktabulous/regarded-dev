package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;

/**
 * @author Kris | 15/05/2019 23:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ToyFeline extends ItemPlugin {
    @Override
    public void handle() {
        bind("Play-with", (player, item, slotId) -> {
            final long emoteDelay = player.getNumericTemporaryAttribute("emote_delay").longValue();
            if (emoteDelay > Utils.currentTimeMillis()) {
                return;
            }
            final int id = item.getId();
            final Animation animation = new Animation(id == 13215 ? 3414 : id == 13216 ? 3413 : id == 13217 ? 3541 : 3839);
            player.setAnimation(animation);
            player.addTemporaryAttribute("emote_delay", Utils.currentTimeMillis() + AnimationUtil.getCeiledDuration(animation));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {13215, 13216, 13217, 13218};
    }
}

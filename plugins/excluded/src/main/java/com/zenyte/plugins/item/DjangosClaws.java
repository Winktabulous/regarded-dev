package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Kris | 15/05/2019 22:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DjangosClaws extends ItemPlugin {
    @Override
    public void handle() {
        bind("Emote", (player, item, slotId) -> {
            final long emoteDelay = player.getNumericTemporaryAttribute("emote_delay").longValue();
            if (emoteDelay > Utils.currentTimeMillis()) {
                return;
            }
            player.getAppearance().forceAppearance(EquipmentSlot.WEAPON.getSlot(), 13188);
            player.getAppearance().forceAppearance(EquipmentSlot.SHIELD.getSlot(), -1);
            final Animation animation = new Animation(7527, 10);
            player.setAnimation(animation);
            player.addTemporaryAttribute("emote_delay", Utils.currentTimeMillis() + AnimationUtil.getCeiledDuration(animation));
            WorldTasksManager.schedule(() -> player.getAppearance().clearForcedAppearance(), (AnimationUtil.getCeiledDuration(animation) / 600) - 1);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {13188};
    }
}

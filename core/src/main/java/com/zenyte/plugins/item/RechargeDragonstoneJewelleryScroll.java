package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.plugins.dialogue.ItemChat;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 18/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RechargeDragonstoneJewelleryScroll extends ItemPlugin {
    @Override
    public void handle() {
        bind("Read", (player, __, ___, slotId) -> {
            final Container[] containers = new Container[] {player.getInventory().getContainer(), player.getEquipment().getContainer()};
            int count = 0;
            int firstSuccessfulItem = -1;
            for (final Container ct : containers) {
                for (int i = ct.getContainerSize(); i >= 0; i--) {
                    final Item containerItem = ct.get(i);
                    if (containerItem == null) {
                        continue;
                    }
                    if (rechargeDragonstoneItem(containerItem)) {
                        count++;
                        if (firstSuccessfulItem == -1) {
                            firstSuccessfulItem = containerItem.getId();
                        }
                    }
                }
                ct.setFullUpdate(true);
                ct.refresh(player);
            }
            if (count > 0) {
                player.getInventory().deleteItem(new Item(ItemId.CHARGE_DRAGONSTONE_JEWELLERY_SCROLL, 1));
                player.sendMessage("You read the scroll...");
                player.getDialogueManager().start(new ItemChat(player, new Item(firstSuccessfulItem), "Your scroll disintegrates as it recharges your jewellery."));
            } else {
                player.sendMessage("You don't have any jewellery that the scroll can recharge.");
            }
        });
    }

    private final boolean rechargeDragonstoneItem(@NotNull final Item item) {
        final int id = item.getId();
        final boolean isUnchargedGlory = ArrayUtils.contains(BASIC_GLORIES, id);
        final boolean isTrimmedUnchargedGlory = ArrayUtils.contains(TRIMMED_GLORIES, id);
        final boolean isSkillsNecklace = ArrayUtils.contains(SKILLS_NECKLACES, id);
        final boolean isCombatBracelet = ArrayUtils.contains(COMBAT_BRACELETS, id);
        if (isUnchargedGlory || isTrimmedUnchargedGlory || isSkillsNecklace || isCombatBracelet) {
            item.setId(isSkillsNecklace ? CHARGED_SKILLS_NECKLACE : isCombatBracelet ? CHARGED_COMBAT_BRACELET : isUnchargedGlory ? BASIC_CHARGED_GLORY : TRIMMED_CHARGED_GLORY);
            return true;
        }
        return false;
    }

    private static final int BASIC_CHARGED_GLORY = 1712;
    private static final int TRIMMED_CHARGED_GLORY = 10354;
    private static final int CHARGED_SKILLS_NECKLACE = 11105;
    private static final int CHARGED_COMBAT_BRACELET = 11118;
    private static final int[] BASIC_GLORIES = new int[] {1704, 1706, 1708, 1710};
    private static final int[] TRIMMED_GLORIES = new int[] {10362, 10360, 10358, 10356};
    private static final int[] SKILLS_NECKLACES = new int[] {11113, 11111, 11109, 11107};
    private static final int[] COMBAT_BRACELETS = new int[] {11126, 11124, 11122, 11120};

    @Override
    public int[] getItems() {
        return new int[] {ItemId.CHARGE_DRAGONSTONE_JEWELLERY_SCROLL};
    }
}

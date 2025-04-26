package com.zenyte.game.content.skills.fletching.composite;

import com.google.common.collect.Lists;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

/**
 * @author Christopher
 * @since 3/20/2020
 */
public class CompOgreBowItemOnItemAction implements ItemOnItemAction {
    public static final int LEVEL_REQ = 30;
    public static final IntList unstrungMats = new IntArrayList(Lists.newArrayList(ItemId.ACHEY_TREE_LOGS, ItemId.WOLF_BONES));
    public static final IntList strungMats = new IntArrayList(Lists.newArrayList(ItemId.UNSTRUNG_COMP_BOW, ItemId.BOW_STRING));
    public static final Item unstrungBow = new Item(ItemId.UNSTRUNG_COMP_BOW);
    public static final Item strungBow = new Item(ItemId.COMP_OGRE_BOW);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (!player.getSkills().checkLevel(SkillConstants.FLETCHING, LEVEL_REQ, "do this")) {
            return;
        }
        if (!player.getInventory().containsAll(strungMats)) {
            player.sendMessage("You do not have the required items to do this.");
            return;
        }
        player.getDialogueManager().start(new CompOgreBowCreationD(player, strungBow));
    }

    @Override
    public int[] getItems() {
        final ArrayList<Integer> items = new ArrayList<Integer>(strungMats);
        return ArrayUtils.toPrimitive(items.toArray(new Integer[0]));
    }
}

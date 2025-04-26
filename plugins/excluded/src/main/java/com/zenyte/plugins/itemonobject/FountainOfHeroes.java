package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 12/05/2019 14:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FountainOfHeroes implements ItemOnObjectAction {

    private static final Animation ANIM = new Animation(832);

    private static final int BASIC_CHARGED_GLORY = 1712;

    private static final int TRIMMED_CHARGED_GLORY = 10354;

    private static final int[] BASIC_GLORIES = new int[] { 1704, 1706, 1708, 1710 };

    private static final int[] TRIMMED_GLORIES = new int[] { 10362, 10360, 10358, 10356 };

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        player.lock(1);
        player.setAnimation(ANIM);
        WorldTasksManager.schedule(() -> {
            final Container container = player.getInventory().getContainer();
            container.setFullUpdate(true);
            for (int i = container.getContainerSize(); i >= 0; i--) {
                final Item containerItem = container.get(i);
                if (containerItem == null) {
                    continue;
                }
                final int id = containerItem.getId();
                final boolean isUnchargedGlory = ArrayUtils.contains(BASIC_GLORIES, id);
                final boolean isTrimmedUnchargedGlory = ArrayUtils.contains(TRIMMED_GLORIES, id);
                if (isUnchargedGlory || isTrimmedUnchargedGlory) {
                    containerItem.setId(isUnchargedGlory ? BASIC_CHARGED_GLORY : TRIMMED_CHARGED_GLORY);
                }
            }
            container.refresh(player);
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    item(item, "You feel a power emanating from the fountain as it recharges your jewellery.");
                }
            });
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1704, 1706, 1708, 1710, 10362, 10360, 10358, 10356 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 2939 };
    }
}

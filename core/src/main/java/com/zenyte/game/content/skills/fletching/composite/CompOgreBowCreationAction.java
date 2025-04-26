package com.zenyte.game.content.skills.fletching.composite;

import com.zenyte.game.content.skills.fletching.FletchingDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.IntList;

/**
 * @author Christopher
 * @since 3/20/2020
 */
public class CompOgreBowCreationAction extends Action {
    private static final double EXPERIENCE = 45;
    private static final Animation stringingAnim = new Animation(6685);
    private final int amount;
    private final boolean isStringing;
    private int cycles;
    private int ticks;

    @Override
    public boolean start() {
        return check();
    }

    @Override
    public boolean process() {
        return cycles < amount && check();
    }

    @Override
    public int processWithDelay() {
        if (ticks == 0) {
            player.setAnimation(isStringing ? stringingAnim : FletchingDefinitions.ANIMATION);
        } else if (ticks == 2) {
            final IntList mats = isStringing ? CompOgreBowItemOnItemAction.strungMats : CompOgreBowItemOnItemAction.unstrungMats;
            final Item product = isStringing ? CompOgreBowItemOnItemAction.strungBow : CompOgreBowItemOnItemAction.unstrungBow;
            player.getInventory().deleteItemsIfContains(mats, () -> {
                player.getInventory().addOrDrop(product);
                player.getSkills().addXp(SkillConstants.FLETCHING, EXPERIENCE);
                final String itemString = isStringing ? "a composite ogre bow" : "an unstrung composite bow";
                player.sendFilteredMessage("You carefully cut the wood into " + itemString + ".");
                cycles++;
            });
            return ticks = 0;
        }
        ticks++;
        return 0;
    }

    private boolean check() {
        if (!player.getSkills().checkLevel(SkillConstants.FLETCHING, CompOgreBowItemOnItemAction.LEVEL_REQ, "do this")) {
            return false;
        }
        final IntList mats = isStringing ? CompOgreBowItemOnItemAction.strungMats : CompOgreBowItemOnItemAction.unstrungMats;
        if (!player.getInventory().containsAll(mats)) {
            player.sendMessage("You don't have all the required items.");
            return false;
        }
        return true;
    }

    public CompOgreBowCreationAction(int amount, boolean isStringing) {
        this.amount = amount;
        this.isStringing = isStringing;
    }
}

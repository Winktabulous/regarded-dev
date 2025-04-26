package com.zenyte.game.content.skills.fletching.actions;

import com.zenyte.game.content.skills.fletching.FletchingDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Christopher
 * @since 3/20/2020
 */
public class OgreAmmunitionFletching extends Action {
    private final FletchingDefinitions.AmmunitionFletchingData data;
    private final int amount;
    private int cycles;

    public OgreAmmunitionFletching(final FletchingDefinitions.AmmunitionFletchingData data, final int amount) {
        this.data = data;
        this.amount = amount;
    }

    @Override
    public boolean start() {
        return check();
    }

    @Override
    public boolean process() {
        return check();
    }

    @Override
    public int processWithDelay() {
        if (data == FletchingDefinitions.AmmunitionFletchingData.WOLFBONE_TIPS) {
            player.sendFilteredMessage("You fletch the wolf bone into wolfbone tips.");
        } else {
            player.sendFilteredMessage("You fletch the logs into ogre arrow shafts.");
        }
        player.getSkills().addXp(SkillConstants.FLETCHING, (data.getXp() * data.getProduct().getAmount()));
        for (final Item item : data.getMaterials()) {
            player.getInventory().deleteItem(new Item(item.getId(), item.getAmount()));
        }
        if (data == FletchingDefinitions.AmmunitionFletchingData.OGRE_ARROW_SHAFT || data == FletchingDefinitions.AmmunitionFletchingData.WOLFBONE_TIPS) {
            player.getInventory().addOrDrop(new Item(data.getProduct().getId(), Utils.random(2, 6)));
        } else {
            player.getInventory().addOrDrop(new Item(data.getProduct().getId(), data.getProduct().getAmount()));
        }
        cycles++;
        return 1;
    }

    private boolean check() {
        if (cycles >= amount) {
            return false;
        }
        if (!player.getSkills().checkLevel(SkillConstants.FLETCHING, data.getLevel(), "do that")) {
            return false;
        }
        for (final Item item : data.getMaterials()) {
            if (!player.getInventory().containsItem(new Item(item.getId(), item.getAmount()))) {
                player.sendMessage("You don't have the necessary items to do this.");
                return false;
            }
        }
        if (!player.getInventory().hasFreeSlots() && (!data.getProduct().isStackable() || !player.getInventory().containsItem(data.getProduct().getId(), 1))) {
            player.sendMessage("You need some more free inventory space to do this.");
            return false;
        }
        return true;
    }
}

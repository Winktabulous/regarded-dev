package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.cooking.CookingDefinitions;
import com.zenyte.game.content.skills.cooking.CookingDefinitions.CookingData;
import com.zenyte.game.content.skills.cooking.actions.Cooking;
import com.zenyte.game.content.skills.cooking.actions.CookingCombo;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SkillDialogue;
import com.zenyte.plugins.itemonitem.CookingComboItemAction.CookingCombination;
import mgi.types.config.items.ItemDefinitions;

import java.util.List;

public class CookingD extends SkillDialogue {
    private final WorldObject object;
    private boolean leftClick;
    private List<CookingData> cookableList;
    private List<CookingCombination> combo;

    public CookingD(final Player player, final WorldObject object, final boolean leftClick, final List<CookingData> cookableList, final Item... items) {
        super(player, "What would you like to cook?", items);
        this.object = object;
        this.leftClick = leftClick;
        this.cookableList = cookableList;
    }

    public CookingD(final Player player, final List<CookingCombination> combo, final Item... items) {
        super(player, "What would you like to cook?", items);
        object = null;
        this.combo = combo;
    }

    @Override
    public void run(final int slotId, final int amount) {
        if (slotId < 0 || slotId >= items.length) {
            return;
        }
        final CookingDefinitions.CookingData cookable = cookableList == null ? CookingData.getDataByProduct(items[slotId]) : cookableList.get(slotId);
        if (cookable != null) {
            if (!leftClick && (cookable == CookingData.KARAMBWAN || cookable == CookingData.POISON_KARAMBWAN)) {
                player.getActionManager().setActionDelay(-1);
            }
            player.getActionManager().setAction(new Cooking(cookable, amount, object));
            return;
        }
        if (object == null) {
            final CookingCombination comboElement = combo.get(slotId);
            if (CookingCombination.CHOPPED.contains(comboElement)) {
                if (!player.getInventory().containsItem(946, 1)) {
                    player.sendMessage("You need a knife to chop up the " + ItemDefinitions.getOrThrow(comboElement.getRaw()[0]).getName().toLowerCase() + ".");
                    return;
                }
                if (!player.getInventory().containsItem(1923, 1) && !combo.toString().equalsIgnoreCase("chilli_con_carne")) {
                    player.sendMessage("You need a bowl to hold the " + comboElement.getProduct().getName().toLowerCase() + ".");
                    return;
                }
            }
            if (comboElement != null) {
                if (player.getSkills().getLevel(SkillConstants.COOKING) >= comboElement.getLevel()) {
                    if (comboElement.getProduct().getAmount() > 1 && player.getInventory().getFreeSlots() < comboElement.getProduct().getAmount() - 1) player.sendMessage("You need at least " + (comboElement.getProduct().getAmount() - 1) + " free slots to cut this!");
                     else player.getActionManager().setAction(new CookingCombo(comboElement, amount));
                } else {
                    player.sendMessage("You need at least level " + comboElement.getLevel() + " Cooking to make this!");
                }
            }
        }
    }
}

package com.zenyte.game.content.skills.cooking.actions;

import com.zenyte.game.model.item.enums.ContainerItem;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.itemonitem.CookingComboItemAction.CookingCombination;

public class CookingCombo extends Action {

	private final CookingCombination combo;
	private final int amount;
	private int cycles;

	public CookingCombo(CookingCombination combo, int amount) {
		this.combo = combo;
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
		for (int raw : combo.getRaw()) {
			if (raw == 946)
				continue;

			if (!combo.isConsume() && ContainerItem.all.containsKey(raw))
				player.getInventory().replaceItem(ContainerItem.all.get(raw).getType().getEmpty().getId(), 1, player.getInventory().getContainer().getSlotOf(raw));
			else
				player.getInventory().deleteItem(raw, 1);
		}

		if (combo == CookingCombination.WATERMELON_SLICE)
			player.setAnimation(Animation.CUT_WATERMELON);

		player.getInventory().addItem(combo.getProduct());
		player.getSkills().addXp(SkillConstants.COOKING, combo.getXp());
		player.sendFilteredMessage(combo.getMessage());
		cycles++;
		return combo.getDelay();
	}

	public boolean check() {
		if (cycles >= amount) {
			return false;
		}
		return combo.hasRequirements(player);
	}

}

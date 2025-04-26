package com.zenyte.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.testinterfaces.ExperienceLampInterface;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.utils.TextUtils;

import java.util.function.IntConsumer;

import static com.zenyte.game.GameInterface.EXPERIENCE_LAMP;

public class DarkRelic extends ItemPlugin {


	@Override
	public void handle() {
		bind("Commune", (player, item, slotId) -> {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					item(new Item(ItemId.DARK_RELIC), "A dark power emanates from the relic.<br>You sense that this power can be directed.");
					setOnCloseRunnable(() -> {
						player.getTemporaryAttributes().put("experience_lamp_custom_handler", (IntConsumer) id -> {
							final Object object = player.getTemporaryAttributes().get("experience_lamp_info");
							if (!(object instanceof Object[])) {
								return;
							}
							Object[] args = (Object[]) object;
							final int minimumLevel = (int) args[1];
							final int slotId = (int) args[2];
							final Item item = (Item) args[3];
							if (player.getInventory().getItem(slotId) != item) {
								return;
							}
							if (id < 0) {
								player.sendMessage("You haven't selected a skill.");
								return;
							}
							if (player.getSkills().getLevelForXp(id) < minimumLevel) {
								player.sendMessage("You need to select a skill that is at least level " + minimumLevel + ".");
								return;
							}
							int mod = Skills.isCombatSkill(id) || id == SkillConstants.MINING || id == SkillConstants.WOODCUTTING || id == SkillConstants.HERBLORE || id == SkillConstants.FARMING || id == SkillConstants.HUNTER || id == SkillConstants.COOKING || id == SkillConstants.FISHING ? 150 : 50;
							int experience = player.getSkills().getLevelForXp(id) * mod;
							player.getInventory().set(slotId, null);
							player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
							player.getSkills().addXp(id, experience);
							player.getPacketDispatcher().sendSoundEffect(ExperienceLampInterface.SOUND_EFFECT);
							player.getDialogueManager().start(new ItemChat(player, new Item(ItemId.DARK_RELIC), "The relic offers you " + TextUtils.formatCurrency(experience * player.getExperienceRate(id)) + " XP in " + Skills.getSkillName(id) + "."));
						});
						player.getTemporaryAttributes().put("experience_lamp_info", new Object[] {1, 1, slotId, item});
						EXPERIENCE_LAMP.open(player);
					});
				}
			});
		});
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.DARK_RELIC};
	}

}

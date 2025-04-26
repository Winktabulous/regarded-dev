package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;

public class LevelUpMessage implements Message {

	public LevelUpMessage(final int skill) {
		this.skill = skill;
	}
	
	private static final int[] SKILL_IDS = new int[] { 5, 16, 46, 29, 37, 35, 31, 11, 50, 24, 22, 20, 13, 44, 33, 27, 3, 48, 42, 18, 40,
			-1, 8 };
	
	private final int skill;

	@Override
	public void display(final Player player) {
		player.getInterfaceHandler().closeInterface(InterfacePosition.DIALOGUE);
		if (skill == SkillConstants.HUNTER) {
			new ItemMessage(9951,
					"<col=000080>Congratulations, you've just advanced a Hunter level.<col=000000><br><br>Your Hunter level is now "
					+ player.getSkills().getLevelForXp(skill) + ".").display(player);
			return;
		}
    	player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, 233);
		player.getPacketDispatcher().sendComponentText(233, 1,
				"<col=000080>Congratulations, you just advanced a " + Skills.getSkillName(skill) + " level.");
		player.getPacketDispatcher().sendComponentText(233, 2,
				"Your " + Skills.getSkillName(skill) + " level is now " + player.getSkills().getLevelForXp(skill) + ".");
		player.getPacketDispatcher().sendComponentVisibility(233, SKILL_IDS[skill] + 1, false);
        player.getPacketDispatcher().sendComponentText(233, 3, continueMessage(player));
	}

}

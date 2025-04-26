package com.zenyte.game.world.entity.player.dialogue.impl;

import com.zenyte.game.GameConstants;
import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh | 9 mrt. 2018 : 15:21:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class Hans extends Dialogue {
	public Hans(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final int seconds = (int) (player.getVariables().getPlayTime() * 0.6);
		final int days = seconds / 86400;
		final int hours = (seconds / 3600) - (days * 24);
		final int minutes = (seconds / 60) - (days * 1440) - (hours * 60);
		npc("Hello. What are you doing here?");
		options(TITLE, "I'm looking for whoever is in charge of this place", "I have come to kill everyone in this castle!", "I don't know. I'm lost. Where am I?", "Can you tell me how long I've been here?", "Nothing.").onOptionOne(() -> setKey(3)).onOptionTwo(() -> setKey(6)).onOptionThree(() -> setKey(9)).onOptionFour(() -> setKey(12)).onOptionFive(() -> setKey(16));
		player(3, "I'm looking for whoever is in charge of this place.");
		npc("Who, the Duke? He's in his study, on the first floor.");
		player(6, "I have to come to kill everyone in this castle!");
		player(9, "I don't know. I'm lost. Where am I?");
		npc("You are in Lumbridge Castle.");
		player(12, "Can you tell me how long I've been here?");
		npc("Ahh, I see all the newcomers arriving in Lumbridge,<br>fresh-faced and eager for adventure. I remember you...").executeAction(() -> player.getAchievementDiaries().update(LumbridgeDiary.LEARN_YOUR_AGE));
		npc("You've spent " + days + (days > 1 || days == 0 ? " days" : " day") + ", " + hours + (hours > 1 || hours == 0 ? " hours" : " hour") + ", " + minutes + (minutes > 1 || minutes == 0 ? " minutes" : " minute") + " in the world since you arrived in " + GameConstants.SERVER_NAME + ", " + StringFormatUtil.format(player.getPlayerInformation().getDaysSinceRegistry()) + " days ago.");
		player(16, "Nothing.");
	}
}

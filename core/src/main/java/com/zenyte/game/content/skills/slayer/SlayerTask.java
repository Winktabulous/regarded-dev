package com.zenyte.game.content.skills.slayer;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

import java.util.function.Predicate;

/**
 * @author Kris | 7. aug 2018 : 16:42:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface SlayerTask {
	
	boolean validate(final String name, final NPC npc);
	
	float getExperience(final NPC npc);
	
	int getTaskId();
	
	String[] getMonsters();

	int[] getMonsterIds();

	int getSlayerRequirement();
	
	String getTip();
	
	Predicate<Player> getPredicate();
	
	String getTaskName();
	
	String getEnumName();
	
}

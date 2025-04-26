package com.zenyte.game.content.skills.runecrafting.abyss;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 29 jul. 2018 | 22:28:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface ObstacleScript {
	
	void handle(final Player player, final WorldObject object, final Location destination);

}

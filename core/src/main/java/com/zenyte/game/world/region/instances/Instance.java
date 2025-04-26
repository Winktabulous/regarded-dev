package com.zenyte.game.world.region.instances;

import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 23. jaan 2018 : 20:17.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public interface Instance {
	
	int getMaxPlayers();

	int getMinimumCombat();

	InstanceSpeed getSpawnSpeed();

	InstanceProtection getProtection();

	Location getEntranceLocation();
	
	Location getExitLocation();
	
	void process();
	
}

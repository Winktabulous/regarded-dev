package com.zenyte.game.world.entity.player.login;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 08/05/2019 01:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public record CachedEntry(long time, Player cachedAccount) {

}

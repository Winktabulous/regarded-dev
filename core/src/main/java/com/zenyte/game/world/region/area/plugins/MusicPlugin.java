package com.zenyte.game.world.region.area.plugins;


import com.zenyte.game.model.music.Music;
import com.zenyte.game.world.entity.player.Player;

import java.util.Set;

/**
 * @author Savions.
 */
public interface MusicPlugin {

    Set<Music> getMusics(Player player);
}

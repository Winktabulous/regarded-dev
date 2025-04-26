package com.zenyte.game.content.treasuretrails.npcs.drops;

import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 22/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface ClueNPC {

    String monsterName();
    double rate();

    boolean accept(@NotNull final NPCDefinitions definitions);

}

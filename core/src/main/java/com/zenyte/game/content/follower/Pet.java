package com.zenyte.game.content.follower;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 23-11-2018 | 18:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface Pet {

    int itemId();
    int petId();
    boolean hasPet(Player player);
    Class<? extends Dialogue> dialogue();
    boolean roll(Player player, int rarity);
    String petName();

}

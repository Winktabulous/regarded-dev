package com.zenyte.game.content.minigame.duelarena;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;

import java.util.Optional;

/**
 * @author Tommeh | 28-11-2018 | 22:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DuelContainer extends Container {

    private final Player player;

    public DuelContainer(ContainerPolicy policy, ContainerType type, Optional<Player> player) {
        super(policy, type, player);
        this.player = player.get();
    }
}

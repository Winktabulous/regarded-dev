package com.zenyte.game.packet.in.event;

import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Tommeh | 25-1-2019 | 21:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MapBuildCompleteEvent implements ClientProtEvent {
    @Override
    public void log(@NotNull final Player player) {
        log(player, "");
    }

    @Override
    public void handle(Player player) {
        player.setLoadingRegion(false);
        World.getNPCs().stream()
            .filter(Objects::nonNull)
            .filter(NPC::hasOverheadActive)
            .forEach(demon -> demon.getUpdateFlags().set(UpdateFlag.NPC_PRAYER_OVERHEAD, true));
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }
}

package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TimeUnit;

/**
 * @author Kris | 25/10/2018 17:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MinigameStructure extends HomeStructure {
    @Override
    public boolean isTeleportPrevented(final Player player, final Teleport teleport) {
        if (player.inArea("Duel arena")) {
            player.sendMessage("You cannot use minigames teleports from duel arena.");
            return true;
        }
        if (teleport.getDestination() == null) {
            player.sendMessage("This activity does not have a teleport.");
            return true;
        }
        final long currentTime = Utils.currentTimeMillis();
        final long lastTeleport = player.getNumericAttribute("LAST_MINIGAME_TELEPORT").longValue();
        if (lastTeleport > currentTime) {
            final long milliseconds = lastTeleport - currentTime;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
            if (milliseconds % TimeUnit.MINUTES.toMillis(1) != 0) minutes++;
            player.sendMessage("You have to wait " + minutes + " minutes to use minigames teleports again.");
            return true;
        }
        return false;
    }
}

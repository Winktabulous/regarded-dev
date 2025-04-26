package com.zenyte.game.content.skills.magic.spells.teleports;

import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.world.region.area.wilderness.WildernessArea.isWithinWilderness;

/**
 * @author Kris | 19/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ForceTeleport implements Teleport {

    public ForceTeleport(@NotNull final Location location) {
        this.location = location;
    }

    private final Location location;

    @Override
    public TeleportType getType() {
        return TeleportType.INSTANT_UNSAFE;
    }

    @Override
    public Location getDestination() {
        return location;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public double getExperience() {
        return 0;
    }

    @Override
    public int getRandomizationDistance() {
        return 0;
    }

    @Override
    public Item[] getRunes() {
        return null;
    }

    @Override
    public int getWildernessLevel() {
        return 0;
    }

    @Override
    public boolean isCombatRestricted() {
        return false;
    }

    @Override
    public void teleport(final Player player) {
        try {
            getType().getStructure().teleport(player, this);
        } catch (final Exception e) {
            Magic.logger.error("", e);
        }
    }
}

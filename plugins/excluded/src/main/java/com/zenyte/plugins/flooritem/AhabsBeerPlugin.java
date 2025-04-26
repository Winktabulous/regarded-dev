package com.zenyte.plugins.flooritem;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 4. juuni 2018 : 01:30:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AhabsBeerPlugin implements FloorItemPlugin {

	private static final ForceTalk TALK = new ForceTalk("Oy matey, leave my beer alone!");
	private static final Location AHABS_LOC = new Location(3049, 3256, 0);

	@Override
	public void handle(final Player player, final FloorItem item, final int optionId, final String option) {
		if (option.equalsIgnoreCase("take")) {
		    World.findNPC(1337, AHABS_LOC).ifPresent(ahab -> ahab.setForceTalk(TALK));
			player.sendMessage("Ahab doesn't allow you to take his beer.");
        }
	}

	@Override
    public boolean canTelegrab(@NotNull final Player player, @NotNull final FloorItem item) {
        World.findNPC(1337, AHABS_LOC).ifPresent(ahab -> ahab.setForceTalk(TALK));
        player.sendMessage("Ahab doesn't allow you to take his beer.");
        return false;
    }

	@Override
	public boolean overrideTake() {
		return true;
	}

	@Override
	public int[] getItems() {
		return new int[] { 6561 };
	}

}

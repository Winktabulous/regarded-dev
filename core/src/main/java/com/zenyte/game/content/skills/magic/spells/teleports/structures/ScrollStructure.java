package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.itemtransportation.masterscrolls.ScrollBookTeleport;
import com.zenyte.game.content.itemtransportation.masterscrolls.TeleportScroll;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 16. juuli 2018 : 22:48:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ScrollStructure implements TeleportStructure {
	private static final SoundEffect SOUND = new SoundEffect(200, 1, 10);
	private static final Animation ANIM = new Animation(3864);
	private static final Graphics GFX = new Graphics(1039);

	@Override
	public Animation getStartAnimation() {
		return ANIM;
	}

	@Override
	public Graphics getStartGraphics() {
		return GFX;
	}

	@Override
	public void start(final Player player, final Teleport teleport) {
		TeleportStructure.super.start(player, teleport);
		World.sendSoundEffect(player, SOUND);
	}

	@Override
	public void teleport(final Player player, final Teleport teleport) {
		final boolean itemTeleport = player.getTemporaryAttributes().remove("master scroll book teleport") == null;
		if (isTeleportPrevented(player, teleport) || isAreaPrevented(player, teleport) || isTeleblockRestricted(player) || isRestricted(player, teleport)) {
			return;
		}
		if (itemTeleport) {
			final SpellState state = new SpellState(player, teleport.getLevel(), teleport.getRunes());
			if (!state.check()) {
				return;
			}
			state.remove();
		} else {
			if (teleport instanceof TeleportScroll) {
				ScrollBookTeleport.decrementCountInBook(player, ((TeleportScroll) teleport));
			}
		}
		player.stopAll();
		start(player, teleport);
	}
}

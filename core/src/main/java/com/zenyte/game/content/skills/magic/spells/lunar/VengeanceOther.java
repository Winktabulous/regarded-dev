package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.minigame.castlewars.CastleWarsArea;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.NPCSpell;
import com.zenyte.game.content.skills.magic.spells.PlayerSpell;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;

/**
 * @author Kris | 19. veebr 2018 : 0:00.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class VengeanceOther implements PlayerSpell, NPCSpell {
	private static final Graphics GFX = new Graphics(725, 0, 92);
	private static final Animation ANIM = new Animation(4411);
	private static final SoundEffect sound = new SoundEffect(2908, 10, 66);

	@Override
	public int getDelay() {
		return 4000;
	}

	@Override
	public boolean spellEffect(final Player player, final Player target) {
		if (!hasDefenceRequirement(player)) {
			return false;
		}
		player.faceEntity(target);
		if (target.isDead() || !target.isInitialized() || target.isFinished() || target.isLocked()) {
			player.sendMessage("The other player is busy.");
			return false;
		}
		if (player.inArea(CastleWarsArea.class)) {
			player.sendMessage("You cannot cast vengeance other in Castle-Wars.");
			return false;
		}
		if (target.getVarManager().getBitValue(SettingVariables.ACCEPT_AID_VARBIT_ID) != 1) {
			player.sendMessage("The other player is not accepting aid.");
			return false;
		}
		if (target.getDuel() != null) {
			player.sendMessage("You cannot cast lunar spells on players within duels.");
			return false;
		}
		final int vengDelay = player.getVariables().getTime(TickVariable.VENGEANCE);
		if (vengDelay > 0) {
			final int seconds = (int) Math.ceil(vengDelay * 0.6F);
			player.sendMessage("You need to wait another " + Math.min(30, seconds) + " second" + (seconds == 1 ? "" : "s") + " to cast a vengeance.");
			return false;
		}
		player.getVarManager().sendBit(2451, 1);
		this.addXp(player, 108);
		target.getAttributes().put("vengeance", true);
		player.getVariables().schedule(50, TickVariable.VENGEANCE);
		player.getVarManager().sendBit(2451, 1);
		target.sendMessage("You have the power of vengeance!");
		target.setGraphics(GFX);
		player.setAnimation(ANIM);
		World.sendSoundEffect(target, sound);
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

	@Override
	public boolean spellEffect(final Player player, final NPC npc) {
		player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> {
			player.sendMessage("Nothing interesting happens.");
		}, false));
		return false;
	}
}

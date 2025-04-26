package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.MovementLock;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 17. veebr 2018 : 15:13.01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Dream implements DefaultSpell {

	private static final Animation SLEEP_ANIM = new Animation(7627);
	private static final Animation WAKE_ANIM = new Animation(7628);
	private static final RenderAnimation RENDER = new RenderAnimation(6296, -1, -1);
	private static final Graphics GFX = new Graphics(277);//1056, 0, 20

	@Override
	public int getDelay() {
		return 3000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (player.isLogoutPrevented()) {
			player.sendMessage("You can't cast dream until 10 seconds after the end of combat.");
			return false;
		} else if (player.getHitpoints() >= player.getMaxHitpoints()) {
			player.sendMessage("You have no need to cast this spell since your hitpoints are already full.");
			return false;
		}
		this.addXp(player, 82);
		player.getActionManager().setAction(new DreamAction());
		return true;
	}
	
	private static final class DreamAction extends Action {

		private boolean isRenderSet;

		private int ticks;
		
		@Override
		public boolean start() {
			player.setAnimation(SLEEP_ANIM);
			delay(6);
			return true;
		}

		@Override
		public boolean process() {
		    if (++ticks % 34 == 0) {
		        player.heal(1);
            }
            return player.getHitpoints() < player.getMaxHitpoints();
        }

		@Override
		public int processWithDelay() {
			if (!isRenderSet) {
				isRenderSet = true;
				player.getAppearance().setRenderAnimation(RENDER);
			}
			player.setGraphics(GFX);
			return 3;
		}
		
		@Override
		public void stop() {
			player.lock(2);
			player.addMovementLock(new MovementLock(System.currentTimeMillis() + 1200));
			if (player.hasWalkSteps()) {
				player.resetWalkSteps();
			}
			player.getAppearance().resetRenderAnimation();
			player.setAnimation(WAKE_ANIM);
		}
		
	}
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

}

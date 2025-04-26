package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 17. veebr 2018 : 2:23.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SpinFlax implements DefaultSpell {

	private static final Animation ANIM = new Animation(4413);
	private static final Graphics GFX = new Graphics(729, 0, 92);
	private static final SoundEffect sound = new SoundEffect(3615);

	@Override
	public int getDelay() {
		return 3000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (!player.getInventory().containsItem(1779, 1)) {
			player.sendMessage("You have no flax to turn to bow strings.");
			return false;
		}
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		player.sendSound(sound);
        final Inventory inventory = player.getInventory();
        int count = 0;
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null || item.getId() != 1779) {
                continue;
            }
            item.setId(1777);
            if (++count == 5) {
                break;
            }
        }
        player.getInventory().refreshAll();
        this.addXp(player, 75);
        this.addXp(player, SkillConstants.CRAFTING, 15 * count);
		return true;
	}
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

}

package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.consumables.drinks.Potion;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.ItemSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * @author Kris | 17. veebr 2018 : 17:46.22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BoostPotionShare implements ItemSpell {
	private static final Potion[] VALID_POTIONS = new Potion[] {Potion.ATTACK_POTION, Potion.SUPER_ATTACK_POTION, Potion.STRENGTH_POTION, Potion.SUPER_STRENGTH_POTION, Potion.DEFENCE_POTION, Potion.SUPER_DEFENCE_POTION, Potion.MAGIC_ESSENCE, Potion.MAGIC_POTION, Potion.SUPER_MAGIC_POTION, Potion.RANGING_POTION, Potion.SUPER_RANGING, Potion.SUPER_COMBAT_POTION};
	private static final Graphics GFX = new Graphics(733, 0, 92);
	private static final Animation ANIM = new Animation(4413);
	private static final SoundEffect playerSound = new SoundEffect(2901, 10, 35);
	private static final SoundEffect otherSound = new SoundEffect(2904, 10, 35);

	@Override
	public int getDelay() {
		return 3000;
	}

	@Override
	public boolean spellEffect(final Player player, final Item item, final int slot) {
		final Consumable consumable = Consumable.consumables.get(item.getId());
		if (!(consumable instanceof Potion) || !ArrayUtils.contains(VALID_POTIONS, consumable)) {
			player.getInterfaceHandler().openGameTab(GameTab.SPELLBOOK_TAB);
			player.sendMessage("You can only cast this spell on stat boosting potions.");
			return false;
		}
		final Potion potion = (Potion) consumable;
		int count = 0;
		final int doses = potion.getDoses(item.getId());
		final List<Player> characters = player.findCharacters(1, Player.class, p2 -> p2 != player && !p2.isDead() && p2.getVarManager().getBitValue(SettingVariables.ACCEPT_AID_VARBIT_ID) == 1 && p2.getDuel() == null);
		for (int i = characters.size() - 1; i >= 0; i--) {
			final Player p2 = characters.get(i);
			if (count >= doses) {
				break;
			}
			count++;
			World.sendSoundEffect(p2, otherSound);
			p2.setGraphics(GFX);
			potion.applyEffects(p2);
			p2.sendMessage(player.getPlayerInformation().getDisplayname() + " has just shared the effects of " + potion.toString().toLowerCase().replaceAll("_", " ") + " with you.");
		}
		if (count == 0) {
			player.sendMessage("There are no players with whom you can share the potion around you.");
			return false;
		}
		World.sendSoundEffect(player, playerSound);
		potion.applyEffects(player);
		player.setGraphics(GFX);
		player.setAnimation(ANIM);
		this.addXp(player, 88);
		final int dosesLeft = doses - count;
		final Item leftover = potion.getItem(dosesLeft);
		player.getInventory().set(slot, leftover);
		player.getInventory().refresh(slot);
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
}

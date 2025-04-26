package com.zenyte.game.content.skills.smithing;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.boons.impl.MasterOfTheCraft;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 10 jun. 2018 | 16:12:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class CannonballSmithing extends Action {

    private static final int PRODUCT_MULTIPLIER = 2;
	private static final int LEVEL_REQ = 35;
	private static final double XP = 25.6;
	public static final Item MATERIAL = new Item(2353);
	public static final Item PRODUCT = new Item(2, 4 * PRODUCT_MULTIPLIER);
	public static final Item MOULD = new Item(4);

	private final int amount;
	private int cycle, ticks;

    public CannonballSmithing(int amount) {
        this.amount = amount;
    }

	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(MOULD)) {
			player.sendMessage("You need an ammo mould to make cannonballs.");
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.SMITHING) < LEVEL_REQ) {
			player.getDialogueManager().start(new PlainChat(player, "You need a Smithing level of at least " + LEVEL_REQ + " to smith cannonballs."));
			return false;
		}
		return true;
    }

    @Override
    public boolean process() {
        if (!player.getInventory().containsItem(MATERIAL)) {
            return false;
        }
        return cycle < amount;
    }

    @Override
    public int processWithDelay() {
        int actionDelay = player.getBoonManager().hasBoon(MasterOfTheCraft.class) ? 1 : 3;
        if (ticks == 0) {
            player.sendFilteredMessage("You heat the steel bar into a liquid state.");
            player.setAnimation(Smelting.ANIMATION);
            player.sendSound(Smelting.soundEffect);
        } else if (ticks == actionDelay) {
            player.getInventory().deleteItemsIfContains(new Item[]{MATERIAL}, () -> {
                player.getAchievementDiaries().update(MorytaniaDiary.MAKE_CANNONBALLS);
                player.getInventory().addItem(PRODUCT);
                player.getSkills().addXp(SkillConstants.SMITHING, XP);
                player.sendFilteredMessage("You pour the molten metal into your cannonball mould.");
                player.sendFilteredMessage("The molten metal cools slowly to form " + PRODUCT_MULTIPLIER * 4 + " cannonballs.");
                cycle++;
            });
            return ticks = 0;
        }
        ticks++;
        return 0;
    }

}

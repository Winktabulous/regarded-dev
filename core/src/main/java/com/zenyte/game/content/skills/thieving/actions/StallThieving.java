package com.zenyte.game.content.skills.thieving.actions;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary;
import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.skills.thieving.CoinPouch;
import com.zenyte.game.content.skills.thieving.Stall;
import com.zenyte.game.content.skills.thieving.StallType;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ImmutableItem;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.area.apeatoll.Greegree;

import java.util.List;

/**
 * @author Kris | 21. okt 2017 : 19:17.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 */
public class StallThieving extends Action {

	private static final Animation THIEVING_ANIM = new Animation(881);
	private static final ForceTalk FORCE_TALK = new ForceTalk("Hey! Get your hands off there!");
	private static final ForceTalk DOG_FORCE_TALK = new ForceTalk("Woof! Woof! Woof!");

	public static void handleStall(final Player player, final WorldObject object) {
		final Stall stall = Stall.getStall(object.getId());
		if (stall == null) {
			return;
		}
		player.getActionManager().setAction(new StallThieving(stall, object));
	}

	public StallThieving(final Stall stall, final WorldObject object) {
		this.stall = stall;
		this.object = object;
	}

	private final Stall stall;
	private final WorldObject object;

	@Override
	public boolean start() {
		if (player.getSkills().getLevel(SkillConstants.THIEVING) < stall.getType().getLevel()) {
			player.sendMessage("You need a Thieving level of at least " + stall.getType().getLevel() + " to steal from this stall.");
			return false;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.sendMessage("You need some free inventory slots to steal from this stall.");
			return false;
		}
		if (player.isUnderCombat(0)) {
			player.sendMessage("You can't do this while in combat.");
			return false;
		}

		if (!World.containsObjectWithId(object, object.getId())) {
			return false;
		}
		if (Stall.APE_ATOLL_STALLS.contains(stall) && Greegree.MAPPED_VALUES.containsKey(player.getEquipment().getId(EquipmentSlot.WEAPON))) {
			player.sendMessage("I wouldn't like to blow my cover by getting caught stealing.");
			return false;
		}
		player.setAnimation(THIEVING_ANIM);
		player.lock();
		delay(2);
		return true;
	}

	@Override
	public boolean process() {
		return true;
	}

	@Override
	public int processWithDelay() {
		player.unlock();
		player.getSkills().addXp(SkillConstants.THIEVING, stall.getType().getExperience());
		if (stall.getType().equals(StallType.GEM_STALL)) {
            SherlockTask.STEAL_GEM_FROM_ARDOUGNE_MARKET.progress(player);
        }
		addLoot();
		checkGuards();
		spawnEmptyStall();
		return -1;
	}

	private final void spawnEmptyStall() {
		if (World.containsObjectWithId(object, object.getId())) {
			final WorldObject obj = new WorldObject(object);
			obj.setId(stall.getEmptyId());
			World.spawnObject(obj);
			WorldTasksManager.schedule(() -> World.spawnObject(object), Math.max(0, stall.getType().getTime() / 5));
		}
	}

	private final void checkGuards() {
		final List<NPC> list = CharacterLoop.find(player.getLocation(), 5, NPC.class, n -> {
			final String name = n.getDefinitions().getName().toLowerCase();
			return !n.isDead() && (name.contains("guard") || name.contains("tzhaar-ket"))
							&& n.getDefinitions().containsOption("Attack")
							&& !n.isProjectileClipped(player.getLocation(), false);
				}
		);
	    if (list.isEmpty()) {
			return;
		} //TODO 10% extra chance on not getting caught for ardy cloak 1
		final NPC npc = list.get(0);
        npc.getCombat().setTarget(player);
		npc.setForceTalk(npc.getName(player).contains("dog") ? DOG_FORCE_TALK : FORCE_TALK);
	}

	private void addLoot() {
		switch (stall) {
			case ARDOUGNE_AND_KOUREND_BAKERS_STALL -> player.getAchievementDiaries().update(ArdougneDiary.STEAL_CAKE);
			case VARROCK_AND_KOUREND_TEA_STALL -> player.getAchievementDiaries().update(VarrockDiary.STEAL_FROM_TEA_STALL);
			case KELDAGRIM_BAKERY_STALL, KELDAGRIM_CRAFTING_STALL -> player.getAchievementDiaries().update(FremennikDiary.STEAL_FROM_CRAFTING_STALL);
			case RELLEKKA_FISH_STALL -> player.getAchievementDiaries().update(FremennikDiary.STEAL_FROM_RELLEKKA_FISH_STALLS);
			case KELDAGRIM_GEM_STALL -> player.getAchievementDiaries().update(FremennikDiary.STEAL_FROM_GEM_STALL);
			case KOUREND_FRUIT_STALL -> player.getAchievementDiaries().update(KourendDiary.STEAL_FROM_FOOD_STALL);
			case TZHAAR_GEM_SHOP_COUNTER -> player.getDailyChallengeManager().update(SkillingChallenge.PICKPOCKET_GEM_STALL_TZHAARS);
			case HOME_COINS_STALL_BEGINNER, HOME_COINS_STALL_EASY, HOME_COINS_STALL_MEDIUM, HOME_COINS_STALL_HARD, HOME_COINS_STALL_MASTER -> Analytics.flagInteraction(player, Analytics.InteractionType.HOME_STALLS);
		}
		final boolean isBot = PlayerAttributesKt.getFlaggedAsBot(player);
		final Inventory inventory = player.getInventory();

		/* stall does not have items */
		if(stall.getType().getItems().length == 0)
			return;

		if (stall.getType().isRandomize()) {
			final ImmutableItem random = stall.getType().getItems()[Utils.random(stall.getType().getItems().length - 1)];
			if (random == null) {
				return;
			}
			int id = random.getId();
			int amount = Utils.random(random.getMinAmount(), random.getMaxAmount());
			if (isBot) {
				amount = (int) Math.max(1, amount * 0.01);
				final CoinPouch pouch = CoinPouch.ITEMS.get(id);
				if (pouch != null)
					id = CoinPouch.MAN.getItemId();
			}
			inventory.addItem(new Item(id, amount));
		} else {
			final double random = Utils.getRandomDouble(100);
			double currentRoll = 0;
			ImmutableItem loot = null;
			final ImmutableItem[] lootArr = stall.getType().getItems();
			for (int i = lootArr.length - 1; i >= 0; i--) {
				final ImmutableItem l = lootArr[i];
				if (l == null) {
					continue;
				}
				if ((currentRoll += l.getRate()) >= random) {
					loot = l;
					break;
				}
			}
			if (loot == null) {
				return;
			}
			int id = loot.getId();
			int amount = Utils.random(loot.getMinAmount(), loot.getMaxAmount());
			if (isBot) {
				amount = (int) Math.max(1, amount * 0.01);
				final CoinPouch pouch = CoinPouch.ITEMS.get(id);
				if (pouch != null)
					id = CoinPouch.MAN.getItemId();
			}
			inventory.addItem(new Item(id, amount));
		}
	}

	@Override
	public void stop() {}

}

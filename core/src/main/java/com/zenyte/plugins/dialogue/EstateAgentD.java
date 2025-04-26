package com.zenyte.plugins.dialogue;

import com.zenyte.game.GameConstants;
import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.skills.construction.constants.House;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class EstateAgentD extends Dialogue {

	public EstateAgentD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Hello. Welcome to the " + GameConstants.SERVER_NAME + " Housing Agency!<br> What can I do for you?");
		options(TITLE, "Can you move my house please?", "Can you redecorate my house please?").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10));
		npc(5, "Certainly. Where would you like it moved to?");
		options(6, TITLE, "Rimmington (5,000)", "Taverly (5,000)", "Pollnivneach (7,500)", "Kourend (8,750)", "More...").onOptionOne(() -> setKey(15)).onOptionTwo(() -> setKey(20)).onOptionThree(() -> setKey(25)).onOptionFour(() -> setKey(30)).onOptionFive(() -> setKey(35));
		player(15, "To Rimmington please!");
		if (player.getConstruction().getHouse().equals(House.RIMMINGTON)) {
			npc("Your house is already there!");
		} else if (player.getSkills().getLevel(SkillConstants.CONSTRUCTION) < House.RIMMINGTON.getLevel()) {
			npc("I'm afraid you don't have a high enough Construction<br> level to move there. You need to have level " + House.RIMMINGTON.getLevel() + ".");
		} else if (player.getInventory().getAmountOf(995) < House.RIMMINGTON.getPrice()) {
			npc("I'm afraid you don't have enough coins on you to move your house");
		} else {
			npc("There you go! Your house was moved to Rimmington!").executeAction(() -> {
				player.getConstruction().setHouse(House.RIMMINGTON);
				player.getInventory().deleteItem(new Item(995, House.RIMMINGTON.getPrice()));
			});
		}
		player(20, "To Taverly please!");
		if (player.getConstruction().getHouse().equals(House.TAVERLY)) {
			npc("Your house is already there!");
		} else if (player.getSkills().getLevel(SkillConstants.CONSTRUCTION) < House.TAVERLY.getLevel()) {
			npc("I'm afraid you don't have a high enough Construction<br> level to move there. You need to have level " + House.TAVERLY.getLevel() + ".");
		} else if (player.getInventory().getAmountOf(995) < House.RIMMINGTON.getPrice()) {
			npc("I'm afraid you don't have enough coins on you to move your house");
		} else {
			npc("There you go! Your house was moved to Taverley!").executeAction(() -> {
				player.getConstruction().setHouse(House.TAVERLY);
				player.getInventory().deleteItem(new Item(995, House.TAVERLY.getPrice()));
			});
		}
		player(25, "To Pollnivneach please!");
		if (player.getConstruction().getHouse().equals(House.POLLNIVNEACH)) {
			npc("Your house is already there!");
		} else if (player.getSkills().getLevel(SkillConstants.CONSTRUCTION) < House.POLLNIVNEACH.getLevel()) {
			npc("I'm afraid you don't have a high enough Construction<br> level to move there. You need to have level " + House.POLLNIVNEACH.getLevel() + ".");
		} else if (player.getInventory().getAmountOf(995) < House.RIMMINGTON.getPrice()) {
			npc("I'm afraid you don't have enough coins on you to move your house");
		} else {
			npc("There you go! Your house was moved to Pollnivneach!").executeAction(() -> {
				player.getConstruction().setHouse(House.POLLNIVNEACH);
				player.getInventory().deleteItem(new Item(995, House.POLLNIVNEACH.getPrice()));
			});
		}
		player(30, "To Kourend please!");
		if (player.getConstruction().getHouse().equals(House.KOUREND)) {
			npc("Your house is already there!");
		} else if (player.getSkills().getLevel(SkillConstants.CONSTRUCTION) < House.KOUREND.getLevel()) {
			npc("I'm afraid you don't have a high enough Construction<br> level to move there. You need to have level " + House.KOUREND.getLevel() + ".");
		} else if (player.getInventory().getAmountOf(995) < House.KOUREND.getPrice()) {
			npc("I'm afraid you don't have enough coins on you to move your house");
		} else {
			npc("There you go! Your house was moved to Kourend!").executeAction(() -> {
				player.getConstruction().setHouse(House.KOUREND);
				player.getInventory().deleteItem(new Item(995, House.KOUREND.getPrice()));
			});
		}
		options(35, TITLE, "Rellekka (10,000)", "Brimhaven (15,000)", "Yanille (25,000)", "...Previous").onOptionOne(() -> setKey(40)).onOptionTwo(() -> setKey(45)).onOptionThree(() -> setKey(50)).onOptionFour(() -> setKey(6));
		player(40, "To Rellekka please!");
		if (player.getConstruction().getHouse().equals(House.RELLEKKA)) {
			npc("Your house is already there!");
		} else if (player.getSkills().getLevel(SkillConstants.CONSTRUCTION) < House.RELLEKKA.getLevel()) {
			npc("I'm afraid you don't have a high enough Construction<br> level to move there. You need to have level " + House.RELLEKKA.getLevel() + ".");
		} else if (player.getInventory().getAmountOf(995) < House.RELLEKKA.getPrice()) {
			npc("I'm afraid you don't have enough coins on you to move your house");
		} else {
			npc("There you go! Your house was moved to Rellekka!").executeAction(() -> {
				player.getConstruction().setHouse(House.RELLEKKA);
				player.getInventory().deleteItem(new Item(995, House.RELLEKKA.getPrice()));
			});
		}
		player(45, "To Brimhaven please!");
		if (player.getConstruction().getHouse().equals(House.BRIMHAVEN)) {
			npc("Your house is already there!");
		} else if (player.getSkills().getLevel(SkillConstants.CONSTRUCTION) < House.BRIMHAVEN.getLevel()) {
			npc("I'm afraid you don't have a high enough Construction<br> level to move there. You need to have level " + House.BRIMHAVEN.getLevel() + ".");
		} else if (player.getInventory().getAmountOf(995) < House.BRIMHAVEN.getPrice()) {
			npc("I'm afraid you don't have enough coins on you to move your house");
		} else {
			npc("There you go! Your house was moved to Brimhaven!").executeAction(() -> {
				player.getConstruction().setHouse(House.BRIMHAVEN);
				player.getInventory().deleteItem(new Item(995, House.BRIMHAVEN.getPrice()));
			});
		}
		player(50, "To Yanille please!");
		if (player.getConstruction().getHouse().equals(House.YANILLE)) {
			npc("Your house is already there!");
		} else if (player.getSkills().getLevel(SkillConstants.CONSTRUCTION) < House.YANILLE.getLevel()) {
			npc("I'm afraid you don't have a high enough Construction<br> level to move there. You need to have level " + House.YANILLE.getLevel() + ".");
		} else if (player.getInventory().getAmountOf(995) < House.YANILLE.getPrice()) {
			npc("I'm afraid you don't have enough coins on you to move your house");
		} else {
			npc("There you go! Your house was moved to Yanille!").executeAction(() -> {
				player.getConstruction().setHouse(House.YANILLE);
				player.getInventory().deleteItem(new Item(995, House.YANILLE.getPrice()));
			});
		}
		npc(10, "Certainly. My magic can rebuild the house in a<br> completely new style! What style would you like?");
		options(11, TITLE, "Basic wood (5,000)", "Basic stone (5,000)", "Whitewashed stone (7,500)", "Fremennik-style wood (10,000)", "More...").onOptionOne(() -> setKey(55)).onOptionTwo(() -> setKey(60)).onOptionThree(() -> setKey(65)).onOptionFour(() -> setKey(70)).onOptionFive(() -> setKey(75));
		player(55, "Basic wood please!");
		if (player.getConstruction().getDecoration() == 0) {
			npc("Your house is already in that style!");
		} else if (player.getInventory().getAmountOf(995) < 5000) {
			npc("I'm afraid you don't have enough coins on you to redecorate your house");
		} else {
			npc("There you go! Your house was redecorated.").executeAction(() -> {
				player.getConstruction().setDecoration(0);
				player.getInventory().deleteItem(new Item(995, 5000));
			});
			player(60, "Basic stone please!");
		}
		if (player.getConstruction().getDecoration() == 1) {
			npc("Your house is already in that style!");
		} else if (player.getInventory().getAmountOf(995) < 5000) {
			npc("I'm afraid you don't have enough coins on you to redecorate your house");
		} else {
			npc("There you go! Your house was redecorated.").executeAction(() -> {
				player.getConstruction().setDecoration(1);
				player.getInventory().deleteItem(new Item(995, 5000));
			});
		}
		player(65, "Whitewashed stone please!");
		if (player.getConstruction().getDecoration() == 2) {
			npc("Your house is already in that style!");
		} else if (player.getInventory().getAmountOf(995) < 7500) {
			npc("I'm afraid you don't have enough coins on you to redecorate your house");
		} else {
			npc("There you go! Your house was redecorated.").executeAction(() -> {
				player.getConstruction().setDecoration(2);
				player.getInventory().deleteItem(new Item(995, 7500));
			});
		}
		player(70, "Fremennik-style wood please!");
		if (player.getConstruction().getDecoration() == 3) {
			npc("Your house is already in that style!");
		} else if (player.getInventory().getAmountOf(995) < 10000) {
			npc("I'm afraid you don't have enough coins on you to redecorate your house");
		} else {
			npc("There you go! Your house was redecorated.").executeAction(() -> {
				player.getConstruction().setDecoration(3);
				player.getInventory().deleteItem(new Item(995, 10000));
			});
		}
		options(75, TITLE, "Tropical wood (15,000)", "Fancy stone (25,000)", "Deathly mansion (35,000)", "...Previous").onOptionOne(() -> setKey(80)).onOptionTwo(() -> setKey(85)).onOptionThree(() -> setKey(90)).onOptionFour(() -> setKey(11));
		player(80, "Tropical wood please!");
		if (player.getConstruction().getDecoration() == 4) {
			npc("Your house is already in that style!");
		} else if (player.getInventory().getAmountOf(995) < 15000) {
			npc("I'm afraid you don't have enough coins on you to redecorate your house");
		} else {
			npc("There you go! Your house was redecorated.").executeAction(() -> {
				player.getConstruction().setDecoration(4);
				player.getInventory().deleteItem(new Item(995, 15000));
			});
		}
		player(85, "Fancy stone please!");
		if (player.getConstruction().getDecoration() == 5) {
			npc("Your house is already in that style!");
		} else if (player.getInventory().getAmountOf(995) < 25000) {
			npc("I'm afraid you don't have enough coins on you to redecorate your house");
		} else {
			npc("There you go! Your house was redecorated.").executeAction(() -> {
				player.getAchievementDiaries().update(VarrockDiary.DECORATE_HOUSE_WITH_FANCY_STONE);
				player.getAchievementDiaries().update(KandarinDiary.DECORATE_HOUSE_WITH_FANCY_STONE);
				player.getConstruction().setDecoration(5);
				player.getInventory().deleteItem(new Item(995, 25000));
			});
		}
		player(90, "Deathly mansion please!");
		if (player.getConstruction().getDecoration() == 6) {
			npc("Your house is already in that style!");
		} else if (player.getInventory().getAmountOf(995) < 35000) {
			npc("I'm afraid you don't have enough coins on you to redecorate your house");
		} else {
			npc("There you go! Your house was redecorated.").executeAction(() -> {
				player.getConstruction().setDecoration(6);
				player.getInventory().deleteItem(new Item(995, 35000));
			});
		}

	}

}

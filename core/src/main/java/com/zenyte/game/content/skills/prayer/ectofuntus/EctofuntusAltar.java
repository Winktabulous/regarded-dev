package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.boons.impl.BoneCruncher;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.DoubleItemChat;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.utilities.CollectionUtils;

/**
 * @author Kris | 23/06/2019 14:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EctofuntusAltar implements ObjectAction {

    private static final Animation praying = new Animation(1651);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Worship")) {
            final long delay = player.getNumericTemporaryAttribute("ectofuntus delay").longValue();
            if (delay == WorldThread.WORLD_CYCLE) {
                return;
            }
            player.addTemporaryAttribute("ectofuntus delay", WorldThread.WORLD_CYCLE);
            final Inventory inventory = player.getInventory();
            if (!inventory.containsItem(4286, 1)) {
                player.getDialogueManager().start(new DoubleItemChat(player, new Item(4286), new Item(4255), "You need a bucket of slime and some bonemeal to do this."));
                return;
            }
            BoneGrinding.Bonemeal bonemeal = null;
            for (final Int2ObjectMap.Entry<Item> item : inventory.getContainer().getItems().int2ObjectEntrySet()) {
                if ((bonemeal = CollectionUtils.findMatching(BoneGrinding.Bonemeal.values, meal -> meal.getBonemeal() == item.getValue().getId())) != null) {
                    break;
                }
            }
            if (bonemeal == null) {
                player.getDialogueManager().start(new DoubleItemChat(player, new Item(4286), new Item(4255), "You need a bucket of slime and some bonemeal to do this."));
                return;
            }
            player.getAchievementDiaries().update(MorytaniaDiary.OFFER_SOME_BONEMEAL);
            player.setAnimation(praying);
            inventory.deleteItem(4286, 1);
            boolean rolledPerk = player.getBoonManager().hasBoon(BoneCruncher.class) && BoneCruncher.roll();
            if(!rolledPerk) inventory.deleteItem(bonemeal.getBonemeal(), 1);
            inventory.addItem(1931, 1);
            inventory.addItem(1925, 1);
            player.getSkills().addXp(SkillConstants.PRAYER, bonemeal.getBones().getXp() * 4.0F);
            player.addAttribute("ecto-tokens", player.getNumericAttribute("ecto-tokens").intValue() + 5);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ECTOFUNTUS };
    }
}

package com.zenyte.game.content.skills.cooking;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Kris | 21. aug 2018 : 19:00:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class MilkChurning implements ObjectAction {

    private static final Item BUCKET = new Item(1925);

    private enum Churnable {

        BUCKET_OF_MILK(new Item(1927), 0, 0, null), CREAM(new Item(2130), 18, 8, new Animation(2793)), BUTTER(new Item(6697), 41, 16, new Animation(2794)), CHEESE(new Item(1985), 64, 25, new Animation(2795));

        private final Item product;

        private final int xp;

        private static final Churnable[] VALUES = values();

        private final int delay;

        private final Animation animation;

        Churnable(Item product, int xp, int delay, Animation animation) {
            this.product = product;
            this.xp = xp;
            this.delay = delay;
            this.animation = animation;
        }

        @Override
        public String toString() {
            final String name = name().toLowerCase().replaceAll("_", " ").replaceAll("bucket of ", "");
            return name;
        }
    }

    private static final class ChurnAction extends Action {

        private final Churnable churnable;

        private int amount;

        @Override
        public boolean start() {
            final MilkChurning.Churnable ingredient = getQuickestIngredient();
            if (ingredient == null) {
                player.sendMessage("You need some milk to churn " + churnable.toString() + ".");
                return false;
            }
            final int ordinal = churnable.ordinal() - ingredient.ordinal();
            final MilkChurning.Churnable churn = Churnable.VALUES[ordinal];
            player.setAnimation(churn.animation);
            delay(churn.delay);
            return true;
        }

        private Churnable getQuickestIngredient() {
            final Inventory inventory = player.getInventory();
            if (churnable == Churnable.CHEESE) {
                if (inventory.containsItem(Churnable.BUTTER.product)) {
                    return Churnable.BUTTER;
                } else if (inventory.containsItem(Churnable.CREAM.product)) {
                    return Churnable.CREAM;
                }
            } else if (churnable == Churnable.BUTTER) {
                if (inventory.containsItem(Churnable.CREAM.product)) {
                    return Churnable.CREAM;
                }
            }
            if (inventory.containsItem(Churnable.BUCKET_OF_MILK.product)) {
                return Churnable.BUCKET_OF_MILK;
            }
            return null;
        }

        @Override
        public boolean process() {
            return true;
        }

        public ChurnAction(Churnable churnable, int amount) {
            this.churnable = churnable;
            this.amount = amount;
        }

        @Override
        public int processWithDelay() {
            final MilkChurning.Churnable quickestIngredient = getQuickestIngredient();
            if (quickestIngredient == null) {
                return -1;
            }
            final Item ingredient = quickestIngredient.product;
            final Inventory inventory = player.getInventory();
            inventory.deleteItem(ingredient);
            inventory.addItem(churnable.product);
            if (quickestIngredient == Churnable.BUCKET_OF_MILK) {
                inventory.addItem(BUCKET).onFailure(item -> World.spawnFloorItem(item, player));
            }
            final int xp = churnable.xp - quickestIngredient.xp;
            player.getSkills().addXp(SkillConstants.COOKING, xp);
            player.sendMessage("You churn your " + quickestIngredient + " to make " + churnable + ".");
            if (--amount <= 0) {
                return -1;
            }
            final MilkChurning.Churnable nextQuickest = getQuickestIngredient();
            if (nextQuickest == null) {
                return -1;
            }
            final int ordinal = churnable.ordinal() - nextQuickest.ordinal();
            final MilkChurning.Churnable churn = Churnable.VALUES[ordinal];
            player.setAnimation(churn.animation);
            return churn.delay + 2;
        }

        @Override
        public void stop() {
            player.setAnimation(Animation.STOP);
        }
    }

    private static final class ChurnDialogue extends SkillDialogue {

        private static final Item[] ITEMS = new Item[] { Churnable.CREAM.product, Churnable.BUTTER.product, Churnable.CHEESE.product };

        public ChurnDialogue(final Player player) {
            super(player, ITEMS);
        }

        @Override
        public void run(final int slotId, final int amount) {
            if (slotId < 0 || slotId >= ITEMS.length) {
                return;
            }
            final MilkChurning.Churnable churnable = Churnable.VALUES[slotId + 1];
            player.getActionManager().setAction(new ChurnAction(churnable, amount));
        }
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getDialogueManager().start(new ChurnDialogue(player));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DAIRY_CHURN };
    }
}

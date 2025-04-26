package com.zenyte.game.content.skills.cooking.actions;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.plugins.dialogue.SkillDialogue;
import com.zenyte.plugins.flooritem.FloorItemPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 24/04/2019 19:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WineCreation extends Action {

    public static final class FermentingWineItemTakePlugin implements FloorItemPlugin {
        @Override
        public boolean overrideTake() {
            return true;
        }

        @Override
        public void telegrab(@NotNull final Player player, @NotNull final FloorItem item) {
            if (!canTelegrab(player, item)) {
                return;
            }
            final int space = player.getInventory().getFreeSlots();
            World.destroyFloorItem(item);
            player.getInventory().addItem(item).onFailure(it -> World.spawnFloorItem(it, player));
            final int remainingSpace = player.getInventory().getFreeSlots();
            if (space != remainingSpace) {
                player.getVariables().schedule(20, TickVariable.WINE_FERMENTATION);
            }
        }

        @Override
        public void handle(final Player player, final FloorItem item, final int optionId, final String option) {
            if (option.equalsIgnoreCase("Take")) {
                final int space = player.getInventory().getFreeSlots();
                World.takeFloorItem(player, item);
                final int remainingSpace = player.getInventory().getFreeSlots();
                if (space != remainingSpace) {
                    player.getVariables().schedule(20, TickVariable.WINE_FERMENTATION);
                }
            }
        }

        @Override
        public int[] getItems() {
            return new int[] {20752, 1995};
        }
    }


    public static final class WineOnGrapesItemAction implements PairedItemOnItemPlugin {
        @Override
        public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
            final Item jug = from.getId() == 1937 ? from : to;
            final Item grapes = from == jug ? to : from;
            player.getDialogueManager().start(new WineCreationD(player, "How many wines would you like to ferment?", new Item(grapes.getId() == 20749 ? 20752 : 1995)));
        }

        @Override
        public ItemPair[] getMatchingPairs() {
            return new ItemPair[] {ItemPair.of(1987, 1937), ItemPair.of(20749, 1937)};
        }
    }


    private static final class WineCreationD extends SkillDialogue {
        WineCreationD(final Player player, final String question, final Item... items) {
            super(player, question, items);
        }

        @Override
        public void run(final int slotId, final int amount) {
            player.getActionManager().setAction(new WineCreation(amount, items[0].getId() == 20752 ? 20749 : 1987));
        }
    }

    private static final Animation animation = new Animation(7529);
    private static final Graphics graphics = new Graphics(47);
    private int count;
    private final int grapes;

    @Override
    public boolean start() {
        final int level = grapes == 20749 ? 65 : 35;
        if (player.getSkills().getLevel(SkillConstants.COOKING) < level) {
            player.sendMessage("You need a Cooking level of at least " + level + " to ferment this wine.");
            return false;
        }
        player.setAnimation(animation);
        player.setGraphics(graphics);
        delay(2);
        return true;
    }

    @Override
    public boolean process() {
        final Inventory inventory = player.getInventory();
        return count > 0 && inventory.containsItem(1937, 1) && inventory.containsItem(grapes, 1);
    }

    @Override
    public int processWithDelay() {
        player.getInventory().deleteItemsIfContains(new Item[] {new Item(1937, 1), new Item(grapes, 1)}, () -> {
            player.getInventory().addOrDrop(new Item(grapes == 20749 ? 20752 : 1995));
            player.getDailyChallengeManager().update(SkillingChallenge.COOK_WINES);
        });
        count--;
        player.getVariables().schedule(20, TickVariable.WINE_FERMENTATION);
        if (!process()) {
            return -1;
        }
        player.setAnimation(animation);
        player.setGraphics(graphics);
        player.sendFilteredMessage("You squeeze the grapes into the jug. The wine begins to ferment.");
        return 1;
    }

    public WineCreation(int count, int grapes) {
        this.count = count;
        this.grapes = grapes;
    }
}

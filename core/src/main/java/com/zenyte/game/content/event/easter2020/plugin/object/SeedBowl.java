package com.zenyte.game.content.event.easter2020.plugin.object;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.SplittingHeirs;
import com.zenyte.game.content.event.easter2020.Stage;
import com.zenyte.game.content.event.easter2020.plugin.npc.EasterBird;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.object.WorldObjectUtils;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.PlayerChat;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

/**
 * @author Corey
 * @since 02/04/2020
 */
@SkipPluginScan
public class SeedBowl implements ObjectAction, ItemOnObjectAction {

    public static void foodOnBowl(final Player player, final Item item) {
        if (!Contents.EMPTY.getVarbit().isSet(player)) {
            player.getDialogueManager().start(new PlayerChat(player, "Looks like it's already full."));
            return;
        }
        final SeedBowl.Contents bowl = Contents.byFoodItemId.get(item.getId());
        bowl.getVarbit().sendVar(player);
        player.getDialogueManager().start(new PlainChat(player, "You fill the seed bowl with food."));
        player.getInventory().deleteItem(item);
    }

    public static Contents getRequiredBowl(final Player player) {
        switch(player.getPlayerInformation().getUserIdentifier() % 4) {
            case 0:
                return Contents.SUNFLOWER_SEEDS;
            case 1:
                return Contents.CRACKER_BITS;
            case 2:
                return Contents.WORM_BITS;
            case 3:
                return Contents.POPPY_SEEDS;
        }
        return Contents.SUNFLOWER_SEEDS;
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final SeedBowl.Contents bowl = Contents.byBowlId.get(WorldObjectUtils.getObjectIdOfPlayer(object, player));
        if (option.equalsIgnoreCase("look-at")) {
            player.getDialogueManager().start(new PlainChat(player, "The bowl is full of " + bowl.getContents() + "."));
            return;
        }
        if (option.equalsIgnoreCase("empty")) {
            if (SplittingHeirs.progressedAtLeast(player, Stage.BIRD_FED_SPEAK_TO_BUNNY_JR)) {
                player.getDialogueManager().start(new PlayerChat(player, "I should probably leave the Easter Bird alone now."));
            } else {
                Contents.EMPTY.getVarbit().sendVar(player);
                player.getDialogueManager().start(new PlainChat(player, "You empty the bowl of it's contents."));
            }
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        foodOnBowl(player, item);
    }

    @Override
    public Object[] getItems() {
        return Contents.byFoodItemId.keySet().stream().filter(i -> i != -1).toArray();
    }

    @Override
    public Object[] getObjects() {
        // base object
        return new Object[] { 46406 };
    }

    public enum Contents {

        EMPTY(46277, -1, -1, "", EasterBird.Varbit.EMPTY_BOWL), SUNFLOWER_SEEDS(46273, EasterConstants.EasterItem.SUNFLOWER_SEEDS.getItemId(), 46267, "Sun seed.", EasterBird.Varbit.SUNFLOWER_SEED_BOWL), CRACKER_BITS(46274, EasterConstants.EasterItem.CRACKER_BITS.getItemId(), 46268, "Polly wanna cracker!", EasterBird.Varbit.CRACKER_BITS_BOWL), WORM_BITS(46275, EasterConstants.EasterItem.WORM_BITS.getItemId(), 46269, "Squirmy wormy!", EasterBird.Varbit.WORM_BITS_BOWL), POPPY_SEEDS(46276, EasterConstants.EasterItem.POPPY_SEEDS.getItemId(), 46270, "Small black seed!", EasterBird.Varbit.POPPY_SEED_BOWL);

        public static final Int2ObjectOpenHashMap<Contents> byBowlId;

        public static final Int2ObjectOpenHashMap<Contents> byFoodBagId;

        public static final Int2ObjectOpenHashMap<Contents> byFoodItemId;

        private static final Contents[] values = values();

        static {
            CollectionUtils.populateMap(values, byBowlId = new Int2ObjectOpenHashMap<>(values.length), Contents::getBowlObjectId);
            CollectionUtils.populateMap(values, byFoodBagId = new Int2ObjectOpenHashMap<>(values.length), Contents::getFoodBagObjectId);
            CollectionUtils.populateMap(values, byFoodItemId = new Int2ObjectOpenHashMap<>(values.length), Contents::getFoodItemId);
        }

        private final int bowlObjectId;

        private final int foodItemId;

        private final int foodBagObjectId;

        private final String foodMessage;

        private final EasterBird.Varbit varbit;

        public String getContents() {
            return name().toLowerCase().replace("_", " ");
        }

        Contents(int bowlObjectId, int foodItemId, int foodBagObjectId, String foodMessage, EasterBird.Varbit varbit) {
            this.bowlObjectId = bowlObjectId;
            this.foodItemId = foodItemId;
            this.foodBagObjectId = foodBagObjectId;
            this.foodMessage = foodMessage;
            this.varbit = varbit;
        }

        public int getBowlObjectId() {
            return bowlObjectId;
        }

        public int getFoodItemId() {
            return foodItemId;
        }

        public int getFoodBagObjectId() {
            return foodBagObjectId;
        }

        public String getFoodMessage() {
            return foodMessage;
        }

        public EasterBird.Varbit getVarbit() {
            return varbit;
        }
    }
}

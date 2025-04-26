package com.zenyte.game.content.skills.hunter.plugins;

import com.zenyte.game.content.skills.farming.FarmingProduct;
import com.zenyte.game.content.skills.hunter.node.BirdHouseState;
import com.zenyte.game.content.skills.hunter.object.Birdhouse;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mgi.utilities.CollectionUtils;

import java.util.*;

/**
 * @author Kris | 24/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SeedOnBirdhousePlugin implements ItemOnObjectAction {

    private enum BirdhouseSeed {

        BARLEY(ItemId.BARLEY_SEED, false),
        HAMMERSTONE(ItemId.HAMMERSTONE_SEED, false),
        ASGARNIAN(ItemId.ASGARNIAN_SEED, false),
        JUTE(ItemId.JUTE_SEED, false),
        YANILLIAN(ItemId.YANILLIAN_SEED, false),
        KRANDORIAN(ItemId.KRANDORIAN_SEED, false),
        WILDBLOOD(ItemId.WILDBLOOD_SEED, true),

        GUAM(ItemId.GUAM_SEED, false),
        MARRENTILL(ItemId.MARRENTILL_SEED, false),
        TARROMIN(ItemId.TARROMIN_SEED, false),
        HARRALANDER(ItemId.HARRALANDER_SEED, false),
        RANARR(ItemId.RANARR_SEED, true),
        TOADFLAX(ItemId.TOADFLAX_SEED, true),
        IRIT(ItemId.IRIT_SEED, true),
        AVANTOE(ItemId.AVANTOE_SEED, true),
        KWUARM(ItemId.KWUARM_SEED, true),
        SNAPDRAGON(ItemId.SNAPDRAGON_SEED, true),
        CADANTINE(ItemId.CADANTINE_SEED, true),
        LANTADYME(ItemId.LANTADYME_SEED, true),
        DWARF_WEED(ItemId.DWARF_WEED_SEED, true),
        TORSTOL(ItemId.TORSTOL_SEED, true),

        MARIGOLD(ItemId.MARIGOLD_SEED, false),
        ROSEMARY(ItemId.ROSEMARY_SEED, false),
        NASTURTIUM(ItemId.NASTURTIUM_SEED, false),
        WOAD(ItemId.WOAD_SEED, false),
        LIMPWURT(ItemId.LIMPWURT_SEED, false),
        WHITE_LILY(ItemId.WHITE_LILY_SEED, false),

        POTATO(ItemId.POTATO_SEED, false),
        ONION(ItemId.ONION_SEED, false),
        CABBAGE(ItemId.CABBAGE_SEED, false),
        TOMATO(ItemId.TOMATO_SEED, false),
        SWEETCORN(ItemId.SWEETCORN_SEED, false),
        STRAWBERRY(ItemId.STRAWBERRY_SEED, false),
        WATERMELON(ItemId.WATERMELON_SEED, false),
        SNAPE_GRASS(ItemId.SNAPE_GRASS_SEED, false),
        REDBERRY(ItemId.REDBERRY_SEED, false),
        POISON_IVY(ItemId.POISON_IVY_SEED, false),
        CADAVA(ItemId.CADAVABERRY_SEED, false),
        DWELLBERRY(ItemId.DWELLBERRY_SEED, false),
        JANGERBERRY(ItemId.JANGERBERRY_SEED, false),
        WHITEBERRY(ItemId.WHITEBERRY_SEED, false)

        ;

        private final int seedId;

        private final boolean addsDouble;

        private static final List<BirdhouseSeed> values = Collections.unmodifiableList(Arrays.asList(values()));

        BirdhouseSeed(int seedId, boolean addsDouble) {
            this.seedId = seedId;
            this.addsDouble = addsDouble;
        }

        public static List<BirdhouseSeed> getValues() {
            return values;
        }
    }

    private static final Animation animation = new Animation(827);

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        final SeedOnBirdhousePlugin.BirdhouseSeed seed = CollectionUtils.findMatching(BirdhouseSeed.getValues(), value -> value.seedId == item.getId());
        if (seed == null) {
            player.sendMessage("That seed can't be put in this birdhouse.");
            return;
        }
        final Optional<Birdhouse> optionalBirdhouse = player.getHunter().findBirdhouse(object.getId());
        if (!optionalBirdhouse.isPresent()) {
            player.sendMessage("You need to place a birdhouse here first.");
            return;
        }
        final Birdhouse birdhouse = optionalBirdhouse.get();
        final BirdHouseState state = birdhouse.getState();
        switch(state) {
            case EMPTY:
                final int currentSeeds = birdhouse.getSeedsFilled();
                final int amountRequired = 10 - currentSeeds;
                final boolean isDouble = seed.addsDouble;
                final Inventory inventory = player.getInventory();
                final int amountToDelete = Math.min(inventory.getAmountOf(item.getId()), (int) Math.ceil(amountRequired / (isDouble ? 2.0F : 1.0F)));
                if (amountToDelete <= 0) {
                    throw new IllegalStateException();
                }
                player.setAnimation(animation);
                player.sendSound(2605);
                inventory.deleteItem(new Item(item.getId(), amountToDelete));
                birdhouse.setSeedsFilled(Math.min(10, currentSeeds + (amountToDelete * (isDouble ? 2 : 1))));
                birdhouse.setState(BirdHouseState.IN_PROGRESS);
                if (birdhouse.getSeedsFilled() < 10) {
                    player.getDialogueManager().start(new PlainChat(player, "You add " + amountToDelete + " x " + item.getName().toLowerCase() + " to the birdhouse."));
                    player.sendMessage("You add " + amountToDelete + " x " + item.getName().toLowerCase() + " to the birdhouse.");
                } else {
                    birdhouse.setFillTime(System.currentTimeMillis());
                    player.getDialogueManager().start(new PlainChat(player, "Your birdhouse trap is now full of seed and will start to catch birds."));
                }
                birdhouse.refreshVarbits();
                break;
            case IN_PROGRESS:
                player.sendMessage("Your birdhouse is already filled with seeds.");
                break;
            case FINISHED:
                player.sendMessage("Your birdhouse trap has run out of seed. It is ready to dismantle to see what is has caught.");
                break;
            default:
                throw new IllegalStateException(Objects.toString(state));
        }
    }

    @Override
    public Object[] getItems() {
        final IntOpenHashSet list = new IntOpenHashSet();
        for (final SeedOnBirdhousePlugin.BirdhouseSeed seed : BirdhouseSeed.getValues()) {
            list.add(seed.seedId);
        }
        for (final FarmingProduct product : FarmingProduct.values) {
            final Item seed = product.getSeed();
            if (seed == null || !seed.getName().contains("seed")) {
                continue;
            }
            list.add(seed.getId());
        }
        return list.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 30565, 30566, 30567, 30568 };
    }
}

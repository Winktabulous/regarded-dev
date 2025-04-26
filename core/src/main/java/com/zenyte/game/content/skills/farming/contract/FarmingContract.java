package com.zenyte.game.content.skills.farming.contract;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.zenyte.game.content.skills.farming.FarmingProduct;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.content.skills.farming.contract.FarmingContractDifficulty.*;

/**
 * @author Christopher
 * @since 4/7/2020
 */
public enum FarmingContract {
    POTATO(FarmingProduct.POTATO, "some potatoes", Pair.of(EASY, 45)), MARIGOLD(FarmingProduct.MARIGOLD, "a marigold", Pair.of(EASY, 45)), ONION(FarmingProduct.ONION, "some onions", Pair.of(EASY, 45)), CABBAGE(FarmingProduct.CABBAGE, "some cabbages", Pair.of(EASY, 45)), REDBERRY(FarmingProduct.REDBERRY, "a redberry bush", Pair.of(EASY, 45)), ROSEMARY(FarmingProduct.ROSEMARY, "some rosemary", Pair.of(EASY, 45)), TOMATO(FarmingProduct.TOMATO, "some tomatoes", Pair.of(EASY, 45)), SWEETCORN(FarmingProduct.SWEETCORN, "some sweetcorns", Pair.of(EASY, 45)), CADAVABERRY(FarmingProduct.CADAVABERRY, "a cadavaberry bush", Pair.of(EASY, 45)), NASTURTIUM(FarmingProduct.NASTURTIUM, "some nasturtiums", Pair.of(EASY, 45)), WOAD(FarmingProduct.WOAD, "some woads", Pair.of(EASY, 45)), LIMPWURT(FarmingProduct.LIMPWURT, "some limpwurts", Pair.of(EASY, 45)), STRAWBERRY(FarmingProduct.STRAWBERRY, "some strawberries", Pair.of(EASY, 45), Pair.of(MEDIUM, 65)), DWELLBERRY(FarmingProduct.DWELLBERRY, "a dwellberry bush", Pair.of(EASY, 45)), JANGERBERRY(FarmingProduct.JANGERBERRY, "a jangerberry bush", Pair.of(EASY, 48), Pair.of(MEDIUM, 65)), CACTUS(FarmingProduct.CACTUS, "a cactus", Pair.of(EASY, 55), Pair.of(MEDIUM, 65)), GUAM(FarmingProduct.GUAM, "some guams", Pair.of(EASY, 65)), MARRENTILL(FarmingProduct.MARRENTILL, "some marrentills", Pair.of(EASY, 65)), OAK_TREE(FarmingProduct.OAK, "an oak tree", Pair.of(EASY, 65)), TARROMIN(FarmingProduct.TARROMIN, "some tarromins", Pair.of(EASY, 65)), HARRALANDER(FarmingProduct.HARRALANDER, "some harralanders", Pair.of(EASY, 65)), WILLOW_TREE(FarmingProduct.WILLOW, "a willow tree", Pair.of(EASY, 65)), RANARR(FarmingProduct.RANARR, "some ranarrs", Pair.of(EASY, 65)), TOADFLAX(FarmingProduct.TOADFLAX, "some toadflaxes", Pair.of(EASY, 65)), IRIT(FarmingProduct.IRIT, "some irits", Pair.of(EASY, 65), Pair.of(MEDIUM, 65)), MAPLE_TREE(FarmingProduct.MAPLE, "a maple tree", Pair.of(EASY, 65), Pair.of(MEDIUM, 65), Pair.of(HARD, 85)), AVANTOE(FarmingProduct.AVANTOE, "some avantoes", Pair.of(EASY, 65), Pair.of(MEDIUM, 65)), APPLE_TREE(FarmingProduct.APPLE, "an apple tree", Pair.of(EASY, 85)), BANANA_TREE(FarmingProduct.BANANA, "a banana tree", Pair.of(EASY, 85)), ORANGE_TREE(FarmingProduct.ORANGE, "an orange tree", Pair.of(EASY, 85)), CURRY_TREE(FarmingProduct.CURRY, "a curry tree", Pair.of(EASY, 85), Pair.of(MEDIUM, 85)), WATERMELON(FarmingProduct.WATERMELON, "some watermelons", Pair.of(MEDIUM, 65), Pair.of(HARD, 85)), KWUARM(FarmingProduct.KWUARM, "some kwuarms", Pair.of(MEDIUM, 65)), WHITE_LILY(FarmingProduct.WHITE_LILY, "some white lilies", Pair.of(MEDIUM, 65), Pair.of(HARD, 85)), WHITEBERRY(FarmingProduct.WHITEBERRY, "a whiteberry bush", Pair.of(MEDIUM, 65), Pair.of(HARD, 85)), YEW_TREE(FarmingProduct.YEW, "a yew tree", Pair.of(MEDIUM, 65), Pair.of(HARD, 85)), SNAPE_GRASS(FarmingProduct.SNAPE_GRASS, "some snape grass", Pair.of(MEDIUM, 65), Pair.of(HARD, 85)), SNAPDRAGON(FarmingProduct.SNAPDRAGON, "some snapdragons", Pair.of(MEDIUM, 65), Pair.of(HARD, 85)), POTATO_CACTUS(FarmingProduct.POTATO_CACTUS, "a potato cactus", Pair.of(MEDIUM, 65), Pair.of(HARD, 85)), CADANTINE(FarmingProduct.CADANTINE, "some cadantines", Pair.of(MEDIUM, 67), Pair.of(HARD, 85)), POISON_IVY(FarmingProduct.POISON_IVY, "a poison ivy bush", Pair.of(MEDIUM, 70), Pair.of(HARD, 85)), LANTADYME(FarmingProduct.LANTADYME, "some lantadymes", Pair.of(MEDIUM, 73), Pair.of(HARD, 85)), MAGIC_TREE(FarmingProduct.MAGIC, "a magic tree", Pair.of(MEDIUM, 75), Pair.of(HARD, 85)), PINEAPPLE_PLANT(FarmingProduct.PINEAPPLE, "a pineapple plant", Pair.of(MEDIUM, 85)), PAPAYA_TREE(FarmingProduct.PAPAYA, "a papaya tree", Pair.of(MEDIUM, 85)), PALM_TREE(FarmingProduct.PALM, "a palm tree", Pair.of(MEDIUM, 85), Pair.of(HARD, 85)), DWARF_WEED(FarmingProduct.DWARF_WEED, "some dwarf weeds", Pair.of(HARD, 85)), DRAGONFRUIT_TREE(FarmingProduct.DRAGONFRUIT, "a dragonfruit tree", Pair.of(HARD, 85)), CELASTRUS_TREE(FarmingProduct.CELASTRUS, "a celastrus tree", Pair.of(HARD, 85)), TORSTOL(FarmingProduct.TORSTOL, "some torstols", Pair.of(HARD, 85)), REDWOOD_TREE(FarmingProduct.REDWOOD, "a redwood tree", Pair.of(HARD, 90));
    public static final int MINIMUM_LEVEL = 45;
    public static final String CONTRACT_ATTR = "farming contract";
    public static final String CONTRACT_DIFFICULTY_ATTR = "farming contract difficulty";
    public static final String COMPLETED_ATTR = "farming contract completed";
    public static final String COMPLETED_COUNT_ATTR = "completed farming contract count";
    public static final String SEED_PACK_LOW_ROLLS_ATTR = "seed pack low rolls";
    public static final String SEED_PACK_MEDIUM_ROLLS_ATTR = "seed pack medium rolls";
    public static final String SEED_PACK_HIGH_ROLLS_ATTR = "seed pack high rolls";
    private static final FarmingContract[] contracts = values();
    private static final Multimap<FarmingContractDifficulty, FarmingContract> contractsByDifficulty = HashMultimap.create();

    static {
        for (FarmingContract contract : contracts) {
            for (Pair<FarmingContractDifficulty, Integer> stage : contract.getDifficulties()) {
                contractsByDifficulty.put(stage.getKey(), contract);
            }
        }
    }

    private final FarmingProduct product;
    private final String displayName;
    private final Pair<FarmingContractDifficulty, Integer>[] difficulties;

    @SafeVarargs
    FarmingContract(final FarmingProduct product, final String displayName, final Pair<FarmingContractDifficulty, Integer>... difficulties) {
        this.product = product;
        this.displayName = displayName;
        this.difficulties = difficulties;
    }

    public static FarmingContract getByName(@NotNull final String name) {
        for (FarmingContract contract : contracts) {
            if (contract.name().equals(name)) {
                return contract;
            }
        }
        throw new IllegalArgumentException("Tried getting a non-existent contract with name: " + name);
    }

    public static void assign(@NotNull final Player player, @NotNull final FarmingContractDifficulty difficulty, @NotNull FarmingContract contract) {
        player.addAttribute(CONTRACT_ATTR, contract.name());
        player.addAttribute(CONTRACT_DIFFICULTY_ATTR, difficulty.name());
    }

    public static FarmingContract generateContract(@NotNull final Player player, @NotNull final FarmingContractDifficulty difficulty) {
        final ObjectArrayList<Object> possibleContracts = new ObjectArrayList<>();
        for (FarmingContract contract : contractsByDifficulty.get(difficulty)) {
            if (player.getSkills().getLevel(SkillConstants.FARMING) >= contract.getLevelForDifficulty(difficulty)) {
                possibleContracts.add(contract);
            }
        }
        return possibleContracts.isEmpty() ? null : (FarmingContract) Utils.getRandomCollectionElement(possibleContracts);
    }

    private static int random(final Player player, final FarmingContractRoll roll) {
        if (roll.getPredicate().test(player)) {
            return Utils.random(roll.getLowInclusive(), roll.getHighInclusive());
        }
        return 0;
    }

    private int getLevelForDifficulty(@NotNull final FarmingContractDifficulty difficulty) {
        for (Pair<FarmingContractDifficulty, Integer> difficultyWithLevel : difficulties) {
            if (difficultyWithLevel.getKey() == difficulty) {
                return difficultyWithLevel.getValue();
            }
        }
        throw new IllegalArgumentException("Invalid difficulty " + difficulty + " for contract " + name());
    }

    public void complete(@NotNull final Player player) {
        if (player.getBooleanAttribute(COMPLETED_ATTR)) {
            return;
        }
        player.sendMessage("You've completed a Farming Guild Contract. You should return to Guildmaster Jane.");
        player.putBooleanAttribute(COMPLETED_ATTR, true);
    }

    public void reward(@NotNull final Player player) {
        final Item seedPack = createSeedPack(player);
        player.getInventory().addOrDrop(seedPack);
        player.getAttributes().remove(CONTRACT_ATTR);
        player.getAttributes().remove(CONTRACT_DIFFICULTY_ATTR);
        player.putBooleanAttribute(COMPLETED_ATTR, false);
        player.addAttribute(COMPLETED_COUNT_ATTR, player.getNumericAttribute(COMPLETED_COUNT_ATTR).intValue() + 1);
    }

    private Item createSeedPack(@NotNull final Player player) {
        final Item seedPack = new Item(ItemId.SEED_PACK);
        final int productGrowthTime = (int) TimeUnit.MILLISECONDS.toMinutes(getProduct().getCycleTime()) * getProduct().getRegularStage().amount();
        final FarmingContractDifficulty difficulty = FarmingContractDifficulty.getByName(player.getAttributes().get(CONTRACT_DIFFICULTY_ATTR).toString());
        final FarmingContractTier tier = FarmingContractTier.get(productGrowthTime, difficulty);
        applySeedPackAttributes(player, seedPack, tier);
        return seedPack;
    }

    public static void applySeedPackAttributes(@NotNull final Player player, @NotNull final Item seedPack, @NotNull final FarmingContractTier tier) {
        final int highRolls = random(player, tier.getHighRolls());
        final int mediumRolls = random(player, tier.getMediumRolls());
        final int lowRolls = Math.min(tier.getMaxRolls() - (highRolls + mediumRolls), random(player, tier.getLowRolls()));
        seedPack.setAttribute(SEED_PACK_HIGH_ROLLS_ATTR, highRolls);
        seedPack.setAttribute(SEED_PACK_MEDIUM_ROLLS_ATTR, mediumRolls);
        seedPack.setAttribute(SEED_PACK_LOW_ROLLS_ATTR, lowRolls);
    }

    public FarmingProduct getProduct() {
        return product;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Pair<FarmingContractDifficulty, Integer>[] getDifficulties() {
        return difficulties;
    }
}

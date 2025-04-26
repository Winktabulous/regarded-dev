package com.zenyte.game.content.skills.fishing;

import com.google.common.collect.ImmutableMap;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalTool;
import com.near_reality.game.world.entity.player.PlayerSkillingModifiersKt;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.boons.impl.BarbarianFisher;
import com.zenyte.game.content.boons.impl.IWantItAll;
import com.zenyte.game.content.boons.impl.SwissArmyMan;
import com.zenyte.game.content.boons.impl.TheLegendaryFisherman;
import com.zenyte.game.content.skills.cooking.CookingDefinitions;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.ClueItemUtil;
import com.zenyte.game.content.treasuretrails.clues.CharlieTask;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.FishingSpot;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.region.area.wilderness.WildernessResourceArea;
import com.zenyte.plugins.dialogue.PlainChat;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.near_reality.game.world.entity.player.PlayerSkillingModifiersKt.determineGatheringMultiplier;
import static com.near_reality.game.world.entity.player.PlayerSkillingModifiersKt.onGather;
import static com.zenyte.game.content.skills.woodcutting.actions.Woodcutting.BURN_GFX;

/**
 * @author Noele | Nov 9, 2017 : 12:22:34 AM
 */
public class Fishing extends Action {
    public static final Map<DiaryReward, Integer> diaryRewardMap = ImmutableMap.<DiaryReward, Integer>builder().put(DiaryReward.RADAS_BLESSING4, 2).put(DiaryReward.RADAS_BLESSING3, 4).put(DiaryReward.RADAS_BLESSING2, 6).put(DiaryReward.RADAS_BLESSING1, 8).build();
    private final FishingSpot spot;
    private final SpotDefinitions defs;
    private FishingTool.Tool tool;
    private final List<FishDefinitions> fish = new ObjectArrayList<>();
    private final int posHash;
    private final boolean barbarian;

    public Fishing(final FishingSpot spot, final SpotDefinitions spotDefinitions) {
        this.spot = spot;
        defs = spotDefinitions;
        posHash = spot.getPosition().getPositionHash();
        barbarian = defs == SpotDefinitions.BARBARIAN_FISH;
    }

    public static void init(final Player player, final NPC npc, final String option) {
        if (!(npc instanceof FishingSpot)) {
            throw new RuntimeException("Spot is not original/implemented: " + npc.getId() + ", " + option);
        }
        final SpotDefinitions spot = SpotDefinitions.get(player.getTransmogrifiedId(npc.getDefinitions(), npc.getId()) + "|" + option);
        if (spot == null) {
            throw new RuntimeException("Spot is null: " + npc.getId() + ", " + option);
        }
        player.getActionManager().setAction(new Fishing((FishingSpot) npc, spot));
    }

    @Override
    public boolean start() {
        perkFishingCounter = 0;
        final Optional<FishingTool.Tool> tool = defs.getTool().getTool(player);
        if (tool.isEmpty()) {
            final String name = ItemDefinitions.getOrThrow(defs.getTool().tools[0].id).getName().toLowerCase();
            player.getDialogueManager().start(new PlainChat(player, "You need a " + name + " to " + (barbarian ? "catch" : defs.getActions()[0].toLowerCase()) + " these fish."));
            return false;
        }
        this.tool = tool.get();
        if (!check()) {
            return false;
        }
        this.fish.addAll(Arrays.stream(defs.getFish()).filter(fish -> player.getSkills().getLevel(SkillConstants.FISHING) >= fish.getLevel()).collect(Collectors.toList()));
        if (fish.contains(FishDefinitions.INFERNAL_EEL)) {
            final int gloves = player.getEquipment().getId(EquipmentSlot.HANDS);
            if (gloves != 1580 && gloves != 30030) {
                player.sendMessage("You need a pair of ice gloves to fish in this hot lava.");
                return false;
            }
        }
        final String name = (fish.size() == 1) ? Utils.checkPlural(fish.get(0).getName()) : "a fish";
        if (defs == SpotDefinitions.MINNOW) {
            player.sendFilteredMessage("You attempt to catch some minnows.");
        } else {
            player.sendFilteredMessage("You attempt to catch " + name + ".");
        }
        player.setFaceEntity(spot);
        delay(getDelay());
        return true;
    }

    private int getDelay() {
        if(defs == SpotDefinitions.MINNOW)
            return 1;
        if(player.getBoonManager().hasBoon(TheLegendaryFisherman.class))
            return 2;
        return tool.increasedSpeed ? 3 : 4;
    }

    @Override
    public boolean process() {
        player.setAnimation(tool.animation);
        return checkSpot();
    }

    @Override
    public int processWithDelay() {
        if (!success()) {
            return getDelay();
        }
        if (!check()) {
            player.setAnimation(Animation.STOP);
            return -1;
        }
        addFish();
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You do not have enough space in your inventory.");
            return -1;
        }
        return getDelay();
    }

    @Override
    public void stop() {
        player.setFaceEntity(null);
    }

    public boolean success() {
        final int fishLevel = defs == SpotDefinitions.MINNOW ? 20 : defs == SpotDefinitions.BARBARIAN_FISH ? 1 : defs.getLowestTierFish().getLevel();
        final int level = player.getSkills().getLevel(SkillConstants.FISHING) + (player.inArea("Fishing Guild") ? 7 : 0);
        final int advancedLevels = level - fishLevel;
        return Math.min(Math.round(advancedLevels * 0.6F) + 30, 67) > Utils.random(80);
    }

    private void addFish() {
        //TODO : add proper catch rate algorithm to 'fish' value
        final FishingBait[] baits = defs.getBait();
        if (baits != null) {
            for (final FishingBait bait : baits) {
                if (player.getInventory().containsItem(bait.getId(), 1)) {
                    player.getInventory().deleteItem(bait.getId(), 1);
                    break;
                }
            }
        }
        final FishDefinitions fish = this.fish.get(Utils.random(this.fish.size() - 1));
        if (fish.equals(FishDefinitions.TROUT)) {
            player.getAchievementDiaries().update(VarrockDiary.CATCH_TROUT);
            player.getDailyChallengeManager().update(SkillingChallenge.CATCH_TROUT);
            player.getAchievementDiaries().update(KourendDiary.FISH_A_TROUT);
        } else if (fish.equals(FishDefinitions.SALMON)) {
            player.getAchievementDiaries().update(LumbridgeDiary.CATCH_SALMON);
        } else if (fish.equals(FishDefinitions.ANCHOVIES)) {
            player.getAchievementDiaries().update(LumbridgeDiary.CATCH_ANCHOVIES);
        } else if (fish.equals(FishDefinitions.KARAMBWAN)) {
            player.getAchievementDiaries().update(KaramjaDiary.CATCH_A_KARAMBWAN);
        } else if (fish.equals(FishDefinitions.DARK_CRAB)) {
            player.getAchievementDiaries().update(WildernessDiary.FISH_AND_COOK_DARK_CRAB, 1);
        } else if (fish.equals(FishDefinitions.MACKERAL)) {
            player.getAchievementDiaries().update(KandarinDiary.CATCH_A_MACKEREL);
        } else if (fish.equals(FishDefinitions.BASS)) {
            player.getAchievementDiaries().update(KandarinDiary.CATCH_AND_COOK_BASS, 1);
            player.getAchievementDiaries().update(WesternProvincesDiary.FISH_BASS);
        } else if (fish.equals(FishDefinitions.LEAPING_STURGEON)) {
            player.getAchievementDiaries().update(KandarinDiary.CATCH_LEAPING_STURGEON);
        } else if (fish.equals(FishDefinitions.SHARK)) {
            AdventCalendarManager.increaseChallengeProgress(player, 2022, 18, 1);
            SherlockTask.CATCH_RAW_SHARK.progress(player);
            player.getDailyChallengeManager().update(SkillingChallenge.CATCH_SHARKS);
        } else if (fish.equals(FishDefinitions.ANGLERFISH)) {
            player.getAchievementDiaries().update(KourendDiary.CATCH_ANGLERFISH, 1);
            player.getDailyChallengeManager().update(SkillingChallenge.CATCH_ANGLERFISH);
            AdventCalendarManager.increaseChallengeProgress(player, 2022, 6, 1);
        } else if (fish.equals(FishDefinitions.KARAMBWANJI)) {
            player.getDailyChallengeManager().update(SkillingChallenge.CATCH_KARAMBWANJI);
        } else if (fish.equals(FishDefinitions.MONKFISH)) {
            player.getDailyChallengeManager().update(SkillingChallenge.CATCH_MONKFISH);
        } else if (fish.equals(FishDefinitions.SWORDFISH)) {
            player.getDailyChallengeManager().update(SkillingChallenge.CATCH_SWORDFISH);
        } else if (fish.equals(FishDefinitions.LAVA_EEL)) {
            player.getAchievementDiaries().update(WildernessDiary.FISH_RAW_LAVA_EEL);
        }
        if (fish.equals(FishDefinitions.HERRING)) {
            CharlieTask.FISH_A_HERRING.progress(player);
        } else if (fish.equals(FishDefinitions.TROUT)) {
            CharlieTask.FISH_A_TROUT.progress(player);
        }
        //TODO
        //player.getAchievementDiaries().update(ArdougneDiary.CATCH_FISH_ON_FISHING_PLATFORM);
        player.getAchievementDiaries().update(KaramjaDiary.USE_FISHING_SPOTS);
        if (defs == SpotDefinitions.MINNOW) {
            final int amount = 10 + Math.min(4, ((player.getSkills().getLevel(SkillConstants.FISHING) - 82) / 3));
            player.getInventory().addOrDrop(new Item(fish.getId(), amount));
            player.sendFilteredMessage("You catch some minnows in your net.");
        } else if (fish.equals(FishDefinitions.KARAMBWANJI)) {
            final int amount = (int) (1 + Math.floor(player.getSkills().getLevel(SkillConstants.FISHING) / 5.0F));
            final boolean incinerated = incinerate(fish, amount);
            if (!incinerated) {
                player.getInventory().addItem(fish.getId(), amount).onFailure(remainder -> World.spawnFloorItem(remainder, player));
                player.sendFilteredMessage("You catch " + ((amount > 1) ? "some" : "a") + " karambwanji in your net.");
            } else {
                player.sendFilteredMessage("Your infernal harpoon instantly incinerates the karambwanji.");
            }
        } else {
            int diaryChance = 0;
            for (final Map.Entry<DiaryReward, Integer> entry : diaryRewardMap.entrySet()) {
                if (DiaryUtil.eligibleFor(entry.getKey(), player) && player.getEquipment().containsItem(entry.getKey().getItem())) {
                    diaryChance = entry.getValue();
                }
            }
            int amount = (player.getPerkManager().isValid(PerkWrapper.MASTER_FISHERMAN) && Utils.random(100) <= 15 || diaryChance > 0 && Utils.random(100) <= diaryChance) ? 2 : 1;
            amount = (int) (amount * determineGatheringMultiplier(player).orElse(1.0));

            onGather(player);

            final boolean incinerated = incinerate(fish, amount);
            if (!incinerated) {
                if (tool.id == CrystalTool.Harpoon.INSTANCE.getProductItemId())
                    handleCrystalHarpoonDegrading(amount);
                player.getInventory().addItem(fish.getId(), amount).onFailure(remainder -> World.spawnFloorItem(remainder, player));
                player.sendFilteredMessage("You catch " + Utils.checkPlural(fish.getName()) + ".");
            } else {
                player.sendFilteredMessage("Your infernal harpoon instantly incinerates the " + fish.getName() + ".");
            }
            if (amount == 2) {
                player.getPerkManager().consume(PerkWrapper.MASTER_FISHERMAN);
            }
        }
        player.getSkills().addXp(SkillConstants.FISHING, fish.getXp());
        if (barbarian) {
            player.getSkills().addXp(SkillConstants.AGILITY, fish.getBarbarianXp());
            player.getSkills().addXp(SkillConstants.STRENGTH, fish.getBarbarianXp());
        }
        if(player.getBoonManager().hasBoon(BarbarianFisher.class) && BarbarianFisher.roll()) {
            player.getSkills().addXp(SkillConstants.AGILITY, fish.getXp());
            player.getSkills().addXp(SkillConstants.STRENGTH, fish.getXp());
        }
        if (Utils.random(999) == 0 && player.getSkills().getLevelForXp(SkillConstants.FISHING) >= 34) {
            Item item = null;
            if (!player.containsItem(ItemId.ANGLER_BOOTS)) {
                player.getInventory().addOrDrop(item = new Item(ItemId.ANGLER_BOOTS));
                player.sendMessage("A pair of angler boots washes up ashore; you catch them before they drift away.");
            } else if (!player.containsItem(ItemId.ANGLER_HAT)) {
                player.getInventory().addOrDrop(item = new Item(ItemId.ANGLER_HAT));
                player.sendMessage("An angler's hat washes up ashore; you catch it before it drifts away.");
            } else if (!player.containsItem(ItemId.ANGLER_WADERS)) {
                player.getInventory().addOrDrop(item = new Item(ItemId.ANGLER_WADERS));
                player.sendMessage("A pair of angler's waders washes up ashore; you catch it before it drifts away.");
            } else if (!player.containsItem(ItemId.ANGLER_TOP)) {
                player.getInventory().addOrDrop(item = new Item(ItemId.ANGLER_TOP));
                player.sendMessage("An angler top washes up ashore; you catch it before it drifts away.");
            }
            if (item != null) {
                player.setForceTalk(new ForceTalk("Ooh! I found something."));
                player.getCollectionLog().add(item);
            }
        }
        ClueItemUtil.roll(player, defs.getBaseClueBottleChance(), player.getSkills().getLevel(SkillConstants.FISHING), ClueItem::getClueBottle);
    }

    private void handleCrystalHarpoonDegrading(int amount) {
        Optional.ofNullable(player.getEquipment().getAny(tool.id))
                .or(() -> Optional.ofNullable(player.getInventory().getAny(tool.id)))
                        .ifPresent(item -> {
                            final Container container = player.getEquipment().containsItem(item)
                                    ? player.getEquipment().getContainer()
                                    : player.getInventory().getContainer();
                            final int slot = container.getSlot(item);
                            player.getChargesManager().removeCharges(item, amount, container, slot);
                        });
    }

    private boolean incinerate(final FishDefinitions fish, final int amount) {
        final boolean isUsingInfernal = tool.id == 21031;
        if (isUsingInfernal) {
            if (Utils.random(2) == 0) {
                final Item weapon = player.getWeapon();
                if (weapon != null && weapon.getId() == 21031 && weapon.getCharges() > 0) {
                    incinerate(fish, amount, weapon, EquipmentSlot.WEAPON.getSlot(), player.getEquipment().getContainer());
                    return true;
                }
                for (final Int2ObjectMap.Entry<Item> entryset : player.getInventory().getContainer().getItems().int2ObjectEntrySet()) {
                    final Item value = entryset.getValue();
                    if (value == null) {
                        continue;
                    }
                    if (value.getId() == 21031 && value.getCharges() > 0) {
                        incinerate(fish, amount, value, entryset.getIntKey(), player.getInventory().getContainer());
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void incinerate(final FishDefinitions fish, final int amount, final Item harpoon, final int slot, final Container container) {
        player.getChargesManager().removeCharges(harpoon, amount, container, slot);
        player.sendSound(2577);
        player.setGraphics(BURN_GFX);
        for (int i = 0; i < amount; i++) {
            final CookingDefinitions.CookingData cookable = CookingDefinitions.CookingData.getData(fish.getId());
            if (cookable != null) {
                player.getSkills().addXp(SkillConstants.COOKING, cookable.getXp() / 2.0F);
            }
        }
    }

    private boolean check() {
        return (checkBait() && checkLevel() && player.getInventory().checkSpace());
    }

    private boolean checkLevel() {

        if (CrystalTool.Harpoon.INSTANCE.is(tool)) {
            final int requiredLevel = CrystalTool.Harpoon.INSTANCE.getSkillRequirement().getSecond();
            if (requiredLevel > player.getSkills().getLevel(SkillConstants.FISHING)) {
                final String name = ItemDefinitions.nameOf(tool.id).toLowerCase();
                player.getDialogueManager().start(new PlainChat(player, "You need to be at least level " + requiredLevel + " Fishing to use "+Utils.getAOrAn(name)+" "+name+"."));
                return false;
            }
        }

        final FishDefinitions fish = defs.getLowestTierFish();
        if (fish.getLevel() > player.getSkills().getLevel(SkillConstants.FISHING)) {
            player.getDialogueManager().start(new PlainChat(player, "You need to be at least level " + fish.getLevel() + " Fishing to " + defs.getActions()[0].toLowerCase() + " these fish."));
            return false;
        }
        if (fish.getBarbarianLevel() > player.getSkills().getLevel(SkillConstants.AGILITY) || fish.getBarbarianLevel() > player.getSkills().getLevel(SkillConstants.STRENGTH)) {
            player.getDialogueManager().start(new PlainChat(player, "You need to be at least level " + fish.getBarbarianLevel() + " in Agility and Strength to catch these fish."));
            return false;
        }
        return true;
    }

    private int perkFishingCounter = 0;

    private boolean checkBait() {
        final FishingBait[] baits = defs.getBait();
        if (baits == null) {
            return true;
        }
        for (final FishingBait bait : baits) {
            if (player.getInventory().containsItem(bait.getId(), 1) || (player.getBoonManager().hasBoon(SwissArmyMan.class) && perkFishingCounter < 100)) {
                perkFishingCounter++;
                return true;
            } else if(player.getBoonManager().hasBoon(SwissArmyMan.class)) {
                player.getDialogueManager().start(new PlainChat(player, "Your SwissArmy tackle set has run out of bait! Better start with a new one."));
                perkFishingCounter = 0;
                return false;
            }
        }
        player.getDialogueManager().start(new PlainChat(player, "You don't have any bait for this fishing spot!"));
        return false;
    }

    private boolean checkSpot() {
        if (spot.getTicks() <= 1 || spot.isFinished() || spot.getPosition().getPositionHash() != posHash) {
            player.setAnimation(Animation.STOP);
            player.setFaceEntity(null);
            return false;
        }
        return true;
    }
}

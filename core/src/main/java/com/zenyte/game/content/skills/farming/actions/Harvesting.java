package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.boons.impl.SwissArmyMan;
import com.zenyte.game.content.skills.farming.*;
import com.zenyte.game.content.skills.farming.hespori.HesporiInstance;
import com.zenyte.game.content.skills.woodcutting.actions.Woodcutting;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.containers.HerbSack;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

/**
 * @author Kris | 04/02/2019 18:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Harvesting extends Action {
    private static final SoundEffect DAMAGE_SOUND = new SoundEffect(1138, 0, 0);

    public Harvesting(final FarmingSpot spot) {
        this.spot = spot;
    }

    private final FarmingSpot spot;

    private boolean fast;

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public boolean isFast() {
        return fast;
    }

    @Override
    public boolean start() {
        if (spot.getPatch().getType() == PatchType.HESPORI_PATCH) {
            final boolean dismiss = player.getAttributes().containsKey("start_hespori_fight");
            if (dismiss) {
                HesporiInstance.start(player);
                return false;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Do you want to fight the Hespori?", new DialogueOption("Yes.", () -> HesporiInstance.start(player)), new DialogueOption("Yes (Don't show this again).", () -> {
                        HesporiInstance.start(player);
                        player.getAttributes().put("start_hespori_fight", 1);
                    }), new DialogueOption("No."));
                }
            });
            return false;
        }
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some free inventory space to harvest the produce.");
            return false;
        }
        //Apparently secateurs are no longer needed for herb-harvesting.
        /*if (spot.getPatch().getType().equals(PatchType.HERB_PATCH) && !FarmingConstants.hasSecateurs(player)) {
            player.sendMessage("You need secateurs in order to harvest this.");
            return false;
        }*/
        if (spot.getPatch().getType().equals(PatchType.CELASTRUS_PATCH)) {
            final Optional<Woodcutting.AxeResult> axe = Woodcutting.getAxe(player);
            if (!player.hasBoon(SwissArmyMan.class) && !axe.isPresent()) {
                player.sendMessage("You need an axe to harvest the Celastrus tree.");
                return false;
            }
        }
        if (!spot.isTreePatch()) {
            player.sendFilteredMessage("You begin to harvest the " + spot.getPatch().getType().getSanitizedName() + ".");
        }
        harvest();
        delay(isFast() ? 1 : 3);
        return true;
    }

    public void harvestLimpwurt() {
        updateDiaries();
        updateChallenges();
        player.getFarming().handleContractCompletion(player, spot.getProduct());

        final PatchType type = spot.getPatch().getType();
        final Animation animation = type.getHarvestAnimation();
        final SoundEffect sound = type.getHarvestSoundEffect();
        if (animation != null) {
            player.setAnimation(animation);
        }
        if (sound != null) {
            player.getPacketDispatcher().sendSoundEffect(sound);
        }
        int amt = 0;
        FarmingProduct product = spot.getProduct();
        while(!spot.isClear()) {
            amt++;
            spot.removeFruit();
            if(spot.getValue() <= 3) {
                spot.clear();
            }
        }
        player.getSkills().addXp(SkillConstants.FARMING, product.getHarvestExperience());
        player.getInventory().addOrDrop(new Item(product.getProduct().getId(), amt));
        player.sendFilteredMessage("The " + spot.getPatch().getType().getSanitizedName() + " is now empty.");
        spot.refresh();
        player.getTemporaryAttributes().remove("harvesting");
        sendHarvestMessage();
    }

    @Override
    public boolean process() {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some free inventory space to harvest the produce.");
            player.setAnimation(Animation.STOP);
            return false;
        }
        return true;
    }

    @Override
    public int processWithDelay() {
        final PatchType type = spot.getPatch().getType();
        final double chanceOfLosingLife = spot.successProbability();
        final double rate = Math.max(50, 1000 * chanceOfLosingLife);
        if (Utils.random((int) rate) == 0) {
            player.getInventory().addOrDrop(new Item(22875, 1));
            player.sendMessage("You find a Hespori seed.");
        }
        if (type == PatchType.BELLADONNA_PATCH) {
            harvestBelladonna();
            return -1;
        } else if (spot.getProduct() == FarmingProduct.WHITE_LILY) {
            harvestWhitelily();
            return -1;
        } else if(spot.getProduct() == FarmingProduct.LIMPWURT) {
            harvestLimpwurt();
            return -1;
        }
        final SoundEffect sound = type.getPickSoundEffect();
        if (sound != null) {
            player.sendSound(sound);
        }
        updateDiaries();
        updateChallenges();
        if (spot.getProduct().equals(FarmingProduct.CELASTRUS)) {
            player.getAchievementDiaries().update(KourendDiary.CREATE_BATTLESTAFF, 1);
        }
        final HerbSack herbSack = player.getHerbSack();
        if(herbSack != null && spot.getPatch().getType() == PatchType.HERB_PATCH &&
                player.getInventory().containsItem(com.zenyte.plugins.item.HerbSack.HERB_SACK_OPEN)
                && herbSack.isOpen() && !herbSack.isFull(spot.getProduct().getProductHarvest(player)) &&
                ArrayUtils.contains(HerbSack.HERBS, spot.getProduct().getProduct().getId())) {
            herbSack.getContainer().add(spot.getProduct().getProductHarvest(player));
            herbSack.getContainer().refresh(player);
        } else
            player.getInventory().addItem(spot.getProduct().getProductHarvest(player));
        player.getSkills().addXp(SkillConstants.FARMING, spot.getProduct().getHarvestExperience());
        sendHarvestMessage();

        final FarmingProduct product = spot.getProduct();
        spot.removeFruit();
        if (spot.getValue() <= 3) {
            player.sendFilteredMessage("The " + spot.getPatch().getType().getSanitizedName() + " is now empty.");
            player.getFarming().handleContractCompletion(player, product);
            spot.clear();
            if(Utils.random(500) == 1) {
                player.sendMessage(Colour.RS_RED.wrap("You find a piece of a farmer's outfit when clearing the patch."));
                player.getInventory().addOrDrop(new Item(getRandomPieceId()));
            }
            player.getTemporaryAttributes().remove("harvesting");
            return -1;
        }
        if (spot.isClear() || spot.bearsFruit() && spot.isFruitless()) {
            player.getTemporaryAttributes().remove("harvesting");
            return -1;
        }
        harvest();
        return isFast() ? 1 : 2;
    }

    private int getRandomPieceId() {
        int roll = Utils.random(3);
        return switch (roll) {
            case 1 -> ItemId.FARMERS_BORO_TROUSERS;
            case 2 -> ItemId.FARMERS_JACKET;
            case 3 -> ItemId.FARMERS_BOOTS;
            default -> ItemId.FARMERS_STRAWHAT;
        };
    }

    private void updateDiaries() {
        final FarmingProduct product = spot.getProduct();
        if (product.equals(FarmingProduct.MARRENTILL)) {
            player.getDailyChallengeManager().update(SkillingChallenge.HARVEST_MARRENTILLS);
        } else if (product.equals(FarmingProduct.RANARR)) {
            player.getDailyChallengeManager().update(SkillingChallenge.HARVEST_RANARRS);
        } else if (product.equals(FarmingProduct.SNAPDRAGON)) {
            player.getDailyChallengeManager().update(SkillingChallenge.HARVEST_SNAPDRAGONS);
        } else if (product.equals(FarmingProduct.SNAPE_GRASS)) {
            player.getDailyChallengeManager().update(SkillingChallenge.HARVEST_SNAPE_GRASS);
        }
    }

    private void updateChallenges() {
        final int id = spot.getProduct().getProduct().getId();
        final FarmingPatch patch = spot.getPatch();
        final FarmingProduct product = spot.getProduct();
        if (patch.equals(FarmingPatch.CATHERBY_FLOWER) && id == 225) {
            player.getAchievementDiaries().update(KandarinDiary.PICK_LIMPWURT_ROOT);
        } else if (patch.equals(FarmingPatch.CATHERBY_HERB) && id == 217) {
            player.getAchievementDiaries().update(KandarinDiary.PICK_DWARF_WEED);
        } else if (patch.equals(FarmingPatch.ARDOUGNE_HERB) && id == 219) {
            player.getAchievementDiaries().update(ArdougneDiary.PICK_TORSTOL);
        } else if (patch.equals(FarmingPatch.ARDOUGNE_BUSH) && product.equals(FarmingProduct.POISON_IVY)) {
            player.getAchievementDiaries().update(ArdougneDiary.PICK_POISON_IVY_BERRIES);
        } else if (patch.equals(FarmingPatch.CANIFIS_MUSHROOM) && product.equals(FarmingProduct.MUSHROOM)) {
            player.getAchievementDiaries().update(MorytaniaDiary.HARVEST_BITTERCAP_MUSHROOMS);
        }
    }

    private void harvestWhitelily() {
        assert spot.getLives() > 0;
        updateDiaries();
        player.getSkills().addXp(SkillConstants.FARMING, spot.getProduct().getHarvestExperience());
        player.getInventory().addOrDrop(spot.getProduct().getProductHarvest(player));
        player.getFarming().handleContractCompletion(player, spot.getProduct());
        player.sendFilteredMessage("The " + spot.getPatch().getType().getSanitizedName() + " is now empty.");
        spot.clear();
        spot.refresh();
    }

    /**
     * Belladonna patch functions in mysterious ways - You harvest the patch in a single take.
     */
    private void harvestBelladonna() {
        assert spot.getLives() > 0;
        if (player.getEquipment().getId(EquipmentSlot.HANDS) == -1) {
            player.getPacketDispatcher().sendSoundEffect(DAMAGE_SOUND);
            player.applyHit(new Hit(2, HitType.POISON));
            player.sendMessage("You have been poisoned by the belladonna!");
            return;
        }
        player.getAchievementDiaries().update(LumbridgeDiary.PICK_BELLADONNA);
        player.getSkills().addXp(SkillConstants.FARMING, spot.getProduct().getHarvestExperience());
        player.getFarming().handleContractCompletion(player, spot.getProduct());
        final Container container = player.getInventory().getContainer();
        int count = 100;
        final FarmingProduct product = spot.getProduct();
        while (--count > 0 && spot.checkHarvest() > 0 && product == FarmingProduct.BELLADONNA) {
            //You only receive belladonna produce if you have inventory space; excess belladonna is not dropped.
            container.add(product.getProductHarvest(player));
        }
        container.refresh(player);
        sendHarvestMessage();
    }

    private void sendHarvestMessage() {
        final FarmingProduct product = spot.getProduct();
        if (product == FarmingProduct.CACTUS) {
            player.sendFilteredMessage("You carefully pick a spine from the cactus.");
        } else if (product == FarmingProduct.BELLADONNA) {
            player.sendFilteredMessage("You pick some Deadly Nightshade.");
        } else if (product == FarmingProduct.GIANT_SEAWEED) {
            player.sendFilteredMessage("You pick some giant seaweed.");
        }
    }

    private void harvest() {
        final PatchType type = spot.getPatch().getType();
        if (type == PatchType.CELASTRUS_PATCH) {
            Woodcutting.getAxe(player).ifPresent(axe -> player.setAnimation(axe.getDefinition().getTreeCutAnimation()));
            return;
        }
        final FarmingProduct product = spot.getProduct();
        if (product == FarmingProduct.STRAWBERRY) {
            player.getAchievementDiaries().update(ArdougneDiary.HARVEST_STRAWBERRIES);
        } else if (product == FarmingProduct.WATERMELON) {
            player.getAchievementDiaries().update(MorytaniaDiary.HARVEST_WATERMELON);
        }
        final Animation animation = type.getHarvestAnimation();
        final SoundEffect sound = type.getHarvestSoundEffect();
        if (animation != null) {
            player.setAnimation(animation);
        }
        if (sound != null) {
            player.getPacketDispatcher().sendSoundEffect(sound);
        }
    }
}

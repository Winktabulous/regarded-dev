package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.content.boons.impl.FarmersFortune;
import com.zenyte.game.content.boons.impl.SwissArmyMan;
import com.zenyte.game.content.skills.farming.*;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.RSColour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.object.OldFirePit;

import static com.zenyte.game.content.skills.farming.PatchState.WEEDS;

/**
 * @author Kris | 03/02/2019 17:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Planting extends Action {
    private static final Animation SPADE_ANIM = new Animation(830);
    private static final Animation SEED_DIPPING_ANIM = new Animation(2291);
    private static final SoundEffect SPADE_PLANTING_SOUND = new SoundEffect(1470);
    private static final SoundEffect SEED_PLANTING_SOUND = new SoundEffect(2432);
    private static final Item EMPTY_POT = new Item(5350);
    private static final Item SEED_DIBBER = new Item(5343);
    private final FarmingSpot spot;
    private final FarmingProduct product;

    public Planting(final FarmingSpot spot, final FarmingProduct product) {
        this.spot = spot;
        this.product = product;
    }

    @Override
    public boolean start() {
        final FarmingPatch patch = spot.getPatch();
        final PatchType type = patch.getType();
        if (product.getType() != type) {
            player.sendMessage("You can only plant " + product.getSeedsPlural() + " in " + product.getPatchName() + ".");
            return false;
        }
        if (!spot.isClear()) {
            player.getDialogueManager().start(new PlainChat(player, "You can only plant " + product.getSeedsPlural() + " in an empty patch."));
            return false;
        }
        if (player.getSkills().getLevel(SkillConstants.FARMING) < product.getLevelRequired()) {
            player.sendMessage("You need a farming level of at least " + product.getLevelRequired() + " to plant this.");
            return false;
        }
        if (patch == FarmingPatch.TROLL_STRONGHOLD_HERB) {
            if (product == FarmingProduct.GOUTWEED) {
                player.getDialogueManager().start(new ItemChat(player, new Item(6311), "You can't plant goutweed in " +
                        "this patch."));
                return false;
            }
        }
        if (patch == FarmingPatch.WEISS_HERB_PATCH) {
            if (!OldFirePit.FirePit.WEISS_FIRE.isBuilt(player)) {
                player.getDialogueManager().start(new PlainChat(player, "The crops can't grow in areas of extreme " +
                        "cold. Perhaps you should look into warming up the area around first."));
                return false;
            }
        }
        if (type == PatchType.SPIRIT_TREE_PATCH) {
            final int level = player.getSkills().getLevel(SkillConstants.FARMING);
            if (level < 99) {
                final int spiritTreeCount = player.getFarming().getGrownCount(PatchType.SPIRIT_TREE_PATCH, spot -> !spot.getState().equals(WEEDS));
                if (spiritTreeCount >= 2 || spiritTreeCount >= 1 && level < 91) {
                    player.getDialogueManager().start(new ItemChat(player, new Item(6063), "You can only have " + (level < 91 ? "one" : "two") + " spirit tree" + (level < 91 ? "" : "s") + " planted at a time."));
                    return false;
                }
            }
        }
        if (!player.getInventory().containsItems(product.getSeed())) {
            player.sendMessage("You need " + product.getSeed().getAmount() + " " + product.getSeed().getDefinitions().getName() + " to plant this.");
            return false;
        }
        final boolean tree = product.isTree();
        if (tree) {
            if (!player.hasBoon(SwissArmyMan.class) && !player.getInventory().containsItem(FarmingConstants.SPADE)) {
                player.sendMessage("You need a spade to plant this.");
                return false;
            }
        } else {
            if (!player.hasBoon(SwissArmyMan.class) && !player.getInventory().containsItem(SEED_DIBBER)) {
                player.sendMessage("You need a seed dibber to plant this.");
                return false;
            }
        }
        if (!type.equals(PatchType.GIANT_SEAWEED_PATCH)) {
            player.setAnimation(tree ? SPADE_ANIM : SEED_DIPPING_ANIM);
        }
        player.getPacketDispatcher().sendSoundEffect(tree ? SPADE_PLANTING_SOUND : SEED_PLANTING_SOUND);
        delay(tree ? 2 : 3);
        player.lock(tree ? 2 : 3);
        return true;
    }

    @Override
    public boolean process() {
        return spot.isClear();
    }

    @Override
    public int processWithDelay() {
        if (spot.getPatch().equals(FarmingPatch.MCGRUBOR_HOPS) && product.equals(FarmingProduct.JUTE)) {
            player.getAchievementDiaries().update(KandarinDiary.PLANT_JUTE_SEEDS);
        }
        player.sendMessage("You plant " + product.getSeedName() + " in the " + product.getType().getSanitizedName() + ".");
        if (product == FarmingProduct.WATERMELON) {
            SherlockTask.PLANT_WATERMELON_SEED.progress(player);
        }
        player.getSkills().addXp(SkillConstants.FARMING, product.getPlantExperience());
        final Item item = new Item(product.getSeed());
        if (product.getType() == PatchType.ALLOTMENT) {
            if (player.getEquipment().getId(EquipmentSlot.AMULET) == 21160) {
                if (Utils.random(3) == 0) {
                    final int uses = player.getNumericAttribute("amulet of bounty uses").intValue() + 1;
                    player.addAttribute("amulet of bounty uses", uses % 10);
                    item.setAmount(1);
                    if (uses == 10) {
                        player.getEquipment().set(EquipmentSlot.AMULET, null);
                        player.sendMessage("Your amulet of bounty allows you to plant the allotment with just one seed. " + Colour.RED.wrap("It then crumbles to dust."));
                    } else {
                        player.sendFilteredMessage("Your amulet of bounty allows you to plant the allotment with just one seed. " + Colour.RED.wrap("It has " + (10 - uses) + " charge" + (uses == 9 ? "" : "s") + " left."));
                    }
                }
            }
        }
        if(player.hasBoon(FarmersFortune.class) && FarmersFortune.roll()) {
            player.sendFilteredMessage(Colour.RS_GREEN.wrap("Your Farmer's Fortune perk saves your supplies from being used"));
        } else {
            player.getInventory().deleteItem(item);
        }
        spot.setProduct(product);
        if (product.isTree()) {
            player.getInventory().addItem(EMPTY_POT);
        }
        return -1;
    }
}

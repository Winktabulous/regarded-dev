package com.zenyte.game.content.pyramidplunder.npc;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.pyramidplunder.PlunderReward;
import com.zenyte.game.content.pyramidplunder.PlunderRewardTier;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.types.config.items.ItemDefinitions;

import java.util.ArrayList;
import java.util.List;

import static com.zenyte.game.model.item.ItemChain.PHARAOH_SCEPTRE;

/**
 * @author Chris
 * @since May 20 2020
 */
public class GuardianMummyDialogue extends Dialogue {
    private static final int GAME_EXPLANATION_KEY = 10;
    private static final int SECOND_OPTION_KEY = 20;
    private static final int LEAVE_GAME_KEY = 30;
    private static final int EXPLAIN_ARTEFACTS_KEY = 40;
    private static final int SELL_ARTEFACTS_KEY = 50;
    private static final int READY_KEY = 60;
    private static final int CHARGE_KEY = 70;
    private static final int GOLD_KEY = 75;
    private static final int STONE_KEY = 80;
    private static final int POTTERY_IVORY_KEY = 85;
    private static final int REMOVE_CHARGE_KEY = 95;
    private static final int MISSING_SCEPTRE_KEY = 100;
    private final boolean onlyChargeDialogue;

    public GuardianMummyDialogue(Player player, final NPC npc, final boolean onlyChargeDialogue) {
        super(player, npc);
        this.onlyChargeDialogue = onlyChargeDialogue;
    }

    @Override
    public void buildDialogue() {
        final int chargeKey = player.carryingItem(PHARAOH_SCEPTRE.first()) ? CHARGE_KEY : player.carryingAny(PHARAOH_SCEPTRE.getAllButFirst()) ? REMOVE_CHARGE_KEY : MISSING_SCEPTRE_KEY;
        {
            player(onlyChargeDialogue ? 1 : CHARGE_KEY, "This sceptre seems to have run out of charges.");
            npc("You shouldn\'t have that thing in the first place, thief!");
            player("If I gave you back some of the artefacts I\'ve taken from the tomb, would you recharge the sceptre for me?");
            npc("*sigh* Oh alright. But only if the sceptre is fully empty, I\'m not wasting the King\'s magic...");
            options("Recharge the sceptre with...", new DialogueOption("Gold artefacts?", () -> {
                charge(player, GOLD_KEY);
                setKey(GOLD_KEY);
            }), new DialogueOption("Stone artefacts?", () -> {
                charge(player, STONE_KEY);
                setKey(STONE_KEY);
            }), new DialogueOption("Pottery and Ivory artefacts?", () -> {
                charge(player, POTTERY_IVORY_KEY);
                setKey(POTTERY_IVORY_KEY);
            }), new DialogueOption("Actually, I\'m more interested in plundering the tombs.", () -> setKey(1)));
            plain(GOLD_KEY, "You recharge your sceptre with gold artefacts.");
            plain(STONE_KEY, "You recharge your sceptre with stone artefacts.");
            plain(POTTERY_IVORY_KEY, "You recharge your sceptre with pottery and ivory artefacts.");
        }
        if (!onlyChargeDialogue) {
            options(1, "Play the \'Pyramid Plunder\' minigame?", new DialogueOption("That sounds like fun, what do I do?", key(GAME_EXPLANATION_KEY)), new DialogueOption("Not right now."), new DialogueOption("I know what I\'m doing - let\'s get on with it.", key(READY_KEY)), new DialogueOption("I want to charge or remove charges from my sceptre.", key(chargeKey)));
            {
                player(GAME_EXPLANATION_KEY, "That sounds like fun, what do I do?");
                npc("You have five minutes to explore the treasure rooms and collect as many artefacts as you can.");
                npc("The artefacts are in the urns, chests and sarcophagi found in each room.");
                npc("There are eight treasure rooms, each subsequent room requires higher thieving skills to both enter the room and thieve from the urns and other containers.");
                npc("The rewards also become more lucrative the further into the tomb you go.");
                npc("You will also have to deactivate a trap in order to enter the main part of each room.");
                npc("When you want to move onto the next room you need to find the correct door first.");
                npc("There are four possible exits, you must open the door before finding out whether it is the exit or not.");
                npc("Opening the doors require picking their locks. Having a lockpick will make this easier.").executeAction(() -> setKey(SECOND_OPTION_KEY));
            }
            {
                player(READY_KEY, "I know what I\'m doing - let\'s get on with it.");
                npc("Fine, I\'ll take you to the first room now...").executeAction(() -> GuardianMummy.startGame(player));
            }
            options(SECOND_OPTION_KEY, "Do you have any more questions?", new DialogueOption("How do I leave the game?", key(LEAVE_GAME_KEY)), new DialogueOption("Tell me about these artefacts.", key(EXPLAIN_ARTEFACTS_KEY)), new DialogueOption("What do I do with the artefacts I collect?", key(SELL_ARTEFACTS_KEY)), new DialogueOption("I\'m ready to give it a go now.", key(READY_KEY)));
            {
                player(LEAVE_GAME_KEY, "How do I leave the game?");
                npc("If at any point you decide you need to leave just use a glowing door.");
                npc("The game will end and you will be taken out of the pyramid.").executeAction(() -> setKey(SECOND_OPTION_KEY));
            }
            {
                player(EXPLAIN_ARTEFACTS_KEY, "Tell me about these artefacts.");
                npc("There are a number of different artefacts, of three main types. The least valuable are the pottery statuettes and scarabs, and the ivory combs.");
                npc("Next are the stone scarabs, statuettes and seals, and finally the gold versions of those artefacts.");
                npc("They are not old, but are well made. The urns contains all types of artefacts while the chest and sarcophagus contain stone and gold artefacts.").executeAction(() -> setKey(SECOND_OPTION_KEY));
            }
            {
                player(SELL_ARTEFACTS_KEY, "What do I do with the artefacts I collect?");
                npc("That Simon Simpleton, I mean Templeton, will probably give you some money for them.");
                npc("He couldn\'t spot a real artefact if it came up to him and bit him in the face.");
                npc("He usually slinks about near the pyramid north-east of Sophanem. I expect he\'s trying to get some poor fools to steal things from that pyramid as well.");
                npc("I expect he\'ll give you some more gold for some than others.").executeAction(() -> setKey(SECOND_OPTION_KEY));
            }
            {
                player(REMOVE_CHARGE_KEY, "Can you remove charges from my sceptre?").executeAction(() -> {
                    uncharge(player);
                });
                npc("It is done.");
            }
            npc(MISSING_SCEPTRE_KEY, "You don\'t have any sceptres.");
        }
    }

    public static void uncharge(final Player player) {
        if (player.carryingAny(PHARAOH_SCEPTRE.getAllButFirst())) {
            // Check if players inv or equipment has a sceptre
            return;
        }
        final Container container = player.getInventory().containsAnyOf(PHARAOH_SCEPTRE.getAllButFirst()) ? player.getInventory().getContainer() : player.getEquipment().getContainer();
        final Item sceptre = container.getAny(PHARAOH_SCEPTRE.getAllButFirst());
        if (sceptre == null) {
            return;
        }
        final int slot = container.getSlot(sceptre);
        if (slot == -1) {
            return;
        }
        container.set(slot, new Item(PHARAOH_SCEPTRE.first(), 1));
        container.refresh(player);
    }

    private void charge(final Player player, final int key) {
        final Container container = player.getInventory().containsItem(PHARAOH_SCEPTRE.first()) ? player.getInventory().getContainer() : player.getEquipment().getContainer();
        final int slot = container.getSlotOf(PHARAOH_SCEPTRE.first());
        if (slot == -1) {
            return;
        }
        final ArrayList<Item> materials = new ArrayList<Item>();
        if (key == GOLD_KEY) {
            materials.addAll(getCarriedMaterials(player, key, PlunderRewardTier.GOLD));
        } else if (key == STONE_KEY) {
            materials.addAll(getCarriedMaterials(player, key, PlunderRewardTier.STONE));
        } else if (key == POTTERY_IVORY_KEY) {
            materials.addAll(getCarriedMaterials(player, key, PlunderRewardTier.POTTERY));
            materials.addAll(getCarriedMaterials(player, key, PlunderRewardTier.IVORY));
        }
        final int amount = getNeededMaterialAmount(key);
        if (materials.size() < amount) {
            player.sendMessage("You need " + amount + " artefacts of this type to charge the sceptre.");
            player.getDialogueManager().finish();
            return;
        }
        player.getInventory().deleteItemsByAmountIfContains(materials.toArray(new Item[0]), () -> {
            container.set(slot, new Item(getChargedSceptre(player), 1));
            container.refresh(player);
        });
    }

    private int getNeededMaterialAmount(final int key) {
        if (key == GOLD_KEY) {
            return 6;
        } else if (key == STONE_KEY) {
            return 12;
        } else {
            return 24;
        }
    }

    private int getChargedSceptre(final Player player) {
        int maxCharges = 3;
        if (DiaryUtil.eligibleFor(DiaryReward.DESERT_AMULET4, player)) {
            maxCharges = 8;
        } else if (DiaryUtil.eligibleFor(DiaryReward.DESERT_AMULET3, player)) {
            maxCharges = 6;
        } else if (DiaryUtil.eligibleFor(DiaryReward.DESERT_AMULET2, player)) {
            maxCharges = 5;
        } else if (DiaryUtil.eligibleFor(DiaryReward.DESERT_AMULET1, player)) {
            maxCharges = 4;
        }
        return PHARAOH_SCEPTRE.get(maxCharges);
    }

    private List<Item> getCarriedMaterials(final Player player, final int key, final PlunderRewardTier tier) {
        final ArrayList<Item> materials = new ArrayList<Item>();
        final int amountNeeded = getNeededMaterialAmount(key);
        for (PlunderReward plunderReward : PlunderReward.get(tier)) {
            final int[] rewardIds = new int[] {plunderReward.getItemId(), ItemDefinitions.get(plunderReward.getItemId()).getNotedId()};
            for (int rewardId : rewardIds) {
                if (player.carryingItem(rewardId)) {
                    final Inventory inventory = player.getInventory();
                    final Item item = inventory.getAny(rewardId);
                    if (item == null) {
                        continue;
                    }
                    final int id = item.getId();
                    final int amount = Math.min(amountNeeded - materials.size(), inventory.getAmountOf(id));
                    for (int i = 0; i < amount; i++) {
                        materials.add(new Item(id, 1));
                    }
                }
            }
        }
        return materials;
    }
}

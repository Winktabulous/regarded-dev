package com.zenyte.game.content.sandstorm.dialogue;

import com.zenyte.game.content.sandstorm.Grinder;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.UnmodifiableItem;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 20 2020
 */
public class DrewClaimDialogue extends Dialogue {
    private static final UnmodifiableItem COST_PER_BUCKET = new UnmodifiableItem(ItemId.COINS_995, 50);

    public DrewClaimDialogue(@NotNull final Player player, @NotNull final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int currentBucketsHeld = player.getNumericAttribute(DrewDepositDialogue.DREW_BUCKET_AMOUNT_KEY).intValue();
        final int currentBucketsOfSand = player.getNumericAttribute(Grinder.BUCKET_OF_SAND_AMOUNT_KEY).intValue();
        final String costString = ItemUtil.toPrettyString(COST_PER_BUCKET);
        final int notedBucketId = ItemDefinitions.get(ItemId.BUCKET_OF_SAND) == null ? ItemId.BUCKET_OF_SAND + 1 : ItemDefinitions.get(ItemId.BUCKET_OF_SAND).getNotedOrDefault();
        if (currentBucketsHeld <= 0 || currentBucketsOfSand <= 0) {
            npc("Ya don't have any bucket of sand to withdraw. Ya need to give me empty buckets, and grind sandstone " +
                    "in my grinder, then I'll sell ya buckets of sand.");
        } else if (player.carryingItem(COST_PER_BUCKET)) {
            player.sendInputInt("How many buckets of sand do ya want? (" + costString + " each)", bucketsToBuy -> {
                final Inventory inventory = player.getInventory();
                final int maxAffordableBuckets = inventory.getAmountOf(COST_PER_BUCKET.getId()) / COST_PER_BUCKET.getAmount();
                final int cappedAmount = Math.min(maxAffordableBuckets, Math.min(currentBucketsOfSand, Math.min(currentBucketsHeld, bucketsToBuy)));
                player.incrementNumericAttribute(DrewDepositDialogue.DREW_BUCKET_AMOUNT_KEY, -cappedAmount);
                player.incrementNumericAttribute(Grinder.BUCKET_OF_SAND_AMOUNT_KEY, -cappedAmount);
                inventory.addOrDrop(notedBucketId, cappedAmount);
                inventory.deleteItem(COST_PER_BUCKET.getId(), COST_PER_BUCKET.getAmount() * cappedAmount);
                player.getDialogueManager().start(new NPCChat(player, NpcId.DREW, npc, "If ya need any more sand, please come back and use Sandstorm again."));
            });
        } else {
            npc("Ya do not have enough money to withdraw even one bucket of sand; I charge " + costString + " per bucket. Come back to me when ya have more money.");
        }
    }
}

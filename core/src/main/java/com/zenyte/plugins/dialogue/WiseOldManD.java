package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.drops.DropTableBuilder;
import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

public class WiseOldManD extends Dialogue {

    public WiseOldManD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        npc("Hello, " + player.getName() + "!");
//        npc("What can I help you with, " + player.getName() + "?");
//        options("Choose an option", "Check donations", "Check vote rewards", "Cancel")
//                .onOptionOne(() -> PlayerApiKt.claimDonations(player))
//                .onOptionTwo(() -> PlayerApiKt.claimVotes(player));
    }

    private static final DropTable voteNon2faTable = new DropTableBuilder()
            .append(ClueItem.MEDIUM.getScrollBox(), 70)
            .append(ClueItem.HARD.getScrollBox(), 20)
            .append(ClueItem.ELITE.getScrollBox(), 10)
            .build();

    private static final DropTable vote2faTable =
            new DropTableBuilder()
                    .append(ClueItem.MEDIUM.getScrollBox(), 68)
                    .append(ClueItem.HARD.getScrollBox(), 20)
                    .append(ClueItem.ELITE.getScrollBox(), 10)
                    .append(ClueItem.MASTER.getScrollBox(), 2)
                    .build();

    public static void rollClues(@NotNull final Player player, final int amount) {
        if (amount <= 0) {
            return;
        }
        final int existingRolls = player.getNumericAttribute("claimed vote points").intValue();
        player.addAttribute("claimed vote points", existingRolls + amount);
        final boolean twoFactorAuthenticator = player.getAuthenticator().isEnabled();
        final Inventory inventory = player.getInventory();
        //Offset it by one so that when the player claims their very first vote, they don't immediately receive a clue.
        final int length = 1 + existingRolls + amount;
        final int interval = 3;
        final Int2IntAVLTreeMap map = new Int2IntAVLTreeMap();
        for (int i = 1 + existingRolls; i < length; i++) {
            if (i % interval != 0) {
                continue;
            }
            final Item scrollBox = generateRandomClue(twoFactorAuthenticator);
            inventory.addOrDrop(scrollBox);
            map.put(scrollBox.getId(), map.get(scrollBox.getId()) + scrollBox.getAmount());
        }
        if (map.isEmpty()) {
            return;
        }
        player.sendMessage(Colour.RED.wrap("You've received the following scroll boxes for voting: "));
        for (final Int2IntMap.Entry entry : map.int2IntEntrySet()) {
            if (entry.getIntKey() == ClueItem.MASTER.getScrollBox()) {
                player.sendMessage(Colour.RS_PURPLE.wrap("2FA Special: ") + Colour.RED.wrap(entry.getIntValue() + " x" +
                        " " + ItemDefinitions.getOrThrow(entry.getIntKey()).getName()));
                continue;
            }
            player.sendMessage(Colour.RED.wrap(entry.getIntValue() + " x " + ItemDefinitions.getOrThrow(entry.getIntKey()).getName()));
        }
    }

    private static Item generateRandomClue(final boolean twofactorAuthenticator) {
        final DropTable table = twofactorAuthenticator ? vote2faTable : voteNon2faTable;
        return table.rollItem();
    }
}

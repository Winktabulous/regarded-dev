package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.plugins.dialogue.*;

/**
 * @author Kris | 26/11/2018 18:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Banker extends NPCPlugin implements ItemOnNPCAction {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new BankerD(player, npc)));
        bind("Bank", (player, npc) -> GameInterface.BANK.open(player));
        bind("Collect", (player, npc) -> GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player));
    }

    // TODO: Route event.
    @Override
    public int[] getNPCs() {
        return new int[] {
                NpcId.BANKER_10389, NpcId.EMERALD_BENEDICT,
                NpcId.BANKER_3092, NpcId.BANKER_3093, NpcId.GUNDAI, NpcId.BANKER_2119, NpcId.BANKER_2292, NpcId.BANKER_2293, NpcId.BANKER_2368, NpcId.BANKER_2369, NpcId.GHOST_BANKER, NpcId.BANKER_3318, 3887, 3888, NpcId.BANKER_4054, NpcId.BANKER_4055, NpcId.ARNOLD_LYDSPOR, NpcId.NARDAH_BANKER, NpcId.GNOME_BANKER, NpcId.BANKER_6859, NpcId.BANKER_6860, NpcId.BANKER_6861, NpcId.BANKER_6862, NpcId.BANKER_6863, NpcId.BANKER_6864, NpcId.BANKER_6939, NpcId.BANKER_6940, NpcId.BANKER_6941, NpcId.BANKER_6942, NpcId.BANKER_6969, NpcId.BANKER_6970, NpcId.BANKER_7057, NpcId.BANKER_7058, NpcId.BANKER_7059, NpcId.BANKER_7060, NpcId.BANKER_7077, NpcId.BANKER_7078, NpcId.BANKER_7079, NpcId.BANKER_7080, NpcId.BANKER_7081, NpcId.BANKER_7082, NpcId.BANKER_8321, NpcId.BANKER_8322, 7417, 6765, NpcId.JUMAANE };
    }

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        if (item.getId() == 995 || item.getId() == 13204) {
            if (item.getId() == 995 && item.getAmount() < 1000) {
                player.sendMessage("You need at least 1,000 coins to convert the coins into platinum token(s).");
                return;
            }
            player.getDialogueManager().start(new CurrencyConversionD(player, item, slot));
            return;
        }
        if (item.getDefinitions().isNoted()) {
            player.getDialogueManager().start(new UnnoteD(player, item));
        } else {
            if (item.getDefinitions().getNotedId() == -1) {
                player.getDialogueManager().start(new PlainChat(player, "You cannot turn this into banknotes, try another item."));
                return;
            }
            player.getDialogueManager().start(new NoteD(player, item));
        }
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.COFFIN, 30401, ObjectId.GATE_1600, ObjectId.STAIRCASE_2119, ObjectId.SIGN_2292, 2293, ObjectId.SIGNPOST_2368, ObjectId.SIGNPOST_2369, 3003, 3318, ObjectId.TREE_3887, ObjectId.TREE_3888, 4054, ObjectId.HUGE_MUSHROOM, 4293, 4762, ObjectId.BANK_BOOTH, 6859, 6860, 6861, 6862, 6863, 6864, ObjectId.BONE_ARCH_6939, ObjectId.BONE_ARCH_6940, 6941, 6942, 6969, ObjectId.SWAMP_BOATY_6970, 7057, ObjectId.OLD_BOOKSHELF, ObjectId.OLD_BOOKSHELF_7059, ObjectId.OLD_BOOKSHELF_7060, ObjectId.OLD_BOOKSHELF_7077, ObjectId.OLD_BOOKSHELF_7078, ObjectId.OLD_BOOKSHELF_7079, ObjectId.OLD_BOOKSHELF_7080, ObjectId.OLD_BOOKSHELF_7081, ObjectId.OLD_BOOKSHELF_7082, ObjectId.BITTERCAP_MUSHROOMS_8321, ObjectId.BITTERCAP_MUSHROOMS_8322, ObjectId.TABLE_7417};
    }
}

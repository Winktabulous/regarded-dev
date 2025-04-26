package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.smithing.SmeltableBar;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.plugins.dialogue.BlastFurnaceBarFinishD;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BlastFurnaceBarD extends SkillDialogue {
    private final Item[] bars;

    public BlastFurnaceBarD(final Player player, final Item[] bars) {
        super(player, bars);
        this.bars = bars;
    }

    @Override
    public void run(int slotId, int amount) {
        final Item data = bars[slotId];
        if (data == null) {
            return;
        }
        final int inventorySpace = player.getInventory().getFreeSlots();
        final SmeltableBar bar = SmeltableBar.getDataByBar(data.getId());
        if (bar == null) return;
        int amt = player.getBlastFurnace().getBar(bar);
        if(player.getMemberRank().equalToOrGreaterThan(MemberRank.MYTHICAL))
            amt = player.getInventory().hasSpaceFor(data.getDefinitions().getNotedId()) ? Math.min(amt, amount) : 0;
        else
            amt = amount > amt ? (Math.min(amt, inventorySpace)) : (Math.min(amount, inventorySpace));

        if(amt == 0) {
            player.sendMessage("You don't have any inventory space to grab bars!");
            return;
        }
        player.getBlastFurnace().subBars(bar, amt);
        if(player.getMemberRank().equalToOrGreaterThan(MemberRank.MYTHICAL))
            player.getInventory().addItem(data.getDefinitions().getNotedId(), amt);
        else
            player.getInventory().addItem(data.getId(), amt);

        if (!player.getBlastFurnace().hasBars()) {
            player.getBlastFurnace().setDispenser(0);
        }
        final String payload = amt > 1 ? data.getName().toLowerCase() + "s" : data.getName().toLowerCase();
        player.getBlastFurnace().processVarbits();
        player.getDialogueManager().start(new BlastFurnaceBarFinishD(player, amt, payload));
    }
}

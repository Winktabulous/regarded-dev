
package com.zenyte.plugins.drop;

import com.zenyte.game.content.minigame.barrows.BarrowsWight;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import mgi.utilities.StringFormatUtil;
import net.runelite.api.ItemID;

/**
 * @author Kris | 09/09/2019 17:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DonatorBarrowsDropProcessor extends DropProcessor {
    @Override
    public void attach() {
        final int[] ids = getAllIds();

        appendDrop(new DisplayedDrop(ItemId.BONES, 1, 1, 1));
        appendDrop(new DisplayedDrop(ItemId.CHAOS_RUNE, 10, 30, 3));
        appendDrop(new DisplayedDrop(ItemId.DEATH_RUNE, 5, 25, 3));
        appendDrop(new DisplayedDrop(ItemId.BLOOD_RUNE, 5, 15, 3));
        appendDrop(new DisplayedDrop(ItemId.COINS_995, 1000, 3000, 5));
        appendDrop(new DisplayedDrop(32168, 1, 3, 10));
        appendDrop(new DisplayedDrop(ItemId.SCROLL_BOX_ELITE, 1, 1, 100));
        for (int brotherId : ids) {
            for (Item item : get(brotherId).getArmour()) {
                appendDrop(new DisplayedDrop(item.getId(), 1, 1, 20, (p, npcId) -> npcId == brotherId, brotherId));
                put(brotherId, item.getId(), new PredicatedDrop((p, npcId) -> npcId == brotherId, "This is only dropped by the barrows brothers found on RDI."));
            }
        }
    }


    private BarrowsWight get(int id) {
        return switch (id) {
            case 16052 -> BarrowsWight.AHRIM;
            case 16053 -> BarrowsWight.DHAROK;
            case 16054 -> BarrowsWight.GUTHAN;
            case 16055 -> BarrowsWight.KARIL;
            case 16056 -> BarrowsWight.TORAG;
            case 16057 -> BarrowsWight.VERAC;
            default -> BarrowsWight.DHAROK;
        };
    }

    @Override
    public void onDeath(NPC npc, Player killer) {
        for (DisplayedDrop basicDrop : getBasicDrops()) {
            if(basicDrop.getPredicate() == null || basicDrop.getPredicate().test(killer, npc.getId()))
                if(random((int)basicDrop.getRate()) == 0) {
                    npc.dropItem(killer, new Item(basicDrop.getId(), Utils.random(basicDrop.getMinAmount(), basicDrop.getMaxAmount())));
                    if(basicDrop.getRate() != 1)
                        break;
            }
        }
    }

    @Override
    public boolean disregardTableFromDefinitions() {
        return true;
    }

    @Override
    public int[] ids() {
        return new int[] {16052, 16053, 16054, 16055, 16056, 16057};
    }
}

package com.zenyte.game.content.achievementdiary.plugins.drop;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.boons.impl.HoleyMoley;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 02/05/2019 | 20:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GiantMoleProcessor extends DropProcessor {

    private static final int MOLE_CLAW = 7416;
    private static final int MOLE_SKIN = 7418;

    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(32058, 1, 1, 2000));

        put(MOLE_CLAW, new PredicatedDrop("This drop will be noted if you have the hard Falador diary completed."));
        put(MOLE_SKIN, new PredicatedDrop("This drop will be noted if you have the hard Falador diary completed."));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if(killer != null && killer.getBoonManager().hasBoon(HoleyMoley.class) && (item.getId() == MOLE_CLAW || item.getId() == MOLE_SKIN)) {
            int originalCount = item.getAmount();
            item.setAmount(originalCount * 2);
        }
        if ((item.getId() == MOLE_CLAW || item.getId() == MOLE_SKIN) && DiaryUtil.eligibleFor(DiaryReward.FALADOR_SHIELD3, killer)) {
            item.setId(item.getDefinitions().getNotedId());
        }
        if(npc.getId() == 5779) {
            if(randomDrop(killer, 2000) == 0) {
                npc.dropItem(killer, new Item(32058));
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 5779, 6499 };
    }
}

package com.zenyte.plugins.drop;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorMonster;
import com.zenyte.game.world.entity.player.Player;

import java.util.LinkedHashMap;

/**
 * @author Kris | 12/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SuperiorProcessor extends DropProcessor {

    public LinkedHashMap<Integer, Integer> ID_BY_RATES = new LinkedHashMap<>();

    public void buildRateTables() {
        ID_BY_RATES.put(NpcId.CRUSHING_HAND, 1000);
        ID_BY_RATES.put(NpcId.CHASM_CRAWLER, 950);
        ID_BY_RATES.put(NpcId.SCREAMING_BANSHEE, 900);
        ID_BY_RATES.put(NpcId.SCREAMING_TWISTED_BANSHEE, 875);
        ID_BY_RATES.put(NpcId.GIANT_ROCKSLUG, 850);
        ID_BY_RATES.put(NpcId.COCKATHRICE, 800);
        ID_BY_RATES.put(NpcId.FLAMING_PYRELORD, 775);
        ID_BY_RATES.put(NpcId.INFERNAL_PYRELORD, 750);
        ID_BY_RATES.put(NpcId.MONSTROUS_BASILISK, 700);
        ID_BY_RATES.put(NpcId.MALEVOLENT_MAGE, 650);
        ID_BY_RATES.put(NpcId.INSATIABLE_BLOODVELD, 600);
        ID_BY_RATES.put(NpcId.INSATIABLE_MUTATED_BLOODVELD, 550);
        ID_BY_RATES.put(NpcId.VITREOUS_JELLY, 500);
        ID_BY_RATES.put(NpcId.VITREOUS_WARPED_JELLY, 475);
        ID_BY_RATES.put(NpcId.SPIKED_TUROTH, 450);
        ID_BY_RATES.put(NpcId.CAVE_ABOMINATION, 400);
        ID_BY_RATES.put(NpcId.ABHORRENT_SPECTRE, 350);
        ID_BY_RATES.put(NpcId.REPUGNANT_SPECTRE, 325);
        ID_BY_RATES.put(NpcId.BASILISK_SENTINEL, 300);
        ID_BY_RATES.put(NpcId.CHOKE_DEVIL, 275);
        ID_BY_RATES.put(NpcId.KING_KURASK, 250);
        ID_BY_RATES.put(NpcId.MARBLE_GARGOYLE, 200);
        ID_BY_RATES.put(NpcId.NECHRYARCH, 150);
        ID_BY_RATES.put(NpcId.GREATER_ABYSSAL_DEMON, 125);
        ID_BY_RATES.put(NpcId.NIGHT_BEAST, 100);
        ID_BY_RATES.put(NpcId.NUCLEAR_SMOKE_DEVIL, 80);
    }

    @Override
    public void attach() {
        buildRateTables();
        //Actual drops are being dropped through the NPC class itself.
        for (int id : getAllIds()) {
            final int req = NPCCDLoader.get(id).getSlayerLevel();
            final double probability = 1.0F / (1.0F / (200.0F - (Math.pow(req + 55.0F, 2) / 125.0F)));
            appendDrop(new DisplayedDrop(ItemId.BRIMSTONE_KEY, 1, 1, 1.00D, (player, npcId) -> npcId == id));
            appendDrop(new DisplayedDrop(ItemId.LARRANS_KEY, 1, 1, 1.00D, (player, npcId) -> npcId == id));
            appendDrop(new DisplayedDrop(20736, 1, 1, probability / (3 / 8.0F), (player, npcId) -> npcId == id));
            appendDrop(new DisplayedDrop(20730, 1, 1, probability / (3 / 8.0F), (player, npcId) -> npcId == id));
            appendDrop(new DisplayedDrop(ItemId.IMBUED_HEART, 1, 1, ID_BY_RATES.getOrDefault(id, (int) (probability / (1 / 8.0F))), (player, npcId) -> npcId == id));
            appendDrop(new DisplayedDrop(ItemId.ETERNAL_GEM, 1, 1, ID_BY_RATES.getOrDefault(id, (int) (probability / (1 / 8.0F))), (player, npcId) -> npcId == id));
            put(id, 20736, new PredicatedDrop("Superior monsters will always roll three times on the parent NPC's " +
                    "drop table in addition to rolling once on the drops shown here."));
        }
    }

    @Override
    public void onDeath(NPC npc, Player killer) {
        npc.dropItem(killer, new Item(ItemId.BRIMSTONE_KEY));
        npc.dropItem(killer, new Item(ItemId.LARRANS_KEY));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        /* rolled item natively */
        if(item.getId() == ItemId.IMBUED_HEART || item.getId() == ItemId.ETERNAL_GEM)
            return item;
        else {
            if(randomDrop(killer, ID_BY_RATES.getOrDefault(npc.getId(), 1000)) == 0) {
                int random = Utils.random(1);
                if(random == 0)
                    npc.dropItem(killer, new Item(ItemId.IMBUED_HEART));
                else npc.dropItem(killer, new Item(ItemId.ETERNAL_GEM));
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return SuperiorMonster.superiorMonsters.toIntArray();
    }
}

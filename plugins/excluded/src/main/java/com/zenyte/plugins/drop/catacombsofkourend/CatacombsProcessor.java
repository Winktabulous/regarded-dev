package com.zenyte.plugins.drop.catacombsofkourend;

import com.near_reality.game.content.custom.SlayerHelmetEffects;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorMonster;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.CatacombsOfKourend;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 30/01/2019 20:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CatacombsProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Dark totem base
        appendDrop(new DisplayedDrop(19679, 1, 1) {
            public double getRate(final Player player, final int id) {
                final NPCCombatDefinitions definitions = NPCCDLoader.get(id);
                if (definitions == null) {
                    return 450;
                }
                return Math.max(1, 450 - definitions.getHitpoints());
            }
        });
        put(19679, new PredicatedDrop("This item is dropped in a specific order, and is only dropped if you don't already have the respective totem piece. Always dropped by superior monsters."));


        //Dark totem middle
        appendDrop(new DisplayedDrop(19681, 1, 1) {
            public double getRate(final Player player, final int id) {
                final NPCCombatDefinitions definitions = NPCCDLoader.get(id);
                return 450 - definitions.getHitpoints();
            }
        });
        put(19681, new PredicatedDrop("This item is dropped in a specific order, and is only dropped if you don't already have the respective totem piece. Always dropped by superior monsters."));
        //Dark totem top
        appendDrop(new DisplayedDrop(19683, 1, 1) {
            public double getRate(final Player player, final int id) {
                final NPCCombatDefinitions definitions = NPCCDLoader.get(id);
                return 450 - definitions.getHitpoints();
            }
        });
        put(19683, new PredicatedDrop("This item is dropped in a specific order, and is only dropped if you don't already have the respective totem piece. Always dropped by superior monsters."));
        //Ancient shard
        appendDrop(new DisplayedDrop(19677, 1, 1) {
            public double getRate(final Player player, final int id) {
                final NPCCombatDefinitions definitions = NPCCDLoader.get(id);
                return (int) (2F / 3F * (450 - Math.min(400, definitions.getHitpoints())));
            }
        });
    }

    public void onDeath(final NPC npc, final Player killer) {
        int rate = Math.max(1, 450 - npc.getMaxHitpoints());
        if (SlayerHelmetEffects.INSTANCE.purpleHelmet(killer, npc)) {
            rate *= 0.75;
        }
        if (random(rate) == 0 || (SuperiorMonster.superiorMonsters.contains(npc.getId()) && !CatacombsOfKourend.polygon.contains(npc.getLocation()))) {
            dropTotemPiece(killer, npc);
        }
        if (!SuperiorMonster.superiorMonsters.contains(npc.getId()) || CatacombsOfKourend.polygon.contains(npc.getLocation())) {
            final int shardRate = (int) (2F / 3F * (450 - Math.min(400, npc.getMaxHitpoints())));
            if (random(shardRate) == 0) {
                npc.dropItem(killer, new Item(19677, 1));
            }
        }
    }
    
    public static void dropTotemPiece(final Player killer, final NPC victim) {
        final int baseCount = killer.getAmountOf(ItemId.DARK_TOTEM_BASE);
        final int middleCount = killer.getAmountOf(ItemId.DARK_TOTEM_MIDDLE);
        final int topCount = killer.getAmountOf(ItemId.DARK_TOTEM_TOP);
        if (baseCount <= middleCount && baseCount <= topCount) {
            victim.dropItem(killer, new Item(ItemId.DARK_TOTEM_BASE, 1));
        } else if (middleCount <= topCount) {
            victim.dropItem(killer, new Item(ItemId.DARK_TOTEM_MIDDLE, 1));
        } else {
            victim.dropItem(killer, new Item(ItemId.DARK_TOTEM_TOP, 1));
        }
    }
    
    public static void dropAncientShard(final Player killer, final NPC victim) {
        victim.dropItem(killer, new Item(ItemId.ANCIENT_SHARD, 1));
    }
    
    @Override
    public int[] ids() {
        final IntOpenHashSet set = new IntOpenHashSet(new int[]{7268, 7276, 7256, 7247, 7251, 7248, 7255, 7252, 7272, 7273, 7246, 7277, 7279, 7244, 7242,
                7243, 7241, 7270, 7258, 7245, 7278, 7250, 7253, 7254, 7249, 7275, 7274, 7269, 7271, 7265, 7259, 7257, 7260, 7261, 7262,
                //7263, 7264 Ghosts do not drop the totem pieces!
        });
        return set.toIntArray();
    }
}

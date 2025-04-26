package com.zenyte.plugins.drop;

import com.zenyte.game.content.chambersofxeric.npc.RaidNPC;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.GemDropTable;
import com.zenyte.game.world.entity.npc.drop.StandardRareDropTable;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import mgi.types.config.npcs.NPCDefinitions;

import java.util.stream.Stream;

/**
 * @author Kris | 04/04/2019 01:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RareDropTableProcessor extends DropProcessor {

    @Override
    public void attach() {
        for (final int id : getAllIds()) {
            //Rare drop table
            final StandardRareDropTable.SRDTNPC srdt = StandardRareDropTable.getTable(id);
            if (srdt == null) continue;
            this.appendDrop(new DisplayedDrop(2689, srdt.getRollsCount(), srdt.getRollsCount(),
                    (float) srdt.getMaxRoll() / srdt.getRoll(), (player, npcId) -> npcId == id));
            if (srdt.isDoubleLoot()) {
                this.put(id, 2689, new PredicatedDrop("Gives double the loot when rolled."));
            } else if (srdt.getRollsCount() == 2) {
                this.put(id, 2689, new PredicatedDrop("Two individual rolls on the rare drop table are given."));
            }
        }
        for (final int id : getAllIds()) {
            //Gem drop table
            final GemDropTable.GDTNPC grdt = GemDropTable.getTable(id);
            if (grdt == null) continue;
            this.appendDrop(new DisplayedDrop(2690, 1, 1, (float) grdt.getMaxRoll() / grdt.getRoll(),
                    (player, npcId) -> npcId == id));
        }
    }

    public void onDeath(final NPC npc, final Player killer) {
        if (npc instanceof RaidNPC) {
            return;
        }
        final StandardRareDropTable.SRDTNPC srdt = StandardRareDropTable.getTable(npc.getId());
        if (srdt != null) {
            if (StandardRareDropTable.roll(npc)) {
                for (int i = 0; i < srdt.getRollsCount(); i++) {
                    StandardRareDropTable.get(killer).ifPresent(drop -> {
                        if (srdt.isDoubleLoot()) {
                            drop.setAmount(drop.getAmount() * 2);
                        }
                        npc.dropItem(killer, drop);
                        if (killer.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
                            killer.sendMessage("Rare drop table access: " + drop.getName() + " x " + drop.getAmount());
                        }
                    });
                }
            }
        }
        if (GemDropTable.roll(npc)) {
            GemDropTable.get(killer).ifPresent(drop -> {
                npc.dropItem(killer, drop);
                if (killer.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
                    killer.sendMessage("Gem drop table access: " + drop.getName() + " x " + drop.getAmount());
                }
            });
        }
    }

    @Override
    public int[] ids() {
        final IntList list = IntLists.synchronize(new IntArrayList(100));
        Stream.of(NPCDefinitions.getDefinitions())
                .parallel()
                .forEach(definition -> {
                    if (definition == null) return;
                    final String name = definition.getName();
                    if (GemDropTable.isGDTNPC(definition.getName()) || StandardRareDropTable.isSRDTNPC(name)) {
                        list.add(definition.getId());
                    }
                });
        return list.toIntArray();
    }

}

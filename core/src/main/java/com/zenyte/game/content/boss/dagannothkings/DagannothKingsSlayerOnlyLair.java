package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.WaterbirthDungeon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 26/06/2019 15:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DagannothKingsSlayerOnlyLair extends WaterbirthDungeon implements RandomEventRestrictionPlugin, EntityAttackPlugin, LootBroadcastPlugin, CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2880, 4480 - 64}, {2880, 4416 - 64}, {2944, 4416 - 64}, {2944, 4480 - 64}}, 0)};
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Dagannoth Kings Slayer-Only Lair";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final NPC npc = (NPC) entity;
            if (npc.getName(player).equalsIgnoreCase("Spinolyp")) {
                return true;
            }
            if (!player.getSlayer().isCurrentAssignment(entity)) {
                player.getDialogueManager().start(new PlainChat(player, "You can only kill these dagannoths while on a slayer assignment."));
                return false;
            }
        }
        return true;
    }
}

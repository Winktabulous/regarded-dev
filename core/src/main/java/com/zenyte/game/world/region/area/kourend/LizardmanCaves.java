package com.zenyte.game.world.region.area.kourend;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 05/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LizardmanCaves extends LizardmanSettlement implements EntityAttackPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 1275, 9985 },
                        { 1275, 9928 },
                        { 1344, 9928 },
                        { 1344, 9985 },
                })
        };
    }

    @Override
    public String name() {
        return "Lizardman Caves";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            if (!player.getSlayer().isCurrentAssignment(entity)) {
                player.getDialogueManager().start(new NPCChat(player, 7742, "You can only kill these lizardman shamans while on a slayer assignment."));
                return false;
            }
        }
        return true;
    }

}

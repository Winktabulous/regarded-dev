package com.zenyte.game.world.region.area;

import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.impl.slayer.wyverns.SkeletalWyvern;
import com.zenyte.game.world.entity.npc.impl.slayer.wyverns.Wyvern;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 21/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TaskOnlyWyvernCave extends PolygonRegionArea implements CannonRestrictionPlugin, EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{3584, 10304}, {3584, 10240}, {3648, 10240}, {3648, 10304}})};
    }

    @Override
    public void enter(Player player) {
        player.getTeleportManager().unlock(PortalTeleport.WYVERN_CAVE);
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Wyvern Cave (task only)";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof Wyvern && !(entity instanceof SkeletalWyvern)) {
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || assignment.getTask() != RegularTask.FOSSIL_ISLAND_WYVERN) {
                player.getDialogueManager().start(new Dialogue(player, NpcId.WEVE) {
                    @Override
                    public void buildDialogue() {
                        npc("You can only kill the wyverns in this area if you're assigned to kill them.");
                    }
                });
                return false;
            }
        }
        return true;
    }
}

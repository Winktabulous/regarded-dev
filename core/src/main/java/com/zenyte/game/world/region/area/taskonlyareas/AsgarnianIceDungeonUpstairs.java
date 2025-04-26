package com.zenyte.game.world.region.area.taskonlyareas;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.AsgarnianIceDungeon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 26/01/2019 21:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AsgarnianIceDungeonUpstairs extends AsgarnianIceDungeon implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{3039, 9574}, {3038, 9574}, {3037, 9573}, {3037, 9571}, {3038, 9570}, {3038, 9569}, {3037, 9568}, {3037, 9567}, {3039, 9566}, {3041, 9565}, {3041, 9561}, {3039, 9559}, {3043, 9557}, {3045, 9559}, {3046, 9559}, {3048, 9557}, {3049, 9557}, {3050, 9558}, {3052, 9558}, {3054, 9556}, {3059, 9556}, {3060, 9557}, {3061, 9557}, {3063, 9559}, {3065, 9559}, {3067, 9557}, {3067, 9556}, {3066, 9555}, {3066, 9554}, {3067, 9553}, {3068, 9553}, {3069, 9552}, {3069, 9550}, {3070, 9549}, {3071, 9548}, {3073, 9550}, {3076, 9550}, {3077, 9551}, {3078, 9551}, {3080, 9553}, {3080, 9554}, {3081, 9555}, {3081, 9557}, {3082, 9558}, {3082, 9568}, {3081, 9569}, {3081, 9580}, {3082, 9581}, {3082, 9590}, {3081, 9591}, {3081, 9592}, {3080, 9593}, {3078, 9593}, {3077, 9594}, {3074, 9594}, {3070, 9590}, {3069, 9590}, {3068, 9589}, {3066, 9589}, {3065, 9588}, {3068, 9585}, {3068, 9581}, {3069, 9580}, {3069, 9576}, {3067, 9574}, {3067, 9571}, {3064, 9568}, {3064, 9567}, {3062, 9565}, {3061, 9565}, {3058, 9562}, {3055, 9562}, {3053, 9564}, {3051, 9564}, {3046, 9568}, {3046, 9571}, {3043, 9574}, {3042, 9574}, {3041, 9575}, {3040, 9575}}, 0)};
    }

    @Override
    public String name() {
        return "Asgarnian Ice Dungeon: Task-Only Area";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            if (!player.getSlayer().isCurrentAssignment(entity)) {
                if (((NPC) entity).getDefinitions().getName().equalsIgnoreCase("Skeletal Wyvern")) {
                    player.getDialogueManager().start(new Dialogue(player, 6799) {
                        @Override
                        public void buildDialogue() {
                            npc("You can only kill the wyverns while on a slayer task!");
                        }
                    });
                    return false;
                }
            }
        }
        return true;
    }
}

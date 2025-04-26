package com.zenyte.game.world.entity.player.action.combat.special;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.race.Demon;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript;

import static com.zenyte.game.task.WorldTasksManager.schedule;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-17
 */
public class ScorchingShacklesSpecial implements SpecialAttackScript {

    private final Projectile projectile =
        new Projectile(2807, 32, 32, 0);

    @Override
    public void attack(Player player, PlayerCombat combat, Entity target) {
        if (!Demon.isDemon(((NPC) target), true)) {
            player.sendMessage("Scorching shackles won't work against a non-demon enemy.");
            return;
        }
        player.setAnimation(new Animation(player.getEquipment().getAttackAnimation(player.getCombatDefinitions().getStyle())));
        player.setGraphics(new Graphics(2806));
        var hit = combat.getHit(player, target, 1.3, 1, 1, false);
        schedule(new WorldTask() {
            @Override
            public void run() {
                var delay = World.sendProjectile(player, target, projectile);
                combat.delayHit(delay, hit);
                target.setGraphics(new Graphics(2808, delay, 0));
                target.freeze(20);
                var attribute = target.getTemporaryAttributes().get("burningDamage");
                if (attribute != null) {
                    var burn = Integer.parseInt(attribute.toString());
                    burn += 5;
                    target.getTemporaryAttributes().put("burningDamage", burn);
                }
                else
                    target.getTemporaryAttributes().put("burningDamage", 5);
                stop();
            }
        }, 1, 0);
    }
}

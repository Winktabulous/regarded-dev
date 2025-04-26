package com.zenyte.game.content.area.prifddinas.zalcano.combat.impl;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import com.zenyte.game.content.area.prifddinas.zalcano.combat.ZalcanoAttack;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

import static com.zenyte.game.content.area.prifddinas.zalcano.combat.impl.DroppingBouldersAttack.ANIM;

/**
 * Drops pebbles on players
 */
public class DroppingPebblesAttack implements ZalcanoAttack {

    public static final Graphics PEBBLES = new Graphics(60);

    @Override
    public void execute(ZalcanoInstance instance) {
        var players = instance.getPlayers();
        instance.getZalcano().setAnimation(ANIM);

        WorldTasksManager.schedule(()-> {
            for (var player :  players) {
                CombatUtilities.processHit(player, new Hit(instance.getZalcano(), Utils.random(1, 8), HitType.DEFAULT));
            }
            int randomAmount = Utils.random(4, 12);
            for (int i = 0; i < randomAmount; i++) {
                var pos = instance.getLair().getRandomPosition();
                World.sendGraphics(PEBBLES, pos);
            }
        }, 1);



    }

    @Override
    public boolean canProcess(ZalcanoInstance instance) {
        return instance.getPlayers().size() != 0;
    }

    @Override
    public void interrupt() {
        return;
    }
}

package com.zenyte.game.content.minigame.inferno.npc.impl;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 06/12/2019 | 17:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalAkRekKet extends InfernoNPC {

    private static final Animation attackAnimation = new Animation(7582);

    public JalAkRekKet(final Location location, final Inferno inferno) {
        super(7696, location, inferno);
    }

    @Override
    public int attack(final Player player) {
        setAnimation(attackAnimation);
        delayHit(0, player, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), CRUSH, player), HitType.MELEE));
        return combatDefinitions.getAttackSpeed();
    }

}

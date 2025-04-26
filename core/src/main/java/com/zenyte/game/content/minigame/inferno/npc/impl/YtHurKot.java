package com.zenyte.game.content.minigame.inferno.npc.impl;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCCollidingEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

/**
 * @author Tommeh | 29/11/2019 | 19:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class YtHurKot extends InfernoNPC {
    private static final SoundEffect attackSound = new SoundEffect(608);
    private static final Graphics healGfx = new Graphics(444, 0, 250);
    private static final Animation healAnimation = new Animation(2639);
    private static final SoundEffect healSound = new SoundEffect(167);
    private final JalTokJad jad;
    private int ticks;

    public YtHurKot(final Location location, final Inferno inferno, final JalTokJad jad) {
        super(7701, location, inferno);
        this.jad = jad;
        setIntelligent(true);
    }

    @Override
    public boolean isForceAggressive() {
        return false;
    }

    @Override
    public void processNPC() {
        setIntelligent(true);
        final boolean process = combat.process();
        if (!(process || jad == null || isDead() || jad.isDead() || jad.isFinished())) {
            if (!hasWalkSteps()) {
                if (!CombatUtilities.isWithinMeleeDistance(jad, this)) {
                    setRouteEvent(new NPCCollidingEvent(this, new EntityStrategy(jad)));
                    setFaceEntity(jad);
                }
            }
        }
        if (++ticks % 4 != 0 || process || jad == null || jad.isDead() || jad.isFinished() || !CombatUtilities.isWithinMeleeDistance(jad, this)) {
            return;
        }
        inferno.playSound(healSound);
        setAnimation(healAnimation);
        jad.heal(Utils.random(1, 10));
        if (ticks % 8 == 0) {
            jad.setGraphics(healGfx);
        }
    }

    @Override
    public int attack(Entity target) {
        animate();
        inferno.playSound(attackSound);
        delayHit(0, target, melee(target, combatDefinitions.getMaxHit()));
        return combatDefinitions.getAttackSpeed();
    }
}

package com.zenyte.game.content.minigame.inferno.npc.impl;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.util.WorldUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.utils.TimeUnit;

import java.util.Optional;

/**
 * @author Tommeh | 29/11/2019 | 19:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalImKot extends InfernoNPC {
    private static final Animation attackAnimation = new Animation(7597);
    private static final Animation burrowStartAnimation = new Animation(7600);
    private static final Animation burrowEndAnimation = new Animation(7601);
    private boolean burrowing;

    public JalImKot(final Location location, final Inferno inferno) {
        super(7697, location, inferno);
    }

    @Override
    public void onSpawn() {
        setAttackingDelay(Utils.currentTimeMillis() + TimeUnit.TICKS.toMillis(25));
    }

    @Override
    public boolean isRevivable() {
        return true;
    }

    @Override
    public boolean canAttack(final Player source) {
        if (burrowing) {
            source.sendMessage("You cannot attack it while it's burrowing.");
            return false;
        }
        return true;
    }

    @Override
    public void processNPC() {
        if (!isDead() && Utils.currentTimeMillis() - getAttackingDelay() >= TimeUnit.TICKS.toMillis(10) && !hasWalkSteps()) {
            burrowing = true;
            lock(9);
            setAnimation(burrowStartAnimation);
            setAttackingDelay(Utils.currentTimeMillis() + TimeUnit.TICKS.toMillis(25));
            final Location burrowLocation = getBurrowLocation();
            WorldTasksManager.schedule(() -> {
                burrowing = false;
                combat.setCombatDelay(6);
                faceEntity(inferno.getPlayer());
                setLocation(burrowLocation);
                setAnimation(burrowEndAnimation);
            }, 4);
            return;
        }
        super.processNPC();
    }

    private Location getBurrowLocation() {
        final Player player = inferno.getPlayer();
        final Optional<Location> optionalLocation = WorldUtil.findEmptySquare(player.getLocation(), player.getSize() + getSize(), getSize(), Optional.of(l -> CombatUtilities.isWithinMeleeDistance(l, getSize(), player)));
        return optionalLocation.orElseGet(this::getLocation);
    }

    @Override
    public int attack(final Player player) {
        setAnimation(attackAnimation);
        delayHit(0, player, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), CRUSH, player), HitType.MELEE));
        return combatDefinitions.getAttackSpeed();
    }
}

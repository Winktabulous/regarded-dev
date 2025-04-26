package com.zenyte.game.content.treasuretrails.npcs.mimic;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 27/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
class ThirdAgeMonster extends NPC implements CombatScript {
    static final int WARRIOR = 8635, RANGER = 8636, MAGE = 8637;
    private static final Graphics drawbackGraphics = new Graphics(1611, 0, 96);
    private static final Projectile rangedProjectile = new Projectile(1574, 42, 30, 40, 15, 3, 64, 5);

    ThirdAgeMonster(@MagicConstant(valuesFromClass = ThirdAgeMonster.class) int id, @NotNull Location tile) {
        super(id, tile, Direction.SOUTH, 0);
        assert id == WARRIOR || id == RANGER || id == MAGE;
        spawned = true;
        setAnimation(combatDefinitions.getSpawnDefinitions().getSpawnAnimation());
        lock(3);
    }

    @Override
    public int attack(Entity target) {
        if (id == MAGE) {
            useSpell(CombatSpell.EARTH_WAVE, target, combatDefinitions.getMaxHit());
        } else if (id == RANGER) {
            animate();
            setGraphics(drawbackGraphics);
            delayHit(World.sendProjectile(this, target, rangedProjectile), target, ranged(target, combatDefinitions.getMaxHit()));
        } else if (id == WARRIOR) {
            animate();
            executeMeleeHit(target, combatDefinitions.getMaxHit());
        } else {
            throw new IllegalStateException();
        }
        return combatDefinitions.getAttackSpeed();
    }
}

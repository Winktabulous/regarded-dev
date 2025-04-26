package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 04/04/2019 22:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Gorak extends NPC implements Spawnable, CombatScript {
    private static final IntArrayList drainableSkills = new IntArrayList();

    static {
        for (int i = 0; i < SkillConstants.SKILLS.length; i++) {
            drainableSkills.add(i);
        }
        drainableSkills.rem(SkillConstants.HITPOINTS);
    }

    public Gorak(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 1834;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 1;
    }

    @Override
    public int attack(Entity target) {
        animate();
        final Hit hit = new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), AttackType.CRUSH, target), HitType.REGULAR);
        delayHit(0, target, hit);
        if (!(target instanceof Player)) {
            return combatDefinitions.getAttackSpeed();
        }
        final Player player = (Player) target;
        if (hit.getDamage() > 0) {
            final int skill = drainableSkills.getInt(Utils.random(drainableSkills.size() - 1));
            player.getSkills().drainSkill(skill, 1);
        }
        if (player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
            player.sendMessage("Your protection prayer doesn't seem to work!");
        }
        return combatDefinitions.getAttackSpeed();
    }
}

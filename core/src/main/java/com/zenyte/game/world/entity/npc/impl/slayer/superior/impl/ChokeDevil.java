package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChokeDevil extends SuperiorNPC implements CombatScript {
    public ChokeDevil(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7404, tile);
    }

    private static final Projectile PROJECTILE = new Projectile(73, 30, 35, 108, 10);

    @Override
    public int attack(final Entity target) {
        if (target instanceof NPC) {
            return -1;
        }
        final Player player = (Player) target;
        setAnimation(getCombatDefinitions().getAttackAnim());
        if (!SlayerEquipment.FACE_MASK.isWielding(player)) {
            getCombatDefinitions().setAttackStyle("Magic");
            delayHit(this, World.sendProjectile(this, target, PROJECTILE), player, new Hit(this, 15, HitType.REGULAR).onLand(hit -> {
                player.getSkills().setLevel(SkillConstants.ATTACK, 0);
                player.getSkills().setLevel(SkillConstants.STRENGTH, 0);
                player.getSkills().setLevel(SkillConstants.RANGED, 0);
                player.getSkills().setLevel(SkillConstants.MAGIC, 0);
                player.getSkills().setLevel(SkillConstants.PRAYER, (int) (player.getSkills().getLevel(SkillConstants.PRAYER) * 0.5064935064935066));
                player.getSkills().setLevel(SkillConstants.DEFENCE, (int) (player.getSkills().getLevel(SkillConstants.DEFENCE) * 0.5064935064935066));
                player.getSkills().setLevel(SkillConstants.AGILITY, (int) (player.getSkills().getLevel(SkillConstants.AGILITY) * 0.5064935064935066));
            }));
        } else {
            getCombatDefinitions().setAttackStyle("Melee");
            delayHit(this, 0, player, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return getCombatDefinitions().getAttackSpeed();
    }
}

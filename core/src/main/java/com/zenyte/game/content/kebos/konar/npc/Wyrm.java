package com.zenyte.game.content.kebos.konar.npc;

import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import mgi.types.config.AnimationDefinitions;

/**
 * @author Tommeh | 19/10/2019 | 15:25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Wyrm extends NPC implements CombatScript, Spawnable {
    private static final Animation transformToAttackAnim = new Animation(8268);
    private static final Animation transformToIdleAnim = new Animation(8269);
    private static final Animation magicAttackAnim = new Animation(8270);
    private static final Animation meleeAttackAnim = new Animation(8271);
    private static final Projectile magicAttackProj = new Projectile(1634, 43, 10, 33, 10, 18, 0, 5);
    private static final int IDLE_STATE_NPC_ID = 8610;
    private static final int ATTACK_STATE_NPC_ID = 8611;

    public Wyrm(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (id == ATTACK_STATE_NPC_ID) {
            if ((Utils.currentTimeMillis() - getAttackingDelay()) >= 5000) {
                setAttackingDelay(getAttackingDelay() + 1200);
                setAnimation(transformToIdleAnim);
                WorldTasksManager.schedule(() -> {
                    setTransformation(IDLE_STATE_NPC_ID);
                    cancelCombat();
                    heal(getMaxHitpoints());
                    setAttackingDelay(Utils.currentTimeMillis());
                }, 1);
            }
        }
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
        if (id == IDLE_STATE_NPC_ID) {
            setTransformation(ATTACK_STATE_NPC_ID);
            setAnimation(transformToAttackAnim);
            setAttackingDelay(Utils.currentTimeMillis());
        } else {
            super.handleIngoingHit(hit);
        }
    }

    @Override
    public void setAnimation(final Animation animation) {
        this.animation = animation;
        if (animation == null) {
            updateFlags.set(UpdateFlag.ANIMATION, false);
            lastAnimation = 0;
        } else {
            updateFlags.flag(UpdateFlag.ANIMATION);
            final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
            if (defs != null) {
                lastAnimation = Utils.currentTimeMillis() + defs.getDuration();
            } else {
                lastAnimation = Utils.currentTimeMillis();
            }
        }
    }

    private static final SoundEffect magicSound = new SoundEffect(1965, 10, 0);

    @Override
    public int attack(final Entity target) {
        if (isWithinMeleeDistance(this, target) && Utils.random(2) == 0) {
            attackSound();
            setAnimation(meleeAttackAnim);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), AttackType.SLASH, target), HitType.MELEE));
        } else {
            World.sendSoundEffect(getMiddleLocation(), magicSound);
            setAnimation(magicAttackAnim);
            delayHit(World.sendProjectile(this, target, magicAttackProj), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), AttackType.MAGIC, target), HitType.MAGIC));
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(KourendDiary.KILL_A_WYRM);
            player.getCombatAchievements().complete(CAType.A_SLITHERY_ENCOUNTER);
        }
    }

    @Override
    public void onFinish(final Entity source) {
        setId(IDLE_STATE_NPC_ID);
        super.onFinish(source);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 8610 || id == 8611;
    }
}

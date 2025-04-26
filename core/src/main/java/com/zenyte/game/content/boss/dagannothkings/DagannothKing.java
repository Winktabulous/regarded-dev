package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.boons.impl.DagaWHO;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.utils.TimeUnit;
import kotlin.collections.CollectionsKt;

import java.util.Set;

/**
 * @author Tommeh | 19 mrt. 2018 : 20:39:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class DagannothKing extends NPC implements Spawnable, CombatScript {

    private boolean rexChainHit = false;
    private boolean bypassSpecificStyleReq = false;
    private boolean canAttackMultipleTarget = false;

    public DagannothKing(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        this.maxDistance = 64;
    }

    @Override
    public int getRespawnDelay() {
        return 100;
    }

    @Override
    public void onDeath(Entity source) {
        super.onDeath(source);
        if (source instanceof Player player) {
            final int flag = id == 2265 ? 1 : id == 2266 ? 2 : 4;
            player.getAchievementDiaries().update(FremennikDiary.KILL_DAGANNOTH_KINGS, flag);
            AdventCalendarManager.increaseChallengeProgress(player, 2022, 10, 1);
        }
    }


    private static final int[] OTHER_DAGS_REX = {NpcId.DAGANNOTH_PRIME, NpcId.DAGANNOTH_SUPREME};
    private static final int[] OTHER_DAGS_PRIME = {NpcId.DAGANNOTH_REX, NpcId.DAGANNOTH_SUPREME};
    private static final int[] OTHER_DAGS_SUPREME = {NpcId.DAGANNOTH_REX, NpcId.DAGANNOTH_PRIME};
    private static final String SUPREME_ATT = "dagannoth_death_" + NpcId.DAGANNOTH_SUPREME;
    private static final String REX_ATT = "dagannoth_death_" + NpcId.DAGANNOTH_REX;
    private static final String PRIME_ATT = "dagannoth_death_" + NpcId.DAGANNOTH_PRIME;

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof Player player) {
            player.getAttributes().put("dagannoth_death_" + id, Utils.currentTimeMillis());
            final long supremeDeathTime = player.getNumericAttribute(SUPREME_ATT).longValue();
            final long rexDeathTime = player.getNumericAttribute(REX_ATT).longValue();
            final long primeDeathTime = player.getNumericAttribute(PRIME_ATT).longValue();
            final long firstKill = Math.min(supremeDeathTime, Math.min(rexDeathTime, primeDeathTime));
            final long lastKill = Math.max(supremeDeathTime, Math.max(rexDeathTime, primeDeathTime));
            if (TimeUnit.MILLISECONDS.toSeconds(lastKill - firstKill) <= 9) {
                player.getCombatAchievements().complete(CAType.RAPID_SUCCESSION);
            }
            if (getId() == NpcId.DAGANNOTH_REX) {
                checkCATask(player, "dagannoth rex", CAType.DAGANNOTH_REX_CHAMPION, CAType.DAGANNOTH_REX_ADEPT, OTHER_DAGS_REX, CAType.DEATH_TO_THE_WARRIOR_KING);
                if (isFrozen()) {
                    player.getCombatAchievements().complete(CAType.A_FROZEN_KING);
                }
                if (TimeUnit.MILLISECONDS.toSeconds(Utils.currentTimeMillis() - supremeDeathTime) <= 1
                    || TimeUnit.MILLISECONDS.toSeconds(Utils.currentTimeMillis() - primeDeathTime) <= 1) {
                    player.getCombatAchievements().complete(CAType.TOPPLING_THE_DIARCHY);
                }
            } else {
                if (TimeUnit.MILLISECONDS.toSeconds(Utils.currentTimeMillis() - rexDeathTime) <= 1) {
                    player.getCombatAchievements().complete(CAType.TOPPLING_THE_DIARCHY);
                }
                if (getId() == NpcId.DAGANNOTH_PRIME) {
                    checkCATask(player, "dagannoth prime", CAType.DAGANNOTH_PRIME_CHAMPION, CAType.DAGANNOTH_PRIME_ADEPT, OTHER_DAGS_PRIME, CAType.DEATH_TO_THE_SEER_KING);
                    if (rexChainHit) {
                        player.getCombatAchievements().complete(CAType.FROM_ONE_KING_TO_ANOTHER);
                    }
                } else if (getId() == NpcId.DAGANNOTH_SUPREME) {
                    checkCATask(player, "dagannoth supreme", CAType.DAGANNOTH_SUPREME_CHAMPION, CAType.DAGANNOTH_SUPREME_ADEPT, OTHER_DAGS_SUPREME, CAType.DEATH_TO_THE_ARCHER_KING);
                }
            }
        }
    }

    private void checkCATask(final Player player, final String name, final CAType mediumTask, final CAType hardTask, int[] otherDagIds, final CAType deathTask) {
        player.getCombatAchievements().checkKcTask(name, 10, mediumTask);
        player.getCombatAchievements().checkKcTask(name, 25, hardTask);
        if (TimeUnit.MILLISECONDS.toSeconds(Utils.currentTimeMillis() - (long) player.getAttributes().getOrDefault("dagannoth_last_attacked_" + otherDagIds[0], 0L)) <= 5 &&
                TimeUnit.MILLISECONDS.toSeconds(Utils.currentTimeMillis() - (long) player.getAttributes().getOrDefault("dagannoth_last_attacked_" + otherDagIds[0], 0L)) <= 5) {
            player.getCombatAchievements().complete(deathTask);
        }
    }

    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (target instanceof final Player player) {
            player.getAttributes().put("dagannoth_last_attacked_" + getId(), Utils.currentTimeMillis());
        }
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id >= 2265 && id <= 2267;
    }

    private static final Projectile magicProj = new Projectile(162, 63, 25, 27, 15, 33, 64, 5);

    private static final Projectile rangedProj = new Projectile(475, 50, 30, 25, 30, 28, 5, 5);

    @Override public void handleIngoingHit(Hit hit) {
        if(!bypassSpecificStyleReq && hit.getSource() instanceof Player player) {
            boolean bypass = player.getBoonManager().hasBoon(DagaWHO.class);
            if (!bypass && getId() == NpcId.DAGANNOTH_SUPREME && hit.getHitType() != HitType.MELEE) {
                hit.setDamage(0);
            }
            if (!bypass && getId() == NpcId.DAGANNOTH_REX && hit.getHitType() != HitType.MAGIC) {
                hit.setDamage(0);
            }
            if (!bypass && getId() == NpcId.DAGANNOTH_PRIME && hit.getHitType() != HitType.RANGED) {
                hit.setDamage(0);
            }
        }
        super.handleIngoingHit(hit);
        if (getId() == NpcId.DAGANNOTH_PRIME) {
            rexChainHit = hit.getAttribute("chainhit_prev") instanceof final NPC npc && npc.getId() == NpcId.DAGANNOTH_REX;
        }
    }

    @Override
    public int attack(final Entity target) {
        final DagannothKing npc = this;

        final Set<Entity> targets = canAttackMultipleTarget
                ? CollectionsKt.toSet(CollectionsKt.filterIsInstance(getPossibleTargets(EntityType.PLAYER), Player.class))
                : Set.of(target);

        if (getId() == NpcId.DAGANNOTH_PRIME) {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            targets.forEach(t -> delayHit(npc, World.sendProjectile(npc, t, magicProj), t, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, t), HitType.MAGIC)));
        } else if (getId() == NpcId.DAGANNOTH_REX) {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        } else {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            targets.forEach(t -> delayHit(npc, World.sendProjectile(npc, t, rangedProj), t, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), RANGED, t), HitType.RANGED)));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }

    public void setBypassSpecificStyleReq(boolean bypassSpecificStyleReq) {
        this.bypassSpecificStyleReq = bypassSpecificStyleReq;
    }

    public void setCanAttackMultipleTarget(boolean canAttackMultipleTarget) {
        this.canAttackMultipleTarget = canAttackMultipleTarget;
    }
}

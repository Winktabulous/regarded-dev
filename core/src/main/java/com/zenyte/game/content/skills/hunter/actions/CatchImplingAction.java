package com.zenyte.game.content.skills.hunter.actions;

import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.boons.impl.FirstImpressions;
import com.zenyte.game.content.minigame.puropuro.PuroPuroArea;
import com.zenyte.game.content.skills.hunter.node.Impling;
import com.zenyte.game.content.skills.hunter.npc.ImplingNPC;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.CombatEntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 2-11-2018 | 18:44
 * @author Corey 26/12/19
 */
public class CatchImplingAction extends Action {
    private static final Animation ANIMATION = new Animation(6606);
    private static final Animation BAREHANDED_ANIMATION = new Animation(7171);
    private final Impling impling;
    private final ImplingNPC npc;
    private boolean barehanded;
    private int requiredLevel;
    private Animation anim;

    public static boolean success(final Player player, final int requiredLevel) {
        final boolean hasMagicNet = hasMagicButterflyNetEquipped(player);
        final int level = player.getSkills().getLevel(SkillConstants.HUNTER);
        final double n = Math.floor((306.0F * (level - 1.0F + (hasMagicNet ? 8 : 0))) / 98.0F) - requiredLevel;
        final double chance = n / 255.0F;
        final double rand = Utils.randomDouble();
        return rand < chance;
    }

    private boolean initiateCombat(final Player player) {
        if (player.isDead() || player.isFinished() || player.isLocked() || player.isStunned() || player.isFullMovementLocked()) {
            return false;
        }
        final ImplingNPC target = npc;
        if (target.isFinished() || target.isCantInteract() || target.isDead()) {
            return false;
        }
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = target.getSize();
        final int viewDistance = player.getViewDistance();
        if (player.getPlane() != target.getPlane() || distanceX > size + viewDistance || distanceX < -1 - viewDistance || distanceY > size + viewDistance || distanceY < -1 - viewDistance) {
            return false;
        }
        if (player.isFrozen() || player.isStunned() || player.isMovementLocked(false)) {
            return true;
        }
        if (!target.hasWalkSteps() && CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), target.getX(), target.getY(), target.getSize())) {
            player.getCombatEvent().process();
            return true;
        }
        player.resetWalkSteps();
        if (player.isProjectileClipped(target, true) || !(withinRange(target, target.getSize()))) {
            appendWalksteps();
        }
        return true;
    }

    protected boolean isWithinAttackDistance() {
        final ImplingNPC target = npc;
        final Location nextTile = target.getLocation();
        final Location nextPosition = player.getNextPosition(player.isRun() ? 2 : 1);
        final Location tile = nextTile != null ? nextTile : target.getLocation();
        final int distanceX = nextPosition.getX() - tile.getX();
        final int distanceY = nextPosition.getY() - tile.getY();
        final int size = target.getSize();
        final Location nextLocation = target.getNextPosition(target.isRun() ? 2 : 1);
        if ((player.isFrozen() || player.isStunned()) && (CollisionUtil.collides(nextPosition.getX(), nextPosition.getY(), player.getSize(), nextLocation.getX(), nextLocation.getY(), target.getSize()) || !withinRange(target, target.getSize()))) {
            return false;
        }
        return distanceX <= size && distanceX >= -1 && distanceY <= size && distanceY >= -1;
    }

    protected final void appendWalksteps() {
        player.getCombatEvent().process();
    }

    final boolean withinRange(final Position targetPosition, final int targetSize) {
        final Location target = targetPosition.getPosition();
        final Location nextPosition = player.getNextPosition(player.isRun() ? 2 : 1);
        final int distanceX = nextPosition.getX() - target.getX();
        final int distanceY = nextPosition.getY() - target.getY();
        final int npcSize = player.getSize();
        if (distanceX == -npcSize && distanceY == -npcSize || distanceX == targetSize && distanceY == targetSize || distanceX == -npcSize && distanceY == targetSize || distanceX == targetSize && distanceY == -npcSize) {
            return false;
        }
        return !(distanceX > targetSize || distanceY > targetSize || distanceX < -npcSize || distanceY < -npcSize);
    }

    @Override
    public boolean process() {
        return initiateCombat(player);
    }

    @Override
    public boolean start() {
        player.setCombatEvent(new CombatEntityEvent(player, new EntityStrategy(npc)));
        barehanded = !hasButterflyNetEquipped();
        anim = barehanded ? BAREHANDED_ANIMATION : ANIMATION;
        requiredLevel = Math.min(impling.getLevel() + (barehanded ? 10 : 0), 99);
        player.setFaceEntity(npc);
        if (initiateCombat(player)) {
            return true;
        }
        player.setFaceEntity(null);
        return false;
    }

    @Override
    public void stop() {
        final int faceEntity = player.getFaceEntity();
        final long lastDelay = player.getLastFaceEntityDelay();
        WorldTasksManager.schedule(() -> {
            if (player.getFaceEntity() == faceEntity && player.getLastFaceEntityDelay() == lastDelay) {
                player.setFaceEntity(null);
            }
        });
    }

    @Override
    public int processWithDelay() {
        if (!isWithinAttackDistance()) {
            return 0;
        }
        if (!player.getInventory().containsItem(ItemId.IMPLING_JAR)) {
            if (!barehanded || player.getArea() instanceof PuroPuroArea) {
                // puro-puro implings must be caught in a jar
                player.sendMessage("You don't have an empty impling jar in which to keep an impling.");
                return -1;
            }
        }
        if (!barehanded) {
            if (player.getSkills().getLevel(SkillConstants.HUNTER) < requiredLevel) {
                player.sendMessage("You need a Hunter level of at least " + requiredLevel + " to catch this impling.");
                return -1;
            }
        } else {
            if (player.getSkills().getLevel(SkillConstants.HUNTER) < requiredLevel) {
                player.sendMessage("You need a Hunter level of at least " + requiredLevel + " to catch this impling barehanded.");
                return -1;
            }
            if (player.getInventory().getFreeSlots() < 2) {
                player.sendMessage("You need at least 2 spaces in your pack before attempting to catch this impling barehanded.");
                return -1;
            }
        }
        npc.lock(1);
        npc.resetWalkSteps();
        if (attemptCatch()) {
            player.getActionManager().setActionDelay(2);
            return -1;
        }
        return npc.isFrozen() ? 2 : 4;
    }

    private boolean attemptCatch() {
        final boolean success = !npc.isCantInteract() && success(player, requiredLevel);
        if (success) {
            npc.setCantInteract(true);
        }
        WorldTasksManager.schedule(() -> {
            player.faceEntity(npc);
            player.setAnimation(anim);
            player.sendSound(new SoundEffect(2623));
            npc.setAnimation(new Animation(6615));
            npc.resetWalkSteps();
            npc.faceEntity(player);
            if (success) {
                npc.lock(5);
                WorldTasksManager.schedule(() -> {
                    npc.finish();
                    npc.setCantInteract(false);
                }, 1);
                player.getSkills().addXp(SkillConstants.HUNTER, impling.getExperienceGielinor() * (barehanded ? 1.2 : 1.0));
                if (player.getArea() instanceof PuroPuroArea || !barehanded) {
                    player.getInventory().deleteItem(ItemId.IMPLING_JAR, 1);
                    player.getInventory().addItems(impling.getJar().getJarItem());
                    player.sendFilteredMessage("You manage to catch the impling and squeeze it into a jar.");
                } else {
                    player.sendFilteredMessage("You manage to catch the impling and acquire some loot.");
                    player.getInventory().addItems(impling.getJar().generateLoot(player));
                    boolean hasBoon = player.getBoonManager().hasBoon(FirstImpressions.class);
                    if(hasBoon) {
                        for (final Item it : impling.getJar().generateLoot(player)) {
                            player.getInventory().addOrDrop(it);
                        }
                    }
                }
                final boolean isPuro = (player.getArea() instanceof PuroPuroArea);
                final String key = (isPuro ? Impling.PURO_IMPLING_TRACKER_ATTRIBUTE_KEY : Impling.SURFACE_IMPLING_TRACKER_ATTRIBUTE_KEY) + impling.getNpcId();
                player.addAttribute(key, player.getNumericAttribute(key).intValue() + 1);
                if (isPuro && (impling == Impling.ECLECTIC || impling == Impling.ESSENCE)) {
                    player.getAchievementDiaries().update(LumbridgeDiary.CATCH_IMPLING);
                }
                return;
            } else {
                npc.resetWalkSteps();
                npc.lock(1);
            }
            npc.setRetreatingFrom(player);
        });
        return success;
    }

    private static boolean hasMagicButterflyNetEquipped(final Player player) {
        return player.getEquipment().containsItem(new Item(ItemId.MAGIC_BUTTERFLY_NET));
    }

    private boolean hasButterflyNetEquipped() {
        if (player.getEquipment().containsItem(new Item(ItemId.BUTTERFLY_NET))) {
            return true;
        }
        return hasMagicButterflyNetEquipped(player);
    }

    public CatchImplingAction(Impling impling, ImplingNPC npc) {
        this.impling = impling;
        this.npc = npc;
    }
}

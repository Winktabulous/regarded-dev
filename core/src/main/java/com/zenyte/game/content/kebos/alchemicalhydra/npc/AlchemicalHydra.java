package com.zenyte.game.content.kebos.alchemicalhydra.npc;

import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.content.kebos.alchemicalhydra.HydraPhase;
import com.zenyte.game.content.kebos.alchemicalhydra.instance.AlchemicalHydraInstance;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.HydraPhaseSequence;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.phases.EnragedPhase;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.combat.phases.FlamePhase;
import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.BossTaskSumona;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.utils.TimeUnit;
import mgi.types.config.AnimationDefinitions;

import java.util.List;

/**
 * @author Tommeh | 02/11/2019 | 16:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class AlchemicalHydra extends NPC implements CombatScript {

    public static final String CA_TASK_INSTANCE_KC_ATT = "alchemical_hydra_instance_kc";
    private static final int DEATH_SEQ_NPC_ID = 8622;
    private static final Animation preDeathAnim = new Animation(8257);
    private static final Animation finalDeathAnim = new Animation(8258);
    public static final ForceTalk roar = new ForceTalk("Roaaaaaaaaaaar!");
    private final AlchemicalHydraInstance instance;
    private HydraPhase phase;
    private HydraPhaseSequence sequence;
    private boolean shielded;
    private boolean transforming;
    private boolean dying;
    private double attackModifier;
    private int autoAttacks;
    private long switchAttackTicks = -1L;
    private boolean empoweredOnce;
    private boolean playerHitByPoisonOnce;
    private boolean playerGotHitOnce;
    private boolean playerHitByLighteningOnce;
    private boolean playerHitByFlameWallOnce;

    public AlchemicalHydra(final AlchemicalHydraInstance instance, final Location tile) {
        super(8615, tile, Direction.SOUTH, 16);
        this.instance = instance;
        resetNPC(HydraPhase.POISON);
        setAttackDistance(3);
    }

    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (hit.getDamage() > 0) {
            playerGotHitOnce = true;
        }
    }

    public void switchStyle() {
        final AttackType style = getCombatDefinitions().getAttackStyle();
        if (style.equals(AttackType.MAGIC)) {
            combatDefinitions.setAttackStyle(AttackType.RANGED);
        } else {
            combatDefinitions.setAttackStyle(AttackType.MAGIC);
        }
    }

    @Override
    protected boolean preserveStatsOnTransformation() {
        return true;
    }

    @Override
    public void setTransformation(final int id) {
        final AttackType style = combatDefinitions.getAttackStyle();
        nextTransformation = id;
        setId(id);
        size = definitions.getSize();
        updateFlags.flag(UpdateFlag.TRANSFORMATION);
        if (preserveStatsOnTransformation()) {
            updateTransformationalDefinitions();
        } else {
            updateCombatDefinitions();
        }
        combatDefinitions.setAttackStyle(style);
    }

    public void transform(final HydraPhase nextPhase) {
        if (transforming) {
            return;
        }
        final Player player = instance.getPlayer();
        transforming = true;
        player.stopAll();
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    setCantInteract(true);
                    combat.setTarget(null);
                    setInteractingWith(null);
                    resetWalkSteps();
                    lock();
                    setTransformation(nextPhase.getPreTransformation());
                    setAnimation(nextPhase.getPreTransformationAnim());
                } else if (ticks == nextPhase.getSequenceDelay()) {
                    setTransformation(nextPhase.getPostTransformation());
                    setAnimation(nextPhase.getPostTransformationAnim());
                } else if (ticks == nextPhase.getSequenceDelay() + 1) {
                    if (nextPhase.equals(HydraPhase.ENRAGED)) {
                        player.sendMessage("The Alchemical Hydra becomes enraged!");
                    }
                    resetNPC(nextPhase);
                    setCantInteract(false);
                    combat.setTarget(player);
                    setInteractingWith(player);
                    unlock();
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    public void incrementAutoAttacks() {
        autoAttacks++;
    }

    public void increaseStrength() {
        attackModifier = Math.min(1.5, attackModifier + 0.05);
        empoweredOnce = true;
    }

    public Location getRangedHeadLocation(final Entity target) {
        final Location tile = getMiddleLocation();
        final int dir = DirectionUtil.getFaceDirection(target.getX() - tile.getX(), target.getY() - tile.getY());
        if (dir < 256) {
            tile.moveLocation(-2, -1, 0);
        } else if (dir < 512) {
            tile.moveLocation(-1, 1, 0);
        } else if (dir < 768) {
            tile.moveLocation(0, 1, 0);
        } else if (dir < 1024) {
            tile.moveLocation(1, 1, 0);
        } else if (dir < 1280) {
            tile.moveLocation(2, 0, 0);
        } else if (dir < 1536) {
            tile.moveLocation(2, -1, 0);
        } else if (dir < 1792) {
            tile.moveLocation(1, -1, 0);
        } else {
            tile.moveLocation(0, -2, 0);
        }
        return tile;
    }

    public Location getMagicHeadLocation(final Entity target) {
        final Location tile = getMiddleLocation();
        final int dir = DirectionUtil.getFaceDirection(target.getX() - tile.getX(), target.getY() - tile.getY());
        if (dir < 256) {
            tile.moveLocation(1, -1, 0);
        } else if (dir < 512) {
            tile.moveLocation(0, -2, 0);
        } else if (dir < 768) {
            tile.moveLocation(-1, -1, 0);
        } else if (dir < 1024) {
            tile.moveLocation(-1, 1, 0);
        } else if (dir < 1280) {
            tile.moveLocation(-2, 1, 0);
        } else if (dir < 1536) {
            tile.moveLocation(0, 2, 0);
        } else if (dir < 1792) {
            tile.moveLocation(1, 2, 0);
        } else {
            tile.moveLocation(2, 0, 0);
        }
        return tile;
    }

    public int getMaxHit() {
        return phase.equals(HydraPhase.ENRAGED) ? 26 : 17;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public NPC spawn() {
        final NPC hydra = super.spawn();
        if (instance.isEntered()) {
            instance.getPlayer().getBossTimer().startTracking("Alchemical Hydra");
        }
        if (Utils.random(1) == 0) {
            switchStyle();
        }
        return hydra;
    }

    @Override
    public int getRespawnDelay() {
        return BossRespawnTimer.ALCHEMICAL_HYDRA.getTimer().intValue();
    }

    @Override
    public boolean canAttack(final Player source) {
        if (transforming) {
            source.sendMessage("You cannot attack the Alchemical Hydra while it's transforming.");
            return false;
        }
        final Assignment assignment = source.getSlayer().getAssignment();
        if (assignment == null || !assignment.getTask().equals(RegularTask.HYDRAS) && !assignment.getTask().equals(BossTaskSumona.ALCHEMICAL_HYDRA_SUMONA)) {
            source.sendMessage("You can only attack the Alchemical Hydra while on a Hydras slayer task.");
            return false;
        }
        return true;
    }

    @Override
    public boolean checkAggressivity() {
        if (sequence instanceof FlamePhase && ((FlamePhase) sequence).isPerformingSpecialAttack() && hasWalkSteps()) {
            return false;
        }
        return super.checkAggressivity();
    }

    @Override
    public void resetWalkSteps() {
        if (sequence instanceof FlamePhase && ((FlamePhase) sequence).isPerformingSpecialAttack()) {
            return;
        }
        super.resetWalkSteps();
    }

    @Override
    public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
        if (sequence instanceof FlamePhase && ((FlamePhase) sequence).isPerformingSpecialAttack()) {
            return false;
        }
        return super.addWalkStep(nextX, nextY, lastX, lastY, check);
    }

    @Override
    public void autoRetaliate(final Entity source) {
        if (transforming)
            return;
        if (sequence instanceof FlamePhase && ((FlamePhase) sequence).isWalkingToMiddle()) {
            return;
        }
        super.autoRetaliate(source);
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

    @Override
    public int attack(final Entity target) {
        if (transforming)
            return -1;
        randomWalkDelay = 10;
        if (target instanceof Player) {
            final boolean specialAttack = sequence.attack(this, (Player) target);
            if (!specialAttack && autoAttacks > 0 && autoAttacks % 3 == 0) {
                switchStyle();
                switchAttackTicks = WorldThread.getCurrentCycle() + 3;
            }
            return combatDefinitions.getAttackSpeed();
        }
        return -1;
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
        if (shielded && !phase.equals(HydraPhase.ENRAGED)) {
            final Entity source = hit.getSource();
            hit.setDamage((int) (hit.getDamage() * 0.75F));
            if (source instanceof Player) {
                ((Player) source).sendFilteredMessage("The Alchemical Hydra's defences partially absorb your attack!");
            }
        }
        final int newHitpoints = getHitpoints() - hit.getDamage();
        if (newHitpoints > 0) {
            final HydraPhase newPhase;
            if (newHitpoints < getMaxHitpoints() * 0.25)
                newPhase = HydraPhase.ENRAGED;
            else if (newHitpoints < getMaxHitpoints() * 0.5)
                newPhase = HydraPhase.FLAME;
            else if (newHitpoints < getMaxHitpoints() * 0.75)
                newPhase = HydraPhase.LIGHTNING;
            else
                newPhase = HydraPhase.POISON;
            if (newPhase != phase)
                transform(newPhase);
        }
        final Object weapon = hit.getWeapon();
        if (!"Thrall".equals(weapon) && (!(weapon instanceof final Item item) || (item.getId() != ItemId.DHAROKS_GREATAXE && item.getId() != ItemId.DHAROKS_GREATAXE_100
                && item.getId() != ItemId.DHAROKS_GREATAXE_75  && item.getId() != ItemId.DHAROKS_GREATAXE_50  && item.getId() != ItemId.DHAROKS_GREATAXE_25))) {
            instance.setEligibleForNoPressureTask(false);
        }
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (switchAttackTicks == WorldThread.getCurrentCycle()) {
            Animation switchAnim = phase.getAttackStyleSwapAnimation();
            if (switchAnim != null) {
                setAnimation(phase.getAttackStyleSwapAnimation());
            }
        }
        if (!transforming)
            sequence.process(this, instance.getPlayer());
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) {
            player.getAchievementDiaries().update(KourendDiary.KILL_A_HYDRA);
            player.getCombatAchievements().checkKcTask("alchemical hydra", 75, CAType.ALCHEMICAL_VETERAN);
            player.getCombatAchievements().checkKcTask("alchemical hydra", 150, CAType.ALCHEMICAL_MASTER);
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - player.getBossTimer().getCurrentTracker()) <= 105) {
                player.getCombatAchievements().complete(CAType.ALCHEMICAL_SPEED_CHASER);
            }
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - player.getBossTimer().getCurrentTracker()) <= 80) {
                player.getCombatAchievements().complete(CAType.ALCHEMICAL_SPEED_RUNNER);
            }
            if (instance.isEligibleForNoPressureTask()) {
                player.getCombatAchievements().complete(CAType.NO_PRESSURE);
            }
            if (!empoweredOnce) {
                player.getCombatAchievements().complete(CAType.MIXING_CORRECTLY);
            }
            if (!playerHitByPoisonOnce) {
                player.getCombatAchievements().complete(CAType.UNREQUIRED_ANTIPOISONS);
            }
            if (!playerGotHitOnce) {
                player.getCombatAchievements().complete(CAType.ALCLEANICAL_HYDRA);
            }
            if (!playerHitByLighteningOnce) {
                player.getCombatAchievements().complete(CAType.LIGHTNING_LURE);
            }
            if (!playerHitByFlameWallOnce) {
                player.getCombatAchievements().complete(CAType.DONT_FLAME_ME);
            }
            int kc = (int) player.getAttributes().getOrDefault(CA_TASK_INSTANCE_KC_ATT, 0) + 1;
            if (kc >= 15) {
                player.getCombatAchievements().complete(CAType.WORKING_OVERTIME);
            }
            player.getAttributes().put(CA_TASK_INSTANCE_KC_ATT, kc);
        }
    }

    @Override
    public void sendDeath() {
        final Player source = getMostDamagePlayerCheckIronman();
        onDeath(source);
        lock();
        setAnimation(preDeathAnim);
        dying = true;
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (ticks == 2) {
                    setTransformation(DEATH_SEQ_NPC_ID);
                    setAnimation(finalDeathAnim);
                } else if (ticks == 9) {
                    unlock();
                    setId(phase.getPostTransformation());
                    onFinish(source);
                    setId(HydraPhase.POISON.getPostTransformation());
                    resetNPC(HydraPhase.POISON);
                    dying = false;
                    stop();
                    return;
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public Location getRespawnTile() {
        return instance.getLocation(AlchemicalHydraInstance.hydraCenterLocation);
    }

    @Override
    protected void drop(final Location tile) {
        final Player killer = instance.getPlayer();
        if (killer == null) {
            return;
        }
        killer.getBossTimer().finishTracking("Alchemical Hydra");
        onDrop(killer);
        final List<DropProcessor> processors = DropProcessorLoader.get(HydraPhase.ENRAGED.getPostTransformation());
        if (processors != null) {
            for (final DropProcessor processor : processors) {
                processor.onDeath(this, killer);
            }
        }
        final NPCDrops.DropTable drops = NPCDrops.getTable(HydraPhase.ENRAGED.getPostTransformation());
        if (drops == null) {
            return;
        }
        NPCDrops.rollTable(killer, drops, drop -> dropItem(killer, drop, tile, true));
        NPCDrops.rollTable(killer, drops, drop -> {
            if (!drop.isAlways()) {
                dropItem(killer, drop, tile, false);
            }
        });
    }

    public void dropItem(final Player killer, final Drop drop, final Location location, final boolean uniques) {
        Item item = new Item(drop.getItemId(), Utils.random(drop.getMinAmount(), drop.getMaxAmount()));
        if (uniques) {
            final List<DropProcessor> processors = DropProcessorLoader.get(HydraPhase.ENRAGED.getPostTransformation());
            if (processors != null) {
                final Item baseItem = item;
                for (final DropProcessor processor : processors) {
                    if ((item = processor.drop(this, killer, drop, item)) == null) {
                        return;
                    }
                    if (item != baseItem) break;
                }
            }
        }
        //do NOT reference 'drop' after this line, rely on 'item' only!
        dropItem(killer, item, location, drop.isAlways());
    }

    @Override
    protected void spawnDrop(final Item item, final Location tile, final Player killer) {
        if (item.getId() == ItemId.MYSTIC_FIRE_STAFF) {
            dropItem(killer, new Item(ItemId.MYSTIC_WATER_STAFF, item.getAmount()));
        } else if (item.getId() == ItemId.RUNE_PLATEBODY) {
            dropItem(killer, killer.getAppearance().isMale() ? new Item(ItemId.RUNE_PLATELEGS, item.getAmount()) : new Item(ItemId.RUNE_PLATESKIRT, item.getAmount()));
        } else if (item.getId() == ItemId.MYSTIC_ROBE_TOP_LIGHT) {
            dropItem(killer, new Item(ItemId.MYSTIC_ROBE_BOTTOM_LIGHT, item.getAmount()));
        } else if (item.getId() == ItemId.RANGING_POTION3) {
            dropItem(killer, new Item(ItemId.SUPER_RESTORE3, 2 * item.getAmount()));
        }
        super.spawnDrop(item, tile, killer);
    }

    private void resetNPC(final HydraPhase nextPhase) {
        shielded = true;
        empoweredOnce = false;
        playerHitByPoisonOnce = false;
        playerGotHitOnce = false;
        playerHitByLighteningOnce = false;
        playerHitByFlameWallOnce = false;
        transforming = false;
        attackModifier = 1.0;
        if (nextPhase == HydraPhase.ENRAGED) {
            //If the stage didn't already switch after the last hit, we reset the counter and swap the style.
            if (autoAttacks % 3 == 0) {
                switchStyle();
            }
            autoAttacks = 0;
        } else if (nextPhase == HydraPhase.POISON) {
            //When the next stage is poison(AKA back to the first stage of the kill), we reset the counter.
            autoAttacks = 0;
            //To start off the fight, we randomize the starting attack style.
            if (Utils.random(1) == 0) {
                switchStyle();
            }
            instance.setEligibleForNoPressureTask(true);
        }
        final AttackType style = combatDefinitions.getAttackStyle();
        phase = nextPhase;
        try {
            sequence = phase.getPhaseSequence().newInstance();
            if (sequence instanceof EnragedPhase) {
                ((EnragedPhase) sequence).setPreviousStyle(style);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public AlchemicalHydraInstance getInstance() {
        return instance;
    }

    public HydraPhase getPhase() {
        return phase;
    }

    public void setPhase(HydraPhase phase) {
        this.phase = phase;
    }

    public void setSequence(HydraPhaseSequence sequence) {
        this.sequence = sequence;
    }

    public boolean isShielded() {
        return shielded;
    }

    public void setShielded(boolean shielded) {
        this.shielded = shielded;
    }

    public boolean isTransforming() {
        return transforming;
    }

    public void setTransforming(boolean transforming) {
        this.transforming = transforming;
    }

    public boolean isDying() {
        return dying;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }

    public double getAttackModifier() {
        return attackModifier;
    }

    public void setPlayerHitByPoisonOnce() { playerHitByPoisonOnce = true; }

    public void setPlayerHitByLighteningOnce() { playerHitByLighteningOnce = true; }

    public void setPlayerHitByFlameWallOnce() { playerHitByFlameWallOnce = true; }
}

package com.zenyte.game.content.boss.wildernessbosses.spiders.spindel;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.content.boss.wildernessbosses.spiders.WebObject;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.RSPolygon;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collections;

/**
 * @author Cresinkel
 */
public class Spindel extends NPC implements CombatScript, Spawnable {
    private final Animation MELEE_ATTACK_ANIM = new Animation(9991);
    private final Animation RANGED_ATTACK_ANIM = new Animation(9989);
    private final Projectile RANGED_ATTACK_PROJ = new Projectile(2356, 43, 25, 30, 15, 10, 64, 0);
    private final Graphics RANGED_HIT_GFX = new Graphics(2357);
    private final Animation MAGIC_ATTACK_ANIM = new Animation(9990);
    private final Projectile MAGIC_ATTACK_PROJ = new Projectile(2358, 43, 25, 30, 15, 10, 64, 0);
    private final Graphics MAGIC_HIT_GFX = new Graphics(2359,0,120);
    private final Projectile WEB_ATTACK_PROJ = new Projectile(2360, 43, 25, 30, 15, 25, 64, 5);
    private final Location MIDDLE = new Location(1630, 11547, 2);
    private final RSPolygon MOVE_AREA = new RSPolygon(new int [][]{
            { 1623, 11539 },
            { 1640, 11539 },
            { 1640, 11558 },
            { 1623, 11558 }
    });
    private final RSPolygon WEB_AREA = new RSPolygon(new int [][]{
            { 1623, 11539 },
            { 1630, 11539 },
            { 1630, 11526 },
            { 1633, 11526 },
            { 1633, 11539 },
            { 1640, 11539 },
            { 1640, 11558 },
            { 1623, 11558 }
    });

    private int attacks;
    private boolean started;
    private int startTicks;
    public int spiderlingAlive;
    private long walkingTicks;
    private final Object2ObjectArrayMap<Player, Integer> damageMap = new Object2ObjectArrayMap <>();

    public Spindel(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int getRespawnDelay() {
        return BossRespawnTimer.SPINDEL.getTimer().intValue();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof final Player player) {
            player.getAchievementDiaries().update(WildernessDiary.KILL_CALLISTO, 2);
            player.getHpHud().close();
        }
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) { //Spindel counts towards venenatis' CA tasks in OSRS
            player.getCombatAchievements().checkKcTask("venenatis", 10, CAType.VENENATIS_ADEPT);
            player.getCombatAchievements().checkKcTask("venenatis", 20, CAType.VENENATIS_VETERAN);
        }
    }

    @Override
    public int attack(Entity target) {
        if (target instanceof NPC) {
            return 0;
        }
        if (hasWalkSteps()) {
            return 0;
        }
        if (!started) {
            startTicks++;
            if (startTicks == 3) {
                started = true;
            }
            return 1;
        }
        if (target instanceof final Player player) {
            if (attacks < 4) {
                getCombatDefinitions().setAttackStyle("Ranged");
                for (final Entity e : getPossibleTargets(EntityType.PLAYER)) {
                    if (isWithinMeleeDistance(this, e) && Utils.random(3) != 0) {
                        if (e == target) {
                            setAnimation(MELEE_ATTACK_ANIM);
                        }
                        delayHit(0, e, melee(e, 14 + (spiderlingAlive*2)));
                    } else {
                        if (e == target) {
                            setAnimation(RANGED_ATTACK_ANIM);
                        }
                        int delay = World.sendProjectile(this, e, RANGED_ATTACK_PROJ);
                        delayHit(this, delay, e, new Hit(this, getRandomMaxHit(this, 24 + (spiderlingAlive*2), RANGED, e), HitType.RANGED));
                        WorldTasksManager.schedule(() -> {
                            e.setGraphics(RANGED_HIT_GFX);
                        },Math.max(0,delay));
                    }
                }
                if (attacks == 0) {
                    for (int i = 0; i < Utils.random(2,3); i++) {
                        SpindelSpiderling minion = new SpindelSpiderling(MIDDLE.transform(Utils.random(-5,5),Utils.random(-5,5)), 10, this);
                        World.spawnNPC(minion);
                    }
                }
            } else {
                getCombatDefinitions().setAttackStyle("Magic");
                for (final Entity e : getPossibleTargets(EntityType.PLAYER)) {
                    if (isWithinMeleeDistance(this, e) && Utils.random(3) != 0) {
                        setAnimation(MELEE_ATTACK_ANIM);
                        delayHit(0, e, melee(e, 14 + (spiderlingAlive*2)));
                    } else {
                        setAnimation(MAGIC_ATTACK_ANIM);
                        int delay = World.sendProjectile(this, e, MAGIC_ATTACK_PROJ);
                        delayHit(this, delay, e, new Hit(this, getRandomMaxHit(this, 24 + (spiderlingAlive * 2), MAGIC, e), HitType.MAGIC));
                        WorldTasksManager.schedule(() -> {
                            e.setGraphics(MAGIC_HIT_GFX);
                        },Math.max(0,delay));
                    }
                }
                if (attacks == 6) {
                    shootWebs(new Location(player.getLocation().transform(-4,-4)));
                }
            }

            attacks++;
            if (attacks == 8) {
                attacks = 0;
            }

            if (attacks == 0 || attacks % 4 == 0) {
                Location randomMoveLocation = null;
                ObjectArrayList<Location> possibleMoves = MOVE_AREA.getAllpositions(2);
                Collections.shuffle(possibleMoves);
                for (Location possibleMove : possibleMoves) {
                    if (possibleMove.getTileDistance(getLocation()) > 3) {
                        randomMoveLocation = possibleMove;
                    }
                }
                if (randomMoveLocation == null) {
                    randomMoveLocation = MIDDLE;
                }
                getCombat().setTarget(null);
                resetWalkSteps();
                addWalkSteps(randomMoveLocation.getX(), randomMoveLocation.getY());
                walkingTicks = WorldThread.WORLD_CYCLE + getWalkSteps().size() - 1;
                setFaceLocation(randomMoveLocation);
            }
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override public void setFaceEntity(Entity entity) {
        if (walkingTicks <= WorldThread.WORLD_CYCLE) {
            super.setFaceEntity(entity);
        }
    }

    @Override public boolean checkAggressivity() {
        return walkingTicks <= WorldThread.WORLD_CYCLE && super.checkAggressivity();
    }

    @Override public void autoRetaliate(Entity source) {
        if (walkingTicks <= WorldThread.WORLD_CYCLE) {
            super.autoRetaliate(source);
        }
    }

    @Override
    protected void postHitProcess(Hit hit) {
        if (hit.getSource() instanceof final Player player) {
            player.getHpHud().updateValue(getHitpoints());
        }
    }

    @Override
    public NPC spawn() {
        for (Player player : CharacterLoop.find(this.getPosition(), 30, Player.class, __ -> true)) {
            player.getHpHud().open(NpcId.SPINDEL, 515);
        }
        setAnimation(Animation.STOP);
        setAnimation(new Animation(9993));
        setAttackDistance(25);
        setAggressionDistance(25);
        setMaxDistance(50);
        setRun(true);

        attacks = 0;
        started = false;
        startTicks = 0;
        spiderlingAlive = 0;
        return super.spawn();
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equals("spindel");
    }

    private void shootWebs(Location landing) {
        int delay =  World.sendProjectile(this, landing.transform(4,4), WEB_ATTACK_PROJ);
        WorldTasksManager.schedule(() -> {
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    WebObject web;
                    if (isEmptySpot(row, column)) {
                        continue;
                    }
                    if (!WEB_AREA.contains(landing.getX() + row, landing.getY() + column)) {
                        continue;
                    }
                    if (row == 0 || row == 8 || column == 0 || column == 8) {
                        web = new WebObject(47085, 11, row == 0 ? 2 : row == 8 ? 4 : column == 0 ? 1 : 3, landing.getX() + row, landing.getY() + column, this);
                    } else if ((row == 1 || row == 7) && (column == 1 || column == 7)) {
                        web = new WebObject(47086, 11, row == 1 && column == 1 ? 1 : row == 1 && column == 7 ? 2 : row == 7 && column == 1 ? 4 : 3, landing.getX() + row, landing.getY() + column, this);
                    } else {
                        web = new WebObject(47084, 11, 1, landing.getX() + row, landing.getY() + column, this);
                    }
                    World.spawnObject(web);
                }
            }
        },Math.max(0, delay));
    }

    private boolean isEmptySpot(int row, int column) {
        if (row == 0 || row == 8) {
            return column < 2 || column > 6;
        } else if (row == 1 || row == 7) {
            return column < 1 || column > 7;
        }
        return false;
    }

    @Override
    protected void drop(Location tile) {
        damageMap.forEach((player, value) -> {
            if (player.getAreaManager().getArea() instanceof WebChasm) {
                onDrop(player);
                DropProcessorLoader.get(this.getId()).forEach(d -> d.onDeath(this, player));
            }
        });
        damageMap.clear();
        super.drop(tile);
    }

    @Override
    public boolean isForceAttackable() {
        return true;
    }
}

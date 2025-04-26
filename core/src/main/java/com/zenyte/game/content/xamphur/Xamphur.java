package com.zenyte.game.content.xamphur;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.EntityHitBar;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RSPolygon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zenyte.game.content.xamphur.XamphurConstants.*;

public class Xamphur extends NPC implements CombatScript {

    private static final Logger LOGGER = LoggerFactory.getLogger(Xamphur.class);

    public static Location center = new Location(3360, 7071);
    public static RSPolygon rsPolygon = RSPolygon.growAbsolute(center, 14);

    public static ObjectArrayList<Location> marksOfDarknessPositions = rsPolygon.getAllpositions(0);
    public static final ObjectArrayList<Location> marksOfDarkness = new ObjectArrayList<>();
    public static final ObjectArrayList<Player> handAttackPlrs = new ObjectArrayList<>();
    public static final ObjectArrayList<NPC> handNpcs = new ObjectArrayList<>();

    private AttackType attackType = AttackType.MAGIC;
    private Graphics attackGraphic = HAND_RISE_MAGIC;
    public boolean fightStarted = false;
    private SpecialAttack currentPhase = null;
    private int attackCounter;
    private int attackTypeCounter;

    public Xamphur() {
        super(XAMPHUR_NORAML, SPAWN_LOCATION, Direction.NORTH, 0);
        this.attackDistance = 25;
        this.maxDistance = 25;
        var currentHp = this.getMaxHitpoints();
        var currentOnline = World.getOnlineActivePlayerCount();
        var modifiedHp = currentHp + (currentOnline * 100);
        this.setHitpoints(modifiedHp);
        hitBar = new EntityHitBar(this) {
            @Override
            public int getType() {
                return 21;
            }
        };
    }

    @Override
    public int getRespawnDelay() {
        return 1;
    }

    public void start() {
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks) {
                    case 0:
                        setTransformation(XAMPHUR_TRANSFORMED_1);
                        setAnimation(SPAWN_ANIM);
                        break;
                    case 2:
                        setTransformation(XAMPHUR_TRANSFORMED_2);
                        setAnimation(SPAWN_ANIM_2);
                        break;

                    case 4:
                        setAnimation(Animation.STOP);
                        break;

                    case 5:
                        setTransformation(XAMPHUR_FIGHT_HANDS);
                        setAnimation(STAND_ANIM);
                        fightStarted = true;
                        spawnMarksOfDarkness();
                        stop();
                        break;
                }
                ticks++;
            }
        }, 1, 1);
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);

        if (hit.getHitType().equals(HitType.MAGIC) || handNpcs.size() > 0) {
            hit.setDamage(0);
        }

        if (hit.getSource() instanceof Player player && handNpcs.size() > 0) {
            player.sendMessage("Your hit is ineffective, you must defeat the hands first.");
        }
    }

    @Override
    protected void postHitProcess(Hit hit) {
        List<Entity> targets = getPossibleTargets(EntityType.PLAYER);
        for (Entity entity : targets) {
            if (entity instanceof final Player player) {
                if (!player.getHpHud().isOpen()) {
                    player.getHpHud().open(id, getMaxHitpoints());
                }
                player.getHpHud().updateValue(getHitpoints());
            }
        }
        super.postHitProcess(hit);
    }

    @Override
    protected boolean canMove(int fromX, int fromY, int direction) {
        return false;
    }

    @Override
    public boolean isWithinMeleeDistance(NPC npc, Entity target) {
        return true;
    }

    public static void markOfDarkness(Player target) {
        if(target.getTemporaryAttributes().containsKey("mark_of_darkness_effect")) {
            return;
        }

        target.sendMessage("<col=a53fff>A Mark of Darkness has been placed upon you.</col>");
        target.sendSound(5015);
        target.setGraphics(new Graphics(1852));
        target.getTemporaryAttributes().put("mark_of_darkness_effect", true);


        target.delay(50, () -> {
            if(!target.getTemporaryAttributes().containsKey("mark_of_darkness_effect"))
                return;
            target.getTemporaryAttributes().remove("mark_of_darkness_effect");
            target.sendMessage("<col=6800bf>Your Mark of Darkness has faded away.</col>");
            target.sendSound(5000);
        });
    }


    public void regularAttack(Player player) {
        if(player.getNextLocation() != null && player.getNextLocation().getRegionId() != this.getLocation().getRegionId())
            return;
        if (player.getLocation().getRegionId() == this.getLocation().getRegionId() && !player.isDying()) {
            player.setGraphics(attackGraphic);
            delayHit(this, 0, player, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), getAttackType(), player), getAttackType().getHitType()));
        }
    }


    public void handAttack(Player player) {
        Location firstLocation = player.getLocation().copy();
        Graphics handDrop2 = attackCounter % 2 == 0 ? HAND_DROP : HAND_DROP2;
        World.sendGraphics(handDrop2, firstLocation);
        player.delay(1, () -> {
            if (player.getLocation().getPositionHash() == firstLocation.getPositionHash() && !player.isDying()) {
                delayHit(this, -1, player, new Hit(this, Utils.random(2, 20), HitType.REGULAR));
            }
        });
    }

    public void sendHands(List<Player> players) {
        setAnimation(DROP_HANDS_ANIMATION);
        int amt = Math.min(Math.max(1, (2 * players.size() / 5)), 16);
        Direction face = Direction.getNPCDirection(getRoundedDirection());

        Location handl = getMiddleLocation().transform(Direction.WEST, 2);
        Location handr = getMiddleLocation().transform(Direction.EAST, 2);

        for (int i = 0; i < amt; i++) {
            PhantomHand leftHand = new PhantomHand(HAND_L, handl, face);
            PhantomHand rightHand = new PhantomHand(HAND_R, handr, face);
            handNpcs.add(leftHand);
            handNpcs.add(rightHand);
        }

        delay(1, () -> {
            setAnimation(Animation.STOP);
            setTransformation(XAMPHUR_FIGHT_NO_HANDS);
            for (NPC handNpc : handNpcs) {
                    handNpc.spawn();
                    handNpc.checkAggressivity();
            }
            if (isDead() || isFinished()) {
                WorldTasksManager.schedule(() -> {
                    for (NPC handNpc : handNpcs) {
                        handNpc.setHitpoints(0);
                    }
                });
            }
        });
        delay(4, () -> setTransformation(XAMPHUR_FIGHT_HANDS));
    }


    public void finishFight() {
        fightStarted = false;
        attackCounter = 0;
        setTransformation(XAMPHUR_NORAML);
        deSpawnMarksOfDarkness();
        despawnHands();
        handAttackPlrs.clear();
        for (int i = 0; i < 2; i++) {
            XamphurHandler.get().activateRandomInactiveWorldBoost();
        }
        WorldBroadcasts.sendMessage("<img=" + 22 + "><col=" + "e59400" + ">" + "<shad=000000>" + "Event: Xamphur has been defeated! Only 150 more votes until Xamphur returns!", BroadcastType.XAMPHUR, false);
        for(Player player: GlobalAreaManager.getArea(XamphurArea.class).getPlayers()) {
            if(player != null && player.getHpHud() != null) {
                player.getHpHud().close();
            }
        }
    }

    @Override
    protected void onDeath(Entity source) {
        finishFight();
        WorldTasksManager.schedule(new RemoveAllXamphurPlayersTask(), 0, 0);
        super.onDeath(source);
    }

    @Override
    protected void onFinish(@Nullable Entity source) {
        List<Entity> targets = getPossibleTargets(EntityType.PLAYER);
        for (Entity entity : targets) {
            if (entity instanceof final Player player) {
                player.getHpHud().close();
            }
        }
        super.onFinish(source);
    }

    @Override
    public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
        return false;
    }

    @Override
    public boolean isForceAggressive() {
        return true;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public boolean checkProjectileClip(Player player, boolean melee) {
        return false;
    }

    public void spawnMarksOfDarkness() {
        List<Location> allpositions = new ArrayList<>(marksOfDarknessPositions);
        Collections.shuffle(allpositions);
        for (Location position : allpositions) {
            if(marksOfDarkness.size() <= 100 && Utils.random(5) == 0) {
                marksOfDarkness.add(position);
                World.spawnObject(new WorldObject(MARK_OF_DARKNESS_OBJECT_ID, 10, 0, position));
            }
        }
    }
    public void deSpawnMarksOfDarkness() {
        for(Location location : marksOfDarkness) {
            World.removeObject(World.getObjectWithType(location, 10));
        }

        marksOfDarkness.clear();
    }

    public void despawnHands() {
        for (NPC handNpc : handNpcs) {
            if(handNpc == null || handNpc.isDead() || handNpc.isFinished()) {
                continue;
            }
            handNpc.setHitpoints(0);
        }
    }

    @Override
    public int attack(Entity target) {
        List<Player> players = CharacterLoop.find(getLocation(), 20, Player.class,
                (p) ->  p != null && !p.isDead() && !p.isDying() && !p.isFinished() && p.isInitialized());
        attackCounter++;

        if(currentPhase == SpecialAttack.QUICK_HANDS) {
            fillHandAttackList(players);
            handAttackPlrs.forEach(this::handAttack);
            if(attackCounter >= 40) {
                attackCounter = 0;
                currentPhase = null;
            }
            return 1;
        }

        if(currentPhase == SpecialAttack.HANDS_MINION && handNpcs.size() == 0) {
            despawnHands();
            sendHands(players);
            attackCounter = 0;
            currentPhase = null;
            return 6;
        }


        setAnimation(ATTACK_ANIMATION);

        players.forEach(this::regularAttack);

        if(currentPhase == null) {
            attackTypeCounter++;

            if (attackTypeCounter >= 5) {
                switchAttackType();
            }
        }

        if(attackCounter >= 10) {
            attackCounter = 0;
            currentPhase = Utils.random(SpecialAttack.VALUES);
            setForceTalk(currentPhase.message);
        }
        return 5;
    }

    @Override
    public boolean isAttackableNPC() {
        return fightStarted || hasMinions();
    }

    private boolean hasMinions() {
        for (NPC handNpc : handNpcs) {
            if(!handNpc.isFinished())
                return true;
        }
        return false;
    }

    public void fillHandAttackList(List<Player> players) {
        handAttackPlrs.removeIf(p -> p.isDying() || p.getLocation().getRegionId() != getLocation().getRegionId());
        for (Player player1 : players) {
            if(handAttackPlrs.size() >= 5)
                return;
            if(Utils.random(5) == 0 && !handAttackPlrs.contains(player1))
                handAttackPlrs.add(player1);
        }
    }

    @Override
    protected void drop(Location tile) {
        if(!XamphurHandler.get().isEnabled()) {//no drops for anyone
            return;
        }
        super.drop(tile);
    }

    public AttackType getAttackType() {
        return attackType;
    };

    public void switchAttackType() {
        attackTypeCounter = 0;
        attackType = attackType == AttackType.MAGIC ? AttackType.RANGED : attackType == AttackType.RANGED ? AttackType.MELEE : AttackType.MAGIC;
        attackGraphic = attackType == AttackType.MAGIC ? HAND_RISE_MAGIC : attackType == AttackType.RANGED ? HAND_RISE_RANGED : HAND_RISE_MELEE;
    }

    @Override
    public boolean canAttack(Player source) {
        return fightStarted;
    }

    enum SpecialAttack {
        QUICK_HANDS("Your death is at hand."),
        HANDS_MINION("You will fall by my hand."),
        MIMIC("");
        static final SpecialAttack[] VALUES = values();
        final String message;
        SpecialAttack(String message) {
            this.message = message;
        }

    }

}

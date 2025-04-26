package com.zenyte.game.content.boss.wildernessbosses.vetion;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.EntityHitBar;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * @author Andys1814
 */
public final class Vetion extends NPC implements CombatScript {

    // 9969 - spawn or some shit, same as 9970
    // 9971, 9972
    // 9973, 9974 - shields
    // 9975 - idfk
    // 9980 - death

    // blue lighting 2346
    // orange lighting 2347
    // sword thing 2348

    private static final Animation LIGHTNING_ANIMATION_SMITE = new Animation(9969);

    private static final Animation LIGHTNING_ANIMATION_STRIKE = new Animation(9971);

    private static final Animation SHIELD_BASH_ANIMATION = new Animation(9973);

    private static final Animation HELLHOUND_SUMMON_ANIMATION = new Animation(9975);

    private static final Graphics REGULAR_LIGHTNING = new Graphics(2346);

    private static final Graphics ENRAGED_LIGHTNING = new Graphics(2347);


    private static final ForceTalk TRANSFORM_FORCE_TALK = new ForceTalk("Now... DO IT AGAIN!!!");

    private boolean calvarion;

    private Class<? extends PolygonRegionArea> area;

    private boolean spawnedHellhounds = false;

    private final Object2ObjectArrayMap<Player, Integer> damageMap = new Object2ObjectArrayMap <>();

    private Set<SkeletalHellhound> hellhounds = new HashSet<>();

    private enum Phase {
        REGULAR,
        ENRAGED
    }

    private final String[] FORCE_CHATS = {
            "You are powerless to me!",
            "You're not blocking this one!",
            "You are weak! You are feeble!",
            "Die, rodent!",
            "Stand still, rat!",
            "You're mine now!",
            "Now I've got you!",
            "Hold still so I can smite you!",
            "Take this!",
            "Time to die, mortal!",
            "Raaargh!",
            "Grrrah!",
            "For the lord!",
            "You can't escape!",
            "Filthy whelps!",
            "Dodge this!",
            "I will cut you down!",
            "You call that a weapon?!"
    };

    private final String[] HOUND_SPAWN_CHATS = {
            "Gah! Hounds! Get them!",
            "Time to feast, hounds!",
            "Hounds! Dispose of these tresspassers!",
            "Hounds! Get rid of these interlopers!",
            "Go forth, my hounds, and destroy them!",
            "I've had enough of this! Hounds!"
    };

    private final String[] HOUND_DEATH_CHATS = {
        "I'll kill you for killing my pets!",
        "My hounds! I'll make you pay for that!",
        "Must I do everything around here?!",
        "Fine! I'll deal with you myself!"
    };

    private final String[] DEATH_CHATS = {
        "This isn't... the last... of me...",
        "Urk! I... failed...",
        "I'll get you... next... time...",
        "My lord... I'm... sorry...",
        "Urg... not... again...",
        "I'll... be... back..."
    };

    private Phase phase = Phase.REGULAR;

    public Vetion(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        calvarion = id == NpcId.CALVARION;
        area = calvarion ? SkeletalTomb.class : VetionsRest.class;
        hitBar = new EntityHitBar(this) {
            @Override
            public int getType() {
                return 18;
            }
        };

        // Manually set the death delay for this NPC because the new animation system broke animation duration calculation
        // TODO we can remove this if we properly find out how to calculate animation durations for new anim system
        deathDelay = 2;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        Iterator<SkeletalHellhound> iter = hellhounds.iterator();
        while (iter.hasNext()) {
            SkeletalHellhound hellhound = iter.next();
            if (hellhound == null || hellhound.isFinished()) {
                iter.remove();
            }
            if (hellhounds.isEmpty()) {
                forceTalk(Utils.random(HOUND_DEATH_CHATS));
            }
        }
    }


    @Override
    public int attack(Entity target) {
        List<Entity> targets = getPossibleTargets(EntityType.PLAYER, 4);
        if (!targets.isEmpty() && Utils.roll(50)) {
            Player player = (Player) Utils.getRandomCollectionElement(targets);
            setFaceEntity(player);
            setAnimation(SHIELD_BASH_ANIMATION);
            return 6;
        }

        // Default lightning attack
        forceTalk(Utils.random(FORCE_CHATS));
        lightningAttack(target);
        return 6;
    }

    private void shieldBashAttack() {

    }

    private void lightningAttack(Entity target) {
        Animation animation = Utils.roll(50) ? LIGHTNING_ANIMATION_SMITE : LIGHTNING_ANIMATION_STRIKE;
        setAnimation(animation);

        // Find list of eligible tiles based on the radius around the target
        List<Location> radius = new ArrayList<>(5 * 5);
        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                radius.add(target.getLocation().copy().transform(x, y));
            }
        }

        Set<Location> tiles = new HashSet<>();

        // Find the tile that we want to spawn "adjacent" to the player
        Location adjacent = target.getLocation().copy().transform(Utils.random(-1, 1), Utils.random(-1, 1));
        if (!World.isFloorFree(adjacent, 1)) {
            // if the adjacent tile is in a wall just fallback to the player's tile
            adjacent = target.getLocation().copy();
        }

        tiles.add(adjacent);

        // Randomly find 3-4 more tiles
        Collections.shuffle(radius);
        Iterator<Location> iter = radius.iterator();
        // Continue to randomly add tiles until we have our 5 selected
        while (iter.hasNext() && tiles.size() < 5) {
            Location tile = iter.next();
            if (World.isFloorFree(tile, 1)) {
                tiles.add(tile);
            }
            iter.remove();
        }

        for (Location tile : tiles) {
            Graphics lightning = phase == Phase.ENRAGED ? ENRAGED_LIGHTNING : REGULAR_LIGHTNING;
            lightningHit(tile, lightning);
        }
    }

    private void lightningHit(Location tile, Graphics graphics) {
        World.sendGraphics(graphics, tile);
        WorldTasksManager.schedule(() -> {
            final Set<Player> players = GlobalAreaManager.getArea(area).getPlayers();
            for (Player player : players) {
                int delta = (int) player.getLocation().getDistance(tile);
                if (delta <= 1) {
                    delayHit(0, player, new Hit(Utils.random(delta == 0 ? (calvarion ? 20 : 30) : (calvarion ? 10 : 15)), HitType.DEFAULT));
                }
            }
        }, 2);
    }

    @Override
    public NPC spawn() {
        List<Entity> targets = getPossibleTargets(EntityType.PLAYER);
        for (Entity entity : targets) {
            if (entity instanceof final Player player) {
                player.getHpHud().open(id, getMaxHitpoints());
            }
        }
        return super.spawn();
    }

    private void lightningRing() {
        Location middle = getMiddleLocation();
        Location[] ring = new Location[] {
                middle.transform(1, 3),
                middle.transform(2, 2),
                middle.transform(3, 1),

                middle.transform(3, -1),
                middle.transform(2, -2),
                middle.transform(1, -3),

                middle.transform(-1, -3),
                middle.transform(-2, -2),
                middle.transform(-3, -1),

                middle.transform(-3, 1),
                middle.transform(-2, 2),
                middle.transform(-1, 3)
        };

        setAnimation(LIGHTNING_ANIMATION_SMITE);
        combat.setCombatDelay(6);
        freeze(6);

        for (Location tile : ring) {
            if (!World.isFloorFree(tile, 1)) {
                continue;
            }
            lightningHit(tile, ENRAGED_LIGHTNING);
        }
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        if (!isDamageable()) {
            final Entity source = hit.getSource();
            hit.setDamage(0);
            if (source instanceof Player) {
                ((Player) source).sendMessage("You must kill the Skeletal Hellhounds first in order to harm Vet'ion!");
            }
        }
        super.handleIngoingHit(hit);
        appendPlayerDamage(hit);
    }

    private void appendPlayerDamage(Hit hit) {
        Entity source = hit.getSource();
        if (source == null) {
            return;
        }

        if (source instanceof Player player) {
            damageMap.compute(player, (k, v) -> Integer.sum(v == null ? 0 : v, hit.getDamage()));
        }
    }

    @Override
    protected void postHitProcess(Hit hit) {
        if (getHitpointsAsPercentage() <= 50 && !spawnedHellhounds && !isDead()) {
            spawnHellhounds();
            spawnedHellhounds = true;
        }

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
    public float getXpModifier(final Hit hit) {
        return isDamageable() ? 1 : 0;
    }

    private void spawnHellhounds() {
        final Set<Player> players = GlobalAreaManager.getArea(area).getPlayers();
        // Two hellhound for the "main target" and one for each additional player in the room, up to 25 max
        int numHellhounds = calvarion  ? 2 : Math.min(2 + players.size() - 1, 25);

        Stack<Location> coords = new Stack<>();
        while (coords.size() < numHellhounds) {
            final int x = calvarion ? Utils.random(1876, 1894) : Utils.random(3288, 3302);
            final int y = calvarion ? Utils.random(11540, 11552) : Utils.random(10196, 10208);
            final Location coord = new Location(x, y, 1);
            if (coords.contains(coord) || !World.isFloorFree(1, x, y)) {
                continue;
            }
            coords.add(coord);
        }

        if (!coords.isEmpty()) {
            String talk = Utils.random(HOUND_SPAWN_CHATS);
            if (phase == Phase.ENRAGED) {
                talk = talk.toUpperCase();
            }
            setForceTalk(talk);
            setAnimation(HELLHOUND_SUMMON_ANIMATION);
            combat.setCombatDelay(6);
        }

        int id = phase == Phase.ENRAGED ? 6614 : 6613;
        spawnHellhound(id, coords.pop(), getCombat().getTarget());
        spawnHellhound(id, coords.pop(), getCombat().getTarget());

        // Spawn one hellhound for each of the remaining players in the room
        for (Player player : players) {
            if (player == getCombat().getTarget()) {
                continue;
            }
            if (!coords.isEmpty()) {
                spawnHellhound(id, coords.pop(), player);
            }
        }
    }

    private void spawnHellhound(int npcId, Location tile, Entity target) {
        SkeletalHellhound hellhound = new SkeletalHellhound(npcId, tile, Direction.SOUTH, 0);
        hellhounds.add(hellhound);
        hellhound.spawn();
        if (target != null) {
            hellhound.setTarget(target);
        }
    }

    private void forceTalk(String text) {
        String talk = text;
        if (phase == Phase.ENRAGED) {
            talk = talk.toUpperCase();
        }
        setForceTalk(talk);
    }

    private boolean isDamageable() {
        if (hellhounds.isEmpty()) {
            return true;
        }

        for (SkeletalHellhound hellhound : hellhounds) {
            if (hellhound != null && !hellhound.isFinished()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public int getRespawnDelay() {
        return calvarion ? 30 : 33;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public void sendDeath() {
        if (getHitpoints() == 0 && phase == Phase.REGULAR) {
            // on first death it will transform
            setTransformation(calvarion ? NpcId.CALVARION_11994 : 6612);
            heal(getMaxHitpoints());
            lightningRing();
            setForceTalk(TRANSFORM_FORCE_TALK);
            phase = Phase.ENRAGED;
            List<Entity> targets = getPossibleTargets(EntityType.PLAYER);
            for (Entity entity : targets) {
                if (entity instanceof final Player player) {
                    player.getHpHud().open(id, getMaxHitpoints());
                }
            }
        } else {
            // "official" death
            forceTalk(Utils.random(DEATH_CHATS));
            super.sendDeath();
            setId(calvarion ? NpcId.CALVARION : NpcId.VETION);
            phase = Phase.REGULAR;
        }
        spawnedHellhounds = false;
        hellhounds.clear();
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);
        List<Entity> targets = getPossibleTargets(EntityType.PLAYER);
        if (source instanceof final Player player) {
            player.getAchievementDiaries().update(WildernessDiary.KILL_CALLISTO, 4);
        }
        for (Entity entity : targets) {
            if (entity instanceof final Player player) {
                player.getHpHud().close();
            }
        }
        if (source instanceof Player player) {
            player.getCombatAchievements().checkKcTask("vet'ion", 10, CAType.VETION_ADEPT);
            player.getCombatAchievements().checkKcTask("vet'ion", 20, CAType.VETERAN);
        }
    }

    /**
     * Override default item drop logic so that we can give drops to each of the (up to) top 10 damagers.
     * <p>
     * However, the "rare" drops are only rolled once per kill. For example, only one player can get a rare per kill,
     * even if 10 players damaged Vet'ion.
     *
     * This should be WAY easier but Zenyte drop system is putrid :)
     */
    @Override
    protected void drop(final Location tile) {
        List<Player> players = new ArrayList<>(damageMap.keySet());
        players.sort((a, b) -> damageMap.getOrDefault(b, 0) - damageMap.getOrDefault(a, 0));

        // Only drop items for 10 players at most
        for (int i = 0; i < 10; i++) {
            if (i >= players.size()) {
                break;
            }

            final Player player = players.get(i);
            if (player == null) {
                return;
            }

            if (i == 0) {
                // Only execute drop processors for the top killer
                final List<DropProcessor> processors = DropProcessorLoader.get(id);
                if (processors != null) {
                    for (final DropProcessor processor : processors) {
                        processor.onDeath(this, player);
                    }
                }
            }

            final NPCDrops.DropTable drops = NPCDrops.getTable(getId());
            if (drops == null) {
                return;
            }

            int finalI = i;
            NPCDrops.rollTable(player, drops, drop -> {
                Item item = new Item(drop.getItemId(), Utils.random(drop.getMinAmount(), drop.getMaxAmount()));
                // Only execute drop processors for the top killer
                if (finalI == 0) {
                    final List<DropProcessor> processors = DropProcessorLoader.get(id);
                    if (processors != null) {
                        final Item baseItem = item;
                        for (final DropProcessor processor : processors) {
                            if ((item = processor.drop(this, player, drop, item)) == null) {
                                return;
                            }
                            if (item != baseItem) break;
                        }
                    }
                }
                dropItem(player, item, location, drop.isAlways());
            });
        }
        damageMap.clear();
    }

    @Override
    public boolean isForceAttackable() {
        return calvarion;
    }
}

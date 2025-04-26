package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.model.HintArrowPosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ReceivedDamage;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.area.darkcaves.FaladorMoleLairArea;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Tommeh | 02/05/2019 | 18:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GiantMole extends NPC implements Spawnable {

    public static final Animation DIG_START = new Animation(3314);
    public static final Animation DIG_END = new Animation(3315);

    private int burrowCount;
    private int lastBurrowTicks;

    public static final List<Location> RESURFACE_LOCATIONS = List.of(
            new Location(1736, 5227, 0),
            new Location(1776, 5237, 0),
            new Location(1779, 5208, 0),
            new Location(1769, 5199, 0),
            new Location(1748, 5206, 0),
            new Location(1741, 5187, 0),
            new Location(1746, 5170, 0),
            new Location(1773, 5173, 0),
            new Location(1760, 5163, 0),
            new Location(1755, 5151, 0),
            new Location(1739, 5151, 0)
    );

    private final List<Location> resurfaceLocations = new ObjectArrayList<>(RESURFACE_LOCATIONS);
    private boolean damagedAnyone = false;
    private boolean canBurrow = true;


    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (hit.getDamage() > 0) {
            damagedAnyone = true;
        }
    }

    public GiantMole(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.maxDistance = 100;
        // this.radius = 100;
    }

    @Override
    public int getRespawnDelay() {
        return 15;
    }

    @Override
    public NPC spawn() {
        if (!isFinished()) {
            throw new RuntimeException("The NPC has already been spawned.");
        }
        World.addNPC(this);
        location.setLocation(getRespawnTile());
        setFinished(false);
        updateLocation();
        if (!combatDefinitionsMap.isEmpty()) {
            combatDefinitionsMap.clear();
        }
        updateCombatDefinitions();
        updateHintArrow();
        return this;
    }

    @Override
    public void onMovement() {
        updateHintArrow();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (canBurrow) {
            final Set<Player> players = GlobalAreaManager.get("Falador Mole Lair").getPlayers();
            for (final Player player : players) {
                player.getPacketDispatcher().resetHintArrow();
            }
        }
        if (source instanceof Player player) {
            player.getAchievementDiaries().update(FaladorDiary.KILL_GIANT_MOLE);
        }
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof Player player) {
            player.getCombatAchievements().checkKcTask("giant mole", 10, CAType.GIANT_MOLE_NOVICE);
            player.getCombatAchievements().checkKcTask("giant mole", 25, CAType.GIANT_MOLE_CHAMPION);
            if (!damagedAnyone) {
                player.getCombatAchievements().complete(CAType.AVOIDING_THOSE_LITTLE_ARMS);
            }
            if (lastBurrowTicks > 0) {
                player.getCombatAchievements().complete(CAType.WHY_ARE_YOU_RUNNING);
            }
            if (burrowCount <= 2) {
                player.getCombatAchievements().complete(CAType.WHACK_A_MOLE);
            }
            final List<ReceivedDamage> damageList = getReceivedDamage().get(Pair.of(player.getUsername(), player.getGameMode()));
            if (damageList != null && damageList.size() <= 4) {
                player.getCombatAchievements().complete(CAType.HARD_HITTER);
            }
        }
    }

    @Override public void processNPC() {
        super.processNPC();
        if (lastBurrowTicks > 0) {
            lastBurrowTicks--;
        }
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
        if (!canBurrow)
            return;
        if (!isDead() && getHitpoints() <= getMaxHitpoints() / 2 && Utils.random(4) == 0) {
            final Entity target = hit.getSource();
            if (target == null) {
                super.handleIngoingHit(hit);
                return;
            }
            if (target instanceof Player) {
                final Player player = (Player) target;
                player.cancelCombat();
                WorldTasksManager.schedule(() -> player.getPacketDispatcher().sendClientScript(896, 135, 12027));
            }
            setAnimation(DIG_START);
            lock();
            WorldTasksManager.schedule(() -> {
                Location location = null;
                Collections.shuffle(resurfaceLocations);
                for (final Location l : resurfaceLocations) {
                    if (!getLocation().withinDistance(l, 10)) {
                        location = l;
                        break;
                    }
                }
                burrowCount++;
                lastBurrowTicks = 17;
                setAnimation(DIG_END);
                setLocation(location);
                unlock();
            }, 2);
        }
    }

    private void updateHintArrow() {
        if (!canBurrow)
            return;
        final Location middle = getMiddleLocation();
        final Set<Player> players = GlobalAreaManager.get("Falador Mole Lair").getPlayers();
        for (final Player player : players) {
            if (player.getInventory().containsAnyOf(FaladorMoleLairArea.shields)
                    || player.getEquipment().containsAnyOf(FaladorMoleLairArea.shields)) {
                player.getPacketDispatcher().sendHintArrow(new HintArrow(middle.getX(), middle.getY(), (byte) 100,
                        HintArrowPosition.EAST));
            } else {
                player.getPacketDispatcher().resetHintArrow();
            }
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equals("giant mole");
    }


    public void setCanBurrow(boolean canBurrow) {
        this.canBurrow = canBurrow;
    }
}

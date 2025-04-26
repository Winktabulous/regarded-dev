package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.model.HintArrowPosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.darkcaves.FaladorMoleLairArea;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.object.memberzones.GiantMoleInstance;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collections;
import java.util.List;

import static com.zenyte.game.world.entity.npc.impl.GiantMole.DIG_END;
import static com.zenyte.game.world.entity.npc.impl.GiantMole.DIG_START;

@SkipPluginScan
public final class GiantMoleInstanced extends NPC implements Spawnable {

    private final List<Location> resurfaceLocations = new ObjectArrayList<>();

    private final Player owner;

    public GiantMoleInstanced(int id, Location tile, Direction facing, int radius, Player player,
                              GiantMoleInstance giantMoleInstance) {
        super(id, tile, facing, radius);
        this.maxDistance = 100;
        this.owner = player;
        for (Location loc : GiantMole.RESURFACE_LOCATIONS) {
            resurfaceLocations.add(giantMoleInstance.getLocation(loc));
        }
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
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getPacketDispatcher().resetHintArrow();
            player.getAchievementDiaries().update(FaladorDiary.KILL_GIANT_MOLE);
        }
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
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
                setAnimation(DIG_END);
                setLocation(location);
                unlock();
            }, 2);
        }
    }

    private void updateHintArrow() {
        final Location middle = getMiddleLocation();
        if (owner.getInventory().containsAnyOf(FaladorMoleLairArea.shields)
                | owner.getEquipment().containsAnyOf(FaladorMoleLairArea.shields)) {
            owner.getPacketDispatcher().sendHintArrow(new HintArrow(middle.getX(), middle.getY(), (byte) 100,
                    HintArrowPosition.EAST));
        } else {
            owner.getPacketDispatcher().resetHintArrow();
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equals("giant mole");
    }

}

package com.zenyte.game.content.minigame.puropuro;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.content.drops.table.impl.ImmutableDropTable;
import com.zenyte.game.content.skills.hunter.node.Impling;
import com.zenyte.game.content.skills.hunter.npc.ImplingNPC;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.LoginPlugin;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Arrays;

public class PuroPuroArea extends PolygonRegionArea implements CycleProcessPlugin, LoginPlugin {
    private static final ObjectArrayList<PuroPuroImplingSpawn> spawns = new ObjectArrayList<>(Arrays.asList(new PuroPuroImplingSpawn(new Location(2563, 4291, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2563, 4348, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2569, 4323, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2571, 4305, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2581, 4300, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2596, 4296, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2609, 4339, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2610, 4304, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2615, 4322, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2620, 4291, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2620, 4348, 0), Impling.BABY), new PuroPuroImplingSpawn(new Location(2564, 4321, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2573, 4330, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2574, 4321, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2590, 4348, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2592, 4291, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2595, 4343, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2612, 4327, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2612, 4309, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2619, 4322, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2587, 4300, 0), Impling.YOUNG), new PuroPuroImplingSpawn(new Location(2568, 4296, 0), Impling.GOURMET), new PuroPuroImplingSpawn(new Location(2569, 4327, 0), Impling.GOURMET), new PuroPuroImplingSpawn(new Location(2574, 4311, 0), Impling.GOURMET), new PuroPuroImplingSpawn(new Location(2574, 4311, 0), Impling.GOURMET), new PuroPuroImplingSpawn(new Location(2585, 4296, 0), Impling.GOURMET), new PuroPuroImplingSpawn(new Location(2597, 4293, 0), Impling.GOURMET), new PuroPuroImplingSpawn(new Location(2609, 4317, 0), Impling.GOURMET), new PuroPuroImplingSpawn(new Location(2615, 4298, 0), Impling.GOURMET), new PuroPuroImplingSpawn(new Location(2618, 4321, 0), Impling.GOURMET), new PuroPuroImplingSpawn(new Location(2570, 4330, 0), Impling.EARTH), new PuroPuroImplingSpawn(new Location(2598, 4340, 0), Impling.EARTH), new PuroPuroImplingSpawn(new Location(2587, 4342, 0), Impling.EARTH), new PuroPuroImplingSpawn(new Location(2612, 4310, 0), Impling.EARTH), new PuroPuroImplingSpawn(new Location(2611, 4334, 0), Impling.EARTH), new PuroPuroImplingSpawn(new Location(2567, 4319, 0), Impling.ECLECTIC), new PuroPuroImplingSpawn(new Location(2591, 4340, 0), Impling.ECLECTIC), new PuroPuroImplingSpawn(new Location(2591, 4295, 0), Impling.ECLECTIC), new PuroPuroImplingSpawn(new Location(2615, 4326, 0), Impling.ECLECTIC)));
    private static final ObjectArrayList<Location> rareSpawns = new ObjectArrayList<>(Arrays.asList(new Location(2568, 4343, 0), new Location(2615, 4342, 0), new Location(2615, 4296, 0), new Location(2568, 4295, 0)));
    private static final ObjectArrayList<MagicalWheatSpawn> wheatSpawns = new ObjectArrayList<>(Arrays.asList(
    // perpendicular
    new MagicalWheatSpawn(new Location(2584, 4330), new Location(2584, 4329), true), new MagicalWheatSpawn(new Location(2594, 4330), new Location(2594, 4329), true), new MagicalWheatSpawn(new Location(2601, 4333), new Location(2601, 4332), true), new MagicalWheatSpawn(new Location(2582, 4333), new Location(2582, 4332), true), new MagicalWheatSpawn(new Location(2584, 4336), new Location(2584, 4335), true), new MagicalWheatSpawn(new Location(2576, 4339), new Location(2576, 4338), true), new MagicalWheatSpawn(new Location(2598, 4336), new Location(2598, 4335), true), new MagicalWheatSpawn(new Location(2604, 4339), new Location(2604, 4338), true), new MagicalWheatSpawn(new Location(2585, 4310), new Location(2585, 4309), true), new MagicalWheatSpawn(new Location(2598, 4310), new Location(2598, 4309), true), new MagicalWheatSpawn(new Location(2587, 4307), new Location(2587, 4306), true), new MagicalWheatSpawn(new Location(2583, 4304), new Location(2583, 4303), true), new MagicalWheatSpawn(new Location(2602, 4307), new Location(2602, 4306), true), new MagicalWheatSpawn(new Location(2603, 4304), new Location(2603, 4303), true), 
    // regular
    new MagicalWheatSpawn(new Location(2582, 4346), new Location(2583, 4346), false), new MagicalWheatSpawn(new Location(2618, 4305), new Location(2618, 4304), false), new MagicalWheatSpawn(new Location(2600, 4293), new Location(2601, 4293), false), new MagicalWheatSpawn(new Location(2565, 4311), new Location(2565, 4310), false), new MagicalWheatSpawn(new Location(2595, 4343), new Location(2596, 4343), false), new MagicalWheatSpawn(new Location(2615, 4315), new Location(2615, 4314), false), new MagicalWheatSpawn(new Location(2583, 4296), new Location(2584, 4296), false), new MagicalWheatSpawn(new Location(2568, 4330), new Location(2568, 4329), false), new MagicalWheatSpawn(new Location(2601, 4340), new Location(2602, 4340), false), new MagicalWheatSpawn(new Location(2612, 4324), new Location(2612, 4323), false), new MagicalWheatSpawn(new Location(2609, 4310), new Location(2609, 4309), false), new MagicalWheatSpawn(new Location(2586, 4302), new Location(2587, 4302), false), new MagicalWheatSpawn(new Location(2590, 4299), new Location(2591, 4299), false), new MagicalWheatSpawn(new Location(2599, 4305), new Location(2600, 4305), false), new MagicalWheatSpawn(new Location(2574, 4311), new Location(2574, 4310), false), new MagicalWheatSpawn(new Location(2571, 4316), new Location(2571, 4315), false), new MagicalWheatSpawn(new Location(2581, 4337), new Location(2582, 4337), false), new MagicalWheatSpawn(new Location(2577, 4328), new Location(2577, 4327), false), new MagicalWheatSpawn(new Location(2586, 4334), new Location(2587, 4334), false), new MagicalWheatSpawn(new Location(2606, 4329), new Location(2606, 4328), false), new MagicalWheatSpawn(new Location(2595, 4308), new Location(2596, 4308), false), new MagicalWheatSpawn(new Location(2603, 4314), new Location(2603, 4313), false), new MagicalWheatSpawn(new Location(2596, 4331), new Location(2597, 4331), false), new MagicalWheatSpawn(new Location(2580, 4326), new Location(2580, 4325), false)));
    private static final ImmutableDropTable rareImplingTable = new DropTable().append(Impling.ESSENCE.getNpcId(), 75).append(Impling.NATURE.getNpcId(), 45).append(Impling.MAGPIE.getNpcId(), 25).append(Impling.NINJA.getNpcId(), 15).append(Impling.DRAGON.getNpcId(), 10).append(Impling.LUCKY.getNpcId(), 5).toImmutable().build();

    public PuroPuroArea() {
        rareSpawns.forEach(PuroPuroArea::spawnRareImpling);
        spawns.forEach(s -> new ImplingNPC(s.getImpling().getNpcId(), s.getLocation(), Direction.SOUTH)
                .setInvisibleSpawn(Utils.random(10) < 3)
                .spawn());
        wheatSpawns.forEach(w -> {
            if (Utils.random(1) == 0) {
                w.spawn();
            }
        });
    }

    public static void spawnRareImpling(final Location location) {
        new ImplingNPC(rareImplingTable.rollInt(), location, Direction.SOUTH)
                .setInvisibleSpawn(true)
                .setOnFinished(npc -> spawnRareImpling(npc.getRespawnTile()))
                .spawn();
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2562, 4350}, {2562, 4289}, {2623, 4289}, {2623, 4350}}, 0)};
    }

    @Override
    public void enter(Player player) {
        player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED);
        if (player.getAttributeOrDefault(PuroPuroImplingOverlay.PURO_PURO_OVERLAY_ATTRIBUTE, false)) {
            GameInterface.PURO_PURO_IMPLING_OVERLAY.open(player);
        }
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
        player.getInterfaceHandler().closeInterface(GameInterface.PURO_PURO_IMPLING_OVERLAY);
    }

    @Override
    public String name() {
        return "Puro-Puro";
    }

    @Override
    public void process() {
        wheatSpawns.forEach(w -> {
            if (!w.isSpawnCooldown()) {
                if (Utils.random(600) < 5) {
                    if (w.isVisible()) {
                        w.despawn();
                    } else {
                        w.spawn();
                    }
                }
            }
        });
    }

    @Override
    public void login(Player player) {
        if (!World.isFloorFree(player.getLocation(), 1)) {
            WorldTasksManager.schedule(() -> player.setRouteEvent(new TileEvent(player, new TileStrategy(player.getLocation(), 1), null)));
        }
    }


    private static class PuroPuroImplingSpawn {
        private final Location location;
        private final Impling impling;

        public PuroPuroImplingSpawn(Location location, Impling impling) {
            this.location = location;
            this.impling = impling;
        }

        public Location getLocation() {
            return location;
        }

        public Impling getImpling() {
            return impling;
        }

        @Override
        public String toString() {
            return "PuroPuroArea.PuroPuroImplingSpawn(location=" + this.getLocation() + ", impling=" + this.getImpling() + ")";
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof PuroPuroArea.PuroPuroImplingSpawn)) return false;
            final PuroPuroArea.PuroPuroImplingSpawn other = (PuroPuroArea.PuroPuroImplingSpawn) o;
            if (!other.canEqual(this)) return false;
            final Object this$location = this.getLocation();
            final Object other$location = other.getLocation();
            if (this$location == null ? other$location != null : !this$location.equals(other$location)) return false;
            final Object this$impling = this.getImpling();
            final Object other$impling = other.getImpling();
            return this$impling == null ? other$impling == null : this$impling.equals(other$impling);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof PuroPuroArea.PuroPuroImplingSpawn;
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $location = this.getLocation();
            result = result * PRIME + ($location == null ? 43 : $location.hashCode());
            final Object $impling = this.getImpling();
            result = result * PRIME + ($impling == null ? 43 : $impling.hashCode());
            return result;
        }
    }


    public static class MagicalWheatSpawn {
        public static final int REGULAR_WHEAT_PERP = 25016;
        public static final int REGULAR_WHEAT = 25019;
        public static final int SPAWNED_WHEAT = 25021;
        private static final int GROWING_WHEAT = 25022;
        private static final int WILTING_WHEAT = 25023;
        private static final int WHEAT_DECORATION_PERP = 25027;
        private static final int WHEAT_DECORATION = 25028;
        private static final Animation growingWheatAnimation = new Animation(6596);
        private static final Animation wiltingWheatAnimation = new Animation(6599);
        private final Location first;
        private final Location second;
        private final boolean perpendicular;
        private boolean visible;
        private boolean spawnCooldown;

        public boolean isVertical() {
            return first.getX() == second.getX();
        }

        public void spawn() {
            spawnCooldown = true;
            visible = true;
            final WorldObject first = new WorldObject(GROWING_WHEAT, 22, isVertical() ? 0 : 1, getFirst());
            final WorldObject second = new WorldObject(GROWING_WHEAT, 22, isVertical() ? 0 : 1, getSecond());
            World.sendSoundEffect(first, new SoundEffect(3729, 10));
            World.spawnObject(first);
            World.spawnObject(second);
            WorldTasksManager.schedule(() -> {
                World.sendObjectAnimation(first, growingWheatAnimation);
                World.sendObjectAnimation(second, growingWheatAnimation);
            }, 1);
            WorldTasksManager.schedule(() -> {
                World.spawnObject(new WorldObject(perpendicular ? WHEAT_DECORATION_PERP : WHEAT_DECORATION, 22, perpendicular ? 0 : isVertical() ? 0 : 1, getFirst().transform(isVertical() ? Direction.NORTH : Direction.WEST, 1)));
                World.spawnObject(new WorldObject(perpendicular ? WHEAT_DECORATION_PERP : WHEAT_DECORATION, 22, perpendicular ? 2 : isVertical() ? 0 : 1, getSecond().transform(isVertical() ? Direction.SOUTH : Direction.EAST, 1)));
                World.spawnObject(new WorldObject(SPAWNED_WHEAT, 22, isVertical() ? 0 : 1, getFirst()));
                World.spawnObject(new WorldObject(SPAWNED_WHEAT, 22, isVertical() ? 0 : 1, getSecond()));
                moveEntitiesOffWheat();
                spawnCooldown = false;
            }, (int) TimeUnit.MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(growingWheatAnimation)));
        }

        public void despawn() {
            spawnCooldown = true;
            visible = false;
            final WorldObject first = new WorldObject(WILTING_WHEAT, 22, isVertical() ? 0 : 1, getFirst());
            final WorldObject second = new WorldObject(WILTING_WHEAT, 22, isVertical() ? 0 : 1, getSecond());
            World.sendSoundEffect(first, new SoundEffect(3730, 10));
            World.spawnObject(first);
            World.spawnObject(second);
            World.spawnObject(new WorldObject(perpendicular ? REGULAR_WHEAT_PERP : REGULAR_WHEAT, 22, perpendicular ? 0 : isVertical() ? 0 : 3, getFirst().transform(isVertical() ? Direction.NORTH : Direction.WEST, 1)));
            World.spawnObject(new WorldObject(perpendicular ? REGULAR_WHEAT_PERP : REGULAR_WHEAT, 22, perpendicular ? 2 : isVertical() ? 2 : 1, getSecond().transform(isVertical() ? Direction.SOUTH : Direction.EAST, 1)));
            WorldTasksManager.schedule(() -> {
                World.sendObjectAnimation(first, wiltingWheatAnimation);
                World.sendObjectAnimation(second, wiltingWheatAnimation);
            }, 1);
            WorldTasksManager.schedule(() -> {
                World.removeObject(first);
                World.removeObject(second);
                World.spawnObject(new WorldObject(25000, 22, perpendicular ? 2 : isVertical() ? 2 : 1, getFirst()));
                World.spawnObject(new WorldObject(25000, 22, perpendicular ? 2 : isVertical() ? 2 : 1, getSecond()));
                spawnCooldown = false;
            }, (int) TimeUnit.MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(wiltingWheatAnimation)) + 1);
        }

        private void moveEntitiesOffWheat() {
            CharacterLoop.forEach(first, 0, Entity.class, e -> {
                final Location newLocation = e.getLocation().transform(isVertical() ? Direction.EAST : Direction.NORTH, 1);
                e.resetWalkSteps();
                e.addWalkSteps(newLocation.getX(), newLocation.getY());
            });
            CharacterLoop.forEach(second, 0, Entity.class, e -> {
                final Location newLocation = e.getLocation().transform(isVertical() ? Direction.EAST : Direction.NORTH, 1);
                e.resetWalkSteps();
                e.addWalkSteps(newLocation.getX(), newLocation.getY());
            });
        }

        public MagicalWheatSpawn(Location first, Location second, boolean perpendicular) {
            this.first = first;
            this.second = second;
            this.perpendicular = perpendicular;
        }

        public Location getFirst() {
            return first;
        }

        public Location getSecond() {
            return second;
        }

        public boolean isPerpendicular() {
            return perpendicular;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public boolean isSpawnCooldown() {
            return spawnCooldown;
        }

        public void setSpawnCooldown(boolean spawnCooldown) {
            this.spawnCooldown = spawnCooldown;
        }

        @Override
        public String toString() {
            return "PuroPuroArea.MagicalWheatSpawn(first=" + this.getFirst() + ", second=" + this.getSecond() + ", perpendicular=" + this.isPerpendicular() + ", visible=" + this.isVisible() + ", spawnCooldown=" + this.isSpawnCooldown() + ")";
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof PuroPuroArea.MagicalWheatSpawn)) return false;
            final PuroPuroArea.MagicalWheatSpawn other = (PuroPuroArea.MagicalWheatSpawn) o;
            if (!other.canEqual(this)) return false;
            if (this.isPerpendicular() != other.isPerpendicular()) return false;
            if (this.isVisible() != other.isVisible()) return false;
            if (this.isSpawnCooldown() != other.isSpawnCooldown()) return false;
            final Object this$first = this.getFirst();
            final Object other$first = other.getFirst();
            if (this$first == null ? other$first != null : !this$first.equals(other$first)) return false;
            final Object this$second = this.getSecond();
            final Object other$second = other.getSecond();
            return this$second == null ? other$second == null : this$second.equals(other$second);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof PuroPuroArea.MagicalWheatSpawn;
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.isPerpendicular() ? 79 : 97);
            result = result * PRIME + (this.isVisible() ? 79 : 97);
            result = result * PRIME + (this.isSpawnCooldown() ? 79 : 97);
            final Object $first = this.getFirst();
            result = result * PRIME + ($first == null ? 43 : $first.hashCode());
            final Object $second = this.getSecond();
            result = result * PRIME + ($second == null ? 43 : $second.hashCode());
            return result;
        }
    }
}

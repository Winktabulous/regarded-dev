package com.zenyte.game.world.region.area.apeatoll;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.Region;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.plugins.events.FloorItemSpawnEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.IntConsumer;

/**
 * @author Kris | 21/03/2019 13:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ApeAtollDungeonArea extends ApeAtollArea implements CycleProcessPlugin {

    private static final Animation trapAnimation = new Animation(1420);

    private static final SoundEffect trapSound = new SoundEffect(1383);

    private static final SoundEffect stalagmiteTrapSound = new SoundEffect(1384);

    private static final ForceTalk forceTalk = new ForceTalk("Ouch!");

    private static final SoundEffect earthquakeSound = new SoundEffect(1678);

    private static final Graphics earthquakeGraphics = new Graphics(302);

    private static final Animation stalagmiteTrapRise = new Animation(462);

    private static final Animation stalagmiteTrapFall = new Animation(463);

    private static final RSPolygon[] safePolygons = new RSPolygon[] { new RSPolygon(new int[][] { { 2766, 9090 }, { 2766, 9091 }, { 2767, 9092 }, { 2767, 9095 }, { 2768, 9096 }, { 2768, 9097 }, { 2769, 9098 }, { 2769, 9101 }, { 2770, 9102 }, { 2768, 9104 }, { 2768, 9107 }, { 2771, 9110 }, { 2771, 9111 }, { 2773, 9113 }, { 2773, 9114 }, { 2772, 9114 }, { 2771, 9115 }, { 2768, 9115 }, { 2767, 9116 }, { 2764, 9116 }, { 2761, 9119 }, { 2760, 9119 }, { 2759, 9120 }, { 2758, 9120 }, { 2756, 9122 }, { 2754, 9122 }, { 2754, 9121 }, { 2755, 9120 }, { 2755, 9119 }, { 2754, 9118 }, { 2754, 9115 }, { 2755, 9114 }, { 2755, 9112 }, { 2757, 9111 }, { 2757, 9109 }, { 2756, 9108 }, { 2756, 9107 }, { 2755, 9106 }, { 2755, 9105 }, { 2754, 9104 }, { 2754, 9101 }, { 2756, 9099 }, { 2756, 9098 }, { 2754, 9095 }, { 2754, 9093 }, { 2755, 9092 }, { 2755, 9091 }, { 2746, 9092 }, { 2750, 9096 }, { 2750, 9097 }, { 2751, 9098 }, { 2751, 9099 }, { 2749, 9101 }, { 2749, 9102 }, { 2750, 9103 }, { 2750, 9105 }, { 2751, 9106 }, { 2750, 9107 }, { 2750, 9109 }, { 2748, 9111 }, { 2749, 9112 }, { 2746, 9115 }, { 2746, 9116 }, { 2748, 9118 }, { 2748, 9124 }, { 2750, 9126 }, { 2749, 9127 }, { 2750, 9128 }, { 2750, 9129 }, { 2751, 9130 }, { 2751, 9131 }, { 2750, 9132 }, { 2750, 9133 }, { 2751, 9134 }, { 2751, 9138 }, { 2750, 9139 }, { 2750, 9140 }, { 2751, 9141 }, { 2751, 9147 }, { 2749, 9149 }, { 2756, 9149 }, { 2755, 9148 }, { 2755, 9147 }, { 2754, 9146 }, { 2754, 9144 }, { 2755, 9143 }, { 2755, 9142 }, { 2754, 9141 }, { 2754, 9139 }, { 2755, 9138 }, { 2755, 9137 }, { 2754, 9136 }, { 2754, 9135 }, { 2755, 9134 }, { 2755, 9133 }, { 2754, 9132 }, { 2754, 9131 }, { 2755, 9130 }, { 2755, 9129 }, { 2756, 9128 }, { 2756, 9127 }, { 2757, 9127 }, { 2759, 9129 }, { 2759, 9130 }, { 2760, 9131 }, { 2761, 9131 }, { 2761, 9133 }, { 2764, 9136 }, { 2766, 9136 }, { 2769, 9139 }, { 2769, 9140 }, { 2770, 9141 }, { 2770, 9142 }, { 2771, 9143 }, { 2772, 9143 }, { 2773, 9144 }, { 2774, 9144 }, { 2775, 9145 }, { 2775, 9146 }, { 2777, 9148 }, { 2778, 9148 }, { 2780, 9150 }, { 2783, 9150 }, { 2782, 9149 }, { 2782, 9148 }, { 2780, 9146 }, { 2780, 9143 }, { 2779, 9142 }, { 2775, 9142 }, { 2774, 9141 }, { 2774, 9140 }, { 2773, 9139 }, { 2771, 9139 }, { 2771, 9137 }, { 2768, 9134 }, { 2767, 9134 }, { 2766, 9133 }, { 2766, 9132 }, { 2764, 9130 }, { 2763, 9130 }, { 2762, 9129 }, { 2762, 9128 }, { 2761, 9127 }, { 2760, 9127 }, { 2759, 9126 }, { 2759, 9125 }, { 2758, 9124 }, { 2758, 9123 }, { 2759, 9123 }, { 2760, 9122 }, { 2762, 9122 }, { 2763, 9121 }, { 2764, 9121 }, { 2765, 9120 }, { 2767, 9120 }, { 2768, 9119 }, { 2769, 9119 }, { 2770, 9118 }, { 2772, 9118 }, { 2773, 9117 }, { 2774, 9117 }, { 2776, 9115 }, { 2776, 9113 }, { 2775, 9112 }, { 2775, 9109 }, { 2774, 9108 }, { 2774, 9105 }, { 2775, 9104 }, { 2775, 9101 }, { 2773, 9099 }, { 2773, 9097 }, { 2772, 9096 }, { 2772, 9093 }, { 2771, 9092 }, { 2771, 9090 }, { 2772, 9090 }, { 2773, 9089 }, { 2777, 9089 }, { 2779, 9091 }, { 2779, 9092 }, { 2781, 9094 }, { 2781, 9095 }, { 2780, 9096 }, { 2780, 9099 }, { 2781, 9100 }, { 2781, 9104 }, { 2782, 9105 }, { 2782, 9106 }, { 2785, 9107 }, { 2786, 9108 }, { 2786, 9109 }, { 2787, 9110 }, { 2788, 9110 }, { 2789, 9111 }, { 2789, 9112 }, { 2790, 9113 }, { 2791, 9113 }, { 2792, 9114 }, { 2791, 9115 }, { 2791, 9118 }, { 2792, 9119 }, { 2792, 9121 }, { 2793, 9122 }, { 2793, 9124 }, { 2792, 9125 }, { 2792, 9127 }, { 2791, 9128 }, { 2791, 9133 }, { 2793, 9135 }, { 2793, 9136 }, { 2792, 9137 }, { 2792, 9138 }, { 2791, 9139 }, { 2791, 9141 }, { 2792, 9142 }, { 2792, 9144 }, { 2793, 9145 }, { 2793, 9148 }, { 2794, 9149 }, { 2794, 9150 }, { 2798, 9151 }, { 2798, 9150 }, { 2797, 9149 }, { 2797, 9143 }, { 2796, 9142 }, { 2796, 9140 }, { 2797, 9139 }, { 2797, 9137 }, { 2796, 9136 }, { 2796, 9134 }, { 2795, 9133 }, { 2795, 9129 }, { 2796, 9128 }, { 2796, 9125 }, { 2797, 9124 }, { 2797, 9122 }, { 2796, 9121 }, { 2796, 9119 }, { 2795, 9118 }, { 2795, 9116 }, { 2796, 9115 }, { 2797, 9115 }, { 2801, 9114 }, { 2802, 9113 }, { 2807, 9113 }, { 2808, 9114 }, { 2809, 9114 }, { 2810, 9113 }, { 2811, 9113 }, { 2812, 9112 }, { 2813, 9112 }, { 2814, 9113 }, { 2814, 9111 }, { 2813, 9110 }, { 2813, 9109 }, { 2810, 9109 }, { 2809, 9110 }, { 2807, 9110 }, { 2806, 9109 }, { 2804, 9109 }, { 2803, 9110 }, { 2800, 9110 }, { 2799, 9111 }, { 2795, 9111 }, { 2794, 9112 }, { 2790, 9108 }, { 2789, 9108 }, { 2786, 9105 }, { 2785, 9105 }, { 2784, 9104 }, { 2784, 9102 }, { 2783, 9101 }, { 2783, 9099 }, { 2784, 9098 }, { 2784, 9097 }, { 2783, 9096 }, { 2783, 9095 }, { 2784, 9094 }, { 2784, 9092 }, { 2782, 9090 }, { 2782, 9089 }, { 2791, 9089 }, { 2792, 9090 }, { 2793, 9090 }, { 2794, 9091 }, { 2795, 9091 }, { 2795, 9092 }, { 2796, 9093 }, { 2796, 9095 }, { 2799, 9098 }, { 2802, 9098 }, { 2804, 9100 }, { 2804, 9101 }, { 2805, 9102 }, { 2806, 9102 }, { 2807, 9101 }, { 2807, 9100 }, { 2806, 9099 }, { 2806, 9098 }, { 2805, 9097 }, { 2804, 9097 }, { 2803, 9096 }, { 2801, 9096 }, { 2800, 9095 }, { 2799, 9095 }, { 2798, 9094 }, { 2798, 9091 }, { 2791, 9089 } }, 0), new RSPolygon(new int[][] { { 2691, 9148 }, { 2693, 9148 }, { 2694, 9147 }, { 2694, 9146 }, { 2695, 9145 }, { 2696, 9145 }, { 2697, 9144 }, { 2697, 9143 }, { 2698, 9142 }, { 2699, 9142 }, { 2701, 9140 }, { 2702, 9140 }, { 2703, 9141 }, { 2703, 9142 }, { 2702, 9143 }, { 2702, 9145 }, { 2701, 9146 }, { 2700, 9146 }, { 2699, 9147 }, { 2699, 9149 }, { 2700, 9150 }, { 2737, 9150 }, { 2738, 9149 }, { 2739, 9149 }, { 2740, 9148 }, { 2741, 9148 }, { 2742, 9147 }, { 2742, 9146 }, { 2740, 9144 }, { 2739, 9144 }, { 2739, 9142 }, { 2738, 9141 }, { 2738, 9139 }, { 2739, 9138 }, { 2739, 9136 }, { 2738, 9135 }, { 2738, 9134 }, { 2739, 9133 }, { 2739, 9132 }, { 2740, 9131 }, { 2741, 9131 }, { 2742, 9132 }, { 2742, 9135 }, { 2743, 9136 }, { 2743, 9138 }, { 2742, 9139 }, { 2742, 9140 }, { 2743, 9141 }, { 2743, 9142 }, { 2744, 9143 }, { 2744, 9144 }, { 2746, 9146 }, { 2746, 9148 }, { 2747, 9149 }, { 2747, 9153 }, { 2691, 9152 } }, 0), new RSPolygon(new int[][] { { 2687, 9119 }, { 2690, 9119 }, { 2691, 9118 }, { 2693, 9118 }, { 2694, 9117 }, { 2696, 9117 }, { 2697, 9118 }, { 2699, 9118 }, { 2700, 9117 }, { 2702, 9117 }, { 2703, 9118 }, { 2704, 9118 }, { 2705, 9117 }, { 2706, 9117 }, { 2707, 9116 }, { 2708, 9116 }, { 2709, 9117 }, { 2709, 9118 }, { 2710, 9119 }, { 2711, 9119 }, { 2712, 9120 }, { 2713, 9123 }, { 2713, 9124 }, { 2714, 9125 }, { 2714, 9128 }, { 2715, 9129 }, { 2715, 9130 }, { 2716, 9131 }, { 2718, 9131 }, { 2719, 9132 }, { 2719, 9135 }, { 2720, 9136 }, { 2720, 9137 }, { 2721, 9138 }, { 2722, 9138 }, { 2723, 9137 }, { 2723, 9134 }, { 2722, 9133 }, { 2722, 9130 }, { 2721, 9129 }, { 2721, 9128 }, { 2720, 9127 }, { 2719, 9127 }, { 2718, 9126 }, { 2718, 9123 }, { 2717, 9122 }, { 2716, 9122 }, { 2715, 9121 }, { 2715, 9118 }, { 2714, 9117 }, { 2713, 9117 }, { 2712, 9116 }, { 2712, 9115 }, { 2711, 9114 }, { 2710, 9114 }, { 2709, 9113 }, { 2708, 9113 }, { 2707, 9112 }, { 2706, 9112 }, { 2705, 9113 }, { 2704, 9113 }, { 2703, 9114 }, { 2701, 9114 }, { 2700, 9113 }, { 2698, 9113 }, { 2697, 9114 }, { 2694, 9114 }, { 2693, 9113 }, { 2692, 9113 }, { 2690, 9111 }, { 2687, 9111 } }, 0), new RSPolygon(new int[][] { { 2713, 9087 }, { 2713, 9090 }, { 2714, 9091 }, { 2715, 9091 }, { 2716, 9092 }, { 2717, 9092 }, { 2718, 9093 }, { 2718, 9095 }, { 2719, 9096 }, { 2720, 9096 }, { 2721, 9097 }, { 2722, 9097 }, { 2723, 9098 }, { 2724, 9098 }, { 2725, 9099 }, { 2728, 9099 }, { 2729, 9100 }, { 2730, 9100 }, { 2730, 9101 }, { 2731, 9102 }, { 2732, 9102 }, { 2733, 9103 }, { 2733, 9105 }, { 2732, 9106 }, { 2732, 9108 }, { 2731, 9108 }, { 2729, 9110 }, { 2729, 9111 }, { 2730, 9112 }, { 2730, 9114 }, { 2731, 9115 }, { 2731, 9117 }, { 2732, 9118 }, { 2733, 9118 }, { 2734, 9119 }, { 2734, 9120 }, { 2733, 9121 }, { 2733, 9122 }, { 2732, 9123 }, { 2732, 9125 }, { 2733, 9126 }, { 2733, 9128 }, { 2734, 9129 }, { 2735, 9129 }, { 2736, 9128 }, { 2736, 9125 }, { 2735, 9124 }, { 2735, 9123 }, { 2736, 9122 }, { 2736, 9121 }, { 2737, 9120 }, { 2737, 9117 }, { 2736, 9116 }, { 2735, 9116 }, { 2734, 9115 }, { 2734, 9114 }, { 2733, 9113 }, { 2733, 9111 }, { 2734, 9110 }, { 2734, 9108 }, { 2735, 9107 }, { 2735, 9105 }, { 2736, 9104 }, { 2736, 9102 }, { 2735, 9101 }, { 2735, 9100 }, { 2734, 9099 }, { 2733, 9099 }, { 2732, 9098 }, { 2731, 9098 }, { 2730, 9097 }, { 2728, 9097 }, { 2727, 9096 }, { 2725, 9096 }, { 2724, 9095 }, { 2723, 9095 }, { 2722, 9094 }, { 2724, 9092 }, { 2724, 9087 } }, 0) };

    private final Int2ObjectMap<WorldObject> traps = new Int2ObjectOpenHashMap<>();

    private final int firstStalagmiteTrap = new Location(2693, 9109, 0).getPositionHash();

    private final Location firstTrapObjectLocation = new Location(2692, 9109, 0);

    private final int secondStalagmiteTrap = new Location(2743, 9146, 0).getPositionHash();

    private final Location secondTrapObjectLocation = new Location(2743, 9145, 0);

    private int delayUntilEarthquake;

    public ApeAtollDungeonArea() {
        mapSpikes();
        resetEarthquakeTimer();
    }

    @Subscribe
    public static void onFloorItemSpawn(final FloorItemSpawnEvent event) {
        if (event.getItem().getId() == 3187) {
            final FloorItem bones = event.getItem();
            if (!GlobalAreaManager.get("Ape Atoll Dungeon").inside(bones.getLocation())) {
                return;
            }
            WorldTasksManager.schedule(() -> {
                // Destroy up to 10 bones at a time; keeping it at 10 in case something goes wrong/deadlocks w/ while.
                for (int i = 0; i < 10; i++) {
                    if (!bones.isPresent()) {
                        break;
                    }
                    World.destroyFloorItem(bones);
                }
                final Location tile = bones.getLocation();
                final List<NPC> list = CharacterLoop.find(tile, 15, NPC.class, n -> !n.isDead() && n.getId() == 5237);
                if (list.size() >= 25) {
                    return;
                }
                new Skeleton(tile).spawn();
            }, 10);
        }
    }

    private void mapSpikes() {
        final IntArrayList regions = new IntArrayList(new int[] { 10894, 11150 });
        regions.forEach((IntConsumer) id -> {
            final Region region = World.getRegion(id, true);
            final ObjectCollection<WorldObject> objects = region.getObjects().values();
            for (final WorldObject object : objects) {
                if (object.getId() == ObjectId.FLOOR_SPIKES_4886) {
                    traps.put(object.getPositionHash(), object);
                }
            }
        });
    }

    private void resetEarthquakeTimer() {
        delayUntilEarthquake = Utils.random(20, 30);
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2686, 9153 }, { 2686, 9085 }, { 2816, 9085 }, { 2816, 9216 }, { 2752, 9216 }, { 2752, 9153 } }, 0) };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.APE_ATOLL_DUNGEON);
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        super.leave(player, logout);
        player.getPacketDispatcher().resetCamera();
        final Inventory inventory = player.getInventory();
        int count = 0;
        for (final Int2ObjectMap.Entry<Item> entry : inventory.getContainer().getItems().int2ObjectEntrySet()) {
            final Item item = entry.getValue();
            if (item != null && item.getId() == 3187) {
                inventory.set(entry.getIntKey(), new Item(592));
                count++;
            }
        }
        if (count > 0) {
            inventory.refresh();
            player.sendMessage("The skeleton bones you are carrying crumble to dust.");
        }
    }

    @Override
    public void process() {
        if (players.isEmpty()) {
            return;
        }
        for (final Player player : players) {
            final int hash = player.getLocation().getPositionHash();
            final WorldObject trap = traps.get(hash);
            if (trap != null) {
                sendSpikes(player, trap);
            } else if (hash == firstStalagmiteTrap || hash == secondStalagmiteTrap) {
                final WorldObject object = World.getObjectWithType(hash == firstStalagmiteTrap ? firstTrapObjectLocation : secondTrapObjectLocation, 10);
                if (object != null) {
                    if (!object.isLocked()) {
                        World.sendObjectAnimation(object, stalagmiteTrapRise);
                        player.sendSound(stalagmiteTrapSound);
                        player.applyHit(new Hit(Utils.random(5, 7), HitType.REGULAR));
                        object.setLocked(true);
                        WorldTasksManager.schedule(() -> World.sendObjectAnimation(object, stalagmiteTrapFall), 1);
                        WorldTasksManager.schedule(() -> object.setLocked(false), 2);
                    }
                }
            }
        }
        if (--delayUntilEarthquake <= 0) {
            resetEarthquakeTimer();
            for (final Player player : players) {
                player.sendSound(earthquakeSound);
                player.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 5, 0, 0);
                WorldTasksManager.schedule(() -> {
                    player.getPacketDispatcher().resetCamera();
                    if (isSafe(player)) {
                        return;
                    }
                    World.sendGraphics(earthquakeGraphics, player.getLocation());
                    player.applyHit(new Hit(Utils.random(3, 4), HitType.REGULAR));
                }, 3);
            }
        }
    }

    private void sendSpikes(final Player player, final WorldObject object) {
        World.sendObjectAnimation(object, trapAnimation);
        player.sendSound(trapSound);
        player.setForceTalk(forceTalk);
        player.applyHit(new Hit(4, HitType.REGULAR));
        player.getToxins().applyToxin(Toxins.ToxinType.POISON, 4);
    }

    private boolean isSafe(@NotNull final Player player) {
        final Location tile = player.getLocation();
        RSPolygon polygon;
        for (int i = safePolygons.length - 1; i >= 0; i--) {
            polygon = safePolygons[i];
            if (polygon.contains(tile)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String name() {
        return "Ape Atoll Dungeon";
    }
}

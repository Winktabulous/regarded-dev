package com.zenyte.game.content.skills.farming.hespori;

import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.utils.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

import static com.zenyte.game.world.entity.player.Player.DEATH_ANIMATION;

/**
 * @author Kris | 24/02/2019 13:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class HesporiInstance extends DynamicArea implements FullMovementPlugin, DeathPlugin, LootBroadcastPlugin {
    private static final Logger log = LoggerFactory.getLogger(HesporiInstance.class);
    /**
     * The chunk coordinates for the Hespori instance.
     */
    private static final int CHUNK_X = 154;
    private static final int CHUNK_Y = 1259;
    /**
     * The width and height of the Hespori instance.
     */
    private static final int SIZE = 4;
    /**
     * The varbit stage of the Hespori patch which dictates the grown stage of Hespori.
     */
    private static final int HESPORI_PATCH_GROWN = 8;
    /**
     * The coordinates to the entrance of the Hespori instance.
     */
    private static final Location entrance = new Location(1243, 10081, 0);
    /**
     * The coordinates to the location of the farming patch for the Hespori.
     */
    private static final Location patchTile = new Location(1246, 10086, 0);
    /**
     * The sound effect played when the Hespori spawns.
     */
    private static final SoundEffect spawnSound = new SoundEffect(4059);
    /**
     * The animation played through Hespori when it spawns.
     */
    private static final Animation spawnAnimation = new Animation(8221);
    /**
     * The name of the fight instance for Hespori.
     */
    static final String name = "Hespori - Fight Arena";

    private long flowerSpawnMillis = 0;

    /**
     * Launches the Hespori instance - constructing the instance and teleporting player inside it.
     *
     * @param player the player launching the fight.
     */
    public static final void start(final Player player) {
        try {
            new HesporiInstance(player, MapBuilder.findEmptyChunk(SIZE, SIZE)).constructRegion();
        } catch (OutOfSpaceException e) {
            log.error("", e);
        }
    }

    /**
     * The constructor for the Hespori Instance.
     *
     * @param owner         the player owning the instance.
     * @param allocatedArea the allocated area for the dynamic map.
     */
    private HesporiInstance(final Player owner, final AllocatedArea allocatedArea) {
        super(allocatedArea, CHUNK_X, CHUNK_Y);
        this.owner = owner;
    }

    /**
     * The owner of the Hespori Instance.
     */
    final Player owner;
    /**
     * An array of four flowers - the flower buds surrounding the Hespori instance. If any of the flowers are
     * blooming at any given time, the Hespori will be immune to all kinds of damage-
     */
    private final Flower[] flowers = new Flower[4];
    /**
     * The patch object that is erased at the build of the instance, and spawned back once the Hespori dies.
     */
    private WorldObject patch;

    /**
     * Checks to see if any of the flowers are still blooming - and thus, protecting the Hespori.
     *
     * @return whether or not the Hespori is immune to damage.
     */
    boolean isImmune() {
        for (int i = flowers.length - 1; i >= 0; i--) {
            if (flowers[i].isBlooming()) {
                return true;
            }
        }
        return false;
    }

    void onFlowerDeath(final Player killer) {
        if (killer != null && !isImmune() && System.currentTimeMillis() - flowerSpawnMillis < TimeUnit.MICROSECONDS.toTicks(10)) {
            killer.getCombatAchievements().complete(CAType.WEED_WHACKER);
        }
    }

    /**
     * Sets the Hespori flower buds to blooming state if they're not yet blooming. Resets their health and attributes
     * alongside it.
     */
    void setBlooming() {
        if (isImmune()) {
            return;
        }
        for (int i = flowers.length - 1; i >= 0; i--) {
            final Flower flower = flowers[i];
            flower.reset();
            flower.bloom();
            flowerSpawnMillis = System.currentTimeMillis();
        }
    }

    /**
     * Spawns the Hespori farming patch object back into the instance upon Hespori's death, updates its state and
     * finishes tracking the boss timer for the Hespori.
     */
    void finish() {
        World.spawnObject(patch);
        final FarmingSpot spot = owner.getFarming().create(patch);
        spot.setValue(HESPORI_PATCH_GROWN);
        spot.refresh();
        long diff = System.currentTimeMillis() - owner.getBossTimer().getCurrentTracker();
        owner.getBossTimer().inform("Hespori", diff);
        if (TimeUnit.MILLISECONDS.toSeconds(diff) <= 48) {
            owner.getCombatAchievements().complete(CAType.HESPORI_SPEED_TRIALIST);
        }
        if (TimeUnit.MILLISECONDS.toSeconds(diff) <= 42) {//Was 36, but we make it 42
            owner.getCombatAchievements().complete(CAType.HESPORI_SPEED_CHASER);
        }
    }

    /**
     * Teleports the player into the Hespori instance once the instance has finished constructing. Unlocks the
     * player, deletes the underlying farming patfch, spawns the Hespori and its flower buds - and sets them blooming.
     */
    @Override
    public void constructed() {
        owner.setLocation(this.getLocation(owner.inArea("Hespori") ? owner.getLocation() : entrance));
        owner.unlock();
        patch = Objects.requireNonNull(World.getRegion(getLocation(patchTile).getRegionId(), true).getObjectWithType(getLocation(patchTile), 10));
        World.removeObject(patch);
        owner.sendSound(spawnSound);
        owner.sendMessage("The Hespori doesn't take kindly to your attempt to harvest it.");
        owner.getBossTimer().startTracking("Hespori");
        for (int i = 0; i < 4; i++) {
            final Flower.FlowerPosition position = Flower.FlowerPosition.values[i];
            final Flower flower = new Flower(this, position);
            flower.spawn();
            flower.bloom();
            flowers[i] = flower;
            flowerSpawnMillis = System.currentTimeMillis();
        }
        Hespori hespori = new Hespori(this);
        hespori.spawn();
        hespori.setFaceEntity(owner);
        hespori.setAnimation(spawnAnimation);
        hespori.lock(2);
    }

    /**
     * Executed when a player enters the instance - we remove the entanglement attributes to prevent potential
     * problems with the upcoming entanglement attack.
     *
     * @param player the player entering.
     */
    @Override
    public void enter(Player player) {
        final Map<Object, Object> attr = player.getTemporaryAttributes();
        attr.remove(Hespori.entanglementAttrKey);
        attr.remove(Hespori.entanglementVerificationAttrKey);
    }

    /**
     * Executed when a player leaves the instance.
     *
     * @param player the player leaving the instance.
     * @param logout whether or not the player left through logging out.
     */
    @Override
    public void leave(Player player, boolean logout) {
    }

    /**
     * The name of the Hespori instance.
     *
     * @return the name of the instance.
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * Defines the location that the player spawns at when logging into the game after logging out whilst being
     * inside a Hespori instance.
     *
     * @return the location where the player logs into if they were inside the instance when they logged out.
     */
    @Override
    public Location onLoginLocation() {
        return entrance;
    }

    /**
     * Processes the movement of the player. Overriding to allow entanglement of the player when the Hespori lands
     * the respective attack on the player. Once the Hespori Entangles the player, they will be unable to walk for a
     * total of 5 movement attempts - breaking the vines and freeing them on the 6th. If the player doesn't free
     * themselves in time, they get punished by a deadly attack.
     *
     * @param player the player whose movement is being processed.
     * @param x      the x coordinate the player is walking to.
     * @param y      the y coordinate the player is walking to.
     * @return whether or not the player should move to the requested destination.
     */
    @Override
    public boolean processMovement(Player player, int x, int y) {
        final Object entanglement = player.getTemporaryAttributes().remove(Hespori.entanglementAttrKey);
        if (entanglement instanceof Integer) {
            final int count = (int) entanglement;
            player.resetWalkSteps();
            player.cancelCombat();
            if (count > 0) {
                player.sendMessage("You feel the vines loosen slightly as you try to move.");
                player.getTemporaryAttributes().put(Hespori.entanglementAttrKey, count - 1);
            } else if (count == 0) {
                player.sendMessage(Colour.RS_GREEN + "You manage to break free of the vines!");
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return null;
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        player.setAnimation(Animation.STOP);
        player.lock();
        player.stopAll();
        if (player.getPrayerManager().isActive(Prayer.RETRIBUTION)) {
            player.getPrayerManager().applyRetributionEffect(source);
        }
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                if (player.isFinished() || player.isNulled()) {
                    stop();
                    return;
                }
                if (ticks == 0) {
                    player.setAnimation(DEATH_ANIMATION);
                } else if (ticks == 2) {
                    player.getDeathMechanics().service(ItemRetrievalService.RetrievalServiceType.ARNO, source, true);
                    player.sendMessage("Oh dear, you have died.");
                    player.reset();
                    player.setAnimation(Animation.STOP);
                    player.sendMessage("Arno has retrieved some of your items. You can collect them from him in the Farming Guild.");
                    ItemRetrievalService.updateVarps(player);
                    if (player.getVariables().isSkulled()) {
                        player.getVariables().setSkull(false);
                    }
                    player.blockIncomingHits();
                    player.setLocation(player.getRespawnPoint().getLocation());
                } else if (ticks == 3) {
                    player.unlock();
                    player.getAppearance().resetRenderAnimation();
                    player.setAnimation(Animation.STOP);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);
        return true;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}

package com.zenyte.game.content.godwars.instance;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.GodwarsInstanceManager;
import com.zenyte.game.content.godwars.npcs.GodwarsBossMinion;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.godwars.GodwarsDungeonArea;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfBoundaryException;
import com.zenyte.plugins.events.ClanLeaveEvent;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.zenyte.game.world.entity.player.Player.DEATH_ANIMATION;

/**
 * @author Kris | 14/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class GodwarsInstance extends DynamicArea implements FullMovementPlugin, TeleportMovementPlugin, DeathPlugin, DropPlugin, LogoutRestrictionPlugin, CannonRestrictionPlugin, LootBroadcastPlugin {

    public static final String CA_TASK_INSTANCE_KC_ATT = "gwd_boss_chamber_kc";
    public static final String CA_TASK_INSTANCE_ENTERED_ATT = "gwd_boss_chamber_entered";
    private static final Logger log = LoggerFactory.getLogger(GodwarsInstance.class);
    static final int STATUS_VAR = 3625;

    protected final String clan;
    final GodType god;
    public RSPolygon polygon;
    GodwarsBossMinion[] minions;
    public final List<NPC> killcountNPCS = new ObjectArrayList<>();

    protected abstract IntList possibleKillcountMonsters();

    protected abstract List<Location> possibleSpawnTiles();

    protected GodwarsInstance(final String clan, final GodType god, AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY) {
        super(allocatedArea, copiedChunkX, copiedChunkY);
        this.god = god;
        this.clan = clan;
    }

    @Subscribe
    public static void onClanLeave(final ClanLeaveEvent event) {
        final Player player = event.getPlayer();
        if (!(player.getArea() instanceof GodwarsInstance)) {
            return;
        }
        player.sendMessage(Colour.RED.wrap("As you are no longer part of the clan, you will be removed from the instance within the next 3-10 seconds."));
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (++ticks >= 100 || player.isNulled() || player.isFinished()) {
                    stop();
                    return;
                }
                if (player.isDead() || player.isLocked()) {
                    return;
                }
                final RegionArea area = player.getArea();
                if (!(area instanceof GodwarsInstance)) {
                    stop();
                    return;
                }
                player.lock(1);
                player.stopAll();
                player.sendMessage(Colour.RED.wrap("You have been removed from the instance."));
                player.blockIncomingHits();
                player.setLocation(new Location(((GodwarsInstance) area).onLoginLocation()));
                stop();
            }
        }, Utils.random(5, 15), 0);
    }

    public abstract Location onLoginLocation();

    protected abstract void destroyed();

    @Override
    public void constructed() {
        final List<Location> possibleSpawnTiles = possibleSpawnTiles();
        final IntList possibleKillcountMonsters = possibleKillcountMonsters();
        final ObjectArrayList<Location> tiles = new ObjectArrayList<Location>(possibleSpawnTiles.size());
        for (final Location tile : possibleSpawnTiles) {
            tiles.add(getLocation(tile));
        }
        for (final Integer id : possibleKillcountMonsters) {
            final NPC npc = World.spawnNPC(id, tiles.remove(Utils.random(tiles.size() - 1)), Direction.SOUTH, 5);
            if (npc == null) {
                continue;
            }
            killcountNPCS.add(npc);
        }
    }

    @Override
    public final void constructRegion() {
        if (isConstructed()) {
            return;
        }
        setConstructed(true);
        //Build the instance, if this fails, an exception is thrown and the area gets destroyed, and this method stops executing.
        build();
        GodwarsInstanceManager.getManager().addInstance(clan, this);
        GlobalAreaManager.add(this);
        constructed();
    }

    @Override
    public final void destroyRegion() {
        if (isDestroyed() || !players.isEmpty()) {
            return;
        }
        setDestroyed(true);
        try {
            GodwarsInstanceManager.getManager().removeInstance(clan, this);
            destroyed();
            GlobalAreaManager.remove(this);
        } catch (Exception e) {
            log.error("", e);
        }
        destroy();
    }

    /**
     * Build the map. If this process fails, destroy whatever remnants are actually built, and throw another exception
     * to interrupt the flow of the code which would register the area officially.
     */
    protected void build() {
        try {
            MapBuilder.copyAllPlanesMap(area, staticChunkX, staticChunkY, chunkX, chunkY, sizeX, sizeY);
        } catch (OutOfBoundaryException e) {
            destroyRegion();
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void enter(final Player player) {
        player.setViewDistance(Player.SCENE_DIAMETER);
        GodwarsDungeonArea.enterArea(player);
        updateStatus(player);
        player.getAttributes().put(CA_TASK_INSTANCE_ENTERED_ATT, Boolean.TRUE);
    }

    protected void updateStatus(@NotNull final Player player) {
        player.getVarManager().sendVar(STATUS_VAR, 1);
    }

    @Override
    public void leave(final Player player, boolean logout) {
        player.resetViewDistance();
        GodwarsDungeonArea.leaveArea(player);
        player.getVarManager().sendVar(STATUS_VAR, 0);
        player.getAttributes().remove(CA_TASK_INSTANCE_KC_ATT);
        player.getAttributes().remove(CA_TASK_INSTANCE_ENTERED_ATT);
    }

    /**
     * Attempt to destroy the area, log the exceptions if any occur.
     */
    private final void destroy() {
        try {
            MapBuilder.destroy(area);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public RSPolygon chamberPolygon() {
        return polygon;
    }

    @Override
    public boolean processMovement(final Player player, final int x, final int y) {
        final RSPolygon polygon = chamberPolygon();
        if (!polygon.contains(player.getX(), player.getY())) {
            if (polygon.contains(x, y)) {
                GodwarsDungeonArea.enterArea(player);
            }
        } else {
            if (!polygon.contains(x, y)) {
                GodwarsDungeonArea.leaveArea(player);
            }
        }
        return true;
    }

    @Override
    public void processMovement(final Player player, final Location destination) {
        this.processMovement(player, destination.getX(), destination.getY());
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
                    player.getDeathMechanics().service(ItemRetrievalService.RetrievalServiceType.GODWARS, source, true);
                    player.sendMessage("Oh dear, you have died.");
                    player.reset();
                    player.setAnimation(Animation.STOP);
                    player.sendMessage("The dying knight has retrieved some of your items. You can collect them from him in any of the godwars lobby areas.");
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
    public boolean manualLogout(final Player player) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Are you sure you wish to log out?<br>If there are no players left, " + Colour.RED.wrap("the instance will be destroyed") + ".", new DialogueOption("Yes", () -> player.logout(false)), new DialogueOption("No."));
            }
        });
        return false;
    }

    public GodType getGod() {
        return god;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}

package com.zenyte.game.content.wildernessVault;

import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.wildernessVault.queenreaver.QueenReaver;
import com.zenyte.game.content.wildernessVault.reward.VaultRoomInstance;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WildernessVaultHandler implements WorldTask {

    public static final Logger LOGGER = LoggerFactory.getLogger(WildernessVaultHandler.class);
    public static final ObjectArrayList<String> looted = new ObjectArrayList<>();
    public static final ObjectArrayList<String> canLoot = new ObjectArrayList<>();
    private static WildernessVaultHandler instance;
    private WildernessVaultStatus vaultStatus;
    private BossStatus bossStatus;
    private VaultSpawnDefinition currentSpawn;
    private int cycle;
    public int firstEntranceTime;
    private int bossKilled;
    private final ObjectArrayList<NPC> npcs = new ObjectArrayList<>();
    public QueenReaver queenReaver;
    private int minionsSpawned;
    private int minionsKilled;
    private int lastMinion;
    public static final Projectile soulsplitProjectile = new Projectile(2009, 30, 50, 0, 0, 100, 64, 0);


    private WildernessVaultHandler() {
        resetVars();
    }

    private void resetVars() {
        cycle = 0;
        firstEntranceTime = 0;
        bossKilled = 0;
        minionsSpawned = 0;
        minionsKilled = 0;
        lastMinion = 0;
        vaultStatus = WildernessVaultStatus.INACTIVE;
        looted.clear();
        canLoot.clear();
        if (queenReaver != null) {
            if (!queenReaver.isFinished()) {
                queenReaver.finish();
            }
            queenReaver = null;
        }

        if (!npcs.isEmpty()) {
            for (NPC npc : npcs) {
                npc.finish();
            }
            npcs.clear();
        }
    }

    public void finishEvent(boolean announce) {

        LOGGER.info("Finishing event at cycle {}", cycle);
        resetVars();
        if (currentSpawn == null) {
            return;
        }

        World.removeObject(World.getObjectWithType(currentSpawn.getLocation(), 10));
        if (announce) {
            WorldBroadcasts.sendMessage("<img=47> <col=8b0000><shad=000000>The Wilderness Vault has decayed, another vault will spawn in " + Utils.getTimeTicks(WildernessVaultConstants.SPAWN_DELAY, true) + ".", BroadcastType.WILDERNESS_VAULT, true);
        }

        //kick players
        for (Player player : getAllPlayers()) {
            player.setLocation(currentSpawn.locationPlayer());
            player.setAnimation(Animation.KNOCKBACK);
            resetPlayer(player);
        }
    }

    public static void resetPlayer(Player player) {
        final RegionArea nextArea = GlobalAreaManager.getArea(player.getLocation());
        if (nextArea instanceof VaultRoomInstance || nextArea instanceof WildernessVaultArea)
            return;
        player.getPacketDispatcher().resetCamera();
        player.getTemporaryAttributes().remove("WILDY_CHEST_LOOT");
        player.getHpHud().close();
        player.getInterfaceHandler().closeInterface(GameInterface.WILDERNESS_VAULT_HUD);
    }

    private void spawnEvent() {
        resetVars();
        vaultStatus = WildernessVaultStatus.UNSEALED;
        bossStatus = BossStatus.NOT_SPAWNED;
        this.currentSpawn = Utils.random(VaultSpawnDefinition.VALUES);
        World.spawnObject(new WorldObject(WildernessVaultConstants.WILDERNESS_VAULT_ENTRANCE, 10, currentSpawn.getEntranceRotation(), currentSpawn.getLocation()));
        WorldBroadcasts.sendMessage("<img=47> <col=8b0000><shad=000000>A Wilderness Vault has appeared " + currentSpawn.getName() + ".", BroadcastType.WILDERNESS_VAULT, true);
        LOGGER.info("Spawning vault entrance at {} on cycle {}", currentSpawn.getLocation(), cycle);
    }

    public void minionKilled(AngelOfDeath angelOfDeath) {
        npcs.remove(angelOfDeath);

        int delay = soulsplitProjectile.build(angelOfDeath.getLocation(), WildernessVaultConstants.GOBLET_LOCATION);
        WorldTasksManager.schedule(() -> {
            minionsKilled++;
            if(bossStatus == BossStatus.NOT_SPAWNED && npcs.size() == 0 && minionsKilled >= amtMinionsToKill())
                spawnBoss();
        }, delay);
    }

    public void spawnBoss() {
        QueenReaver reaver = new QueenReaver();
        LOGGER.info("Spawning boss {}", reaver);
        World.spawnNPC(reaver);
        npcs.add(reaver);
        queenReaver = reaver;
        bossStatus = BossStatus.SPAWNED;
    }

    public void spawnMinion() {
        if(minionsSpawned >= amtMinionsToKill())
            return;

        if(lastMinion == 0 || cycle > lastMinion + getSpawnTime() && Utils.random(3) == 0) {
            int toSpawn = Math.min(8, amtMinionsToKill() - minionsKilled);
            List<Location> spawnCoords = new ArrayList<>(WildernessVaultConstants.MINION_SPAWNS);
            Collections.shuffle(spawnCoords);
            List<Player> players = new ArrayList<>(getPlayersInVault());
            for (int i = 0; i < toSpawn; i++) {
                Location randomPosition = spawnCoords.get(i);
                AngelOfDeath angelOfDeath = new AngelOfDeath(randomPosition, randomPosition.getX() == 1945 ? Direction.EAST : Direction.WEST);
                angelOfDeath.spawn();
                angelOfDeath.getCombat().setCombatDelay(i + 1);
                angelOfDeath.setTarget(Utils.random(players));
                npcs.add(angelOfDeath);
            }

            minionsSpawned += toSpawn;
            lastMinion = cycle;
        }
    }

    public int getSpawnTime() {
        int amt = amtMinionsToKill();
        int time = WildernessVaultConstants.EVENT_DURATION - firstEntranceTime;
        return Math.min(15, (int)((time / amt) * 0.50));
    }

    public int amtMinionsToKill() {
        return Math.max(5, (getPlayersInVault().size() * 4));
    }

    public Set<Player> getPlayersInVault() {
        return GlobalAreaManager.getArea(WildernessVaultArea.class).getPlayers();
    }

    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        players.addAll(getPlayersInVault());
        players.addAll(VaultRoomInstance.playersInside);
        return players;
    }


    private static final int ESSENCE_NEEDED = 14;
    private static final int TIME_LEFT = 12;
    private static final int PLAYERS_IN_VAULT = 10;

    private void updateOverlay() {
        final Set<Player> players = getPlayersInVault();
        final int size = players.size();
        if (size == 0) {
            return;
        }

        final String playersInVault = Integer.toString(size);
        final String timeLeft = timeTillNextEvent(false);
        final String essence = bossStatus == BossStatus.NOT_SPAWNED && vaultStatus == WildernessVaultStatus.STARTED ? Integer.toString(amtMinionsToKill() - minionsKilled) : "--";
        for (Player player : players) {
            player.getPacketDispatcher().sendComponentText(GameInterface.WILDERNESS_VAULT_HUD, PLAYERS_IN_VAULT, playersInVault);
            player.getPacketDispatcher().sendComponentText(GameInterface.WILDERNESS_VAULT_HUD, TIME_LEFT, timeLeft);
            player.getPacketDispatcher().sendComponentText(GameInterface.WILDERNESS_VAULT_HUD, ESSENCE_NEEDED, essence);
        }
    }

    public String timeTillNextEvent(boolean format) {
        if(vaultStatus == WildernessVaultStatus.UNSEALED)
            return Utils.getTimeTicks(WildernessVaultConstants.LOCK_DURATION - (cycle - firstEntranceTime), format);
        if(bossStatus == BossStatus.KILLED)
            return Utils.getTimeTicks(WildernessVaultConstants.LOOT_ROOM_TIME - (cycle - bossKilled), format);
        if(vaultStatus == WildernessVaultStatus.INACTIVE)
            return Utils.getTimeTicks(WildernessVaultConstants.SPAWN_DELAY - cycle, format);
        if(vaultStatus == WildernessVaultStatus.STARTED)
            return Utils.getTimeTicks(WildernessVaultConstants.EVENT_DURATION - cycle, format);
        return "";
    }

    @Override
    public void run() {
        cycle++;

        if (!DeveloperCommands.INSTANCE.getEnableWildernessVault())
            return;

        if(vaultStatus == WildernessVaultStatus.INACTIVE) {
            if(cycle == WildernessVaultConstants.SPAWN_DELAY)
                spawnEvent();
            return;
        }


        if (bossStatus == BossStatus.KILLED) {
            if (cycle == bossKilled + WildernessVaultConstants.LOOT_ROOM_TIME) {
                finishEvent(true);
                return;
            }

            updateOverlay();
        }

        if((vaultStatus == WildernessVaultStatus.STARTED || vaultStatus == WildernessVaultStatus.UNSEALED) && bossStatus != BossStatus.KILLED) {
            if(cycle == WildernessVaultConstants.EVENT_DURATION) {
                finishEvent(true);
                return;
            }
            updateOverlay();
        }

        if(vaultStatus == WildernessVaultStatus.UNSEALED) {
            if(firstEntranceTime > 0 && (cycle == firstEntranceTime + WildernessVaultConstants.LOCK_DURATION)) {
                vaultStatus = WildernessVaultStatus.STARTED;
                cycle = 0;
                World.replaceObject(World.getObjectWithType(currentSpawn.getLocation(), 10), new WorldObject(WildernessVaultConstants.WILDERNESS_VAULT_ENTRANCE_SEALED, 10, currentSpawn.getEntranceRotation(), currentSpawn.getLocation()));
                for (Player player : getAllPlayers()) {
                    player.sendMessage("The vault has been sealed!");
                }
            }
        }

        if(vaultStatus == WildernessVaultStatus.STARTED) {
            spawnMinion();

            if(cycle % WildernessVaultConstants.EVENT_DURATION == 2) {
                final String message = "The vault will expire in " + timeTillNextEvent(true) + ".";
                for (Player player : getAllPlayers()) {
                    player.sendMessage(message);
                }
            }

            if(cycle == (WildernessVaultConstants.EVENT_DURATION * 0.95)
            || (bossStatus == BossStatus.KILLED && cycle == ((bossKilled + WildernessVaultConstants.LOOT_ROOM_TIME)) * 0.95)) {
                for (Player player : getAllPlayers()) {
                    player.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, (byte) 5, (byte) 0, (byte) 0);
                }
            }
        }
    }

    public void setBossStatus(BossStatus bossStatus) {
        if(bossStatus == BossStatus.KILLED) {
            bossKilled = cycle;
            for (Player player : getPlayersInVault()) {
                player.sendMessage("You have two minutes to loot the chest before the vault expels everyone.");
            }
        }
        this.bossStatus = bossStatus;
    }

    public BossStatus getBossStatus() {
        return bossStatus;
    }

    public QueenReaver getQueenReaver() {
        return queenReaver;
    }

    public VaultSpawnDefinition getCurrentSpawn() {
        return currentSpawn;
    }

    public WildernessVaultStatus getVaultStatus() {
        return vaultStatus;
    }

    public void setVaultStatus(WildernessVaultStatus vaultStatus) {
        this.vaultStatus = vaultStatus;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public static WildernessVaultHandler getInstance() {
        if(instance == null)
            instance = new WildernessVaultHandler();
        return instance;
    }

    public int getFirstEntranceTime() {
        return firstEntranceTime;
    }

    public static ObjectArrayList<String> getLooted() {
        return looted;
    }

    public void setFirstEntranceTime() {
        this.firstEntranceTime = cycle;
    }

    public int getCycle() {
        return cycle;
    }

    public enum BossStatus {
        NOT_SPAWNED,
        SPAWNED,
        KILLED
    }
}

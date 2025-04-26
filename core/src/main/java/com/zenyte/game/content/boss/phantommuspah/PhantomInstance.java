package com.zenyte.game.content.boss.phantommuspah;

import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.plugins.dialogue.PlainChat;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Savions.
 */
public class PhantomInstance extends DynamicArea implements CycleProcessPlugin, CannonRestrictionPlugin, GravestoneLocationPlugin, PartialMovementPlugin, TeleportMovementPlugin, HitProcessPlugin, LootBroadcastPlugin {

	private static final Logger log = LoggerFactory.getLogger(PhantomInstance.class);
	static final String CA_TASK_INSTANCE_STARTED = "phantom_instance_started";
	static final String CA_TASK_INSTANCE_KC_ATT = "phantom_instance_kc";
	static final int[] MUSPAH_SPAWN_CIR = { 28, 34 };
	private static final int[][] INITIAL_SPIKE_CIR = {{21, 41}, {30, 43}, {42, 38}, {41, 30}, {39, 25}, {33, 24}, {26, 24}, {20, 26}, {21, 31}, {21, 36}};
	private static final int ORIGINAL_CHUNK_X = 352;
	private static final int ORIGINAL_CHUNK_Y = 528;
	private static final Location OUTSIDE_ENTRANCE = new Location(2909, 10317, 0);
	private static final Location INSIDE_ENTRANCE = new Location(2859, 4259, 0);
	private static final Location GRAVESTONE_LOCATION = new Location(2910, 10318, 0);
	private static final Animation SPAWN_ANIM = new Animation(9938);
	private static final Animation PLAYER_SPIKE_HIT_ANIM = new Animation(1114);
	private static final Animation SPIKE_SWING_ANIM = new Animation(9923);
	private static final Graphics SPIKE_SPAWN_GFX = new Graphics(2324);
	private static final Graphics SPIKE_RUMBLING_GFX = new Graphics(2335);
	private static final Graphics SPIKE_SWING_GFX = new Graphics(2321);
	private static final SoundEffect SPAWN_SOUND = new SoundEffect(6698, 1, 2);
	private static final SoundEffect RUMBLING_SOUND = new SoundEffect(1678);
	private static final SoundEffect SPIKE_SOUND = new SoundEffect(6780);
	private static final SoundEffect PLAYER_SPIKE_STEP_SOUND = new SoundEffect(6723, 1, 30);
	static final String DARKNESS_MESSAGE = "<col=6800bf>The Phantom Muspah floods the room with darkness...</col>";
	private static final String SHIELD_MITIGATE_DAMAGE_MSG = "<col=e00a19>The Phantom Muspah's</col> <col=00ffff>prayer shield</col> <col=e00a19>mitigates regular damage.</col>";
	private static final int SPIKE_SPAWN_OBJECT_ID = 46693;
	static final int SPIKE_OBJECT_ID = 46695;
	private final Player player;
	private PhantomMuspah muspah;
	private final ArrayList<PhantomSpike> spikes = new ArrayList<PhantomSpike>();
	private final ArrayList<PhantomSpike> movingSpikes = new ArrayList<PhantomSpike>();
	private int processingTicks = 10;
	private boolean loopSpikes;
	private int spikeImmunityTicks = 0;

	private PhantomInstance(AllocatedArea allocatedArea, final Player player) {
		super(allocatedArea, ORIGINAL_CHUNK_X, ORIGINAL_CHUNK_Y);
		this.player = player;
	}

	@Override public void process() {
		if (players.size() > 0) {
			if (!spikes.isEmpty()) {
				movingSpikes.removeIf(spike -> !spike.move());
				if (spikeImmunityTicks <= 0) {
					if (loopSpikes && muspah != null && !muspah.isFinished()) {
						spikes.stream().filter(object -> player.getLocation().equals(object.getLocation())).findFirst().ifPresent(spike -> damagePlayerBySpike(spike.getLocation()));
					}
				} else {
					spikeImmunityTicks--;
				}
			}
			if (muspah == null && processingTicks > 0) {
				processingTicks--;
				if (processingTicks <= 0) {
					spawnMuspah();
					player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 818);
					player.getPacketDispatcher().sendClientScript(7021, 240);
				}
			}
		}
	}

	private void spawnMuspah() {
		muspah = new PhantomMuspah(Utils.random(1) == 0 ? PhantomMuspah.MUSPAH_RANGE_ID : PhantomMuspah.MUSPAH_MELEE_ID, getLocationByIR(MUSPAH_SPAWN_CIR), this);
		muspah.spawn();
		muspah.setAnimation(SPAWN_ANIM);
		player.sendSound(SPAWN_SOUND);
		muspah.setCantInteract(true);
		muspah.lock(4);
		player.getMusic().unlock(Music.get("More Than Meets the Eye"));
		player.getBossTimer().startTracking("phantom muspah");
		spawnInitialSpikes();
	}

	public static void start(final Player player) {
		try {
			final AllocatedArea area = MapBuilder.findEmptyChunk(8, 6);
			final PhantomInstance instance = new PhantomInstance(area, player);
			instance.constructRegion();
		} catch (OutOfSpaceException e) {
			log.error("", e);
		}
	}

	@Override public void constructed() {
		final FadeScreen screen = new FadeScreen(player, () -> {
			player.setLocation(getLocation(INSIDE_ENTRANCE));
			player.faceDirection(Direction.WEST);
			IntStream.range(9907, 9958).forEach(animId -> player.getPacketDispatcher().sendClientScript(1846, animId));
			player.getDialogueManager().start(new PlainChat(player, "You enter the icy cavern."));
		});
		player.getDialogueManager().start(new PlainChat(player, "You enter the icy cavern.", false));
		screen.fade();
		WorldTasksManager.schedule(screen::unfade, 2);
	}

	@Override public void enter(Player player) {
		player.getTemporaryAttributes().put(CA_TASK_INSTANCE_STARTED, Boolean.TRUE);
	}

	@Override
	public Location onLoginLocation() {
		return OUTSIDE_ENTRANCE;
	}

	@Override public void leave(Player player, boolean logout) {
		player.getTemporaryAttributes().remove(CA_TASK_INSTANCE_STARTED);
		player.getTemporaryAttributes().remove(CA_TASK_INSTANCE_KC_ATT);
		player.blockIncomingHits(1);
		player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
		player.getPacketDispatcher().sendClientScript(7021, 255);
		if (logout) {
			player.forceLocation(OUTSIDE_ENTRANCE);
		}
	}

	void leave() {
		player.blockIncomingHits(2);
		final FadeScreen screen = new FadeScreen(player, () -> {
			player.setLocation(OUTSIDE_ENTRANCE);
		});
		screen.fade();
		WorldTasksManager.schedule(screen::unfade, 2);
		spikes.forEach(World::removeObject);
	}

	private void spawnInitialSpikes() {
		List<int[]> spikeCIRS = Arrays.asList(INITIAL_SPIKE_CIR);
		Collections.shuffle(spikeCIRS);
		muspah.setCantInteract(true);
		muspah.getCombat().setCombatDelay(7);
		spawnSpikes(getLocationByIR(spikeCIRS.get(0)), getLocationByIR(spikeCIRS.get(1)));
		WorldTasksManager.schedule(() -> {
			if (muspah != null && players.size() > 0) {
				muspah.setCantInteract(false);
			}
		}, 3);
	}

	void spawnNextSpikes() {
		ArrayList<Location> newSpikes = new ArrayList<Location>();
		final Location playerLocation = player.getLocation().copy();
		if (World.getObjectWithType(playerLocation, 10) == null) {
			newSpikes.add(playerLocation);
		}
		final Direction[] directions = Direction.values;
		final int maxSpikes = Math.max((int) Math.ceil(0.6F * spikes.size()), 3);

		spikeLoop : for (final PhantomSpike spike : spikes) {
			if (newSpikes.size() > maxSpikes && muspah.getId() != PhantomMuspah.MUSPAH_RANGE_FAST_ID) {
				break;
			}
			if (spike.isMoving()) {
				continue;
			}
			final int startIndex = Utils.random((directions.length / 2) - 1) * 2;
			for (int i = 0; i < directions.length; i += 2) {
				final Direction direction = directions[(startIndex + i) % directions.length];
				final Location newLocation = spike.getLocation().transform(direction.getOffsetX(), direction.getOffsetY());
				if (newSpikes.contains(newLocation) || isSpikeTileBlocked(newLocation)) {
					continue;
				}
				newSpikes.add(newLocation);
				if (muspah.getId() != PhantomMuspah.MUSPAH_RANGE_FAST_ID || Utils.random(2) == 0) {
					break;
				}
			}
		}
		spawnSpikes(newSpikes.toArray(new Location[0]));
	}

	private void spawnSpikes(@NonNull final Location... tiles) {
		for (Location tile : tiles) {
			World.sendGraphics(SPIKE_RUMBLING_GFX, tile);
		}
		List<PhantomSpike> addedSpikes = new ArrayList<PhantomSpike>(tiles.length);
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override public void run() {
				if (player.isFinished() || players.size() < 1 || muspah == null || muspah.isDying() || muspah.isFinished()) {
					if (!player.isFinished()) {
						player.getPacketDispatcher().resetCamera();
					}
					stop();
					return;
				}
				if (++ticks == 1) {
					for (Location tile : tiles) {
						World.sendGraphics(SPIKE_SPAWN_GFX, tile);
						final PhantomSpike spike = new PhantomSpike(PhantomInstance.this, SPIKE_SPAWN_OBJECT_ID, Utils.random(3), tile);
						addedSpikes.add(spike);
						World.spawnObject(spike);
					}
				} else if (ticks == 2) {
					player.getPacketDispatcher().resetCamera();
					player.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 5, 0, 0);
					player.getPacketDispatcher().sendCameraShake(CameraShakeType.UP_AND_DOWN, 8, 0, 0);
					player.getPacketDispatcher().sendCameraShake(CameraShakeType.FRONT_AND_BACK, 9, 0, 0);
					player.getPacketDispatcher().sendSoundEffect(SPIKE_SOUND);
					player.getPacketDispatcher().sendSoundEffect(RUMBLING_SOUND);
					loopSpikes = true;
					addedSpikes.forEach(spike -> {
						spike.setId(SPIKE_OBJECT_ID);
						spikes.add(spike);
						World.spawnObject(spike);
						if (player.getLocation().equals(spike.getLocation())) {
							damagePlayerBySpike(spike);
						}
					});
					addedSpikes.clear();
				} else if (ticks == 4) {
					player.getPacketDispatcher().resetCamera();
					stop();
				}
			}
		}, 1, 0);
	}

	void damagePlayerBySpike(@NonNull Location spike) {
		if (player.isLocked() || spikeImmunityTicks > 0) {
			return;
		}
		player.stopAll();
		final Hit hit = new Hit(muspah, Utils.random(10, 20), HitType.DEFAULT, 2);
		muspah.setTakenAvoidableDamage();
		player.applyHit(hit);
		spikeImmunityTicks = 1;
		if (muspah != null) {
			muspah.applyHit(new Hit(player, (int) (hit.getDamage() * 0.75), HitType.HEALED));
		}
		Location nextLoc = null;
		loc : for (int radius = 1; radius < 5; radius++) {
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					if (x != -radius && x != radius && y != -radius && y != radius) {
						continue;
					}
					final Location newLocation = spike.transform(x, y);
					if (World.checkWalkStep(spike.getPlane(), spike.getX(), spike.getY(), Direction.getDirection(x, y).getMovementDirection(), 1, false, false)
							&& World.getObjectWithType(newLocation, 10) == null) {
						nextLoc = newLocation;
						break loc;
					}
				}
			}
		}
		if (nextLoc != null) {
			player.getPacketDispatcher().sendSoundEffect(PLAYER_SPIKE_STEP_SOUND);
			player.lock(1);
			player.setAnimation(PLAYER_SPIKE_HIT_ANIM);
			player.autoForceMovement(spike, nextLoc, 0, 29);
		} else {
			loopSpikes = true;
		}
	}

	void sendMovingSpikes() {
		if (muspah == null || players.size() < 1) {
			return;
		}
		player.sendMessage(DARKNESS_MESSAGE);
		muspah.setAnimation(SPIKE_SWING_ANIM);
		muspah.setGraphics(SPIKE_SWING_GFX);
		muspah.getCombat().setCombatDelay(Math.max(muspah.getCombat().getCombatDelay(), 4));
		player.getPacketDispatcher().sendClientScript(7021, 130);
		for (int index = spikes.size() - 1; index >= 0 && movingSpikes.size() < 4; index--) {
			final PhantomSpike spike = spikes.get(index);
			if (player.getLocation().getTileDistance(spike) > 4 && !spike.fetchNextLocation().equals(spike)) {
				if (movingSpikes.size() % 2 != 0) {
					spike.setOffTick();
				}
				movingSpikes.add(spike);
				spike.setMoving();
			}
		}
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override public void run() {
				if (players.size() < 1) {
					return;
				}
				player.getPacketDispatcher().sendClientScript(7021, 255);
			}
		}, 4);
	}

	boolean surroundedBySpikes(final Location tile, int radius) {
		mainLoop : for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				if (x != radius && x != -radius && y != radius && y != -radius) {
					continue;
				}
				final Location testTile = tile.transform(x, y);
				Optional<PhantomSpike> spike = spikes.stream().filter(s -> s.getLocation().equals(testTile)).findAny();
				if (spike.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	boolean isSpikeTileBlocked(final Location tile) {
		return !World.isFloorFree(tile, 1) || World.getObjectWithType(tile, 10) != null || World.getObjectWithType(tile, 11) != null;
	}

	void muspahFinished() {
		muspah = null;
		processingTicks = BossRespawnTimer.PHANTOM_MUSPAH.getTimer().getValue();
		movingSpikes.clear();
		if (players.size() > 0) {
			player.getMusic().unlock(Music.get("Secrets of the North"));
			spikes.forEach(spike -> {
				spike.setId(PhantomSpike.DOWN_OBJECT_ID);
				World.spawnObject(spike);
			});
			WorldTasksManager.schedule(() -> {
				if (players.size() > 0) {
					spikes.forEach(World::removeObject);
					spikes.clear();
				}
			});
		} else {
			spikes.clear();
		}
	}

	void setSpikesBlockProjectiles(boolean block) {
		spikes.forEach(spike -> {
			final int mask = World.getMask(0, spike.getX(), spike.getY());
			World.setMask(0, spike.getX(), spike.getY(), block ? mask | Flags.OBJECT_PROJECTILE : mask & ~Flags.OBJECT_PROJECTILE);
		});
	}

	@Override public boolean hit(Player source, Entity target, Hit hit, float modifier) {
		if (muspah == null || muspah.isFinished() || muspah.getShieldHitBar() == null || players.size() < 1) {
			return true;
		}
		final Item weapon = hit.getWeapon() instanceof Item ? (Item) hit.getWeapon() : null;
		if (!HitType.SHIELD_DOWN.equals(hit.getHitType())) {
			if (player.getPrayerManager().isActive(Prayer.SMITE)) {
				hit.setDamage((int) (hit.getDamage() * 0.25));
				hit.setHitType(HitType.SHIELD_DOWN);
			} else if (!"Thrall".equals(hit.getWeapon()) &&
					!(weapon != null && hit.isSpecial() && (weapon.getId() == ItemId.ANCIENT_MACE || weapon.getId() == ItemId.ZARYTE_CROSSBOW))) {
				player.sendMessage(SHIELD_MITIGATE_DAMAGE_MSG);
			}
		}
		return true;
	}

	@Override public String name() {
		return "Phantom instance";
	}

	@Override
	public boolean isMultiwayArea(Position position) {
		return true;
	}

	@Override public boolean canPlaceCannon(Position position) {
		return false;
	}

	@Override public Location getGravestoneLocation() {
		return GRAVESTONE_LOCATION;
	}

	@Override public boolean processMovement(Player player, int x, int y) {
		loopSpikes = true;
		return true;
	}

	@Override public void processMovement(Player player, Location destination) {
		loopSpikes = true;
	}

	final Location getLocationByIR(int[] cir) {
		return new Location((chunkX * 8) + (cir[0] & 0x3f), (chunkY * 8) + (cir[1] & 0x3f), 0);
	}

	public final Player getPlayer() { return player; }

	public final PhantomMuspah getMuspah() { return muspah; }
}

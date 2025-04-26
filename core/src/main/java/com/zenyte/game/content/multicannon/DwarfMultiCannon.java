package com.zenyte.game.content.multicannon;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.plugins.Listener;
import com.zenyte.plugins.ListenerType;
import com.zenyte.plugins.PluginManager;
import com.zenyte.plugins.events.CannonRemoveEvent;
import com.zenyte.plugins.events.InitializationEvent;
import com.zenyte.plugins.events.ServerShutdownEvent;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Optional;

/**
 * Handles the Dwarf multicannon settings for the player. Contains all the world's cannons. Sound effect: 2877
 * 
 * @author Kris | 13. okt 2017 : 13:03.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class DwarfMultiCannon {

	private static final Logger log = LoggerFactory.getLogger(DwarfMultiCannon.class);
	private static final Animation ANIM = new Animation(827);
	private static final SoundEffect CANNON_SETUP = new SoundEffect(2876, 10);
	private static final SoundEffect CANNON_PICKUP = new SoundEffect(2581);
	private static final long CANNON_MILLIS = TimeUnit.TICKS.toMillis(3000);
	public static final Object2ObjectMap<String, Multicannon> placedCannons = new Object2ObjectOpenHashMap<>();

	private transient Player player;
	private transient Multicannon cannon;
	private byte cannonballs;
	private byte graniteballs;
	private long setupTime;
	private byte setupStage;
	private DwarfMultiCannonType type;

	public DwarfMultiCannon(final Player player) {
		this.player = player;
	}

	@Listener(type = ListenerType.LOGIN)
	private static void onLogin(final Player player) {
		final DwarfMultiCannon multicannon = player.getDwarfMulticannon();
		if (multicannon.setupTime == 0) {
			return;
		}
		final Multicannon existingCannon = placedCannons.get(player.getUsername());
		if (existingCannon != null) {
			multicannon.cannon = existingCannon;
			multicannon.cannon.setPlayer(new WeakReference<>(player));
			multicannon.cannon.setCannon(multicannon);
		}
		if (multicannon.setupTime < (Utils.currentTimeMillis() - CANNON_MILLIS)) {
			player.sendMessage("<col=ff0000>Your dwarf multicannon has decayed! Speak with Nulodion to retrieve it.");
		}
	}


	@Subscribe
	public static void onInit(final InitializationEvent event) {
		final Player player = event.getPlayer();
		final Player savedPlayer = event.getSavedPlayer();
		final DwarfMultiCannon cannon = player.getDwarfMulticannon();
		cannon.player = player;
		if (savedPlayer == null) {
			return;
		}
		final DwarfMultiCannon savedCannon = savedPlayer.getDwarfMulticannon();
		cannon.setCannonballs(savedCannon.cannonballs);
		cannon.setGraniteballs(savedCannon.graniteballs);
		cannon.setupTime = savedCannon.setupTime;
		cannon.setupStage = savedCannon.setupStage;
		cannon.type = savedCannon.type;
	}

	@Subscribe
	public static void onServerShutdown(final ServerShutdownEvent event) {
		try {
			for (final Multicannon cannon : placedCannons.values()) {
				final Player player = cannon.getPlayer().get();
				if (player == null) {
					continue;
				}
				player.getDwarfMulticannon().setupTime = 1;// Force the cannon to be picked up at Nulodion.
			}
		} catch (Exception e){
			log.error("Failed to shutdown cannons successfully.", e);
		}
	}

	/**
	 * Initiates a never-ending process of processing all the cannons.
	 */
	public static void init() {
		WorldTasksManager.schedule(() -> {
			final Iterator<Multicannon> cannonIterator = placedCannons.values().iterator();
			while (cannonIterator.hasNext()) {
				final Multicannon cannon = cannonIterator.next();
				try {
					if (!cannon.process()) {
						cannonIterator.remove();
						PluginManager.post(new CannonRemoveEvent(cannon));
						Optional.ofNullable(cannon.getPlayer().get())
								.ifPresent(player -> player.getDwarfMulticannon().cannon = null);
					}
				} catch (Exception e){
					log.error("Failed to process cannon {}", cannon, e);
				}
			}
		}, 0, 0);
	}

	/**
	 * Goes through numerous checks, if all come in true, plants the cannon under the player's location.
	 */
	public void setupCannon(DwarfMultiCannonType type) {
		if (isDecayed()) {
			player.sendMessage("A cannon of yours has decayed. Speak to Nulodion to retrieve it.");
			return;
		}
		if (cannon != null) {
			player.sendMessage("You already have a dwarf multicannon setup.");
			return;
		}
		if (!player.getInventory().containsItems(type.getParts())) {
			player.sendMessage("You need a cannon base, stand, barrels and a furnace to setup a dwarf multicannon.");
			return;
		}
		if (!canSetupCannon(player)) {
			return;
		}
		final Location cannonTile = new Location(player.getX() - 1, player.getY() - 1, player.getPlane());
		final Location center = new Location(player.getLocation());
		block:
		if (World.getObjectWithType(cannonTile, 10) != null || !World.isFloorFree(cannonTile, 2) || containsCannon(center.getX(), center.getY()) || isProjectileClipped(cannonTile)) {
			final int cannonX = cannonTile.getX();
			final int cannonY = cannonTile.getY();
			final int z = cannonTile.getPlane();
			final int centerX = center.getX();
			final int centerY = center.getY();
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					cannonTile.setLocation(cannonX + x, cannonY + y, z);
					center.setLocation(centerX + x, centerY + y, z);
					if (World.getObjectWithType(cannonTile, 10) == null && World.isFloorFree(cannonTile, 2) && !containsCannon(center.getX(), center.getY()) && !isProjectileClipped(cannonTile)) {
						break block;
					}
				}
			}
			player.sendMessage("You can't place a cannon here.");
			return;
		}
		player.lock(10);
		this.type = type;
		final TileEvent event = new TileEvent(player, new TileStrategy(center, 2), () -> {
			player.unlock();
			WorldTasksManager.schedule(new TickTask() {
				private int stage;
				private long lastSetup;
				@Override
				public void run() {
					if (player.isFinished()) {
						stop();
						return;
					}
					ticks++;
					final double distance = player.getLocation().getDistance(center);
					if (distance > 3) {
						if (distance > 6 || player.isProjectileClipped(center, true)) {
							stop();
						} else {
							player.resetWalkSteps();
							player.setRouteEvent(new TileEvent(player, new TileStrategy(center, 2), null));
						}
						return;
					}
					if (Utils.currentTimeMillis() - TimeUnit.TICKS.toMillis(2) < lastSetup) {
						return;
					}
					lastSetup = Utils.currentTimeMillis();
					if (!player.getInventory().containsItem(type.getParts()[stage])) {
						stop();
						return;
					}
					if (stage > 0 && cannon == null) {
						stop();
						return;
					}
					Multicannon cannon = new Multicannon(type.getBaseLoc(), 10, 0, cannonTile, player, type);
					if (stage == 0) {
						if (World.getObjectWithType(cannonTile, 10) != null || !World.isFloorFree(cannonTile, 2) || containsCannon(center.getX(), center.getY())) {
							stop();
							return;
						}
					}
					player.faceObject(cannon);
					player.setAnimation(ANIM);
					World.sendSoundEffect(center, CANNON_SETUP);
					player.getInventory().deleteItem(type.getParts()[stage]);
					switch (setupStage = (byte) stage++) {
						case 0 -> {
							DwarfMultiCannon.this.cannon = cannon;
							setSetupTime(Utils.currentTimeMillis());
							World.spawnObject(cannon);
							placedCannons.put(player.getUsername(), cannon);
							player.sendFilteredMessage("You place the cannon base on the ground.");
							return;
						}
						case 1 -> {
							World.spawnObject(cannon = new Multicannon(type.getStandLoc(), 10, 0, cannonTile, player, type));
							placedCannons.put(player.getUsername(), DwarfMultiCannon.this.cannon = cannon);
							player.sendFilteredMessage("You add the stand.");
							return;
						}
						case 2 -> {
							World.spawnObject(cannon = new Multicannon(type.getBarrelsLoc(), 10, 0, cannonTile, player, type));
							placedCannons.put(player.getUsername(), DwarfMultiCannon.this.cannon = cannon);
							player.sendFilteredMessage("You add the barrels.");
							return;
						}
						case 3 -> {
							World.spawnObject(cannon = new Multicannon(type.getCannonLoc(), 10, 0, cannonTile, player, type));
							placedCannons.put(player.getUsername(), DwarfMultiCannon.this.cannon = cannon);
							player.sendFilteredMessage("You add the furnace.");
							loadCannon();
							stop();
						}
					}
				}
			}, 0, 1);
		});
		event.setOnFailure(() -> player.unlock());
		player.setRouteEvent(event);
	}

	private boolean isProjectileClipped(final Location cannonTile) {
		return ProjectileUtils.isProjectileClipped(null, null, cannonTile, new Location(cannonTile.getX() + 2, cannonTile.getY(), cannonTile.getPlane()), true) || ProjectileUtils.isProjectileClipped(null, null, cannonTile, new Location(cannonTile.getX(), cannonTile.getY() + 2, cannonTile.getPlane()), true) || ProjectileUtils.isProjectileClipped(null, null, cannonTile, new Location(cannonTile.getX() + 2, cannonTile.getY() + 2, cannonTile.getPlane()), true) || ProjectileUtils.isProjectileClipped(null, null, cannonTile, new Location(cannonTile.getX() + 1, cannonTile.getY() + 2, cannonTile.getPlane()), true) || ProjectileUtils.isProjectileClipped(null, null, cannonTile, new Location(cannonTile.getX() + 2, cannonTile.getY() + 1, cannonTile.getPlane()), true);
	}

	/**
	 * Checks whether there's a cannon at the specified location or not.
	 */
	private boolean containsCannon(final int x, final int y) {
		for (final Multicannon cannon : placedCannons.values()) {
			if (CollisionUtil.collides(x, y, 2, cannon.getX() + 1, cannon.getY() + 1, 2)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param tile the tile to which the cannon is being placed.
	 * @return whether or not the cannon can be placed there.
	 */
	public static boolean canSetupCannon(final Position tile) {
		if (tile instanceof Player) {
			final RegionArea area = ((Player) tile).getArea();
			if (area instanceof CannonRestrictionPlugin) {
				if (!((CannonRestrictionPlugin) area).canPlaceCannon(tile)) {
					((Player) tile).sendMessage(((CannonRestrictionPlugin) area).restrictionMessage());
					return false;
				}
			}
		} else {
			final RegionArea area = GlobalAreaManager.getArea(tile);
			if (area instanceof CannonRestrictionPlugin) {
				return ((CannonRestrictionPlugin) area).canPlaceCannon(tile);
			}
		}
		return true;
	}

	/**
	 * Handles the object options for the multicannon.
	 * 
	 * @param object
	 *            to handle
	 * @param option
	 *            which option was clicked.
	 * @return whether the operation was with a cannon.
	 */
	public boolean handleCannon(final WorldObject object, final int option) {
		if (!object.equals(placedCannons.get(player.getUsername()))) {
			return false;
		}
		final String opt = object.getDefinitions().getOption(option);
		switch (opt) {
		case "Fire": 
			if (cannon.isFiring() || cannonballs == 0 && graniteballs == 0) {
				if (!player.getInventory().containsItem(2, 1) && !player.getInventory().containsItem(21728, 1)) {
					player.sendMessage("You need some cannonballs to load the cannon.");
					return true;
				}
				loadCannon();
				return true;
			} else {
				player.sendMessage("You fire the dwarf multicannon.");
				cannon.setFiring(true);
			}
			break;
		case "Pick-up": 
			take(object.getId(), true);
			break;
		case "Repair": 
			cannon.setDecayTimer(3000);
			cannon.setId(type.getCannonLoc());
			World.spawnObject(cannon);
			player.sendMessage("You repair the cannon.");
			setSetupTime(Utils.currentTimeMillis());
			break;
		case "Empty": 
			int spaceRequired = 0;
			if (cannonballs > 0) {
				if (!player.getInventory().containsItem(2, 1)) {
					spaceRequired++;
				}
			}
			if (graniteballs > 0) {
				if (!player.getInventory().containsItem(21728, 1)) {
					spaceRequired++;
				}
			}
			if (player.getInventory().getFreeSlots() < spaceRequired) {
				player.sendMessage("You need at least " + spaceRequired + " free inventory slots to pick the cannon up.");
				return true;
			}
			final boolean granite = graniteballs > 0;
			if (cannonballs > 0) {
				player.getInventory().addItem(2, cannonballs);
				setCannonballs(0);
			}
			if (graniteballs > 0) {
				player.getInventory().addItem(21728, graniteballs);
				setGraniteballs(0);
			}
			cannon.setFiring(false);
			player.sendMessage("You unload your cannon and receive " + (granite ? "Granite cannonball" : "Cannonball") + ".");
			break;
		}
		return true;
	}

	public void take(final int id, final boolean message) {
		int spaceRequired = 4;
		if (cannonballs > 0) {
			if (!player.getInventory().containsItem(2, 1)) {
				spaceRequired++;
			}
		}
		if (graniteballs > 0) {
			if (!player.getInventory().containsItem(21728, 1)) {
				spaceRequired++;
			}
		}
		if (player.getInventory().getFreeSlots() < spaceRequired) {
			player.sendMessage("You need at least " + spaceRequired + " free inventory slots to pick the cannon up.");
			return;
		}
		if (message) {
			player.sendMessage("You pick up the cannon. It's really heavy.");
		}
		placedCannons.remove(player.getUsername());
		setSetupTime(0);
		if (cannonballs > 0) {
			player.getInventory().addItem(2, cannonballs);
			setCannonballs(0);
		}
		if (graniteballs > 0) {
			player.getInventory().addItem(21728, graniteballs);
			setGraniteballs(0);
		}
		player.getPacketDispatcher().sendSoundEffect(CANNON_PICKUP);
		if (cannon != null) {
			PluginManager.post(new CannonRemoveEvent(cannon));
			World.removeObject(cannon);
			cannon = null;
		}
		if (id == type.getBaseLoc() || id == type.getStandLoc() || id == type.getBarrelsLoc() || id == type.getCannonLoc() || id == type.getBrokenCannonLoc()) {
			player.getInventory().addItem(type.getBase());
		}
		if (id == type.getStandLoc() || id == type.getBarrelsLoc() || id == type.getCannonLoc() || id == type.getBrokenCannonLoc()) {
			player.getInventory().addItem(type.getStand());
		}
		if (id == type.getBarrelsLoc() || id == type.getCannonLoc() || id == type.getBrokenCannonLoc()) {
			player.getInventory().addItem(type.getBarrel());
		}
		if (id == type.getCannonLoc() || id == type.getBrokenCannonLoc()) {
			player.getInventory().addItem(type.getFurnace());
		}
	}

	public static int getCannonBallsMax(Player player) {
		int maxBalls = switch (player.getMemberRank()) {
			case UBER, AMASCUT -> 100;
			case LEGENDARY, MYTHICAL -> 90;
			case RESPECTED -> 70;
			case EXTREME -> 60;
			case EXPANSION -> 50;
			case PREMIUM -> 40;
			default -> 30;
		};
		if (player.getCombatAchievements().hasTierCompleted(CATierType.MEDIUM)) {
			maxBalls += 5;
		} else if (player.getCombatAchievements().hasTierCompleted(CATierType.HARD)) {
			maxBalls += 10;
		} else if (player.getCombatAchievements().hasTierCompleted(CATierType.ELITE)) {
			maxBalls += 15;
		}
		return maxBalls;
	}

	/**
	 * Attempts to load the cannon with cannonballs.
	 */
	public void loadCannon() {
		int maxBalls = getCannonBallsMax(player);
		if ((cannonballs + graniteballs) >= maxBalls) {
			return;
		}
		final int freeSpace = maxBalls - (cannonballs + graniteballs);
		final int balls = player.getInventory().getAmountOf(2);
		final int amount = Math.min(freeSpace, balls);
		if (amount > 0) {
			player.getInventory().deleteItem(2, amount);
			setCannonballs(cannonballs + amount);
			player.sendMessage("You load the cannon with " + amount + " cannonball" + (amount == 1 ? "." : "s."));
		}
		final int granite = player.getInventory().getAmountOf(21728);
		final int remainingSpace = maxBalls - (cannonballs + graniteballs);
		final int toAdd = Math.min(remainingSpace, granite);
		if (toAdd > 0) {
			player.getInventory().deleteItem(21728, toAdd);
			setGraniteballs(graniteballs + toAdd);
			player.sendMessage("You load the cannon with " + toAdd + " granite cannonball" + (toAdd == 1 ? "." : "s."));
		}
	}


	public boolean isDecayed() {
		return setupTime != 0 && setupTime < (Utils.currentTimeMillis() - CANNON_MILLIS);
	}

	public Multicannon getCannon() {
		return cannon;
	}

	public byte getCannonballs() {
		return cannonballs;
	}

	public void setCannonballs(int cannonballs) {
		this.cannonballs = (byte) cannonballs;
		player.getVarManager().sendVar(3, Math.max(cannonballs, graniteballs));
	}

	public byte getGraniteballs() {
		return graniteballs;
	}

	public void setGraniteballs(int graniteballs) {
		this.graniteballs = (byte) graniteballs;
		player.getVarManager().sendVar(3, Math.max(cannonballs, graniteballs));
	}

	public void setSetupTime(long setupTime) {
		this.setupTime = setupTime;
	}

	public byte getSetupStage() {
		return setupStage;
	}

	public DwarfMultiCannonType getType() {
		return type;
	}

	public void setType(DwarfMultiCannonType type) {
		this.type = type;
	}

}

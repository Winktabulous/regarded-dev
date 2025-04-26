package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.impl.slayer.Wallbeast;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.PartialMovementPlugin;
import com.zenyte.plugins.item.LightSourceItem;
import com.zenyte.plugins.item.LightSourceItem.LightSource;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Map;

/**
 * @author Kris | 27. aug 2018 : 01:49:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class LumbridgeSwampCavesArea extends DarkArea implements PartialMovementPlugin {
	private static final Location[] FLAMMABLE_LOCATIONS = new Location[] {new Location(3145, 9579, 0), new Location(3146, 9577, 0), new Location(3186, 9567, 0), new Location(3164, 9539, 0), new Location(3162, 9559, 0), new Location(3161, 9561, 0), new Location(3156, 9541, 0), new Location(3159, 9560, 0), new Location(3146, 9576, 0), new Location(3145, 9575, 0), new Location(3154, 9540, 0), new Location(3182, 9589, 0), new Location(3160, 9560, 0), new Location(3163, 9595, 0), new Location(3161, 9559, 0), new Location(3164, 9594, 0), new Location(3153, 9541, 0), new Location(3157, 9552, 0), new Location(3182, 9541, 0), new Location(3154, 9543, 0), new Location(3183, 9541, 0), new Location(3248, 9572, 0), new Location(3244, 9556, 0), new Location(3211, 9591, 0), new Location(3248, 9571, 0), new Location(3205, 9569, 0), new Location(3231, 9582, 0), new Location(3205, 9571, 0), new Location(3231, 9583, 0), new Location(3246, 9570, 0), new Location(3237, 9544, 0), new Location(3221, 9543, 0), new Location(3246, 9571, 0), new Location(3232, 9582, 0), new Location(3254, 9585, 0), new Location(3205, 9575, 0), new Location(3232, 9584, 0), new Location(3248, 9569, 0), new Location(3232, 9551, 0), new Location(3231, 9580, 0), new Location(3205, 9573, 0), new Location(3220, 9554, 0), new Location(3248, 9570, 0), new Location(3220, 9555, 0)};
	private static final Int2ObjectOpenHashMap<Location> WALL_BEASTS = new Int2ObjectOpenHashMap<Location>() {
		{
			put(new Location(3161, 9546, 0).getPositionHash(), new Location(3161, 9547, 0));
			put(new Location(3164, 9555, 0).getPositionHash(), new Location(3164, 9556, 0));
			put(new Location(3162, 9573, 0).getPositionHash(), new Location(3162, 9574, 0));
			put(new Location(3198, 9553, 0).getPositionHash(), new Location(3198, 9554, 0));
			put(new Location(3198, 9571, 0).getPositionHash(), new Location(3198, 9572, 0));
			put(new Location(3216, 9587, 0).getPositionHash(), new Location(3216, 9588, 0));
			put(new Location(3215, 9559, 0).getPositionHash(), new Location(3215, 9560, 0));
		}
	};

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(new int[][] {{3162, 9601}, {3136, 9585}, {3147, 9534}, {3187, 9532}, {3198, 9546}, {3213, 9545}, {3226, 9536}, {3257, 9550}, {3264, 9585}, {3336, 9595}, {3330, 9658}, {3224, 9657}, {3221, 9625}, {3221, 9614}, {3208, 9605}}, 0)};
	}

	@Override
	public String name() {
		return "Lumbridge Swamp Caves";
	}

	@Override
	public void process() {
		if (players.isEmpty()) {
			return;
		}
		for (final Player player : players) {
			final Map<Object, Object> attributes = player.getTemporaryAttributes();
			final Object flaring = attributes.get("lightsource flaring");
			if (flaring != null) {
				final int flareTimer = player.getNumericTemporaryAttribute("lightsource flare count").intValue();
				if (flareTimer == 5) {
					final Location position = new Location(player.getNumericTemporaryAttribute("lightsource flare hash").intValue());
					if (player.getLocation().withinDistance(position, 3)) {
						attributes.remove("lightsource flaring");
						if (Utils.random(10) >= 3) {
							LightSource.extinguish(player, Integer.MAX_VALUE, LightSource.MAPPED_SOURCES.values().toArray(new LightSource[0]));
							player.sendMessage("The swamp gas explodes!");
							player.applyHit(new Hit(Utils.random(10, 20), HitType.REGULAR));
							refreshOverlay(player);
							continue;
						}
						final Object name = attributes.get("lightsource name");
						if (name instanceof String) {
							player.sendMessage("Your " + name + " stops flaring.");
						}
					}
					continue;
				}
				attributes.put("lightsource flare count", flareTimer + 1);
			}
			if (player.getInterfaceHandler().isVisible(96)) {
				final int ticksPassed = player.getNumericTemporaryAttribute("full darkess timer").intValue();
				if (ticksPassed == 5) {
					player.sendMessage("You hear tiny insects skittering over the ground...");
				}
				if (ticksPassed >= 25) {
					if (ticksPassed == 25) {
						player.sendMessage("Tiny biting insects swarm all over you!");
					}
					player.applyHit(new Hit(1, HitType.REGULAR));
				}
				attributes.put("full darkess timer", ticksPassed + 1);
				continue;
			}
			if (Utils.random(1000) == 0) {
				final LightSourceItem.LightSource[] lightsources = LightSource.getLitLightSources(player, LightSourceItem.NONE);
				if (lightsources.length > 0) {
					LightSource.extinguish(player, 1, lightsources[Utils.random(lightsources.length - 1)]);
					refreshOverlay(player);
				}
				continue;
			}
			if (Utils.random(10) == 0 && flaring == null) {
				final LightSourceItem.LightSource[] lightsources = LightSource.getLitLightSources(player, LightSourceItem.FLAMMABLE);
				if (lightsources.length == 0) {
					continue;
				}
				final Location location = player.getLocation();
				for (final Location flammableLocation : FLAMMABLE_LOCATIONS) {
					if (location.withinDistance(flammableLocation, 3)) {
						final String name = lightsources[Utils.random(lightsources.length - 1)].toString().toLowerCase().replaceAll("_", " ");
						player.sendMessage("Your " + name + " flares brightly.");
						attributes.put("lightsource name", name);
						attributes.put("lightsource flaring", 1);
						attributes.put("lightsource flare count", 0);
						attributes.put("lightsource flare hash", location.getPositionHash());
						break;
					}
				}
			}
		}
	}

	private static final SoundEffect repelSound = new SoundEffect(895, 10, 0);
	private static final SoundEffect grabSound = new SoundEffect(893, 10, 0);
	private static final Animation animation = new Animation(1810);

	@Override
	public boolean processMovement(Player player, int x, int y) {
		final Location tile = WALL_BEASTS.get(y | x << 14 | player.getPlane() << 28);
		if (tile == null) {
			return true;
		}
		World.findNPC(475, tile, 0).ifPresent(npc -> {
			if (!(npc instanceof Wallbeast) || !((Wallbeast) npc).isAttackReady()) {
				return;
			}
			npc.lock(5);
			player.stopAll();
			player.lock(1);
			WorldTasksManager.schedule(() -> {
				npc.setTransformation(476);
				npc.setAnimation(Wallbeast.AWAKENING_ANIM);
				npc.setAttackingDelay(Utils.currentTimeMillis());
				if (!SlayerEquipment.SPINY_HELMET.isWielding(player)) {
					player.setAnimation(animation);
					World.sendSoundEffect(new Location(player.getLocation()), grabSound);
					player.setFaceEntity(npc);
					player.stun(6);
					player.applyHit(new Hit(npc, Utils.random(13, 18), HitType.REGULAR));
					player.sendMessage("A giant hand appears and grabs your head!");
					WorldTasksManager.schedule(() -> player.setAnimation(Animation.STOP), 5);
				} else {
					World.sendSoundEffect(new Location(player.getLocation()), repelSound);
					final int helm = player.getEquipment().getId(EquipmentSlot.HELMET);
					player.sendMessage("Your " + (helm == 4551 ? "spiny helmet" : "slayer helmet") + " repels the wall beast!");
				}
			});
		});
		return true;
	}
}

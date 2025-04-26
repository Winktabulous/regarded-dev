package com.zenyte.plugins.object;

import com.google.common.eventbus.Subscribe;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.DefaultGson;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.Ladder;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.object.WorldObjectUtils;
import com.zenyte.plugins.dialogue.OptionDialogue;
import com.zenyte.plugins.events.ServerLaunchEvent;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Kris | 19. veebr 2018 : 17:21.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LadderOA implements ObjectAction {
	private static final Logger log = LoggerFactory.getLogger(LadderOA.class);
	public static final Animation CLIMB_UP = new Animation(828);
	public static final Animation CLIMB_DOWN = new Animation(827);

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final Ladder ladder = Ladder.LADDER_MAP.get(object.getPositionHash());
		switch (option.toLowerCase()) {
		case "climb": 
			if (ladder == null) {
				player.getDialogueManager().start(new OptionDialogue(player, "Which way do you want to go?", new String[] {"Climb up", "Climb down", "Cancel."}, new Runnable[] {() -> climb(player, object, new Location(player.getX(), player.getY(), object.getPlane() + 1), CLIMB_UP, null), () -> climb(player, object, new Location(player.getX(), player.getY(), object.getPlane() - 1), CLIMB_DOWN, null), null}));
			} else {
				climb(player, object, ladder.getTo(), ladder.getTo().getPlane() < player.getPlane() ? CLIMB_DOWN : CLIMB_UP, ladder);
			}
			break;
		case "climb up": 
		case "climb-up": 
			if (ladder == null) {
				climb(player, object, new Location(player.getX(), player.getY(), (player.getPlane() + 1) & 3), CLIMB_UP, null);
			} else {
				climb(player, object, ladder.getTo(), CLIMB_UP, ladder);
			}
			break;
		case "climb down": 
		case "climb-down": 
			if (ladder == null) {
				climb(player, object, new Location(player.getX(), player.getY(), (player.getPlane() - 1) & 3), CLIMB_DOWN, null);
			} else {
				climb(player, object, ladder.getTo(), CLIMB_DOWN, ladder);
			}
			break;
		}
	}

	private void climb(final Player player, final WorldObject originalObject, final Location tile, final Animation anim, Ladder ladder) {
		if (tile.withinDistance(originalObject, 10)) {
			final WorldObject object = World.getObjectWithType(new Location(originalObject.getX(), originalObject.getY(), tile.getPlane()), 10);
			if (object == null) {
				player.sendMessage("Looks like this ladder doesn't lead anywhere.");
				return;
			}
		}
		if (ladder == null) {
			final WorldObject obj = World.getObjectWithType(new Location(originalObject.getX(), originalObject.getY(), tile.getPlane()), originalObject.getType());
			if (obj == null || player.getPlane() == 3 && tile.getPlane() == 0 || player.getPlane() == 0 && tile.getPlane() == 3) {
				enlistInvalidLadder(originalObject);
				player.sendMessage("Undefined ladder detected; Submitting automatic bug report.");
				return;
			}
		}
		player.lock(2);
		player.setAnimation(anim);
		WorldTasksManager.schedule(() -> player.setLocation(tile));
	}

	private static final ObjectSet<String> invalidLadders = ObjectSets.synchronize(new ObjectOpenHashSet<>());

	private static final Path INVALID_LADDERS_PATH = Path.of("data", "invalid ladders.json");

	@Subscribe
	public static void onServerLaunch(@SuppressWarnings("unused") final ServerLaunchEvent event) {
		if (Files.notExists(INVALID_LADDERS_PATH)) {
			return; // File doesn't exist, no need to load
		}

		try (final BufferedReader reader = new BufferedReader(
				new FileReader(
						INVALID_LADDERS_PATH.toFile()
				)
		)) {
			String[] values = DefaultGson.getGson().fromJson(reader, String[].class);
			if (values != null) {
				List<String> list = Arrays.asList(values);
				invalidLadders.addAll(list);
			}
		} catch (final Exception e) {
			log.error("Failed to load invalid ladders", e);
		}
	}

	private static void enlistInvalidLadder(@NotNull final WorldObject ladder) {
		if (!WorldObjectUtils.isMapObject(ladder)) {
			return;
		}
		if (!invalidLadders.add(ladder.toString())) {
			return;
		}
		CoresManager.slowExecutor.execute(() -> {
			final String json = DefaultGson.getGson().toJson(invalidLadders);
			try (PrintWriter writer = new PrintWriter(new File("data/invalid ladders.json"))) {
				writer.write(json);
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	@Override
	public int getDelay() {
		return 1;
	}

	@Override
	public Object[] getObjects() {
		final Set<Object> list = new ObjectOpenHashSet<>(Ladder.VALUES.length);
		list.add("ladder");
		list.add("Bamboo ladder");
		for (final Ladder ladder : Ladder.VALUES) {
			list.add(ladder.getLadder().getId());
		}
		return list.toArray();
	}
}

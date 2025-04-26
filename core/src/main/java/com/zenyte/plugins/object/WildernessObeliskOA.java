package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.plugins.dialogue.SetObeliskDestinationD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kris | 7. apr 2018 : 3:17.59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 * profile</a>}
 */
public final class WildernessObeliskOA implements ObjectAction {

    public static final Location[] CENTER_OBELISK_TILES = new Location[] { new Location(3307, 3916, 0), new Location(3227, 3667, 0), new Location(3156, 3620, 0), new Location(3106, 3794, 0), new Location(3035, 3732, 0), new Location(2980, 3866, 0) };

    private static final int[][] OBELISK_OFFSETS = new int[][] { { -2, -2 }, { -2, 2 }, { 2, 2 }, { 2, -2 } };

    private static final Animation ANIM = new Animation(3945);

    private static final Graphics GFX = new Graphics(661);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Set Destination")) {
            if (!DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player)) {
                player.sendMessage("You need to complete the hard wilderness diary to do this.");
                return;
            }
            player.getDialogueManager().start(new SetObeliskDestinationD(player, object));
            return;
        }
        final int index = 14831 - object.getId();
        Location dest = null;
        if (option.equals("Teleport to Destination")) {
            if (!DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player)) {
                player.sendMessage("You need to complete the hard wilderness diary to do this.");
                return;
            }
            final Object obj = player.getTemporaryAttributes().get("ObeliskDestinationIndex");
            if (!(obj instanceof Integer)) {
                player.sendMessage("You need to select a destination first.");
                return;
            }
            dest = CENTER_OBELISK_TILES[(int) obj];
        } else {
            final ArrayList<Location> list = new ArrayList<Location>(Arrays.asList(CENTER_OBELISK_TILES));
            Collections.shuffle(list);
            for (final Location tile : list) {
                if (player.getLocation().withinDistance(tile, 10)) {
                    continue;
                }
                dest = tile;
                break;
            }
        }
        final Location tile = dest;
        for (int i = 0; i < OBELISK_OFFSETS.length; i++) {
            World.spawnObject(new WorldObject(14825, 10, 1, CENTER_OBELISK_TILES[index].transform(OBELISK_OFFSETS[i][0], OBELISK_OFFSETS[i][1], 0)));
        }
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            private final List<Player> players = new ArrayList<>();

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        for (int i = 0; i < OBELISK_OFFSETS.length; i++) {
                            World.spawnObject(new WorldObject(object.getId(), 10, 1, CENTER_OBELISK_TILES[index].transform(OBELISK_OFFSETS[i][0], OBELISK_OFFSETS[i][1], 0)));
                        }
                        break;
                    case 1:
                        {
                            final Location center = CENTER_OBELISK_TILES[index];
                            CharacterLoop.forEach(center, 1, Player.class, p -> {
                                if (p.isDead() || p.getVariables().getTime(TickVariable.TELEBLOCK) > 0 || !WildernessArea.isWithinWilderness(p.getX(), p.getY())) {
                                    return;
                                }
                                players.add(p);
                                p.setAnimation(ANIM);
                                p.setGraphics(GFX);
                                p.lock(2);
                            });
                            break;
                        }
                    case 2:
                        {
                            for (final Player p : players) {
                                if (p.isDead() || p.getVariables().getTime(TickVariable.TELEBLOCK) > 0 || !WildernessArea.isWithinWilderness(p.getX(), p.getY())) {
                                    continue;
                                }
                                p.setAnimation(Animation.STOP);
                                p.setLocation(tile.transform(Utils.random(-1, 1), Utils.random(-1, 1), 0));
                                p.sendMessage("Ancient magic teleports you to a place within the wilderness!");
                                p.getTemporaryAttributes().remove("ObeliskDestinationIndex");
                            }
                            stop();
                            break;
                        }
                }
            }
        }, 7, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.OBELISK_14826, ObjectId.OBELISK_14827, ObjectId.OBELISK_14828, ObjectId.OBELISK_14829, ObjectId.OBELISK_14830, ObjectId.OBELISK_14831 };
    }
}

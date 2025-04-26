package com.zenyte.game.content.godwars.objects;

import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.godwars.*;
import com.zenyte.utils.TextUtils;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Tommeh | 24-3-2019 | 14:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodwarsBossDoorObject implements ObjectAction {

    public enum BossDoor {
        //@formatter:off
        BANDOS(26503, BandosChamberArea.class),
        ARMADYL(26502, ArmadylChamberArea.class),
        SARADOMIN(26504, SaradominChamberArea.class),
        ZAMORAK(26505, ZamorakChamberArea.class),
        ANCIENT(42934, "com.near_reality.game.content.boss.nex.AncientChamberArea");
        //@formatter:on
        private static final List<BossDoor> values = Collections.unmodifiableList(Arrays.asList(values()));
        private final int objectId;
        private final Class<? extends GodwarsDungeonArea> clazz;
        private final String formattedName = TextUtils.capitalizeFirstCharacter(name().toLowerCase());

        BossDoor(int objectId, Class<? extends GodwarsDungeonArea> clazz) {
            this.objectId = objectId;
            this.clazz = clazz;
        }

        @SuppressWarnings("unchecked")
        BossDoor(int objectId, String clazz) {
            this.objectId = objectId;
            Class<? extends GodwarsDungeonArea> areaClass = null;
            try {
                areaClass = (Class<? extends GodwarsDungeonArea>) Class.forName(clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            this.clazz = areaClass;
        }

        public static List<BossDoor> getValues() {
            return values;
        }

        @Override
        public String toString() {
            return formattedName;
        }

        public int getObjectId() {
            return objectId;
        }

        public String getFormattedName() {
            return formattedName;
        }
    }

    private int calculateRequiredKillcount(@NotNull final Player player) {
        int requiredKillcount = 10;
        final MemberRank rank = player.getMemberRank();
        if (rank.equalToOrGreaterThan(MemberRank.UBER)) {
            requiredKillcount -= 10;
        } else if (rank.equalToOrGreaterThan(MemberRank.RESPECTED)) {
            requiredKillcount -= 8;
        } else if (rank.equalToOrGreaterThan(MemberRank.EXTREME)) {
            requiredKillcount -= 6;
        } else if (rank.equalToOrGreaterThan(MemberRank.EXPANSION)) {
            requiredKillcount -= 4;
        } else if (rank.equalToOrGreaterThan(MemberRank.PREMIUM)) {
            requiredKillcount -= 2;
        }
        return requiredKillcount;
    }

    private void notifyChamberSize(@NotNull final Player player, final int size) {
        player.sendMessage("There " + (size == 1 ? "is" : "are") + " " + size + " adventurer" + (size == 1 ? "" : "s") + " inside the chamber.");
    }

    public static final int getInstanceChamberCount(@NotNull final RegionArea area) {
        int count = 0;
        if (area instanceof GodwarsInstance) {
            final GodwarsInstance instance = (GodwarsInstance) area;
            final RSPolygon polygon = instance.chamberPolygon();
            for (final Player p : instance.getPlayers()) {
                if (polygon.contains(p.getLocation())) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean insideChamber(@NotNull final Player player, @NotNull final BossDoor door) {
        final RegionArea area = player.getArea();
        if (area instanceof GodwarsInstance) {
            return (((GodwarsInstance) area).chamberPolygon().contains(player.getLocation()));
        }
        final Class<? extends GodwarsDungeonArea> clazz = door.clazz;
        return clazz != null && GlobalAreaManager.getArea(clazz).getPlayers().contains(player);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final GodwarsBossDoorObject.BossDoor door = Objects.requireNonNull(CollectionUtils.findMatching(BossDoor.getValues(), v -> v.getObjectId() == object.getId()));
        if (option.equals("Peek")) {
            final RegionArea playerArea = player.getArea();
            final int count = playerArea instanceof GodwarsInstance
                    ? getInstanceChamberCount(playerArea)
                    : GlobalAreaManager.getArea(door.clazz).getPlayers().size();
            notifyChamberSize(player, count);
            return;
        }

        //EDGE: ancient door has two areas, safe and unsafe
        //      this door is entering and exiting the safe one, so we do not need
        //      to pay when leaving and actually can leave.
        boolean insideChamber = this.insideChamber(player, door);
        boolean ancientDoor = door == GodwarsBossDoorObject.BossDoor.ANCIENT;

        if (!ancientDoor && insideChamber) {
            player.sendMessage("You cannot leave the boss room through this side of the door!");
            return;
        }

        boolean canPass = true;
        if (!insideChamber) {
            canPass = this.pay(player, door);
        }

        if (canPass) {
            this.pass(player, object, door);
        }
    }

    private boolean pay(Player player, GodwarsBossDoorObject.BossDoor door) {

        if (player.getInventory().containsAnyOf(ItemId.ECUMENICAL_KEY))
            return player.getInventory().deleteItem(ItemId.ECUMENICAL_KEY, 1).getResult() == RequestResult.SUCCESS;

        final int requiredKillcount = calculateRequiredKillcount(player);
        final int killcount = player.getNumericAttribute(door.formattedName + "Kills").intValue();
        if (killcount < requiredKillcount && !player.getInventory().containsItem(ItemId.ECUMENICAL_KEY, 1)) {
            player.sendMessage("This door is locked by the power of " + door.formattedName + "! You will need to collect the essence of at least " + requiredKillcount + " of his followers before the door will open.");
            return false;
        }

        if (killcount >= requiredKillcount) {
            player.addAttribute(door.formattedName + "Kills", Math.max(0, killcount - requiredKillcount));
            GodwarsDungeonArea.refreshKillcount(player);
            player.sendMessage("The door devours the life-force of " + requiredKillcount + " followers of " + door.formattedName + " that you have slain.");
        } else {
            player.getInventory().deleteItem(ItemId.ECUMENICAL_KEY, 1);
            player.sendMessage("The door devours the ecumenical key.");
        }
        return true;
    }

    private void pass(Player player, WorldObject object, GodwarsBossDoorObject.BossDoor door) {
        object.setLocked(true);
        final WorldObject obj = new WorldObject(object);
        obj.setRotation((obj.getRotation() - 1) & 3);
        World.spawnGraphicalDoor(obj);
        player.lock();
        player.setRunSilent(2);
        final boolean horizontal = (object.getRotation() & 1) == 0;
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                switch (ticks++) {
                    case 0:
                        final int destinationX = horizontal ? (player.getX() + (player.getX() < object.getX() ? 2 : -2)) : (player.getX());
                        final int destinationY = !horizontal ? (player.getY() + (player.getY() < object.getY() ? 2 : -2)) : (player.getY());
                        player.addWalkSteps(destinationX, destinationY, 2, false);
                        break;
                    case 1:
                        player.unlock();
                        World.spawnGraphicalDoor(object);
                        break;
                    case 2:
                        object.setLocked(false);
                        stop();
                        break;
                }
            }
        }, 0, 1);
    }

    @Override
    public Object[] getObjects() {
        return Arrays.stream(BossDoor.values())
                .map(it -> (Object) it.objectId)
                .toArray();
    }
}

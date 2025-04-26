package com.zenyte.game.content.skills.construction.objects.achievementgallery;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.FurnitureData;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.constants.Furniture;
import com.zenyte.game.content.skills.construction.constants.FurnitureSpace;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.items.ItemDefinitions;

import java.util.List;

/**
 * @author Kris | 26. veebr 2018 : 19:50.10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BossLairDisplay implements ObjectInteraction, ItemOnObjectAction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BOSS_LAIR_DISPLAY, ObjectId.KRAKEN_DISPLAY, ObjectId.ZULRAH_DISPLAY, ObjectId.KALPHITE_QUEEN_DISPLAY, ObjectId.CERBERUS_DISPLAY, ObjectId.ABYSSAL_SIRE_DISPLAY, ObjectId.SKOTIZO_DISPLAY, 31689, ObjectId.VORKATH_DISPLAY_31978 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("jars")) {
            player.getDialogueManager().start(new JarDialogue(player, reference, object));
            return;
        } else if (option.equals("configure")) {
            player.getDialogueManager().start(new ConfigurationD(player, reference));
            return;
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        final int id = item.getId();
        if (!ConfigurationD.MAP.containsKey(id)) {
            player.sendMessage("You can only store jars with boss souls in them here.");
            return;
        }
        final List<Integer> jars = player.getConstruction().getDisplayJars();
        if (!jars.contains(id)) {
            if (!player.getInventory().containsItem(id, 1)) {
                player.sendMessage("You need a " + ItemDefinitions.get(id).getName().toLowerCase() + " to enqueue it to the display.");
                return;
            }
            jars.add(id);
            player.getInventory().deleteItem(id, 1);
            player.sendMessage("You enqueue the " + ItemDefinitions.get(id).getName().toLowerCase() + " to the display.");
            return;
        } else
            player.sendMessage("The boss display case already contains a " + ItemDefinitions.get(id).getName().toLowerCase() + ".");
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 12007, 12885, 12936, 13245, 13277, 19701, 21745, 22106 };
    }

    private static final class ConfigurationD extends Dialogue {

        public ConfigurationD(Player player, final RoomReference reference) {
            super(player);
            this.reference = reference;
        }

        public static final ImmutableMap<Integer, Integer> MAP = ImmutableMap.<Integer, Integer>builder().put(12007, 0).put(12885, 1).put(12936, 2).put(13245, 3).put(13277, 4).put(19701, 5).put(21745, 6).put(22106, 7).build();

        private final List<Integer> jars = player.getConstruction().getDisplayJars();

        private final RoomReference reference;

        @Override
        public void buildDialogue() {
            options("Select a jar to display.", jars.contains(12007) ? "Jar of dirt." : "<str>Jar of dirt.", jars.contains(12885) ? "Jar of sand." : "<str>Jar of sand.", jars.contains(12936) ? "Jar of swamp." : "<str>Jar of swamp.", jars.contains(13245) ? "Jar of souls." : "<str>Jar of souls.", "Next selection.").onOptionOne(() -> {
                handleJar(12007);
                finish();
            }).onOptionTwo(() -> {
                handleJar(12885);
                finish();
            }).onOptionThree(() -> {
                handleJar(12936);
                finish();
            }).onOptionFour(() -> {
                handleJar(13245);
                finish();
            }).onOptionFive(() -> setKey(5));
            options(5, "Select a jar to display.", jars.contains(13277) ? "Jar of miasma." : "<str>Jar of miasma.", jars.contains(19701) ? "Jar of darkness." : "<str>Jar of darkness.", jars.contains(21745) ? "Jar of stone." : "<str>Jar of stone.", jars.contains(22106) ? "Jar of decay." : "<str>Jar of decay.", "Previous selection.").onOptionOne(() -> {
                handleJar(13277);
                finish();
            }).onOptionTwo(() -> {
                handleJar(19701);
                finish();
            }).onOptionThree(() -> {
                handleJar(21745);
                finish();
            }).onOptionFour(() -> {
                handleJar(22106);
                finish();
            }).onOptionFive(() -> setKey(1));
        }

        private void handleJar(final int id) {
            if (!player.getConstruction().getDisplayJars().contains(id)) {
                player.sendMessage("You haven't added that jar to the display case yet.");
                return;
            }
            final Integer val = MAP.get(id);
            if (val == null)
                return;
            final FurnitureData data = reference.getFurniture(FurnitureSpace.BOSS_LAIR_SPACE);
            if (data == null)
                return;
            final int index = Furniture.KRAKEN_DISPLAY.ordinal() + val;
            if (index < 0 || index >= Furniture.VALUES.length)
                return;
            final Furniture furn = Furniture.VALUES[index];
            if (furn == null || !furn.toString().contains("display"))
                return;
            if (furn == data.getFurniture()) {
                player.sendMessage("The display is already of that style.");
                return;
            }
            data.setFurniture(furn);
            final int x = (reference.getX() * 8 - (player.getConstruction().getYardOffset() * 8)) % 64;
            final int y = (reference.getY() * 8 - (player.getConstruction().getYardOffset() * 8)) % 64;
            final Location tile = new Location(player.getConstruction().getChunkX() * 8 + x + data.getLocation().getX(), player.getConstruction().getChunkY() * 8 + y + data.getLocation().getY(), player.getPlane());
            World.spawnObject(new WorldObject(data.getFurniture().getObjectId(), 10, data.getRotation(), tile));
        }
    }

    private static final class JarDialogue extends Dialogue {

        public JarDialogue(final Player player, final RoomReference reference, final WorldObject object) {
            super(player);
            this.reference = reference;
            this.object = object;
        }

        private static final ImmutableMap<Integer, Integer> MAP = ImmutableMap.<Integer, Integer>builder().put(12007, 29158).put(12936, 29159).put(12885, 29160).put(13245, 29161).put(13277, 29162).put(19701, 29163).put(21745, 31689).put(22106, 31978).build();

        private final RoomReference reference;

        private final WorldObject object;

        private final List<Integer> jars = player.getConstruction().getDisplayJars();

        @Override
        public void buildDialogue() {
            options("Select a jar.", jars.contains(12007) ? "Jar of dirt." : "<str>Jar of dirt.", jars.contains(12885) ? "Jar of sand." : "<str>Jar of sand.", jars.contains(12936) ? "Jar of swamp." : "<str>Jar of swamp.", jars.contains(13245) ? "Jar of souls." : "<str>Jar of souls.", "Next selection.").onOptionOne(() -> {
                handleJar(12007);
                finish();
            }).onOptionTwo(() -> {
                handleJar(12885);
                finish();
            }).onOptionThree(() -> {
                handleJar(12936);
                finish();
            }).onOptionFour(() -> {
                handleJar(13245);
                finish();
            }).onOptionFive(() -> setKey(5));
            options(5, "Select a jar.", jars.contains(13277) ? "Jar of miasma." : "<str>Jar of miasma.", jars.contains(19701) ? "Jar of darkness." : "<str>Jar of darkness.", jars.contains(21745) ? "Jar of stone." : "<str>Jar of stone.", jars.contains(22106) ? "Jar of decay." : "<str>Jar of decay.", "Previous selection.").onOptionOne(() -> {
                handleJar(13277);
                finish();
            }).onOptionTwo(() -> {
                handleJar(19701);
                finish();
            }).onOptionThree(() -> {
                handleJar(21745);
                finish();
            }).onOptionFour(() -> {
                handleJar(22106);
                finish();
            }).onOptionFive(() -> setKey(1));
        }

        private void handleJar(final int id) {
            if (!jars.contains(id)) {
                if (!player.getInventory().containsItem(id, 1)) {
                    player.sendMessage("You need a " + ItemDefinitions.get(id).getName().toLowerCase() + " to enqueue it to the display.");
                    return;
                }
                jars.add(id);
                player.getInventory().deleteItem(id, 1);
                player.sendMessage("You enqueue the " + ItemDefinitions.get(id).getName().toLowerCase() + " to the display.");
                return;
            }
            FurnitureData data = null;
            for (FurnitureData v : reference.getFurnitureData()) {
                final int ordinal = v.getFurniture().ordinal();
                if (ordinal >= Furniture.KRAKEN_DISPLAY.ordinal() && ordinal <= Furniture.VORKATH_DISPLAY.ordinal()) {
                    data = v;
                    break;
                }
            }
            if (data != null) {
                final Integer objectId = MAP.get(id);
                if (objectId == null)
                    return;
                final Furniture currentDisplay = Furniture.MAP.get(objectId);
                if (currentDisplay == null)
                    return;
                if (currentDisplay == data.getFurniture()) {
                    data.setFurniture(Furniture.BOSS_LAIR_DISPLAY);
                    World.spawnObject(new WorldObject(Furniture.BOSS_LAIR_DISPLAY.getObjectId(), object.getType(), object.getRotation(), object));
                }
            }
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some more free space to remove the jar from the display.");
                return;
            }
            player.sendMessage("You remove the " + ItemDefinitions.get(id).getName().toLowerCase() + " from the display.");
            jars.remove((Integer) id);
            player.getInventory().addItem(id, 1);
        }
    }
}

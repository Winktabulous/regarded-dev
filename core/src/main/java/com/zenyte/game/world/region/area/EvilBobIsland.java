package com.zenyte.game.world.region.area;

import com.zenyte.game.content.skills.cooking.CookingDefinitions;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraResetAction;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.TeleportPlugin;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static com.zenyte.game.GameInterface.EXPERIENCE_LAMP;

/**
 * @author Kris | 25/06/2019 18:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EvilBobIsland extends PolygonRegionArea implements DeathPlugin, TeleportPlugin, RandomEventRestrictionPlugin {

    public static final class BobServantNPC extends NPCPlugin {

        @Override
        public void handle() {
            bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    if (player.getAttributes().get("evil bob complete") != null) {
                        npc("Use the portal while you can!");
                        return;
                    }
                    player("I need help, I've been kidnapped by an evil cat!");
                    npc("Meow! Errr... I c-c-c-an't help you... He'll kill us all!");
                    player("Now you listen to me! He's just a little cat! There must be something I can do!");
                    npc("F-f-f-fish... give him the f-f-f-fish he likes and he might f-f-f-fall asleep.");
                    npc("Look... over t-t-there! That fishing spot c-c-contains the f-f-f-fish he likes.").executeAction(() -> {
                        final EvilBobIsland.FishSpot direction = getDirection(player);
                        player.getCutsceneManager().play(new Cutscene() {

                            @Override
                            public void build() {
                                addActions(0, () -> player.lock(), new CameraPositionAction(player, direction.cameraPosition, 800, 10, 20), new CameraLookAction(player, direction.cameraLook, 0, 10, 20));
                                addActions(5, () -> player.unlock(), new CameraResetAction(player));
                            }
                        });
                    });
                }
            }));
        }

        @Override
        public int[] getNPCs() {
            return new int[] { 393 };
        }
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2496, 4805 }, { 2496, 4748 }, { 2560, 4748 }, { 2560, 4800 }, { 2553, 4800 }, { 2553, 4805 } }) };
    }

    public static final class BobTheCat extends NPCPlugin {

        private static final Dialogue getDialogue(final Player player, final NPC bob) {
            return new Dialogue(player, bob) {

                @Override
                public void buildDialogue() {
                    player("Huh?");
                    player("Where am I?");
                    npc("On my island.");
                    player("Who brought me here?");
                    npc("That would be telling.");
                    player("Take me to your leader!");
                    npc("I am your leader, you are but a slave.");
                    player("I am not a slave, I am a free man!");
                    npc("Ah-ha-ha-ha-ha-ha!");
                }
            };
        }

        @Override
        public void handle() {
            bind("Talk-to", (player, npc) -> player.getDialogueManager().start(getDialogue(player, npc)));
        }

        @Override
        public int[] getNPCs() {
            return new int[] { 391 };
        }
    }

    public static final class BobFishingSpot implements ObjectAction {

        @Override
        public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
            if (player.getAttributes().get("evil bob complete") != null) {
                player.getDialogueManager().start(new PlainChat(player, "You have already fed the cat - RUN!"));
                return;
            }
            player.getActionManager().setAction(new Action() {

                private final Animation animation = new Animation(621);

                private int ticks;

                @Override
                public boolean start() {
                    if (!player.getInventory().containsItem(303, 1)) {
                        player.getDialogueManager().start(new ItemChat(player, new Item(303, 1), "You need a small fishing net to catch the fish. Perhaps you can find them on the island."));
                        return false;
                    }
                    if (!player.getInventory().hasFreeSlots()) {
                        player.sendMessage("You need some free inventory space to catch fish.");
                        return false;
                    }
                    player.sendMessage("You cast out your net...");
                    delay(8);
                    return true;
                }

                @Override
                public boolean process() {
                    if (ticks++ % 4 == 0) {
                        player.setAnimation(animation);
                    }
                    if (!player.getInventory().hasFreeSlots()) {
                        player.sendMessage("You need some free inventory space to catch fish.");
                        return false;
                    }
                    return true;
                }

                @Override
                public int processWithDelay() {
                    final EvilBobIsland.FishSpot direction = getDirection(player);
                    final boolean correctSpot = direction.predicate.test(player);
                    final Item fish = correctSpot ? new Item(6202) : new Item(6206);
                    player.getInventory().addOrDrop(fish);
                    player.getDialogueManager().start(new ItemChat(player, fish, "You catch a... what is this?? Is " + "this a fish?? And... it's cooked already?"));
                    return -1;
                }

                @Override
                public void stop() {
                    delay(2);
                }
            });
        }

        @Override
        public Object[] getObjects() {
            return new Object[] { ObjectId.FISHING_SPOT_23114 };
        }
    }

    public static final class FishlikeThingOnBob implements ItemOnNPCAction {

        @Override
        public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
            final boolean correct = item.getId() == 6200;
            player.getInventory().deleteItem(item);
            if (!correct) {
                player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        npc("What was that? That was absolutely disgusting!");
                        npc("Don't you know what kind of fish I like? Talk to my other servants for advice.");
                    }
                });
                return;
            }
            player.getAttributes().put("evil bob complete", true);
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Mmm, mmm...that's delicious.");
                    npc("Now, let me take...a little...catnap.").executeAction(() -> npc.setForceTalk(new ForceTalk("ZZZzzz")));
                }
            });
        }

        @Override
        public Object[] getItems() {
            return new Object[] { 6200, 6202, 6204, 6206 };
        }

        @Override
        public Object[] getObjects() {
            return new Object[] { 391 };
        }
    }

    public static final class BobPortal implements ObjectAction {

        @Override
        public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
            if (player.getAttributes().get("evil bob complete") == null) {
                World.findNPC(391, player.getLocation(), 20).ifPresent(bob -> player.getDialogueManager().start(new Dialogue(player, bob) {

                    @Override
                    public void buildDialogue() {
                        npc("You're going nowhere, human!");
                    }
                }));
                return;
            }
            player.lock();
            player.addWalkSteps(object.getX(), object.getY(), 1, false);
            final boolean lamp = player.getAttributes().get("observing random event") == null;
            WorldTasksManager.schedule(() -> {
                World.findNPC(391, player.getLocation(), 20).ifPresent(bob -> player.setFaceLocation(bob.getLocation()));
                player.setAnimation(Emote.RASPBERRY.getAnimation());
                WorldTasksManager.schedule(() -> new Teleport() {

                    @Override
                    public TeleportType getType() {
                        return TeleportType.RANDOM_EVENT_TELEPORT;
                    }

                    @Override
                    public Location getDestination() {
                        return getEntryLocation(player);
                    }

                    @Override
                    public int getLevel() {
                        return 0;
                    }

                    @Override
                    public double getExperience() {
                        return 0;
                    }

                    @Override
                    public void onArrival(final Player player) {
                        if (lamp) {
                            WorldTasksManager.schedule(() -> {
                                player.getInventory().addOrDrop(new Item(7498));
                                player.getDialogueManager().start(new ItemChat(player, new Item(7498), "You find an antique lamp on your way off the island."));
                            });
                        }
                    }

                    @Override
                    public int getRandomizationDistance() {
                        return 0;
                    }

                    @Override
                    public Item[] getRunes() {
                        return new Item[0];
                    }

                    @Override
                    public int getWildernessLevel() {
                        return 0;
                    }

                    @Override
                    public boolean isCombatRestricted() {
                        return false;
                    }
                }.teleport(player), 4);
            });
        }

        @Override
        public Object[] getObjects() {
            return new Object[] { ObjectId.PORTAL_23115 };
        }
    }

    public static final class AntiqueLamp extends ItemPlugin {

        @Override
        public void handle() {
            bind("Rub", (player, item, container, slotId) -> {
                final Object[] args = new Object[] { 750, 1, slotId, item };
                player.getTemporaryAttributes().put("experience_lamp_info", args);
                EXPERIENCE_LAMP.open(player);
            });
        }

        @Override
        public int[] getItems() {
            return new int[] { 7498 };
        }
    }

    public static final class FishlikeThingOnCookingPot implements ItemOnObjectAction {

        @Override
        public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
            if (item.getId() == 6200 || item.getId() == 6204) {
                player.getDialogueManager().start(new ItemChat(player, item, "The fishlike thing is already uncooked!"));
                return;
            }
            player.getActionManager().setAction(new Action() {

                @Override
                public boolean start() {
                    player.setAnimation(CookingDefinitions.STOVE);
                    delay(2);
                    player.lock(3);
                    return true;
                }

                @Override
                public boolean process() {
                    return true;
                }

                @Override
                public int processWithDelay() {
                    player.getInventory().deleteItem(item);
                    player.getInventory().addOrDrop(new Item(item.getId() - 2, 1));
                    return -1;
                }
            });
        }

        @Override
        public Object[] getItems() {
            return new Object[] { 6200, 6202, 6204, 6206 };
        }

        @Override
        public Object[] getObjects() {
            return new Object[] { ObjectId.UNCOOKING_POT };
        }
    }

    private enum FishSpot {

        NORTH(p -> p.getY() >= 4788, new Location(2526, 4780, 0), new Location(2526, 4790, 0)), WEST(p -> p.getX() <= 2513, new Location(2522, 4777, 0), new Location(2512, 4777, 0)), EAST(p -> p.getX() >= 2540, new Location(2530, 4777, 0), new Location(2539, 4777, 0)), SOUTH(p -> p.getY() <= 4767, new Location(2525, 4774, 0), new Location(2525, 4767, 0));

        private static final FishSpot[] values = values();

        private final Predicate<Player> predicate;

        private final Location cameraPosition;

        private final Location cameraLook;

        FishSpot(Predicate<Player> predicate, Location cameraPosition, Location cameraLook) {
            this.predicate = predicate;
            this.cameraPosition = cameraPosition;
            this.cameraLook = cameraLook;
        }
    }

    private static final Location getEntryLocation(@NotNull final Player player) {
        final int object = player.getNumericAttribute("evil bob before entering position").intValue();
        if (object == 0) {
            return new Location(3087, 3487, 0);
        }
        return new Location(object);
    }

    public static final void teleport(@NotNull final Player player) {
        TeleportCollection.EVIL_BOB_ISLAND.teleport(player);
        player.addAttribute("evil bob before entering position", player.getLocation().getPositionHash());
        player.getAttributes().remove("evil bob fish direction");
        // Sets and retrieves the next random direction object.
        getDirection(player);
    }

    @Override
    public void enter(final Player player) {
        player.sendMessage("Speak to the servant in order to complete the random event.");
        World.findNPC(391, player.getLocation(), 20).ifPresent(bob -> {
            player.setFaceLocation(bob.getLocation());
            player.getDialogueManager().start(BobTheCat.getDialogue(player, bob));
        });
    }

    private static final FishSpot getDirection(@NotNull final Player player) {
        FishSpot direction = Utils.getRandomElement(FishSpot.values);
        final Object attribute = player.getAttributes().get("evil bob fish direction");
        if (attribute == null) {
            player.addAttribute("evil bob fish direction", direction.toString());
        } else {
            try {
                direction = FishSpot.valueOf(attribute.toString());
            } catch (Exception ignored) {
            }
        }
        return direction;
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        player.getAttributes().remove("evil bob fish direction");
        player.getAttributes().remove("evil bob complete");
        player.getAttributes().remove("evil bob before entering position");

        player.setLocation(getEntryLocation(player));

        if (player.getAttributes().remove("observing random event") == null) {
            player.getAttributes().put("last random event", System.currentTimeMillis());
        }

        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null || !(item.getId() >= 6200 && item.getId() <= 6206)) {
                continue;
            }
            inventory.deleteItem(item);
        }
    }



    @Override
    public String name() {
        return "Evil Bob's Island";
    }

    @Override
    public boolean isSafe() {
        return true;
    }

    @Override
    public String getDeathInformation() {
        return "You will not lose any items if you die on the random event islands.";
    }

    @Override
    public Location getRespawnLocation() {
        return new Location(2526, 4778, 0);
    }

    @Override
    public boolean canTeleport(final Player player, final Teleport teleport) {
        World.findNPC(391, player.getLocation(), 20).ifPresent(bob -> player.getDialogueManager().start(new Dialogue(player, bob) {

            @Override
            public void buildDialogue() {
                npc("You're going nowhere, human!");
            }
        }));
        return false;
    }
}

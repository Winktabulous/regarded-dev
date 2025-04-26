package com.zenyte.game.content.boss.wildernessbosses;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cresinkel
 */
public final class WildernessBossEntrance implements ObjectAction {

    public enum Caves {
        VENENATIS_DUNGEON_ENTRANCE(47077, new Location(3321, 3794, 0), new Location(3424, 10212, 2), new Location(3423, 10203, 2), "venenatis"),
        SPINDEL_DUNGEON_ENTRANCE(47078, new Location(3183, 3744, 0), new Location(1632, 11556, 2), new Location(1631, 11546, 2), "spindel"),
        CALLISTO_DUNGEON_ENTRANCE(47140, new Location(3291, 3849, 0), new Location(3359, 10334, 0), new Location(3359, 10327, 0), "callisto"),
        ARTIO_DUNGEON_ENTRANCE(47141, new Location(3115, 3676, 0), new Location(1759, 11550, 0), new Location(1759, 11542, 0), "artio"),
        VETION_DUNGEON_ENTRANCE(46995, new Location(3221, 3787, 0), new Location(3295, 10202, 1), new Location(3295, 10202, 1), "vet'ion"),
        CALVARION_DUNGEON_ENTRANCE(46996, new Location(3180, 3683, 0), new Location(1887, 11546, 1), new Location(1887, 11546, 1), "calvar'ion"),
        ESCAPE_CAVES_ENTRANCE_1(47175, new Location(3282, 3774, 0), new Location(3359, 10246, 0), null, ""),
        ESCAPE_CAVES_ENTRANCE_2(47175, new Location(3320, 3830, 0), new Location(3381, 10286, 0), null, ""),
        ESCAPE_CAVES_ENTRANCE_3(47175, new Location(3260, 3832, 0), new Location(3338, 10286, 0), null, ""),
        ESCAPE_CAVES_ENTRANCE_4(47175, new Location(3284, 3807, 0), new Location(3360, 10273, 0), null, "");

        private final int id;
        private final Location object;
        private final Location exitLocation;
        private final Location roomCenter;
        private final String bossName;

        Caves(final int id, final Location object, final Location exitLocation, final Location roomCenter, final String bossName) {
            this.id = id;
            this.object = object;
            this.exitLocation = exitLocation;
            this.roomCenter = roomCenter;
            this.bossName = bossName;
        }

        public Location getExitLocation() {
            return this.exitLocation;
        }

        public Location getObject() {
            return this.object;
        }

        public Location getRoomCenter() {
            return this.roomCenter;
        }

        public String getBossName() {
            return this.bossName;
        }

        public int getId() {
            return this.id;
        }

        public static Caves get(int id, Location object) {
            for (Caves cave : VALUES) {
                if (cave.getId() == id && cave.getObject().equals(object)) {
                    return cave;
                }
            }
            return null;
        }

        public static final Caves[] VALUES = values();
        public static final Int2ObjectMap<Caves> entries = new Int2ObjectOpenHashMap<>(VALUES.length);

        static {
            for (final Caves entry : VALUES) {
                entries.put(entry.getId(), entry);
            }
        }
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final int cost = 50000 - (player.getNumericAttribute("wildernessBossFeeKc").intValue() * 10000);
        if (option.equals("Jump-Down") || option.equals("Enter")) {
            if (player.getBooleanAttribute("paidWildernessBossFee")) {
                teleport(player, object);
                return;
            }
            if (!(player.getInventory().containsItem(ItemId.COINS_995, cost) || player.getBank().containsItem(new Item(ItemId.COINS_995, cost)))) {
                player.sendMessage(Colour.RED.wrap("You need " + cost + " coins in your inventory or bank to pay the entry fee."));
                return;
            }
            ArrayList<String> options = new ArrayList<>(3);
            options.add("Yes.");
            if (!player.getBooleanAttribute("wildyBossFeeWarning")) {
                options.add("Yes, and don't ask again.");
            }
            options.add("No.");
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(new Item(ItemId.COINS_995, 10000), "You need to pay a " + cost + " coin fee to enter. This can be taken from your inventory or bank.");
                    options("Pay " + cost + " coins to enter?", options.toArray(new String[0]))
                            .onOptionOne(() -> {
                                payAndTeleport(player, object, cost);
                            })
                            .onOptionTwo(() -> {
                                if (options.size() > 2) {
                                    payAndTeleport(player, object, cost);
                                    player.putBooleanAttribute("wildyBossFeeWarning", true);
                                    player.sendMessage(Colour.RED.wrap("You will not be asked again about paying the entry fee."));
                                }
                            });
                }
            });
        } else if (option.equals("Peek")) {
            final int kc = player.getNotificationSettings().getKillcount(Caves.get(object.getId(), object.getLocation()).getBossName());
            if (kc < 20) {
                player.sendMessage("You don't know this cave well enough to glean any useful information. You feel that you'll be more succesful once you've killed whatever is inside, " + (20 - kc) + " times.");
                return;
            }
            final int count = CharacterLoop.find(Caves.get(object.getId(), object.getLocation()).getRoomCenter(), 25, Player.class, __ -> true).size();
            if (count > 0) {
                player.sendMessage("You peek into the darkness and can make out some movement. There is activity inside.");
            } else {
                player.sendMessage("You peek into the darkness and can make out no movement. There is no activity inside.");
            }
        } else if (option.equals("Check-Fee")) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(new Item(ItemId.COINS_995, 10000), "The base entry fee for these caves is 50,000 coins, which "
                            + (player.getBooleanAttribute("paidWildernessBossFee") ? "you have paid." : "can be paid from your inventory or bank.")
                            + " Killing wilderness bosses will reduce the fee by 10,000."
                            + " You have "
                            + (player.getNumericAttribute("wildernessBossFeeKc").intValue() > 0 ? "a " + cost :
                            "no"
                            + " discount towards your next fee."));
                }
            });
        }
    }

    private void payAndTeleport(Player player, WorldObject object, int cost) {
        teleport(player, object);
        if (player.getInventory().containsItem(ItemId.COINS_995, cost)) {
            player.getInventory().deleteItem(ItemId.COINS_995, cost);
        } else {
            player.sendMessage(Colour.RED.wrap("You enter the cave and " + cost + " coins are taken from your bank to pay the entry fee."));
            player.getBank().remove(new Item(ItemId.COINS_995, cost));
        }
        player.putBooleanAttribute("paidWildernessBossFee", true);
    }

    private void teleport(Player player, WorldObject object) {
        player.setAnimation(Animation.STOP);
        player.lock();
        player.faceObject(object);
        player.setAnimation(new Animation(object.getId() == 47175 ? 2796 : 7041));
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(Caves.get(object.getId(), object.getLocation()).getExitLocation());
            player.setAnimation(new Animation(object.getId() == 47175 ? -1 : 7041));
            player.unlock();
        }, 0);
    }

    @Override
    public Object[] getObjects() {
        final List<Object> list = new ArrayList<>();
        for (Caves entry : Caves.VALUES) if (!list.contains(entry.getId())) list.add(entry.getId());
        return list.toArray(new Object[0]);
    }
}

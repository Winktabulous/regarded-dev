package com.zenyte.game.content.stars;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Andys1814
 */
public final class StarEmblem extends ItemPlugin {

    @Override
    public void handle() {
        bind("locate", (player, item, container, slotId) -> {
            player.getDialogueManager().start(new PlainChat(player, "You rub the emblem and wait for a response...", false));
            WorldTasksManager.schedule(() -> {
                if (ShootingStars.getCurrent() != null) {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            item(new Item(32082), "With the help of the emblem, you discover that there is currently a shooting star crashed <col=2980B9>" + ShootingStars.getCurrent().getStarLocation().getLocation() + "</col>.");
                        }
                    });
                    return;
                }

                if (ShootingStars.getSpawn() != null) {
//                    String time = Utils.getTimeTicks(ShootingStars.getSpawn().getTicks(), true);
                    String time = Utils.ticksToTime(ShootingStars.getSpawn().getTicks());
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            item(new Item(32082), "The emblem helps you predict that the next shooting star will land <col=2980B9>" + ShootingStars.getSpawn().getLocation().getLocation() + "</col> in <col=2980B9>" + time + "</col>.");
                        }
                    });
                    return;
                }

                player.getDialogueManager().start(new PlainChat(player, "The emblem can not be of much help. Try again later and you may have better luck."));
            }, 3);
        });

        bind("teleport", (player, item, container, slotId) -> {
            player.getDialogueManager().start(new PlainChat(player, "You rub the emblem and wait for a response...", false));
            WorldTasksManager.schedule(() -> {
                if (ShootingStars.getCurrent() != null) {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            ShootingStarLocation location = ShootingStars.getCurrent().getStarLocation();
                            final Teleport teleport = new Teleport() {
                                @Override
                                public TeleportType getType() {
                                    return TeleportType.NEAR_REALITY_PORTAL_TELEPORT;
                                }

                                @Override
                                public Location getDestination() {
                                    return new Location(location.getX() - 1, location.getY(), location.getZ());
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
                                public int getRandomizationDistance() {
                                    return 0;
                                }

                                @Override
                                public Item[] getRunes() {
                                    return null;
                                }

                                @Override
                                public int getWildernessLevel() {
                                    return WILDERNESS_LEVEL;
                                }

                                @Override
                                public boolean isCombatRestricted() {
                                    return true;
                                }
                            };
                            teleport.teleport(player);
                        }
                    });
                    return;
                }

                if (ShootingStars.getSpawn() != null) {
                    player.getDialogueManager().start(new PlainChat(player, "The emblem can not be of much help - perhaps use it to locate the next star before trying to teleport."));
                    return;
                }

                player.getDialogueManager().start(new PlainChat(player, "The emblem can not be of much help. Try again later and you may have better luck."));
            }, 3);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { 32082 };
    }

}

package com.zenyte.game.content.chambersofxeric.npc;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.room.ResourcesRoom;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.dialogue.ItemChat;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/07/2019 08:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Bat extends RaidNPC<ResourcesRoom> {

    public Bat(final Raid raid, final ResourcesRoom room, final int id, final Location tile) {
        super(raid, room, id, tile);
    }

    @Override
    public int getRespawnDelay() {
        return 2;
    }

    @Override
    public void setRespawnTask() {
        if (!isFinished()) {
            reset();
            finish();
        }
        WorldTasksManager.schedule(this::spawn, getRespawnDelay());
    }

    public static final class BatPlugin extends NPCPlugin {

        private static final SoundEffect attempting = new SoundEffect(2623);

        private static final SoundEffect caught = new SoundEffect(293);

        @Override
        public void handle() {
            bind("Catch", new OptionHandler() {

                @Override
                public void handle(final Player player, final NPC npc) {
                    player.getActionManager().setAction(new Action() {

                        private BatType bat;

                        private boolean success;

                        @Override
                        public boolean start() {
                            if (player.getEquipment().getId(EquipmentSlot.WEAPON) != 10010) {
                                player.getDialogueManager().start(new ItemChat(player, new Item(10010), "You need a butterfly net to catch these bats."));
                                return false;
                            }
                            if ((bat = CollectionUtils.findMatching(BatType.values, bat -> bat.id == npc.getId())) == null) {
                                throw new IllegalStateException();
                            }
                            if (player.getSkills().getLevel(SkillConstants.HUNTER) < bat.level) {
                                player.sendMessage("You need a Hunter level of at least " + bat.level + " to catch this bat.");
                                return false;
                            }
                            if (!player.getInventory().hasFreeSlots()) {
                                player.sendMessage("You need some free inventory space to catch the bat.");
                                return false;
                            }
                            this.success = success(player);
                            player.sendSound(attempting);
                            player.setAnimation(new Animation(success ? 6606 : 6605));
                            if (success) {
                                npc.setCantInteract(true);
                            }
                            delay(1);
                            return true;
                        }

                        private boolean success(@NotNull final Player player) {
                            final int level = player.getSkills().getLevel(SkillConstants.HUNTER);
                            final double n = Math.floor((306.0F * (level - 1.0F)) / 98.0F) - 78;
                            final double chance = n / 255.0F;
                            return Utils.randomDouble() < chance;
                        }

                        @Override
                        public boolean process() {
                            return true;
                        }

                        @Override
                        public int processWithDelay() {
                            if (!success) {
                                player.sendMessage("You fail to catch the " + npc.getName(player).toLowerCase() + ".");
                                return -1;
                            }
                            player.sendSound(caught);
                            player.sendMessage("You successfully catch the " + npc.getName(player).toLowerCase() + ".");
                            player.getInventory().addItem(new Item(bat.itemId));
                            player.getSkills().addXp(SkillConstants.HUNTER, bat.experience);
                            npc.setRespawnTask();
                            if (success) {
                                npc.setCantInteract(false);
                            }
                            return -1;
                        }
                    });
                }

                @Override
                public void execute(final Player player, final NPC npc) {
                    player.stopAll();
                    player.setFaceEntity(npc);
                    handle(player, npc);
                }
            });
        }

        @Override
        public int[] getNPCs() {
            return new int[] { NpcId.GUANIC_BAT, NpcId.PRAEL_BAT, NpcId.GIRAL_BAT, NpcId.PHLUXIA_BAT, NpcId.KRYKET_BAT, NpcId.MURNG_BAT, NpcId.PSYKK_BAT };
        }
    }

    public enum BatType {

        GUANIC(7587, 20870, 1, 5),
        PRAEL(7588, 20872, 15, 9),
        GIRAL(7589, 20874, 30, 13),
        PHLUXIA(7590, 20876, 45, 17),
        KRYKET(7591, 20878, 60, 21),
        MURNG(7592, 20880, 75, 25),
        PSYKK(7593, 20882, 90, 29);

        private final int id;

        private final int itemId;

        private final int level;

        private final int experience;

        public static final BatType[] values = values();

        BatType(int id, int itemId, int level, int experience) {
            this.id = id;
            this.itemId = itemId;
            this.level = level;
            this.experience = experience;
        }

        public int getId() {
            return id;
        }

        public int getItemId() {
            return itemId;
        }

        public int getLevel() {
            return level;
        }

        public int getExperience() {
            return experience;
        }
    }
}

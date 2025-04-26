package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.EasterConstants.EasterItem;
import com.zenyte.game.content.event.easter2020.SplittingHeirs;
import com.zenyte.game.content.event.easter2020.Stage;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.dialogue.PlayerChat;

/**
 * @author Kris | 09/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class EggPaintingMachine extends NPCPlugin {

    private static final Location westernPaintingMachine = new Location(2197, 4361, 0);

    private static final Location easternPaintingMachine = new Location(2203, 4361, 0);

    @Override
    public void handle() {
        bind("Look-at", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                // Verified on OSRS; Starts same sequence as using item on it.
                final Stage progress = SplittingHeirs.getProgress(player);
                if (progress != Stage.GATHER_IMPLINGS) {
                    player.getDialogueManager().start(new PlayerChat(player, progress.ordinal() < Stage.GATHER_IMPLINGS.ordinal() ? "You look at the enormous machine and wonder why it isn't yet working." : "The machine is properly working as expected!"));
                    return;
                }
                if (player.getInventory().getAmountOf(EasterItem.EASTER_IMPLING_JAR.getItemId()) < 5) {
                    player.getDialogueManager().start(new PlayerChat(player, "I should catch all the implings before returning them."));
                    return;
                }
                final Item item = new Item(EasterItem.EASTER_IMPLING_JAR.getItemId());
                player.getDialogueManager().start(new Dialogue(player) {

                    @Override
                    public void buildDialogue() {
                        player("Here we go, little implings. Time to get back to work.");
                        item(item, "Who will supervise us? No one cares, it's why we left - or tried to!");
                        player("Don't worry, I'm sure the floor manager will see to it. After all, slapping some " + "paint on some eggs can't be that difficult, can it?");
                        item(item, "It's a highly specialised job; we are experts!");
                        player("Well, take a seat and get cracking.").executeAction(() -> {
                            player.getInventory().deleteItem(new Item(EasterItem.EASTER_IMPLING_JAR.getItemId(), 28));
                            SplittingHeirs.advanceStage(player, Stage.IMPLINGS_GATHERED_SPEAK_TO_BUNNY_JR);
                        });
                    }
                });
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                final WorldObject object = World.getObjectWithType(npc.getId() == EasterConstants.EGG_PAINTING_MACHINE_BASE_WEST ? westernPaintingMachine : easternPaintingMachine, 10);
                player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> execute(player, npc)));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { EasterConstants.EGG_PAINTING_MACHINE_BASE_WEST, EasterConstants.EGG_PAINTING_MACHINE_BASE_EAST, EasterConstants.EGG_PAINTING_MACHINE_WEST, EasterConstants.EGG_PAINTING_MACHINE_EAST, NpcId.EGGPAINTING_MACHINE_15241, NpcId.EGGPAINTING_MACHINE_15237 };
    }

    public static class ImplingOnPaintingMachine implements ItemOnNPCAction {

        @Override
        public void handle(final Player player, final Item item, final int slot, final NPC npc) {
            final WorldObject object = World.getObjectWithType(npc.getId() == EasterConstants.EGG_PAINTING_MACHINE_BASE_WEST ? westernPaintingMachine : easternPaintingMachine, 10);
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> {
                player.stopAll();
                player.faceObject(object);
                handleItemOnNPCAction(player, item, slot, npc);
            }));
        }

        @Override
        public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
            if (SplittingHeirs.getProgress(player) != Stage.GATHER_IMPLINGS) {
                return;
            }
            if (player.getInventory().getAmountOf(EasterItem.EASTER_IMPLING_JAR.getItemId()) < 5) {
                player.getDialogueManager().start(new PlayerChat(player, "I should catch all the implings before returning them."));
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    player("Here we go, little implings. Time to get back to work.");
                    item(item, "Who will supervise us? No one cares, it's why we left - or tried to!");
                    player("Don't worry, I'm sure the floor manager will see to it. After all, slapping some paint on" + " some eggs can't be that difficult, can it?");
                    item(item, "It's a highly specialised job; we are experts!");
                    player("Well, take a seat and get cracking.").executeAction(() -> {
                        player.getInventory().deleteItem(new Item(EasterItem.EASTER_IMPLING_JAR.getItemId(), 28));
                        SplittingHeirs.advanceStage(player, Stage.IMPLINGS_GATHERED_SPEAK_TO_BUNNY_JR);
                    });
                }
            });
        }

        @Override
        public Object[] getItems() {
            return new Object[] { EasterItem.EASTER_IMPLING_JAR.getItemId() };
        }

        @Override
        public Object[] getObjects() {
            return new Object[] { EasterConstants.EGG_PAINTING_MACHINE_BASE_WEST, EasterConstants.EGG_PAINTING_MACHINE_BASE_EAST, EasterConstants.EGG_PAINTING_MACHINE_WEST, EasterConstants.EGG_PAINTING_MACHINE_EAST };
        }
    }
}

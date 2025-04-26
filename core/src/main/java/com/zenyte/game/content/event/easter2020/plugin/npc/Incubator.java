package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.EasterConstants.EasterItem;
import com.zenyte.game.content.event.easter2020.SplittingHeirs;
import com.zenyte.game.content.event.easter2020.Stage;
import com.zenyte.game.content.event.easter2020.plugin.object.CoalSupplyObject;
import com.zenyte.game.content.event.easter2020.plugin.object.IncubatorControlsObject;
import com.zenyte.game.content.event.easter2020.plugin.object.IncubatorWaterTankObject;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.PlayerChat;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import mgi.utilities.CollectionUtils;

/**
 * @author Corey
 * @since 08/04/2020
 */
@SkipPluginScan
public class Incubator extends NPCPlugin {
    public static boolean isCompletelyFixed(final Player player) {
        if (State.getCurrentState(player) != State.WITH_CHIMNEY) {
            return false;
        }
        if (!IncubatorWaterTankObject.isFixed(player)) {
            return false;
        }
        if (!IncubatorControlsObject.isFixed(player)) {
            return false;
        }
        return CoalSupplyObject.isFixed(player);
    }

    public static void fixIncubator(final Player player) {
        if (!isCompletelyFixed(player)) {
            return;
        }
        SplittingHeirs.advanceStage(player, Stage.INCUBATOR_FIXED_SPEAK_TO_BUNNY_JR);
    }

    private static final Location incubatorLocation = new Location(2201, 4380, 0);

    @Override
    public void handle() {
        bind("Look-at", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new PlainChat(player, "This looks like the main body of the incubator: the part where the egg gets incubated."));
            }
            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                final WorldObject object = World.getObjectWithType(incubatorLocation, 10);
                player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> execute(player, npc)));
            }
            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.faceObject(World.getObjectWithType(incubatorLocation, 10));
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return State.forId.keySet().toIntArray();
    }


    public enum State {
        BROKEN(15229, 15224, EasterConstants.INCUBATOR_VARBIT, 0), WITH_COG(15230, 15225, EasterConstants.INCUBATOR_VARBIT, 1), WITH_PISTONS(15231, 15226, EasterConstants.INCUBATOR_VARBIT, 2), WITH_CHIMNEY(15232, 15227, EasterConstants.INCUBATOR_VARBIT, 3), FULLY_FIXED(15233, 15228, EasterConstants.INCUBATOR_VARBIT, 4);
        private static final Int2ObjectOpenHashMap<State> forId;
        private static final Int2ObjectOpenHashMap<State> forVarbitValue;
        private static final State[] values = values();
        private static final IntSet baseObjectIds = new IntArraySet(values.length);

        static {
            CollectionUtils.populateMap(values, forId = new Int2ObjectOpenHashMap<>(values.length), State::getId);
            CollectionUtils.populateMap(values, forVarbitValue = new Int2ObjectOpenHashMap<>(values.length), State::getVarbitValue);
            for (State value : values) {
                baseObjectIds.add(value.getBaseId());
            }
        }

        private final int id;
        private final int baseId;
        private final int varbitId;
        private final int varbitValue;

        State(int id, int baseId, int varbitId, int varbitValue) {
            this.id = id;
            this.baseId = baseId;
            this.varbitId = varbitId;
            this.varbitValue = varbitValue;
        }

        public static State getCurrentState(final Player player) {
            return forVarbitValue.get(player.getVarManager().getBitValue(EasterConstants.INCUBATOR_VARBIT));
        }

        public void setState(final Player player) {
            player.getVarManager().sendBit(getVarbitId(), getVarbitValue());
        }

        public int getId() {
            return id;
        }

        public int getBaseId() {
            return baseId;
        }

        public int getVarbitId() {
            return varbitId;
        }

        public int getVarbitValue() {
            return varbitValue;
        }
    }


    public static class ItemOnIncubator implements ItemOnNPCAction {
        @Override
        public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
            if (!SplittingHeirs.progressedAtLeast(player, Stage.SPOKEN_WITH_INCUBATOR_WORKER)) {
                player.getDialogueManager().start(new PlayerChat(player, "I should speak with the Impling working this machine to see what they want me to do."));
                return;
            }
            if (item.getName().toLowerCase().contains("pipe")) {
                player.getDialogueManager().start(new PlainChat(player, "Doesn't look like that will fit anywhere on " +
                        "this section. I wonder if it goes on one of the other units."));
                return;
            }
            final Incubator.State state = State.getCurrentState(player);
            switch (state) {
            case BROKEN: 
                if (item.getId() == EasterItem.COG.getItemId()) {
                    State.WITH_COG.setState(player);
                    player.getInventory().deleteItem(item);
                    player.getDialogueManager().start(new PlainChat(player, "You wrestle the cog into place."));
                    return;
                }
                break;
            case WITH_COG: 
                if (item.getId() == EasterItem.PISTONS.getItemId()) {
                    State.WITH_PISTONS.setState(player);
                    player.getInventory().deleteItem(item);
                    player.getDialogueManager().start(new PlainChat(player, "You slot the pistons into place."));
                    return;
                }
                break;
            case WITH_PISTONS: 
                if (item.getId() == EasterItem.CHIMNEY.getItemId()) {
                    State.WITH_CHIMNEY.setState(player);
                    player.getInventory().deleteItem(item);
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            plain("You place the chimney on top.");
                            player("Hmm, looks like it needs to be connected up with the other parts.");
                        }
                    });
                    return;
                }
                break;
            default: 
                player.getDialogueManager().start(new PlayerChat(player, "I've already fixed the machine!"));
                return;
            }
            player.getDialogueManager().start(new PlayerChat(player, "Hmm, looks like these fit, but not until another part has been added."));
        }

        @Override
        public Object[] getItems() {
            return new Object[] {EasterItem.COG.getItemId(), EasterItem.PISTONS.getItemId(), EasterItem.CHIMNEY.getItemId(), EasterItem.CLEAN_PIPE.getItemId(), EasterItem.SOOTY_PIPE.getItemId(), EasterItem.WET_PIPE.getItemId()};
        }

        @Override
        public Object[] getObjects() {
            return State.baseObjectIds.toArray();
        }
    }
}

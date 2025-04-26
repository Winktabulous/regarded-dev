package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.Puzzle;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.*;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.Enums;
import mgi.types.config.enums.IntEnum;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Manages the treasure trails puzzle boxes throughout the game.
 *
 * @author Kris | 29. march 2018 : 23:40.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class PuzzleBox {
    /**
     * The player managing the puzzle box.
     */
    private final Player player;
    /**
     * The puzzle that we're currently managing.
     */
    private Puzzle puzzle;
    /**
     * The container of the current puzzle pieces.
     */
    private Container currentPuzzle;
    /**
     * Whether the puzzle is already complete or not.
     */
    private boolean complete;

    /**
     * Constructs the puzzle box for the player.
     *
     * @param player the player whose box this is.
     */
    public PuzzleBox(final Player player) {
        this.player = player;
    }

    /**
     * Opens the puzzle interface for the player. If the puzzle is null, it will
     * generate a puzzle for the player.
     *
     * @param id the id of the puzzle box that we're managing.
     */
    public final void openPuzzle(final int id) {
        if (currentPuzzle == null || puzzle != null && puzzle.getPuzzleBox() != id) {
            generatePuzzle(id);
        }
        assert puzzle != null : "Puzzle is not yet set";
        GameInterface.PUZZLE_BOX.open(player);
    }

    public final void reset() {
        currentPuzzle = null;
    }

    /**
     * Performs a full container and varp refresh on the current puzzle.
     */
    public void fullRefresh() {
        assert puzzle != null : "Puzzle is not set yet.";
        currentPuzzle.setFullUpdate(true);
        currentPuzzle.refresh(player);
        Enums.PUZZLE_BOX_ENUMS.getKey(puzzle.getEnumId()).ifPresent(puzzleIndex -> player.getVarManager().sendVar(261, puzzleIndex));
    }

    /**
     * Checks whether or not a puzzle has currently been generated.
     *
     * @return whether or not a puzzle has been generated.
     */
    public boolean containsPuzzle() {
        return puzzle != null;
    }

    /**
     * Gets the current int enum for the puzzle box, resulting an enum of slot:itemId for all the puzzle pieces.
     *
     * @return the enum containing all of the puzzle pieces in the correct order.
     */
    private IntEnum getEnum() {
        return EnumDefinitions.getIntEnum(puzzle.getEnumId());
    }

    /**
     * Gets the current empty slot based on the only available slot in the container.
     *
     * @return the current empty slot of the puzzle.
     */
    private int emptySlot() {
        final IntAVLTreeSet slots = currentPuzzle.getAvailableSlots();
        assert slots.size() == 1;
        return slots.firstInt();
    }

    /**
     * Checks whether the player can move that puzzle tile.
     *
     * @param slotId the slot on which the player clicked.
     * @return whether the player can move that puzzle tile or not.
     */
    private boolean canMove(final int slotId) {
        final int emptySlot = emptySlot();
        return (slotId + 1) == emptySlot || (slotId - 1) == emptySlot || (slotId + 5) == emptySlot || (slotId - 5) == emptySlot;
    }

    /**
     * Shifts the puzzle tile upon clicking if possible.
     *
     * @param slot the slot that was clicked.
     */
    public void shiftPuzzle(final int slot) {
        if (complete || !canMove(slot)) {
            return;
        }
        final Item movedTile = currentPuzzle.get(slot);
        currentPuzzle.set(emptySlot(), movedTile);
        currentPuzzle.set(slot, null);
        currentPuzzle.refresh(player);
    }

    /**
     * Checks whether the puzzle has been completed or not; if it has been and
     * the state isn't set to complete, it will update the puzzle box in
     * player's inventory - if there still is one - and inform the player that
     * they've solved the puzzle.
     */
    public void checkCompletion() {
        if (complete) {
            return;
        }
        final IntEnum enumList = getEnum();
        for (int i = 0; i < 24; i++) {
            final OptionalInt optionalValue = enumList.getValue(i);
            final Item currentPiece = currentPuzzle.get(i);
            if (!optionalValue.isPresent() || currentPiece == null || (currentPiece.getId() != optionalValue.getAsInt())) {
                return;
            }
        }
        final Item item = findPuzzleItem();
        if (item == null) {
            return;
        }
        item.setCharges(1);
        complete = true;
        player.sendMessage("Congratulations! You've solved the puzzle!");
    }

    /**
     * Finds the puzzle box item in the player's inventory that matches this puzzle.
     *
     * @return the puzzle box item.
     */
    private Item findPuzzleItem() {
        final int puzzleBox = puzzle.getPuzzleBox();
        final Int2ObjectSortedMap.FastSortedEntrySet<Item> entrySet = player.getInventory().getContainer().getItems().int2ObjectEntrySet();
        final Int2ObjectMap.Entry<Item> item = CollectionUtils.findMatching(entrySet, entry -> entry.getValue().getId() == puzzleBox);
        return item.getValue();
    }

    /**
     * Generates the puzzle and fills it with with the pieces in a random order.
     *
     * @param id the puzzle box id to fill the puzzle with.
     */
    private final void generatePuzzle(final int id) {
        puzzle = Puzzle.getMap().get(id);
        currentPuzzle = new Container(ContainerPolicy.NORMAL, ContainerType.PUZZLE_BOX, Optional.of(player));
        complete = false;
        currentPuzzle.clear();
        final int firstPieceItemId = getEnum().getValue(0).orElseThrow(RuntimeException::new);
        for (int i = 0; i < 24; i++) {
            currentPuzzle.set(i, new Item(firstPieceItemId + i));
        }
        shuffle();
    }

    /**
     * Shuffles the puzzle in a backwards direction to ensure the puzzle remains completeable and is not just a few steps away from being completed.
     */
    private void shuffle() {
        final IntArrayList availableMoves = new IntArrayList(4);
        int emptySlot = emptySlot();
        int lastSlot = -1;
        for (int i = 0; i < 40; i++) {
            final int x = emptySlot % 5;
            final int y = emptySlot / 5;
            if (x > 0) {
                availableMoves.add(emptySlot - 1);
            }
            if (x < 4) {
                availableMoves.add(emptySlot + 1);
            }
            if (y > 0) {
                availableMoves.add(emptySlot - 5);
            }
            if (y < 4) {
                availableMoves.add(emptySlot + 5);
            }
            availableMoves.rem(lastSlot);
            lastSlot = emptySlot;
            assert !availableMoves.isEmpty() : "No available moves remaining";
            assert ensureBoundaries(availableMoves) : "One or more of the available moves are out of boundaries: " + availableMoves;
            emptySlot = availableMoves.getInt(Utils.random(availableMoves.size() - 1));
            assert canMove(emptySlot) : "Cannot move on the requested position of " + emptySlot + " with the puzzle of " + currentPuzzle.getItems();
            shiftPuzzle(emptySlot);
            assert emptySlot == emptySlot() : "Current empty slot does not match the container's empty slot.";
            availableMoves.clear();
        }
    }

    /**
     * Ensures that the slots provided in the list are within the boundaries of the puzzle palette.
     *
     * @param list the lsit of slots.
     * @return whether or not the slots are within boundaries of the puzzle palette.
     */
    private boolean ensureBoundaries(@NotNull final IntList list) {
        return CollectionUtils.findMatching(list, element -> element < 0 || element > 24) == null;
    }
}

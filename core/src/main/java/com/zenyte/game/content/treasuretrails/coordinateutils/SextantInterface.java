package com.zenyte.game.content.treasuretrails.coordinateutils;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.out.IfSetAnim;
import com.zenyte.game.packet.out.IfSetPosition;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.PlainChat;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.zenyte.game.content.treasuretrails.clues.CoordinateClue.*;

/**
 * @author Kris | 02/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SextantInterface extends Interface {
    /**
     * The temporary attribute using which the sextant position object is serialized whilst the interface is open.
     */
    private static final String sextantAttrKey = "Sextant position";
    /**
     * The maximum coordinates at which the sun offset is calculated to, if you go past it horizontally, it'll treat the sun's position as if you were at the cap.
     */
    private static final int COORD_CAP = 6400;
    /**
     * The offset of the sun, every 2^value - 1 steps sun moves a bit further up.
     */
    private static final int SUN_OFFSET_COORD_RATIO = 8;
    /**
     * The base view horizontal coordinate where the sun and the grass models are placed at.
     */
    private static final int BASE_VIEW_HORIZONTAL_COORD = 419;
    /**
     * The center view vertical coordinate where the grass and sun need to be to properly calculate your position.
     */
    private static final int CENTER_VIEW_VERTICAL_COORD = 141;
    /**
     * The multiplier by how many pixels the grass moves up or down each movement reference.
     */
    private static final int GRASS_MULTIPLIER = 6;
    /**
     * The multiplier by how many pixels the sun moves up or down each movement reference.
     */
    private static final int SUN_MULTIPLIER = 2;
    /**
     * The offset of the arm on the right side. When moving the mainframe from the absolute max on the right to the left, it takes this number of movements before the arm starts moving too.
     */
    private static final int ARM_RIGHT_OFFSET = 2;
    /**
     * The minimum vertical coordinate of the grass.
     */
    private static final int MIN_VIEW_VERTICAL_GRASS_COORD = CENTER_VIEW_VERTICAL_COORD - ((SextantPosition.FRAME_POSITIONS_COUNT >> 1) * GRASS_MULTIPLIER);
    /**
     * The maximum vertical coordinate of the grass.
     */
    private static final int MAX_VIEW_VERTICAL_GRASS_COORD = CENTER_VIEW_VERTICAL_COORD + ((SextantPosition.FRAME_POSITIONS_COUNT >> 1) * GRASS_MULTIPLIER);
    /**
     * The minimum vertical coordinate of the sun.
     */
    private static final int MIN_VIEW_VERTICAL_SUN_COORD = CENTER_VIEW_VERTICAL_COORD - ((SextantPosition.ARM_POSITIONS_COUNT - SextantPosition.FRAME_POSITIONS_COUNT) << 1);
    /**
     * The maximum vertical coordinate of the sun.
     */
    private static final int MAX_VIEW_VERTICAL_SUN_COORD = CENTER_VIEW_VERTICAL_COORD + ((SextantPosition.ARM_POSITIONS_COUNT - SextantPosition.FRAME_POSITIONS_COUNT) << 1);

    @Override
    protected void attach() {
        put(1, "Grass");
        put(2, "Sun");
        put(4, "Down arrow");
        put(5, "Up arrow");
        put(6, "Right arrow");
        put(7, "Left arrow");
        put(8, "Frame");
        put(9, "Arm");
        put(10, "Index mirror");
        put(11, "Get location");
    }

    /**
     * Opens the interface and randomizes the position of the grass and the sun within available boundaries.
     * @param player the player that is opening the interface.
     */
    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        final int framePosition = Utils.random(SextantPosition.FRAME_POSITIONS_COUNT - 1);
        final int armPosition = Math.max(framePosition, Math.min(SextantPosition.ARM_POSITIONS_COUNT - (SextantPosition.FRAME_POSITIONS_COUNT - ARM_RIGHT_OFFSET - framePosition), Utils.random(SextantPosition.ARM_POSITIONS_COUNT - 1)));
        final SextantPosition sextantPosition = new SextantPosition(framePosition, armPosition);
        setSextant(player, sextantPosition);
        refreshSextantPosition(player);
    }

    /**
     * Stores the sextant's position values in temporary attributes list on the player so that it can be referenced while interacted with.
     * @param player the player who's viewing the sextant.
     * @param sextantPosition the position data object.
     */
    private final void setSextant(@NotNull final Player player, @NotNull final SextantPosition sextantPosition) {
        player.getTemporaryAttributes().put(sextantAttrKey, sextantPosition);
    }

    /**
     * Refreshes the positions of the arm, frame, index mirror, sun and grass on the interface. The latter two correlate to the former three.
     * @param player the player viewing the sextant.
     */
    private final void refreshSextantPosition(@NotNull final Player player) {
        final SextantPosition sextantPosition = getSextantPosition(player);
        final int armPosition = Math.max(sextantPosition.getFramePosition(), Math.min(SextantPosition.ARM_POSITIONS_COUNT - (SextantPosition.FRAME_POSITIONS_COUNT - ARM_RIGHT_OFFSET - sextantPosition.getFramePosition()), sextantPosition.getArmPosition()));
        player.send(new IfSetPosition(getId(), getComponent("Grass"), BASE_VIEW_HORIZONTAL_COORD, getGrassVerticalPosition(player)));
        player.send(new IfSetPosition(getId(), getComponent("Sun"), BASE_VIEW_HORIZONTAL_COORD, getSunVerticalPosition(player)));
        player.send(new IfSetAnim(getId(), getComponent("Arm"), SextantPosition.BASE_ARM_POSITION + armPosition));
        player.send(new IfSetAnim(getId(), getComponent("Frame"), SextantPosition.BASE_FRAME_POSITION + sextantPosition.getFramePosition()));
        player.send(new IfSetAnim(getId(), getComponent("Index mirror"), SextantPosition.BASE_FRAME_POSITION + sextantPosition.getFramePosition()));
    }

    /**
     * Calculates the vertical position of the grass while respecting the boundaries.
     * @param player the player viewing the sextant.
     * @return the vertical coordinate upon which the grass model is to be sent to.
     */
    private final int getGrassVerticalPosition(@NotNull final Player player) {
        final SextantPosition sextantPosition = getSextantPosition(player);
        return Math.min(MAX_VIEW_VERTICAL_GRASS_COORD, Math.max(MIN_VIEW_VERTICAL_GRASS_COORD, MAX_VIEW_VERTICAL_GRASS_COORD - (sextantPosition.getFramePosition() * GRASS_MULTIPLIER)));
    }

    /**
     * Calculates the vertical position of the sun while respecting the boundaries. Adjusts it according to the player's horizontal location on the world map.
     * @param player the player viewing the sextant.
     * @return the vertical coordinate upon which the sun model is to be sent to.
     */
    private final int getSunVerticalPosition(@NotNull final Player player) {
        final SextantPosition sextantPosition = getSextantPosition(player);
        final int sunOffset = (Math.min(COORD_CAP, player.getX()) >> SUN_OFFSET_COORD_RATIO) - SUN_OFFSET_COORD_RATIO;
        final int armPosition = Math.max(sextantPosition.getFramePosition(), Math.min(SextantPosition.ARM_POSITIONS_COUNT - (SextantPosition.FRAME_POSITIONS_COUNT - ARM_RIGHT_OFFSET - sextantPosition.getFramePosition()), sextantPosition.getArmPosition()));
        return Math.min(MAX_VIEW_VERTICAL_SUN_COORD, Math.max(MIN_VIEW_VERTICAL_SUN_COORD, MIN_VIEW_VERTICAL_SUN_COORD + ((armPosition - sunOffset) * SUN_MULTIPLIER)));
    }

    /**
     * Gets the stored sextant position on the player.
     * @param player the player viewing the sextant.
     * @return the sextant position data object.
     */
    private final SextantPosition getSextantPosition(@NotNull final Player player) {
        return (SextantPosition) Objects.requireNonNull(player.getTemporaryAttributes().get(sextantAttrKey));
    }

    @Override
    protected void build() {
        bind("Down arrow", player -> move(player, 1, 1));
        bind("Up arrow", player -> move(player, -1, -1));
        bind("Right arrow", player -> move(player, 1, 0));
        bind("Left arrow", player -> move(player, -1, 0));
        bind("Get location", player -> {
            final Inventory inventory = player.getInventory();
            if (!inventory.containsItem(ItemId.WATCH, 1) || !inventory.containsItem(ItemId.CHART, 1)) {
                player.getDialogueManager().start(new PlainChat(player, "You need a watch and navigator's chart to " +
                        "work out your location."));
                return;
            }
            if (getGrassVerticalPosition(player) != CENTER_VIEW_VERTICAL_COORD) {
                player.sendMessage("You need to get the horizon in the middle of the eye piece.");
                return;
            }
            final int sunCoordinate = getSunVerticalPosition(player);
            //The sun moves at a stage of two steps per movement, therefore - to avoid potential issues - we check for the center, and one coordinate next to the center to ensure it always works.
            if (sunCoordinate != CENTER_VIEW_VERTICAL_COORD && sunCoordinate != CENTER_VIEW_VERTICAL_COORD + 1) {
                player.sendMessage("You need to get the sun in the middle of the eye piece.");
                return;
            }
            sendCoordinates(player);
        });
    }

    /**
     * Calculates the coordinates and informs the player of them.
     * @param player the player viewing the sextant.
     */
    private final void sendCoordinates(@NotNull final Player player) {
        final int x = player.getX();
        final int y = player.getY();
        final boolean south = y < LATITUDE_OFFSET;
        final boolean west = x < LONGITUDE_OFFSET;
        final int xOffset = Math.abs(x - LONGITUDE_OFFSET);
        final int yOffset = Math.abs(y - LATITUDE_OFFSET);
        final int totalLatitudeMinutes = (int) Math.floor(xOffset * MINUTE_TO_COORDINATE_MULTIPLIER);
        final int totalLongitudeMinutes = (int) Math.floor(yOffset * MINUTE_TO_COORDINATE_MULTIPLIER);
        final int latitudeDegrees = totalLatitudeMinutes / 60;
        final int longitudeDegrees = totalLongitudeMinutes / 60;
        final int latitudeMinutes = totalLatitudeMinutes % 60;
        final int longitudeMinutes = totalLongitudeMinutes % 60;
        final String longitudeMessage = longitudeDegrees + " degree" + (longitudeDegrees == 1 ? "" : "s") + " " + longitudeMinutes + " minute" + (longitudeMinutes == 1 ? "" : "s") + " " + (south ? "south" : "north");
        final String latitudeMessage = latitudeDegrees + " degree" + (latitudeDegrees == 1 ? "" : "s") + " " + latitudeMinutes + " minute" + (latitudeMinutes == 1 ? "" : "s") + " " + (west ? "west" : "east");
        player.sendMessage("The sextant displays:");
        player.sendMessage(longitudeMessage);
        player.sendMessage(latitudeMessage);
        player.getDialogueManager().start(new PlainChat(player, longitudeMessage + "<br><br>" + latitudeMessage));
    }

    /**
     * Moves the arm or the frame & index mirror either up or down. The position of the sun and grass respectively adjust accordingly.
     * @param player the player viewing the sextant.
     * @param horizontalOffset the horizontal offset which corresponds to the arm of the sextant, whereas the valid values are either -1 or 1, as moving the main frame also moves the arm.
     * @param verticalOffset the vertical offset which corresponds to the frame and index mirror of the sextant, whereas the valid values are either -1, 0 or 1.
     */
    private final void move(@NotNull final Player player, @MagicConstant(intValues = {-1, 1}) final int horizontalOffset, @MagicConstant(intValues = {-1, 0, 1}) final int verticalOffset) {
        final SextantPosition currentSextantPosition = getSextantPosition(player);
        final int framePosition = Math.max(0, Math.min(currentSextantPosition.getFramePosition() + verticalOffset, SextantPosition.FRAME_POSITIONS_COUNT - 1));
        final int horizontalConditionalOffset = verticalOffset == 0 || framePosition != currentSextantPosition.getFramePosition() ? horizontalOffset : 0;
        final int armPosition = Math.max(0, Math.min(currentSextantPosition.getArmPosition() + horizontalConditionalOffset, SextantPosition.ARM_POSITIONS_COUNT - 1));
        final SextantPosition newSextantPosition = new SextantPosition(framePosition, horizontalConditionalOffset == 0 ? armPosition : (Math.max(framePosition, Math.min(SextantPosition.ARM_POSITIONS_COUNT - (SextantPosition.FRAME_POSITIONS_COUNT - 2 - framePosition), armPosition))));
        if (!newSextantPosition.equals(currentSextantPosition)) {
            setSextant(player, newSextantPosition);
            refreshSextantPosition(player);
        }
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SEXTANT;
    }
}

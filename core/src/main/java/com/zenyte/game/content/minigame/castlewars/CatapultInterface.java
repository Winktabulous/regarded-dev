package com.zenyte.game.content.minigame.castlewars;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.out.IfSetModel;
import com.zenyte.game.packet.out.IfSetPosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.CharacterLoop;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 08/09/2019 15:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CatapultInterface extends Interface {
    private static final Location ZAMORAK_CATAPULT_LOWEST_LAND_COORDS = new ImmutableLocation(2387, 3116, 0);
    private static final Location SARADOMIN_CATAPULT_LOWEST_LAND_COORDS = new ImmutableLocation(2412, 3091, 0);
    private static final Location ZAMORAK_CATAPULT_START = new ImmutableLocation(2385, 3118, 0);
    private static final Location SARADOMIN_CATAPULT_START = new ImmutableLocation(2414, 3089, 0);
    private static final int ZAMORAK_X = 86;
    private static final int ZAMORAK_Y = 113;
    private static final int ZAMORAK_OFFSET_MULTIPLIER = 2;
    private static final int SARADOMIN_X = 193;
    private static final int SARADOMIN_Y = 222;
    private static final int SARADOMIN_OFFSET_MULTIPLIER = -2;
    private static final int MAX_CLICKS = 30;
    private static final int NUMBER_0_MODEL_ID = 4863;
    private static final Projectile projectile = new Projectile(304, 50, 5, 0, 45, 100, 32, 5);

    @Override
    protected void attach() {
        put(146, "First vertical digit");
        put(147, "Second vertical digit");
        put(148, "First horizontal digit");
        put(149, "Second horizontal digit");
        put(150, "Increase vertical");
        put(151, "Decrease vertical");
        put(152, "Increase horizontal");
        put(153, "Decrease horizontal");
        put(158, "Fire");
        put(161, "Cross");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
        resetDigits(player);
        refreshDigits(player);
        refreshCross(player);
    }

    @Override
    protected void build() {
        bind("Increase vertical", player -> setVerticalClicks(player, getVerticalClicks(player) + 1));
        bind("Decrease vertical", player -> setVerticalClicks(player, getVerticalClicks(player) - 1));
        bind("Increase horizontal", player -> setHorizontalClicks(player, getHorizontalClicks(player) + 1));
        bind("Decrease horizontal", player -> setHorizontalClicks(player, getHorizontalClicks(player) - 1));
        bind("Fire", player -> {
            player.getInterfaceHandler().closeInterface(getInterface());
            final Location destination = getDestination(player);
            final CastleWarsTeam team = CastleWars.getTeam(player);
            WorldTasksManager.schedule(() -> {
                CharacterLoop.forEach(destination, 2, Player.class, p -> {
                    if (CastleWars.getTeam(player) != CastleWars.getTeam(p)) {
                        p.applyHit(new Hit(Utils.random(8, 20), HitType.REGULAR));
                    }
                });
            }, World.sendProjectile(team == CastleWarsTeam.SARADOMIN ? SARADOMIN_CATAPULT_START : ZAMORAK_CATAPULT_START, destination, projectile));
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.CASTLE_WARS_CATAPULT;
    }

    /**
     * Gets the model id representing the digit shown on the catapult interface.
     *
     * @param digit the digit for which the model to obtain.
     * @return the model if of the digit.
     */
    private static final int getModel(final int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("Digit must be between 0 and 9, inclusive." + digit);
        }
        return NUMBER_0_MODEL_ID + digit;
    }

    /**
     * Resets the digit attributes for the player.
     *
     * @param player the player for whom to reset the attributes.
     */
    private void resetDigits(@NotNull final Player player) {
        player.getTemporaryAttributes().remove("cw horizontal clicks");
        player.getTemporaryAttributes().remove("cw vertical clicks");
    }

    /**
     * Gets the horizontal clicks from the catapult.
     *
     * @param player the player who is using the catapult at the time.
     * @return the horizontal clicks value.
     */
    private int getHorizontalClicks(@NotNull final Player player) {
        return player.getNumericTemporaryAttribute("cw horizontal clicks").intValue();
    }

    /**
     * Sets the horizontal clicks to the requested value, but capped to a minimum of 0, and a maximum of {@link CatapultInterface#MAX_CLICKS}.
     * In addition to this, refreshes the values for the player.
     *
     * @param player the player who is using the catapult.
     * @param value  the value to which the clicks to set to.
     */
    private void setHorizontalClicks(@NotNull final Player player, final int value) {
        player.addTemporaryAttribute("cw horizontal clicks", Math.max(0, Math.min(value, MAX_CLICKS)));
        refreshDigits(player);
        refreshCross(player);
    }

    /**
     * Gets the vertical clicks from the catapult.
     *
     * @param player the player who is using the catapult at the time.
     * @return the vertical clicks value.
     */
    private int getVerticalClicks(@NotNull final Player player) {
        return player.getNumericTemporaryAttribute("cw vertical clicks").intValue();
    }

    /**
     * Sets the vertical clicks to the requested value, but capped to a minimum of 0, and a maximum of {@link CatapultInterface#MAX_CLICKS}.
     * In addition to this, refreshes the values for the player.
     *
     * @param player the player who is using the catapult.
     * @param value  the value to which the clicks to set to.
     */
    private void setVerticalClicks(@NotNull final Player player, final int value) {
        player.addTemporaryAttribute("cw vertical clicks", Math.max(0, Math.min(value, MAX_CLICKS)));
        refreshDigits(player);
        refreshCross(player);
    }

    /**
     * Refreshes the click digits on the catapult interface based on the values stored on the player.
     *
     * @param player the player whom to display the digits to.
     */
    private void refreshDigits(@NotNull final Player player) {
        final int horizontalClicks = getHorizontalClicks(player);
        final int verticalClicks = getVerticalClicks(player);
        assert horizontalClicks >= 0 && horizontalClicks <= MAX_CLICKS;
        assert verticalClicks >= 0 && verticalClicks <= MAX_CLICKS;
        final String horizontalCoordsString = Integer.toString(horizontalClicks);
        final String verticalCoordsString = Integer.toString(verticalClicks);
        assert !horizontalCoordsString.isEmpty();
        assert !verticalCoordsString.isEmpty();
        final int horizontalLength = horizontalCoordsString.length();
        final int verticalLength = verticalCoordsString.length();
        final int firstHorizontalDigit = horizontalLength == 1 ? 0 : Character.getNumericValue(horizontalCoordsString.charAt(0));
        final int secondHorizontalDigit = Character.getNumericValue(horizontalCoordsString.charAt(horizontalLength - 1));
        final int firstVerticalDigit = verticalLength == 1 ? 0 : Character.getNumericValue(verticalCoordsString.charAt(0));
        final int secondVerticalDigit = Character.getNumericValue(verticalCoordsString.charAt(verticalLength - 1));
        player.send(new IfSetModel(getId(), getComponent("First horizontal digit"), getModel(firstHorizontalDigit)));
        player.send(new IfSetModel(getId(), getComponent("Second horizontal digit"), getModel(secondHorizontalDigit)));
        player.send(new IfSetModel(getId(), getComponent("First vertical digit"), getModel(firstVerticalDigit)));
        player.send(new IfSetModel(getId(), getComponent("Second vertical digit"), getModel(secondVerticalDigit)));
    }

    /**
     * Refreshes the cross position on the interface based on the values stored on the player.
     *
     * @param player the player for whom to refresh the cross position.
     */
    private void refreshCross(@NotNull final Player player) {
        final CastleWarsTeam team = CastleWars.getTeam(player);
        final int horizontalClicks = getHorizontalClicks(player);
        final int verticalClicks = getVerticalClicks(player);
        assert horizontalClicks >= 0 && horizontalClicks <= MAX_CLICKS;
        assert verticalClicks >= 0 && verticalClicks <= MAX_CLICKS;
        final int startX = team == CastleWarsTeam.SARADOMIN ? SARADOMIN_X : ZAMORAK_X;
        final int startY = team == CastleWarsTeam.SARADOMIN ? SARADOMIN_Y : ZAMORAK_Y;
        final int multiplier = team == CastleWarsTeam.SARADOMIN ? SARADOMIN_OFFSET_MULTIPLIER : ZAMORAK_OFFSET_MULTIPLIER;
        player.send(new IfSetPosition(getId(), getComponent("Cross"), startX + (horizontalClicks * multiplier), startY + (verticalClicks * multiplier)));
    }

    /**
     * Gets the destination tile where the projectile is expected to land based on the interface configuration.
     *
     * @param player the player who is using the catapult.
     * @return the tile where the projectile is meant to land.
     */
    private Location getDestination(@NotNull final Player player) {
        final CastleWarsTeam team = CastleWars.getTeam(player);
        final int horizontalClicks = getHorizontalClicks(player);
        final int verticalClicks = getVerticalClicks(player);
        assert horizontalClicks >= 0 && horizontalClicks <= MAX_CLICKS;
        assert verticalClicks >= 0 && verticalClicks <= MAX_CLICKS;
        final Location startTile = team == CastleWarsTeam.SARADOMIN ? SARADOMIN_CATAPULT_LOWEST_LAND_COORDS : ZAMORAK_CATAPULT_LOWEST_LAND_COORDS;
        return new Location(startTile.getX() + (horizontalClicks / (team == CastleWarsTeam.SARADOMIN ? SARADOMIN_OFFSET_MULTIPLIER : ZAMORAK_OFFSET_MULTIPLIER)), startTile.getY() + (verticalClicks / (team != CastleWarsTeam.SARADOMIN ? SARADOMIN_OFFSET_MULTIPLIER : ZAMORAK_OFFSET_MULTIPLIER)), startTile.getPlane());
    }
}

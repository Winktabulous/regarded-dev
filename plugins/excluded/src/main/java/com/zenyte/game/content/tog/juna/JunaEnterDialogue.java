package com.zenyte.game.content.tog.juna;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * @author Chris
 * @since September 07 2020
 */
public class JunaEnterDialogue extends Dialogue {
    public static final ImmutableLocation OUTSIDE_CAVE_TILE = new ImmutableLocation(3251, 9516, 2);
    public static final ImmutableLocation INSIDE_CAVE_TILE = new ImmutableLocation(3253, 9517, 2);
    public static final String LAST_ATTEMPT_DATE_ATTR = "last tog attempt date";
    static ZoneId zone = ZoneId.systemDefault();
    /**
     * The amount of days a player must wait to enter the cave again.
     */
    private static final int ENTER_CAVE_INTERVAL_HOURS = 24;
    private static final ImmutableLocation CAVE_CENTER_TILE = new ImmutableLocation(3257, 9517, 2);
    private static final RenderAnimation RENDER_ANIM = new RenderAnimation(2040, RenderAnimation.STAND_TURN, 2041, 2041, 2041, 2041, 2042);

    public JunaEnterDialogue(@NotNull final Player player, @NotNull final int npcId) {
        super(player, npcId);
    }

    @Override
    public void buildDialogue() {
        player("Can I enter the cave?");
        if (player.getEquipment().getId(EquipmentSlot.WEAPON) != -1) {
            npc("Perhaps you should empty your hands before you begin.");
        } else if (hasTimePassed(player)) {
            npc("You are qualified to enter, I will let you into the cave for a short time.").executeAction(this::enter);
        } else {
            final LocalDateTime lastAttempt = LocalDate
                    .parse(player.getAttributeOrDefault(LAST_ATTEMPT_DATE_ATTR, LocalDate.ofYearDay(1969, 1).toString()))
                    .atTime(LocalTime.of(0, 0, 0));
            ZonedDateTime endTime = LocalDate.now(zone)
                    .plusDays(1)
                    .atTime(LocalTime.of(0, 0, 0))
                    .atZone(zone);
            Duration remainingTime = Duration.between(lastAttempt, endTime);
            final long hoursLeft = remainingTime.toHours();
            npc("I will not permit any adventurer to access the tears more than once a day. Come back in " + hoursLeft + " " + (hoursLeft == 1 ? "hour" : "hours")+".");
        }
    }

    private void enter() {
        final int firstTileDistance = player.getLocation().getTileDistance(OUTSIDE_CAVE_TILE);
        final int secondTileDistance = player.getLocation().getTileDistance(INSIDE_CAVE_TILE);
        final int lastTileDistance = INSIDE_CAVE_TILE.getTileDistance(CAVE_CENTER_TILE);
        player.setRun(false);
        player.lock(firstTileDistance + secondTileDistance + lastTileDistance + 2);
        player.getEquipment().set(EquipmentSlot.WEAPON, new Item(ItemId.STONE_BOWL));
        player.getAppearance().setRenderAnimation(RENDER_ANIM);
        // Walk to first step outside cave and have Juna open path.
        player.addWalkSteps(OUTSIDE_CAVE_TILE.getX(), OUTSIDE_CAVE_TILE.getY(), firstTileDistance, false);
        // Send animation to Juna to open the path.
        WorldTasksManager.schedule(() -> {
            player.sendSound(1797);
            World.sendObjectAnimation(Juna.JUNA_OBJECT, new Animation(2055));
        }, firstTileDistance);
        // Walk past Juna to inside of cave.
        WorldTasksManager.schedule(() -> player.addWalkSteps(INSIDE_CAVE_TILE.getX(), INSIDE_CAVE_TILE.getY(), secondTileDistance, false), firstTileDistance + 1);
        // Walk to the center of the cave.
        WorldTasksManager.schedule(() -> {
            player.addWalkSteps(CAVE_CENTER_TILE.getX(), CAVE_CENTER_TILE.getY(), lastTileDistance, false);
            player.getAttributes().put(LAST_ATTEMPT_DATE_ATTR, LocalDate.now().toString());
        }, firstTileDistance + secondTileDistance + 1);
    }

    public static boolean hasTimePassed(@NotNull final Player player) {
        final LocalDate lastAttempt = LocalDate.parse(player.getAttributeOrDefault(LAST_ATTEMPT_DATE_ATTR, LocalDate.ofYearDay(1969, 1).toString()));
        final LocalDate today = LocalDate.now();
        player.sendDeveloperMessage("Last attempt: " + lastAttempt + ", Today: " + today+", (Days between: "+ChronoUnit.DAYS.between(lastAttempt, today)+")");
        return ChronoUnit.DAYS.between(lastAttempt, today) >= 1;
    }
}

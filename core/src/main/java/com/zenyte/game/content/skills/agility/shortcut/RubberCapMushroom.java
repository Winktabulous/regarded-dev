package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Failable;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.object.MudPit;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since July 20 2020
 */
public class RubberCapMushroom implements Shortcut, Failable {
    private static final ImmutableLocation START_TILE = new ImmutableLocation(3665, 3808, 0);
    private static final ImmutableLocation MUSHROOM_CENTER_TILE = new ImmutableLocation(3664, 3810, 1);
    private static final ImmutableLocation PIT_TILE = new ImmutableLocation(3687, 3756, 1);
    private static final Animation bounceAnimation = new Animation(4365);
    private static final Animation fallAnimation = new Animation(4367);
    private static final Graphics bounceGraphic = new Graphics(1411);
    private static final Graphics safeFallGraphic = new Graphics(1410);
    private static final ForceTalk fallTalk = new ForceTalk("Ow!");

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Runnable runnable = () -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("You might hurt yourself if you do that", new DialogueOption("I don't care, I'm doing it.", () -> handle(player, object, 0, null)), new DialogueOption("I'd better not in that case"));
            }
        });
        player.setRouteEvent(new TileEvent(player, new TileStrategy(getRouteEvent(player, object), distance(object)), runnable, 1));
    }

    @Override
    public boolean successful(@NotNull final Player player, @NotNull final WorldObject object) {
        return player.getVarManager().getBitValue(MudPit.FILLED_PIT_VARBIT) == 1;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        start(player, false);
    }

    @Override
    public void startFail(Player player, WorldObject object) {
        start(player, true);
    }

    private void start(@NotNull final Player player, final boolean fail) {
        // Move player one plane higher to avoid object rendering over player.
        player.setLocation(START_TILE.transform(0, 0, 1));
        final int distance = player.getLocation().getTileDistance(MUSHROOM_CENTER_TILE) + 1;
        WorldTasksManager.schedule(() -> player.addWalkSteps(MUSHROOM_CENTER_TILE.getX(), MUSHROOM_CENTER_TILE.getY(), distance, false));
        WorldTasksManager.schedule(() -> {
            player.setAnimation(bounceAnimation);
            player.setGraphics(bounceGraphic);
        }, distance);
        WorldTasksManager.schedule(() -> {
            player.setLocation(PIT_TILE);
            player.setAnimation(fallAnimation);
            if (!fail) {
                player.getVarManager().sendBit(MudPit.FILLED_PIT_VARBIT, 0);
            }
            player.setGraphics(fail ? Graphics.RESET : safeFallGraphic);
        }, distance + 2);
        WorldTasksManager.schedule(() -> player.addWalkSteps(3685, 3756, -1, false), distance + 11);
        WorldTasksManager.schedule(() -> {
            player.setLocation(player.getLocation().transform(0, 0, -1));
            if (fail) {
                player.applyHit(new Hit(Utils.random(40, 50), HitType.REGULAR));
                player.setForceTalk(fallTalk);
            }
        }, distance + 15);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_TILE;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 19;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 1;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.RUBBER_CAP_MUSHROOM};
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }

    @Override
    public double getFailXp(WorldObject object) {
        return 0;
    }
}

package com.zenyte.game.content.event.easter2020.plugin.object;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.SplittingHeirs;
import com.zenyte.game.content.event.easter2020.Stage;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Corey
 * @since 26/03/2020
 */
@SkipPluginScan
public class RabbitHoleObject implements ObjectAction {
    @Override
    public int getDelay() {
        return 1;
    }

    private void transformToHuman(final Player player, final Runnable then) {
        final Animation anim = new Animation(EasterConstants.BUNNY_TO_HUMAN_ANIM);
        player.lock(AnimationUtil.getDuration(anim) / 600);
        player.setAnimation(anim);
        if (then != null) {
            WorldTasksManager.schedule(then::run, AnimationUtil.getDuration(anim) / 600);
        }
    }

    private void transformToBunny(final Player player, final Runnable then) {
        final Animation anim = new Animation(EasterConstants.HUMAN_TO_BUNNY_ANIM);
        player.lock(AnimationUtil.getDuration(anim) / 600);
        player.setGraphics(new Graphics(EasterConstants.HUMAN_TO_BUNNY_GFX));
        player.setAnimation(anim);
        if (then != null) {
            WorldTasksManager.schedule(then::run, AnimationUtil.getDuration(anim) / 600);
        }
    }

    private void enterBunnyTunnels(final Player player) {
        if (!player.getInventory().getContainer().isEmpty() || !player.getEquipment().getContainer().isEmpty()) {
            player.sendMessage("Your items won't fit through the rabbit hole - you need to bank them before going " +
                    "inside.");
            return;
        }
        transformToBunny(player, () -> {
            player.setLocation(EasterConstants.rabbitHoleStart);
            player.setFaceLocation(new Location(player.getLocation().transform(Direction.NORTH, 1)));
            player.getDialogueManager().start(new PlainChat(player, "You squeeze into the rabbit hole and are surprised to find that you<br>fit inside."));
        });
    }

    private void exitToSurface(final Player player) {
        transformToHuman(player, () -> player.getDialogueManager().start(new PlainChat(player, "The Easter Bunny's " +
                "magic fades, crumbling away the items you<br>collected, and changing you back to your usual self.")));
        player.setLocation(new Location(3089, 3471));
        player.setFaceLocation(new Location(player.getLocation().transform(Direction.NORTH, 1)));
    }

    private void enterEasterFactory(final Player player) {
        transformToHuman(player, () -> player.sendMessage("The Easter Bunny's magic wears off."));
        player.setLocation(new Location(2205, 4356));
    }

    private void exitEasterFactory(final Player player) {
        player.sendMessage("As you approach the hole, you feel the Easter Bunny's magic wash over you.");
        transformToBunny(player, () -> {
            player.sendMessage("The Easter Bunny's magic transforms you into a bunny.");
            player.setLocation(new Location(2205, 4349));
        });
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!SplittingHeirs.progressedAtLeast(player, Stage.START)) {
            player.sendMessage("You wouldn't be able to fit down there without some help.");
            return;
        }
        if (player.getFollower() != null) {
            player.sendMessage("The Easter bunny does not allow familiars down in that hole - they may get lost - or even worse!");
            return;
        }
        switch (object.getId()) {
        case EasterConstants.WARREN_ENTRANCE: 
            enterBunnyTunnels(player);
            break;
        case EasterConstants.WARREN_EXIT: 
            exitToSurface(player);
            break;
        case EasterConstants.EASTER_FACTORY_ENTRANCE: 
            enterEasterFactory(player);
            break;
        case EasterConstants.EASTER_FACTORY_EXIT: 
            exitEasterFactory(player);
            break;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {EasterConstants.WARREN_ENTRANCE, EasterConstants.WARREN_EXIT, EasterConstants.EASTER_FACTORY_ENTRANCE, EasterConstants.EASTER_FACTORY_EXIT};
    }
}

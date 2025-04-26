package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchState;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 23/02/2019 14:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BinInteraction extends Action {
    private final FarmingSpot spot;
    private final String option;
    private static final Animation dumpAnimation = new Animation(832);
    private static final Animation animation = new Animation(810);
    private static final SoundEffect openingSound = new SoundEffect(2429);
    private static final SoundEffect closingSound = new SoundEffect(2428);
    private static final SoundEffect dumpSound = new SoundEffect(2441);

    @Override
    public boolean start() {
        if (option.equalsIgnoreCase("Dump")) {
            player.setAnimation(dumpAnimation);
            player.sendSound(dumpSound);
            delay(1);
            return true;
        } else {
            if (option.equalsIgnoreCase("Open")) {
                final PatchState state = spot.getState();
                if (state != PatchState.GROWN) {
                    player.getDialogueManager().start(new PlainChat(player, "The vegetation hasn't finished rotting " +
                            "yet."));
                    return false;
                }
                player.sendSound(openingSound);
            } else if (option.equalsIgnoreCase("Close")) {
                assert spot.getCompostBin().isFull();
                player.sendMessage("You close the compost bin.");
                player.sendSound(closingSound);
            } else {
                throw new RuntimeException("Unknown option: " + option);
            }
            player.setAnimation(animation);
            delay(1);
        }
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        if (option.equalsIgnoreCase("Open")) {
            spot.openCompostBin();
        } else if (option.equalsIgnoreCase("Close")) {
            spot.closeCompostBin();
            player.sendMessage("The contents have begun to rot.");
        } else if (option.equalsIgnoreCase("Dump")) {
            spot.clear();
        } else {
            throw new RuntimeException("Unknown option: " + option);
        }
        return -1;
    }

    public BinInteraction(FarmingSpot spot, String option) {
        this.spot = spot;
        this.option = option;
    }
}

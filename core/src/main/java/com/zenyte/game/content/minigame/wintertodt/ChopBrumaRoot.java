package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.content.skills.woodcutting.AxeDefinitions;
import com.zenyte.game.content.skills.woodcutting.actions.Woodcutting;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.Optional;

/**
 * @author Corey
 * @since 23:18 - 27/07/2019
 */
public class ChopBrumaRoot implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!option.equalsIgnoreCase("chop")) {
            throw new RuntimeException("Invalid object option: " + option);
        }
        player.getActionManager().setAction(new ChopBrumaRootAction());
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BRUMA_ROOTS };
    }

    private static final class ChopBrumaRootAction extends Action {

        private int ticks;

        private Woodcutting.AxeResult axe;

        private boolean canChop() {
            if (Wintertodt.betweenRounds()) {
                player.sendFilteredMessage("There's no use for bruma roots at this time.");
                return false;
            }
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("Your inventory is too full to hold any more roots.");
                return false;
            }
            return true;
        }

        @Override
        public boolean start() {
            final Optional<Woodcutting.AxeResult> optionalAxe = Woodcutting.getAxe(player);
            if (!optionalAxe.isPresent()) {
                player.sendMessage("You do not have an axe which you have the woodcutting level to use.");
                return false;
            }
            axe = optionalAxe.get();
            if (!canChop()) {
                return false;
            }
            player.sendMessage("You swing your axe at the roots.");
            delay(2);
            return true;
        }

        @Override
        public boolean process() {
            if (ticks++ % 4 == 0)
                player.setAnimation(axe.getDefinition().getTreeCutAnimation());
            return canChop();
        }

        private boolean success() {
            final int level = player.getSkills().getLevel(SkillConstants.WOODCUTTING) - 1;
            return Math.min(Math.round(level * 0.4F) + 55, 90) > Utils.random(100);
        }

        private int chopDelay() {
            return Math.max(axe.getDefinition().getCutTime(), AxeDefinitions.STEEL.getCutTime()) - 3;
        }

        @Override
        public int processWithDelay() {
            if (!success()) {
                return chopDelay();
            }
            player.sendFilteredMessage("You get a bruma root.");
            player.getInventory().addItem(BrumaRoot.ROOT, 1);
            player.getSkills().addXp(SkillConstants.WOODCUTTING, player.getSkills().getLevelForXp(SkillConstants.WOODCUTTING) * 0.3);
            return chopDelay();
        }

        @Override
        public void stop() {
            player.setAnimation(Animation.STOP);
        }

        @Override
        public boolean interruptedByCombat() {
            return false;
        }
    }
}

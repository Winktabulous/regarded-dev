package com.zenyte.plugins.object;

import com.near_reality.game.item.CustomObjectId;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class HomeRejuvanationPool implements ObjectAction {
    private static final Graphics HEAL_GFX = new Graphics(1177);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        Analytics.flagInteraction(player, Analytics.InteractionType.ORNATE_POOL);
        if (option.equals("Drink")) {
            player.setGraphics(HEAL_GFX);
            player.heal(player.getMaxHitpoints());
            player.sendFilteredMessage("The Ornate pool of Rejuvenation restores your health, skills and stamina.");
            player.getVariables().setRunEnergy(100);
            for (int i = 0; i < SkillConstants.SKILLS.length; i++) {
                if (player.getSkills().getLevel(i) < player.getSkills().getLevelForXp(i)) {
                    player.getSkills().setLevel(i, player.getSkills().getLevelForXp(i));
                }
            }
            player.getToxins().reset();
            final long time = player.getNumericAttribute("box of restoration delay").longValue();
            if (time <= System.currentTimeMillis()) {
                player.getCombatDefinitions().setSpecialEnergy(100);
                player.addAttribute("box of restoration delay", System.currentTimeMillis() + (TimeUnit.SECONDS.toMillis(getUsageCooldownDurationInSeconds(player))));
            } else {
                final int totalSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(time - System.currentTimeMillis());
                final int seconds = totalSeconds % 60;
                final int minutes = totalSeconds / 60;
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain("You need to wait another " + (minutes == 0 ? (seconds + " seconds") : (minutes + " minutes")) + " until you can recharge your special energy again.");
                    }
                });
            }
        } else if (option.equalsIgnoreCase("Remove-skull")) {
            if (player.getVariables().getTime(TickVariable.SKULL) > TimeUnit.MINUTES.toTicks(20)) {
                player.getDialogueManager().start(new PlainChat(player, "You are currently under the effects of a permanent skull and cannot be unskulled."));
                return;
            }
            player.blockIncomingHits();
            player.getVariables().removeSkull();
            player.sendFilteredMessage("Your skull has been removed.");
        } else if (option.equalsIgnoreCase("Skull")) {
            promptSkull(player);
        }
    }

    public static void promptSkull(@NotNull final Player player) {
        player.getDialogueManager().finish();
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Obtain a PK skull?", new DialogueOption("Yes, skull me.", () -> player.getVariables().setSkull(true)), new DialogueOption("No, don't skull me."));
            }
        });
    }

    private int getUsageCooldownDurationInSeconds(final Player player) {
        final MemberRank memberRank = player.getMemberRank();
        if (memberRank.equalToOrGreaterThan(MemberRank.UBER)) {
            return 0;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.MYTHICAL)) {
            return 0;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.LEGENDARY)) {
            return 0;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.RESPECTED)) {
            return 30;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.EXTREME)) {
            return 60;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.EXPANSION)) {
            return 90;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.PREMIUM)) {
            return 120;
        }
        return 180;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                CustomObjectId.REJUVINATION_POOL
        };
    }
}

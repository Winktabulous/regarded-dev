package com.zenyte.game.content.skills.construction.objects.achievementgallery;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27. veebr 2018 : 1:29.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SpellbookAltar implements ObjectInteraction {

    private static final Animation PRAY_ANIM = new Animation(645);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ANCIENT_ALTAR, ObjectId.LUNAR_ALTAR, ObjectId.DARK_ALTAR_29149, 29150 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (option.equals("venerate")) {
            switch(object.getId()) {
                case 29147:
                case 29148:
                case 29149:
                    if (player.getCombatDefinitions().getSpellbook().ordinal() == (object.getId() - 29146)) {
                        player.sendMessage("You're already on the " + (object.getId() == 29147 ? "ancient" : object.getId() == 29148 ? "lunar" : "arceuus") + " spellbook.");
                        return;
                    }
                    player.lock(5);
                    player.setAnimation(PRAY_ANIM);
                    player.getCombatDefinitions().setSpellbook(Spellbook.getSpellbook((object.getId() - 29146)), true);
                    return;
                case 29150:
                    player.getDialogueManager().start(new VenerateD(player));
                    return;
            }
        }
    }

    private static final class VenerateD extends Dialogue {

        public VenerateD(final Player player) {
            super(player);
        }

        @Override
        public void buildDialogue() {
            final int id = player.getCombatDefinitions().getSpellbook().ordinal();
            options("Select a spellbook.", (id == 0 ? "<str>" : "") + "Normal spellbook.", (id == 1 ? "<str>" : "") + "Ancient spellbook.", (id == 2 ? "<str>" : "") + "Lunar spellbook.", (id == 3 ? "<str>" : "") + "Arceuus spellbook.").onOptionOne(() -> {
                pray(0);
                finish();
            }).onOptionTwo(() -> {
                pray(1);
                finish();
            }).onOptionThree(() -> {
                pray(2);
                finish();
            }).onOptionFour(() -> {
                pray(3);
                finish();
            });
        }

        private final void pray(final int id) {
            if (player.getCombatDefinitions().getSpellbook().ordinal() == id) {
                player.sendMessage("You're already on the " + (id == 1 ? "ancient" : id == 2 ? "lunar" : id == 3 ? "arceuus" : "normal") + " spellbook.");
                return;
            }
            player.lock(5);
            player.setAnimation(PRAY_ANIM);
            player.getCombatDefinitions().setSpellbook(Spellbook.getSpellbook(id), true);
        }
    }
}

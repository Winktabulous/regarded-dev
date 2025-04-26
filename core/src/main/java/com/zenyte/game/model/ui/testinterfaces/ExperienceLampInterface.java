package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.types.config.enums.Enums;

import java.util.function.IntConsumer;

/**
 * @author Tommeh | 8-11-2018 | 18:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ExperienceLampInterface extends Interface {
    public static final SoundEffect SOUND_EFFECT = new SoundEffect(2655, 0, 0);

    @Override
    protected void attach() {
        put(25, "Text");
        put(0, "Confirm");
    }

    @Override
    public void open(Player player) {
        final Object object = player.getTemporaryAttributes().get("experience_lamp_info");
        if (!(object instanceof Object[])) {
            return;
        }
        Object[] args = (Object[]) object;
        final int minimumLevel = (int) args[1];
        player.getVarManager().sendVar(261, minimumLevel);
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Text"), "Choose the stat you wish to be advanced!");
    }

    @Override
    protected void build() {
        bind("Confirm", (player, slot, __, ___) -> {
            final String name = Enums.SKILL_NAMES_ENUM.getValue(Enums.FAKE_XP_DROPS.getValue(slot + 1).orElseThrow()).orElseThrow();
            final int id = Skills.getSkill(name);
            final Object consumer = player.getTemporaryAttributes().get("experience_lamp_custom_handler");
            if (consumer instanceof IntConsumer) {
                ((IntConsumer) consumer).accept(id);
                return;
            }
            final Object object = player.getTemporaryAttributes().get("experience_lamp_info");
            if (!(object instanceof Object[] args)) {
                return;
            }
            final int experience = (int) args[0];
            final int minimumLevel = (int) args[1];
            if (experience < 0 || minimumLevel < 0) {
                return;
            }
            final int slotId = (int) args[2];
            final Item item = (Item) args[3];
            if (player.getInventory().getItem(slotId) != item) {
                return;
            }
            if (id < 0) {
                player.sendMessage("You haven't selected a skill.");
                return;
            }
            if (player.getSkills().getLevelForXp(id) < minimumLevel) {
                player.sendMessage("You need to select a skill that is at least level " + minimumLevel + ".");
                return;
            }
            player.getInventory().set(slotId, null);
            player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
            player.getSkills().addXp(id, experience);
            player.getPacketDispatcher().sendSoundEffect(SOUND_EFFECT);
            player.getDialogueManager().start(new PlainChat(player, "<col=000080>Your wish has been granted!</col><br><br>You have been awarded " + (experience * player.getExperienceRate(id)) + " " + Skills.getSkillName(id) + " experience!"));
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.EXPERIENCE_LAMP;
    }
}

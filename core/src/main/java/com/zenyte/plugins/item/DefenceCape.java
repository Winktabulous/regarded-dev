package com.zenyte.plugins.item;

import com.zenyte.game.content.RespawnPoint;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16/03/2019 02:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DefenceCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Toggle Effect", (player, item, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final boolean enabled = player.getBooleanAttribute("Skillcape ring of life teleport");
                plain("Your cape will" + (enabled ? " " : " not ") + "currently teleport you to safety should your health reach dangerous levels.");
                options("Would you like to " + (enabled ? "disable" : "enable") + " this feature?", new DialogueOption("Yes", () -> {
                    player.toggleBooleanAttribute("Skillcape ring of life teleport");
                    setKey(60);
                }), new DialogueOption("No"));
                plain(60, "Your cape will follow your instructions to" + (enabled ? " not " : " ") + "save you when your health is low.");
            }
        }));
        bind("Toggle Respawn", (player, item, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Choose a respawn destination.", new DialogueOption("Ardougne", () -> {
                    player.setRespawnPoint(RespawnPoint.ARDOUGNE);
                    player.sendMessage(Colour.RED.wrap("Your respawn location has now been changed to Ardougne."));
                }), new DialogueOption("Lumbridge - " + Colour.RS_RED.wrap("Default"), () -> {
                    player.setRespawnPoint(RespawnPoint.LUMBRIDGE);
                    player.sendMessage(Colour.RED.wrap("Your respawn location has now been changed to the default."));
                }));
            }
        }));
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.DEFENCE.getSkillCapes();
    }
}

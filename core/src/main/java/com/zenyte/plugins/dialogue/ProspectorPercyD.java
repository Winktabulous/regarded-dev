package com.zenyte.plugins.dialogue;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.events.LoginEvent;

import java.util.ArrayList;
import java.util.List;

import static com.zenyte.game.content.minigame.motherlode.MotherlodeArea.NUGGET;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class ProspectorPercyD extends Dialogue {
    private final List<String> options = new ArrayList<>();

    @Subscribe
    public static final void onLogin(final LoginEvent event) {
        final Player player = event.getPlayer();
        if (player.getBooleanAttribute("motherlode_sack_upgrade")) {
            player.getVarManager().sendBit(5556, 1);
        }
    }

    public ProspectorPercyD(final Player player, final int npcId) {
        super(player, npcId);
        if (!player.getBooleanAttribute("motherlode_upstairs")) options.add("Upper level access <col=600000>(40 nuggets)</col>");
        if (!player.getBooleanAttribute("motherlode_sack_upgrade")) options.add("Double space in paydirt sack <col=600000>(80 nuggets)</col>");
        options.add("Cancel.");
    }

    @Override
    public void buildDialogue() {
        if (options.size() == 0) {
            player.openShop("Prospector Percy's Nugget Shop");
            return;
        }
        options("What would you like to say?", "Is there anything else I can unlock here?", "Would you like to trade?", "Nevermind").onOptionOne(() -> setKey(5)).onOptionTwo(() -> player.openShop("Prospector Percy's Nugget Shop"));
        if (options.size() == 1) {
            npc(5, "No, you've unlocked everything I have to offer.");
            return;
        }
        options(5, "What would you like to buy?", options.toArray(new String[options.size()])).onOptionOne(() -> {
            if (!player.getBooleanAttribute("motherlode_upstairs")) {
                if (player.getInventory().containsItem(12012, 40) && !player.getBooleanAttribute("motherlode_upstairs") && player.getSkills().getLevelForXp(SkillConstants.MINING) >= 72) {
                    setKey(10);
                } else {
                    finish();
                    if (player.getBooleanAttribute("motherlode_upstairs")) {
                        player.sendMessage(Colour.RS_RED.wrap("You already unlocked upstairs access to the Motherlode Mine!"));
                        return;
                    }
                    if (player.getSkills().getLevelForXp(SkillConstants.MINING) < 72) {
                        player.sendMessage(Colour.RS_RED.wrap("You need at least level 72 Mining to buy this!"));
                        return;
                    }
                    if (!player.getInventory().containsItem(12012, 40)) {
                        player.sendMessage(Colour.RS_RED.wrap("You need 40 golden nuggets to do this!"));
                        return;
                    }
                }
            } else {
                if (player.getInventory().containsItem(12012, 80) && !player.getBooleanAttribute("motherlode_sack_upgrade")) {
                    setKey(20);
                } else {
                    finish();
                    if (player.getBooleanAttribute("motherlode_sack_upgrade")) {
                        player.sendMessage(Colour.RS_RED.wrap("You have already upgraded the sack!"));
                        return;
                    }
                    if (!player.getInventory().containsItem(12012, 80)) {
                        player.sendMessage(Colour.RS_RED.wrap("You need 80 golden nuggets to do this!"));
                        return;
                    }
                }
            }
        }).onOptionTwo(() -> {
            if (options.size() == 2) {
                return;
            }
            if (player.getInventory().containsItem(12012, 80) && !player.getBooleanAttribute("motherlode_sack_upgrade")) {
                setKey(20);
            } else {
                finish();
                if (player.getBooleanAttribute("motherlode_sack_upgrade")) {
                    player.sendMessage(Colour.RS_RED.wrap("You have already upgraded the sack!"));
                    return;
                }
                if (!player.getInventory().containsItem(12012, 80)) {
                    player.sendMessage(Colour.RS_RED.wrap("You need 80 golden nuggets to do this!"));
                    return;
                }
            }
        });
        options(10, "Purchase access for <col=006000>40 golden nuggets</col>?", "Yes", "No").onOptionOne(() -> {
            if (player.getInventory().containsItem(NUGGET.getId(), 40)) {
                player.getInventory().deleteItem(NUGGET.getId(), 40);
                player.putBooleanAttribute("motherlode_upstairs", true);
                player.sendMessage(Colour.RS_GREEN.wrap("You have purchased upstairs access to Motherlode Mine!"));
                player.getVarManager().sendBit(2086, 0);
            } else player.sendMessage(Colour.RS_RED.wrap("You need 40 golden nuggets to do this!"));
        });
        options(20, "Upgrade sack for <col=006000>80 golden nuggets</col>?", "Yes", "No").onOptionOne(() -> {
            if (player.getInventory().containsItem(NUGGET.getId(), 80)) {
                player.getInventory().deleteItem(NUGGET.getId(), 80);
                player.putBooleanAttribute("motherlode_sack_upgrade", true);
                player.getVarManager().sendBit(5556, 1);
                player.sendMessage(Colour.RS_GREEN.wrap("You have purchased 2x sack space in Motherlode Mine!"));
            } else player.sendMessage(Colour.RS_RED.wrap("You need 80 golden nuggets to do this!"));
        });
    }
}

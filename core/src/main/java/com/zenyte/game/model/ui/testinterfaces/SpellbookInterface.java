package com.zenyte.game.model.ui.testinterfaces;

import com.near_reality.cache.interfaces.teleports.Category;
import com.near_reality.cache.interfaces.teleports.TeleportsList;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.content.skills.magic.spells.arceuus.ThrallSpell;
import com.zenyte.game.content.skills.magic.spells.lunar.Vengeance;
import com.zenyte.game.content.skills.magic.spells.teleports.SpellbookTeleport;
import com.zenyte.game.content.skills.magic.spells.teleports.structures.HomeStructure;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingsInterface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.utilities.StringFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Kris | 07/01/2019 15:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpellbookInterface extends Interface {
    private static final Logger log = LoggerFactory.getLogger(SpellbookInterface.class);

    @Override
    protected void attach() {
        put(195, "Spell filters");
        put(195, 0, "Show combat spells");
        put(195, 1, "Show teleport spells");
        put(195, 2, "Show utility spells");
        put(195, 3, "Show spells you lack the magic level to cast");
        put(195, 4, "Show spells you lack the runes to cast");
        put(195, 5, "Show spells you lack the requirements to cast");
        put(195, 6, "Enable icon resizing");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Spell filters"), 0, 6, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("Show combat spells", player -> player.getSettings().toggleSetting(Setting.SHOW_COMBAT_SPELLS));
        bind("Show teleport spells", player -> player.getSettings().toggleSetting(Setting.SHOW_TELEPORT_SPELLS));
        bind("Show utility spells", player -> player.getSettings().toggleSetting(Setting.SHOW_UTILITY_SPELLS));
        bind("Show spells you lack the magic level to cast", player -> player.getSettings().toggleSetting(Setting.SHOW_SPELLS_YOU_LACK_THE_MAGIC_LEVEL_TO_CAST));
        bind("Show spells you lack the runes to cast", player -> player.getSettings().toggleSetting(Setting.SHOW_SPELLS_YOU_LACK_THE_RUNES_TO_CAST));
        bind("Show spells you lack the requirements to cast", player -> player.getSettings().toggleSetting(Setting.SHOW_SPELLS_YOU_LACK_THE_REQUIREMENTS_TO_CAST));
        bind("Enable icon resizing", player -> player.getSettings().toggleSetting(Setting.ENABLE_ICON_RESIZING));
    }

    @Override
    public DefaultClickHandler getDefaultHandler() {
        return ((player, componentId, slotId, itemId, optionId) -> {
            if (player.isLocked()) {
                return;
            }
            final String option = getOptionString(componentId, optionId);

            final String spellName = SpellDefinitions.getSpellName(componentId);
            if(componentId == 29) {
                player.sendMessage("Construction is incomplete, therefore this teleport is disabled.");
                return;
            }
            if (spellName != null) {
                final Category category = TeleportsList.getCategories().get(spellName.toLowerCase());
                if (category != null) { // instead of executing the spell, open the respective category.
                    player.getVarManager().sendVar(261, category.getId());
                    player.getTeleportsManager().setSelectedCategory(category);
                    player.getTeleportsManager().attemptOpen();
                    return;
                }
            }

            final DefaultSpell spell = Magic.getSpell(player.getCombatDefinitions().getSpellbook(), spellName, DefaultSpell.class);
            //The below block prevents flapping on the home teleport spell when multi-clicking it.
            if (spell != null
                    && spell.getSpellName().toLowerCase().endsWith("home teleport")
                    && player.getActionManager().getAction() instanceof HomeStructure.HomeTeleportAction
                    && player.getTemporaryAttributes().get("Current teleport spell") == ((SpellbookTeleport) spell).getDestination()
                    && Objects.equals(player.getTemporaryAttributes().get("Last Spellbook Click Option"), option)) {
                return;
            }
            player.getTemporaryAttributes().put("Last Spellbook Click Option", option);
            if (spell instanceof SpellbookTeleport) {
                player.getTemporaryAttributes().put("Current teleport spell", ((SpellbookTeleport) spell).getDestination());
            }
            if (spell instanceof Vengeance || spell instanceof ThrallSpell) {
                player.stop(Player.StopType.INTERFACES);
            } else {
                player.stop(Player.StopType.INTERFACES, Player.StopType.WALK, Player.StopType.ROUTE_EVENT);
            }
            //Exception for vengeance spell as it is a direct combat spell and shouldn't interrupt combat. And thralls.
            if (spell == null || !(spell instanceof Vengeance) && !(spell instanceof ThrallSpell)) {
                player.stop(Player.StopType.ACTIONS);
            }
            if (option.equals("Warnings")) {
                final String name = SpellDefinitions.getSpellName(componentId);
                if ("low level alchemy".equals(name) || "high level alchemy".equals(name)) {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            final int value = player.getVarManager().getBitValue(SettingsInterface.MINIMUM_ALCH_TRIGGER_VALUE_VARBIT_ID);
                            options("Warning trigger: " + StringFormatUtil.format(value), new DialogueOption("Change warning value", () -> {
                                finish();
                                player.sendInputInt("Set new warning value: ", val -> {
                                    player.getVarManager().sendBit(SettingsInterface.MINIMUM_ALCH_TRIGGER_VALUE_VARBIT_ID, val);
                                    player.sendMessage("Alchemy warning set to: " + val);
                                });
                            }), new DialogueOption("Cancel"));
                        }
                    });
                }
                return;
            }
            if (spell == null) {
                return;
            }
            try {
                spell.execute(player, optionId, option);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SPELLBOOK;
    }
}

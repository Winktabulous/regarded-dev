package com.zenyte.game.model.ui.testinterfaces;

import com.near_reality.game.content.middleman.MiddleManPlayerPromptKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.consumables.drinks.BarbarianMix;
import com.zenyte.game.content.consumables.drinks.Potion;
import com.zenyte.game.content.consumables.edibles.Food;
import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.content.minigame.duelarena.DuelSetting;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.lunar.CureMe;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zenyte.game.GameInterface.WORLD_MAP;
import static com.zenyte.game.world.entity.Toxins.ToxinType.*;

/**
 * @author Tommeh | 28-10-2018 | 16:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OrbsInterface extends Interface {
    private static final Logger log = LoggerFactory.getLogger(OrbsInterface.class);

    @Override
    protected void attach() {
        put(5, "Experience Tracker");
        put(8, "Cure Toxins");
        put(19, "Prayer");
        put(27, "Run");
        put(35, "Toggle special");
        put(45, "Open Store");
        put(53, "View World Map");
    }

    @Override
    public void open(Player player) {
        if (player.isOnMobile() && player.getBooleanSetting(Setting.MINIMIZE_MINIMAP)) {
            GameInterface.MINIMIZED_ORBS.open(player);
            return;
        }
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Toggle special", (player, slotId, itemId, option) -> {
            //In case of spoofing.
//            if (WildernessArea.isWithinWilderness(player)) {
//                return;
//            }
            if (player.isLocked()) {
                return;
            }
            final Duel duel = player.getDuel();
            if (duel != null && duel.hasRule(DuelSetting.NO_SPECIAL_ATTACK) && duel.inDuel()) {
                player.sendMessage("Use of special attacks has been turned off for this duel.");
                return;
            }
            player.getCombatDefinitions().setSpecial(!player.getCombatDefinitions().isUsingSpecial(), false);
        });
        bind("Experience Tracker", (player, slotId, itemId, option) -> {
            if (option == 1) {
                player.getSettings().toggleSetting(Setting.EXPERIENCE_TRACKER);
            } else if (option == 2) {
                if (player.isLocked() || player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
                    player.sendMessage("You can't configure your XP drops at the moment.");
                    return;
                }
                GameInterface.EXPERIENCE_TRACKER.open(player);
            } else if (option == 3) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        options(TITLE, new DialogueOption("Show x" + player.getSkillingXPRate() + " experience drops", () -> {
                            player.setXpDropsMultiplied(false);
                            player.setXPDropsWildyOnly(false);
                            player.getVarManager().sendVar(3504, 1);
                        }), new DialogueOption("Show x1 experience drops in Wilderness", () -> {
                            player.setXpDropsMultiplied(true);
                            player.setXPDropsWildyOnly(true);
                            player.getVarManager().sendVar(3504, WildernessArea.isWithinWilderness(player.getX(), player.getY()) ? player.getSkillingXPRate() : 1);
                        }), new DialogueOption("Show x1 experience drops everywhere", () -> {
                            player.setXpDropsMultiplied(true);
                            player.setXPDropsWildyOnly(false);
                            player.getVarManager().sendVar(3504, player.getSkillingXPRate());
                        }), new DialogueOption("Cancel"));
                    }
                });
            }
        });
        bind("Cure Toxins", (player, slotId, itemId, option) -> {
            if (player.isLocked() || player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
                player.sendMessage("You can't do that right now.");
                return;
            }
            final Toxins toxins = player.getToxins();
            final Toxins.ToxinType type = player.getVarManager().getBitValue(10151) > 0 ? PARASITE : toxins.isVenomed() ? VENOM : toxins.isPoisoned() ? POISON : toxins.isDiseased() ? DISEASE : null;
            if (type == null) {
                return;
            }
            final Inventory inventory = player.getInventory();
            for (int i = 0; i < 28; i++) {
                final Item item = inventory.getItem(i);
                if (item == null) {
                    continue;
                }
                final ItemDefinitions definitions = item.getDefinitions();
                if (definitions == null || definitions.isNoted()) {
                    continue;
                }
                switch (type) {
                    case PARASITE: {
                        final Consumable consumable = Consumable.consumables.get(item.getId());
                        if (consumable == Potion.RELICYMS_BALM || consumable == BarbarianMix.RELICYMS_MIX || consumable == Potion.SANFEW_SERUM) {
                            consumable.consume(player, item, i);
                            return;
                        }
                        continue;
                    }
                    case DISEASE: {
                        final String name = item.getName().toLowerCase();
                        if (name.contains("relicym's")) {
                            final Consumable consumable = Consumable.consumables.get(item.getId());
                            if (consumable != null) {
                                if (consumable == Potion.RELICYMS_BALM || consumable == BarbarianMix.RELICYMS_MIX) {
                                    consumable.consume(player, item, i);
                                    return;
                                }
                            }
                        }
                        continue;
                    }
                    case POISON: {
                            final Consumable consumable = Consumable.consumables.get(item.getId());
                            if (consumable == Potion.ANTIPOISON || consumable == Potion.SUPERANTIPOISON || consumable == Potion.ANTIDOTE_PLUS || consumable == Potion.ANTIDOTE_PLUS_PLUS || consumable == Food.STRANGE_FRUIT || consumable == Potion.GUTHIX_REST || consumable == Potion.SANFEW_SERUM || consumable == BarbarianMix.ANTIPOISON_MIX || consumable == BarbarianMix.ANTIPOISON_SUPERMIX || consumable == BarbarianMix.ANTIDOTE_PLUS_MIX) {
                                consumable.consume(player, item, i);
                                return;
                            }
                            continue;
                        }
                    case VENOM: {
                        final Consumable consumable = Consumable.consumables.get(item.getId());
                        if (consumable == Potion.ANTIPOISON || consumable == Potion.SUPERANTIPOISON || consumable == Potion.ANTIDOTE_PLUS_PLUS || consumable == BarbarianMix.ANTIPOISON_MIX || consumable == BarbarianMix.ANTIPOISON_SUPERMIX || consumable == Potion.ANTI_VENOM || consumable == Potion.ANTI_VENOM_PLUS) {
                            consumable.consume(player, item, i);
                            return;
                        }
                    }
                }
            }
            if (type == POISON || type == VENOM) {
                final CureMe spell = Magic.getSpell(Spellbook.LUNAR, "cure me", CureMe.class);
                if (spell != null && spell.canCast(player)) {
                    if (!spell.canUse(player)) {
                        return;
                    }
                    final SpellState state = new SpellState(player, spell.getLevel(), spell.getRunes());
                    if (state.check(false)) {
                        try {
                            spell.execute(player, 1, "Cast");
                        } catch (Exception e) {
                            log.error("", e);
                        }
                        return;
                    }
                }
            }
            player.sendMessage("You haven't got any potions to cure the " + type.toString().toLowerCase() + ".");
        });
        bind("Prayer", (player, slotId, itemId, option) -> {
            if (option == 1) {
                player.getPrayerManager().toggleQuickPrayers();
            } else if (option == 2) {
                if (player.isLocked() || player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
                    player.sendMessage("You can't set up your prayers at the moment.");
                    return;
                }
                player.getPrayerManager().openQuickPrayers();
            }
        });
        bind("Run", player -> player.setRun(!player.isRun()));
        bind("View World Map", (player, slotId, itemId, option) -> {
            if (player.isLocked() || (option == 3 && player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL))) {
                player.sendMessage("You can't do that right now.");
                return;
            }
            if (!player.getWorldMap().isVisible()) {
                if (option == 3) {
                    if (player.isUnderCombat()) {
                        player.sendMessage("You cannot open full-screen world map while under attack.");
                        return;
                    }
                }
                player.getWorldMap().setFullScreen(option == 3);
                WORLD_MAP.open(player);
            } else {
                player.getWorldMap().close();
            }
        });
        bind("Open Store", GameInterface.CREDIT_STORE::open);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ORBS;
    }

}

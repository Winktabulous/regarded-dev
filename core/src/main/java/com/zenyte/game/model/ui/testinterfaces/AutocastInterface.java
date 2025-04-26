package com.zenyte.game.model.ui.testinterfaces;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import static com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell.*;

/**
 * @author Kris | 20/10/2018 23:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AutocastInterface extends Interface {
    public static final EnumDefinitions AUTOCASTABLE_SPELLS_ENUM = EnumDefinitions.get(1986);
    private static final int VAR_AUTOCAST_PAGE = 664;
    private static final CombatSpell[] NORMAL_AUTOCASTABLE_SPELLS = new CombatSpell[] {WIND_STRIKE, WATER_STRIKE, EARTH_STRIKE, FIRE_STRIKE, WIND_BOLT, WATER_BOLT, EARTH_BOLT, FIRE_BOLT, WIND_BLAST, WATER_BLAST, EARTH_BLAST, FIRE_BLAST, WIND_WAVE, WATER_WAVE, EARTH_WAVE, FIRE_WAVE, WIND_SURGE, WATER_SURGE, EARTH_SURGE, FIRE_SURGE};
    private static final CombatSpell[] ANCIENT_AUTOCASTABLE_SPELLS = new CombatSpell[] {SMOKE_RUSH, SHADOW_RUSH, BLOOD_RUSH, ICE_RUSH, SMOKE_BURST, SHADOW_BURST, BLOOD_BURST, ICE_BURST, SMOKE_BLITZ, SHADOW_BLITZ, BLOOD_BLITZ, ICE_BLITZ, SMOKE_BARRAGE, SHADOW_BARRAGE, BLOOD_BARRAGE, ICE_BARRAGE};
    private static final CombatSpell[] ARCEUUS_AUTOCASTABLE_SPELLS = new CombatSpell[] {GHOSTLY_GRASP, SKELETAL_GRASP, UNDEAD_GRASP, DARK_DEMONBANE, INFERIOR_DEMONBANE, SUPERIOR_DEMONBANE};


    @Override
    protected void attach() {
        put(1, 0, "Close");
        put(1, "Set autocast spell");
    }

    @Override
    public void open(Player player) {
        final Optional<AutocastInterface.AutocastPage> page = AutocastPage.getPage(player);
        if (!page.isPresent()) {
            player.sendMessage("You can't choose a spell to autocast with that combination of weapon and spellbook.");
            return;
        }
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getVarManager().sendVar(VAR_AUTOCAST_PAGE, page.get().baseItemId);
        player.getPacketDispatcher().sendComponentSettings(getId(), getComponent("Set autocast spell"), 0, AUTOCASTABLE_SPELLS_ENUM.getLargestIntValue(), AccessMask.CLICK_OP1);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        if (replacement.isPresent()) return;
        GameInterface.COMBAT_TAB.open(player);
    }

    @Override
    protected void build() {
        bind("Close", player -> close(player));
        bind("Set autocast spell", ((player, slotId, itemId, option) -> {
            final int spellItemId = AUTOCASTABLE_SPELLS_ENUM.getIntValue(slotId);
            final ItemDefinitions definitions = ItemDefinitions.get(spellItemId);
            final Int2ObjectMap<Object> params = definitions.getParameters();
            if (params == null) throw new RuntimeException("Spell item parameters are null!");
            final String name = (String) params.get(601);
            final Spellbook playerSpellbook = player.getCombatDefinitions().getSpellbook();
            final Optional<CombatSpell> optionalCombatSpell = Magic.getCombatSpell(playerSpellbook, name.replaceAll("-", "").toLowerCase());
            if (!optionalCombatSpell.isPresent()) {
                return;
            }
            final CombatSpell spell = optionalCombatSpell.get();
            final Spellbook spellbook = spell.getSpellbook();
            final Optional<AutocastInterface.AutocastPage> optionalAutocastPage = AutocastPage.getPage(player);
            if (player.getCombatDefinitions().getSpellbook() != spellbook || !optionalAutocastPage.isPresent()) {
                player.sendMessage("You cannot autocast that spell on this spellbook.");
                return;
            }
            final AutocastInterface.AutocastPage page = optionalAutocastPage.get();
            if (!page.spells.contains(spell)) {
                switch (spell) {
                case IBAN_BLAST: 
                    player.sendMessage("You cannot autocast this spell without an Iban's staff.");
                    return;
                case MAGIC_DART: 
                    player.sendMessage("You cannot autocast this spell without a Slayer's staff.");
                    return;
                case FLAMES_OF_ZAMORAK: 
                    player.sendMessage("You cannot autocast this spell without a Zamorak staff.");
                    return;
                case CLAWS_OF_GUTHIX: 
                    player.sendMessage("You cannot autocast this spell without a Guthix staff.");
                    return;
                case SARADOMIN_STRIKE: 
                    player.sendMessage("You cannot autocast this spell without a Saradomin staff.");
                    return;
                default: 
                    throw new IllegalArgumentException("Unhandled exception for spell " + spell + " on spellbook " + spellbook + ".");
                }
            }
            final SpellState state = new SpellState(player, spell);


            player.getInterfaceHandler().sendInterface(GameInterface.COMBAT_TAB);

            if (!state.check(true)) {
                player.getCombatDefinitions().setAutocastSpell(null);
                return;
            }
            player.getCombatDefinitions().setAutocastSpell(spell);
            if (player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
                player.sendMessage("Autocast spell set to: " + StringFormatUtil.formatString(spell.toString().toLowerCase()) + ".");
            }
            player.getActionManager().forceStop();
        }));
    }

    public static final boolean canCast(@NotNull final Player player, @NotNull final CombatSpell spell) {
        final Spellbook spellbook = spell.getSpellbook();
        final Optional<AutocastInterface.AutocastPage> optionalAutocastPage = AutocastPage.getPage(player);
        if (player.getCombatDefinitions().getSpellbook() != spellbook || !optionalAutocastPage.isPresent()) {
            return false;
        }
        final AutocastInterface.AutocastPage page = optionalAutocastPage.get();
        if (!page.spells.contains(spell)) {
            return false;
        }
        return new SpellState(player, spell.getLevel(), spell.getRunes()).check(true);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.AUTOCAST_TAB;
    }


    private enum AutocastPage {

        ANCIENT(4675, new EnumBuilder<CombatSpell>()
                .add(ANCIENT_AUTOCASTABLE_SPELLS)
                .build(),
                (player, name) -> onSpellbook(player, Spellbook.ANCIENT) && (
                    name.equals("Eldritch nightmare staff") ||
                        name.equals("Volatile nightmare staff") ||
                        name.equals("Ancient sceptre") ||
                        name.endsWith("ancient sceptre") ||
                        name.equalsIgnoreCase("Staff of Light") ||
                        name.equals("Staff of the dead") ||
                        name.equals("Toxic staff of the dead") ||
                        name.equals("Nightmare staff") ||
                        name.equals("Master wand") ||
                        name.equals("Kodai wand") ||
                        name.equals("Ancient staff") ||
                        player.getCombatDefinitions().hasFullAhrimsAndDamned())),

        ARCEUUS(9013, new EnumBuilder<CombatSpell>()
                .add(ARCEUUS_AUTOCASTABLE_SPELLS)
                .build(),
                (player, name) -> onSpellbook(player, Spellbook.ARCEUUS) && (
                            name.equalsIgnoreCase("slayer's staff") ||
                            name.equalsIgnoreCase("Slayer's staff (e)") ||
                            name.equalsIgnoreCase("Ahrim's staff") ||
                            name.equalsIgnoreCase("Blue moon spear") ||
                            name.equalsIgnoreCase("staff of the dead") ||
                            name.equalsIgnoreCase("Toxic staff of the dead") ||
                            name.equalsIgnoreCase("Master wand") ||
                            name.equalsIgnoreCase("Kodai wand") ||
                            name.equalsIgnoreCase("Purging staff"))),

        IBANS_STAFF(1409, new EnumBuilder<CombatSpell>()
                .add(NORMAL_AUTOCASTABLE_SPELLS)
                .add(IBAN_BLAST)
                .build(),
                (player, name) -> onSpellbook(player, Spellbook.NORMAL) && (name.equals("Iban's staff") || name.equals("Iban's staff (u)"))),
        SLAYERS_STAFF(4170, new EnumBuilder<CombatSpell>()
                .add(NORMAL_AUTOCASTABLE_SPELLS)
                .add(CRUMBLE_UNDEAD, MAGIC_DART)
                .remove(WIND_STRIKE, WATER_STRIKE, EARTH_STRIKE, FIRE_STRIKE, WIND_BOLT, WATER_BOLT, EARTH_BOLT, FIRE_BOLT, WIND_BLAST, WATER_BLAST, EARTH_BLAST, FIRE_BLAST)
                .build(),
                (player, name) -> onSpellbook(player, Spellbook.NORMAL) && (name.equals("Slayer's staff") || name.equals("Slayer's staff (e)"))),
        ZAMORAK_STAFF(11791, new EnumBuilder<CombatSpell>()
                .add(NORMAL_AUTOCASTABLE_SPELLS)
                .add(CRUMBLE_UNDEAD, MAGIC_DART, FLAMES_OF_ZAMORAK)
                .build(),
                (player, name) -> onSpellbook(player, Spellbook.NORMAL) && (name.equals("Staff of the dead") || name.equals("Toxic staff of the dead") || name.equals("Zamorak staff"))),
        GUTHIX_STAFF(8841, new EnumBuilder<CombatSpell>()
                .add(NORMAL_AUTOCASTABLE_SPELLS)
                .add(CRUMBLE_UNDEAD, CLAWS_OF_GUTHIX)
                .remove(WIND_STRIKE, WATER_STRIKE, EARTH_STRIKE, FIRE_STRIKE, WIND_BOLT, WATER_BOLT, EARTH_BOLT, FIRE_BOLT, WIND_BLAST, WATER_BLAST, EARTH_BLAST, FIRE_BLAST)
                .build(),
                (player, name) -> onSpellbook(player, Spellbook.NORMAL) && (name.equals("Void knight mace") || name.equals("Guthix staff"))),
        STAFF_OG_BALANCE(ItemId.STAFF_OF_BALANCE, new EnumBuilder<CombatSpell>()
                .add(NORMAL_AUTOCASTABLE_SPELLS)
                .add(CRUMBLE_UNDEAD, MAGIC_DART, CLAWS_OF_GUTHIX)
                .build(),
                (player, name) -> onSpellbook(player, Spellbook.NORMAL) && name.equals("Staff of balance")
        ),
        SARADOMIN_STAFF(22296, new EnumBuilder<CombatSpell>()
                .add(NORMAL_AUTOCASTABLE_SPELLS)
                .add(CRUMBLE_UNDEAD, MAGIC_DART, SARADOMIN_STRIKE)
                .build(),
                (player, name) -> onSpellbook(player, Spellbook.NORMAL) && (name.equals("Saradomin staff") || name.equals("Staff of light"))),
        REGULAR(-1, new EnumBuilder<CombatSpell>()
                .add(NORMAL_AUTOCASTABLE_SPELLS)
                .build(),
                (player, name) -> onSpellbook(player, Spellbook.NORMAL) && !name.equals("Ancient staff"));

        private static final AutocastPage[] values = values();
        private final int baseItemId;
        private final ImmutableSet<CombatSpell> spells;
        private final BiPredicate<Player, String> predicate;

        /**
         * Checks whether the player is on the spellbook requested.
         *
         * @param player    the player whose spellbook to check.
         * @param spellbook the spellbook to test.
         * @return whether the player is on this spellbook or not.
         */
        private static boolean onSpellbook(final Player player, final Spellbook spellbook) {
            return player.getCombatDefinitions().getSpellbook().equals(spellbook);
        }

        /**
         * Gets the autocast page that should be opened based on player's spellbook and held weapon.
         *
         * @param player the player who's opening the interface.
         * @return an optional autocast page, or empty is absent.
         */
        private static final Optional<AutocastPage> getPage(final Player player) {
            final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON.getSlot());
            if (weapon == null) {
                return Optional.empty();
            }
            final String name = weapon.getName();
            for (final AutocastInterface.AutocastPage page : values) {
                if (page.predicate.test(player, name)) return Optional.of(page);
            }
            return Optional.empty();
        }

        /**
         * An immutable enumset builder of a generic type.
         *
         * @param <E> the generic type, must be an instance of an enum.
         */
        private static final class EnumBuilder<E extends Enum> {
            /**
             * The set used in building process of the enum.
             */
            private final Set<E> set = new HashSet<>();

            /**
             * Adds an array of values to the set if absent.
             *
             * @param e the value to enqueue to the set.
             * @return this builder for chaining.
             */
            private EnumBuilder<E> add(final E... e) {
                for (int i = e.length - 1; i >= 0; i--) {
                    set.add(e[i]);
                }
                return this;
            }

            /**
             * Removes an array of values to the set if prevent.
             *
             * @param e the value to remove from the set.
             * @return this builder for chaining.
             */
            private EnumBuilder<E> remove(final E... e) {
                for (int i = e.length - 1; i >= 0; i--) {
                    set.remove(e[i]);
                }
                return this;
            }

            /**
             * Builds the immutable set.
             *
             * @return immutable set of the generic type.
             */
            private ImmutableSet<E> build() {
                return Sets.immutableEnumSet(set);
            }
        }

        AutocastPage(int baseItemId, ImmutableSet<CombatSpell> spells, BiPredicate<Player, String> predicate) {
            this.baseItemId = baseItemId;
            this.spells = spells;
            this.predicate = predicate;
        }
    }
}

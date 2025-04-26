package com.zenyte.game.content.skills.magic;

import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.content.skills.magic.spells.teleports.SpellbookTeleport;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Kris | 11. dets 2017 : 2:56.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class Magic {
	private static final Logger log = LoggerFactory.getLogger(Magic.class);
	public static final Logger logger = LoggerFactory.getLogger(Magic.class);
	public static final int FIRE_RUNE = 554;
	public static final int WATER_RUNE = 555;
	public static final int AIR_RUNE = 556;
	public static final int EARTH_RUNE = 557;
	public static final int MIND_RUNE = 558;
	public static final int BODY_RUNE = 559;
	public static final int DEATH_RUNE = 560;
	public static final int NATURE_RUNE = 561;
	public static final int CHAOS_RUNE = 562;
	public static final int LAW_RUNE = 563;
	public static final int COSMIC_RUNE = 564;
	public static final int BLOOD_RUNE = 565;
	public static final int SOUL_RUNE = 566;
	public static final int ASTRAL_RUNE = 9075;
	public static final int WRATH_RUNE = 21880;
	public static final int STEAM_RUNE = 4694;
	public static final int MIST_RUNE = 4695;
	public static final int DUST_RUNE = 4696;
	public static final int SMOKE_RUNE = 4697;
	public static final int MUD_RUNE = 4698;
	public static final int LAVA_RUNE = 4699;
	public static final Map<String, MagicSpell> regularSpells = new Object2ObjectOpenHashMap<>();
	public static final Map<String, MagicSpell> ancientSpells = new Object2ObjectOpenHashMap<>();
	public static final Map<String, MagicSpell> lunarSpells = new Object2ObjectOpenHashMap<>();
	public static final Map<String, MagicSpell> arceuusSpells = new Object2ObjectOpenHashMap<>();
	public static final Map<String, MagicSpell> SPELLS_BY_NAME = new HashMap<>();

	public static final Optional<CombatSpell> getCombatSpell(@NotNull final Spellbook spellbook, @NotNull final String name) {
		final MagicSpell spell = spellbook.getSpellCollection().get(name);
		if (!(spell instanceof CombatSpell)) return Optional.empty();
		return Optional.of((CombatSpell) spell);
	}

	public static void add(final Class<? extends MagicSpell> c) {
		try {
			if (c.isEnum()) {
				final MagicSpell[] possibleValues = c.getEnumConstants();
				for (final MagicSpell spell : possibleValues) {
					if (spell == SpellbookTeleport.TELEPORT_TO_BOUNTY_TARGET) {
						for (final Spellbook spellbook : Spellbook.VALUES) {
							spellbook.getSpellCollection().put(spell.getSpellName(), spell);
						}
						continue;
					}
					final Spellbook spellbook = spell.getSpellbook();
					if (spellbook == null) {
						continue;
					}
					spellbook.getSpellCollection().put(spell.getSpellName(), spell);
				}
				return;
			}
			final MagicSpell spell = c.getDeclaredConstructor().newInstance();
			final Spellbook spellbook = spell.getSpellbook();
			spellbook.getSpellCollection().put(spell.getSpellName(), spell);
		} catch (final Exception e) {
			log.error("", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static void addUnsafe(final Class<?> c) {
		add((Class<? extends MagicSpell>) c);
	}

	@SuppressWarnings("unchecked")
	public static final <T extends MagicSpell> T getSpell(@NotNull final Spellbook spellbook, final String name, @NotNull final Class<T> type) {
		final MagicSpell spell = spellbook.getSpellCollection().get(name);
		if (type.isInstance(spell)) {
			return (T) spell;
		}
		return null;
	}


	public enum TeleportType {
		REGULAR, ITEM, OBJECT
    }
}

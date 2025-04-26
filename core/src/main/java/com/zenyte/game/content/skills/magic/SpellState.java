package com.zenyte.game.content.skills.magic;

import com.zenyte.game.content.skills.magic.resources.BlightedSackResource;
import com.zenyte.game.content.skills.magic.resources.BryophytasResource;
import com.zenyte.game.content.skills.magic.resources.InventoryResource;
import com.zenyte.game.content.skills.magic.resources.RunePouchResource;
import com.zenyte.game.content.skills.magic.resources.RuneResource;
import com.zenyte.game.content.skills.magic.resources.SecondaryRunePouchResource;
import com.zenyte.game.content.skills.magic.resources.StaffResource;
import com.zenyte.game.content.skills.magic.resources.TomeOfFireResource;
import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kris | 7. juuli 2018 : 00:34:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SpellState {
	private static final int[] AIR_RUNES = new int[] {11715, 4697, 4695, 4696};
	private static final int[] WATER_RUNES = new int[] {11716, 4694, 4695, 4698};
	private static final int[] EARTH_RUNES = new int[] {11717, 4696, 4699, 4698};
	private static final int[] FIRE_RUNES = new int[] {11718, 4694, 4697, 4699};
	private static final int[] CHAOS_RUNES = new int[] {11712};
	private static final int[] DEATH_RUNES = new int[] {11713};
	private static final int[] BLOOD_RUNES = new int[] {11714};
	private static final int[] IBANS_STAVES = new int[] {1409, 12658};
	private static final int[] SLAYER_STAVES = new int[] {12902, 21255, 12904, 22296, 4170, 11791, ItemId.STAFF_OF_BALANCE};
	private static final int[] SARADOMIN_STAVES = new int[] {2415, 22296};
	private static final int[] GUTHIX_STAVES = new int[] {2416, 8841, ItemId.STAFF_OF_BALANCE};
	private static final int[] ZAMORAK_STAVES = new int[] {2417, 12902, 12904, 11791};

	private static final int[] BLIGHTED_SACKS = new int[] { 24609, 24611, 24613, 24615, 24607, 24621, 26705, 26704 };
	private static final Int2ObjectOpenHashMap<int[]> ALT_RUNES;
	private static final Int2ObjectOpenHashMap<int[]> ALT_STAVES;

	private static final Map<String, Integer> BLIGHTED_SACKS_MAP = new HashMap<>();

	static {

		BLIGHTED_SACKS_MAP.put("bind", 24609);
		BLIGHTED_SACKS_MAP.put("snare", 24611);
		BLIGHTED_SACKS_MAP.put("entangle", 24613);
		BLIGHTED_SACKS_MAP.put("tele block", 24615);
		BLIGHTED_SACKS_MAP.put("teleport to bounty target", 24615);

		BLIGHTED_SACKS_MAP.put("ice rush", 24607);
		BLIGHTED_SACKS_MAP.put("ice burst", 24607);
		BLIGHTED_SACKS_MAP.put("ice blitz", 24607);
		BLIGHTED_SACKS_MAP.put("ice barrage", 24607);

		BLIGHTED_SACKS_MAP.put("vengeance", 24621);

		BLIGHTED_SACKS_MAP.put("wind surge", 26705);
		BLIGHTED_SACKS_MAP.put("water surge", 26705);
		BLIGHTED_SACKS_MAP.put("earth surge", 26705);
		BLIGHTED_SACKS_MAP.put("fire surge", 26705);

		BLIGHTED_SACKS_MAP.put("wind wave", 26704);
		BLIGHTED_SACKS_MAP.put("water wave", 26704);
		BLIGHTED_SACKS_MAP.put("earth wave", 26704);
		BLIGHTED_SACKS_MAP.put("fire wave", 26704);



		ALT_RUNES = new Int2ObjectOpenHashMap<int[]>(7);
		ALT_RUNES.put(Magic.AIR_RUNE, AIR_RUNES);
		ALT_RUNES.put(Magic.WATER_RUNE, WATER_RUNES);
		ALT_RUNES.put(Magic.EARTH_RUNE, EARTH_RUNES);
		ALT_RUNES.put(Magic.FIRE_RUNE, FIRE_RUNES);
		ALT_RUNES.put(Magic.CHAOS_RUNE, CHAOS_RUNES);
		ALT_RUNES.put(Magic.DEATH_RUNE, DEATH_RUNES);
		ALT_RUNES.put(Magic.BLOOD_RUNE, BLOOD_RUNES);
		ALT_STAVES = new Int2ObjectOpenHashMap<int[]>(5);
		ALT_STAVES.put(8843, GUTHIX_STAVES);
		ALT_STAVES.put(2417, ZAMORAK_STAVES);
		ALT_STAVES.put(2415, SARADOMIN_STAVES);
		ALT_STAVES.put(4170, SLAYER_STAVES);
		ALT_STAVES.put(1409, IBANS_STAVES);
	}

	private static final RuneResource STAFF_RESOURCE = new StaffResource();
	private static final RuneResource TOME_RESOURCE = new TomeOfFireResource();
	private static final RuneResource INVENTORY_RESOURCE = new InventoryResource();
	private static final RuneResource RUNE_POUCH_RESOURCE = new RunePouchResource();
	private static final RuneResource SECONDARY_RUNE_POUCH_RESOURCE = new SecondaryRunePouchResource();
	private static final RuneResource BRYOPHYTAS_RESOURCE = new BryophytasResource();
	private static final RuneResource BLIGHTED_RESOURCES = new BlightedSackResource();
	private static final RuneResource[] RESOURCES = new RuneResource[] {
			BLIGHTED_RESOURCES, STAFF_RESOURCE, TOME_RESOURCE, INVENTORY_RESOURCE,
			RUNE_POUCH_RESOURCE, SECONDARY_RUNE_POUCH_RESOURCE,
			BRYOPHYTAS_RESOURCE};

	public SpellState(final Player player, final MagicSpell spell) {
		this(player, spell.getLevel(), spell.getRunes());
		this.spell = spell;
	}

	public SpellState(final Player player, final int level, final Item... runes) {
		this.player = player;
		this.level = level;
		this.runes = runes;
		resources = new ArrayList<>(runes == null ? 0 : runes.length);
	}

	/**
	 * The player who's casting the spell.
	 */
	private final Player player;
	/**
	 * The level required to cast this spell.
	 */
	private final int level;
	/**
	 * The array of runes used by the spell.
	 */
	private final Item[] runes;
	/**
	 * A list of resources which point to the runes required by the spell.
	 */
	private final List<Resource> resources;
	@Nullable
	private MagicSpell spell;

	/**
	 * Checks whether the player can cast the given spell. Will inform the player if they cannot.
	 * 
	 * @return whether the player can cast this spell.
	 */
	public boolean check() {
		return check(true);
	}

	/**
	 * Checks whether the player has all of the required runes on them, either through the worn staff, in inventory, in rune pouch or by
	 * other means.
	 * 
	 * @return whether the player has *all* of the requested runes or not.
	 */
	public boolean check(final boolean inform) {
		if (player.getSkills().getLevel(SkillConstants.MAGIC) < level) {
			if (inform) {
				player.sendMessage("Your Magic level is not high enough for this spell.");
			}
			return false;
		}
		if (runes == null) {
			return true;
		}
		for (final Item rune : runes) {
			if (rune == null) {
				continue;
			}
			int amount = 0;
			final int id = rune.getId();
			final int[] staves = ALT_STAVES.get(id);
			if (staves != null) {
				final Item weapon = player.getWeapon();
				if (weapon == null || !ArrayUtils.contains(staves, weapon.getId())) {
					if (inform) {
						if (spell == CombatSpell.SARADOMIN_STRIKE) {
							player.sendMessage("You must be wielding the Staff of Saradomin or the Staff of Light to cast this spell.");
						} else if (spell == CombatSpell.CLAWS_OF_GUTHIX) {
							player.sendMessage("You must wield the Staff of Guthix or the Void Knight mace to cast this spell.");
						} else if (spell == CombatSpell.FLAMES_OF_ZAMORAK) {
							player.sendMessage("You must be wielding the Staff of Zamorak or the Staff of the Dead to cast this spell.");
						} else {
							player.sendMessage("You must be wielding the " + ItemDefinitions.getOrThrow(id).getName() + " to cast this spell.");
						}
					}
					return false;
				}
				continue;
			}
			final int requiredAmount = rune.getAmount();
			for (final RuneResource resource : RESOURCES) {

				if (resource == BLIGHTED_RESOURCES && WildernessArea.isWithinWilderness(player)) {

					if (spell != null) {
						var spellName = spell.getSpellName().toLowerCase();
						var sackToUse = BLIGHTED_SACKS_MAP.get(spellName);

						if (sackToUse != null) {
							if (player.getInventory().containsAnyOf(sackToUse)) {
								return true;
							}
						}
					}

					if (!player.getInventory().containsAnyOf(BLIGHTED_SACKS)) {
						continue;
					}

				}

				amount += checkRuneResource(resource, id, requiredAmount - amount);
				if (amount == requiredAmount) {
					break;
				}
			}
			if (amount != requiredAmount) {
				if (inform) {
					player.sendMessage("You do not have enough " + ItemDefinitions.getOrThrow(id).getName() + "s to cast this spell.");
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes all of the cached runes by {@link #check()} from the resources found when requested.
	 */
	public void remove() {

		if (player.getPerkManager().isValid(PerkWrapper.AUBURYS_APPRENTICE) && Utils.random(100) <= 10) {
			player.getPerkManager().consume(PerkWrapper.AUBURYS_APPRENTICE);
			return;
		}


		if(spell != null) {
			Integer resourceToConsume = BLIGHTED_SACKS_MAP.get(spell.getSpellName());
			if (resourceToConsume != null && resourceToConsume > 0 && WildernessArea.isWithinWilderness(player)) {
				if (player.getInventory().containsItem(resourceToConsume)) {
					player.getInventory().deleteItem(resourceToConsume, 1);
					return;
				}
			}
		}


		for (final Resource resource : resources) {
			resource.getSource().consume(player, resource.getRuneId(), resource.getAmount());
		}
	}

	/**
	 * Checks whether the player has the given rune in the given resource.
	 * 
	 * @param resource
	 *            the resource to check.
	 * @param id
	 *            the id of the rune.
	 * @param amount
	 *            the amount of the rune.
	 * @return the amount the player has in the given resource.
	 */
	private int checkRuneResource(final RuneResource resource, int id, final int amount) {
		final VarManager varManager = player.getVarManager();
		if (varManager.getBitValue(4145) == 1) {
			if (EnumDefinitions.get(55).getIntValue(id) > 0) {
				return amount;
			}
		}
		int totalAmount = 0;
		if (resource == BLIGHTED_RESOURCES && WildernessArea.isWithinWilderness(player) && spell != null) {
			Integer resourceToUse = BLIGHTED_SACKS_MAP.get(spell.getSpellName());
			if (resourceToUse != null && resourceToUse != 0) {
				if (player.getInventory().containsItem(resourceToUse)) {
					return amount;
				}
			}
		} else if (resource == STAFF_RESOURCE || resource == TOME_RESOURCE || resource == BRYOPHYTAS_RESOURCE) {
			final Resource result = getResource(resource, id, amount);
			if (result != null) {
				totalAmount = result.getAmount();
			}
		} else if (resource == INVENTORY_RESOURCE || resource == RUNE_POUCH_RESOURCE || resource == SECONDARY_RUNE_POUCH_RESOURCE) {
			final boolean withinLastManStanding = varManager.getBitValue(3923) == 1 || varManager.getBitValue(5314) == 1;
			if (withinLastManStanding) {
				id = EnumDefinitions.get(13).getIntValueOrDefault(id, id);
			}
			final Resource result = getResource(resource, id, amount);
			totalAmount = result == null ? 0 : result.getAmount();
			if (totalAmount != amount) {
				final int[] array = ALT_RUNES.get(id);
				if (array != null) {
					for (final int rune : array) {
						totalAmount += checkRuneResource(resource, rune, amount - totalAmount);
						if (totalAmount == amount) {
							break;
						}
					}
				}
			}
		}
		return totalAmount;
	}

	/**
	 * Creates a resource object if the player has any of the requested runes in the requested rune resource. If so, the resource is also
	 * added to the resources list.
	 * 
	 * @param source
	 *            the source to check for the given rune.
	 * @param id
	 *            the id of the rune.
	 * @param amount
	 *            the amount of the rune.
	 * @return the resource object, if player has any in the given resource, or null if none.
	 */
	private Resource getResource(final RuneResource source, final int id, final int amount) {
		final int[] affected = source.getAffectedRunes();
		if (affected != null && !ArrayUtils.contains(affected, id)) {
			return null;
		}
		final int held = source.getAmountOf(player, id, amount);
		if (held <= 0) {
			return null;
		}
		final Resource resource = new Resource(id, held, source);
		resources.add(resource);
		return resource;
	}
}

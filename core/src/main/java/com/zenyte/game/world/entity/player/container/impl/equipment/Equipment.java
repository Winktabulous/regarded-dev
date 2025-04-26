package com.zenyte.game.world.entity.player.container.impl.equipment;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.boons.impl.TorvasGluttony;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.parser.impl.ItemRequirements;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.CombatDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.action.combat.AttackStyleDefinition;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.ContainerPlugin;
import com.zenyte.game.world.region.area.plugins.EquipmentPlugin;
import com.zenyte.game.world.region.area.plugins.TempPlayerStatePlugin;
import com.zenyte.plugins.equipment.equip.EquipPlugin;
import com.zenyte.plugins.equipment.equip.EquipPluginLoader;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Equipment extends ContainerWrapper implements TempPlayerStatePlugin.State {
	private static final Logger log = LoggerFactory.getLogger(Equipment.class);
	public static final int SIZE = 14;
	public static final int INTERFACE = 387;
	private static final SoundEffect UNEQUIP_SOUNDEFFECT = new SoundEffect(2238);
	private final boolean tempVariant;

	/**
	 * The equipment constructor.
	 */
	public Equipment(final Player player) {
		this(player, false);
	}

	public Equipment(final Player player, boolean tempVariant) {
		this.player = player;
		this.tempVariant = tempVariant;
		container = new EquipmentContainer(ContainerPolicy.ALWAYS_STACK, ContainerType.EQUIPMENT, player);
	}

	public void setEquipment(final Equipment equipment) {
		for (int slot = 0; slot < container.getContainerSize(); slot++) {
			if (equipment.getItem(slot) == null) {
				continue;
			}
			((EquipmentContainer) container).silentSet(slot, equipment.getItem(slot));
		}
	}

	public int getStylesCount() {
		final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot());
		if (weaponId == -1) {
			return 3;
		}
		final ItemDefinitions definitions = ItemDefinitions.get(weaponId);
		int varbit = 0;
		if (definitions != null) {
			if (definitions.getInterfaceVarbit() > 0 && definitions.getInterfaceVarbit() < 28) {
				varbit = definitions.getInterfaceVarbit();
			}
		}
		return AttackStyleDefinition.values[varbit].getStyles().length;
	}

	private void onRefresh() {
		if(player != null && player.hasBoon(TorvasGluttony.class) && wearingFullTorva()) {
			player.setTorvaBoosted(true);
		} else {
			if (player != null) {
				player.setTorvaBoosted(false);
			}
		}
	}

	private final transient Item torvaHelm = new Item(ItemId.TORVA_FULLHELM);
	private final transient Item torvaPlate = new Item(ItemId.TORVA_PLATEBODY);
	private final transient Item torvaLegs = new Item(ItemId.TORVA_PLATELEGS);

	public boolean wearingFullTorva() {
		return isWearing(torvaHelm) && isWearing(torvaPlate) && isWearing(torvaLegs);
	}

	@Override
	public void refresh() {
		super.refresh();
		onRefresh();
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	@Override
	public void refresh(final int... slots) {
		super.refresh(slots);
		onRefresh();
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	@Override
	public void refreshAll() {
		super.refreshAll();
		onRefresh();
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	public final boolean wear(final int slotId) {
		if (player.isLocked() || player.isFinished() || player.isDead()) {
			return false;
		}
		final Inventory inventory = player.getInventory();
		final Item item = inventory.getItem(slotId);
		if (item == null) {
			return false;
		}
		final int id = item.getId();
		final ItemDefinitions definitions = item.getDefinitions();
		if (definitions == null) {
			return false;
		}
		final int equipmentSlot = definitions.getSlot();
		if (!item.isWieldable()) {
			player.sendMessage("You can't wield this!");
			return false;
		}
		final RegionArea area = player.getArea();
		if ((area instanceof EquipmentPlugin && !((EquipmentPlugin) area).equip(player, item, equipmentSlot)) || !player.getControllerManager().canEquipItem(item, equipmentSlot)) {
			player.sendMessage("You cannot equip that item in this area!");
			return false;
		}
		if (equipmentSlot == -1) {
			player.sendMessage("You cannot wield that; the item hasn't been defined to be equippable yet!");
			return false;
		}
		if (definitions.isNoted()) {
			return false;
		}
		final DiaryReward reward = DiaryReward.get(item.getId());
		if (reward != null && !DiaryUtil.eligibleFor(reward, player)) {
			player.sendMessage("You need to complete all of the " + reward.getComplexity().toString().toLowerCase() + " " + reward.getArea().getAreaName() + " diaries to wear this.");
			return false;
		}
		final ItemRequirements.ItemRequirement requirement = definitions.getRequirements();
		final List<ItemRequirements.ItemRequirement.PrimitiveRequirement> requirements = requirement == null ? null : requirement.getRequirements();
		if (requirements != null) {
			if (!requirements.isEmpty()) {
				final Skills skills = player.getSkills();
				if (requirements.size() >= 10) {
					if (!skills.isMaxed()) {
						player.sendMessage("You are not a high enough level to use this item.");
						player.sendMessage("You need to have all skills level 99.");
						return false;
					}
				}
				final Iterator<ItemRequirements.ItemRequirement.PrimitiveRequirement> it = requirements.iterator();
				ItemRequirements.ItemRequirement.PrimitiveRequirement entry;
				List<String> requiredLevels = null;
				int key;
				int value;
				while (it.hasNext()) {
					entry = it.next();
					key = entry.getSkill();
					value = entry.getLevel();
					if (skills.getLevelForXp(key) < value) {
						if (requiredLevels == null) {
							requiredLevels = new ArrayList<String>();
							requiredLevels.add("You are not a high enough level to use this item.");
						}
						final String skillName = Skills.getSkillName(key);
						requiredLevels.add("You need to have " + (Utils.startWithVowel(skillName) ? ("an") : "a") + " " + skillName + " level of " + value + ".");
					}
				}
				if (requiredLevels != null) {
					for (String requiredLevel : requiredLevels) {
						player.sendMessage(requiredLevel);
					}
					return false;
				}
			}
		}
		final Item weapon = player.getWeapon();
		final Item shield = player.getShield();
		Item worn = getItem(equipmentSlot);
		final EquipPlugin inventoryPlugin = EquipPluginLoader.plugins.get(id);
		if (inventoryPlugin != null) {
			if (!inventoryPlugin.handle(player, item, slotId, equipmentSlot)) {
				return false;
			}
		}
		if (worn != null && worn.getId() != item.getId()) {
			final EquipPlugin equipmentPlugin = EquipPluginLoader.plugins.get(worn.getId());
			if (equipmentPlugin != null) {
				if (!equipmentPlugin.handle(player, worn, -1, worn.getDefinitions().getSlot())) {
					return false;
				}
			}
		}
		if (equipmentSlot == 3) {
			if (weapon != null) {
				if (weapon.getId() == ItemId.CORMORANTS_GLOVE_22817 || weapon.getId() == ItemId.CORMORANTS_GLOVE) {
					player.sendMessage("You should speak to Alry to get this removed safely.");
					return false;
				}
			}
			final boolean twoHanded = definitions.isTwoHanded();
			if (twoHanded) {
				final int freeSlots = inventory.getFreeSlots();
				if (shield != null) {
					if (freeSlots == 0 && weapon != null) {
						player.sendMessage("You need some more free space to equip this.");
						return false;
					}
					final EquipPlugin shieldPlugin = EquipPluginLoader.plugins.get(shield.getId());
					if (shieldPlugin != null) {
						if (!shieldPlugin.handle(player, shield, -1, 5)) {
							return false;
						}
					}
					/**
					 * Switching the worn item to the shield if the player is wielding a shield and no weapon, additionally resetting the
					 * shield.
					 */
					if (weapon == null) {
						worn = getItem(5);
						set(5, null);
						refresh(5);
					} else {
						inventory.addItem(shield);
						set(5, null);
						refresh(5);
					}
				}
			}
			final CombatDefinitions combatDefinitions = player.getCombatDefinitions();
			combatDefinitions.resetAutocast();
			if (weapon != null && player.getVarManager().getValue(VarCollection.REMEMBER_COMBAT_SETTINGS.getId()) == 0) {
				final int currentAttackStyle = combatDefinitions.getStyle();
				final int varbit = definitions.getInterfaceVarbit();
				final int maximumStyles = id == -1 ? AttackStyleDefinition.UNARMED.getStyles().length : AttackStyleDefinition.values[varbit < 0 || varbit >= AttackStyleDefinition.values.length ? 0 : varbit].getStyles().length;
				if (currentAttackStyle == 2 && maximumStyles == 3) {
					combatDefinitions.setStyle(1);
				}
			}
			if (combatDefinitions.isUsingSpecial()) {
				player.getCombatDefinitions().setSpecial(false, false);
			}
		} else if (equipmentSlot == 5) {
			final boolean twoHanded = weapon != null && weapon.getDefinitions().isTwoHanded();
			if (twoHanded) {
				final EquipPlugin weaponPlugin = EquipPluginLoader.plugins.get(weapon.getId());
				if (weaponPlugin != null) {
					if (!weaponPlugin.handle(player, weapon, -1, 3)) {
						return false;
					}
				}
				worn = getItem(3);
				set(3, null);
				refresh(5);
				final CombatDefinitions combatDefinitions = player.getCombatDefinitions();
				if (combatDefinitions.isUsingSpecial()) {
					player.getCombatDefinitions().setSpecial(false, false);
				}
			}
		}
		if (definitions.isStackable() && worn != null && worn.getId() == definitions.getId()) {
			final int wornAmount = worn.getAmount();
			final int itemAmount = item.getAmount();
			if (itemAmount + wornAmount < 0) {
				final int amountToAdd = Integer.MAX_VALUE - wornAmount;
				worn.setAmount(Integer.MAX_VALUE);
				item.setAmount(itemAmount - amountToAdd);
				refresh(equipmentSlot);
				inventory.refresh(slotId);
				player.sendMessage("Not enough space to hold all of this item!");
			} else {
				set(equipmentSlot, new Item(item.getId(), wornAmount + itemAmount));
				refresh(equipmentSlot);
				inventory.set(slotId, null);
				inventory.refresh();
			}
		} else {
			if (worn != null && worn.getDefinitions().isStackable() && inventory.containsItem(worn.getId(), 1)) {
				final int wornAmount = worn.getAmount();
				final int itemAmount = inventory.getAmountOf(worn.getId());
				if (itemAmount + wornAmount < 0) {
					final int amountToAdd = Integer.MAX_VALUE - itemAmount;
					inventory.addItem(new Item(worn.getId(), amountToAdd));
					worn.setAmount(worn.getAmount() - amountToAdd);
					refresh(equipmentSlot);
					inventory.refresh(slotId);
					player.sendMessage("Not enough space in your inventory to unequip this item!");
				} else {
					set(equipmentSlot, item);
					refresh(equipmentSlot);
					inventory.set(slotId, null);
					inventory.addItem(worn);
					inventory.refresh();
				}
			} else {
				set(equipmentSlot, item);
				refresh(equipmentSlot);
				inventory.set(slotId, worn);
				inventory.refresh();
			}
		}
		if (equipmentSlot == 3 || equipmentSlot == 5) {
			player.getAppearance().resetRenderAnimation();
			player.getCombatDefinitions().refresh();
		}
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		return true;
	}

	/**
	 * Switching the worn item to the weapon if the player is wielding a two-handed weapon instead, additionally resetting the
	 * weapon.
	 */
	public final boolean unequipItem(final int slot) {
		if (player.isLocked() || player.isFinished() || player.isDead()) {
			return false;
		}
		final Item item = getItem(slot);
		if (item == null) {
			return false;
		}
		final ItemDefinitions definitions = item.getDefinitions();
		if (definitions == null) {
			return false;
		}
		final RegionArea area = player.getArea();
		if ((area instanceof EquipmentPlugin && !((EquipmentPlugin) area).unequip(player, item, slot))) {
			return false;
		}
		final int id = item.getId();
		int amount = item.getAmount();
		final Inventory inventory = player.getInventory();

		if (id == ItemId.CORMORANTS_GLOVE_22817 || id == ItemId.CORMORANTS_GLOVE) {
			player.sendMessage("You should speak to Alry to get this removed safely.");
			return false;
		}

		if (definitions.isStackable()) {
			final int inInventory = inventory.getAmountOf(id);
			if (inInventory == 0 && !inventory.hasFreeSlots()) {
				player.sendFilteredMessage("You need some more free space to unequip this.");
				return false;
			}
			if (inInventory + amount < 0) {
				amount = Integer.MAX_VALUE - inInventory;
			}
		} else {
			if (!inventory.hasFreeSlots()) {
				player.sendFilteredMessage("You need some more free space to unequip this.");
				return false;
			}
		}
		final EquipPlugin plugin = EquipPluginLoader.plugins.get(id);
		if (plugin != null) {
			if (!plugin.handle(player, item, -1, slot)) {
				return false;
			}
		}
		if (definitions.isNoted() || definitions.isStackable()) {
			final int inInventory = inventory.getAmountOf(id);
			if (inInventory > 0) {
				if (amount + inInventory < 0) {
					final int toRemove = Integer.MAX_VALUE - inInventory;
					item.setAmount(amount - toRemove);
					inventory.addItem(id, toRemove);
					return true;
				}
			}
		}
		if (amount == item.getAmount()) {
			set(slot, null);
			inventory.addItem(item);
		} else {
			player.sendMessage("Not enough space to hold all of this item!");
			set(slot, new Item(item.getId(), item.getAmount() - amount));
			inventory.addItem(new Item(item.getId(), amount));
		}
		refresh(slot);
		if (slot == 3) {
			final CombatDefinitions combatDefinitions = player.getCombatDefinitions();
			combatDefinitions.setAutocastSpell(null);
			combatDefinitions.refresh();
			player.getAppearance().resetRenderAnimation();
		}
		player.getPacketDispatcher().sendSoundEffect(UNEQUIP_SOUNDEFFECT);
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		return true;
	}

	public void set(final EquipmentSlot slot, final Item item) {
		set(slot.getSlot(), item);
	}

	public Item getItem(final EquipmentSlot slot) {
		return getItem(slot.getSlot());
	}

	public final boolean isWearing(final Item item) {
		return container.contains(item);
	}

	public int getId(final EquipmentSlot slot) {
		return getId(slot.getSlot());
	}


	public String getName(final EquipmentSlot slot) {
		int getId = getId(slot);
		return ItemDefinitions.nameOf(getId);
	}


	public String getName(int slot) {
		int getId = getId(slot);
		return ItemDefinitions.nameOf(getId);
	}
	/**
	 * Clears the container.
	 */
	@Override
	public void clear() {
		super.clear();
		refreshAll();
	}

	public void sendEquipmentStatsInterface() {
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 84);
		player.getInterfaceHandler().sendInterface(InterfacePosition.SINGLE_TAB, 85);
		player.getPacketDispatcher().sendClientScript(149, 5570560, 93, 4, 7, 1, -1, "Equip", "", "", "", "");
		player.getPacketDispatcher().sendComponentSettings(85, 0, 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10, AccessMask.DRAG_DEPTH1, AccessMask.DRAG_TARGETABLE);
		player.getBonuses().update();
	}

	public final int[] getStanceAnimations() {
		final int weaponSlot = getId(EquipmentSlot.WEAPON.getSlot());
		if (weaponSlot == -1) {
			return new int[] {808, 823, 819, 820, 821, 822, 824};
		}
		final ItemDefinitions definitions = ItemDefinitions.get(getId(EquipmentSlot.WEAPON.getSlot()));
		if (definitions == null) {
			return new int[] {808, 823, 819, 820, 821, 822, 824};
		}
		return new int[] {definitions.getStandAnimation() == 0 ? 808 : definitions.getStandAnimation(), 823, definitions.getWalkAnimation() == 0 ? 819 : definitions.getWalkAnimation(), 820, 821, 822, definitions.getRunAnimation() == 0 ? 824 : definitions.getRunAnimation()};
	}

	public final int getDefenceAnimation() {
		final int weaponSlot = getId(EquipmentSlot.WEAPON.getSlot());
		if (weaponSlot == -1) {
			return 424;
		}
		final ItemDefinitions definitions = ItemDefinitions.get(getId(EquipmentSlot.WEAPON.getSlot()));
		if (definitions == null) {
			return 424;
		}
		return definitions.getDefensiveAnimation();
	}

	public final int getAttackAnimation(final int attackType) {
		return getAttackAnimation(getId(EquipmentSlot.WEAPON), attackType);
	}

	public static final int getAttackAnimation(final int weaponId, final int attackType) {
		if (weaponId == -1) {
			return attackType == 3 ? 422 : 422 + attackType;
		}
		final ItemDefinitions definitions = ItemDefinitions.get(weaponId);
		if (definitions == null) {
			return 422 + attackType;
		}
		switch (attackType) {
		case 0:
			return definitions.getAccurateAnimation();
		case 1:
			return definitions.getAggressiveAnimation();
		case 2:
			return definitions.getControlledAnimation();
		default:
			return definitions.getDefensiveAnimation();
		}
	}

	private static final Map<Integer, Integer> EQUIPMENT_SCREEN_BUTTONS = ImmutableMap.<Integer, Integer>builder()
			.put(10, EquipmentSlot.HELMET.getSlot())
			.put(11, EquipmentSlot.CAPE.getSlot())
			.put(12, EquipmentSlot.AMULET.getSlot())
			.put(13, EquipmentSlot.WEAPON.getSlot())
			.put(14, EquipmentSlot.PLATE.getSlot())
			.put(15, EquipmentSlot.SHIELD.getSlot())
			.put(16, EquipmentSlot.LEGS.getSlot())
			.put(17, EquipmentSlot.HANDS.getSlot())
			.put(18, EquipmentSlot.BOOTS.getSlot())
			.put(19, EquipmentSlot.RING.getSlot())
			.put(20, EquipmentSlot.AMMUNITION.getSlot()).build();
	private static final Map<Integer, Integer> EQUIPMENT_TAB_BUTTONS = ImmutableMap.<Integer, Integer>builder()
			.put(15, EquipmentSlot.HELMET.getSlot())
			.put(16, EquipmentSlot.CAPE.getSlot())
			.put(17, EquipmentSlot.AMULET.getSlot())
			.put(18, EquipmentSlot.WEAPON.getSlot())
			.put(19, EquipmentSlot.PLATE.getSlot())
			.put(20, EquipmentSlot.SHIELD.getSlot())
			.put(21, EquipmentSlot.LEGS.getSlot())
			.put(22, EquipmentSlot.HANDS.getSlot())
			.put(23, EquipmentSlot.BOOTS.getSlot())
			.put(24, EquipmentSlot.RING.getSlot())
			.put(25, EquipmentSlot.AMMUNITION.getSlot()).build();

	public static final int getIndexByButton(final int interfaceId, final int componentId) {
		if (interfaceId == 84) {
			return EQUIPMENT_SCREEN_BUTTONS.getOrDefault(componentId, -1);
		} else if (interfaceId == 387) {
			return EQUIPMENT_TAB_BUTTONS.getOrDefault(componentId, -1);
		}
		return -1;
	}

	@Override
	public boolean isTempVariant() {
		return tempVariant;
	}

	@Override
	public TempPlayerStatePlugin.StateType tempType() {
		return TempPlayerStatePlugin.StateType.EQUIPMENT;
	}


	public final class EquipmentContainer extends Container {
		public EquipmentContainer(final ContainerPolicy policy, final ContainerType type, final Player player) {
			super(policy, type, Optional.of(player));
		}

		/**
		 * Sets a specific slot in the container to the requested object.
		 *
		 * @param slot
		 *            the slot to modify.
		 * @param item
		 *            the object to set it to, or null if removing.
		 */
		@Override
		public void set(final int slot, final Item item) {
			final Item oldItem = item == null ? items.remove(slot) : items.put(slot, item);
			if (oldItem != null) {
				weight -= oldItem.getDefinitions().getWeight();
			}
			if (player != null) {
				final RegionArea area = player.getArea();
				if (area instanceof ContainerPlugin) {
					final ContainerPlugin plugin = (ContainerPlugin) area;
					plugin.onContainerModification(player, this, oldItem, item);
				}
			}
			equipmentPlugin(oldItem, item);
			if (item == null) {
				modifiedSlots.add(slot);
				availableSlots.add(slot);
			} else {
				weight += item.getDefinitions().getWeight();
				modifiedSlots.add(slot);
				availableSlots.remove(slot);
			}
			if (player != null) {
				player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
				player.getBonuses().update();
				if (slot == 3 || slot == 5) {
					player.getAppearance().resetRenderAnimation();
				}
				if (slot == EquipmentSlot.WEAPON.getSlot()) {
					player.getCombatDefinitions().resetAutocastIfNotCached();
				}
			}
		}

		public void silentSet(final int slot, final Item item) {
			final Item oldItem = item == null ? items.remove(slot) : items.put(slot, item);
			if (oldItem != null) {
				weight -= oldItem.getDefinitions().getWeight();
			}
			if (item == null) {
				modifiedSlots.add(slot);
				availableSlots.add(slot);
			} else {
				weight += item.getDefinitions().getWeight();
				modifiedSlots.add(slot);
				availableSlots.remove(slot);
				try {
					final EquipPlugin plugin = EquipPluginLoader.plugins.get(item.getId());
					if (plugin == null) {
						return;
					}
					plugin.onLogin(player, item, slot);
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}

		@Override
		public void clear() {
			super.clear();
			player.getBonuses().update();
			player.getAppearance().resetRenderAnimation();
			player.getCombatDefinitions().setAutocastSpell(null);
		}
	}

	private void equipmentPlugin(final Item old, final Item newItem) {
		if (old != null) {
			final EquipPlugin inventoryPlugin = EquipPluginLoader.plugins.get(old.getId());
			if (inventoryPlugin != null) {
				inventoryPlugin.onUnequip(player, container, old);
			}
		}
		if (newItem != null) {
			final EquipPlugin inventoryPlugin = EquipPluginLoader.plugins.get(newItem.getId());
			if (inventoryPlugin != null) {
				inventoryPlugin.onEquip(player, container, newItem);
			}
		}
	}
}

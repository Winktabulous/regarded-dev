package com.zenyte.game.content.area.prifddinas.zalcano.combat;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

public class TephraCombatStrategy extends RangedCombat {

    private static final Animation TEPHRA_THROWING_ANIMATION = new Animation(401);

    public TephraCombatStrategy(Entity target, AmmunitionDefinitions defs) {
        super(target, defs);
        this.ammunition = AmmunitionDefinitions.IMBUED_TEPHRA;
    }

    @Override
    public int processWithDelay() {
        if (!isWithinAttackDistance()) {
            return 0;
        }
        if (!canAttack()) {
            return -1;
        }
        return super.processWithDelay();

    }


    @Override
    protected void animate() {
        player.setAnimation(TEPHRA_THROWING_ANIMATION);
    }

    @Override
    protected int getWeaponSpeed() {
        return 2;
    }

    @Override
    public final Hit getHit(final Player player, final Entity target, final double accuracyModifier, final double passiveModifier, double activeModifier, final boolean ignorePrayers) {
        return new Hit(player, getRandomHit(player, target, getMaxHit(player, passiveModifier, 1, ignorePrayers), accuracyModifier), HitType.ARMOUR);
    }


    @Override
    protected void grantExperience(int skill, double amount) {
        return;//We don't want xp here
    }



    @Override
    public int getMaxHit(Player player, double specialModifier, double activeModifier, boolean ignorePrayers) {
        int finalDamage = 0;

        float smithingBoost = ((float)player.getSkills().getLevel(SkillConstants.SMITHING)) / 10;
        float runecraftingBoost = ((float)player.getSkills().getLevel(SkillConstants.RUNECRAFTING)) / 10;
        boolean onBlueSymbol = false; // Testing

        if (player.getTemporaryAttributes().containsKey("BLUE_SYMBOL")) {
            onBlueSymbol = (boolean)player.getTemporaryAttributes().get("BLUE_SYMBOL");

            player.setGraphics(new Graphics(1726));
            player.sendFilteredMessage("You feel the symbol beneath your feet fill you with power.");
        }

        finalDamage += (smithingBoost + runecraftingBoost);
        if (finalDamage <= 10) {
            finalDamage = 10;
        }

        if (onBlueSymbol) {
            finalDamage += (((float) finalDamage / 100) * 50);
            player.sendMessage("");
        }

        if (player.hasPrivilege(PlayerPrivilege.DEVELOPER)) {
            finalDamage *= 4;
        }

        return finalDamage;
    }

    @Override
    protected int getAttackDistance() {
        return 6;
    }

    private boolean hasTephra() {
        return player.getInventory().containsItem(ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID)
                || player.getEquipment().getId(EquipmentSlot.WEAPON) == ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID;
    }

    @Override
    protected void dropAmmunition(int delay, boolean destroy) {
        depleteAmmunition(player.getEquipment(), EquipmentSlot.WEAPON, EquipmentSlot.WEAPON.getSlot(), 1, null);
    }

    @Override
    protected void depleteAmmunition(Equipment equipment, EquipmentSlot slot, int slotId, int amount, Item ammo) {
        final var itemId = equipment.getItem(EquipmentSlot.WEAPON);

        if (itemId != null && itemId.getId() == ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID) {
            final int ammoAmount = itemId.getAmount();
            if (ammoAmount > 1) {
                itemId.setAmount(ammoAmount - amount);
            } else {
                equipment.set(slot, null);
            }
            equipment.refresh(slotId);
        } else {
            player.getInventory().deleteItem(ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID, 1);
        }
    }

    @Override
    protected boolean outOfAmmo() {
        return !hasTephra();
    }

    @Override
    protected boolean checkPreconditions() {
        if (hasTephra()) {
            return true;
        }
        return super.checkPreconditions();
    }
}

package mgi.types.config.items;

import java.util.HashMap;

public final class WearableDefinition {

    WieldableDefinition weaponDefinition;
    private HashMap<Integer, Integer> requirements;
    private String bonuses;

    public HashMap<Integer, Integer> getRequirements() {
        return requirements;
    }

    public void setRequirements(HashMap<Integer, Integer> requirements) {
        this.requirements = requirements;
    }

    public String getBonuses() {
        return bonuses;
    }

    public void setBonuses(String bonuses) {
        this.bonuses = bonuses;
    }

    public WieldableDefinition getWeaponDefinition() {
        return weaponDefinition;
    }

    public void setWeaponDefinition(WieldableDefinition weaponDefinition) {
        this.weaponDefinition = weaponDefinition;
    }

    public WearableDefinition copy() {
        final WearableDefinition copy = new WearableDefinition();
        copy.setBonuses(bonuses);
        if (weaponDefinition != null)
            copy.setWeaponDefinition(weaponDefinition.copy());
        if (requirements != null)
            copy.setRequirements(new HashMap<>(requirements));
        return copy;
    }
}

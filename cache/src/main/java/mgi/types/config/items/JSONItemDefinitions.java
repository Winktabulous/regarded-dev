package mgi.types.config.items;

import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType;
import mgi.utilities.StringFormatUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public final class JSONItemDefinitions {

    private static final Logger log = LoggerFactory.getLogger(JSONItemDefinitions.class);

    private int id;
    private Boolean tradable;
    private float weight;
    private int slot = -1;
    private EquipmentType equipmentType;
    private String examine;
    WearableDefinition equipmentDefinition;

    public void parseMainTableData(final List<Element> dataRows) {
        for (final Element row : dataRows) {
            final String key = row.select("th").text();
            String value = row.select("td").text();
            if (value.trim().length() == 0) {
                continue;
            }
            if (key.toLowerCase().contains("release date")) {
                continue;
            }
            value = StringFormatUtil.sanitiseValue(value).trim();
            switch (key.toLowerCase().replace("?", "").trim()) {
                case "tradeable":
                case "tradable":
                    setTradable(Boolean.parseBoolean(value));
                    break;
                case "weight":
                    try {
                        setWeight((float) Double.parseDouble(value));
                    } catch (final Exception e) {
                        log.error("", e);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void parseBonusesTable(final List<Element> bonusRows) {
        int bonusIndex = 0;
        if (equipmentDefinition == null) {
            equipmentDefinition = new WearableDefinition();
        }
        final int[] bonuses = new int[14];
        for (final Element row : bonusRows) {
            final Elements values = row.select("td");
            for (final Element val : values) {
                if (val.attributes().hasKey("data-attr-param")) {
                    String attr = val.attr("data-attr-param");
                    if (attr.contains("image") || attr.contains("slot") || attr.contains("speed") || attr.contains(
                            "attackrange"))
                        continue;

                    final String value = StringFormatUtil.sanitiseValue(val.text());
                    int bonusValue = 0;
                    try {
                        bonusValue = Integer.parseInt(value);
                    } catch (final Exception e) {
                        bonusValue = 0;
                    }
                    System.err.println("Element: " + val + ", " + bonusValue + ", " + bonusIndex);
                    bonuses[bonusIndex] = bonusValue;
                    bonusIndex++;
                }
            }
            final Element speed = row.select("[src]").first();
            if (speed != null) {
                try {
                    if (speed.attr("alt").contains("speed")) {
                        if (equipmentDefinition == null) {
                            equipmentDefinition = new WearableDefinition();
                        }
                        if (equipmentDefinition.weaponDefinition == null) {
                            equipmentDefinition.weaponDefinition = new WieldableDefinition();
                        }
                        equipmentDefinition.weaponDefinition.setAttackSpeed(Integer.parseInt(speed.attr("alt")
                                .toLowerCase()
                                .replace("monster attack speed", "")
                                .replace(".png", "")
                                .trim()));
                    }
                } catch (final Exception e) {
                    log.error("", e);
                }
            }

        }

        final String string = Arrays.toString(bonuses);
        equipmentDefinition.setBonuses(string.substring(1, string.length() - 1));
    }

    public void parseItemSlot(final String input) {
        if (input.contains("Weapon slot")) {
            slot = 3;
        } else if (input.contains("Head slot")) {
            slot = 0;
        } else if (input.contains("Shield slot")) {
            slot = 5;
        } else if (input.contains("Feet slot") || input.contains("Boots slot")) {
            slot = 10;
        } else if (input.contains("Body slot") || input.contains("Torso slot")) {
            slot = 4;
        } else if (input.contains("Legwear slot") || input.contains("Legs slot")) {
            slot = 7;
        } else if (input.contains("Hands slot") || input.contains("Gloves slot")) {
            slot = 9;
        } else if (input.contains("Cape slot")) {
            slot = 1;
        } else if (input.contains("Ring slot")) {
            slot = 12;
        } else if (input.contains("Neck slot")) {
            slot = 2;
        } else if (input.contains("Ammunition slot") || input.contains("Ammo slot")) {
            slot = 13;
        } else if (input.contains("Two-handed slot") || input.contains("2h slot")) {
            slot = 3;
            if (equipmentDefinition == null) {
                equipmentDefinition = new WearableDefinition();
            }
            if (equipmentDefinition.weaponDefinition == null) {
                equipmentDefinition.weaponDefinition = new WieldableDefinition();
            }
            equipmentDefinition.weaponDefinition.setTwoHanded(true);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getTradable() {
        return tradable;
    }

    public void setTradable(Boolean tradable) {
        this.tradable = tradable;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public String getExamine() {
        return examine;
    }

    public WearableDefinition getEquipmentDefinition() {
        return equipmentDefinition;
    }

    public void setEquipmentDefinition(WearableDefinition equipmentDefinition) {
        this.equipmentDefinition = equipmentDefinition;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public JSONItemDefinitions copy() { // btw this okay? could prob be done easier but me lazy
        final JSONItemDefinitions copy = new JSONItemDefinitions();
        copy.setId(id);
        copy.setTradable(tradable);
        copy.setWeight(weight);
        copy.setSlot(slot);
        copy.setExamine(examine);
        copy.setEquipmentType(equipmentType);
        if (equipmentDefinition != null)
            copy.setEquipmentDefinition(equipmentDefinition.copy());
        return copy;
    }
}

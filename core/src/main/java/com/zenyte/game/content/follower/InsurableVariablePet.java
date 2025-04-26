package com.zenyte.game.content.follower;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;

import java.util.OptionalInt;

/**
 * @author Christopher
 * @since 4/14/2020
 */
public enum InsurableVariablePet {
    NOON(49),
    PET_DARK_CORE(285),
    PET_SMOKE_DEVIL(995),
    KALPHITE_PRINCESS(1686),
    PET_SNAKELING(1687),
    VETION_JR(1688),
    BABY_CHINCHOMPA(1689),
    RIFT_GUARDIAN(1690),
    ROCK_GOLEM(1691),
    OLMLET(1711),
    JAL_NIB_REK(1893),
    IKKLE_HYDRA(2176),
    YOUNGLLEF(2364),
    SRARACHA(3939),
    ;

    public static final InsurableVariablePet[] pets = values();
    private final IntList ids = new IntArrayList();

    InsurableVariablePet(final int enumId) {
        try {
            EnumDefinitions.getIntEnum(enumId).getValues().forEach((key, value) -> ids.add(value.intValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static InsurableVariablePet getPet(final int petId) {
        for (InsurableVariablePet pet : pets) {
            if (pet.ids.contains(petId)) {
                return pet;
            }
        }
        return null;
    }

    public int getBaseId() {
        return ids.getInt(0);
    }

    public OptionalInt getParentId() {
        final IntEnum parentEnum = EnumDefinitions.getIntEnum(985);
        for (final Integer id : ids) {
            if (parentEnum.getValues().containsValue(id.intValue())) {
                return OptionalInt.of(id);
            }
        }
        return OptionalInt.empty();
    }

    public IntList getIds() {
        return ids;
    }
}

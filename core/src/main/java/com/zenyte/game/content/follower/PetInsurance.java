package com.zenyte.game.content.follower;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tommeh | 24-11-2018 | 13:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PetInsurance {
    public static final Item INSURANCE_PRICE = new Item(995, 500000);
    public static final Item RECLAIM_PRICE = new Item(995, 1000000);
    private final transient Player player;
    private IntOpenHashSet insuredPets;
    private IntOpenHashSet claimablePets;

    public PetInsurance(final Player player) {
        this.player = player;
        insuredPets = new IntOpenHashSet();
        claimablePets = new IntOpenHashSet();
    }

    public void initialize(final PetInsurance insurance) {
        if (insurance == null) {
            return;
        }
        insuredPets = insurance.insuredPets;
        claimablePets = insurance.claimablePets;
    }

    public void insurePet(final int petItemId) {
        insuredPets.add(petItemId);
    }

    public boolean isInsured(final Pet pet) {
        return isInsured(pet.itemId());
    }

    public boolean isInsured(final int petItemId) {
        if (!insuredPets.contains(petItemId)) {
            final InsurableVariablePet variablePet = InsurableVariablePet.getPet(petItemId);
            if (variablePet == null) {
                return false;
            }
            for (final Integer id : variablePet.getIds()) {
                if (insuredPets.contains(id.intValue())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public List<Pet> getInsuredPets() {
        final ArrayList<Pet> list = new ArrayList<Pet>();
        for (final Integer id : insuredPets) {
            final Pet pet = PetWrapper.getByItem(id);
            if (pet == null) {
                continue;
            }
            list.add(pet);
        }
        return list;
    }
}

package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.follower.InsurableVariablePet;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.PetInsurance;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.StringFormatUtil;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tommeh | 24-11-2018 | 13:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public class PetInsuranceInterface extends Interface {

    private static final Logger log = Logger.getLogger(PetInsuranceInterface.class.getName());

    @Override
    protected void attach() {
        put(11, "Reclaim Insured Pet");
    }

    @Override
    public void open(Player player) {
        long insureValue = 0;
        long existingPetsValue = 0;
        final IntEnum e = EnumDefinitions.getIntEnum(985);
        loop:
        for (final Pet pet : player.getPetInsurance().getInsuredPets()) {
            if (pet == null) {
                continue;
            }
            OptionalInt p = e.getKey(pet.itemId());
            long mask = 0L;
            if (p.isPresent()) {
                mask = 1L << p.getAsInt();
                insureValue |= mask;
            }
            if (pet.hasPet(player)) {
                existingPetsValue |= mask;
                if (mask == 0) {
                    log.log(Level.WARNING, "Insured pet that doesn't exist in an enum? " + pet, new Throwable());
                }
                continue;
            }
            final InsurableVariablePet variablePet = InsurableVariablePet.getPet(pet.itemId());
            if (variablePet != null) {
                for (final Integer variableId : variablePet.getIds()) {
                    final Pet alternativePet = PetWrapper.getByItem(variableId);
                    if (alternativePet == null) {
                        continue;
                    }
                    if (alternativePet.hasPet(player)) {
                        existingPetsValue |= mask;
                        continue loop;
                    }
                }
            }
        }

        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendClientScript(6126, (int) insureValue, (int) (insureValue >>> 31), (int) (existingPetsValue & 2147483647), (int) (existingPetsValue >> 31), 0, 0, 0, 0, 0, 0, 0);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Reclaim Insured Pet"), 0, 400, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
        player.getAchievementDiaries().update(ArdougneDiary.CHECK_INSURED_PETS);
    }

    @Override
    protected void build() {
        bind("Reclaim Insured Pet", (player, slotId, itemId, option) -> {
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
                return;
            }

            final PetInsurance insurance = player.getPetInsurance();
            final Pet pet = PetWrapper.getByItem(itemId);
            if (pet == null || !insurance.isInsured(pet)) {
                return;
            }
            if (pet.hasPet(player)) {
                return;
            }
            final InsurableVariablePet variablePet = InsurableVariablePet.getPet(pet.itemId());
            if (variablePet != null) {
                for (final Integer variableId : variablePet.getIds()) {
                    final Pet alternativePet = PetWrapper.getByItem(variableId);
                    if (alternativePet == null) {
                        continue;
                    }
                    if (alternativePet.hasPet(player)) {
                        return;
                    }
                }
            }
            if (!player.getMemberRank().equalToOrGreaterThan(MemberRank.RESPECTED)) {
                if (!player.getInventory().containsItem(PetInsurance.RECLAIM_PRICE)) {
                    player.getDialogueManager().start(new NPCChat(player, 5906, "I'm afraid you don't have enough gold to reclaim the <col=00080>" + Objects.requireNonNull(NPCDefinitions.get(pet.petId())).getName() + "</col> pet."));
                    return;
                }
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Would you like to reclaim the <col=00080>" + NPCDefinitions.getOrThrow(pet.petId()).getName() + "</col> pet for " + (player.getMemberRank().equalToOrGreaterThan(MemberRank.RESPECTED) ? "free?" : (StringFormatUtil.format(PetInsurance.RECLAIM_PRICE.getAmount()) + " coins?")), "Yes.", "Nevermind.").onOptionOne(() -> {
                        if (!player.getInventory().checkSpace()) {
                            setKey(5);
                        } else {
                            setKey(10);
                            if (!player.getMemberRank().equalToOrGreaterThan(MemberRank.RESPECTED)) {
                                player.getInventory().deleteItem(PetInsurance.RECLAIM_PRICE);
                            }
                            player.getInventory().addItem(pet.itemId(), 1);
                            GameInterface.PET_INSURANCE.open(player);
                        }
                    }).onOptionTwo(() -> setKey(15));
                    npc(5906, "I'm afraid you don't have enough inventory space.", 5);
                    npc(5906, "Here's your pet!", 10);
                    player(15, "Nevermind.");
                }
            });
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PET_INSURANCE;
    }
}

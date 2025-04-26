package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.content.follower.impl.MiscPet;
import com.zenyte.game.content.follower.impl.SkillingPet;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 25/11/2018 19:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PetNPCPlugin extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final Pet pet = PetWrapper.getByPet(npc.getId());
            final Class<? extends Dialogue> dialogue = pet.dialogue();
            PetWrapper.startDialogue(player, npc, dialogue);
        });
        bind("Pick-up", (player, npc) -> {
            final Pet pet = PetWrapper.getByPet(npc.getId());
            if (!(npc instanceof Follower)) {
                return;
            }
            if (((Follower) npc).getOwner() != player) {
                player.sendMessage("You cannot touch others' pets.");
                return;
            }
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some free inventory space to pick the pet up.");
                return;
            }
            if (player.getCurrentHouse() != null) {
                player.getCurrentHouse().getCatsOnBlanket().remove(player);
            }
            player.setFollower(null);
            player.setAnimation(PetWrapper.DROP_ANIMATION);
            player.getInventory().addItem(pet.itemId(), 1);
            npc.finish();
        });
        bind("Locking", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    if (player.getBooleanAttribute("rift_guardian_colour_lock")) {
                        npc("Even if you bind essence at a Runecrafting Altar, I<br><br>will remain locked to this colour.");
                        options(TITLE, "I'd like you to adopt the altar's colour next time.", "Okay, please stay locked to this colour.").onOptionOne(() -> {
                            player.addAttribute("rift_guardian_colour_lock", 0);
                            setKey(5);
                        }).onOptionTwo(() -> setKey(10));
                        player(5, "I'd like you to adopt the altar's colour next time.");
                        npc("Very well, I will adopt the altar's colour next time you<br><br>bind essence at a " +
                                "Runecrafting Altar.");
                        player(10, "Okay, please stay locked to this colour.");
                        npc("Very well, I will remain locked to this colour, even if<br><br>you bind essence at a Runecrafting Altar.");
                    } else {
                        npc("If you take me to a Runecrafting Altar, I will adopt the<br><br>altar's colour as you " +
                                "bind essence there.");
                        options(TITLE, "I want you to stay this colour instead.", "Okay, carry on adopting the colour of altars.").onOptionOne(() -> {
                            player.addAttribute("rift_guardian_colour_lock", 1);
                            setKey(5);
                        }).onOptionTwo(() -> setKey(10));
                        player(5, "I want you to stay this colour instead.");
                        npc("Very well, I will remain locked to this colour, even if<br><br>you bind essence at a Runecrafting Altar.");
                        player(10, "I want you to carry on adopting the colour of altars.");
                        npc("Very well, I will adopt the altar's colour next time you bind essence at a Runecrafting " +
                                "Altar.");
                    }
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        final IntOpenHashSet list = new IntOpenHashSet();
        for (final BossPet pet : BossPet.VALUES) {
            list.add(pet.petId());
        }
        for (final SkillingPet pet : SkillingPet.VALUES) {
            list.add(pet.petId());
        }
        for (final MiscPet pet : MiscPet.VALUES) {
            list.add(pet.petId());
        }
        list.add(6717);
        return list.toIntArray();
    }
}

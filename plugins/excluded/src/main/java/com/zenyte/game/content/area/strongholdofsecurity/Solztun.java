package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 27/11/2018 11:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Solztun extends NPCPlugin {

    private static final SoundEffect SOUND = new SoundEffect(1651);

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("<col=000080>Can you hear me?");
                    if (player.getEquipment().getId(EquipmentSlot.AMULET) == 552) {
                        player("Of course! I'm wearing an amulet of ghostspeak.");
                    } else {
                        player("...I can, but I'm not wearing an amulet of ghostspeak... A-am I dead?");
                    }
                    npc("<col=000080>I'm no ghost! I'm a spirit, I'm injecting my thoughts into your head.");
                    player("Oh, that's... interesting. Also rather disturbing.");
                    npc("<col=000080>Try not to think about it.");
                    ContainerWrapper[] wrappers = new ContainerWrapper[] { player.getInventory(), player.getEquipment() };
                    Item sceptre = null;
                    loop: for (ContainerWrapper wrapper : wrappers) {
                        for (Item item : wrapper.getContainer().getItems().values()) {
                            if (item == null) {
                                continue;
                            }
                            if (item.getId() == 9013) {
                                sceptre = item;
                                break loop;
                            }
                        }
                    }
                    List<DialogueOption> list = new ArrayList<>(4);
                    if (sceptre != null) {
                        list.add(new DialogueOption("Could you imbue my sceptre?", key(50)));
                    }
                    list.add(new DialogueOption("Who are you?", key(100)));
                    list.add(new DialogueOption("Why are you here?", key(200)));
                    list.add(new DialogueOption("Bye!", key(300)));
                    DialogueOption[] options = list.toArray(new DialogueOption[0]);
                    options(TITLE, options);
                    player(50, "Could you imbue my sceptre?");
                    npc("<col=000080>Of course I can... But first, you must show me whatever treasure lies within that cradle and make sure that your account is safe.");
                    DialogueOption[] sceptreOptions = new DialogueOption[] { new DialogueOption("Is my account safe?", key(500)), new DialogueOption("Is this the treasure?", key(600)) };
                    options(TITLE, sceptreOptions);
                    player(500, "Is my account safe?");
                    npc("<col=000080>It seems you have the authenticator set up, that's perfect! Now just show me the treasure.");
                    options(TITLE, sceptreOptions);
                    player(600, "Is this the treasure?");
                    // or 9006
                    item(new Item(9005), "You show the fancy boots to Solztun.");
                    npc("<col=000080>Oh marvellous! If only I had corporeal form I'd be able<br><col=000080>to wear them, but alas...");
                    player("So could you imbue my sceptre?");
                    Item skullSceptre = sceptre;
                    npc("<col=000080>Of course, of course!").executeAction(() -> {
                        if (skullSceptre != null) {
                            player.getPacketDispatcher().sendSoundEffect(SOUND);
                            skullSceptre.setId(21276);
                            player.getInventory().refreshAll();
                            player.getEquipment().refreshAll();
                        }
                    });
                    npc("<col=000080>There you go, now that I've imbued it, once it runs<br><col=000080>out of charges it will no longer break and it can be<br><col=000080>charged with any piece of the sceptre you find.");
                    player("Thank you very much! How many charges does each piece give?");
                    npc("<col=000080>Each piece will increase the charges of the sceptre by 3.<br><col=000080>If you disable your authenticator, the imbue on the<br><col=000080>sceptre will be removed.");
                    player("Fantastic! It's best I keep the authenticator activated<br>then. Good bye Solztun!");
                    npc("<col=000080>Stay safe, adventurer.");
                    player(100, "Who are you?");
                    npc("<col=000080>As I said, I am Solztun, the greatest barbarian explorer! I came to explore this place, but unfortunately I died long before I made it to the treasure.");
                    player("Nobody came to help?!");
                    npc("<col=000080>Oh no, they had no idea I was hurt, I guess they assumed I'd found the treasure and made a new life for myself.");
                    player("Were you not a fan of the barbarian life style?");
                    npc("<col=000080>It was all death and pillaging, I prefer the finer things in life, like exploration and discovery!");
                    player("Ah, I too enjoy those.");
                    options(TITLE, options);
                    player(200, "Why are you here?");
                    npc("<col=000080>I came for treasure!");
                    player("Oh right, you were looking for what's in that cradle?");
                    npc("<col=000080>Yes, but alas, I'm dead.");
                    player("I guess I could show you... for a price.");
                    npc("<col=000080>Hmm... I don't have much to offer, but if you happen upon a skull sceptre, I could make it stronger.");
                    player("Stronger you say... In what way?");
                    npc("<col=000080>Currently the sceptre is fragile: it breaks once all charges have been used. Show me the treasure and I could imbue it so it can be recharged with any sceptre piece.");
                    player("You, my dead friend, have a deal!");
                    options(TITLE, options);
                    player(300, "Bye!");
                    npc("<col=000080>Stay safe, adventurer.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SOLZTUN };
    }
}

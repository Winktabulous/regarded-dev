package com.zenyte.game.content.tombsofamascut.encounter;

import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

public class TOACommands {

    static {
        TOACommands.register();
    }

    public static void register() {
        new GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "toatest", (p, args) -> {
            final TOALobbyParty party = new TOALobbyParty(p);
            TOALobbyParty.addLobbyParty(party);
            p.getTOAManager().enterRaid();
        });
        new GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "starttoa", (p, args) -> {
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    options(
                            new DialogueOption("Akkha", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.HET_BOSS);
                             }),
                            new DialogueOption("Ba-Ba", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.APMEKEN_BOSS);
                            }),
                            new DialogueOption("Zebak", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.CRONDIS_BOSS);
                            }),
                            new DialogueOption("Kephri", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.SCABARIS_BOSS);
                            }),
                            new DialogueOption("Wardens", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.WARDENS_FIRST_ROOM);
                            })
                    );
                }
            });
        });

    }
}

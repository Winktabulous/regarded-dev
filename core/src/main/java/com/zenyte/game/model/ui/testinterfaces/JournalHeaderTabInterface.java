package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfaceHandler;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 12/05/2019 | 18:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JournalHeaderTabInterface extends Interface {

    @Override
    protected void attach() {
        put(3, "Character Summary");
        put(8, "Quests");
        put(13, "Achievement Diary");
        put(18, "Game Noticeboard");
        put(28, "Server Events");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Quests", player -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.QUEST_TAB));
        bind("Achievement Diary", player -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.ACHIEVEMENT_DIARIES));
        bind("Character Summary", player -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.CHARACTER_SUMMARY));
        bind("Game Noticeboard", player -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.GAME_NOTICEBOARD));
        bind("Server Events", player -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.SERVER_EVENTS));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.JOURNAL_HEADER_TAB;
    }
}

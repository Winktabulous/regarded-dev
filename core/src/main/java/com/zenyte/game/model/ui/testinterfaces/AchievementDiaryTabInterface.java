package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.enums.EnumDefinitions;

/**
 * @author Kris | 19/10/2018 01:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class AchievementDiaryTabInterface extends Interface {

    private static final int DIARY_ENUM = 595;

    @Override
    protected void attach() {
        put(2, "Diaries");
        put(2, 0, "Open Karamja Diary");
        put(2, 1, "Open Ardougne Diary");
        put(2, 2, "Open Falador Diary");
        put(2, 3, "Open Fremennik Diary");
        put(2, 4, "Open Kandarin Diary");
        put(2, 5, "Open Desert Diary");
        put(2, 6, "Open Lumbridge & Draynor Diary");
        put(2, 7, "Open Morytania Diary");
        put(2, 8, "Open Varrock Diary");
        put(2, 9, "Open Wilderness Diary");
        put(2, 10, "Open Western Provinces Diary");
        put(2, 11, "Open Kourend & Kebos Diary");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(getInterface().getId(), 33, PaneType.JOURNAL_TAB_HEADER, true);
        player.getPacketDispatcher().sendComponentSettings(getId(), getComponent("Diaries"), 0, EnumDefinitions.get(DIARY_ENUM).getSize(), AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("Open Ardougne Diary", player -> Diary.sendDiary(player, ArdougneDiary.MAP));
        bind("Open Falador Diary", player -> Diary.sendDiary(player, FaladorDiary.MAP));
        bind("Open Fremennik Diary", player -> Diary.sendDiary(player, FremennikDiary.MAP));
        bind("Open Karamja Diary", player -> Diary.sendDiary(player, KaramjaDiary.MAP));
        bind("Open Kandarin Diary", player -> Diary.sendDiary(player, KandarinDiary.MAP));
        bind("Open Desert Diary", player -> Diary.sendDiary(player, DesertDiary.MAP));
        bind("Open Lumbridge & Draynor Diary", player -> Diary.sendDiary(player, LumbridgeDiary.MAP));
        bind("Open Morytania Diary", player -> Diary.sendDiary(player, MorytaniaDiary.MAP));
        bind("Open Varrock Diary", player -> Diary.sendDiary(player, VarrockDiary.MAP));
        bind("Open Wilderness Diary", player -> Diary.sendDiary(player, WildernessDiary.MAP));
        bind("Open Western Provinces Diary", player -> Diary.sendDiary(player, WesternProvincesDiary.MAP));
        bind("Open Kourend & Kebos Diary", player -> Diary.sendDiary(player, KourendDiary.MAP));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ACHIEVEMENT_DIARY_TAB;
    }
}

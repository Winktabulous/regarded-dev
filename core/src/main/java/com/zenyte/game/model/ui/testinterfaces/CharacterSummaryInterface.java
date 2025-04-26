package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfaceHandler;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;

import static com.zenyte.game.GameInterface.*;

/**
 * @author Tommeh | 28-10-2018 | 19:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public class CharacterSummaryInterface extends Interface {

    static {
        VarManager.appendPersistentVarbit(12933);
    }

    @Override
    protected void attach() {
        put(2, "Component layer");
        put(3, "Element layer");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface().getId(), 33, PaneType.JOURNAL_TAB_HEADER, true);
        player.getPacketDispatcher().sendClientScript(3954, getComponent("Component layer"), getComponent("Element layer"), player.getSkills().getCombatLevel());
        player.getPacketDispatcher().sendComponentSettings(getInterface(),
                getComponent("Element layer"),
                3,
                7,
                AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4
        );
    }

    @Override
    protected void build() {
        bind("Element layer", (player, slotId, itemId, option) -> {
            if (player.isLocked()) return;
            switch (slotId) {
                case 3 -> COMP_PROGRESS.open(player);
                case 4 -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.ACHIEVEMENT_DIARIES);
                case 5 -> {
                    switch (option) {
                        case 1 -> CA_OVERVIEW.open(player);
                        case 2 -> CA_BOSS_OVERVIEW.open(player);
                        case 3 -> CA_TASKS.open(player);
                        case 4 -> CA_REWARDS.open(player);
                    }
                }
                case 6 -> COLLECTION_LOG.open(player);
                case 7 -> {
                    player.getVarManager().flipBit(12933);
                    player.getPacketDispatcher().sendClientScript(3970, getId() << 16 | getComponent("Component layer"),
                            getId() << 16 | getComponent("Element layer"), player.getVariables().getPlayTime() / 100);
                }
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return CHARACTER_SUMMARY;
    }
}

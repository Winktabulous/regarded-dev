package com.zenyte.game.content.bountyhunter;

import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

/**
 * @author Kris | 26/03/2019 17:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WildernessOverlay extends Interface {
    @Override
    protected void attach() {
        put(47, "Red circle");

        /*put(6, "Bounty Hunter overlay");//Component updated.
        put(20, "Deadman mode final overlay");//Component updated.
        put(57, "Skip Bounty Hunter target");//Component updated.
        put(54, "Target name");//Component updated.
        put(55, "Wilderness level and target combat");//Component updated.
        put(43, "Current rogue streak");//Component updated.
        put(44, "Current hunter streak");//Component updated.
        put(46, "Rogue streak record");//Component updated.
        put(45, "Hunter streak record");//Component updated.
        put(58, "Minimise");//Component updated.
        put(3, "Wilderness level");//Component updated.*/
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentVisibility(getInterface(), getComponent("Red circle"), true);
        /* BH not used so lets skip. */
        /*if (GameConstants.BOUNTY_HUNTER) {
            final BountyHunter bountyhunter = player.getBountyHunter();
            dispatcher.sendComponentText(getInterface(), getComponent("Current rogue streak"), bountyhunter.getValue(BountyHunterVar.CURRENT_ROGUE_KILLS));
            dispatcher.sendComponentText(getInterface(), getComponent("Current hunter streak"), bountyhunter.getValue(BountyHunterVar.CURRENT_HUNTER_KILLS));
            dispatcher.sendComponentText(getInterface(), getComponent("Rogue streak record"), bountyhunter.getValue(BountyHunterVar.ROGUE_KILLS_RECORD));
            dispatcher.sendComponentText(getInterface(), getComponent("Hunter streak record"), bountyhunter.getValue(BountyHunterVar.HUNTER_KILLS_RECORD));
            dispatcher.sendComponentText(getInterface(), getComponent("Target name"), "None");
            dispatcher.sendComponentText(getInterface(), getComponent("Wilderness level and target combat"), "Level: -----");
            updateTargetInformation(player);
        } else {
            dispatcher.sendComponentVisibility(getInterface(), getComponent("Bounty Hunter overlay"), true);
        }*/
    }

    void updateTargetInformation(@NotNull final Player player) {
        if (!GameConstants.BOUNTY_HUNTER) {
            return;
        }
        final BountyHunter bounty = player.getBountyHunter();
        final Player target = bounty.getTarget();
        if (target == null) {
            return;
        }
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentText(getInterface(), getComponent("Target name"), target.getName());
        final OptionalInt targetWildernessLevel = WildernessArea.getWildernessLevel(target.getLocation());
        final int level = targetWildernessLevel.orElse(0);
        final int minLevel = Math.max(1, level - 2);
        final int maxLevel = Math.min(64, level + 2);
        dispatcher.sendComponentText(getInterface(), getComponent("Wilderness level and target combat"), "<col=990000>Level: " + minLevel + "-" + maxLevel + ", Cmb " + target.getSkills().getCombatLevel() + "</col>");
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.WILDERNESS_OVERLAY;
    }
}

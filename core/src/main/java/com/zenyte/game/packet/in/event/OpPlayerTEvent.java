package com.zenyte.game.packet.in.event;

import com.near_reality.game.queue.QueueType;
import com.near_reality.game.world.entity.player.PlayerActionPlugin;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.spells.PlayerSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnPlayerHandler;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.SpellPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Tommeh | 25-1-2019 | 20:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OpPlayerTEvent implements ClientProtEvent {
    private final int interfaceId;
    private final int componentId;
    private final int index;
    private final boolean run;
    private final int itemId;
    private final int componentIndex;

    @Override
    public void log(@NotNull final Player player) {
        final Player target = World.getPlayers().get(index);
        if (target == null) {
            log(player, "Index: " + index + ", interface: " + interfaceId + ", component: " + componentId + ", run: " + run + "; name: null");
            return;
        }
        final Location tile = target.getLocation();
        log(player, "Index: " + index + ", interface: " + interfaceId + ", component: " + componentId + ", run: " + run + "; name: " + target.getUsername() + ", location: x" + tile.getX() + ", y" + tile.getY() + ", z: " + tile.getPlane());
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {

        final Player target = World.getPlayers().get(index);

        if (target == null || target == player || target.isFinished() || !target.isInitialized())
            return;

        if (!player.isLocked()) {

            if (interfaceId == GameInterface.FORM_GIM_TAB.getId()) {
                PlayerActionPlugin.Companion
                        .findHandler("invite")
                        .ifPresent(handler -> {
                            handler.getOptionHandler().handle(player, target);
                        });
                return;
            } else if (interfaceId == GameInterface.INVENTORY_TAB.getId()) {

                final Item item = player.getInventory().getItem(componentIndex);
                if (item == null)
                    return;

                if (run) {
                    if (player.eligibleForShiftTeleportation()) {
                        player.setLocation(new Location(target.getLocation()));
                        return;
                    }
                    player.setRun(true);
                }

                if (item.getId() != itemId)
                    return;

                ItemOnPlayerHandler.handleItemOnPlayer(player, item, componentIndex, target);
                return;
            }
        }
        if (interfaceId == GameInterface.SPELLBOOK.getId()) {

            final PlayerSpell spell = Magic.getSpell(
                    player.getCombatDefinitions().getSpellbook(),
                    SpellDefinitions.getSpellName(componentId),
                    PlayerSpell.class);
            if (spell == null)
                return;

            final RegionArea area = player.getArea();
            if (area instanceof SpellPlugin && !((SpellPlugin) area).canCast(player, spell))
                return;

            spell.execute(player, target);
        }
    }

    public OpPlayerTEvent(int interfaceId, int componentId, int index, boolean run, int itemId, int componentIndex) {
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        this.index = index;
        this.run = run;
        this.itemId = itemId;
        this.componentIndex = componentIndex;
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.Normal.INSTANCE;
    }

}

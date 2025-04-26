package com.zenyte.game.content.rottenpotato;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.rottenpotato.handler.NpcRottenPotatoActionHandler;
import com.zenyte.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import com.zenyte.game.content.rottenpotato.handler.RottenPotatoActionHandler;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.OptionsMenuD;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public class RottenPotatoDialogue extends OptionsMenuD {
    private final Optional<?> target;
    private final List<RottenPotatoActionHandler> options;

    public RottenPotatoDialogue(Player player, String title, final Optional<?> target, final List<RottenPotatoActionHandler> options) {
        super(player, title, getOptionStrings(options));
        this.options = options;
        this.target = target;
    }

    private static String[] getOptionStrings(final List<RottenPotatoActionHandler> actions) {
        final ArrayList<String> list = new ArrayList<String>();
        for (RottenPotatoActionHandler action : actions) {
            list.add(action.option());
        }
        return list.toArray(new String[0]);
    }

    @Override
    public void handleClick(int slotId) {
        final RottenPotatoActionHandler action = options.get(slotId);
        if (action.type() == RottenPotatoActionType.ITEM_ON_PLAYER) {
            handlePlayerAction((PlayerRottenPotatoActionHandler) action);
            return;
        } else if (action.type() == RottenPotatoActionType.ITEM_ON_NPC) {
            handleNpcAction((NpcRottenPotatoActionHandler) action);
            return;
        }
    }

    private void handlePlayerAction(final PlayerRottenPotatoActionHandler handler) {
        if (target.isPresent()) {
            final Object target = this.target.get();
            Preconditions.checkArgument(target instanceof Player);
            handler.execute(player, (Player) target);
        } else {
            player.sendInputString("Name of player?", name -> {
                final Player target = World.getPlayerByUsername(name);
                if (target == null) {
                    return;
                }
                handler.execute(player, target);
            });
        }
    }

    private void handleNpcAction(final NpcRottenPotatoActionHandler handler) {
        if (target.isPresent()) {
            final Object target = this.target.get();
            Preconditions.checkArgument(target instanceof NPC);
            handler.execute(player, (NPC) target);
        }
    }

    @Override
    public boolean cancelOption() {
        return true;
    }
}

package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import static com.zenyte.game.content.minigame.castlewars.CastleWars.SARADOMIN_TEAM;
import static com.zenyte.game.content.minigame.castlewars.CastleWars.ZAMORAK_TEAM;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWarsLateJoinD extends Dialogue {
    private final CastleWarsTeam team;
    private final String teamName;

    public CastleWarsLateJoinD(final Player player, final CastleWarsTeam team) {
        super(player);
        this.team = team;
        this.teamName = team.equals(CastleWarsTeam.SARADOMIN) ? "Saradomin" : "Zamorak";
    }

    @Override
    public void buildDialogue() {
        options("There's an open spot in the game, would you like to join?", "Yes", "No").onOptionOne(() -> {
            final boolean saradomin = team.equals(CastleWarsTeam.SARADOMIN);
            if (saradomin && SARADOMIN_TEAM.size() > CastleWars.ZAMORAK_TEAM.size() || !saradomin && SARADOMIN_TEAM.size() < CastleWars.ZAMORAK_TEAM.size()) {
                plain("The game invite invite isn't available anymore");
                return;
            } else {
                finish();
                (saradomin ? SARADOMIN_TEAM : ZAMORAK_TEAM).add(player);
                player.setLocation(team.getRespawn());
            }
        });
    }
}

package com.zenyte.game.content.treasuretrails.npcs;

import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.challenges.EmoteRequest;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 14/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DoubleAgent extends TreasureGuardian {
    public DoubleAgent(@NotNull final ClueLevel tier, @NotNull Location tile, @NotNull final Player player, @NotNull final EmoteRequest clue) {
        super(player, tile, tier == ClueLevel.MASTER ? 7312 : tier == ClueLevel.ELITE ? 1778 : 1777);
        this.username = player.getUsername();
        this.clue = clue;
        setForceTalk(new ForceTalk("I expect you to die!"));
        combat.setCombatDelay(2);
        combat.forceTarget(player);
    }

    private final String username;
    private final EmoteRequest clue;
    private boolean slain;

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        slain = true;
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        if (slain) {
            //Automatically spawn URI if the player is still wielding the items - execute the same code that executes when the player clicks the correct emote.
            World.getPlayer(username).ifPresent(player -> WorldTasksManager.schedule(() -> TreasureTrail.emoteChallenge(player, clue.getEmotes().get(0))));
        }
    }
}

package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.CharlieRequest;
import com.zenyte.game.content.treasuretrails.challenges.ClueChallenge;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 04/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum CharlieTask implements Clue {

    COOK_A_TROUT("I need to cook a raw trout for Charlie."),
    COOK_A_PIKE("I need to cook a raw pike for Charlie."),
    FISH_A_HERRING("I need to fish Charlie a herring."),
    FISH_A_TROUT("I need to fish Charlie a trout."),
    MINE_IRON("I need to mine Charlie a piece of iron ore from an iron vein."),
    SMITH_AN_IRON_DRAGGER("I need to smith Charlie one iron dagger."),
    CRAFT_A_LEATHER_BODY("I need to craft Charlie a leather body."),
    CRAFT_LEATHER_CHAPS("I need to craft Charlie some leather chaps.");

    private final String text;
    private final CharlieRequest task;

    public final void progress(@NotNull final Player player) {
        TreasureTrail.progressCharlieTask(player, this);
    }

    CharlieTask(final String text) {
        this.text = text;
        this.task = new CharlieRequest(this);
    }

    @Override
    public void view(@NotNull final Player player, @NotNull final Item item) {
        player.getTemporaryAttributes().put("Clue scroll item", item);
        GameInterface.CLUE_SCROLL.open(player);
    }

    @Override
    public TreasureTrailType getType() {
        return TreasureTrailType.CHARLIE;
    }

    @Override
    public String getEnumName() {
        return toString();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public ClueChallenge getChallenge() {
        return task;
    }

    @NotNull
    @Override
    public ClueLevel level() {
        return ClueLevel.BEGINNER;
    }}

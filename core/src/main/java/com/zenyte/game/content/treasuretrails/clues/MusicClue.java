package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.ClueChallenge;
import com.zenyte.game.content.treasuretrails.challenges.SongRequest;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.world.entity.player.Player;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 04/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum MusicClue implements Clue {

    ALONE(ClueLevel.EASY),
    ON_THE_SHORE(ClueLevel.EASY),
    RUGGED_TERRAIN(ClueLevel.EASY),
    THE_FORLORN_HOMESTEAD(ClueLevel.EASY),
    TIPTOE(ClueLevel.EASY),
    VISION(ClueLevel.EASY),

    CATCH_ME_IF_YOU_CAN(ClueLevel.MEDIUM),
    CAVE_OF_BEASTS(ClueLevel.MEDIUM),
    DEVILS_MAY_CARE(ClueLevel.MEDIUM),
    FAERIE(ClueLevel.MEDIUM),
    FORGOTTEN(ClueLevel.MEDIUM),
    KARAMJA_JAM(ClueLevel.MEDIUM),
    SUBTERRANEA(ClueLevel.MEDIUM),

    COMPLICATION(ClueLevel.HARD),
    FOSSILISED(ClueLevel.HARD),
    HELLS_BELLS(ClueLevel.HARD),
    LA_MORT(ClueLevel.HARD),
    LITTLE_CAVE_OF_HORRORS(ClueLevel.HARD),
    ROC_AND_ROLL(ClueLevel.HARD),
    SCORPIA_DANCES(ClueLevel.MEDIUM);

    private final ClueLevel level;


    @Override
    public void view(@NotNull Player player, @NotNull Item item) {
        player.getTemporaryAttributes().put("Clue scroll item", item);
        GameInterface.CLUE_SCROLL.open(player);
    }

    @Override
    public ClueChallenge getChallenge() {
        return new SongRequest(StringFormatUtil.formatString(toString()));
    }

    @Override
    public TreasureTrailType getType() {
        return TreasureTrailType.MUSIC;
    }

    @Override
    public String getEnumName() {
        return toString();
    }

    @Override
    public String getText() {
        return "I'd like to hear some music.<br>" +
                "Come and see me on the bridge in<br>" +
                "Falador Park, and play:<br>" +
                "<col=ffffff>" + Music.lowercaseMap.get(toString().toLowerCase().replace("_", " ")).getName() + "</col><br><br>" +
                "Thanks,<br>" +
                "Cecilia";
    }

    @NotNull
    @Override
    public ClueLevel level() {
        return level;
    }

    MusicClue(ClueLevel level) {
        this.level = level;
    }
}

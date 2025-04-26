package com.zenyte.game.content.minigame.motherlode;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 29/06/2019 16:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class UpperMotherlodeArea extends MotherlodeArea {

    public static final RSPolygon polygon = new RSPolygon(new int[][]{
            { 3733, 5687 },
            { 3732, 5687 },
            { 3732, 5684 },
            { 3736, 5680 },
            { 3739, 5680 },
            { 3740, 5681 },
            { 3745, 5681 },
            { 3746, 5680 },
            { 3747, 5680 },
            { 3748, 5679 },
            { 3750, 5679 },
            { 3751, 5678 },
            { 3751, 5677 },
            { 3753, 5675 },
            { 3754, 5675 },
            { 3755, 5674 },
            { 3756, 5674 },
            { 3758, 5672 },
            { 3759, 5672 },
            { 3759, 5670 },
            { 3761, 5668 },
            { 3761, 5665 },
            { 3760, 5664 },
            { 3760, 5658 },
            { 3759, 5657 },
            { 3759, 5655 },
            { 3760, 5654 },
            { 3763, 5654 },
            { 3764, 5653 },
            { 3766, 5653 },
            { 3767, 5654 },
            { 3767, 5655 },
            { 3767, 5657 },
            { 3767, 5658 },
            { 3767, 5658 },
            { 3764, 5660 },
            { 3764, 5661 },
            { 3766, 5662 },
            { 3766, 5663 },
            { 3767, 5664 },
            { 3767, 5666 },
            { 3765, 5667 },
            { 3765, 5669 },
            { 3763, 5670 },
            { 3763, 5672 },
            { 3767, 5676 },
            { 3767, 5678 },
            { 3765, 5680 },
            { 3765, 5683 },
            { 3765, 5684 },
            { 3765, 5685 },
            { 3764, 5685 },
            { 3762, 5685 },
            { 3761, 5686 },
            { 3759, 5686 },
            { 3758, 5687 },
            { 3757, 5686 },
            { 3751, 5686 },
            { 3750, 5685 },
            { 3749, 5685 },
            { 3747, 5687 },
            { 3745, 5687 },
            { 3744, 5688 },
            { 3743, 5687 },
            { 3741, 5687 },
            { 3740, 5686 },
            { 3737, 5686 },
            { 3736, 5687 }
    });

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getVarManager().sendBit(2086, 1);
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        super.leave(player, logout);
        player.getVarManager().sendBit(2086, 0);
    }

    @Override
    void spawn() {

    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                polygon
        };
    }

    @Override
    public String name() {
        return "Motherlode mine: Upper floor";
    }
}

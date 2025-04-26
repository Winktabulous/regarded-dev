package com.zenyte.game.world.region.area;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.plugins.object.OldFirePit;

/**
 * @author Tommeh | 31 mei 2018 | 01:16:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class IcePlateauArea extends FremennikProvince {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2943, 3839 }, { 2932, 3816 }, { 2926, 3797 }, { 2921, 3784 }, { 2917, 3777 },
                { 2931, 3769 }, { 2938, 3764 }, { 2941, 3749 }, { 2940, 3739 }, { 2940, 3730 }, { 2936, 3721 }, { 2926, 3719 },
                { 2923, 3723 }, { 2920, 3724 }, { 2915, 3719 }, { 2914, 3716 }, { 2911, 3717 }, { 2902, 3718 }, { 2885, 3714 },
                { 2874, 3717 }, { 2872, 3714 }, { 2831, 3732 }, { 2827, 3724 }, { 2819, 3722 }, { 2815, 3712 }, { 2740, 3712 },
                { 2741, 3714 }, { 2743, 3715 }, { 2743, 3716 }, { 2739, 3718 }, { 2735, 3724 }, { 2734, 3725 }, { 2733, 3725 },
                { 2728, 3725 }, { 2728, 3735 }, { 2753, 3753 }, { 2706, 3796 }, { 2687, 3817 }, { 2687, 3838 }, { 2709, 3874 },
                { 2720, 3879 }, { 2751, 3888 }, { 2815, 3885 }, { 2816, 3840 } }, 0) };
	}

	@Override
	public void enter(final Player player) {
	    super.enter(player);
		if (OldFirePit.FirePit.GODWARS_DUNGEON_FIRE.isBuilt(player)) {
			return;
		}
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 167);
	}

	@Override
	public void leave(final Player player, boolean logout) {
	    super.leave(player, logout);
		player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
	}

	@Override
	public String name() {
		return "Ice Plateau";
	}

}

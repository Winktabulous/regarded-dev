package com.zenyte.game.world.region.area;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 16. mai 2018 : 15:12:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class DuelArenaArea extends DesertArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3326, 3267 }, { 3324, 3265 }, { 3324, 3252 }, { 3323, 3251 }, { 3323, 3250 },
				{ 3322, 3249 }, { 3322, 3248 }, { 3313, 3248 }, { 3312, 3247 }, { 3312, 3245 }, { 3317, 3245 }, { 3317, 3240 },
				{ 3311, 3240 }, { 3311, 3239 }, { 3312, 3238 }, { 3312, 3225 }, { 3313, 3224 }, { 3321, 3224 }, { 3322, 3223 },
				{ 3324, 3223 }, { 3324, 3218 }, { 3323, 3217 }, { 3323, 3215 }, { 3324, 3214 }, { 3324, 3209 }, { 3325, 3208 },
				{ 3325, 3204 }, { 3324, 3203 }, { 3324, 3200 }, { 3327, 3197 }, { 3328, 3197 }, { 3329, 3196 }, { 3331, 3196 },
				{ 3333, 3198 }, { 3334, 3198 }, { 3336, 3200 }, { 3340, 3200 }, { 3341, 3201 }, { 3343, 3201 }, { 3344, 3200 },
				{ 3351, 3200 }, { 3352, 3199 }, { 3354, 3199 }, { 3355, 3200 }, { 3359, 3200 }, { 3360, 3201 }, { 3362, 3201 },
				{ 3363, 3200 }, { 3369, 3200 }, { 3370, 3199 }, { 3372, 3199 }, { 3373, 3200 }, { 3377, 3200 }, { 3378, 3201 },
				{ 3380, 3201 }, { 3381, 3200 }, { 3384, 3200 }, { 3385, 3201 }, { 3387, 3201 }, { 3388, 3200 }, { 3393, 3200 },
				{ 3394, 3201 }, { 3396, 3201 }, { 3397, 3200 }, { 3400, 3200 }, { 3401, 3201 }, { 3402, 3203 }, { 3398, 3208 },
				{ 3398, 3211 }, { 3400, 3214 }, { 3400, 3215 }, { 3404, 3219 }, { 3404, 3220 }, { 3405, 3221 }, { 3405, 3225 },
				{ 3402, 3228 }, { 3402, 3229 }, { 3401, 3230 }, { 3401, 3231 }, { 3402, 3232 }, { 3402, 3233 }, { 3404, 3235 },
				{ 3404, 3238 }, { 3402, 3240 }, { 3402, 3241 }, { 3401, 3242 }, { 3401, 3243 }, { 3403, 3245 }, { 3403, 3249 },
				{ 3399, 3253 }, { 3399, 3254 }, { 3398, 3255 }, { 3398, 3257 }, { 3399, 3258 }, { 3399, 3259 }, { 3398, 3260 },
				{ 3398, 3264 }, { 3395, 3265 }, { 3392, 3267 }, { 3390, 3270 }, { 3388, 3275 }, { 3386, 3277 }, { 3386, 3283 },
				{ 3385, 3284 }, { 3385, 3286 }, { 3383, 3288 }, { 3383, 3289 }, { 3357, 3289 }, { 3353, 3287 }, { 3344, 3287 },
				{ 3341, 3286 }, { 3341, 3283 }, { 3343, 3283 }, { 3342, 3278 }, { 3339, 3278 }, { 3339, 3275 }, { 3339, 3270 },
				{ 3337, 3268 }, { 3331, 3268 }, { 3330, 3267 } }) };
	}



	@Override
	public void enter(final Player player) {
		GameInterface.DUEL_OVERLAY.open(player);
		player.setPlayerChallengeable(true);
	}

	@Override
	public void leave(final Player player, boolean logout) {
		player.getInterfaceHandler().closeInterface(GameInterface.DUEL_OVERLAY);
		player.setPlayerChallengeable(false);
	}

	@Override
	public String name() {
		return "Duel arena";
	}

}

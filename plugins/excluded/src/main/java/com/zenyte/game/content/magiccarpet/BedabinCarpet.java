package com.zenyte.game.content.magiccarpet;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;

/**
 * @author Kris | 21. aug 2018 : 12:37:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class BedabinCarpet extends MagicCarpet {
	public BedabinCarpet(final Player player, final boolean forward) {
		super(player, forward);
	}

	public static final CarpetMovement[] FORWARD_MOVEMENT = new CarpetMovement[] {new CarpetMovement(new Location(3305, 3109, 0), 30, 512), new CarpetMovement(new Location(3301, 3109, 0), 30, 512), new CarpetMovement(new Location(3299, 3107, 0)), new CarpetMovement(new Location(3297, 3105, 0)), new CarpetMovement(new Location(3295, 3103, 0)), new CarpetMovement(new Location(3295, 3100, 0), 30, 0), new CarpetMovement(new Location(3293, 3098, 0)), new CarpetMovement(new Location(3291, 3096, 0)), new CarpetMovement(new Location(3289, 3094, 0)), new CarpetMovement(new Location(3287, 3092, 0)), new CarpetMovement(new Location(3285, 3090, 0)), new CarpetMovement(new Location(3283, 3088, 0)), new CarpetMovement(new Location(3280, 3088, 0), 30, 512), new CarpetMovement(new Location(3278, 3086, 0)), new CarpetMovement(new Location(3276, 3084, 0)), new CarpetMovement(new Location(3274, 3082, 0)), new CarpetMovement(new Location(3272, 3080, 0)), new CarpetMovement(new Location(3270, 3078, 0)), new CarpetMovement(new Location(3270, 3078, 0)), new CarpetMovement(new Location(3267, 3075, 0)), new CarpetMovement(new Location(3265, 3073, 0)), new CarpetMovement(new Location(3262, 3070, 0), 30, 0), new CarpetMovement(new Location(3262, 3067, 0), 30, 0), new CarpetMovement(new Location(3262, 3064, 0), 30, 0), new CarpetMovement(new Location(3262, 3061, 0), 30, 0), new CarpetMovement(new Location(3262, 3058, 0), 30, 0), new CarpetMovement(new Location(3260, 3056, 0)), new CarpetMovement(new Location(3260, 3056, 0)), new CarpetMovement(new Location(3256, 3055, 0), 30, 512), new CarpetMovement(new Location(3253, 3055, 0), 30, 512), new CarpetMovement(new Location(3250, 3055, 0), 30, 512), new CarpetMovement(new Location(3247, 3055, 0), 30, 512), new CarpetMovement(new Location(3244, 3055, 0), 30, 512), new CarpetMovement(new Location(3241, 3055, 0), 30, 512), new CarpetMovement(new Location(3238, 3055, 0), 30, 512), new CarpetMovement(new Location(3235, 3055, 0), 30, 512), new CarpetMovement(new Location(3232, 3055, 0), 30, 512), new CarpetMovement(new Location(3229, 3055, 0), 30, 512), new CarpetMovement(new Location(3226, 3055, 0), 30, 512), new CarpetMovement(new Location(3223, 3055, 0), 30, 512), new CarpetMovement(new Location(3220, 3055, 0), 30, 512), new CarpetMovement(new Location(3217, 3055, 0), 30, 512), new CarpetMovement(new Location(3214, 3055, 0), 30, 512), new CarpetMovement(new Location(3211, 3055, 0), 30, 512), new CarpetMovement(new Location(3208, 3055, 0), 30, 512), new CarpetMovement(new Location(3206, 3053, 0)), new CarpetMovement(new Location(3204, 3051, 0)), new CarpetMovement(new Location(3202, 3049, 0)), new CarpetMovement(new Location(3199, 3049, 0), 30, 512), new CarpetMovement(new Location(3197, 3047, 0)), new CarpetMovement(new Location(3195, 3045, 0)), new CarpetMovement(new Location(3193, 3043, 0)), new CarpetMovement(new Location(3190, 3043, 0), 30, 512), new CarpetMovement(new Location(3187, 3043, 0), 30, 512), new CarpetMovement(new Location(3184, 3043, 0), 30, 512), new CarpetMovement(new Location(3181, 3046, 0), 30, 512), new CarpetMovement(new Location(3181, 3046, 0)), new CarpetMovement(new Location(3180, 3045, 0), 30, 0)};
	private static final CarpetMovement[] BACKWARDS_MOVEMENT = new CarpetMovement[] {new CarpetMovement(new Location(3183, 3044, 0), 30, 1536), new CarpetMovement(new Location(3187, 3043, 0), 30, 1536), new CarpetMovement(new Location(3190, 3043, 0), 30, 1536), new CarpetMovement(new Location(3193, 3043, 0), 30, 1536), new CarpetMovement(new Location(3195, 3045, 0)), new CarpetMovement(new Location(3197, 3047, 0)), new CarpetMovement(new Location(3199, 3049, 0)), new CarpetMovement(new Location(3202, 3049, 0), 30, 1536), new CarpetMovement(new Location(3204, 3051, 0)), new CarpetMovement(new Location(3206, 3053, 0)), new CarpetMovement(new Location(3208, 3055, 0)), new CarpetMovement(new Location(3211, 3055, 0), 30, 1536), new CarpetMovement(new Location(3214, 3055, 0), 30, 1536), new CarpetMovement(new Location(3217, 3055, 0), 30, 1536), new CarpetMovement(new Location(3220, 3055, 0), 30, 1536), new CarpetMovement(new Location(3223, 3055, 0), 30, 1536), new CarpetMovement(new Location(3226, 3055, 0), 30, 1536), new CarpetMovement(new Location(3229, 3055, 0), 30, 1536), new CarpetMovement(new Location(3232, 3055, 0), 30, 1536), new CarpetMovement(new Location(3235, 3055, 0), 30, 1536), new CarpetMovement(new Location(3238, 3055, 0), 30, 1536), new CarpetMovement(new Location(3241, 3055, 0), 30, 1536), new CarpetMovement(new Location(3244, 3055, 0), 30, 1536), new CarpetMovement(new Location(3247, 3055, 0), 30, 1536), new CarpetMovement(new Location(3250, 3055, 0), 30, 1536), new CarpetMovement(new Location(3253, 3055, 0), 30, 1536), new CarpetMovement(new Location(3256, 3055, 0), 30, 1536), new CarpetMovement(new Location(3259, 3055, 0), 30, 1536), new CarpetMovement(new Location(3261, 3057, 0)), new CarpetMovement(new Location(3261, 3057, 0)), new CarpetMovement(new Location(3262, 3061, 0), 30, 1024), new CarpetMovement(new Location(3262, 3064, 0), 30, 1024), new CarpetMovement(new Location(3262, 3067, 0), 30, 1024), new CarpetMovement(new Location(3262, 3070, 0), 30, 1024), new CarpetMovement(new Location(3264, 3072, 0)), new CarpetMovement(new Location(3266, 3074, 0)), new CarpetMovement(new Location(3268, 3076, 0)), new CarpetMovement(new Location(3270, 3078, 0)), new CarpetMovement(new Location(3272, 3080, 0)), new CarpetMovement(new Location(3274, 3082, 0)), new CarpetMovement(new Location(3276, 3084, 0)), new CarpetMovement(new Location(3278, 3086, 0)), new CarpetMovement(new Location(3280, 3088, 0)), new CarpetMovement(new Location(3283, 3088, 0), 30, 1536), new CarpetMovement(new Location(3285, 3090, 0)), new CarpetMovement(new Location(3287, 3092, 0)), new CarpetMovement(new Location(3289, 3094, 0)), new CarpetMovement(new Location(3291, 3096, 0)), new CarpetMovement(new Location(3293, 3098, 0)), new CarpetMovement(new Location(3295, 3100, 0)), new CarpetMovement(new Location(3295, 3103, 0), 30, 1024), new CarpetMovement(new Location(3297, 3105, 0)), new CarpetMovement(new Location(3299, 3107, 0)), new CarpetMovement(new Location(3301, 3109, 0)), new CarpetMovement(new Location(3304, 3109, 0), 30, 1536), new CarpetMovement(new Location(3308, 3110, 0), 30, 1536)};

	@Override
	public CarpetMovement[] getForwardMovement() {
		return FORWARD_MOVEMENT;
	}

	@Override
	public CarpetMovement[] getBackwardsMovement() {
		return BACKWARDS_MOVEMENT;
	}

	@Override
	public Location getStartLocation() {
		return forward ? new Location(3308, 3110, 0) : new Location(3180, 3044, 0);
	}

	@Override
	public Location getStartFaceLocation() {
		return forward ? new Location(3308, 3109, 0) : new Location(3180, 3045, 0);
	}

	@Override
	public Location getStartNextToCarpetLocation() {
		return forward ? new Location(3309, 3110, 0) : new Location(3181, 3044, 0);
	}

	@Override
	public Location getEndNextToCarpetLocation() {
		return forward ? new Location(3309, 3109, 0) : new Location(3181, 3045, 0);
	}

	@Override
	public CameraPositionAction getStartCameraPositionAction() {
		final Location pos = forward ? new Location(3302, 3110, 0) : new Location(3183, 3052, 0);
		return new CameraPositionAction(player, pos, 2500, -128, 0);
	}

	@Override
	public CameraPositionAction getEndCameraPositionAction() {
		final Location pos = forward ? new Location(3300, 3109, 0) : new Location(3177, 3048, 0);
		return new CameraPositionAction(player, pos, 2500, -128, 0);
	}

	@Override
	public CameraLookAction getStartCameraLookAction() {
		final Location pos = forward ? new Location(3308, 3109, 0) : new Location(3179, 3043, 0);
		return new CameraLookAction(player, pos, 0, -128, 0);
	}

	@Override
	public CameraLookAction getEndCameraLookAction() {
		final Location pos = forward ? new Location(3308, 3109, 0) : new Location(3181, 3044, 0);
		return new CameraLookAction(player, pos, 0, -128, 0);
	}
}

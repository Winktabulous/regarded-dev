package com.zenyte.game.content.chambersofxeric.greatolm;

import com.zenyte.game.world.entity.masks.Animation;
import mgi.types.config.AnimationDefinitions;

import java.util.Objects;

/**
 * @author Kris | 14. jaan 2018 : 1:58.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum OlmAnimation {

	HEAD_RISE(7335, 7383, false),
	HEAD_FALL(7348),
	HEAD_MIDDLE_DEFAULT(7336, 7374, true),
	HEAD_LEFT_DEFAULT(7338, 7375, true),
	HEAD_RIGHT_DEFAULT(7337, 7376, true),
	HEAD_MIDDLE_TO_LEFT(7341, 7377, true),
	HEAD_LEFT_TO_MIDDLE(7342, 7378, true),
	HEAD_MIDDLE_TO_RIGHT(7339, 7381, true),
	HEAD_RIGHT_TO_MIDDLE(7340, 7382, true),
	HEAD_LEFT_TO_RIGHT(7343, 7379, true),
	HEAD_RIGHT_TO_LEFT(7344, 7380, true),
	HEAD_MIDDLE_ATTACK(7345, 7371, true),
	HEAD_LEFT_ATTACK(7347, 7372, true),
	HEAD_RIGHT_ATTACK(7346, 7373, true),
	RIGHT_CLAW_INVISIBLE(7349),
	RIGHT_CLAW_RISE(7350),
	RIGHT_CLAW_DEFAULT(7351),
	RIGHT_CLAW_FALL(7352),
	LEFT_CLAW_INVISIBLE(7353),
	LEFT_CLAW_RISE(7354),
	LEFT_CLAW_DEFAULT(7355, 7365, false),
	LEFT_CLAW_FALL(7370),
	LEFT_CLAW_CRYSTAL(7356, 7366, false),
	LEFT_CLAW_LIGHTNING(7358, 7368, false),
	LEFT_CLAW_SWIRL(7359, 7369, false),
	LEFT_CLAW_INFINITY(7357, 7357, false),
	LEFT_CLAW_REGAINING_POWER(7360),
	LEFT_CLAW_TWITCHING(7361),
	LEFT_CLAW_STOPPING_TWITCHING(7362),
	LEFT_CLAW_RETREAT(7363),
	LEFT_CLAW_TWITCH_RETREAT(7364);

    /**
     * The primary and the special animation. Special animation is only available for Olm's animations, and its only difference is that the Olm will show green circles around its
     * head when the special animation is performed. It is only used in the final phase of the Olm fight.
     */
	private final Animation primary, special;

    /**
     * The length of the animation in milliseconds, only available for the head animations. If unavailable, a value of -1 is set.
     */
	private final int animationLength;

	private final boolean turnAnimation;

	OlmAnimation(final int regular) {
		this(regular, -1, false);
	}
	
	OlmAnimation(final int primary, final int special, final boolean turnAnimation) {
		this.primary = new Animation(primary);
		this.special = special == -1 ? this.primary : new Animation(special);
		this.animationLength = !toString().startsWith("HEAD") ? -1 : Objects.requireNonNull(AnimationDefinitions.get(primary)).getDuration();
		this.turnAnimation = turnAnimation;
	}
	
	public Animation getPrimary() {
	    return primary;
	}
	
	public Animation getSpecial() {
	    return special;
	}
	
	public int getAnimationLength() {
	    return animationLength;
	}
	
	public boolean isTurnAnimation() {
	    return turnAnimation;
	}
}

package com.near_reality.game.content.boss.nex;

public enum NexStage {
	SPAWN(true),
	SMOKE_CHOKE,
	SMOKE_DASH,
	SMOKE_AA(false, true),
	SHADOW_SWITCH(true),
	SHADOW_SMASH,
	SHADOW_EMBRACE,
	SHADOW_AA(false, true),
	BLOOD_SWITCH(true),
	BLOOD_AA(false, true),
	BLOOD_SIPHON,
	BLOOD_SACRIFICE,
	ICE_SWITCH(true),
	ICE_AA(false, true),
	ICE_PRISON,
	ICE_CONTAINMENT,
	ZAROS_SWITCH(true),
	ZAROS_AA(false, true);

	private final boolean priority;
	private final boolean autoAttack;

	NexStage() {
		this(false, false);
	}

	NexStage(boolean priority) {
		this(priority, false);
	}

	NexStage(boolean priority, boolean autoAttack) {
		this.priority = priority;
		this.autoAttack = autoAttack;
	}

	public boolean isPriority() {
		return priority;
	}

	public boolean isAutoAttack() {
		return autoAttack;
	}

}

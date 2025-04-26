package com.zenyte.game.world.entity.player;

public enum LogoutType {
	NONE(true),
	FORCE(false),
	REQUESTED(true),
	CONNECTION_LOST(false),
	UPDATING(false);

	private final boolean processSession;

	LogoutType(boolean processSession) {
		this.processSession = processSession;
	}

	public boolean processSession() {
		return processSession;
	}
}

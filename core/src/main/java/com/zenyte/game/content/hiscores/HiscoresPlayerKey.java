package com.zenyte.game.content.hiscores;

import com.zenyte.game.world.entity.player.privilege.GameMode;

import java.util.Objects;

public class HiscoresPlayerKey {

	private final String name;
	private final GameMode gameMode;
	private final int rate;

	HiscoresPlayerKey(String name, GameMode gameMode, int rate) {
		this.name = name;
		this.gameMode = gameMode;
		this.rate = rate;
	}

	public String getName() {
		return name;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public int getRate() {
		return rate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HiscoresPlayerKey that = (HiscoresPlayerKey) o;
		return rate == that.rate && Objects.equals(name, that.name) && gameMode == that.gameMode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, gameMode, rate);
	}

}

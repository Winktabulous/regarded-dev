package com.zenyte.game.content.boss.nightmare;

public class NightmarePhase {

	private final NightmarePhaseNumber number;
	private final int hp, nextAwakeId, nextAwakeShieldId, lobbyId, totemDmg;

	public int forPlayers = 0;

	public NightmarePhase(NightmarePhaseNumber number, int hp, int nextAwakeId, int nextAwakeShieldId, int totemDmg) {
		this(number, hp, nextAwakeId, nextAwakeShieldId, -1, totemDmg);
	}

	public NightmarePhase(NightmarePhaseNumber number, int hp, int nextAwakeId, int nextAwakeShieldId, int lobbyId, int totemDmg) {
		this.number = number;
		this.hp = hp;
		this.nextAwakeId = nextAwakeId;
		this.nextAwakeShieldId = nextAwakeShieldId;
		this.lobbyId = lobbyId;
		this.totemDmg = totemDmg;
	}

	public NightmarePhaseNumber getNumber() {
		return number;
	}

	public int getHp() {
		return hp;
	}

	public int getNextAwakeId() {
		return nextAwakeId;
	}

	public int getNextAwakeShieldId() {
		return nextAwakeShieldId;
	}

	public int getLobbyId() {
		return lobbyId;
	}

	public int getTotemDmg() {
		return totemDmg;
	}

	public enum NightmarePhaseNumber {
		FIRST,
		SECOND,
		THIRD,
		FOURTH,
		FIFTH
	}

	@Override
	public String toString() {
		return "NightmarePhase{" +
				"number=" + number +
				'}';
	}

}

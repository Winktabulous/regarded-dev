package com.zenyte.game.content.tombsofamascut;

import java.util.Arrays;

/**
 * @author Savions.
 */
public class TOAPartySettings {
	public TOAPartySettings(){
		this.data = new TOAPartySettingData();
		extractData();
	}

	public TOAPartySettings(TOAPartySettingData data) {
		this.data = data;
		extractData();
	}

	private void extractData() {
		this.raidLevel = data.raidLevel;
		this.activeInvocations = data.activeInvocations;
		this.kcRequirement = data.kcRequirement;
		this.invocationBitmaps = data.invocationBitmaps;
	}

	TOAPartySettingData data;
	private int raidLevel;
	private int activeInvocations;
	private int kcRequirement;
	private int[] invocationBitmaps = new int[3];


	void unFlagCategory(final InvocationCategoryType category) {
		Arrays.stream(InvocationType.VALUES).filter(invocation -> category.equals(invocation.getCategory()) && isActive(invocation)).forEach(this::unFlagInvocation);
	}

	void unFlagInvocation(final InvocationType invocation) {
		invocationBitmaps[getBitMapIndex(invocation)] &= ~getBitIndex(invocation);
		raidLevel -= invocation.getLevelModifier();
		activeInvocations--;
	}

	void flagInvocation(final InvocationType invocation) {
		invocationBitmaps[getBitMapIndex(invocation)] |= getBitIndex(invocation);
		raidLevel += invocation.getLevelModifier();
		activeInvocations++;
	}

	public void loadInvocationPreset(int[] preset) {
		assert preset.length == 3;
		invocationBitmaps = preset;
		raidLevel = 0;
		activeInvocations = 0;
		Arrays.stream(InvocationType.VALUES).filter(this::isActive).forEach(invocation -> {
			raidLevel += invocation.getLevelModifier();
			activeInvocations++;
		});
	}

	public void copyPartySettings(final TOAPartySettings partySettings) {
		this.data = partySettings.generateData();
		extractData();
	}

	private TOAPartySettingData generateData() {
		return new TOAPartySettingData(this.raidLevel, this.activeInvocations, this.kcRequirement, this.invocationBitmaps);
	}

	public boolean allActive(InvocationCategoryType category) {
		return Arrays.stream(InvocationType.VALUES).noneMatch(invocation -> category.equals(invocation.getCategory()) && !isActive(invocation));
	}

	public boolean isActive(final InvocationType invocation) {
		return (invocationBitmaps[getBitMapIndex(invocation)] & (1 << (invocation.getIndex() % 31))) != 0;
	}

	private int getBitIndex(final InvocationType invocation) {
		return 1 << (invocation.getIndex() % 31);
	}

	private int getBitMapIndex(final InvocationType invocation) {
		return invocation.getIndex() > 61 ? 2 : (invocation.getIndex() > 30 ? 1 : 0);
	}

	public String getMode() {
		if (raidLevel >= 300) {
			return "Expert";
		} else if (raidLevel >= 150) {
			return "Normal";
		}
		return "Entry";
	}

	public void clearInvocations() {
		Arrays.fill(invocationBitmaps, 0);
		raidLevel = 0;
		activeInvocations = 0;
	}

	public int getRaidLevel() {
		return raidLevel;
	}

	public void setRaidLevel(int raidLevel) {
		this.raidLevel = raidLevel;
	}

	public int getKcRequirement() {
		return kcRequirement;
	}

	public void setKcRequirement(int kcRequirement) {
		this.kcRequirement = kcRequirement;
	}

	public int[] getInvocationBitmaps() { return invocationBitmaps; }

	public int getActiveInvocations() { return activeInvocations; }
}

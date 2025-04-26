package com.zenyte.game.content.tombsofamascut;

import com.zenyte.game.content.tombsofamascut.raid.TOAPlayerLogoutState;

import java.util.Arrays;

public class TOAPartySettingData {
    public TOAPartySettingData() {
        this.raidLevel = 0;
        this.kcRequirement = 0;
        Arrays.fill(invocationBitmaps, 0);
        this.activeInvocations = 0;
    }

    public TOAPartySettingData(int raidLevel, int activeInvocations, int kcRequirement, int[] invocationBitmaps) {
        this.raidLevel = raidLevel;
        this.activeInvocations = activeInvocations;
        this.kcRequirement = kcRequirement;
        this.invocationBitmaps = invocationBitmaps;
    }

    public int raidLevel;
    public int activeInvocations;
    public int kcRequirement;
    public int[] invocationBitmaps = new int[3];
}

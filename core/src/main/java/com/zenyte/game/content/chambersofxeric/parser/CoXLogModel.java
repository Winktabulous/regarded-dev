package com.zenyte.game.content.chambersofxeric.parser;

import java.util.ArrayList;

public class CoXLogModel {
    private String host;
    private String mode;
    private String completionStatus;
    private long totalPoints;
    private ArrayList<RaidPlayer> raidPlayers;
    private ArrayList<RaidRewardInfo> raidRewardInfos;
    private ArrayList<RaidDeath> raidDeaths;
    private ArrayList<RaidFloorTime> raidFloorTimes;
    private ArrayList<RaidRoomInfo> raidRoomInfos;

    public CoXLogModel(String host, String mode, String completionStatus, long totalPoints) {
        this.host = host;
        this.mode = mode;
        this.completionStatus = completionStatus;
        this.totalPoints = totalPoints;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public ArrayList<RaidPlayer> getRaidPlayers() {
        return raidPlayers;
    }

    public void setRaidPlayers(ArrayList<RaidPlayer> raidPlayers) {
        this.raidPlayers = raidPlayers;
    }

    public ArrayList<RaidRewardInfo> getRaidRewardInfos() {
        return raidRewardInfos;
    }

    public void setRaidRewardInfos(ArrayList<RaidRewardInfo> raidRewardInfos) {
        this.raidRewardInfos = raidRewardInfos;
    }

    public ArrayList<RaidDeath> getRaidDeaths() {
        return raidDeaths;
    }

    public void setRaidDeaths(ArrayList<RaidDeath> raidDeaths) {
        this.raidDeaths = raidDeaths;
    }

    public ArrayList<RaidFloorTime> getRaidFloorTimes() {
        return raidFloorTimes;
    }

    public void setRaidFloorTimes(ArrayList<RaidFloorTime> raidFloorTimes) {
        this.raidFloorTimes = raidFloorTimes;
    }

    public ArrayList<RaidRoomInfo> getRaidRoomInfos() {
        return raidRoomInfos;
    }

    public void setRaidRoomInfos(ArrayList<RaidRoomInfo> raidRoomInfos) {
        this.raidRoomInfos = raidRoomInfos;
    }
}

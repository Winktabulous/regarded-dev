package com.zenyte.game.world.entity.player;

import com.google.gson.annotations.Expose;
import com.near_reality.net.HardwareInfo;
import com.zenyte.utils.TextUtils;
import mgi.utilities.StringFormatUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PlayerInformation {

    private final transient HardwareInfo hardware;
    private String username;

    /**
     * The player's display name.
     */
    private String displayname;
    /**
     * The last known login address of a player
     */
    @Expose
    private String ip;
    /**
     * The register date of the player.
     */
    @Expose
    private LocalDate registryDate;
    /**
     * The display mode of the user.
     */
    @Expose
    private int mode;
    /**
     * This id is binded to the player and will NEVER change. This is their branded id.
     */
    @Expose
    private int userIdentifier;

    @Expose
    private byte[] UUID;

    @Expose
    private String plainPassword;

    public PlayerInformation(
            final String username,
            final String plainPassword,
            final int clientMode,
            final byte[] UUID,
            final HardwareInfo hardwareInfo
    ) {
        setUsername(username);
        setDisplayname(username);
        this.plainPassword = plainPassword;
        this.mode = clientMode;
        this.UUID = UUID;
        this.hardware = hardwareInfo;
        this.userIdentifier = -1;
        this.registryDate = LocalDate.now();
    }

    public boolean isOnMobile() {
        return false;
    }

    public void setPlayerInformation(final PlayerInformation details) {
        setUsername(details.getUsername());
        setPlainPassword(details.getPlainPassword());
        setDisplayname(details.getDisplayname());
        setUserIdentifier(details.getUserIdentifier());
        setIp(details.getIp());
        setRegistryDate(details.getRegistryDate());
    }

    public int getDaysSinceRegistry() {
        return (int) registryDate.until(LocalDate.now(), ChronoUnit.DAYS);
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayname() {
        return displayname;
    }

    private void setDisplayname(final String displayName) {
        this.displayname = StringFormatUtil.formatString(
                displayName == null || displayName.isEmpty()
                        ? username
                        : displayName
        );
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(final String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDate getRegistryDate() {
        return registryDate;
    }

    public void setRegistryDate(LocalDate registryDate) {
        this.registryDate = registryDate;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public HardwareInfo getHardware() {
        return hardware;
    }

    public int getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(int userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public void setUsername(String username) {
        this.username = TextUtils.formatNameForProtocol(username);
    }

    public byte[] getUUID() {
        return UUID;
    }

    public void setUUID(byte[] UUID) {
        this.UUID = UUID;
    }

}

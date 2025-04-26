package com.zenyte.game.content.killstreak;

import com.zenyte.game.world.entity.player.Player;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class KillstreakLogEntry {

    private final String usernameKilled;
    private final String ipAddress;
    private final long dateKilled;

    public KillstreakLogEntry(String usernameKilled, String ipAddress, long dateKilled) {
        this.usernameKilled = usernameKilled;
        this.ipAddress = ipAddress;
        this.dateKilled = dateKilled;
    }

    public String getUsernameKilled() {
        return usernameKilled;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public long getDateKilled() {
        return dateKilled;
    }
    
    public LocalDateTime getDateKilledAsDateTime() {
        return Instant.ofEpochMilli(dateKilled).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    public static KillstreakLogEntry createEntry(Player player) {
        return new KillstreakLogEntry(
                player.getUsername(),
                player.getIP(),
                Instant.now().toEpochMilli());
    }

}

package com.zenyte.game.content.godwars;

import com.zenyte.game.content.godwars.instance.*;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;

import java.util.List;

/**
 * @author Kris | 14/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum GodwarsInstancePortal {
    //@formatter:off
    ARMADYL(Colour.LIGHT_GRAY.wrap("Armadyl"), InstanceConstants.INSTANCE_COST, 35016, 4674, 26365, ArmadylInstance.class, new ImmutableLocation(1194, 4243, 0),
            new ImmutableLocation(2828, 5290, 2), new ImmutableLocation(1197, 4233, 0), GodType.ARMADYL),
    BANDOS(Colour.RS_GREEN.wrap("Bandos"), InstanceConstants.INSTANCE_COST, 35014, 4675, 26366, BandosInstance.class, new ImmutableLocation(1189, 4312, 0), new ImmutableLocation(2854
            , 5363, 2), new ImmutableLocation(1196, 4303, 0), GodType.BANDOS),
    ZAMORAK(Colour.RS_RED.wrap("Zamorak"), InstanceConstants.INSTANCE_COST, 35015, 4676, 26363, ZamorakInstance.class, new ImmutableLocation(1206, 4397, 0),
            new ImmutableLocation(2933, 5357, 2), new ImmutableLocation(1198, 4374, 0), GodType.ZAMORAK),
    SARADOMIN(Colour.BLUE.wrap("Saradomin"), InstanceConstants.INSTANCE_COST, 35017, 4677, 26364, SaradominInstance.class, new ImmutableLocation(1199, 4426, 0),
            new ImmutableLocation(2927, 5258, 0), new ImmutableLocation(1182, 4433, 0), GodType.SARADOMIN),
    ANCIENT(Colour.RS_PURPLE.wrap("Nex"),
            InstanceConstants.ZAROS_INSTANCE_COST,
            50083,
            11345,
            42965,
            "com.near_reality.game.content.boss.nex.NexGodwarsInstance",
            new ImmutableLocation(2906, 5206, 0), // portal location
            new ImmutableLocation(2906, 5206, 0), // godwars portal location
            new ImmutableLocation(1182, 4433, 0), // altar teleport location
            GodType.ANCIENT
    );
    //@formatter:on

    private final int portalObjectId, instanceRegionId, altarId;
    private final int cost;
    private final Class<? extends GodwarsInstance> instanceClass;
    private final Location portalLocation;
    private final Location godwarsPortalLocation;
    private final Location altarTeleportLocation;
    private final GodType god;
    private final String displayName;

    public int getChunkX() {
        return (instanceRegionId >> 8) << 3;
    }

    public int getChunkY() {
        return (instanceRegionId & 0xFF) << 3;
    }

    public static final List<GodwarsInstancePortal> values = List.of(values());

    @SuppressWarnings("unchecked")
    GodwarsInstancePortal(String displayName, int cost, int portalObjectId, int instanceRegionId, int altarId,
                          String instanceClass, Location portalLocation, Location godwarsPortalLocation,
                          Location altarTeleportLocation, GodType god) {
        this.displayName = displayName;
        this.cost = cost;
        this.portalObjectId = portalObjectId;
        this.instanceRegionId = instanceRegionId;
        this.altarId = altarId;
        Class<? extends GodwarsInstance> instanceClazz = null;
        try {
            instanceClazz = (Class<? extends GodwarsInstance>) Class.forName(instanceClass);
        } catch (ClassNotFoundException e) {
            //throw new RuntimeException(e);
        }
        this.instanceClass = instanceClazz;
        this.portalLocation = portalLocation;
        this.godwarsPortalLocation = godwarsPortalLocation;
        this.altarTeleportLocation = altarTeleportLocation;
        this.god = god;
    }

    GodwarsInstancePortal(String displayName, int cost, int portalObjectId, int instanceRegionId, int altarId, Class<?
            extends GodwarsInstance> instanceClass, Location portalLocation, Location godwarsPortalLocation,
                          Location altarTeleportLocation, GodType god) {
        this.displayName = displayName;
        this.cost = cost;
        this.portalObjectId = portalObjectId;
        this.instanceRegionId = instanceRegionId;
        this.altarId = altarId;
        this.instanceClass = instanceClass;
        this.portalLocation = portalLocation;
        this.godwarsPortalLocation = godwarsPortalLocation;
        this.altarTeleportLocation = altarTeleportLocation;
        this.god = god;
    }

    public static List<GodwarsInstancePortal> getValues() {
        return values;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCost() {
        return cost;
    }

    public int getPortalObjectId() {
        return portalObjectId;
    }

    public int getInstanceRegionId() {
        return instanceRegionId;
    }

    public int getAltarId() {
        return altarId;
    }

    public Class<? extends GodwarsInstance> getInstanceClass() {
        return instanceClass;
    }

    public Location getPortalLocation() {
        return portalLocation;
    }

    public Location getGodwarsPortalLocation() {
        return godwarsPortalLocation;
    }

    public Location getAltarTeleportLocation() {
        return altarTeleportLocation;
    }

    public GodType getGod() {
        return god;
    }

    public static int getCost(GodType god) {
        for (GodwarsInstancePortal godwarsInstancePortal : values) {
            if (godwarsInstancePortal.god == god) {
                return godwarsInstancePortal.cost;
            }
        }

        return InstanceConstants.INSTANCE_COST;
    }

}

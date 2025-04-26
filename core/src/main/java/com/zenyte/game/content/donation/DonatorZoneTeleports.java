package com.zenyte.game.content.donation;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.TextUtils;

public enum DonatorZoneTeleports {

    UDI(MemberRank.UBER, create(new Location(3393, 7758, 0))),
    LDI(MemberRank.LEGENDARY, create(new Location(3390, 7575, 0))),
    RDI(MemberRank.RESPECTED, create(new Location(3404, 8003, 0))),
    DI(MemberRank.PREMIUM, create(new Location(3359, 8170, 2))),
    DIE(MemberRank.EXPANSION, create(new Location(3359, 8299, 2)));

    private Location location;
    DonatorZoneTeleports(MemberRank rank, Teleport teleport) {
        this.location = teleport.getDestination();
        String rankName = TextUtils.capitalizeEnum(rank.name());
        new GameCommands.Command(PlayerPrivilege.PLAYER, name().toLowerCase(), rankName+" Member Zone", (p, args) -> {
            if (p.isLocked()) {
                return;
            }
            if(p.getMemberRank().equalToOrGreaterThan(rank) || p.isStaff())
            teleport.teleport(p);
            else {
                p.sendMessage("You must be "+rankName+"+ to use this teleport.");
            }
        });
    }

    public Location getLocation() {
        return location;
    }

    @Subscribe
    public static void on(ServerLaunchEvent event) {
    }

    private static Teleport create(Location location) {
        return new Teleport() {
            @Override
            public TeleportType getType() {
                return TeleportType.NEAR_REALITY_PORTAL_TELEPORT;
            }

            @Override
            public Location getDestination() {
                return location;
            }

            @Override
            public int getLevel() {
                return 0;
            }

            @Override
            public double getExperience() {
                return 0;
            }

            @Override
            public int getRandomizationDistance() {
                return 3;
            }

            @Override
            public Item[] getRunes() {
                return null;
            }

            @Override
            public int getWildernessLevel() {
                return 20;
            }

            @Override
            public boolean isCombatRestricted() {
                return false;
            }
        };
    }
}

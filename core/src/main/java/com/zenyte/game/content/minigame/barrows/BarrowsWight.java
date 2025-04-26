package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kris | 28/11/2018 21:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * <p>Enum#ordinal() is being used for this enum. Preserve order.</p>
 */

public enum BarrowsWight {
    
    AHRIM(new Location(3565, 3288, 0), new Location(3557, 9703, 3),
            new Location(3557, 9699, 3), 20770, 98, 6737, 29228, 1672, 20667,
            new Item[]{new Item(4708), new Item(4712), new Item(4714), new Item(4710)}),
    
    DHAROK(new Location(3575, 3298, 0), new Location(3556, 9718, 3),
            new Location(3555, 9716, 3), 20720, 115, 6738, 29225, 1673, 20668,
            new Item[]{new Item(4716), new Item(4720), new Item(4722), new Item(4718)}),
    
    GUTHAN(new Location(3577, 3281, 0), new Location(3534, 9704, 3),
            new Location(3539, 9705, 3), 20722, 115, 6739, 29224, 1674, 20669,
            new Item[]{new Item(4724), new Item(4728), new Item(4730), new Item(4726)}),
    
    KARIL(new Location(3565, 3275, 0), new Location(3546, 9684, 3),
            new Location(3549, 9683, 3), 20771, 98, 6740, 29226, 1675, 20670,
            new Item[]{new Item(4732), new Item(4736), new Item(4738), new Item(4734)}),
    
    TORAG(new Location(3553, 3283, 0), new Location(3568, 9683, 3),
            new Location(3568, 9686, 3), 20721, 115, 6741, 29227, 1676, 20671,
            new Item[]{new Item(4745), new Item(4749), new Item(4751), new Item(4747)}),
    
    VERAC(new Location(3556, 3298, 0), new Location(3578, 9706, 3),
            new Location(3575, 9706, 3), 20772, 115, 6742, 29223, 1677, 20672,
            new Item[]{new Item(4753), new Item(4757), new Item(4759), new Item(4755)});


    public static final List<Item> ALL_WIGHT_EQUIPMENT = Arrays.stream(values())
            .map(BarrowsWight::getArmour)
            .map(Arrays::asList)
            .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
    static BarrowsWight[] values = values();
    private final Location moundCenter, inChamber, bySarcophagus;
    private final int sarcophagusId, combatLevel, surfaceModelId, cryptModelId, npcId, staircaseId;
    private final Item[] armour;

    int getModel(final Player player) {
        return player.getPlane() == 0 ? cryptModelId : surfaceModelId;
    }

    BarrowsWight(Location moundCenter, Location inChamber, Location bySarcophagus, int sarcophagusId, int combatLevel, int surfaceModelId, int cryptModelId, int npcId, int staircaseId, Item[] armour) {
        this.moundCenter = moundCenter;
        this.inChamber = inChamber;
        this.bySarcophagus = bySarcophagus;
        this.sarcophagusId = sarcophagusId;
        this.combatLevel = combatLevel;
        this.surfaceModelId = surfaceModelId;
        this.cryptModelId = cryptModelId;
        this.npcId = npcId;
        this.staircaseId = staircaseId;
        this.armour = armour;
    }

    public Location getMoundCenter() {
        return moundCenter;
    }

    public Location getInChamber() {
        return inChamber;
    }

    public Location getBySarcophagus() {
        return bySarcophagus;
    }

    public int getSarcophagusId() {
        return sarcophagusId;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public int getSurfaceModelId() {
        return surfaceModelId;
    }

    public int getCryptModelId() {
        return cryptModelId;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getStaircaseId() {
        return staircaseId;
    }

    public Item[] getArmour() {
        return armour;
    }
}

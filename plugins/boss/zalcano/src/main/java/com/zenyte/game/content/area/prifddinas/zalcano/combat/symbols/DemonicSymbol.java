package com.zenyte.game.content.area.prifddinas.zalcano.combat.symbols;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants.DEMONIC_BLUE_SYMBOL_ID;
import static com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants.DEMONIC_ORANGE_SYMBOL_ID;


public class DemonicSymbol extends WorldObject {

    private static final Graphics BURN_GRAPHIC = new Graphics(1725);

    private final ZalcanoInstance instance;
    private final boolean isOrange;
    private int ticksAlive;

    /**
     * Is orange symbol will basically check each person
     *
     * @param location
     * @param isOrange
     */
    public DemonicSymbol(ZalcanoInstance instance, Location location, boolean isOrange) {
        super(isOrange ? DEMONIC_ORANGE_SYMBOL_ID : DEMONIC_BLUE_SYMBOL_ID, 10, 0, location);
        this.isOrange = isOrange;
        this.instance = instance;
    }


    public Location getMiddle() {
        return new Location(getX() + 1, getY() + 1);
    }

    public void processOrange() {
        ticksAlive++;
        int maxDamage = (ticksAlive) / 2;
        if (ticksAlive <= 10) {
            maxDamage = 0;
        }

        for (Player player : instance.getPlayers()) {
            if (player.getLocation().getTileDistance(getMiddle()) <= 1) {
                if (isOrange) {
                    player.applyHit(new Hit(instance.getZalcano(), Utils.random(0, maxDamage), HitType.DEFAULT));
                    player.setGraphics(BURN_GRAPHIC);
                    var currentRun = player.getVariables().getRunEnergy();
                    player.getVariables().setRunEnergy(currentRun - 1);
                    player.setForceTalk("Agh!");
                    player.sendMessage("You feel the symbol beneath your feet burn through you!");
                    player.getCombatAchievements().setCurrentTaskValue(CAType.PERFECT_ZALCANO, 0);
                }
            }
        }
    }

    public boolean isStandingOnBlue(Player player) {
        if (player.getLocation().getTileDistance(getMiddle()) <= 1) {
            return !isOrange;
        }
        return false;
    }


}

package com.zenyte.game.content.chambersofxeric;

import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom;
import com.zenyte.game.world.entity.player.Player;
import kotlin.Pair;

import java.util.List;

public class ScalingMechanics {
    public static int getScavengerCount(Raid raid) {
        final int size = raid.getCurrentPlayerScale();
        return
                size <= 1 ? 1 :
                size <= 4 ? 2 :
                size <= 7 ? 3 :
                size <= 15 ? 4 :
                size <= 23 ? 5 :
                size <= 31 ? 6 :
                size <= 39 ? 7 : 8;
    }

    public static int getShamanCount(Raid raid) {
        final int size = raid.getCurrentPlayerScale();
        return size < 4 ? 2 :
               size <= 7 ? 3 : 4;
    }

    public static int getKindlingRequirement(Raid raid, int kindling) {
        return Math.min(raid.getCurrentPlayerScale(), kindling);
    }

    public static int getIcefiendCount(Raid raid) {
        return Math.min(raid.getCurrentPlayerScale(), 4);
    }

    public static Pair<Integer, Integer> getDeathlyMobCount(Raid raid) {
        final int amount = raid.getCurrentPlayerScale();
        final int rangersAmount = amount >= 7 ? 4 : amount >= 4 ? 3 : 2;
        final int magersAmount = amount > 9 ? 4 : amount >= 4 ? 3 : 2;
        return new Pair<>(rangersAmount, magersAmount);
    }


    public static int getSkeletalMysticCount(Raid raid) {
        final int size = raid.getCurrentPlayerScale();
        return size < 3 ? 3 : size < 5 ? 4 : size < 8 ? 5 : 6;
    }

    public static int getXericsAidDropQuantity(Raid raid) {
        return raid.getCurrentPlayerScale() > 8 ? 2 : 1;
    }

    public static int getDeathlyRangeMaxHit(Raid raid) {
        int baseHit = raid.usingFakeScale ? 23 : 46;
        return (int) (baseHit + (raid.getCurrentPlayerScale() * 0.38F * (raid.isChallengeMode() ? 1.5F : 1.0F)));
    }

    public static int getDeathlyMageMaxHit(Raid raid) {
        int baseHit = raid.usingFakeScale ? 17 : 34;
       return  (int) (baseHit * (raid.isChallengeMode() ? 1.5F : 1.0F));
    }

    public static int getIceDemonMaxHP(Raid raid) {
        if(raid.usingFakeScale)
            return 1250;
        return raid.getCurrentPlayerScale() * 250;
    }

    public static int getCorruptedScavengerHP(Raid raid) {
        int absoluteMinimum = 300;
        int dynamicScale = (raid.getCurrentPlayerScale() - 1) * 16;

        return Math.min(absoluteMinimum, Math.max(30, 15 + dynamicScale)) * 10;
    }

    public static int getOlmBombCount(Raid raid) {
        final int players = raid.getCurrentPlayerScale();
        return players >= 56 ? 5 :
               players >= 24 ? 4 :
               players >= 8 ? 3 : 2;
    }

    public static int getOlmLightningCount(GreatOlm olm) {
        final List<Player> everyone = olm.everyone(GreatOlm.ENTIRE_CHAMBER);
        return everyone.size() >= 6 ? 5 : 4;
    }

    public static int getOlmMaxHitStandard(Raid raid) {
        if(raid.isBypassed)
            return 25;
        return 33 + (raid.getCurrentPlayerScale() / 8);
    }

    public static int getTotalOlmPhases(Raid raid) {
        if(raid.isBypassed)
            return 3;
        return 3 + (raid.getCurrentPlayerScale() / 8);
    }

    public static int getOlmClenchThreshold(OlmRoom room) {
        if(room.getRaid().usingFakeScale)
            return 150;
        return room.getOlm().everyone(GreatOlm.ENTIRE_CHAMBER).size() * 20;
    }

    public static double getOlmClawHpMulitplier(Raid raid) {
        if(raid.usingFakeScale)
            return Math.min(10, raid.getCurrentPlayerScale());
        return raid.getCurrentPlayerScale();
    }

    public static int getOlmClawHp(Raid raid) {
        return (int) Math.floor(300 + (getOlmClawHpMulitplier(raid) * 300));
    }

    public static double getOlmHpMultiplier(Raid raid) {
        if(raid.usingFakeScale)
            return Math.min(10, raid.getCurrentPlayerScale());
        return raid.getCurrentPlayerScale();
    }

    public static int getOlmHp(Raid raid) {
        return (int) Math.floor(400 + (getOlmHpMultiplier(raid) * 400));
    }
}

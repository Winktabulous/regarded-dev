package com.zenyte.game.content.skills.afk;

import com.near_reality.game.content.shop.ShopCurrencyHandler;
import com.zenyte.game.content.boons.impl.NoOnesHome;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.shop.ShopCurrency;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

public abstract class BasicAfkAction extends Action {



    @Override
    public boolean start() {

        if(!check())
            return false;

        player.setAnimation(actionAnimation());
        delay(5);
        return true;
    }

    @Override
    public boolean process() {
        if(!check())
            return false;

        return true;
    }

    @Override
    public int processWithDelay() {
        int amt = Utils.random(1, 3);
        if (doubleAfkPointsAmount())
            amt *= 2;
        player.setAnimation(actionAnimation());
        player.sendFilteredMessage(getMessage()+" and receive "+amt+" AFK points.");
        boolean boon = player.getBoonManager().hasBoon(NoOnesHome.class);
        double xp = getExp(player.getSkillingXPRate());
        xp *= 2;
        if(boon) xp *= 2;
        player.getSkills().addXp(getSkill(), xp, true, false, false);
        ShopCurrencyHandler.add(ShopCurrency.AFK_POINTS, player, amt);
        return 5;
    }

    private boolean doubleAfkPointsAmount() {
        return player.getEquipment().containsAnyOf(ItemId.SLEEPING_CAP, ItemId.SLEEPING_CAP_10746, 32240);
    }

    public boolean check() {
        if (getSkillCap() != -1 && player.getSkills().getLevel(getSkill()) > getSkillCap()) {
            player.sendMessage("You are above the allowed level to perform this action.");
            return false;
        }
        if(!hasRequiredItem()) {
            return false;
        }

        return true;
    }

    public abstract Animation actionAnimation();

    public abstract int getSkill();

    public int getSkillCap() {
        return 99;
    }

    public abstract String getMessage();

    public double getExp(int rate) {
        if(rate >= 50)
            return 1.5;
        if(rate >= 20)
            return 2.0;
        if(rate >= 10)
            return 2.75;
        if(rate >= 5)
            return 4.8;
        return 1.5;
    }

    public abstract boolean hasRequiredItem();
}

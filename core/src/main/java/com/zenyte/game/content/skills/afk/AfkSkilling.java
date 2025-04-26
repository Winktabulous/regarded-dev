package com.zenyte.game.content.skills.afk;

import com.near_reality.game.content.shop.ShopCurrencyHandler;
import com.zenyte.game.model.shop.ShopCurrency;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.TimeUnit;

import static com.zenyte.game.content.skills.afk.AfkSkillingConstants.*;
import static com.zenyte.game.content.skills.afk.AfkSkillingConstants.AFK_TICKS;

public class AfkSkilling {

    public static long getAfkTime(Player player) {
        return player.getNumericAttribute(AFK_TIME).longValue();
    }

    public static void pushAfkAreaTicks(Player player) {
        player.getAttributes().put(AFK_TICKS, player.getNumericAttribute(AFK_TICKS).longValue() + 100L);
    }

    public static boolean hasAfkTime(Player player) {
        return true;
    }


    public static void addAfkTime(Player player, long time) {
        long l = player.getNumericAttribute(AFK_TIME).longValue();
        if(l > System.currentTimeMillis())
            player.getAttributes().put(AFK_TIME, l + time);
        else
            player.getAttributes().put(AFK_TIME, System.currentTimeMillis() + time);

    }

    public static void buyTime(Player player, int votePoints, int hours) {
        if(ShopCurrencyHandler.getAmount(ShopCurrency.VOTE_POINTS, player) < votePoints) {
            player.sendMessage("You can not afford this extension, you have " + ShopCurrencyHandler.getAmount(ShopCurrency.VOTE_POINTS, player) + " vote points.");
            return;
        }

        player.getDialogueManager().start(new Dialogue(player, AFK_MASTER) {
            @Override
            public void buildDialogue() {
                options("Buy <col=000080>"+hours+"</col> hour(s) for <col=000080>"+votePoints+"</col> vote points?", "Yes", "No")
                        .onOptionOne(() -> {
                            addAfkTime(player, TimeUnit.HOURS.toMillis(hours));
                            ShopCurrencyHandler.remove(ShopCurrency.VOTE_POINTS, player, votePoints);
                            setKey(5);
                        });
                plain(5, "You've successfully purchased <col=000080>"+hours+"</col> hours. You have <col=000080>"+(ShopCurrencyHandler.getAmount(ShopCurrency.VOTE_POINTS, player) - votePoints)+"</col> Vote points remaining.");

            }
        });
    }

    public static boolean overTimeLimit(Player player) {
        return player.getNumericAttribute(AFK_TICKS).longValue() > TimeUnit.HOURS.toTicks(10);
    }
}

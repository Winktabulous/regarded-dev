package com.zenyte.game.content.skills.firemaking;

import com.zenyte.game.content.boons.impl.Pyromaniac;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

public class BonfireAction extends Action {


    private final Firemaking firemaking;


    public BonfireAction(Firemaking firemaking) {
        this.firemaking = firemaking;
    }

    public boolean check() {
        Firemaking logs = firemaking;
        if(logs != null && logs.getLevel() > player.getSkills().getLevel(SkillConstants.FIREMAKING)) {
            player.sendMessage("You lack the required firemaking level to light these.");
            return false;
        }
        return player.getInventory().containsItem(firemaking.getLogs());
    }

    @Override
    public boolean start() {
        return check();
    }

    @Override
    public boolean process() {
        return check();
    }

    @Override
    public int processWithDelay() {
        player.getInventory().deleteItem(firemaking.getLogs().getId(), 1);
        player.setAnimation(new Animation(827));
        player.getSkills().addXp(SkillConstants.FIREMAKING, firemaking.getXp());
        if(player.getBoonManager().hasBoon(Pyromaniac.class) && Pyromaniac.rollPage()) {
            player.sendFilteredMessage("Your Pyromancer boon causes a burnt page to arise from the flames.");
            player.getInventory().addOrDrop(ItemId.BURNT_PAGE, 1);
        }
        return 4;
    }

    @Override
    public void stop() {
        delay(3);
    }
}

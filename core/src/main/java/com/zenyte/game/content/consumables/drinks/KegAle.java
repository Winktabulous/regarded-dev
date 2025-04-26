package com.zenyte.game.content.consumables.drinks;

import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.consumables.Drinkable;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.DrinkablePlugin;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 02/12/2018 18:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum KegAle implements Drinkable {
    KEG_OF_BEER(new int[] {1330}, new Restoration(SkillConstants.HITPOINTS, 0, 15), new Boost(SkillConstants.STRENGTH, 0, 10), new Debuff(SkillConstants.ATTACK, 0.43F, 0)) {
        @Override
        public Item leftoverItem(int id) {
            return null;
        }

        @Override
        public String startMessage() {
            return "You chug the keg. You feel reinvigorated...";
        }

        @Override
        public String endMessage(final Player player) {
            return "... but extremely drunk too.";
        }

        @Override
        public Animation animation() {
            return new Animation(1330);
        }
    },
    DWARVEN_STOUT(new int[] {5771, 5773, 5775, 5777}, new Boost(SkillConstants.MINING, 0, 1), new Boost(SkillConstants.SMITHING, 0, 1), new Debuff(SkillConstants.ATTACK, 0, 3), new Debuff(SkillConstants.STRENGTH, 0, 3), new Restoration(SkillConstants.HITPOINTS, 0, 1)), ASGARNIAN_ALE(new int[] {5779, 5781, 5783, 5785}, new Restoration(SkillConstants.HITPOINTS, 0, 2), new Boost(SkillConstants.STRENGTH, 0, 2), new Debuff(SkillConstants.ATTACK, 0, 4)), GREENMANS_ALE(new int[] {5787, 5789, 5791, 5793}, new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.HERBLORE, 0, 1), new Debuff(SkillConstants.ATTACK, 0, 9), new Debuff(SkillConstants.STRENGTH, 0, 9), new Debuff(SkillConstants.DEFENCE, 0, 9)), MIND_BOMB(new int[] {5795, 5797, 5799, 5801}, new Boost(SkillConstants.MAGIC, 0.02F, 2), new Debuff(SkillConstants.ATTACK, 0, 3), new Debuff(SkillConstants.STRENGTH, 0, 4), new Debuff(SkillConstants.DEFENCE, 0, 4)), DRAGON_BITTER(new int[] {5803, 5805, 5807, 5809}, new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.STRENGTH, 0, 2), new Debuff(SkillConstants.ATTACK, 0, 4)), MOONLIGHT_MEAD(new int[] {5811, 5813, 5815, 5817}, new Restoration(SkillConstants.HITPOINTS, 0, 4)), AXEMANS_FOLLY(new int[] {5819, 5821, 5823, 5825}, new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.WOODCUTTING, 0, 1), new Debuff(SkillConstants.STRENGTH, 0, 3), new Debuff(SkillConstants.ATTACK, 0, 3)), CHEFS_DELIGHT(new int[] {5827, 5829, 5831, 5833}, new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.COOKING, 0.05F, 1), new Debuff(SkillConstants.ATTACK, 0, 2), new Debuff(SkillConstants.STRENGTH, 0, 2)), SLAYERS_RESPITE(new int[] {5835, 5837, 5839, 5841}, new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.SLAYER, 0, 2), new Debuff(SkillConstants.ATTACK, 0, 2), new Debuff(SkillConstants.STRENGTH, 0, 2)), CIDER(new int[] {5843, 5845, 5847, 5849}, new Restoration(SkillConstants.HITPOINTS, 0, 2), new Boost(SkillConstants.FARMING, 0, 1), new Debuff(SkillConstants.ATTACK, 0, 2), new Debuff(SkillConstants.STRENGTH, 0, 2)), MATURED_DWARVEN_STOUT(new int[] {5851, 5853, 5855, 5857}, new Boost(SkillConstants.MINING, 0, 2), new Boost(SkillConstants.SMITHING, 0, 2), new Debuff(SkillConstants.ATTACK, 0, 7), new Debuff(SkillConstants.STRENGTH, 0, 7), new Debuff(SkillConstants.DEFENCE, 0, 7), new Restoration(SkillConstants.HITPOINTS, 0, 1)), MATURED_ASGARNIAN_ALE(new int[] {5859, 5861, 5863, 5865}, new Restoration(SkillConstants.HITPOINTS, 0, 2), new Boost(SkillConstants.STRENGTH, 0, 3), new Debuff(SkillConstants.ATTACK, 0, 6)), MATURED_GREENMANS_ALE(new int[] {5867, 5869, 5871, 5873}, new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.HERBLORE, 0, 2), new Debuff(SkillConstants.ATTACK, 0, 2), new Debuff(SkillConstants.STRENGTH, 0, 2)), MATURED_MIND_BOMB(new int[] {5875, 5877, 5879, 5881}, new Boost(SkillConstants.MAGIC, 0.02F, 3), new Debuff(SkillConstants.ATTACK, 0, 5), new Debuff(SkillConstants.STRENGTH, 0, 5), new Debuff(SkillConstants.DEFENCE, 0, 5)), MATURED_DRAGON_BITTER(new int[] {5883, 5885, 5887, 5889}, new Restoration(SkillConstants.HITPOINTS, 0, 2), new Boost(SkillConstants.STRENGTH, 0, 3), new Debuff(SkillConstants.ATTACK, 0, 6)), MATURED_MOONLIGHT_MEAD(new int[] {5891, 5893, 5895, 5897}, new Restoration(SkillConstants.HITPOINTS, 0, 6)), MATURED_AXEMANS_FOLLY(new int[] {5899, 5901, 5903, 5905}, new Restoration(SkillConstants.HITPOINTS, 0, 2), new Boost(SkillConstants.WOODCUTTING, 0, 2), new Debuff(SkillConstants.STRENGTH, 0, 4), new Debuff(SkillConstants.ATTACK, 0, 4)), MATURED_CHEFS_DELIGHT(new int[] {5907, 5909, 5911, 5913}, new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.COOKING, 0.05F, 2), new Debuff(SkillConstants.ATTACK, 0, 2), new Debuff(SkillConstants.STRENGTH, 0, 2)), MATURED_SLAYERS_RESPITE(new int[] {5915, 5917, 5919, 5921}, new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.SLAYER, 0, 4), new Debuff(SkillConstants.ATTACK, 0, 2), new Debuff(SkillConstants.STRENGTH, 0, 2)), MATURED_CIDER(new int[] {5843, 5845, 5847, 5849}, new Restoration(SkillConstants.HITPOINTS, 0, 2), new Boost(SkillConstants.FARMING, 0, 2), new Debuff(SkillConstants.ATTACK, 0, 5), new Debuff(SkillConstants.STRENGTH, 0, 5));

    KegAle(final int[] ids, final Boost... boosts) {
        this.ids = ids;
        this.boosts = boosts;
    }

    private static final Item keg = new Item(5769);
    /**
     * The ids of the potion. These go in ascending order, so effectively {@code { 1dose, 2doses, 3doses, 4doses }}.
     */
    private final int[] ids;
    private final Boost[] boosts;
    public static final KegAle[] values = values();

    @Override
    public Item leftoverItem(int id) {
        final int index = ArrayUtils.indexOf(ids, id);
        if (index == -1) {
            throw new RuntimeException("Invalid id: " + id + " " + this);
        }
        if (index == 0) {
            return new Item(keg);
        }
        return new Item(ids[index - 1]);
    }

    @Override
    public Item getItem(final int doses) {
        if (doses < 0 || doses > ids.length) throw new RuntimeException("The keg " + this + " doesn't support dose " + doses + ".");
        if (doses == 0) {
            return keg;
        }
        return new Item(ids[doses - 1]);
    }

    @Override
    public Boost[] boosts() {
        return boosts;
    }

    @Override
    public String startMessage() {
        return "You drink some %s%s.";
    }

    @Override
    public int delay() {
        return 4;
    }

    @Override
    public String endMessage(Player player) {
        return "You have %d dose%s of potion left.";
    }

    @Override
    public int getDoses(int id) {
        final int index = ArrayUtils.indexOf(ids, id);
        if (index == -1) {
            throw new RuntimeException("Invalid id: " + id + " " + this);
        }
        return index + 1;
    }

    @Override
    public Animation animation() {
        return anim;
    }

    @Override
    public String emptyMessage(final Player player) {
        return "The keg is now empty.";
    }

    private static final Animation anim = new Animation(2289);

    @Override
    public void consume(final Player player, final Item item, final int slotId) {
        final RegionArea area = player.getArea();
        if ((area instanceof DrinkablePlugin && !((DrinkablePlugin) area).drink(player, this)) || !canConsume(player)) {
            return;
        }
        final String name = item.getDefinitions().getName();
        final Inventory inventory = player.getInventory();
        player.setUnprioritizedAnimation(animation());
        player.getPacketDispatcher().sendSoundEffect(sound);
        final int id = item.getId();
        final String startMessage = startMessage();
        player.sendFilteredMessage(String.format(startMessage, (name.contains("(m") ? "matured " : ""), name.replaceAll("[([m][1-4])]", "")));
        final int doses = getDoses(id) - 1;
        if (doses == 0) {
            player.sendFilteredMessage(emptyMessage(player));
        }
        final Item leftover = leftoverItem(item.getId());
        if (leftover != null) {
            inventory.set(slotId, leftover);
        } else {
            inventory.deleteItem(slotId, new Item(id, 1));
        }
        applyEffects(player);
    }

    @Override
    public void applyEffects(final Player player) {
        final int delay = delay();
        if (delay > 0) {
            player.getVariables().setPotionDelay(delay);
        }
        onConsumption(player);
        heal(player);
        final Consumable.Boost[] boosts = boosts();
        if (boosts != null) {
            for (Boost boost : boosts) {
                boost.apply(player);
            }
        }
    }

    public int[] getIds() {
        return ids;
    }
}

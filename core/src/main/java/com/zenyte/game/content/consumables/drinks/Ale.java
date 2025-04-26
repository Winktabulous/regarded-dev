package com.zenyte.game.content.consumables.drinks;

import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.consumables.ConsumableAnimation;
import com.zenyte.game.content.consumables.Drinkable;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.GameSetting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.DrinkablePlugin;

/**
 * @author Kris | 02/12/2018 22:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Ale implements Drinkable {
    BEER_TANKARD(3803, "You quaff the beer. You feel reinvigorated...", "...but very dizzy too.", new Boost(SkillConstants.STRENGTH, 0, 4), new Debuff(SkillConstants.ATTACK, 0, 9)) {
        @Override
        public Item leftoverItem(final int id) {
            return new Item(3805);
        }
    },
    GROG(1915, "You drink the Grog. You feel slightly reinvigorated...", "...and slightly dizzy too.", new Restoration(SkillConstants.HITPOINTS, 0, 3), new Boost(SkillConstants.STRENGTH, 0, 3), new Debuff(SkillConstants.ATTACK, 0, 6)), BEER(1917, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.STRENGTH, 0, 4), new Debuff(SkillConstants.ATTACK, 0, 7)), BANDITS_BREW(4627, "You drink the Bandit's Brew.", "It has a clean, refreshing taste.", new Boost(SkillConstants.THIEVING, 0, 1), new Boost(SkillConstants.ATTACK, 0, 1), new Debuff(SkillConstants.STRENGTH, 0, 1), new Debuff(SkillConstants.DEFENCE, 0, 6)), POH_BEER(7740, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", new Restoration(SkillConstants.HITPOINTS, 0, 1), new Boost(SkillConstants.STRENGTH, 0, 4), new Debuff(SkillConstants.ATTACK, 0, 7)), BLOODY_BRACER(22430, "You drink the Bloody Bracer.", "The tincture tastes great, but you feel an evil within you stir.", new Restoration(SkillConstants.HITPOINTS, 0, 2), new Debuff(SkillConstants.PRAYER, 0, 4)), //TODO: Can boost over max hp.
    PINT_OF_BLOOD(24774, "You drink the Blood Pint...", "...it tastes unsurprisingly awful and makes you feel a bit sick"),
    KELDA_STOUT(6118, null, null) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("This stout seems absolutely vile and disgusting. Besides, I'm supposed to bring it to" +
                    " my drunken, kebab obsessed friend.");
            return false;
        }
    },
    ASGOLDIAN_ALE(7508, null, null) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I don't think I'd like gold in beer thanks. Leave it for the dwarves.");
            return false;
        }
    },
    DWARVEN_STOUT(1913, "You drink the Dwarven Stout. It tastes foul.", "It tastes pretty strong too", KegAle.DWARVEN_STOUT), ASGARNIAN_ALE(1905, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.ASGARNIAN_ALE), GREENMANS_ALE(1909, "You drink the Greenman's Ale.", "It has a strange taste.", KegAle.GREENMANS_ALE), MIND_BOMB(1907, "You drink the Wizard's Mind Bomb.", "You feel very strange.", KegAle.MIND_BOMB), DRAGON_BITTER(1911, "You drink the Dragon Bitter. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.DRAGON_BITTER), MOONLIGHT_MEAD(2955, "You drink the foul smelling brew.", "It tastes like something just died in your mouth.", KegAle.MOONLIGHT_MEAD), AXEMANS_FOLLY(5751, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.AXEMANS_FOLLY), CHEFS_DELIGHT(5755, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.CHEFS_DELIGHT), SLAYERS_RESPITE(5759, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.SLAYERS_RESPITE), CIDER(5763, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.CIDER), POH_ASGARNIAN_ALE(7744, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.ASGARNIAN_ALE), POH_GREENMANS_ALE(7746, "You drink the Greenman's Ale.", "It has a strange taste.", KegAle.GREENMANS_ALE), POH_DRAGON_BITTER(7748, "You drink the Dragon Bitter. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.DRAGON_BITTER), POH_MOONLIGHT_MEAD(7750, "You drink the foul smelling brew.", "It tastes like something just died in your mouth.", KegAle.MOONLIGHT_MEAD), POH_CIDER(7752, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.CIDER), POH_CHEFS_DELIGHT(7754, "You drink the Beer. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.CHEFS_DELIGHT), MATURED_DWARVEN_STOUT(5747, "You drink the matured Dwarven Stout. It tastes foul.", "It tastes pretty strong too", KegAle.MATURED_DWARVEN_STOUT), MATURED_ASGARNIAN_ALE(5739, "You drink the ale. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.MATURED_ASGARNIAN_ALE), MATURED_GREENMANS_ALE(5743, "You drink the matured Greenman's Ale.", "It has a strange taste.", KegAle.MATURED_GREENMANS_ALE), MATURED_MIND_BOMB(5741, "You drink the matured Wizard's Mind Bomb.", "You feel very strange.", KegAle.MATURED_MIND_BOMB), MATURED_DRAGON_BITTER(5745, "You drink the matured Dragon Bitter. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.MATURED_DRAGON_BITTER), MATURED_MOONLIGHT_MEAD(5749, "You drink the foul smelling brew.", "It tastes like something just died in your mouth.", KegAle.MATURED_MOONLIGHT_MEAD), MATURED_AXEMANS_FOLLY(5753, "You drink the ale. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.MATURED_AXEMANS_FOLLY), MATURED_CHEFS_DELIGHT(5757, "You drink the ale. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.MATURED_CHEFS_DELIGHT), MATURED_SLAYERS_RESPITE(5761, "You drink the ale. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.MATURED_SLAYERS_RESPITE), MATURED_CIDER(5765, "You drink the ale. You feel slightly reinvigorated...", "...and slightly dizzy too.", KegAle.MATURED_CIDER);

    Ale(int id, String startMessage, String endMessage, KegAle kegAle) {
        this(id, startMessage, endMessage, kegAle.boosts());
    }

    Ale(int id, String startMessage, String endMessage, Boost... boosts) {
        this.id = id;
        this.startMessage = startMessage;
        this.endMessage = endMessage;
        this.boosts = boosts;
    }

    private static final Item emptyBeerGlass = new Item(1919);
    /**
     * The ids of the potion. These go in ascending order, so effectively {@code { 1dose, 2doses, 3doses, 4doses }}.
     */
    private final int id;
    private final String startMessage;
    private final String endMessage;
    private final Boost[] boosts;
    public static final Ale[] values = values();

    public int[] getIds() {
        return new int[] {id};
    }

    @Override
    public Item leftoverItem(int id) {
        return emptyBeerGlass;
    }

    @Override
    public Item getItem(final int doses) {
        if (doses < 0 || doses > 1) throw new RuntimeException("The potion " + this + " doesn't support dose " + doses + ".");
        if (doses == 0) {
            return leftoverItem(-1);
        }
        return new Item(id);
    }

    @Override
    public Boost[] boosts() {
        return boosts;
    }

    @Override
    public String startMessage() {
        return startMessage;
    }

    @Override
    public int delay() {
        return 4;
    }

    @Override
    public String endMessage(Player player) {
        return endMessage;
    }

    @Override
    public int getDoses(int id) {
        return 1;
    }

    @Override
    public String emptyMessage(final Player player) {
        return null;
    }

    @Override
    public Animation animation() {
        return ConsumableAnimation.EAT_ANIM;
    }

    @Override
    public void consume(final Player player, final Item item, final int slotId) {
        final RegionArea area = player.getArea();
        if ((area instanceof DrinkablePlugin && !((DrinkablePlugin) area).drink(player, this)) || !canConsume(player)) {
            return;
        }
        final Inventory inventory = player.getInventory();
        player.setUnprioritizedAnimation(ConsumableAnimation.transformEatAnimation(player, animation()));
        player.getPacketDispatcher().sendSoundEffect(sound);
        final int id = item.getId();
        final String startMessage = startMessage();
        player.sendFilteredMessage(startMessage);
        final String endMessage = endMessage(player);
        if (endMessage != null) {
            player.sendFilteredMessage(endMessage);
        }
        final Item leftover = leftoverItem(item.getId());
        if (leftover != null) {
            if (leftover.getId() == 229 && player.getNumericAttribute(GameSetting.SMASH_VIALS.toString()).intValue() == 1) {
                player.sendFilteredMessage("You quickly smash the empty vial using the trick a Barbarian taught you.");
                inventory.deleteItem(slotId, new Item(id, 1));
            } else {
                inventory.set(slotId, leftover);
            }
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

    public int getId() {
        return id;
    }
}

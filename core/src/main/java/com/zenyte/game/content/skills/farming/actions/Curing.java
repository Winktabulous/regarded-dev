package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.FarmingConstants;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchState;
import com.zenyte.game.content.skills.farming.PatchType;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.ObjectSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.WorldObject;

import java.util.Objects;

/**
 * @author Kris | 16/02/2019 11:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Curing extends Action {
    private static final Animation animation = new Animation(2288);
    private static final SoundEffect sound = new SoundEffect(2438);
    private static final Animation magicPruneAnimation = new Animation(3337);
    private static final Animation pruneAnimation = new Animation(2274);
    private static final SoundEffect pruneSound = new SoundEffect(2440);
    private static final SoundEffect delayedPruneSound = new SoundEffect(2440, 0, 60);

    public Curing(final WorldObject patch, final FarmingSpot spot) {
        this.patch = patch;
        this.spot = spot;
    }

    private final WorldObject patch;
    private final FarmingSpot spot;

    @Override
    public boolean start() {
        final PatchState state = spot.getState();
        if (spot.isClear()) {
            player.sendMessage("This farming patch is empty.");
            return false;
        }
        if (state != PatchState.DISEASED) {
            player.sendMessage("This patch doesn't need curing.");
            return false;
        }
        final Inventory inventory = player.getInventory();
        if (spot.isTreePatch() || spot.getPatch().getType() == PatchType.BUSH_PATCH) {
            final Equipment equipment = player.getEquipment();
            if (!inventory.containsItem(FarmingConstants.SECATEURS) && !inventory.containsItem(FarmingConstants.MAGIC_SECATEURS) && equipment.getId(EquipmentSlot.WEAPON) != FarmingConstants.MAGIC_SECATEURS.getId()) {
                player.sendMessage("To cure trees you need to prune the diseased leaves away with a pair of secateurs.");
                return false;
            }
            player.sendFilteredMessage("You start pruning the " + (spot.getPatch().getType() == PatchType.BUSH_PATCH ? "bush" : "tree") + "...");
            player.setAnimation(inventory.containsItem(FarmingConstants.MAGIC_SECATEURS) || equipment.getId(EquipmentSlot.WEAPON) == FarmingConstants.MAGIC_SECATEURS.getId() ? magicPruneAnimation : pruneAnimation);
            player.sendSound(pruneSound);
            player.sendSound(delayedPruneSound);
            delay(4);
        } else {
            final boolean containsCure = inventory.containsItem(FarmingConstants.PLANT_CURE);
            final SpellState spell = new SpellState(player, Objects.requireNonNull(Magic.getSpell(Spellbook.LUNAR, "cure plant", ObjectSpell.class)));
            if (!containsCure) {
                if (player.getCombatDefinitions().getSpellbook() == Spellbook.LUNAR && spell.check(false)) {
                    player.getActionManager().forceStop();
                    Objects.requireNonNull(Magic.getSpell(Spellbook.LUNAR, "cure plant", ObjectSpell.class)).spellEffect(player, patch);
                    return false;
                }
                final boolean containsSecateurs = inventory.containsItem(FarmingConstants.SECATEURS) || inventory.containsItem(FarmingConstants.MAGIC_SECATEURS);
                if (containsSecateurs) {
                    player.sendMessage("Secateurs don't work with this - you need to use plant cure.");
                } else {
                    player.sendMessage("You need a plant cure to cure the disease on this patch.");
                }
                return false;
            }
            player.setAnimation(animation);
            player.sendSound(sound);
            player.sendFilteredMessage("You treat the allotment with the plant cure.");
            delay(2);
        }
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        spot.cure();
        if (spot.isTreePatch() || spot.getPatch().getType() == PatchType.BUSH_PATCH) {
            player.getInventory().addOrDrop(spot.getProduct().getLeaves());
            player.sendFilteredMessage("You have successfully removed all the diseased leaves.");
            player.setAnimation(Animation.STOP);
        } else {
            player.sendFilteredMessage("It is restored to health.");
            player.getInventory().deleteItem(FarmingConstants.PLANT_CURE);
            player.getInventory().addOrDrop(new Item(229));
        }
        return -1;
    }
}

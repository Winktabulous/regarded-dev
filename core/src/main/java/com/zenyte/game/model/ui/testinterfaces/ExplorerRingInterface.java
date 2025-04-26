package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.plugins.item.ExplorersRing;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.ItemSpell;
import com.zenyte.game.content.skills.magic.spells.regular.HighLevelAlchemy;
import com.zenyte.game.content.skills.magic.spells.regular.LowLevelAlchemy;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;

import static com.zenyte.game.GameInterface.EXPLORER_RING_ALCH;

/**
 * @author Christopher
 * @since 1/25/2020
 */
public class ExplorerRingInterface extends Interface {
    public static final int CHARGES_VARBIT = 4554;
    private static final int HIGH_ALCHEMY_VARBIT = 5398;

    @Override
    protected void attach() {
        put(1, "Low alchemy");
        put(2, "High alchemy");
        put(4, "Close");
        put(7, "Inventory");
        put(8, "Charges");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Inventory"), 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2);
        player.getVarManager().sendBit(CHARGES_VARBIT, player.getVariables().getFreeAlchemyCasts());
    }

    @Override
    protected void build() {
        bind("Close", player -> player.getInterfaceHandler().sendInterface(GameInterface.SPELLBOOK));
        bind("Low alchemy", player -> {
            player.getVarManager().sendBit(HIGH_ALCHEMY_VARBIT, false);
            player.getVarManager().sendBit(CHARGES_VARBIT, player.getVariables().getFreeAlchemyCasts());
        });
        bind("High alchemy", ((player, slotId, itemId, option) -> {
            if (player.carryingItem(ItemId.EXPLORERS_RING_4)) {
                player.getVarManager().sendBit(HIGH_ALCHEMY_VARBIT, true);
                player.getVarManager().sendBit(CHARGES_VARBIT, player.getVariables().getFreeAlchemyCasts());
            } else {
                player.sendMessage("You must unlock the elite tier of rewards from the Lumbridge Diaries before you can do that.");
                player.sendSound(new SoundEffect(2277));
            }
        }));
        bind("Inventory", ((player, slotId, itemId, option) -> {
            if (ExplorersRing.rings.contains(itemId)) {
                player.sendMessage("You see no reason to cast on this item.");
                return;
            }
            final ItemSpell spell = player.getVarManager().getBitValue(HIGH_ALCHEMY_VARBIT) == 1 ? Magic.getSpell(Spellbook.NORMAL, "high level alchemy", HighLevelAlchemy.class) : Magic.getSpell(Spellbook.NORMAL, "low level alchemy", LowLevelAlchemy.class);
            if (spell == null) {
                return;
            }
            spell.execute(player, player.getInventory().getItem(slotId), slotId);
        }));
    }

    @Override
    public GameInterface getInterface() {
        return EXPLORER_RING_ALCH;
    }
}

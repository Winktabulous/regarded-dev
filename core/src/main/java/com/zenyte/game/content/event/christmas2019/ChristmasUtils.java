package com.zenyte.game.content.event.christmas2019;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.event.christmas2019.cutscenes.FrozenGuest;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.LoginEvent;
import mgi.types.config.ObjectDefinitions;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Corey
 * @since 14/12/2019
 */
public class ChristmasUtils {
    
    private static final String IMP_NAME_KEY = "christmas_warble_imp_name";
    private static final String IMP_ID_KEY = "christmas_warble_imp_id";
    
    public static String getImpName(final Player player) {
        return player.getAttributeOrDefault(IMP_NAME_KEY, "Snow Imp");
    }
    
    public static String getFrozenGuestOrder(final Player player) {
        return ChristmasConstants.FROZEN_GUEST_ORDERS[player.getPlayerInformation().getUserIdentifier() % 10];
    }
    
    public static String getImpNpcName(final Player player, final NPC npc) {
        return ChristmasConstants.SNOW_IMP_NAMES[(player.getPlayerInformation().getUserIdentifier() + npc.getIndex()) % 10];
    }
    
    public static void saveImpName(final Player player, final String name) {
        player.addAttribute(IMP_NAME_KEY, name);
    }
    
    public static void saveImpId(final Player player, final int id) {
        player.addAttribute(IMP_ID_KEY, id);
    }
    
    public static PersonalSnowImp.Imp getImp(final Player player) {
        return PersonalSnowImp.Imp.forId(player.getNumericAttribute(IMP_ID_KEY).intValue());
    }
    
    public static boolean wearingGhostCostume(final Player player) {
        return player.getEquipment().containsItems(new Item(ChristmasConstants.GHOST_HOOD_ID),
                new Item(ChristmasConstants.GHOST_TOP_ID),
                new Item(ChristmasConstants.GHOST_BOTTOMS_ID));
    }

    @Subscribe
    public static final void onLogin(@NotNull final LoginEvent event) {
        refreshAllVarbits(event.getPlayer());
    }

    public static final void refreshAllVarbits(@NotNull final Player player) {
        final PersonalSnowImp.Imp imp = getImp(player);
        if (imp != null) {
            player.getVarManager().sendBit(imp.getVarbit(), AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_PERSONAL_IMP) ? 1 : 0);
        }
        player.getVarManager().sendBit(ChristmasConstants.EBENEZER_SCOURGE_VAR, AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_SCOURGE) ? 1 : 0);
        player.getVarManager().sendBit(ChristmasConstants.FROZEN_NPCS_VAR, AChristmasWarble.getProgress(player) == AChristmasWarble.ChristmasWarbleProgress.FROZEN_GUESTS ? 1 : 0);
        if (AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.GHOST_OF_CHRISTMAS_PRESENT)) {
            for (final FrozenGuest guest : FrozenGuest.getValues()) {
                player.getVarManager().sendBit(ObjectDefinitions.getOrThrow(guest.getBaseObject()).getVarbitId(), 0);
            }
        } else if (AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.FROZEN_GUESTS)) {
            final int unfrozenGuestsHash = player.getNumericAttribute(ChristmasConstants.UNFROZEN_GUESTS_HASH_KEY).intValue();
            final String characters = getFrozenGuestOrder(player);
            for (final char c : characters.toCharArray()) {
                final FrozenGuest guest = CollectionUtils.findMatching(FrozenGuest.getValues(), g -> g.getConstant() == c);
                Preconditions.checkArgument(guest != null);
                player.getVarManager().sendBit(ObjectDefinitions.getOrThrow(guest.getBaseObject()).getVarbitId(), ((unfrozenGuestsHash >> guest.ordinal()) & 1) == 1 ? 2 : 3);
            }
        }
        player.getVarManager().sendBit(ChristmasConstants.REWARD_PRESENT_VARBIT, AChristmasWarble.getProgress(player) == AChristmasWarble.ChristmasWarbleProgress.CAN_OPEN_PRESENT ? 1 : 0);
        player.getVarManager().sendBit(ChristmasConstants.FREED_SANTA_VAR, AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SANTA_FREED) ? 1 : 0);
    }
}

package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.rots.RotsInstance;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Kris | 21/10/2018 10:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class BarrowsRewardsInterface extends Interface {
    @Override
    protected void attach() {
        put(3, "Examine");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().closeInterfaces();

        if (player.inArea("Rise of the Six")) {
            RotsInstance area = (RotsInstance) player.getArea();
            if (area.isCompleted()) {
                if (area.isLooted()) {
                    player.sendMessage("The chest is empty!");
                    return;
                }

                area.setLooted(true);
                area.getContainer().setFullUpdate(true);
                player.getPacketDispatcher().sendUpdateItemContainer(area.getContainer());
            }
        } else {
            final Barrows barrows = player.getBarrows();
            barrows.setLooted(true);
            barrows.refreshShaking();
            barrows.calculateLoot();
            barrows.getContainer().setFullUpdate(true);
            player.getPacketDispatcher().sendUpdateItemContainer(barrows.getContainer());
            if (CombatUtilities.hasAnyBarrowsSet(player)) {
                player.getAchievementDiaries().update(MorytaniaDiary.LOOT_BARROWS_CHEST);
            }
            player.getNotificationSettings().increaseKill("barrows");
            player.getNotificationSettings().sendBossKillCountNotification("barrows");
            AdventCalendarManager.increaseChallengeProgress(player, 2022, 3, 1);
            player.getCombatAchievements().checkKcTask("barrows", 10, CAType.BARROWS_NOVICE);
            player.getCombatAchievements().checkKcTask("barrows", 25, CAType.BARROWS_CHAMPION);
            if (player.getCombatAchievements().hasCurrentTaskFlags(CAType.PRAY_FOR_SUCCESS, Barrows.CA_TASK_NO_DAMAGE)) {
                player.getCombatAchievements().complete(CAType.PRAY_FOR_SUCCESS);
            }
            if (player.getCombatAchievements().hasCurrentTaskFlags(CAType.CANT_TOUCH_ME, Barrows.CA_TASK_NO_MELEE_DAMAGE_DHAROCK, Barrows.CA_TASK_NO_MELEE_DAMAGE_GUTHAN,
                    Barrows.CA_TASK_NO_MELEE_DAMAGE_TORAG, Barrows.CA_TASK_NO_MELEE_DAMAGE_VERAC)) {
                player.getCombatAchievements().complete(CAType.CANT_TOUCH_ME);
            }
            if (player.getCombatAchievements().hasCurrentTaskFlags(CAType.FAITHLESS_CRYPT_RUN, Barrows.CA_TASK_FAITHLESS_RUN) && barrows.getSlainWights().size() == 6) {
                player.getCombatAchievements().complete(CAType.FAITHLESS_CRYPT_RUN);
            }
        }

        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getId(), getComponent("Examine"), 0, Container.getSize(ContainerType.BARROWS_CHEST), AccessMask.CLICK_OP10);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        if (player.inArea("Rise of the Six")) {
            ((RotsInstance) player.getArea()).addLoot();
        } else {
            final ArrayList<Item> equipmentPieces = new ArrayList<>();
            player.getBarrows().getContainer().getItems().int2ObjectEntrySet().fastForEach(loot -> {
                // check if loot is barrows piece or amulet of the damned
                if (BarrowsWight.ALL_WIGHT_EQUIPMENT.contains(loot.getValue()) || loot.getValue().getId() == 12851) {
                    equipmentPieces.add(loot.getValue());
                }
            });
            if (equipmentPieces.size() > 0) {
                final int chestCount = player.getNotificationSettings().getKillcount("barrows");
                final String icon = equipmentPieces.get(0).getId() + ".png"; // use the first piece as the adv log entry icon
                final ArrayList<String> equipmentPieceNames = new ArrayList<>(equipmentPieces.size());
                for (final Item piece : equipmentPieces) {
                    equipmentPieceNames.add(piece.getName()); // no stream, you're welcome Kris
                }
                final String joinedEquipmentLootString = String.join(", ", equipmentPieceNames);
                player.sendAdventurersEntry(icon, player.getName() + " opened Barrows chest " + chestCount + " and received: " + joinedEquipmentLootString, false);
            }
            player.getBarrows().addLoot();
        }
    }

    @Override
    protected void build() {
        bind("Examine", ((player, slotId, itemId, option) -> ItemUtil.sendItemExamine(player, itemId)));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.BARROWS_REWARDS;
    }

    @Override
    public boolean closeInCombat() {
        return false;
    }

}

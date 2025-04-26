package com.zenyte.game.content.minigame.warriorsguild.kegbalance;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.WarriorsGuildArea;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;

/**
 * @author Kris | 17. dets 2017 : 22:46.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class KegBalanceArea extends WarriorsGuildArea implements CycleProcessPlugin {
    private static final Animation PICKUP = new Animation(4180);
    private static final RenderAnimation RENDER_ANIM = new RenderAnimation(4179, RenderAnimation.STAND_TURN, 4178, RenderAnimation.ROTATE180, RenderAnimation.ROTATE90, RenderAnimation.ROTATE270, 4178);
    private static final ForceTalk FTALK = new ForceTalk("Ouch!");

    public static final boolean pickKeg(final Player player, final WorldObject object, final int kegCount) {
        if (player.getWeapon() != null || player.getShield() != null || player.getGloves() != null || kegCount == 0 && player.getHelmet() != null) {
            player.sendMessage("You must have both your hands as well as your head completely free to throw a shot.");
            return false;
        }
        player.lock();
        player.setAnimation(PICKUP);
        WorldTasksManager.schedule(() -> {
            if (!player.inArea(KegBalanceArea.class)) {
                return;
            }
            if (kegCount == 0) {
                player.getAppearance().setRenderAnimation(RENDER_ANIM);
            }
            player.unlock();
            player.getVarManager().sendBit(object.getDefinitions().getVarbit(), 1);
            player.getEquipment().set(EquipmentSlot.HELMET.getSlot(), new Item(8860 + kegCount));
            player.getEquipment().refresh(EquipmentSlot.HELMET.getSlot());
            player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        }, 2);
        return true;
    }

    public final void loseBalance(final Player player) {
        final int kegCount = player.getNumericTemporaryAttribute("WG Kegs Count").intValue();
        if (kegCount <= 0) {
            return;
        }
        final int ticks = player.getNumericTemporaryAttribute("WG Kegs Ticks").intValue();
        player.setGraphics(new Graphics(689 - kegCount));
        player.lock(2);
        player.applyHit(new Hit(Utils.random(2, 4), HitType.REGULAR));
        player.sendMessage("You lose balance and the kegs fall onto your head.");
        player.setForceTalk(FTALK);
        if (kegCount != 1) {
            player.getSkills().addXp(SkillConstants.STRENGTH, 10 * kegCount);
            final int tokens = player.getNumericAttribute("warriorsGuildTokens").intValue();
            player.getAttributes().put("warriorsGuildTokens", tokens + (10 * kegCount) + (ticks / 2));
        }
        player.getEquipment().set(EquipmentSlot.HELMET.getSlot(), null);
        player.getEquipment().refresh(EquipmentSlot.HELMET.getSlot());
        player.getAppearance().setRenderAnimation(player.getAppearance().generateRenderAnimation());
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        for (int i = 0; i < 6; i++) {
            player.getVarManager().sendBit(2252 + i, 0);
        }
        player.getTemporaryAttributes().remove("WG Kegs Count");
        player.getTemporaryAttributes().remove("WG Kegs Ticks");
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2861, 3544}, {2861, 3536}, {2865, 3536}, {2866, 3535}, {2867, 3535}, {2868, 3536}, {2871, 3536}, {2872, 3535}, {2873, 3535}, {2874, 3536}, {2877, 3536}, {2877, 3539}, {2878, 3540}, {2878, 3541}, {2877, 3542}, {2877, 3544}}, 1)};
    }

    @Override
    public String name() {
        return "Warriors' guild Keg Balance Room";
    }

    @Override
    public void leave(Player player, boolean logout) {
        super.leave(player, logout);
        loseBalance(player);
    }

    @Override
    public void process() {
        if (players.isEmpty()) {
            return;
        }
        for (final Player player : players) {
            final int kegs = player.getNumericTemporaryAttribute("WG Kegs Count").intValue();
            if (kegs > 0) {
                final int ticks = player.getNumericTemporaryAttribute("WG Kegs Ticks").intValue();
                player.getTemporaryAttributes().put("WG Kegs Ticks", ticks + 1);
                if (ticks % 10 == 0) {
                    final double energy = player.getVariables().getRunEnergy();
                    player.getVariables().setRunEnergy(energy < 9 ? 0 : (energy - 9));
                }
                if (player.isLocked()) {
                    continue;
                }
                if ((player.getVariables().getRunEnergy() * Utils.randomDouble()) <= Utils.random(15) || player.hasWalkSteps() && player.isRun() || player.getX() < 2861) {
                    loseBalance(player);
                }
            }
        }
    }
}

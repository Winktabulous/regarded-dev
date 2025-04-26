package com.zenyte.game.content.minigame.warriorsguild.catapultroom;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.model.ui.InterfaceHandler;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.WarriorsGuildArea;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

/**
 * @author Kris | 16. dets 2017 : 16:37.48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 * profile</a>}
 */
public final class CatapultRoom extends WarriorsGuildArea implements FullMovementPlugin, CycleProcessPlugin {
    public static final Item SHIELD = new Item(8856);
    public static final Location LOCATION = new Location(2842, 3545, 1);
    private static final Location PROJECTILE_LOCATION = new Location(2842, 3554, 1);
    private static final WorldObject CATAPULT = new WorldObject(23950, 10, 0, new Location(2840, 3552, 1));
    private static final Animation LAUNCH = new Animation(4157);
    private static final Animation[] FALL_ANIMS = new Animation[] {new Animation(4172), new Animation(4173), new Animation(4174), new Animation(4175)};
    private static final InterfacePosition[] tabs = new InterfacePosition[] {InterfacePosition.JOURNAL_TAB_HEADER, InterfacePosition.INVENTORY_TAB, InterfacePosition.PRAYER_TAB, InterfacePosition.SPELLBOOK_TAB, InterfacePosition.SETTINGS_TAB, InterfacePosition.EMOTE_TAB};
    private static final String[] PROJ_NAMES = new String[] {"spiky ball", "flung anvil", "slashing blades", "magic missile"};
    private static final Projectile[] PROJS = new Projectile[] {new Projectile(679, 60, 15, 0, 45, 40 + 60, 0, 5), new Projectile(680, 60, 15, 0, 45, 40 + 60, 0, 5), new Projectile(681, 60, 15, 0, 45, 40 + 60, 0, 5), new Projectile(682, 60, 15, 0, 45, 40 + 60, 0, 5)};
    private static final Animation[] DEFENSIVE_ANIMATIONS = new Animation[] {new Animation(4169), new Animation(4168), new Animation(4171), new Animation(4170)};

    @Override
    public void leave(final Player player, final boolean logout) {
        super.leave(player, logout);
        if (!player.getAppearance().getForcedAppearance().isEmpty()) {
            unequipShield(player);
        }
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2837, 3557}, {2837, 3542}, {2848, 3542}, {2848, 3557}}, 1)};
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        if (player.getLocation().getPositionHash() == CatapultRoom.LOCATION.getPositionHash()) {
            player.setFaceLocation(CatapultRoom.PROJECTILE_LOCATION);
            CatapultRoom.wieldShield(player);
        }
    }

    @Override
    public String name() {
        return "Warriors' guild Catapult Room";
    }

    /**
     * Wields the large defensive shield and sends the right tabs.
     *
     * @param player player wielding it.
     */
    public static final void wieldShield(final Player player) {
        findShield(player).ifPresent(slot -> {
            final Appearance appearance = player.getAppearance();
            appearance.forceAppearance(EquipmentSlot.WEAPON.getSlot(), -1);
            appearance.forceAppearance(EquipmentSlot.SHIELD.getSlot(), SHIELD.getId());
            appearance.setRenderAnimation(RenderAnimation.DEFAULT_RENDER);
        });
        for (final InterfacePosition tab : tabs) {
            player.getInterfaceHandler().closeInterface(tab);
        }
        player.getVarManager().sendBit(2247, 0);
        player.getInterfaceHandler().sendInterface(InterfacePosition.EQUIPMENT_TAB, 411);
        player.getInterfaceHandler().openGameTab(GameTab.EQUIPMENT_TAB);
    }

    private static final OptionalInt findShield(@NotNull final Player player) {
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item != null && item.getId() == SHIELD.getId()) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Unequips the large defensive shield when the player moves away from the
     * correct location.
     *
     * @param player the player equipping the shield.
     */
    private final void unequipShield(final Player player) {
        final Appearance appearance = player.getAppearance();
        appearance.clearForcedAppearance();
        appearance.resetRenderAnimation();
        final InterfaceHandler i = player.getInterfaceHandler();
        i.openJournal();
        i.sendInterface(InterfacePosition.INVENTORY_TAB, 149);
        i.sendInterface(InterfacePosition.EQUIPMENT_TAB, 387);
        i.sendInterface(InterfacePosition.PRAYER_TAB, 541);
        GameInterface.SPELLBOOK.open(player);
        GameInterface.SETTINGS.open(player);
        GameInterface.EMOTE_TAB.open(player);
        player.getInterfaceHandler().openGameTab(GameTab.INVENTORY_TAB);
    }

    private int ticks = 3;
    private int type;

    private final void checkStance(final Player player) {
        final int value = player.getNumericTemporaryAttribute("catapultStance").intValue();
        final int stance = value == 8 ? 3 : (value - 1);
        if (stance == type) {
            player.getSkills().addXp(SkillConstants.DEFENCE, 15);
            final int tokens = player.getNumericAttribute("warriorsGuildTokens").intValue();
            player.getAttributes().put("warriorsGuildTokens", tokens + 5);
            player.setAnimation(DEFENSIVE_ANIMATIONS[type]);
            player.sendMessage("You successfully defend against the " + PROJ_NAMES[type] + ".");
        } else {
            player.sendMessage("You fail defending against the " + PROJ_NAMES[type] + ".");
            player.setAnimation(FALL_ANIMS[type]);
            CombatUtilities.delayHit(null, 1, player, new Hit(player, Utils.random(1, 5), HitType.REGULAR));
        }
    }

    @Override
    public boolean processMovement(Player player, int x, int y) {
        if (player.getLocation().getPositionHash() == CatapultRoom.LOCATION.getPositionHash()) {
            if (!player.getAppearance().getForcedAppearance().isEmpty()) {
                unequipShield(player);
            }
        }
        return true;
    }

    private static final Location gamfredPosition = new Location(2841, 3541, 1);

    @Override
    public void process() {
        if (ticks == 3) {
            World.sendObjectAnimation(CATAPULT, LAUNCH);
        } else if (ticks == 0) {
            type = Utils.random(3);
            World.sendProjectile(PROJECTILE_LOCATION, LOCATION, PROJS[type]);
            ticks = 11;
        } else if (ticks == 7) {
            if (!players.isEmpty()) {
                for (Player p : players) {
                    if (p.getLocation().getPositionHash() == LOCATION.getPositionHash()) {
                        if (!p.getAppearance().getForcedAppearance().isEmpty()) checkStance(p);
                         else {
                            final int direction = Utils.random(2);
                            switch (direction) {
                            case 0: 
                                p.addWalkSteps(2841, 3545, 1, false);
                                break;
                            case 1: 
                                p.addWalkSteps(2842, 3546, 1, false);
                                break;
                            default: 
                                p.addWalkSteps(2843, 3545, 1, false);
                                break;
                            }
                            p.getDialogueManager().start(new Dialogue(p, 2459) {
                                @Override
                                public void buildDialogue() {
                                    World.findNPC(2459, gamfredPosition).ifPresent(npc -> npc.setFaceLocation(p.getLocation()));
                                    npc("Watch out! You'll need to equip the shield as soon as you're on the target " +
                                            "spot else you could get hit! Speak to me to get one, and make sure both " +
                                            "your hands are free to equip it.");
                                }
                            });
                        }
                    }
                }
            }
        }
        ticks--;
    }
}

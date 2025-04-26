package com.zenyte.game.content.minigame.warriorsguild.dummyroom;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.AttackStyleDefinition;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.WarriorsGuildArea;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import mgi.types.config.items.ItemDefinitions;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Kris | 16. dets 2017 : 4:20.17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DummyRoom extends WarriorsGuildArea implements CycleProcessPlugin {

    @Override
    public void process() {
        if (ticks == 2) {
            World.sendObjectAnimation(DUMMIES[index], FALL_ANIM);
            if (index == 3) {
                World.sendObjectAnimation(EXTRA_DUMMY, FALL_ANIM);
            }
        } else if (ticks == 1) {
            World.spawnObject(HOLES[index]);
            if (index == 3) {
                World.spawnObject(EXTRA_HOLE);
            }
        } else if (ticks == 0) {
            if (!taggedPlayers.isEmpty()) {
                taggedPlayers.clear();
            }
            index = Utils.random(6);
            ticks = Utils.random(4, 15);
            World.spawnObject(DUMMIES[index]);
            if (index == 3) {
                World.spawnObject(EXTRA_DUMMY);
            }
        }
        ticks--;
    }

    private static final WorldObject[] DUMMIES = new WorldObject[] { new WorldObject(23958, 10, 0, new Location(2856, 3554, 0)), new WorldObject(23959, 10, 0, new Location(2858, 3554, 0)), new WorldObject(23960, 10, 1, new Location(2860, 3553, 0)), new WorldObject(23961, 10, 1, new Location(2860, 3551, 0)), new WorldObject(23962, 10, 2, new Location(2859, 3549, 0)), new WorldObject(23963, 10, 2, new Location(2857, 3549, 0)), new WorldObject(23964, 10, 3, new Location(2855, 3550, 0)) };

    private static final WorldObject[] HOLES = new WorldObject[] { new WorldObject(23965, 10, 0, new Location(2856, 3554, 0)), new WorldObject(24297, 10, 0, new Location(2858, 3554, 0)), new WorldObject(24298, 10, 1, new Location(2860, 3553, 0)), new WorldObject(24299, 10, 1, new Location(2860, 3551, 0)), new WorldObject(24300, 10, 2, new Location(2859, 3549, 0)), new WorldObject(24301, 10, 2, new Location(2857, 3549, 0)), new WorldObject(24302, 10, 3, new Location(2855, 3550, 0)) };

    private static final WorldObject EXTRA_DUMMY = new WorldObject(23961, 10, 3, new Location(2855, 3552, 0));

    private static final WorldObject EXTRA_HOLE = new WorldObject(24299, 10, 3, new Location(2855, 3552, 0));

    private static final Animation FALL_ANIM = new Animation(4164);

    private static final Graphics DIZZY = new Graphics(80, 0, 100);

    private int index;

    private int ticks;

    private final List<Player> taggedPlayers = new CopyOnWriteArrayList<>();

    public void handleObject(final Player player, final WorldObject object) {
        if (object.getId() == ObjectId.INFORMATION_SCROLL_24908) {
            player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 412);
            return;
        }
        if (ticks <= 1) {
            return;
        }
        if (taggedPlayers.contains(player)) {
            player.sendMessage("You already hit a dummy this turn.");
            return;
        }
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot());
        final ItemDefinitions definitions = ItemDefinitions.get(weaponId);
        int varbit = 0;
        if (definitions != null) {
            if (definitions.getInterfaceVarbit() > 0 && definitions.getInterfaceVarbit() < 28) {
                varbit = definitions.getInterfaceVarbit();
            }
        }
        final int maxStyles = weaponId == -1 ? 3 : AttackStyleDefinition.values[varbit].getStyles().length;
        final int value = player.getVarManager().getValue(43);
        final int bit = player.getVarManager().getBitValue(357);
        final int varp = maxStyles == 2 && value == 3 ? 2 : value;
        final CombatStyle style = CombatStyle.VALUES[bit > 27 ? 0 : bit];
        if (style.getStyles().length == 0) {
            player.sendMessage("You can only use melee weapons in this minigame.");
            return;
        }
        taggedPlayers.add(player);
        final StyleDefinition[] styles = style.getStyles();
        final boolean valid = varp < styles.length && validStyle(styles[varp]);
        player.setAnimation(new Animation(player.getEquipment().getAttackAnimation(player.getCombatDefinitions().getStyle())));
        WorldTasksManager.schedule(() -> {
            if (!valid) {
                player.sendMessage("You whack the dummy with the wrong attack style.");
                player.setGraphics(DIZZY);
                player.stun(3);
                WorldTasksManager.schedule(() -> player.setGraphics(Graphics.RESET), 2);
            } else {
                player.getSkills().addXp(SkillConstants.ATTACK, 15);
                final int tokens = player.getNumericAttribute("warriorsGuildTokens").intValue();
                player.getAttributes().put("warriorsGuildTokens", tokens + 2);
                player.sendMessage("You whack the dummy successfully!");
            }
        });
    }

    private boolean validStyle(final StyleDefinition combatStyle) {
        final DummyStyle dummyStyle = DummyStyle.VALUES[index];
        for (final DummyStyle style : combatStyle.getStyles()) {
            if (style == dummyStyle) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2854, 3556 }, { 2854, 3548 }, { 2862, 3548 }, { 2862, 3556 } }, 0) };
    }

    @Override
    public String name() {
        return "Warriors' Guild Dummy Room";
    }
}

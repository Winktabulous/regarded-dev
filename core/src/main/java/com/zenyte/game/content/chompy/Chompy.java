package com.zenyte.game.content.chompy;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.chompy.plugins.BloatedToadNPC;
import com.zenyte.game.content.chompy.plugins.DeadChompy;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

public class Chompy extends NPC {
    public static final String KILL_ATTRIB = "chompy kills";
    public static final int CHOMPY_ID = 1475;
    private static final Animation spawningAnim = new Animation(6766);
    private static final Animation leavingAnim = new Animation(6767);
    private final String playerName;
    private int ticks = 200;
    private final BloatedToadNPC toad;

    public Chompy(String playerName, int id, Location tile, boolean spawned, final BloatedToadNPC toad) {
        super(id, tile, spawned);
        this.playerName = playerName;
        this.toad = toad;
    }

    @Override
    public NPC spawn() {
        setAnimation(spawningAnim);
        WorldTasksManager.schedule(() -> setForceTalk(new ForceTalk("Squawk!")));
        return super.spawn();
    }

    @Override
    public void processNPC() {
        if (--ticks <= 0) {
            finish();
            return;
        }
        if (ticks >= 198) {
            return;
        }
        if (!isUnderCombat() && !toad.isFinished()) {
            if (getFaceEntity() == -1) {
                setFaceEntity(toad);
            }
            calcFollow(toad.getLocation(), 1, true, false, false);
            if (getLocation().withinDistance(toad.getLocation(), 1)) {
                faceEntity(toad);
                toad.finish();
                setFaceEntity(null);
            }
            return;
        }
        super.processNPC();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            if (DiaryUtil.eligibleFor(DiaryReward.WESTERN_BANNER4, player)) {
                DeadChompy.roll(player, 499);
            }
            player.addAttribute(KILL_ATTRIB, player.getNumericAttribute(KILL_ATTRIB).intValue() + 1);
        }
    }

    @Override
    public void drop(final Location location) {
        final NPC deadChompy = new NPC(DeadChompy.DEAD_CHOMPY_ID, location, true);
        deadChompy.setRadius(0);
        deadChompy.spawn();
        deadChompy.getTemporaryAttributes().put("owner", playerName);
        WorldTasksManager.schedule(deadChompy::finish, 99);
    }

    @Override
    public boolean canAttack(final Player player) {
        if (!player.getName().equals(playerName)) {
            player.sendMessage("This is not your Chompy Bird to shoot.");
            return false;
        }
        return hasProperEquipment(player);
    }

    @Override
    public boolean canBeMulticannoned(final Player player) {
        return false;
    }

    private boolean hasProperEquipment(final Player player) {
        if (!hasBow(player) || !hasArrow(player)) {
            player.sendMessage("You need an ogre bow or composite ogre bow with ogre arrows to attack the chompy.");
            return false;
        }
        return true;
    }

    private boolean hasBow(final Player player) {
        final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON);
        if (weapon == null) {
            return false;
        }
        return weapon.getId() == ItemId.OGRE_BOW || weapon.getId() == ItemId.COMP_OGRE_BOW;
    }

    private boolean hasArrow(final Player player) {
        final Item ammunition = player.getEquipment().getItem(EquipmentSlot.AMMUNITION);
        if (ammunition == null) {
            return false;
        }
        return ammunition.getId() == ItemId.OGRE_ARROW || ammunition.getName().contains("brutal");
    }

    @Override
    public void autoRetaliate(final Entity source) {
    }

    @Override
    public void finish() {
        setAnimation(leavingAnim);
        setForceTalk(new ForceTalk("Screech!"));
        resetWalkSteps();
        randomWalkDelay = 5;
        World.getPlayer(playerName).ifPresent(p -> {
            final Object arrow = p.getTemporaryAttributes().get("last hint arrow");
            if (arrow instanceof HintArrow) {
                final HintArrow hint = (HintArrow) arrow;
                if (hint.getTarget() == Chompy.this) {
                    p.getPacketDispatcher().resetHintArrow();
                }
            }
        });
        WorldTasksManager.schedule(super::finish, 1);
    }
}

package com.zenyte.game.content.skills.magic.spells.arceuus;

import com.google.common.base.CaseFormat;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.FloorItemSpell;
import com.zenyte.game.content.skills.magic.spells.ItemSpell;
import com.zenyte.game.content.skills.magic.spells.lunar.SpellbookSwap;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.util.WorldUtil;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.utils.TimeUnit;
import mgi.types.config.npcs.NPCDefinitions;

import java.util.List;
import java.util.Optional;

/**
 * @author Kris | 16/06/2022
 */
public enum ReanimationType implements ItemSpell, FloorItemSpell {
    BASIC,
    ADEPT,
    EXPERT,
    MASTER;

    @Override
    public String getSpellName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, toString().toLowerCase()).replaceAll("_", " ") + " reanimation";
    }

    private static final RSPolygon DARK_ALTAR_PRESENCE = new RSPolygon(new int[][] {{1636, 3887}, {1638, 3870}, {1645, 3877}, {1655, 3877}, {1657, 3875}, {1657, 3871}, {1655, 3869}, {1653, 3869}, {1650, 3866}, {1650, 3860}, {1654, 3856}, {1667, 3856}, {1670, 3853}, {1678, 3853}, {1680, 3851}, {1681, 3851}, {1690, 3842}, {1698, 3842}, {1699, 3843}, {1700, 3843}, {1701, 3844}, {1703, 3844}, {1704, 3845}, {1709, 3845}, {1711, 3847}, {1743, 3848}, {1743, 3857}, {1747, 3867}, {1743, 3871}, {1741, 3871}, {1742, 3874}, {1728, 3904}, {1708, 3904}, {1702, 3898}, {1702, 3896}, {1696, 3896}, {1696, 3891}, {1694, 3893}, {1691, 3893}, {1689, 3891}, {1689, 3887}, {1688, 3887}, {1688, 3894}, {1682, 3894}, {1682, 3887}, {1681, 3887}, {1681, 3890}, {1679, 3892}, {1675, 3892}, {1675, 3897}, {1669, 3897}, {1669, 3890}, {1667, 3890}, {1657, 3900}, {1649, 3900}}, 0);
    private static final Graphics CAST_GFX = new Graphics(1288);
    private static final Graphics IMPACT_GFX = new Graphics(1290);
    private static final Animation ANIMATION = new Animation(7198);
    private static final Projectile PROJECTILE = new Projectile(1289, 30, 0, 50, 15, 30, 0, 5);

    @Override
    public boolean spellEffect(Player player, FloorItem item) {
        final int itemId = item.getId();
        final Reanimation reanimation = findReanimation(itemId);
        if (reanimation == null) {
            player.getDialogueManager().start(new ItemChat(player, new Item(itemId), "This spell cannot reanimate that item."));
            return false;
        }
        final int npcId = reanimation.getNpcId();
        final NPCDefinitions definitions = NPCDefinitions.get(npcId);
        if (definitions == null) {
            return false;
        }
        if (player.getTemporaryAttributes().containsKey("reanimation creature")) {
            player.sendMessage("You should finish off your last one first.");
            return false;
        }
        player.setRouteEvent(new TileEvent(player, new TileStrategy(item.getLocation(), 1), () -> {
            if (World.getFloorItem(item.getId(), item.getLocation(), player) == null) {
                player.sendMessage("Too late - It's gone!");
                return;
            }
            final int size = definitions.getSize();
            final Location tile = new Location(item.getLocation());
            final Location square = World.findEmptyNPCSquare(tile, size);
            final long receiveTime = item.getNumericAttribute("ensouled head drop time").longValue();
            if (receiveTime < (Utils.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))) {
                if (!DARK_ALTAR_PRESENCE.contains(player.getLocation())) {
                    player.getDialogueManager().start(new ItemChat(player, new Item(itemId), "That creature cannot be reanimated here. The power of the crystals by the Dark Altar will increase the potency of the spell."));
                    return;
                }
            }
            if (square == null) {
                player.sendMessage("The creature wouldn't have room to re-animate there.");
                return;
            }
            final SpellState state = new SpellState(player, getLevel(), getRunes());
            if (!state.check()) {
                return;
            }
            final double magicExperience = reanimation.getMagicExperience();
            player.setLunarDelay(getDelay());
            state.remove();
            SpellbookSwap.checkSpellbook(player);
            World.destroyFloorItem(item);
            World.spawnFloorItem(item, tile, player, 200, 100);
            player.getSkills().addXp(SkillConstants.MAGIC, magicExperience);
            player.lock();
            player.setAnimation(ANIMATION);
            player.setGraphics(CAST_GFX);
            player.setFaceLocation(tile);
            World.sendProjectile(player, tile, PROJECTILE);
            WorldTasksManager.schedule(() -> World.destroyFloorItem(World.getFloorItem(item.getId(), tile, player)), PROJECTILE.getTime(player, tile) + 1);
            World.scheduleProjectile(player, tile, PROJECTILE).schedule(() -> {
                World.sendGraphics(IMPACT_GFX, tile);
                WorldTasksManager.schedule(() -> {
                    player.unlock();
                    if (!World.isFloorFree(square, size)) {
                        return;
                    }
                    new ReanimatedNPC(reanimation, player, square).spawn();
                }, 5);
            });
        }));
        return false;
    }

    @Override
    public boolean spellEffect(Player player, Item item, int slot) {
        final int itemId = item.getId();
        final Reanimation reanimation = findReanimation(itemId);
        if (reanimation == null) {
            player.getDialogueManager().start(new ItemChat(player, new Item(itemId), "This spell cannot reanimate that item."));
            return false;
        }
        final int npcId = reanimation.getNpcId();
        final NPCDefinitions definitions = NPCDefinitions.get(npcId);
        if (definitions == null) {
            return false;
        }
        if (player.getTemporaryAttributes().containsKey("reanimation creature")) {
            player.sendMessage("You should finish off your last one first.");
            return false;
        }
        final int size = definitions.getSize();
        final Location pos = new Location(player.getLocation());
        if (!DARK_ALTAR_PRESENCE.contains(player.getLocation())) {
            player.getDialogueManager().start(new ItemChat(player, new Item(itemId), "That creature cannot be reanimated here. The power of the crystals by the Dark Altar will increase the potency of the spell."));
            return false;
        }
        final Location square = WorldUtil.findEmptySquare(pos, size + 6, size, Optional.empty()).orElse(null);
        if (square == null) {
            player.sendMessage("The creature wouldn't have room to re-animate there.");
            return false;
        }
        final double magicExperience = reanimation.getMagicExperience();
        player.setRouteEvent(new TileEvent(player, new TileStrategy(square, 1), () -> {
            final SpellState state = new SpellState(player, getLevel(), getRunes());
            if (!state.check()) {
                return;
            }
            player.getInterfaceHandler().closeInterfaces();
            player.setLunarDelay(getDelay());
            state.remove();
            SpellbookSwap.checkSpellbook(player);
            addXp(player, magicExperience);
            player.lock();
            player.setAnimation(ANIMATION);
            player.setGraphics(CAST_GFX);
            final Location tile = player.getLocation().transform(Direction.getMovementDirection(DirectionUtil.getMoveDirection(square.getX() - player.getX(), square.getY() - player.getY())), 1);
            player.setFaceLocation(tile);
            player.getInventory().set(slot, null);
            World.spawnFloorItem(item, player, tile);
            World.sendProjectile(player, tile, PROJECTILE);
            WorldTasksManager.schedule(() -> World.destroyFloorItem(World.getFloorItem(item.getId(), tile, player)), PROJECTILE.getTime(player, tile) + 1);
            World.scheduleProjectile(player, tile, PROJECTILE).schedule(() -> {
                World.sendGraphics(IMPACT_GFX, tile);
                WorldTasksManager.schedule(() -> {
                    player.unlock();
                    final NPC npc = new ReanimatedNPC(reanimation, player, square).spawn();
                    npc.freeze(1);
                    npc.resetWalkSteps();
                }, 5);
            });
        }));
        return false;
    }

    @Override
    public int getDelay() {
        return 1000;
    }

    @Override
    public Spellbook getSpellbook() {
        return Spellbook.ARCEUUS;
    }

    private Reanimation findReanimation(final int itemId) {
        for (Reanimation reanimation : Reanimation.values()) {
            if (reanimation.getType() != this) continue;
            for (int i : reanimation.getItemId()) {
                if (i == itemId) {
                    return reanimation;
                }
            }
        }
        return null;
    }

    private static final class ReanimatedNPC extends NPC {
        ReanimatedNPC(final Reanimation reanimation, final Player owner, final Location tile) {
            super(reanimation.getNpcId(), tile, true);
            this.reanimation = reanimation;
            this.owner = owner;
            owner.getTemporaryAttributes().put("reanimation creature", true);
            combat.forceTarget(owner);
        }

        private final Reanimation reanimation;
        private final Player owner;
        private int ticks = 50;

        @Override
        public void processNPC() {
            super.processNPC();
            if (owner.isFinished() || !owner.getLocation().withinDistance(getLocation(), 25)) {
                finish();
                return;
            }
            if (!isUnderCombat()) {
                if (--ticks <= 0) {
                    finish();
                }
                return;
            }
            ticks = 50;
        }

        @Override
        public void sendDeath() {
            super.sendDeath();
            owner.getSkills().addXp(SkillConstants.PRAYER, reanimation.getPrayerExperience());
            if (reanimation == Reanimation.REANIMATE_ABYSSAL_CREATURE) {
                SherlockTask.KILL_REANIMATED_ABYSSAL.progress(owner);
            }
        }

        @Override
        public void finish() {
            super.finish();
            owner.getTemporaryAttributes().remove("reanimation creature");
        }

        @Override
        public boolean canAttack(final Player source) {
            if (source != owner) {
                source.sendMessage("You cannot attack that creature.");
                return false;
            }
            return super.canAttack(source);
        }

        @Override
        public List<Entity> getPossibleTargets(final EntityType type) {
            if (!possibleTargets.isEmpty()) {
                possibleTargets.clear();
            }
            possibleTargets.add(owner);
            return possibleTargets;
        }
    }
}

package com.zenyte.game.world.entity.npc.impl.wilderness;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * @author Kris | 21/06/2019 00:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BattleMageNPC extends NPC implements Spawnable, CombatScript {
    private final BattleMage def;

    public BattleMageNPC(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        this.def = BattleMage.get(id);
    }

    /**
     * Applies the hits pending for the NPC - overridden to allow the immunity effect on Hespori whilst flower buds
     * are alive.
     *
     * @param hit the hit being processed.
     */
    @Override
    public void applyHit(final Hit hit) {
        super.applyHit(hit);
        final Object weapon = hit.getWeapon();
        if (!ArrayUtils.contains(BattleMage.getSpells(), weapon)) {
            hit.setDamage(0);
        }
    }

    /**
     * Gets the xp modifier that is used to multiply the combat xp when attacking the monster.
     *
     * @return the xp modifier, 0-1.
     * @param hit the hit dealt.
     */
    @Override
    public float getXpModifier(final Hit hit) {
        final Object weapon = hit.getWeapon();
        if (!ArrayUtils.contains(BattleMage.getSpells(), weapon)) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return ArrayUtils.contains(BattleMage.getMageIds(), id);
    }

    @Override
    public boolean isAcceptableTarget(final Entity target) {
        if (target instanceof Player) {
            final Item cape = ((Player) target).getCape();
            return cape == null || def.getCapeId() != cape.getId();
        }
        return false;
    }

    @Override
    public int attack(final Entity target) {
        final CombatSpell spell = def.getSpell();
        final Projectile projectile = spell.getProjectile();
        int delay = 0;
        int clientDelay = 0;
        if (projectile != null) {
            clientDelay = projectile.getProjectileDuration(getLocation(), target.getLocation());
            if (projectile.getGraphicsId() != -1) {
                delay = World.sendProjectile(this, target, projectile);
            } else {
                delay = projectile.getTime(this, target);
            }
            final SoundEffect sound = spell.getHitSound();
            if (sound != null) {
                World.sendSoundEffect(target.getLocation(), new SoundEffect(sound.getId(), sound.getRadius(), clientDelay));
            }
        }
        final Graphics gfx = spell.getHitGfx();
        if (gfx != null) {
            target.setGraphics(new Graphics(gfx.getId(), clientDelay, gfx.getHeight()));
        }
        if (def == BattleMage.GUTHIX) {
            setAnimation(getCombatDefinitions().getAttackAnim());
        } else {
            setAnimation(spell.getAnimation());
        }
        delayHit(delay, target, magic(target, 20));
        return 5;
    }


    private enum BattleMage {
        SARADOMIN(NpcId.BATTLE_MAGE_1611, ItemId.SARADOMIN_CAPE, CombatSpell.SARADOMIN_STRIKE), GUTHIX(NpcId.BATTLE_MAGE_1612, ItemId.GUTHIX_CAPE, CombatSpell.CLAWS_OF_GUTHIX), ZAMORAK(NpcId.BATTLE_MAGE, ItemId.ZAMORAK_CAPE, CombatSpell.FLAMES_OF_ZAMORAK);
        private static final BattleMage[] mages = values();
        private static final int[] mageIds = Arrays.stream(mages).mapToInt(cape -> cape.getMageId()).toArray();
        private static final CombatSpell[] spells = Arrays.stream(mages).map(m -> m.getSpell()).toArray(CombatSpell[]::new);
        private final int mageId;
        private final int capeId;
        private final CombatSpell spell;

        public static BattleMage get(final int mageId) {
            for (BattleMage mage : mages) {
                if (mage.getMageId() == mageId) {
                    return mage;
                }
            }
            return null;
        }

        BattleMage(int mageId, int capeId, CombatSpell spell) {
            this.mageId = mageId;
            this.capeId = capeId;
            this.spell = spell;
        }

        public static int[] getMageIds() {
            return mageIds;
        }

        public static CombatSpell[] getSpells() {
            return spells;
        }

        public int getMageId() {
            return mageId;
        }

        public int getCapeId() {
            return capeId;
        }

        public CombatSpell getSpell() {
            return spell;
        }
    }


    public static final class BattleMageProcessor extends DropProcessor {
        @Override
        public void attach() {
            this.appendDrop(new DisplayedDrop(ItemId.INFINITY_HAT, 1, 1, 50));
            this.appendDrop(new DisplayedDrop(ItemId.INFINITY_TOP, 1, 1, 50));
            this.appendDrop(new DisplayedDrop(ItemId.INFINITY_BOTTOMS, 1, 1, 50));
            this.appendDrop(new DisplayedDrop(ItemId.INFINITY_GLOVES, 1, 1, 50));
            this.appendDrop(new DisplayedDrop(ItemId.INFINITY_BOOTS, 1, 1, 50));
            this.appendDrop(new DisplayedDrop(ItemId.BEGINNER_WAND, 1, 1, 15));
            this.appendDrop(new DisplayedDrop(ItemId.APPRENTICE_WAND, 1, 1, 20));
            this.appendDrop(new DisplayedDrop(ItemId.TEACHER_WAND, 1, 1, 30));
            this.appendDrop(new DisplayedDrop(ItemId.MASTER_WAND, 1, 1, 75));
            this.appendDrop(new DisplayedDrop(ItemId.MAGES_BOOK, 1, 1, 50));
        }

        @Override
        public void onDeath(final NPC npc, final Player killer) {
            final int random = random(50);
            if (random < 6) {
                switch (random) {
                case 0: 
                    npc.dropItem(killer, new Item(ItemId.INFINITY_HAT));
                    break;
                case 1: 
                    npc.dropItem(killer, new Item(ItemId.INFINITY_TOP));
                    break;
                case 2: 
                    npc.dropItem(killer, new Item(ItemId.INFINITY_BOTTOMS));
                    break;
                case 3: 
                    npc.dropItem(killer, new Item(ItemId.INFINITY_GLOVES));
                    break;
                case 4: 
                    npc.dropItem(killer, new Item(ItemId.INFINITY_BOOTS));
                    break;
                case 5: 
                    npc.dropItem(killer, new Item(ItemId.MAGES_BOOK));
                    break;
                }
            } else {
                final boolean master = random(75) == 0;
                final boolean teacher = random(30) == 0;
                final boolean apprentice = random(20) == 0;
                final boolean beginner = random(15) == 0;
                if (master) {
                    npc.dropItem(killer, new Item(ItemId.MASTER_WAND));
                } else if (teacher) {
                    npc.dropItem(killer, new Item(ItemId.TEACHER_WAND));
                } else if (apprentice) {
                    npc.dropItem(killer, new Item(ItemId.APPRENTICE_WAND));
                } else if (beginner) {
                    npc.dropItem(killer, new Item(ItemId.BEGINNER_WAND));
                }
            }
        }

        @Override
        public int[] ids() {
            return BattleMage.getMageIds();
        }
    }
}

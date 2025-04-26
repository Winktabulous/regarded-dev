package com.zenyte.game.world.entity.npc.impl.slayer.dragons;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;

/**
 * @author Kris | 20/08/2019 23:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BrutalDragon extends NPC implements Spawnable, CombatScript {
    public BrutalDragon(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("brutal green dragon") || name.equalsIgnoreCase("brutal blue dragon") || name.equalsIgnoreCase("brutal red dragon") || name.equalsIgnoreCase("brutal black dragon");
    }

    private static final Projectile blackDragonProjectile = new Projectile(88, 27, 30, 40, 5, 18, 0, 5);
    private static final Projectile greenDragonProjectile = new Projectile(CombatSpell.WATER_BLAST.getProjectile().getGraphicsId(), 27, 30, 40, 5, 18, 0, 5);
    private static final Projectile blueDragonProjectile = new Projectile(CombatSpell.WIND_BLAST.getProjectile().getGraphicsId(), 27, 30, 40, 5, 18, 0, 5);
    private static final Projectile redDragonProjectile = new Projectile(CombatSpell.FIRE_BLAST.getProjectile().getGraphicsId(), 27, 30, 40, 5, 18, 0, 5);
    private static final Graphics dragonfireGraphics = new Graphics(1, 0, 90);
    private static final Animation attackAnimation = new Animation(80);
    private static final Animation secondaryAttackAnimation = new Animation(91);
    private static final Animation dragonfireAnimation = new Animation(81);
    private static final Animation magicalAnimation = new Animation(6722);

    @Override
    public int attack(final Entity target) {
        final BrutalDragon npc = this;
        final int style = Utils.random(!isWithinMeleeDistance(npc, target) ? 1 : 2);
        if (style == 0) {
            if (!(target instanceof Player)) {
                return 0;
            }
            npc.setAnimation(dragonfireAnimation);
            npc.setGraphics(dragonfireGraphics);
            final Player player = (Player) target;
            final boolean perk = player.getPerkManager().isValid(PerkWrapper.BACKFIRE);
            final double modifier = !perk ? 1 : Math.max(0, Utils.randomDouble() - 0.25F);
            final Dragonfire dragonfire = new Dragonfire(DragonfireType.CHROMATIC_DRAGONFIRE, 50, DragonfireProtection.getProtection(this, player, true));
            final int deflected = !perk ? 0 : ((int) Math.floor(dragonfire.getMaximumDamage() * modifier));
            PlayerCombat.appendDragonfireShieldCharges(player);
            player.sendFilteredMessage(String.format(dragonfire.getMessage(), "dragon's fiery breath"));
            delayHit(npc, 0, target, new Hit(npc, Utils.random(Math.max(0, dragonfire.getDamage() - deflected)), HitType.REGULAR));
            if (perk) {
                dragonfire.backfire(npc, player, 1, deflected);
            }
        } else if (style == 1) {
            npc.setAnimation(magicalAnimation);
            final String name = npc.getDefinitions().getName().toLowerCase();
            final Projectile projectile = name.contains("green") ? greenDragonProjectile : name.contains("blue") ? blueDragonProjectile : name.contains("red") ? redDragonProjectile : blackDragonProjectile;
            delayHit(npc, World.sendProjectile(npc, target, projectile), target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC));
        } else {
            npc.setAnimation(Utils.random(1) == 0 ? attackAnimation : secondaryAttackAnimation);
            delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return 4;
    }
}

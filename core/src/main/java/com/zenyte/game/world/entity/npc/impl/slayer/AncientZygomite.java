package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.enums.FungicideSpray;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawn;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCTileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19/06/2019 11:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AncientZygomite extends NPC implements Spawnable, CombatScript {

    private boolean messageSent;

    private final int original;

    private boolean dying;

    public AncientZygomite(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
        original = id;
        if (id == 7797) {
            lock();
        }
    }

    @Override
    public NPC spawn() {
        dying = false;
        return super.spawn();
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (getId() == NpcId.ANCIENT_ZYGOMITE) {
            if ((Utils.currentTimeMillis() - getAttackingDelay()) >= 5000) {
                final NPCSpawn spawn = getNpcSpawn();
                final Location location = spawn == null ? getLocation() : new Location(spawn.getX(), spawn.getY(), spawn.getZ());
                cancelCombat();
                heal(getMaxHitpoints());
                setAttackingDelay(Utils.currentTimeMillis());
                setRouteEvent(new NPCTileEvent(this, new TileStrategy(location), () -> {
                    setTransformation(original);
                    lock();
                }).setOnFailure(() -> {
                    setTransformation(original);
                    lock();
                }));
            }
        }
    }

    @Override
    public void sendDeath() {
        if (dying) {
            return;
        }
        final Player source = getMostDamagePlayerCheckIronman();
        if (source == null) {
            super.sendDeath();
            return;
        }
        final boolean isUnlocked = source.getSlayer().isUnlocked("'Shroom sprayer");
        final Object usedOn = getTemporaryAttributes().remove("used_fungicide_spray");
        final Object obj = isUnlocked && usedOn == null ? FungicideSpray.get(source) : usedOn;
        if (getHitpoints() == 0 && obj == null) {
            if (!messageSent) {
                source.sendMessage("The Zygomite is on its last legs! Finish it quickly!");
                messageSent = true;
            }
            heal(1);
        } else {
            final Object[] info = (Object[]) obj;
            assert info != null && info[0] instanceof FungicideSpray;
            final FungicideSpray spray = (FungicideSpray) info[0];
            final int slot = (int) info[1];
            final int nextCharge = spray.getNextCharge().getId();
            source.getInventory().set(slot, new Item(nextCharge));
            source.sendMessage("The Zygomite is covered in fungicide. It bubbles away to nothing!");
            dying = true;
            super.sendDeath();
        }
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        setId(original);
        messageSent = false;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 471 || id == NpcId.ANCIENT_ZYGOMITE;
    }

    private static final Projectile projectile = new Projectile(681, 20, 34, 0, 10, 25, 0, 5);

    @Override
    public int attack(final Entity target) {
        animate();
        attackSound();
        final int style = Utils.random(isWithinMeleeDistance(this, target) ? 1 : 0);
        if (style == 0) {
            delayHit(World.sendProjectile(new Location(getLocation()), target, projectile), target, new Hit(this, getRandomMaxHit(this, 10, AttackType.MAGIC, AttackType.RANGED, target), HitType.RANGED));
        } else {
            delayHit(0, target, new Hit(this, getRandomMaxHit(this, 10, AttackType.MAGIC, AttackType.MELEE, target), HitType.MELEE));
        }
        return 4;
    }
}

package com.zenyte.game.content.boss.kraken;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
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

/**
 * @author Tommeh | 21 mei 2018 | 20:02:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class EnormousTentacle extends NPC implements CombatScript, Spawnable {

	private static final Animation AWAKE_ANIM = new Animation(3860);
	private static final Projectile PROJECTILE = new Projectile(162, 80, 28, 35, 20, 18, 64, 5);
	private static final Graphics SPLASH_GRAPHICS = new Graphics(85, 0, 124);

	private int ticks;

	public EnormousTentacle(final int id, final Location tile, final Direction direction, final int radius) {
		super(id, tile, direction, radius);
		setForceMultiArea(true);
        setAggressionDistance(20);
        this.attackDistance = 50;
		supplyCache = false;
		spawned = true;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (id == 5535) {
			if (getCombat().underCombat()) {
				ticks = 0;
			}
			if (++ticks >= 50) {
				setTransformation(5534);
				cancelCombat();
				heal(getMaxHitpoints());
			}
		}
	}


	@Override
    public void dropItem(final Player killer, final Item item, final Location tile, boolean guaranteedDrop) {
        //Tentacles always drops loot underneath the player.
        tile.setLocation(killer.getLocation());
        super.dropItem(killer, item, tile, guaranteedDrop);
    }

	@Override
	public void setRespawnTask() {
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (!(hit.getSource() instanceof Player)) {
			return;
		}

		super.handleIngoingHit(hit);
		disturb((Player) hit.getSource());
	}

	public void disturb(Player target) {
		if (id == 5534) {
			WorldTasksManager.schedule(new WorldTask() {
				int ticks;

				@Override
				public void run() {
					switch (ticks++) {
						case 0:
							setTransformation(5535);
							setAnimation(AWAKE_ANIM);
							faceEntity(target);
							break;
						case 3:
							getCombat().setTarget(target);
							stop();
							break;
					}
				}
			}, 0, 1);
		}
	}

	@Override
	public boolean canAttack(final Player source) {
		return true;
	}
	
	@Override
	public boolean isTolerable() {
		return false;
	}

	@Override
	public boolean isFreezeImmune() {
		return true;
	}

	@Override
	public int attack(Entity target) {
		setAnimation(getCombatDefinitions().getAttackDefinitions().getAnimation());
        delayHit(this, World.sendProjectile(this, target, PROJECTILE), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, MAGIC, target),
                HitType.REGULAR).onLand(hit -> {
            if (hit.getDamage() <= 0) {
                target.setGraphics(SPLASH_GRAPHICS);
            }
        }));
		return getCombatDefinitions().getAttackSpeed();
	}
	
	@Override
	public boolean validate(final int id, final String name) {
		return id == 5534 || id == 5535;
	}

	@Override
	public float getXpModifier(final Hit hit) {
		return id == 5534 ? 0 : 1;
	}

}

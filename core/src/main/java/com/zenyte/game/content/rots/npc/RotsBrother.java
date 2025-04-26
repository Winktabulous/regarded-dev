package com.zenyte.game.content.rots.npc;

import com.zenyte.game.content.rots.RotsInstance;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.HitBar;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

public abstract class RotsBrother extends NPC implements CombatScript {

	private int restoreTick;
	private final RotsInstance instance;
	private final int restoredMaxTicks = 50;

	public RotsBrother(int id, Location tile, RotsInstance instance) {
		super(id, tile, Direction.SOUTH, 64);
		this.instance = instance;
		spawned = true;
		supplyCache = false;
		setIntelligent(true);
		maxDistance = 64;
		forceCheckAggression = true;
	}

	@Override
	public void processNPC() {
		super.processNPC();

		if (!isDead()) {
			return;
		}

		if (restoreTick >= restoredMaxTicks) {
			restoreTick = 0;
			setHitpoints(getMaxHitpoints());
			receivedHits.add(new Hit(this, getMaxHitpoints(), HitType.HEALED));
			setAnimation(Animation.STOP);
			combat.removeTarget();
			unlock();
		} else {
			restoreTick++;
		}
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	public void restartDeathTick() {
		restoreTick = 0;
		getHitBars().clear();
		getHitBars().add(fillBar);
		getUpdateFlags().flag(UpdateFlag.HIT);
	}

	@Override
	public void sendDeath() {
		boolean allDead = true;
		for (RotsBrother brother : instance.getBrothers()) {
			if (brother != this) {
				if (!brother.isDead()) {
					brother.applyHit(new Hit(this, (int) (getMaxHitpoints() * 0.05), HitType.HEALED));
					allDead = false;
				} else {
					brother.restartDeathTick();
				}
			}
		}

		if (allDead) {
			for (RotsBrother brother : instance.getBrothers()) {
				brother.sendDeath_();
			}

			instance.setCompleted();
		} else {
			restoreTick = 0;
			lock();
			getHitBars().clear();
			getHitBars().add(removeRegularHpBar);
			getHitBars().add(fillBar);
			setAnimation(new Animation(12003));

			final Player source = getMostDamagePlayerCheckIronman();
			if (source != null) {
				source.sendMessage("As you defeat " + getName() + ", the shadow engulfs the remaining wights!");
			}
		}
	}

	public void sendDeath_() {
		super.sendDeath();
	}

	@Override
	public void addHitbar() {
		super.addHitbar();

		getHitBars().add(removeFillBar);
	}

	@Override
	public boolean isFreezeImmune() {
		return true;
	}

	private final HitBar removeRegularHpBar = new HitBar() {
		@Override
		public int getType() {
			return hitBar.getType();
		}

		@Override
		public int interpolateTime() {
			return 32767;
		}

		@Override
		public int getPercentage() {
			return 0;
		}
	};

	private final HitBar removeFillBar = new HitBar() {
		@Override
		public int getType() {
			return 31;
		}

		@Override
		public int getPercentage() {
			return 0;
		}

		@Override
		public int interpolateTime() {
			return 32767;
		}

	};

	private final HitBar fillBar = new HitBar() {
		@Override
		public int getType() {
			return 31;
		}

		@Override
		public int getPercentage() {
			return 0;
		}

		@Override
		public int interpolatePercentage() {
			return 30;
		}

		@Override
		public int interpolateTime() {
			return restoredMaxTicks * 30;
		}

	};

}

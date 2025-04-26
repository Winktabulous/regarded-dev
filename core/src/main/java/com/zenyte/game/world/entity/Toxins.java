package com.zenyte.game.world.entity;

import com.google.gson.annotations.Expose;
import com.near_reality.game.content.custom.SlayerHelmetEffects;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity.EntityType;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.ImmunityType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.variables.TickVariable;

import java.util.EnumSet;

/**
 * @author Kris | 5. apr 2018 : 17:07.06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Toxins {
	private final transient Entity entity;
	@Expose
	private ToxinType type;
	@Expose
	private int damage;
	@Expose
	private int cycle;
	@Expose
	private int interval;
	@Expose
	private int diseaseCycle;
	@Expose
	private int diseaseInterval;
	private transient Entity source;

	public Toxins(final Entity entity) {
		this.entity = entity;
	}

	public void initialize(final Toxins toxins) {
		type = toxins.type;
		damage = toxins.damage;
		cycle = toxins.cycle;
		interval = toxins.interval;
		diseaseCycle = toxins.diseaseCycle;
		diseaseInterval = toxins.diseaseInterval;
	}

	public void applyToxin(final ToxinType type, final int damage, final Entity source) {
		applyToxin(type, damage, false, source);
	}

	public void applyToxin(final ToxinType type, final int damage) {
		applyToxin(type, damage, false, null);
	}

	public void applyToxin(final ToxinType requestedType, final int damage, final boolean applyDamage, final Entity source) {
		if (entity.isDead() || entity.isFinished()) {
			return;
		}
		ToxinType type = requestedType;
		if (entity.getEntityType() == EntityType.PLAYER) {
			final Player player = (Player) entity;
			if ((type == ToxinType.VENOM && player.getVariables().getTime(TickVariable.VENOM_IMMUNITY) > 0) || (type == ToxinType.POISON && player.getVariables().getTime(TickVariable.POISON_IMMUNITY) > 0)) {
				return;
			}
			if (CombatUtilities.isWearingSerpentineHelmet(player))
				return;
			if (SlayerHelmetEffects.INSTANCE.immuneToPoison(player, source))
				return;
		} else if (entity.getEntityType() == EntityType.NPC) {
			final NPC npc = (NPC) entity;
			if (type == ToxinType.VENOM && npc.getCombatDefinitions().isVenomImmune() || type == ToxinType.POISON && npc.getCombatDefinitions().isPoisonImmune()) {
				return;
			}
			final EnumSet<ImmunityType> immunityTypes = npc.getCombatDefinitions().getImmunityTypes();
			if (type == ToxinType.VENOM && immunityTypes != null && immunityTypes.contains(ImmunityType.VENOM_TURNS_POISON)) {
				type = ToxinType.POISON;
			}
		}
		if (type == ToxinType.VENOM) {
			if (isVenomed() && damage <= this.damage) {
				return;
			}
			this.source = source;
			this.damage = damage;
			if (!isVenomed()) {
				cycle = 30;
				interval = 2;
				this.type = type;
				if (applyDamage) {
					entity.applyHit(new Hit(source, damage, HitType.VENOM));
				}
				refresh();
			}
		} else if (type == ToxinType.POISON) {
			if (isVenomed() || isPoisoned() && damage <= this.damage) {
				return;
			}
			this.source = source;
			this.damage = damage;
			if (!isPoisoned()) {
				cycle = 30;
				interval = 4;
				this.type = type;
				if (applyDamage) {
					entity.applyHit(new Hit(source, damage, HitType.POISON));
				}
				refresh();
			}
		} else if (type == ToxinType.DISEASE) {
			final boolean isDiseased = isDiseased();
			diseaseInterval += 3;
			if (diseaseInterval > 10) {
				diseaseInterval = 10;
			}
			if (!isDiseased) {
				diseaseCycle = 30;
				refresh();
			}
		}
	}

	public void process() {
		if (diseaseInterval > 0) {
			if (!(entity instanceof Player && ((Player) entity).getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL))) {
				if (--diseaseCycle <= 0) {
					if (diseaseInterval > 0) {
						diseaseInterval--;
					}
					if (diseaseInterval == 0) {
						diseaseCycle = 0;
						diseaseInterval = 0;
						refresh();
					} else {
						applyDisease();
					}
				}
			}
		}
		if (type == null) {
			return;
		}
		if (--cycle > 0) {
			return;
		}
		if (entity instanceof Player && ((Player) entity).getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
			return;
		}
		if (interval > 0) {
			interval--;
		}
		if (type == ToxinType.VENOM) {
			entity.applyHit(new Hit(source, damage, HitType.VENOM));
			cycle = 30;
			if (damage < 20) {
				damage += 2;
			}
		} else if (type == ToxinType.POISON) {
			if (interval == 0) {
				interval = 4;
				if (--damage == 0) {
					cureToxin(ToxinType.POISON);
					return;
				}
			}
			cycle = 30;
			entity.applyHit(new Hit(source, damage, HitType.POISON));
		}
	}

	public void cureToxin(final ToxinType type) {
		//Disease is cured through other means.
		if (type == ToxinType.DISEASE) return;
		if (type == ToxinType.VENOM || !this.isVenomed()) {
			reset();
		} else {
			this.type = ToxinType.POISON;
			damage = 6;
			interval = 4;
			cycle = 30;
			refresh();
		}
	}

	private void applyDisease() {
		diseaseCycle = 30;
		final int hit = Utils.random(10);
		entity.applyHit(new Hit(hit, HitType.DISEASED));
		if (entity.getEntityType() == EntityType.PLAYER) {
			final Player player = (Player) entity;
			final int randomSkill = Utils.random(22);
			if (randomSkill == SkillConstants.PRAYER) {
				player.getPrayerManager().drainPrayerPoints(hit);
			} else if (randomSkill == SkillConstants.HITPOINTS) {
				final int hitpoints = player.getHitpoints() - hit;
				player.setHitpoints(Math.max(hitpoints, 0));
			} else {
				final int level = player.getSkills().getLevel(randomSkill) - hit;
				player.getSkills().setLevel(randomSkill, Math.max(level, 0));
			}
		}
	}

	public void weakenDisease() {
		if (diseaseInterval == 0) {
			return;
		}
		diseaseInterval -= 2;
		if (diseaseInterval <= 0) {
			diseaseInterval = 0;
			diseaseCycle = 0;
			refresh();
		}
	}

	public void refresh() {
		if (entity.getEntityType() == EntityType.PLAYER) {
			final Player player = (Player) entity;
			player.getVarManager().sendVar(102, type == ToxinType.POISON ? 1 : type == ToxinType.VENOM ? 1000000 : 0);
			player.getVarManager().sendVar(456, diseaseInterval > 0 ? 1 : 0);
		}
	}

	public void resetVenom() {
		type = null;
		damage = 0;
		interval = 0;
		cycle = 0;
		refresh();
	}

	public void reset() {
		type = null;
		damage = 0;
		interval = 0;
		cycle = 0;
		diseaseCycle = 0;
		diseaseInterval = 0;
		refresh();
	}

	public boolean isVenomed() {
		return type == ToxinType.VENOM && damage > 0;
	}

	public boolean isPoisoned() {
		return type == ToxinType.POISON && damage > 0;
	}

	public boolean isDiseased() {
		return diseaseInterval > 0;
	}

	public boolean isIll() {
		return isVenomed() || isPoisoned() || isDiseased();
	}

	public void setType(ToxinType type) {
		this.type = type;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setDiseaseCycle(int diseaseCycle) {
		this.diseaseCycle = diseaseCycle;
	}

	public void setDiseaseInterval(int diseaseInterval) {
		this.diseaseInterval = diseaseInterval;
	}


	public enum ToxinType {
		POISON, VENOM, DISEASE, PARASITE
    }
}

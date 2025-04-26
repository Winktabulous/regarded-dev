package com.zenyte.game.world.entity.masks;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Kris | 6. nov 2017 : 14:30.53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public class Hit {

	private Entity source;
	private HitType hitType;
	private int damage;
	private int delay;
	private long scheduleTime;
	private boolean forcedHitsplat;
	private final boolean accurate;
	private Map<String, Object> attributes;


	public Hit(final int damage, final HitType look) {
		this(null, damage, look, 0);
	}

	public Hit(final Entity source, final int damage, final HitType look) {
		this(source, damage, look, 0);
	}

	public Hit(final Entity source, final int damage, final HitType hitType, final int delay) {
		this(source, damage, damage > 0, hitType, delay);
	}

	public Hit(final Entity source, final int damage, final boolean accurate, final HitType hitType, final int delay) {
		this.source = source;
		this.damage = damage;
		this.accurate = accurate;
		this.hitType = hitType;
		this.delay = delay;
		scheduleTime = WorldThread.getCurrentCycle();
	}

	public boolean containsAttribute(final String key) {
		if (attributes == null) {
			return false;
		}
		return attributes.containsKey(key);
	}

	public Object getAttribute(final String key) {
		if (attributes == null) {
			return null;
		}
		return attributes.get(key);
	}

	public HitType getAppliedSplat() {
		return damage == 0 && !forcedHitsplat ? HitType.MISSED : hitType;
	}

	public Hit putAttribute(final String key, final Object value) {
		if (attributes == null) {
			attributes = new Object2ObjectOpenHashMap<>();
		}
		attributes.put(key, value);
		return this;
	}

	public void setWeapon(final Object weapon) {
		putAttribute("weapon", weapon);
	}
	public void setSpell(final Object spell) {
		putAttribute("spell", spell);
	}

	public Hit onLand(final Consumer<Hit> consumer) {
		putAttribute("on_hit_land", consumer);
		return this;
	}

	@SuppressWarnings("all")
	public Consumer<Hit> getOnLandConsumer() {
		if (attributes == null) {
			return null;
		}
		final Object attachment = attributes.get("on_hit_land");
		if (attachment instanceof Consumer) {
			return (Consumer<Hit>) attachment;
		}
		return null;
	}

	public Hit onPreApply(final Consumer<Hit> consumer) {
		putAttribute("on_pre_apply", consumer);
		return this;
	}

	@SuppressWarnings("all")
	public Consumer<Hit> getOnPreApply() {
		if (attributes == null) {
			return null;
		}
		final Object attachment = attributes.get("on_pre_apply");
		if (attachment instanceof Consumer) {
			return (Consumer<Hit>) attachment;
		}
		return null;
	}

	public void setPredicate(final Predicate<Hit> attachment) {
		putAttribute("predicate", attachment);
	}

	@SuppressWarnings("unchecked cast")
	public Predicate<Hit> getPredicate() {
		if (attributes == null) {
			return null;
		}
		final Object attachment = attributes.get("predicate");
		if (attachment instanceof Predicate) {
			return (Predicate<Hit>) attachment;
		}
		return null;
	}

	public boolean executeIfLocked() {
		return containsAttribute("execute_if_locked");
	}

	public Hit setExecuteIfLocked() {
		putAttribute("execute_if_locked", Boolean.TRUE);
		return this;
	}

	public boolean isSpecial() { return containsAttribute("usingSpecial"); }

	public Hit setSpecialAttack() {
		putAttribute("usingSpecial", Boolean.TRUE);
		return this;
	}

	public Object getWeapon() {
		if (attributes == null) {
			return null;
		}
		return attributes.get("weapon");
	}

	public Entity getSource() {
		return source;
	}

	public void setSource(Entity source) {
		this.source = source;
	}

	public HitType getHitType() {
		return hitType;
	}

	public void setHitType(HitType hitType) {
		this.hitType = hitType;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(final int damage) {
		this.damage = Math.min(32767, Math.max(0, damage));
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(final int delay) {
		this.delay = Math.min(32767, Math.max(0, delay));
	}

	public long getScheduleTime() {
		return scheduleTime;
	}

	public boolean isForcedHitsplat() {
		return forcedHitsplat;
	}

	public void setForcedHitsplat(boolean forcedHitsplat) {
		this.forcedHitsplat = forcedHitsplat;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public boolean isAccurate() {
		return accurate;
	}

	public Hit copy() {
		return new Hit(source, damage, hitType, delay);
	}

    public void miss() {
        this.damage = 0;
    }

	public void rollDamage(int i, int i1) {
		this.damage = Utils.random(i, i1);
	}
}

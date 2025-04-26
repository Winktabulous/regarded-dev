package com.zenyte.game.world;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.AbstractCollection;
import java.util.Iterator;

public class EntityList<T extends Entity> extends AbstractCollection<T> {
	private static final int MIN_VALUE = 1;
	public Object[] entities;
	public IntList indicies = new IntArrayList();
	public int curIndex = MIN_VALUE;
	public int capacity;

	public EntityList(final int capacity) {
		this.entities = new Object[capacity];
		this.capacity = capacity;
	}

	public int getEmptySlot() {
		for (int i = 1; i < this.entities.length; i++) {
			if (this.entities[i] == null) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean add(final T entity) {
		final int slot = this.getEmptySlot();
		if (slot == -1) {
			return false;
		}
		this.add(entity, slot);
		return true;
	}

	public void remove(final T entity) {
		this.entities[entity.getIndex()] = null;
		this.indicies.rem(entity.getIndex());
		this.decreaseIndex();
		if (entity instanceof Player) {
			final Player player = (Player) entity;
			World.usedPIDs.remove(player.getPid());
			World.availablePIDs.add(player.getPid());
		}
	}

	@SuppressWarnings("unchecked")
	public T remove(final int index) {
		final Object temp = this.entities[index];
		this.entities[index] = null;
		this.indicies.rem(index);
		this.decreaseIndex();
		if (temp instanceof Player) {
			final Player player = (Player) temp;
			World.usedPIDs.remove(player.getPid());
			World.availablePIDs.add(player.getPid());
		}
		return (T) temp;
	}

	@SuppressWarnings("unchecked")
	public T get(final int index) {
		if (index >= this.entities.length) {
			return null;
		}
		return (T) this.entities[index];
	}

	@SuppressWarnings("unchecked")
	public T getDirect(final int index) {
		return (T) this.entities[index];
	}

	public void add(final T entity, final int index) {
		if (this.entities[index] != null) {
			return;
		}
		this.entities[index] = entity;
		entity.setIndex(index);
		this.indicies.add(index);
		if (entity instanceof Player) {
			final Player player = (Player) entity;
			final int pid = World.availablePIDs.removeInt(Utils.random(World.availablePIDs.size() - 1));
			player.setPid(pid);
			World.usedPIDs.put(pid, player);
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new EntityListIterator<T>(this.entities, this.indicies, this);
	}

	public void increaseIndex() {
		this.curIndex++;
		if (this.curIndex >= this.capacity) {
			this.curIndex = MIN_VALUE;
		}
	}

	public void decreaseIndex() {
		this.curIndex--;
		if (this.curIndex <= this.capacity) {
			this.curIndex = MIN_VALUE;
		}
	}

	public boolean contains(final T entity) {
		return this.indexOf(entity) > -1;
	}

	public int indexOf(final T entity) {
		for (final int index : this.indicies) {
			if (this.entities[index].equals(entity)) {
				return index;
			}
		}
		return -1;
	}

	@Override
	public int size() {
		return this.indicies.size();
	}
}

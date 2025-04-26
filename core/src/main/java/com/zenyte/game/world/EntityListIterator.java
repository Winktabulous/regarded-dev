/*
 * Copyright (C) 2008  RS2DBase Development team
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.zenyte.game.world;

import com.zenyte.game.world.entity.Entity;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Iterator;

public class EntityListIterator<E extends Entity> implements Iterator<E> {
	private final IntList indicies;
	private final Object[] entities;
	@SuppressWarnings("rawtypes")
	private final EntityList entityList;
	private int curIndex = 0;

	public EntityListIterator(Object[] entities, IntList indicies,
			@SuppressWarnings("rawtypes") EntityList entityList) {
		this.entities = entities;
		this.indicies = indicies;
		this.entityList = entityList;
	}

	@Override
	public boolean hasNext() {
		return indicies.size() != curIndex;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E next() {
		Object temp = entities[indicies.getInt(curIndex)];
		curIndex++;
		return (E) temp;
	}

	@Override
	public void remove() {
		if (curIndex >= 1) {
			entityList.remove(indicies.getInt(curIndex - 1));
		}
	}
}

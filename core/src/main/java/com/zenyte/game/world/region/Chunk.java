package com.zenyte.game.world.region;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.efficientarea.EfficientArea;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Kris | 28. march 2018 : 17:28.55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Chunk implements MapSquare {
	/**
	 * The bitpacked if of this chunk.
	 */
	private final int chunkId;
	private long referenceTime;

	public Chunk resetReferenceTime() {
		referenceTime = System.currentTimeMillis();
		return this;
	}

	/**
	 * List of players in the chunk.
	 */
	private final List<Player> players;
	/**
	 * List of NPCs in the chunk.
	 */
	private final ObjectArrayList<NPC> npcs;
	/**
	 * List of objects that have been spawned.
	 */
	private final Short2ObjectMap<WorldObject> spawnedObjects;
	/**
	 * List of original map-bound objects that have been removed or replaced.
	 */
	private final Short2ObjectMap<WorldObject> originalObjects;
	private final Set<FloorItem> floorItems;
	@Nullable
	private List<RSArea> multiPolygons;

	public boolean isFree() {
		return players.isEmpty() && npcs.isEmpty()
				&& (spawnedObjects == null || spawnedObjects.isEmpty())
				&& (originalObjects == null || originalObjects.isEmpty())
				&& (floorItems == null || floorItems.isEmpty())
				&& (multiPolygons == null || multiPolygons.isEmpty());
	}

	public Chunk(final int hash) {
		chunkId = hash;
		players = new ArrayList<>();
		npcs = new ObjectArrayList<>();
		spawnedObjects = new Short2ObjectOpenHashMap<>();
		originalObjects = new Short2ObjectOpenHashMap<>();
		floorItems = new HashSet<>();
	}

	public void addFloorItem(final FloorItem item) {
		floorItems.add(item);
		World.getAllFloorItems().add(item);
		if (item.getInvisibleTicks() <= 0) {
			World.destroyFloorItem(getRemovedItemIfCapReached());
		}
	}

	public void removeFloorItem(final FloorItem item, final boolean removeFromGlobal) {
		floorItems.remove(item);
		if (removeFromGlobal) World.getAllFloorItems().remove(item);
	}

	public FloorItem getRemovedItemIfCapReached() {
		if (floorItems.size() <= 128) {
			return null;
		}
		int count = 0;
		for (final FloorItem item : floorItems) {
			if (item == null || item.getInvisibleTicks() > 0 || !item.isTradable()) {
				continue;
			}
			count++;
		}
		if (count > 129) {
			FloorItem cheapest = null;
			for (final FloorItem item : floorItems) {
				if (item == null || item.getInvisibleTicks() > 0 || !item.isTradable()) {
					continue;
				}
				if (cheapest == null || ((long) item.getSellPrice() * item.getAmount()) < ((long) cheapest.getSellPrice() * cheapest.getAmount())) {
					cheapest = item;
				}
			}
			return cheapest;
		}
		return null;
	}

	public void clearFloorItems() {
		if (floorItems.isEmpty()) return;
		for (final FloorItem floorItem : floorItems) {
			World.getAllFloorItems().remove(floorItem);
		}
		floorItems.clear();
	}

	public void wipe() {
		clearFloorItems();
		spawnedObjects.clear();
		originalObjects.clear();
	}

	public void addMultiPolygon(final RSArea area) {
		if (multiPolygons == null) multiPolygons = new ArrayList<>();
		multiPolygons.add(area);
	}

	public void addNPC(NPC npc) {
		if(!npcs.contains(npc)){
			npcs.add(npc);
		}
	}


	public static final class RSArea {
		private final int height;
		private final EfficientArea area;

		public RSArea(int height, EfficientArea area) {
			this.height = height;
			this.area = area;
		}

		public int getHeight() {
			return height;
		}

		public EfficientArea getArea() {
			return area;
		}
	}

	public static final int getChunkHash(final int chunkX, final int chunkY, final int height) {
		return chunkX | chunkY << 11 | height << 22;
	}

	public final int getChunkX() {
		return chunkId & 2047;
	}

	public final int getChunkY() {
		return chunkId >> 11 & 2047;
	}

	public final int getChunkZ() {
		return chunkId >> 22;
	}

	@Override
	public ObjectArrayList<NPC> getNPCs() {
		return npcs;
	}

	public void removeNPC(NPC npc) {
		npcs.remove(npc);
	}

	public int getChunkId() {
		return chunkId;
	}

	public long getReferenceTime() {
		return referenceTime;
	}

	public ObjectArrayList<Player> getPlayers() {
		return new ObjectArrayList<Player>(players.stream().filter(Chunk::isPlayerValid).toList());
	}

	private static boolean isPlayerValid(Player it) {
		return it != null && it.getAttributes() != null;
	}

	public Iterator<@NotNull Player> safePlayerIterator() {
		return new Iterator<@NotNull Player>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				if (i >= players.size())
					return false;
				final Player next = players.get(i);
				if (!isPlayerValid(next)) {
					++i;
					return hasNext();
				}
				return true;
			}

			@Override
			public Player next() {
				if (i >= players.size())
					throw new NoSuchElementException();
				final Player next = players.get(i++);
				if (!isPlayerValid(next)) {
					return next();
				}
				return next;
			}
		};
	}


	public void removePlayer(Player player) {
		players.remove(player);
	}

	public void addPlayer(Player player) {
		if(!players.contains(player))
			players.add(player);
	}

	public Short2ObjectMap<WorldObject> getSpawnedObjects() {
		return spawnedObjects;
	}

	public Short2ObjectMap<WorldObject> getOriginalObjects() {
		return originalObjects;
	}

	public Set<FloorItem> getFloorItems() {
		return floorItems;
	}

	public List<RSArea> getMultiPolygons() {
		return multiPolygons;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof Chunk)) return false;
		final Chunk other = (Chunk) o;
		if (this.getChunkId() != other.getChunkId()) return false;
		if (this.getReferenceTime() != other.getReferenceTime()) return false;
		final Object this$players = this.players;
		final Object other$players = other.players;
		if (this$players == null ? other$players != null : !this$players.equals(other$players)) return false;
		final Object this$npcs = this.getNPCs();
		final Object other$npcs = other.getNPCs();
		if (this$npcs == null ? other$npcs != null : !this$npcs.equals(other$npcs)) return false;
		final Object this$spawnedObjects = this.getSpawnedObjects();
		final Object other$spawnedObjects = other.getSpawnedObjects();
		if (this$spawnedObjects == null ? other$spawnedObjects != null : !this$spawnedObjects.equals(other$spawnedObjects)) return false;
		final Object this$originalObjects = this.getOriginalObjects();
		final Object other$originalObjects = other.getOriginalObjects();
		if (this$originalObjects == null ? other$originalObjects != null : !this$originalObjects.equals(other$originalObjects)) return false;
		final Object this$floorItems = this.getFloorItems();
		final Object other$floorItems = other.getFloorItems();
		if (this$floorItems == null ? other$floorItems != null : !this$floorItems.equals(other$floorItems)) return false;
		final Object this$multiPolygons = this.getMultiPolygons();
		final Object other$multiPolygons = other.getMultiPolygons();
		return this$multiPolygons == null ? other$multiPolygons == null : this$multiPolygons.equals(other$multiPolygons);
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + this.getChunkId();
		final long $referenceTime = this.getReferenceTime();
		result = result * PRIME + (int) ($referenceTime >>> 32 ^ $referenceTime);
		final Object $players = this.players;
		result = result * PRIME + ($players == null ? 43 : $players.hashCode());
		final Object $npcs = this.getNPCs();
		result = result * PRIME + ($npcs == null ? 43 : $npcs.hashCode());
		final Object $spawnedObjects = this.getSpawnedObjects();
		result = result * PRIME + ($spawnedObjects == null ? 43 : $spawnedObjects.hashCode());
		final Object $originalObjects = this.getOriginalObjects();
		result = result * PRIME + ($originalObjects == null ? 43 : $originalObjects.hashCode());
		final Object $floorItems = this.getFloorItems();
		result = result * PRIME + ($floorItems == null ? 43 : $floorItems.hashCode());
		final Object $multiPolygons = this.getMultiPolygons();
		result = result * PRIME + ($multiPolygons == null ? 43 : $multiPolygons.hashCode());
		return result;
	}

}

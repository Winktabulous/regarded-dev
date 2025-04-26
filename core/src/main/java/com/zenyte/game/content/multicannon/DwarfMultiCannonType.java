package com.zenyte.game.content.multicannon;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.object.ObjectId;

public enum DwarfMultiCannonType {

	REGULAR(ItemId.CANNON_BASE, ItemId.CANNON_STAND, ItemId.CANNON_BARRELS, ItemId.CANNON_FURNACE, ObjectId.CANNON_BASE, ObjectId.CANNON_STAND, ObjectId.CANNON_BARRELS, ObjectId.DWARF_MULTICANNON, ObjectId.BROKEN_MULTICANNON_14916),
	ORNAMENT(26520, 26522, 26524, 26526, 43029, 43030, 43031, 43027, 43028);

	private final Item base, stand, barrel, furnace;
	private final Item[] parts;
	private final int baseLoc, standLoc, barrelsLoc, cannonLoc, brokenCannonLoc;

	DwarfMultiCannonType(final int base, final int stand, final int barrel, final int furnace, final int baseLoc, final int standLoc, final int barrelsLoc, final int cannonLoc, final int brokenCannonLoc) {
		this.base = new Item(base);
		this.stand = new Item(stand);
		this.barrel = new Item(barrel);
		this.furnace = new Item(furnace);
		this.parts = new Item[]{this.base, this.stand, this.barrel, this.furnace};
		this.baseLoc = baseLoc;
		this.standLoc = standLoc;
		this.barrelsLoc = barrelsLoc;
		this.cannonLoc = cannonLoc;
		this.brokenCannonLoc = brokenCannonLoc;
	}

	public Item getBase() {
		return base;
	}

	public Item getStand() {
		return stand;
	}

	public Item getBarrel() {
		return barrel;
	}

	public Item getFurnace() {
		return furnace;
	}

	public Item[] getParts() {
		return parts;
	}

	public int getBaseLoc() {
		return baseLoc;
	}

	public int getStandLoc() {
		return standLoc;
	}

	public int getBarrelsLoc() {
		return barrelsLoc;
	}

	public int getCannonLoc() {
		return cannonLoc;
	}

	public int getBrokenCannonLoc() {
		return brokenCannonLoc;
	}

	public static DwarfMultiCannonType list(Item item) {
		if (item.getId() == ItemId.CANNON_BASE) {
			return REGULAR;
		}

		return ORNAMENT;
	}

}

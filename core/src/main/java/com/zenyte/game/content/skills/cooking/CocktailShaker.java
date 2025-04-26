package com.zenyte.game.content.skills.cooking;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public enum CocktailShaker {
	
	FRUIT_BLAST(GnomeCocktail.FRUIT_BLAST.getShaker(), 50, new Item(2084), 2106),
	PINEAPPLE_PUNCH(GnomeCocktail.PINEAPPLE_PUNCH.getShaker(), 70, new Item(2048), 2122, 2116, 2112),
	WIZARD_BLIZZARD(GnomeCocktail.WIZARD_BLIZZARD.getShaker(), 110, new Item(2054), 2116, 2124),
	SHORT_GREEN_GUY(GnomeCocktail.SHORT_GREEN_GUY.getShaker(), 120, new Item(2080), 2124, 2128),
	DRUNK_DRAGON(GnomeCocktail.DRUNK_DRAGON.getShaker(), 160, new Item(9575), 2116, 2130),
	CHOCOLATE_SATURDAY(GnomeCocktail.CHOCOLATE_SATURDAY.getShaker(), 170, new Item(9572), 1975, 2130),
	BLURBERRY_SPECIAL(GnomeCocktail.BLURBERRY_SPECIAL.getShaker(), 180, new Item(9520), 2104, 2110, 2128, 2124)
	;
	
	
	private final int id;
	private final double xp;
	private final Item product;
	private final int[] garnish;
	
	public static final CocktailShaker[] VALUES = values();
	public static final Map<Integer, CocktailShaker> SHAKERS = new HashMap<Integer, CocktailShaker>();
	
	static {
		for(CocktailShaker entry : VALUES)
			SHAKERS.put(entry.getId(), entry);
	}
	
	CocktailShaker(int id, double xp, Item product, int... garnish) {
		this.id = id;
		this.xp = xp;
		this.product = product;
        this.garnish = garnish;
    }

    public static boolean hasGarnish(Player player, CocktailShaker shaker, boolean special) {
        if (shaker == null)
            return false;

        if (!special)
            if (shaker == CocktailShaker.DRUNK_DRAGON || shaker == CocktailShaker.CHOCOLATE_SATURDAY)
                return true;

        for (int item : shaker.getGarnish()) {
            if (player.getInventory().containsItem(item, 1))
                continue;
            else
                return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public double getXp() {
        return xp;
    }

    public Item getProduct() {
        return product;
    }

    public int[] getGarnish() {
        return garnish;
    }

}
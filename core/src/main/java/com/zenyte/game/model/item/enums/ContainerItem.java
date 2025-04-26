package com.zenyte.game.model.item.enums;

import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Noele | Jul 18, 2018 : 6:08:09 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public enum ContainerItem {
	
	WATER_VIAL(new Item(227), Type.VIAL, "empty", "water"),
	
	WATER_BOWL(new Item(1921), Type.BOWL, "empty", "water"),
	
	WATER_JUG(new Item(1937), Type.JUG, "empty", "water"),
	
	SAND_BUCKET(new Item(1783), Type.BUCKET, "empty"),
	MILK_BUCKET(new Item(1927), Type.BUCKET, "empty"),
	WATER_BUCKET(new Item(1929), Type.BUCKET, "empty", "water"),
	SLIME_BUCKET(new Item(4286), Type.BUCKET, "empty"),
	SAP_BUCKET(new Item(4687), Type.BUCKET, "empty"),
	
	FLOUR_POT(new Item(1933), Type.POT, "empty"),
	CORNFLOUR_POT(new Item(7468), Type.POT, "empty"),
	VINEGAR_POT(new Item(7811), Type.POT, "empty"),
	
	COKE_SPADE(new Item(6448), Type.SPADE, "empty"),
	
	ICY_WATER_BUCKET(new Item(ChristmasConstants.ICY_WATER_BUCKET), Type.BUCKET, "empty");
	
    public final static ContainerItem[] VALUES = values();
    public final static Map<Integer, ContainerItem> all = new HashMap<Integer, ContainerItem>();
    public final static Map<String, List<ContainerItem>> lists = new HashMap<String, List<ContainerItem>>();

	static {
		List<String> flags = new ArrayList<String>();
		for(ContainerItem entry : VALUES)
			for(String flag : entry.getFlags())
				if(!flags.contains(flag))
					flags.add(flag);

        for (String flag : flags)
            lists.put(flag, new ArrayList<ContainerItem>());

        for (ContainerItem entry : VALUES) {
            all.put(entry.getContainer().getId(), entry);

            for (String flag : entry.getFlags())
                appendList(flag, entry);
        }
    }

    private final Item container;
    private final Type type;
    private final String[] flags;

    ContainerItem(final Item container, final Type type, String... flags) {
        this.container = container;
        this.type = type;
        this.flags = flags;
    }

    public static void appendList(final String flag, final ContainerItem entry) {
        List<ContainerItem> list = lists.get(flag);
        if (list != null)
			list.add(entry);
	}
	
    public Item getContainer() {
        return container;
    }

    public Type getType() {
        return type;
    }

    public String[] getFlags() {
        return flags;
    }

	public enum Type {

        VIAL(new Item(229)),
        BOWL(new Item(1923)),
        JUG(new Item(1935)),
        BUCKET(new Item(1925)),
        POT(new Item(1931)),
        PIE(new Item(2313)),
        SPADE(new Item(952));

        private final Item empty;

        Type(final Item empty) {
            this.empty = empty;
        }

        public Item getEmpty() {
            return empty;
        }

    }


}

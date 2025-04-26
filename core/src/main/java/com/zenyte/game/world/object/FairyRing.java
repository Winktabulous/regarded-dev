package com.zenyte.game.world.object;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary;
import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.PlayerChat;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.plugins.interfaces.FairyRingLog;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.enums.Enums;
import mgi.types.config.enums.StringEnum;

import java.util.List;
import java.util.OptionalInt;

public enum FairyRing {
	MUDSKIPPER_POINT(new Location(2996, 3114, 0), "AIQ", "Asgarnia: Mudskipper Point", "Mudskipper point"), ARDOUGNE_ISLAND(new Location(2700, 3247, 0), "AIR", "Islands: South-east of Ardougne", "Islands: SE of Ardy (Clue)"), DORGESH_KAAN(new Location(2735, 5221, 0), "AJQ", "Dungeons: Dorgesh-Kaan cave", "Cave south of Dorgesh-Kaan"), GOLDEN_APPLE_TREE(new Location(2780, 3613, 0), "AJR", "Kandarin: Slayer cave near Rellekka", "Rellekka slayer cave"), MISCELLANIA_SMALL(new Location(2500, 3896, 0), "AJS", "Islands: Penguins", "Penguins"), PISCATORIS(new Location(2319, 3619, 0), "AKQ", "Kandarin: Piscatoris Hunter area", "Piscatoris hunter area"), FELDIP_HILLS(new Location(2571, 2956, 0), "AKS", "Feldip Hills: Jungle Hunter area", "Feldip hunter area"), LIGHTHOUSE(new Location(2503, 3636, 0), "ALP", "Islands: Lighthouse", "Lighthouse"), HAUNTED_WOODS(new Location(3597, 3495, 0), "ALQ", "Morytania: Haunted Woods", "Haunted Woods near Canifis"), ABYSSAL_AREA(new Location(3059, 4875, 0), "ALR", "Other Realms: Abyss", "Abyssal Area"), MCGRUBBORS_WOOD(new Location(2644, 3495, 0), "ALS", "Kandarin: McGrubor's Wood", "McGrubor's Wood near Seers' Village"), MORT_MYRE_SWAMP(new Location(3410, 3324, 0), "BIP", "Islands: River Salve", "Islands: SW of Mort Myre (Clue)"), KALPHITE_LAIR(new Location(3251, 3095, 0), "BIQ", "Kharidian Desert: Near the Kalphite Hive", "Kharidian Desert near Kalphite Hive"), ARDOUGNE_ZOO(new Location(2635, 3266, 0), "BIS", "Kandarin: Ardougne Zoo unicorns", "Ardougne Zoo: Unicorns"), ZULANDRA(new Location(2150, 3070, 0), "BJS", "Islands: Near Zul-Andra", "Islands: Zul-Andra (Zulrah)"), CASTLE_WARS(new Location(2385, 3035, 0), "BKP", "Feldip Hills: South of Castle Wars", "South of Castle Wars"), ENCHANTED_VALLEY(new Location(3041, 4532, 0), "BKQ", "Other Realms: Enchanted Valley", "Enchanted Valley"), ZANARIS(new Location(2412, 4434, 0), "BKS", "Other Realms: Zanaris", "Zanaris"), TZHAAR(new Location(2437, 5126, 0), "BLP", "Dungeons: TzHaar area", "TzHaar area"), LEGENDS_GUILD(new Location(2740, 3351, 0), "BLR", "Kandarin: Legends' Guild", "Legends' Guild"), MYRE_SWAMP(new Location(3469, 3431, 0), "BKR", "Morytania: Mort Myre Swamp, south of Canifis", "Mort Myre Swamp"), MISCELLANIA(new Location(2513, 3884, 0), "CIP", "Islands: Miscellania", "Miscellania"), YANILLE(new Location(2528, 3127, 0), "CIQ", "Kandarin: North-west of Yanille", "North-west of Yanille"), FARMING_GUILD(new Location(1302, 3762, 0), "CIR", "Kebos Lowlands: South of Mount Karuulm", "North-east of Farming Guild"), ARCEUUS_HOUSE(new Location(1639, 3868, 0), "CIS", "Zeah: Arceuus House Library", "North of the Arceuus House Library"), SINCLAIR_MANSION(new Location(2705, 3576, 0), "CJR", "Kandarin: Sinclair Mansion", "Sinclair Mansion (east)"), SHILO_VILLAGE(new Location(2801, 3003, 0), "CKR", "Karamja: Tai Bwo Wannai Village", "Near Shilo Village (Nature Altar)"), CANIFIS(new Location(3447, 3470, 0), "CKS", "Morytania: Canifis", "Canifis"), DRAYNOR_ISLAND(new Location(3082, 3206, 0), "CLP", "Islands: South of Draynor Village", "Island: South of Draynor (Clues)"), APE_ATOLL(new Location(2740, 2738, 0), "CLR", "Islands: Ape Atoll", "Ape Atoll Agility Area"), YANILLE2(new Location(2682, 3081, 0), "CLS", "Islands: Hazelmere's home", "Jungle Spiders near Yanille"),
	//ABYSSAL_NEXUS(107, new Location(3037, 4763, 0), "DIP", "Other Realms: Abyssal Nexus", "Abyssal Nexus (Abyssal Sire)"),
	GORAKS(new Location(3038, 5348, 0), "DIR", "Other Realms: Goraks' plane", "Goraks' plane"), WIZARDS_TOWER(new Location(3108, 3149, 0), "DIS", "Misthalin: Wizards' Tower", "Wizards' Tower"), LIFE_TOWER(new Location(2658, 3230, 0), "DJP", "Kandarin: Tower of Life", "Tower of Life"), FIRE_CHASM(new Location(1455, 3658, 0), "DJR", "Zeah: Chasm of Fire", "Chasm of Fire"), KARAMJA(new Location(2900, 3111, 0), "DKP", "Karamja: Gnome Glider", "Karamja (Karambwan fishing spots)"), EDGEVILLE(new Location(3095, 3488, 0), "DKR", "Misthalin: Edgeville", "Edgeville"), KELDAGRIM(new Location(2744, 3719, 0), "DKS", "Kandarin: Polar Hunter area", "Snowy hunter area (Near Keldagrim)"), LIZARDS(new Location(3423, 3016, 0), "DLQ", "Kharidian Desert: North of Nardah", "North of Nardah (Desert"), POISON_WASTE(new Location(2213, 3099, 0), "DLR", "Islands: Poison Waste", "Islands: Poison Waste south of Isafdar"), REALM_OF_THE_FISHER_KING(new Location(2650, 4730, 0), "BJR", "Realm of the Fisher King", "Realm of the Fisher King"), COSMIC_ENTITYS_PLANE(new Location(2075, 4848, 0), "CKP", "Cosmic entity's plane", "Cosmic entity's plane"), ZANARIS_ENTRANCE(new Location(2452, 4473, 0), "---", "N/A", "N/A"), LUMBRIDGE_SHED(new Location(3202, 3169, 0), "---", "N/A", "N/A");
	private final int component;
	private final Location tile;
	private final String code;
	private final String tag;
	private final String name;
	public static final FairyRing[] values;
	public static final Object2ObjectOpenHashMap<String, FairyRing> codes;
	private static final Int2ObjectOpenHashMap<FairyRing> components;
	private static final Graphics flowers = new Graphics(569);
	private static final Animation fadeOut = new Animation(3265);
	private static final Animation fadeIn = new Animation(3266);

	static {
		final FairyRing[] entries = values();
		values = new FairyRing[entries.length - 2];
		System.arraycopy(entries, 0, values, 0, entries.length - 2);
		codes = new Object2ObjectOpenHashMap<>(values.length);
		components = new Int2ObjectOpenHashMap<>(values.length);
	}

	FairyRing(final Location tile, final String code, final String tag, final String name) {
		final StringEnum e = Enums.FAIRY_RING_CODES;
		assert code.length() == 3;
		final OptionalInt optionalComponentHash = e.getKey(code.charAt(0) + " " + code.charAt(1) + " " + code.charAt(2));
		this.component = optionalComponentHash.isPresent() ? (optionalComponentHash.getAsInt() & 65535) : -1;
		this.tile = tile;
		this.code = code;
		this.tag = tag;
		this.name = name;
	}

	public static FairyRing getRing(final String code) {
		return codes.get(code);
	}

	public static FairyRing getRing(final int component) {
		return components.get(component);
	}

	static {
		for (final FairyRing entry : values) {
			components.put(entry.getComponent(), entry);
			codes.put(entry.getCode(), entry);
		}
	}

	@SuppressWarnings("unchecked")
	public static final List<String> getFavourites(final Player player) {
		final Object obj = player.getAttributes().get("fairyRingFavourites");
		return obj == null ? new ObjectArrayList<>(4) : (List<String>) obj;
	}

	public static final void addFavourite(final Player player, final FairyRing ring) {
		final List<String> favourites = getFavourites(player);
		if (favourites.size() >= 4) {
			player.sendMessage("You already have 4 favourites.");
			return;
		}
		if (favourites.contains(ring.toString())) {
			return;
		}
		player.sendMessage("You have added " + ring.getCode() + " to your favourites.");
		favourites.add(ring.getCode());
		player.getAttributes().put("fairyRingFavourites", favourites);
		FairyRingLog.open(player);
	}

	public static final void removeFavourite(final Player player, final int componentId, final int option) {
		final List<String> favourites = getFavourites(player);
		int index = -1;
		if (option == 1) {
			if (componentId >= 148 && componentId <= 151) {
				index = componentId - 148;
			}
		} else if (option == 2) {
			if (componentId >= 140 && componentId <= 143) {
				index = componentId - 140;
			}
		}
		if (index == -1) {
			return;
		}
		final FairyRing ring = FairyRing.getRing(favourites.get(index));
		if (ring == null) {
			return;
		}
		player.sendMessage("You have removed " + ring.getCode() + " from your favourites.");
		favourites.remove(ring.getCode());
		if (favourites.size() == 0) {
			player.getAttributes().remove("fairyRingFavourites");
		} else {
			player.getAttributes().put("fairyRingFavourites", favourites);
		}
		FairyRingLog.open(player);
	}

	@Subscribe
	public static final void onLogin(final LoginEvent event) {
		final int component = event.getPlayer().getNumericAttribute("lastFairyRing").intValue();
		if (component == 0) {
			return;
		}
		final OptionalInt bitValue = Enums.FAIRY_RING_VARBIT_CODES.getKey(381 << 16 | component);
		bitValue.ifPresent(value -> event.getPlayer().getVarManager().sendBit(5374, value));
	}

	public static final void handle(final Player player, final WorldObject object, final FairyRing ring) {
		player.lock();
		player.getTemporaryAttributes().put("fairyRingCode", "AIP");
		for (int i = 3985; i <= 3987; i++) {
			player.getVarManager().sendBit(i, 0);
		}
		if (ring != null) {
			final OptionalInt bitValue = Enums.FAIRY_RING_VARBIT_CODES.getKey(381 << 16 | ring.getComponent());
			bitValue.ifPresent(value -> {
				player.getVarManager().sendBit(5374, value);
				player.getAttributes().put("lastFairyRing", ring.getComponent());
			});
		}
		if (object != null && object.getId() != 35003) {
			player.addWalkSteps(object.getX(), object.getY(), 1, false);
		}
		player.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			@Override
			public void run() {
				if (ticks == 0) {
					player.sendSound(1098);
					player.setGraphics(flowers);
				} else if (ticks == 1) {
					player.setAnimation(fadeOut);
				} else if (ticks == 3) {
					player.setAnimation(fadeIn);
				} else if (ticks == 4) {
					if (ring != null) {
						if (ring.equals(FairyRing.WIZARDS_TOWER)) {
							player.getAchievementDiaries().update(LumbridgeDiary.TRAVEL_TO_WIZARDS_TOWER);
						} else if (ring.equals(FairyRing.MISCELLANIA)) {
							player.getAchievementDiaries().update(FremennikDiary.TRAVEL_TO_MISCELLANIA);
						} else if (ring.equals(FairyRing.MCGRUBBORS_WOOD)) {
							player.getAchievementDiaries().update(KandarinDiary.TRAVEL_TO_MCGRUBORS_WOODS);
						} else if (ring.equals(FairyRing.FARMING_GUILD)) {
							player.getAchievementDiaries().update(KourendDiary.FAIRY_RING_TO_MT_KARUULM);
						}
						player.setLocation(new Location(ring.getTile()));
					} else {
						player.getDialogueManager().start(new PlayerChat(player, "Wow, fairy magic sure is useful, I hardly moved at all!"));
					}
				} else if (ticks == 6) {
					stop();
					player.unlock();
					return;
				}
				ticks++;
			}
		}, 0, 0);
	}

	public int getComponent() {
		return component;
	}

	public Location getTile() {
		return tile;
	}

	public String getCode() {
		return code;
	}

	public String getTag() {
		return tag;
	}

	public String getName() {
		return name;
	}
}

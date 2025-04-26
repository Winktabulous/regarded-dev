package com.zenyte.plugins.renewednpc;

import com.zenyte.ContentConstants;
import com.zenyte.game.world.entity.npc.NpcId;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import static com.zenyte.game.world.entity.npc.NpcId.*;

/**
 * @author Kris | 25/11/2018 09:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum ShopNPCHandler {

    AARONS_ARCHERY_APPENDAGES("Aaron's Archery Appendages", ARMOUR_SALESMAN),
    AEMADS_ADVENTURING_SUPPLIES("Aemad's Adventuring Supplies", // TODO: Npc only visible after quest completion.
    AEMAD),
    // TODO: Npc only visible after quest completion.
    AGMUNDI_QUALITY_CLOTHES("Agmundi Quality Clothes", AGMUNDI),
    // TODO: Npc only visible after quest completion.
    AK_HARANUS_EXOTIC_SHOP("Ak-Haranu's Exotic Shop", AKHARANU),
    AL_KHARID_GENERAL_STORE("Al Kharid General Store", SHOP_KEEPER_2817),
    ALECKS_HUNTER_EMPORIUM("Aleck's Hunter Emporium", ALECK),
    ALICES_FARMING_SHOP("Alice's Farming shop", ALICE),
    ALLANNAS_FARMING_SHOP("Allanna's Farming Shop", ALLANNA),
    // TODO: Ali's Discount Wares,
    // Potentially TODO: Western side sells more items.
    AMELIAS_SEED_SHOP("Amelia's Seed Shop", AMELIA_8530),
    // Potentially TODO: Western side sells more items.
    ARDOUGNE_BAKERS_STALL("Ardougne Baker's Stall", BAKER),
    // TODO: Verify that npc exists + add access to it.
    ARDOUGNE_FUR_STALL("Ardougne Fur Stall", FUR_TRADER),
    // TODO: Verify that npc exists + add access to it.
    ARDOUGNE_GEM_STALL("Ardougne Gem Stall", GEM_MERCHANT),
    // TODO: Verify that npc exists + add access to it.
    ARDOUGNE_SILVER_STALL("Ardougne Silver Stall", SILVER_MERCHANT),
    // TODO: Verify that npc exists + add access to it.
    ARDOUGNE_SPICE_STALL("Ardougne Spice Stall", SPICE_SELLER),
    // TODO: Verify that npc exists + add access to it.
    NEDS_HANDMADE_ROPE("Ned's Handmade Rope", NED),
    // TODO: Verify that npc exists + add access to it.
    ARHEIN_STORE("Arhein Store", ARHEIN),
    // TODO: Verify that npc exists + add access to it.
    RAUM_URDA_STEIN_ARMOUR_SHOP("Armour Shop", RAUM_URDASTEIN),
    // TODO: Verify that npc exists + add access to it.
    JORZIK_ARMOUR_STORE("Armour store", JORZIK),
    // TODO: Add npc
    ARMOURY("Armoury", SHOP_KEEPER_2894),
    // TODO: Add npc
    ARNOLDS_ECLECTIC_SUPPLIES("Arnold's Eclectic Supplies", ARNOLD_LYDSPOR),
    // TODO: Requires quest completion.
    AUBURY_RUNES_SHOP("Aubury's Rune Shop", AUBURY),
    // TODO: Requires quest completion.
    AURELS_SUPPLIES("Aurel's Supplies", AUREL),
    // TODO: Requires quest completion.
    AUTHENTIC_THROWING_WEAPONS("Authentic Throwing Weapons", TRIBAL_WEAPON_SALESMAN),
    // TODO: Requires quest completion.
    AVAS_ODDS_AND_ENDS("Ava's Odds and Ends", AVA),
    // TODO: Add access + spawns.
//    BABA_YAGAS_MAGIC_SHOP("Baba Yaga's Magic Shop", BABA_YAGA),
    BANDIT_BARGAINS("Bandit Bargains", BANDIT_SHOPKEEPER),
    BANDIT_DUTY_FREE("Bandit Duty Free", NOTERAZZO),
    BARKER_HABERDASHERY("Barker's Haberdashery", // TODO: Requires quest completion.
    BARKER),
    // TODO: Requires quest completion.
    BATTLE_RUNES("Battle Runes", MAGE_OF_ZAMORAK_2581),
    // TODO: Requires quest completion.
    BEDABIN_VILLAGE_BARTERING("Bedabin Village Bartering", BEDABIN_NOMAD),
    // TODO: Requires quest completion.
    BETTYS_MAGIC_EMPORIUM("Betty's Magic Emporium", BETTY),
    // TODO: Requires quest completion.
    BLADES_BY_URBI("Blades by Urbi", URBI),
    // TODO: Daero npc also allows opening shop.
    BLURBERRY_BAR("Blurberry Bar", BARMAN_6532),
    // TODO: On option "Armour"
    BOBS_BRILLIANT_AXES("Bob's Brilliant Axes", BOB_10619),
    // TODO: On option "Armour"
    BOLKOYS_VILLAGE_SHOP("Bolkoy's Village Shop", BOLKOY),
    // TODO: On option "Armour"
    BRIANS_ARCHERY_SUPPLIES("Brian's Archery Supplies", BRIAN_8694),
    // TODO: On option "Armour"
    BRIANS_BATTLEAXE_BAZAAR("Brian's Battleaxe Bazaar", BRIAN),
    // TODO: On option "Armour"
    BRIDGETS_ARMOUR("Briget's Armour", 7201),
    // TODO: On option "Weapons"
    BRIDGETS_WEAPONS("Briget's Weapons", 7201),
    // TODO: Ensure npc exists
    BURTHORPE_SUPPLIES("Burthorpe Supplies", WISTAN),
    // TODO: Ensure npc exists
    CANDLE_SHOP("Candle Shop", CANDLE_MAKER),
    // TODO: Ensure npc exists
    CAREFREE_CRAFTING_STALL("Carefree Crafting Stall", NOLAR),
    // TODO: No trade option.
    CASSIES_SHIELD_SHOP("Cassie's Shield Shop", CASSIE),
    // TODO: No trade option.
    CONTRABAND_YAK_PRODUCE("Contraband yak produce", VANLIGGA_GASTFRIHET),
    // TODO: Culinaromancer's chest.
    // TODO: No trade option.
    DAGAS_SCIMITAR_SMITHY("Daga's Scimitar Smithy", DAGA),
    // TODO: No trade option.
    DALS_GENERAL_OGRE_SUPPLIES("Dal's General Ogre Supplies", OGRE_TRADER_4404),
    // TODO: No trade option.
    DARGAUDS_BOW_AND_ARROWS("Dargaud's Bow and Arrows", BOW_AND_ARROW_SALESMAN),
    // TODO: No trade option.
    DARRENS_WILDERNESS_CAPE_SHOP("Darren's Wilderness Cape Shop", DARREN),
    // TODO: No trade option.
    DAVONS_AMULET_STORE("Davon's Amulet Store", DAVON),
    // TODO: No trade option.
    DEAD_MANS_CHEST("Dead Man's Chest", BARTENDER_1314),
    DIANGOS_TOY_STORE("Diango's Toy Store", DIANGO),
    // DODGY_MIKES_SECOND_HAND_CLOTHING("Dodgy Mike's Second-hand Clothing", 4022),
    // TODO: Verify npc's existence.
    DOMMIKS_CRAFTING_STORE("Dommik's Crafting Store", DOMMIK),
    // TODO: Verify npc's existence.
    DORGESH_KAAN_GENERAL_SUPPLIES("Dorgesh-Kaan General Supplies", LURGON),
    // TODO: Quest requirement
    DRAYNOR_SEED_MARKET("Draynor Seed Market", OLIVIA),
    // TODO: Quest requirement
    DROGOS_MINING_EMPORIUM("Drogo's Mining Emporium", DROGO_DWARF),
    // TODO: Quest requirement
    DWARVEN_SHOPPING_STORE("Dwarven Shopping Store", DWARF_5904),
    // TODO: Quest requirement
    GENERAL_STORE_ASSISTANCE(ContentConstants.SERVER_NAME + " General Store", SHOP_ASSISTANT_2822),
    // TODO: Quest requirement
    GENERAL_STORE_KEEPER(ContentConstants.SERVER_NAME + " General Store", SHOP_KEEPER_2821),
    // TODO: Quest requirement
    EDMONDS_WILDERNESS_CAPE_SHOP("Edmond's Wilderness Cape Shop", EDMOND),
    // TODO: Quest requirement
    EDWARDS_WILDERNESS_CAPE_SHOP("Edward's Wilderness Cape Shop", EDWARD),
    // TODO: Quest requirement
    ETCETERIA_FISH("Etceteria Fish", FISHMONGER),
    // TODO: Quest requirement
    FAIRY_FIXITS_FAIRY_ENCHANTMENT("Fairy Fixit's Fairy Enchantment", FAIRY_FIXIT_7333),
    // TODO: Add npc spawn
    FALADOR_GENERAL_STORE("Falador General Store", SHOP_KEEPER_2817, SHOP_ASSISTANT_2818),
    // TODO: Add npc spawn
    FANCY_CLOTHES_STORE("Fancy Clothes Store", ASYFF),
    // TODO: Add npc spawn
    FERNAHEIS_FISHING_HUT("Fernahei's Fishing Hut", FERNAHEI),
    // TODO: Add npc spawn
    FILAMINAS_WARES("Filamina's Wares", FILAMINA),
    FINE_FASHIONS("Fine Fashions", ROMETTI),
    FISHING_GUILD_SHOP("Fishing Guild Shop", ROACHEY),
    FLOSIS_FISHMONGERS("Flosi's Fishmongers", // TODO: Co-op option + tradingsticks used.
    FLOSI_DALKSSON),
    // TODO: Co-op option + tradingsticks used.
    FLYNNS_MACE_MARKET("Flynn's Mace Market", FLYNN),
    // TODO: Co-op option + tradingsticks used.
    FORTUNADOS_FINE_WINE("Fortunato's Fine Wine", FORTUNATO),
    // TODO: Co-op option + tradingsticks used.
    FOSSIL_ISLAND_GENERAL_STORE("Fossil Island General Store", SHOP_KEEPER_7769),
    // TODO: Co-op option + tradingsticks used.
    FRANKIES_FISHING_EMPORIUM("Frankie's Fishing Emporium", FRANKIE),
    // TODO: Co-op option + tradingsticks used.
    FREMENNIK_FISHMONGER("Fremennik Fishmonger", FISH_MONGER),
    // TODO: Co-op option + tradingsticks used.
    FREMENNIK_FUR_TRADER("Fremennik Fur Trader", FUR_TRADER_3948),
    // TODO: Co-op option + tradingsticks used.
    FRENITAS_COOKERY_SHOP("Frenita's Cookery Shop", FRENITA),
    // TODO: Co-op option + tradingsticks used.
    FRINCOS_FABULOUS_HERB_STORE("Frincos' Fabulous Herb Store", FRINCOS),
    // TODO: Co-op option + tradingsticks used.
    FUNCHS_FINE_GROCERIES("Funch's Fine Groceries", HECKEL_FUNCH),
    // TODO: Co-op option + tradingsticks used.
    GABOOTYS_TAI_BWO_WANNAI_COOPERATIVE("Gabooty's Tai Bwo Wannai Cooperative", GABOOTY),
    // TODO: Drinks option +trading sticks
    GABOOTYS_TAI_BWO_WANNAI_DRINKY_STORE("Gabooty's Tai Bwo Wannai Drinky Store", GABOOTY),
    // TODO: Verify existence + use marks of grace currency
    GAIUS_TWO_HANDED_STORE("Gaius' Two-Handed Shop", GAIUS),
    // TODO: Verify existence + use marks of grace currency
    GARDEN_CENTRE("Garden Centre", GARDEN_SUPPLIER),
    // TODO: Verify existence + use marks of grace currency
    GEM_TRADER("Gem Trader", NpcId.GEM_TRADER),
    // TODO: Verify existence + use marks of grace currency
    GENERAL_STORE_CANIFIS("General Store (Canifis)", FIDELIO),
    // TODO: Verify existence + use marks of grace currency
    GERRANTS_FISHY_BUSINESS("Gerrant's Fishy Business", GERRANT),
    // TODO: Verify existence + use marks of grace currency
    GIANNES_RESTAURANT("Gianne's Restaurant", GNOME_WAITER),
    // TODO: Verify existence + use marks of grace currency
    GRACES_GRACEFUL_CLOTHING("Grace's Graceful Clothing", GRACE),
    // TODO: Verify existence
    GRAND_TREE_GROCERIES("Grand Tree Groceries", HUDO),
    // TODO: Verify existence
    GREEN_GEMSTONE_GEMS("Green Gemstone Gems", HERVI),
    // TODO: Quest requirement
    GREENGROCER_OF_MISCELLANIA("Greengrocer of Miscellania", GREENGROCER_3689),
    // TODO: Quest requirement
    GRUDS_HERBLORE_STALL("Grud's Herblore Stall", OGRE_MERCHANT),
    // TODO: Quest requirement
    GRUMS_GOLD_EXCHANGE("Grum's Gold Exchange", GRUM),
    // TODO: Quest requirement
    GULLUCK_AND_SONS("Gulluck and Sons", GULLUCK),
    // TODO: Quest requirement
    GUNSLIK_ASSORTED_ITEMS("Gunslik's Assorted Items", GUNSLIK),
    // TODO: Quest requirement
    HABABS_CRAFTING_EMPORIUM("Hamab's Crafting Emporium", HAMAB),
    // TODO: Quest requirement
    HAPPY_HEROES_HEMPORIUM("Happy Heroes' H'Emporium", HELEMOS),
    // TODO: Quest requirement
    HARPOON_JOES_HOUSE_OF_RUM("Harpoon Joe's House of 'Rum'", JOE_4019),
    // TODO: Quest requirement
    HARRYS_FISHING_SHOP("Harry's Fishing Shop", HARRY),
    // TODO: Quest requirement
    HELMET_SHOP("Helmet Shop", PEKSA),
    // TODO: Quest requirement
    HENDORS_AWESOME_ORES("Hendor's Awesome Ores", HENDOR),
    // TODO: Quest requirement
    HICKTONS_ARCHERY_EMPORIUM("Hickton's Archery Emporium", HICKTON),
    // TODO: Quest requirement
    HORVIKS_SMITHY("Horvik's Armour Shop", HORVIK),
    // TODO: Quest requirement
    IANS_WILDERNESS_CAPE_SHOP("Ian's Wilderness Cape Shop", IAN),
    // TODO: Quest requirement
    IFABAS_GENERAL_STORE("Ifaba's General Store", IFABA),
    // TODO: Quest requirement
    ISLAND_FISHMONGER("Island Fishmonger", FISHMONGER_3688),
    // TODO: Quest requirement
    ISLAND_GREENGROCER("Island Greengrocer", GREENGROCER),
    // TODO: Quest requirement
    JAMILAS_CRAFT_STALL("Jamila's Craft Stall", JAMILA),
    JATIXS_HERBLORE_SHOP("Jatix's Herblore Shop", JATIX),
    JENNIFERS_GENERAL_FIELD_SUPPLIES("Jennifer's General Field Supplies", // TODO: Verify existence.
    305),
    // TODO: Verify existence.
    JIMINUAS_JUNGLE_STORE("Jiminua's Jungle Store", JIMINUA),
    // TODO: Verify existence.
    KARAMJA_GENERAL_STORE("Karamja General Store", SHOP_KEEPER_2825, SHOP_ASSISTANT_2826),
    // TODO: Verify existence.
    KARAMJA_WINES_SPIRITS_BEERS("Karamja Wines, Spirits, and Beers", ZAMBO),
    // TODO: Verify existence.
    KEEPA_KETTILONS_STORE("Keepa Kettilon's Store", KEEPA_KETTILON),
    // TODO: Verify existence.
    KELDAGRIM_STONEMASON("Keldagrim Stonemason", STONEMASON),
    // TODO: Verify existence.
    KELDAGRIMS_BEST_BREAD("Keldagrim's Best Bread", RANDIVOR),
    KENELMES_WARES("Kenelme's Wares", KENELME),
    KHAZARD_GENERAL_STORE("Khazard General Store", SHOP_KEEPER_2888),
    // KING_NARNODES_ROYAL_SEED_PODS("King Narnode's Royal Seed Pods", 8020),
    // TODO: Kjut's kebabs through dialogue
    LARRYS_WILDERNESS_CAPE_SHOP("Larry's Wilderness Cape Shop", LARRY_2197),
    LEENZ_GENERAL_SUPPLIES("Leenz's General Supplies", // TODO: Add npc
    LEENZ),
    // TODO: Add npc
    LEGENDS_GUILD_GENERAL_STORE("Legends Guild General Store", FIONELLA),
    // TODO: Add npc
    LEGENDS_GUILD_SHOP_OF_USEFUL_ITEMS("Legends Guild Shop of Useful Items", SIEGFRIED_ERKLE),
    // TODO: Add npc
    LEPRECHAUN_LARRYS_FARMING_SUPPLIES("Leprechaun Larry's Farming Supplies", TOOL_LEPRECHAUN_757),
    // TODO: Add npc
    LITTLE_MUNTYS_LITTLE_SHOP("Little Munty's Little Shop", MUNTY),
    // TODO: Add npc
    LITTLE_SHOP_OF_HORACE("Little Shop of Horace", HORACE),
    // TODO: Verify existence
    LLETYA_ARCHERY_SHOP("Lletya Archery Shop", 1481),
    // TODO: Verify existence
    LLETYA_FOOD_STORE("Lletya Food Store", 1482),
    // TODO: Verify existence
    LLETYA_GENERAL_STORE("Lletya General Store", 1477),
    // TODO: Verify existence
    LLETYA_SEAMSTRESS("Lletya Seamstress", 1478),
    // TODO: Verify existence
    LOGAVA_GRICOLLERS_COOKING_SUPPLIES("Logava Gricoller's Cooking Supplies", LOGAVA),
    // TODO: Verify existence
    LOUIES_ARMOURED_LEGS_BAZAAR("Louie's Armoured Legs Bazaar", LOUIE_LEGS),
    // TODO: Verify existence
    LOVECRAFTS_TACKLE("Lovecraft's Tackle", EZEKIAL_LOVECRAFT),
    // TODO: Verify existence
    LOWES_ARCHERY_EMPORIUM("Lowe's Archery Emporium", LOWE),
    // TODO: Verify existence
    LUMBRIDGE_GENERAL_STORE("Lumbridge General Store", SHOP_KEEPER, SHOP_ASSISTANT),
    // TODO: Verify existence
    LUNDAILS_ARENASIDE_RUNE_SHOP("Lundail's Arena-side Rune Shop", LUNDAIL),
    // TODO: Verify existence
    MAGE_ARENA_STAFFS("Mage Arena Staffs", CHAMBER_GUARDIAN),
    // TODO: Verify existence
    MAGIC_GUILD_STORE("Magic Guild Store (Mystic Robes)", WIZARD_SININA),
    // TODO: Verify existence
    MAGIC_GUID_RUNES("Magic Guild Store (Runes and Staves)", WIZARD_AKUTHA),
    // TODO: Verify existence
    IRKSOL("Irksol", NpcId.IRKSOL),
    // TODO: Verify existence
    JUKAT("Jukat", NpcId.JUKAT),
    // TODO: Verify existence
    MARTIN_THWAITS_LOST_AND_FOUND("Martin Thwait's Lost and Found", MARTIN_THWAIT),
    // TODO: Verify existence
    MILTOGS_LAMPS("Miltog's Lamps", MILTOG),
    // TODO: Add npc
    MISCELLANIAN_CLOTHES_SHOP("Miscellanian Clothes Shop", HALLA),
    // TODO: Verify existence
    MISCELLANIAN_FOOD_SHOP("Miscellanian Food Shop", OSVALD),
    // TODO: Verify existence
    MISCELLANIAN_GENERAL_STORE("Miscellanian General Store", FINN),
    // TODO: Verify existence
    MOON_CLAN_FINE_CLOTHES("Moon Clan Fine Clothes", RIMAE_SIRSALIS),
    // TODO: Verify existence
    MOON_CLAN_GENERAL_STORE("Moon Clan General Store", MELANA_MOONLANDER),
    // TODO: Verify existence
    MULTICANNON_PARTS("Multicannon Parts", NULODION),
    // TODO: Verify existence
    MYTHICAL_CAPE_STORE("Mythical Cape Store", JACK_8037),
    // TODO: Verify existence
    MYTHS_GUILD_ARMOURY("Myths' Guild Armoury", ERDAN),
    // TODO: Verify existence
    MYTHS_GUILD_HERBALIST("Myths' Guild Herbalist", PRIMULA),
    // TODO: Verify existence
    MYTHS_GUILD_WEAPONRY("Myths' Guild Weaponry", DIANA),
    // TODO: Verify existence
    NARDAH_GENERAL_STORE("Nardah General Store", KAZEMDE),
    // TODO: Verify existence
    NARDAH_HUNTER_SHOP("Nardah Hunter Shop", ARTIMEUS),
    // TODO: Verify existence
    NARDOKS_BONE_WEAPONS("Nardok's Bone Weapons", NARDOK),
    // TODO: Requires quest completion.
    NATHIFAS_BAKE_STALL("Nathifa's Bake Stall", NATHIFA),
    // TODO: Verify exitence
    NEILS_WILDERNESS_CAPE_SHOP("Neil's Wilderness Cape Shop", NEIL),
    // TODO: Verify exitence
    NEITIZNOT_SUPPLIES("Neitiznot supplies", JOFRIDR_MORDSTATTER),
    // TODO: Verify exitence
    NURMOFS_PICKAXE_SHOP("Nurmof's Pickaxe Shop", NURMOF),
    // TODO: Verify exitence
    OBLIS_GENERAL_STORE("Obli's General Store", OBLI),
    // TODO: Verify exitence
    OOBAPOHKS_JAVELIN_STORE("Oobapohk's Javelin Store", OOBAPOHK),
    // TODO: Verify exitence
    ORE_SELLER("Ore Seller", ORDAN),
    ORE_STORE("Ore store", HRING_HRING),
    OZIACHS_ARMOUR("Oziach's Armour", OZIACH),
    PERRYS_CHOP_CHOP_SHOP("Perry's Chop-chop Shop", // TODO: Open through dialogue, no trade option.
    PERRY),
    // TODO: Open through dialogue, no trade option.
    PICKAXE_IS_MINE("Pickaxe-Is-Mine", TATI),
    // TODO: Open through dialogue, no trade option.
    PIE_SHOP("Pie Shop", ROMILY_WEAKLAX),
    // TODO: Open through dialogue, no trade option.
    POLLNIVNEACH_GENERAL_STORE("Pollnivneach general store", MARKET_SELLER),
    // TODO: Open through dialogue, no trade option.
    PORT_PHASMATYS_GENERAL_STORE("Port Phasmatys General Store", GHOST_SHOPKEEPER),
    // TODO: Open through dialogue, no trade option.
    QUALITY_ARMOUYR_SHOP("Quality Armour Shop", SARO),
    // TODO: Open through dialogue, no trade option.
    QUALITY_WEAPONS_SHOP("Quality Weapons Shop", SANTIRI),
    // TODO: Open through dialogue, no trade option.
    QUARTERMASTERS_STORES("Quartermaster's Stores", QUARTERMASTER),
    // TODO: Open through dialogue, no trade op.
    RAETUL_AND_COS_CLOTHS_STORE("Raetul and Co's Cloth Store", RAETUL),
    // TODO two options, no trade. Additionally quest
    RANAELS_SUPER_SKIRT_STORE("Ranael's Super Skirt Store", RANAEL),
    // TODO two options, no trade. Additionally quest
    RASOLO_THE_WANDERING_MERCHANT("Rasolo the Wandering Merchant", RASOLO),
    // TODO two options, no trade. Additionally quest
    RAZMIRE_BUILDERS_MERCHANTS("Razmire Builders Merchants", RAZMIRE_KEELGAN),
    // TODO: Two options, no trade. Additionally quest
    RAZMIRE_GENERAL_STORE("Razmire General Store", RAZMIRE_KEELGAN),
    // TODO: Verify exitence
    REGATHS_WARES("Regath's Wares", REGATH),
    // TODO: Verify exitence
    RELDAKS_LEATHER_ARMOUR("Reldak's Leather Armour", RELDAK),
    RICHARDS_FARMING_SHOP("Richard's Farming shop", RICHARD),
    RICHARDS_WILDERNESS_CAPE_SHOP("Richard's Wilderness Cape Shop", // TODO: Uses Buy option instead.
    RICHARD_2200),
    // TODO: Uses Buy option instead.
    RIMMINGTON_GENERAL_STORE("Rimmington General Store", SHOP_KEEPER_2823, SHOP_ASSISTANT_2824),
    // TODO: Uses Buy option instead.
    ROKS_CHOCS_BOX("Rok's Chocs Box", ROKUH),
    // TODO: Uses Buy option instead.
    ROMMIKS_CRAFTY_SUPPLIES("Rommik's Crafty Supplies", ROMMIK),
    // TODO: Uses Buy option instead.
    RUFUS_MEAT_EMPORIUM("Rufus's Meat Emporium", RUFUS_6478),
    // TODO: Uses Buy option instead.
    SAMS_WILDERNESS_CAPE_SHOP("Sam's Wilderness Cape Shop", SAM),
    // TODO: Uses Buy option instead.
    SARAHS_FARMING_SHOP("Sarah's Farming Shop", SARAH),
    // TODO: Uses Buy option instead.
    SCAVVOS_RUNE_STORE("Scavvo's Rune Store", SCAVVO),
    // TODO: Uses Buy option instead.
    SEDDUS_ADVENTURER_STORE("Seddu's Adventurers' Store", SEDDU),
    // TODO: Uses Buy option instead.
    SHANTAY_PASS_SHOP("Shantay Pass Shop", SHANTAY),
    // TODO: Uses Buy option instead.
    SHOP_OF_DISTASTE("Shop of Distaste", FADLI),
    // TODO: Verify existence
    SILVER_COG_SILVER_STALL("Silver Cog Silver Stall", GULLDAMAR),
    SIMONS_WILDERNESS_CAPE_SHOP("Simon's Wilderness Cape Shop", SIMON),
    SKULGRIMENS_BATTLE_GEAR("Skulgrimen's Battle Gear", // TODO: Add npc, trading done through dialogue.
    SKULGRIMEN),
    // TODO: Add npc, trading done through dialogue.
    SMITHING_SMITHS_SHOP("Smithing Smith's Shop", SMITH),
    // TODO: Add npc, trading done through dialogue.
    SOLIHIBS_FOOD_STAFF("Solihib's Food Stall", SOLIHIB),
    // TODO: Add npc, trading done through dialogue.
    TAMAYUS_SPEAR_STALL("Tamayu's Spear Stall", TAMAYU),
    // TODO: Multiple npcs open it.
    THE_ASP_AND_SNAKE_BAR("The Asp & Snake Bar", 3535),
    // TODO: Another npc opens through dialogue.
    THE_BIG_HEIST_LODGE("The Big Heist Lodge", BARTENDER),
    // TODO: Another npc opens through dialogue.
    THE_DEEPER_LODE("The Deeper Lode", FUGGY),
    // TODO: Another npc opens through dialogue.
    THE_GOLDEN_FIELD("The Golden Field", RICHARD_6954),
    // TODO: Another npc opens through dialogue.
    THE_HAYMAKERS_ARMS("The Haymaker's Arms", GOLOVA),
    // TODO: Another npc opens through dialogue.
    THE_LIGHTHOUSE_STORE("The Lighthouse Store", JOSSIK),
    // TODO: Another npc opens through dialogue.
    THE_OTHER_INN("The Other Inn", MAMA),
    // TODO Another npc opens it too.
    THE_SHRIMP_AND_PARROT("The Shrimp and Parrot", ALFONSE_THE_WAITER),
    // TODO: Add npc + Quest req
    THE_SPICE_IS_RIGHT("The Spice is Right", EMBALMER),
    // TODO: Add npc + Quest req
    THESSELIAS_FINE_CLOTHES("Thessalia's Fine Clothes", 10477),
    // TODO: Add npc + Quest req
    THIRUS_URKAR_FINE_DYNAMITE_STORE("Thirus Urkar's Fine Dynamite Store", 7208),
    // TODO: Add npc + Quest req
    THYRIAS_WARES("Thyria's Wares", THYRIA),
    // TODO: Add npc + Quest req
    TIADECHES_KARAMBWAN_STALL("Tiadeche's Karambwan Stall", TIADECHE_4700),
    TOAD_AND_CHICKEN("Toad and Chicken", TOSTIG),
    TONYS_PIZZA_BASES("Tony's Pizza Bases", FAT_TONY),
    TOOTHYS_PICKAXES("Toothy's Pickaxes", // TODO: Opened through dialogue.
    TOOTHY),
    // TODO: Opened through dialogue.
    TRADER_STANS_TRADING_POST("Trader Stan's Trading Post", 1328, 1329, 1330, 1331, 1332, 1333, 1334, 9312, 9348, 9360, 9372, 9299, 9336, 9324),
    // TODO: Opened through dialogue.
    TRADER_SVENS_BLACK_MARKET_GOODS("Trader Sven's Black-market Goods", TRADER_SVEN),
    // TODO: Add npc
    TUTABS_MAGICAL_MARKET("Tutab's Magical Market", TUTAB),
    // TODO: Add npc
    TWO_FEET_CHARLEYS_FISH_SHOP("Two Feet Charley's Fish Shop", CHARLEY),
    // TODO: Add npc
    TYNANS_FISHING_SUPPLIES("Tynan's Fishing Supplies", TYNAN),
    // TODO: Add npc
    TZHAAR_HUR_LEKS_ORE_AND_GEM_STORE("TzHaar-Hur-Lek's Ore and Gem Store", TZHAARHURLEK),
    // TODO: Add npc
    TZHAAR_HUR_RINS_ORE_AND_GEM_STORE("TzHaar-Hur-Rin's Ore and Gem Store", TZHAARHURRIN),
    // TODO: Add npc
    TZHAAR_HUR_TELS_EQUIPMENT_STORE("TzHaar-Hur-Tel's Equipment Store", TZHAARHURTEL),
    // TODO: Add npc
    TZHAAR_MEJ_ROHS_RUNE_STORE("TzHaar-Mej-Roh's Rune Store", TZHAARMEJROH),
    // TODO: Add npc
    TZHAAR_HUR_ZALS_EQUIPMENT_STORE("TzHaar-Hur-Zal's Equipment Store", TZHAARHURZAL),
    // TODO: Add npc
    UGLUGS_STUFFIES("Uglug's Stuffsies", UGLUG_NAR),
    // TODO: Add npc
    VALAINES_SHOP_OF_CHAMPIONS("Valaine's Shop of Champions", VALAINE),
    // TODO: Add npc
    VANESSAS_FARMING_SHOP("Vanessa's Farming shop", VANESSA),
    // TODO: Add npc
    VANNAHS_FARM_STORE("Vannah's Farm Store", VANNAH),
    // TODO: Add npc
    VARROCK_GENERAL_STORE("Varrock General Store", SHOP_KEEPER_2815, SHOP_ASSISTANT_2816),
    // TODO: Add npc
    VARROCK_SWORDSHOP("Varrock Swordshop", SHOP_KEEPER_2884),
    // TODO: Add npc
    VERMUNDIS_CLOTHES_STALL("Vermundi's Clothes Stall", VERMUNDI),
    // TODO: Add npc
    VIGRS_WARHAMMERS("Vigr's Warhammers", VIGR),
    // TODO: Add npc
    VOID_KNIGHT_ARCHERY_STORE("Void Knight Archery Store", SQUIRE_1765),
    // TODO: Add npc
    VOID_KNIGHT_GENERAL_STORE("Void Knight General Store", SQUIRE_1768),
    // TODO: Add npc
    VOID_KNIGHT_MAGIC_STORE("Void Knight Magic Store", SQUIRE_1767),
    // TODO: Add npc
    WARRENS_FISH_MONGER("Warrens Fish Monger", FISH_MONGER_7912),
    // TODO: Add npc
    WARRENS_GENERAL_STORE("Warrens General Store", SHOP_KEEPER_7913),
    WARRIOR_GUILD_ARMOURY("Warrior Guild Armoury", ANTON),
    WARRIOR_GUILD_FOOD_SHOP("Warrior Guild Food Shop", LIDIO),
    WARRIOR_GUILD_POTION_SHOP("Warrior Guild Potion Shop", LILLY),
    WAYNES_CHAINS_CHAINMAIL_SPECIALIST("Wayne's Chains - Chainmail Specialist", WAYNE),
    WEAPONS_GALORE("Weapons galore", SKULI_MYRKA),
    WEST_ARDOUGNE_GENERAL_STORE("West Ardougne General Store", CHADWELL),
    WILLIAMS_WILDERNESS_CAPE_SHOP("William's Wilderness Cape Shop", WILLIAM),
    WYDINS_FOOD_STORE("Wydin's Food Store", WYDIN),
    YARSUL_PRODIGIUOUS_PICKAXES("Yarsul's Prodigious Pickaxes", YARSUL),
    YE_OLDE_TEA_SHOPPE("Ye Olde Tea Shoppe", 1302),
    YRSAS_ACCPUNTREMENTS("Yrsa's Accoutrements", YRSA_3933),
    ZANARIS_GENERAL_STORE("Zanaris General Store", FAIRY_SHOP_KEEPER),
    ZEKES_SUPERIOR_SCIMITARS("Zeke's Superior Scimitars", ZEKE),
    ZENESHAS_PLATE_MAIL_BODY_SHOP("Zenesha's Plate Mail Body Shop", ZENESHA),
    LEKE_QUO_KERAN("Mount Karuulm Weapon Shop", NpcId.LEKE_QUO_KERAN),
    SLAYER_EQUIPMENT("Slayer Equipment", TURAEL, MAZCHNA, VANNAKA, CHAELDAR, DURADEL, 490, NIEVE, KONAR_QUO_MATEN, 16064),
    ;

    ShopNPCHandler(final String shop, final int... npcIds) {
        this.npcIds = npcIds;
        this.shop = shop;
    }

    final int[] npcIds;

    final String shop;

    static final ShopNPCHandler[] values = values();

    static final Int2ObjectOpenHashMap<ShopNPCHandler> map = new Int2ObjectOpenHashMap<>();

    static {
        for (final ShopNPCHandler shop : values) {
            for (final int id : shop.npcIds) {
                map.put(id, shop);
            }
        }
    }
}

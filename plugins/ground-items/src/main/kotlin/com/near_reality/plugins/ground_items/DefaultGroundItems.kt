package com.near_reality.plugins.ground_items

import com.near_reality.scripts.ground_items.GroundItemSpawnScript
import com.zenyte.game.item.ItemId.ADDRESS_FORM
import com.zenyte.game.item.ItemId.AHABS_BEER
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.ANCESTRAL_KEY
import com.zenyte.game.item.ItemId.ASTRONOMY_BOOK
import com.zenyte.game.item.ItemId.BANANA
import com.zenyte.game.item.ItemId.BARREL_3216
import com.zenyte.game.item.ItemId.BEER
import com.zenyte.game.item.ItemId.BEER_GLASS
import com.zenyte.game.item.ItemId.BEER_TANKARD
import com.zenyte.game.item.ItemId.BIG_BONES
import com.zenyte.game.item.ItemId.BIG_FISHING_NET
import com.zenyte.game.item.ItemId.BLACK_SCIMITAR
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.BLUE_DRAGON_SCALE
import com.zenyte.game.item.ItemId.BODY_RUNE
import com.zenyte.game.item.ItemId.BONES
import com.zenyte.game.item.ItemId.BOOTS_OF_LIGHTNESS
import com.zenyte.game.item.ItemId.BOWL
import com.zenyte.game.item.ItemId.BRASS_KEY
import com.zenyte.game.item.ItemId.BRASS_NECKLACE
import com.zenyte.game.item.ItemId.BROKEN_ARROW
import com.zenyte.game.item.ItemId.BROKEN_GLASS_1469
import com.zenyte.game.item.ItemId.BRONZE_ARROW
import com.zenyte.game.item.ItemId.BRONZE_AXE
import com.zenyte.game.item.ItemId.BRONZE_BOLTS
import com.zenyte.game.item.ItemId.BRONZE_CHAINBODY
import com.zenyte.game.item.ItemId.BRONZE_DAGGER
import com.zenyte.game.item.ItemId.BRONZE_MACE
import com.zenyte.game.item.ItemId.BRONZE_MED_HELM
import com.zenyte.game.item.ItemId.BRONZE_PICKAXE
import com.zenyte.game.item.ItemId.BRONZE_SQ_SHIELD
import com.zenyte.game.item.ItemId.BRONZE_SWORD
import com.zenyte.game.item.ItemId.BROOCH
import com.zenyte.game.item.ItemId.BUCKET
import com.zenyte.game.item.ItemId.BUCKET_OF_MILK
import com.zenyte.game.item.ItemId.BUCKET_OF_WATER
import com.zenyte.game.item.ItemId.BURNT_FISH_357
import com.zenyte.game.item.ItemId.CABBAGE
import com.zenyte.game.item.ItemId.CAKE_TIN
import com.zenyte.game.item.ItemId.CATTLEPROD
import com.zenyte.game.item.ItemId.CAVE_NIGHTSHADE
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.CHEESE
import com.zenyte.game.item.ItemId.CHISEL
import com.zenyte.game.item.ItemId.COCKTAIL_GLASS
import com.zenyte.game.item.ItemId.COCKTAIL_SHAKER
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.COOKING_APPLE
import com.zenyte.game.item.ItemId.COSMIC_RUNE
import com.zenyte.game.item.ItemId.CRIMINALS_DAGGER
import com.zenyte.game.item.ItemId.CUP_OF_TEA_1978
import com.zenyte.game.item.ItemId.CUP_OF_TEA_4242
import com.zenyte.game.item.ItemId.DAMAGED_ARMOUR
import com.zenyte.game.item.ItemId.DARK_FISHING_BAIT
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.DOOGLE_LEAVES
import com.zenyte.game.item.ItemId.DWELLBERRIES
import com.zenyte.game.item.ItemId.EARTH_RUNE
import com.zenyte.game.item.ItemId.EGG
import com.zenyte.game.item.ItemId.EQUA_LEAVES
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.GARLIC
import com.zenyte.game.item.ItemId.GLASSBLOWING_BOOK
import com.zenyte.game.item.ItemId.GLASSBLOWING_PIPE
import com.zenyte.game.item.ItemId.GOLD_BAR
import com.zenyte.game.item.ItemId.GOLD_NECKLACE
import com.zenyte.game.item.ItemId.GOLD_ORE
import com.zenyte.game.item.ItemId.GUIDE_BOOK
import com.zenyte.game.item.ItemId.HALF_A_REDBERRY_PIE
import com.zenyte.game.item.ItemId.HAMMER
import com.zenyte.game.item.ItemId.HARPOON
import com.zenyte.game.item.ItemId.IMPLING_JAR_30129
import com.zenyte.game.item.ItemId.IMPLING_NET
import com.zenyte.game.item.ItemId.INCUBATOR_BLUEPRINT
import com.zenyte.game.item.ItemId.INSECT_REPELLENT
import com.zenyte.game.item.ItemId.IRON_AXE
import com.zenyte.game.item.ItemId.IRON_BAR
import com.zenyte.game.item.ItemId.IRON_DAGGER
import com.zenyte.game.item.ItemId.IRON_MACE
import com.zenyte.game.item.ItemId.IRON_MED_HELM
import com.zenyte.game.item.ItemId.IRON_PICKAXE
import com.zenyte.game.item.ItemId.JANGERBERRIES
import com.zenyte.game.item.ItemId.JUG
import com.zenyte.game.item.ItemId.JUG_OF_WATER
import com.zenyte.game.item.ItemId.KEG_OF_BEER
import com.zenyte.game.item.ItemId.KING_WORM
import com.zenyte.game.item.ItemId.KNIFE
import com.zenyte.game.item.ItemId.LEATHER_BODY
import com.zenyte.game.item.ItemId.LEATHER_BOOTS
import com.zenyte.game.item.ItemId.LEATHER_GLOVES
import com.zenyte.game.item.ItemId.LETTER_4615
import com.zenyte.game.item.ItemId.LIT_CANDLE
import com.zenyte.game.item.ItemId.LOBSTER_POT
import com.zenyte.game.item.ItemId.LOCKPICK
import com.zenyte.game.item.ItemId.LOGS
import com.zenyte.game.item.ItemId.LONGBOW
import com.zenyte.game.item.ItemId.MIND_RUNE
import com.zenyte.game.item.ItemId.MONKS_ROBE
import com.zenyte.game.item.ItemId.MONKS_ROBE_TOP
import com.zenyte.game.item.ItemId.NATURE_RUNE
import com.zenyte.game.item.ItemId.OAK_SHIELD
import com.zenyte.game.item.ItemId.PAPYRUS
import com.zenyte.game.item.ItemId.PICTURE
import com.zenyte.game.item.ItemId.PIE_DISH
import com.zenyte.game.item.ItemId.PIGEON_CAGE
import com.zenyte.game.item.ItemId.POT
import com.zenyte.game.item.ItemId.POTATO_CACTUS
import com.zenyte.game.item.ItemId.PUNGENT_POT
import com.zenyte.game.item.ItemId.PURPLE_DYE
import com.zenyte.game.item.ItemId.RED_SPIDERS_EGGS
import com.zenyte.game.item.ItemId.ROPE
import com.zenyte.game.item.ItemId.RUBY_RING
import com.zenyte.game.item.ItemId.SAPPHIRE
import com.zenyte.game.item.ItemId.SEAWEED
import com.zenyte.game.item.ItemId.SHOES
import com.zenyte.game.item.ItemId.SKULL
import com.zenyte.game.item.ItemId.SLICE_OF_CAKE
import com.zenyte.game.item.ItemId.SMALL_FISHING_NET
import com.zenyte.game.item.ItemId.SNAPE_GRASS
import com.zenyte.game.item.ItemId.SPADE
import com.zenyte.game.item.ItemId.STAFF_OF_EARTH
import com.zenyte.game.item.ItemId.STEEL_PLATEBODY
import com.zenyte.game.item.ItemId.SUPERANTIPOISON1
import com.zenyte.game.item.ItemId.SWAMP_TAR
import com.zenyte.game.item.ItemId.SWAMP_TOAD
import com.zenyte.game.item.ItemId.TANKARD
import com.zenyte.game.item.ItemId.THREAD
import com.zenyte.game.item.ItemId.TILE
import com.zenyte.game.item.ItemId.TOMATO
import com.zenyte.game.item.ItemId.WATER_RUNE
import com.zenyte.game.item.ItemId.WHITE_APRON
import com.zenyte.game.item.ItemId.WHITE_APRON_7957
import com.zenyte.game.item.ItemId.WHITE_BERRIES
import com.zenyte.game.item.ItemId.WINE_OF_ZAMORAK

class DefaultGroundItems : GroundItemSpawnScript() {
    init {
        BLUE_DRAGON_SCALE(1922, 8967, 1, 28)
        LOCKPICK(2615, 9571, 0, 28)
        BLUE_DRAGON_SCALE(1932, 8965, 1, 28)
        BLUE_DRAGON_SCALE(1923, 8970, 1, 28)
        BLUE_DRAGON_SCALE(1925, 8984, 1, 28)
        BLUE_DRAGON_SCALE(1927, 8990, 1, 28)
        BLUE_DRAGON_SCALE(1928, 8965, 1, 28)
        SNAPE_GRASS(1843, 3643, 0, 30)
        CAVE_NIGHTSHADE(2522, 3065, 0, 10)
        CAVE_NIGHTSHADE(2524, 3013, 0, 10)
        CHISEL(1764, 3863, 0, 10)
        BUCKET(1943, 4956, 0, 15)
        BLOOD_RUNE(3297, 3890, 0, 15)
        BUCKET(1631, 4712, 0, 10)
        HAMMER(1953, 4974, 0, 15)
        SNAPE_GRASS(1840, 3644, 0, 30)
        SNAPE_GRASS(1840, 3643, 0, 30)
        SNAPE_GRASS(1842, 3644, 0, 30)
        SNAPE_GRASS(1838, 3643, 0, 30)
        SNAPE_GRASS(1837, 3642, 0, 30)
        SNAPE_GRASS(1835, 3641, 0, 30)
        SNAPE_GRASS(1835, 3639, 0, 30)
        STEEL_PLATEBODY(3086, 3859, 0, 5)
        STAFF_OF_EARTH(3099, 3862, 0, 5)
        WHITE_BERRIES(3218, 3815, 0, 2)
        WHITE_BERRIES(3215, 3811, 0, 2)
        MONKS_ROBE_TOP(3128, 3477, 1, 30)
        MONKS_ROBE(3128, 3478, 1, 30)
        SNAPE_GRASS(2904, 3296, 0, 30)
        SNAPE_GRASS(2905, 3298, 0, 30)
        SNAPE_GRASS(2906, 3298, 0, 30)
        WINE_OF_ZAMORAK(2938, 3517, 1, 30)
        SAPPHIRE(3170, 3885, 0, 5)
        SPADE(3574, 3312, 0, 30)
        BRONZE_PICKAXE(3079, 3425, 0, 30)
        BEER(3443, 3441, 0, 30)
//LEATHER_GLOVES(3097, 3486, 0, 30)
//IRON_MACE(3111, 3517, 0, 30)
        BRASS_KEY(3131, 9862, 0, 30)
        BONES(3138, 9880, 0, 30)
        BONES(3141, 9879, 0, 30)
        BONES(3142, 9880, 0, 30)
        BONES(3143, 9878, 0, 30)
        COINS_995(8, 3167, 9897, 0, 30)
        COINS_995(3173, 9897, 0, 30)
        IRON_AXE(3162, 9888, 0, 30)
        COINS_995(5, 3173, 9897, 0, 30)
        RED_SPIDERS_EGGS(3177, 9880, 0, 10)
        RED_SPIDERS_EGGS(3179, 9881, 0, 10)
        RED_SPIDERS_EGGS(3185, 9888, 0, 10)
        KNIFE(3218, 9887, 0, 30)
        MIND_RUNE(3224, 9910, 0, 30)
        BODY_RUNE(3227, 9910, 0, 30)
        BRONZE_ARROW(3135, 9916, 0, 30)
        BRONZE_ARROW(3, 3130, 9903, 0, 30)
        RED_SPIDERS_EGGS(3128, 9956, 0, 10)
        RED_SPIDERS_EGGS(3129, 9954, 0, 10)
        RED_SPIDERS_EGGS(3126, 9958, 0, 10)
        RED_SPIDERS_EGGS(3118, 9948, 0, 10)
//CHOCOLATE_BAR(3095, 3462, 0, 30)
        DOOGLE_LEAVES(3156, 3401, 0, 30)
        DOOGLE_LEAVES(3152, 3401, 0, 30)
        DOOGLE_LEAVES(3152, 3399, 0, 30)
        DOOGLE_LEAVES(3151, 3400, 0, 30)
        THREAD(3286, 3491, 0, 30)
        LOGS(3303, 3502, 0, 30)
        LOGS(3303, 3503, 0, 30)
        LOGS(3310, 3504, 0, 30)
        LOGS(3311, 3503, 0, 30)
        LOGS(3302, 3507, 0, 30)
        LOGS(3301, 3507, 0, 30)
        LOGS(3299, 3508, 0, 30)
        LOGS(3302, 3510, 0, 30)
        LOGS(3299, 3511, 0, 30)
        JUG(3272, 3409, 0, 30)
        LOGS(3243, 3395, 0, 30)
        LOGS(3243, 3397, 0, 30)
        LEATHER_BODY(3244, 3398, 0, 30)
        POT(3232, 3399, 0, 30)
        COINS_995(3, 3195, 9834, 0, 30)
        GOLD_BAR(3192, 9822, 0, 30)
        GOLD_NECKLACE(3192, 9821, 0, 30)
        RUBY_RING(3196, 9822, 0, 30)
        GOLD_ORE(3195, 9821, 0, 30)
        COINS_995(4, 3195, 9820, 0, 30)
        COINS_995(66, 3191, 9821, 0, 30)
        BRASS_NECKLACE(3191, 9820, 0, 30)
        COINS_995(56, 3190, 9819, 0, 30)
        COINS_995(35, 3189, 9819, 0, 30)
        COINS_995(26, 3188, 9819, 0, 30)
        COINS_995(42, 3188, 9820, 0, 30)
        JUG(3211, 3212, 0, 30)
        POT(3209, 3214, 0, 30)
        BOWL(3208, 3214, 0, 30)
        KNIFE(3205, 3212, 0, 30)
        MIND_RUNE(3206, 3208, 0, 30)
        LEATHER_BOOTS(3210, 9615, 0, 30)
        LEATHER_BOOTS(3208, 9620, 0, 30)
        JUG(3211, 9625, 0, 30)
        KNIFE(3215, 9625, 0, 30)
        BUCKET(3216, 9625, 0, 30)
        CABBAGE(3217, 9622, 0, 30)
        BROOCH(3230, 9610, 0, 30)
        BRONZE_DAGGER(3213, 3216, 1, 30)
        LOGS(3205, 3224, 2, 30)
        LOGS(3205, 3226, 2, 30)
        LOGS(3209, 3224, 2, 30)
        KNIFE(3224, 3202, 0, 30)
        POT(3148, 3211, 0, 30)
        SWAMP_TAR(3164, 3169, 0, 30)
        SWAMP_TAR(3170, 3167, 0, 30)
        SWAMP_TAR(3173, 3166, 0, 30)
        SWAMP_TAR(3171, 3177, 0, 30)
        SWAMP_TAR(3173, 3178, 0, 30)
        SWAMP_TAR(3164, 3180, 0, 30)
        SWAMP_TAR(3164, 3187, 0, 30)
        SWAMP_TAR(3171, 3191, 0, 30)
        SWAMP_TAR(3179, 3190, 0, 30)
        SWAMP_TAR(3181, 3193, 0, 30)
        SWAMP_TAR(3193, 3181, 0, 30)
        SWAMP_TAR(3194, 3168, 0, 30)
        SWAMP_TAR(3191, 3162, 0, 30)
        SWAMP_TAR(3189, 3163, 0, 30)
        SMALL_FISHING_NET(3244, 3159, 0, 30)
        SMALL_FISHING_NET(3245, 3155, 0, 30)
        LEATHER_GLOVES(3148, 3177, 0, 30)
        LEATHER_BOOTS(3111, 3159, 0, 30)
        LOGS(3106, 3160, 0, 30)
        LOGS(3106, 3159, 0, 30)
        LOGS(3105, 3159, 0, 30)
        LEATHER_BOOTS(3112, 3155, 1, 30)
        IRON_DAGGER(3248, 3245, 0, 30)
        EGG(3229, 3297, 0, 30)
        EGG(3226, 3301, 0, 30)
        TOMATO(3085, 3261, 0, 30)
        CHEESE(3083, 3260, 0, 30)
        LOGS(3089, 3266, 0, 30)
        LOGS(3089, 3265, 0, 30)
        AHABS_BEER(3049, 3257, 0, 30)
        WHITE_APRON_7957(3016, 3229, 0, 30)
        WHITE_APRON(3009, 3204, 0, 30)
        BANANA(3009, 3207, 0, 30)
        BUCKET(3026, 3289, 0, 30)
        EGG(3017, 3295, 0, 30)
        EGG(3016, 3295, 0, 30)
        BRONZE_PICKAXE(3009, 3342, 0, 30)
        COINS_995(3003, 9801, 0, 30)
        COINS_995(3001, 9798, 0, 30)
        BRONZE_CHAINBODY(2985, 9817, 0, 30)
        PIE_DISH(2993, 9847, 0, 30)
        WINE_OF_ZAMORAK(2930, 3515, 0, 30)
        CUP_OF_TEA_1978(2903, 3441, 0, 30)
        CUP_OF_TEA_1978(2886, 3412, 0, 30)
        KING_WORM(2896, 3414, 0, 30)
        SWAMP_TOAD(2908, 3410, 0, 30)
        SWAMP_TOAD(2903, 3400, 0, 30)
        SWAMP_TOAD(2907, 3410, 0, 30)
        BLUE_DRAGON_SCALE(2902, 9806, 0, 30)
        BLUE_DRAGON_SCALE(2908, 9805, 0, 30)
        BLUE_DRAGON_SCALE(2910, 9809, 0, 30)
        BLUE_DRAGON_SCALE(2905, 9801, 0, 30)
        BLUE_DRAGON_SCALE(2906, 9796, 0, 30)
        BLUE_DRAGON_SCALE(2912, 9786, 0, 30)
        BLUE_DRAGON_SCALE(2910, 9782, 0, 30)
        BLUE_DRAGON_SCALE(2905, 9778, 0, 30)
        BLUE_DRAGON_SCALE(2902, 9784, 0, 30)
        BLUE_DRAGON_SCALE(2908, 9789, 0, 30)
        BLUE_DRAGON_SCALE(2912, 9786, 0, 30)
        LOGS(2959, 3205, 0, 30)
        LOGS(2958, 3205, 0, 30)
        BRONZE_ARROW(2957, 3205, 0, 30)
        BRONZE_PICKAXE(2963, 3216, 0, 30)
        INSECT_REPELLENT(2807, 3450, 0, 30)
        BUCKET(2766, 3441, 0, 30)
        BUCKET_OF_WATER(2823, 3449, 0, 30)
        BUCKET_OF_WATER(2820, 3452, 0, 30)
        BURNT_FISH_357(2820, 3453, 0, 30)
        PIE_DISH(2820, 3455, 0, 30)
        KNIFE(2820, 3450, 0, 30)
        KNIFE(2700, 3407, 0, 30)
        GARLIC(2714, 3478, 0, 30)
        KNIFE(2704, 3475, 0, 30)
        DWELLBERRIES(2667, 3494, 0, 30)
        DWELLBERRIES(2645, 3498, 0, 30)
        DWELLBERRIES(2633, 3496, 0, 30)
        DWELLBERRIES(2638, 3479, 0, 30)
        CRIMINALS_DAGGER(2746, 3578, 0, 30)
        PUNGENT_POT(2747, 3579, 0, 30)
        ADDRESS_FORM(2739, 3581, 1, 30)
        BUCKET_OF_MILK(2685, 3683, 0, 30)
        KEG_OF_BEER(2660, 3676, 0, 30)
        BEER_TANKARD(2658, 3676, 0, 30)
        TANKARD(2659, 3676, 0, 30)
        IRON_PICKAXE(2672, 3728, 0, 30)
        KNIFE(2681, 3717, 0, 30)
        BONES(2675, 3719, 0, 30)
        BONES(2682, 3729, 0, 30)
        BRONZE_AXE(2678, 3731, 0, 30)
        SAPPHIRE(2679, 3740, 0, 30)
        NATURE_RUNE(2672, 3738, 0, 30)
        NATURE_RUNE(2671, 3737, 0, 30)
        SEAWEED(2693, 3727, 0, 30)
        SEAWEED(2701, 3731, 0, 30)
        SEAWEED(2699, 3729, 0, 30)
        SEAWEED(2708, 3728, 0, 30)
        SEAWEED(2712, 3732, 0, 30)
        SEAWEED(2714, 3733, 0, 30)
        SEAWEED(2719, 3733, 0, 30)
        SEAWEED(2721, 3731, 0, 30)
        SEAWEED(2725, 3731, 0, 30)
        DOOGLE_LEAVES(2738, 3642, 0, 30)
        DOOGLE_LEAVES(2736, 3642, 0, 30)
        DOOGLE_LEAVES(2736, 3640, 0, 30)
        DOOGLE_LEAVES(2734, 3638, 0, 30)
        DOOGLE_LEAVES(2737, 3637, 0, 30)
        DOOGLE_LEAVES(2735, 3636, 0, 30)
        DOOGLE_LEAVES(2739, 3634, 0, 30)
        DOOGLE_LEAVES(2742, 3636, 0, 30)
        DOOGLE_LEAVES(2742, 3638, 0, 30)
        DOOGLE_LEAVES(2744, 3638, 0, 30)
        DOOGLE_LEAVES(2741, 3639, 0, 30)
        DOOGLE_LEAVES(2742, 3641, 0, 30)
        DOOGLE_LEAVES(2742, 3643, 0, 30)
        BEER(2525, 3365, 0, 30)
        BEER(2517, 3378, 0, 30)
        BEER(2521, 3379, 0, 30)
        BARREL_3216(2487, 3371, 0, 30)
        COCKTAIL_GLASS(2489, 3489, 1, 30)
        COCKTAIL_GLASS(2483, 3482, 1, 30)
        COCKTAIL_SHAKER(2449, 3510, 1, 30)
        SWAMP_TOAD(2421, 3509, 0, 30)
        SWAMP_TOAD(2417, 3508, 0, 30)
        SWAMP_TOAD(2418, 3511, 0, 30)
        SWAMP_TOAD(2417, 3512, 0, 30)
        SWAMP_TOAD(2416, 3512, 0, 30)
        SWAMP_TOAD(2417, 3515, 0, 30)
        SWAMP_TOAD(2417, 3516, 0, 30)
        SWAMP_TOAD(2418, 3517, 0, 30)
        SWAMP_TOAD(2421, 3519, 0, 30)
        SWAMP_TOAD(2424, 3517, 0, 30)
        SWAMP_TOAD(2424, 3514, 0, 30)
        SWAMP_TOAD(2428, 3510, 0, 30)
        SWAMP_TOAD(2415, 3517, 0, 30)
        SWAMP_TOAD(2412, 3519, 0, 30)
        SWAMP_TOAD(2407, 3516, 0, 30)
        SWAMP_TOAD(2411, 3512, 0, 30)
        SWAMP_TOAD(2412, 3512, 0, 30)
        KING_WORM(2419, 3511, 0, 30)
        KING_WORM(2415, 3511, 0, 30)
        KING_WORM(2415, 3517, 0, 30)
        KING_WORM(2414, 3518, 0, 30)
        KING_WORM(2427, 3508, 0, 30)
        KING_WORM(2426, 3507, 0, 30)
        KING_WORM(2425, 3515, 0, 30)
        KING_WORM(2414, 3510, 0, 30)
        KING_WORM(2408, 3515, 0, 30)
        KING_WORM(2408, 3518, 0, 30)
        SWAMP_TOAD(2382, 3412, 0, 30)
        SWAMP_TOAD(2381, 3414, 0, 30)
        SWAMP_TOAD(2384, 3409, 0, 30)
        SWAMP_TOAD(2398, 3406, 0, 30)
        SWAMP_TOAD(2403, 3402, 0, 30)
        SWAMP_TOAD(2408, 3408, 0, 30)
        SWAMP_TOAD(2407, 3411, 0, 30)
        GLASSBLOWING_PIPE(2652, 3454, 0, 30)
        OAK_SHIELD(2656, 3424, 0, 30)
        BROKEN_ARROW(2670, 3437, 0, 30)
        BROKEN_ARROW(2657, 3424, 0, 30)
        BROKEN_ARROW(2670, 3424, 0, 30)
        BROKEN_ARROW(2665, 3424, 0, 30)
        BROKEN_ARROW(2671, 3420, 0, 30)
        BROKEN_ARROW(2673, 3428, 0, 30)
        BROKEN_ARROW(2676, 3431, 0, 30)
        BROKEN_ARROW(2677, 3425, 0, 30)
        BROKEN_ARROW(2677, 3422, 0, 30)
        BRONZE_ARROW(2680, 3424, 0, 30)
        BRONZE_ARROW(2678, 3426, 0, 30)
        DAMAGED_ARMOUR(2679, 3425, 0, 30)
        LONGBOW(2678, 3427, 0, 30)
        CATTLEPROD(2604, 3357, 0, 30)
        LOBSTER_POT(2608, 3397, 0, 30)
        HARPOON(2605, 3396, 0, 30)
        BIG_FISHING_NET(2605, 3395, 0, 30)
        BOWL(2667, 3387, 0, 30)
        COOKING_APPLE(2645, 3363, 0, 30)
        HAMMER(2684, 3318, 0, 30)
        CHISEL(2683, 3318, 0, 30)
        IRON_MACE(2675, 3299, 0, 30)
        GUIDE_BOOK(2638, 3292, 0, 30)
        GARLIC(2646, 3299, 0, 30)
        PIGEON_CAGE(2618, 3325, 0, 30)
        PIGEON_CAGE(2618, 3324, 0, 30)
        PIGEON_CAGE(2618, 3323, 0, 30)
        BUCKET(2616, 3355, 0, 30)
        LIT_CANDLE(2598, 3211, 0, 30)
        BLACK_SCIMITAR(2572, 3284, 1, 30)
        PICTURE(2576, 3334, 0, 30)
        BUCKET(2564, 3332, 0, 30)
        SPADE(2565, 3330, 0, 30)
        PURPLE_DYE(2563, 3261, 0, 30)
        PAPYRUS(2724, 3367, 0, 30)
        IRON_MED_HELM(2540, 3249, 0, 30)
        BONES(2456, 3234, 0, 30)
        BONES(2456, 3230, 0, 30)
        BONES(2453, 3230, 0, 30)
        BONES(2466, 3226, 0, 30)
        BONES(2459, 3222, 0, 30)
        BONES(2447, 3224, 0, 30)
        BONES(2455, 3220, 0, 30)
        BONES(2461, 3230, 0, 30)
        BONES(2470, 3223, 0, 30)
        BONES(2470, 3217, 0, 30)
        LIT_CANDLE(2547, 3114, 0, 30)
        CUP_OF_TEA_4242(2593, 3103, 1, 30)
        JUG_OF_WATER(2568, 3102, 0, 30)
        JANGERBERRIES(2510, 3090, 0, 30)
        JANGERBERRIES(2516, 3086, 0, 30)
        JANGERBERRIES(2512, 3080, 0, 30)
        JANGERBERRIES(2517, 3082, 0, 30)
        BONES(2516, 3084, 0, 30)
        BONES(2513, 3085, 0, 30)
        BONES(2510, 3088, 0, 30)
        BONES(2508, 3086, 0, 30)
        BONES(2509, 3084, 0, 30)
        BONES(2510, 3080, 0, 30)
        BONES(2511, 3114, 0, 30)
        BONES(2507, 3116, 0, 30)
        BONES(2505, 3114, 0, 30)
        BONES(2503, 3118, 0, 30)
        SUPERANTIPOISON1(2467, 3176, 0, 10)
        GLASSBLOWING_BOOK(2438, 3185, 0, 30)
        ASTRONOMY_BOOK(2438, 3187, 0, 30)
        DEATH_RUNE(2500, 2967, 0, 30)
        BONES(2508, 2982, 0, 30)
        BONES(2512, 2982, 0, 30)
        BONES(2522, 2983, 0, 30)
        BONES(2542, 2983, 0, 30)
        BONES(2552, 2984, 0, 30)
        BONES(2552, 2986, 0, 30)
        DOOGLE_LEAVES(2565, 2972, 0, 30)
        DOOGLE_LEAVES(2563, 2971, 0, 30)
        DOOGLE_LEAVES(2562, 2973, 0, 30)
        DOOGLE_LEAVES(2560, 2971, 0, 30)
        TOMATO(2584, 2966, 0, 30)
        BONES(2603, 2968, 0, 30)
        BONES(2603, 2964, 0, 30)
        BONES(2607, 2964, 0, 30)
        BONES(2601, 2959, 0, 30)
        EQUA_LEAVES(2639, 2952, 0, 30)
        EQUA_LEAVES(2649, 2956, 0, 30)
        EQUA_LEAVES(2648, 2963, 0, 30)
        ROPE(2571, 3026, 0, 30)
        BONES(2571, 3030, 0, 30)
        BONES(2577, 3029, 0, 30)
        BONES(2574, 3035, 0, 30)
        BONES(2571, 3036, 0, 30)
        BONES(2548, 3044, 0, 30)
        BONES(2549, 3043, 0, 30)
        BONES(2551, 3042, 0, 30)
        BONES(2545, 3031, 0, 30)
        BONES(2542, 3027, 0, 30)
        BONES(2541, 3023, 0, 30)
        BONES(2512, 3036, 0, 30)
        BONES(2512, 3021, 0, 30)
        BONES(2513, 3021, 0, 30)
        BONES(2513, 3020, 0, 30)
        BONES(2514, 3020, 0, 30)
        BONES(2515, 3020, 0, 30)
        BROKEN_ARROW(2576, 3051, 0, 30)
        BROKEN_ARROW(2573, 3056, 0, 30)
        BONES(2499, 2958, 0, 30)
        BONES(2496, 2954, 0, 30)
        BONES(2506, 2952, 0, 30)
        BONES(2485, 2926, 0, 30)
        BONES(2490, 2929, 0, 30)
        BONES(2493, 2926, 0, 30)
        BONES(2489, 2921, 0, 30)
        BRONZE_SWORD(2483, 2929, 0, 30)
        SKULL(2485, 2928, 0, 30)
        BUCKET(2371, 3127, 0, 30)
        BUCKET(2371, 3128, 0, 30)
        BUCKET(2428, 3080, 0, 30)
        BUCKET(2428, 3079, 0, 30)
        SKULL(2978, 3531, 0, 30)
        SKULL(2977, 3529, 0, 30)
        TILE(3002, 3698, 0, 30)
        TILE(2993, 3691, 0, 30)
        TILE(2983, 3697, 0, 30)
        TILE(2978, 3704, 0, 30)
        TILE(2976, 3682, 0, 30)
        TILE(2987, 3684, 0, 30)
        TILE(2957, 3697, 0, 30)
        TILE(2959, 3697, 0, 30)
        TILE(2955, 3700, 0, 30)
        TILE(2956, 3702, 0, 30)
        TILE(2963, 3706, 0, 30)
        CAKE_TIN(2987, 3686, 0, 30)
        COINS_995(4, 2979, 3675, 0, 30)
        COINS_995(4, 2985, 3675, 0, 30)
        COINS_995(4, 2990, 3675, 0, 30)
        SMALL_FISHING_NET(2999, 3701, 0, 30)
        BONES(2999, 3702, 0, 30)
        BONES(2969, 3689, 0, 30)
        DARK_FISHING_BAIT(2998, 3705, 0, 30)
        DARK_FISHING_BAIT(2988, 3683, 0, 30)
        CHEESE(3039, 3706, 0, 30)
        TOMATO(3039, 3707, 0, 30)
        COINS_995(2, 3099, 3557, 0, 30)
        COINS_995(2, 3103, 3548, 0, 30)
        COINS_995(2, 3098, 3564, 0, 30)
        COINS_995(2, 3097, 3578, 0, 30)
        COINS_995(2, 3108, 3534, 0, 30)
        BRONZE_ARROW(3098, 3596, 0, 30)
        BRONZE_ARROW(2, 3098, 3600, 0, 30)
        BRONZE_ARROW(2, 3101, 3606, 0, 30)
        BRONZE_ARROW(2, 3105, 3607, 0, 30)
        BRONZE_ARROW(2, 3103, 3600, 0, 30)
        BRONZE_ARROW(2, 3101, 3592, 0, 30)
        BRONZE_ARROW(2, 3109, 3604, 0, 30)
        BRONZE_ARROW(3110, 3611, 0, 30)
        BODY_RUNE(7, 3021, 3637, 0, 50)
        CHAOS_RUNE(2, 3021, 3640, 0, 50)
        MIND_RUNE(5, 3023, 3640, 0, 50)
        FIRE_RUNE(3, 3026, 3637, 0, 50)
        WATER_RUNE(3, 3028, 3637, 0, 50)
        AIR_RUNE(3, 3030, 3637, 0, 50)
        EARTH_RUNE(3, 3032, 3637, 0, 50)
        COINS_995(2, 3099, 3572, 0, 30)
        BODY_RUNE(3229, 3566, 0, 30)
        BODY_RUNE(3232, 3574, 0, 30)
        BODY_RUNE(3223, 3583, 0, 30)
        GOLD_ORE(3292, 3933, 0, 30)
        GOLD_ORE(3290, 3928, 0, 30)
        GOLD_ORE(3289, 3926, 0, 30)
        GOLD_ORE(3287, 3932, 1, 30)
        BRONZE_SQ_SHIELD(3294, 3935, 1, 30)
        GOLD_ORE(3283, 3936, 3, 30)
        IRON_DAGGER(3279, 3938, 3, 30)
        HALF_A_REDBERRY_PIE(3042, 3952, 0, 30)
        KNIFE(3106, 3956, 0, 30)
        BONES(3004, 3923, 0, 30)
        BONES(3002, 3922, 0, 30)
        BONES(2995, 3922, 0, 30)
        BONES(2994, 3924, 0, 30)
        BONES(2992, 3920, 0, 30)
        COSMIC_RUNE(2947, 3898, 0, 30)
        WATER_RUNE(2960, 3899, 0, 30)
        WINE_OF_ZAMORAK(2950, 3824, 0, 15)
        BONES(2978, 3763, 0, 30)
        BONES(2980, 3764, 0, 30)
        BONES(2962, 3697, 0, 30)
        COINS_995(2990, 3675, 0, 30)
        COINS_995(2985, 3675, 0, 30)
        BRONZE_BOLTS(3101, 3702, 0, 30)
        BRONZE_BOLTS(3097, 3695, 0, 30)
        BRONZE_BOLTS(3105, 3694, 0, 30)
        BRONZE_BOLTS(3109, 3697, 0, 30)
        BRONZE_BOLTS(3116, 3701, 0, 30)
        BRONZE_BOLTS(3109, 3691, 0, 30)
        BRONZE_BOLTS(3114, 3690, 0, 30)
        BRONZE_BOLTS(3112, 3682, 0, 30)
        BRONZE_BOLTS(3105, 3679, 0, 30)
        BRONZE_BOLTS(3099, 3684, 0, 30)
        BRONZE_BOLTS(3099, 3688, 0, 30)
        BONES(3103, 3688, 0, 30)
        BONES(3112, 3686, 0, 30)
        BONES(3112, 3695, 0, 30)
        BONES(3117, 3696, 0, 30)
        BONES(3100, 3697, 0, 30)
        IRON_BAR(3111, 3657, 0, 30)
        BUCKET(3307, 3195, 0, 30)
        LEATHER_BOOTS(3302, 3190, 0, 30)
        CABBAGE(3285, 3175, 0, 30)
        JUG_OF_WATER(3302, 3170, 0, 30)
        BRONZE_MACE(3320, 3137, 0, 30)
        FIRE_RUNE(3, 3304, 3311, 0, 50)
        WATER_RUNE(4, 3298, 3316, 0, 50)
        BODY_RUNE(3290, 3191, 1, 50)
        BRONZE_DAGGER(3407, 3184, 0, 30)
        BRONZE_MED_HELM(3408, 3184, 0, 30)
        BUCKET(3346, 2966, 0, 30)
        BUCKET(3351, 2963, 0, 30)
        BEER_GLASS(3358, 2955, 0, 30)
        SHOES(3439, 2913, 0, 1)
        ANCESTRAL_KEY(3432, 2928, 0, 30)
        LETTER_4615(3479, 3092, 0, 30)
        SNAPE_GRASS(2540, 3765, 0, 30)
        SNAPE_GRASS(2542, 3765, 0, 30)
        SNAPE_GRASS(2545, 3764, 0, 30)
        SNAPE_GRASS(2552, 3757, 0, 30)
        SNAPE_GRASS(2553, 3754, 0, 30)
        SNAPE_GRASS(2553, 3751, 0, 30)
        SNAPE_GRASS(2533, 3762, 0, 30)
        SNAPE_GRASS(2516, 3761, 0, 30)
        SNAPE_GRASS(2502, 3756, 0, 30)
        SNAPE_GRASS(2504, 3760, 0, 30)
        SNAPE_GRASS(2503, 3759, 0, 30)
        SNAPE_GRASS(2502, 3757, 0, 30)
        SNAPE_GRASS(2503, 3754, 0, 30)
        SNAPE_GRASS(2505, 3753, 0, 30)
        SNAPE_GRASS(2525, 3751, 0, 30)
        SNAPE_GRASS(2527, 3746, 0, 30)
        SNAPE_GRASS(2529, 3735, 0, 30)
        SNAPE_GRASS(2531, 3736, 0, 30)
        SNAPE_GRASS(2528, 3716, 0, 30)
        SNAPE_GRASS(2502, 3725, 0, 30)
        SNAPE_GRASS(2503, 3725, 0, 30)
        SNAPE_GRASS(2501, 3728, 0, 30)
        SNAPE_GRASS(2501, 3728, 0, 30)
        SNAPE_GRASS(2501, 3729, 0, 30)
        SNAPE_GRASS(2501, 3731, 0, 30)
        SMALL_FISHING_NET(2521, 4768, 0, 5)
        SMALL_FISHING_NET(2523, 4766, 0, 5)
        SMALL_FISHING_NET(2531, 4869, 0, 5)
        SMALL_FISHING_NET(2535, 4770, 0, 5)
        SMALL_FISHING_NET(2541, 4778, 0, 5)
        SMALL_FISHING_NET(2538, 4775, 0, 5)
        SMALL_FISHING_NET(2536, 4785, 0, 5)
        SMALL_FISHING_NET(2534, 4783, 0, 5)
        SMALL_FISHING_NET(2528, 4787, 0, 5)
        SMALL_FISHING_NET(2524, 4787, 0, 5)
        SMALL_FISHING_NET(2518, 4782, 0, 5)
        SMALL_FISHING_NET(2514, 4781, 0, 5)
        SMALL_FISHING_NET(2512, 4775, 0, 5)
        SMALL_FISHING_NET(2515, 4773, 0, 5)
        RED_SPIDERS_EGGS(3119, 9949, 0, 10)
        RED_SPIDERS_EGGS(3117, 9951, 0, 10)
        BOOTS_OF_LIGHTNESS(2654, 9767, 0, 30)
        KNIFE(2653, 9767, 0, 30)
//BUCKET(3110, 3506, 0, 50)
        POTATO_CACTUS(3460, 9484, 2, 30)
        POTATO_CACTUS(3461, 9480, 2, 30)
        POTATO_CACTUS(3465, 9477, 2, 30)
        POTATO_CACTUS(3486, 9517, 0, 30)
        POTATO_CACTUS(3474, 9509, 0, 30)
        POTATO_CACTUS(3470, 9502, 0, 30)
        POTATO_CACTUS(3467, 9493, 0, 30)
        POTATO_CACTUS(3479, 9483, 0, 30)
        BROKEN_GLASS_1469(3152, 3638, 0, 30)
        BROKEN_GLASS_1469(3150, 3640, 0, 30)
        BROKEN_GLASS_1469(3151, 3644, 0, 30)
        BROKEN_GLASS_1469(3154, 3646, 0, 30)
        BUCKET_OF_WATER(3148, 3642, 0, 30)
        BEER(3148, 3645, 0, 30)
        CHISEL(3402, 7804, 0, 30)
        IRON_PICKAXE(3396, 7804, 0, 30)
        SLICE_OF_CAKE(2702, 3205, 0, 45)
        SLICE_OF_CAKE(2700, 3211, 0, 45)
        INCUBATOR_BLUEPRINT(2195, 4400, 1, 1)
        IMPLING_JAR_30129(2206, 4390, 1, 1)
        IMPLING_JAR_30129(2205, 4391, 1, 1)
        IMPLING_NET(2213, 4399, 1, 1)
        RED_SPIDERS_EGGS(1829, 9966, 0, 45)
        RED_SPIDERS_EGGS(1829, 9963, 0, 45)
        RED_SPIDERS_EGGS(1829, 9959, 0, 45)
        RED_SPIDERS_EGGS(1830, 9954, 0, 45)
        RED_SPIDERS_EGGS(1831, 9950, 0, 45)
        RED_SPIDERS_EGGS(1848, 9960, 0, 45)
        RED_SPIDERS_EGGS(1846, 9965, 0, 45)
        RED_SPIDERS_EGGS(1841, 9960, 0, 45)
        RED_SPIDERS_EGGS(1839, 9956, 0, 45)
        RED_SPIDERS_EGGS(1837, 9958, 0, 45)
        RED_SPIDERS_EGGS(1937, 9961, 0, 45)
        KNIFE(1366, 3635, 0, 1)
        BIG_BONES(2956, 3814, 0, 30)
        BIG_BONES(2953, 3815, 0, 30)
        BIG_BONES(2956, 3811, 0, 30)
        BIG_BONES(2965, 3819, 0, 30)
        BIG_BONES(2960, 3823, 0, 30)
        BIG_BONES(2956, 3827, 0, 30)
        BIG_BONES(2954, 3825, 0, 30)
        BIG_BONES(2950, 3827, 0, 30)
    }
}
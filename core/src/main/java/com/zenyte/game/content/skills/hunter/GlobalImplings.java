package com.zenyte.game.content.skills.hunter;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.skills.hunter.node.Impling;
import com.zenyte.game.content.skills.hunter.npc.ImplingNPC;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.plugins.events.ServerLaunchEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GlobalImplings {
    private static final Logger log = LoggerFactory.getLogger(GlobalImplings.class);
    private static final Location[] implingSpawns = {new Location(3088, 3481, 0), new Location(3090, 3457, 0),
            new Location(3133, 3475, 0), new Location(3164, 3517, 0), new Location(3217, 3513, 0), new Location(3260,
            3505, 0), new Location(3277, 3476, 0), new Location(3283, 3452, 0), new Location(3285, 3421, 0),
            new Location(3286, 3346, 0), new Location(3243, 3367, 0), new Location(3128, 3431, 0), new Location(3168,
            3386, 0), new Location(3142, 3349, 0), new Location(3097, 3308, 0), new Location(2985, 3289, 0),
            new Location(3052, 3320, 0), new Location(3075, 3357, 0), new Location(3067, 3395, 0), new Location(3047,
            3441, 0), new Location(2974, 3491, 0), new Location(2963, 3429, 0), new Location(3118, 3507, 0),
            new Location(2954, 3271, 0), new Location(2998, 3210, 0), new Location(3148, 3244, 0), new Location(3178,
            3216, 0), new Location(3174, 3331, 0), new Location(3255, 3288, 0), new Location(3290, 3256, 0),
            new Location(3331, 3307, 0), new Location(3311, 3404, 0), new Location(3353, 3485, 0), new Location(3434,
            3506, 0), new Location(3470, 3416, 0), new Location(3518, 3441, 0), new Location(3577, 3487, 0),
            new Location(3624, 3504, 0), new Location(3549, 3528, 0), new Location(3428, 3530, 0), new Location(3500,
            3298, 0), new Location(3505, 3213, 0), new Location(3419, 3283, 0), new Location(3674, 3310, 0),
            new Location(3757, 3395, 0), new Location(3661, 3494, 0), new Location(3290, 3183, 0), new Location(3329,
            3190, 0), new Location(3264, 3101, 0), new Location(3337, 3056, 0), new Location(3220, 3010, 0),
            new Location(3228, 2848, 0), new Location(3352, 2886, 0), new Location(3424, 3008, 0), new Location(3432,
            3104, 0), new Location(3707, 3041, 0), new Location(3795, 3032, 0), new Location(3747, 2961, 0),
            new Location(3685, 3018, 0), new Location(3211, 3152, 0), new Location(3153, 3190, 0), new Location(3113,
            3196, 0), new Location(3072, 3270, 0), new Location(2995, 3143, 0), new Location(2928, 3223, 0),
            new Location(2927, 3307, 0), new Location(2906, 3377, 0), new Location(2924, 3498, 0), new Location(2885,
            3469, 0), new Location(2809, 3481, 0), new Location(2788, 3457, 0), new Location(2726, 3452, 0),
            new Location(2688, 3366, 0), new Location(2692, 3537, 0), new Location(2740, 3632, 0), new Location(2606,
            3631, 0), new Location(2654, 3616, 0), new Location(2630, 3676, 0), new Location(2747, 3536, 0),
            new Location(2630, 3491, 0), new Location(2531, 3490, 0), new Location(2565, 3388, 0), new Location(2662,
            3306, 0), new Location(2607, 3297, 0), new Location(2582, 3298, 0), new Location(2537, 3306, 0),
            new Location(2484, 3330, 0), new Location(2546, 3249, 0), new Location(2608, 3252, 0), new Location(2611,
            3133, 0), new Location(2602, 3096, 0), new Location(2580, 3095, 0), new Location(2542, 3091, 0),
            new Location(2518, 3172, 0), new Location(2557, 3201, 0), new Location(2652, 3158, 0), new Location(2628,
            3084, 0), new Location(2488, 2859, 0), new Location(2522, 2850, 0), new Location(2512, 2926, 0),
            new Location(2592, 2925, 0), new Location(2544, 2889, 0), new Location(2592, 2979, 0), new Location(2348,
            3053, 0), new Location(2379, 3054, 0), new Location(2424, 3050, 0), new Location(2460, 3089, 0),
            new Location(2450, 3114, 0), new Location(2482, 3100, 0), new Location(2471, 3072, 0), new Location(2451,
            3213, 0), new Location(2479, 3218, 0), new Location(2487, 3254, 0), new Location(2486, 3348, 0),
            new Location(2381, 3385, 0), new Location(2460, 3397, 0), new Location(2465, 3486, 0), new Location(2405,
            3453, 0), new Location(2421, 3431, 0), new Location(2674, 3693, 0), new Location(2719, 2778, 0),
            new Location(2781, 2713, 0), new Location(2795, 2926, 0), new Location(2910, 2909, 0), new Location(2939,
            2996, 0), new Location(2922, 3094, 0), new Location(2884, 3050, 0), new Location(2794, 3088, 0),
            new Location(2770, 3132, 0), new Location(2804, 2974, 0), new Location(2862, 2983, 0), new Location(2836,
            2999, 0), new Location(2833, 2953, 0), new Location(2862, 2964, 0), new Location(2743, 3173, 0),
            new Location(2843, 3154, 0), new Location(2901, 3172, 0), new Location(2909, 3151, 0), new Location(2940,
            3146, 0), new Location(2729, 3222, 0), new Location(2759, 3231, 0), new Location(2710, 3276, 0),
            new Location(2712, 3307, 0), new Location(2583, 3329, 0), new Location(2525, 3347, 0), new Location(2510,
            3402, 0), new Location(2504, 3527, 0), new Location(2525, 3590, 0), new Location(2330, 3538, 0),
            new Location(2299, 3592, 0), new Location(2349, 3627, 0), new Location(2342, 3689, 0), new Location(2198,
            3056, 0), new Location(2402, 3083, 0), new Location(2147, 3944, 0), new Location(2085, 3913, 0),
            new Location(2102, 3864, 0), new Location(2143, 3854, 0), new Location(2321, 3792, 0), new Location(2318,
            3811, 0), new Location(2330, 3831, 0), new Location(2351, 3862, 0), new Location(2381, 3798, 0),
            new Location(2412, 3809, 0), new Location(2407, 3788, 0), new Location(2404, 3853, 0), new Location(2353,
            3891, 0), new Location(2513, 3859, 0), new Location(2539, 3866, 0), new Location(2538, 3888, 0),
            new Location(2612, 3893, 0), new Location(2605, 3875, 0), new Location(2578, 3879, 0), new Location(2528,
            3739, 0), new Location(1757, 3430, 0), new Location(1764, 3471, 0), new Location(1807, 3464, 0),
            new Location(1823, 3454, 0), new Location(1855, 3511, 0), new Location(1834, 3587, 0), new Location(1758,
            3599, 0), new Location(1790, 3549, 0), new Location(1690, 3481, 0), new Location(1645, 3673, 0),
            new Location(1631, 3664, 0), new Location(1631, 3680, 0), new Location(1708, 3672, 0), new Location(1754,
            3672, 0), new Location(1800, 3689, 0), new Location(1834, 3689, 0), new Location(1803, 3746, 0),
            new Location(1803, 3778, 0), new Location(1751, 3789, 0), new Location(1644, 3741, 0), new Location(1708,
            3740, 0), new Location(1643, 3787, 0), new Location(1590, 3777, 0), new Location(1632, 3838, 0),
            new Location(1558, 3886, 0), new Location(1576, 3891, 0), new Location(1495, 3783, 0), new Location(1472,
            3731, 0), new Location(1457, 3885, 0), new Location(1631, 3946, 0), new Location(1669, 3881, 0),
            new Location(1703, 3943, 0), new Location(1295, 3763, 0), new Location(1342, 3733, 0), new Location(1257,
            3707, 0), new Location(1220, 3780, 0), new Location(1210, 3672, 0), new Location(1248, 3556, 0),
            new Location(1504, 3416, 0), new Location(1520, 3445, 0), new Location(1592, 3457, 0), new Location(1616,
            3499, 0), new Location(1592, 3473, 0), new Location(1560, 3512, 0), new Location(1550, 3611, 0),
            new Location(1483, 3595, 0), new Location(1464, 3652, 0), new Location(1311, 3825, 0), new Location(1311,
            3797, 0), new Location(3765, 3758, 0), new Location(3677, 3876, 0), new Location(3693, 3844, 0),
            new Location(3764, 3859, 0), new Location(3734, 3816, 0), new Location(1428, 3542, 0), new Location(1512,
            3512, 0), new Location(1435, 3791, 0), new Location(1476, 3636, 0), new Location(2827, 3344, 0),
            new Location(2840, 3375, 0), new Location(2808, 3434, 0), new Location(2838, 3433, 0), new Location(2847,
            3441, 0), new Location(2886, 3411, 0), new Location(2896, 3440, 0), new Location(2923, 3429, 0),
            new Location(2894, 3535, 0), new Location(2915, 3545, 0), new Location(2930, 3559, 0), new Location(2878,
            3546, 0), new Location(3111, 3169, 0), new Location(3216, 3422, 0), new Location(3212, 3448, 0),
            new Location(3181, 3487, 0), new Location(3179, 3406, 0), new Location(3215, 3344, 0), new Location(3328,
            3334, 0), new Location(2965, 3380, 0), new Location(3041, 3355, 0), new Location(2945, 3322, 0),
            new Location(3239, 3344, 0), new Location(3367, 3267, 0), new Location(3536, 3290, 0), new Location(3563,
            3317, 0), new Location(3644, 3530, 0), new Location(3503, 3552, 0), new Location(3318, 3491, 0),
            new Location(3119, 3404, 0), new Location(3202, 3266, 0), new Location(2999, 3266, 0), new Location(2939,
            3372, 0), new Location(3056, 3332, 0), new Location(2857, 3359, 0), new Location(2661, 3239, 0),
            new Location(2536, 3234, 0), new Location(2666, 3453, 0), new Location(2645, 3377, 0), new Location(2653,
            3578, 0), new Location(2677, 3718, 0), new Location(2706, 3730, 0), new Location(2716, 3666, 0),
            new Location(2985, 3465, 0), new Location(3082, 3423, 0), new Location(3164, 3464, 0), new Location(3382,
            3265, 0), new Location(3238, 3280, 0), new Location(3259, 3227, 0), new Location(3222, 3218, 0),
            new Location(3209, 3204, 0), new Location(3206, 3233, 0), new Location(3243, 3195, 0), new Location(2773,
            3184, 0), new Location(2768, 3214, 0), new Location(2606, 3220, 0), new Location(2511, 3204, 0),
            new Location(2499, 3098, 0), new Location(2528, 3093, 0), new Location(2332, 3166, 0), new Location(2344,
            3175, 0), new Location(2454, 3240, 0), new Location(2545, 3269, 0), new Location(2642, 3283, 0),
            new Location(2675, 3287, 0), new Location(2635, 3340, 0), new Location(2664, 3347, 0), new Location(2655,
            3368, 0), new Location(2711, 3462, 0), new Location(2703, 3481, 0), new Location(2726, 3484, 0),
            new Location(2682, 3482, 0), new Location(2758, 3479, 0), new Location(2899, 3554, 0), new Location(2930,
            3431, 0), new Location(2938, 3445, 0), new Location(2966, 3412, 0), new Location(3008, 3406, 0),
            new Location(3137, 3517, 0), new Location(3125, 3456, 0), new Location(3192, 3446, 0), new Location(3212,
            3428, 0), new Location(3298, 3453, 0), new Location(3264, 3462, 0), new Location(3215, 3502, 0),
            new Location(3239, 3494, 0), new Location(3188, 3459, 0), new Location(3144, 3515, 0), new Location(3052,
            3482, 0), new Location(3170, 3277, 0), new Location(3177, 3295, 0), new Location(3148, 3280, 0),
            new Location(3119, 3279, 0), new Location(3203, 3292, 0), new Location(3080, 3250, 0), new Location(3104,
            3249, 0), new Location(3093, 3271, 0), new Location(3026, 3206, 0), new Location(3028, 3242, 0),
            new Location(3042, 3253, 0), new Location(3005, 3201, 0), new Location(2955, 3215, 0), new Location(2954,
            3240, 0), new Location(2972, 3233, 0), new Location(2945, 3452, 0), new Location(2956, 3492, 0),
            new Location(2733, 3413, 0), new Location(2702, 3421, 0), new Location(2754, 3400, 0), new Location(2728,
            3347, 0), new Location(3191, 3365, 0), new Location(3284, 3373, 0), new Location(3189, 3375, 0),
            new Location(3198, 3388, 0), new Location(3557, 3476, 0), new Location(3507, 3502, 0), new Location(3494,
            3488, 0), new Location(3472, 3473, 0), new Location(3592, 3525, 0), new Location(2846, 3932, 0),
            new Location(2314, 3666, 0), new Location(2317, 3696, 0), new Location(2112, 3890, 0), new Location(2105,
            3913, 0), new Location(1625, 3933, 0), new Location(1248, 3719, 0), new Location(1271, 3757, 0),
            new Location(1293, 3665, 0), new Location(1237, 3644, 0), new Location(1310, 3614, 0), new Location(1233,
            3568, 0), new Location(1254, 3566, 0), new Location(1271, 3557, 0), new Location(1535, 3486, 0),
            new Location(1587, 3494, 0), new Location(1652, 3511, 0), new Location(1659, 3504, 0), new Location(1712,
            3463, 0), new Location(1724, 3463, 0), new Location(1778, 3427, 0), new Location(1784, 3408, 0),
            new Location(1772, 3639, 0), new Location(1772, 3658, 0), new Location(1767, 3707, 0), new Location(1840,
            3785, 0), new Location(1827, 3747, 0), new Location(1541, 3733, 0), new Location(1535, 3780, 0),
            new Location(1705, 3880, 0), new Location(1609, 3910, 0), new Location(1675, 3920, 0), new Location(2760,
            2788, 0), new Location(2852, 2948, 0), new Location(2905, 3180, 0), new Location(2756, 3191, 0),
            new Location(2725, 3290, 0), new Location(2728, 3392, 0), new Location(2749, 3431, 0), new Location(2751,
            3454, 0), new Location(2711, 3582, 0), new Location(3080, 3447, 0), new Location(3115, 3448, 0),
            new Location(3143, 3442, 0), new Location(3179, 3430, 0)};
    private static final ObjectList<ImplingSpawn> globalSpawns = new ObjectArrayList<>(implingSpawns.length);
    private static final ObjectList<DonatorImplingSpawn> donorImplings = new ObjectArrayList<>(new DonatorImplingSpawn[]{
            new DonatorImplingSpawn(Impling.ECLECTIC, new Location(3346, 8162, 2)),
            new DonatorImplingSpawn(Impling.NATURE, new Location(3355, 8146, 2)),
            new DonatorImplingSpawn(Impling.NATURE, new Location(3368, 8164, 2)),
            new DonatorImplingSpawn(Impling.MAGPIE, new Location(3366, 8279, 2)),
            new DonatorImplingSpawn(Impling.NINJA, new Location(3390, 7984, 0)),
            new DonatorImplingSpawn(Impling.CRYSTAL, new Location(3370, 7605, 0)),
            new DonatorImplingSpawn(Impling.DRAGON, new Location(3414, 7593, 0)),
            new DonatorImplingSpawn(Impling.LUCKY, new Location(3385, 7821, 0)),

    });

    @Subscribe
    public static void onServerLaunch(final ServerLaunchEvent event) {
        int spawnedImplingCount = 0;
        int rareImplingCount = 0;
        int invisibleImplingCount = 0;
        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (final Location spawn : implingSpawns) {
            final boolean isRare = r.nextInt(100) < 13;
            final boolean invisible = isRare || r.nextInt(10) > 3;
            final GlobalImplings.ImplingSpawn implingSpawn = new ImplingSpawn(spawn, isRare, invisible);
            globalSpawns.add(implingSpawn);
            if (r.nextBoolean()) {
                spawnNewImpling(implingSpawn);
                spawnedImplingCount++;
                if (isRare) {
                    rareImplingCount++;
                }
                if (invisible) {
                    invisibleImplingCount++;
                }
            }
        }
        for (DonatorImplingSpawn impling : donorImplings) {
            ImplingNPC implingNPC = new ImplingNPC(impling.getImpling().getNpcId(), impling.getLocation(), Direction.SOUTH)
                    .setOnFinished(npc -> {
                        npc.setRespawnTime(Utils.random(2000, 6000));
                        npc.setRespawnTask();
                    });
            implingNPC.spawn();
            implingNPC.setRadius(10);
        }
        log.debug(spawnedImplingCount + " global implings spawned; " + rareImplingCount + " rare, " + invisibleImplingCount + " invisible, "+ donorImplings.size()+" donator islands");
    }

    public static void spawnRandomImpling() {
        final ImplingSpawn randomElement = Utils.random(globalSpawns);
        if (randomElement != null)
            spawnNewImpling(randomElement);
    }

    public static void spawnNewImpling(final ImplingSpawn spawn) {
        // 'high-level spawns' can spawn high-level and low-level imps
        // 'low-level spawns' can only spawn low-level imps
        final List<Impling> implingList = (spawn.isRare() ? Impling.values : Impling.notRareImplings)
                .stream()
                .filter(Impling::isAutoSpawn)
                .collect(Collectors.toList());
        final Impling impling = Utils.random(implingList);
        if (impling == null)
            return;
        new ImplingNPC(impling.getNpcId(), spawn.getLocation(), Direction.SOUTH)
                .setInvisibleSpawn(spawn.isInvisible())
                .setOnFinished(npc -> GlobalImplings.spawnRandomImpling())
                .spawn();
    }


    static class ImplingSpawn {
        private final Location location;
        private final boolean isRare;
        private final boolean invisible;

        public ImplingSpawn(Location location, boolean isRare, boolean invisible) {
            this.location = location;
            this.isRare = isRare;
            this.invisible = invisible;
        }

        public Location getLocation() {
            return location;
        }

        public boolean isRare() {
            return isRare;
        }

        public boolean isInvisible() {
            return invisible;
        }

        @Override
        public String toString() {
            return "GlobalImplings.ImplingSpawn(location=" + this.getLocation() + ", isRare=" + this.isRare() + ", " +
                    "invisible=" + this.isInvisible() + ")";
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof GlobalImplings.ImplingSpawn)) return false;
            final GlobalImplings.ImplingSpawn other = (GlobalImplings.ImplingSpawn) o;
            if (!other.canEqual(this)) return false;
            if (this.isRare() != other.isRare()) return false;
            if (this.isInvisible() != other.isInvisible()) return false;
            final Object this$location = this.getLocation();
            final Object other$location = other.getLocation();
            return this$location == null ? other$location == null : this$location.equals(other$location);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof GlobalImplings.ImplingSpawn;
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.isRare() ? 79 : 97);
            result = result * PRIME + (this.isInvisible() ? 79 : 97);
            final Object $location = this.getLocation();
            result = result * PRIME + ($location == null ? 43 : $location.hashCode());
            return result;
        }
    }

    public static class DonatorImplingSpawn {
        private Impling impling;
        private Location location;

        public Impling getImpling() {
            return impling;
        }

        public Location getLocation() {
            return location;
        }

        public DonatorImplingSpawn(Impling impling, Location location) {
            this.impling = impling;
            this.location = location;
        }
    }
}

package com.zenyte.game.world.entity.player.action.combat;

import com.near_reality.game.content.combat.CombatUtility;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalArmour;
import com.near_reality.game.content.custom.SlayerHelmetEffects;
import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.boons.impl.*;
import com.zenyte.game.content.boss.phantommuspah.PhantomMuspah;
import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.tombsofamascut.AbstractTheatreNPC;
import com.zenyte.game.content.tombsofamascut.npc.AbstractTOANPC;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Entity.EntityType;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.StatType;
import com.zenyte.game.world.entity.pathfinding.events.player.CombatEntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.PredictedEntityStrategy;
import com.zenyte.game.world.entity.player.Bonuses;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Kris | 14. okt 2017 : 19:51.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class RangedCombat extends PlayerCombat {

    private enum EnchantedBoltSpecial {
        LUCKY_LIGHTNING(new SoundEffect(2918, 15, 0), new Graphics(749), 0.05F, 0.055F, 0.05F, 0.055F, 9236, 21932),
        EARTHS_FURY(new SoundEffect(2916, 15, 0), new Graphics(755), 0.06F, 0.066F, 0.06F, 0.066F, 9237, 21934),
        SEA_CURSE(new SoundEffect(2920, 15, 0), new Graphics(750), 0.06F, 0.066F, 0.06F, 0.066F, 9238, 21936),
        DOWN_TO_EARTH(new SoundEffect(2914, 15, 0), new Graphics(757), 0.04F, 0.044F, 0.0F, 0.0F, 9239, 21938), //n
        CLEAR_MIND(new SoundEffect(2912, 15, 0), new Graphics(751), 0.05F, 0.055F, 0.0F, 0.0F, 9240, 21940), //n
        MAGICAL_POISON(new SoundEffect(2919, 15, 0), new Graphics(752), 0.54F, 0.594F, 0.55F, 0.605F, 9241, 21942), //n
        BLOOD_FORFEIT(new SoundEffect(2911, 15, 0), new Graphics(754), 0.11F, 0.121F, 0.06F, 0.066F, 9242, 21944),
        ARMOUR_PIERCING(new SoundEffect(2913, 15, 0), new Graphics(758), 0.05F, 0.055F, 0.1F, 0.11F, 9243, 21946),
        DRAGONS_BREATH(new SoundEffect(2915, 15, 0), new Graphics(756), 0.06F, 0.066F, 0.06F, 0.066F, 9244, 21948),
        LIFE_LEECH(new SoundEffect(2917, 15, 0), new Graphics(753), 0.1F, 0.11F, 0.11F, 0.121F, 9245, 21950);

        private final float pvpProcChance;
        private final float pvpKandarinProcChance;
        private final float pvmProcChance;
        private final float pvmKandarinProcChance;
        private final SoundEffect sound;
        private final Graphics graphics;
        private final int[] bolts;
        private static final Int2ObjectMap<EnchantedBoltSpecial> map = new Int2ObjectOpenHashMap<>();

        static {
            for (final RangedCombat.EnchantedBoltSpecial value : values()) {
                for (final int bolt : value.bolts) {
                    map.put(bolt, value);
                }
            }
        }

        EnchantedBoltSpecial(final SoundEffect sound, final Graphics graphics, final float pvpProcChance, final float pvpKandarinProcChance, final float pvmProcChance, final float pvmKandarinProcChance, final int... bolts) {
            this.sound = sound;
            this.graphics = graphics;
            this.pvpProcChance = pvpProcChance;
            this.pvpKandarinProcChance = pvpKandarinProcChance;
            this.pvmProcChance = pvmProcChance;
            this.pvmKandarinProcChance = pvmKandarinProcChance;
            this.bolts = bolts;
        }

        private static EnchantedBoltSpecial get(@NotNull final Player player, @NotNull final Entity target, final boolean successfulHit, final boolean zaryteSpec) {
            final RangedCombat.EnchantedBoltSpecial element = map.get(player.getEquipment().getId(EquipmentSlot.AMMUNITION));
            if (element == null) {
                return null;
            }

            if (zaryteSpec) {
                //https://youtu.be/IpjLvHlT_Bs?t=1224 diamond bolts being able to hit 0 with a spec, even though they have armor piecing effect.
                return successfulHit ? element : null;
            }

            if (!element.isEffective(player, target)) {
                return null;
            }

            return element;
        }

        private boolean isEffective(@NotNull final Player player, @NotNull final Entity target) {
            final int ammunitionId = player.getEquipment().getId(EquipmentSlot.AMMUNITION);
            final AmmunitionDefinition ammunition = AmmunitionDefinitions.getDefinitions(ammunitionId);
            if (ammunition == null || !ammunition.isCompatible(player.getEquipment().getId(EquipmentSlot.WEAPON))) {
                return false;
            }

            final boolean diary = DiaryUtil.eligibleFor(DiaryReward.KANDARIN_HEADGEAR3, player);
            final double chance = (target instanceof Player ? (diary ? pvpKandarinProcChance : pvpProcChance) : (diary ? pvmKandarinProcChance : pvmProcChance));
            return Utils.randomDouble() < chance;
        }
    }

    protected static final int BREAK_CHANCE = 20;
    protected AmmunitionDefinition ammunition;
    private EnchantedBoltSpecial boltSpecial;
    private boolean zaryteSpec;

    public RangedCombat(final Entity target, final AmmunitionDefinition defs) {
        super(target);
        ammunition = defs;
    }

    @Override
    public Hit getHit(final Player player, final Entity target, final double accuracyModifier,
                      final double passiveModifier, double activeModifier, final boolean ignorePrayers) {
        return new Hit(player, getRandomHit(player, target, getMaxHit(player, passiveModifier, 1, ignorePrayers), accuracyModifier), HitType.RANGED);
    }

    @Override
    public int getMaxHit(final Player player, final double specialModifier, double activeModifier, final boolean ignorePrayers) {
        float boost = CombatUtilities.hasFullRangedVoid(player, true) ? 1.125F : CombatUtilities.hasFullRangedVoid(player, false) ? 1.1F : 1.0F;
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);

        var dragonHunterCrossBows = List.of(ItemId.DRAGON_HUNTER_CROSSBOW, ItemId.DRAGON_HUNTER_CROSSBOW_B, ItemId.DRAGON_HUNTER_CROSSBOW_T);
        if (dragonHunterCrossBows.contains(weaponId) && CombatUtilities.isDraconic(target)) {
            boost += 0.3F;
        }

        if (CombatUtilities.applyForinthrySurge(player, target))
            boost += 0.15F;

        if (CombatUtilities.applyPvmArenaBoost(player, target))
            boost += 0.05F;

        boost += determineBountyHunterDmgBoost(player, target);
        boost += (float) CrystalArmour.Companion.getTotalDamageBonus(player);

        final double a = (Math.floor(player.getSkills().getLevel(SkillConstants.RANGED) * player.getPrayerManager().getRangedBoost(SkillConstants.STRENGTH)) + (player.getCombatDefinitions().getStyle() == 0 ? 3 : 0) + 8) * (boost);
        final float rangedStrength = (float) player.getBonuses().getBonus(11);
        double result = Math.floor(0.5F + a * (rangedStrength + 64.0F) / 640.0F);
        final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
        final String amuletName = ItemDefinitions.nameOf(amuletId).toLowerCase();

        if (amuletId == ItemId.AMULET_OF_AVARICE && CombatUtilities.isRevenant(target)) {
            result *= 1.2F;
        } else if ((amuletName.startsWith("salve amulet")) && (CombatUtilities.SALVE_AFFECTED_NPCS.contains(name) || CombatUtilities.isUndeadCombatDummy(target))) {
            result *= amuletId == 12017 ? 1.15F : 1.2F;
        }

        boolean hasTask = (player.getSlayer().isCurrentAssignment(target) || player.hasBoon(SlayersSovereignty.class)) || CombatUtilities.isCombatDummy(target);
        result *= determineSlayerHelmetDamageBoost(hasTask, HitType.RANGED, player, target);
        result = Math.floor(result);

        if(target instanceof NPC npc && MinionsMight.shouldBoostCombat(player, npc))
            result *= 1.1F;

        if (boltSpecial == EnchantedBoltSpecial.ARMOUR_PIERCING) {
            result *= wearingZaryteCrossbow() ? 1.25F : 1.15F;
        } else if (boltSpecial == EnchantedBoltSpecial.LIFE_LEECH) {
            if (!CombatUtilities.SALVE_AFFECTED_NPCS.contains(name)) {
                result *= wearingZaryteCrossbow() ? 1.30 : 1.2F;
            }
        }
        result *= getTwistedBowDamageBoost(player, target, target.getMagicLevel(), player.isInRaid());
        if (!ignorePrayers) {
            if (target instanceof Player && ((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MISSILES)) {
                result *= target.getRangedPrayerMultiplier();
            }
        }
        result *= specialModifier;
        result = SlayerHelmetEffects.INSTANCE.rollBonusDamage(player, target, result);
        if (boltSpecial == EnchantedBoltSpecial.DRAGONS_BREATH) {
            if (CombatUtilities.isDragonsBreath(this)) {
                float percent = wearingZaryteCrossbow() ? 0.22F : 0.2F;
                result += player.getSkills().getLevel(SkillConstants.RANGED) * percent;
            }
        } else if (boltSpecial == EnchantedBoltSpecial.LUCKY_LIGHTNING) {
            int mod = wearingZaryteCrossbow() ? 9 : 10;
            result += (double) player.getSkills().getLevel(SkillConstants.RANGED) / mod;
        } else if (boltSpecial == EnchantedBoltSpecial.SEA_CURSE) {
            target.setGraphics(new Graphics(750));
            int divider = 20;
            if (target instanceof Player) {
                final int targetWeapon = ((Player) target).getEquipment().getId(EquipmentSlot.WEAPON.getSlot());
                if (targetWeapon == 1387 || targetWeapon == 1393 || targetWeapon == 1401 || targetWeapon == 3054) {
                    divider = 15;
                } else if (targetWeapon == 1383 || targetWeapon == 1395 || targetWeapon == 1403) {
                    divider = -1;
                }
            } else {
                if (CombatUtilities.isFireNPC((NPC) target)) {
                    divider = 15;
                }
            }
            if (divider != -1) {
                result += (float) player.getSkills().getLevel(SkillConstants.RANGED) / divider;
            }
        }
        //Castle wars bracelet effect
        if (player.getTemporaryAttributes().containsKey("castle wars bracelet effect") && player.inArea("Castle Wars")) {
            if (target instanceof Player) {
                final int targetWeapon = ((Player) target).getEquipment().getId(EquipmentSlot.WEAPON);
                if (targetWeapon == 4037 || targetWeapon == 4039) {
                    result = result * 1.2F;
                }
            }
        }
        return (int) Math.floor(result);
    }

    @Override
    public final int getRandomHit(final Player player, final Entity target, final int maxhit, final double modifier) {
        return getRandomHit(player, target, maxhit, modifier, AttackType.RANGED);
    }

    @Override
    public int getRandomHit(final Player player, final Entity target, final int maxhit, final double modifier, final AttackType oppositeIndex) {
        if (CombatUtilities.isAlwaysTakeMaxHit(target, HitType.RANGED)) {
            return maxhit;
        }
        final int accuracy = getAccuracy(player, target, modifier);
        if (CombatUtilities.isMovingWarden(target)) {
            final int minimumHit = accuracy / 2000;
            final int bonus = player.getBonuses().getBonus(Bonuses.Bonus.RANGE_STRENGTH);
            final double bonusModifier = 1.0F + (bonus / 10F);
            return Utils.random(minimumHit, (int) (minimumHit * bonusModifier));
        }
        if (boltSpecial == EnchantedBoltSpecial.ARMOUR_PIERCING) {
            return Utils.random(maxhit);
        }
        final int targetRoll = getTargetDefenceRollRanged(player, target, oppositeIndex);
        sendDebug(accuracy, targetRoll, maxhit);
        final int accRoll = accuracy > 0 ? Utils.random(accuracy) : 0;
        final int defRoll = targetRoll > 0 ? Utils.random(targetRoll) : 0;
        if (accRoll <= defRoll) {
            return 0;
        }
        int playerMinimum = calculateMinimumHit(player, maxhit);
        return Utils.random(playerMinimum, maxhit);
    }


    public int getTargetDefenceRollRanged(final Entity attacker, final Entity target, AttackType type) {
        int effectiveLevel = 0;
        int effectiveDefenseBonus = 0;
        if(target instanceof NPC npc) {
            int actualLevel = npc.getCombatDefinitions().getStatDefinitions().get(StatType.DEFENCE);
            effectiveLevel = actualLevel + 9;

            int baseBonus = npc.getCombatDefinitions().getStatDefinitions().get(StatType.getDefenceType(type));
            effectiveDefenseBonus = baseBonus + 64;
        }

        if(target instanceof Player player2) {
            int actualLevel = player2.getSkills().getLevel(SkillConstants.DEFENCE);
            effectiveLevel = actualLevel + 9;

            int baseBonus = player.getBonuses().getBonus(Bonuses.Bonus.DEF_RANGE);
            effectiveDefenseBonus = baseBonus + 64;
        }
        return (int) (effectiveLevel * (effectiveDefenseBonus));
    }

    @Override
    public int getAccuracy(final Player player, final Entity target, final double resultModifier) {
        float voidBoost = 1.0F;
        voidBoost += determineVoidBoost(player);

        int skillLevel = player.getSkills().getLevel(SkillConstants.RANGED);
        double prayerBonus = player.getPrayerManager().getRangedBoost(SkillConstants.ATTACK);
        int styleBonusLevels = player.getCombatDefinitions().getStyle() == 0 ? 3 : 0;
        double baseRangedAttack = Math.floor(Math.floor(skillLevel * prayerBonus) + (styleBonusLevels) + 8.0F);
        final double effectiveRangedAttack = baseRangedAttack * (voidBoost);

        boolean hasTask = (player.getSlayer().isCurrentAssignment(target) || player.hasBoon(SlayersSovereignty.class)) || CombatUtilities.isUndeadCombatDummy(target);

        float gearBonus = 1.0F;
        gearBonus *= determineAmuletBonus(player);
        gearBonus *= (float) determineSlayerHelmetAccuracyBoost(hasTask, HitType.RANGED, player, target);
        gearBonus += determineDraconicBoost(player, target);
        gearBonus += determineForinthrySurge(player, target);
        gearBonus += (float) CrystalArmour.Companion.getTotalAccuracyBonus(player);
        gearBonus += determineBountyHunterAccBoost(player, target);
        if (CombatUtilities.applyPvmArenaBoost(player, target))
            gearBonus += 0.05F;
        final int equipmentRangedAttack = player.getBonuses().getBonus(4);
        double attackRoll = effectiveRangedAttack * (equipmentRangedAttack + 64.0F) * gearBonus;

        attackRoll = Math.floor(attackRoll);
        attackRoll *= getTwistedBowAccuracyBoost(player, target, target.getMagicLevel(), player.isInRaid());
        attackRoll = determinePerkAccuracyBonuses(player, target, attackRoll);

        attackRoll *= resultModifier;
        attackRoll = Math.floor(attackRoll);
        if (player.getTemporaryAttributes().containsKey("combat debug")) {
            player.sendMessage("Acc || Base: " + baseRangedAttack + ", Void: " + voidBoost + ", Gear: " + gearBonus + ", Final: " + attackRoll);
        }
        return (int) attackRoll;
    }


    private double determinePerkAccuracyBonuses(Player player, Entity target, double roll) {
        if(target instanceof NPC npc) {
            if(player.hasBoon(JabbasRightHand.class) && CombatUtility.hasBountyHunterWeapon(player))
                roll *= 1.15;

            if (player.hasBoon(BrawnOfJustice.class) && BrawnOfJustice.applies(player) && player.getBooleanTemporaryAttribute("TOB_inside"))
                roll *= 1.2;

            if(MinionsMight.shouldBoostCombat(player, npc))
                roll *= 1.1F;
        }
        return roll;
    }

    /**
     * Determines the bonus accuracy granted by various amulets
     *
     * @param player the player attacking the entity
     * @return the boost in base 1 added to the pre-roll factorial
     */
    private float determineAmuletBonus(Player player) {
        final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
        if (amuletId == ItemId.AMULET_OF_AVARICE && CombatUtilities.isRevenant(target)) {
            return 1.2F;
        } else if ((amuletId == 12017 || amuletId == 12018) && CombatUtilities.SALVE_AFFECTED_NPCS.contains(name)) {
            return amuletId == 12017 ? 1.15F : 1.2F;
        }
        return 1.0F;
    }

    /**
     * Determines the bonus accuracy granted by a forinthry surge in the wilderness
     *
     * @param player the player attacking the entity
     * @param target the entity being attacked by the player
     * @return the boost in base 1 added to the pre-roll factorial
     */
    private float determineForinthrySurge(Player player, Entity target) {
        if (CombatUtilities.applyForinthrySurge(player, target)) {
            return 0.15F;
        }
        return 0;
    }

    /**
     * Determines the bonus accuracy granted by the DHCB against draconic targets
     *
     * @param player the player attacking the entity
     * @param target the entity being attacked by the player
     * @return the boost in base 1 added to the pre-roll factorial
     */
    private float determineDraconicBoost(Player player, Entity target) {
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        var dragonHunterCrossBows = List.of(ItemId.DRAGON_HUNTER_CROSSBOW, ItemId.DRAGON_HUNTER_CROSSBOW_B, ItemId.DRAGON_HUNTER_CROSSBOW_T);
        if (dragonHunterCrossBows.contains(weaponId) && CombatUtilities.isDraconic(target)) {
            return 0.3F;
        }
        return 0;
    }

    /**
     * Determines the bonus accuracy granted if player has void equipped. For a full set of normal
     * void, it adds 10% DR, for a full set of elite void it adds 12.5% DR.
     * @param player player in combat
     * @return the bonus added to the pre-acc roll factorial
     */
    private float determineVoidBoost(Player player) {
        if(CombatUtilities.hasFullRangedVoid(player, true))
            return 0.125F;
        if(CombatUtilities.hasFullRangedVoid(player, false))
            return 0.1F;
        return 0;
    }

    public boolean hasTwistedBow() {
        return player.getWeapon() != null && CombatUtilities.isTwistedBow(player.getWeapon().getId());
    }

    public static double getTwistedBowDamageBoost(Player player, Entity target, int magicLevel, boolean inRaid) {
        if(target instanceof AbstractTOANPC || target instanceof AbstractTheatreNPC)
            inRaid = true;
        if(player.getWeapon() == null || !CombatUtilities.isTwistedBow(player.getWeapon().getId()))
            return 1.0;
        if (magicLevel > (inRaid ? 350 : 250))
            magicLevel = (inRaid ? 350 : 250);
        double boost = 250 + ((3d * magicLevel - 14d) / 100d) - (Math.pow((3d * magicLevel / 10d) - 140d, 2) / 100d);
        return (Math.min(boost, inRaid ? 350 : 250) / 100);
    }

    public static double getTwistedBowAccuracyBoost(Player player, Entity target, int magicLevel, boolean inRaid) {
        if(target instanceof AbstractTOANPC || target instanceof AbstractTheatreNPC)
            inRaid = true;

        if(player.getWeapon() == null || !CombatUtilities.isTwistedBow(player.getWeapon().getId()))
            return 1.0;
        if (magicLevel > (inRaid ? 350 : 250))
            magicLevel = (inRaid ? 350 : 250);
        double boost = 140 + ((3d * magicLevel - 10d) / 100d) - (Math.pow(3d * magicLevel / 10d - 100d, 2) / 100d);
        return (Math.min(boost, 140) / 100);
    }

    @Override
    public boolean process() {
        return initiateCombat(player);
    }

    @Override
    protected boolean isWithinAttackDistance() {
        if (target.checkProjectileClip(player, false) && isProjectileClipped(true, false)) {
            return false;
        }
        final Location nextTile = target.getNextLocation();
        final Location tile = nextTile != null ? nextTile : target.getLocation();
        final int distanceX = player.getX() - tile.getX();
        final int distanceY = player.getY() - tile.getY();
        final int size = target.getSize();
        int maxDistance = getAttackDistance();
        final Location nextLocation = target.getLocation();
        if ((player.isFrozen() || player.isStunned()) && (CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), nextLocation.getX(), nextLocation.getY(), target.getSize()) || !withinRange(target, maxDistance, target.getSize()))) {
            return false;
        }
        return distanceX <= size + maxDistance && distanceX >= -1 - maxDistance && distanceY <= size + maxDistance && distanceY >= -1 - maxDistance;
    }

    @Override
    public int processWithDelay() {
        return 0;
    }

    private int getHit(final int maxHit, final boolean successful) {
        if (CombatUtilities.isCombatDummy(target)) {
            return maxHit;
        }

        return successful ? Utils.random(maxHit) : 0;
    }

    public boolean isPvp() {
        return target instanceof Player;
    }

    protected void extra(final Hit hit) {
        bloodFury(HitType.RANGED, hit);
    }

    @Override
    public int processAfterMovement() {
        if (!isWithinAttackDistance()) {
            return 0;
        }
        if (!canAttack()) {
            return -1;
        }

        addAttackedByDelay(player, target);
        final RegionArea area = player.getArea();
        if (area instanceof PlayerCombatPlugin) {
            ((PlayerCombatPlugin) area).onAttack(player, target, "Ranged", null, false);
        }

        if (player.getCombatDefinitions().isUsingSpecial()) {
            final int delay = useSpecial(player, SpecialType.RANGED);
            if (delay == SpecialAttackScript.WEAPON_SPEED) {
                return getWeaponSpeed();
            }
            if (delay >= 0) {
                return delay;
            }
        }

        final int attackRoll = getAccuracy(player, target, 1.0);
        final int defenseRoll = getTargetDefenceRollRanged(player, target, AttackType.RANGED);
        boolean successfulHit = false;

        double hitChance;
        if(attackRoll > defenseRoll) {
            hitChance = 1 - ((double) (defenseRoll + 2) / (2 * (attackRoll + 1)));
        } else {
            hitChance = ((double) (attackRoll) / (2 * (defenseRoll + 1)));
        }
        double randomRoll = Utils.randomDouble(1D);
        if(randomRoll <= hitChance)
            successfulHit = true;

        zaryteSpec = false;
        if(wearingZaryteCrossbow()) {
            if (player.getTemporaryAttributes().remove("zaryte_cbow_spec") != null) {
                zaryteSpec = true;
                successfulHit = true;
            }
        }

        if (player.getTemporaryAttributes().containsKey("combat debug")) {
            player.sendMessage("Atk || Targ: " + defenseRoll + ", Atkr: " + attackRoll + ", Success: " + successfulHit + ", Chance: " + hitChance * 100D + "%");
        }

        boltSpecial = EnchantedBoltSpecial.get(player, target, successfulHit, zaryteSpec);
        if (boltSpecial == EnchantedBoltSpecial.ARMOUR_PIERCING) {
            successfulHit = true;
            player.sendDeveloperMessage("Bypassed range accuracy calc due to bolts");
        }
        boolean hasBonusHit = false;
        Hit bonusHit = null;

        if(hasTwistedBow() && player.getBoonManager().hasBoon(RelentlessPrecision.class)) {
            RelentlessPrecision.Special boost = RelentlessPrecision.determineBoost();
            switch(boost) {
                case BOTH -> {
                    successfulHit = true;
                    hasBonusHit = true;
                    bonusHit = new Hit(player, getHit(getMaxHit(player, 1.0, 1.0, false), true), HitType.RANGED);
                }
                case EXTRA_HIT -> {
                    hasBonusHit = true;
                    bonusHit = new Hit(player, getHit(getMaxHit(player, 1.0, 1.0, false), true), HitType.RANGED);
                }
                case BYPASS_DEF ->  {
                    successfulHit = true;
                    player.sendFilteredMessage(Colour.MAROON.wrap("Your arrow manages to bypass the target's defense."));
                }
                case NONE -> {}
            }

        }

        final Hit hit = new Hit(player, getHit(getMaxHit(player, 1.0, 1.0, false), successfulHit), HitType.RANGED);
        extra(hit);
        animate();
        final int ticks = this.fireProjectile();
        if (ammunition.getSoundEffect() != null) {
            player.getPacketDispatcher().sendSoundEffect(ammunition.getSoundEffect());
        }
        resetFlag();
        applyBoltSpecials(hit);
        if (hit.getDamage() > 0) {
            addPoisonTask(ticks);
        }
        delayHit(ticks, hit);
        if(hasBonusHit) {
            player.sendFilteredMessage(Colour.MAROON.wrap("Your bow transforms your single arrow into two mid-air."));
            delayHit(ticks, bonusHit);
        }
        drawback();
        dropAmmunition(ticks, !ammunition.isRetrievable());
        checkIfShouldTerminate(HitType.RANGED);
        return getWeaponSpeed();
    }

    private final void applyBoltSpecials(@NotNull final Hit hit) {
        if (boltSpecial == EnchantedBoltSpecial.LUCKY_LIGHTNING || boltSpecial == EnchantedBoltSpecial.SEA_CURSE || boltSpecial == EnchantedBoltSpecial.ARMOUR_PIERCING) {
            target.setGraphics(boltSpecial.graphics);
            World.sendSoundEffect(new Location(target.getLocation()), boltSpecial.sound);
        } else if (boltSpecial == EnchantedBoltSpecial.DOWN_TO_EARTH) {
            if (target instanceof Player) {
                final Player p = (Player) target;
                final int level = p.getSkills().getLevel(SkillConstants.MAGIC);
                if (level > 0) {
                    p.getSkills().setLevel(SkillConstants.MAGIC, level - 1);
                }
                target.setGraphics(boltSpecial.graphics);
                World.sendSoundEffect(new Location(target.getLocation()), boltSpecial.sound);
            }
        } else if (boltSpecial == EnchantedBoltSpecial.CLEAR_MIND) {
            var drainFraction = wearingZaryteCrossbow() ? 18 : 20;
            hit.putAttribute("sapphire-proc", "true");
            if (target instanceof Player) {
                final Player p = (Player) target;
                final int points = player.getPrayerManager().getPrayerPoints();
                final int drained = p.getPrayerManager().drainPrayerPoints(points / drainFraction);
                player.getPrayerManager().restorePrayerPoints(drained);
                target.setGraphics(boltSpecial.graphics);
                World.sendSoundEffect(new Location(target.getLocation()), boltSpecial.sound);
            } else if (target instanceof final PhantomMuspah muspah && muspah.getShieldHitBar() != null) {
                final int points = player.getPrayerManager().getPrayerPoints();
                final int drained = points / drainFraction;
                player.getPrayerManager().restorePrayerPoints(drained);
                hit.setDamage((int) (player.getSkills().getLevelForXp(SkillConstants.RANGED) / (wearingZaryteCrossbow() ? 2.75F : 3F)));
                hit.setHitType(HitType.SHIELD_DOWN);
                target.setGraphics(boltSpecial.graphics);
                World.sendSoundEffect(new Location(target.getLocation()), boltSpecial.sound);
            }
        } else if (boltSpecial == EnchantedBoltSpecial.MAGICAL_POISON) {
            target.setGraphics(boltSpecial.graphics);
            World.sendSoundEffect(new Location(target.getLocation()), boltSpecial.sound);
            target.getToxins().applyToxin(ToxinType.POISON, wearingZaryteCrossbow() ? 6 : 5, player);
        } else if (boltSpecial == EnchantedBoltSpecial.BLOOD_FORFEIT) {
            float effectMod = 0.2F;
            int effectCap = 100;
            if (wearingZaryteCrossbow()) {
                effectMod = 0.22F;
                effectCap = 110;
            }
            if (player.getHitpoints() * 0.1F >= 1) {
                target.setGraphics(boltSpecial.graphics);
                World.sendSoundEffect(new Location(target.getLocation()), boltSpecial.sound);
                player.applyHit(new Hit(Math.min(99, (int) (player.getHitpoints() * 0.1F)), HitType.DEFAULT));
                hit.setDamage((int) Math.min(effectCap, target.getHitpoints() * effectMod));
            }
        } else if (boltSpecial == EnchantedBoltSpecial.DRAGONS_BREATH) {
            if (CombatUtilities.isDragonsBreath(this)) {
                target.setGraphics(boltSpecial.graphics);
                World.sendSoundEffect(new Location(target.getLocation()), boltSpecial.sound);
            }
        } else if (boltSpecial == EnchantedBoltSpecial.LIFE_LEECH) {
            if (!CombatUtilities.SALVE_AFFECTED_NPCS.contains(name)) {
                target.setGraphics(boltSpecial.graphics);
                World.sendSoundEffect(new Location(target.getLocation()), boltSpecial.sound);
                player.heal((int) (hit.getDamage() * 0.25F));
            }
        } else if (boltSpecial == EnchantedBoltSpecial.EARTHS_FURY) {
            if (Utils.randomDouble() >= (target instanceof Player ? (((Player) target).getSkills().getLevel(SkillConstants.AGILITY) / 200.0F) : 0)) {
                target.setGraphics(boltSpecial.graphics);
                target.setAnimation(new Animation(4172));
                target.stun(8);
                target.resetWalkSteps();
                World.sendSoundEffect(new Location(target.getLocation()), boltSpecial.sound);
            }
        }
    }


    private enum AmmunitionPreserver {
        AVAS_ATTRACTOR(10498, 16),
        AVAS_ACCUMULATOR(10499, 8),
        AVAS_ASSEMBLER(22109, 0),
        ASSEMBLER_MAX_CAPE(21898, 0),
        MASORI_ASSEMBLER_MAX_CAPE(ItemId.MASORI_ASSEMBLER_MAX_CAPE, 0),
        MASORI_ASSEMBLER(ItemId.MASORI_ASSEMBLER, 0),

        COMP_CAPE_T1(32261, 0),
        COMP_CAPE_T2(32263, 0),
        COMP_CAPE_T3(32243, 0),
        COMP_CAPE_T3_1(32245, 0),
        COMP_CAPE_T3_2(32247, 0),
        COMP_CAPE_T3_3(32249, 0),
        COMP_CAPE_T3_4(32251, 0),
        COMP_CAPE_T3_5(32253, 0),
        COMP_CAPE_T3_6(32255, 0),
        COMP_CAPE_T3_7(32257, 0),
        COMP_CAPE_T3_8(32259, 0),
        COMP_CAPE_T3_9(32265, 0),
        ;
        private static final Int2IntOpenHashMap map = new Int2IntOpenHashMap();

        static {
            for (final int id : SkillcapePerk.RANGED.getCapes()) {
                map.put(id, 8);
            }
            for (final RangedCombat.AmmunitionPreserver entry : values()) {
                map.put(entry.id, entry.amount);
            }
        }

        private final int id;
        private final int amount;

        AmmunitionPreserver(int id, int amount) {
            this.id = id;
            this.amount = amount;
        }
    }

    @Override
    public boolean start() {
        player.setCombatEvent(new CombatEntityEvent(player, new PredictedEntityStrategy(target)));
        player.setLastTarget(target);
        notifyIfFrozen();
        if (!checkPreconditions()) return false;
        player.setFaceEntity(target);
        if (initiateCombat(player)) {
            return true;
        }
        player.setFaceEntity(null);
        return false;
    }

    protected boolean checkPreconditions() {
        if (ammunition == null) {
            player.sendMessage("You don't have any ammunition.");
            return false;
        } else if (!ammunition.isCompatible(player.getEquipment().getId(EquipmentSlot.WEAPON))) {
            player.sendMessage("You cannot use that ammunition with this weapon.");
            return false;
        } else if (!ammunition.isWeapon() && player.getEquipment().getId(EquipmentSlot.AMMUNITION) == 9419) {
            player.sendMessage("You cannot use grapple-tipped bolts for combat.");
            return false;
        }
        return true;
    }

    protected void addPoisonTask(final int delay) {
        final Item weapon = player.getEquipment().getItem(ammunition.isWeapon() ? EquipmentSlot.WEAPON.getSlot() : EquipmentSlot.AMMUNITION.getSlot());
        if (weapon == null) {
            return;
        }
        final String name = weapon.getName();
        if (!name.contains("(p")) {
            return;
        }
        if (Utils.random(3) == 0) {
            WorldTasksManager.schedule(() -> target.getToxins().applyToxin(ToxinType.POISON, name.contains("p++") ? 6 : name.contains("p+") ? 5 : 4, player), delay);
        }
    }

    protected void animate() {
        player.setAnimation(new Animation(getAttackAnimation(target instanceof Player, player.getEquipment().getAttackAnimation(player.getCombatDefinitions().getStyle()))));
    }

    protected final boolean canAttack() {
        if (!attackable()) return false;
        if (!target.canAttack(player)) {
            return false;
        }
        final RegionArea area = player.getArea();
        if ((area instanceof EntityAttackPlugin && !((EntityAttackPlugin) area).attack(player, target, this))) {
            return false;
        }
        if ((area instanceof PlayerCombatPlugin && !((PlayerCombatPlugin) area).processCombat(player, target, "Ranged")) || !player.getControllerManager().processPlayerCombat(target, "Ranged")) {
            return false;
        }
        return !target.isDying();
    }

    protected boolean outOfAmmo() {
        return ammunition.isWeapon() ? player.getWeapon() == null : player.getAmmo() == null;
    }

    protected void drawback() {
        if (ammunition.getDrawbackGfx() != null) {
            player.setGraphics(ammunition.getDrawbackGfx());
        }
    }

    protected void dropAmmunition(final int delay, final boolean destroy) {
        if (ammunition == null) {
            return;
        }
        final EquipmentSlot slot = ammunition.isWeapon() ? EquipmentSlot.WEAPON : EquipmentSlot.AMMUNITION;
        final int slotId = slot.getSlot();
        final Item ammo = player.getEquipment().getItem(slotId);
        if (ammo == null) {
            return;
        }
        final int dropChance = getAmmunitionDropChance();
        final int roll = Utils.random(100);
        final Equipment equipment = player.getEquipment();
        if (destroy || roll <= BREAK_CHANCE || roll <= (BREAK_CHANCE + dropChance)) {
            depleteAmmunition(equipment, slot, slotId, 1, ammo);
            if (destroy || roll <= BREAK_CHANCE) {
                return;
            }
        }
        if (roll <= (BREAK_CHANCE + dropChance)) {
            final Location location = new Location(target.getLocation());
            WorldTasksManager.schedule(() -> {
                final Item item = new Item(ammo.getId());
                final Duel duel = player.getDuel();
                if (duel != null) {
                    duel.getAmmunitions().get(player).add(item);
                } else {
                    World.spawnFloorItem(item, !World.isFloorFree(location, 1) ? new Location(player.getLocation()) : location, 20, player, player, 300, 500);
                }
            }, delay);
        }
    }

    protected void depleteAmmunition(Equipment equipment, EquipmentSlot slot, int slotId, int amount, Item ammo) {
        final int ammoAmount = ammo.getAmount();
        if (ammoAmount > 1) {
            ammo.setAmount(ammoAmount - amount);
        } else {
            equipment.set(slot, null);
            player.getCombatDefinitions().refresh();
        }
        equipment.refresh(slotId);
    }

    protected int fireProjectile() {
        return fireProjectile(zaryteSpec ? ZARYTE_SPEC_PROJ : ammunition.getProjectile());
    }

    protected int fireProjectile(final Projectile projectile) {
        if (projectile == null) {
            return 0;
        }
        final Location startTile = new Location(player.getLocation());
        World.sendProjectile(startTile, target, projectile);
        return projectile.getTime(player.getLocation().getAxisDistance(player.getSize(), target.getLocation(), target.getSize()));
    }

    protected int getAmmunitionDropChance() {
        int dropChance = 100;
        if(player.hasBoon(EndlessQuiver.class))
            dropChance = 16;

        final Item cape = player.getCape();
        if (cape != null && hasVorkathHeadEffect(cape)) {
            dropChance = Math.min(dropChance, AmmunitionPreserver.map.getOrDefault(22109, 100));
        }
        return Math.min(dropChance, AmmunitionPreserver.map.getOrDefault(player.getEquipment().getId(EquipmentSlot.CAPE), 100));
    }

    private boolean hasVorkathHeadEffect(final Item cape) {
        return (cape.getId() == ItemId.RANGING_CAPE || cape.getId() == ItemId.RANGING_CAPET || cape.getId() == ItemId.MAX_CAPE_13342) && cape.getNumericAttribute("vorkath head effect").intValue() != 0;
    }

    @Override
    protected int getAttackDistance() {
        final ItemDefinitions definitions = ItemDefinitions.get(player.getEquipment().getId(EquipmentSlot.WEAPON));
        if (definitions == null) {
            return 5;
        }
        return (player.getCombatDefinitions().getStyle() == 3 ? definitions.getLongAttackDistance() : definitions.getNormalAttackDistance());
    }

    protected int getWeaponSpeed() {
        int speed = 5;
        final ItemDefinitions definitions = ItemDefinitions.get(player.getEquipment().getId(EquipmentSlot.WEAPON));
        if (definitions != null) {
            speed = (player.getCombatDefinitions().getStyle() == 1 ? 8 : 9) - definitions.getAttackSpeed();
            if(definitions.getInterfaceVarbit() == 5 && !isPvp() && player.getBoonManager().hasBoon(DrawPartner.class))
                speed -= 1;
        }
        return adjustAttackSpeed(player, speed);
    }

    protected boolean initiateCombat(final Player player) {
        if (player.isDead() || player.isFinished() || player.isLocked() || player.isStunned()) {
            return false;
        }
        if (target.isDead() || target.isFinished() || target.isCantInteract()) {
            return false;
        }
        if (outOfAmmo()) {
            GameInterface.COMBAT_TAB.open(player);
            player.sendMessage("You've ran out of ammo!");
            return false;
        }
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = target.getSize();
        if (outOfAmmo()) {//edge case?
            GameInterface.COMBAT_TAB.open(player);
            return false;
        }
        final int viewDistance = player.getViewDistance();
        if (player.getPlane() != target.getPlane() || distanceX > size + viewDistance || distanceX < -1 - viewDistance || distanceY > size + viewDistance || distanceY < -1 - viewDistance) {
            return false;
        }
        if (target.getEntityType() == EntityType.PLAYER) {
            if (!player.isCanPvp() || !((Player) target).isCanPvp()) {
                player.sendMessage("You can't attack someone in a safe zone.");
                return false;
            }
        }
        if (player.isFrozen() || player.getMovementLock() > Utils.currentTimeMillis()) {
            return true;
        }
        if (colliding()) {
            player.getCombatEvent().process();
            return true;
        }
        if (handleDragonfireShields(player, false)) {
            if (!canAttack()) {
                return false;
            }
            handleDragonfireShields(player, true);
            player.getActionManager().addActionDelay(4);
            return true;
        }
        return pathfind();
    }

}

package com.zenyte.game.world.entity.player.action.combat;

import com.near_reality.game.item.CustomItemId;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.minigame.fightcaves.npcs.FightCavesNPC;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.content.tombsofamascut.AbstractTheatreNPC;
import com.zenyte.game.content.tombsofamascut.npc.IMovingWarden;
import com.zenyte.game.content.tombsofamascut.npc.IWardenCore;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.impl.CombatDummy;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.StatDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.StatType;
import com.zenyte.game.world.entity.npc.impl.Shade;
import com.zenyte.game.world.entity.npc.impl.slayer.Vampyre;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.area.TzHaarCity;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class CombatUtilities {
    public static final Animation CAST = new Animation(711);
    public static final Animation SURGE_CAST = new Animation(7855);
    public static final Animation DEBUFF_CAST = new Animation(710);
    public static final Animation ANCIENT_SINGLE_CAST = new Animation(1978);
    public static final Animation ANCIENT_MULTI_CAST = new Animation(1979);
    public static final Animation CHINCHOMPA_THROW_ANIM = new Animation(2779);
    public static final Graphics CHINCHOMPA_GFX = new Graphics(157, 0, 50);
    public static final Graphics TORAGS_SET_GFX = new Graphics(399);
    public static final Graphics GUTHANS_SET_GFX = new Graphics(398);
    public static final Graphics VERACS_SET_GFX = new Graphics(-1);
    public static final Graphics KARILS_SET_GFX = new Graphics(401, 0, 96);
    public static final Graphics AHRIMS_SET_GFX = new Graphics(400, 0, 96);
    private static final String[] SALVE_NPCS = new String[] {
            "aberrant spectre", "abhorrent spectre", "deviant spectre", "repugnant spectre", "ankou", "banshee",
            "screaming banshee", "twisted banshee", "crawling hand", "crushing hand", "ghast", "ghost",
            "greater skeleton hellhound", "mummy", "monkey zombie", "revenant imp", "revenant goblin", "revenant pyrefiend",
            "revenant hobgoblin", "revenant cyclops", "revenant hellhound", "revenant demon", "revenant ork", "revenant dark beast",
            "revenant knight", "revenant dragon", "shade", "loar shade", "phrin shade", "riyl shade", "asyn shade", "fiyr shade",
            "skeleton", "skeleton brute", "skeleton hellhound", "vet'ion", "skeleton mage", "skeleton thug", "skeleton warlord",
            "skogre", "summoned zombie", "tarn razorlor", "the draugen", "tortured soul", "undead chicken", "undead cow",
            "undead one", "vorkath", "zogre", "zombie", "zombie rat", "<col=00ffff>undead combat dummy</col>", "skeletal mystic",
            "pestilent bloat"
    };
    private static final String[] DEMON_NPCS = {
            "imp", "imp champion",
            "lesser demon", "lesser demon champion",
            "greater demon",
            "black demon", "porazdir",  "skotizo",
            "abyssal demon", "greater abyssal demon", "abyssal sire",
            "k'ril tsutsaroth", "balfrug kreeyath", "tstanon karlak", "zakl'n gritch",
            "ice demon", "duke sucullus"
    };
    private static final String[] OTHER_DEMON_NPCS = {
            "bloodveld", "insatiable bloodveld", "mutated bloodveld", "insatiable mutated bloodveld",
            "blood reaver",
            "demonic gorilla",
            "hellhound", "skeleton hellhound", "greater skeleton hellhound",
            "cerberus",
            "nechryael", "death spawn", "greater nechryael", "nechryarch", "chaotic death spawn"
    };
    public static final List<String> SALVE_AFFECTED_NPCS = Arrays.asList(SALVE_NPCS);
    private static final String[] BARROWS_NAMES = {"Verac's", "Ahrim's", "Karil's", "Dharok's", "Guthan's", "Torag's"};

    public static final float getInquisitorSetBoost(final Player player) {
        final int helm = player.getEquipment().getId(EquipmentSlot.HELMET);
        final int body = player.getEquipment().getId(EquipmentSlot.PLATE);
        final int legs = player.getEquipment().getId(EquipmentSlot.LEGS);
        int count = 0;
        if (helm == ItemId.INQUISITORS_GREAT_HELM) {
            count++;
        }
        if (body == ItemId.INQUISITORS_HAUBERK) {
            count++;
        }
        if (legs == ItemId.INQUISITORS_PLATESKIRT) {
            count++;
        }
        return count * 0.005f + (count == 3 ? 0.01f : 0.0f);
    }

    public static boolean dragonSlayerGlovesEquipped(Player player) {
        return player.getEquipment().getId(EquipmentSlot.HANDS) == 26255;
    }

    public static final boolean lanceEquipped(final int weaponId) {
        switch (weaponId) {
            case ItemId.DRAGON_HUNTER_LANCE:
            case CustomItemId.HOLY_GREAT_LANCE:
                return true;
            default:
                return false;
        }
    }

    public static final boolean isFireNPC(final NPC target) {
        final String name = target.getDefinitions().getName().toLowerCase();
        return name.contains("dragon") && !name.contains("baby") || name.equals("fire giant") || name.equals("pyrefiend") || name.equals("fire elemental") || name.contains("tzhaar-");
    }

    public static final boolean isCombatDummy(@NotNull final Entity target) {
        return target instanceof CombatDummy;
    }

    public static final boolean isAlwaysTakeMaxHit(@NotNull final Entity target, HitType hitType) {
        return target instanceof final NPC npc && npc.isAlwaysTakeMaxHit(hitType);
    }

    public static boolean isMovingWarden(@NotNull final Entity target) {
        return target instanceof IMovingWarden;
    }

    public static boolean isWardenCore(@NotNull final Entity target) {
        return target instanceof IWardenCore;
    }

    public static boolean isUndeadCombatDummy(@NotNull final Entity target) {
        return target instanceof CombatDummy && ((CombatDummy) target).getId() == NpcId.UNDEAD_COMBAT_DUMMY_16020;
    }

    public static boolean isRevenant(@NotNull final Entity target) {
        if (!(target instanceof NPC npc)) {
            return false;
        }
        final String name = npc.getDefinitions().getName().toLowerCase();
        return name.contains("revenant");
    }

    public static boolean applyForinthrySurge(final Player player, @NotNull final Entity target) {
        if (target instanceof NPC && player.getVariables().getTime(TickVariable.FORINTHRY_SURGE) > 0)
            return isRevenant(target);
        return false;
    }

    public static boolean applyPvmArenaBoost(@NotNull final Player player, @NotNull final Entity target) {
        if (!(target instanceof NPC))
            return false;
        else
            return player.getVariables().getPvmArenaBoosterTick() > 0;
    }

    public static final boolean isDraconic(@NotNull final Entity entity) {
        if (!(entity instanceof NPC)) {
            return false;
        }
        final NPC npc = (NPC) entity;
        final String name = npc.getDefinitions().getName().toLowerCase();
        return (!name.contains("elvarg") && !name.contains("revenant")) && (name.contains("dragon") || name.contains("wyvern") || name.contains("wyrm") || name.contains("drake") || name.contains("hydra") || name.contains("great olm") || name.contains("left claw") || name.contains("right claw") || name.contains("vorkath"));
    }

    public static final boolean isHydra(@NotNull final Entity entity) {
        if (!(entity instanceof NPC)) {
            return false;
        }
        final NPC npc = (NPC) entity;
        final String name = npc.getDefinitions().getName().toLowerCase();
        return name.contains("hydra");
    }

    public static boolean hasFullMeleeVoid(final Player player, final boolean eliteOnly) {
        final int helm = player.getEquipment().getId(EquipmentSlot.HELMET);
        if (helm != ItemId.VOID_MELEE_HELM && helm != 26477) {
            return false;
        }
        final int gloves = player.getEquipment().getId(EquipmentSlot.HANDS);
        if (gloves != ItemId.VOID_KNIGHT_GLOVES && gloves != 26467) {
            return false;
        }
        final int body = player.getEquipment().getId(EquipmentSlot.PLATE);
        final int legs = player.getEquipment().getId(EquipmentSlot.LEGS);
        if (eliteOnly) {
            return (body == ItemId.ELITE_VOID_TOP || body == 26469) && (legs == ItemId.ELITE_VOID_ROBE || legs == 26471);
        }
        return (body == ItemId.VOID_KNIGHT_TOP || body == 26463) && (legs == ItemId.VOID_KNIGHT_ROBE || legs == 26465);
    }

    public static boolean hasFullRangedVoid(final Player player, final boolean eliteOnly) {
        final int helm = player.getEquipment().getId(EquipmentSlot.HELMET);
        if (helm != ItemId.VOID_RANGER_HELM && helm != 26475) {
            return false;
        }
        final int gloves = player.getEquipment().getId(EquipmentSlot.HANDS);
        if (gloves != ItemId.VOID_KNIGHT_GLOVES && gloves != 26467) {
            return false;
        }
        final int body = player.getEquipment().getId(EquipmentSlot.PLATE);
        final int legs = player.getEquipment().getId(EquipmentSlot.LEGS);
        if (eliteOnly) {
            return (body == ItemId.ELITE_VOID_TOP || body == 26469) && (legs == ItemId.ELITE_VOID_ROBE || legs == 26471);
        }
        return (body == ItemId.VOID_KNIGHT_TOP || body == 26463) && (legs == ItemId.VOID_KNIGHT_ROBE || legs == 26465);
    }

    public static boolean hasFullMagicVoid(final Player player, final boolean eliteOnly) {
        final int helm = player.getEquipment().getId(EquipmentSlot.HELMET);
        if (helm != ItemId.VOID_MAGE_HELM && helm != 26473) {
            return false;
        }
        final int gloves = player.getEquipment().getId(EquipmentSlot.HANDS);
        if (gloves != ItemId.VOID_KNIGHT_GLOVES && gloves != 26467) {
            return false;
        }
        final int body = player.getEquipment().getId(EquipmentSlot.PLATE);
        final int legs = player.getEquipment().getId(EquipmentSlot.LEGS);
        if (eliteOnly) {
            return (body == ItemId.ELITE_VOID_TOP || body == 26469) && (legs == ItemId.ELITE_VOID_ROBE || legs == 26471);
        }
        return (body == ItemId.VOID_KNIGHT_TOP || body == 26463) && (legs == ItemId.VOID_KNIGHT_ROBE || legs == 26465);
    }

    public static final boolean hasFullBarrowsSet(final Player player, final String name) {
        final Item helm = player.getHelmet();
        final Item chest = player.getChest();
        final Item legs = player.getLegs();
        final Item weapon = player.getWeapon();
        if (helm == null || chest == null || legs == null || weapon == null) {
            return false;
        }
        final String helmName = helm.getName();
        final String chestName = chest.getName();
        final String legsName = legs.getName();
        final String weaponName = weapon.getName();
        if (helmName.endsWith(" 0") || chestName.endsWith(" 0") || legsName.endsWith(" 0") || weaponName.endsWith(" 0")) {
            return false;
        }
        return helmName.startsWith(name) && chestName.startsWith(name) && legsName.startsWith(name) && weaponName.startsWith(name);
    }

    public static boolean hasAnyBarrowsSet(final Player player) {
        for (final String name : BARROWS_NAMES) {
            if (hasFullBarrowsSet(player, name)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isWearingSerpentineHelmet(final Player player) {
        final int helm = player.getEquipment().getId(EquipmentSlot.HELMET);
        switch (helm) {
            case ItemId.SERPENTINE_HELM, ItemId.MAGMA_HELM, ItemId.TANZANITE_HELM:
                return true;
            default:
                return false;
        }
    }

    public static final boolean isWearingVenomDamagingEquipment(Player player) {
        final String weaponName = player.getEquipment().getName(EquipmentSlot.WEAPON).toLowerCase();
        if (weaponName.contains("trident of the swamp")) return true;
        if (weaponName.contains("toxic blowpipe")) return true;
        if (weaponName.contains("toxic staff of the dead")) return true;
        return false;
    }

    public static final boolean isWearingVenomMageEquipment(final Player player) {
        final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON);
        if (weapon == null) return false;
        final String weaponName = weapon.getDefinitions().getName();
        if (weaponName.equalsIgnoreCase("toxic staff of the dead")) return true;
        if (weaponName.equalsIgnoreCase("trident of the swamp") && weapon.hasCharges()) return true;
        return false;
    }

    public static final boolean hasFullJusticiarSet(final Player player) {
        final Item helm = player.getHelmet();
        final Item chest = player.getChest();
        final Item legs = player.getLegs();
        if (helm == null || chest == null || legs == null) {
            return false;
        }
        final String name = "Justiciar";
        return helm.getName().startsWith(name) && chest.getName().startsWith(name) && legs.getName().startsWith(name);
    }

    public static final float getDharokModifier(final Player player) {
        final int max = player.getMaxHitpoints();
        final int current = player.getHitpoints();
        if (current > max) return 1;
        return 1 + ((max - current) / 100.0F);
    }

    public static final boolean hasFullObisidian(final Player player) {
        final int helm = player.getEquipment().getId(EquipmentSlot.HELMET.getSlot());
        final int body = player.getEquipment().getId(EquipmentSlot.PLATE.getSlot());
        final int legs = player.getEquipment().getId(EquipmentSlot.LEGS.getSlot());
        return helm == 21298 && body == 21301 && legs == 21304;
    }

    public static final boolean isWieldingZurielsStaff(final Player player) {
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        return isWieldingZurielsStaff(weaponId);
    }

    public static final boolean isWieldingZurielsStaff(final int weaponId) {
        return weaponId == ItemId.ZURIELS_STAFF || weaponId == ItemId.ZURIELS_STAFF_23617;
    }

    public static final boolean canCastCrumbleUndead(@NotNull final NPC target) {
        final String name = target.getDefinitions().getName().toLowerCase();
        return name.contains("skeleton") || name.contains("zombie") || name.contains("ghost") || isShade(target) || name.contains("zombified spawn") || isUndeadCombatDummy(target);
    }

    static final boolean isShade(@NotNull final Entity target) {
        if (!(target instanceof NPC)) return false;
        final NPC npc = (NPC) target;
        final int id = npc.getId();
        return id == 5633 || id == 6740 || id == 7258 || ArrayUtils.contains(Shade.shades, id) || ArrayUtils.contains(Shade.shadows, id);
    }

    public static boolean isKerisAffected(@NotNull final Entity target) {
        if (!(target instanceof final NPC npc)) return false;
        final String name = npc.getDefinitions().getName().toLowerCase();
        return name.contains("kalphite") || name.contains("scarab") || name.contains("locust rider");
    }

    public static boolean isDemon(@NotNull final Entity target) {
        if (!(target instanceof final NPC npc))
            return false;
        final String name = npc.getDefinitions().getName().toLowerCase();
        return ArrayUtils.contains(DEMON_NPCS, name)
                || ArrayUtils.contains(OTHER_DEMON_NPCS, name)
                || isUndeadCombatDummy(target);
    }

    public static void delayHit(final NPC npc, int delay, final Entity target, final Hit... hits) {

        boolean melee = false;
        for (int i = hits.length - 1; i >= 0; i--) {
            if (hits[i].getHitType() == HitType.MELEE) {
                melee = true;
                break;
            }
        }
        if (melee) {
            if (delay == 0) {
                delay = -1;
            }
        }
        for (final Hit hit : hits) {
            target.scheduleHit(npc, hit, delay);
        }
    }

    public static void processHit(final Entity target, final Hit hit) {
        final long delay = target.getProtectionDelay();
        if (hit.getScheduleTime() < delay) {
            return;
        }
        if (hit.getSource() instanceof NPC) {
            final NPC npc = (NPC) hit.getSource();
            if (!npc.applyDamageFromHitsAfterDeath() && (npc.isDead() || npc.isFinished()) || target.isDead() || target.isFinished()) {
                return;
            }
            npc.handleOutgoingHit(target, hit);
            target.autoRetaliate(npc);
        }
        final Consumer<Hit> consumer2 = hit.getOnLandConsumer();
        if (consumer2 != null) {
            consumer2.accept(hit);
        }
        target.applyHit(hit);
        final Consumer<Hit> consumer = hit.getOnLandConsumer();
        if (consumer != null) {
            consumer.accept(hit);
        }
    }

    public static boolean isWithinMeleeDistance(final NPC npc, final Entity target) {
        final int distanceX = npc.getX() - target.getX();
        final int distanceY = npc.getY() - target.getY();
        final int npcSize = npc.getSize();
        final int targetSize = target.getSize();
        if (distanceX == -npcSize && distanceY == -npcSize || distanceX == targetSize && distanceY == targetSize || distanceX == -npcSize && distanceY == targetSize || distanceX == targetSize && distanceY == -npcSize) {
            return false;
        }
        return distanceX >= -npcSize && distanceX <= 1 && distanceY >= -npcSize && distanceY <= 1;
    }

    public static boolean isWithinMeleeDistance(final Location location, final int size, final Entity target) {
        final int distanceX = location.getX() - target.getX();
        final int distanceY = location.getY() - target.getY();
        final int targetSize = target.getSize();
        if (distanceX == -size && distanceY == -size || distanceX == targetSize && distanceY == targetSize || distanceX == -size && distanceY == targetSize || distanceX == targetSize && distanceY == -size) {
            return false;
        }
        return distanceX >= -size && distanceX <= 1 && distanceY >= -size && distanceY <= 1;
    }

    public static int getRandomMaxHit(final NPC npc, final int maxHit, final AttackType attackStyle, final Entity target) {
        return getRandomMaxHit(npc, maxHit, attackStyle, attackStyle, target);
    }

    public static int getRandomMaxHit(final NPC npc, int maxHit, final AttackType attackStyle, AttackType targetStyle, final Entity target) {
        double accuracy = getHitAccuracy(npc, target, attackStyle, targetStyle);
        if (accuracy < Utils.randomDouble()) {
            return 0;
        }
        int damage = Utils.random(maxHit);
        return clampMaxHit(npc, target, damage);
    }

    public static int getRandomMaxHit(NPC npc, Entity target, int maxHit, double accuracy) {
        if (accuracy < Utils.randomDouble()) {
            return 0;
        }
        //maxHit *= attackStyle.isMelee() ? target.getMeleePrayerMultiplier() : attackStyle.isRanged() ? target.getRangedPrayerMultiplier() : target.getMagicPrayerMultiplier();
        //This is an implementation for tick-eating. Some exceptional monsters such as the great olm and zuk from inferno will bypass this feature.
        int damage = Utils.random(maxHit);
        return clampMaxHit(npc, target, damage);
    }

    public static int clampMaxHit(NPC source, Entity target, int damage) {
        if (source.isTickEdible()) {
            if (damage > target.getHitpoints()) {
                damage = target.getHitpoints();
            }
        }
        return damage;
    }

    public static int getAccurateRandomMaxHit(NPC npc, Entity target, int maxHit) {
        int damage = Utils.random(maxHit);
        return clampMaxHit(npc, target, damage);
    }


    public static double getHitAccuracy(NPC npc, Entity target, AttackType attackStyle) {
        return getHitAccuracy(npc, target, attackStyle, attackStyle);
    }

    public static double getHitAccuracy(NPC npc, Entity target, AttackType attackStyle, AttackType targetStyle) {
        final StatDefinitions statDefs = npc.getCombatDefinitions().getStatDefinitions();
        final int effectiveLevel = statDefs.get(attackStyle.isRanged() ? StatType.RANGED : attackStyle.isMagic() ? StatType.MAGIC : StatType.ATTACK) + 8;
        final int accuracyBoost = statDefs.get(StatType.getAttackType(attackStyle));
        final int roll = (int) Math.floor((effectiveLevel * (accuracyBoost + 64)) * npc.getAccuracyMultiplier());
        final int targetRoll = PlayerCombat.getTargetDefenceRoll(npc, target, targetStyle);
        double accuracy;
        if (roll > targetRoll) {
            accuracy = 1 - (targetRoll + 2.0) / (2 * (roll + 1));
        } else {
            accuracy = roll / (2.0 * (targetRoll + 1.0));
        }
        return accuracy;
    }

    static boolean isDragonsBreath(PlayerCombat rangedCombat) {
        if (rangedCombat.target instanceof Player) {
            final Player p = (Player) rangedCombat.target;
            final int shield = p.getEquipment().getId(EquipmentSlot.SHIELD.getSlot());
            return p.getVariables().getTime(TickVariable.ANTIFIRE) <= 0
                    && p.getVariables().getTime(TickVariable.SUPER_ANTIFIRE) <= 0
                    && shield != 1540 && shield != 8282 && shield != 11283 && shield != 11284
                    && shield != CustomItemId.DRAGON_KITE;
        } else if (rangedCombat.target.getEntityType() == Entity.EntityType.NPC) {
            return !isFireNPC((NPC) rangedCombat.target);
        }
        return true;
    }

    public static boolean isTwistedBow(int itemId) {
        return     itemId == ItemId.TWISTED_BOW
                || itemId == ItemId.PURPLE_TWISTED_BOW
                || itemId == ItemId.BLUE_TWISTED_BOW
                || itemId == ItemId.WHITE_TWISTED_BOW
                || itemId == ItemId.RED_TWISTED_BOW;
    }

    public static boolean isElysianSpiritShield(int itemId) {
        return itemId == ItemId.ELYSIAN_SPIRIT_SHIELD || itemId == ItemId.ELYSIAN_SPIRIT_SHIELD_19559
                || itemId == ItemId.ELYSIAN_SPIRIT_SHIELD_OR;
    }

    public static boolean isVampyric(Entity entity) {
        return entity instanceof Vampyre || entity instanceof AbstractTheatreNPC;
    }

    public static boolean isTzhaar(Entity entity) {
        return entity instanceof FightCavesNPC || entity instanceof InfernoNPC || Optional.ofNullable(GlobalAreaManager.getNullableArea(TzHaarCity.class)).stream().anyMatch(tzHaarCity -> tzHaarCity.inside(entity.getPosition()));
    }

    public static boolean isSalveEffected(Entity entity) {
        if (entity instanceof NPC npc)
            return CombatUtilities.SALVE_AFFECTED_NPCS.contains(npc.getName());
        else
            return false;
    }
}

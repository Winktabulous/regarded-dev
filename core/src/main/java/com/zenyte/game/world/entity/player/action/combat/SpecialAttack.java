package com.zenyte.game.world.entity.player.action.combat;

import com.near_reality.game.content.crystal.recipes.chargeable.CrystalTool;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon;
import com.near_reality.game.content.custom.GodBow;
import com.near_reality.game.content.custom.PolyporeStaff;
import com.near_reality.game.item.CustomItemId;
import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.near_reality.game.world.entity.player.action.combat.ISpecialAttack;
import com.zenyte.game.content.boons.impl.HammerDown;
import com.zenyte.game.content.boons.impl.SliceNDice;
import com.zenyte.game.content.boss.phantommuspah.PhantomMuspah;
import com.zenyte.game.content.boss.wildernessbosses.callisto.Callisto;
import com.zenyte.game.content.chambersofxeric.npc.Tekton;
import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.prayer.PrayerManager;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Entity.EntityType;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Tinting;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.npc.combatdefs.StatType;
import com.zenyte.game.world.entity.npc.race.Demon;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.action.combat.special.BurningBarrageSpecial;
import com.zenyte.game.world.entity.player.action.combat.special.ScatteredAshesSpecial;
import com.zenyte.game.world.entity.player.action.combat.special.ScorchingShacklesSpecial;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import mgi.custom.Korasi;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.zenyte.game.item.ItemId.*;
import static com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript.*;
import static com.zenyte.game.world.entity.player.action.combat.SpecialType.*;
import static com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell.*;

/**
 * @author Kris | 1. jaan 2018 : 23:03.01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public enum SpecialAttack implements ISpecialAttack {
    QUICK_SMASH(AttackType.CRUSH, new int[]{4153, 12848, 24227, 24225, 20557}, 0, MELEE, new Animation(1667), new Graphics(340, 0, 96), (player, combat, target) -> {
        combat.delayHit(0, combat.getHit(player, target, 1, 1, 1, false));
        player.sendSound(QUICKSMASH_SOUND);
    }),

    CLEAVE(AttackType.SLASH, new int[] { DRAGON_LONGSWORD, DRAGON_LONGSWORD_CR, DRAGON_LONGSWORD_BH }, WEAPON_SPEED, MELEE, new Animation(1058), new Graphics(248, 0, 100), (player, combat, target) -> {
        var weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot());
        var accuracyModifier = Objects.equals(weaponId, DRAGON_LONGSWORD_BH) ? 1.25 : 1.0;
        var passiveModifier = Objects.equals(weaponId, DRAGON_LONGSWORD_BH) ? 1.25 : 1.15;

        if (combat.isSuccessful(player, target, 1, AttackType.SLASH)) {
            player.sendSound(CLEAVE_SOUND);
            combat.delayHit(0, combat.getHit(player, target, accuracyModifier, passiveModifier, 1, false));
            if (player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll()) {
                Hit hit = combat.getHit(player, target, 1.50F, 1.0F, 1.00F, false);
                hit.setDamage(hit.getDamage()/2);
                combat.delayHit(1, hit);
            }
        }
    }),

    DESCENT_OF_DARKNESS(AttackType.RANGED, new int[]{11235, 12765, 12766, 12767, 12768, 20408, DARK_BOW_BH}, 8, RANGED, new Animation(426), new Graphics(1112, 0, 95), (player, combat, target) -> {
        final int ammo = player.getEquipment().getId(EquipmentSlot.AMMUNITION.getSlot());
        final int bow = player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot());
        final boolean dragons = ammo == 11212 || ammo == 11227 || ammo == 11228 || ammo == 11229 || ammo == 11237;
        var firstArrow = DESCENT_OF_DARKNESS_FIRST_PROJ;
        var secondArrow = DESCENT_OF_DARKNESS_SECOND_PROJ;
        var maxBowDamage = Objects.equals(bow, DARK_BOW_BH) ? 7 : 5;
        var sound = DESCENT_OF_DRAGONS_SOUND;
        var modifier = 1.3;

        final int firstDelay = firstArrow.getTime(player.getLocation(), target.getMiddleLocation());
        final int secondDelay = secondArrow.getTime(player.getLocation(), target.getMiddleLocation());

        if (combat instanceof RangedCombat ranged) {
            ranged.dropAmmunition(firstDelay, false);
            ranged.dropAmmunition(secondDelay, false);
        }
        target.setGraphics(new Graphics(1100, 30 * firstDelay, 95));
        player.sendSound(DARK_BOW_DRAGON_LOCAL_FIRST_SOUND);
        player.sendSound(DARK_BOW_DRAGON_LOCAL_SECOND_SOUND);
        if (dragons) {
            maxBowDamage = Objects.equals(bow, DARK_BOW_BH) ? 10 : 8;
            firstArrow = DESCENT_OF_DRAGONS_FIRST_PROJ;
            secondArrow = DESCENT_OF_DRAGONS_SECOND_PROJ;
            modifier = 1.5;
        }
        World.sendProjectile(player, target, firstArrow);
        World.sendProjectile(player, target, secondArrow);
        final int firstClientTicks = firstArrow.getProjectileDuration(player.getLocation(), target.getMiddleLocation());
        final int secondClientTicks = secondArrow.getProjectileDuration(player.getLocation(), target.getMiddleLocation());
        World.sendSoundEffect(target, new SoundEffect(sound.getId(), sound.getRadius(), firstClientTicks));
        World.sendSoundEffect(target, new SoundEffect(sound.getId(), sound.getRadius(), secondClientTicks));
        final int max = Math.min(combat.getMaxHit(player, modifier, 1, false), 48);
        final int firstHit = Math.max(maxBowDamage, combat.getRandomHit(player, target, max, 1));
        final int secondHit = Math.max(maxBowDamage, combat.getRandomHit(player, target, max, 1));
        combat.delayHit(firstDelay, new Hit(player, firstHit, HitType.RANGED));
        combat.delayHit(secondDelay, new Hit(player, secondHit, HitType.RANGED));
    }),

    DUALITY(AttackType.RANGED, new int[]{22804, 22806, 22808, 22810, 22812, 22814}, WEAPON_SPEED, RANGED, null, null, (player, combat, target) -> {
        final int weapon = player.getWeapon().getId();
        final boolean poisonous = weapon == 22806 || weapon == 22808 || weapon == 22810;
        player.setAnimation(poisonous ? DUALITY_POISONOUS_ANIM : DUALITY_REGULAR_ANIM);
        player.sendSound(DUALITY_SOUND);
        final Projectile projectile = poisonous ? DUALITY_POISONOUS_PROJ : DUALITY_REGULAR_PROJ;
        final int delay = World.sendProjectile(player, target, projectile);
        combat.delayHit(delay, combat.getHit(player, target, 1, 1, 1, false), combat.getHit(player, target, 1, 1, 1, false));
    }),

    PUNCTURE(AttackType.SLASH, new int[]{1215, 1231, 5680, 5698, 20407, DRAGON_DAGGER_CR, DRAGON_DAGGER_PCR, DRAGON_DAGGER_PCR_28023, DRAGON_DAGGER_PCR_28025}, WEAPON_SPEED, MELEE, new Animation(1062), new Graphics(252, 0, 100), (player, combat, target) -> {
        player.sendSound(PUNCTURE_SOUND);
        if (target.getEntityType() == EntityType.PLAYER) {
            combat.delayHit(0, combat.getHit(player, target, 1.15, 1.15, 1, false), combat.getHit(player, target, 1.15, 1.15, 1, false));
        } else {
            combat.delayHit(0, combat.getHit(player, target, 1.15, 1.15, 1, false));
            combat.delayHit(1, combat.getHit(player, target, 1.15, 1.15, 1, false));
        }
        if(player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll()) {
            Hit hit = combat.getHit(player, target, 1.50F, 1.0F, 1.00F, false);
            hit.setDamage(hit.getDamage()/2);
            combat.delayHit(1, hit);
        }
    }),

    SEVER(AttackType.SLASH, new int[]{4587, 20000, 20406, DRAGON_SCIMITAR_CR}, WEAPON_SPEED, MELEE, new Animation(1872), new Graphics(347, 0, 100), (player, combat, target) -> {
        final Hit hit = combat.getHit(player, target, 1, 1.25F, 1, false);
        combat.delayHit(0, hit);
        player.sendSound(SEVER_SOUND);
        if (target.getEntityType() == EntityType.PLAYER && hit.getDamage() > 0) {
            final Player p2 = (Player) target;
            final PrayerManager prayers = p2.getPrayerManager();
            if (prayers.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                prayers.deactivatePrayer(Prayer.PROTECT_FROM_MAGIC);
            }
            if (prayers.isActive(Prayer.PROTECT_FROM_MISSILES)) {
                prayers.deactivatePrayer(Prayer.PROTECT_FROM_MISSILES);
            }
            if (prayers.isActive(Prayer.PROTECT_FROM_MELEE)) {
                prayers.deactivatePrayer(Prayer.PROTECT_FROM_MELEE);
            }
            p2.getTemporaryAttributes().put("SeverEffect", Utils.currentTimeMillis() + 5000);
        }
        if(player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll()) {
            Hit hit2 = combat.getHit(player, target, 1.50F, 1.0F, 1.00F, false);
            hit2.setDamage(hit2.getDamage()/2);
            combat.delayHit(1, hit2);
        }
    }),

    SHATTER(AttackType.SLASH, new int[] { DRAGON_MACE, DRAGON_MACE_CR, DRAGON_MACE_BH }, WEAPON_SPEED, MELEE, new Animation(1060), new Graphics(251, 0, 100), (player, combat, target) -> {
        player.sendSound(SHATTER_SOUND);
        var weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        var hit = combat.getHit(player, target, 1.25, 1.5, 1, false);
        var modifier = Objects.equals(weaponId, DRAGON_MACE_BH) ? 1.6 : 1.0;
        if (combat.isSuccessful(player, target, modifier, AttackType.CRUSH)) {
            combat.delayHit(0, hit);
            if (player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll()) {
                hit = combat.getHit(player, target, 1.50F, 1.0F, 1.00F, false);
                hit.setDamage(hit.getDamage() / 2);
                combat.delayHit(1, hit);
            }
        }
    }),

    SMASH(AttackType.CRUSH, new int[]{DRAGON_WARHAMMER, DRAGON_WARHAMMER_CR, DRAGON_WARHAMMER_20785, CustomItemId.HOLY_GREAT_WARHAMMER, ItemId.DRAGON_WARHAMMER_OR}, WEAPON_SPEED, MELEE, new Animation(1378), new Graphics(1292), (player, combat, target) -> {
        boolean hasDWHBoon = player.getBoonManager().hasBoon(HammerDown.class);
        double multiplier = hasDWHBoon ? 1.65 : 1.5;
        final Hit hit = combat.getHit(player, target, 1.75, multiplier, 1, false);
        if(hasDWHBoon && hit.getDamage() <= 4)
            hit.setDamage(5);
        combat.delayHit(0, hit);
        final boolean tekton = target instanceof Tekton;
        if (!tekton && hit.getDamage() == 0) {
            return;
        }
        target.drainSkill(SkillConstants.DEFENCE, hit.getDamage() == 0 ? 5.0 : 30.0);
    }),

    SNAPSHOT(AttackType.RANGED, new int[]{861, 20558, 12788}, 2, RANGED, new Animation(1074), new Graphics(250, 25, 95), (player, combat, target) -> {
        World.sendProjectile(player, target, SNAPSHOT_FIRST_PROJ);
        World.sendProjectile(player, target, SNAPSHOT_SECOND_PROJ);
        player.sendSound(SNAPSHOT_SOUND);
        combat.delayHit(SNAPSHOT_SECOND_PROJ.getTime(player.getLocation(), target.getLocation()), combat.getHit(player, target, 0.8, 1, 1, false), combat.getHit(player, target, 0.8, 1, 1, false));
    }),

    THE_JUDGEMENT(AttackType.SLASH, new int[] {ItemId.ARMADYL_GODSWORD, 20593, 20368}, WEAPON_SPEED, MELEE, null, new Graphics(1211), (player, combat, target) -> {
        final int weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        player.setAnimation(weaponId == 20368 ? ORNAMENT_JUDGEMENT_ANIM : JUDGEMENT_ANIM);
        combat.delayHit(0, combat.getHit(player, target, 2.15F, 1.1F, 1.25F, false));
        World.sendSoundEffect(player, THE_JUDGEMENT_SOUND);
        if(player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll()) {
            Hit hit = combat.getHit(player, target, 1.50F, 1.0F, 1.00F, false);
            hit.setDamage(hit.getDamage()/2);
            combat.delayHit(1, hit);
        }
    }),

    HEALING_BLADE(AttackType.SLASH, new int[]{11806, 20372}, WEAPON_SPEED, MELEE, null, new Graphics(1209), (player, combat, target) -> {
        final int weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        player.setAnimation(weaponId == 11806 ? HEALING_BLADE_ANIM : ORNAMENT_HEALING_BLADE_ANIM);
        final Hit hit = combat.getHit(player, target, 2, 1.1, 1, false);
        World.sendSoundEffect(player, HEALING_BLADE_SOUND);
        combat.delayHit(0, hit);
        int prayer = (int) (hit.getDamage() * 0.25);
        int hitpoints = (int) (hit.getDamage() * 0.5);
        if (hit.getDamage() <= 22) {
            hitpoints = 10;
            prayer = 5;
        }
        player.heal(hitpoints);
        player.getPrayerManager().restorePrayerPoints(prayer);
    }),

    PENANCE(AttackType.CRUSH, 13263, WEAPON_SPEED, MELEE, new Animation(3299), null, (player, combat, target) -> {
        final int max = player.getSkills().getLevelForXp(SkillConstants.PRAYER);
        final int points = player.getPrayerManager().getPrayerPoints();
        final int missing = (points > max) ? 0 : max - points;
        final int maxHit = combat.getMaxHit(player, 1, 1, false);
        final float strengthModifier = (missing == 0 ? 1 : (1 + (missing * 0.005F)));
        final int extra = (int) ((strengthModifier * maxHit) - maxHit);
        World.sendSoundEffect(player, PENANCE_SWORD_SOUND);
        World.sendSoundEffect(player, PENANCE_SPECIAL_SOUND);
        World.sendGraphics(PENANCE_GFX, new Location(target.getLocation()));
        combat.delayHit(target, 0, new Hit(player, combat.getRandomHit(player, target, maxHit + extra, 1), HitType.MELEE));
    }),

    TUMEKENS_LIGHT(AttackType.STAB, ItemId.KERIS_PARTISAN_OF_THE_SUN, WEAPON_SPEED, MELEE, TUMEKENS_LIGHT_ANIM, TUMEKENS_LIGHT_GFX, (player, combat, target) -> {
        player.getPrayerManager().drainPrayerPoints(50);
        player.setHitpoints((int) (player.getMaxHitpoints() * 1.20));
        player.getToxins().cureToxin(ToxinType.VENOM);
        player.getVariables().setRunEnergy(100);
        for (int i = 0, length = RESTORED_SKILLS.length; i < length; i++) {
            final int skill = RESTORED_SKILLS[i];
            final int realLevel = player.getSkills().getLevelForXp(skill);
            final int currentLevel = player.getSkills().getLevel(skill);
            if (currentLevel >= realLevel) continue;
            player.getSkills().setLevel(skill, realLevel);
        }
    }),

    //Double the attack speed, which is 7 + 1.
    WRATH_OF_AMASCUT(AttackType.STAB, ItemId.KERIS_PARTISAN_OF_CORRUPTION, 7, MELEE, WRATH_OF_AMASCUT_ANIM, WRATH_OF_AMASCUT_GFX, (player, combat, target) -> {
        //No need for 1.25 mod for max hit, if we hit successfully we increase the dmg by 25% with the attribute.
        final Hit hit = combat.getHit(player, target, 2.0F, 1.00F, 1.00F, false);
        if (hit.getDamage() > 0) {//TODO ToA check on this special attack
            target.getTemporaryAttributes().put(PlayerCombat.WRATH_OF_AMASCUT_ATT, WorldThread.getCurrentCycle() + 10);
        }
        combat.delayHit(0, hit);
    }),

    EVISCERATE(AttackType.SLASH, new int[] {ItemId.OSMUMTENS_FANG, ItemId.OSMUMTENS_FANG_OR}, WEAPON_SPEED, MELEE, EVISCERATE_ANIM, EVISCERATE_GFX, (player, combat, target) -> {
        World.sendSoundEffect(player, OSMUMTEN_FANG_SOUND);
        combat.delayHit(0, combat.getHit(player, target, 1.50F, 1.0F, 1.00F, false));
        if(player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll()) {
            Hit hit = combat.getHit(player, target, 1.50F, 1.0F, 1.00F, false);
            hit.setDamage(hit.getDamage()/2);
            combat.delayHit(1, hit);
        }
    }),

    SLICE_AND_DICE(AttackType.SLASH, new int[]{DRAGON_CLAWS, DRAGON_CLAWS_20784, DRAGON_CLAWS_OR, DRAGON_CLAWS_CR }, WEAPON_SPEED, MELEE, new Animation(7514, 10), new Graphics(1171, 10, 0), (player, combat, target) -> {
        int probability = -1;
        for (int i = 0; i < 4; i++) {
            if (!combat.isSuccessful(player, target, 1, AttackType.SLASH)) continue;
            probability = i;
            break;
        }
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        World.sendSoundEffect(player, SLICE_AND_DICE_SOUND);
        if (probability == -1) {
            final int boost = Utils.random(1);
            combat.delayHit(0, new Hit(player, 0, HitType.MISSED), new Hit(player, 0, HitType.MISSED));
            combat.delayHit(1, new Hit(player, boost, HitType.MELEE), new Hit(player, boost, HitType.MELEE));
            if(hasBonusHit && !(combat.getTarget() instanceof Player))
                combat.delayHit(2, new Hit(player, boost, HitType.MELEE));
            return;
        }
        final int ordinaryMaxHit = combat.getMaxHit(player, 1, 1, false);
        switch (probability) {
            case 0: {
                final int maxHit = combat.getMaxHit(player, 1, 1, false) - 1;
                final int minHit = (maxHit + 1) / 2;
                int firstHit = Utils.random(minHit, maxHit);
                int secondHit = firstHit / 2;
                int thirdHit = secondHit / 2;
                int fourthHit = Utils.random(1) == 0 ? thirdHit : (thirdHit + 1);
                combat.delayHit(0, new Hit(player, firstHit, HitType.MELEE), new Hit(player, secondHit, HitType.MELEE));
                combat.delayHit(1, new Hit(player, thirdHit, HitType.MELEE), new Hit(player, fourthHit, HitType.MELEE));
                if(hasBonusHit && !(combat.getTarget() instanceof Player))
                    combat.delayHit(2, new Hit(player, secondHit, HitType.MELEE));
                return;
            }
            case 1: {
                final int maxHit = (int) (ordinaryMaxHit * 7.0 / 8.0);
                final int minHit = (int) (ordinaryMaxHit * 3.0 / 8.0);
                int secondHit = Utils.random(minHit, maxHit);
                int thirdHit = secondHit / 2;
                int fourthHit = Utils.random(1) == 0 ? thirdHit : (thirdHit + 1);
                combat.delayHit(0, new Hit(player, 0, HitType.MELEE), new Hit(player, secondHit, HitType.MELEE));
                combat.delayHit(1, new Hit(player, thirdHit, HitType.MELEE), new Hit(player, fourthHit, HitType.MELEE));
                if(hasBonusHit && !(combat.getTarget() instanceof Player))
                    combat.delayHit(2, new Hit(player, thirdHit, HitType.MELEE));
                return;
            }
            case 2: {
                final int maxHit = (int) (ordinaryMaxHit * 3.0 / 4.0);
                final int minHit = (int) (ordinaryMaxHit * 1.0 / 4.0);
                int thirdHit = Utils.random(minHit, maxHit);
                int fourthHit = Utils.random(1) == 0 ? thirdHit : (thirdHit + 1);
                combat.delayHit(0, new Hit(player, 0, HitType.MELEE), new Hit(player, 0, HitType.MELEE));
                combat.delayHit(1, new Hit(player, thirdHit, HitType.MELEE), new Hit(player, fourthHit, HitType.MELEE));
                if(hasBonusHit && !(combat.getTarget() instanceof Player))
                    combat.delayHit(2, new Hit(player, thirdHit, HitType.MELEE));
                return;
            }
            default:
                final int maxHit = (int) (ordinaryMaxHit * 1.25);
                final int minHit = (int) (ordinaryMaxHit * 0.25);
                int fourthHit = Utils.random(minHit, maxHit);
                combat.delayHit(0, new Hit(player, 0, HitType.MISSED), new Hit(player, 0, HitType.MISSED));
                combat.delayHit(1, new Hit(player, 0, HitType.MISSED), new Hit(player, fourthHit, HitType.MELEE));
                if(hasBonusHit && !(combat.getTarget() instanceof Player))
                    combat.delayHit(2, new Hit(player, fourthHit, HitType.MELEE));
                return;
        }
    }),

    RAMPAGE(AttackType.SLASH, new int[] { DRAGON_BATTLEAXE, DRAGON_BATTLEAXE_CR }, WEAPON_SPEED, MELEE, new Animation(1056), new Graphics(246), (player, combat, target) -> {
        final int attack = player.getSkills().drainSkill(SkillConstants.ATTACK, 10.0, 0);
        final int defence = player.getSkills().drainSkill(SkillConstants.DEFENCE, 10.0, 0);
        final int magic = player.getSkills().drainSkill(SkillConstants.MAGIC, 10.0, 0);
        final int ranged = player.getSkills().drainSkill(SkillConstants.RANGED, 10.0, 0);
        final int total = (attack + defence + magic + ranged) / 4;
        final int strength = player.getSkills().getLevel(SkillConstants.STRENGTH);
        final int trueStrength = player.getSkills().getLevelForXp(SkillConstants.STRENGTH);
        final int level = Math.min(strength, trueStrength);
        player.sendSound(RAMPAGE_SOUND);
        player.getSkills().setLevel(SkillConstants.STRENGTH, level + 10 + total);
        player.setForceTalk(RAMPAGE_FORCETALK);
    }),

    SHOVE(AttackType.SLASH, new int[]{1249, 1263, 3176, 5716, 5730, 11824, 11889, DRAGON_SPEAR_CR, DRAGON_SPEAR_PCR, DRAGON_SPEAR_PCR_28045, DRAGON_SPEAR_PCR_28047}, WEAPON_SPEED, MELEE, new Animation(1064), new Graphics(253, 0, 96), (player, combat, target) -> {
        player.sendSound(SHOVE_SOUND);
        if (target.getSize() == 1) {
            int x = target.getX();
            int y = target.getY();
            int px = player.getX();
            int py = player.getY();
            if (target instanceof Player tp) {
                final long lastShoveTick = tp.getNumericTemporaryAttribute("Last shove push").longValue();
                if (WorldThread.WORLD_CYCLE < lastShoveTick) {
                    return;
                }
                tp.getTemporaryAttributes().put("Last shove push", WorldThread.WORLD_CYCLE + 7);
            }
            if (px > target.getX()) {
                x--;
            } else if (px < target.getX()) {
                x++;
            }
            if (py > target.getY()) {
                y--;
            } else if (py < target.getY()) {
                y++;
            }
            player.getActionManager().forceStop();
            player.setFaceLocation(target.getLocation());
            target.getWalkSteps().clear();
            target.setAnimation(null);
            target.performDefenceAnimation(player);
            target.addWalkSteps(x, y, 1, true);
            target.lock(5);
            target.setGraphics(SHOVE_GFX);
        }
    }),

    ABYSSAL_PUNCTURE(AttackType.STAB,
        new int[] {
            ABYSSAL_DAGGER, ABYSSAL_DAGGER_P, ABYSSAL_DAGGER_P_13269, ABYSSAL_DAGGER_P_13271,
            ABYSSAL_DAGGER_BH, ABYSSAL_DAGGER_BHP, ABYSSAL_DAGGER_BHP_27865, ABYSSAL_DAGGER_BHP_27867
        },
        WEAPON_SPEED, MELEE, new Animation(1062), new Graphics(1283), (player, combat, target) -> {
            var bhVariant = List.of(ABYSSAL_DAGGER_BH, ABYSSAL_DAGGER_BHP, ABYSSAL_DAGGER_BHP_27865, ABYSSAL_DAGGER_BHP_27867);
            var weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot());
            var modifier = bhVariant.contains(weaponId) ? 0.95 : 0.85;

            player.sendSound(ABYSSAL_PUNCTURE_SOUND);
            final Hit hit = combat.getHit(player, target, 1.25, modifier, 1, false);
            final int damage = combat.getMaxHit(player, modifier, 1, false);
            var secondHit = hit.getDamage() == 0 ? new Hit(player, 0, HitType.MELEE) : new Hit(player, Utils.random(1, damage), HitType.MELEE);
            if (target.getEntityType() == EntityType.PLAYER) {
                combat.delayHit(0, hit, secondHit);
            } else {
                combat.delayHit(0, hit);
                combat.delayHit(1, secondHit);
            }
        }),

    BINDING_TENTACLE(AttackType.SLASH, new int[]{ItemId.ABYSSAL_TENTACLE, 26484, ItemId.LIME_WHIP}, WEAPON_SPEED, MELEE, new Animation(1658), null, (player, combat, target) -> {
        combat.delayHit(0, combat.getHit(player, target, 1, 1, 1, false));
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, combat.getHit(player, target, 1, 1, 1, false));
        player.sendSound(BINDING_TENTACLE_SOUND);
        WorldTasksManager.schedule(() -> {
            if (Utils.random(1) == 0) {
                target.getToxins().applyToxin(ToxinType.POISON, 4, player);
            }
            target.freeze(8);
            target.setGraphics(WHIP_GFX);
        });
    }),

    ENERGY_DRAIN(AttackType.SLASH, new int[]{4151, ItemId.LAVA_WHIP, 20405, 12773, 12774, 26482}, WEAPON_SPEED, MELEE, new Animation(1658), null, (player, combat, target) -> {
        combat.delayHit(0, combat.getHit(player, target, 1.25, 1, 1, false));
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, combat.getHit(player, target, 1, 1, 1, false));
        WorldTasksManager.schedule(() -> {
            if (target.getEntityType() == EntityType.PLAYER) {
                final Player p2 = (Player) target;
                final int targetEnergy = (int) p2.getVariables().getRunEnergy();
                final int siphon = (int) (targetEnergy * 0.1);
                p2.getVariables().setRunEnergy(p2.getVariables().getRunEnergy() - siphon);
                player.getVariables().setRunEnergy((player.getVariables().getRunEnergy() + siphon) > 100 ? 100 : (player.getVariables().getRunEnergy() + siphon));
            }
            target.setGraphics(WHIP_GFX);
        });
    }),

    ARMADYL_EYE(AttackType.RANGED, 11785, 5, RANGED, new Animation(4230), null, (player, combat, target) -> {
        World.sendProjectile(player, target, ARMADYL_EYE_PROJ);
        player.sendSound(ARMADYL_EYE_SOUND);
        combat.delayHit(ARMADYL_EYE_PROJ.getTime(player.getLocation(), target.getLocation()), combat.getHit(player, target, 2, 1, 1, false));
    }),

    CHAINHIT(AttackType.RANGED, 805, 4, RANGED, new Animation(1068), new Graphics(257, 0, 90), (player, combat, target) -> {
        final List<Entity> possibleTargets = player.getPossibleTargets(EntityType.BOTH);
        possibleTargets.remove(target);
        possibleTargets.removeIf(e -> e instanceof Player && !player.canHit((Player) e));
        World.sendProjectile(player, target, CHAINHIT_PROJ);
        final int delay = CHAINHIT_PROJ.getTime(player.getLocation(), target.getLocation());
        if (combat instanceof RangedCombat ranged) {
            ranged.dropAmmunition(delay, false);
        }
        final int maxhit = combat.getMaxHit(player, 1, 1, false);
        combat.delayHit(delay, new Hit(player, combat.getRandomHit(player, target, maxhit, 1), HitType.RANGED));
        player.sendSound(THROWNAXE_SOUND);
        if (player.isMultiArea()) {
            WorldTasksManager.scheduleOrExecute(new WorldTask() {
                private int count = 0;
                private Entity t;
                private int delay;

                @Override
                public void run() {
                    if (count == 4 || possibleTargets.isEmpty() || player.getCombatDefinitions().getSpecialEnergy() < 10) {
                        stop();
                        return;
                    }
                    count++;
                    final Entity previousTarget = t == null ? target : t;
                    for (int i = 0; i < possibleTargets.size(); i++) {
                        t = possibleTargets.get(i);
                        if (t.getLocation().getDistance(previousTarget.getLocation()) <= 3 || !t.isMultiArea()) {
                            break;
                        }
                        if (i == possibleTargets.size()) {
                            stop();
                            return;
                        }
                    }
                    delay = CHAINHIT_CHAIN_PROJ.getTime(previousTarget.getLocation(), t.getLocation());
                    possibleTargets.remove(t);
                    player.getCombatDefinitions().setSpecialEnergy(player.getCombatDefinitions().getSpecialEnergy() - 10);
                    World.sendProjectile(previousTarget, t, CHAINHIT_CHAIN_PROJ);
                    combat.delayHit(t, (delay < 0 ? (delay + 1) : delay), new Hit(player, combat.getRandomHit(player, target, maxhit, 1), HitType.RANGED).putAttribute("chainhit_prev", t).setSpecialAttack());
                }
            }, CHAINHIT_PROJ.getTime(player.getLocation(), target.getLocation()) - 1, 0);
        }
    }),

    SWEEP(AttackType.SLASH, new int[] { DRAGON_HALBERD_CR, 3204, 13080, 13081, 13082, 13083, 13084, 13085, 13086, 13087, 13088, 13089, 13090, 13091, 13092, 13093, 13094, 13095, 13096, 13097, 13098, 13099, 13100, 13101, CrystalWeapon.Halberd.INSTANCE.getProductItemId(), CrystalWeapon.Halberd.INSTANCE.getInactiveId()}, WEAPON_SPEED, MELEE, new Animation(1203), null, (player, combat, target) -> {
        final int weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        final Location center = target.getMiddleLocation();
        final int x = target.getSize() == 1 ? target.getX() : (int) ((center.getX() + player.getX()) / 2.0F);
        final int y = target.getSize() == 1 ? target.getY() : (int) ((center.getY() + player.getY()) / 2.0F);
        player.sendSound(SWEEP_SOUND);
        Graphics gfx = weaponId == 3204 ? SWEEP_DRAGON_SOUTH_GFX : SWEEP_CRYSTAL_SOUTH_GFX;
        if (x > player.getX()) {
            gfx = weaponId == 3204 ? SWEEP_DRAGON_EAST_GFX : SWEEP_CRYSTAL_EAST_GFX;
        } else if (x < player.getX()) {
            gfx = weaponId == 3204 ? SWEEP_DRAGON_WEST_GFX : SWEEP_CRYSTAL_WEST_GFX;
        } else if (y < player.getY()) {
            gfx = weaponId == 3204 ? SWEEP_DRAGON_NORTH_GFX : SWEEP_CRYSTAL_NORTH_GFX;
        }
        final Location tile = new Location(x, y, player.getPlane());
        World.sendGraphics(gfx, tile);
        final Hit primaryHit = combat.getHit(player, target, 1, 1.1, 1, false);
        combat.delayHit(target, 0, primaryHit);
        if (target.getSize() > 1) {
            final Hit secondaryHit = combat.getHit(player, target, 0.75, 1.1, 1, false);
            combat.delayHit(target, 0, secondaryHit);
        }
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, combat.getHit(player, target, 1, 1, 1, false));
        if (target.getSize() == 1) {
            final List<Entity> possibleTargets = player.getPossibleTargets(EntityType.BOTH);
            possibleTargets.remove(target);
            possibleTargets.removeIf(e -> e instanceof Player && !player.canHit((Player) e));
            for (final Entity e : possibleTargets) {
                if (e == null || !e.isMultiArea()) {
                    continue;
                }
                if (e.getSize() == 1 && e.getLocation().withinDistance(tile, 1)) {
                    final Hit hit = combat.getHit(player, e, 1, 1.1, 1, false);
                    combat.delayHit(e, 0, hit);
                }
            }
        }
    }),

    POWERSTAB(AttackType.SLASH, new int[]{DRAGON_2H_SWORD, DRAGON_2H_SWORD_20559, DRAGON_2H_SWORD_CR}, WEAPON_SPEED, MELEE, new Animation(3157), new Graphics(1214), (player, combat, target) -> {
        final List<Entity> possibleTargets = player.getPossibleTargets(EntityType.BOTH);
        final List<Entity> toRemove = new ArrayList<>();
        for (final Entity e : possibleTargets) {
            if (!e.getLocation().withinDistance(player.getLocation(), 1) || (e instanceof Player && !player.canHit((Player) e))) {
                toRemove.add(e);
            }
        }
        player.sendSound(POWERSTAB_SOUND);
        possibleTargets.remove(target);
        possibleTargets.removeAll(toRemove);
        combat.delayHit(target, 0, combat.getHit(player, target, 1, 1, 1, false));
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, combat.getHit(player, target, 1, 1, 1, false));
        int count = 0;
        if (player.isMultiArea()) {
            for (final Entity e : possibleTargets) {
                if (!e.isMultiArea()) {
                    continue;
                }
                combat.delayHit(e, 0, combat.getHit(player, e, 1, 1, 1, false));
                if (++count == 14) {
                    break;
                }
            }
        }
    }),

    ROCK_KNOCKER(AttackType.SLASH, new int[]{11920, 12797, 13243, 20014, 13244, CrystalTool.Pickaxe.INSTANCE.getProductItemId(), CrystalTool.Pickaxe.INSTANCE.getInactiveId()}, 0, MELEE, null, null, (player, combat, target) -> {
        final int weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        if (weaponId == 11920 || weaponId == 12797 || weaponId == 20014) {
            player.setAnimation(ROCK_KNOCKER_ANIM);
        } else if (CrystalTool.Pickaxe.INSTANCE.is(weaponId)) {
            player.setAnimation(ROCK_KNOCKER_CRYSTAL_ANIM);
        } else if (weaponId == 13243) {
            player.setAnimation(ROCK_KNOCKER_INFERNAL_ANIM);
        }
        player.setForceTalk(ROCK_KNOCKER_FORCETALK);
        World.sendSoundEffect(player, ROCK_KNOCKER_SOUND);
        final int maxLevel = player.getSkills().getLevelForXp(SkillConstants.MINING);
        final int currentLevel = player.getSkills().getLevel(SkillConstants.MINING);
        final int level = Math.min(currentLevel, maxLevel);
        player.getSkills().setLevel(SkillConstants.MINING, level + 3);
    }),

    LUMBER_UP(AttackType.SLASH, new int[]{6739, 13241, 20011, 13242, CrystalTool.Axe.INSTANCE.getProductItemId(), CrystalTool.Axe.INSTANCE.getInactiveId()}, 0, MELEE, new Animation(2876), null, (player, combat, target) -> {
        final int weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        player.setGraphics(new Graphics(CrystalTool.Axe.INSTANCE.is(weaponId) ? 1688 : 479));
        player.setForceTalk(LUMBER_UP_FORCETALK);

        final int maxLevel = player.getSkills().getLevelForXp(SkillConstants.WOODCUTTING);
        final int currentLevel = player.getSkills().getLevel(SkillConstants.WOODCUTTING);
        final int level = Math.min(currentLevel, maxLevel);
        World.sendSoundEffect(player, LUMBER_UP_SOUND);
        player.getSkills().setLevel(SkillConstants.WOODCUTTING, level + 3);
    }),

    FISHSTABBER(AttackType.STAB, new int[]{21028, 21031, 21033, CrystalTool.Harpoon.INSTANCE.getProductItemId(), CrystalTool.Harpoon.INSTANCE.getInactiveId()}, 0, MELEE, null, null, (player, combat, target) -> {
        final int weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        if (weaponId == 21028) {
            player.setAnimation(FISHSTABBER_DRAGON_ANIM);
            player.setGraphics(new Graphics(246));
        } else if (CrystalTool.Harpoon.INSTANCE.is(weaponId)) {
            player.setAnimation(FISHSTABBER_CRYSTAL_ANIM);
            player.setGraphics(new Graphics(1688));
        } else {
            player.setAnimation(FISHSTABBER_INFERNAL_ANIM);
            player.setGraphics(new Graphics(246));
        }
        player.setForceTalk(FISHSTABBER_FORCETALK);
        final int maxLevel = player.getSkills().getLevelForXp(SkillConstants.FISHING);
        final int currentLevel = player.getSkills().getLevel(SkillConstants.FISHING);
        final int level = Math.min(currentLevel, maxLevel);
        player.getSkills().setLevel(SkillConstants.FISHING, level + 3);
    }),

    MOMENTUM_THROW(AttackType.RANGED, new int[]{20849, 21207}, 4, RANGED, new Animation(7521), new Graphics(1317, 0, 96), (player, combat, target) -> {
        World.sendProjectile(player, target, MOMENTUM_THROW_PROJ);
        final int delay = MOMENTUM_THROW_PROJ.getTime(player.getLocation(), target.getLocation());
        combat.delayHit(delay, combat.getHit(player, target, 1.25, 1, 1, false));
        player.sendSound(THROWNAXE_SOUND);
        if (combat instanceof RangedCombat ranged) {
            ranged.dropAmmunition(delay, true);
        }
    }),

    WILD_STAB(AttackType.STAB, new int[] { 21009, 21206, DRAGON_SWORD_CR}, WEAPON_SPEED, MELEE, new Animation(7515), new Graphics(1369, 0, 96), (player, combat, target) -> {
        final Hit hit = combat.getHit(player, target, 1.25, 1.25, 1, true);
        player.sendSound(WILD_STAB_SOUND);
        combat.delayHit(0, hit);
    }),

    UNLEASH(AttackType.STAB, new int[]{ItemId.DRAGON_HASTA, ItemId.DRAGON_HASTAKP, ItemId.DRAGON_HASTAP, ItemId.DRAGON_HASTAP_22737, ItemId.DRAGON_HASTAP_22740}, WEAPON_SPEED, MELEE, new Animation(7515), new Graphics(1369, 0, 96), (player, combat, target) -> {
        final int energy = player.getCombatDefinitions().getSpecialEnergy();
        final int intervals = energy / 5;
        player.getCombatDefinitions().setSpecialEnergy(0);
        final Hit hit = combat.getHit(player, target, 1 + (intervals * 0.05), 1 + (intervals * 0.025), 1, false);
        player.sendSound(WILD_STAB_SOUND);
        combat.delayHit(0, hit);
    }),

    WARSTRIKE(AttackType.SLASH, new int[]{11804, 20370, 20782, 21060}, WEAPON_SPEED, MELEE, null, new Graphics(1212), (player, combat, target) -> {
        final int weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        player.setAnimation(weaponId == 20370 ? ORNAMENT_WARSTRIKE_ANIM : WARSTRIKE_ANIM);
        final Hit hit = combat.getHit(player, target, 2, 1.1, 1.1, false);
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, combat.getHit(player, target, 1, 1, 1, false));
        combat.delayHit(0, hit);
        World.sendSoundEffect(player, WARSTRIKE_SOUND);
        final boolean tekton = target instanceof Tekton;
        int damage = Math.max(tekton ? 10 : 0, hit.getDamage());
        damage -= target.drainSkill(SkillConstants.DEFENCE, damage);
        damage -= target.drainSkill(SkillConstants.STRENGTH, damage);
        damage -= target.drainSkill(SkillConstants.PRAYER, damage);
        damage -= target.drainSkill(SkillConstants.ATTACK, damage);
        damage -= target.drainSkill(SkillConstants.MAGIC, damage);
        target.drainSkill(SkillConstants.RANGED, damage);
    }),

    ICE_CLEAVE(AttackType.SLASH, new int[]{ItemId.ZAMORAK_GODSWORD, ItemId.ZAMORAK_GODSWORD_OR}, WEAPON_SPEED, MELEE, null, new Graphics(1210), (player, combat, target) -> {
        final int weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        player.setAnimation(weaponId == ItemId.ZAMORAK_GODSWORD ? ICE_CLEAVE_ANIM : ORNAMENT_ICE_CLEAVE_ANIM);
        final Hit hit = combat.getHit(player, target, 2, 1.1, 1, false);
        World.sendSoundEffect(player, ICE_CLEAVE_SOUND);
        combat.delayHit(0, hit);
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, combat.getHit(player, target, 2, 1, 1, false));
        if (hit.getDamage() > 0) {
            target.resetWalkSteps();
            target.freezeWithNotification(32);
            target.setGraphics(ICE_CLEAVE_GFX);
        }
    }),

    SUNDER(AttackType.CRUSH, new int[] {BARRELCHEST_ANCHOR, BARRELCHEST_ANCHOR_BH}, WEAPON_SPEED, MELEE, new Animation(5870), new Graphics(1027), (player, combat, target) -> {
        var weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        var modifier = Objects.equals(weaponId, BARRELCHEST_ANCHOR_BH) ? 1.25 : 1.1;
        var hit = combat.getHit(player, target, 2, modifier, 1, false);
        var damage = hit.getDamage();
        var random = Utils.random(3);

        combat.delayHit(0, hit);
        World.sendSoundEffect(player, SUNDER_SOUND);
        if (Objects.equals(weaponId, BARRELCHEST_ANCHOR_BH)) {
            var skillsToDrain = List.of(SkillConstants.DEFENCE, SkillConstants.ATTACK, SkillConstants.RANGED, SkillConstants.MAGIC);
            for (var skill : skillsToDrain) {
                if (target instanceof Player foe)
                    if (foe.getSkills().getLevel(skill) > 1) {
                        foe.drainSkill(skill, (int) (damage * 0.1));
                        break;
                    }
            }
        }
        else
            target.drainSkill(random < 2 ? random : random == 2 ? SkillConstants.MAGIC : SkillConstants.RANGED, (int) (damage * 0.1));
    }),

    HAMMER_BLOW(AttackType.CRUSH, 21742, WEAPON_SPEED, MELEE, new Animation(1378), new Graphics(1450), (player, combat, target) -> {
        final Hit hit = combat.getHit(player, target, 1.5, 1, 1, false);
        hit.setDamage(hit.getDamage() + 5);
        combat.delayHit(0, hit);
        player.sendSound(HAMMER_BLOW_SOUND);
    }),

    IMPALE(AttackType.SLASH, 3101, WEAPON_SPEED, MELEE, new Animation(923), new Graphics(274, 0, 96), (player, combat, target) -> {
        combat.delayHit(0, combat.getHit(player, target, 1.1, 1.1, 1, false));
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, combat.getHit(player, target, 1.1, 1.1, 1, false));

        player.sendSound(IMPALE_SOUND);
    }),

    SANCTUARY(AttackType.SLASH, 35, 0, MELEE, new Animation(1057), new Graphics(247), (player, combat, target) -> {
        final int maxLevel = player.getSkills().getLevelForXp(SkillConstants.DEFENCE);
        final int currentLevel = player.getSkills().getLevel(SkillConstants.DEFENCE);
        final int level = Math.min(currentLevel, maxLevel);
        player.sendSound(SANCTUARY_SOUND);
        player.getSkills().setLevel(SkillConstants.DEFENCE, level + 8);
        player.setForceTalk(SANCTUARY_FORCETALK);
    }),

    ENHANCED_SANCTUARY(AttackType.SLASH, 32161, 0, MELEE, new Animation(1057), new Graphics(247), (player, combat, target) -> {
        player.sendSound(SANCTUARY_SOUND);
        player.setForceTalk(SANCTUARY_FORCETALK);
        new Consumable.Boost(SkillConstants.DEFENCE, 0.15f, 2).apply(player);
        player.putBooleanAttribute("enhanced_excalibur", true);
        WorldTasksManager.schedule(new TickTask() {
            int heals = 5;

            @Override
            public void run() {
                ticks++;
                if (ticks == 4) {
                    ticks = 0;
                    player.heal(10);
                    heals--;
                    if (heals <= 0) {
                        stop();
                        player.putBooleanAttribute("enhanced_excalibur", false);
                    }
                }
            }
        }, 0, 0);
    }),

    WEAKEN(AttackType.STAB, new int[]{6746, 19675}, WEAPON_SPEED, MELEE, new Animation(2890), new Graphics(483), (player, combat, target) -> {
        combat.delayHit(0, combat.getHit(player, target, 1, 1, 1, false));
        player.sendSound(WEAKEN_SOUND);
        final boolean demon = target.getEntityType() == EntityType.NPC && Demon.isDemon((NPC) target, false);
        //Weaken will stack additively upon successful hits, unlike the dragon warhammer, which stacks multiplicatively.
        //Two successful special attacks thus reduce an opponent's Strength, Attack and Defence by 10%, or 20% if it is a demon.
        final boolean playerTarget = target instanceof Player;
        final int normalAttack = playerTarget ? ((Player) target).getSkills().getLevelForXp(SkillConstants.ATTACK) : NPCCDLoader.get(((NPC) target).getId()).getStatDefinitions().get(StatType.ATTACK);
        final int normalStrength = playerTarget ? ((Player) target).getSkills().getLevelForXp(SkillConstants.STRENGTH) : NPCCDLoader.get(((NPC) target).getId()).getStatDefinitions().get(StatType.STRENGTH);
        final int normalDefence = playerTarget ? ((Player) target).getSkills().getLevelForXp(SkillConstants.DEFENCE) : NPCCDLoader.get(((NPC) target).getId()).getStatDefinitions().get(StatType.DEFENCE);
        final double percentage = demon ? 0.1 : 0.05;
        final int attackToDrain = (int) (normalAttack * percentage);
        final int strengthToDrain = (int) (normalStrength * percentage);
        final int defenceToDrain = (int) (normalDefence * percentage);
        target.drainSkill(SkillConstants.ATTACK, attackToDrain);
        target.drainSkill(SkillConstants.STRENGTH, strengthToDrain);
        target.drainSkill(SkillConstants.DEFENCE, defenceToDrain);
    }),

    EMBERLIGHT_WEAKEN(AttackType.STAB, EMBERLIGHT, WEAPON_SPEED, MELEE, new Animation(11138), new Graphics(2810), (player, combat, target) -> {
        combat.delayHit(0, combat.getHit(player, target, 1, 1, 1, false));
        player.sendSound(WEAKEN_SOUND);
        final var demon = target.getEntityType() == EntityType.NPC && Demon.isDemon((NPC) target, false);
        final var percentage = demon ? 0.15 : 0.05;
        int normalAttack = -1;
        int normalStrength = -1;
        int normalDefence = -1;
        if (target instanceof Player targetPlayer) {
            normalAttack = targetPlayer.getSkills().getLevelForXp(SkillConstants.ATTACK);
            normalStrength = targetPlayer.getSkills().getLevelForXp(SkillConstants.STRENGTH);
            normalDefence = targetPlayer.getSkills().getLevelForXp(SkillConstants.DEFENCE);
        }
        else if (target instanceof NPC npc) {
            normalAttack = NPCCDLoader.get(npc.getId()).getStatDefinitions().get(StatType.ATTACK);
            normalStrength = NPCCDLoader.get(npc.getId()).getStatDefinitions().get(StatType.STRENGTH);
            normalDefence = NPCCDLoader.get(npc.getId()).getStatDefinitions().get(StatType.DEFENCE);
        }
        final var attackToDrain = (int) (normalAttack * percentage);
        final var strengthToDrain = (int) (normalStrength * percentage);
        final var defenceToDrain = (int) (normalDefence * percentage);

        target.drainSkill(SkillConstants.ATTACK, attackToDrain);
        target.drainSkill(SkillConstants.STRENGTH, strengthToDrain);
        target.drainSkill(SkillConstants.DEFENCE, defenceToDrain);
    }),

    SCATTERED_ASHES(AttackType.MAGIC, PURGING_STAFF, WEAPON_SPEED, MAGIC, new Animation(-1), new Graphics(-1), new ScatteredAshesSpecial()),
    SCORCHING_SHACKLES(AttackType.RANGED, SCORCHING_BOW, WEAPON_SPEED, RANGED, new Animation(-1), new Graphics(-1), new ScorchingShacklesSpecial()),
    BURNING_BARRAGE(AttackType.MELEE, BURNING_CLAWS, WEAPON_SPEED, MELEE, new Animation(11140), new Graphics(2814), new BurningBarrageSpecial()),

    BACKSTAB(AttackType.STAB, new int[]{8872, 8874, 8876, 8878}, WEAPON_SPEED, MELEE, new Animation(4198), new Graphics(704), (player, combat, target) -> {
        double accuracy = 1;
        if (target.getAttackedBy() != player) {
            accuracy = 2;
        }
        player.sendSound(BACKSTAB_SOUND);
        final Hit hit = combat.getHit(player, target, accuracy, 1, 1, false);
        combat.delayHit(0, hit);
        target.drainSkill(SkillConstants.DEFENCE, hit.getDamage());
    }),

    LIQUEFY(AttackType.SLASH, 11037, WEAPON_SPEED, MELEE, new Animation(6118), new Graphics(1048, 0, 96), (player, combat, target) -> {
        final Hit hit = combat.getHit(player, target, 2, 1, 1, false);
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, combat.getHit(player, target, 2, 1, 1, false));
        combat.delayHit(0, hit);
        final int quarter = hit.getDamage() / 4;
        player.getSkills().boostSkill(SkillConstants.ATTACK, quarter);
        player.getSkills().boostSkill(SkillConstants.STRENGTH, quarter);
        player.getSkills().boostSkill(SkillConstants.DEFENCE, quarter);
    }),

    FAVOUR_OF_THE_WAR_GOD(AttackType.CRUSH, ItemId.ANCIENT_MACE, WEAPON_SPEED, MELEE, new Animation(6147), new Graphics(1052), (player, combat, target) -> {
        final Hit hit = combat.getHit(player, target, 1, 1, 1, true);
        combat.delayHit(0, hit);
        int amount = hit.getDamage();
        final int amt = player.getPrayerManager().getPrayerPoints();
        player.sendSound(FAVOUR_OF_THE_WAR_GOD_SOUND);
        player.getPrayerManager().setPrayerPoints((Math.min(amt, player.getSkills().getLevelForXp(SkillConstants.PRAYER))) + amount);
        if (target.getEntityType() == EntityType.PLAYER) {
            final Player p2 = (Player) target;
            final int prayer = p2.getPrayerManager().getPrayerPoints();
            amount = Math.min(prayer, hit.getDamage());
            p2.getPrayerManager().drainPrayerPoints(amount);
        } else if (target instanceof final PhantomMuspah muspah && muspah.getShieldHitBar() != null) {
            hit.setHitType(HitType.SHIELD_DOWN);
        }
    }),

    SARADOMINS_LIGHTNING(AttackType.SLASH, ItemId.SARADOMIN_SWORD, WEAPON_SPEED, MELEE, new Animation(1132), new Graphics(1213), (player, combat, target) -> {
        target.setGraphics(SARADOMINS_LIGHTNING_GFX);
        final int meleeDamage = combat.getRandomHit(player, target, combat.getMaxHit(player, 1.1, 1, false), 1, AttackType.MAGIC);
        final int magicDamage = Utils.random(1, 16);
        combat.delayHit(0, new Hit(player, meleeDamage, HitType.MELEE), new Hit(player, magicDamage, HitType.MAGIC));
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, new Hit(player, meleeDamage, HitType.MELEE), new Hit(player, magicDamage, HitType.MAGIC));
        World.sendSoundEffect(player, SARADOMINS_LIGHTNING_SWORD_SOUND);
        World.sendSoundEffect(player, SARADOMINS_LIGHTNING_SOUND);
    }),

    BLESSED_SARADOMINS_LIGHTNING(AttackType.SLASH, new int[]{12808, 12809}, WEAPON_SPEED, MELEE, new Animation(1133), new Graphics(1213), (player, combat, target) -> {
        target.setGraphics(SARADOMINS_LIGHTNING_GFX);
        World.sendGraphics(BLESSED_SARADOMINS_LIGHTNING_GFX, new Location(target.getLocation()));
        final int meleeDamage = combat.getRandomHit(player, target, combat.getMaxHit(player, 1.25, 1, false), 1, AttackType.MAGIC);
        combat.delayHit(0, new Hit(player, meleeDamage, HitType.MELEE));
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player)) {
            final int meleeDamage2 = combat.getRandomHit(player, target, combat.getMaxHit(player, 1.25, 1, false), 1, AttackType.MAGIC);
            combat.delayHit(1, new Hit(player, meleeDamage2, HitType.MELEE));
        }
    }),

    SHIELD_BASH(AttackType.CRUSH, 21015, WEAPON_SPEED, MELEE, new Animation(7511), new Graphics(1336, 0, 30), (player, combat, target) -> {
        final List<Entity> possibleTargets = player.getPossibleTargets(EntityType.BOTH);
        possibleTargets.remove(target);
        combat.delayHit(0, combat.getHit(player, target, 1.2, 1, 1, false));
        World.sendSoundEffect(player, SHIELD_BASH_SOUND);
        int count = 1;
        if (player.isMultiArea()) {
            for (final Entity e : possibleTargets) {
                if (!e.getLocation().withinDistance(player.getLocation(), 5) || !e.isMultiArea() || (e instanceof Player && !player.canHit((Player) e))) {
                    continue;
                }
                combat.delayHit(e, 0, combat.getHit(player, e, 1.2, 1, 1, false));
                if (++count == 10) {
                    break;
                }
            }
        }
    }),

    POWERSHOT(AttackType.RANGED, new int[]{859, 10284}, 5, RANGED, new Animation(426), new Graphics(250, 0, 95), (player, combat, target) -> {
        World.sendProjectile(player, target, SNAPSHOT_FIRST_PROJ);
        final int damage = combat.getMaxHit(player, 1, 1, false);
        final int delay = SNAPSHOT_FIRST_PROJ.getTime(player.getLocation(), target.getLocation());
        combat.delayHit(delay, new Hit(player, Utils.random(1, damage), HitType.RANGED));
        player.sendSound(POWERSHOT_SOUND);
        if (combat instanceof RangedCombat ranged) {
            ranged.dropAmmunition(delay, false);
        }
    }),

    SNIPE(AttackType.RANGED, 8880, 4, RANGED, new Animation(426), null, (player, combat, target) -> {
        World.sendProjectile(player, target, SNIPE_PROJ);
        double accuracy = 1;
        if (target.getAttackedBy() != player) {
            accuracy = 2;
        }
        final Hit hit = combat.getHit(player, target, accuracy, 1, 1, false);
        final int delay = SNIPE_PROJ.getTime(player.getLocation(), target.getLocation());
        combat.delayHit(delay, hit);
        target.drainSkill(SkillConstants.DEFENCE, hit.getDamage());
        player.sendSound(SNIPE_SOUND);
        if (combat instanceof RangedCombat ranged) {
            ranged.dropAmmunition(delay, false);
        }
    }),

    SOULSHOT(AttackType.RANGED, 6724, 4, RANGED, new Animation(426), new Graphics(472, 0, 90), (player, combat, target) -> {
        World.sendProjectile(player, target, SOULSHOT_PROJ);
        target.setGraphics(SOULSHOT_GFX);
        final int maxhit = combat.getMaxHit(player, 1, 1, false);
        final int damage = Utils.random(1, maxhit);
        player.sendSound(SOULSHOT_SOUND);
        combat.delayHit(SOULSHOT_PROJ.getTime(player.getLocation(), target.getLocation()), new Hit(player, damage, HitType.RANGED));
        target.drainSkill(SkillConstants.MAGIC, damage);
    }),

    TOXIC_SIPHON(AttackType.RANGED, 12926, 3, RANGED, new Animation(5061), null, (player, combat, target) -> {
        World.sendProjectile(player, target, TOXIC_SIPHON_PROJ);
        final Hit hit = combat.getHit(player, target, 1, 1.5, 1, false);
        final int delay = TOXIC_SIPHON_PROJ.getTime(player.getLocation(), target.getLocation());
        combat.delayHit(delay, hit);
        final int clientCycles = TOXIC_SIPHON_PROJ.getProjectileDuration(player.getLocation(), target.getLocation());
        player.sendSound(TOXIC_SIPHON_DART_SOUND);
        player.sendSound(new SoundEffect(TOXIC_SIPHON_FART_SOUND.getId(), 0, clientCycles));
        player.heal(hit.getDamage() / 2);
        if (target.getEntityType() == EntityType.NPC) {
            WorldTasksManager.schedule(() -> player.getActionManager().setActionDelay(player.getActionManager().getActionDelay() - 1));
        }
    }),

    CONCENTRATED_SHOT(AttackType.RANGED, new int[]{ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR}, 6, RANGED, new Animation(7222), null, (player, combat, target) -> {
        if (player.getAmmo() == null) {
            return;
        }
        final AmmunitionDefinition defs = AmmunitionDefinitions.getConcentratedDefinitions(player.getAmmo().getId());
        if (defs == null) {
            return;
        }
        if (defs.getProjectile() == null) {
            return;
        }
        final Projectile projectile = defs.getProjectile();
        final int clientCycles = projectile.getProjectileDuration(player.getLocation(), target.getLocation());
        target.setGraphics(new Graphics(344, clientCycles, 146));
        player.sendSound(CONCENTRATED_SHOT_SOUND);
        World.sendProjectile(player, target, defs.getProjectile());
        final int delay = defs.getProjectile().getTime(player.getLocation(), target.getLocation());
        combat.delayHit(delay, combat.getHit(player, target, 1.25, 1.25, 1, false));
        if (combat instanceof RangedCombat ranged) {
            ranged.dropAmmunition(delay, true);
        }
    }),

    POWER_OF_DEATH(AttackType.MAGIC, PowerOfDeathVariableTask.POWER_OF_DEATH_ITEMS, 0, MAGIC, null, null, (player, combat, target) -> {
        final int weaponId = player.getWeapon() == null ? -1 : player.getWeapon().getId();
        if (weaponId == ItemId.STAFF_OF_LIGHT) {
            player.setAnimation(SOL_POWER_OF_DEATH_ANIM);
            player.setGraphics(SOL_POWER_OF_DEATH_GFX);
        } else if (weaponId == ItemId.STAFF_OF_THE_DEAD) {
            player.setAnimation(POWER_OF_DEATH_ANIM);
            player.setGraphics(POWER_OF_DEATH_GFX);
        } else if (weaponId == ItemId.STAFF_OF_BALANCE) {
            player.setAnimation(POWER_OF_DEATH_ANIM);
            player.setGraphics(SOB_POWER_OF_DEATH_GFX);
        } else {
            player.setAnimation(TOXIC_POWER_OF_DEATH_ANIM);
            player.setGraphics(TOXIC_POWER_OF_DEATH_GFX);
        }
        player.sendMessage(Colour.RS_GREEN.wrap("Spirits of deceased evildoers offer you their protection."));
        World.sendSoundEffect(player, POWER_OF_DEATH_SOUND);
        player.getVariables().schedule(100, TickVariable.POWER_OF_DEATH);
    }),

    SPEAR_WALL(AttackType.CRUSH, new int[] { VESTAS_SPEAR, VESTAS_SPEAR_BH }, WEAPON_SPEED, MELEE, new Animation(8184), new Graphics(1627), (player, combat, target) -> {
        final List<Entity> possibleTargets = !player.isMultiArea()
                ? Arrays.asList(ArrayUtils.toArray(target))
                : player.getPossibleTargets(EntityType.BOTH, 1);
        final int hash = player.getLocation().getPositionHash();
        int count = 0;
        player.addImmunity(HitType.MELEE, 4800);
        player.sendSound(SPEAR_WALL_SOUND);
        for (final Entity possibleTarget : possibleTargets) {
            if (possibleTarget instanceof Player && !player.canHit((Player) possibleTarget)) {
                continue;
            }
            final Location location = possibleTarget.getLocation();
            final int distanceX = player.getX() - target.getX();
            final int distanceY = player.getY() - target.getY();
            final int size = target.getSize();
            if (possibleTarget != target && (location.getPositionHash() == hash || distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)) {
                continue;
            }
            combat.delayHit(possibleTarget, 0, combat.getHit(player, target, 1, 1, 1, false));
            if (++count >= 16) break;
        }
    }),

    FEINT(AttackType.STAB, new int[] { VESTAS_LONGSWORD, VESTAS_LONGSWORD_BH }, WEAPON_SPEED, MELEE, new Animation(246), null, (player, combat, target) -> {
        //Accuracy modifier set to 4x as target's defence is reduced by 75%, which is an identical comparison.
        final Hit hit = combat.getHit(player, target, 4, 1, 1, false);
        if (hit.getDamage() > 0) {
            hit.setDamage(((int) (combat.getMaxHit(player, 1, 1, false) * 0.2F)) + hit.getDamage());
        }
        player.sendSound(FEINT_SOUND);
        combat.delayHit(target, 0, hit);
    }),

    SWH_SMASH(AttackType.CRUSH, new int[] { STATIUSS_WARHAMMER, STATIUSS_WARHAMMER_BH }, WEAPON_SPEED, MELEE, new Animation(1378), new Graphics(844), (player, combat, target) -> {
        final Hit hit = combat.getHit(player, target, 1, 1, 1, false);
        if (hit.getDamage() > 0) {
            hit.setDamage(((int) (combat.getMaxHit(player, 1, 1, false) * 0.2F)) + hit.getDamage());
            target.drainSkill(SkillConstants.DEFENCE, 30.0F);
        }
        player.sendSound(SMASH_SOUND);
        combat.delayHit(target, 0, hit);
    }),

    HAMSTRING(AttackType.RANGED, new int[] { MORRIGANS_THROWING_AXE, MORRIGANS_THROWING_AXE_BH }, 4, RANGED, new Animation(929), new Graphics(1626, 0, 90), (player, combat, target) -> {
        final int delay = World.sendProjectile(player, target, HAMSTRING_PROJ);
        final Hit hit = combat.getHit(player, target, 1, 1, 1, false);
        if (hit.getDamage() > 0) {
            hit.setDamage(((int) (combat.getMaxHit(player, 1, 1, false) * 0.2F)) + hit.getDamage());
        }
        if (combat instanceof RangedCombat ranged) {
            ranged.dropAmmunition(delay, true);
        }
        player.sendSound(HAMSTRING_SOUND);
        WorldTasksManager.schedule(() -> {
            combat.delayHit(target, -1, hit);
            if (target instanceof Player) {
                ((Player) target).getVariables().schedule(100, TickVariable.HAMSTRUNG);
                ((Player) target).sendMessage("You've been hamstrung! For the next minute, your run energy will drain " +
                        "6x faster.");
            }
        }, delay);
    }),

    PHANTOM_STRIKE(AttackType.RANGED, new int[] { MORRIGANS_JAVELIN, MORRIGANS_JAVELIN_BH }, 4, RANGED, new Animation(806), new Graphics(1621, 0, 90), (player, combat, target) -> {
        final int delay = World.sendProjectile(player, target, PHANTOM_STRIKE_PROJ);
        final Hit hit = combat.getHit(player, target, 1, 1, 1, false);
        if (combat instanceof RangedCombat ranged) {
            ranged.dropAmmunition(delay, true);
        }
        WorldTasksManager.schedule(() -> {
            combat.delayHit(target, -1, hit);
            if (target instanceof Player) {
                WorldTasksManager.schedule(new TickTask() {
                    private final Player targetPlayer = (Player) target;
                    private int damage = hit.getDamage();

                    @Override
                    public void run() {
                        if (target.isFinished() || target.isDead() || target.isLocked()) {
                            stop();
                            return;
                        }
                        targetPlayer.sendMessage(ticks++ == 0 ? "You start to bleed as a payload of the javelin strike." : "You continue to bleed as a payload of the javelin strike.");
                        targetPlayer.applyHit(new Hit(player, Math.min(damage, 5), HitType.REGULAR));
                        if ((damage -= 5) <= 0) {
                            stop();
                        }
                    }
                }, 3, 3);
            }
        }, delay);
    }),

    ANNIHILATE(AttackType.RANGED, new int[] { DRAGON_CROSSBOW, DRAGON_CROSSBOW_CR }, 5, RANGED, new Animation(4230), null, (player, combat, target) -> {
        World.sendProjectile(player, target, ANNIHILATE_PROJ);
        final int delay = SNIPE_PROJ.getTime(player.getLocation(), target.getMiddleLocation());
        WorldTasksManager.schedule(() -> target.setGraphics(ANNIHILATE_GFX), delay);
        player.sendSound(ANNIHILATE_START_SOUND);
        final int clientTicks = SNIPE_PROJ.getProjectileDuration(player.getLocation(), target.getMiddleLocation());
        player.sendSound(new SoundEffect(ANNIHILATE_END_SOUND.getId(), ANNIHILATE_END_SOUND.getRadius(), clientTicks));
        combat.attackTarget(combat.getMultiAttackTargets(player), originalTarget -> {
            final Hit hit = combat.getHit(player, combat.target, 1, combat.target == originalTarget ? 1.2F : 1, 1, false);
            combat.delayHit(combat.target, delay, hit);
            if (combat.target == originalTarget) {
                return hit.getDamage() > 0;
            }
            return true;
        });
    }),

    KORASI(AttackType.SLASH, new int[]{Korasi.ITEM_ID}, WEAPON_SPEED, MELEE, new Animation(32764), new Graphics(32767), (player, combat, target) -> {
        final List<Entity> possibleTargets = player.getPossibleTargets(EntityType.BOTH);
        final List<Entity> toRemove = new ArrayList<>();
        for (final Entity e : possibleTargets) {
            if (!e.getLocation().withinDistance(target.getLocation(), 1) || (e instanceof Player && !player.canHit((Player) e))) {
                toRemove.add(e);
            }
        }
        player.sendSound(new SoundEffect(2945, 5, 25));
        possibleTargets.remove(target);
        possibleTargets.removeAll(toRemove);

        target.setGraphics(new Graphics(32766, 30, 0));
        final int halvedMax = combat.getMaxHit(player, 1.0, 1.0, false) / 2;
        final int addition = !possibleTargets.isEmpty() ? Utils.random(halvedMax) : halvedMax;
        final Hit hit = combat.getHit(player, target, 1, 1, 1, false);
        final int damage = hit.getDamage() + addition + Utils.random(0, 5);
        hit.setHitType(HitType.MAGIC);
        hit.setDamage(damage);
        combat.delayHit(target, 0, hit);
        boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        if(hasBonusHit && !(combat.getTarget() instanceof Player))
            combat.delayHit(1, combat.getHit(player, target, 1, 1, 1, false));

        int count = 1;
        for (Entity e : possibleTargets) {
            if (!e.isMultiArea()) {
                continue;
            }
            e.setGraphics(new Graphics(32766, 30 * (count + 1), 0));
            final Hit targetHit = combat.getHit(player, e, 1, 1, 1, false);
            targetHit.setDamage(count == 1 ? (damage / 2) : (damage / 4));
            combat.delayHit(e, count, targetHit);
            if (++count >= 3) break;
        }
    }),

    BLOOD_SACRIFICE(AttackType.SLASH, new int[]{ItemId.ANCIENT_GODSWORD}, WEAPON_SPEED, MELEE, null, new Graphics(1996), (player, combat, target) -> {
        player.setAnimation(new Animation(9171));
        Hit hit = combat.getHit(player, target, 2, 1.1F, 1, false);
        combat.delayHit(0, hit);

        if (hit.getDamage() > 0) {
            // target has 8 ticks to run away from the attacker, if it doesnt it gets hit
            // with 25 damage and the attacker is healed for 25.
            var duration = 8;
            WorldTasksManager.schedule(() -> {
                if (player.getLocation().getDistance(target.getLocation()) > 5) {
                    return;
                }
                var damage = 25;
                target.setGraphics(new Graphics(2003));
                var modifiedDamage = damage;
                if (target instanceof Callisto) {
                    modifiedDamage = 0;
                }
                combat.delayHit(0, new Hit(player, modifiedDamage, HitType.REGULAR));
                boolean hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
                if(hasBonusHit && !(combat.getTarget() instanceof Player))
                    combat.delayHit(1, combat.getHit(player, target, 2, 1, 1, false));
                player.heal(damage);
            }, duration);
            // additionally for the duration of the special effect the target glows red.
            target.setTinting(new Tinting(0, 6, 28, 112, 0, duration * 30));
        }
    }),

  ZARYTE_CROSSBOW(AttackType.RANGED, new int[] { ItemId.ZARYTE_CROSSBOW }, -2, RANGED, null, null, (player, combat, target) -> {
    player.addTemporaryAttribute("zaryte_cbow_spec", 1);
    // NOTE: Zaryte Crossbow Special Effect is handled on the ranged combat logic
  }),

  DAWNBRINGER(AttackType.MAGIC, new int[] { ItemId.DAWNBRINGER }, WEAPON_SPEED, MAGIC, new Animation(1167), new Graphics(1543, 0, 92), (player, combat, target) -> {
    World.sendProjectile(player, target, DAWNBRINGER_PROJ);
      combat.delayHit(DAWNBRINGER_PROJ.getTime(player.getLocation(), target.getLocation()), new Hit(player, Utils.random(75, 150), HitType.SHIELD));
  }),
    INVOCATE(AttackType.MAGIC, ItemId.ELDRITCH_NIGHTMARE_STAFF, WEAPON_SPEED, MELEE, new Animation(8532), new Graphics(1762), (player, combat, target) -> {
        player.cancelCombat();
        target.setGraphics(INVOCATE_GFX);
        final int max = (int) (Utils.interpolate(39, 50, 75, 99, Math.min(player.getSkills().getLevel(SkillConstants.MAGIC), 99)) * MagicCombat.getDamageBoost(player));
        final int hitRoll = combat.getRandomHit(player, target, max, 1);
        final Hit hit = new Hit(player, hitRoll, HitType.MAGIC);
        combat.delayHit(2, hit);

        final int amount = hit.getDamage() * 50 / 100;
        final int amt = player.getPrayerManager().getPrayerPoints();
        player.getPrayerManager().setPrayerPoints(Math.min(120, amt + amount));
    }),
    IMMOLATE(AttackType.MAGIC, ItemId.VOLATILE_NIGHTMARE_STAFF, WEAPON_SPEED, MELEE, new Animation(8532), new Graphics(1760), (player, combat, target) -> {
        player.cancelCombat();
        target.setGraphics(IMMOLATE_GFX);
        final int max = (int) (Utils.interpolate(50, 66, 75, 99, Math.min(player.getSkills().getLevel(SkillConstants.MAGIC), 99)) * MagicCombat.getDamageBoost(player));
        final int hitRoll = combat.getRandomHit(player, target, max, 1.75);
        final Hit hit = new Hit(player, hitRoll, HitType.MAGIC);
        combat.delayHit(2, hit);
  }),

    DISRUPT(AttackType.SLASH, new int[] { 27690 }, WEAPON_SPEED, MELEE, new Animation(1378), null, (player, combat, target) -> {
        player.sendSound(new SoundEffect(2945, 5, 25));
        target.setGraphics(new Graphics(2363, 0, 0));

        // Compute max hit with melee formula
        final int max = combat.getMaxHit(player, 1.0, 1.0, true);

        // Set the damage to a guaranteed amount between 50% - 150% of the max computed above
        final double percentageModifier = (CombatUtilities.isCombatDummy(target) ? 150 : Utils.random(50, 150)) / 100d;
        int adjustedDamage = (int) (max * percentageModifier);
        if (target instanceof Player) {
            if (((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
                // Praying against does reduce damage, but its still guaranteed!
                adjustedDamage *= 0.50;
            }
        }

        final Hit hit = new Hit(player, adjustedDamage, HitType.MAGIC);
        combat.delayHit(target, 0, hit);
    }),

    BEAR_DOWN(AttackType.MELEE, new int[] { 27660 }, WEAPON_SPEED, MELEE, new Animation(9963), new Graphics(2342, 0, 90), (player, combat, target) -> {
        // Double accuracy while calculating hit
        final Hit hit = combat.getHit(player, target, 2, 1, 1, false);
        combat.delayHit(0, hit);

        if (target instanceof Player) {
            if (hit.isAccurate()) {
                target.freeze(6);
                ((Player) target).getSkills().drainSkill(SkillConstants.AGILITY, 20);
            }
        }
    }),

    SWARM(AttackType.RANGED, new int[] { 27655 }, WEAPON_SPEED, RANGED, new Animation(9964), new Graphics(2354, 0, 90), (player, combat, target) -> {
        // Compute the max hit using the ranged formula and cap it at 40% since the special attack hits 4 times.
        final int max = combat.getMaxHit(player, 1.0, 1.0, false);
        final int adjustedMax = (int) (max * 0.40d);

        // Calculate the first hit independently of the others because the impact should be consistent for the first hit
        // and the impact graphics also needs to happen on the first hit only.
        int firstHitDamage = combat.getRandomHit(player, target, adjustedMax, 2.0);
        Hit firstHit = new Hit(firstHitDamage, HitType.RANGED).onLand(it -> {
            target.setGraphics(new Graphics(2355));
            if (Utils.roll(10)) {
                target.getToxins().applyToxin(ToxinType.POISON, 4, target);
            }
        });
        firstHit.setSource(player);
        combat.delayHit(2, firstHit);

        // Randomize the remaining 3 hits
        for (int i = 0; i < 3; i++) {
            int damage = combat.getRandomHit(player, target, adjustedMax, 2.0);
            Hit hit = new Hit(damage, HitType.RANGED).onLand(it -> {
                // No idea what the chance for poison is but we'll set it relatively low since there's 4 hit chances
                if (Utils.roll(10)) {
                    target.getToxins().applyToxin(ToxinType.POISON, 4, target);
                }
            });
            hit.setSource(player);
            int delay = Utils.random(2, 3);
            combat.delayHit(delay, hit);
        }
    }),

    CONDEMN(AttackType.MAGIC, new int[] { 27665 }, WEAPON_SPEED, MAGIC, new Animation(9963), new Graphics(2338), (player, combat, target) -> {
        player.cancelCombat();
        Projectile projectile = new Projectile(2339, 23, 15, 51, 23, 25, 64, 5);
        int delay = World.sendProjectile(player, target, projectile);
        Hit hit = combat.getHit(player, target, 1.50, 1.50, 1.0, false);
        //standard is 2337!
        combat.delayHit(delay, hit);

        if (target instanceof Player playerTarget) {
            if (hit.isAccurate()) {
                playerTarget.getSkills().drainPercentageStatically(SkillConstants.DEFENCE, 0.15);
                playerTarget.getSkills().drainPercentageStatically(SkillConstants.MAGIC, 0.15);
            }
        }
    }),

  ;

    public static final Map<Integer, ISpecialAttack> SPECIAL_ATTACKS = new HashMap<>();

    static {
        for (final SpecialAttack att : values())
            register(att);
        register(GodBow.Saradomin.INSTANCE);
        register(GodBow.Bandos.INSTANCE);
        register(GodBow.Zamorak.INSTANCE);
        register(GodBow.Armadyl.INSTANCE);
        register(PolyporeStaff.Special.INSTANCE);
    }

    private static void register(ISpecialAttack specialAttack) {
        for (int id : specialAttack.getWeapons()) {
            SPECIAL_ATTACKS.put(id, specialAttack);
        }
    }

    private final int[] weapons;
    private final int delay;
    private final SpecialType type;
    private final Animation animation;
    private final Graphics graphics;
    private final SpecialAttackScript attack;
    private final AttackType attackType;

    SpecialAttack(AttackType attackType, final int weapon, final int delay, final SpecialType type,
                  final Animation animation, final Graphics graphics, final SpecialAttackScript attack) {
        this(attackType, new int[]{weapon}, delay, type, animation, graphics, attack);
    }

    SpecialAttack(AttackType attackType, final int[] weapons, final int delay, final SpecialType type,
                  final Animation animation, final Graphics graphics, final SpecialAttackScript attack) {
        this.weapons = weapons;
        this.delay = delay;
        this.type = type;
        this.animation = animation;
        this.graphics = graphics;
        this.attack = attack;
        this.attackType = attackType;
    }

    @NotNull
    @Override
    public String getSpecialAttackName() {
        return StringUtils.capitalize(name().toLowerCase().replace("_", " "));
    }

    @Override
    public int @NotNull [] getWeapons() {
        return weapons;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @NotNull
    @Override
    public SpecialType getType() {
        return type;
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @NotNull
    @Override
    public SpecialAttackScript getAttack() {
        return attack;
    }

    @NotNull
    @Override
    public AttackType getAttackType() {
        return attackType;
    }
}

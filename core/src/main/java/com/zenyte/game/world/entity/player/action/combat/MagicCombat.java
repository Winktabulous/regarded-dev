package com.zenyte.game.world.entity.player.action.combat;

import com.near_reality.game.content.combat.CombatUtility;
import com.near_reality.game.content.custom.SlayerHelmetEffects;
import com.zenyte.game.content.achievementdiary.diaries.DesertDiary;
import com.zenyte.game.content.boons.impl.*;
import com.zenyte.game.content.skills.hunter.npc.ImplingNPC;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.arceuus.MarkOfDarknessEffectKt;
import com.zenyte.game.content.skills.magic.spells.lunar.SpellbookSwap;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.pathfinding.events.player.CombatEntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.PredictedEntityStrategy;
import com.zenyte.game.world.entity.player.Bonuses;
import com.zenyte.game.world.entity.player.CombatDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.action.combat.magic.TumekensShadowCombat;
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.SpellEffect;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;
import com.zenyte.game.world.region.area.plugins.SpellPlugin;
import com.zenyte.plugins.item.TomeOfFire;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import static com.zenyte.game.world.entity.player.action.combat.CombatUtilities.AHRIMS_SET_GFX;
import static com.zenyte.game.world.entity.player.action.combat.CombatUtilities.isWieldingZurielsStaff;

/**
 * @author Kris | 03/03/2019 16:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MagicCombat extends PlayerCombat {

    public enum CastType {
        AUTO_CAST, MANUAL_CAST
    }

    public MagicCombat(@NotNull final Entity target, @NotNull final CombatSpell spell, @NotNull final CastType castType) {
        super(target);
        this.spell = spell;
        this.castType = castType;
    }

    protected final CombatSpell spell;
    private final CastType castType;
    private SpellState state;
    private boolean splash;
    protected boolean interrupt;
    private boolean firstCast = true;

    public static final List<CombatSpell> southWestSpells = Arrays.asList(
            CombatSpell.SMOKE_BARRAGE, CombatSpell.SHADOW_BARRAGE, CombatSpell.BLOOD_BARRAGE, CombatSpell.ICE_BARRAGE
    );

    @Override
    int fireProjectile() {
        final Projectile projectile = spell.getProjectile();
        if (projectile.getGraphicsId() != -1 && spell != CombatSpell.TELE_BLOCK) {
            if (spell == CombatSpell.SMOKE_BURST || spell == CombatSpell.ICE_BURST || spell == CombatSpell.SMOKE_BARRAGE || spell == CombatSpell.ICE_BARRAGE) {
                World.sendProjectile(target.getMiddleLocation(), target, projectile);
            } else {
                World.sendProjectile(player, target, projectile);
            }
        }
        if (southWestSpells.contains(spell)) {
            return projectile.getTime(player.getLocation().getTileDistance(target.getLocation()));
        }
        return projectile.getTime(player.getLocation().getAxisDistance(player.getSize(), target.getLocation(), target.getSize()));
    }

    @Override
    public Hit getHit(Player player, Entity target, double accuracyModifier, double passiveModifier, double activeModifier, final boolean ignorePrayers) {
        final Hit hit = new Hit(player, getRandomHit(player, target, getMaxHit(player, passiveModifier, activeModifier, false), accuracyModifier), getHitType());
        hit.setWeapon(spell);
        hit.setSpell(spell);
        return hit;
    }

    @Override
    public int getRandomHit(Player player, Entity target, int maxhit, double modifier) {
        return getRandomHit(player, target, maxhit, modifier, AttackType.MAGIC);
    }

    @Override
    public int getRandomHit(Player player, Entity target, int maxhit, double modifier, AttackType oppositeIndex) {
        if (CombatUtilities.isAlwaysTakeMaxHit(target, HitType.MAGIC)) {
            return maxhit;
        }
        final int accuracy = getAccuracy(player, target, modifier);
        if (CombatUtilities.isMovingWarden(target)) {
            final int minimumHit = accuracy / 2000;
            final int bonus = player.getBonuses().getBonus(Bonuses.Bonus.MAGIC_DAMAGE);
            final double bonusModifier = 1.0F + (Math.min(100.0F, bonus) / 100.0F);
            splash = false;
            return Utils.random(minimumHit, (int) (minimumHit * bonusModifier));
        }
        final int targetRoll = getTargetDefenceRoll(player, target, oppositeIndex);
        sendDebug(accuracy, targetRoll, maxhit);
        final int accRoll = accuracy > 0 ? Utils.random(accuracy) : 0;
        final int defRoll = targetRoll > 0 ? Utils.random(targetRoll) : 0;
        splash = accRoll <= defRoll;
        if (splash) {
            return 0;
        }
        int playerMinimum = calculateMinimumHit(player, maxhit);
        return Utils.random(playerMinimum, maxhit);
    }

    @Override
    public int getAccuracy(Player player, Entity target, double resultModifier) {
        double effectiveLevel = Math.floor(player.getSkills().getLevel(SkillConstants.MAGIC) * player.getPrayerManager().getMagicBoost(SkillConstants.ATTACK));
        final CombatDefinitions combatDefinitions = player.getCombatDefinitions();
        if (combatDefinitions.getStyleDefinition() == AttackStyleDefinition.THROWN_MAGIC) {
            final int style = combatDefinitions.getStyle();
            //Accurate
            if (style == 0) {
                effectiveLevel += 3;
            } else
                //Defensive longrange
                if (style > 1) {
                    effectiveLevel += 1;
                }
        }
        effectiveLevel += 8;
        if (CombatUtilities.hasFullMagicVoid(player, false)) {
            effectiveLevel *= 1.45F;
        }
        effectiveLevel = Math.floor(effectiveLevel);
        int effectiveBonus = player.getBonuses().getBonus(Bonuses.Bonus.ATT_MAGIC);
        if (this instanceof TumekensShadowCombat) {
            if (player.getTOAManager().getRaidParty() != null) {
                effectiveBonus *= 4;
            } else {
                effectiveBonus *= 3;
            }
        }
        double result = effectiveLevel * (effectiveBonus + 64.0F);
        final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
        if (amuletId == ItemId.AMULET_OF_AVARICE && CombatUtilities.isRevenant(target)) {
            result *= 1.2F;
        } else if ((amuletId == 12017 || amuletId == 12018) && CombatUtilities.SALVE_AFFECTED_NPCS.contains(name)) {
            result *= amuletId == 12017 ? 1.15F : 1.2F;
        }

        boolean hasTask = (player.getSlayer().isCurrentAssignment(target) || player.hasBoon(SlayersSovereignty.class)) || CombatUtilities.isUndeadCombatDummy(target);
        result *= determineSlayerHelmetAccuracyBoost(hasTask, HitType.MAGIC, player, target);
        result += determineBountyHunterAccBoost(player, target);
        result = Math.floor(result);

        if(target instanceof NPC npc && MinionsMight.shouldBoostCombat(player, npc))
            result *= 1.1F;

        //If the weapon is smoke battlestaff and the player is on normal spellbook.
        final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
        if (weapon == 11998 || weapon == 12000) {
            if (player.getCombatDefinitions().getSpellbook() == Spellbook.NORMAL) {
                resultModifier += 0.1F;
            }
        }
        if (isThammaronsSceptre() || isAccursedSceptre()) {
            if (target instanceof NPC && ((NPC) target).isInWilderness()) {
                final Item wep = player.getWeapon();
                if (wep.getCharges() > 1000) {
                    resultModifier += 1;
                }
            }
        }
        if (CombatUtilities.applyForinthrySurge(player, target))
            resultModifier += 0.15F;
        if (CombatUtilities.applyPvmArenaBoost(player, target))
            resultModifier += 0.05F;
        result *= resultModifier;
        if(target instanceof NPC) {
            if(player.hasBoon(JabbasRightHand.class) && CombatUtility.hasBountyHunterWeapon(player))
                result *= 1.15;
            if (player.hasBoon(BrawnOfJustice.class) && BrawnOfJustice.applies(player) && player.getBooleanTemporaryAttribute("TOB_inside"))
                result *= 1.2;
        }
        result = Math.floor(result);
        return (int) result;
    }

    @Override
    protected boolean isWithinAttackDistance() {
        //use working calculation for NPCs as changes for freeze mechanics in 84be0d2d broke a lot of other things
        if(target instanceof NPC)
            return isWithinAttackDistanceNPC();
        else return isWithinAttackDistancePlayer();
    }

    private boolean isWithinAttackDistancePlayer() {
        // Check if the spell casting is immediate based on the type or if the spell or its spellbook is missing.
        final boolean immediateCast = castType == CastType.MANUAL_CAST || spell == null || spell.getSpellbook() == null;

        // Check if there's a direct line of projectile path between player and target.
        if (target.checkProjectileClip(player, false) && isProjectileClipped(immediateCast, false))
            return false;

        // Determine the next location of the target, falling back to the current location if there's no "next" location.
        final Location effectiveTargetLocation = target.getLocation();
        final Location effectivePlayerLocation = player.getLocation();

        // Calculate the horizontal and vertical distances from player to the effective target location.
        final int distanceX = effectivePlayerLocation.getX() - effectiveTargetLocation.getX();
        final int distanceY = effectivePlayerLocation.getY() - effectiveTargetLocation.getY();

        // Fetch the size of the target and the maximum attack distance.
        final int targetSize = target.getSize();
        final int maxAttackDistance = getAttackDistance();

        // Check if the player is hindered by being frozen or stunned.
        if (player.isFrozen() || player.isStunned()) {
            // Check if there's any collision between player and target, or if the target is out of range.
            final boolean isCollision = CollisionUtil.collides(
                    effectivePlayerLocation.getX(), effectivePlayerLocation.getY(), player.getSize(),
                    effectiveTargetLocation.getX(), effectiveTargetLocation.getY(), targetSize
            );
            final boolean withinRange = withinRange(target, maxAttackDistance, targetSize);
            if (isCollision || !withinRange) {
                return false;
            }
        }

        // Check if the target is within a rectangle defined by the max distance and the target's size around the player.
        return Math.abs(distanceX) <= targetSize + maxAttackDistance && Math.abs(distanceY) <= targetSize + maxAttackDistance;
    }

    protected boolean isWithinAttackDistanceNPC() {
        final boolean immediateCast = castType == CastType.MANUAL_CAST || spell == null || spell.getSpellbook() == null;
        if (target.checkProjectileClip(player, false) && isProjectileClipped(immediateCast, false)) {
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

    private static final CombatSpell[] fireSpells = new CombatSpell[] {CombatSpell.FIRE_BLAST, CombatSpell.FIRE_BOLT, CombatSpell.FIRE_STRIKE, CombatSpell.FIRE_SURGE, CombatSpell.FIRE_WAVE};

    protected int baseDamage() {
        return spell.getMaxHit();
    }

    public static double getDamageBoost(Player player) {
        //Start calculating the modifer; it is only multiplied by the damage once.
        double modifier = 1 + (player.getBonuses().getBonus(Bonuses.Bonus.MAGIC_DAMAGE) / 100.0F);
        //If the weapon is smoke battlestaff and the player is on normal spellbook.
        final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
        if (weapon == 11998 || weapon == 12000) {
            if (player.getCombatDefinitions().getSpellbook() == Spellbook.NORMAL) {
                modifier += 0.1F;
            }
        }
        //If the player has full elite magic void.
        if (CombatUtilities.hasFullMagicVoid(player, true)) {
            modifier += 0.025F;
        }
        return modifier;
    }

    protected void extra(final Hit hit) {
        bloodFury(HitType.MAGIC, hit);
    }

    @Override
    public int getMaxHit(Player player, double passiveModifier, double activeModifier, final boolean ignorePrayers) {
        int damage = baseDamage();

        if (spell == CombatSpell.MAGIC_DART) {
            final int staff = player.getEquipment().getId(EquipmentSlot.WEAPON);
            //4170, 21255
            final int magic = player.getSkills().getLevel(SkillConstants.MAGIC);
            if (staff == 4170 || !(player.getSlayer().isCurrentAssignment(target) || CombatUtilities.isAlwaysTakeMaxHit(target, HitType.MAGIC))) {
                damage = (int) Math.floor((magic / 10.0F) + 10);
            } else {
                damage = (int) Math.floor((magic / 6.0F) + 13);
            }
        }
        if (player.getVariables().getTime(TickVariable.CHARGE) > 0) {
            final int cape = player.getEquipment().getId(EquipmentSlot.CAPE);
            if (spell == CombatSpell.CLAWS_OF_GUTHIX && (cape == 2413 || cape == 21793 || cape == 13335 || cape == 21784) || spell == CombatSpell.SARADOMIN_STRIKE && (cape == 2412 || cape == 21791 || cape == 13331 || cape == 21776) || spell == CombatSpell.FLAMES_OF_ZAMORAK && (cape == 2414 || cape == 21795 || cape == 13333 || cape == 21780)) {
                damage = 30;
            }
        }
        if (player.getEquipment().getId(EquipmentSlot.HANDS) == 777) {
            if (spell == CombatSpell.EARTH_BOLT || spell == CombatSpell.FIRE_BOLT || spell == CombatSpell.WATER_BOLT || spell == CombatSpell.WIND_BOLT) {
                damage += 3;
            }
        }
        int bonus = player.getBonuses().getBonus(Bonuses.Bonus.MAGIC_DAMAGE);
        bonus = evalVirtusBoost(bonus);
        bonus = evalTumekensBoost(bonus);

        double modifier = 1.0F + (Math.min(100.0F, bonus) / 100.0F);
        //Multiply with the damage modifiers.
        damage *= modifier;
        modifier = 1;
        //If the player is wearing an imbued salve amulet and attacking an undead NPC.

        final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
        final String amuletName = ItemDefinitions.nameOf(amuletId).toLowerCase();

        if (amuletId == ItemId.AMULET_OF_AVARICE && CombatUtilities.isRevenant(target)) {
            modifier += 0.2F;
        } else if (amuletName.startsWith("salve amulet") && (CombatUtilities.SALVE_AFFECTED_NPCS.contains(name) || CombatUtilities.isUndeadCombatDummy(target))) {
            modifier += amuletId == 12017 ? 0.15F : 0.2F;
        }

        boolean hasTask = (player.getSlayer().isCurrentAssignment(target) || player.hasBoon(SlayersSovereignty.class)) || CombatUtilities.isCombatDummy(target);
        modifier *= determineSlayerHelmetDamageBoost(hasTask, HitType.MAGIC, player, target);
        modifier = SlayerHelmetEffects.INSTANCE.rollBonusDamage(player, target, modifier);

        if(target instanceof NPC npc && MinionsMight.shouldBoostCombat(player, npc))
            modifier *= 1.1F;
        if (CombatUtilities.applyForinthrySurge(player, target))
            modifier += 0.15F;
        if (CombatUtilities.applyPvmArenaBoost(player, target))
            modifier += 0.05F;
        modifier += determineBountyHunterDmgBoost(player, target);
        //Multiply with the damage modifiers.
        damage *= modifier;
        if (isThammaronsSceptre() || isAccursedSceptre()) {
            if (target instanceof NPC && ((NPC) target).isInWilderness()) {
                final Item wep = player.getWeapon();
                if (wep.getCharges() > 1000) {
                    passiveModifier += 0.40F;
                }
            }
        }
        damage = (int) Math.floor(damage);
        if (!ignorePrayers) {
            if (target instanceof Player) {
                if (((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
                    damage *= target.getMagicPrayerMultiplier();
                }
            }
        }
        damage *= passiveModifier;
        //Tome of fire effect.
        final Item shield = player.getEquipment().getItem(EquipmentSlot.SHIELD);
        if (shield != null && shield.getId() == TomeOfFire.TOME_OF_FIRE && shield.hasCharges()) {
            if (ArrayUtils.contains(fireSpells, spell)) {
                damage *= 1.5F;
            }
        }
        //Castle wars bracelet effect
        if (player.getTemporaryAttributes().containsKey("castle wars bracelet effect") && player.inArea("Castle wars instance")) {
            if (target instanceof Player) {
                final int targetWeapon = ((Player) target).getEquipment().getId(EquipmentSlot.WEAPON);
                if (targetWeapon == 4037 || targetWeapon == 4039) {
                    damage *= 1.2F;
                }
            }
        }

        return damage;
    }

    private int evalTumekensBoost(int bonus) {
        if (this instanceof TumekensShadowCombat) {
            if (player.getTOAManager().getRaidParty() != null) {
                bonus = Math.min(bonus * 4, 100);
            } else {
                bonus = Math.min(bonus * 3, 100);
            }
        }
        return bonus;
    }

    private int evalVirtusBoost(int initial) {
        if(player.getCombatDefinitions().getSpellbook() != Spellbook.ANCIENT)
            return initial;

        int bonusAdded = 0;
        if(player.getHelmet() != null) {
            int helm = player.getHelmet().getId();
            if(helm == ItemId.VIRTUS_MASK) bonusAdded += 3;
        }

        if(player.getChest() != null) {
            int helm = player.getChest().getId();
            if(helm == ItemId.VIRTUS_ROBE_TOP) bonusAdded += 3;
        }

        if(player.getLegs() != null) {
            int legs = player.getLegs().getId();
            if(legs == ItemId.VIRTUS_ROBE_LEGS) bonusAdded += 3;
        }

        return initial + bonusAdded;
    }

    @Override
    public boolean start() {
        if (!spell.canCast(player, target)) {
            return false;
        }
        state = new SpellState(player, spell.getLevel(), spell.getRunes());
        int distanceToCast = getTileDistance(false) - getAttackDistance();
        player.setCombatEvent(new CombatEntityEvent(player, new PredictedEntityStrategy(target, distanceToCast + 1)));
        player.setLastTarget(target);
        notifyIfFrozen();
        player.setFaceEntity(target);
        if (initiateCombat(player)) {
            return true;
        }
        player.setFaceEntity(null);
        return false;
    }

    private boolean isTargetDead() {
        return target instanceof Player || castType == CastType.AUTO_CAST ? target.isDead() : (target.isDead() && ((NPC) target).getTimeOfDeath() != WorldThread.WORLD_CYCLE);
    }

    private boolean initiateCombat(final Player player) {
        if (player.isDead() || player.isFinished() || player.isLocked() || player.isStunned() || player.isFullMovementLocked()) {
            return false;
        }
        if (isTargetDead() || target.isFinished() || target.isCantInteract()) {
            return false;
        }
        if (spell == CombatSpell.MAGIC_DART && player.getSkills().getLevel(SkillConstants.SLAYER) < 50) {
            player.sendMessage("You need a Slayer level of at least 50 to cast Slayer Dart.");
            return false;
        }
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = target.getSize();
        final int viewDistance = player.getViewDistance();
        if (player.getPlane() != target.getPlane() || distanceX > size + viewDistance || distanceX < -1 - viewDistance || distanceY > size + viewDistance || distanceY < -1 - viewDistance) {
            return false;
        }
        if (target.getEntityType() == Entity.EntityType.PLAYER) {
            if (!player.isCanPvp() || !((Player) target).isCanPvp()) {
                player.sendMessage("You can't attack someone in a safe zone.");
                return false;
            }
        }
        if (player.isFrozen() || player.isMovementLocked(false)) {
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
        /*if (checkAutocastDelay() && castType == CastType.AUTO_CAST && firstCast) {
            firstCast = false;
            if (player.getActionManager().getActionDelay() <= 0) {
                player.getActionManager().setActionDelay(1);
            }
            return true;
        }*/
        return pathfind();
    }

    public boolean checkAutocastDelay() {
        return true;
    }

    @Override
    public boolean process() {
        return !interrupt && initiateCombat(player);
    }

    @Override
    protected int getAttackDistance() {
        return 9;
    }

    private static final Projectile teleblock = new Projectile(1300, 43, 31, 46, 23, 29, 64, 5);

    @Override
    public int processWithDelay() {
        return 0;
    }

    @Override
    public int processAfterMovement() {
        if (!isWithinAttackDistance()) {
            return 0;
        }
        if (!canAttack()) {
            return -1;
        }
        state = new SpellState(player, spell);
        if (!state.check()) {
            return -1;
        }
        splash = false;
        addAttackedByDelay(player, target);
        final RegionArea area = player.getArea();
        if (area instanceof PlayerCombatPlugin) {
            ((PlayerCombatPlugin) area).onAttack(player, target, "Magic", spell, splash);
        }
        if (player.getCombatDefinitions().isUsingSpecial()) {
            final int delay = useSpecial(player, SpecialType.MAGIC);
            if (delay == SpecialAttackScript.WEAPON_SPEED) {
                return attackSpeed();
            }
            if (delay >= 0) {
                return delay;
            }
        }
        final Item shield = player.getEquipment().getItem(EquipmentSlot.SHIELD);
        if (shield != null && shield.getId() == TomeOfFire.TOME_OF_FIRE && shield.hasCharges()) {
            if (ArrayUtils.contains(fireSpells, spell)) {
                player.getChargesManager().removeCharges(shield, 1, player.getEquipment().getContainer(), EquipmentSlot.SHIELD.getSlot());
            }
        }
        boolean saveRuneOrCharge = SlayerHelmetEffects.INSTANCE.redHelmet(player, target) && Utils.random(100) < 25;
        if(!saveRuneOrCharge && player.getBoonManager().hasBoon(SuperiorSorcery.class))
            saveRuneOrCharge = SuperiorSorcery.roll();
        final int delay = fireProjectile();
        hit(delay);
        if (area instanceof PlayerCombatPlugin) {
            ((PlayerCombatPlugin) area).onAttack(player, target, "Magic", spell, splash);
        }
        addBaseXP();
        if (!saveRuneOrCharge) {
            degrade();
        }
        animate();
        if (!(player.getEquipment().getId(EquipmentSlot.WEAPON) == ItemId.KODAI_WAND && Utils.random(100) < 15) && !saveRuneOrCharge) {
            state.remove();
        }
        if (spell.equals(CombatSpell.ICE_BARRAGE)) {
            player.getAchievementDiaries().update(DesertDiary.CAST_ICE_BARRAGE);
        }
        //player.getAchievementDiaries().update(ArdougneDiary.CAST_ICE_BARRAGE_ON_PLAYER_IN_CW);
        if (spell == CombatSpell.TELE_BLOCK) {
            World.sendProjectile(player, target, this.splash ? teleblock : spell.getProjectile());
        }
        if (spell == CombatSpell.BLOOD_BLITZ || spell == CombatSpell.BLOOD_BARRAGE || spell == CombatSpell.BLOOD_BURST || spell == CombatSpell.BLOOD_RUSH) {
            if (!splash) {
                player.sendFilteredMessage("You drain some of your opponent's health.");
            }
        }
        if (spell.getCastSound() != null) {
            player.getPacketDispatcher().sendSoundEffect(spell.getCastSound());
        }
        if (Utils.random(3) == 0 && CombatUtilities.hasFullBarrowsSet(player, "Ahrim's")) {
            target.setGraphics(AHRIMS_SET_GFX);
            target.drainSkill(SkillConstants.STRENGTH, 5);
        }
        addToxinTask(delay);
        resetFlag();
        SpellbookSwap.checkSpellbook(player);
        if (castType == CastType.MANUAL_CAST) {
            interrupt = true;
        }
        checkIfShouldTerminate(HitType.MAGIC);
        return adjustAttackSpeed(player, attackSpeed());
    }

    protected int attackSpeed() {
        return 4;
    }

    private boolean isThammaronsSceptre() {
        return player.getEquipment().getId(EquipmentSlot.WEAPON) == 22555;
    }

    private boolean isAccursedSceptre() {
        return player.getEquipment().getId(EquipmentSlot.WEAPON) == 27665;
    }

    private static final EnumSet<CombatSpell> multiSpells = EnumSet.of(CombatSpell.ICE_BURST, CombatSpell.SMOKE_BURST, CombatSpell.BLOOD_BURST, CombatSpell.SHADOW_BURST, CombatSpell.ICE_BARRAGE, CombatSpell.SMOKE_BARRAGE, CombatSpell.BLOOD_BARRAGE, CombatSpell.SHADOW_BARRAGE);

    protected void hit(final int delay) {
        final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        float passiveModifier = (amuletId == 12851 || amuletId == 12853) && CombatUtilities.hasFullBarrowsSet(player, "Ahrim's") ? 1.3F : 1;
        float accuracyModifier = 1.0F;
        switch (spell) {
            case INFERIOR_DEMONBANE, DARK_DEMONBANE, SUPERIOR_DEMONBANE: {
                if (MarkOfDarknessEffectKt.getHasMarkOfDarkness(target)) {
                    accuracyModifier += 0.4F;
                    passiveModifier += 0.25F;
                } else {
                    accuracyModifier += 0.2F;
                }
                break;
            }
            case ICE_RUSH, ICE_BURST, ICE_BLITZ, ICE_BARRAGE: {
                if (isWieldingZurielsStaff(player))
                    accuracyModifier += 0.1F;

                if (Objects.equals(weaponId, ItemId.ICE_ANCIENT_SCEPTRE_28262))
                    accuracyModifier += 0.1F;
                break;
            }
        }

        boolean ignorePrayers = false;

        if(target instanceof NPC npc) {
            if(player.hasBoon(InfallibleShackles.class) && InfallibleShackles.applies(spell) && !npc.isFrozen() && npc.isFreezeable()) {
                accuracyModifier = 100.0F;
                ignorePrayers = true;
            }
        }

        final float passive = passiveModifier;
        final float accuracy = accuracyModifier;
        final Hit primaryHit = getHit(player, target, accuracy, passive, 1, ignorePrayers);
        extra(primaryHit);
        final Projectile projectile = spell.getProjectile();
        final int clientCycles = projectile.getProjectileDuration(player.getLocation(), target.getLocation());
        if(player.getWeapon() != null && this.spell == CombatSpell.TUMEKENS_SHADOW && player.getBoonManager().hasBoon(DivineHealing.class)) {
            DivineHealing.Special special = DivineHealing.determineBoost();
            switch(special) {
                case NONE -> {}
                case BOTH -> {
                    player.heal(primaryHit.getDamage());
                    player.getPrayerManager().restorePrayerPoints(primaryHit.getDamage());
                }
                case HEAL_HP -> player.heal(primaryHit.getDamage());
                case HEAL_PRAY -> player.getPrayerManager().restorePrayerPoints(primaryHit.getDamage());
            }
        }
        applyHit(target, primaryHit, splash, delay, clientCycles);
        if (multiSpells.contains(spell)) {
            attackTarget(getMultiAttackTargets(player), originalTarget -> {
                if (target == originalTarget) {
                    return true;
                }
                final Hit hit = getHit(player, target, accuracy, passive, 1, false);
                applyHit(target, hit, splash, delay, clientCycles);
                return true;
            });
        }
    }

    protected void applyHit(final Entity target, final Hit hit, final boolean splash, final int delay, final int clientcycles) {
        final Graphics gfx = splash ? new Graphics(85, -1, 124) : spell.getHitGfx();
        if (gfx != null) {
            target.setGraphics(new Graphics(gfx.getId(), gfx.getDelay() != -1 ? gfx.getDelay() : clientcycles, gfx.getHeight()));
        }
        final SoundEffect hitSound = splash ? new SoundEffect(227, 10, -1) : spell.getHitSound();
        if (hitSound != null) {
            World.sendSoundEffect(target.getLocation(), new SoundEffect(hitSound.getId(), hitSound.getRadius(), hitSound.getDelay() == -1 ? clientcycles : hitSound.getDelay()));
            if (spell == CombatSpell.IBAN_BLAST) {
                World.sendSoundEffect(target.getLocation(), new SoundEffect(hitSound.getId(), hitSound.getRadius(), hitSound.getDelay() == -1 ? clientcycles : hitSound.getDelay()));
            }
        }
        final boolean isFreezingImpling = (spell == CombatSpell.BIND || spell == CombatSpell.SNARE || spell == CombatSpell.ENTANGLE || spell == CombatSpell.DARK_LURE) && target instanceof ImplingNPC;
        if (!splash) {
            if (spell == CombatSpell.MARK_OF_DARKNESS || spell == CombatSpell.DARK_LURE) {
                WorldTasksManager.schedule(() -> {
                    if (target instanceof ImplingNPC) {
                        Location tile = player.getLocation().random(1);
                        target.resetWalkSteps();
                        target.addWalkStepsInteract(tile.getX(), tile.getY(), -1, target.getSize(), true);
                    } else {
                        target.autoRetaliate(player);
                        if (target instanceof NPC) {
                            ((NPC) target).flinch();
                        }
                    }
                }, delay);
            } else {
                if (!isFreezingImpling && spell != CombatSpell.TELE_BLOCK) {
                    this.delayHit(target, delay, hit);
                }
            }
            final SpellEffect effect = spell.getEffect();
            if (effect != null) {
                effect.spellEffect(player, target, hit.getDamage());
            }
        } else {
            if (isFreezingImpling) {
                return;
            }
            WorldTasksManager.schedule(() -> {
                target.autoRetaliate(player);
                if (target instanceof NPC) {
                    ((NPC) target).flinch();
                }
            }, delay);
        }
    }

    private void addToxinTask(final int delay) {
        final boolean isFreezingImpling = (spell == CombatSpell.BIND || spell == CombatSpell.SNARE || spell == CombatSpell.ENTANGLE || spell == CombatSpell.DARK_LURE) && target instanceof ImplingNPC;
        if (isFreezingImpling) return;

        if (!CombatUtilities.isWearingVenomMageEquipment(player)) return;

        if (target instanceof NPC && CombatUtilities.isWearingSerpentineHelmet(player) || Utils.randomBoolean(3)) {
            WorldTasksManager.scheduleOrExecute(() -> target.getToxins().applyToxin(Toxins.ToxinType.VENOM, 6, player), delay);
        }
    }

    protected void animate() {
        final Animation animation = spell.getAnimation();
        final Graphics graphics = spell.getCastGfx();
        if (animation != null) {
            player.setAnimation(animation);
        }
        if (graphics != null) {
            player.setGraphics(graphics);
        }
    }

    private void addBaseXP() {
        spell.addXp(player, spell.getExperience());
    }

    protected void degrade() {
        player.getChargesManager().removeCharges(DegradeType.SPELL);
    }

    protected boolean canAttack() {
        if (!attackable()) {
            return false;
        }
        final boolean isFreezingImpling = (spell == CombatSpell.BIND || spell == CombatSpell.SNARE || spell == CombatSpell.ENTANGLE || spell == CombatSpell.DARK_LURE) && target instanceof ImplingNPC;
        if (!isFreezingImpling && !target.canAttack(player)) {
            return false;
        }
        final RegionArea area = player.getArea();
        if ((area instanceof EntityAttackPlugin && !((EntityAttackPlugin) area).attack(player, target, this))) {
            return false;
        }
        if ((area instanceof PlayerCombatPlugin && !((PlayerCombatPlugin) area).processCombat(player, target, "Magic")) || !player.getControllerManager().processPlayerCombat(target, "Magic")) {
            return false;
        }
        if (area instanceof SpellPlugin && !((SpellPlugin) area).canCast(player, spell))
            return false;
        return !isTargetDead();
    }

    public HitType getHitType() {
        return HitType.MAGIC;
    }

}

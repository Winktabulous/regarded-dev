package com.zenyte.game.content.follower.impl;

import com.zenyte.game.GameConstants;
import com.zenyte.game.content.boons.impl.NoPetDebt;
import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.followers.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import mgi.utilities.CollectionUtils;

/**
 * @author Tommeh | 23-11-2018 | 18:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum SkillingPet implements Pet {
    ROCK_GOLEM_DEFAULT(13321, 7451, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_TIN(21187, 7452, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_COPPER(21188, 7453, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_IRON(21189, 7454, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_BLURITE(21190, 7455, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_SILVER(21191, 7642, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_COAL(21192, 7643, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_GOLD(21193, 7644, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_MITHRIL(21194, 7645, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_GRANITE(21195, 7646, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_ADAMANTITE(21196, 7647, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_RUNITE(21197, 7648, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_AMETHYST(21340, 7711, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_LOVAKITE(21358, 7739, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEM_ELEMENTAL(21359, 7740, SkillConstants.MINING, RockGolemD.class, 13321),
    ROCK_GOLEMD_DAEYALT(21360, 7741, SkillConstants.MINING, RockGolemD.class, 13321),
    RIFT_GUARDIAN_DEFAULT(20665, 7354, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),  //dont remove cuz serialized
    RIFT_GUARDIAN_AIR(20667, 7355, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_MIND(20669, 7356, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_WATER(20671, 7357, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_EARTH(20673, 7358, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_FIRE(20665, 7354, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_BODY(20675, 7359, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_COSMIC(20677, 7360, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_CHAOS(20679, 7361, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_NATURE(20681, 7362, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_LAW(20683, 7363, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_DEATH(20685, 7364, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_BLOOD(20691, 7367, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_SOUL(20687, 7365, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_ASTRAL(20689, 7366, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RIFT_GUARDIAN_WRATH(21990, 8028, SkillConstants.RUNECRAFTING, RiftGuardianD.class, 20665),
    RED_BABY_CHINCHOMPA(13323, 6756, SkillConstants.HUNTER, BabyChinchompaD.class, 13324),
    GRAY_BABY_CHINCHOMPA(13324, 6757, SkillConstants.HUNTER, BabyChinchompaD.class, 13324),
    BLACK_BABY_CHINCHOMPA(13325, 6758, SkillConstants.HUNTER, BabyChinchompaD.class, 13324),
    GOLDEN_BABY_CHINCHOMPA(13326, 6759, SkillConstants.HUNTER, BabyChinchompaD.class, 13324),
    BEAVER(13322, 6724, SkillConstants.WOODCUTTING, BeaverD.class),
    ROCKY(20663, 7353, SkillConstants.THIEVING, RockyD.class),
    GIANT_SQUIRREL(20659, 7351, SkillConstants.AGILITY, GiantSquirrelD.class),
    HERON(13320, 6722, SkillConstants.FISHING, HeronD.class),
    TANGLEROOT(20661, 7352, SkillConstants.FARMING, TanglerootD.class);

    public static final SkillingPet[] VALUES = values();
    public static final Int2ObjectOpenHashMap<ObjectList<SkillingPet>> SKILLING_PETS_BY_SKILL;
    public static final Int2ObjectOpenHashMap<SkillingPet> SKILLING_PETS_BY_ITEM;
    public static final IntList SKILLING_PETS_SKILLS;

    static {
        CollectionUtils.populateObjectListMap(VALUES, SKILLING_PETS_BY_SKILL = new Int2ObjectOpenHashMap<>(VALUES.length), SkillingPet::getSkill);
        CollectionUtils.populateMap(VALUES, SKILLING_PETS_BY_ITEM = new Int2ObjectOpenHashMap<>(VALUES.length), SkillingPet::getItemId);
        SKILLING_PETS_SKILLS = new IntArrayList();
        for (SkillingPet pet : VALUES) {
            if (!SKILLING_PETS_SKILLS.contains(pet.getSkill())) {
                SKILLING_PETS_SKILLS.add(pet.getSkill());
            }
        }
    }

    private final int itemId;
    private final int petId;
    private final int skill;
    private final Class<? extends Dialogue> dialogue;
    private final int collectionLogId;

    public static SkillingPet getBySkill(final int skill) {
        if (skill == SkillConstants.MINING) {
            return ROCK_GOLEM_DEFAULT;
        }
        final ObjectList<SkillingPet> pets = SKILLING_PETS_BY_SKILL.get(skill);
        if (skill == SkillConstants.HUNTER) {
            if (Utils.random(9999) == 0) {
                return GOLDEN_BABY_CHINCHOMPA;
            }
            pets.remove(GOLDEN_BABY_CHINCHOMPA);
            return Utils.getRandomCollectionElement(pets);
        }
        return pets != null ? pets.get(0) : null;
    }

    public static SkillingPet getByItem(final int itemId) {
        return SKILLING_PETS_BY_ITEM.get(itemId);
    }

    @Override
    public int itemId() {
        return itemId;
    }

    @Override
    public int petId() {
        return petId;
    }

    @Override
    public String petName() {
        return name();
    }

    @Override
    public boolean hasPet(final Player player) {
        for (SkillingPet skillingPet : SKILLING_PETS_BY_SKILL.get(skill)) {
            if (player.containsItem(skillingPet.getItemId())) {
                return true;
            }
            if (PetWrapper.checkFollower(player) && player.getFollower().getPet().petId() == skillingPet.getPetId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Class<? extends Dialogue> dialogue() {
        return dialogue;
    }

    @Override
    public boolean roll(final Player player, final int rarity) {
        int roll = rarity;
        if (GameConstants.BOOSTED_SKILLING_PETS || World.hasBoost(XamphurBoost.BONUS_PET_RATES)) {
            roll = (int) (rarity - (rarity * GameConstants.BOOSTED_SKILLING_PET_RATE));
        }

        if (player != null && player.getVariables().getPetBoosterTick() > 0) {
            roll *= 0.9;
        }

        if(player.getBoonManager().hasBoon(NoPetDebt.class)) {
            roll /= 2;
        }

        if (Utils.random(roll) != 0) {
            return false;
        }

        player.getCollectionLog().add(new Item(collectionLogId));
        final Item item = new Item(itemId);
        if (hasPet(player)) {
            player.sendMessage("<col=ff0000>You have a funny feeling like you would have been followed...</col>");
            WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
            return false;
        }
        if (player.getFollower() != null) {
            if (player.getInventory().addItem(item).isFailure()) {
                if (player.getBank().add(item).isFailure()) {
                    player.sendMessage("There was not enough space in your bank, and therefore the pet was lost.");
                    return false;
                }
                player.sendMessage("You have a funny feeling like you're being followed - The pet has been added to " +
                        "your bank.");
                return false;
            }
            player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed - The pet has been " +
                    "added to your inventory.</col>");
        } else {
            player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed.</col>");
            player.setFollower(new Follower(petId, player));
        }
        WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
        return true;
    }

    public static boolean isRiftGuardian(final Pet pet) {
        return SKILLING_PETS_BY_SKILL.get(SkillConstants.RUNECRAFTING).contains(pet);
    }

    public static boolean isRockGolem(final Pet pet) {
        return SKILLING_PETS_BY_SKILL.get(SkillConstants.MINING).contains(pet);
    }

    public static boolean isChinchompa(final Pet pet) {
        return SKILLING_PETS_BY_SKILL.get(SkillConstants.HUNTER).contains(pet);
    }

    SkillingPet(int itemId, int petId, int skill, Class<? extends Dialogue> dialogue) {
        this(itemId, petId, skill, dialogue, itemId);
    }

    SkillingPet(int itemId, int petId, int skill, Class<? extends Dialogue> dialogue, int collectionLogId) {
        this.itemId = itemId;
        this.petId = petId;
        this.skill = skill;
        this.dialogue = dialogue;
        this.collectionLogId = collectionLogId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPetId() {
        return petId;
    }

    public int getSkill() {
        return skill;
    }

    public Class<? extends Dialogue> getDialogue() {
        return dialogue;
    }
}

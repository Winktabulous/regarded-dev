package com.zenyte.game.content.follower.impl;

import com.zenyte.game.content.boons.impl.NoPetDebt;
import com.zenyte.game.content.event.christmas2019.SnowImpFollowerD;
import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.followers.BloodHoundD;
import com.zenyte.plugins.dialogue.followers.ChompyChickD;
import com.zenyte.plugins.dialogue.followers.HerbiD;

/**
 * @author Tommeh | 23-11-2018 | 18:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum MiscPet implements Pet {
    //TODO kittens and other variations of these.
    HERBI(21509, 7760, HerbiD.class),
    CHOMPY_CHICK(13071, 4002, ChompyChickD.class),
    BLOODHOUND(19730, 7232, BloodHoundD.class),
    TOY_CAT(14924, 2782),
    OVERGROWN_HELLCAT(7581, 5604),
    WILY_HELLCAT(7585, 5590),
    BLUEFISH(6670, -1),
    GREENFISH(6671, -1),
    SPINEFISH(6672, -1),
    CAT_1(1561, 1619),
    CAT_2(1562, 1620),
    CAT_3(1563, 1621),
    CAT_4(1564, 1622),
    CAT_5(1565, 1623),
    CAT_6(1566, 1624),
    CUTE_CREATURE(30000, 10013),
    STRAY_DOG(30001, 10014),
    EVIL_CREATURE(30002, 10015),
    JAL_IMREK(30003, 10016),
    BUCKET_PETE(30004, 10017),
    WYRMY(30005, 10018),
    SNOW_IMP(30114, 15060),
    AREA_LOCKED_SNOW_IMP(-1, 15062, SnowImpFollowerD.class),
    SARADOMIN_OWL(30150, 15104),
    ZAMORAK_HAWK(30151, 15105),
    GUTHIX_RAPTOR(30152, 15106),
    /* Do not rearrange the following pets */
    SPIRIT_KALPHITE(30207, 15107),
    RED_BABYDRAGON(30156, 15128),
    BLUE_BABYDRAGON(30157, 15129),
    GREEN_BABYDRAGON(30158, 15130),
    BLACK_BABYDRAGON(30159, 15131),
    PRAYING_MANTIS(30206, 15101),
    WOLPERTINGER(30209, 15103),
    GRANITE_CRAB(30205, 15100),
    COCKROACH(30203, 15108),
    EVIL_TURNIP(30204, 15102),
    SPIRIT_MOSQUITO(30208, 15109),
    YELLOW_GECKO(30194, 15133),
    GREEN_GECKO(30195, 15134),
    RED_GECKO(30196, 15135),
    BLUE_GECKO(30197, 15136),
    BEIGE_PENGUIN(30153, 15148),
    DARK_PENGUIN(30202, 15149),
    WHITE_BULLDOG(30167, 15125),
    GRAY_BULLDOG(30190, 15126),
    BEIGE_BULLDOG(30191, 15127),
    GRAY_SQUIRREL(30161, 15153),
    BEIGE_SQUIRREL(30198, 15154),
    WHITE_SQUIRREL(30199, 15155),
    BLACK_SQUIRREL(30200, 15156),
    BROWN_SQUIRREL(30201, 15157),
    GRAY_RACCOON(30160, 15150),
    BEIGE_RACCOON(30192, 15151),
    RED_RACCOON(30193, 15152),
    BROWN_PLATYPUS(30168, 15168),
    BEIGE_PLATYPUS(30169, 15169),
    DARK_PLATYPUS(30170, 15170),
    BEIGE_TERRIER(30162, 15110),
    WHITE_TERRIER(30180, 15111),
    BLACK_TERRIER(30181, 15112),
    BEIGE_GREYHOUND(30163, 15113),
    WHITE_GREYHOUND(30182, 15114),
    RED_GREYHOUND(30183, 15115),
    WHITE_LABRADOR(30164, 15116),
    BLACK_LABRADOR(30184, 15117),
    BEIGE_LABRADOR(30185, 15118),
    WHITE_DALMATIAN(30165, 15119),
    PINKISH_DALMATIAN(30186, 15120),
    BLACK_SHEEPDOG(30166, 15122),
    WHITE_SHEEPDOG(30188, 15123),
    BEIGE_SHEEPDOG(30189, 15124),
    MONKEY_1(30154, 15137),
    MONKEY_2(30171, 15138),
    MONKEY_3(30172, 15139),
    MONKEY_4(30173, 15140),
    MONKEY_5(30174, 15141),
    MONKEY_6(30175, 15142),
    MONKEY_7(30176, 15143),
    MONKEY_8(30177, 15144),
    MONKEY_9(30178, 15145),
    MONKEY_10(30179, 15146),
    CHAMELEON_1(30155, 15159),
    CHAMELEON_2(30155, 15160),
    CHAMELEON_3(30155, 15161),
    CHAMELEON_4(30155, 15162),
    CHAMELEON_5(30155, 15163),
    CHAMELEON_6(30155, 15164),
    CHAMELEON_7(30155, 15165),
    CHAMELEON_8(30155, 15166),
    CHAMELEON_9(30155, 15167),
    CHAMELEON_10(30155, 15158);

    public static final MiscPet[] VALUES = values();
    private final int itemId;
    private final int petId;
    private final Class<? extends Dialogue> dialogue;
    /* Do not re-arrange the above pets */
    MiscPet(final int itemId, final int petId) {
        this(itemId, petId, null);
    }
    MiscPet(final int itemId, final int petId, final Class<? extends Dialogue> dialogue) {
        this.itemId = itemId;
        this.petId = petId;
        this.dialogue = dialogue;
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
        final int petItemId = getItemId();
        if (player.containsItem(petItemId)) {
            return true;
        }
        return PetWrapper.checkFollower(player) && player.getFollower().getPet().petId() == getPetId();
    }

    @Override
    public Class<? extends Dialogue> dialogue() {
        return dialogue;
    }

    public boolean roll(final Player player, int rarity) {
        if (player.getBoonManager().hasBoon(NoPetDebt.class)) {
            rarity /= 2;
        }
        if (this != BLOODHOUND || rarity == -1 || Utils.random(rarity) != 0) {
            return false;
        }
        final Item item = new Item(itemId);
        player.getCollectionLog().add(item);
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
                player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed - The pet has " +
                    "been added to your bank.</col>");
                return false;
            }
            player.sendMessage("<col=ff0000>You feel something weird sneaking into your backpack.</col>");
            WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
        }
        else {
            player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed.</col>");
            player.setFollower(new Follower(petId, player));
            WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
        }
        return true;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPetId() {
        return petId;
    }

    public Class<? extends Dialogue> getDialogue() {
        return dialogue;
    }
}

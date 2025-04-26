package com.zenyte.game.content.treasuretrails.clues;

import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.ClueChallenge;
import com.zenyte.game.content.treasuretrails.challenges.FaloTheBardChallenge;
import com.zenyte.game.content.treasuretrails.clues.emote.AnyRequirementCollection;
import com.zenyte.game.content.treasuretrails.clues.emote.ItemRequirement;
import com.zenyte.game.content.treasuretrails.clues.emote.RangeItemRequirement;
import com.zenyte.game.content.treasuretrails.clues.emote.SingleItemRequirement;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 04/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum FaloTheBardClue implements Clue {

    DRAGON_SCIMITAR("A blood red weapon, a strong curved sword, found on the island of primate lords.", item(ItemId.DRAGON_SCIMITAR)),
    GOD_BOOK("A book that preaches of some great figure, lending strength, might and vigour.", any("Any god book (must be complete)", item(HOLY_BOOK), item(BOOK_OF_BALANCE), item(UNHOLY_BOOK),
            item(BOOK_OF_LAW), item(BOOK_OF_WAR), item(BOOK_OF_DARKNESS),
            item(26488), item(26490), item(26492),
            item(26494), item(26496), item(26498))),
    CRYSTAL_BOW("A bow of elven craft was made, it shimmers bright, but will soon fade.",
            any("Crystal Bow",
                    range(NEW_CRYSTAL_BOW, CRYSTAL_BOW_110),
                    range(NEW_CRYSTAL_BOW_I, CRYSTAL_BOW_110_I),
                    item(CrystalWeapon.Bow.INSTANCE.getProductItemId()),
                    item(CrystalWeapon.Bow.INSTANCE.getInactiveId()),
                    item(CrystalWeapon.BowOfFaerdhinen.INSTANCE.getProductItemId()),
                    item(CrystalWeapon.BowOfFaerdhinen.INSTANCE.getInactiveId())
            )),
    INFERNAL_AXE("A fiery axe of great inferno, when you use it, you'll wonder where the logs go.", any("Any infernal axe", item(ItemId.INFERNAL_AXE), item(INFERNAL_AXE_UNCHARGED))),
    MARK_OF_GRACE("A mark used to increase one's grace, found atop a seer's place.", item(ItemId.MARK_OF_GRACE)),
    LAVA_DRAGON_BONES("A molten beast with fiery breath, you acquire these with its death.", item(ItemId.LAVA_DRAGON_BONES)),
    ARMADYL_HELMET("A shiny helmet of flight, to obtain this with melee, struggle you might.", item(ItemId.ARMADYL_HELMET)),
    DRAGON_DEFENDER("A sword held in the other hand, red its colour, Cyclops strength you must withstand.", any("Dragon defender", item(ItemId.DRAGON_DEFENDER), item(DRAGON_DEFENDER_T), item(AVERNIC_DEFENDER))),
    WARRIORS_GUILD_TOKEN("A token used to kill mythical beasts, in hopes of a blade or just for an xp feast.", item(WARRIOR_GUILD_TOKEN)),
    //GREENMANS_ALE("Green is my favourite, mature ale I do love, this takes your herblore above.", item(ItemId.GREENMANS_ALE)),
    BARRELCHEST_ANCHOR("It can hold down a boat or crush a goat, this object, you see, is quite heavy.", item(ItemId.BARRELCHEST_ANCHOR)),
    BASALT("It comes from the ground, underneath the snowy plain. Trolls aplenty, with what looks like a mane.", item(ItemId.BASALT)),
    TZHAAR_KET_OM("No attack to wield, only strength is required, made of obsidian, but with no room for a shield.", item(TZHAARKETOM)),
    FIGHTER_TORSO("Penance healers runners and more, obtaining this body often gives much deplore.", item(ItemId.FIGHTER_TORSO)),
    BARROWS_GLOVES("Strangely found in a chest, many believe these gloves are the best.", item(ItemId.BARROWS_GLOVES)),
    COOKING_GLOVES("These gloves of white won't help you fight, but aid in cooking, they just might.", item(COOKING_GAUNTLETS)),
    NUMULITE("They come from some time ago, from a land unto the east. Fossilised they have become, this small and gentle beast.", item(ItemId.NUMULITE)),
    RUNE_PLATEBODY("To slay a dragon you must first do, before this chest piece can be put on you.", item(ItemId.RUNE_PLATEBODY)),
    //ROD_OF_IVANDIS("Vampyres are agile opponents, damaged best with a weapon of many components.", any("Rod of Ivandis or Ivandis flail", range(ROD_OF_IVANDIS_10, ROD_OF_IVANDIS_1), item
    // (IVANDIS_FLAIL)))
    ;

    private final String text;
    private final ItemRequirement[] itemRequirements;

    FaloTheBardClue(@NotNull String text, @NotNull ItemRequirement... itemRequirements) {
        this.text = text;
        this.itemRequirements = itemRequirements;
    }

    private static SingleItemRequirement item(int itemId) {
        return new SingleItemRequirement(itemId);
    }

    private static AnyRequirementCollection any(String name, ItemRequirement... requirements) {
        return new AnyRequirementCollection(name, requirements);
    }

    private static RangeItemRequirement range(int startItemId, int endItemId) {
        return new RangeItemRequirement(null, startItemId, endItemId);
    }

    @Override
    public void view(@NotNull final Player player, @NotNull final Item item) {
        player.getTemporaryAttributes().put("Clue scroll item", item);
        GameInterface.CLUE_SCROLL.open(player);
    }

    @Override
    public TreasureTrailType getType() {
        return TreasureTrailType.FALO_THE_BARD;
    }

    @Override
    public String getEnumName() {
        return toString();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public ClueChallenge getChallenge() {
        return new FaloTheBardChallenge(this);
    }

    @NotNull
    @Override
    public ClueLevel level() {
        return ClueLevel.MASTER;
    }
    
    public ItemRequirement[] getItemRequirements() {
        return itemRequirements;
    }
}

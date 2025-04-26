package com.zenyte.game.content.event.christmas2019;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.event.christmas2019.cutscenes.FrozenGuest;
import com.zenyte.game.content.event.christmas2019.cutscenes.PastScourgeCutsceneP2Repeat;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

import static com.zenyte.game.content.event.christmas2019.ChristmasConstants.UNFROZEN_GUESTS_HASH_KEY;

/**
 * @author Kris | 20/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FrozenGuestPlugin implements ObjectAction, ItemOnObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Throw-water")) {
            if (!player.getInventory().containsItem(ChristmasConstants.ICY_WATER_BUCKET, 1)) {
                player.sendMessage("You need a bucket of icy water to unfreeze the guest.");
                return;
            }
            throwWater(player, object, OptionalInt.empty());
        }
    }

    private final void throwWater(@NotNull final Player player, @NotNull final WorldObject object, @NotNull final OptionalInt bucketSlot) {
        final FrozenGuest guest = CollectionUtils.findMatching(FrozenGuest.getValues(), g -> g.getBaseObject() == object.getId());
        Preconditions.checkArgument(guest != null);
        final int previousHash = player.getNumericAttribute(UNFROZEN_GUESTS_HASH_KEY).intValue();
        final int hash = previousHash | (1 << guest.ordinal());
        if (hash == previousHash) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        if (bucketSlot.isPresent()) {
            player.getInventory().deleteItem(bucketSlot.getAsInt(), new Item(ChristmasConstants.ICY_WATER_BUCKET, 1));
        } else {
            player.getInventory().deleteItem(ChristmasConstants.ICY_WATER_BUCKET, 1);
        }
        player.getInventory().addOrDrop(new Item(ItemId.BUCKET, 1));
        player.setInvalidAnimation(new Animation(15082));
        player.setGraphics(new Graphics(2501));
        final NPC npc = World.findNPC(guest.getBaseNPC(), guest.getTile(), 1).orElseThrow(RuntimeException::new);
        npc.setInvalidAnimation(guest.getPreDefreezeAnimation());
        player.addAttribute(UNFROZEN_GUESTS_HASH_KEY, hash);
        ChristmasUtils.refreshAllVarbits(player);
        player.sendMessage("You empty the bucket and free the guest.");
        final String guests = ChristmasUtils.getFrozenGuestOrder(player);
        final char[] chars = guests.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            final char character = chars[i];
            final FrozenGuest respectiveGuest = CollectionUtils.findMatching(FrozenGuest.getValues(), g -> g.getConstant() == character);
            Preconditions.checkArgument(respectiveGuest != null);
            if (respectiveGuest == guest) {
                break;
            }
            final boolean isUnfrozen = ((hash >> respectiveGuest.ordinal()) & 1) == 1;
            if (!isUnfrozen) {
                player.lock();
                WorldTasksManager.schedule(() -> player.getCutsceneManager().play(new PastScourgeCutsceneP2Repeat()), 2);
                return;
            }
        }
        if (hash == Math.pow(2, chars.length) - 1) {
            player.sendMessage("Congratulations, you've beaten Scourge's curse.");
            executeCompletionDialogue(player);
        }
    }

    private void executeCompletionDialogue(@NotNull final Player player) {
        player.lock();
        AChristmasWarble.progressWithoutRefreshing(player, AChristmasWarble.ChristmasWarbleProgress.GHOST_OF_CHRISTMAS_PRESENT);
        WorldTasksManager.schedule(() -> {
            final String impName = ChristmasUtils.getImpName(player);
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    npcWithId(15059, "This is the worst Christmas ever!", Expression.HIGH_REV_MAD);
                    player("I guess getting trapped in fire isn't the best...");
                    npcWithId(15059, "I don't care about no fire, guv'nor, there's going to be no turkey...", Expression.HIGH_REV_MAD);
                    npcWithId(15059, "...no p-party...", Expression.HIGH_REV_MAD);
                    npcWithId(15059, "...and w-worst of all...", Expression.HIGH_REV_MAD);
                    npcWithId(15059, "NO PRESENTS!", Expression.HIGH_REV_MAD);
                    player("He seems really upset.");
                    npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Oh man! I's been a stoopid-head.", Expression.HIGH_REV_NORMAL);
                    player("Why? What's up?");
                    npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "We's been going about this all wrong!", Expression.HIGH_REV_NORMAL);
                    player("Yeah, I was gonna say - dressing up as a ghost was never going to work, maybe we should try someth-");
                    npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "We's been wasting our time digging up " +
                            "Scourge's past. Past is way too long ago. Everyone knows misers have rubbish memory! " +
                            "This weeping boy, he's from presents.", Expression.HIGH_REV_NORMAL);
                    player("Bu-");
                    npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "If there is anyone who can melt the " +
                            "frozen heartstrings of that crankerous ol' miser, it's this angelic boy. We can use this" +
                            " boy to show Scourge how he's hurting people in the presents!", Expression.HIGH_REV_NORMAL);
                    npcWithId(15059, "Presents? I want presents!", Expression.HIGH_REV_MAD);
                    player("He seems a bit spoilt for me. Maybe we should find som-");
                    npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Nah, he's perfect, mate! Let's pay " +
                            "Scourge a visit.", Expression.HIGH_REV_NORMAL).executeAction(() -> {
                        new FadeScreen(player, () -> {
                            player.setLocation(new Location(2464, 5385, 0));
                            player.setFaceLocation(player.getLocation().transform(Direction.NORTH, 1));
                            ChristmasUtils.refreshAllVarbits(player);
                            player.unlock();
                        }).fade(3);
                    });
                }
            });
        }, 1);
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        throwWater(player, object, OptionalInt.of(slot));
    }

    @Override
    public Object[] getItems() {
        return new Object[] {ChristmasConstants.ICY_WATER_BUCKET};
    }

    @Override
    public Object[] getObjects() {
        final IntOpenHashSet set = new IntOpenHashSet();
        for (final FrozenGuest guest : FrozenGuest.getValues()) {
            final int baseObject = guest.getBaseObject();
            set.add(baseObject);
        }
        return set.toArray();
    }
}

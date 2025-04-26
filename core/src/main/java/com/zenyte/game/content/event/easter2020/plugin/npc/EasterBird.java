package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.SplittingHeirs;
import com.zenyte.game.content.event.easter2020.Stage;
import com.zenyte.game.content.event.easter2020.plugin.object.SeedBowl;
import com.zenyte.game.content.event.easter2020.plugin.object.WaterBowl;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.plugins.SkipPluginScan;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Corey
 * @since 01/04/2020
 */
@SkipPluginScan
public class EasterBird extends NPCPlugin implements ItemOnNPCAction {
   public static final int RASPBERRY = 10;
   public static final int CHEER = 20;
   public static final int WAVE = 30;
   public static final int CLAP = 40;

   @Override
   public void handle() {
      bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
         @Override
         public void buildDialogue() {
            if (SplittingHeirs.progressedAtLeast(player, Stage.BIRD_FED_SPEAK_TO_BUNNY_JR)) {
               npc("Squawk!", Expression.EASTER_BIRD_HAPPY);
               return;
            } else if (!SplittingHeirs.progressedAtLeast(player, Stage.CHECK_TODO)) {
               player("Guess I'd better go look at the to-do list before interfering with things, like the Easter " +
                       "Bunny's son said.");
               return;
            }
            final SeedBowl.Contents requiredFood = SeedBowl.getRequiredBowl(player);
            if (!SplittingHeirs.progressedAtLeast(player, Stage.FEED_BIRD)) {
               player("Hello...erm...birdy?");
               npc("Zzzzzzzzzzz...", Expression.EASTER_BIRD_SNORING);
               options(TITLE, new DialogueOption("Blow a raspberry at the bird.", () -> {
                  player.setAnimation(Emote.RASPBERRY.getAnimation());
                  SplittingHeirs.advanceStage(player, Stage.FEED_BIRD);
                  setKey(RASPBERRY);
               }), new DialogueOption("Cheer at the bird.", () -> {
                  player.setAnimation(Emote.CHEER.getAnimation());
                  SplittingHeirs.advanceStage(player, Stage.FEED_BIRD);
                  setKey(CHEER);
               }), new DialogueOption("Wave at the bird.", () -> {
                  player.setAnimation(Emote.WAVE.getAnimation());
                  SplittingHeirs.advanceStage(player, Stage.FEED_BIRD);
                  setKey(WAVE);
               }), new DialogueOption("Clap at the bird.", () -> {
                  player.setAnimation(Emote.CLAP.getAnimation());
                  SplittingHeirs.advanceStage(player, Stage.FEED_BIRD);
                  setKey(CLAP);
               }));
               plain(RASPBERRY, "You blow a raspberry at the bird, startling it awake.");
               plain(CHEER, "You cheer at the bird, startling it awake.");
               plain(WAVE, "You wave at the bird, startling it awake.");
               plain(CLAP, "You clap at the bird, startling it awake.");
            } else {
               if (!Varbit.EMPTY_BOWL.exists(player)) {
                  npc("Squawwwk! Feed me!", Expression.EASTER_BIRD_CHATTY);
                  plain("The bird looks really hungry - almost as if it could eat a unicorn.");
                  player("Okay, but what? There's so many different seeds there.");
                  npc("Squark! " + requiredFood.getFoodMessage(), Expression.EASTER_BIRD_CHATTY);
                  player("Well, that's a clue, I guess. Best go look at the sacks of seed.");
               } else if (Varbit.EMPTY_BOWL.getCurrentValue(player) != requiredFood.getVarbit().getValue()) {
                  npc("Squawwwk! Feed me!", Expression.EASTER_BIRD_CHATTY);
                  player("What? I already gave you food!", Expression.ANNOYED);
                  npc("Squawwwk! Wrong food!", Expression.EASTER_BIRD_SAY_NO);
                  player("Okay, but what? There's so many different seeds there.");
                  npc("Squark! " + requiredFood.getFoodMessage(), Expression.EASTER_BIRD_CHATTY);
                  player("Well, that's a clue, I guess. Best go look at the sacks of seed.");
               } else if (!Varbit.WATER_BOWL.exists(player)) {
                  npc("Squawwwk! Feed me!", Expression.EASTER_BIRD_CHATTY);
                  player("I already have. You have seed, what else do you want?");
                  npc("Squawk...thirsty.", Expression.EASTER_BIRD_CHATTY);
                  player("I guess I'd better find you some water.");
               } else {
                  npc("Squawwwk! Feed me!", Expression.EASTER_BIRD_CHATTY);
                  player("I have, and you have fresh water too.");
                  npc("Squawk! Shiver me Squirrels!", Expression.EASTER_BIRD_HAPPY);
                  player("Now will you start laying eggs?");
                  npc("Squawk! Aye, aye, cap'n!", Expression.EASTER_BIRD_HAPPY).executeAction(() -> SplittingHeirs.advanceStage(player, Stage.BIRD_FED_SPEAK_TO_BUNNY_JR));
                  player("Excellent; that's wrapped up.");
               }
            }
         }
      }));
   }

   @Override
   public int[] getNPCs() {
      return new int[] {EasterConstants.EASTER_BIRD_NPC};
   }

   @Override
   public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
      if (item.getId() == ItemId.BUCKET_OF_WATER) {
         WaterBowl.waterOnBowl(player, item);
      } else {
         SeedBowl.foodOnBowl(player, item);
      }
   }

   @Override
   public Object[] getItems() {
      final Object[] food = SeedBowl.Contents.byFoodItemId.keySet().stream().filter(i -> i != -1).toArray();
      return ArrayUtils.add(food, ItemId.BUCKET_OF_WATER);
   }

   @Override
   public Object[] getObjects() {
      return new Object[] {EasterConstants.EASTER_BIRD_NPC};
   }


   public enum Varbit {
      EMPTY_BOWL(EasterConstants.EASTER_BIRD_SEED_BOWL_VARBIT, 0), SUNFLOWER_SEED_BOWL(EasterConstants.EASTER_BIRD_SEED_BOWL_VARBIT, 1), CRACKER_BITS_BOWL(EasterConstants.EASTER_BIRD_SEED_BOWL_VARBIT, 2), WORM_BITS_BOWL(EasterConstants.EASTER_BIRD_SEED_BOWL_VARBIT, 3), POPPY_SEED_BOWL(EasterConstants.EASTER_BIRD_SEED_BOWL_VARBIT, 4), WATER_BOWL(EasterConstants.EASTER_BIRD_WATER_BOWL_VARBIT);
      private final int varbit;
      private final int value;

      Varbit() {
         this.varbit = -1;
         this.value = -1;
      }

      Varbit(final int varbit) {
         this.varbit = varbit;
         this.value = 1;
      }

      public void sendVar(final Player player) {
         if (varbit != -1) {
            player.getVarManager().sendBit(varbit, value);
         }
      }

      public int getCurrentValue(final Player player) {
         return player.getVarManager().getBitValue(varbit);
      }

      public boolean isSet(final Player player) {
         return player.getVarManager().getBitValue(varbit) == value;
      }

      public boolean exists(final Player player) {
         return player.getVarManager().getBitValue(varbit) != 0;
      }

      Varbit(int varbit, int value) {
         this.varbit = varbit;
         this.value = value;
      }

      public int getVarbit() {
         return varbit;
      }

      public int getValue() {
         return value;
      }
   }
}

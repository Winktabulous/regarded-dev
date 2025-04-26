package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

/**
 * @author Corey
 * @since 11:46 - 22/07/2019
 */
public class Pyromancer extends NPC {
   private static final String[] incapacitatedPhrases = new String[] {"Mummy!", "My flame burns low.", "We are doomed.", "I think I'm dying.", "Ugh, help me!"};
   private int ticks = 5;
   private boolean beingAttacked = false;

   Pyromancer(Location tile, Direction facing) {
      super(State.HEALTHY.getNpcId(), tile, facing, 0);
   }

   @Override
   public void processNPC() {
      super.processNPC();
      randomActions();
   }

   @Override
   public void sendDeath() {
      transitionState(State.INCAPACITATED);
      setForceTalk(new ForceTalk("Arg, it got me!"));
      setAnimation(new Animation(7627));
      World.sendSoundEffect(getPosition(), new SoundEffect(512, 3, 0));
      Wintertodt.setPyromancersFallen(true);
   }

   void transitionState(final State state, final boolean refreshOverlay) {
      if (id == state.getNpcId()) {
         return;
      }
      if (Wintertodt.betweenRounds()) {
         return;
      }
      setTransformation(state.getNpcId());
      setAnimation(Animation.STOP);
      if (state == State.HEALTHY) {
         setHitpoints(getMaxHitpoints());
      }
      if (refreshOverlay) {
         Wintertodt.refreshOverlayForAllPlayers();
      }
   }

   @Override
   public void reset() {
      setTransformation(State.HEALTHY.getNpcId());
      setAnimation(Animation.STOP);
      setHitpoints(getMaxHitpoints());
      heal(getMaxHitpoints());
      super.reset();
   }

   private void transitionState(final State state) {
      transitionState(state, true);
   }

   void startColdAttack() {
      beingAttacked = true;
      World.spawnTemporaryObject(new WorldObject(Wintertodt.SNOWFALL_OBJECT, 10, 0, getX(), getY(), 0), 2);
      WorldTasksManager.schedule(() -> {
         if (Wintertodt.betweenRounds()) {
            beingAttacked = false;
            return;
         }
         World.sendGraphics(new Graphics(502), getLocation());
         WorldTasksManager.schedule(() -> this.applyHit(new Hit(Utils.random(5, 15), HitType.DEFAULT)), 1);
         WorldTasksManager.schedule(() -> beingAttacked = false, 5);
      }, 2);
   }

   private void randomActions() {
      if (Wintertodt.betweenRounds()) {
         return;
      }
      if (--ticks > 0) {
         return;
      }
      final Brazier brazier = Wintertodt.Corner.getFromPyromancer(this).getBrazier();
      if (brazier.isLit() && isHealthy()) {
         if (Utils.random(0, 5) == 0) {
            setAnimation(new Animation(4432));
         }
      }
      if (Utils.random(0, 10) != 0) {
         return;
      }
      if (!isHealthy()) {
         setForceTalk(new ForceTalk(Utils.random(incapacitatedPhrases)));
         ticks = 5;
         return;
      }
      setForceTalk(new ForceTalk(brazier.getState().getPryomancerPhrase()));
      ticks = 5;
   }

   boolean isHealthy() {
      return id == State.HEALTHY.getNpcId();
   }


   public enum State {
      HEALTHY(7371), INCAPACITATED(7372);
      private final int npcId;

      State(int npcId) {
         this.npcId = npcId;
      }

      public int getNpcId() {
         return npcId;
      }
   }


   public static final class PotionOnPyromancer implements ItemOnNPCAction {
      @Override
      public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
         final Pyromancer pyromancer = (Pyromancer) npc;
         if (!RejuvenationPotion.Potion.finishedPotions.contains(item.getId())) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
               @Override
               public void buildDialogue() {
                  npc("That's no use!");
               }
            });
            return;
         } else if (Wintertodt.betweenRounds()) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
               @Override
               public void buildDialogue() {
                  npc("Looks like it's quiet for now. I don't need help, but thank you.");
               }
            });
            return;
         } else if (pyromancer.isHealthy() && pyromancer.getHitpoints() == pyromancer.getMaxHitpoints()) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
               @Override
               public void buildDialogue() {
                  npc("No need! I'm fine thanks.");
               }
            });
            player.sendMessage("You don't need to do that");
            return;
         }
         player.getInventory().set(slot, RejuvenationPotion.getNextPotion(item.getId()));
         if (!pyromancer.isHealthy()) {
            player.getCombatAchievements().complete(CAType.MUMMY);
            pyromancer.transitionState(State.HEALTHY);
         } else {
            pyromancer.heal(8);
         }
         Wintertodt.addPoints(player, 30);
      }

      @Override
      public Object[] getItems() {
         return new Object[] {-1};
      }

      @Override
      public Object[] getObjects() {
         return new Object[] {State.INCAPACITATED.getNpcId(), State.HEALTHY.getNpcId()};
      }
   }


   public static final class HelpPyromancer extends NPCPlugin {
      @Override
      public void handle() {
         bind("Help", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
               final Pyromancer pyromancer = (Pyromancer) npc;
               if (pyromancer.isHealthy() || Wintertodt.betweenRounds()) {
                  return;
               }
               player.stopAll();
               player.faceEntity(npc);
               int slot = -1;
               Item firstItem = null;
               for (final Int2ObjectMap.Entry<Item> entrySet : player.getInventory().getContainer().getItems().int2ObjectEntrySet()) {
                  final Item item = entrySet.getValue();
                  if (RejuvenationPotion.Potion.finishedPotions.contains(item.getId())) {
                     firstItem = item;
                     slot = entrySet.getIntKey();
                     break;
                  }
               }
               if (firstItem == null) {
                  player.getDialogueManager().start(new Dialogue(player, npc) {
                     @Override
                     public void buildDialogue() {
                        plain("You need a rejuvenation potion made from the bruma herb to do<br><br>that.");
                     }
                  });
                  return;
               }
               player.getInventory().set(slot, RejuvenationPotion.getNextPotion(firstItem.getId()));
               pyromancer.transitionState(State.HEALTHY);
               player.getCombatAchievements().complete(CAType.MUMMY);
               Wintertodt.addPoints(player, 30);
            }
            @Override
            public void execute(Player player, NPC npc) {
               player.stopAll();
               player.setFaceEntity(npc);
               handle(player, npc);
            }
         });
      }

      @Override
      public int[] getNPCs() {
         return new int[] {State.INCAPACITATED.getNpcId()};
      }
   }

   public boolean isBeingAttacked() {
      return beingAttacked;
   }
}

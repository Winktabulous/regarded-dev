package com.zenyte.game.world.entity.player.cutscene;

import com.zenyte.game.model.MinimapState;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.actions.DialogueAction;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kris | 4. dets 2017 : 0:23.00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 * 
 *      An abstract class with a lot of built-in methods for cutscenes.
 */
public abstract class Cutscene {

	/**
	 * The main player of the cutscene around whom it revolves.
	 */
	protected Player player;

	/**
	 * A potential list of NPCs in the case of dynamic regions. Use addNPC(final
	 * NPC npc) method to spawn npcs. The NPCs will all be cleared up
	 * automatically at the end of the cutscene.
	 */
	protected List<NPC> npcs;

	/**
	 * A map of actions to be ran in the cutscene.
	 * The key of the map stands for the delay in ticks before the 
	 * actions {@code runnable} are to be ran.
	 */
	private final Map<Integer, Runnable[]> actions = new HashMap<Integer, Runnable[]>();

	private final List<DialogueAction> blockingActions = new ObjectArrayList<>();

	
	/**
	 * The duration of the cutscene as it progresses.
	 */
	private int duration;
	
	/**
	 * The length of the cutscene as a whole, in ticks. 
	 * Variable is automatically generated based off of the actions.
	 */
	private int length;

	/**
	 * The state of the minimap during the cutscene.
	 * 
	 * @return the state.
	 */
	public MinimapState getMinimapState() {
		return MinimapState.ENABLED;
	}

	/**
	 * Whether to use large or small scene view. Large scene view displays NPCs
	 * within 126 tiles from the player, small displays them within 14 tiles.
	 * 
	 * @return whether it's large or small view.
	 */
	public boolean isLargeSceneView() {
		return false;
	}
	
	/**
	 * An empty method that's ran on logout if the player
	 * is still within a cutscene.
	 */
	public void logout() {}
	
	public abstract void build();
	
	/**
	 * A method that processes the cutscene in general.
	 * The scene is automatically completely destroyed
	 * once the duration of it reaches length + 3 ticks.
	 * @return whether to finish the cutscene or not.
	 */
	public final boolean process() {
		if (!blockingActions.isEmpty()) {
			blockingActions.removeIf(dialogueAction -> {
				if (dialogueAction.isFinished()) {
					dialogueAction.setFinished(false);
					return true;
				}
				return false;
			});
			if (!blockingActions.isEmpty()) {
				return true;
			}
			duration++;
		}
		if (duration >= length + 3) {
			if (npcs != null) {
				for (NPC n : npcs) {
					if (n == null)
						continue;
					n.finish();
				}
			}
			return false;
		} else if (duration == length) {
			if (getMinimapState() != MinimapState.ENABLED)
				player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
		} else if (duration == 0) {
			if (getMinimapState() != MinimapState.ENABLED)
				player.getPacketDispatcher().sendMinimapState(getMinimapState());
		}
		if (actions.get(duration) != null) {
			for (Runnable action : actions.get(duration)) {
				action.run();
				if (action instanceof DialogueAction) {
					blockingActions.add((DialogueAction) action);
				}
			}
		}
		if (blockingActions.isEmpty()) {
			duration++;
		}
		return true;
	}

	protected final void jump(final int timeStamp) {
		this.duration = timeStamp - 1;
	}

	protected void finish() {
		player.getCutsceneManager().finish();
	}

	/**
	 * Adds a NPC to the list of npcs. Constructs the list if it's null (default
	 * state) The NPC is spawned instantaneously as it's done through the
	 * constructor.
	 * 
	 * @param npc
	 *            NPC to spawn.
	 */
	public final void addNPC(final NPC npc) {
		if (npcs == null)
			npcs = new ArrayList<NPC>();
		npcs.add(npc);
	}

	/**
	 * Adds an array of actions to the timeline.
	 * 
	 * @param time
	 *            the duration in ticks before the action should be ran.
	 * @param action
	 *            an array of runnable actions to enqueue.
	 * Automatically recreates the action set if there's already a set with the
	 * specified delay
	 */
	public void addActions(final int time, final Runnable... action) {
		if (time > length)
			length = time;
		if (actions.containsKey(time)) {
			final Runnable[] existingActions = actions.get(time);
			final Runnable[] actionSet = new Runnable[action.length + existingActions.length];
			System.arraycopy(existingActions, 0, actionSet, 0, existingActions.length);
			System.arraycopy(action, 0, actionSet, 0 + existingActions.length, action.length);
			actions.put(time, actionSet);
			return;
		}
		actions.put(time, action);
	}

	public void overrideActions(final int time, final Runnable... action) {
		if (time > length)
			length = time;
		actions.put(time, action);
	}

	protected void turnLeft() {
		player.setInvalidAnimation(new Animation(15102));
		player.addTemporaryAttribute("facing animation direction", 15102);
	}

	protected void turnStraight() {
		player.setInvalidAnimation(new Animation(player.getNumericTemporaryAttribute("facing animation direction").intValue() == 15102 ? 15103 : 15105));
	}

	protected void turnRight() {
		player.setInvalidAnimation(new Animation(15104));
		player.addTemporaryAttribute("facing animation direction", 15104);
	}
	
	public void setPlayer(Player player) {
	    this.player = player;
	}

}

package com.zenyte.game.world.entity.player.dialogue;

import com.near_reality.game.world.entity.player.dialogue.LoadingMessage;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.MakeType;
import com.zenyte.plugins.dialogue.OptionDialogue;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.npcs.NPCDefinitions;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Arham 4 on 2/15/2016.
 * <p>
 * Represents a Dialogue (which contains Messages)
 */
public abstract class Dialogue {

    private static final Logger log = Logger.getLogger(Dialogue.class.getName());

    public static final String TITLE = "Select an Option";
    protected final Int2ObjectOpenHashMap<Message> dialogue;
    protected final Player player;
    protected int key = 1;
    protected int npcId;
    protected NPC npc;
    private Runnable onCloseRunnable;
    private Runnable onBuild;

    public Dialogue(final Player player, final int npcId) {
        this.player = player;
        dialogue = new Int2ObjectOpenHashMap<>();
        this.npcId = npcId;
        npc = null;
    }

    public Dialogue(final Player player, final NPC npc) {
        this.player = player;
        dialogue = new Int2ObjectOpenHashMap<>();
        npcId = npc.getId();
        this.npc = npc;
    }

    public Dialogue(final Player player, final int npcId, final NPC npc) {
        this.player = player;
        dialogue = new Int2ObjectOpenHashMap<>();
        this.npcId = npcId;
        this.npc = npc;
    }

    public Dialogue(final Player player) {
        this.player = player;
        dialogue = new Int2ObjectOpenHashMap<>();
        npcId = -1;
        npc = null;
    }

    /**
     * Where we make the magic with the dialogue HashMap happen.
     */
    public abstract void buildDialogue();

    public void communicate() {
        if (dialogue.containsKey(key)) {
            dialogue.get(key).display(player);
        }
    }

    public PlayerMessage player(final String message) {
        final PlayerMessage msg = new PlayerMessage(Expression.CALM, message);
        dialogue.put(key++, msg);
        return msg;
    }

    public PlayerMessage player(final int stage, final String message) {
        final PlayerMessage msg = new PlayerMessage(Expression.CALM, message);
        key = stage + 1;
        dialogue.put(stage, msg);
        return msg;
    }

    public PlayerMessage player(final String message, final Expression expression) {
        final PlayerMessage msg = new PlayerMessage(expression, message);
        dialogue.put(key++, msg);
        return msg;
    }

    public PlayerMessage player(final int stage, final String message, final Expression expression) {
        final PlayerMessage msg = new PlayerMessage(expression, message);
        key = stage + 1;
        dialogue.put(stage, msg);
        return msg;
    }

    public SkillMessage skill(final int maxQuantity, final boolean maxTen, final String message, final Item... items) {
        final SkillMessage msg = new SkillMessage(maxQuantity, maxTen, message, items);
        dialogue.put(key++, msg);
        return msg;
    }

    public SkillMessage skill(final int maxQuantity, final MakeType type, final String message, final Item... items) {
        final SkillMessage msg = new SkillMessage(maxQuantity, type, message, items);
        dialogue.put(key++, msg);
        return msg;
    }

    public NPCMessage npc(final String npcName, final String message) {
        final NPCMessage msg = new NPCMessage(NPC.getTransformedId(npcId, player), Expression.CALM, message, npcName);
        dialogue.put(key++, msg);
        return msg;
    }

    public NPCMessage npc(final String npcName, final String message, final Expression expression) {
        final NPCMessage msg = new NPCMessage(NPC.getTransformedId(npcId, player), expression, message, npcName);
        dialogue.put(key++, msg);
        return msg;
    }

    public NPCMessage npc(final String npcName, final String message, final boolean showContinue) {
        final NPCMessage msg = new NPCMessage(NPC.getTransformedId(npcId, player), Expression.CALM, message, showContinue, npcName);
        dialogue.put(key++, msg);
        return msg;
    }

    public NPCMessage npc(final String message) {
        final NPCMessage msg = new NPCMessage(NPC.getTransformedId(npcId, player), Expression.CALM, message);
        dialogue.put(key++, msg);
        return msg;
    }

    public NPCMessage npc(final String message, final Expression expression) {
        final NPCMessage msg = new NPCMessage(NPC.getTransformedId(npcId, player), expression, message);
        dialogue.put(key++, msg);
        return msg;
    }

    public NPCMessage npc(final String message, final boolean showContinue) {
        final NPCMessage msg = new NPCMessage(NPC.getTransformedId(npcId, player), Expression.CALM, message, showContinue);
        dialogue.put(key++, msg);
        return msg;
    }

    public NPCMessage npcWithId(final int npcId, final String npcName, final String message, final Expression expression) {
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(npcId);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, npcId);
        final NPCMessage msg = new NPCMessage(transmogrifiedId, expression, message, npcName);
        dialogue.put(key++, msg);
        return msg;
    }

    public ItemMessage item(final int stage, final int itemId, final String message) {
        final ItemMessage msg = new ItemMessage(itemId, message);
        key = stage + 1;
        dialogue.put(stage, msg);
        return msg;
    }
    public ItemMessage item(final int stage, final Item item, final String message) {
        final ItemMessage msg = new ItemMessage(item, message, true);
        key = stage + 1;
        dialogue.put(stage, msg);
        return msg;
    }

    public ItemMessage item(final Item item, final String message) {
        return item(item, message, true);
    }

    public ItemMessage item(final Item item, final String message, final boolean showContinue) {
        final ItemMessage msg = new ItemMessage(item, message, showContinue);
        dialogue.put(key++, msg);
        return msg;
    }

    public ItemMessage item(final int itemId, final String message) {
        final ItemMessage msg = new ItemMessage(itemId, message);
        dialogue.put(key++, msg);
        return msg;
    }

    public DestroyItemMessage destroyItem(final Item item, final String message) {
        final DestroyItemMessage msg = new DestroyItemMessage(item, message);
        dialogue.put(key++, msg);
        return msg;
    }

    public DestroyItemMessage destroyItem(final Item item, final String title, final String message) {
        final DestroyItemMessage msg = new DestroyItemMessage(item, title, message);
        dialogue.put(key++, msg);
        return msg;
    }

    public DoubleItemMessage doubleItem(final int first, final int second, final String message) {
        return doubleItem(new Item(first), new Item(second), message);
    }

    public DoubleItemMessage doubleItem(final Item first, final Item second, final String message) {
        final DoubleItemMessage msg = new DoubleItemMessage(first, second, message);
        dialogue.put(key++, msg);
        return msg;
    }

    public DoubleItemMessage doubleItem(final int stage, final Item first, final Item second, final String message) {
        key = stage + 1;
        final DoubleItemMessage msg = new DoubleItemMessage(first, second, message);
        dialogue.put(stage, msg);
        return msg;
    }

    public PlainMessage plain(final String message) {
        return plain(message, true);
    }

    public PlainMessage plain(final int stage, final String message) {
        return plain(stage, message, true);
    }

    public PlainMessage plain(final String message, final int lineSpacing) {
        final PlainMessage msg = new PlainMessage(message, true, lineSpacing);
        dialogue.put(key++, msg);
        return msg;
    }

    public PlainMessage plain(final String message, final boolean showContinue, final int lineSpacing) {
        final PlainMessage msg = new PlainMessage(message, showContinue, lineSpacing);
        dialogue.put(key++, msg);
        return msg;
    }

    public PlainMessage plain(final String message, final boolean showContinue) {
        final PlainMessage msg = new PlainMessage(message, showContinue);
        dialogue.put(key++, msg);
        return msg;
    }

    public PlainMessage plain(final int stage, final String message, final boolean showContinue) {
        key = stage + 1;
        final PlainMessage msg = new PlainMessage(message, showContinue);
        dialogue.put(stage, msg);
        return msg;
    }

    public PlainMessage plain(final int stage, final String message, final boolean showContinue, final int lineSpacing) {
        key = stage + 1;
        final PlainMessage msg = new PlainMessage(message, showContinue, lineSpacing);
        dialogue.put(stage, msg);
        return msg;
    }

    public NPCMessage npc(final int stage, final String message) {
        key = stage + 1;
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(npcId);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, npcId);
        final NPCMessage msg = new NPCMessage(transmogrifiedId, Expression.CALM, message);
        dialogue.put(stage, msg);
        return msg;
    }

    public NPCMessage npc(final int npcId, final String message, final int stage) {
        key++;
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(npcId);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, npcId);
        final NPCMessage msg = new NPCMessage(transmogrifiedId, Expression.CALM, message);
        dialogue.put(stage, msg);
        return msg;
    }

    public NPCMessage npc(final int stage, final String message, final Expression expression) {
        key = stage + 1;
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(npcId);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, npcId);
        final NPCMessage msg = new NPCMessage(transmogrifiedId, expression, message);
        dialogue.put(stage, msg);
        return msg;
    }

    public NPCMessage npc(final int npcId, final String npcName, final String message, final int stage, final Expression expression) {
        key++;
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(npcId);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, npcId);
        final NPCMessage msg = new NPCMessage(transmogrifiedId, expression, message, npcName);
        dialogue.put(stage, msg);
        return msg;
    }

    public NPCMessage npc(final int npcId, final String npcName, final String message, final int stage) {
        key++;
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(npcId);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, npcId);
        final NPCMessage msg = new NPCMessage(transmogrifiedId, Expression.CALM, message, npcName);
        dialogue.put(stage, msg);
        return msg;
    }

    public NPCMessage npc(final int stage, final String npcName, final String message, final Expression expression) {
        key = stage + 1;
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(npcId);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, npcId);
        final NPCMessage msg = new NPCMessage(transmogrifiedId, expression, message, npcName);
        dialogue.put(stage, msg);
        return msg;
    }

    public NPCMessage npc(final int npcId, final String message, final int stage, final Expression expression) {
        key++;
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(npcId);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, npcId);
        final NPCMessage msg = new NPCMessage(transmogrifiedId, expression, message);
        dialogue.put(stage, msg);
        return msg;
    }

    public NPCMessage npcWithId(final int npcId, final String message, final Expression expression) {
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(npcId);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, npcId);
        final NPCMessage msg = new NPCMessage(transmogrifiedId, expression, message);
        dialogue.put(key++, msg);
        return msg;
    }

    public OptionMessage options(final String title, final String... options) {
        final OptionMessage message = new OptionMessage(title, options);
        dialogue.put(key++, message);
        return message;
    }

    public OptionMessage options(final int stage, final String title, final String... options) {
        key = stage + 1;
        final OptionMessage message = new OptionMessage(title, options);
        dialogue.put(stage, message);
        return message;
    }

    public DualItemOptionMessage itemOptions(final int stage, final String title, final int item1, final int item2, final String option1, final String option2) {
        key = stage + 1;
        final DualItemOptionMessage message = new DualItemOptionMessage(title, item1, item2, option1, option2);
        dialogue.put(stage, message);
        return message;
    }

    public OptionMessage options(final DialogueOption... options) {
        return options(TITLE, options);
    }

    public OptionMessage options(final String title, final DialogueOption... options) {
        DialogueOption[] cleanedOptions = ArrayUtils.removeAllOccurrences(options, null);
        assert cleanedOptions.length > 0;
        final String[] ops = new String[cleanedOptions.length];
        final Runnable[] runs = new Runnable[cleanedOptions.length];
        DialogueOption op;
        for (int i = 0; i < cleanedOptions.length; i++) {
            op = cleanedOptions[i];
            ops[i] = op.getOption();
            runs[i] = op.getRunnable();
        }
        final OptionMessage message = new OptionMessage(title, ops);
        message.setOptions(runs);
        dialogue.put(key++, message);
        return message;
    }

    public OptionMessage options(final int stage, final DialogueOption... options) {
        return options(stage, TITLE, options);
    }

    public OptionMessage options(final int stage, final String title, final DialogueOption... options) {
        key = stage + 1;
        final String[] ops = new String[options.length];
        final Runnable[] runs = new Runnable[options.length];
        DialogueOption op;
        for (int i = 0; i < options.length; i++) {
            op = options[i];
            ops[i] = op.getOption();
            runs[i] = op.getRunnable();
        }
        final OptionMessage message = new OptionMessage(title, ops);
        message.setOptions(runs);
        dialogue.put(stage, message);
        return message;
    }

    public DecantingMessage decant(final int stage) {
        key = stage + 1;
        final DecantingMessage message = new DecantingMessage();
        dialogue.put(stage, message);
        return message;
    }

    public DecantingMessage decant() {
        final DecantingMessage message = new DecantingMessage();
        dialogue.put(key++, message);
        return message;
    }

    public LoadingMessage loading(String loadingText) {
        final LoadingMessage loadingMessage = new LoadingMessage(loadingText);
        dialogue.put(key++, loadingMessage);
        return loadingMessage;
    }

    public Runnable key(final int key) {
        return () -> setKey(key);
    }

    public void finish() {
        if (player.getDialogueManager().getLastDialogue() == this) {
            key = -1;
            player.getDialogueManager().finish();
            if (npc != null) {
                npc.finishInteractingWith(player);
            }
        }
    }

    /**
     * Better name?
     */
    public void communicateNext() {
        final Message message = dialogue.get(key);
        if (message instanceof OptionMessage) {
            return;
        }
        if (message == null) {
            log.log(Level.WARNING, "Message is null. Error with " + this, new Throwable());
        }
        key++;
        message.execute(player);
        if (isFinished()) {
            finish();
        }
        if (isFinished()) {
            return;
        }
        communicate();
        if (npc != null) {
            npc.setInteractingWith(player);
        }
    }

    /**
     * Better name? Possibly a better way to do this?
     */
    public void communicateNext(final int slotId, final int componentId) {
        final Message object = dialogue.get(key);
        if (object instanceof OptionMessage) {
            final OptionMessage message = (OptionMessage) object;
            final int currentKey = key;
            message.onClick(componentId);
            if (key == currentKey && equals(player.getDialogueManager().getLastDialogue())) {
                finish();
            }
            if (npc != null) {
                npc.setInteractingWith(player);
            }
        } else if (object instanceof DestroyItemMessage) {
            final DestroyItemMessage message = (DestroyItemMessage) object;
            final int currentKey = key;
            message.onClick(player, componentId);
            if (key == currentKey && equals(player.getDialogueManager().getLastDialogue())) {
                finish();
            }
            if (npc != null) {
                npc.setInteractingWith(player);
            }
        } else if (object instanceof DecantingMessage) {
            final DecantingMessage message = (DecantingMessage) object;
            final int currentKey = key;
            message.onClick(player, componentId);
            if (key == currentKey && equals(player.getDialogueManager().getLastDialogue())) {
                finish();
            }
            if (npc != null) {
                npc.setInteractingWith(player);
            }
        } else if (object instanceof DualItemOptionMessage) {
            final DualItemOptionMessage message = (DualItemOptionMessage) object;
            final int currentKey = key;
            message.onClick(componentId - 1);
            if (key == currentKey && equals(player.getDialogueManager().getLastDialogue())) {
                finish();
            }
            if (npc != null) {
                npc.setInteractingWith(player);
            }
        }
    }

    /**
     * I don't want to obligate the State enum, but its needed for option dialogues. However, if you don't use the option dialogue, then the
     * key will handle the job for you.
     */
    protected boolean isFinished() {
        return key == -1 || !dialogue.containsKey(key) || this instanceof OptionDialogue;
    }

    protected void forceKey(final int key) {
        this.key = key;
    }

    public Player getPlayer() {
        return player;
    }

    public int getKey() {
        return key;
    }

    /**
     * Used for option dialogue.
     */
    protected void setKey(final int key) {
        if (player.getDialogueManager().getLastDialogue() != this) {
            return;
        }
        this.key = key;
        communicate();
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    public NPC getNpc() {
        return npc;
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }

    public Runnable getOnCloseRunnable() {
        return onCloseRunnable;
    }

    public Dialogue setOnCloseRunnable(Runnable onCloseRunnable) {
        this.onCloseRunnable = onCloseRunnable;
        return this;
    }

    public Runnable getOnBuild() {
        return onBuild;
    }

    public void setOnBuild(Runnable onBuild) {
        this.onBuild = onBuild;
    }

    public static Dialogue create(Player player, int npcId, Consumer<Dialogue> buildDialogue) {
        return new Dialogue(player, npcId) {
            @Override
            public void buildDialogue() {
                buildDialogue.accept(this);
            }
        };
    }

    public static Dialogue create(Player player, NPC npc, Consumer<Dialogue> buildDialogue) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                buildDialogue.accept(this);
            }
        };
    }

    public static Dialogue create(Player player, int npcId, NPC npc, Consumer<Dialogue> buildDialogue) {
        return new Dialogue(player, npcId, npc) {
            @Override
            public void buildDialogue() {
                buildDialogue.accept(this);
            }
        };
    }

    public static Dialogue create(Player player, Consumer<Dialogue> buildDialogue) {
        return new Dialogue(player) {
            @Override
            public void buildDialogue() {
                buildDialogue.accept(this);
            }
        };
    }

    public static class DialogueOption {
        private final String option;
        private Runnable runnable;

        public DialogueOption(final String option) {
            this(option, null);
        }

        public DialogueOption(final String option, final Runnable runnable) {
            this.option = option;
            this.runnable = runnable;
        }

        public String getOption() {
            return option;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }
    }
}

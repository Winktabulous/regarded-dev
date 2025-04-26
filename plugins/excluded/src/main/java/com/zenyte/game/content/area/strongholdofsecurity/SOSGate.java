package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.game.content.area.strongholdofsecurity.StrongholdOfSecurity.Question;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.entity.player.dialogue.NPCMessage;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import mgi.utilities.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 4. sept 2018 : 21:37:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SOSGate implements ObjectAction {

    private static final Animation OPEN = new Animation(4282);

    private static final Animation POST_OPEN = new Animation(4283);

    private static final SoundEffect SOUND = new SoundEffect(2858);

    private static final Location[] ENTRY_POINTS = new Location[] { new Location(1859, 5239, 0), new Location(1858, 5239, 0) };

    private enum Gate {

        GATE_OF_WAR(2494, new int[] { 19206, 19207 }), RICKETY_DOOR(2495, new int[] { 17009, 17100 }), OOZING_BARRIER(2496, new int[] { 23653, 23654 }), PORTAL_OF_DEATH(2497, new int[] { 23727, 23728 });

        private static final Gate[] VALUES = values();

        private final int npcId;

        private final int[] gateIds;

        Gate(int npcId, int[] gateIds) {
            this.npcId = npcId;
            this.gateIds = gateIds;
        }
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Open")) {
            final int objectId = object.getId();
            if (objectId == 19206 || objectId == 19207) {
                if (CollectionUtils.findMatching(ENTRY_POINTS, tile -> tile.matches(player)) != null) {
                    player.getDialogueManager().start(new Dialogue(player, 2494) {

                        @Override
                        public void buildDialogue() {
                            npc("Greeting Adventurer. This place is kept safe by the spirits within the doors. As you pass through you will be asked questions about security. Hopefully you will learn much from us.");
                            npc("Please pass through and begin your adventure, beware of the various monsters that dwell within.").executeAction(() -> pass(player, object));
                            if (!player.getBooleanAttribute("sos visited")) {
                                player("Oh my! I just got sucked through that door... what a weird feeling! Still, I guess I should expect it as these evidently aren\'t your average kind of doors... they talk and look creepy!", Expression.ANXIOUS);
                                player.addAttribute("sos visited", 1);
                            }
                        }
                    });
                    return;
                }
            }
            pass(player, object);
        }
    }

    private static final Location exception = new Location(1906, 5242, 0);

    private static final void pass(final Player player, final WorldObject object) {
        final int objectId = object.getId();
        final int type = object.getType();
        final int rotation = object.getRotation();
        final int offset = object.withinDistance(exception, 3) ? 4 : (objectId == 17009 || objectId == 17100 ? 2 : 3);
        if (rotation == 0) {
            /**
             * West/East
             */
            final WorldObject eastern = World.getObjectWithType(object.transform(offset, 0, 0), type);
            final WorldObject western = World.getObjectWithType(object.transform(-offset, 0, 0), type);
            if (eastern == null && western == null) {
                throw new RuntimeException("Unable to locate a paired gate for object: " + object);
            }
            final boolean west = player.getX() < object.getX();
            interact(player, west ? object : object.transform(-1, 0, 0), object, west ? western != null : eastern != null);
        } else if (rotation == 1) {
            /**
             * South/North
             */
            final WorldObject northern = World.getObjectWithType(object.transform(0, offset, 0), type);
            final WorldObject southern = World.getObjectWithType(object.transform(0, -offset, 0), type);
            if (northern == null && southern == null) {
                throw new RuntimeException("Unable to locate a paired gate for object: " + object);
            }
            final boolean north = player.getY() > object.getY();
            interact(player, north ? object : object.transform(0, 1, 0), object, north ? northern != null : southern != null);
        } else if (rotation == 2) {
            final WorldObject eastern = World.getObjectWithType(object.transform(offset, 0, 0), type);
            final WorldObject western = World.getObjectWithType(object.transform(-offset, 0, 0), type);
            if (eastern == null && western == null) {
                throw new RuntimeException("Unable to locate a paired gate for object: " + object);
            }
            final boolean west = player.getX() <= object.getX();
            interact(player, !west ? object : object.transform(1, 0, 0), object, west ? western != null : eastern != null);
        } else if (rotation == 3) {
            final WorldObject northern = World.getObjectWithType(object.transform(0, offset, 0), type);
            final WorldObject southern = World.getObjectWithType(object.transform(0, -offset, 0), type);
            if (northern == null && southern == null) {
                throw new RuntimeException("Unable to locate a paired gate for object: " + object);
            }
            final boolean north = player.getY() >= object.getY();
            interact(player, north ? object.transform(0, -1, 0) : object, object, north ? northern != null : southern != null);
        } else {
            throw new RuntimeException("Unknown rotation for SOS gate: " + object);
        }
    }

    /**
     * West/East
     */
    private static final void interact(final Player player, final Location tile, final WorldObject object, final boolean question) {
        if (question) {
            final boolean canPass = player.getBooleanAttribute("sos first claimed") && player.getBooleanAttribute("sos second claimed") && player.getBooleanAttribute("sos fourth claimed") && player.getBooleanAttribute("sos third claimed");
            if (canPass) {
                pass(player, tile);
                return;
            }
            final SOSGate.Gate gate = CollectionUtils.findMatching(Gate.VALUES, g -> ArrayUtils.contains(g.gateIds, object.getId()));
            if (gate == null) {
                throw new RuntimeException("Unable to locate gate for object " + object);
            }
            player.getDialogueManager().start(new GateDialogue(player, gate.npcId, StrongholdOfSecurity.getRandomQuestion(), tile));
            return;
        }
        pass(player, tile);
    }

    private static final class GateDialogue extends Dialogue {

        public GateDialogue(final Player player, final int npcId, final Question question, final Location tile) {
            super(player, npcId);
            this.question = question;
            this.tile = tile;
        }

        private final Question question;

        private final Location tile;

        @Override
        public void buildDialogue() {
            final String[] strings = question.getQuestion().getStrings();
            for (final String string : strings) {
                npc(string);
            }
            final StrongholdOfSecurity.AnswerMessage[] answers = question.getAnswers();
            final Dialogue.DialogueOption[] options = new DialogueOption[answers.length];
            for (int i = 0; i < answers.length; i++) {
                final StrongholdOfSecurity.AnswerMessage answer = answers[i];
                options[i] = new DialogueOption(answer.getOption(), key(5 + (i * 5)));
            }
            this.options(TITLE, options);
            for (int i = 0; i < answers.length; i++) {
                final StrongholdOfSecurity.AnswerMessage answer = answers[i];
                int index = 5 + (i * 5);
                final String[] msgs = answer.getMessage();
                final boolean success = msgs[0].startsWith("Correct!") || msgs[0].startsWith("Well done!");
                for (int x = 0; x < msgs.length; x++) {
                    final String message = msgs[x];
                    final NPCMessage msg = npc(index++, message);
                    if (success) {
                        if (x == (msgs.length - 1)) {
                            msg.executeAction(() -> {
                                finish();
                                pass(player, tile);
                            });
                        }
                    } else {
                        msg.executeAction(() -> {
                            finish();
                            player.getDialogueManager().start(new GateDialogue(player, npcId, StrongholdOfSecurity.getRandomQuestion(), tile));
                        });
                    }
                }
            }
        }
    }

    private static final void pass(final Player player, final Location tile) {
        player.lock();
        player.setAnimation(OPEN);
        player.getPacketDispatcher().sendSoundEffect(SOUND);
        player.setFaceLocation(tile);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(POST_OPEN);
            player.setLocation(tile);
            player.unlock();
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.RICKETY_DOOR, ObjectId.RICKETY_DOOR_17100, ObjectId.GATE_OF_WAR, ObjectId.GATE_OF_WAR_19207, ObjectId.OOZING_BARRIER, ObjectId.OOZING_BARRIER_23654, ObjectId.PORTAL_OF_DEATH, ObjectId.PORTAL_OF_DEATH_23728 };
    }
}

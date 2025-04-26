package com.zenyte.game.content.treasuretrails.npcs;

import com.zenyte.game.content.treasuretrails.challenges.EmoteRequest;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.util.WorldUtil;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Kris | 23/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class UriNPC extends TreasureGuardian {
    private static final List<String> quotes = Arrays.asList("What's cheese?", "Up in the north, I hear they keep milk in bags rather than buckets.", "Have you found the key to the secret room?", "My magic carpet is full of eels.", "Actions have consequences.", "It is possible to commit no mistakes and still lose.", "A great captain is always willing to change course.", "Once, I was a poor man, but then I found a party hat.", "There were three goblins in a bar, which one left first?", "Would you like to buy a pewter spoon?", "In the end, only the three-legged survive.", "I heard that the tall man fears only strong winds.", "In Canifis the men are known for eating much spam.", "I am the egg man, are you one of the egg men?", "The sudden appearance of a deaf squirrel is most puzzling, Comrade.", "I believe that it is very rainy in Varrock.", "The slowest of fishermen catch the swiftest of fish.", "It is quite easy being green.", "Don't forget to find the jade monkey.", "Brother, do you even lift?", "Do you want ants? Because that's how you get ants.", "I once named a duck after a girl. Big mistake, they all hated it.", "Loser says what.", "I'm looking for a girl named Molly. I can't seem to find her. any assistance?", "Fancy a holiday? I heard there was a whole other world to the west.", "Guys, let's lake dive!", "I gave you what you needed; not what you think you needed.", "Want to see me bend a spoon?", "Is that Deziree?", "This is the last night you'll spend alone.", "(Breathing intensifies)", "Init doe. Lyk, I hope yer reward iz goodd aye?", "I'm going to get married, to the night.", "I took a college course in bowling; still not any good.", "Tonight we dine................quite nicely actually.", "I don't like pineapple, it has that bone in it.", "Some say...", "I told him not to go near that fence, and what did he do? Sheesh....", "Connection lost. Please wait - attempting to reestablish.", "There's this guy I sit next to. Makes weird faces and sounds. Kind of an odd fellow.", "Mate, mate... I'm the best.", "Quickly! I've got a bee sticking out of my arm!", "The Ankou's are a lie.", "9 years my princess, forever my light.", "Took a hair dryer to a party in my handbag. Ah, so fabulous!!", "Oranges are the fruit of the vegetable.", "I quite fancy an onion.", "Ahhhhhh, leeches!", "Mother's parsnips tasted like onion.", "Can you stand me?");
    private static final SoundEffect sound = new SoundEffect(1930, 10);

    @NotNull
    public static final String randomQuote() {
        return Utils.getRandomCollectionElement(quotes);
    }

    public static final void spawnUri(@NotNull final Player player, @NotNull final EmoteRequest clue) {
        final Optional<Location> square = WorldUtil.findEmptySquare(player.getLocation(), 2, 1, Optional.of(tile -> !tile.matches(player.getLocation()) && !ProjectileUtils.isProjectileClipped(null, null, player.getLocation(), tile, true) && clue.getPolygon().contains(tile)));
        final Location tile = square.orElse(player.getLocation());
        final UriNPC uri = new UriNPC(player, tile, clue);
        uri.faceEntity(player);
        uri.spawn();
        if (clue.getEmotes().size() == 1) {
            uri.talkative = true;
        }
        World.sendSoundEffect(tile, sound);
    }

    private UriNPC(@NotNull final Player source, Location tile, final EmoteRequest clue) {
        super(source, tile, 1774);
        //super(1774, tile, Direction.SOUTH, 3);
        this.username = source.getUsername();
        this.clue = clue;
        this.radius = 0;
        this.randomWalkDelay = Integer.MAX_VALUE >> 1;
    }

    private final String username;
    private final EmoteRequest clue;
    private boolean talkative;

    @Override
    public NPC spawn() {
        World.getPlayer(username).ifPresent(player -> player.getTemporaryAttributes().put("spawned uri npc", UriNPC.this));
        World.sendGraphics(new Graphics(86, 0, 100), getLocation());
        return super.spawn();
    }

    @Override
    public void onFinish(final Entity source) {
        final Location tile = new Location(getLocation());
        World.sendGraphics(new Graphics(86, 0, 100), tile);
        World.sendSoundEffect(tile, sound);
        super.onFinish(source);
        World.getPlayer(username).ifPresent(player -> {
            player.getTemporaryAttributes().remove("double agent clue");
            final Object attr = player.getTemporaryAttributes().get("spawned uri npc");
            if (attr == UriNPC.this) {
                player.getTemporaryAttributes().remove("spawned uri npc");
            }
        });
    }

    public static final Optional<UriNPC> findUri(@NotNull final Player player) {
        final Object attr = player.getTemporaryAttributes().get("spawned uri npc");
        if (attr instanceof UriNPC) {
            final UriNPC uri = (UriNPC) attr;
            if (!uri.isFinished() && !uri.isDead()) {
                return Optional.of(uri);
            }
        }
        return Optional.empty();
    }

    public EmoteRequest getClue() {
        return clue;
    }

    public boolean isTalkative() {
        return talkative;
    }

    public void setTalkative(boolean talkative) {
        this.talkative = talkative;
    }
}

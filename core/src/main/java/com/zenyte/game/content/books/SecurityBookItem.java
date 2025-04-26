package com.zenyte.game.content.books;

import com.zenyte.game.content.Book;
import com.zenyte.game.content.ChapteredBook;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 15/04/2019 21:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SecurityBookItem extends ItemPlugin {

    private static final class SecurityBook extends ChapteredBook {

        SecurityBook(final Player player) {
            super(player);
        }

        @Override
        public String[] getChapters() {
            return new String[] {
                    "<col=000080>Password Tips",
                    "<col=000080>Recovery Questions",
                    "<col=000080>Other Security Tips",
                    "<col=000080>Stronghold of Security", };
        }

        @Override
        public String[] getContent() {
            return new String[] {
                    "<col=000080> Password Tips </col>  <br>  A good password should be easily remembered by yourself but not easily guessed by anyone else. <br> " +
                            " <br> " +
                            "Choose a password that has both letters and numbers in it for the best security but don't make it so hard that you'll forget it! Never write your " +
                            "password down or leave it in a text file on your computer, someone could find it easily! <br> " +
                            " <br> " +
                            "Never tell anyone your password in RuneScape, not even a Moderator of any kind.",

                    "<col=000080> Recovery Questions </col>  <br>  Ideally your recovery questions should be easy remembered by you but not guessable by anyone who may know you or " +
                            "given away in conversation. Choose things that do not change, like dates or names but don't choose obvious ones like your birthday and your sister or " +
                            "brother's name because lots of people will know that.",

                    "<col=000080> Other Security Tips </col>  <br>  Bear in mind that recovery questions will take 14 days to become active after you have applied for them to be " +
                            "changed. This is to protect your account from hijackers who may change them. <br> " +
                            " <br> " +
                            "Never give your password to ANYONE. This includes your friends, family and moderators in game. <br> " +
                            " <br> " +
                            "Never leave you account logged on if you are away from the computer, it only takes 5 seconds to steal your account!",

                    "<col=000080> Stronghold of Security </col>  <br>  Location: The Stronghold of Security, as we call it, is located under the village filled with Barbarians. It was " +
                            "found after they moved their mining operatios and a miner fell through. The Stronghold contains many challenges. Both for those who enjoy combat and " +
                            "those who enjoy challenges of the mind. This book will be very useful to you in your travels there. <br> " +
                            " <br> " +
                            "You can find the Stronghold of Security by looking for a hole in the Barbarian Village. Be sure to take your combat equipment though!",

            };
        }

        @Override
        public String getTitle() {
            return "Security Book";
        }

    }

    @Override
    public void handle() {
        bind("Read", (player, item, slotId) -> Book.openBook(new SecurityBook(player)));
    }

    @Override
    public int[] getItems() {
        return new int[] { 9003 };
    }

}

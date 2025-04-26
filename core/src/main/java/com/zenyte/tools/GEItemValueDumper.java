package com.zenyte.tools;

import com.zenyte.game.GameLoader;
import mgi.Indice;
import mgi.types.Definitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

/**
 * This pulls the current market prices for all GE items by using runescape.wiki
 *
 * @author John J. Woloszyk / Kryeus
 */
public class GEItemValueDumper {

    public static void main(String[] parameters) {
        GameLoader.load();
        Definitions.loadDefinitions(Definitions.lowPriorityDefinitions);


        for (int index = 0; index < CollectionUtils.getIndiceSize(Indice.ITEM_DEFINITIONS); index++) {
            ItemDefinitions def = ItemDefinitions.get(index);

            if (def == null) {
                continue;
            }

            String line = "";
            String itemName = def.getName().replace(" ", "_");

            try {
                URL url = new URL("https://oldschool.runescape.wiki/w/Exchange:" + itemName);
                url.openConnection();
                Scanner scanner = new Scanner(new InputStreamReader(url.openStream()));
                BufferedWriter writer = new BufferedWriter(new FileWriter("data/items/ge_prices_new.txt", true));

                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();

                    if (line.contains("<div><span class=\"gemw-price slide-down\" id=\"GEPrice\">")) {
                        line = line
                                .replace(
                                        "<div><span class=\"gemw-price slide-down\" id=\"GEPrice\">",
                                        "^");
                        line = line.replace("</span><span class=\"gemw-change slide-down-2\">", "|");
                        line = line.substring(line.indexOf("^") + 1);
                        line = line.substring(0, line.indexOf("|"));
                        line = line.replace(",", "");

                        if (Integer.valueOf(line) == null) {
                            continue;
                        }

                        int price = Integer.valueOf(line);

                        writer.write(index + ":" + price);

                        System.out.println(index + ":" + price);

                        writer.newLine();
                    } else if (line.contains("<div><span class=\"gemw-price slide-up\" id=\"GEPrice\">")) {
                        line = line
                                .replace(
                                        "<div><span class=\"gemw-price slide-up\" id=\"GEPrice\">",
                                        "^");
                        line = line.replace("</span><span class=\"gemw-change slide-up-2\">", "|");
                        line = line.substring(line.indexOf("^") + 1);
                        line = line.substring(0, line.indexOf("|"));
                        line = line.replace(",", "");

                        if (Integer.valueOf(line) == null) {
                            continue;
                        }

                        int price = Integer.valueOf(line);

                        writer.write(index + ":" + price);

                        System.out.println(index + ":" + price);

                        writer.newLine();
                    }

                }
                scanner.close();
                writer.close();

            } catch (IOException e) {

            }
        }

    }
}

package com.zenyte.game.content.boons;

import com.google.gson.Gson;
import com.zenyte.game.world.DefaultGson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

public class BoonDataGenerator {
    static Gson gsonInstance;

    static ArrayList<BoonModel> output = new ArrayList<>();
    static ArrayList<RemnantValueModel> output2 = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BoonLoader loader = new BoonLoader();
        gsonInstance = DefaultGson.getGson();
        loader.init();
        for(Boon boon: BoonLoader.boonTypes) {
            if (boon.isHidden())
                continue;
            output.add(new BoonModel(boon.name(), boon.description(), boon.price(), boon.item(), BoonWrapper.get(boon.getClass()).getId()));
        }
        File file;
        if((file = new File("./cache/data/dynamic/perk_data.json")).exists()) {
            file.delete();
        }
        Writer writer = new FileWriter("./cache/data/dynamic/perk_data.json");
        gsonInstance.toJson(output, writer);
        writer.close();

        RemnantValueManager.categorize();

        for(RemnantValue value: RemnantValueManager.values) {
            output2.add(new RemnantValueModel(value.getValue(), value.getIds()));
        }

        File file2;
        if((file2 = new File("./cache/data/dynamic/remnant_data.json")).exists()) {
            file2.delete();
        }
        Writer writer2 = new FileWriter("./cache/data/dynamic/remnant_data.json");
        gsonInstance.toJson(output2, writer2);
        writer2.close();

    }
}

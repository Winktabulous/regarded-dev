package mgi.custom.relaunch;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.zenyte.game.content.boons.BoonModel;
import com.zenyte.game.content.boons.RemnantValueModel;
import mgi.tools.parser.TypeParser;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import net.runelite.cache.util.ScriptVarType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class RemnantExchangeValuePacker {

    public final void packAll() {
        try {
            parseJson();
        } catch (FileNotFoundException e) {
            log.error("Failed to locate perk data");
            return;
        }
        generateEnums();
    }

    private void generateEnums() {
        EnumDefinitions ids = new EnumDefinitions(10057,  ScriptVarType.INTEGER,  ScriptVarType.INTEGER);
        ids.setDefaultInt(-1);
        EnumDefinitions values = new EnumDefinitions(10058, ScriptVarType.INTEGER,  ScriptVarType.INTEGER);
        values.setDefaultInt(-1);
        int index = 0;
        for(RemnantValueModel model: parsed) {
            for(int id: model.getIds()) {
                ids.getValues().put(index, id);
                values.getValues().put(index, model.getValue());
                index++;
            }
        }
        ids.pack();
        values.pack();
    }

    static Gson gsonInstance;
    public static final Logger log = LoggerFactory.getLogger(RemnantExchangeValuePacker.class);
    ArrayList<RemnantValueModel> parsed = new ArrayList<>();
    private void parseJson() throws FileNotFoundException {
        gsonInstance = TypeParser.getGson();
        RemnantValueModel[] parse = gsonInstance.fromJson(new JsonReader(new FileReader("./data/dynamic/remnant_data.json")), RemnantValueModel[].class);
        parsed.addAll(Arrays.stream(parse).sorted(Comparator.comparingInt(RemnantValueModel::getValue)).toList());
    }
}

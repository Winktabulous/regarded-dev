package mgi.custom.relaunch;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.zenyte.game.content.boons.BoonModel;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.tools.parser.TypeParser;
import mgi.types.component.ComponentDefinitions;
import mgi.types.config.ParamDefinitions;
import mgi.types.config.StructDefinitions;
import mgi.types.config.VarbitDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import net.runelite.cache.util.ScriptVarType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class PerkPacker {
    static Gson gsonInstance;
    public static final Logger log = LoggerFactory.getLogger(PerkPacker.class);
    public static ArrayList<StructDefinitions> createdStructs = new ArrayList<>();
    ArrayList<BoonModel> parsedBoons = new ArrayList<>();

    static final int START_STRUCT = 9500;

    static final int NAME_PARAM = 8500;
    static final int DESC_PARAM = 8501;
    static final int PRICE_PARAM = 8502;
    static final int ITEM_PARAM = 8503;
    static final int ENUM_ID = 9500;
    static final int VARBIT_BASE = 19500;
    static int boonCounter = 0;

    public final void packAll() {
        try {
            parseJson();
        } catch (FileNotFoundException e) {
            log.error("Failed to locate perk data");
            return;
        }
        generateParams();
        generateStructs();
        generateEnums();
        generateVarbits();
        copyVarbits();
        //addLoadHook();
    }

    private void addLoadHook() {
        ComponentDefinitions componentDefinitions = ComponentDefinitions.get(5004, 0);
        componentDefinitions.onLoadListener[0] = 34000;
        componentDefinitions.pack();
    }

    private void copyVarbits() {
        // Rune Pouch
        VarbitDefinitions copy1 = VarbitDefinitions.get(14285);
        copy1.setBaseVar(19494);
        copy1.setId(19494);
        copy1.pack();

        VarbitDefinitions copy2 = VarbitDefinitions.get(14286);
        copy2.setBaseVar(19495);
        copy2.setId(19495);
        copy2.pack();

        VarbitDefinitions copy3 = VarbitDefinitions.get(261);
        copy3.setBaseVar(19450);
        copy3.setId(19450);
        copy3.pack();

        VarbitDefinitions copy4 = VarbitDefinitions.get(262);
        copy4.setBaseVar(19451);
        copy4.setId(19451);
        copy4.pack();

        VarbitDefinitions copy5 = VarbitDefinitions.get(263);
        copy5.setBaseVar(19452);
        copy5.setId(19452);
        copy5.pack();

        VarbitDefinitions copy6 = VarbitDefinitions.get(264);
        copy6.setBaseVar(19453);
        copy6.setId(19453);
        copy6.pack();


    }

    private void generateVarbits() {
        for(int i = VARBIT_BASE; i <= VARBIT_BASE + boonCounter; i++) {
            VarbitDefinitions varbit = new VarbitDefinitions(i);
            varbit.setBaseVar(i);
            varbit.setStartBit(0);
            varbit.setEndBit(2);
            varbit.pack();
        }
    }

    private void parseJson() throws FileNotFoundException {
        gsonInstance = TypeParser.getGson();
        BoonModel[] boons = gsonInstance.fromJson(new JsonReader(new FileReader("./data/dynamic/perk_data.json")), BoonModel[].class);
        boonCounter = boons.length;
        parsedBoons.addAll(Arrays.stream(boons).sorted(Comparator.comparingInt(BoonModel::getSort)).toList());
    }

    private void generateParams() {
        ParamDefinitions name = new ParamDefinitions(NAME_PARAM, ScriptVarType.STRING);
        name.pack();

        ParamDefinitions description = new ParamDefinitions(DESC_PARAM, ScriptVarType.STRING);
        description.pack();

        ParamDefinitions price = new ParamDefinitions(PRICE_PARAM, ScriptVarType.INTEGER);
        price.setDefaultInt(999999);
        price.pack();

        ParamDefinitions itemDisplay = new ParamDefinitions(ITEM_PARAM, ScriptVarType.OBJ);
        itemDisplay.setDefaultInt(0);
        itemDisplay.pack();
    }

    private void generateStructs() {
        int start = START_STRUCT;
        for(BoonModel model: parsedBoons) {
            StructDefinitions struct = new StructDefinitions(start);
            struct.setParameters(generateStructParams(model));
            log.info("Packing struct [{}] with params [{}]", start, struct.printParams());
            struct.pack();
            createdStructs.add(struct);
            start++;
        }
    }

    private Int2ObjectMap<Object> generateStructParams(BoonModel model) {
        Int2ObjectMap<Object> mapper = new Int2ObjectOpenHashMap<>();
        mapper.put(NAME_PARAM, model.name);
        mapper.put(DESC_PARAM, model.description);
        mapper.put(PRICE_PARAM, (Object) model.price);
        mapper.put(ITEM_PARAM, (Object) model.itemId);
        return mapper;
    }

    private void generateEnums() {
        EnumDefinitions boons = new EnumDefinitions(ENUM_ID, ScriptVarType.INTEGER, ScriptVarType.STRUCT);
        int index = 0;
        for(StructDefinitions struct: createdStructs) {
            boons.getValues().put(index, struct.getId());
            index++;
        }

        log.info("Packing enum [{}] with params [{}]", ENUM_ID, boons.toString());
        boons.pack();
    }


}

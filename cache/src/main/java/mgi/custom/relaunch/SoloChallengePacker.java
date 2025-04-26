package mgi.custom.relaunch;

import com.zenyte.ContentConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SoloChallengePacker {
    public static final Logger log = LoggerFactory.getLogger(SoloChallengePacker.class);


    static final int START_CHALLENGE_STRUCT = 10410;
    static final int START_ENUM = 9600;
    private static final ArrayList<ChallengeModel> challenges = new ArrayList<>();

    static {
        // 10410
        challenges.add(new ChallengeModel(10021, 9999, "First player to max a realist account", 0, 1, 2178));
        // 10411
        challenges.add(new ChallengeModel(10022, 9998, "First player to max a non-group ironman account", 1, 1, 2178));
        // 10412
        challenges.add(new ChallengeModel(10022, 9998, "First player to max a ultimate ironman account",2, 1, 2178));
        // 10413
        challenges.add(new ChallengeModel(10021, 9998, "First player to max a Realist ironman account (5x XP) (HCIM counts)",3, 1, 2178));
        // 10414
        challenges.add(new ChallengeModel(10022, 9998, "First player to achieve the Achievement Cape (all achievements)",4, 1, 1));
        // 10415
        challenges.add(new ChallengeModel(10022, 9997, "First player to receive a Twisted Bow",5, 1, 1));
        // 10416
        challenges.add(new ChallengeModel(10022, 9996, "First player to receive an Olmlet", 6, 1, 1));
        // 10417
        challenges.add(new ChallengeModel(10022, 9996, "First player to receive a metamorphic dust", 9, 1, 1));
        // 10418
        challenges.add(new ChallengeModel(10022, 9996, "The player with the most CoX completions", 7, 1, 99999));
        // 10419
        challenges.add(new ChallengeModel(10022, 9997, "The player with the fastest solo CoX CM completion (in ticks)", 8, 1, 999999));
    }

    public void packAll() {
        generateEnums();
        generateStructs();
    }

    private void generateEnums() {
        EnumDefinitions ps5 = new EnumDefinitions();
        ps5.setId(9600);
        ps5.setDefaultInt(-1);
        ps5.setKeyType("int");
        ps5.setValueType("int");
        HashMap<Integer, Object> values = new LinkedHashMap<>();
        values.put(0, 10410);
        ps5.setValues(values);
        ps5.pack();

        EnumDefinitions fiveHundred = new EnumDefinitions();
        fiveHundred.setId(9601);
        fiveHundred.setDefaultInt(-1);
        fiveHundred.setKeyType("int");
        fiveHundred.setValueType("int");
        HashMap<Integer, Object> pvm_values = new LinkedHashMap<>();
        pvm_values.put(0, 10411);
        pvm_values.put(1, 10412);
        pvm_values.put(2, 10413);
        pvm_values.put(3, 10414);
        fiveHundred.setValues(pvm_values);
        fiveHundred.pack();

        EnumDefinitions weekly = new EnumDefinitions();
        weekly.setId(9602);
        weekly.setKeyType("int");
        weekly.setValueType("int");
        HashMap<Integer, Object> wild_values = new LinkedHashMap<>();
        wild_values.put(0, 10415);
        wild_values.put(1, 10416);
        wild_values.put(2, 10417);
        wild_values.put(3, 10418);
        wild_values.put(4, 10419);
        weekly.setValues(wild_values);
        weekly.pack();
    }

    private void generateStructs() {
        int start = START_CHALLENGE_STRUCT;
        StructDefinitions init_struct = new StructDefinitions(10400);
        init_struct.setParameters(generateInitial());
        log.info("Packing struct [{}] with params [{}]", 10400, init_struct.printParams());
        init_struct.pack();

        StructDefinitions skillingCategory = new StructDefinitions(10402);
        skillingCategory.setParameters(generateSkilling());
        skillingCategory.pack();

        StructDefinitions pvmCategory = new StructDefinitions(10403);
        pvmCategory.setParameters(generatePVM());
        pvmCategory.pack();

        StructDefinitions wildyCategory = new StructDefinitions(10404);
        wildyCategory.setParameters(generateWildy());
        wildyCategory.pack();

        for(ChallengeModel model: challenges) {
            StructDefinitions struct = new StructDefinitions(start);
            struct.setParameters(generateStructParams(model));
            log.info("Packing struct [{}] with params [{}]", start, struct.printParams());
            struct.pack();
            start++;
        }
    }

    private Int2ObjectMap<Object> generateSkilling() {
        Int2ObjectMap<Object> mapper = new Int2ObjectOpenHashMap<>();
        mapper.put(5007, "PS5 Contest");
        mapper.put(5006, (Object) 9600);
        return mapper;
    }

    private Int2ObjectMap<Object> generatePVM() {
        Int2ObjectMap<Object> mapper = new Int2ObjectOpenHashMap<>();
        mapper.put(5007, "$500 Contests");
        mapper.put(5006, (Object) 9601);
        return mapper;
    }

    private Int2ObjectMap<Object> generateWildy() {
        Int2ObjectMap<Object> mapper = new Int2ObjectOpenHashMap<>();
        mapper.put(5007, "Weekly");
        mapper.put(5006, (Object) 9602);
        return mapper;
    }

    private Int2ObjectMap<Object> generateStructParams(ChallengeModel model) {
        Int2ObjectMap<Object> mapper = new Int2ObjectOpenHashMap<>();
        mapper.put(5012, (Object) model.category_id);
        mapper.put(5015, (Object) model.param_5015);
        mapper.put(5009, model.display);
        mapper.put(5014, (Object) model.sort);
        mapper.put(5016, (Object) model.param_5016);
        mapper.put(5011, (Object) model.param_5011);
        return mapper;
    }

    private Int2ObjectMap<Object> generateInitial() {
        Int2ObjectMap<Object> mapper = new Int2ObjectOpenHashMap<>();
        mapper.put(5005, (Object) 10404);
        mapper.put(5008, ContentConstants.SERVER_NAME + " Solo Challenges");
        mapper.put(5003, (Object) 10402);
        mapper.put(5004, (Object) 10403);
        return mapper;
    }
}

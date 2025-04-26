package mgi.types.config.db;

import net.runelite.cache.util.ScriptVarType;

public class ColumnValue {
    public ScriptVarType type;
    public int defaultInt = -1;
    public String defaultString = "";

    public ColumnValue(ScriptVarType type, int defaultInt) {
        this.type = type;
        this.defaultInt = defaultInt;
    }

    public ColumnValue(ScriptVarType type, String defaultString) {
        this.type = type;
        this.defaultString = defaultString;
    }


}

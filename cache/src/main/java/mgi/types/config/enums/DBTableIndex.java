package mgi.types.config.enums;

import net.runelite.cache.util.BaseVarType;

import java.util.List;
import java.util.Map;

public class DBTableIndex
{
    private final int tableId;
    private final int columnId;
    private BaseVarType[] tupleTypes;
    private List<Map<Object, List<Integer>>> tupleIndexes;


    public DBTableIndex(int table, int column) {
        this.tableId = table;
        this.columnId = column;
    }
}
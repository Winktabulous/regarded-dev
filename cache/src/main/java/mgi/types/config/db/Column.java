package mgi.types.config.db;

import java.util.LinkedHashMap;

public class Column {
    public int columnId;
    public LinkedHashMap<Integer, ColumnValue> values;
    public Column(int id) {
        this.columnId = id;
        values = new LinkedHashMap<>();
    }

    public void addValue(int valueIndex, ColumnValue value) {
        values.put(valueIndex, value);
    }

    public ColumnValue getForIdx(int idx) {
        return values.get(idx);
    }

    public Integer getId() {
        return this.columnId;
    }

    public Column withValue(ColumnValue value) {
        values.put(0, value);
        return this;
    }
}

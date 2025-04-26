package mgi.types.config;

import com.zenyte.CacheManager;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.types.config.db.Column;
import mgi.types.config.db.ColumnValue;
import mgi.utilities.ByteBuffer;
import net.runelite.cache.util.ScriptVarType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.near_reality.cache.DBTableIndexDefinitionKt.readProtobufVarInt;

@SuppressWarnings("unused")
public class DBRowDefinition implements Definitions, Cloneable {
	private static final Logger log = LoggerFactory.getLogger(DBRowDefinition.class);
	public static DBRowDefinition[] definitions;
	public static LinkedHashMap<Integer, Integer> rowCounts = new LinkedHashMap<>();
	public static LinkedHashMap<Integer, List<DBRowDefinition>> tableRows = new LinkedHashMap<>();

	public final LinkedHashMap<Integer, Integer> typeCountMap = new LinkedHashMap<>();
	public final LinkedHashMap<Integer, Integer> fieldCountMap = new LinkedHashMap<>();
	public final LinkedHashMap<Integer, Boolean> defaultValueMap = new LinkedHashMap<>();
	public final LinkedHashMap<Integer, Column> columns = new LinkedHashMap<>();
	public int columnCount = 0;





	public static int getRowCount(int tableId) {
		return rowCounts.getOrDefault(tableId, 0);
	}

	public static DBRowDefinition get(final int id) {
		if (id < 0 || id >= definitions.length) throw new IllegalArgumentException();
		return definitions[id];
	}

	private DBRowDefinition(final int id, final ByteBuffer buffer) {
		this.id = id;
		decode(buffer);
	}

	private int id;
	/**
	 * This is a custom field used for tracking the 0-up index of the
	 * current DBRowDefinition throughout the course of the object's
	 * lifecycle.
	 */
	private int index;
	public int tableId;
	public ScriptVarType[][] types;

	@Override
	public void load() {
		final Cache cache = CacheManager.getCache();
		final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
		final Group dbrow = configs.findGroupByID(GroupType.DBROW);
		definitions = new DBRowDefinition[dbrow.getHighestFileId()];
		for (int id = 0; id < dbrow.getHighestFileId(); id++) {
			final File file = dbrow.findFileByID(id);
			if (file == null) {
				continue;
			}
			final ByteBuffer buffer = file.getData();
			if (buffer == null) {
				continue;
			}
			if (buffer.remaining() < 1) {
				continue;
			}
			definitions[id] = new DBRowDefinition(id, buffer);
		}
	}

	public DBRowDefinition clone() throws CloneNotSupportedException {
		return (DBRowDefinition) super.clone();
	}

	@Override
	public void decode(final ByteBuffer buffer) {
		while (true) {
			final int opcode = buffer.readUnsignedByte();
			if (opcode == 0) {
				if(tableId != -1) {
					insertRow(this);
				}
				return;
			}
			decode(buffer, opcode);
		}
	}

	private void insertRow(DBRowDefinition def) {
		List<DBRowDefinition> rows = tableRows.getOrDefault(def.tableId, new ArrayList<>());
		def.index = rows.size();
		rows.add(def);
		tableRows.put(def.tableId, rows);
	}

	@Override
	public void decode(final ByteBuffer buffer, int opcode) {
		switch (opcode) {
			case 3:
				columnCount = buffer.readUnsignedByte();
				types = new ScriptVarType[columnCount][];
				Object[][] columnValues = new Object[columnCount][];

				while (true) {
					int columnId = buffer.readUnsignedByte();
					if(columnId == 255)
						break;
					int numTypes = buffer.readUnsignedByte();


					typeCountMap.put(columnId, numTypes);
					ScriptVarType[] scriptVarTypes = new ScriptVarType[numTypes];

					for (int i = 0; i < scriptVarTypes.length; ++i) {
						scriptVarTypes[i] = ScriptVarType.forId(buffer.readUnsignedSmart());
					}

					types[columnId] = scriptVarTypes;
					int fieldCount = buffer.readUnsignedSmart();
					fieldCountMap.put(columnId, fieldCount);
					Column column = new Column(columnId);
					decodeColumnFields(column, buffer);
					columns.put(columnId, column);
				}
				break;
			case 4:
				this.tableId = readProtobufVarInt(buffer);
				rowCounts.put(this.tableId, rowCounts.getOrDefault(this.tableId, 0) + 1);
				break;
			default:
				throw new IllegalStateException("Opcode: " + opcode);
		}
	}

	@Override
	public ByteBuffer encode() {
		final ByteBuffer buffer = new ByteBuffer(1132);
		buffer.writeUnsignedByte(3);
		buffer.writeUnsignedByte(columnCount);
		for(Map.Entry<Integer, Column> column: columns.entrySet()) {
			int columnId = column.getKey();
			buffer.writeUnsignedByte(columnId);
			buffer.writeUnsignedByte(typeCountMap.get(columnId));

			ScriptVarType[] varTypes = types[columnId];
			for(int typeIdx = 0; typeIdx < varTypes.length; typeIdx++) {
				buffer.writeSmart(varTypes[typeIdx].getId());
			}
			buffer.writeSmart(fieldCountMap.get(columnId));

			encodeColumnDefaults(column.getValue(), buffer);
		}

		buffer.writeUnsignedByte(255);
		if(tableId != -1) {
			buffer.writeProtobufVarIntWithOpcode(4, tableId);
		}
		buffer.writeUnsignedByte(0);
		return buffer;
	}


	private void decodeColumnFields(Column column, ByteBuffer stream) {
		int fieldCount = fieldCountMap.getOrDefault(column.getId(), 0);
		ScriptVarType[] scriptTypes = types[column.getId()];
		int typesCount = scriptTypes.length;

		for(int fieldIndex = 0; fieldIndex < fieldCount; ++fieldIndex) {
			for(int typeIndex = 0; typeIndex < typesCount; ++typeIndex) {
				ScriptVarType type = scriptTypes[typeIndex];
				int valuesIndex = fieldIndex * typesCount + typeIndex;
				if (type == ScriptVarType.STRING) {
					column.addValue(valuesIndex, new ColumnValue(type, stream.readString()));
				} else {
					column.addValue(valuesIndex, new ColumnValue(type, stream.readInt()));
				}
			}
		}
	}

	private void encodeColumnDefaults(Column column, ByteBuffer stream) {
		int fieldCount = fieldCountMap.getOrDefault(column.getId(), 0);
		ScriptVarType[] scriptTypes = types[column.getId()];
		int typesCount = scriptTypes.length;

		for(int fieldIndex = 0; fieldIndex < fieldCount; ++fieldIndex) {
			for(int typeIndex = 0; typeIndex < typesCount; ++typeIndex) {
				ScriptVarType type = scriptTypes[typeIndex];
				int valuesIndex = fieldIndex * typesCount + typeIndex;
				if (type == ScriptVarType.STRING) {
					ColumnValue value = column.getForIdx(valuesIndex);
					if(value.type != type)
						throw new IllegalStateException();
					stream.writeString(value.defaultString);
				} else {
					ColumnValue value = column.getForIdx(valuesIndex);
					if(value.type != type)
						throw new IllegalStateException();
					stream.writeInt(value.defaultInt);
				}
			}
		}
	}

	public void setNumColumns(int numColumns) {
		this.columnCount = numColumns;
	}




	@Override
	public void pack() {
		if(id == 0) {
			File file = new File(encode());
			CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.DBROW).addFile(file);
			this.id = file.getID();
		}
		else
			CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.DBROW).addFile(new File(id, encode()));
	}

	public DBRowDefinition(int id) {
		this.id = id;
	}

	public DBRowDefinition() {
		this.id = 0;
	}

	public int getId() {
		return id;
	}

	public int getTableId() {
		return tableId;
	}

	public Object getValueFromRow(int columnIndex) {
		return getValueFromRow(columnIndex, 0);
	}

	public Object getValueFromRow(int columnIndex, int valueIndex) {
		ColumnValue value = columns.get(columnIndex).getForIdx(valueIndex);
		return value.type == ScriptVarType.STRING ? value.defaultString : value.defaultInt;
	}

    public static boolean findItemOnTable(int tableId, int itemId) {
		for(DBRowDefinition def: tableRows.get(tableId)) {
			if(def.getValueFromRow(2, 0) instanceof Integer && (Integer) def.getValueFromRow(2, 0) == itemId)
				return true;
		}
        return false;
    }

	public static int getRowIndex(int tableId, int itemId) {
		for(DBRowDefinition def: tableRows.get(tableId)) {
			if(def.getValueFromRow(2, 0) instanceof Integer && (Integer) def.getValueFromRow(2, 0) == itemId)
				return def.index;
		}
		return 0;
	}

	public static int getColumnValueAsInt(int tableId, int itemId, int columnIndex) {
		for(DBRowDefinition def: tableRows.get(tableId)) {
			if(def.getValueFromRow(columnIndex, 0) instanceof Integer && def.getValueFromRow(2) instanceof Integer && (Integer) def.getValueFromRow(2) == itemId)
				return (int) def.getValueFromRow(columnIndex);
		}
		return 0;
	}

	public static int getRowColumnByIndexesInt(int table, int rowIndex, int columnIndex) {
		log.debug("Lookup table:{}, row:{}, column:{}, values:{},{},{},{},{}",
				table,
				rowIndex,
				columnIndex,
				tableRows.get(table).get(rowIndex).getValueFromRow(0),
				tableRows.get(table).get(rowIndex).getValueFromRow(1),
				tableRows.get(table).get(rowIndex).getValueFromRow(2),
				tableRows.get(table).get(rowIndex).getValueFromRow(3),
				tableRows.get(table).get(rowIndex).getValueFromRow(4)
		);
		return (int) tableRows.get(table).get(rowIndex).getValueFromRow(columnIndex);
	}
}

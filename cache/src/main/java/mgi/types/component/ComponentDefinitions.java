package mgi.types.component;

import com.zenyte.CacheManager;
import com.zenyte.game.util.AccessMask;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;
import mgi.utilities.StringFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Kris | 12. dets 2017 : 5:46.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 * profile</a>}
 */
public class ComponentDefinitions implements Definitions, Cloneable {
	private static final Logger log = LoggerFactory.getLogger(ComponentDefinitions.class);
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;
	public static final int FONT_SMALL = 494;
	public static final int FONT_REGULAR = 495;
	public static final int FONT_BOLD = 496;
	public static final int FONT_LARGE_STYLE = 497;
	public static final Map<Integer, ComponentDefinitions> definitions = new HashMap<>(5000);
	private static final ComponentDefinitions EXAMPLE = new ComponentDefinitions();
	private static final Object DEFAULT = new Object();
	public String name;
	public int interfaceId;
	public int componentId;
	public boolean isIf3;
	public int type;
	public int contentType;
	public int xMode;
	public int yMode;
	public int widthMode;
	public int heightMode;
	public int x;
	public int y;
	public int width;
	public int height;
	public int parentId;
	public boolean hidden;
	public int scrollWidth;
	public int scrollHeight;
	public int color;
	public boolean filled;
	public int opacity;
	public int lineWidth;
	public int spriteId;
	public int textureId;
	public boolean spriteTiling;
	public int borderType;
	public int shadowColor;
	public boolean flippedVertically;
	public boolean flippedHorizontally;
	public int modelType;
	public int modelId;
	public int offsetX2d;
	public int offsetY2d;
	public int rotationX;
	public int rotationZ;
	public int rotationY;
	public int modelZoom;
	public int font;
	public String text;
	public String alternateText;
	public boolean textShadowed;
	public int xPitch;
	public int yPitch;
	int[] xOffsets;
	public String[] configActions;
	public int accessMask;
	public String opBase;
	public String[] actions;
	public int dragDeadZone;
	public int dragDeadTime;
	public boolean dragRenderBehavior;
	public String targetVerb;
	public Object[] onLoadListener;
	public Object[] onClickListener;
	public Object[] onClickRepeatListener;
	public Object[] onReleaseListener;
	public Object[] onHoldListener;
	public Object[] onMouseOverListener;
	public Object[] onMouseRepeatListener;
	public Object[] onMouseLeaveListener;
	public Object[] onDragListener;
	public Object[] onDragCompleteListener;
	public Object[] onTargetEnterListener;
	public Object[] onTargetLeaveListener;
	public Object[] onVarTransmitListener;
	int[] varTransmitTriggers;
	public Object[] onInvTransmitListener;
	int[] invTransmitTriggers;
	public Object[] onStatTransmitListener;
	int[] statTransmitTriggers;
	public Object[] onTimerListener;
	public Object[] onOpListener;
	public Object[] onScrollWheelListener;
	int[][] dynamicValues;
	int[] valueCompareType;
	public String spellName;
	public String tooltip;
	int[] itemIds;
	int[] itemQuantities;
	public List<ComponentDefinitions> children;
	public boolean noClickThrough;
	public int menuType;
	public int alternateTextColor;
	public int hoveredTextColor;
	public int alternateHoveredTextColor;
	public boolean lineDirection;
	public int alternateSpriteId;
	public int field2840;
	public int alternateModelId;
	public int animation;
	public int alternateAnimation;
	public int modelHeightOverride;
	public boolean orthogonal;
	public int lineHeight;
	public int xAllignment;
	public int yAllignment;
	int[] yOffsets;
	int[] sprites;
	int[] requiredValues;
	public int hoveredSiblingId;
	private Map<String, Object[]> hooks;

	public ComponentDefinitions() {
		setDefaults();
	}

	public ComponentDefinitions(final int id, final ByteBuffer buffer) {
		interfaceId = id >> 16;
		componentId = id & 65535;
		setDefaults();
		final byte[] data = buffer.getBuffer();
		if (data != null && data.length > 0) {
			isIf3 = data[0] == -1;
			if (isIf3) {
				decodeIf3(buffer);
			} else {
				decode(buffer);
			}
		}
	}

	public ComponentDefinitions clone() throws CloneNotSupportedException {
		return (ComponentDefinitions) super.clone();
	}

	private static final Object getValue(final Object object, final Field field) throws Throwable {
		field.setAccessible(true);
		final Class<?> type = field.getType();
		if (field.get(object) == null) {
			return DEFAULT;
		}
		if (type == int[][].class) {
			return Arrays.toString((int[][]) field.get(object));
		} else if (type == int[].class) {
			return Arrays.toString((int[]) field.get(object));
		} else if (type == byte[].class) {
			return Arrays.toString((byte[]) field.get(object));
		} else if (type == short[].class) {
			return Arrays.toString((short[]) field.get(object));
		} else if (type == double[].class) {
			return Arrays.toString((double[]) field.get(object));
		} else if (type == float[].class) {
			return Arrays.toString((float[]) field.get(object));
		} else if (type == String[].class) {
			if (field.get(object) == null) {
				return "null";
			}
			return "[" + String.join(", ", (String[]) field.get(object)) + "]";
		} else if (type == Object[].class) {
			return Arrays.toString((Object[]) field.get(object));
		}
		return field.get(object);
	}

	public static void add(final ComponentDefinitions component) {
		final int bitpacked = component.getInterfaceId() << 16 | component.getComponentId();
		definitions.put(bitpacked, component);
	}

	public static final ComponentDefinitions get(final int id, final int componentId) {
		return definitions.get(id << 16 | componentId);
	}

	public static final List<ComponentDefinitions> getComponents(final int id) {
		final ArrayList<ComponentDefinitions> components = new ArrayList<ComponentDefinitions>();
		for (final Map.Entry<Integer, ComponentDefinitions> entry : definitions.entrySet()) {
			final Integer bitpacked = entry.getKey();
			if (bitpacked >> 16 == id) {
				components.add(entry.getValue());
			}
		}
		return components;
	}

	@Override
	public void load() {
		final Cache cache = CacheManager.getCache();
		final Archive interfaces = cache.getArchive(ArchiveType.INTERFACES);
		for (int interfaceId = 0; interfaceId < interfaces.getHighestGroupId(); interfaceId++) {
			final Group interfaceGroup = interfaces.findGroupByID(interfaceId);
			if (interfaceGroup == null) {
				continue;
			}
			for (int componentId = 0; componentId < interfaceGroup.getHighestFileId(); componentId++) {
				final File file = interfaceGroup.findFileByID(componentId);
				if (file == null) {
					continue;
				}
				final ByteBuffer buffer = file.getData();
				if (buffer == null) {
					continue;
				}
				buffer.setPosition(0);
				final int id = interfaceId << 16 | componentId;
				definitions.put(id, new ComponentDefinitions(id, buffer));
			}
		}
	}

	public static void reload() {
		final Cache cache = CacheManager.getCache();
		final Archive interfaces = cache.getArchive(ArchiveType.INTERFACES);
		for (int interfaceId = 0; interfaceId < interfaces.getHighestGroupId(); interfaceId++) {
			final Group interfaceGroup = interfaces.findGroupByID(interfaceId);
			if (interfaceGroup == null) {
				continue;
			}
			for (int componentId = 0; componentId < interfaceGroup.getHighestFileId(); componentId++) {
				final File file = interfaceGroup.findFileByID(componentId);
				if (file == null) {
					continue;
				}
				final ByteBuffer buffer = file.getData();
				if (buffer == null) {
					continue;
				}
				buffer.setPosition(0);
				final int id = interfaceId << 16 | componentId;
				definitions.put(id, new ComponentDefinitions(id, buffer));
			}
		}
	}

	public void printFields() {
		for (final Field field : getClass().getDeclaredFields()) {
			if ((field.getModifiers() & 8) != 0) {
				continue;
			}
			try {
				final Object val = getValue(this, field);
				final Object defaultVal = getValue(EXAMPLE, field);
				if (val == defaultVal || val.equals(defaultVal)) {
					continue;
				}
				if (val == DEFAULT) {
					continue;
				}
				final String[] fieldName = field.getName().split("(?=[A-Z])");
				final StringBuilder fieldBuilder = new StringBuilder();
				fieldBuilder.append(StringFormatUtil.formatString(fieldName[0]));
				for (int i = 1; i < fieldName.length; i++) {
					fieldBuilder.append(" " + (fieldName[i].length() == 1 ? fieldName[i].toUpperCase() : fieldName[i].toLowerCase()));
				}
				//strings.enqueue(fieldBuilder.toString() + ": " + val);
				System.out.println(fieldBuilder + ": " + val);
			} catch (final Throwable e) {
				log.error("", e);
			}
		}
		System.out.println("--------------------------END OF COMPONENT------------------------");
	}

	private void setDefaults() {
		isIf3 = false;
		menuType = 0;
		contentType = 0;
		xMode = 0;
		yMode = 0;
		widthMode = 0;
		heightMode = 0;
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		parentId = -1;
		hidden = false;
		scrollWidth = 0;
		scrollHeight = 0;
		color = 0;
		alternateTextColor = 0;
		hoveredTextColor = 0;
		alternateHoveredTextColor = 0;
		filled = false;
		opacity = 0;
		lineWidth = 1;
		lineDirection = false;
		spriteId = -1;
		alternateSpriteId = -1;
		textureId = 0;
		spriteTiling = false;
		borderType = 0;
		shadowColor = 0;
		modelType = 1;
		modelId = -1;
		field2840 = 1;
		alternateModelId = -1;
		animation = -1;
		alternateAnimation = -1;
		offsetX2d = 0;
		offsetY2d = 0;
		rotationX = 0;
		rotationZ = 0;
		rotationY = 0;
		modelZoom = 100;
		modelHeightOverride = 0;
		orthogonal = false;
		font = -1;
		text = "";
		alternateText = "";
		lineHeight = 0;
		xAllignment = 0;
		yAllignment = 0;
		textShadowed = false;
		xPitch = 0;
		yPitch = 0;
		accessMask = 0;
		opBase = "";
		dragDeadZone = 0;
		dragDeadTime = 0;
		dragRenderBehavior = false;
		targetVerb = "";
		hoveredSiblingId = -1;
		spellName = "";
		tooltip = "Ok";
		noClickThrough = false;
		children = new ArrayList<>();
	}

	@Override
	public void decode(final ByteBuffer buffer) {
		type = buffer.readUnsignedByte();
		menuType = buffer.readUnsignedByte();
		contentType = buffer.readUnsignedShort();
		x = buffer.readShort();
		y = buffer.readShort();
		width = buffer.readUnsignedShort();
		height = buffer.readUnsignedShort();
		opacity = buffer.readUnsignedByte();
		parentId = buffer.readUnsignedShort();
		if (parentId == 65535) {
			parentId = -1;
		} else {
			parentId += -1 & -65536;
		}
		hoveredSiblingId = buffer.readUnsignedShort();
		if (hoveredSiblingId == 65535) {
			hoveredSiblingId = -1;
		}
		final int var2 = buffer.readUnsignedByte();
		int var3;
		if (var2 > 0) {
			valueCompareType = new int[var2];
			requiredValues = new int[var2];
			for (var3 = 0; var3 < var2; ++var3) {
				valueCompareType[var3] = buffer.readUnsignedByte();
				requiredValues[var3] = buffer.readUnsignedShort();
			}
		}
		var3 = buffer.readUnsignedByte();
		int var4;
		int var5;
		int var6;
		if (var3 > 0) {
			dynamicValues = new int[var3][];
			for (var4 = 0; var4 < var3; ++var4) {
				var5 = buffer.readUnsignedShort();
				dynamicValues[var4] = new int[var5];
				for (var6 = 0; var6 < var5; ++var6) {
					dynamicValues[var4][var6] = buffer.readUnsignedShort();
					if (dynamicValues[var4][var6] == 65535) {
						dynamicValues[var4][var6] = -1;
					}
				}
			}
		}
		if (type == 0) {
			scrollHeight = buffer.readUnsignedShort();
			hidden = buffer.readUnsignedByte() == 1;
		}
		if (type == 1) {
			buffer.readShort();
			buffer.readByte();
		}
		if (type == 2) {
			itemIds = new int[width * height];
			itemQuantities = new int[height * width];
			var4 = buffer.readUnsignedByte();
			if (var4 == 1) {
				accessMask |= 268435456;
			}
			var5 = buffer.readUnsignedByte();
			if (var5 == 1) {
				accessMask |= 1073741824;
			}
			var6 = buffer.readUnsignedByte();
			if (var6 == 1) {
				accessMask |= Integer.MIN_VALUE;
			}
			final int var7 = buffer.readUnsignedByte();
			if (var7 == 1) {
				accessMask |= 536870912;
			}
			xPitch = buffer.readUnsignedByte();
			yPitch = buffer.readUnsignedByte();
			xOffsets = new int[20];
			yOffsets = new int[20];
			sprites = new int[20];
			int var8;
			for (var8 = 0; var8 < 20; ++var8) {
				final int var9 = buffer.readUnsignedByte();
				if (var9 == 1) {
					xOffsets[var8] = buffer.readShort();
					yOffsets[var8] = buffer.readShort();
					sprites[var8] = buffer.readInt();
				} else {
					sprites[var8] = -1;
				}
			}
			configActions = new String[5];
			for (var8 = 0; var8 < 5; ++var8) {
				final String var11 = buffer.readString();
				if (var11.length() > 0) {
					configActions[var8] = var11;
					accessMask |= 1 << var8 + 23;
				}
			}
		}
		if (type == 3) {
			filled = buffer.readUnsignedByte() == 1;
		}
		if (type == 4 || type == 1) {
			xAllignment = buffer.readUnsignedByte();
			yAllignment = buffer.readUnsignedByte();
			lineHeight = buffer.readUnsignedByte();
			font = buffer.readUnsignedShort();
			if (font == 65535) {
				font = -1;
			}
			textShadowed = buffer.readUnsignedByte() == 1;
		}
		if (type == 4) {
			text = buffer.readString();
			alternateText = buffer.readString();
		}
		if (type == 1 || type == 3 || type == 4) {
			color = buffer.readInt();
		}
		if (type == 3 || type == 4) {
			alternateTextColor = buffer.readInt();
			hoveredTextColor = buffer.readInt();
			alternateHoveredTextColor = buffer.readInt();
		}
		if (type == 5) {
			spriteId = buffer.readInt();
			alternateSpriteId = buffer.readInt();
		}
		if (type == 6) {
			modelType = 1;
			modelId = buffer.readUnsignedShort();
			if (modelId == 65535) {
				modelId = -1;
			}
			field2840 = 1;
			alternateModelId = buffer.readUnsignedShort();
			if (alternateModelId == 65535) {
				alternateModelId = -1;
			}
			animation = buffer.readUnsignedShort();
			if (animation == 65535) {
				animation = -1;
			}
			alternateAnimation = buffer.readUnsignedShort();
			if (alternateAnimation == 65535) {
				alternateAnimation = -1;
			}
			modelZoom = buffer.readUnsignedShort();
			rotationX = buffer.readUnsignedShort();
			rotationY = buffer.readUnsignedShort();
		}
		if (type == 7) {
			itemIds = new int[height * width];
			itemQuantities = new int[width * height];
			xAllignment = buffer.readUnsignedByte();
			font = buffer.readUnsignedShort();
			if (font == 65535) {
				font = -1;
			}
			textShadowed = buffer.readUnsignedByte() == 1;
			color = buffer.readInt();
			xPitch = buffer.readShort();
			yPitch = buffer.readShort();
			var4 = buffer.readUnsignedByte();
			if (var4 == 1) {
				accessMask |= 1073741824;
			}
			configActions = new String[5];
			for (var5 = 0; var5 < 5; ++var5) {
				final String var10 = buffer.readString();
				if (var10.length() > 0) {
					configActions[var5] = var10;
					accessMask |= 1 << var5 + 23;
				}
			}
		}
		if (type == 8) {
			text = buffer.readString();
		}
		if (menuType == 2 || type == 2) {
			targetVerb = buffer.readString();
			spellName = buffer.readString();
			var4 = buffer.readUnsignedShort() & 63;
			accessMask |= var4 << 11;
		}
		if (menuType == 1 || menuType == 4 || menuType == 5 || menuType == 6) {
			tooltip = buffer.readString();
			if (tooltip.length() == 0) {
				if (menuType == 1) {
					tooltip = "Ok";
				}
				if (menuType == 4) {
					tooltip = "Select";
				}
				if (menuType == 5) {
					tooltip = "Select";
				}
				if (menuType == 6) {
					tooltip = "Continue";
				}
			}
		}
		if (menuType == 1 || menuType == 4 || menuType == 5) {
			accessMask |= 4194304;
		}
		if (menuType == 6) {
			accessMask |= 1;
		}
	}

	@Override
	public void decode(final ByteBuffer buffer, final int opcode) {
	}

	public void decodeIf3(final ByteBuffer buffer) {
		buffer.readByte();
		type = buffer.readUnsignedByte();
		contentType = buffer.readUnsignedShort();
		x = buffer.readShort();
		y = buffer.readShort();
		width = buffer.readUnsignedShort();
		if (type == 9) {
			height = buffer.readShort();
		} else {
			height = buffer.readUnsignedShort();
		}
		widthMode = buffer.readByte();
		heightMode = buffer.readByte();
		xMode = buffer.readByte();
		yMode = buffer.readByte();
		parentId = buffer.readUnsignedShort();
		if (parentId == 65535) {
			parentId = -1;
		} else {
			parentId += -1 & -65536;
		}
		hidden = buffer.readUnsignedByte() == 1;
		if (type == 0) {
			scrollWidth = buffer.readUnsignedShort();
			scrollHeight = buffer.readUnsignedShort();
			noClickThrough = buffer.readUnsignedByte() == 1;
		}
		if (type == 5) {
			spriteId = buffer.readInt();
			textureId = buffer.readUnsignedShort();
			spriteTiling = buffer.readUnsignedByte() == 1;
			opacity = buffer.readUnsignedByte();
			borderType = buffer.readUnsignedByte();
			shadowColor = buffer.readInt();
			flippedVertically = buffer.readUnsignedByte() == 1;
			flippedHorizontally = buffer.readUnsignedByte() == 1;
		}
		if (type == 6) {
			modelType = 1;
			modelId = buffer.readUnsignedShort();
			if (modelId == 65535) {
				modelId = -1;
			}
			offsetX2d = buffer.readShort();
			offsetY2d = buffer.readShort();
			rotationX = buffer.readUnsignedShort();
			rotationY = buffer.readUnsignedShort();
			rotationZ = buffer.readUnsignedShort();
			modelZoom = buffer.readUnsignedShort();
			animation = buffer.readUnsignedShort();
			if (animation == 65535) {
				animation = -1;
			}
			orthogonal = buffer.readUnsignedByte() == 1;
			buffer.readShort();
			if (widthMode != 0) {
				modelHeightOverride = buffer.readUnsignedShort();
			}
			if (heightMode != 0) {
				buffer.readShort();
			}
		}
		if (type == 4) {
			font = buffer.readUnsignedShort();
			if (font == 65535) {
				font = -1;
			}
			text = buffer.readString();
			lineHeight = buffer.readUnsignedByte();
			xAllignment = buffer.readUnsignedByte();
			yAllignment = buffer.readUnsignedByte();
			textShadowed = buffer.readUnsignedByte() == 1;
			color = buffer.readInt();
		}
		if (type == 3) {
			color = buffer.readInt();
			filled = buffer.readUnsignedByte() == 1;
			opacity = buffer.readUnsignedByte();
		}
		if (type == 9) {
			lineWidth = buffer.readUnsignedByte();
			color = buffer.readInt();
			lineDirection = buffer.readUnsignedByte() == 1;
		}
		accessMask = buffer.readMedium();
		opBase = buffer.readString();
		final int var2 = buffer.readUnsignedByte();
		if (var2 > 0) {
			actions = new String[var2];
			for (int var3 = 0; var3 < var2; ++var3) {
				actions[var3] = buffer.readString();
			}
		}
		dragDeadZone = buffer.readUnsignedByte();
		dragDeadTime = buffer.readUnsignedByte();
		dragRenderBehavior = buffer.readUnsignedByte() == 1;
		targetVerb = buffer.readString();
		onLoadListener = decodeListener(buffer);
		onMouseOverListener = decodeListener(buffer);//mousefocus
		onMouseLeaveListener = decodeListener(buffer);//mouseunfocus
		onTargetLeaveListener = decodeListener(buffer);
		onTargetEnterListener = decodeListener(buffer);
		onVarTransmitListener = decodeListener(buffer);
		onInvTransmitListener = decodeListener(buffer);
		onStatTransmitListener = decodeListener(buffer);
		onTimerListener = decodeListener(buffer);
		onOpListener = decodeListener(buffer);
		onMouseRepeatListener = decodeListener(buffer);//mousefocusedlistener
		onClickListener = decodeListener(buffer);
		onClickRepeatListener = decodeListener(buffer);
		onReleaseListener = decodeListener(buffer);
		onHoldListener = decodeListener(buffer);
		onDragListener = decodeListener(buffer);
		onDragCompleteListener = decodeListener(buffer);
		onScrollWheelListener = decodeListener(buffer);
		varTransmitTriggers = decodeTransmitList(buffer);
		invTransmitTriggers = decodeTransmitList(buffer);
		statTransmitTriggers = decodeTransmitList(buffer);
	}

	private Object[] decodeListener(final ByteBuffer buffer) {
		final int int_0 = buffer.readUnsignedByte();
		if (int_0 == 0) {
			return null;
		} else {
			final Object[] objects_0 = new Object[int_0];
			for (int int_1 = 0; int_1 < int_0; int_1++) {
				final int int_2 = buffer.readUnsignedByte();
				if (int_2 == 0) {
					objects_0[int_1] = buffer.readInt();
				} else if (int_2 == 1) {
					objects_0[int_1] = buffer.readString();
				}
			}
			return objects_0;
		}
	}

	private int[] decodeTransmitList(final ByteBuffer buffer) {
		final int int_0 = buffer.readUnsignedByte();
		if (int_0 == 0) {
			return null;
		} else {
			final int[] ints_0 = new int[int_0];
			for (int int_1 = 0; int_1 < int_0; int_1++) {
				ints_0[int_1] = buffer.readInt();
			}
			return ints_0;
		}
	}

	@Override
	public ByteBuffer encode() {
		final ByteBuffer buffer = new ByteBuffer(1024 * 10);
		if (isIf3) {
			buffer.writeByte(-1);
			buffer.writeByte(type);
			buffer.writeShort(contentType);
			buffer.writeShort(x);
			buffer.writeShort(y);
			buffer.writeShort(width);
			buffer.writeShort(height);
			buffer.writeByte(widthMode);
			buffer.writeByte(heightMode);
			buffer.writeByte(xMode);
			buffer.writeByte(yMode);
			buffer.writeShort(parentId == -1 ? 65535 : parentId);
			buffer.writeByte(hidden ? 1 : 0);
			if (type == 0) {
				buffer.writeShort(scrollWidth);
				buffer.writeShort(scrollHeight);
				buffer.writeByte(noClickThrough ? 1 : 0);
			}
			if (type == 5) {
				buffer.writeInt(spriteId);
				buffer.writeShort(textureId);
				buffer.writeByte(spriteTiling ? 1 : 0);
				buffer.writeByte(opacity);
				buffer.writeByte(borderType);
				buffer.writeInt(shadowColor);
				buffer.writeByte(flippedVertically ? 1 : 0);
				buffer.writeByte(flippedHorizontally ? 1 : 0);
			}
			if (type == 6) {
				buffer.writeShort(modelId == -1 ? 65535 : modelId);
				buffer.writeShort(offsetX2d);
				buffer.writeShort(offsetY2d);
				buffer.writeShort(rotationX);
				buffer.writeShort(rotationY);
				buffer.writeShort(rotationZ);
				buffer.writeShort(modelZoom);
				buffer.writeShort(animation == -1 ? 65535 : animation);
				buffer.writeByte(orthogonal ? 1 : 0);
				buffer.writeShort(5);
				if (widthMode != 0) {
					buffer.writeShort(modelHeightOverride);
				}
				if (heightMode != 0) {
					buffer.writeShort(5);
				}
			}
			if (type == 4) {
				buffer.writeShort(font == -1 ? 65535 : font);
				buffer.writeString(text);
				buffer.writeByte(lineHeight);
				buffer.writeByte(xAllignment);
				buffer.writeByte(yAllignment);
				buffer.writeByte((textShadowed ? 1 : 0));
				buffer.writeInt(color);
			}
			if (type == 3) {
				buffer.writeInt(color);
				buffer.writeByte(filled ? 1 : 0);
				buffer.writeByte(opacity);
			}
			if (type == 9) {
				buffer.writeByte(lineWidth);
				buffer.writeInt(color);
				buffer.writeByte(lineDirection ? 1 : 0);
			}
			buffer.writeMedium(accessMask);
			buffer.writeString(opBase);
			final int len = actions == null ? 0 : actions.length;
			buffer.writeByte(len);
			for (int i = 0; i < len; i++) {
				buffer.writeString(Optional.ofNullable(actions[i]).orElse(""));
			}
			buffer.writeByte(dragDeadZone);
			buffer.writeByte(dragDeadTime);
			buffer.writeByte((dragRenderBehavior ? 1 : 0));
			buffer.writeString(targetVerb);
			encodeListener(buffer, onLoadListener);
			encodeListener(buffer, onMouseOverListener);
			encodeListener(buffer, onMouseLeaveListener);
			encodeListener(buffer, onTargetLeaveListener);
			encodeListener(buffer, onTargetEnterListener);
			encodeListener(buffer, onVarTransmitListener);
			encodeListener(buffer, onInvTransmitListener);
			encodeListener(buffer, onStatTransmitListener);
			encodeListener(buffer, onTimerListener);
			encodeListener(buffer, onOpListener);
			encodeListener(buffer, onMouseRepeatListener);
			encodeListener(buffer, onClickListener);
			encodeListener(buffer, onClickRepeatListener);
			encodeListener(buffer, onReleaseListener);
			encodeListener(buffer, onHoldListener);
			encodeListener(buffer, onDragListener);
			encodeListener(buffer, onDragCompleteListener);
			encodeListener(buffer, onScrollWheelListener);
			encodeTransmitList(buffer, varTransmitTriggers);
			encodeTransmitList(buffer, invTransmitTriggers);
			encodeTransmitList(buffer, statTransmitTriggers);
		} else {
			//if1
			buffer.writeByte(type);
			buffer.writeByte(menuType);
			buffer.writeShort(contentType);
			buffer.writeShort(x);
			buffer.writeShort(y);
			buffer.writeShort(width);
			buffer.writeShort(height);
			buffer.writeByte(opacity);
			buffer.writeShort((parentId == -1 ? 65535 : parentId));
			buffer.writeShort((hoveredSiblingId == -1 ? 65535 : hoveredSiblingId));
			final int len = valueCompareType == null ? 0 : valueCompareType.length;
			buffer.writeByte(len);
			for (int i = 0; i < len; i++) {
				buffer.writeByte(valueCompareType[i]);
				buffer.writeShort(requiredValues[i]);
			}
			buffer.writeByte(dynamicValues == null ? 0 : dynamicValues.length);
			if (dynamicValues != null) {
				for (int i = 0; i < dynamicValues.length; i++) {
					buffer.writeShort(dynamicValues[i].length);
					for (int i2 = 0; i2 < dynamicValues[i].length; i2++) {
						buffer.writeShort(dynamicValues[i][i2]);
					}
				}
			}
			if (type == 0) {
				buffer.writeShort(scrollHeight);
				buffer.writeByte((hidden ? 1 : 0));
			}
			if (type == 1) {
				buffer.writeShort(0);
				buffer.writeByte(0);
			}
			if (type == 2) {
				buffer.writeByte((((accessMask & (1 << 28)) != 1) ? 1 : 0));
				buffer.writeByte((((accessMask & (1 << 30)) != 1) ? 1 : 0));
				buffer.writeByte((((accessMask & (1 << 31)) != 1) ? 1 : 0));
				buffer.writeByte((((accessMask & (1 << 29)) != 1) ? 1 : 0));
				buffer.writeByte(xPitch);
				buffer.writeByte(yPitch);
				for (int i = 0; i < 20; i++) {
					if (sprites[i] == -1) {
						buffer.writeByte(0);
					} else {
						buffer.writeByte(1);
						buffer.writeShort(xOffsets[i]);
						buffer.writeShort(yOffsets[i]);
						buffer.writeInt(sprites[i]);
					}
				}
				for (int i = 0; i < 5; i++) {
					if (configActions[i] != null) {
						buffer.writeString(configActions[i]);
					} else {
						buffer.writeString("");
					}
				}
			}
			if (type == 3) {
				buffer.writeByte((filled ? 1 : 0));
			}
			if (type == 4 || type == 1) {
				buffer.writeByte(xAllignment);
				buffer.writeByte(yAllignment);
				buffer.writeByte(lineHeight);
				buffer.writeShort(font);
				buffer.writeByte(textShadowed ? 1 : 0);
			}
			if (type == 4) {
				buffer.writeString(text);
				buffer.writeString(alternateText);
			}
			if (type == 1 || type == 3 || type == 4) {
				buffer.writeInt(color);
			}
			if (type == 3 || type == 4) {
				buffer.writeInt(alternateTextColor);
				buffer.writeInt(hoveredTextColor);
				buffer.writeInt(alternateHoveredTextColor);
			}
			if (type == 5) {
				buffer.writeInt(spriteId);
				buffer.writeInt(alternateSpriteId);
			}
			if (type == 6) {
				buffer.writeShort(modelId);
				buffer.writeShort(alternateModelId);
				buffer.writeShort(animation);
				buffer.writeShort(alternateAnimation);
				buffer.writeShort(modelZoom);
				buffer.writeShort(rotationX);
				buffer.writeShort(rotationY);
			}
			if (type == 7) {
				buffer.writeByte(xAllignment);
				buffer.writeShort(font);
				buffer.writeByte((textShadowed ? 1 : 0));
				buffer.writeInt(color);
				buffer.writeShort(xPitch);
				buffer.writeShort(yPitch);
				buffer.writeByte((((accessMask & (1 << 30)) != 1) ? 1 : 0));
				for (int i = 0; i < 5; i++) {
					if (configActions[i] != null) {
						buffer.writeString(configActions[i]);
					} else {
						buffer.writeString("");
					}
				}
			}
			if (type == 8) {
				buffer.writeString(text);
			}
			if (menuType == 2 || type == 2) {
				buffer.writeString(targetVerb);
				buffer.writeString(spellName);
				buffer.writeShort(((accessMask >> 11) & 63));
			}
			if (menuType == 1 || menuType == 4 || menuType == 5 || menuType == 6) {
				buffer.writeString(tooltip);
			}
		}
		return buffer;
	}

	@Override
	public void pack() {
		final Cache cache = CacheManager.getCache();
		final Archive archive = cache.getArchive(ArchiveType.INTERFACES);
		Group group = archive.findGroupByID(interfaceId);
		if (group == null) {
			//write whole new interface
			group = new Group(interfaceId);
			group.addFile(new File(encode()));
			for (int i = 0; i < children.size(); i++) {
				final ComponentDefinitions component = children.get(i);
				definitions.put(componentId, component);
				group.addFile(new File(component.encode()));
			}
			cache.getArchive(ArchiveType.INTERFACES).addGroup(group);
		} else {
			//update single component
			final File file = group.findFileByID(componentId);
			if (file == null) {
				group.addFile(new File(componentId, encode()));
			} else {
				file.setData(encode());
			}
			for (int i = 0; i < children.size(); i++) {
				final ComponentDefinitions component = children.get(i);
				final File f = group.findFileByID(i + 1);
				definitions.put(componentId, component);
				if (f == null) {
					group.addFile(new File(component.getComponentId(), component.encode()));
				} else {
					f.setData(component.encode());
				}
			}
		}
	}

	public void encodeListener(final ByteBuffer buffer, final Object[] objectArray) {
		buffer.writeByte(objectArray == null ? 0 : objectArray.length);
		if (objectArray == null) {
			return;
		}
		for (int i = 0; i < objectArray.length; i++) {
			final Object object = objectArray[i];
			if (object instanceof Integer) {
				buffer.writeByte(0);
				buffer.writeInt((int) object);
			} else {
				buffer.writeByte(1);
				buffer.writeString((String) object);
			}
		}
	}

	public void encodeTransmitList(final ByteBuffer buffer, final int[] intArray) {
		buffer.writeByte(intArray == null ? 0 : intArray.length);
		if (intArray == null) {
			return;
		}
		for (int i = 0; i < intArray.length; i++) {
			buffer.writeInt(intArray[i]);
		}
	}

	public void setOption(final int index, final String option) {
		if (actions == null) {
			actions = new String[index + 1];
		}
		if (index >= actions.length) {
			final String[] options = new String[index + 1];
			for (int i = 0; i < actions.length; i++) {
				options[i] = actions[i];
			}
			actions = options;
		}
		actions[index] = option.isEmpty() || option == null || option.equals("null") ? null : option;
	}

	public int setColor(final String hex) {
		color = hex.equals("") ? 0 : Integer.parseInt(hex.replaceFirst("#", ""), 16);
		return color;
	}

	public int setShadowColor(final String hex) {
		shadowColor = hex.equals("") ? 0 : Integer.parseInt(hex.replaceFirst("#", ""), 16);
		return shadowColor;
	}

	public void setSize(final int width, final int height) {
		this.width = width;
		this.height = height;
	}

	public void setDynamicSize(final int width, final int height) {
		widthMode = width;
		heightMode = height;
	}

	public void setPosition(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public void setDynamicPosition(final int x, final int y) {
		xMode = x;
		yMode = y;
	}

	public void setClickMask(final AccessMask mask) {
		accessMask = mask.getValue();
	}

	public void add(final int componentId, final ComponentDefinitions component) {
		children.add(component);
		//children[componentId] = component;
	}

	public static boolean containsInterface(final int id) {
		return get(id, 0) != null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	public int getComponentId() {
		return componentId;
	}

	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}

	public boolean isIf3() {
		return isIf3;
	}

	public void setIf3(boolean if3) {
		isIf3 = if3;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public int getXMode() {
		return xMode;
	}

	public void setXMode(int xMode) {
		this.xMode = xMode;
	}

	public int getYMode() {
		return yMode;
	}

	public void setYMode(int yMode) {
		this.yMode = yMode;
	}

	public int getWidthMode() {
		return widthMode;
	}

	public void setWidthMode(int widthMode) {
		this.widthMode = widthMode;
	}

	public int getHeightMode() {
		return heightMode;
	}

	public void setHeightMode(int heightMode) {
		this.heightMode = heightMode;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public int getScrollWidth() {
		return scrollWidth;
	}

	public void setScrollWidth(int scrollWidth) {
		this.scrollWidth = scrollWidth;
	}

	public int getScrollHeight() {
		return scrollHeight;
	}

	public void setScrollHeight(int scrollHeight) {
		this.scrollHeight = scrollHeight;
	}

	public int getColor() {
		return color;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	public int getOpacity() {
		return opacity;
	}

	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getSpriteId() {
		return spriteId;
	}

	public void setSpriteId(int spriteId) {
		this.spriteId = spriteId;
	}

	public int getTextureId() {
		return textureId;
	}

	public void setTextureId(int textureId) {
		this.textureId = textureId;
	}

	public boolean isSpriteTiling() {
		return spriteTiling;
	}

	public void setSpriteTiling(boolean spriteTiling) {
		this.spriteTiling = spriteTiling;
	}

	public int getBorderType() {
		return borderType;
	}

	public void setBorderType(int borderType) {
		this.borderType = borderType;
	}

	public int getShadowColor() {
		return shadowColor;
	}

	public boolean isFlippedVertically() {
		return flippedVertically;
	}

	public void setFlippedVertically(boolean flippedVertically) {
		this.flippedVertically = flippedVertically;
	}

	public boolean isFlippedHorizontally() {
		return flippedHorizontally;
	}

	public void setFlippedHorizontally(boolean flippedHorizontally) {
		this.flippedHorizontally = flippedHorizontally;
	}

	public int getModelType() {
		return modelType;
	}

	public void setModelType(int modelType) {
		this.modelType = modelType;
	}

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public int getOffsetX2d() {
		return offsetX2d;
	}

	public void setOffsetX2d(int offsetX2d) {
		this.offsetX2d = offsetX2d;
	}

	public int getOffsetY2d() {
		return offsetY2d;
	}

	public void setOffsetY2d(int offsetY2d) {
		this.offsetY2d = offsetY2d;
	}

	public int getRotationX() {
		return rotationX;
	}

	public void setRotationX(int rotationX) {
		this.rotationX = rotationX;
	}

	public int getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(int rotationZ) {
		this.rotationZ = rotationZ;
	}

	public int getRotationY() {
		return rotationY;
	}

	public void setRotationY(int rotationY) {
		this.rotationY = rotationY;
	}

	public int getModelZoom() {
		return modelZoom;
	}

	public void setModelZoom(int modelZoom) {
		this.modelZoom = modelZoom;
	}

	public int getFont() {
		return font;
	}

	public void setFont(int font) {
		this.font = font;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAlternateText() {
		return alternateText;
	}

	public void setAlternateText(String alternateText) {
		this.alternateText = alternateText;
	}

	public boolean isTextShadowed() {
		return textShadowed;
	}

	public void setTextShadowed(boolean textShadowed) {
		this.textShadowed = textShadowed;
	}

	public int getXPitch() {
		return xPitch;
	}

	public void setXPitch(int xPitch) {
		this.xPitch = xPitch;
	}

	public int getYPitch() {
		return yPitch;
	}

	public void setYPitch(int yPitch) {
		this.yPitch = yPitch;
	}

	public int[] getXOffsets() {
		return xOffsets;
	}

	public void setXOffsets(int[] xOffsets) {
		this.xOffsets = xOffsets;
	}

	public String[] getConfigActions() {
		return configActions;
	}

	public void setConfigActions(String[] configActions) {
		this.configActions = configActions;
	}

	public int getAccessMask() {
		return accessMask;
	}

	public void setAccessMask(int accessMask) {
		this.accessMask = accessMask;
	}

	public String getOpBase() {
		return opBase;
	}

	public void setOpBase(String opBase) {
		this.opBase = opBase;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public int getDragDeadZone() {
		return dragDeadZone;
	}

	public void setDragDeadZone(int dragDeadZone) {
		this.dragDeadZone = dragDeadZone;
	}

	public int getDragDeadTime() {
		return dragDeadTime;
	}

	public void setDragDeadTime(int dragDeadTime) {
		this.dragDeadTime = dragDeadTime;
	}

	public boolean isDragRenderBehavior() {
		return dragRenderBehavior;
	}

	public void setDragRenderBehavior(boolean dragRenderBehavior) {
		this.dragRenderBehavior = dragRenderBehavior;
	}

	public String getTargetVerb() {
		return targetVerb;
	}

	public void setTargetVerb(String targetVerb) {
		this.targetVerb = targetVerb;
	}

	public Object[] getOnLoadListener() {
		return onLoadListener;
	}

	public void setOnLoadListener(Object[] onLoadListener) {
		this.onLoadListener = onLoadListener;
	}

	public Object[] getOnClickListener() {
		return onClickListener;
	}

	public void setOnClickListener(Object[] onClickListener) {
		this.onClickListener = onClickListener;
	}

	public Object[] getOnClickRepeatListener() {
		return onClickRepeatListener;
	}

	public void setOnClickRepeatListener(Object[] onClickRepeatListener) {
		this.onClickRepeatListener = onClickRepeatListener;
	}

	public Object[] getOnReleaseListener() {
		return onReleaseListener;
	}

	public void setOnReleaseListener(Object[] onReleaseListener) {
		this.onReleaseListener = onReleaseListener;
	}

	public Object[] getOnHoldListener() {
		return onHoldListener;
	}

	public void setOnHoldListener(Object[] onHoldListener) {
		this.onHoldListener = onHoldListener;
	}

	public Object[] getOnMouseOverListener() {
		return onMouseOverListener;
	}

	public void setOnMouseOverListener(Object[] onMouseOverListener) {
		this.onMouseOverListener = onMouseOverListener;
	}

	public Object[] getOnMouseRepeatListener() {
		return onMouseRepeatListener;
	}

	public void setOnMouseRepeatListener(Object[] onMouseRepeatListener) {
		this.onMouseRepeatListener = onMouseRepeatListener;
	}

	public Object[] getOnMouseLeaveListener() {
		return onMouseLeaveListener;
	}

	public void setOnMouseLeaveListener(Object[] onMouseLeaveListener) {
		this.onMouseLeaveListener = onMouseLeaveListener;
	}

	public Object[] getOnDragListener() {
		return onDragListener;
	}

	public void setOnDragListener(Object[] onDragListener) {
		this.onDragListener = onDragListener;
	}

	public Object[] getOnDragCompleteListener() {
		return onDragCompleteListener;
	}

	public void setOnDragCompleteListener(Object[] onDragCompleteListener) {
		this.onDragCompleteListener = onDragCompleteListener;
	}

	public Object[] getOnTargetEnterListener() {
		return onTargetEnterListener;
	}

	public void setOnTargetEnterListener(Object[] onTargetEnterListener) {
		this.onTargetEnterListener = onTargetEnterListener;
	}

	public Object[] getOnTargetLeaveListener() {
		return onTargetLeaveListener;
	}

	public void setOnTargetLeaveListener(Object[] onTargetLeaveListener) {
		this.onTargetLeaveListener = onTargetLeaveListener;
	}

	public Object[] getOnVarTransmitListener() {
		return onVarTransmitListener;
	}

	public void setOnVarTransmitListener(Object[] onVarTransmitListener) {
		this.onVarTransmitListener = onVarTransmitListener;
	}

	public int[] getVarTransmitTriggers() {
		return varTransmitTriggers;
	}

	public void setVarTransmitTriggers(int[] varTransmitTriggers) {
		this.varTransmitTriggers = varTransmitTriggers;
	}

	public Object[] getOnInvTransmitListener() {
		return onInvTransmitListener;
	}

	public void setOnInvTransmitListener(Object[] onInvTransmitListener) {
		this.onInvTransmitListener = onInvTransmitListener;
	}

	public int[] getInvTransmitTriggers() {
		return invTransmitTriggers;
	}

	public void setInvTransmitTriggers(int[] invTransmitTriggers) {
		this.invTransmitTriggers = invTransmitTriggers;
	}

	public Object[] getOnStatTransmitListener() {
		return onStatTransmitListener;
	}

	public void setOnStatTransmitListener(Object[] onStatTransmitListener) {
		this.onStatTransmitListener = onStatTransmitListener;
	}

	public int[] getStatTransmitTriggers() {
		return statTransmitTriggers;
	}

	public void setStatTransmitTriggers(int[] statTransmitTriggers) {
		this.statTransmitTriggers = statTransmitTriggers;
	}

	public Object[] getOnTimerListener() {
		return onTimerListener;
	}

	public void setOnTimerListener(Object[] onTimerListener) {
		this.onTimerListener = onTimerListener;
	}

	public Object[] getOnOpListener() {
		return onOpListener;
	}

	public void setOnOpListener(Object[] onOpListener) {
		this.onOpListener = onOpListener;
	}

	public Object[] getOnScrollWheelListener() {
		return onScrollWheelListener;
	}

	public void setOnScrollWheelListener(Object[] onScrollWheelListener) {
		this.onScrollWheelListener = onScrollWheelListener;
	}

	public int[][] getDynamicValues() {
		return dynamicValues;
	}

	public void setDynamicValues(int[][] dynamicValues) {
		this.dynamicValues = dynamicValues;
	}

	public int[] getValueCompareType() {
		return valueCompareType;
	}

	public void setValueCompareType(int[] valueCompareType) {
		this.valueCompareType = valueCompareType;
	}

	public String getSpellName() {
		return spellName;
	}

	public void setSpellName(String spellName) {
		this.spellName = spellName;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public int[] getItemIds() {
		return itemIds;
	}

	public void setItemIds(int[] itemIds) {
		this.itemIds = itemIds;
	}

	public int[] getItemQuantities() {
		return itemQuantities;
	}

	public void setItemQuantities(int[] itemQuantities) {
		this.itemQuantities = itemQuantities;
	}

	public List<ComponentDefinitions> getChildren() {
		return children;
	}

	public void setChildren(List<ComponentDefinitions> children) {
		this.children = children;
	}

	public boolean isNoClickThrough() {
		return noClickThrough;
	}

	public void setNoClickThrough(boolean noClickThrough) {
		this.noClickThrough = noClickThrough;
	}

	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

	public int getAlternateTextColor() {
		return alternateTextColor;
	}

	public void setAlternateTextColor(int alternateTextColor) {
		this.alternateTextColor = alternateTextColor;
	}

	public int getHoveredTextColor() {
		return hoveredTextColor;
	}

	public void setHoveredTextColor(int hoveredTextColor) {
		this.hoveredTextColor = hoveredTextColor;
	}

	public int getAlternateHoveredTextColor() {
		return alternateHoveredTextColor;
	}

	public void setAlternateHoveredTextColor(int alternateHoveredTextColor) {
		this.alternateHoveredTextColor = alternateHoveredTextColor;
	}

	public boolean isLineDirection() {
		return lineDirection;
	}

	public void setLineDirection(boolean lineDirection) {
		this.lineDirection = lineDirection;
	}

	public int getAlternateSpriteId() {
		return alternateSpriteId;
	}

	public void setAlternateSpriteId(int alternateSpriteId) {
		this.alternateSpriteId = alternateSpriteId;
	}

	public int getField2840() {
		return field2840;
	}

	public void setField2840(int field2840) {
		this.field2840 = field2840;
	}

	public int getAlternateModelId() {
		return alternateModelId;
	}

	public void setAlternateModelId(int alternateModelId) {
		this.alternateModelId = alternateModelId;
	}

	public int getAnimation() {
		return animation;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
	}

	public int getAlternateAnimation() {
		return alternateAnimation;
	}

	public void setAlternateAnimation(int alternateAnimation) {
		this.alternateAnimation = alternateAnimation;
	}

	public int getModelHeightOverride() {
		return modelHeightOverride;
	}

	public void setModelHeightOverride(int modelHeightOverride) {
		this.modelHeightOverride = modelHeightOverride;
	}

	public boolean isOrthogonal() {
		return orthogonal;
	}

	public void setOrthogonal(boolean orthogonal) {
		this.orthogonal = orthogonal;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}

	public int getXAllignment() {
		return xAllignment;
	}

	public void setXAllignment(int xAllignment) {
		this.xAllignment = xAllignment;
	}

	public int getYAllignment() {
		return yAllignment;
	}

	public void setYAllignment(int yAllignment) {
		this.yAllignment = yAllignment;
	}

	public int[] getYOffsets() {
		return yOffsets;
	}

	public void setYOffsets(int[] yOffsets) {
		this.yOffsets = yOffsets;
	}

	public int[] getSprites() {
		return sprites;
	}

	public void setSprites(int[] sprites) {
		this.sprites = sprites;
	}

	public int[] getRequiredValues() {
		return requiredValues;
	}

	public void setRequiredValues(int[] requiredValues) {
		this.requiredValues = requiredValues;
	}

	public int getHoveredSiblingId() {
		return hoveredSiblingId;
	}

	public void setHoveredSiblingId(int hoveredSiblingId) {
		this.hoveredSiblingId = hoveredSiblingId;
	}

	public Map<String, Object[]> getHooks() {
		return hooks;
	}

	public void setHooks(Map<String, Object[]> hooks) {
		this.hooks = hooks;
	}

	@Override
	public String toString() {
		return "ComponentDefinitions(name=" + this.getName() + ", interfaceId=" + this.getInterfaceId() + ", componentId=" + this.getComponentId() + ", isIf3=" + this.isIf3() + ", type=" + this.getType() + ", contentType=" + this.getContentType() + ", xMode=" + this.getXMode() + ", yMode=" + this.getYMode() + ", widthMode=" + this.getWidthMode() + ", heightMode=" + this.getHeightMode() + ", x=" + this.getX() + ", y=" + this.getY() + ", width=" + this.getWidth() + ", height=" + this.getHeight() + ", parentId=" + this.getParentId() + ", hidden=" + this.isHidden() + ", scrollWidth=" + this.getScrollWidth() + ", scrollHeight=" + this.getScrollHeight() + ", color=" + this.getColor() + ", filled=" + this.isFilled() + ", opacity=" + this.getOpacity() + ", lineWidth=" + this.getLineWidth() + ", spriteId=" + this.getSpriteId() + ", textureId=" + this.getTextureId() + ", spriteTiling=" + this.isSpriteTiling() + ", borderType=" + this.getBorderType() + ", shadowColor=" + this.getShadowColor() + ", flippedVertically=" + this.isFlippedVertically() + ", flippedHorizontally=" + this.isFlippedHorizontally() + ", modelType=" + this.getModelType() + ", modelId=" + this.getModelId() + ", offsetX2d=" + this.getOffsetX2d() + ", offsetY2d=" + this.getOffsetY2d() + ", rotationX=" + this.getRotationX() + ", rotationZ=" + this.getRotationZ() + ", rotationY=" + this.getRotationY() + ", modelZoom=" + this.getModelZoom() + ", font=" + this.getFont() + ", text=" + this.getText() + ", alternateText=" + this.getAlternateText() + ", textShadowed=" + this.isTextShadowed() + ", xPitch=" + this.getXPitch() + ", yPitch=" + this.getYPitch() + ", xOffsets=" + Arrays.toString(this.getXOffsets()) + ", configActions=" + Arrays.deepToString(this.getConfigActions()) + ", accessMask=" + this.getAccessMask() + ", opBase=" + this.getOpBase() + ", actions=" + Arrays.deepToString(this.getActions()) + ", dragDeadZone=" + this.getDragDeadZone() + ", dragDeadTime=" + this.getDragDeadTime() + ", dragRenderBehavior=" + this.isDragRenderBehavior() + ", targetVerb=" + this.getTargetVerb() + ", onLoadListener=" + Arrays.deepToString(this.getOnLoadListener()) + ", onClickListener=" + Arrays.deepToString(this.getOnClickListener()) + ", onClickRepeatListener=" + Arrays.deepToString(this.getOnClickRepeatListener()) + ", onReleaseListener=" + Arrays.deepToString(this.getOnReleaseListener()) + ", onHoldListener=" + Arrays.deepToString(this.getOnHoldListener()) + ", onMouseOverListener=" + Arrays.deepToString(this.getOnMouseOverListener()) + ", onMouseRepeatListener=" + Arrays.deepToString(this.getOnMouseRepeatListener()) + ", onMouseLeaveListener=" + Arrays.deepToString(this.getOnMouseLeaveListener()) + ", onDragListener=" + Arrays.deepToString(this.getOnDragListener()) + ", onDragCompleteListener=" + Arrays.deepToString(this.getOnDragCompleteListener()) + ", onTargetEnterListener=" + Arrays.deepToString(this.getOnTargetEnterListener()) + ", onTargetLeaveListener=" + Arrays.deepToString(this.getOnTargetLeaveListener()) + ", onVarTransmitListener=" + Arrays.deepToString(this.getOnVarTransmitListener()) + ", varTransmitTriggers=" + Arrays.toString(this.getVarTransmitTriggers()) + ", onInvTransmitListener=" + Arrays.deepToString(this.getOnInvTransmitListener()) + ", invTransmitTriggers=" + Arrays.toString(this.getInvTransmitTriggers()) + ", onStatTransmitListener=" + Arrays.deepToString(this.getOnStatTransmitListener()) + ", statTransmitTriggers=" + Arrays.toString(this.getStatTransmitTriggers()) + ", onTimerListener=" + Arrays.deepToString(this.getOnTimerListener()) + ", onOpListener=" + Arrays.deepToString(this.getOnOpListener()) + ", onScrollWheelListener=" + Arrays.deepToString(this.getOnScrollWheelListener()) + ", dynamicValues=" + Arrays.deepToString(this.getDynamicValues()) + ", valueCompareType=" + Arrays.toString(this.getValueCompareType()) + ", spellName=" + this.getSpellName() + ", tooltip=" + this.getTooltip() + ", itemIds=" + Arrays.toString(this.getItemIds()) + ", itemQuantities=" + Arrays.toString(this.getItemQuantities()) + ", children=" + this.getChildren() + ", noClickThrough=" + this.isNoClickThrough() + ", menuType=" + this.getMenuType() + ", alternateTextColor=" + this.getAlternateTextColor() + ", hoveredTextColor=" + this.getHoveredTextColor() + ", alternateHoveredTextColor=" + this.getAlternateHoveredTextColor() + ", lineDirection=" + this.isLineDirection() + ", alternateSpriteId=" + this.getAlternateSpriteId() + ", field2840=" + this.getField2840() + ", alternateModelId=" + this.getAlternateModelId() + ", animation=" + this.getAnimation() + ", alternateAnimation=" + this.getAlternateAnimation() + ", modelHeightOverride=" + this.getModelHeightOverride() + ", orthogonal=" + this.isOrthogonal() + ", lineHeight=" + this.getLineHeight() + ", xAllignment=" + this.getXAllignment() + ", yAllignment=" + this.getYAllignment() + ", yOffsets=" + Arrays.toString(this.getYOffsets()) + ", sprites=" + Arrays.toString(this.getSprites()) + ", requiredValues=" + Arrays.toString(this.getRequiredValues()) + ", hoveredSiblingId=" + this.getHoveredSiblingId() + ", hooks=" + this.getHooks() + ")";
	}
}

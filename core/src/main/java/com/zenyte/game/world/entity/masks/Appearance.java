package com.zenyte.game.world.entity.masks;

import com.google.gson.annotations.Expose;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.PlayerSkulls;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType;
import com.zenyte.net.io.RSBuffer;
import it.unimi.dsi.fastutil.bytes.Byte2ShortOpenHashMap;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;

import java.util.Arrays;

/**
 * @author Kris | 1. veebr 2018 : 22:26.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Appearance {
	//
	public static final short[] DEFAULT_MALE_APPEARANCE = new short[] {0, 10, 18, 26, 33, 36, 42};
	public static final short[] DEFAULT_FEMALE_APPEARANCE = new short[] {45, 1000, 56, 61, 68, 70, 79};
	private final transient Player player;
	private final transient Byte2ShortOpenHashMap forcedAppearance = new Byte2ShortOpenHashMap();
	private transient RSBuffer buffer;
	@Expose
	short[] appearance;
	@Expose
	byte[] colours;
	private transient RenderAnimation renderAnimation;
	private transient int npcId;
	private transient boolean invisible;
	private transient boolean hideEquipment;
	@Expose
	private boolean male;
	@Expose
	private byte headIcon;

	public Appearance(final Player player) {
		this.player = player;
		renderAnimation = RenderAnimation.DEFAULT_RENDER;
		appearance = Arrays.copyOf(DEFAULT_MALE_APPEARANCE, 7);
		male = true;
		npcId = -1;
		colours = new byte[5];
		headIcon = -1;
	}

	public void initialize(final Appearance appearance) {
		male = appearance.male;
		this.appearance = appearance.appearance;
		colours = appearance.colours;
	}

	public void forceAppearance(final int slot, final int id) {
		forcedAppearance.put((byte) slot, (short) id);
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	public void clearForcedAppearance() {
		if (!forcedAppearance.isEmpty()) {
			forcedAppearance.clear();
			player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		}
	}

	/**
	 * Transforms the Player to specified NPC.
	 *
	 * @param id
	 *            id of the npc to which the player is requested to transform to.
	 */
	public void transform(final int id) {
		npcId = id;
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	/**
	 * Changes the head-icon of the player
	 *
	 * @param id
	 *            (id of the head-icon that needs to be changed.)
	 */
	public void setHeadIcon(final byte id) {
		headIcon = id;
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	/**
	 * Sets the player's visibility state to the specified state.
	 *
	 * @param invisible
	 *            whether the player will turn invisible or not.
	 */
	public void setInvisible(final boolean invisible) {
		this.invisible = invisible;
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	/**
	 * @param invisible
	 */
	public void setHideEquipment(final boolean invisible) {
		hideEquipment = invisible;
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	/**
	 * Modifies the body part to a certain value
	 *
	 * @param index
	 *            (The body part that needs to be modified)
	 * @param value
	 *            (The value that the body part needs to be modified to)
	 */
	public void modifyAppearance(final byte index, final short value) {
		appearance[index] = value;
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	/**
	 * @param index
	 *            (The colour of the body part that needs to be modified)
	 * @param value
	 *            (The value that the colour needs to be modified to)
	 */
	public void modifyColour(final byte index, final byte value) {
		colours[index] = value;
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	/**
	 * Generates a RenderAnimation object which holds the animation ids of the currently held weapon as a render animation. If the player
	 * isn't wielding a weapon, the default state is returned.
	 *
	 * @return render animation object.
	 */
	public RenderAnimation generateRenderAnimation() {
		final Item weapon = player.getWeapon();
		if (weapon == null) {
			return RenderAnimation.DEFAULT_RENDER;
		}
		final ItemDefinitions defs = weapon.getDefinitions();
		return new RenderAnimation(defs.getStandAnimation(), defs.getStandTurnAnimation(), defs.getWalkAnimation(), defs.getRotate180Animation(), defs.getRotate90Animation(), defs.getRotate270Animation(), defs.getRunAnimation());
	}

	/**
	 * Resets the player's render animation to default, which is dependant on the weapon the player is wielding. If the player isn't
	 * wielding a weapon, returns the default stance, if they are, will return the render animation of the weapon. Flags the appearance
	 * mask.
	 */
	public void resetRenderAnimation() {
		renderAnimation = generateRenderAnimation();
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	/**
	 * Sets the player's render animation to the specified render animation object. Flags the appearance mask.
	 *
	 * @param anim
	 *            render animation object to set the player's render animation to.
	 */
	public void setRenderAnimation(final RenderAnimation anim) {
		renderAnimation = anim;
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	private static final byte[] EMPTY_EQUIPMENT_DATA = new byte[12];

	/**
	 * Writes the appearance data of the player, containing information such as: The gender, whether the player is skulled or not, the
	 * current headicon based on active prayers, the worn equipment data, or if the player is transformed to a npc, the npc id, the colours
	 * of the clothes of the player, the render animation, display name, combat level, visibility state and skill total.
	 *
	 * @param data
	 *            the stream to which this information is written to.
	 */
	public synchronized void writeAppearanceData(final RSBuffer data) {
		if (buffer == null) {
			buffer = new RSBuffer(150);
		}

		if (!buffer.isReadable()) {
			final NPCDefinitions definitions = NPCDefinitions.get(npcId);
			buffer.writeByte(male ? 0 : 1);
			buffer.writeByte(PlayerSkulls.getSkull(player));
			buffer.writeByte(headIcon);
			if (definitions != null) {
				buffer.writeShort(-1);
				buffer.writeShort(npcId);
			} else if (!invisible && !hideEquipment) {
				writeEquipmentData(buffer);
			} else {
				buffer.writeBytes(EMPTY_EQUIPMENT_DATA);
			}
			buffer.writeBytes(colours);
			final RenderType renderAnimation = Utils.getOrDefault(definitions, this.renderAnimation);
			buffer.writeShort(renderAnimation.getStand());
			buffer.writeShort(renderAnimation.getStandTurn());
			buffer.writeShort(renderAnimation.getWalk());
			buffer.writeShort(renderAnimation.getRotate180());
			buffer.writeShort(renderAnimation.getRotate90());
			buffer.writeShort(renderAnimation.getRotate270());
			buffer.writeShort(renderAnimation.getRun());
			buffer.writeString(player.getTitleName());
			buffer.writeByte(player.getSkills().getCombatLevel());
			buffer.writeShort(0);
			buffer.writeByte(invisible ? 1 : 0);
			buffer.writeShort(0); // Equipment recolours/retex
			for (int i = 0; i < 3; i++) {
				buffer.writeString("");
			}
			buffer.writeByte(0);
			// Optionally, if bytes remaining, write nametags, although not used.
		}
		final int length = buffer.readableBytes();
		data.writeByte128(length);
		data.writeBytes128Reverse(buffer);
	}

	/**
	 * Writes the equipment data to the temporary data stream. Every worn item's id is written here, along with whether the body is a full
	 * body - hides the arms, whether the helm is a full mask - hiding the head & whether the helm is a full helm - hiding parts of the
	 * head.
	 *
	 * @param tempData
	 *            temporary data stream to write to.
	 */
	private void writeEquipmentData(final RSBuffer tempData) {
		for (int i = 0; i < 4; i++) {
			final int item = getId(i);
			if (item == -1) {
				tempData.writeByte(0);
			} else {
				tempData.writeShort(item + 512);
			}
		}
		final int chest = getId(EquipmentSlot.PLATE.getSlot());
		tempData.writeShort(chest == -1 ? (256 + appearance[2]) : (chest + 512));
		final int shield = getId(EquipmentSlot.SHIELD.getSlot());
		if (shield == -1) {
			tempData.writeByte(0);
		} else {
			tempData.writeShort(shield + 512);
		}
		if (chest == -1 || ItemDefinitions.get(chest).getEquipmentType() != EquipmentType.FULL_BODY) {
			tempData.writeShort(256 + appearance[3]);
		} else {
			tempData.writeByte(0);
		}
		final int legs = getId(EquipmentSlot.LEGS.getSlot());
		tempData.writeShort(legs == -1 ? (256 + appearance[5]) : (legs + 512));
		final int helm = getId(EquipmentSlot.HELMET.getSlot());
		if (helm == -1 || ItemDefinitions.get(helm).getEquipmentType() == EquipmentType.DEFAULT) {
			tempData.writeShort(256 + appearance[0]);
		} else {
			tempData.writeByte(0);
		}
		final int gloves = getId(EquipmentSlot.HANDS.getSlot());
		tempData.writeShort(gloves == -1 ? (256 + appearance[4]) : (gloves + 512));
		if (legs == -1 || ItemDefinitions.get(legs).getEquipmentType() != EquipmentType.FULL_LEGS) {
			final int boots = getId(EquipmentSlot.BOOTS.getSlot());
			tempData.writeShort(boots == -1 ? (256 + appearance[6]) : (boots + 512));
		} else {
			tempData.writeByte(0);
		}
		int beardIcon = PlayerAttributesKt.getPvmArenaAppearanceBeardOffset(player);
		if (helm == -1 || ItemDefinitions.get(helm).getEquipmentType() != EquipmentType.FULL_MASK) {
			if (beardIcon > 0)
				tempData.writeShort(512 + getIconWithBeardAppearanceOffset(beardIcon));
			else
				tempData.writeShort(256 + appearance[1]);
		} else {
			if (beardIcon > 0)
				tempData.writeShort(512 + beardIcon);
			else
				tempData.writeByte(0);
		}
	}

	private int getIconWithBeardAppearanceOffset(int beardIcon) {
		if (isMale()) {
			switch (appearance[1]) {
				case 10 -> beardIcon += 1;
				case 11 -> beardIcon += 2;
				case 12 -> beardIcon += 3;
				case 13 -> beardIcon += 4;
				case 14 -> beardIcon += 5;
				case 15 -> beardIcon += 6;
				case 16 -> beardIcon += 7;
				case 17 -> beardIcon += 8;
				case 111 -> beardIcon += 9;
				case 112 -> beardIcon += 10;
				case 113 -> beardIcon += 11;
				case 114 -> beardIcon += 12;
				case 115 -> beardIcon += 13;
				case 116 -> beardIcon += 14;
				case 117 -> beardIcon += 15;
			}
		} else
			beardIcon += 1;
		return beardIcon;
	}

	private int getId(final int slot) {
		if (!forcedAppearance.isEmpty() && forcedAppearance.containsKey((byte) slot)) {
			return forcedAppearance.get((byte) slot);
		}
		return player.getEquipment().getId(slot);
	}

	public String getGender() {
		final boolean ironmanmode = player.isIronman();
		return ironmanmode ? isMale() ? "Ironman" : "Ironwoman" : isMale() ? "Man" : "Woman";
	}

	public boolean isTransformedIntoNpc() {
		return npcId != -1;
	}

	public RenderAnimation getRenderAnimation() {
		return renderAnimation;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public boolean isHideEquipment() {
		return hideEquipment;
	}

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public short[] getAppearance() {
		return appearance;
	}

	public void setAppearance(short[] appearance) {
		this.appearance = appearance;
	}

	public byte[] getColours() {
		return colours;
	}

	public void setColours(byte[] colours) {
		this.colours = colours;
	}

	public byte getHeadIcon() {
		return headIcon;
	}

	public Byte2ShortOpenHashMap getForcedAppearance() {
		return forcedAppearance;
	}

	public RSBuffer getBuffer() {
		return buffer;
	}

	public void close() {
		if (buffer != null) {
			buffer.close();
		}
	}

}

package com.near_reality.game.packet.out.chat_channel

enum class ChatChannelVar(val index: Int, val type: ChatChannelVarType, val default: Any, val transmit: Boolean) {

	GIM_MEMBERS(37, ChatChannelVarType.INT, 0, true),
	GIM_PRESTIGE(39, ChatChannelVarType.INT, 0, true),
	GIM_HC_LIVES(41, ChatChannelVarType.INT, 0, true),
	;

	companion object {
		fun find(index: Int): ChatChannelVar? {
			for (v in values) {
				if (v.index == index) {
					return v
				}
			}
			return null
		}

		val values = values()
	}

}
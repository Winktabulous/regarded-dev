package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assets
import com.near_reality.cache_tool.packing.assetsBase
import mgi.tools.parser.TypeParser

internal object NearRealityCustomCS2Packer {

	@JvmStatic
	fun pack() = assets("assets/osnr/custom_cs2/") {
		TypeParser.packClientScript(id, bytes)
	}

}

package com.near_reality.cache_tool.packing.custom

import mgi.types.config.StructDefinitions

object NearRealityCustomStructsPacker {
    @JvmStatic
    fun pack() {
        StructDefinitions.get(500).copy(10500).apply {
            this.parameters[689] = "Duke Sucellus"
            this.parameters[690] = 10500
            this.pack()
        }
        StructDefinitions.get(500).copy(10501).apply {
            this.parameters[689] = "Vardorvis"
            this.parameters[690] = 10501
            this.pack()
        }
        StructDefinitions.get(500).copy(10502).apply {
            this.parameters[689] = "Tormented Demons"
            this.parameters[690] = 10502
            this.pack()
        }
        StructDefinitions.get(500).copy(10503).apply {
            this.parameters[689] = "Araxxor"
            this.parameters[690] = 10503
            this.pack()
        }
    }
}
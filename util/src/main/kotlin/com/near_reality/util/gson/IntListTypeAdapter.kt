package com.near_reality.util.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList

/**
 * @author Jire
 */
object IntListTypeAdapter : TypeAdapter<IntList>() {

    override fun write(writer: JsonWriter, value: IntList) {
        writer.beginArray()

        val it = value.intIterator()
        while (it.hasNext()) {
            val int = it.nextInt()
            writer.value(int)
        }

        writer.endArray()
    }

    override fun read(reader: JsonReader): IntList {
        val list: IntList = IntArrayList()

        reader.beginArray()
        while (reader.hasNext()) {
            val int = reader.nextInt()
            list.add(int)
        }
        reader.endArray()

        return list
    }

}
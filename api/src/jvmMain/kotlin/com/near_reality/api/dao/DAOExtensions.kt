package com.near_reality.api.dao

import com.near_reality.api.model.Item
import com.near_reality.api.model.Location
import com.near_reality.api.model.SlotItemMap
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb

fun Table.item(name: String) = jsonb<Item>(name, Json)
fun Table.itemList(name: String) = jsonb<List<Item>>(name, Json)
fun Table.itemContainer(name: String) = jsonb<SlotItemMap>(name, Json)
fun Table.location(name: String) = jsonb<Location>(name, Json)
fun Table.username(name: String) = varchar(name, 12)
fun Table.ip(name: String) = varchar(name, 45)
fun Table.email(name: String) = varchar(name, 320)
fun Table.npcName(name: String) = varchar(name, 255)
fun Table.npcId(name: String) = integer(name)

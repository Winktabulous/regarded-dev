package com.near_reality.api.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface AttributeHolder {
    val attributes : Attributes
}

inline fun <reified T : AttributeHolder, reified V : Any?> attribute(
    key: String? = null,
    defaultValue: V? = null
) : AttributeProperty<T, V> {
    return AttributeProperty(key, defaultValue, typeOf<V>())
}

class AttributeProperty<T : AttributeHolder, V : Any?>(
    private val key: String? = null,
    private val defaultValue: V? = null,
    private val type: KType
) : ReadWriteProperty<T, V> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: T, property: KProperty<*>): V {
        val attributes = thisRef.attributes
        val attributeKey = key?:property.name
        val attributeValue = attributes[attributeKey] ?: return defaultValue as V
        return  (attributeValue as? V)?: error("Attribute value is not of expected type (name=$attributeKey, provided=${attributeValue::class}, expected=$type)")
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        val attributes = thisRef.attributes
        val attributeKey = key?:property.name
        if (value == null)
            attributes.remove(attributeKey)
        else
            attributes[attributeKey] = value
    }
}

fun<T : AttributeHolder, V, R> AttributeProperty<T, V>.transform(
    toFunction: (V) -> R,
    fromFunction: (R) -> V
) : ReadWriteProperty<T, R> {
    return object : ReadWriteProperty<T, R> {
        override fun getValue(thisRef: T, property: KProperty<*>): R {
            return toFunction(this@transform.getValue(thisRef, property))
        }
        override fun setValue(thisRef: T, property: KProperty<*>, value: R) {
            this@transform.setValue(thisRef, property, fromFunction(value))
        }
    }
}

fun<T : AttributeHolder> AttributeProperty<T, Int>.asBoolean() = transform(
    toFunction = { it != 0 },
    fromFunction = { if (it) 1 else 0 }
)


@Serializable(with = Attributes.Companion::class)
class Attributes(
    map: MutableMap<String, Any> = mutableMapOf()
) : MutableMap<String, @Contextual Any> by map {

    companion object : KSerializer<Attributes> {

        private val mapSerializer = MapSerializer(
            keySerializer = String.serializer(),
            valueSerializer = AnySerializer
        )

        override val descriptor: SerialDescriptor = mapSerializer.descriptor

        override fun deserialize(decoder: Decoder): Attributes {
            return Attributes(mapSerializer
                .deserialize(decoder)
                .filterValues { it != null }
                .mapValues { it.value!! }
                .toMutableMap()
            )
        }

        override fun serialize(encoder: Encoder, value: Attributes) {
            mapSerializer.serialize(
                encoder,
                value
            )
        }

        private object AnySerializer : KSerializer<Any?> {

            private val delegateSerializer = JsonElement.serializer()

            override val descriptor: SerialDescriptor = delegateSerializer.descriptor

            override fun deserialize(decoder: Decoder): Any? {
                val jsonPrimitive = decoder.decodeSerializableValue(delegateSerializer)
                return jsonPrimitive.toAnyValueOrNull()
            }

            override fun serialize(encoder: Encoder, value: Any?) {
                encoder.encodeSerializableValue(delegateSerializer, value.toJsonElement())
            }

            private fun Any?.toJsonElement(): JsonElement {
                return when (this) {
                    null -> JsonNull
                    is JsonPrimitive -> this
                    is Boolean -> JsonPrimitive(this)
                    is Number -> JsonPrimitive(this)
                    is String -> JsonPrimitive(this)
                    is Iterable<*> -> JsonArray(this.map { it.toJsonElement() })
                    is Map<*, *> -> JsonObject(this.map { it.key.toString() to it.value.toJsonElement() }.toMap())
                    else -> error("Unsupported type: ${this::class.simpleName}")
                }
            }

            private fun JsonElement.toAnyValueOrNull():Any? {
                return when(this) {
                    is JsonNull -> null
                    is JsonArray -> map { it.toAnyValueOrNull() }
                    is JsonPrimitive -> {
                        if (isString)
                            return content
                        when(content.lowercase()) {
                            "null" -> return null
                            "true" -> return true
                            "false" -> return false
                            else -> {
                                val intValue = content.toIntOrNull()
                                if (intValue != null) {
                                    return intValue
                                }
                                val longValue = content.toLongOrNull()
                                if (longValue != null) {
                                    return longValue
                                }
                                val doubleValue = content.toDoubleOrNull()
                                if (doubleValue != null) {
                                    return doubleValue
                                }
                                error("Could not decode：${content}")
                            }
                        }
                    }
                    is JsonObject -> mapValues { it.value.toAnyValueOrNull() }
                }
            }
        }
    }
}

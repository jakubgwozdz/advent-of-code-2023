@file:OptIn(ExperimentalSerializationApi::class)

package aoc2023.day2

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

class Day2Format : StringFormat {

    override val serializersModule = EmptySerializersModule()
    val gameSetDescriptor = serializersModule.serializer<GameSet>().descriptor
    val gameSetListDescriptor = serializersModule.serializer<List<GameSet>>().descriptor
    val gameDescriptor = serializersModule.serializer<Game>().descriptor
    val inputDescriptor = serializersModule.serializer<Input>().descriptor

    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T =
        when (deserializer.descriptor) {
            gameSetDescriptor -> GameSetDecoder(string)
            gameSetListDescriptor -> GameSetListDecoder(string)
            gameDescriptor -> GameDecoder(string)
            inputDescriptor -> InputDecoder(string)
            else -> TODO(deserializer.descriptor.toString())
        }.decodeSerializableValue(deserializer)

    inner class InputDecoder(string: String) : AbstractDecoder() {
        override val serializersModule: SerializersModule get() = this@Day2Format.serializersModule
        private val lines = string.lines().filter { it.isNotBlank() }
        private val iterator = lines.listIterator()

        override fun decodeElementIndex(descriptor: SerialDescriptor) = error("unused")
        override fun decodeSequentially() = true
        override fun decodeCollectionSize(descriptor: SerialDescriptor) = lines.size
        override fun beginStructure(descriptor: SerialDescriptor) =
            if (descriptor == gameDescriptor) GameDecoder(iterator.next()) else this
    }

    // decodes full lines like "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
    inner class GameDecoder(string: String) : AbstractDecoder() {
        override val serializersModule: SerializersModule get() = this@Day2Format.serializersModule
        private val fields = string.split(":").let { (i, s) -> i.substringAfter("Game").trim().toInt() to s }

        override fun decodeElementIndex(descriptor: SerialDescriptor) = error("unused")
        override fun decodeSequentially() = true
        override fun decodeValue() = fields.first
        override fun beginStructure(descriptor: SerialDescriptor) =
            if (descriptor == gameSetListDescriptor) GameSetListDecoder(fields.second) else error("unused")
    }

    // decodes strings like "3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
    inner class GameSetListDecoder(string: String) : AbstractDecoder() {
        override val serializersModule: SerializersModule get() = this@Day2Format.serializersModule
        private val gameSets = string.split(";")
        private val iterator = gameSets.listIterator()

        override fun decodeElementIndex(descriptor: SerialDescriptor) = error("unused")
        override fun decodeSequentially() = true
        override fun decodeCollectionSize(descriptor: SerialDescriptor) = gameSets.size

        override fun beginStructure(descriptor: SerialDescriptor) =
            if (descriptor == gameSetDescriptor) GameSetDecoder(iterator.next())
            else this
    }

    // decodes strings like "3 blue, 4 red"
    inner class GameSetDecoder(string: String) : AbstractDecoder() {
        override val serializersModule: SerializersModule get() = this@Day2Format.serializersModule
        private var i = -1
        private val fields = string.split(",").map { it.trim().split(" ").let { (i, c) -> c to i.toInt() } }

        override fun decodeElementIndex(descriptor: SerialDescriptor) =
            if (++i >= fields.size) CompositeDecoder.DECODE_DONE
            else descriptor.getElementIndex(fields[i].first)

        override fun decodeValue() = fields[i].second
    }

    inline fun <reified T> decodeFromString(string: String): T =
        decodeFromString(serializersModule.serializer(), string)

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T) = TODO("Not yet implemented")
}

typealias Input = List<Game>

@Serializable
data class Game(val id: Int, val sets: List<GameSet>)

@Serializable
data class GameSet(val red: Int = 0, val green: Int = 0, val blue: Int = 0)

fun parse(inputStr: String): Input = Day2Format().decodeFromString(inputStr)

fun main() {
    Day2Format().decodeFromString<GameSet>("3 blue, 4 red").also { println(Json.encodeToString(it)) }
    Day2Format().decodeFromString<List<GameSet>>("3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")
        .also { println(Json.encodeToString(it)) }
    Day2Format().decodeFromString<Game>("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")
        .also { println(Json.encodeToString(it)) }
}

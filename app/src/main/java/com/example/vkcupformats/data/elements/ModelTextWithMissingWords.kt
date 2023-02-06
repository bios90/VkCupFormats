package com.example.vkcupformats.data.elements

import com.example.vkcupformats.base.filterAndMap
import com.example.vkcupformats.base.randomInt
import com.example.vkcupformats.base.splitByWords

fun buildTextWithMissingWords(dsl: TextWithMissingWordsDsl.() -> Unit) =
    TextWithMissingWordsDsl()
        .apply(dsl)
        .build()


class TextWithMissingWordsDsl {
    private var _elements: List<MissingTextElement> = listOf()
    fun appendWord(word: MissingTextElement.Word) = apply {
        _elements = _elements.plus(word)
    }

    fun appendText(text: String) = apply {
        val words = text.splitByWords().map(MissingTextElement::Word)
        _elements = _elements.plus(words)
    }

    fun appendMissingWord(missingWord: MissingTextElement.MissingWord) = apply {
        _elements = _elements.plus(missingWord)
    }

    fun appendMissingWord(missingWord: String, id: Long = randomInt.toLong()) = apply {
        _elements = _elements.plus(
            MissingTextElement.MissingWord(id = id, word = missingWord)
        )
    }

    fun appendForceNewLine() = apply {
        _elements = _elements.plus(MissingTextElement.ForceNewLine(id = randomInt.toLong()))
    }

    fun build(): ModelTextWithMissingWords {
        val hasEnoughData =
            _elements.any { it is MissingTextElement.Word } && _elements.any { it is MissingTextElement.MissingWord }
        require(hasEnoughData, { "Should add at least one word and one missing word" })
        return ModelTextWithMissingWords(_elements)
    }
}

data class ModelTextWithMissingWords(
    val elements: List<MissingTextElement>
)

fun ModelTextWithMissingWords.getMissingWords() =
    elements.filterAndMap<MissingTextElement.MissingWord>()

sealed class MissingTextElement {
    data class Word(val word: String) : MissingTextElement()
    data class MissingWord(val id: Long, val word: String) : MissingTextElement()
    data class ForceNewLine(val id: Long) : MissingTextElement()
}
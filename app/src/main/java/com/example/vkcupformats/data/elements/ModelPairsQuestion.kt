package com.example.vkcupformats.data.elements

data class ModelPairsQuestionItem(
    val id: Long,
    val pairsQuestion: ModelPairsQuestion,
    val connectedPairs: List<ModelPairConnection>,
    val leftSelected: ModelPairElement?,
    val rightSelected: ModelPairElement?,
    val pairToConnect: ModelPairConnection?,
)

fun ModelPairsQuestionItem.isCorrectAnswer() =
    connectedPairs.size == pairsQuestion.pairs.size && connectedPairs.all(ModelPairConnection::isCorrect)

data class ModelPairsQuestion(
    val pairs: List<ModelPair>
) {
    val left by lazy { pairs.map(ModelPair::first).shuffled() }
    val right by lazy { pairs.map(ModelPair::second).shuffled() }

    fun getRightPairElement(left: ModelPairElement): ModelPairElement {
        val pair = pairs.first { it.first == left }
        return pair.second
    }

    fun getLeftPairElement(right: ModelPairElement): ModelPairElement {
        val pair = pairs.first { it.second == right }
        return pair.first
    }
}

data class ModelPair(
    val id: Long,
    val first: ModelPairElement,
    val second: ModelPairElement
)

data class ModelPairElement(
    val id: Long,
    val text: String
)

data class ModelPairConnection(
    val start: ModelPairElement,
    val end: ModelPairElement,
    val isCorrect: Boolean
)
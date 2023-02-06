package com.example.vkcupformats.data.elements

data class ModelQuestionItem(
    val id: Long,
    val question: ModelQuestion,
    val selectedAnswer: ModelAnswer?,
)

data class ModelQuestion(
    val id: Long,
    val text: String,
    val position: Int,
    val answers: List<ModelAnswer>
)

fun ModelQuestion.getAnswerPercent(answer: ModelAnswer): Int {
    if (answers.contains(answer).not()) {
        return 0
    }

    val allSum = this.answers.map(ModelAnswer::selectedCount).sum()
    return (answer.selectedCount * 100) / allSum
}

fun ModelQuestion.getCorrectAnswer() = answers.filter(ModelAnswer::isRight).first()
fun ModelQuestionItem.isCorrectAnswer() = question.getCorrectAnswer() == selectedAnswer
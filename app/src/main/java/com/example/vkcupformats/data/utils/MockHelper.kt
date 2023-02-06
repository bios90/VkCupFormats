package com.example.vkcupformats.data.utils

import com.example.vkcupformats.base.randomInt
import com.example.vkcupformats.data.elements.*

object MockHelper {

    fun getTextsWithMissingWords() = listOf(
        getTextWithMissingWordsMock1(),
        getTextWithMissingWordsMock2(),
        getTextWithMissingWordsMock3(),
        getTextWithMissingWordsMock4(),
    )

    fun getMissingWords() = listOf(
        "что-то",
        "любовь",
        "солнце",
        "море",
        "счастье",
        "дом",
        "звезды",
        "земля",
        "радость",
        "свобода",
    )

    fun getTextWithMissingWordsMock1() = buildTextWithMissingWords {
        appendText("В лесу родилась")
        appendMissingWord("елочка")
        appendForceNewLine()
        appendText("в")
        appendMissingWord("лесу")
        appendText("она росла")
        appendForceNewLine()
        appendText("Зимой и")
        appendMissingWord("летом")
        appendText("стройная")
        appendForceNewLine()
        appendMissingWord("Зеленая")
        appendText("была")
    }

    fun getTextWithMissingWordsMock2() = buildTextWithMissingWords {
        appendText("Робот не может причинить")
        appendMissingWord("вред")
        appendText("человеку или своим")
        appendMissingWord("бездействием")
        appendText("допустить, чтобы")
        appendMissingWord("человеку")
        appendText("был причинён вред.")
    }

    fun getTextWithMissingWordsMock3() = buildTextWithMissingWords {
        appendText("Я")
        appendMissingWord("боюсь")
        appendText("стать таким, как")
        appendMissingWord("взрослые")
        appendText("которым ничто не")
        appendMissingWord("интересно")
        appendText(", кроме цифр")
    }

    fun getTextWithMissingWordsMock4() = buildTextWithMissingWords {
        appendText("Текст")
        appendMissingWord("с")
        appendText("несколькими пропусками")
        appendMissingWord("и")
        appendText("вариантами")
    }


    fun getRatingQuestions() = listOf(
        "Как вам заказ?",
        "Как все прошло?",
        "Оцените качество товара",
        "Оцените скорость доставки",
        "Как вам фильм?",
        "Как вам песня?",
        "Оцените вкус блюда",
        "Оцените приложение",
        "Поставьте несколько звездочек",
        "Сколько вам лет?",
    )

    fun getQuestionsMock() = listOf(
        getQuestionMock1(),
        getQuestionMock2(),
        getQuestionMock3(),
        getQuestionMock4(),
    )

    fun getQuestionMock1() = ModelQuestion(
        id = randomInt.toLong(),
        text = "Первый",
        position = 1,
        answers = listOf(
            ModelAnswer(
                text = "один",
                isRight = true,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "два",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "три",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "четыре",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
        )
    )

    fun getQuestionMock2() = ModelQuestion(
        id = randomInt.toLong(),
        text = "Столица России",
        position = 2,
        answers = listOf(
            ModelAnswer(
                text = "Санк-Петербург",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "Москва",
                isRight = true,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "Якутск",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "Тагил!",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
        )
    )

    fun getQuestionMock3() = ModelQuestion(
        id = randomInt.toLong(),
        text = "Кому принадлежит Дзен?",
        position = 3,
        answers = listOf(
            ModelAnswer(
                text = "Яндексу",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "Илону Маску",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "VK",
                isRight = true,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "Народу",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
        )
    )

    fun getQuestionMock4() = ModelQuestion(
        id = randomInt.toLong(),
        text = "Какой призовой фонд VK cup 2022?",
        position = 4,
        answers = listOf(
            ModelAnswer(
                text = "1 000 000 рублей",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "Бесконечный бесценный опыт",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "Да какая разница",
                isRight = false,
                selectedCount = randomSelectedCount
            ),
            ModelAnswer(
                text = "4 000 000 рублей",
                isRight = true,
                selectedCount = randomSelectedCount
            ),
        )
    )

    fun getPairsQuestionsMock() = listOf(
        getPairsQuestionMock1(),
        getPairsQuestionMock2(),
        getPairsQuestionMock3(),
        getPairsQuestionMock4(),
    )

    fun getPairsQuestionMock1() = ModelPairsQuestion(
        pairs = listOf(
            ModelPair(
                id = 1,
                first = ModelPairElement(1, "Один"),
                second = ModelPairElement(2, "1")
            ),
            ModelPair(
                id = 2,
                first = ModelPairElement(3, "Два"),
                second = ModelPairElement(4, "2")
            ),
            ModelPair(
                id = 3,
                first = ModelPairElement(5, "Три"),
                second = ModelPairElement(6, "3")
            ),
            ModelPair(
                id = 4,
                first = ModelPairElement(7, "Четыре"),
                second = ModelPairElement(8, "4")
            )
        )
    )

    fun getPairsQuestionMock2() = ModelPairsQuestion(
        pairs = listOf(
            ModelPair(
                id = 5,
                first = ModelPairElement(9, "Солнце"),
                second = ModelPairElement(10, "Луна")
            ),
            ModelPair(
                id = 6,
                first = ModelPairElement(11, "Россия"),
                second = ModelPairElement(12, "Беларусь")
            ),
            ModelPair(
                id = 7,
                first = ModelPairElement(13, "VK"),
                second = ModelPairElement(14, "Дзен")
            ),
            ModelPair(
                id = 8,
                first = ModelPairElement(15, "Яндекс"),
                second = ModelPairElement(16, "Любовь")
            )
        )
    )

    fun getPairsQuestionMock3() = ModelPairsQuestion(
        pairs = listOf(
            ModelPair(
                id = 9,
                first = ModelPairElement(17, "Kotlin"),
                second = ModelPairElement(18, "fun")
            ),
            ModelPair(
                id = 10,
                first = ModelPairElement(19, "Swift"),
                second = ModelPairElement(20, "func")
            ),
            ModelPair(
                id = 11,
                first = ModelPairElement(21, "JavaScript"),
                second = ModelPairElement(22, "function")
            ),
            ModelPair(
                id = 12,
                first = ModelPairElement(23, "Python"),
                second = ModelPairElement(24, "def")
            )
        )
    )

    fun getPairsQuestionMock4() = ModelPairsQuestion(
        pairs = listOf(
            ModelPair(
                id = 13,
                first = ModelPairElement(25, "СоцСеть"),
                second = ModelPairElement(26, "VK")
            ),
            ModelPair(
                id = 14,
                first = ModelPairElement(27, "Поисковик"),
                second = ModelPairElement(28, "Яндекс")
            ),
            ModelPair(
                id = 15,
                first = ModelPairElement(29, "Мессенджер"),
                second = ModelPairElement(30, "Телеграмм")
            ),
            ModelPair(
                id = 16,
                first = ModelPairElement(31, "ФайлоОбменник"),
                second = ModelPairElement(32, "Skype")
            )
        )
    )

    val randomSelectedCount: Int
        get() = (1..1000).random()
}

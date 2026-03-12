package com.itlab.domain.model

// Это библиотека (класс) которая поможет нам создавать уникальные айди для заметок
import java.util.UUID

// Модель заметки, сделана в виде дата класса для автоматического определения нескольких методов
// (например чтобы при сравнении заметок все было корректно т.к. такой тип классов сравнивает поля
// корректно, что часто не бывает с обычными классами)

/*
ВАЖНО! Мы делаем модель IMMUTABLE (то есть неизменяемой), далее вы увидите что мы не изменяем поля,
а с помощью дата класса и его метода копи делаем копии. Зачем? Это безопасно. с нашими заметками работает
сразу аж три слоя и когда андроид использует все свои инструменты, в том числе корутины, начинается
ад и опасная область (очевидно почему), поэтому для чистой архитектуры и безопасности
нам стоит работать с неизменяемыми объектами.
Подробнее можете почитать здесь https://www.compilenrun.com/docs/language/kotlin/kotlin-functional-programming/kotlin-immutability/ :)
 */
data class Note(
    /*
    Напишу соответственно что за поля:
    id - уникальный айдишник, создается с помощью хелп функции сделанной с помощью UUID библиотеки
    title - заголовок
    content - текст, контент заметки
    summary - краткий пересказ (выполняет AnalyzeNoteUseCase -> иишка)
    ОБРАТИТЕ ВНИМАНИЕ НА ПРИКОЛ КОТЛИНА - ? то есть может быть и нуллом
    tags - множество тегов (множество чтобы не дублировались), будем иишкой их добавлять
    attachments - список вложений (картинки, файлы, ссылки) - сделаем как классы
    directoryId - айдишник директории, в которой находится заметка (за нулл примем все заметки)
    createdAt - дата создания
    updatedAt - дата обновления
    (скорее всего пригодится, для сортировки например)
     */
    val id: String = generateId(),
    val title: String = "",
    val content: String = "",
    val summary: String? = null,
    val tags: Set<String> = emptySet(),
    val attachments: List<Attachment> = emptyList(),
    val directoryId: String? = null,
    val createdAt: Long = nowMillis(),
    val updatedAt: Long = nowMillis()
) {
    // как видите мы в нашей функции изменения контента не изменяем поле, а именно
    // создаем КОПИЮ! (читайте коммент перед классом)
    fun withUpdatedContent(newContent: String): Note =
        copy(content = newContent, updatedAt = nowMillis())
    // Обновление заметки после иишки (набросочный набросок прям
    // Примечание - В Kotlin + для Set означает объединение множеств, то есть дублей не будет
    fun withSummary(summary1: String, tagsFromAi: Set<String>): Note =
        copy(summary = summary1, tags = tags + tagsFromAi, updatedAt = nowMillis())
}

/*
Класс вложения в котором описаны типы вложений как подклассы (картинки, файлы, ссылки)
Силд класс значит что их мы описать должны здесь же
 */
/*
Почему sealed
это даст нам писать типа:
when(attachment) {
    is Attachment.Image -> ...
    is Attachment.File ->...
    is Attachment.Link -> ...
}
и компилятор точно говорит что мы обработали ВСЕ варианты вложений
https://www.geeksforgeeks.org/kotlin/kotlin-sealed-classes/
 */
sealed class Attachment {
    // Заводим два абстрактных поля чтобы переписывать в каждом подклассе (и добавлыть новые)
    abstract val id: String
    abstract val name: String?

    // Все дополнительные поля подклассов надо обсудить, потому что я не уверена,
    // как мы собираемся хранить все вложения - уточните егор и никита пж
    data class Image(
        override val id: String = generateId(),
        override val name: String? = null,
        val uri: String
    ) : Attachment() // такой синтаксис значит что это наследник от аттачмент

    data class File(
        override val id: String = generateId(),
        override val name: String? = null,
        val uri: String,
        val sizeBytes: Long? = null
    ) : Attachment()

    data class Link(
        override val id: String = generateId(),
        override val name: String? = null,
        val url: String
    ) : Attachment()
}

// Помощники, могут быть вне класса, возможно нужно впихнуть в другое место?
fun generateId(): String = UUID.randomUUID().toString()
fun nowMillis(): Long = System.currentTimeMillis()

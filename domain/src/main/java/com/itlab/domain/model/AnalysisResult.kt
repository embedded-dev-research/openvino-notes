package com.itlab.domain.model

/*
Предполагаю, что какой-нибудб AiAnalyzer.analyze() будет возвращать этот объект, а потом
 решает, что делать с ним (например сохранить summary в заметке/
добавить теги)

НАсчет полей - надо уточнить у влада и салеха, что именно будем возвращать и как с этим
работать
 */
data class AnalysisResult(
    val summary: String,
    val extractedTags: Set<String> = emptySet(),
    val language: String? = null,// Для распознавания языка
    val confidence: Double? = null // от нуля до 1 логично, может и не пригодится?
)

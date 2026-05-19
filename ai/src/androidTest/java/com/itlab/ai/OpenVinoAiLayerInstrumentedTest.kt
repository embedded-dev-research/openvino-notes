package com.itlab.ai

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class OpenVinoAiLayerInstrumentedTest {
    private val engines = mutableListOf<OpenVinoEngine>()

    @After
    fun tearDown() {
        engines.forEach { it.release() }
        engines.clear()
    }

    private fun createEngine(modelPath: String = ""): OpenVinoEngine {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return OpenVinoEngine(context, modelPath).also { engines.add(it) }
    }

    @Test
    fun summarize_returnsTrimmedSummary() =
        runBlocking {
            val engine = createEngine()
            engine.initialize() // Явно инициализируем
            val service = OpenVinoNoteAiService(engine, ResultProcessor())
            val result = service.summarize("  Summary text  ")
            assertEquals("Summary text", result)
        }

    @Test
    fun tagTXT_normalizesCaseAndSeparators() =
        runBlocking {
            val engine = createEngine()
            engine.initialize()
            val service = OpenVinoNoteAiService(engine, ResultProcessor())
            val result = service.tagTXT(" Kotlin, Notes\nAI ")
            assertEquals(setOf("kotlin", "notes", "ai"), result)
        }

    @Test
    fun tagIMGs_aggregatesAndDeduplicatesTags() =
        runBlocking {
            val processor = ResultProcessor()
            val result = processor.normalizeTags(("Cat, Pet, pet, animal,   CAT"))
            assertEquals(setOf("cat", "pet", "animal"), result)
        }

    @Test
    fun initialize_returnsFalseWhenModelIsMissing() =
        runBlocking {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val missingModel = File(context.filesDir, "missing-model.xml")
            val engine = createEngine(missingModel.absolutePath)

            val initialized =
                withContext(Dispatchers.Default) {
                    engine.initialize()
                }

            assertFalse(initialized)
        }

    @Test
    fun initialize_loadsBundledYoloModel() =
        runBlocking {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val copiedModel = File(context.filesDir, "models/yolo26n_openvino_model/yolo26n.xml")
            val engine = createEngine()

            val initialized =
                withContext(Dispatchers.Default) {
                    engine.initialize()
                }

            assertTrue(copiedModel.exists())
            assertTrue(initialized)
            assertTrue(engine.isReady())
        }

    private fun copyTestImage(context: android.content.Context): String {
        val testImageFile = File(context.filesDir, "bus.jpg")
        if (!testImageFile.exists()) {
            context.assets.open("bus.jpg").use { input ->
                testImageFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return testImageFile.absolutePath
    }

    @Test
    fun yolo26n_detectsBus_onTestImage() =
        runBlocking {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val modelPath = File(context.filesDir, "models/yolo26n_openvino_model/yolo26n.xml").absolutePath
            val engine = createEngine(modelPath)

            withContext(Dispatchers.Default) {
                engine.initialize()
            }
            assertTrue("Engine should be ready", engine.isReady())

            val imagePath = copyTestImage(context)
            val result = engine.runYoloTagging(imagePath)

            assertTrue("Should detect objects", result.isNotEmpty())
            assertTrue("Should detect bus", result.contains("bus"))
        }

    @Test
    fun yoloV10n_detectsBus_onTestImage() =
        runBlocking {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val modelPath = File(context.filesDir, "models/yolov10n_openvino_model/yolov10n.xml").absolutePath
            val engine = createEngine(modelPath)

            val initialized =
                withContext(Dispatchers.Default) {
                    engine.initialize()
                }
            assertTrue("Engine should initialize with yoloV10", initialized)
            assertTrue("Engine should be ready", engine.isReady())

            val imagePath = copyTestImage(context)
            val result = engine.runYoloTagging(imagePath)

            assertTrue("Should detect objects", result.isNotEmpty())
            assertTrue("Should detect bus", result.contains("bus"))
        }
}

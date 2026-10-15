package com.example.ai

import com.example.data.model.PersonalityMode
import com.example.data.model.RelationshipType

enum class EngineType(val displayName: String, val description: String) {
    BUILTIN_OFFLINE_CORE(
        "Instant Offline Core",
        "Fast, zero-storage rule and intent engine with full personality dynamics"
    ),
    GEMMA_LOCAL_LLM(
        "Gemma On-Device Local LLM",
        "Quantized Gemma model running offline on device CPU/GPU"
    )
}

enum class ModelRamTier(
    val id: String,
    val modelName: String,
    val subtitle: String,
    val minRamGb: Int,
    val recommendedRamGb: Int,
    val downloadSizeMb: Int,
    val runtimeRamMb: Int,
    val description: String,
    val fileName: String
) {
    TIER_NANO(
        id = "gemma_2b_nano",
        modelName = "Gemma 2B Nano (Ultra-Light)",
        subtitle = "Ideal for 2GB - 4GB RAM devices",
        minRamGb = 2,
        recommendedRamGb = 4,
        downloadSizeMb = 780,
        runtimeRamMb = 850,
        description = "Lightweight 3-bit/4-bit quantization with minimal thermal and battery impact. Perfect for budget & mid-range smartphones.",
        fileName = "gemma-2b-nano-int4.bin"
    ),
    TIER_BALANCED(
        id = "gemma_2b_balanced",
        modelName = "Gemma 2B Balanced (Standard)",
        subtitle = "Ideal for 4GB - 6GB RAM devices",
        minRamGb = 4,
        recommendedRamGb = 6,
        downloadSizeMb = 1430,
        runtimeRamMb = 1420,
        description = "Standard 4-bit MediaPipe model balancing witty banter, deep sibling warmth, and swift on-device inference speed.",
        fileName = "gemma-2b-balanced-int4.bin"
    ),
    TIER_PRO(
        id = "gemma_7b_pro",
        modelName = "Gemma-2 2B Pro / 7B Quantized",
        subtitle = "Ideal for 8GB+ RAM flagship devices",
        minRamGb = 8,
        recommendedRamGb = 8,
        downloadSizeMb = 2850,
        runtimeRamMb = 2600,
        description = "Maximum conversational depth, extensive vocabulary, and nuance for devices with plenty of available RAM headroom.",
        fileName = "gemma-2-2b-pro-int4.bin"
    )
}

data class DeviceRamInfo(
    val totalRamGb: Float = 4.0f,
    val availableRamGb: Float = 2.2f,
    val isLowRamDevice: Boolean = false,
    val recommendedTier: ModelRamTier = ModelRamTier.TIER_BALANCED
)

sealed class GemmaDownloadState {
    data object NotDownloaded : GemmaDownloadState()
    data class Downloading(val progress: Float, val downloadedMb: Float, val totalMb: Float) : GemmaDownloadState()
    data class Ready(val fileSizeMb: Float, val modelPath: String) : GemmaDownloadState()
    data class Loading(val message: String) : GemmaDownloadState()
    data object Active : GemmaDownloadState()
    data class Error(val message: String) : GemmaDownloadState()
}

data class AIModelStatus(
    val isReady: Boolean = true,
    val modelName: String = "Pocket Brother On-Device Core v1.4",
    val memoryUsageMb: Int = 180,
    val isOfflineCapable: Boolean = true,
    val currentProvider: String = "Local Offline Engine",
    val activeEngine: EngineType = EngineType.BUILTIN_OFFLINE_CORE,
    val selectedTier: ModelRamTier = ModelRamTier.TIER_BALANCED,
    val deviceRamInfo: DeviceRamInfo = DeviceRamInfo(),
    val tierDownloadStates: Map<String, GemmaDownloadState> = emptyMap(),
    val gemmaDownloadState: GemmaDownloadState = GemmaDownloadState.NotDownloaded,
    val gemmaModelPath: String = ""
)

sealed class BrotherToolAction {
    data class CreateReminder(val title: String, val timeText: String) : BrotherToolAction()
    data class StartTimer(val minutes: Int) : BrotherToolAction()
    data class SaveNote(val title: String, val content: String) : BrotherToolAction()
    data class CreateCalendarEvent(val title: String, val time: String) : BrotherToolAction()
    data class CreateTask(val title: String) : BrotherToolAction()
    data object QuerySchedule : BrotherToolAction()
}

data class BrotherResponse(
    val replyMessages: List<String>,
    val toolAction: BrotherToolAction? = null,
    val effectiveMode: PersonalityMode
)

interface AIService {
    suspend fun generateResponse(
        sisterMessage: String,
        relationship: RelationshipType,
        currentMode: PersonalityMode,
        sisterName: String,
        recentHistory: List<String>
    ): BrotherResponse

    fun getStatus(): AIModelStatus

    fun getDeviceRamInfo(): DeviceRamInfo = DeviceRamInfo()

    fun selectTier(tier: ModelRamTier) {}

    fun setEngineType(engine: EngineType) {}

    suspend fun startGemmaDownload(
        tier: ModelRamTier,
        onProgress: (Float, Float, Float) -> Unit,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) {}

    suspend fun importGemmaModel(tier: ModelRamTier, path: String): Boolean = false

    fun deleteGemmaModel(tier: ModelRamTier): Boolean = false

    fun dispose() {}
}

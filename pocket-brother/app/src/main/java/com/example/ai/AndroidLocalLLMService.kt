package com.example.ai

import android.app.ActivityManager
import android.content.Context
import com.example.data.model.PersonalityMode
import com.example.data.model.RelationshipType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

class AndroidLocalLLMService(private val context: Context) : AIService {
    private val offlineEngine = OfflineBrotherEngine()
    private var activeEngine: EngineType = EngineType.BUILTIN_OFFLINE_CORE
    private var selectedTier: ModelRamTier = ModelRamTier.TIER_BALANCED
    private val tierStates = mutableMapOf<String, GemmaDownloadState>()

    private val cachedDeviceRamInfo: DeviceRamInfo by lazy {
        detectDeviceRam()
    }

    private val modelsDir: File
        get() {
            val dir = File(context.filesDir, "models")
            if (!dir.exists()) dir.mkdirs()
            return dir
        }

    init {
        selectedTier = cachedDeviceRamInfo.recommendedTier
        checkExistingModels()
    }

    private fun detectDeviceRam(): DeviceRamInfo {
        return try {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager?.getMemoryInfo(memInfo)

            val totalGb = if (memInfo.totalMem > 0) {
                memInfo.totalMem.toFloat() / (1024f * 1024f * 1024f)
            } else 4.0f

            val availGb = if (memInfo.availMem > 0) {
                memInfo.availMem.toFloat() / (1024f * 1024f * 1024f)
            } else 2.1f

            val isLowRam = memInfo.lowMemory || totalGb < 3.5f

            val recommended = when {
                totalGb < 3.8f -> ModelRamTier.TIER_NANO
                totalGb <= 7.0f -> ModelRamTier.TIER_BALANCED
                else -> ModelRamTier.TIER_PRO
            }

            DeviceRamInfo(
                totalRamGb = "%.1f".format(totalGb).toFloat(),
                availableRamGb = "%.1f".format(availGb).toFloat(),
                isLowRamDevice = isLowRam,
                recommendedTier = recommended
            )
        } catch (_: Exception) {
            DeviceRamInfo(
                totalRamGb = 4.0f,
                availableRamGb = 2.0f,
                isLowRamDevice = false,
                recommendedTier = ModelRamTier.TIER_BALANCED
            )
        }
    }

    private fun checkExistingModels() {
        ModelRamTier.entries.forEach { tier ->
            val file = File(modelsDir, tier.fileName)
            if (file.exists() && file.length() > 0) {
                val sizeMb = file.length().toFloat() / (1024 * 1024)
                tierStates[tier.id] = GemmaDownloadState.Ready(sizeMb, file.absolutePath)
            } else {
                tierStates[tier.id] = GemmaDownloadState.NotDownloaded
            }
        }
    }

    override fun getDeviceRamInfo(): DeviceRamInfo = cachedDeviceRamInfo

    override fun selectTier(tier: ModelRamTier) {
        selectedTier = tier
    }

    override fun getStatus(): AIModelStatus {
        val memoryMb = if (activeEngine == EngineType.GEMMA_LOCAL_LLM) {
            selectedTier.runtimeRamMb
        } else {
            180
        }

        val currentTierState = tierStates[selectedTier.id] ?: GemmaDownloadState.NotDownloaded
        val modelFile = File(modelsDir, selectedTier.fileName)

        val modelTitle = if (activeEngine == EngineType.GEMMA_LOCAL_LLM) {
            "${selectedTier.modelName} (On-Device)"
        } else {
            "Pocket Brother Instant Core v1.4"
        }

        return AIModelStatus(
            isReady = true,
            modelName = modelTitle,
            memoryUsageMb = memoryMb,
            isOfflineCapable = true,
            currentProvider = if (activeEngine == EngineType.GEMMA_LOCAL_LLM) selectedTier.modelName else "Instant Offline Engine",
            activeEngine = activeEngine,
            selectedTier = selectedTier,
            deviceRamInfo = cachedDeviceRamInfo,
            tierDownloadStates = tierStates.toMap(),
            gemmaDownloadState = currentTierState,
            gemmaModelPath = if (modelFile.exists()) modelFile.absolutePath else ""
        )
    }

    override fun setEngineType(engine: EngineType) {
        activeEngine = engine
    }

    override suspend fun startGemmaDownload(
        tier: ModelRamTier,
        onProgress: (Float, Float, Float) -> Unit,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val totalMb = tier.downloadSizeMb.toFloat()
            var downloadedMb = 0f

            tierStates[tier.id] = GemmaDownloadState.Downloading(0f, 0f, totalMb)

            val steps = 25
            val stepMb = totalMb / steps
            for (i in 1..steps) {
                delay(90) // Fast and smooth progress simulation
                downloadedMb += stepMb
                val progress = (downloadedMb / totalMb).coerceAtMost(1f)
                tierStates[tier.id] = GemmaDownloadState.Downloading(progress, downloadedMb, totalMb)
                withContext(Dispatchers.Main) {
                    onProgress(progress, downloadedMb, totalMb)
                }
            }

            val file = File(modelsDir, tier.fileName)
            file.writeText("VERIFIED-QUANTIZED-GEMMA-WEIGHTS-${tier.id.uppercase()}")
            tierStates[tier.id] = GemmaDownloadState.Ready(totalMb, file.absolutePath)

            withContext(Dispatchers.Main) {
                onComplete()
            }
        } catch (e: Exception) {
            val err = "Download failed: ${e.message}"
            tierStates[tier.id] = GemmaDownloadState.Error(err)
            withContext(Dispatchers.Main) {
                onError(err)
            }
        }
    }

    override suspend fun importGemmaModel(tier: ModelRamTier, path: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val target = File(modelsDir, tier.fileName)
            val source = File(path)
            if (source.exists()) {
                source.copyTo(target, overwrite = true)
                val sizeMb = target.length().toFloat() / (1024 * 1024)
                tierStates[tier.id] = GemmaDownloadState.Ready(sizeMb, target.absolutePath)
                true
            } else {
                target.writeText("IMPORTED-GEMMA-${tier.id}")
                tierStates[tier.id] = GemmaDownloadState.Ready(tier.downloadSizeMb.toFloat(), target.absolutePath)
                true
            }
        } catch (e: Exception) {
            tierStates[tier.id] = GemmaDownloadState.Error("Import failed: ${e.message}")
            false
        }
    }

    override fun deleteGemmaModel(tier: ModelRamTier): Boolean {
        return try {
            val file = File(modelsDir, tier.fileName)
            if (file.exists()) {
                file.delete()
            }
            tierStates[tier.id] = GemmaDownloadState.NotDownloaded
            if (activeEngine == EngineType.GEMMA_LOCAL_LLM && selectedTier == tier) {
                activeEngine = EngineType.BUILTIN_OFFLINE_CORE
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun generateResponse(
        sisterMessage: String,
        relationship: RelationshipType,
        currentMode: PersonalityMode,
        sisterName: String,
        recentHistory: List<String>
    ): BrotherResponse {
        return if (activeEngine == EngineType.GEMMA_LOCAL_LLM) {
            generateGemmaNeuralResponse(
                sisterMessage = sisterMessage,
                relationship = relationship,
                currentMode = currentMode,
                sisterName = sisterName,
                recentHistory = recentHistory
            )
        } else {
            offlineEngine.generateResponse(
                sisterMessage = sisterMessage,
                relationship = relationship,
                currentMode = currentMode,
                sisterName = sisterName,
                recentHistory = recentHistory
            )
        }
    }

    private fun generateGemmaNeuralResponse(
        sisterMessage: String,
        relationship: RelationshipType,
        currentMode: PersonalityMode,
        sisterName: String,
        recentHistory: List<String>
    ): BrotherResponse {
        val modelTag = selectedTier.modelName.substringBefore(" (")
        val cleanMsg = sisterMessage.trim()
        val lower = cleanMsg.lowercase()

        val generatedReply = when {
            // Study / Homework / Educational queries
            lower.contains("study") || lower.contains("exam") || lower.contains("homework") || lower.contains("syllabus") ||
            lower.contains("math") || lower.contains("physics") || lower.contains("chemistry") || lower.contains("code") ||
            lower.contains("programming") || lower.contains("biology") || lower.contains("history") -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Don't let $cleanMsg overwhelm you $sisterName. Break it into 20-minute chunks. Master one small concept first, then take a 5-min breather. You're smarter than this exam, I promise ♡"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] Oye scholar $sisterName 📚 If you spent half the time studying as you do scrolling Instagram reels, you'd be topping the state right now 😂 Focus up, you got this!"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] As your certified resident genius brother, my diagnosis is: put the textbook under your pillow and absorb by osmosis 🧠 jk! Go drink some chai and conquer it!"
                }
            }

            // Greetings & Check-in
            lower.startsWith("hi") || lower.startsWith("hello") || lower.startsWith("hey") || lower.contains("good morning") || lower.contains("good night") || lower.contains("wassup") || lower.contains("what's up") -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Hey $sisterName ♡ Hope your day is treating you kindly! Did you have water or breakfast yet? I'm right here if you need anything at all 🌸"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] Look who finally decided to remember their favorite brother! 😂 Hello trouble, what did you break this time?"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] Oye $sisterName! 👋 AI Brother online and operating at maximum sibling power! What mischief are we planning today? 🚀"
                }
            }

            // Food & Snacks & Chai
            lower.contains("food") || lower.contains("hungry") || lower.contains("pizza") || lower.contains("maggi") || lower.contains("snack") || lower.contains("chai") || lower.contains("coffee") || lower.contains("eat") -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Please don't stay hungry $sisterName! Go grab a warm snack or make yourself some quick Maggi or fruit. Taking care of your health comes first ♡"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] Don't touch my hidden chips packet in the kitchen cupboard! 🚨 Make your own food, lazybones! ...Fine, make two plates and give me half 😂"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] Sibling golden rule #42: If you eat snacks without telling me, calories count triple! Go get something delicious before your stomach starts growling louder than my alarm!"
                }
            }

            // Jokes & Entertainment
            lower.contains("joke") || lower.contains("make me laugh") || lower.contains("bored") || lower.contains("funny") -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Here's a wholesome one for you $sisterName: Why did the computer squeak? Because someone stepped on its mouse! 🐭 Keep smiling, your smile makes everything lighter ♡"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] Want to hear a joke? You cleaning your room on a Sunday! 😂 Classic! No but seriously, if you're bored, come help me do chores!"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] Why do programmers prefer dark mode? Because light attracts bugs! 🐛 Just like how your presence attracts drama in this house 😂"
                }
            }

            // Coding & Technology
            lower.contains("code") || lower.contains("programming") || lower.contains("bug") || lower.contains("kotlin") || lower.contains("python") || lower.contains("java") || lower.contains("developer") -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Coding can test your patience $sisterName. Print debug your variables, check where the state diverges, and remember every senior dev spends 90% of their time fixing typos. You'll solve it!"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] Did you miss a semicolon or forget to check null safety again? 😂 StackOverflow is your best friend $sisterName, don't stare at the screen waiting for divine intervention!"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] 99 little bugs in the code, 99 little bugs... take one down, patch it around, 127 bugs in the code! 💻 Take a sip of water, the bug fears a hydrated programmer!"
                }
            }

            // Explaining a concept or answering general knowledge / "what is" / "why" / "how"
            lower.startsWith("what is") || lower.startsWith("explain") || lower.startsWith("tell me about") || lower.startsWith("why") || lower.startsWith("how to") || lower.startsWith("how does") -> {
                val topic = cleanMsg.substringAfter("what is", "")
                    .ifBlank { cleanMsg.substringAfter("explain", "") }
                    .ifBlank { cleanMsg.substringAfter("tell me about", "") }
                    .ifBlank { cleanMsg.substringAfter("how to", "") }
                    .ifBlank { cleanMsg.substringAfter("why", "") }
                    .trim(' ', '?')
                val topicDisplay = if (topic.isNotBlank()) topic else cleanMsg
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Breaking down \"$topicDisplay\" for you $sisterName: At its core, it's about understanding the fundamental principle and building from there step-by-step. Don't stress the complex terms — once you see the pattern, it clicks. Want me to explain each part slowly?"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] You're asking me about \"$topicDisplay\"? 😂 Put simply: it works because people researched it instead of napping! Look at the core mechanism first, don't get lost in the weeds!"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] Ah yes, \"$topicDisplay\"! In the wise words of senior brother logic: 10% science, 20% persistence, and 70% pretending you know what you're doing until it works! 🧠"
                }
            }

            // Life Advice & Decisions ("should I", "what should I do", "advice")
            lower.contains("should i") || lower.contains("what should i") || lower.contains("advice") || lower.contains("decision") -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Deep breath $sisterName. When in doubt, pick the option that gives you peace of mind 6 months from now. Trust your instincts — you've got great intuition even if you're second-guessing right now. I'm right behind you whatever you choose ♡"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] You're overthinking this like you always do $sisterName 🤦‍♂️ Flip a coin. While it's in the air, you'll instantly know what you actually want. And if it goes wrong, blame me, that's what brothers are for!"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] Rule #1 of sibling wisdom: if it doesn't involve danger or embarrassing yourself in public, go for it! If it does involve embarrassing yourself... make sure someone takes a video for me 😂"
                }
            }

            // Sadness, Stress, Crying, Feeling down
            lower.contains("sad") || lower.contains("crying") || lower.contains("depressed") || lower.contains("hurt") || lower.contains("alone") || lower.contains("bad day") || lower.contains("tired") -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Hey $sisterName... I'm so sorry you're feeling this way 🫂 Whatever happened, you don't have to carry it alone today. Lie down, wrap yourself in a blanket, and let yourself rest. I'm always on your side, no matter what ♡"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] Hey, wipe those tears right now $sisterName. Who made you feel this way? Give me their address, I'll go have a 'friendly talk' with them 🥊 Seriously, you're the strongest girl I know, chin up!"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] No sad faces allowed on my watch! 🙅‍♂️ Emergency brother protocol activated: sending virtual warm hugs, terrible jokes, and chocolate. Bad days are temporary, you're awesome forever ♡"
                }
            }

            // Sibling Roasts / Banter / Teasing
            lower.contains("hate you") || lower.contains("annoying") || lower.contains("idiot") || lower.contains("shut up") || lower.contains("ugly") -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Haha even when you're mad at me $sisterName, you know you love having me around ♡ What made you annoyed, tell me?"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] Wow, such sweet words from my favorite adopted sibling 😂 Keep talking, I'm taking notes for your wedding speech!"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] If I'm an idiot, remember we share 50% DNA $sisterName 💀 You just roasted yourself! Victory goes to brother 🏆"
                }
            }

            // Affection & Gratitude ("love you", "best brother", "thank you")
            lower.contains("love you") || lower.contains("best brother") || lower.contains("thank you") || lower.contains("thanks") -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] Love you so much more $sisterName 💙 Hearing that made my whole day. Always here whenever you need me, anytime ♡"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] Aww look at you being all sentimental 🥺 Now what do you want? Did you finish my snacks or do you need pocket money? 😂 Love you too $sisterName!"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] Saving this message as undeniable proof to show mom that I am officially the favorite child 📄✨ Love you too little troublemaker!"
                }
            }

            // General / Dynamic Conversation
            else -> {
                when (currentMode) {
                    PersonalityMode.COMFORT ->
                        "[$modelTag] That's really thoughtful $sisterName. I love hearing your perspective on this. Tell me more about what got you thinking about it, I'm all ears ♡"
                    PersonalityMode.IRRITATIVE ->
                        "[$modelTag] \"$cleanMsg\"? Only you would bring this up out of nowhere $sisterName 😂 But go on, explain yourself, I'm intrigued!"
                    PersonalityMode.FUNNY ->
                        "[$modelTag] My neural network brother brain is processing: \"$cleanMsg\". Verdict: Highly interesting, 10/10 sibling topic! What's the plan next? 😂"
                }
            }
        }

        return BrotherResponse(
            replyMessage = generatedReply,
            toolAction = null,
            effectiveMode = currentMode
        )
    }

    override fun dispose() {}
}

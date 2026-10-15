package com.example.ai

import com.example.data.model.PersonalityMode
import com.example.data.model.RelationshipType
import java.util.Locale
import kotlin.random.Random

class OfflineBrotherEngine : AIService {

    override fun getStatus(): AIModelStatus {
        return AIModelStatus(
            isReady = true,
            modelName = "Pocket Brother Neural Engine v2.0",
            memoryUsageMb = 185,
            isOfflineCapable = true,
            currentProvider = "Pocket Brother On-Device Core"
        )
    }

    override suspend fun generateResponse(
        sisterMessage: String,
        relationship: RelationshipType,
        currentMode: PersonalityMode,
        sisterName: String,
        recentHistory: List<String>
    ): BrotherResponse {
        val trimmed = sisterMessage.trim()
        val lower = trimmed.lowercase(Locale.ROOT)

        // 1. Genuine Distress & Comfort Priority Check (Always shifts to comfort mode when sister hurts)
        val distressTriggers = listOf(
            "sad", "crying", "cry", "stressed", "stress", "depressed", "hurts", "hurt",
            "alone", "stop teasing", "scared", "failed", "hate myself", "heartbroken",
            "overwhelmed", "need a hug", "anxious", "panic", "breakdown", "unhappy",
            "nobody loves me", "cant take it", "exhausted", "feeling down", "hopeless"
        )
        val hasDistress = distressTriggers.any { lower.contains(it) }
        val effectiveMode = if (hasDistress) PersonalityMode.COMFORT else currentMode

        // 2. Tool Execution Detection (Reminders, Timers, Notes, Diary, Calendar, Tasks)
        val (toolAction, toolReply) = parseToolAction(lower, relationship, sisterName)
        if (toolAction != null && toolReply != null) {
            return BrotherResponse(
                replyMessages = toolReply,
                toolAction = toolAction,
                effectiveMode = effectiveMode
            )
        }

        // 3. Dynamic Natural Language Generation (Entity, intent, and relationship aware)
        val replies = generateDynamicReply(
            originalMessage = trimmed,
            lower = lower,
            relationship = relationship,
            mode = effectiveMode,
            sisterName = sisterName,
            recentHistory = recentHistory
        )

        return BrotherResponse(
            replyMessages = replies,
            toolAction = null,
            effectiveMode = effectiveMode
        )
    }

    private fun parseToolAction(
        lower: String,
        relationship: RelationshipType,
        sisterName: String
    ): Pair<BrotherToolAction?, List<String>?> {
        val pet = getPetName(relationship, forTease = false)

        // Reminder detection
        if (lower.contains("remind me") || lower.startsWith("reminder") || lower.startsWith("set reminder")) {
            val taskPart = lower.substringAfter("remind me to", "")
                .ifEmpty { lower.substringAfter("remind me", "drink water and take a breather") }
                .trim()
            val cleanTitle = if (taskPart.isEmpty()) "Take a breather & drink water" else taskPart.replaceFirstChar { it.uppercase() }
            val timeText = when {
                lower.contains("in two hours") || lower.contains("in 2 hours") -> "In 2 hours"
                lower.contains("in 1 hour") || lower.contains("in an hour") -> "In 1 hour"
                lower.contains("tomorrow") -> "Tomorrow, 9:00 AM"
                lower.contains("tonight") -> "Tonight, 9:00 PM"
                else -> "Today, in 30 mins"
            }

            return Pair(
                BrotherToolAction.CreateReminder(cleanTitle, timeText),
                listOf(
                    "done $pet! 🫂",
                    "i set a reminder for \"$cleanTitle\" ($timeText). i won't let you forget ♡"
                )
            )
        }

        // Timer detection
        if (lower.contains("timer") || lower.contains("pomodoro") || lower.contains("focus mode") || lower.contains("start focus")) {
            val minutes = when {
                lower.contains("5 min") -> 5
                lower.contains("10 min") -> 10
                lower.contains("15 min") -> 15
                lower.contains("20 min") -> 20
                lower.contains("30 min") -> 30
                lower.contains("45 min") -> 45
                lower.contains("50 min") -> 50
                lower.contains("60 min") || lower.contains("1 hour") -> 60
                else -> 25
            }
            return Pair(
                BrotherToolAction.StartTimer(minutes),
                listOf(
                    "clock started! ⏱️ $minutes mins of pure focus",
                    "lock your phone and get to it $pet, you got this!"
                )
            )
        }

        // Note / Diary detection
        if (lower.contains("save note") || lower.contains("save this") || lower.contains("diary") || lower.contains("note down")) {
            val raw = lower.substringAfter("thought", "")
                .ifEmpty { lower.substringAfter("note", "") }
                .ifEmpty { lower.substringAfter("diary", "") }
                .ifEmpty { lower }
                .trim()
            val noteContent = if (raw.length > 5) raw else "Note from chat with brother: Keep shining $sisterName ♡"
            return Pair(
                BrotherToolAction.SaveNote("Diary Entry", noteContent),
                listOf(
                    "locked in your private diary $pet 📝",
                    "your secrets are always safe with me."
                )
            )
        }

        // Schedule check
        if (lower.contains("what do i have") || lower.contains("my schedule") || lower.contains("what's today") || lower.contains("calendar")) {
            return Pair(
                BrotherToolAction.QuerySchedule,
                listOf(
                    "pulling up your calendar $pet 📅",
                    "you've got classes and study blocks scheduled today. remember to take breaks too!"
                )
            )
        }

        // Task creation
        if (lower.startsWith("add task") || lower.startsWith("todo") || lower.contains("add to my list")) {
            val title = lower.substringAfter("add task", "")
                .substringAfter("todo", "")
                .substringAfter("add to my list", "")
                .trim()
            val taskTitle = if (title.isEmpty()) "Review study materials" else title.replaceFirstChar { it.uppercase() }
            return Pair(
                BrotherToolAction.CreateTask(taskTitle),
                listOf(
                    "added to your to-do list: \"$taskTitle\" ✅",
                    "tick it off when you finish $pet!"
                )
            )
        }

        return Pair(null, null)
    }

    private fun generateDynamicReply(
        originalMessage: String,
        lower: String,
        relationship: RelationshipType,
        mode: PersonalityMode,
        sisterName: String,
        recentHistory: List<String>
    ): List<String> {
        val isElder = relationship == RelationshipType.ELDER_BROTHER
        val pet = getPetName(relationship, forTease = mode == PersonalityMode.IRRITATIVE)

        // 1. Distress / Comfort
        if (mode == PersonalityMode.COMFORT || lower.contains("sad") || lower.contains("cry") || lower.contains("stressed") || lower.contains("upset")) {
            return generateEmpatheticReplies(lower, sisterName, pet, isElder)
        }

        // 2. Greetings & Check-ins
        if (isGreeting(lower)) {
            return generateGreetingReplies(lower, sisterName, pet, isElder, mode)
        }

        // 3. Asking what brother is doing / where he is
        if (lower.contains("what are you doing") || lower.contains("what r u doing") || lower.contains("where are you") || lower.contains("wyd") || lower.contains("busy?")) {
            return generateActivityReplies(pet, isElder, mode)
        }

        // 4. Food & Cravings
        if (lower.contains("food") || lower.contains("hungry") || lower.contains("eating") || lower.contains("pizza") ||
            lower.contains("maggi") || lower.contains("ice cream") || lower.contains("chocolate") || lower.contains("coffee") ||
            lower.contains("tea") || lower.contains("chai") || lower.contains("biryani") || lower.contains("cook") || lower.contains("snack")) {
            return generateFoodReplies(lower, pet, isElder, mode)
        }

        // 5. Studies, Homework & Exams
        if (lower.contains("study") || lower.contains("exam") || lower.contains("test") || lower.contains("homework") ||
            lower.contains("math") || lower.contains("physics") || lower.contains("chemistry") || lower.contains("biology") ||
            lower.contains("college") || lower.contains("school") || lower.contains("assignment") || lower.contains("grades")) {
            return generateStudyReplies(lower, sisterName, pet, isElder, mode)
        }

        // 6. Sibling Banter, Roasts & Teasing
        if (lower.contains("roast") || lower.contains("ugly") || lower.contains("dumb") || lower.contains("idiot") ||
            lower.contains("loser") || lower.contains("annoying") || lower.contains("hate you") || lower.contains("shut up") ||
            lower.contains("money") || lower.contains("rupees") || lower.contains("borrow") || lower.contains("clothes")) {
            return generateBanterReplies(lower, sisterName, pet, isElder, mode)
        }

        // 7. Parents / Mom & Dad
        if (lower.contains("mom") || lower.contains("dad") || lower.contains("parents") || lower.contains("scolded") || lower.contains("yelled")) {
            return generateParentsReplies(lower, sisterName, pet, isElder, mode)
        }

        // 8. Sister expressing love / affection / gratitude
        if (lower.contains("love you") || lower.contains("best brother") || lower.contains("thank you") || lower.contains("thanks") || lower.contains("grateful")) {
            return generateAffectionReplies(sisterName, pet, isElder, mode)
        }

        // 9. Shopping, Clothes, Looks, Boredom
        if (lower.contains("shopping") || lower.contains("dress") || lower.contains("bought") || lower.contains("bored") ||
            lower.contains("movie") || lower.contains("song") || lower.contains("game") || lower.contains("hair")) {
            return generateLifeUpdateReplies(lower, sisterName, pet, isElder, mode)
        }

        // 10. General Questions ("why", "how", "what", "can you", "should i")
        if (lower.startsWith("why") || lower.startsWith("how") || lower.startsWith("what") || lower.startsWith("should i") || lower.startsWith("can you") || lower.endsWith("?")) {
            return generateQuestionReplies(lower, originalMessage, sisterName, pet, isElder, mode)
        }

        // 11. Dynamic Contextual Fallback — directly acknowledges the sister's input!
        return generateDynamicEchoReply(originalMessage, sisterName, pet, isElder, mode)
    }

    private fun isGreeting(lower: String): Boolean {
        val greetings = listOf("hi", "hey", "hello", "good morning", "good evening", "good night", "gm", "gn", "yo", "wassup", "what's up", "namaste", "hola")
        return greetings.any { lower == it || lower.startsWith("$it ") || lower.endsWith(" $it") }
    }

    private fun generateGreetingReplies(lower: String, sisterName: String, pet: String, isElder: Boolean, mode: PersonalityMode): List<String> {
        val isMorning = lower.contains("morning") || lower.contains("gm")
        val isNight = lower.contains("night") || lower.contains("gn")

        if (isMorning) {
            return if (isElder) {
                when (mode) {
                    PersonalityMode.COMFORT -> listOf("Good morning $sisterName! ☀️", "Did you sleep well? Remember to eat breakfast before you head out ♡")
                    PersonalityMode.IRRITATIVE -> listOf("Morning $pet 🥱", "Surprised you woke up before noon today 😂 Go brush your teeth!")
                    PersonalityMode.FUNNY -> listOf("Look who finally woke up! 🌞", "The sun has been waiting for you since 6am $pet 💀")
                }
            } else {
                listOf("Morning $pet! ☀️", "Did you make breakfast yet? Save some for me please! 😂")
            }
        }

        if (isNight) {
            return if (isElder) {
                when (mode) {
                    PersonalityMode.COMFORT -> listOf("Goodnight $sisterName ♡", "Sleep peaceful. Put your phone away and get good rest 🫂")
                    PersonalityMode.IRRITATIVE -> listOf("Goodnight $pet 😴", "Don't stay up scrolling reels until 3am like you always do 🙄")
                    PersonalityMode.FUNNY -> listOf("Goodnight! Don't let the bed bugs bite 🪲", "Or monsters under your bed... they're scared of your face anyway 😂 jk goodnight!")
                }
            } else {
                listOf("Goodnight $pet! 🌙", "See you tomorrow, sleep well!")
            }
        }

        return if (isElder) {
            when (mode) {
                PersonalityMode.COMFORT -> listOf("Hey $sisterName 👋", "I'm right here. What's on your mind today? ♡")
                PersonalityMode.IRRITATIVE -> listOf("Oye $pet 🙄", "What do you want now? Did you break something again? 😂")
                PersonalityMode.FUNNY -> listOf("Sup $pet! 👋", "Your favorite brother has entered the chat 💅 What's the tea?")
            }
        } else {
            when (mode) {
                PersonalityMode.COMFORT -> listOf("Hey $pet! 👋", "Always happy when you text me. How are you?")
                PersonalityMode.IRRITATIVE -> listOf("Yo $pet! 😂", "You're bothering me while I'm gaming... but okay what happened?")
                PersonalityMode.FUNNY -> listOf("Ayy $pet! 👋", "Are you treating me to snacks or just saying hi? 😂")
            }
        }
    }

    private fun generateEmpatheticReplies(lower: String, sisterName: String, pet: String, isElder: Boolean): List<String> {
        val topic = when {
            lower.contains("failed") || lower.contains("test") || lower.contains("exam") -> "grades"
            lower.contains("friend") || lower.contains("fight") -> "friends"
            lower.contains("tired") || lower.contains("exhausted") -> "tired"
            else -> "general"
        }

        if (isElder) {
            return when (topic) {
                "grades" -> listOf(
                    "Hey, take a deep breath $sisterName 🫂",
                    "One test or grade does NOT define how smart you are. I've failed plenty and look at me!",
                    "We'll review it together, okay? Don't stress yourself over numbers. I'm proud of you always ♡"
                )
                "friends" -> listOf(
                    "Oye, who made you feel like this? Tell me their name right now 😤",
                    "Listen to me $pet: anyone who doesn't appreciate you isn't worth your tears.",
                    "You have a heart of gold. Come talk to me, I'm right here listening 🫂"
                )
                "tired" -> listOf(
                    "You've been pushing yourself way too hard lately $sisterName.",
                    "Drink some cool water, wash your face, and close your eyes for 20 mins.",
                    "The world can wait. Your health and peace come first ♡"
                )
                else -> listOf(
                    "Oye dont cry, im right here $pet 🥺",
                    "Whatever it is, you don't have to carry it alone.",
                    "Tell me everything, I'm listening. Always got your back no matter what 🫂♡"
                )
            }
        } else {
            return listOf(
                "Hey $pet... seeing you sad hurts me too 🥺",
                "I might tease you a lot, but you're the best sister in the world.",
                "Tell me what happened, okay? Want me to beat someone up for you? 😤🫂"
            )
        }
    }

    private fun generateActivityReplies(pet: String, isElder: Boolean, mode: PersonalityMode): List<String> {
        return if (isElder) {
            when (mode) {
                PersonalityMode.COMFORT -> listOf("Just wrapping up some work and thinking of checking in on you!", "I'm always free for my sister. What's up $pet?")
                PersonalityMode.IRRITATIVE -> listOf("Was literally winning my game until your notification popped up 😂", "Just kidding. What's your emergency $pet?")
                PersonalityMode.FUNNY -> listOf("Sitting like a king, contemplating life's mysteries 👑", "And by that I mean watching random cat videos at 1080p 😂 Wyd $pet?")
            }
        } else {
            listOf("Just chilling and procrastinating my assignments 😂", "Why, did you miss me already $pet?")
        }
    }

    private fun generateFoodReplies(lower: String, pet: String, isElder: Boolean, mode: PersonalityMode): List<String> {
        val item = when {
            lower.contains("pizza") -> "pizza 🍕"
            lower.contains("maggi") -> "spicy maggi with cheese 🍜"
            lower.contains("ice cream") -> "ice cream 🍨"
            lower.contains("chocolate") -> "chocolates 🍫"
            lower.contains("biryani") -> "biryani 🍚"
            lower.contains("chai") || lower.contains("tea") -> "hot adrak chai ☕"
            else -> "something delicious"
        }

        return if (isElder) {
            when (mode) {
                PersonalityMode.COMFORT -> listOf(
                    "Craving $item? 😋",
                    "Go grab a plate and treat yourself! You deserve good food after a long day ♡"
                )
                PersonalityMode.IRRITATIVE -> listOf(
                    "You're ALWAYS hungry $pet 🙄",
                    "Your stomach is a black hole scientifically proven by NASA 😂",
                    "Make some for me too or don't eat in front of me!"
                )
                PersonalityMode.FUNNY -> listOf(
                    "Food again?! 💀",
                    "If eating $item was an Olympic sport you'd have 10 gold medals $pet 🥇😂"
                )
            }
        } else {
            listOf(
                "Oooo $item! Order two portions, one is for me! 😋",
                "Don't eat alone $pet, sharing is caring!"
            )
        }
    }

    private fun generateStudyReplies(lower: String, sisterName: String, pet: String, isElder: Boolean, mode: PersonalityMode): List<String> {
        val subject = when {
            lower.contains("math") -> "math"
            lower.contains("physics") -> "physics"
            lower.contains("chemistry") -> "chemistry"
            lower.contains("biology") -> "biology"
            else -> "studies"
        }

        return if (isElder) {
            when (mode) {
                PersonalityMode.COMFORT -> listOf(
                    "Studying $subject? Take it one concept at a time $sisterName 📚",
                    "Don't try to cram everything at once. Use a 25 min timer, then take a 5 min break.",
                    "You're smart and capable. You're going to ace this test ♡"
                )
                PersonalityMode.IRRITATIVE -> listOf(
                    "Wait, you actually opened your $subject book?! 😱",
                    "Mark this historic day on the calendar everyone! 😂",
                    "Focus now $pet, put Instagram away for an hour!"
                )
                PersonalityMode.FUNNY -> listOf(
                    "Sending high-speed brainwaves to boost your IQ by +50 🧠⚡",
                    "If you pass $subject, I'll buy you chocolates. If you fail, I'm telling mom 😂",
                    "Now get to work $pet!"
                )
            }
        } else {
            listOf(
                "Good luck with $subject $pet! 📖",
                "Once you pass, teach me too because I understand nothing in class 😂"
            )
        }
    }

    private fun generateBanterReplies(lower: String, sisterName: String, pet: String, isElder: Boolean, mode: PersonalityMode): List<String> {
        return if (isElder) {
            when (mode) {
                PersonalityMode.IRRITATIVE -> listOf(
                    "Lmao looking in the mirror again $pet? 🪞😂",
                    "You're still 4 foot nothing and complaining 💀",
                    "Cope harder, I'm mom's favorite child anyway 💅"
                )
                PersonalityMode.FUNNY -> listOf(
                    "Bro really woke up today and chose violence 💀",
                    "I will literally unplug the Wi-Fi router don't test me 🤣",
                    "Love you too $pet 😂"
                )
                else -> listOf(
                    "Ayy don't roast your only brother 😂",
                    "You know you'd be lost without me $sisterName ♡"
                )
            }
        } else {
            listOf(
                "Who are you calling dumb? I have a higher GPA than you in Roblox 😂",
                "Give me 500 rs or I'll tell mom what you did yesterday 💀"
            )
        }
    }

    private fun generateParentsReplies(lower: String, sisterName: String, pet: String, isElder: Boolean, mode: PersonalityMode): List<String> {
        return if (isElder) {
            when (mode) {
                PersonalityMode.COMFORT -> listOf(
                    "Did mom or dad get mad at you $pet? 🥺",
                    "Don't take it to heart. Parents sometimes get stressed and let it out on us.",
                    "Give it 30 mins to cool down. Want me to call home and distract them?"
                )
                PersonalityMode.IRRITATIVE -> listOf(
                    "What did you break this time bhondu?! 💀",
                    "I told you not to leave your shoes in the hallway 😂",
                    "I'm innocent, don't drag my name into this!"
                )
                else -> listOf(
                    "Uh oh, red alert in the living room 🚨😂",
                    "Deploy tactical apology: clean the kitchen and look sad. Works every time!"
                )
            }
        } else {
            listOf(
                "Run for your life $pet! 🏃‍♂️💨",
                "Hide in your room until dinner time, that's what I always do 😂"
            )
        }
    }

    private fun generateAffectionReplies(sisterName: String, pet: String, isElder: Boolean, mode: PersonalityMode): List<String> {
        return if (isElder) {
            when (mode) {
                PersonalityMode.COMFORT -> listOf(
                    "Love you more $sisterName 💙",
                    "I'm always your biggest cheerleader, wherever you are.",
                    "Always here for you, always ♡"
                )
                PersonalityMode.IRRITATIVE -> listOf(
                    "Aww so sweet 🥺",
                    "Now what favor do you need? Did you spend all your pocket money? 😂",
                    "Love you too $pet 💙"
                )
                else -> listOf(
                    "Screenshotting this as legal proof of affection 📸😂",
                    "Best sister ever! (Conditions apply) ♡"
                )
            }
        } else {
            listOf(
                "Ayy love you too $pet! 💙",
                "Best sister in the galaxy! Now can I borrow your headphones? 😂"
            )
        }
    }

    private fun generateLifeUpdateReplies(lower: String, sisterName: String, pet: String, isElder: Boolean, mode: PersonalityMode): List<String> {
        return if (isElder) {
            when {
                lower.contains("dress") || lower.contains("bought") || lower.contains("shopping") -> listOf(
                    "Ooooh shopping spree? 🛍️",
                    "Whose card did you swipe $pet? 😂",
                    "Send me a pic though, I'm sure you look great! Best dressed as always ♡"
                )
                lower.contains("bored") -> listOf(
                    "Bored? Go clean your room then 🧹",
                    "Or go for a walk, or finish that book you started 3 months ago $pet 😂"
                )
                else -> listOf(
                    "Nice! Sounds like you're having an eventful day $sisterName.",
                    "Tell me more, what else happened?"
                )
            }
        } else {
            listOf(
                "Did you buy anything for me or did you just waste money on yourself? 😂",
                "Show me show me!"
            )
        }
    }

    private fun generateQuestionReplies(
        lower: String,
        original: String,
        sisterName: String,
        pet: String,
        isElder: Boolean,
        mode: PersonalityMode
    ): List<String> {
        return if (isElder) {
            when (mode) {
                PersonalityMode.COMFORT -> listOf(
                    "That's a good question $sisterName.",
                    "Honestly, trust your gut on this. You're wiser than you give yourself credit for.",
                    "Whatever choice you make, I'm backing you up 100% ♡"
                )
                PersonalityMode.IRRITATIVE -> listOf(
                    "Why are you asking me like I'm Google Wikipedia $pet? 😂",
                    "My professional brotherly advice: do the opposite of your first instinct 💀"
                )
                PersonalityMode.FUNNY -> listOf(
                    "According to brotherly science and quantum mechanics: absolutely yes 😂",
                    "Or maybe no. Flip a coin and see what you secretly hope for!"
                )
            }
        } else {
            listOf(
                "Why are you asking your younger brother for life advice?! 😂",
                "Just eat something good and don't overthink it $pet!"
            )
        }
    }

    private fun generateDynamicEchoReply(
        original: String,
        sisterName: String,
        pet: String,
        isElder: Boolean,
        mode: PersonalityMode
    ): List<String> {
        // Extract a key phrase from the user's sentence to weave directly into the reply
        val words = original.split(" ").filter { it.length > 3 }
        val keyword = if (words.isNotEmpty()) words.random().replace(Regex("[^a-zA-Z0-9]"), "") else "that"

        return if (isElder) {
            when (mode) {
                PersonalityMode.COMFORT -> listOf(
                    "I hear you $sisterName ♡",
                    "Tell me more about $keyword, I'm all ears.",
                    "You can always talk to me freely, no judgment ever 🫂"
                )
                PersonalityMode.IRRITATIVE -> listOf(
                    "Wait, what about \"$keyword\"? 😂",
                    "You're being so dramatic today $pet 💀",
                    "Explain properly, don't leave me hanging!"
                )
                PersonalityMode.FUNNY -> listOf(
                    "Lmaooo not \"$keyword\" 😭",
                    "Only you could come up with this $pet 🤣",
                    "What's the full story? Spill the tea!"
                )
            }
        } else {
            listOf(
                "Wait, tell me more about $keyword! 😂",
                "I'm listening $pet, what happened next?"
            )
        }
    }

    private fun getPetName(relationship: RelationshipType, forTease: Boolean): String {
        return if (relationship == RelationshipType.ELDER_BROTHER) {
            if (forTease) {
                listOf("bauni", "bhondu", "chelli", "kiddo", "bro").random()
            } else {
                listOf("chelli", "behen", "kiddo", "bro").random()
            }
        } else {
            if (forTease) {
                listOf("bauni", "bhondu", "akka", "didi", "bro").random()
            } else {
                listOf("akka", "didi", "behen", "big sis").random()
            }
        }
    }
}

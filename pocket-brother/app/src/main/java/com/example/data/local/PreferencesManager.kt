package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.PersonalityMode
import com.example.data.model.RelationshipType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("pocket_brother_prefs", Context.MODE_PRIVATE)

    private val _sisterName = MutableStateFlow(
        prefs.getString(KEY_SISTER_NAME, "Aaradhya") ?: "Aaradhya"
    )
    val sisterName: StateFlow<String> = _sisterName.asStateFlow()

    private val _relationship = MutableStateFlow(
        try {
            RelationshipType.valueOf(
                prefs.getString(KEY_RELATIONSHIP, RelationshipType.ELDER_BROTHER.name)
                    ?: RelationshipType.ELDER_BROTHER.name
            )
        } catch (_: Exception) {
            RelationshipType.ELDER_BROTHER
        }
    )
    val relationship: StateFlow<RelationshipType> = _relationship.asStateFlow()

    private val _currentMode = MutableStateFlow(
        try {
            PersonalityMode.valueOf(
                prefs.getString(KEY_CURRENT_MODE, PersonalityMode.COMFORT.name)
                    ?: PersonalityMode.COMFORT.name
            )
        } catch (_: Exception) {
            PersonalityMode.COMFORT
        }
    )
    val currentMode: StateFlow<PersonalityMode> = _currentMode.asStateFlow()

    private val _isOnboarded = MutableStateFlow(
        prefs.getBoolean(KEY_IS_ONBOARDED, true)
    )
    val isOnboarded: StateFlow<Boolean> = _isOnboarded.asStateFlow()

    private val _themeMode = MutableStateFlow(
        prefs.getString(KEY_THEME_MODE, "DARK") ?: "DARK"
    )
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _sisterAvatar = MutableStateFlow(
        prefs.getString(KEY_SISTER_AVATAR, "👧") ?: "👧"
    )
    val sisterAvatar: StateFlow<String> = _sisterAvatar.asStateFlow()

    private val _preferredGreeting = MutableStateFlow(
        prefs.getString(KEY_GREETING, "Good morning bhaiya") ?: "Good morning bhaiya"
    )
    val preferredGreeting: StateFlow<String> = _preferredGreeting.asStateFlow()

    private val _preferredLanguage = MutableStateFlow(
        prefs.getString(KEY_LANGUAGE, "English") ?: "English"
    )
    val preferredLanguage: StateFlow<String> = _preferredLanguage.asStateFlow()

    private val _weatherCity = MutableStateFlow(
        prefs.getString(KEY_WEATHER_CITY, "Bengaluru") ?: "Bengaluru"
    )
    val weatherCity: StateFlow<String> = _weatherCity.asStateFlow()

    private val _recentEmojis = MutableStateFlow(
        prefs.getString(KEY_RECENT_EMOJIS, "😊,😂,🥺,🫂,❤️,✨,👍,🍕")
            ?.split(",")?.filter { it.isNotBlank() } ?: listOf("😊", "😂", "🥺", "🫂", "❤️", "✨", "👍", "🍕")
    )
    val recentEmojis: StateFlow<List<String>> = _recentEmojis.asStateFlow()

    fun setSisterName(name: String) {
        val clean = name.trim().ifEmpty { "Aaradhya" }
        prefs.edit().putString(KEY_SISTER_NAME, clean).apply()
        _sisterName.value = clean
    }

    fun setRelationship(rel: RelationshipType) {
        prefs.edit().putString(KEY_RELATIONSHIP, rel.name).apply()
        _relationship.value = rel
    }

    fun setCurrentMode(mode: PersonalityMode) {
        prefs.edit().putString(KEY_CURRENT_MODE, mode.name).apply()
        _currentMode.value = mode
    }

    fun setOnboarded(onboarded: Boolean) {
        prefs.edit().putBoolean(KEY_IS_ONBOARDED, onboarded).apply()
        _isOnboarded.value = onboarded
    }

    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME_MODE, mode).apply()
        _themeMode.value = mode
    }

    fun setSisterAvatar(avatar: String) {
        prefs.edit().putString(KEY_SISTER_AVATAR, avatar).apply()
        _sisterAvatar.value = avatar
    }

    fun setPreferredGreeting(greeting: String) {
        prefs.edit().putString(KEY_GREETING, greeting).apply()
        _preferredGreeting.value = greeting
    }

    fun setPreferredLanguage(lang: String) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
        _preferredLanguage.value = lang
    }

    private val _activeEngine = MutableStateFlow(
        prefs.getString(KEY_ACTIVE_ENGINE, "BUILTIN_OFFLINE_CORE") ?: "BUILTIN_OFFLINE_CORE"
    )
    val activeEngine: StateFlow<String> = _activeEngine.asStateFlow()

    private val _selectedTier = MutableStateFlow(
        prefs.getString(KEY_SELECTED_TIER, "gemma_2b_balanced") ?: "gemma_2b_balanced"
    )
    val selectedTier: StateFlow<String> = _selectedTier.asStateFlow()

    fun setActiveEngine(engine: String) {
        prefs.edit().putString(KEY_ACTIVE_ENGINE, engine).apply()
        _activeEngine.value = engine
    }

    fun setSelectedTier(tierId: String) {
        prefs.edit().putString(KEY_SELECTED_TIER, tierId).apply()
        _selectedTier.value = tierId
    }

    fun setWeatherCity(city: String) {
        prefs.edit().putString(KEY_WEATHER_CITY, city).apply()
        _weatherCity.value = city
    }

    fun addRecentEmoji(emoji: String) {
        val current = _recentEmojis.value.toMutableList()
        current.remove(emoji)
        current.add(0, emoji)
        val trimmed = current.take(24)
        prefs.edit().putString(KEY_RECENT_EMOJIS, trimmed.joinToString(",")).apply()
        _recentEmojis.value = trimmed
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
        _sisterName.value = "Aaradhya"
        _relationship.value = RelationshipType.ELDER_BROTHER
        _currentMode.value = PersonalityMode.COMFORT
        _isOnboarded.value = false
        _themeMode.value = "DARK"
        _sisterAvatar.value = "👧"
        _preferredGreeting.value = "Good morning bhaiya"
        _preferredLanguage.value = "English"
        _weatherCity.value = "Bengaluru"
        _activeEngine.value = "BUILTIN_OFFLINE_CORE"
        _selectedTier.value = "gemma_2b_balanced"
    }

    companion object {
        private const val KEY_SISTER_NAME = "sister_name"
        private const val KEY_RELATIONSHIP = "relationship"
        private const val KEY_CURRENT_MODE = "current_mode"
        private const val KEY_IS_ONBOARDED = "is_onboarded"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_SISTER_AVATAR = "sister_avatar"
        private const val KEY_GREETING = "preferred_greeting"
        private const val KEY_LANGUAGE = "preferred_language"
        private const val KEY_WEATHER_CITY = "weather_city"
        private const val KEY_RECENT_EMOJIS = "recent_emojis"
        private const val KEY_ACTIVE_ENGINE = "active_engine"
        private const val KEY_SELECTED_TIER = "selected_tier"
    }
}

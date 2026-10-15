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
        prefs.getBoolean(KEY_IS_ONBOARDED, true) // Defaults true for instant preview, can be tested anytime
    )
    val isOnboarded: StateFlow<Boolean> = _isOnboarded.asStateFlow()

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

    fun clearAllData() {
        prefs.edit().clear().apply()
        _sisterName.value = "Aaradhya"
        _relationship.value = RelationshipType.ELDER_BROTHER
        _currentMode.value = PersonalityMode.COMFORT
        _isOnboarded.value = false
    }

    companion object {
        private const val KEY_SISTER_NAME = "sister_name"
        private const val KEY_RELATIONSHIP = "relationship"
        private const val KEY_CURRENT_MODE = "current_mode"
        private const val KEY_IS_ONBOARDED = "is_onboarded"
    }
}

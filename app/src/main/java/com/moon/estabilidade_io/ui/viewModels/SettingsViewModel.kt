package com.moon.estabilidade_io.ui.viewModels

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.moon.estabilidade_io.drawer.DrawingPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsActivityState())
    val uiState = _uiState.asStateFlow()

    fun cleearDataStore(context: Context) { CoroutineScope(Dispatchers.IO).launch {
        context.dataStore.edit { it.clear() }
        (context as Activity).finishAndRemoveTask()
        exitProcess(0)
    }}

    fun showEdgesToggle() = _uiState.update { it.copy(showEdges = !it.showEdges) }

    fun useRollerBToggle() = _uiState.update { it.copy(useRollerB = !it.useRollerB) }

    /** Updates the Preferences Singletons and the Preferences DataStore from the actual state*/
    fun savePreferences(context: Context) {
        CoroutineScope(Dispatchers.IO).launch { context.dataStore.edit { settings ->
            settings[PreferencesKeys.showEdges] = _uiState.value.showEdges
            settings[PreferencesKeys.useRollerB] = _uiState.value.useRollerB
        } }
        DrawingPreferences.updateFromSettingsState(_uiState.value)
    }
}

data class SettingsActivityState (
    val showEdges: Boolean = DrawingPreferences.showEdges,
    val useRollerB: Boolean = DrawingPreferences.useRollerB
)

object PreferencesKeys {
    val showEdges = booleanPreferencesKey("show_edges")
    val useRollerB = booleanPreferencesKey("use_roller_b")

    /** Transforms the preferences flow into a SettingsActivityStateFlow (at any time) */
    private fun getPreferencesFlow(context: Context): Flow<SettingsActivityState> =
        context.dataStore.data.map { settings ->
            SettingsActivityState(
                settings[showEdges] ?: DrawingPreferences.showEdges,
                settings[useRollerB] ?: DrawingPreferences.useRollerB,
            )
        }

    /** Collects and sets the preferences flow to the Preferences Singletons */
    @Composable
    fun InitPreferences() {
        DrawingPreferences
            .updateFromSettingsState(
                getPreferencesFlow(LocalContext.current)
                    .collectAsState(initial = SettingsActivityState()).value
            )
    }
}
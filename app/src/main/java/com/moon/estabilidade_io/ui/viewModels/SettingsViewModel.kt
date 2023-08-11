package com.moon.estabilidade_io.ui.viewModels

import androidx.lifecycle.ViewModel
import com.moon.estabilidade_io.drawer.DrawingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsActivityState())
    val uiState = _uiState.asStateFlow()

    fun showEdgesToggle() = _uiState.update { it.copy(showEdges = !it.showEdges) }

    fun useRollerBToggle() = _uiState.update { it.copy(useRollerB = !it.useRollerB) }

    fun savePreferences() {
        DrawingPreferences.updateFromSettingsState(_uiState.value)
    }
}

data class SettingsActivityState (
    val showEdges: Boolean = DrawingPreferences.showEdges,
    val useRollerB: Boolean = DrawingPreferences.useRollerB
)
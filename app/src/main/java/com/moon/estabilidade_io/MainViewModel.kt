package com.moon.estabilidade_io

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.moon.estabilidade_io.drawer.DiagramType
import com.moon.kstability.Structure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainActivityState())
    val uiState = _uiState.asStateFlow()

    var yamlValue by mutableStateOf(defaultSampleStructureYaml)

    fun setFramedStructure(structure: Structure) = _uiState
        .update { MainActivityState(structure, it.diagramType) }

    fun setDiagramType(diagramType: DiagramType) = _uiState
        .update { MainActivityState(it.framedStructure, diagramType) }
}

data class MainActivityState(
    val framedStructure: Structure = Structure("Nenhuma estrutura"),  // todo: open last structure
    val diagramType: DiagramType = DiagramType.NONE,
)

const val defaultSampleStructureYaml = """
   projeto: Nome
   nos:
      A: [0, 0]
      B: [1, 0]
      C: [2, 0]

   apoios:
      A:
         genero: 1
         direcao: vertical
      C:
         genero: 2
         direcao: vertical
   barras:
      - [A, C]

   cargas:
      F1:
         no: A
         direcao: vertical
    
"""
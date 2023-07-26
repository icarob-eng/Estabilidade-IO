package com.moon.estabilidade_io

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.moon.estabilidade_io.drawer.DiagramData
import com.moon.estabilidade_io.drawer.DiagramType
import com.moon.kstability.Parsing
import com.moon.kstability.Structure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainActivityState())
    val uiState = _uiState.asStateFlow()

    var yamlValue by mutableStateOf(defaultSampleStructureYaml)

    fun setDiagramType(diagramType: DiagramType) {
        if (_uiState.value.diagramData != null ) _uiState
            .update { it.copy(diagramData = DiagramData(it.diagramData!!.structure, diagramType)) }
    }

    fun parseYamlValue() = _uiState
        .update { it.copy(diagramData = DiagramData (
            structure = Parsing.parseYamlString(yamlValue),
            diagramType = it.diagramData?.diagramType?: DiagramType.NONE
        ),
            yamlHash = yamlValue.hashCode()
        )
        }

    fun restoreYamlValue() {
        yamlValue = Parsing.serializeStructureToYaml(
            _uiState.value.diagramData?.structure?: Structure("Nenhuma estrutura")
        )
    }
}

data class MainActivityState(
    val diagramData: DiagramData? = null,
    val yamlHash: Int = 0  // force to update state hash
)

const val defaultSampleStructureYaml = """estrutura: Estrutura exemplo
nós:
  A: [0, 0]
  B: [1, 0]
  C: [2, 0]

apoios:
  A:
    gênero: 1
    direção: vertical
  C:
    gênero: 2
    direção: vertical
barras:
  - [A, C]

cargas:
  F1:
    nó: B
    direção: vertical
    módulo: -10
"""
package com.testfiles.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Representa um par de itens a ser comparado
data class ItemPair(val first: String, val second: String)

// Nova classe para representar os itens do ranking com sua pontuação
data class RankedItem(val name: String, val score: Int)

class SharedViewModel : ViewModel() {
    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri.asStateFlow()

    private val _processedData = MutableStateFlow<String?>(null)
    val processedData: StateFlow<String?> = _processedData.asStateFlow()

    // Lista de itens extraídos de processedData
    val itemList: StateFlow<List<String>> = processedData
        .map { data ->
            data?.split("\n")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    // Geração dos pares únicos de comparação
    val itemPairs: StateFlow<List<ItemPair>> = itemList
        .map { items ->
            val pairs = mutableListOf<ItemPair>()
            for (i in items.indices) {
                for (j in i + 1 until items.size) {
                    pairs.add(ItemPair(items[i], items[j]))
                }
            }
            pairs
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    // Estado do ranking Condorcet (resultado final)
    private val _ranking = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val ranking: StateFlow<List<Pair<String, Int>>> = _ranking.asStateFlow()

    // Lista ordenada para a tela de ranking (agora com pontuação)
    var rankedItems by mutableStateOf<List<RankedItem>>(emptyList())
        private set

    // Respostas do usuário durante as comparações
    private val userResponses = mutableListOf<Int?>()

    fun selectFile(uri: Uri) {
        _selectedFileUri.value = uri
    }

    fun processData(data: String) {
        viewModelScope.launch {
            _processedData.value = data
        }
    }

    // Adiciona uma resposta do usuário
    fun addResponse(response: Int?) {
        userResponses.add(response)
    }

    // Calcula o ranking final
    fun finishComparing() {
        calcularRankingCondorcet(userResponses)
        rankedItems = _ranking.value
            .sortedByDescending { it.second }
            .map { RankedItem(it.first, it.second) } // Convertendo para RankedItem
    }

    // Lógica de ranking baseada nas respostas do usuário
    private fun calcularRankingCondorcet(respostas: List<Int?>) {
        val placar = mutableMapOf<String, Int>()
        val pares = itemPairs.value

        // Inicializa o placar com todos os itens
        itemList.value.forEach { item ->
            placar[item] = 0
        }

        for ((index, resposta) in respostas.withIndex()) {
            if (index >= pares.size) break

            val (a, b) = pares[index]
            when (resposta) {
                -1 -> { // A vence
                    placar[a] = (placar[a] ?: 0) + 1
                }
                0 -> { // Empate
                    placar[a] = (placar[a] ?: 0) + 1
                    placar[b] = (placar[b] ?: 0) + 1
                }
                1 -> { // B vence
                    placar[b] = (placar[b] ?: 0) + 1
                }
                null -> { // Não contabiliza
                    continue
                }
            }
        }

        _ranking.value = placar.entries
            .sortedByDescending { it.value }
            .map { it.key to it.value }
    }

    // Limpa os dados para uma nova comparação
    fun resetComparison() {
        userResponses.clear()
        _ranking.value = emptyList()
        rankedItems = emptyList()
    }
}
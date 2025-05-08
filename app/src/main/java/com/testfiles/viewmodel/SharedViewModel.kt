package com.testfiles.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ✅ Representa um par de itens a ser comparado
data class ItemPair(val first: String, val second: String)

class SharedViewModel : ViewModel() {
    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri.asStateFlow()

    private val _processedData = MutableStateFlow<String?>(null)
    val processedData: StateFlow<String?> = _processedData.asStateFlow()

    // ✅ Lista de itens extraídos de processedData
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

    // ✅ Geração dos pares únicos de comparação
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

    // ✅ Estado do ranking Condorcet (resultado final)
    private val _ranking = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val ranking: StateFlow<List<Pair<String, Int>>> = _ranking.asStateFlow()

    fun selectFile(uri: Uri) {
        _selectedFileUri.value = uri
    }

    fun processData(data: String) {
        viewModelScope.launch {
            _processedData.value = data
        }
    }

    // ✅ Lógica de ranking baseada nas respostas do usuário
    fun calcularRankingCondorcet(respostas: List<Int?>) {
        val placar = mutableMapOf<String, Int>()
        val pares = itemPairs.value

        for ((index, resposta) in respostas.withIndex()) {
            val (a, b) = pares[index]
            when (resposta) {
                -1 -> { // A vence
                    placar[a] = (placar[a] ?: 0) + 1
                    placar[b] = placar[b] ?: 0
                }
                0 -> { // Empate
                    placar[a] = (placar[a] ?: 0) + 1
                    placar[b] = (placar[b] ?: 0) + 1
                }
                1 -> { // B vence
                    placar[a] = placar[a] ?: 0
                    placar[b] = (placar[b] ?: 0) + 1
                }
            }
        }

        _ranking.value = placar.entries
            .sortedByDescending { it.value }
            .map { it.toPair() }
    }
}

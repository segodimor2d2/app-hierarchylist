package com.testfiles.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter

// ✅ Representa um par de itens a ser comparado
data class ItemPair(val first: String, val second: String)

class SharedViewModel : ViewModel() {
    // Estado do arquivo selecionado
    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri.asStateFlow()

    // Estado dos dados processados
    private val _processedData = MutableStateFlow<String?>(null)
    val processedData: StateFlow<String?> = _processedData.asStateFlow()

    // Estado de mensagens
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    // Estado de loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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

    // Nova função para salvar o ranking no arquivo
    fun saveRankingToFile(context: Context, uri: Uri, ranking: List<Pair<String, Int>>) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    OutputStreamWriter(outputStream).use { writer ->
                        val rankingContent = buildString {
                            append("# hierarchylist\n")
                            ranking.forEachIndexed { index, (item, score) ->
                                append("${index + 1}. $item (${score}pts)\n")
                            }
                        }
                        writer.write(rankingContent)
                        _message.value = "Ranking salvo com sucesso!"
                    }
                } ?: run {
                    _message.value = "Erro: Não foi possível abrir o arquivo para escrita"
                }
            } catch (e: Exception) {
                _message.value = "Erro ao salvar arquivo: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Função para ler conteúdo do arquivo
    fun readFileContent(context: Context, uri: Uri) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    val content = input.bufferedReader().readText()
                    _processedData.value = content
                    _message.value = "Arquivo carregado com sucesso"
                } ?: run {
                    _message.value = "Erro ao abrir arquivo"
                }
            } catch (e: Exception) {
                _message.value = "Erro ao ler arquivo: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Limpar mensagens
    fun clearMessage() {
        _message.value = null
    }
}
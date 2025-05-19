package com.testfiles.viewmodel

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Representa um par de itens a ser comparado no ranking
data class ItemPair(val first: String, val second: String)

class SharedViewModel : ViewModel() {

    // URI do arquivo selecionado pelo usuário
    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri.asStateFlow()

    // Conteúdo processado do arquivo selecionado
    private val _processedData = MutableStateFlow<String?>(null)
    val processedData: StateFlow<String?> = _processedData.asStateFlow()

    // Mensagens para a UI (erros, status, etc)
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    // Estado de carregamento para mostrar progress indicators
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Lista de itens extraídos do conteúdo processado, uma linha por item
    val itemList: StateFlow<List<String>> = processedData
        .map { data ->
            data?.lines()
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    // Lista dos pares únicos para comparação Condorcet
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

    // Ranking final: pares de item e pontuação ordenados
    private val _ranking = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val ranking: StateFlow<List<Pair<String, Int>>> = _ranking.asStateFlow()

    // Seleciona arquivo (URI)
    fun selectFile(uri: Uri) {
        _selectedFileUri.value = uri
    }

    // Atualiza o conteúdo processado
    fun processData(data: String) {
        viewModelScope.launch {
            _processedData.value = data
        }
    }

    /**
     * Calcula o ranking Condorcet baseado nas respostas do usuário.
     * respostas: lista com valores -1 (primeiro vence), 0 (empate), 1 (segundo vence)
     */
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

    /**
     * Cria uma cópia do arquivo com prefixo "done_" no mesmo diretório com o ranking formatado.
     */
    fun createDoneFileCopy(context: Context, originalUri: Uri, ranking: List<Pair<String, Int>>) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Obtém a Uri da pasta pai para salvar o arquivo
                val treeUri = getTreeUriFromDocumentUri(originalUri)
                val treeFolder = DocumentFile.fromTreeUri(context, treeUri)
                    ?: throw Exception("Não foi possível acessar a pasta do arquivo")

                // Nome do arquivo original para prefixar com "done_"
                val originalName = originalUri.lastPathSegment
                    ?.substringAfterLast("%3A")
                    ?.substringAfterLast("%2F")
                    ?.substringAfterLast("/")
                    ?.replace("%20", " ")
                    ?: "ranking_${System.currentTimeMillis()}.md"

                val newName = "done_$originalName"

                // Remove arquivo antigo se existir
                treeFolder.findFile(newName)?.delete()

                // Cria novo arquivo
                val newFile = treeFolder.createFile("text/markdown", newName)
                    ?: throw Exception("Falha ao criar o arquivo")

                // Escreve conteúdo formatado no novo arquivo
                context.contentResolver.openOutputStream(newFile.uri)?.use { output ->
                    val content = buildString {
                        append("# Ranking Concluído\n\n")
                        ranking.forEachIndexed { index, (item, score) ->
                            append("${index + 1}. $item ($score pts)\n")
                        }
                    }
                    output.write(content.toByteArray())
                    _message.value = "Arquivo salvo como $newName"
                }
            } catch (e: Exception) {
                _message.value = "Erro ao criar cópia: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Função auxiliar para extrair a URI da pasta pai (tree Uri) a partir de um DocumentUri.
     * Pode ser necessária para manipular arquivos com Storage Access Framework.
     */
    private fun getTreeUriFromDocumentUri(documentUri: Uri): Uri {
        val uriString = documentUri.toString()
        return if (uriString.contains("/tree/")) {
            Uri.parse(uriString.substringBefore("/document/"))
        } else {
            documentUri
        }
    }

    /**
     * Salva o ranking no arquivo, criando uma cópia "done_".
     */
    fun saveRanking(context: Context, ranking: List<Pair<String, Int>>) {
        val uri = _selectedFileUri.value
        if (uri == null) {
            _message.value = "Nenhum arquivo selecionado para salvar"
            return
        }
        createDoneFileCopy(context, uri, ranking)
    }

    /**
     * Lê o conteúdo do arquivo selecionado.
     */
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

    // Limpa as mensagens da UI
    fun clearMessage() {
        _message.value = null
    }
}

package com.testfiles.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    // URI do arquivo selecionado (já existente)
    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri.asStateFlow()

    // Novo: Dados processados para a tela HierarchyProcessScreen
    private val _processedData = MutableStateFlow<String?>(null)
    val processedData: StateFlow<String?> = _processedData.asStateFlow()

    fun selectFile(uri: Uri) {
        _selectedFileUri.value = uri
    }

    // Novo: Processa os dados (conteúdo do arquivo) e armazena no StateFlow
    fun processData(data: String) {
        viewModelScope.launch {
            _processedData.value = data // Ou adicione lógica de análise aqui
        }
    }
}

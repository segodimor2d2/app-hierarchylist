package com.testfiles

import com.testfiles.viewmodel.SharedViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedViewModelTest {
    @Test
    fun canSwitchFromMbcToAhp() {
        val viewModel = SharedViewModel()
        viewModel.selecionarMetodo("mbc")
        assertEquals("mbc", viewModel.varMetodo)
        viewModel.selecionarMetodo("ahp")
        assertEquals("ahp", viewModel.varMetodo)
    }
}

package com.testfiles

import com.testfiles.util.calculateAhpWeights
import org.junit.Assert.assertEquals
import org.junit.Test

class AhpCalculatorTest {
    @Test fun equalJudgmentsHaveEqualWeights() {
        calculateAhpWeights(3, listOf(1.0, 1.0, 1.0)).forEach {
            assertEquals(1.0 / 3, it, 1e-9)
        }
    }

    @Test fun consistentMatrixRecoversRelativeWeights() {
        val weights = calculateAhpWeights(3, listOf(2.0, 6.0, 3.0))
        listOf(0.6, 0.3, 0.1).zip(weights).forEach { (expected, actual) ->
            assertEquals(expected, actual, 1e-9)
        }
    }

    @Test fun reciprocalJudgmentReversesPreference() {
        val weights = calculateAhpWeights(2, listOf(1.0 / 9))
        assertEquals(0.1, weights[0], 1e-9)
        assertEquals(0.9, weights[1], 1e-9)
    }

    @Test(expected = IllegalArgumentException::class)
    fun incompleteJudgmentsAreRejected() {
        calculateAhpWeights(3, listOf(1.0))
    }
}

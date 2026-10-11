package com.testfiles

import com.testfiles.ui.screen.ahpSelectionAt
import org.junit.Assert.assertEquals
import org.junit.Test

class AhpTouchAreaTest {
    @Test fun verticalBandsUseOnlyFiveIntensitiesFromTopToBottom() {
        listOf(9, 7, 5, 3, 1).forEachIndexed { band, expected ->
            val selection = ahpSelectionAt(50f, band * 60f + 30f, 300f, 300f)
            assertEquals(expected, selection.intensity)
            assertEquals(expected.toDouble(), selection.ratio, 1e-9)
        }
    }

    @Test fun middleAlwaysMeansEquality() {
        listOf(0f, 100f, 200f, 300f).forEach { y ->
            val selection = ahpSelectionAt(150f, y, 300f, 300f)
            assertEquals(0, selection.side)
            assertEquals(1, selection.intensity)
            assertEquals(1.0, selection.ratio, 1e-9)
        }
    }

    @Test fun rightSideUsesReciprocalRatio() {
        assertEquals(1.0 / 9, ahpSelectionAt(250f, 0f, 300f, 300f).ratio, 1e-9)
        assertEquals(1.0, ahpSelectionAt(250f, 300f, 300f, 300f).ratio, 1e-9)
    }

    @Test fun boundariesAndOutsidePositionsSelectTheNearestBand() {
        assertEquals(7, ahpSelectionAt(0f, 60f, 300f, 300f).intensity)
        assertEquals(9, ahpSelectionAt(-10f, -10f, 300f, 300f).intensity)
        assertEquals(1, ahpSelectionAt(310f, 310f, 300f, 300f).intensity)
        assertEquals(-1, ahpSelectionAt(119f, 0f, 300f, 300f).side)
        assertEquals(0, ahpSelectionAt(120f, 0f, 300f, 300f).side)
        assertEquals(0, ahpSelectionAt(179f, 0f, 300f, 300f).side)
        assertEquals(1, ahpSelectionAt(180f, 0f, 300f, 300f).side)
    }
}

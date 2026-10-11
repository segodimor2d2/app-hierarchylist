package com.testfiles

import com.testfiles.ui.screen.mbcAnswerAt
import org.junit.Assert.assertEquals
import org.junit.Test

class MbcTouchAreaTest {
    @Test fun positionUsesFortyTwentyFortyZones() {
        assertEquals(-1, mbcAnswerAt(0f, 300f))
        assertEquals(-1, mbcAnswerAt(119f, 300f))
        assertEquals(0, mbcAnswerAt(120f, 300f))
        assertEquals(0, mbcAnswerAt(179f, 300f))
        assertEquals(1, mbcAnswerAt(180f, 300f))
        assertEquals(1, mbcAnswerAt(300f, 300f))
    }

    @Test fun leavingTheRectangleKeepsTheNearestSide() {
        assertEquals(-1, mbcAnswerAt(-20f, 300f))
        assertEquals(1, mbcAnswerAt(320f, 300f))
    }
}

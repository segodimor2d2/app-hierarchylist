package com.testfiles

import com.testfiles.ui.screen.mbcAnswerAt
import org.junit.Assert.assertEquals
import org.junit.Test

class MbcTouchAreaTest {
    @Test fun positionSelectsTheCorrespondingThird() {
        assertEquals(-1, mbcAnswerAt(0f, 300f))
        assertEquals(-1, mbcAnswerAt(99f, 300f))
        assertEquals(0, mbcAnswerAt(100f, 300f))
        assertEquals(0, mbcAnswerAt(199f, 300f))
        assertEquals(1, mbcAnswerAt(200f, 300f))
        assertEquals(1, mbcAnswerAt(300f, 300f))
    }

    @Test fun leavingTheRectangleKeepsTheNearestSide() {
        assertEquals(-1, mbcAnswerAt(-20f, 300f))
        assertEquals(1, mbcAnswerAt(320f, 300f))
    }
}

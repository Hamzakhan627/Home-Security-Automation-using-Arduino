package com.example.jobexpensemanager

import com.example.jobexpensemanager.util.DateRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class DateRulesTest {

    @Test
    fun dayNumber_fromStart_returnsOneBasedCount() {
        val start = LocalDate.of(2026, 1, 10)
        val selected = LocalDate.of(2026, 1, 12)

        assertEquals(3, DateRules.dayNumberFromStart(start, selected))
    }

    @Test
    fun entryDate_mustBeOnOrAfterStartAndToday() {
        val today = LocalDate.of(2026, 1, 10)
        val start = LocalDate.of(2026, 1, 12)

        assertFalse(DateRules.isValidEntryDate(start, LocalDate.of(2026, 1, 11), today))
        assertTrue(DateRules.isValidEntryDate(start, LocalDate.of(2026, 1, 12), today))
    }
}

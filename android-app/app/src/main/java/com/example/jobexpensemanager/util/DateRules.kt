package com.example.jobexpensemanager.util

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object DateRules {

    fun isValidJobStartDate(selectedDate: LocalDate, today: LocalDate = LocalDate.now()): Boolean {
        return !selectedDate.isBefore(today)
    }

    fun isValidEntryDate(startDate: LocalDate, selectedDate: LocalDate, today: LocalDate = LocalDate.now()): Boolean {
        return !selectedDate.isBefore(startDate) && !selectedDate.isBefore(today)
    }

    fun dayNumberFromStart(startDate: LocalDate, selectedDate: LocalDate): Int {
        require(!selectedDate.isBefore(startDate)) { "Selected date cannot be before start date." }
        return ChronoUnit.DAYS.between(startDate, selectedDate).toInt() + 1
    }
}

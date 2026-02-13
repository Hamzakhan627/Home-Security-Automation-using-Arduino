package com.example.jobexpensemanager.domain

import com.example.jobexpensemanager.data.CreditEntity
import com.example.jobexpensemanager.data.DailyExpenseSummary
import com.example.jobexpensemanager.data.ExpenseEntity
import com.example.jobexpensemanager.data.JobEntity

data class JobDashboard(
    val job: JobEntity,
    val expenses: List<ExpenseEntity>,
    val credits: List<CreditEntity>,
    val dailyExpenseSummary: List<DailyExpenseSummary>,
    val totalExpenses: Double,
    val totalCredits: Double
) {
    val remainingAmount: Double = job.totalAmount - totalExpenses
}

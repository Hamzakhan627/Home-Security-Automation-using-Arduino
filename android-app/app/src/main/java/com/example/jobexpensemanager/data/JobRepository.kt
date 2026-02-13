package com.example.jobexpensemanager.data

import com.example.jobexpensemanager.domain.JobDashboard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class JobRepository(private val dao: JobExpenseDao) {

    fun observeJobs(): Flow<List<JobEntity>> = dao.observeJobs()

    fun observeJobDashboard(jobId: Long): Flow<JobDashboard?> {
        return combine(
            dao.observeJob(jobId),
            dao.observeExpenses(jobId),
            dao.observeCredits(jobId),
            dao.observeDailyExpenseSummary(jobId),
            dao.observeTotalExpenses(jobId),
            dao.observeTotalCredits(jobId)
        ) { job, expenses, credits, daily, totalExpenses, totalCredits ->
            job?.let {
                JobDashboard(
                    job = it,
                    expenses = expenses,
                    credits = credits,
                    dailyExpenseSummary = daily,
                    totalExpenses = totalExpenses,
                    totalCredits = totalCredits
                )
            }
        }
    }

    suspend fun createJob(name: String, totalAmount: Double, startDateEpochDay: Long): Long {
        return dao.insertJob(
            JobEntity(
                name = name,
                totalAmount = totalAmount,
                startDateEpochDay = startDateEpochDay
            )
        )
    }

    suspend fun addExpense(jobId: Long, name: String, amount: Double, dateEpochDay: Long) {
        dao.insertExpense(ExpenseEntity(jobId = jobId, name = name, amount = amount, dateEpochDay = dateEpochDay))
    }

    suspend fun updateExpense(expense: ExpenseEntity) = dao.updateExpense(expense)

    suspend fun deleteExpense(expense: ExpenseEntity) = dao.deleteExpense(expense)

    suspend fun addCredit(jobId: Long, sourceName: String, amount: Double) {
        dao.insertCredit(CreditEntity(jobId = jobId, sourceName = sourceName, amount = amount))
    }

    suspend fun updateCredit(credit: CreditEntity) = dao.updateCredit(credit)

    suspend fun deleteCredit(credit: CreditEntity) = dao.deleteCredit(credit)
}

package com.example.jobexpensemanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JobExpenseDao {

    @Insert
    suspend fun insertJob(job: JobEntity): Long

    @Update
    suspend fun updateJob(job: JobEntity)

    @Query("SELECT * FROM jobs ORDER BY createdAtMillis DESC")
    fun observeJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE id = :jobId")
    fun observeJob(jobId: Long): Flow<JobEntity?>

    @Insert
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE jobId = :jobId ORDER BY dateEpochDay DESC, id DESC")
    fun observeExpenses(jobId: Long): Flow<List<ExpenseEntity>>

    @Query(
        """
        SELECT dateEpochDay, COALESCE(SUM(amount), 0.0) AS totalExpense
        FROM expenses
        WHERE jobId = :jobId
        GROUP BY dateEpochDay
        ORDER BY dateEpochDay ASC
        """
    )
    fun observeDailyExpenseSummary(jobId: Long): Flow<List<DailyExpenseSummary>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE jobId = :jobId")
    fun observeTotalExpenses(jobId: Long): Flow<Double>

    @Insert
    suspend fun insertCredit(credit: CreditEntity): Long

    @Update
    suspend fun updateCredit(credit: CreditEntity)

    @Delete
    suspend fun deleteCredit(credit: CreditEntity)

    @Query("SELECT * FROM credits WHERE jobId = :jobId ORDER BY id DESC")
    fun observeCredits(jobId: Long): Flow<List<CreditEntity>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM credits WHERE jobId = :jobId")
    fun observeTotalCredits(jobId: Long): Flow<Double>
}
